package com.zzheads.CompShop.model;

import com.zzheads.CompShop.utils.Maths;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("session")
public class ShoppingCart {
    public final static double DELIVERY_COST_PER_KILOGRAMM = 26.1;
    private City city;
    private List<Purchase> purchases;
    private double deliveryCostPerKg = DELIVERY_COST_PER_KILOGRAMM;

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

    public double getDeliveryCostPerKg() {
        return deliveryCostPerKg;
    }

    public void setDeliveryCostPerKg(double deliveryCostPerKg) {
        this.deliveryCostPerKg = deliveryCostPerKg;
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

    public Purchase getByProductId(long id) {
        if (purchases == null) return null;
        for (Purchase purchase : purchases) {
            if (purchase.getProduct().getId().equals(id))
                return purchase;
        }
        return null;
    }

    public void removePurchaseByProductId(long id) {
        purchases.remove(getByProductId(id));
    }

    public double evaluateTotal () {
        if (purchases == null) return 0;
        double total = 0;
        for (Purchase purchase : purchases) {
            total += purchase.getProduct().getRetailPrice() * purchase.getQuantity();
        }
        return total;
    }

    public double totalWeight () {
        if (purchases == null) return 0;
        double weight = 0.0;
        for (Purchase purchase : purchases) {
            weight += Double.parseDouble(purchase.getProduct().get_weight()) * purchase.getQuantity(); // in kg
        }
        return weight;
    }

    public List<Product> getAllProducts () {
        if (purchases == null) return null;
        List<Product> result = new ArrayList<>();
        for (Purchase purchase : purchases) {
            for (int i=0;i<purchase.getQuantity();i++)
                result.add(purchase.getProduct());
        }
        return result;
    }

    public double totalVolumeWeight() {
        if (purchases == null) return 0;
        double weight = Maths.volumePack(getAllProducts())/5000;
        return weight;
    }

    public double totalVolume() {
        if (purchases == null) return 0;
        double volume = 0.0;
        for (Purchase purchase : purchases) {
            volume += purchase.getProduct().getVolume() * purchase.getQuantity(); // in cm^2
        }
        return volume;
    }

    public double deliveryCost() {
        return totalWeight()*deliveryCostPerKg;
    }
}
