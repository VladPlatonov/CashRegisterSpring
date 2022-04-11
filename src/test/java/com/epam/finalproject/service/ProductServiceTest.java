package com.epam.finalproject.service;

import com.epam.finalproject.model.Invoice;
import com.epam.finalproject.model.Order;
import com.epam.finalproject.model.Product;
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
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTest {


    @Autowired
    ProductService productService;

    @MockBean
    ProductRepository productRepository;

    Product product;

    @BeforeAll
    public void setUp() {
        product.setDescription("de");
        product.setName("d");
        product.setCost(100);
        product.setProductId(1L);
        product.setQuantity(100);
        product.setCode("DE");

    }

    @Test
    public void testFindById() {
        when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(product));
        assertEquals(product, productService.findById(1L));
    }
}