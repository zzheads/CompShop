package com.zzheads.CompShop.service;

import com.zzheads.CompShop.model.Address;

import java.util.List;

public interface AddressService {
    List<Address> findAll();
    Address findById(Long id);
    Address save(Address address);
    void delete(Address address);
}
