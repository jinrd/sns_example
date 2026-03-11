package com.example.sns.core.util;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    private final SecretKey secretKey;
    private final Long expiration;

    // application.properties 에서 값을 가져와서 초기화
    public JwtUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") Long expiration) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiration = expiration;
    }

    // JWT 토큰 생성 (로그인 성공 시 호출됨)
    public String createToken(String username) {
        return Jwts.builder()
                    .subject(username)
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(secretKey) // 비밀키로 서명 (위조 방지)
                    .compact();
    }

    // 토큰에서 사용자 아이디(username) 추출
    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    // 토큰이 만료되었느닞 확인
    public boolean isExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }


    // 내부 메서드 : 토큰을 파싱해서 Claims(데이터 조각들)을 가져옴
    private Claims getClaims(String token) {
        return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
    }
}
