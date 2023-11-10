package com.apapedia.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
public class ProfileController {

    @GetMapping("/profile/")
    public String profilePage(Model model) {

        model.addAttribute("id", "1234567890");
        model.addAttribute("name", "John Doe");
        model.addAttribute("username", "johndoe");
        model.addAttribute("email", "john@mail");
        model.addAttribute("address", "Jl. Jalan No. 1");
        model.addAttribute("saldo", 231231);

        return "profile-view";
    }

}
