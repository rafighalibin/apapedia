package com.apapedia.frontend.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.apapedia.frontend.DTO.response.ReadUserResponseDTO;
import com.apapedia.frontend.DTO.response.UpdateUserResponseDTO;
import com.apapedia.frontend.service.UserService;
import com.apapedia.frontend.DTO.request.CreateUserRequestDTO;
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

    @GetMapping("/register")
    public String register(Model model) {
        CreateUserRequestDTO createUserDTO = new CreateUserRequestDTO();

        model.addAttribute("createUserDTO", createUserDTO);
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute CreateUserRequestDTO createUserDTO)
            throws IOException, InterruptedException {
        createUserDTO.setPassword("ariefthegoat");
        createUserDTO.setRole("Seller");
        createUserDTO.setEmail(createUserDTO.getUsername() + "@ui.ac.id");
        userService.registerUser(createUserDTO);

        return "redirect:/home";
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
    public String editProfile(@ModelAttribute UpdateUserResponseDTO updateUserResponseDTO,
            HttpServletRequest request, Model model)
            throws IOException, InterruptedException {

        // TODO: Fix password and confirm password
        // TODO: Fix Response
        // if (updateUserResponseDTO.getPassword() !=
        // updateUserResponseDTO.getConfirmPassword()) {
        // model.addAttribute("message", "Password dan Confirm Password tidak sama");
        // return "form-update-profile";

        // }
        String res = userService.updateUser(updateUserResponseDTO, request);

        System.out.println(res);
        if (res == "success") {
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

    @PostMapping(value = "/topup", params = { "addBalance" })
    public String addBalance(Model model, HttpServletRequest request, HttpServletResponse response)
            throws IOException, InterruptedException {
        try {
            long amount = Integer.parseInt(request.getParameter("topupAmount"));
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
            long amount = Integer.parseInt(request.getParameter("topupAmount"));
            userService.withdrawBalance(request, amount);
        } catch (Exception e) {
            model.addAttribute("error", "Invalid amount");
            return "redirect:/topup";
        }
        return "redirect:/profile";
    }



}
