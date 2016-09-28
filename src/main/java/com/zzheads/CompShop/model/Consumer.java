package com.zzheads.CompShop.model;

import org.springframework.context.annotation.Scope;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
@Scope ("session")
public class Consumer {
    private User user;
    private Address address;

    public Consumer() {
    }

    public Consumer(User user, Address address) {
        this.user = user;
        this.address = address;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
