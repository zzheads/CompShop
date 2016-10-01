package com.zzheads.CompShop.service;

import com.zzheads.CompShop.dao.DollarRateDao;
import com.zzheads.CompShop.model.DollarRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DollarRateServiceImpl implements DollarRateService {
    private final DollarRateDao dollarRateDao;
    public static final double AMAZON_PERCENT = 1.06;

    @Autowired
    public DollarRateServiceImpl(DollarRateDao dollarRateDao) {
        this.dollarRateDao = dollarRateDao;
    }

    @Override
    public void save(double rate) {
        DollarRate dollarRate = new DollarRate(new Date(), rate);
        dollarRateDao.save(dollarRate);
    }

    @Override
    public double findByDate(Date date) {
        DollarRate dollarRate = dollarRateDao.findOne(date);
        if (dollarRate != null)
            return dollarRate.getRate();
        else
            return Double.NaN;
    }

    @Override
    public List<DollarRate> findAll() {
        return (List<DollarRate>) dollarRateDao.findAll();
    }

    @Override
    public void delete(DollarRate dollarRate) {
        dollarRateDao.delete(dollarRate);
    }
}
