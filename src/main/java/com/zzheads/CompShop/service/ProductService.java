package com.zzheads.CompShop.service;

import com.zzheads.CompShop.model.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAll();
    Product findById(Long id);
    Product save(Product product);
    void delete(Product product);
    List<Product> findByCategory(Long id);
    List<Product> findBySearch(String pattern);
    Product findByName(String name);
    long count();
}
