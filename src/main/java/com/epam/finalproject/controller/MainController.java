package com.epam.finalproject.controller;


import com.epam.finalproject.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/")
public class MainController {

    @GetMapping
    public String index(@AuthenticationPrincipal User user, Model model){
        model.addAttribute("user",user);
        return "index";
    }
    @GetMapping("/login")
    public String login(@AuthenticationPrincipal User user,  @RequestParam(value = "error", required = false) String error, Model model){
        model.addAttribute("user",user);
        if(error!=null)
            model.addAttribute("error","true");
        return "login";
    }

    @GetMapping("/admin")
    String admin(@AuthenticationPrincipal User user, Model model){
        model.addAttribute("user",user);
        model.addAttribute("role",user.getUserRole().toString());
        return "admin";
    }


    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }

}
