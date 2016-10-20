package com.zzheads.CompShop.service;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.zzheads.CompShop.model.ShoppingCart;

public interface ShoppingCartService {
    double deliveryPrice(ShoppingCart shoppingCart) throws UnirestException;
    double getDeliveryCost() throws UnirestException;
}
