package com.apapedia.user.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.apapedia.user.dto.request.AuthenticationRequest;
import com.apapedia.user.dto.request.LoginJwtRequestDTO;
import com.apapedia.user.dto.response.LoginJwtResponseDTO;
import com.apapedia.user.service.RoleService;
import com.apapedia.user.service.UserService;




@RestController
@RequestMapping("/api")
public class AuthRestController {
    
    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @PostMapping("/login/seller")
    public ResponseEntity<?> loginSeller(@RequestBody LoginJwtRequestDTO loginJwtRequestDTO){
        
            String jwtToken = userService.loginSeller(loginJwtRequestDTO);
            if (jwtToken == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username or password");
            }
            return new ResponseEntity<>(new LoginJwtResponseDTO(jwtToken), HttpStatus.OK);
         
    }

    @PostMapping("/login/customer")
    public ResponseEntity<?> loginCustomer(@RequestBody AuthenticationRequest authenticationRequest){
        try {
            String jwtToken = userService.login(authenticationRequest);
            if (jwtToken == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username or password");;
            return new ResponseEntity<>(new LoginJwtResponseDTO(jwtToken), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    

}
