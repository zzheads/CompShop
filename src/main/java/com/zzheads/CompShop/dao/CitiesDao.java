package com.zzheads.CompShop.dao;

import com.zzheads.CompShop.model.City;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitiesDao extends CrudRepository<City, Long> {
}
