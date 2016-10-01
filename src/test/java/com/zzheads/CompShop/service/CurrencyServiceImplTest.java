package com.zzheads.CompShop.service;

import com.zzheads.CompShop.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class CurrencyServiceImplTest {
    @Autowired
    private CurrencyService currencyService;
    private static final double TODAY_DOLLAR_RATE = 63.396;
    
    @Test
    public void getDollarRate() throws Exception {
        double rate = currencyService.getDollarRate();
        assertEquals(TODAY_DOLLAR_RATE, rate, 0.001);
    }
}