package com.apapedia.user.service;

import com.apapedia.user.dto.request.*;
import com.apapedia.user.dto.response.CreateCartResponseDTO;
import com.apapedia.user.dto.response.UpdateUserBalanceResponse;
import com.apapedia.user.model.UserModel;
import com.apapedia.user.repository.UserDb;
import com.apapedia.user.security.jwt.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

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

    private final WebClient webClient;

    public UserServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://apap-141.cs.ui.ac.id")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public UserModel addUser(UserModel user, CreateUserRequestDTO createUserRequestDTO) {
        user.setRole(roleService.getRoleByRoleName(createUserRequestDTO.getRole()));
        String hashedPass = encrypt(user.getPassword());
        user.setPassword(hashedPass);
        user.setCreatedAt(LocalDateTime.now());

        return userDb.save(user);
    }

    @Override 
    public void updateUserDeleted(UserModel user, CreateUserRequestDTO createUserRequestDTO){
        user.setDeleted(false);
        user.setName(createUserRequestDTO.getName());
        user.setAddress(createUserRequestDTO.getAddress());
        user.setCategory(createUserRequestDTO.getCategory());
        saveUser(user);
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
    public String checkUsernameEmail(HttpServletRequest request,
            UpdateUserRequestDTO newUser) {
        String jwt = getJwtFromHeader(request);
        String oldId = jwtUtils.getIdFromJwtToken(jwt);
        UserModel oldUser = findUserById(oldId);

        for (UserModel user : userDb.findAll()) {
            String username = user.getUsername();
            String email = user.getEmail();
            if (username.equals(newUser.getUsername()) &&
                    !username.equals(oldUser.getUsername()))
                return "duplicateUsername";

            if (email.equals(newUser.getEmail()) && !email.equals(oldUser.getEmail()))
                return "duplicateEmail";
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
        UserModel user = findUserById(idString);
        user.setDeleted(true);
        saveUser(user);
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

        String jwt = jwtUtils.generateJwtToken(user);
        if (user.getRole().getRole().equals("Customer")) {
            CreateCartRequestDTO createCartRequestDTO = new CreateCartRequestDTO();
            createCartRequestDTO.setUserId(user.getId());
            createCartRequestDTO.setTotalPrice(0);
            var response = this.webClient
                    .post()
                    .uri("/api/cart/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                    .header("Cookie", "jwt=" + jwt)
                    .bodyValue(createCartRequestDTO)
                    .retrieve()
                    .bodyToMono(CreateCartResponseDTO.class)
                    .block();
        }
        return jwt;
    }

    @Override
    public String loginSeller(LoginJwtRequestDTO loginJwtRequestDTO) {
        String username = loginJwtRequestDTO.getUsername();

        UserModel user = userDb.findByUsername(username);

        if (user == null || user.isDeleted() == true) {
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
