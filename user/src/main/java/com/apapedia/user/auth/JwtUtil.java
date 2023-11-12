package com.apapedia.user.auth;

import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Date;

import java.util.Map;
import java.util.HashMap;
import org.springframework.stereotype.Component;

import com.apapedia.user.model.User;
import java.util.UUID;


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
        final String id = extractId(token);
        return (id.equals(user.getId().toString()) && !isTokenExpired(token));
    }
    

    public void invalidateToken(HttpServletResponse response){
        // Invalidate the JWT cookie
        Cookie jwtCookie = new Cookie("jwt", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0); // set its age to 0 to expire it immediately
        response.addCookie(jwtCookie);
    }

    public void createCookie(User user, HttpServletResponse response){
        final String jwt = generateToken(user);

        // Set the JWT in a cookie
        Cookie jwtCookie = new Cookie("jwt", jwt);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        // Uncomment the line below if you're using HTTPS
        // jwtCookie.setSecure(true);

        // Add the cookie to the response
        response.addCookie(jwtCookie);

    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getExpiration();
        return expiration.before(new Date());
    }

    public String extractUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

     public String extractId(String token) {
        return Jwts.parser()
                   .setSigningKey(secretKey)
                   .parseClaimsJws(token)
                   .getBody()
                   .get("userId", String.class); // Assuming the user ID is stored as a String
    }
}
