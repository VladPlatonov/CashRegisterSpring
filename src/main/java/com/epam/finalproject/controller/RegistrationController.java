package com.epam.finalproject.controller;


import com.epam.finalproject.model.User;
import com.epam.finalproject.service.UserService;
import com.epam.finalproject.util.RegistrationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
public class RegistrationController {



    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    RegistrationController(PasswordEncoder passwordEncoder, UserService userService){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String registration(@AuthenticationPrincipal User user, Model model){
        model.addAttribute("user",user);
        return "registration";
    }

    @ModelAttribute("form")
    public RegistrationForm registrationForm() {
        return new RegistrationForm();
    }

    @PostMapping
    public  String processUser(@ModelAttribute("form")  RegistrationForm form,Model model){
            if(userService.isValidLogin(form.getUsername())) {
                model.addAttribute("error","true");
                return "registration";
            }
            userService.save(form.toUser(passwordEncoder));
            return "redirect:/login";
        }
    }

