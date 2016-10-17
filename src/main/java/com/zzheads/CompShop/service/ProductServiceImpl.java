package com.zzheads.CompShop.service;

import com.zzheads.CompShop.dao.CategoryDao;
import com.zzheads.CompShop.dao.ProductDao;
import com.zzheads.CompShop.dao.SupplierDao;
import com.zzheads.CompShop.model.Category;
import com.zzheads.CompShop.model.Product;
import com.zzheads.CompShop.model.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collector;
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
    public List<Product> findAll(String sortMethod) {
        switch (sortMethod) {
            case "по Имени": default:
                return findAll().stream().sorted((p1, p2) -> {return p1.getName().compareTo(p2.getName());}).collect(Collectors.toList());
            case "по Цене":
                return findAll().stream().sorted((p1, p2) -> {
                    if (p1.getRetailPrice() == p2.getRetailPrice())
                        return 0;
                    if (p1.getRetailPrice() > p2.getRetailPrice())
                        return 1;
                    return -1;
                }).collect(Collectors.toList());
        }
    }

    @Override
    public Product findById(Long id) {
        return productDao.findOne(id);
    }

    @Override
    public Product save(Product product) {
//        if (product == null) return null;
//
//        Supplier supplier = product.getSupplier();
//        Category category = product.getCategory();
//        if (category!=null) categoryDao.save(category);
//        if (supplier!=null) supplierDao.save(supplier);
//        product.setCategory(category);
//        product.setSupplier(supplier);
//        productDao.save(product);
//        if (category!=null) {
//            category.addProduct(product);
//            categoryDao.save(category);
//        }
//        if (supplier!=null) {
//            supplier.addProduct(product);
//            supplierDao.save(supplier);
//        }
        return productDao.save(product);
    }

    @Override
    public void delete(Product product) {
//        if (product == null) return;
//        Supplier supplier = product.getSupplier();
//        Category category = product.getCategory();
//        if (supplier != null) {
//            supplier.removeProduct(product);
//            product.setSupplier(null);
//            productDao.save(product);
//            supplierDao.save(supplier);
//        }
//        if (category != null) {
//            category.removeProduct(product);
//            product.setCategory(null);
//            productDao.save(product);
//            categoryDao.save(category);
//        }
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
