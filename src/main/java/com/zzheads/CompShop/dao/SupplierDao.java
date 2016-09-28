package com.zzheads.CompShop.dao;

import com.zzheads.CompShop.model.Supplier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierDao extends CrudRepository<Supplier, Long> {
}
