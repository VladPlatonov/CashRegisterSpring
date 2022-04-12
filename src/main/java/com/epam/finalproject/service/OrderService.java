package com.epam.finalproject.service;


import com.epam.finalproject.model.Invoice;
import com.epam.finalproject.model.Order;
import com.epam.finalproject.model.Product;
import com.epam.finalproject.repository.InvoiceRepository;
import com.epam.finalproject.repository.OrderRepository;
import com.epam.finalproject.repository.ProductRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class OrderService {

    private static final Logger logger = LogManager.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;


    OrderService(OrderRepository orderRepository, ProductRepository productRepository){
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public void save(Order order){
        orderRepository.save(order);
    }

    /**
     * This method calculates the amount of all goods in the invoice
     * @param id InvoiceId
     * @return sum
     */
    public Integer totalInvoice(Long id){
        return orderRepository.findAllByInvoice_InvoiceId(id).stream()
                .map(Order::getOrderValue)
                .mapToInt(Integer::intValue).sum();
    }

    public Page<Order> findAllByInvoiceId(Long id, Pageable pageable){
        return orderRepository.findAllByInvoice_InvoiceId(id,pageable);
    }



    @Transactional
    public void addOrder(Invoice invoice, Product product, Integer quantity){
        Order order = new Order();
        order.setQuantity(quantity);
        order.setInvoice(invoice);
        order.setProduct(product);
        product.setQuantity(product.getQuantity()-quantity);
        productRepository.save(product);
        order.setOrderValue(product.getCost()*quantity);
        save(order);
        logger.info("Add order");
    }

    @Transactional
    public void deleteOrder(Long orderId){
        Order order = orderRepository.getById(orderId);
        Product product = order.getProduct();
        product.setQuantity(order.getQuantity() + order.getProduct().getQuantity());
        productRepository.save(product);
        orderRepository.deleteById(order.getOrderId());
        logger.info("Delete order");
    }

    public Order findById(Long orderId){
        return orderRepository.getById(orderId);
    }


    /**
     * update quantity order
     * If the number is increased the quantity product in the warehouse decreases
     * Else quantity product in the warehouse increases
     * @param order
     * @param quantity
     */
    @Transactional
    public void  updateQuantityOrder(Order order, Integer quantity){
        Product product = order.getProduct();
        product.setQuantity(product.getQuantity()+order.getQuantity() -quantity);
        productRepository.save(product);
        order.setQuantity(quantity);
        order.setOrderValue(order.getProduct().getCost()*quantity);
        orderRepository.save(order);
        logger.info("Update order");
    }
}
