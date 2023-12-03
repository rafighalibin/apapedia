package com.apapedia.user.security.jwt;

import java.util.Date;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.apapedia.user.model.UserModel;
import java.util.Map;
import java.util.HashMap;
import jakarta.servlet.http.Cookie;


@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    private String jwtSecret = "apapedia21";

    private int jwtExpirationMs = 86400000;

    public String createToken(Map<String, Object> claims, String subject){
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)

            .setExpiration(new Date((new Date().getTime() + jwtExpirationMs)))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
    }

    public String generateJwtToken(UserModel user) {
        Map<String, Object> claims = new HashMap<>();
        
        // Add custom claims here
        claims.put("username", user.getUsername());
        claims.put("userId", user.getId());
        claims.put("role", user.getRole().getRole()); 

        return createToken(claims, user.getUsername());
    }
    

    

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }


    public String getIdFromJwtToken(String token) {
        return Jwts.parser()
                   .setSigningKey(jwtSecret)
                   .parseClaimsJws(token)
                   .getBody()
                   .get("userId", String.class); // Assuming the user ID is stored as a String
    }

    public void createCookie(UserModel user, HttpServletResponse response){
        final String jwt = generateJwtToken(user);

        // Set the JWT in a cookie
        Cookie jwtCookie = new Cookie("jwt", jwt);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        // Uncomment the line below if you're using HTTPS
        // jwtCookie.setSecure(true);

        // Add the cookie to the response
        response.addCookie(jwtCookie);

    }


    public void invalidateToken(HttpServletResponse response){
        // Invalidate the JWT cookie
        Cookie jwtCookie = new Cookie("jwt", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0); // set its age to 0 to expire it immediately
        response.addCookie(jwtCookie);
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e ) {
            logger.error("Invalid JWT Signature: {}", e.getMessage());

        } catch (MalformedJwtException e ) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        } 
        return false;
    }
}
