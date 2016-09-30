package com.zzheads.CompShop.model;

import com.google.gson.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Product implements Serializable {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "product_id")
    private Long id;

    private String name;

    private String asin;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String description;

    private String photo;

    private double purchasePrice;

    private double retailPrice;

    private double quantity;

    private double height;
    private double length;
    private double width;
    private String unitsL;
    private double weight;
    private String unitsW;

    @ManyToOne
    @JoinColumn (name = "supplier_id")
    private Supplier supplier;

    @ManyToOne
    @JoinColumn (name = "category_id")
    private Category category;

    public Product() {
    }

    public Product(String name, String description, String photo, double purchasePrice, double retailPrice, double quantity, Supplier supplier, Category category) {
        this.name = name;
        this.description = description;
        this.photo = photo;
        this.purchasePrice = purchasePrice;
        this.retailPrice = retailPrice;
        this.quantity = quantity;
        this.supplier = supplier;
        this.category = category;
        this.asin = "";
    }

    public Product(String name, String asin, String description, String photo, double purchasePrice, double retailPrice, double quantity, Supplier supplier, Category category) {
        this.name = name;
        this.asin = asin;
        this.description = description;
        this.photo = photo;
        this.purchasePrice = purchasePrice;
        this.retailPrice = retailPrice;
        this.quantity = quantity;
        this.supplier = supplier;
        this.category = category;
    }

    public Product(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public String getUnitsL() {
        return unitsL;
    }

    public void setUnitsL(String unitsL) {
        this.unitsL = unitsL;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getUnitsW() {
        return unitsW;
    }

    public void setUnitsW(String unitsW) {
        this.unitsW = unitsW;
    }

    static JsonArray toJsonShort(List<Product> src) {
        if (src != null) {
            JsonArray products = new JsonArray();
            for (Product product : src) {
                JsonObject jsonP = new JsonObject();
                if (product.getId() != null) jsonP.addProperty("id", product.getId());
                if (product.getName() != null) jsonP.addProperty("name", product.getName());
                products.add(jsonP);
            }
            return products;
        }
        return null;
    }

    static List<Product> fromJsonShort(JsonElement products) {
        if (products != null && products.isJsonArray()) {
            List<Product> result = new ArrayList<>();
            JsonArray jsonArray = products.getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                result.add(new Product(jsonObject.get("id").getAsLong(), jsonObject.get("name").getAsString()));
            }
            return result;
        }
        return null;
    }

    static class ProductSerializer implements JsonSerializer <Product> {
        @Override
        public JsonElement serialize(Product src, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) {
            JsonObject result = new JsonObject();
            if (src.getId() != null) result.addProperty("id", src.getId());
            if (src.getName() != null) result.addProperty("name", src.getName());
            if (src.getAsin() != null) result.addProperty("asin", src.getAsin());
            if (src.getDescription() != null) result.addProperty("description", src.getDescription());
            if (src.getPhoto() != null) result.addProperty("photo", src.getPhoto());
            if (src.getPurchasePrice() != Double.NaN) result.addProperty("purchase_price", src.getPurchasePrice());
            if (src.getRetailPrice() != Double.NaN) result.addProperty("retail_price", src.getRetailPrice());
            if (src.getQuantity() != Double.NaN) result.addProperty("quantity", src.getQuantity());
            if (src.getHeight() != Double.NaN) result.addProperty("height", src.getHeight());
            if (src.getLength() != Double.NaN) result.addProperty("length", src.getLength());
            if (src.getWidth() != Double.NaN) result.addProperty("width", src.getWidth());
            if (src.getWeight() != Double.NaN) result.addProperty("weight", src.getWeight());
            if (src.getUnitsL() != null) result.addProperty("units_l", src.getUnitsL());
            if (src.getUnitsW() != null) result.addProperty("units_w", src.getUnitsW());
            if (src.getSupplier() != null) result.addProperty("supplier", src.getSupplier().toJson());
            if (src.getCategory() != null) result.addProperty("category", src.getCategory().toJson());

            return result;
        }
    }

    private static class ListProductSerializer implements JsonSerializer<List<Product>> {
        @Override
        public JsonElement serialize(List<Product> src, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) {
            ProductSerializer productSerializer = new ProductSerializer();
            if (src!=null && src.size()>0) {
                JsonArray result = new JsonArray();
                for (Product product : src) {
                    result.add(productSerializer.serialize(product, Product.class, context));
                }
                return result;
            }
            return null;
        }
    }

    public static class ProductDeserializer implements JsonDeserializer<Product> {
        @Override
        public Product deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonObject()) {
                JsonObject jsonObject = json.getAsJsonObject();
                Product result = new Product();
                if (jsonObject.get("id") != null) result.setId(jsonObject.get("id").getAsLong());
                if (jsonObject.get("name") != null) result.setName(jsonObject.get("name").getAsString());
                if (jsonObject.get("asin") != null) result.setAsin(jsonObject.get("asin").getAsString());
                if (jsonObject.get("description") != null) result.setDescription(jsonObject.get("description").getAsString());
                if (jsonObject.get("photo") != null) result.setPhoto(jsonObject.get("photo").getAsString());
                if (jsonObject.get("purchase_price") != null) result.setPurchasePrice(jsonObject.get("purchase_price").getAsDouble());
                if (jsonObject.get("retail_price") != null) result.setRetailPrice(jsonObject.get("retail_price").getAsDouble());
                if (jsonObject.get("quantity") != null) result.setQuantity(jsonObject.get("quantity").getAsDouble());
                if (jsonObject.get("height") != null) result.setHeight(jsonObject.get("height").getAsDouble());
                if (jsonObject.get("length") != null) result.setLength(jsonObject.get("length").getAsDouble());
                if (jsonObject.get("width") != null) result.setWidth(jsonObject.get("width").getAsDouble());
                if (jsonObject.get("weight") != null) result.setWeight(jsonObject.get("weight").getAsDouble());
                if (jsonObject.get("units_l") != null) result.setUnitsL(jsonObject.get("units_l").getAsString());
                if (jsonObject.get("units_w") != null) result.setUnitsW(jsonObject.get("units_w").getAsString());
                if (jsonObject.get("supplier") != null) result.setSupplier(Supplier.fromJson(jsonObject.get("supplier")));
                if (jsonObject.get("category") != null) result.setCategory(Category.fromJson(jsonObject.get("category")));
                return result;
            }
            return null;
        }
    }

    public static Product fromJson (String json) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Product.class, new ProductDeserializer()).create();
        return gson.fromJson(json, Product.class);
    }

    public String toJson() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Product.class, new ProductSerializer()).setPrettyPrinting().create();
        return gson.toJson(this, Product.class);
    }

    public static String toJson(List<Product> products) {
        Gson gson = new GsonBuilder().registerTypeAdapter(List.class, new ListProductSerializer()).setPrettyPrinting().create();
        return gson.toJson(products, List.class);
    }

    static Product fromJson(JsonElement jsonElement) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Product.class, new ProductDeserializer()).setPrettyPrinting().create();
        return gson.fromJson(jsonElement, Product.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (Double.compare(product.purchasePrice, purchasePrice) != 0) return false;
        if (Double.compare(product.retailPrice, retailPrice) != 0) return false;
        if (Double.compare(product.quantity, quantity) != 0) return false;
        if (id != null ? !id.equals(product.id) : product.id != null) return false;
        if (name != null ? !name.equals(product.name) : product.name != null) return false;
        if (asin != null ? !asin.equals(product.asin) : product.asin != null) return false;
        if (description != null ? !description.equals(product.description) : product.description != null) return false;
        return photo != null ? photo.equals(product.photo) : product.photo == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (asin != null ? asin.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (photo != null ? photo.hashCode() : 0);
        temp = Double.doubleToLongBits(purchasePrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(retailPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(quantity);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
