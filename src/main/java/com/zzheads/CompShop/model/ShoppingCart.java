package com.zzheads.CompShop.model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("session")
public class ShoppingCart {

    private City city;

    private List<Purchase> purchases;

    public ShoppingCart() {
    }

    public ShoppingCart(City city) {
        this.city = city;
    }

    public ShoppingCart(List<Purchase> purchases) {
        this.purchases = purchases;
    }

    public ShoppingCart(City city, List<Purchase> purchases) {
        this.city = city;
        this.purchases = purchases;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<Purchase> purchases) {
        this.purchases = purchases;
    }

    private void addPurchase(Product product, int quantity) {
        if (purchases == null) purchases = new ArrayList<>();
        for (Purchase purchase : purchases) {
            if (purchase.getProduct().equals(product)) {
                purchase.setQuantity(purchase.getQuantity() + quantity);
                return;
            }
        }
        purchases.add(new Purchase(product, quantity));
    }

    private void updatePurchase(Product product, int newQuantity) {
        if (purchases == null) return;
        for (Purchase purchase : purchases) {
            if (purchase.getProduct() == product) {
                if (newQuantity > 0) {
                    purchase.setQuantity(newQuantity);
                    return;
                }
                purchases.remove(purchase);
                return;
            }
        }
    }

    public void addPurchase(Purchase purchase) {
        addPurchase(purchase.getProduct(), purchase.getQuantity());
    }

    public void updatePurchase(Purchase purchase) {
        updatePurchase(purchase.getProduct(), purchase.getQuantity());
    }

    public double evaluateTotal () {
        if (purchases == null) return 0;
        double total = 0;
        for (Purchase purchase : purchases) {
            total += purchase.getProduct().getRetailPrice() * purchase.getQuantity();
        }
        return total;
    }
}
