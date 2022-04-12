package com.epam.finalproject.service;


import com.epam.finalproject.model.*;
import com.epam.finalproject.repository.InvoiceRepository;
import com.epam.finalproject.util.CreateInvoiceForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger logger = LogManager.getLogger(InvoiceService.class);

    private final InvoiceRepository invoiceRepository;
    private final ProductService productService;

    InvoiceService(InvoiceRepository invoiceRepository,ProductService productService){
        this.productService = productService;
        this.invoiceRepository = invoiceRepository;

    }

    public Page<Invoice> findAll(Pageable pageable){
        return invoiceRepository.findAll(pageable);
    }



    /**
     * Create invoice
     * @param products selected products to create
     * @param InvoiceCode generated code
     * @param user user who created
     */
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
        logger.info("Create invoice invoiceCode:" + InvoiceCode);
    }

    public void finishInvoice(Invoice invoice) {
        invoice.getStatus().clear();
        invoice.getStatus().add(InvoiceStatus.FINISHED);
        invoiceRepository.save(invoice);
        logger.info("Finish invoice invoiceId:" + invoice.getInvoiceId());
    }

    /**
     * After cancelled invoice
     * Update quantity products in warehouse
     * @param invoice
     */
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
        logger.info("Cancel invoice invoiceId: "+ invoice.getInvoiceId() );
    }


    public Invoice findById(Long id){
        return invoiceRepository.getById(id);
    }

    public void deleteById(Long id ){
        invoiceRepository.deleteById(id);
    }

}
