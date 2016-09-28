package com.zzheads.CompShop.service;

import com.zzheads.CompShop.model.Supplier;

import java.util.List;

public interface SupplierService {
    List<Supplier> findAll();
    Supplier findById(Long id);
    Supplier save(Supplier supplier);
    void delete(Supplier supplier);
    Supplier findByName (String name);
}
