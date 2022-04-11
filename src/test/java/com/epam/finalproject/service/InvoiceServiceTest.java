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

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InvoiceServiceTest {

    @Autowired
    InvoiceService invoiceService;

    @MockBean
    InvoiceRepository invoiceRepository;

    Invoice invoice;

    @BeforeAll
    public void setUp(){
        invoice.setInvoiceId(1L);
        invoice.setDate(new Timestamp(System.currentTimeMillis()));
        invoice.setStatus(Collections.singleton(InvoiceStatus.CREATED));
        invoice.setInvoiceCode(2L);
    }

    @Test
    public void testFindById() {
            when(invoiceRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(invoice));
            assertEquals(invoice, invoiceService.findById(1L));
    }

    @Test
    public void testDeleteInvoice() {
        invoiceService.deleteById(1L);
        verify(invoiceRepository).deleteById(1L);
    }

}