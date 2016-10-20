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
        Product product1 = new Product("Test product #1", 0.5, 100, 50, 50, 100, "cm", "kg");
        Product product2 = new Product("Test product #2", 0.5, 100, 50, 50, 50, "cm", "kg");

        List<String> pickupPoints = new ArrayList<>();
        pickupPoints.add("vlg_1");
        shoppingCart.setCity(new City("Волгоград", pickupPoints));

        Purchase purchase1 = new Purchase(product1, 2);
        Purchase purchase2 = new Purchase(product2, 4);

        shoppingCart.addPurchase(purchase1);
        shoppingCart.addPurchase(purchase2);

        double price = shoppingCartService.deliveryPrice(shoppingCart);
        assertEquals(100, price, 1);
    }

}