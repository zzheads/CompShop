package com.zzheads.CompShop.model;

import com.google.gson.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.List;

@Component
@Scope("session")
public class Purchase {
    private Product product;
    private int quantity;

    public Purchase() {
    }

    public Purchase(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    private static class PurchaseSerializer implements JsonSerializer<Purchase> {
        @Override
        public JsonElement serialize(Purchase src, Type typeOfSrc, JsonSerializationContext context) {
            Product.ProductSerializer productSerializer = new Product.ProductSerializer();
            JsonObject result = new JsonObject();
            if (src.getProduct() != null) result.add("product", productSerializer.serialize(src.getProduct(), Product.class, context));
            result.addProperty("quantity", src.getQuantity());
            return result;
        }
    }

    private static class ListPurchaseSerializer implements JsonSerializer<List<Purchase>> {
        @Override
        public JsonElement serialize(List<Purchase> src, Type typeOfSrc, JsonSerializationContext context) {
            if (src != null) {
                Product.ProductSerializer productSerializer = new Product.ProductSerializer();
                JsonArray result = new JsonArray();
                for (Purchase purchase : src) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.add("product", productSerializer.serialize(purchase.getProduct(), Product.class, context));
                    jsonObject.addProperty("quantity", purchase.getQuantity());
                    result.add(jsonObject);
                }
                return result;
            }
            return null;
        }
    }

    private static class PurchaseDeserializer implements JsonDeserializer<Purchase> {
        @Override
        public Purchase deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Purchase result = new Purchase();
            JsonObject jsonObject = json.getAsJsonObject();
            if (jsonObject.get("product")!=null && jsonObject.get("product").isJsonObject()) result.setProduct(Product.fromJson(jsonObject.get("product")));
            if (jsonObject.get("quantity")!=null) result.setQuantity(jsonObject.get("quantity").getAsInt());
            return result;
        }
    }

    public static Purchase fromJson (JsonElement jsonElement) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Purchase.class, new PurchaseDeserializer()).create();
        return gson.fromJson(jsonElement, Purchase.class);
    }

    public static Purchase fromJson (String jsonString) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Purchase.class, new PurchaseDeserializer()).create();
        return gson.fromJson(jsonString, Purchase.class);
    }

    public String toJson() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Purchase.class, new PurchaseSerializer()).setPrettyPrinting().create();
        return gson.toJson(this, Purchase.class);
    }

    public static String toJson(List<Purchase> purchases) {
        Gson gson = new GsonBuilder().registerTypeAdapter(List.class, new ListPurchaseSerializer()).setPrettyPrinting().create();
        return gson.toJson(purchases, List.class);
    }
}
