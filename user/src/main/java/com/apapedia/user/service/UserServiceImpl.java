package com.apapedia.user.service;

import java.time.LocalDateTime;
import java.util.Optional;

import  org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import  org.springframework.stereotype.Service;

import com.apapedia.user.dto.request.AuthenticationRequest;
import com.apapedia.user.dto.request.CreateUserRequestDTO;
import com.apapedia.user.dto.request.LoginJwtRequestDTO;
import com.apapedia.user.dto.request.UpdateBalance;
import com.apapedia.user.dto.request.UpdateUserRequestDTO;
import com.apapedia.user.model.*;
import com.apapedia.user.repository.*;
import com.apapedia.user.security.jwt.JwtUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.UUID;



@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDb userDb;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RoleService roleService;

    @Override
    public UserModel addUser(UserModel user, CreateUserRequestDTO createUserRequestDTO) {
        user.setRole(roleService.getRoleByRoleName(createUserRequestDTO.getRole()));
        String hashedPass = encrypt(user.getPassword());
        user.setPassword(hashedPass);
        return userDb.save(user);
    }

    @Override
    public String encrypt(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    @Override
    public void updateUser(HttpServletRequest request, UpdateUserRequestDTO newUser){
        // String jwt = getJwtFromCookies(request);
        // String username = jwtUtil.extractUsername(jwt);

        // UserModel oldUser = findUserByUsername(username);
        // newUser.setId(oldUser.getId());
        // newUser.setBalance(oldUser.getBalance());
        // newUser.setRole(oldUser.getRole());
        // newUser.setCreatedAt(oldUser.getCreatedAt());
        // newUser.setUpdatedAt(LocalDateTime.now());
    }

    // @Override 
    // public String checkUsernameEmailPassword(HttpServletRequest request, UpdateUserRequestDTO newUser){
    //     String jwt = getJwtFromCookies(request);
    //     String oldId = jwtUtil.extractId(jwt);
    //     User oldUser = findUserById(oldId);

    //     String oldPassword = encrypt(oldUser.getPassword());
    //     String newPassword = encrypt(newUser.getPassword());
    //     BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    //     for (User user : userDb.findAll()) {
    //         String username = user.getUsername();
    //         String email = user.getEmail();
    //         String password = encrypt(user.getPassword());
    //         if (username.equals(newUser.getUsername()) && !username.equals(oldUser.getUsername())) return "duplicateUsername";

    //         if (email.equals(newUser.getEmail()) && !email.equals(oldUser.getEmail())) return "duplicateEmail";

    //         if (passwordEncoder.matches(password, newPassword) && !passwordEncoder.matches(password, oldPassword)) return "duplicatePassword";
    //     }

    //     return "Y";

    // }

    @Override
    public UserModel updateBalance(HttpServletRequest request, UpdateBalance newBalance){
        // String jwt = getJwtFromCookies(request);
        // String username = jwtUtil.extractUsername(jwt);
        
        // User user = findUserByUsername(username);
        // user.setBalance(user.getBalance() + newBalance.getBalance());
        // user.setUpdatedAt(LocalDateTime.now());
        // saveUser(user);

        return null;
    }


    @Override
    public void deleteUser(String idString){
        // UserModel user = findUserById(idString);
        // user.setDeleted(true);
        // saveUser(user);
    }

    @Override
    public UserModel saveUser(UserModel user){
        return userDb.save(user);
    }

    @Override
    public UserModel findUserByUsername(String username){
        return userDb.findByUsername(username);
    }

    @Override
    public UserModel findUserById(String idString){
        UUID id = UUID.fromString(idString);
        Optional<UserModel> userOptional = userDb.findById(id);
        if (userOptional.isPresent()) {
            UserModel user = userOptional.get();
            return user;
        } else {
            
        }
        return null;
    }
    

    // @Override
    // public User authenticate(String username, String password) {
    //     // Attempt to retrieve user by username
    //     User userOptional = userDb.findByUsername(username);
    //     BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    //     if (userOptional != null) {
    //         User user = userOptional;
    //         // Check if the provided password matches the stored password
    //         if (passwordEncoder.matches(password, user.getPassword())) {
    //             // Return user if passwords match
    //             return user;
    //         }
    //     }
    //     // Return null if authentication fails
    //     return null;
    // }

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
        // String jwt = getJwtFromCookies(request);
        // if (jwt != null && !jwt.isEmpty()) {
        //     return jwtUtil.extractUsername(jwt);
        // }
        return null;
    }

    @Override
    public boolean isLoggedIn(HttpServletRequest request) {
        String jwt = getJwtFromCookies(request);
        if (jwt != null && !jwt.isEmpty()) {
            try {
                String id = jwtUtils.getIdFromJwtToken(jwt);
                UserModel user = findUserById(id);
                return jwtUtils.validateToken(jwt, user);
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public String login(AuthenticationRequest authenticationRequest) {
        
        UserModel user = userDb.findByUsername(authenticationRequest.getUsername());

        if (user == null) return null;
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        
        if (!passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword())) return null;
        
        return jwtUtils.generateJwtToken(user);
    }

    @Override
    public String loginSeller(LoginJwtRequestDTO loginJwtRequestDTO) {
        String username = loginJwtRequestDTO.getUsername();
        String name = loginJwtRequestDTO.getName();

        UserModel user = userDb.findByUsername(username);

        if (user == null) {
           return null;
        }

        return jwtUtils.generateJwtToken(user);
    }

    @Override 
    public String checkUsernameEmailPassword(HttpServletRequest request, UpdateUserRequestDTO newUser){
        String jwt = getJwtFromCookies(request);
        String oldId = jwtUtils.getIdFromJwtToken(jwt);
        UserModel oldUser = findUserById(oldId);

        String oldPassword = encrypt(oldUser.getPassword());
        String newPassword = encrypt(newUser.getPassword());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        for (UserModel user : userDb.findAll()) {
            String username = user.getUsername();
            String email = user.getEmail();
            String password = encrypt(user.getPassword());
            if (username.equals(newUser.getUsername()) && !username.equals(oldUser.getUsername())) return "duplicateUsername";

            if (email.equals(newUser.getEmail()) && !email.equals(oldUser.getEmail())) return "duplicateEmail";

            if (passwordEncoder.matches(password, newPassword) && !passwordEncoder.matches(password, oldPassword)) return "duplicatePassword";
        }

        return "Y";


    }
    @Override
    public String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        
        // Periksa apakah header Authorization ada dan mulai dengan "Bearer "
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            // Ekstrak token JWT tanpa bagian "Bearer "
            return bearerToken.substring(7);
        }
        
        return null;
    }
    



}
