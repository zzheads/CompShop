package com.zzheads.CompShop.service;

import com.zzheads.CompShop.model.Role;

import java.util.List;

public interface RoleService {
    Role findById(Long id);
    Role findByName(String name);
    List<Role> findAll();
    void save(Role role);
    void delete(Role role);
}
