package com.zzheads.CompShop.service;

import com.zzheads.CompShop.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> findAll();
    User findById(Long id);
    User save(User user);
    void delete(User user);
    List<String> getRegisteredUsernames();
    User findByName(String name);
}
