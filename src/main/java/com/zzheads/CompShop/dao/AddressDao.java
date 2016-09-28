package com.zzheads.CompShop.dao;

import com.zzheads.CompShop.model.Address;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressDao extends CrudRepository<Address, Long> {
}
