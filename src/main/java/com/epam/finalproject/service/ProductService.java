package com.epam.finalproject.service;


import com.epam.finalproject.model.Product;
import com.epam.finalproject.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public void deleteById(Long id){
        productRepository.deleteById(id);
    }

    public Product findById(Long id){
        return productRepository.getById(id);
    }

    public Page<Product> findAll(Pageable pageable){
        return productRepository.findAll(pageable);
    }
    public List<Product> findAll(){
        return productRepository.findAll();
    }

    public Product findByCode(String code){
        return productRepository.findByCode(code);
    }

    public boolean isValidCode(String code){
        return productRepository.findByCode(code) != null;
    }

    public void save(Product product){
        productRepository.save(product);
    }


}
