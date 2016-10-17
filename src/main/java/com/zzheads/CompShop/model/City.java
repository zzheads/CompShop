package com.zzheads.CompShop.model;

import javax.persistence.*;
import java.util.*;

@Entity
public class City implements Comparable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @ElementCollection
    private List<String> pickupPoints;

    public City() {
        this.name = "";
        this.pickupPoints = new ArrayList<>();
    }

    public City(String name) {
        this.name = name;
        this.pickupPoints = new ArrayList<>();
    }

    public City(String name, List<String> pickupPoints) {
        this.name = name;
        this.pickupPoints = pickupPoints;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPickupPoints() {
        return pickupPoints;
    }

    public void setPickupPoints(List<String> pickupPoints) {
        this.pickupPoints = pickupPoints;
    }

    @Override
    public int compareTo(Object o) {
        return toString().compareTo(o.toString());
    }

    @Override
    public String toString() {
        return name;
    }
}
