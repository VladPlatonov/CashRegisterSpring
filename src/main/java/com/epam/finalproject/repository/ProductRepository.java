package com.epam.finalproject.repository;

import com.epam.finalproject.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    public Product findByCode(String code);

    public Page<Product> findAll(Pageable pageable);
    public List<Product> findAll();


}
