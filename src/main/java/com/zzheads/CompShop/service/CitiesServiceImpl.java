package com.zzheads.CompShop.service;

import com.zzheads.CompShop.dao.CitiesDao;
import com.zzheads.CompShop.model.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CitiesServiceImpl implements CitiesService {
    @Autowired
    private CitiesDao citiesDao;

    @Override
    public List<String> findAllCities() {
        List<String> result = new ArrayList<>();
        for (City cities : citiesDao.findAll())
            result.add(cities.getName());
        return result;
    }

    @Override
    public List<City> findAll() {
        return (List<City>) citiesDao.findAll();
    }

    @Override
    public City findById(Long id) {
        return citiesDao.findOne(id);
    }

    @Override
    public List<String> findPickupByCity(String city) {
        for (City cities : findAll())
            if (cities.getName().equals(city))
                return cities.getPickup_points();
        return null;
    }

    @Override
    public City save(City cities) {
        return citiesDao.save(cities);
    }
}
