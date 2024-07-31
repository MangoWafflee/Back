package com.example.MangoWafflee.Global.Config.JWT;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @PostConstruct
    public void logKakaoOAuthSettings() {
        logger.info("JWT 설정 값 - secret : {}, expiration : {}", secret, expiration);
    }

    private Key key;

    private Map<String, String> activeTokens = new ConcurrentHashMap<>();
    private Set<String> invalidTokens = ConcurrentHashMap.newKeySet();

    @PostConstruct
    public void init() {
        byte[] keyBytes = secret.getBytes();
        if (keyBytes.length < 64) {
            throw new IllegalArgumentException("경고 : 비밀 키의 길이는 64자 이상으로 설정할 것");
        }
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String uid) {
        invalidateToken(uid);
        Map<String, Object> claims = new HashMap<>();
        String token = doGenerateToken(claims, uid);
        activeTokens.put(uid, token);
        return token;
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUidFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims != null ? claims.getSubject() : null;
    }

    private Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            logger.error("토큰의 유효기간이 지나 만료되었습니다. 다시 로그인 해주세요");
            throw e;
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("토큰이 유효하지 않습니다");
            throw e;
        }
    }

    public Boolean validateToken(String token) {
        try {
            logger.info("토큰 유효성 및 만료여부를 체크합니다");
            final String userUid = getUidFromToken(token);
            if (invalidTokens.contains(token)) {
                logger.error("토큰이 무효화되었습니다.");
                return false;
            }
            return (userUid != null && !isTokenExpired(token) && token.equals(activeTokens.get(userUid)));
        } catch (ExpiredJwtException e) {
            logger.error("토큰의 유효기간이 지나 만료되었습니다. 다시 로그인 해주세요");
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("토큰이 유효하지 않습니다");
            return false;
        }
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims != null ? claims.getExpiration() : null;
    }

    public void invalidateToken(String uid) {
        String token = activeTokens.remove(uid);
        if (token != null) {
            invalidTokens.add(token);
        }
    }

    public void refreshToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        if (claims == null || isTokenExpired(token) || invalidTokens.contains(token)) {
            logger.error("토큰이 유효하지 않거나 만료되었습니다");
            throw new IllegalArgumentException("유효하지 않거나 만료된 토큰");
        }
        claims.setExpiration(new Date(System.currentTimeMillis() + expiration));
        String uid = claims.getSubject();
        String refreshedToken = Jwts.builder()
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
        activeTokens.put(uid, refreshedToken);
        logger.info("토큰의 기간이 연장되었습니다");
    }

    public Long getTokenRemainingTime(String token) {
        Claims claims = getAllClaimsFromToken(token);
        if (claims == null) {
            logger.error("존재하지 않은 토큰입니다");
            throw new IllegalArgumentException("잘못된 토큰입니다");
        }
        if (isTokenExpired(token) || invalidTokens.contains(token)) {
            logger.error("토큰의 유효기간이 지나 만료되었습니다. 재로그인 후 다시 재발급 해주세요");
            throw new IllegalArgumentException("유효기간이 만료된 토큰입니다");
        }
        Date expirationDate = claims.getExpiration();
        if (expirationDate != null) {
            return (expirationDate.getTime() - System.currentTimeMillis()) / 1000;
        }
        return null;
    }

    public String getActiveToken(String uid) {
        return activeTokens.get(uid);
    }

    public boolean isTokenInvalid(String token) {
        return invalidTokens.contains(token);
    }
}
