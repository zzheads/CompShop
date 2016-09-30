package com.zzheads.CompShop.service;

import com.google.gson.JsonObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.HttpException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Service
public class QwintryServiceImpl implements QwintryService {
    private final String RETAIL_PRICING = "1";
    private final String BASE_URL = "http://logistics.qwintry.com";

    QwintryServiceImpl() {
        final String USER_AGENT = "Mozilla/5.0";
        final String API_KEY = "p3DdI_2ufOb3eHkDUUy8mPN36p4O9PTk";
        Unirest.setDefaultHeader("User_Agent", USER_AGENT);
        Unirest.setDefaultHeader("Authorization", "Bearer "+ API_KEY);
    }

    @Override
    public double getCostPickup(String weight, String dimensions, String toPickup, String insurance, String value) throws Exception {

        Map<String, Object> params = new HashMap<>();
        params.put("params[weight_kg]", weight);
        params.put("params[dimensions_cm]", dimensions);
        params.put("params[delivery_pickup]", toPickup);
        params.put("params[insurance]", insurance);
        params.put("params[items_value]", value);
        params.put("params[retail_pricing]", RETAIL_PRICING);

        String url = BASE_URL+"/api/cost";
        HttpResponse<JsonNode> jsonResponse = Unirest.post(url).fields(params).asJson();
        JSONObject result = jsonResponse.getBody().getObject();

        if (result.getBoolean("success")) {
            double insuranceCost = result.getDouble("insurance_cost");
            double shippingCost = result.getDouble("shipping_cost");
            if (insurance.equals("true"))
                return shippingCost + insuranceCost;
            else
                return shippingCost;
        }
        return Double.NaN;
    }

    @Override
    public double getCostDelivery(String method, String weight, String dimensions, String hubCode, String addrCountry, String addrZip, String addrLine1, String addrLine2, String addrCity, String addrState, String insurance, String itemsValue) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("params[method]", method);
        params.put("params[weight_kg]", weight);
        params.put("params[dimensions_cm]", dimensions);
        params.put("params[hub_code]", hubCode);
        params.put("params[addr_country]", addrCountry);
        params.put("params[addr_zip]", addrZip);
        params.put("params[addr_line1]", addrLine1);
        params.put("params[addr_line2]", addrLine2);
        params.put("params[addr_city]", addrCity);
        params.put("params[addr_state]", addrState);
        params.put("params[insurance]", insurance);
        params.put("params[items_value]", itemsValue);
        params.put("params[retail_pricing]", RETAIL_PRICING);

        String url = BASE_URL+"/api/cost";
        HttpResponse<JsonNode> jsonResponse = Unirest.post(url).fields(params).asJson();
        JSONObject result = jsonResponse.getBody().getObject();

        if (result.getBoolean("success")) {
            double insuranceCost = result.getDouble("insurance_cost");
            double shippingCost = result.getDouble("shipping_cost");
            if (insurance.equals("true"))
                return shippingCost + insuranceCost;
            else
                return shippingCost;
        }
        return Double.NaN;
    }

    @Override
    public JsonNode getCountries() throws Exception {
        String url = BASE_URL+"/api/countries-list";
        HttpResponse<JsonNode> jsonResponse = Unirest.get(url).asJson();
        return jsonResponse.getBody();
    }

    @Override
    public JsonNode getLocations() throws Exception {
        String url = BASE_URL+"/api/locations-list";
        HttpResponse<JsonNode> jsonResponse = Unirest.get(url).asJson();
        return jsonResponse.getBody();
    }

    @Override
    public JsonNode getHubs(String country) throws Exception {
        String url = BASE_URL+"/api/hubs-list";
        HttpResponse<JsonNode> jsonResponse = Unirest.get(url).queryString("country", country).asJson();
        return jsonResponse.getBody();
    }
}
