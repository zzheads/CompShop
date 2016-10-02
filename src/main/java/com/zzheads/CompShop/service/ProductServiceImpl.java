package com.zzheads.CompShop.service;

import com.zzheads.CompShop.dao.CategoryDao;
import com.zzheads.CompShop.dao.ProductDao;
import com.zzheads.CompShop.dao.SupplierDao;
import com.zzheads.CompShop.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductDao productDao;
    private final CategoryDao categoryDao;
    private final SupplierDao supplierDao;

    @Autowired
    public ProductServiceImpl(ProductDao productDao, CategoryDao categoryDao, SupplierDao supplierDao) {
        this.productDao = productDao;
        this.categoryDao = categoryDao;
        this.supplierDao = supplierDao;
    }

    @Override
    public List<Product> findAll() {
        return (List<Product>) productDao.findAll();
    }

    @Override
    public Product findById(Long id) {
        return productDao.findOne(id);
    }

    @Override
    public Product save(Product product) {
        return productDao.save(product);
    }

    @Override
    public void delete(Product product) {
        productDao.delete(product);
    }

    @Override
    public List<Product> findByCategory(Long id) {
        return findAll().stream().filter(product -> Objects.equals(product.getCategory().getId(), id)).collect(Collectors.toList());
    }

    @Override
    public List<Product> findBySearch(String pattern) {
        List<Product> result = new ArrayList<>();
        for (Product product : findAll()) {
            if (product.getName().toLowerCase().contains(pattern.toLowerCase()))
                result.add(product);
        }
        return result;
    }

    @Override
    public Product findByName(String name) {
        for (Product product : findAll()) {
            if (product.getName().equals(name))
                return product;
        }
        return null;
    }

    @Override
    public long count() {
        return productDao.count();
    }
}
