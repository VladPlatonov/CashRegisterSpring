package com.epam.finalproject.controller;


import com.epam.finalproject.model.Product;
import com.epam.finalproject.model.User;
import com.epam.finalproject.service.ProductService;
import com.epam.finalproject.util.ProductForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class ProductController {

    private final ProductService productService;

    ProductController(ProductService productService){
        this.productService = productService;
    }

    private List<Integer> pagination(Integer pages){
        return IntStream.rangeClosed(1, pages).boxed() .collect(Collectors.toList());
    }

    private void parseModel(Model model, Optional<Integer> page){
        int currentPage = page.orElse(0);
        Page<Product> pages = productService.findAll(PageRequest.of(currentPage,5));
        model.addAttribute("page",pages);
        if (pages.getTotalPages() > 0)
            model.addAttribute("pageNumbers", pagination(pages.getTotalPages()));
    }

    @ModelAttribute("form")
    public ProductForm registrationForm() {
        return new ProductForm();
    }

    @ModelAttribute("product")
    public Product product() {
        return new Product();
    }

    @RequestMapping(value = "/products",method = RequestMethod.GET)
    String products(@AuthenticationPrincipal User user, Model model,
                    @RequestParam("page") Optional<Integer> page){
        model.addAttribute("user",user);
        parseModel(model,page);
        return "products";
    }

    @RequestMapping(value = "/products",method = RequestMethod.POST, params = "action=delete")
    public String delete(@RequestParam("id") Long id, Model model,
                         @RequestParam("page") Optional<Integer> page){
        productService. deleteById(id);
        parseModel(model,page);
        return "products";
    }

    @RequestMapping(value = "/addProduct",method = RequestMethod.GET)
    String addProducts(@AuthenticationPrincipal User user, Model model ){
        model.addAttribute("user",user);
        return "addProduct";
    }
    @RequestMapping(value = "/addProduct",method = RequestMethod.POST)
    String processProducts(@AuthenticationPrincipal User user,@ModelAttribute("form") @Valid ProductForm form, BindingResult result,Model model){
        if(result.hasErrors()) {
            model.addAttribute("user",user);
            return "addProduct";
        }
        if(productService.isValidCode(form.getCode())){
            model.addAttribute("user",user);
            model.addAttribute("error","true");
            return "addProduct";
        }

        productService.save(form.toProduct());
        return "redirect:/products";
    }

    @RequestMapping(value = "/editProduct",method = RequestMethod.GET)
    String redirectProduct(@RequestParam(value = "id",required = false) Long id,Model model){
        if(id!=null) {
            model.addAttribute("product", productService.findById(id));
            return "editProduct";
        }
        return "redirect:/products";
    }

    @RequestMapping(value = "/editProduct",method = RequestMethod.POST,params = "action=save")
    String saveProduct(@AuthenticationPrincipal User user,@ModelAttribute("product") @Valid Product product,BindingResult result,Model model){
        if(result.hasErrors()) {
            model.addAttribute("user",user);
            model.addAttribute("id",product.getProductId());
            return "editProduct";
        }
        productService.save(product);
        return "redirect:/products";
    }

    @RequestMapping(value = "/editProduct",method = RequestMethod.POST )
    String editProducts(@RequestParam("id") Long id,Model model){
        model.addAttribute("product",productService.findById(id));
        return "editProduct";
    }
}
