package com.example.kickboard.login.config.jwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtUtil {
    private String secretKey;

    public JwtUtil(String secretKey) {
        this.secretKey = secretKey;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true; // 유효한 경우
        } catch (SignatureException e) {
            // 서명 불일치
            log.debug("Invalid JWT signature: " + e.getMessage());
            return false;
        } catch (ExpiredJwtException e) {
            // 토큰 만료
            log.debug("JWT token is expired: " + e.getMessage());
            return false;
        } catch (Exception e) {
            // 기타 예외
            log.debug("Invalid JWT token: " + e.getMessage());
            return false;
        }
    }
}