package com.epam.finalproject.repository;

import com.epam.finalproject.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    public Page<Order> findAllByInvoice_InvoiceId(Long id, Pageable pageable);
    public List<Order> findAllByInvoice_InvoiceId(Long id);
}
