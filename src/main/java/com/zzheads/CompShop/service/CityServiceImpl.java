package com.zzheads.CompShop.service;

import com.zzheads.CompShop.dao.CityDao;
import com.zzheads.CompShop.model.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CityServiceImpl implements CityService {
    private final CityDao cityDao;

    @Autowired
    public CityServiceImpl(CityDao cityDao) {
        this.cityDao = cityDao;
    }

    @Override
    public List<City> findAll() {
        return (List<City>) cityDao.findAll();
    }

    @Override
    public City findById(long id) {
        return cityDao.findOne(id);
    }

    @Override
    public City save(City city) {
        return cityDao.save(city);
    }

    @Override
    public void delete(City city) {
        cityDao.delete(city);
    }

    @Override
    public List<String> findAllCitiesNames() {
        List<String> result = new ArrayList<>();
        for (City city : findAll())
            result.add(city.getName());
        return result;
    }

    @Override
    public City findByCityName(String city) {
        List<City> allCities = findAll();
        for (City c : allCities) {
            if (c.getName().equals(city))
                return c;
        }
        return null;
    }
}
