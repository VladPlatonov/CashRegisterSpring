package com.epam.finalproject.service;

import com.epam.finalproject.model.Invoice;
import com.epam.finalproject.model.Order;
import com.epam.finalproject.model.Product;
import com.epam.finalproject.repository.InvoiceRepository;
import com.epam.finalproject.repository.OrderRepository;
import com.epam.finalproject.repository.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTest {


    @Autowired
    OrderService orderService;

    @MockBean
    OrderRepository orderRepository;

    @MockBean
    ProductRepository productRepository;

    Order order;
    Product product;
    Invoice invoice;

    @BeforeAll
    public void setUp()  {
        order.setOrderId(1L);
        order.setOrderValue(10);
        order.setQuantity(100);
        order.setProduct(product);
        order.setInvoice(invoice);
    }

    @Test
    public void testFindById() {
        when(orderRepository.findById(1L)).thenReturn(Optional.ofNullable(order));
        assertEquals(order, orderService.findById(1L));
    }



}