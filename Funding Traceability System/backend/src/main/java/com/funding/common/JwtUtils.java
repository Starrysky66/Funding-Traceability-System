package com.funding.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;

/**
 * JWT 工具类
 * 通过 @Component + @Value 注入配置，存储为静态字段供静态方法调用
 */
@Component
public class JwtUtils {

    private static String secret;
    private static long expire;

    @Value("${jwt.secret}")
    public void setSecret(String secret) {
        JwtUtils.secret = secret;
    }

    @Value("${jwt.expire}")
    public void setExpire(long expire) {
        JwtUtils.expire = expire;
    }

    /**
     * 生成 JWT token（有效期24h）
     * claims: userId、username、role
     */
    public static String generate(Long userId, String username, String role) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expire);
        return Jwts.builder()
                .claim("userId", userId)
                .claim("username", username)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 解析 token，返回 Claims
     */
    public static Claims parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 从 token 中安全提取 userId（兼容 Integer/Long）
     */
    public static Long extractUserId(Claims claims) {
        Object obj = claims.get("userId");
        if (obj instanceof Number) {
            return ((Number) obj).longValue();
        }
        return null;
    }

    /**
     * 从 token 中提取 role
     */
    public static String extractRole(Claims claims) {
        return Objects.toString(claims.get("role"), null);
    }

    /**
     * 从 token 中提取 username
     */
    public static String extractUsername(Claims claims) {
        return Objects.toString(claims.get("username"), null);
    }
}
