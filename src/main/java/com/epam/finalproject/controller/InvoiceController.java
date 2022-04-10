package com.epam.finalproject.controller;

import com.epam.finalproject.model.*;
import com.epam.finalproject.service.InvoiceService;
import com.epam.finalproject.service.ProductService;
import com.epam.finalproject.util.CreateInvoiceForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final ProductService productService;

    InvoiceController(InvoiceService invoiceService,ProductService productService){
        this.invoiceService = invoiceService;
        this.productService = productService;
    }


    @ModelAttribute("form")
    public CreateInvoiceForm createInvoiceForm() {
        return new CreateInvoiceForm();
    }


    private List<Integer> pagination(Integer pages){
        return IntStream.rangeClosed(1, pages).boxed() .collect(Collectors.toList());
    }

    private void parseModel(Model model, Optional<Integer> page, boolean isInvoice){
        int currentPage = page.orElse(0);
        Page<?> pages = isInvoice? invoiceService.findAll(PageRequest.of(currentPage,5)):productService.findAll(PageRequest.of(currentPage,5));
        model.addAttribute("page",pages);
        if (pages.getTotalPages() > 0)
            model.addAttribute("pageNumbers", pagination(pages.getTotalPages()));
    }

    @RequestMapping(value = "/invoices",method = RequestMethod.GET)
    String invoices(@AuthenticationPrincipal User user, Model model,
                    @RequestParam("page") Optional<Integer> page){
        model.addAttribute("user",user);
        parseModel(model,page,true);
        return "invoices";
    }

    @RequestMapping(value = "/createInvoice",method = RequestMethod.GET)
    String createInvoices(@AuthenticationPrincipal User user,Model model,
                          @RequestParam("page") Optional<Integer> page){
        model.addAttribute("user",user);
        parseModel(model,page,false);
        return "createInvoice";
    }

    @RequestMapping(value = "/createInvoice",method = RequestMethod.POST)
    String createInvoice(@RequestParam("idQuantity") String []quantity ,@AuthenticationPrincipal User user){
        List<CreateInvoiceForm> products =new ArrayList<>();
        for(int i = 0 ;i<quantity.length-1;i+=2){
            if(Integer.parseInt(quantity[i+1])>0) {
                CreateInvoiceForm createInvoiceForm = new CreateInvoiceForm();
                createInvoiceForm.setCode(quantity[i]);
                createInvoiceForm.setQuantity(Integer.parseInt(quantity[i+1]));
                products.add(createInvoiceForm);
            }
        }
        if(!products.isEmpty())
            invoiceService.createInvoice(products,System.currentTimeMillis(),user);
        return "redirect:/invoices";
    }

    @RequestMapping(value = "/invoices",method = RequestMethod.POST,params = "action=delete")
    public String delete(@RequestParam("id") Long id, Model model,
                         @RequestParam("page") Optional<Integer> page){
        invoiceService.deleteById(id);
        parseModel(model,page,true);
        return "invoices";
    }

    @RequestMapping(value = "/invoices",method = RequestMethod.POST,params = "action=cancel")
    public String cancel(@RequestParam("id") Long id, Model model,
                         @RequestParam("page") Optional<Integer> page){
        invoiceService.cancelInvoice(invoiceService.findById(id));
        parseModel(model,page,true);
        return "invoices";
    }

    @RequestMapping(value = "/invoices",method = RequestMethod.POST,params = "action=finish")
    public String finish(@RequestParam("id") Long id, Model model,
                         @RequestParam("page") Optional<Integer> page){
        invoiceService.finishInvoice(invoiceService.findById(id));
        parseModel(model,page,true);
        return "invoices";
    }



}
