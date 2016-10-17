package com.zzheads.CompShop.service;

import com.google.gson.JsonArray;
import com.mashape.unirest.http.JsonNode;
import com.zzheads.CompShop.Application;
import com.zzheads.CompShop.model.PickupRequest;
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
        PickupRequest pickupRequest = new PickupRequest("14.21", "20x16x14", "vol_1", "false", "712.78", "inches", "pounds");
        String cost = qwintryService.getCostPickup(pickupRequest.toJson());
        assertEquals("102.2", cost);
    }

    @Test
    public void testGet() throws Exception {
        double kg = 1;
        double lb = kg/0.453592;
        double cost = qwintryService.getCostDelivery("qwair", Double.toString(lb), "10x10x10","DE1", "RU", "400005", "Батальоная 11-25", "", "Волгоград", "Волгоградская область", "false", "601");
    }

    @Test
    public void testGetCountries () throws Exception {
        JsonNode res = qwintryService.getCountries();
    }

    @Test
    public void testGetHubs () throws Exception {
        JsonNode res = qwintryService.getHubs("us");
    }

    @Test
    public void testGetProfile() throws Exception {
        JsonNode res = qwintryService.getProfile();
    }

    @Test
    public void testGetBalance() throws Exception {
        JsonNode res = qwintryService.getBalance();
    }

    @Test
    public void testGetLocations () throws Exception {
        JsonNode res = qwintryService.getLocations();
        assertEquals(true, res.getObject().getBoolean("success"));
    }

    @Test
    public void getLocationsByCityTest() throws Exception {
        final String[] locationsVolgograd = new String[]{"vol_2", "vol_3", "vol_1", "vol_6", "vol_7", "vol_4", "vol_5", "vol_8"};
        List<String> result = qwintryService.getLocationsByCity("Волгоград");
        assertEquals(locationsVolgograd, result.toArray());
    }

    @Test
    public void getCitiesTest() throws Exception {
//        List<String> cities = qwintryService.getCities();
//        assertEquals("", cities.get(0));
    }
}