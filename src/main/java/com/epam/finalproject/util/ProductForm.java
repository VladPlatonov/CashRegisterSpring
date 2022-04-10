package com.epam.finalproject.util;

import com.epam.finalproject.model.Product;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
public class ProductForm {
    @NotEmpty(message = "Code should not be empty")
    private String code;
    @NotEmpty(message = "Name should not be empty")
    private String name;
    @NotEmpty(message = "Description should not be empty")
    private String description;
    @Min(value=0)
    private Integer cost;
    @Min(value=0)
    private Integer quantity;

    public Product toProduct(){
        Product product = new Product();
        product.setCode(code);
        product.setName(name);
        product.setDescription(description);
        product.setCost(cost);
        product.setQuantity(quantity);
        return product;
    }
}
