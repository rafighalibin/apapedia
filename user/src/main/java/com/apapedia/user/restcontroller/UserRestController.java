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
    private JwtUtil jwtTokenUtil;

    @Autowired
    private UserDb userDb;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createJwtToken(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) {
        try {
            User user = userService.authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
            if (user != null) {
                final String jwt = jwtTokenUtil.generateToken(user);

                // Set the JWT in a cookie
                Cookie jwtCookie = new Cookie("jwt", jwt);
                jwtCookie.setHttpOnly(true);
                jwtCookie.setPath("/");
                // Uncomment the line below if you're using HTTPS
                // jwtCookie.setSecure(true);
                
                // Add the cookie to the response
                response.addCookie(jwtCookie);

                // Return the JWT in the response body as well
                return ResponseEntity.ok("logged in username : " + authenticationRequest.getUsername());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username or password");
            }
        } catch (Exception e) {
            // Log the exception or handle it according to your application's error handling policy
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An internal error occurred");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // Invalidate the JWT cookie
        Cookie jwtCookie = new Cookie("jwt", null); 
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0); // set its age to 0 to expire it immediately
        response.addCookie(jwtCookie);

        return ResponseEntity.ok("User has been logged out successfully.");
    }

    @GetMapping(value="/get/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") String id, HttpServletRequest request) {
        if (userService.isLoggedIn(request)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to get user by id");

        User user = userService.findUserById(id);

        return ResponseEntity.ok(user);

    }

    @PostMapping(value = "/create")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequestDTO userDTO, BindingResult bindingResult, HttpServletRequest request) {

        if (userService.isLoggedIn(request)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged out to create a user.");
        

        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Request body has invalid type or missing field"
                );
        } else {
            var user = userMapper.createUserRequestDTOToUser(userDTO);

            userService.addUser(user);

            return ResponseEntity.ok(user);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateUserRequestDTO userDTO, HttpServletRequest request) {

        if (!userService.isLoggedIn(request)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to update a user.");
        
        // Service method to update user details
        userService.updateUser(userDTO);

        // Convert DTO to User and save
        User updatedUser = userMapper.updateUserRequestDTOToUser(userDTO);

        userService.saveUser(updatedUser);

        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/delete")
    public ResponseEntity<?> deleteUser(@Valid @RequestBody DeleteUserRequestDTO userDTO, HttpServletRequest request) {

        if (!userService.isLoggedIn(request)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to delete a user.");
        
        userService.deleteUser(userDTO.getId());

        return ResponseEntity.ok("User Deleted");
    }




}
