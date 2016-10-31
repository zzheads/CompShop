package com.zzheads.CompShop.model;

import com.google.gson.*;
import com.zzheads.CompShop.model.packing.Box;
import com.zzheads.CompShop.model.packing.Container;
import com.zzheads.CompShop.model.packing.Dimension;
import com.zzheads.CompShop.model.packing.Packager;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
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

    public double totalVolume() {
        if (purchases == null) return 0;
        double volume = 0.0;
        for (Purchase purchase : purchases) {
            volume += purchase.getProduct().getVolume() * purchase.getQuantity(); // in cm^2
        }
        return volume;
    }

    public String getDimensions () {
        if (purchases == null) return "";
        List<Box> boxes = new ArrayList<>();
        int maxLength = 0;
        int maxWidth = 0;
        int totalHeight = 0;
        for (Purchase purchase : purchases) {
            if ((int) Math.round(purchase.getProduct().getLengthInCm())>maxLength)
                maxLength = (int) Math.round(purchase.getProduct().getLengthInCm());
            if ((int) Math.round(purchase.getProduct().getWidthInCm())>maxWidth)
                maxWidth = (int) Math.round(purchase.getProduct().getWidthInCm());
            totalHeight += (int) Math.round(purchase.getProduct().getHeightInCm()*purchase.getQuantity());

            for (int i=0;i<purchase.getQuantity();i++) {
                boxes.add(new Box(
                        purchase.getProduct().getName(),
                        (int) Math.round(purchase.getProduct().getHeightInCm()),
                        (int) Math.round(purchase.getProduct().getLengthInCm()),
                        (int) Math.round(purchase.getProduct().getWidthInCm())));
            }
        }
        List<Dimension> containers = new ArrayList<>();
        for (int height = 5;height<=totalHeight;height++) {
            containers.add(new Dimension(maxLength, maxWidth, height));
        }
        Packager packager = new Packager(containers);
        Container container = packager.pack(boxes);
        return container.getDepth()+"x"+container.getWidth()+"x"+container.getHeight()+" cm";
    }

    public String getWeight () {
        return Double.toString(totalWeight())+" kg";
    }

    public double deliveryCost() {
        return totalWeight()*deliveryCostPerKg;
    }

    private static class ShoppingCartSerializer implements JsonSerializer<ShoppingCart> {
        @Override
        public JsonElement serialize(ShoppingCart src, Type typeOfSrc, JsonSerializationContext context) {
            Purchase.ListPurchaseSerializer listPurchaseSerializer = new Purchase.ListPurchaseSerializer();
            JsonObject result = new JsonObject();
            if (src.getPurchases() != null)
                result.add("purchases", listPurchaseSerializer.serialize(src.getPurchases(), List.class, context));
            result.addProperty("dimensions", src.getDimensions());
            result.addProperty("weight", src.getWeight());
            return result;
        }
    }

    public String toJson () {
        Gson gson = new GsonBuilder().registerTypeAdapter(ShoppingCart.class, new ShoppingCartSerializer()).create();
        return gson.toJson(this, ShoppingCart.class);
    }
}
