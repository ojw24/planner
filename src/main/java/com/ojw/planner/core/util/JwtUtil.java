package com.ojw.planner.core.util;

import com.ojw.planner.app.system.user.domain.User;
import com.ojw.planner.core.enumeration.inner.JwtClaim;
import com.ojw.planner.core.enumeration.inner.JwtPrefix;
import com.ojw.planner.core.enumeration.inner.JwtType;
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
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.refresh-public-key}")
    private String refreshPublicKey;

    public String createToken(User user, JwtType type) {
        return Jwts.builder()
                .claim(JwtClaim.ID.getType(), user.getUserId())
                .claim(JwtClaim.NAME.getType(), user.getName())
                .claim(JwtClaim.UUID.getType(), user.getUuid())
                .claim("isAdmin", user.isAdmin())
                .expiration(new Date(System.currentTimeMillis() + type.getExpire()))
                .signWith(type.getKey())
                .compact();
    }

    //토큰 파싱
    public Claims getSubject(String jwt, JwtType type) {
        return type.equals(JwtType.ACCESS)
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
    public boolean validateToken(String jwt, JwtType type) {

        boolean result = false;

        try {

            Claims claims = null;
            if(type.equals(JwtType.ACCESS)) {
                claims = Jwts.parser()
                        .verifyWith((SecretKey) type.getKey())
                        .build()
                        .parseSignedClaims(jwt)
                        .getPayload();
            } else {
                claims = Jwts.parser()
                        .verifyWith(loadPublicKey())
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

    public static String removeType(String jwt) {
        if(jwt.contains(JwtPrefix.PREFIX.getType())) jwt = jwt.replaceFirst(JwtPrefix.PREFIX.getType(), "");
        return jwt;
    }

}
