package com.hana.ddok.common.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {
    private final SecretKey secretKey;

    // 생성자에서 secretkey 암호화(알고리즘: HS256)
    public JWTUtil(@Value("${jwt.secret}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    // 토큰 내용(Payload) 인증 메소드
    public String getPhoneNumber(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("phoneNumber",
                String.class);
    }

    // 토큰 내용(Payload) 인증 메소드
    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration()
                .before(new Date());
    }

    // JWT 생성 메소드
    public String createJwt(String phoneNumber, Long expiredMs) {

        return Jwts.builder()
                .claim("phoneNumber", phoneNumber)
                .issuedAt(new Date(System.currentTimeMillis())) // 생성 일자
                .expiration(new Date(System.currentTimeMillis() + expiredMs)) // 만료 일자
                .signWith(secretKey)
                .compact();
    }
}