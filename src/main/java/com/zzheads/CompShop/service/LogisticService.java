package com.zzheads.CompShop.service;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

public interface LogisticService {
    JSONObject userLogin (String email, String password) throws Exception;
    public JSONObject userProfile(String token) throws Exception;
    JSONObject calculate (String weight, String weight_type, String country, String city, String insurance, String declaration_total, String shophelp, String shophelp_safe_addr) throws UnirestException;
}
