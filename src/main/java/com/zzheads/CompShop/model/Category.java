package com.zzheads.CompShop.model;

import com.google.gson.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import java.io.Serializable;
import java.lang.reflect.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Category implements Serializable {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "category_id")
    private Long id;

    private String name;

    private String description;

    @OneToMany (cascade = CascadeType.ALL, mappedBy = "category")
    private List<Product> products = new ArrayList<>();

    public Category() {
    }

    public Category(String name, String description, List<Product> products) {
        this.name = name;
        this.description = description;
        this.products = products;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void addProduct(Product product) {
        if (this.products == null) this.products = new ArrayList<>();
        if (!this.products.contains(product)) this.products.add(product);
    }

    public void removeProduct(Product product) {
        if (this.products != null && this.products.contains(product))
            this.products.remove(product);
    }

    private class CategorySerializer implements JsonSerializer<Category> {
        @Override
        public JsonElement serialize(Category src, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) {
            JsonObject result = new JsonObject();
            if (src.getId() != null) result.addProperty("id", src.getId());
            if (src.getName() != null) result.addProperty("name", src.getName());
            if (src.getDescription() != null) result.addProperty("description", src.getDescription());
            if (src.getProducts() != null) result.add("products", Product.toJsonShort(src.getProducts()));
            return result;
        }
    }

    private static class CategoryDeserializer implements JsonDeserializer<Category> {
        @Override
        public Category deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Category result = new Category();
            if (json.isJsonObject()) {
                JsonObject jsonObject = json.getAsJsonObject();
                if (jsonObject.get("id") != null) result.setId(jsonObject.get("id").getAsLong());
                if (jsonObject.get("name") != null) result.setName(jsonObject.get("name").getAsString());
                if (jsonObject.get("description") != null) result.setDescription(jsonObject.get("description").getAsString());
                if (jsonObject.get("products") != null) result.setProducts(Product.fromJsonShort(jsonObject.get("products")));
                return result;
            }
            return null;
        }
    }


    public String toJson() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Category.class, new CategorySerializer()).setPrettyPrinting().create();
        return gson.toJson(this, Category.class);
    }

    public static Category fromJson(JsonElement jsonElement) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Category.class, new CategoryDeserializer()).setPrettyPrinting().create();
        return gson.fromJson(jsonElement, Category.class);
    }
}
