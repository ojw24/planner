package com.ojw.planner.core.util;

import com.ojw.planner.app.system.user.domain.User;
import com.ojw.planner.core.enumeration.inner.JWTType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.FileReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

@Slf4j
@Component
public class JWTUtil {

    @Value("${jwt.refresh-public-key}")
    private String refreshPublicKey;

    public String createToken(User user, JWTType type) {
        return Jwts.builder()
                .claim("userId", user.getUserId())
                .claim("userNm", user.getName())
                .expiration(new Date(System.currentTimeMillis() + type.getExpire() * 1000))
                .signWith(type.getKey())
                .compact();
    }

    //토큰 파싱
    public Claims getSubject(String jwt, JWTType type) {
        return type.equals(JWTType.ACCESS)
                ? Jwts.parser()
                    .verifyWith((SecretKey) type.getKey())
                    .build()
                    .parseSignedClaims(jwt)
                    .getPayload()
                : Jwts.parser()
                    .verifyWith(loadPublicKey())
                    .build()
                    .parseSignedClaims(jwt)
                    .getPayload();
    }

    public PublicKey loadPublicKey() {

        try(PemReader reader = new PemReader(new FileReader(refreshPublicKey))) {

            byte[] bytes = reader.readPemObject().getContent();

            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);

            // KeyFactory를 사용하여 공개 키 객체를 생성
            return KeyFactory.getInstance("RSA").generatePublic(keySpec);

        } catch (Exception e) {
            log.error("error while rsa key loading");
        }

        return null;

    }

    //토큰 검증
    public boolean validateToken(String jwt, JWTType type) {

        boolean result = false;

        try {

            Claims claims = null;
            if(type.equals(JWTType.ACCESS)) {
                claims = Jwts.parser()
                        .decryptWith((SecretKey) type.getKey())
                        .build()
                        .parseSignedClaims(jwt)
                        .getPayload();
            } else {
                claims = Jwts.parser()
                        .decryptWith((PrivateKey) type.getKey())
                        .build()
                        .parseSignedClaims(jwt)
                        .getPayload();
            }

            result = !claims.getExpiration().before(new Date());

        } catch (ExpiredJwtException e) {
            throw e;
        } catch (Exception e) {
            return false;
        }

        return result;

    }

}
