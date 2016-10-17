package com.zzheads.CompShop.model;

import com.google.gson.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Supplier implements Serializable {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "supplier_id")
    private Long id;

    private String name;

    @OneToOne (cascade = CascadeType.ALL)
    @JoinColumn (name = "address_id")
    private Address address;

    @OneToMany (cascade = CascadeType.ALL)
    private List<Product> products;

    public Supplier() {
    }

    public Supplier(String name, Address address, List<Product> products) {
        this.name = name;
        this.address = address;
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void addProduct(Product product) {
        if (products == null) products = new ArrayList<>();
        if (!products.contains(product)) products.add(product);
    }

    public void removeProduct(Product product) {
        if (products != null && products.contains(product)) products.remove(product);
    }

    private class SupplierSerializer implements JsonSerializer<Supplier> {
        @Override
        public JsonElement serialize(Supplier src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject result = new JsonObject();
            if (src.getId() != null) result.addProperty("id", src.getId());
            if (src.getName() != null) result.addProperty("name", src.getName());
            if (src.getAddress() != null) result.addProperty("address", src.getAddress().toJson());
            if (src.getProducts() != null) result.add("products", Product.toJsonShort(src.getProducts()));
            return result;
        }
    }

    private static class SupplierDeserializer implements JsonDeserializer<Supplier> {
        @Override
        public Supplier deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Supplier result = new Supplier();
            if (json.isJsonObject()) {
                JsonObject jsonObject = json.getAsJsonObject();
                if (jsonObject.get("id") != null) result.setId(jsonObject.get("id").getAsLong());
                if (jsonObject.get("name") != null) result.setName(jsonObject.get("name").getAsString());
                if (jsonObject.get("address") != null) result.setAddress(Address.fromJson(jsonObject.get("address")));
                if (jsonObject.get("products") != null) result.setProducts(Product.fromJsonShort(jsonObject.get("products")));
                return result;
            }
            return null;
        }
    }

    public String toJson() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Supplier.class, new SupplierSerializer()).setPrettyPrinting().create();
        return gson.toJson(this, Supplier.class);
    }

    public static Supplier fromJson(JsonElement jsonElement) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Supplier.class, new SupplierDeserializer()).setPrettyPrinting().create();
        return gson.fromJson(jsonElement, Supplier.class);
    }
}
