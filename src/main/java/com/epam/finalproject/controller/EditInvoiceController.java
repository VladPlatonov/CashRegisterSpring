package com.epam.finalproject.controller;

import com.epam.finalproject.model.Invoice;
import com.epam.finalproject.model.Order;
import com.epam.finalproject.model.Product;
import com.epam.finalproject.service.InvoiceService;
import com.epam.finalproject.service.OrderService;
import com.epam.finalproject.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class EditInvoiceController {

    private final InvoiceService invoiceService;
    private final ProductService productService;
    private final OrderService orderService;

    EditInvoiceController(InvoiceService invoiceService,ProductService productService,OrderService orderService){
        this.invoiceService = invoiceService;
        this.productService = productService;
        this.orderService = orderService;
    }

    private List<Integer> pagination(Integer pages){
        return IntStream.rangeClosed(1, pages).boxed() .collect(Collectors.toList());
    }

    private void parseModel(Model model,Long id,Optional<Integer> page){
        model.addAttribute("invoiceId",id);
        model.addAttribute("status",invoiceService.findById(id).getStatus().toString());
        model.addAttribute("total",orderService.totalInvoice(id));
        model.addAttribute("products",productService.findAll());
        int currentPage = page.orElse(0);
        Page<Order> pages = orderService.findAllByInvoiceId(id,PageRequest.of(currentPage,5));
        model.addAttribute("page",pages);
        if (pages.getTotalPages() > 0)
            model.addAttribute("pageNumbers", pagination(pages.getTotalPages()));
    }

    @RequestMapping(value = "/editInvoice",method = RequestMethod.GET)
    public String edit(){
        return "redirect:/invoices";
    }
    @RequestMapping(value = "/editInvoice",method = RequestMethod.POST)
    public String editInvoice(@RequestParam("id") Long id, Model model,
                              @RequestParam("page") Optional<Integer> page){
        parseModel(model,id,page);
        return "editInvoice";
    }

    @RequestMapping(value = "/editInvoice",method = RequestMethod.POST,params = "action=add")
    public String addOrder(@RequestParam("invoiceId") Long id, Model model,
                           @RequestParam("productCode") String code,
                           @RequestParam("addQuantity") Integer quantity,
                           @RequestParam("page") Optional<Integer> page){
        Invoice invoice = invoiceService.findById(id);
        Product product = productService.findByCode(code);
        if(invoice.getStatus().toString().equals("[CREATED]")) {
            if ((product.getQuantity() > quantity))
                orderService.addOrder(invoice, product, quantity);
             else
                model.addAttribute("errorAdd", true);
        }
        parseModel(model,id,page);
        return "editInvoice";
    }
    @RequestMapping(value = "/editInvoice",method = RequestMethod.POST,params = "action=delete")
    public String deleteOrder(@RequestParam("invoiceId") Long id,@RequestParam("orderId") Long orderId,Model model,
                              @RequestParam("page") Optional<Integer> page){
        Invoice invoice = invoiceService.findById(id);
        if(invoice.getStatus().toString().equals("[CREATED]"))
            orderService.deleteOrder(orderId);
        parseModel(model,id,page);
        return "editInvoice";
    }
    @RequestMapping(value = "/editInvoice",method = RequestMethod.POST,params = "action=update")
    public String updateOrder(@RequestParam("invoiceId") Long id,
                              @RequestParam("orderId") Long orderId,
                              @RequestParam("setQuantity") Integer setQuantity,
                              @RequestParam("page") Optional<Integer> page,
                              Model model){
        Invoice invoice = invoiceService.findById(id);
        Order order = orderService.findById(orderId);
        if(invoice.getStatus().toString().equals("[CREATED]"))
            if(setQuantity-order.getQuantity()>order.getProduct().getQuantity())
                model.addAttribute("errorUpdate",orderId);
            else if(setQuantity==0)
                orderService.deleteOrder(orderId);
            else
                orderService.updateQuantityOrder(order,setQuantity);
        parseModel(model,id,page);
        return "editInvoice";
    }
}
