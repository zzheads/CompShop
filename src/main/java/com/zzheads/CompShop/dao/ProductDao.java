package com.zzheads.CompShop.dao;

import com.zzheads.CompShop.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDao extends CrudRepository<Product, Long> {
}
