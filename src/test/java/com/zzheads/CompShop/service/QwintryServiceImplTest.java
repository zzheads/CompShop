package com.zzheads.CompShop.service;

import com.google.gson.JsonArray;
import com.mashape.unirest.http.JsonNode;
import com.zzheads.CompShop.Application;
import org.json.JSONArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class QwintryServiceImplTest {
    @Autowired
    private QwintryService qwintryService;

    @Test
    public void testGetCostPickup() throws Exception {
        double cost = qwintryService.getCostPickup("5", "4x4x4", "msk_1", "false", "600");
    }

    @Test
    public void testGet() throws Exception {
        double kg = 0.01;
        double lb = kg/0.453592;
        double cost = qwintryService.getCostDelivery("qwair", Double.toString(lb), "10x10x10","DE1", "RU", "400005", "Батальоная 11-25", "", "Волгоград", "Волгоградская область", "false", "601");
    }

    @Test
    public void testGetCountries () throws Exception {
        JsonNode res = qwintryService.getCountries();
    }

    @Test
    public void testGetLocations () throws Exception {
        JsonNode res = qwintryService.getLocations();
    }

    @Test
    public void testGetHubs () throws Exception {
        JsonNode res = qwintryService.getHubs("us");
    }
}