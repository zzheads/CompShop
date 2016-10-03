package com.zzheads.CompShop.model;

import com.google.gson.Gson;

public class PickupRequest {
    private final String weight;
    private final String dimensions;
    private final String delivery_pickup;
    private final String insurance;
    private final String items_value;
    private final String units_length;
    private final String units_weight;

    public PickupRequest(String weight, String dimensions, String delivery_pickup, String insurance, String items_value, String units_length, String units_weight) {
        this.weight = weight;
        this.dimensions = dimensions;
        this.delivery_pickup = delivery_pickup;
        this.insurance = insurance;
        this.items_value = items_value;
        this.units_length = units_length;
        this.units_weight = units_weight;
    }

    public PickupRequest(String weight, String dimensions, String delivery_pickup, String insurance, String items_value) {
        this.weight = weight;
        this.dimensions = dimensions;
        this.delivery_pickup = delivery_pickup;
        this.insurance = insurance;
        this.items_value = items_value;
        this.units_weight = "kilograms";
        this.units_length = "centimeters";
    }

    public String getDimensions() {
        return dimensions;
    }

    public String getDimensionsInCm() {
        char[] dimSource = dimensions.toCharArray();
        String length="", height="", width="";
        String currentDim = "height";
        for (int i=0;i<dimSource.length;i++) {
            switch(dimSource[i]) {
                case 'x':
                    if (currentDim.equals("height")) {
                        currentDim = "length";
                        break;
                    }
                    if (currentDim.equals("length")) {
                        currentDim = "width";
                        break;
                    }
                    break;

                default:
                    switch (currentDim) {
                        case "length":
                            length+=dimSource[i];
                            break;
                        case "height":
                            height+=dimSource[i];
                            break;
                        case "width":
                            width+=dimSource[i];
                            break;
                    }
            }
        }
        return toCm(height, length, width, units_length);
    }

    public String getWeight() {
        return weight;
    }

    public String getWeightInKg() {
        return toKg(weight, units_weight);
    }

    public String getDelivery_pickup() {
        return delivery_pickup;
    }

    public String getInsurance() {
        return insurance;
    }

    public String getItems_value() {
        return items_value;
    }
    
    public String toJson () {
        Gson gson = new Gson();
        return gson.toJson(this, PickupRequest.class);
    }
    
    public static PickupRequest fromJson (String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, PickupRequest.class);
    }

    public static String toKg(String weight, String unitsW) {
        // weight in kg
        double multiplier = 1.0;
        switch (unitsW) {
            case "hundredths-pounds":
                multiplier = 0.01 * 0.453592;
                break;
            case "pounds":
                multiplier = 1.00 * 0.453592;
                break;
            case "kilograms": default:
                multiplier = 1.0;
                break;
        }
        String result = Double.toString(Double.parseDouble(weight)*multiplier);
        return result;
    }

    public static String toCm(String height, String length, String width, String unitsL) {
        // dimensions "HxLxW" in cm
        double multiplier = 1.0;
        switch (unitsL) {
            case "hundredths-inches":
                multiplier = 0.01 * 2.54;
                break;
            case "inches":
                multiplier = 1.00 * 2.54;
                break;
            case "centimeters" : default:
                multiplier = 1.0;
                break;
        }
        String result = Integer.toString((int) (Double.parseDouble(height)*multiplier)) +"x"+ Integer.toString((int) (Double.parseDouble(length)*multiplier)) +"x"+ Integer.toString((int) (Double.parseDouble(width)*multiplier));
        return result;
    }

}
