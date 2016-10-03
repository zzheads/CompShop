package com.zzheads.CompShop.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.zzheads.CompShop.model.City;
import com.zzheads.CompShop.model.PickupRequest;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zzheads.CompShop.model.PickupRequest.fromJson;

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

    private double getCost (HttpResponse<JsonNode> jsonResponse, String insurance) {
        JSONObject jsonBody = jsonResponse.getBody().getObject();
        boolean success = jsonBody.getBoolean("success");
        JSONObject jsonResult = jsonBody.getJSONObject("result");
        if (success && jsonResult != null) {
            double insuranceCost = jsonResult.getDouble("insurance_cost");
            double shippingCost = jsonResult.getDouble("shipping_cost");
            if (insurance.equals("true"))
                return insuranceCost + shippingCost;
            else
                return shippingCost;
        }
        return Double.NaN;
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
        return getCost(jsonResponse, insurance);
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
        return getCost(jsonResponse, insurance);
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
    public List<String> getLocationsByCity (String city) throws Exception {
        List<String> result = new ArrayList<>();
        JSONObject object = getLocations().getObject().getJSONObject("result");
        if (object == null) return null;
        JSONObject jsonCity = object.getJSONObject(city);
        if (jsonCity == null) return null;
        JSONObject pickupPoints = jsonCity.getJSONObject("pickup_points");
        if (pickupPoints == null) return null;
        Object[] names = pickupPoints.keySet().toArray();
        for (Object o : names)
            result.add(o.toString());
        return result;
    }

    @Override
    public List<City> getCities() throws Exception {
        List<City> result = new ArrayList<>();
        JSONObject object = getLocations().getObject().getJSONObject("result");
        Object[] namesOfCities = object.keySet().toArray();
        for (Object o : namesOfCities) {
            String cityName = (String) o;
            JSONObject jsonCity = object.getJSONObject(cityName);
            JSONObject pickupPoints = jsonCity.getJSONObject("pickup_points");
            Object[] namesPP = pickupPoints.keySet().toArray();
            List<String> namesOfPickupPoints = new ArrayList<>();
            for (Object namePP : namesPP)
                namesOfPickupPoints.add((String) namePP);
            result.add(new City(null, cityName, namesOfPickupPoints));
        }
        return result;
    }

    @Override
    public JsonNode getHubs(String country) throws Exception {
        String url = BASE_URL+"/api/hubs-list";
        HttpResponse<JsonNode> jsonResponse = Unirest.get(url).queryString("country", country).asJson();
        return jsonResponse.getBody();
    }

    @Override
    public JsonNode getProfile() throws Exception {
        String url = BASE_URL+"/api/profile";
        HttpResponse<JsonNode> jsonResponse = Unirest.get(url).asJson();
        return jsonResponse.getBody();
    }

    @Override
    public JsonNode getBalance() throws Exception {
        String url = BASE_URL+"/api/balance";
        HttpResponse<JsonNode> jsonResponse = Unirest.get(url).asJson();
        return jsonResponse.getBody();
    }

    @Override
    public String getCostPickup(String jsonPickupRequest) throws Exception {
        PickupRequest pickupRequest = fromJson(jsonPickupRequest);
        return Double.toString(getCostPickup(pickupRequest.getWeightInKg(), pickupRequest.getDimensionsInCm(), pickupRequest.getDelivery_pickup(), pickupRequest.getInsurance(), pickupRequest.getItems_value()));
    }
}
