package com.zzheads.CompShop.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mashape.unirest.http.JsonNode;
import com.zzheads.CompShop.Application;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class LogisticServiceTest {
    @Autowired
    private LogisticService logisticService;
    private final static String TEST_EMAIL = "";
    private final static String TEST_PASSWORD = "";

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void userLoginTest() throws Exception {
        JSONObject response = logisticService.userLogin(TEST_EMAIL, TEST_PASSWORD);

        JSONObject data = response.getJSONObject("data");
        String token = data.getString("token");
        String balance = data.getString("balance");
        String firstName = data.getString("firstName");
        String lastName = data.getString("lastName");
        assertEquals(true, response.getBoolean("success"));
    }

    @Test
    public void userProfileTest() throws Exception {
        JSONObject response = logisticService.userLogin(TEST_EMAIL, TEST_PASSWORD);
        JSONObject data = response.getJSONObject("data");
        String token = data.getString("token");
        response = logisticService.userProfile(token);

        data = response.getJSONObject("data");

        assertEquals(true, response.getBoolean("success"));
    }

    @Test
    public void calculateTest() throws Exception {
        String[] cities = {"Волгоград", "Москва", "Казань", "Сахалин", "Владивосток", "Калининград", "Тюмень", "Уфа"};
        Map<String, Double> prices = new HashMap<>();
        for (int i=0;i<cities.length;i++) {
            JSONObject response = logisticService.calculate("1.0", "kg", "RU", cities[i], "no", "100.0", "0", "0");
            JSONArray methods = response.getJSONArray("data");
            JSONObject qwair = methods.getJSONObject(0).getJSONObject("qwair");
            Double price = qwair.getDouble("total");
            prices.put(cities[i], price);
        }
        assertEquals(cities.length, prices.size());
    }
}