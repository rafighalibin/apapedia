package com.apapedia.order.security.jwt;


import java.util.Date;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.HashMap;
import jakarta.servlet.http.Cookie;


@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    private String jwtSecret = "apapedia21";


    
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

        public String getRoleFromJwtToken(String token) {
        return Jwts.parser()
                   .setSigningKey(jwtSecret)
                   .parseClaimsJws(token)
                   .getBody()
                   .get("role", String.class); // Assuming the user ID is stored as a String
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

