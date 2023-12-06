package com.apapedia.frontend.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
            HttpServletRequest request, Model model, RedirectAttributes redirectAttributes)
            throws IOException, InterruptedException {

        ReadUserResponseDTO user = userService.getUser(request);
        UpdateUserResponseDTO oldUser = new UpdateUserResponseDTO();
        oldUser.setId(user.getId());
        oldUser.setName(user.getName());
        oldUser.setUsername(user.getUsername());
        oldUser.setEmail(user.getEmail());
        oldUser.setAddress(user.getAddress());
        oldUser.setBalance(user.getBalance());
        oldUser.setCategory(user.getCategory());
        model.addAttribute("user", oldUser);

        if (updateUserResponseDTO.getPassword() != updateUserResponseDTO.getConfirmPassword()) {
            model.addAttribute("message", "Password dan Confirm Password tidak sama");
            return "form-update-profile";
        }

        String res = userService.updateUser(updateUserResponseDTO, request);

        if (res.equals("duplicate username")) {
            model.addAttribute("message", "Username sudah digunakan");
            return "form-update-profile";
        }

        if (res.equals("duplicate email")) {
            model.addAttribute("message", "Email sudah digunakan");
            return "form-update-profile";
        }

        if (res.equals("duplicate password")) {
            model.addAttribute("message", "Password baru tidak boleh sama dengan sebelumnya");
            return "form-update-profile";
        }

        redirectAttributes.addFlashAttribute("message", "Berhasil update profile");
        return "redirect:/profile";
    }

    @GetMapping("/withdraw")
    public String topupPage(Model model, HttpServletRequest request) throws IOException, InterruptedException {
        ReadUserResponseDTO user = userService.getUser(request);

        model.addAttribute("user", user);

        return "withdraw-view";
    }

    @PostMapping(value = "/withdraw", params = { "withdrawBalance" })
    public String withdrawBalance(Model model, HttpServletRequest request, HttpServletResponse response,
            RedirectAttributes redirectAttributes)
            throws IOException, InterruptedException {
        try {
            long amount = Integer.parseInt(request.getParameter("withdrawAmount"));
            if (amount > userService.getUser(request).getBalance() || amount < 0) {
                throw new Exception();
            }
            userService.withdrawBalance(request, amount);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Saldo tidak mencukupi");
            return "redirect:/withdraw";
        }
        redirectAttributes.addFlashAttribute("message", "Berhasil withdraw saldo");

        return "redirect:/profile";
    }



}
