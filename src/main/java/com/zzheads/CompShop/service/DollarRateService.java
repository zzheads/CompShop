package com.zzheads.CompShop.service;

import com.zzheads.CompShop.model.DollarRate;

import java.util.Date;
import java.util.List;

public interface DollarRateService {
    void save(double rate);
    double findByDate(Date date);
    List<DollarRate> findAll();
    void delete(DollarRate dollarRate);
}
