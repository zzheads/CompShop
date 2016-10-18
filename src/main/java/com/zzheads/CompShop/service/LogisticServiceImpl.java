package com.zzheads.CompShop.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class LogisticServiceImpl implements LogisticService {
    private final String BASE_URL = "http://www.qwintry.com/api-rest/v2";
    private final String UNIQUE_KEY = "9e4fddbb3adc4c67f74bb2b7757cebf9";

    LogisticServiceImpl() {
        final String USER_AGENT = "Mozilla/5.0";
        Unirest.setDefaultHeader("User_Agent", USER_AGENT);
    }

    @Override
    public JSONObject userLogin(String email, String password) throws Exception {
        String url = BASE_URL + "/user/login";
        HttpResponse<JsonNode> jsonResponse = Unirest.get(url)
                .queryString("key", UNIQUE_KEY)
                .queryString("email", email)
                .queryString("password", password)
                .asJson();

        return jsonResponse.getBody().getObject();
    }

    @Override
    public JSONObject userProfile(String token) throws Exception {
        String url = BASE_URL + "/user/profile";
        HttpResponse<JsonNode> jsonResponse = Unirest.get(url)
                .queryString("key", UNIQUE_KEY)
                .queryString("token", token)
                .asJson();

        return jsonResponse.getBody().getObject();
    }

    @Override
    public JSONObject calculate(String weight, String weight_type, String country, String city, String insurance, String declaration_total, String shophelp, String shophelp_safe_addr) throws UnirestException {
        String url = BASE_URL + "/calculator";
        HttpResponse<JsonNode> jsonResponse = Unirest.get(url)
                .queryString("key", UNIQUE_KEY)
                .queryString("weight", weight)
                .queryString("weight_type", weight_type)
                .queryString("country", country)
                .queryString("city", city)
                .queryString("insurance", insurance)
                .queryString("declaration_total", declaration_total)
                .queryString("shophelp", shophelp)
                .queryString("shophelp_safe_addr", shophelp_safe_addr)
                .asJson();

        return jsonResponse.getBody().getObject();
    }
}
