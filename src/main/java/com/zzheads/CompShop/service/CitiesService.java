package com.zzheads.CompShop.service;

import com.zzheads.CompShop.model.City;

import java.util.List;

public interface CitiesService {
    List<String> findAllCities();
    List<City> findAll();
    City findById(Long id);
    List<String> findPickupByCity (String cityName);
    City save(City cities);
}
