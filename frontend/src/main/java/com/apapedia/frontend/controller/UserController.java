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

    @PostMapping(value = "/topup", params = { "addBalance" })
    public String addBalance(Model model, HttpServletRequest request, HttpServletResponse response)
            throws IOException, InterruptedException {
        try {
            int amount = Integer.parseInt(request.getParameter("topupAmount"));
            userService.addBalance(request, amount);
        } catch (Exception e) {
            model.addAttribute("error", "Invalid amount");
            return "redirect:/topup";
        }
        return "redirect:/profile";
    }

    @PostMapping(value = "/topup", params = { "withdrawBalance" })
    public String withdrawBalance(Model model, HttpServletRequest request, HttpServletResponse response)
            throws IOException, InterruptedException {
        try {
            int amount = Integer.parseInt(request.getParameter("topupAmount"));
            userService.withdrawBalance(request, amount);
        } catch (Exception e) {
            model.addAttribute("error", "Invalid amount");
            return "redirect:/topup";
        }
        return "redirect:/profile";
    }

    // TODO: change this to POST
    @GetMapping("/login")
    public String login(HttpServletResponse request) throws IOException, InterruptedException {

        HttpResponse<String> response = userService.login(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        java.net.http.HttpHeaders headers = response.headers();

        Cookie cookie = new Cookie("jwt",
                headers.map().get("Set-Cookie").get(0).toString().split(";")[0].split("=")[1]);
        request.addCookie(cookie);

        return "redirect:/home";
    }

}
