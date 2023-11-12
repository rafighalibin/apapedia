package com.apapedia.user.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.apapedia.user.auth.JwtUtil;
import com.apapedia.user.dto.UserMapper;
import com.apapedia.user.dto.request.AuthenticationRequest;
import com.apapedia.user.dto.request.CreateUserRequestDTO;
import com.apapedia.user.dto.request.DeleteUserRequestDTO;
import com.apapedia.user.dto.request.UpdateBalance;
import com.apapedia.user.dto.request.UpdateUserRequestDTO;
import com.apapedia.user.model.User;
import com.apapedia.user.repository.UserDb;
import com.apapedia.user.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserRestController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createJWT(@RequestBody AuthenticationRequest authenticationRequest,
            HttpServletResponse response) {
        try {
            User user = userService.authenticate(authenticationRequest.getUsername(),
                    authenticationRequest.getPassword());
            if (user != null) {

                jwtUtil.createCookie(user, response);

                return ResponseEntity.ok("logged in username : " + authenticationRequest.getUsername());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username or password");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An internal error occurred");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response, HttpServletRequest request) {

        if (!userService.isLoggedIn(request)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You already logged out");

        jwtUtil.invalidateToken(response);

        return ResponseEntity.ok("User has been logged out successfully.");
    }

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") String id, HttpServletRequest request) {
        if (!userService.isLoggedIn(request))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to get user by id");

        User user = userService.findUserById(id);

        return ResponseEntity.ok(user);

    }

    @PostMapping(value = "/create")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequestDTO userDTO, BindingResult bindingResult,
            HttpServletRequest request) {

        if (userService.isLoggedIn(request))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged out to create a user.");

        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Request body has invalid type or missing field");
        } else {
            var user = userMapper.createUserRequestDTOToUser(userDTO);

            userService.addUser(user);

            return ResponseEntity.ok(user);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateUserRequestDTO userDTO, HttpServletRequest request, HttpServletResponse response) {

        if (!userService.isLoggedIn(request))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to update a user.");

        if (userService.checkUsernameEmailPassword(request, userDTO).equals("duplicateUsername")) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("duplicate username");

        if (userService.checkUsernameEmailPassword(request, userDTO).equals("duplicateEmail")) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("duplicate email");

        // if (userService.checkUsernameEmailPassword(request, userDTO).equals("duplicatePassword")) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("duplicate password");
        
        // Service method to update user details
        userService.updateUser(request, userDTO);

        // Convert DTO to User and save
        User updatedUser = userMapper.updateUserRequestDTOToUser(userDTO);

        userService.saveUser(updatedUser);

        jwtUtil.invalidateToken(response);
        jwtUtil.createCookie(updatedUser, response);

        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/delete")
    public ResponseEntity<?> deleteUser(@Valid @RequestBody DeleteUserRequestDTO userDTO, HttpServletRequest request) {

        if (!userService.isLoggedIn(request))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to delete a user.");

        userService.deleteUser(userDTO.getId());

        return ResponseEntity.ok("User Deleted");
    }

    @PutMapping("/update-balance")
    public ResponseEntity<?> updateBalance(@Valid @RequestBody UpdateBalance updateBalance,
            HttpServletRequest request) {

        if (!userService.isLoggedIn(request))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to update a balance");

        User user = userService.updateBalance(request, updateBalance);

        return ResponseEntity.ok(user);
    }

}
