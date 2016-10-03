package com.zzheads.CompShop.model;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(value = TemporalType.DATE)
    @DateTimeFormat (pattern = "dd/MM/yyyy")
    private final Date modified;

    private final String name;

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    private final List<String> pickup_points;

    public City() {
        this.modified = new Date();
        this.name = "";
        this.pickup_points = null;
    }

    public City(Long id, String name, List<String> pickup_points) {
        this.id = id;
        this.modified = new Date();
        this.name = name;
        this.pickup_points = pickup_points;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getModified() {
        return modified;
    }

    public String getName() {
        return name;
    }

    public List<String> getPickup_points() {
        return pickup_points;
    }
}
