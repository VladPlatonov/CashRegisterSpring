package com.epam.finalproject.service;

import com.epam.finalproject.model.Invoice;
import com.epam.finalproject.model.InvoiceStatus;
import com.epam.finalproject.repository.InvoiceRepository;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InvoiceServiceTest {

    @Autowired
    InvoiceService invoiceService;

    @MockBean
    InvoiceRepository invoiceRepository;


    @Test
    public void createInvoice() {


    }

}