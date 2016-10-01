package com.zzheads.CompShop.service;

import com.mashape.unirest.http.JsonNode;
import com.zzheads.CompShop.model.PickupRequest;

public interface QwintryService {
    double getCostPickup(String weight, String dimensions, String toPickup, String insurance, String value) throws Exception;
    double getCostDelivery(String method, String weight, String dimensions, String hubCode, String addrCountry, String addrZip, String addrLine1, String addrLine2, String addrCity, String addrState, String insurance, String itemsValue) throws Exception;
    JsonNode getCountries() throws Exception;
    JsonNode getLocations() throws Exception;
    JsonNode getHubs(String country) throws Exception;
    JsonNode getProfile() throws Exception;
    JsonNode getBalance() throws Exception;
    String getCostPickup(String jsonPickupRequest) throws Exception;
}
