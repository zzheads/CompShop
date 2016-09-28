package com.zzheads.CompShop.service;

import com.zzheads.CompShop.dao.SupplierDao;
import com.zzheads.CompShop.model.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierService {
    private final SupplierDao supplierDao;

    @Autowired
    public SupplierServiceImpl(SupplierDao supplierDao) {
        this.supplierDao = supplierDao;
    }

    @Override
    public List<Supplier> findAll() {
        return (List<Supplier>) supplierDao.findAll();
    }

    @Override
    public Supplier findById(Long id) {
        return supplierDao.findOne(id);
    }

    @Override
    public Supplier save(Supplier supplier) {
        return supplierDao.save(supplier);
    }

    @Override
    public void delete(Supplier supplier) {
        supplierDao.delete(supplier);
    }

    @Override
    public Supplier findByName(String name) {
        for (Supplier supplier : findAll()) {
            if (supplier.getName().equals(name))
                return supplier;
        }
        return null;
    }
}
