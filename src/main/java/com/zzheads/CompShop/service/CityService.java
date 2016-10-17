package com.zzheads.CompShop.service;

import com.zzheads.CompShop.model.City;

import java.util.List;

public interface CityService {
    List<City> findAll();
    City findById(long id);
    City save(City city);
    void delete(City city);
    List<String> findAllCitiesNames();
    City findByCityName(String city);
}
