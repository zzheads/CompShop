package com.zzheads.CompShop.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
public class DollarRate {
    @Id
    @Temporal(value = TemporalType.DATE)
    @DateTimeFormat (pattern = "dd/MM/yyyy")
    private Date date;
    private double rate;

    public DollarRate() {
    }

    public DollarRate(Date date, double rate) {
        this.date = date;
        this.rate = rate;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
