package com.apapedia.frontend.controller;

import java.io.IOException;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.apapedia.frontend.DTO.request.AuthenticationRequest;
import com.apapedia.frontend.DTO.response.ReadUserResponseDTO;
import com.apapedia.frontend.DTO.response.UpdateUserResponseDTO;
import com.apapedia.frontend.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/home")
    public String homePage() {
        return "home";
    }

    @GetMapping("logout")
    public String logout(HttpServletRequest request) throws IOException, InterruptedException {
        userService.logout(request);
        return "redirect:/login-page";
    }

    @GetMapping("/login-page")
    public String loginPage(Model model, @ModelAttribute AuthenticationRequest authenticationRequest) {
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

    @GetMapping("/profile/edit")
    public String editProfilePage(Model model, HttpServletRequest request) throws IOException, InterruptedException {
        ReadUserResponseDTO user = userService.getUser(request);
        UpdateUserResponseDTO updateUserResponseDTO = new UpdateUserResponseDTO();
        updateUserResponseDTO.setId(user.getId());
        updateUserResponseDTO.setName(user.getName());
        updateUserResponseDTO.setUsername(user.getUsername());
        updateUserResponseDTO.setEmail(user.getEmail());
        updateUserResponseDTO.setAddress(user.getAddress());
        updateUserResponseDTO.setBalance(user.getBalance());
        updateUserResponseDTO.setCategory(user.getCategory());

        model.addAttribute("user", updateUserResponseDTO);
        model.addAttribute("navbarActive", "Profile");

        return "form-update-profile";
    }

    @PostMapping("/profile/edit")
    public String editProfile(@ModelAttribute UpdateUserResponseDTO updateUserResponseDTO, HttpServletRequest request)
            throws IOException, InterruptedException {

        JsonNode res = userService.updateUser(updateUserResponseDTO, request);

        if (res.get("status").asText().equals("success")) {
            return "redirect:/profile";
        }

        if (!res.get("status").asText().equals("success")) {
            // TODO: add error message
            return "redirect:/profile";

        }
        return "redirect:/profile";
    }

    @GetMapping("/topup")
    public String topupPage(Model model, HttpServletRequest request) throws IOException, InterruptedException {
        ReadUserResponseDTO user = userService.getUser(request);

        model.addAttribute("user", user);

        return "topup-view";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute AuthenticationRequest authenticationRequest, HttpServletResponse request)
            throws IOException, InterruptedException {

        HttpResponse<String> response = userService.login(authenticationRequest.getUsername(),
                authenticationRequest.getPassword());

        java.net.http.HttpHeaders headers = response.headers();

        Cookie cookie = new Cookie("jwt",
                headers.map().get("Set-Cookie").get(0).toString().split(";")[0].split("=")[1]);
        request.addCookie(cookie);

        return "redirect:/home";
    }

}
