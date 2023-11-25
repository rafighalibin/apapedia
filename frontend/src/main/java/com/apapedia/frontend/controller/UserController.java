package com.apapedia.frontend.controller;

import java.io.IOException;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.apapedia.frontend.DTO.response.ReadUserResponseDTO;
import com.apapedia.frontend.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;

@Controller
public class UserController {

    @Autowired
    UserService userService;

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

    // TODO: change this to POST
    @GetMapping("/login")
    public String login(HttpServletResponse request) throws IOException, InterruptedException {

        String username = "arief0";
        String password = "ariefthegoat";

        HttpResponse<String> response = userService.login(username, password);

        java.net.http.HttpHeaders headers = response.headers();

        Cookie cookie = new Cookie("jwt",
                headers.map().get("Set-Cookie").get(0).toString().split(";")[0].split("=")[1]);
        request.addCookie(cookie);

        // TODO: redirect to desired page
        return "order-history";
    }

}
