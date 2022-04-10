package com.epam.finalproject.model;


import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import java.util.List;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_id")
    private Long productId;
    @Column(name = "product_code")
    @NotEmpty(message = "Code should not be empty")
    private String code;
    @Column(name = "product_name")
    @NotEmpty(message = "Name should not be empty")
    private String name;
    @Column(name = "product_description")
    @NotEmpty(message = "Description should not be empty")
    private String description;
    @Column(name = "product_cost")
    @Min(value=0)
    private Integer cost;
    @Column(name = "product_quantity")
    @Min(value=0)
    private Integer quantity;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Order> orders;

}
