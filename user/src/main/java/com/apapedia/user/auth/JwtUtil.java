package com.apapedia.user.auth;

import io.jsonwebtoken.*;
import java.util.Date;

import java.util.Map;
import java.util.HashMap;
import org.springframework.stereotype.Component;

import com.apapedia.user.model.User;


@Component
public class JwtUtil {

    private String secretKey = "apapedia21";

   // Generate token for user
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        
        // Add custom claims here
        claims.put("username", user.getUsername());
        claims.put("userId", user.getId());
        claims.put("role", user.getRole()); 

        return createToken(claims, user.getUsername());
}
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours expiration
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }


    // Validate token
    public Boolean validateToken(String token, User user) {
        final String username = extractUsername(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getExpiration();
        return expiration.before(new Date());
    }

    public String extractUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }
}
