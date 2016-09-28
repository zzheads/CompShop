package com.zzheads.CompShop.dao;


import com.zzheads.CompShop.model.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryDao extends CrudRepository<Category, Long> {
}
