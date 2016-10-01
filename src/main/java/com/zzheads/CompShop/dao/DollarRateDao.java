package com.zzheads.CompShop.dao;

import com.zzheads.CompShop.model.DollarRate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface DollarRateDao extends CrudRepository<DollarRate, Date> {
}
