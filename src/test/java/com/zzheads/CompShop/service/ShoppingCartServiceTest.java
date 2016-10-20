package com.zzheads.CompShop.service;

import com.zzheads.CompShop.Application;
import com.zzheads.CompShop.model.City;
import com.zzheads.CompShop.model.Product;
import com.zzheads.CompShop.model.Purchase;
import com.zzheads.CompShop.model.ShoppingCart;
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
public class ShoppingCartServiceTest {
    @Autowired
    private ShoppingCartService shoppingCartService;

    private ShoppingCart shoppingCart = new ShoppingCart();

    @Test
    public void deliveryPrice() throws Exception {
        List<Product> products = new ArrayList<>();
        int NUM = 10;
        for (int i=0;i<NUM;i++) {
            int price = i*2+5;
            double weight = Math.random()*150;
            int length = (int) (Math.random()*10+1);
            int height = (int) (Math.random()*10+1);
            int width = (int) (Math.random()*10+1);
            products.add(new Product("Test product #"+i, weight, length, height, width, price, "cm", "kg"));
        }

        List<String> pickupPoints = new ArrayList<>();
        pickupPoints.add("vlg_1");
        shoppingCart.setCity(new City("Волгоград", pickupPoints));
        List<Purchase> purchases = new ArrayList<>();
        for (int i=0;i<NUM;i++) {
            purchases.add(new Purchase(products.get(i), (int) (Math.random()*9+1)));
            shoppingCart.addPurchase(purchases.get(i));
        }

        double price = shoppingCartService.deliveryPrice(shoppingCart);
        assertEquals(100, price, 1);
    }

}