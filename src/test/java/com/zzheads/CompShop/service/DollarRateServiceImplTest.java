package com.zzheads.CompShop.service;

import com.zzheads.CompShop.Application;
import com.zzheads.CompShop.model.DollarRate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class DollarRateServiceImplTest {
    @Autowired
    private DollarRateService dollarRateService;
    private final double TODAY_RATE = 33.56;

    @Before
    public void setUp() throws Exception {
        dollarRateService.save(TODAY_RATE);
    }

    @After
    public void tearDown() throws Exception {
        for (DollarRate dollarRate : dollarRateService.findAll())
            dollarRateService.delete(dollarRate);
    }

    @Test
    public void save() throws Exception {
        assertEquals(TODAY_RATE, dollarRateService.findByDate(new Date()), 0.001);
    }

    @Test
    public void findByDate() throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/YYYY");
        Date tomorrow = formatter.parse("10/10/2016");
        assertEquals(TODAY_RATE, dollarRateService.findByDate(new Date()), 0.001);
        assertEquals(Double.NaN, dollarRateService.findByDate(tomorrow), 0.1);
    }

}