package com.zzheads.CompShop.service;

import com.zzheads.CompShop.dao.CategoryDao;
import com.zzheads.CompShop.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryDao categoryDao;

    @Autowired
    public CategoryServiceImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    public List<Category> findAll() {
        return (List<Category>) categoryDao.findAll();
    }

    @Override
    public Category findById(Long id) {
        return categoryDao.findOne(id);
    }

    @Override
    public Category save(Category category) {
        return categoryDao.save(category);
    }

    @Override
    public void delete(Category category) {
        categoryDao.delete(category);
    }
}
