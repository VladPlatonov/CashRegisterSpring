package com.epam.finalproject.service;


import com.epam.finalproject.model.*;
import com.epam.finalproject.repository.InvoiceRepository;
import com.epam.finalproject.util.CreateInvoiceForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ProductService productService;
    private final OrderService orderService;

    InvoiceService(InvoiceRepository invoiceRepository,ProductService productService, OrderService orderService){
        this.orderService = orderService;
        this.productService = productService;
        this.invoiceRepository = invoiceRepository;

    }

    public Page<Invoice> findAll(Pageable pageable){
        return invoiceRepository.findAll(pageable);
    }


    @Transactional
    public void createInvoice(List <CreateInvoiceForm> products, Long InvoiceCode, User user) {
        Timestamp stamp = new Timestamp(System.currentTimeMillis());
        Invoice invoice = new Invoice();
        invoice.setUser(user);
        invoice.setInvoiceCode(InvoiceCode);
        invoice.setStatus(Collections.singleton(InvoiceStatus.CREATED));
        invoice.setInvoiceNotes("CREATED BY:" + user.getUserRole().toString());
        invoice.setDate(stamp);
        List<Order> orderList = new ArrayList<>();
        products.stream()
                .filter(s-> productService.findByCode(s.getCode()).getQuantity()>s.getQuantity())
                .forEach(selected->{
            Product product = productService.findByCode(selected.getCode());
            Order order = new Order();
            order.setQuantity(selected.getQuantity());
            order.setInvoice(invoice);
            order.setProduct(product);
            order.setOrderValue(product.getCost()* selected.getQuantity());
            product.setQuantity(product.getQuantity()-selected.getQuantity());
            productService.save(product);
            orderList.add(order);
        });
        invoice.setOrders(orderList);
        invoiceRepository.save(invoice);
    }

    public void finishInvoice(Invoice invoice) {
        invoice.getStatus().clear();
        invoice.getStatus().add(InvoiceStatus.FINISHED);
        invoiceRepository.save(invoice);
    }
    @Transactional
    public void cancelInvoice(Invoice invoice) {
        invoice.getOrders()
                .forEach(p->{
                    Product product = p.getProduct();
                    product.setQuantity(product.getQuantity()+p.getQuantity());
                    productService.save(product);
                });
        invoice.getStatus().clear();
        invoice.getStatus().add(InvoiceStatus.CANCELLED);
        invoiceRepository.save(invoice);
    }


    public Invoice findById(Long id){
        return invoiceRepository.getById(id);
    }

    public void deleteById(Long id ){
        invoiceRepository.deleteById(id);
    }

}
