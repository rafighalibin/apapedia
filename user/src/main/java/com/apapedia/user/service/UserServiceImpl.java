package com.apapedia.user.service;

import java.time.LocalDateTime;
import java.util.Optional;

import  org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import  org.springframework.stereotype.Service;

import com.apapedia.user.auth.JwtUtil;
import com.apapedia.user.dto.request.UpdateUserRequestDTO;
import com.apapedia.user.model.*;
import com.apapedia.user.repository.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;



@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDb userDb;

    @Autowired
    JwtUtil jwtUtil;

    @Override
    public User addUser(User user){
        String password = encrypt(user.getPassword());
        user.setPassword(password);
        user.setCreatedAt(LocalDateTime.now());
        return userDb.save(user);
    }

    @Override
    public void updateUser(UpdateUserRequestDTO newUser){
        User oldUser = findUserByUsername(newUser.getUsername());
        newUser.setId(oldUser.getId());
        newUser.setRole(oldUser.getRole());
        newUser.setCreatedAt(oldUser.getCreatedAt());
        newUser.setUpdatedAt(LocalDateTime.now());
    }

    @Override
    public void deleteUser(String idString){
        User user = findUserById(idString);
        user.setDeleted(true);
        saveUser(user);
    }


    @Override
    public User saveUser(User user){
        return userDb.save(user);
    }

    @Override
    public String encrypt(String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        return hashedPassword;
    }

    @Override
    public User findUserByUsername(String username){
        return userDb.findByUsername(username);
    }

    @Override
    public User findUserById(String idString){
        UUID id = UUID.fromString(idString);
        Optional<User> userOptional = userDb.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user;
        } else {
            // Handle the case where the user wasn't found
        }
        return null;
    }
    

    @Override
    public User authenticate(String username, String password) {
        // Attempt to retrieve user by username
        User userOptional = userDb.findByUsername(username);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (userOptional != null) {
            User user = userOptional;
            // Check if the provided password matches the stored password
            if (passwordEncoder.matches(password, user.getPassword())) {
                // Return user if passwords match
                return user;
            }
        }
        // Return null if authentication fails
        return null;
    }

    @Override
    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public String getUsernameFromJwtCookie(HttpServletRequest request) {
        String jwt = getJwtFromCookies(request);
        if (jwt != null && !jwt.isEmpty()) {
            return jwtUtil.extractUsername(jwt);
        }
        return null;
    }

    @Override
    public boolean isLoggedIn(HttpServletRequest request) {
        String jwt = getJwtFromCookies(request);
        if (jwt != null && !jwt.isEmpty()) {
            try {
                String username = jwtUtil.extractUsername(jwt);
                User user = findUserByUsername(username);
                return jwtUtil.validateToken(jwt, user); // Validates the token
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }


    
}
