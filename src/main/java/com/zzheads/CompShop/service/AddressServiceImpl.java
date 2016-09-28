package com.zzheads.CompShop.service;

import com.zzheads.CompShop.dao.AddressDao;
import com.zzheads.CompShop.model.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    private final AddressDao addressDao;

    @Autowired
    public AddressServiceImpl(AddressDao addressDao) {
        this.addressDao = addressDao;
    }

    @Override
    public List<Address> findAll() {
        return (List<Address>) addressDao.findAll();
    }

    @Override
    public Address findById(Long id) {
        return addressDao.findOne(id);
    }

    @Override
    public Address save(Address address) {
        return addressDao.save(address);
    }

    @Override
    public void delete(Address address) {
        addressDao.delete(address);
    }
}
