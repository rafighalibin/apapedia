package com.apapedia.frontend.controller;

import java.io.IOException;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.apapedia.frontend.DTO.request.AuthenticationRequest;
import com.apapedia.frontend.DTO.response.ReadUserResponseDTO;
import com.apapedia.frontend.service.UserService;
import com.apapedia.frontend.DTO.request.CreateUserRequestDTO;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;


@Controller
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/home")
    public String homePage(){
        return "home";
    }

    @GetMapping("/register")
    public String register(Model model){
        CreateUserRequestDTO createUserDTO = new CreateUserRequestDTO();

        model.addAttribute("createUserDTO", createUserDTO);
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute CreateUserRequestDTO createUserDTO)  throws IOException, InterruptedException{
        createUserDTO.setPassword("ariefthegoat");
        createUserDTO.setRole("Seller");
        createUserDTO.setEmail(createUserDTO.getUsername() + "@ui.ac.id");
        userService.registerUser(createUserDTO);

        return "redirect:/home";
    }

    @GetMapping("logout")
    public String logout(HttpServletRequest request) throws IOException, InterruptedException{
        userService.logout(request);
        return "redirect:/login-page";
    }

    @GetMapping("/login-page")
    public String loginPage(Model model, @ModelAttribute AuthenticationRequest authenticationRequest){
        model.addAttribute("authenticationRequest", authenticationRequest);
        return "login";
    }

    @GetMapping("/profile")
    public String profilePage(Model model, HttpServletRequest request) throws IOException, InterruptedException {
        ReadUserResponseDTO user = userService.getUser(request);

        model.addAttribute("user", user);
        model.addAttribute("navbarActive", "Profile");

        return "profile-view";
    }

    @GetMapping("/topup")
    public String topupPage(Model model, HttpServletRequest request) throws IOException, InterruptedException {
        ReadUserResponseDTO user = userService.getUser(request);

        model.addAttribute("user", user);

        return "topup-view";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute AuthenticationRequest authenticationRequest, HttpServletResponse request) throws IOException, InterruptedException {

        HttpResponse<String> response = userService.login(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        java.net.http.HttpHeaders headers = response.headers();

        Cookie cookie = new Cookie("jwt",
                headers.map().get("Set-Cookie").get(0).toString().split(";")[0].split("=")[1]);
        request.addCookie(cookie);

        return "redirect:/home";
    }

}
