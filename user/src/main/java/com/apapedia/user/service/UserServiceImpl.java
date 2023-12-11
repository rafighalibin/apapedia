package com.apapedia.user.service;

import com.apapedia.user.dto.request.*;
import com.apapedia.user.dto.response.UpdateUserBalanceResponse;
import com.apapedia.user.model.UserModel;
import com.apapedia.user.repository.UserDb;
import com.apapedia.user.security.jwt.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
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
    public UserModel updateUser(HttpServletRequest request, UpdateUserRequestDTO newUser) {
        String jwt = getJwtFromHeader(request);
        String username = jwtUtils.getUserNameFromJwtToken(jwt);

        UserModel oldUser = findUserByUsername(username);

        oldUser.setName(newUser.getName());
        oldUser.setUsername(newUser.getUsername());
        oldUser.setEmail(newUser.getEmail());
        oldUser.setAddress(newUser.getAddress());
        oldUser.setUpdatedAt(LocalDateTime.now());
        if (!newUser.getPassword().isEmpty()) {
            oldUser.setPassword(encrypt(newUser.getPassword()));
        }
        saveUser(oldUser);

        return oldUser;
    }

    @Override
    public String checkUsernameEmailPassword(HttpServletRequest request,
            UpdateUserRequestDTO newUser) {
        String jwt = getJwtFromHeader(request);
        String oldId = jwtUtils.getIdFromJwtToken(jwt);
        UserModel oldUser = findUserById(oldId);

        String oldPassword = encrypt(oldUser.getPassword());
        String newPassword = encrypt(newUser.getPassword());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        for (UserModel user : userDb.findAll()) {
            String username = user.getUsername();
            String email = user.getEmail();
            String password = encrypt(user.getPassword());
            if (username.equals(newUser.getUsername()) &&
                    !username.equals(oldUser.getUsername()))
                return "duplicateUsername";

            if (email.equals(newUser.getEmail()) && !email.equals(oldUser.getEmail()))
                return "duplicateEmail";

            if (passwordEncoder.matches(password, newPassword) &&
                    !passwordEncoder.matches(password, oldPassword))
                return "duplicatePassword";
        }

        return "Y";

    }

    @Override
    public UserModel updateBalance(HttpServletRequest request, UpdateBalance newBalance) {
        String jwt = getJwtFromHeader(request);
        String username = jwtUtils.getUserNameFromJwtToken(jwt);

        UserModel user = findUserByUsername(username);
        user.setBalance(newBalance.getBalance());
        user.setUpdatedAt(LocalDateTime.now());
        saveUser(user);

        return user;
    }

    @Override
    public UpdateUserBalanceResponse updateBalanceAfterTransaction(UpdateBalanceAfterOrder request) {
        UserModel seller = findUserById(String.valueOf(request.getSellerId()));

        Long sellerCurrentBalance = seller.getBalance();
        seller.setUpdatedAt(LocalDateTime.now());
        seller.setBalance(sellerCurrentBalance + request.getTotalPrice());

        saveUser(seller);

        UserModel buyer = findUserById(String.valueOf(request.getCustomerId()));

        Long buyerCurrentBalance = buyer.getBalance();
        buyer.setUpdatedAt(LocalDateTime.now());
        buyer.setBalance(buyerCurrentBalance + request.getTotalPrice());

        saveUser(buyer);

        UpdateUserBalanceResponse response = new UpdateUserBalanceResponse();
        response.setBuyer(buyer);
        response.setSeller(seller);

        return response;
    }

    @Override
    public void deleteUser(String idString) {
        // UserModel user = findUserById(idString);
        // user.setDeleted(true);
        // saveUser(user);
    }

    @Override
    public UserModel saveUser(UserModel user) {
        return userDb.save(user);
    }

    @Override
    public UserModel findUserByUsername(String username) {
        return userDb.findByUsername(username);
    }

    @Override
    public UserModel findUserById(String idString) {
        UUID id = UUID.fromString(idString);
        Optional<UserModel> userOptional = userDb.findById(id);
        return userOptional.orElse(null);
    }

    // @Override
    // public User authenticate(String username, String password) {
    // // Attempt to retrieve user by username
    // User userOptional = userDb.findByUsername(username);
    // BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    // if (userOptional != null) {
    // User user = userOptional;
    // // Check if the provided password matches the stored password
    // if (passwordEncoder.matches(password, user.getPassword())) {
    // // Return user if passwords match
    // return user;
    // }
    // }
    // // Return null if authentication fails
    // return null;
    // }

    @Override
    public String getJwtFromHeader(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            authorizationHeader = authorizationHeader.substring(7); // Remove 'Bearer ' prefix
        }

        if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
            return authorizationHeader;

        }
        return null;
    }

    @Override
    public String getUsernameFromJwtCookie(HttpServletRequest request) {
        // String jwt = getJwtFromHeader(request);
        // if (jwt != null && !jwt.isEmpty()) {
        // return jwtUtil.extractUsername(jwt);
        // }
        return null;
    }

    @Override
    public boolean isLoggedIn(HttpServletRequest request) {
        String jwt = getJwtFromHeader(request);
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

        if (user == null)
            return null;
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (!passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword()))
            return null;

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
