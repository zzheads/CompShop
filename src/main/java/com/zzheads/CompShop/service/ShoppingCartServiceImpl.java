package com.zzheads.CompShop.service;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.zzheads.CompShop.model.ShoppingCart;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private LogisticService logisticService;

    @Override
    public double deliveryPrice(ShoppingCart shoppingCart) throws UnirestException {
        if (shoppingCart.getPurchases() == null) return 0;
        if (shoppingCart.getPurchases().size() == 0) return 0;

        String weight = String.valueOf(shoppingCart.totalWeight());
        String weight_type = "kg";
        String country = "RU";
        String city = shoppingCart.getCity().getName();
        String insurance = "0";
        String declarationTotal = String.valueOf(shoppingCart.evaluateTotal());

        JSONObject calc = logisticService.calculate(weight, weight_type, country, city, insurance, declarationTotal, "0", "0");
        double price = getTotal(calc);
        return price;
    }

    @Override
    public double getDeliveryCost() throws UnirestException {
        String weight = "1.0";
        String weight_type = "kg";
        String country = "RU";
        String city = "Волгоград";
        String insurance = "0";
        String declarationTotal = "0";

        JSONObject calc = logisticService.calculate(weight, weight_type, country, city, insurance, declarationTotal, "0", "0");
        double price = getTotal(calc);
        return price;
    }

    private static double getTotal(JSONObject calc) {
        if (calc.getBoolean("success") && calc.has("data")) {
            JSONArray methods = calc.getJSONArray("data");
            JSONObject qwair = methods.getJSONObject(0).getJSONObject("qwair");
            return qwair.getDouble("total");
        } else {
            return Double.NaN;
        }
    }
}
