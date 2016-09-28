package com.zzheads.CompShop.model;

import com.google.gson.*;

import javax.persistence.*;
import javax.xml.stream.StreamFilter;
import java.io.Serializable;
import java.lang.reflect.Type;

@Entity
public class Address implements Serializable {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "address_id")
    private Long id;

    private String firstName;

    private String lastName;

    private String url;

    private String email;

    private String phone;

    private String country;

    private String city;

    private String zipcode;

    private String street;

    public Address() {
    }

    public Address(String firstName, String lastName, String url, String email, String phone, String country, String city, String zipcode, String street) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.url = url;
        this.email = email;
        this.phone = phone;
        this.country = country;
        this.city = city;
        this.zipcode = zipcode;
        this.street = street;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    private class AddressSerializer implements JsonSerializer<Address> {
        @Override
        public JsonElement serialize(Address src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject result = new JsonObject();
            if (src.getId() != null) result.addProperty("id", src.getId());
            if (src.getFirstName() != null) result.addProperty("first_name", src.getFirstName());
            if (src.getLastName() != null) result.addProperty("last_name", src.getLastName());
            if (src.getUrl() != null) result.addProperty("url", src.getUrl());
            if (src.getEmail() != null) result.addProperty("email", src.getEmail());
            if (src.getPhone() != null) result.addProperty("phone", src.getPhone());
            if (src.getCountry() != null) result.addProperty("country", src.getCountry());
            if (src.getCity() != null) result.addProperty("city", src.getCity());
            if (src.getZipcode() != null) result.addProperty("zipcode", src.getZipcode());
            if (src.getStreet() != null) result.addProperty("street", src.getStreet());
            return null;
        }
    }

    private static class AddressDeserializer implements JsonDeserializer<Address> {
        @Override
        public Address deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Address result = new Address();
            if (json.isJsonObject()) {
                JsonObject jsonObject = json.getAsJsonObject();
                if (jsonObject.get("id") != null) result.setId(jsonObject.get("id").getAsLong());
                if (jsonObject.get("first_name") != null) result.setFirstName(jsonObject.get("first_name").getAsString());
                if (jsonObject.get("last_name") != null) result.setLastName(jsonObject.get("last_name").getAsString());
                if (jsonObject.get("url") != null) result.setUrl(jsonObject.get("url").getAsString());
                if (jsonObject.get("email") != null) result.setEmail(jsonObject.get("email").getAsString());
                if (jsonObject.get("phone") != null) result.setPhone(jsonObject.get("phone").getAsString());
                if (jsonObject.get("country") != null) result.setCountry(jsonObject.get("country").getAsString());
                if (jsonObject.get("city") != null) result.setCity(jsonObject.get("city").getAsString());
                if (jsonObject.get("zipcode") != null) result.setZipcode(jsonObject.get("zipcode").getAsString());
                if (jsonObject.get("street") != null) result.setStreet(jsonObject.get("street").getAsString());
            }
            return null;
        }
    }

    public String toJson() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Address.class, new AddressSerializer()).setPrettyPrinting().create();
        return gson.toJson(this, Address.class);
    }

    public static Address fromJson(JsonElement json) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Address.class, new AddressDeserializer()).setPrettyPrinting().create();
         return gson.fromJson(json, Address.class);
    }
}
