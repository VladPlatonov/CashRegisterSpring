package com.epam.finalproject.util;

import lombok.Data;

@Data
public class CreateInvoiceForm {
    private String code;
    private Integer quantity;

    @Override
    public String toString() {
        return "CreateInvoiceForm{" +
                "code='" + code + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
