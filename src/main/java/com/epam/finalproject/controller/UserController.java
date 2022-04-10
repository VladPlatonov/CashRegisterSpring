package com.epam.finalproject.controller;

import com.epam.finalproject.model.Product;
import com.epam.finalproject.model.User;
import com.epam.finalproject.model.UserRole;
import com.epam.finalproject.service.ProductService;
import com.epam.finalproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    UserController(UserService userService){
        this.userService = userService;
    }

    private List<Integer> pagination(Integer pages){
        return IntStream.rangeClosed(1, pages).boxed() .collect(Collectors.toList());
    }

    private void parseModel(Model model, Optional<Integer> page){
        model.addAttribute("roles", UserRole.values());
        int currentPage = page.orElse(0);
        Page<User> pages = userService.findAll(PageRequest.of(currentPage,5));
        model.addAttribute("page",pages);
        if (pages.getTotalPages() > 0)
            model.addAttribute("pageNumbers", pagination(pages.getTotalPages()));
    }

    @GetMapping
    String users(@AuthenticationPrincipal User user, Model model,
                 @RequestParam("page") Optional<Integer> page){
        model.addAttribute("user",user);
        parseModel(model,page);
        return "users";
    }

    @PostMapping(params = "action=delete")
    public String delete(@RequestParam("id") Long id, Model model,
                         @RequestParam("page") Optional<Integer> page){
        userService.deleteById(id);
        parseModel(model,page);
        return "users";
    }
    @PostMapping(params = "action=update")
    public String edit(@RequestParam("id") Long id, @RequestParam("UserRole") UserRole role, Model model,
                       @RequestParam("page") Optional<Integer> page){
        User user = userService.findById(id);
        user.getUserRole().clear();
        user.getUserRole().add(role);
        userService.save(user);
        parseModel(model,page);
        return "users";
    }


}
