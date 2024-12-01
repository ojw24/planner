package com.ojw.planner.config;

import com.ojw.planner.core.enumeration.inner.JWTType;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.crypto.spec.SecretKeySpec;
import java.io.FileReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

@Slf4j
@Configuration
public class InitConfig {

    @Value("${jwt.access-key}")
    private String accessKey;

    @Value("${jwt.refresh-private-key}")
    private String refreshPrivateKey;

    @Value("${jwt.access-expire}")
    private Long accessExpire;

    @Value("${jwt.refresh-expire}")
    private Long refreshExpire;

    @PostConstruct
    public void init() {
        initJWTType();
    }

    public void initJWTType() {
        JWTType.ACCESS.setKey(new SecretKeySpec(accessKey.getBytes(), "HmacSHA256"));
        JWTType.ACCESS.setExpire(accessExpire);
        JWTType.REFRESH.setKey(loadPrivateKey());
        JWTType.REFRESH.setExpire(refreshExpire);
    }

    public PrivateKey loadPrivateKey() {

        try(PemReader reader = new PemReader(new FileReader(refreshPrivateKey))) {

            byte[] bytes = reader.readPemObject().getContent();

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
            return KeyFactory.getInstance("RSA").generatePrivate(keySpec);

        } catch (Exception e) {
            log.error("error while rsa key loading");
        }

        return null;

    }

}
