package com.disaster.safety.security.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.disaster.safety.member.dto.CustomUserInfoDto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
//import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

// JWT 생성 및 검증
@Slf4j
@Component
public class JwtUtil {
    // JWT 서명키
    private final Key key;
    private final long accessTokenExpTime;

    public JwtUtil(
        @Value("${jwt.secret}") final String secretKey,
        @Value("${jwt.expiration_time:3600}") final long accessTokenExpTime) 
    {
        // jwt.secret 값을 Base64로 디코딩하여 서명키 생성
        //byte[] keyBytes = Decoders.BASE64.decode(secretKey);

        // jwt.secret 값을 UTF-8로 인코딩하여 서명키 생성
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        // 디버깅을 위해 JWT SECRET과 길이 출력
        System.out.println("JWT SECRET = " + secretKey);
        System.out.println("JWT SECRET LENGTH = " + secretKey.length());
        
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenExpTime;

        
    }

    // Access Token 생성
    public String createAccessToken(CustomUserInfoDto member) {
        return createToken(member, accessTokenExpTime);
    }

    // JWT 생성
    private String createToken(CustomUserInfoDto member, long expireTime) {
        Claims claims = Jwts.claims();
        claims.put("memberId", member.getMemberId());
        claims.put("userId", member.getUserId());
        claims.put("password", member.getPassword());
        claims.put("userName", member.getUserName());

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(expireTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Token에서 UserID 추출
    public String getUserId(String token) {
        return parseClaims(token).get("userId", String.class);
    }

    // JWT 검증
    public boolean isValidToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty", e);
        }
        return false;
    }

    // JWT Claims 추출
    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();

        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
