package com.epam.finalproject.model;


import lombok.Data;
import javax.persistence.*;


@Entity
@Data
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id")
    private Long orderId;


    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;


    @Column(name = "order_quantity")
    private Integer quantity;

    @Column(name = "order_value")
    private Integer orderValue;

}
