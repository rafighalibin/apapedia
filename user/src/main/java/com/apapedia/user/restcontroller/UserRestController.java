package com.apapedia.user.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.apapedia.user.dto.UserMapper;
import com.apapedia.user.dto.request.AuthenticationRequest;
import com.apapedia.user.dto.request.CreateUserRequestDTO;
import com.apapedia.user.dto.request.DeleteUserRequestDTO;
import com.apapedia.user.dto.request.UpdateBalance;
import com.apapedia.user.dto.request.UpdateUserRequestDTO;
import com.apapedia.user.dto.response.CreateUserResponseDTO;
import com.apapedia.user.model.UserModel;
import com.apapedia.user.repository.UserDb;
import com.apapedia.user.security.jwt.JwtUtils;
import com.apapedia.user.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserRestController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") String id, HttpServletRequest request) {
        if (!userService.isLoggedIn(request))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to get user by id");

        UserModel user = userService.findUserById(id);

        return ResponseEntity.ok(user);

    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> addUser(@RequestBody CreateUserRequestDTO createUserRequestDTO) {
            UserModel userModel = userMapper.createUserRequestDTOToUserModel(createUserRequestDTO);
            userModel = userService.addUser(userModel, createUserRequestDTO);

            CreateUserResponseDTO createUserResponseDTO = userMapper.createUserResponseDTOToUserModel(userModel);
            createUserResponseDTO.setRole(userModel.getRole().getRole());
            return new ResponseEntity<>(createUserResponseDTO, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateUserRequestDTO userDTO, HttpServletRequest request,
            HttpServletResponse response) {

        if (!userService.isLoggedIn(request))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to update a user.");

        if (userService.checkUsernameEmailPassword(request, userDTO).equals("duplicateUsername"))
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("duplicate username");

        if (userService.checkUsernameEmailPassword(request, userDTO).equals("duplicateEmail"))
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("duplicate email");

        if (userService.checkUsernameEmailPassword(request,
        userDTO).equals("duplicatePassword")) return
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("duplicate password");

        // Service method to update user details
        userService.updateUser(request, userDTO);

        // Convert DTO to User and save
        UserModel updatedUser = userMapper.updateUserRequestDTOToUser(userDTO);

        userService.saveUser(updatedUser);

        jwtUtils.invalidateToken(response);
        jwtUtils.createCookie(updatedUser, response);

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

        UserModel user = userService.updateBalance(request, updateBalance);

        return ResponseEntity.ok(user);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getUser(HttpServletRequest request) {

        if (!userService.isLoggedIn(request))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to get User");

        UserModel user = userService.findUserByUsername(jwtUtils.getUserNameFromJwtToken(userService.getJwtFromCookies(request)));

        return ResponseEntity.ok(user);
    }

}
