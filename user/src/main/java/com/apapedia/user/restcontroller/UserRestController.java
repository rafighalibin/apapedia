package com.apapedia.user.restcontroller;

import com.apapedia.user.dto.UserMapper;
import com.apapedia.user.dto.request.*;
import com.apapedia.user.dto.response.CreateUserResponseDTO;
import com.apapedia.user.dto.response.UpdateUserBalanceResponse;
import com.apapedia.user.model.UserModel;
import com.apapedia.user.security.jwt.JwtUtils;
import com.apapedia.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> addUser(@RequestBody CreateUserRequestDTO createUserRequestDTO) {
        var user = userService.findUserByUsername(createUserRequestDTO.getUsername());
        if (user != null) {
            if (user.isDeleted() == true) {
                userService.updateUserDeleted(user, createUserRequestDTO);
                return ResponseEntity.ok(user);
            } else {
                return null;
            }
        } else {
            UserModel userModel = userMapper.createUserRequestDTOToUserModel(createUserRequestDTO);
            userModel = userService.addUser(userModel, createUserRequestDTO);

            CreateUserResponseDTO createUserResponseDTO = userMapper.createUserResponseDTOToUserModel(userModel);
            createUserResponseDTO.setRole(userModel.getRole().getRole());
            return new ResponseEntity<>(createUserResponseDTO, HttpStatus.OK);
        }

    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateUserRequestDTO userDTO, HttpServletRequest request,
            HttpServletResponse response) {

        if (!userService.isLoggedIn(request))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("You must be logged in to update a user.");

        String checkUsernameEmail = userService.checkUsernameEmail(request,
                userDTO);
        if (checkUsernameEmail.equals("duplicateUsername"))
            return ResponseEntity.ok("duplicate username");

        if (checkUsernameEmail.equals("duplicateEmail"))
            return ResponseEntity.ok("duplicate email");

        // Service method to update user details
        UserModel updatedUser = userService.updateUser(request, userDTO);

        jwtUtils.invalidateToken(response);
        jwtUtils.createCookie(updatedUser, response);

        return ResponseEntity.ok("Success");
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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must belogged in to update a balance");

        UserModel user = userService.updateBalance(request, updateBalance);

        return ResponseEntity.ok(user);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getUser(HttpServletRequest request) {

        if (!userService.isLoggedIn(request))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to get User");

        UserModel user = userService
                .findUserByUsername(jwtUtils.getUserNameFromJwtToken(userService.getJwtFromHeader(request)));

        return ResponseEntity.ok(user);
    }

    @PutMapping("/order/done/update-balance")
    public ResponseEntity<?> updateBalanceAfterTransaction(@Valid @RequestBody UpdateBalanceAfterOrder updateBalance,
            HttpServletRequest request) {
        if (!userService.isLoggedIn(request))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to get User");
        UpdateUserBalanceResponse user = userService.updateBalanceAfterTransaction(updateBalance);

        return ResponseEntity.ok(user);
    }
}
