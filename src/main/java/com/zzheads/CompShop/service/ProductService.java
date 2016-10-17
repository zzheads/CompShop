package com.zzheads.CompShop.service;

import com.zzheads.CompShop.model.Product;

import java.util.Collection;
import java.util.List;

public interface ProductService {
    List<Product> findAll();
    List<Product> findAll(String sortMethod);
    Product findById(Long id);
    Product save(Product product);
    void delete(Product product);
    List<Product> findByCategory(Long id);
    List<Product> findBySearch(String pattern);
    Product findByName(String name);
    long count();
}
