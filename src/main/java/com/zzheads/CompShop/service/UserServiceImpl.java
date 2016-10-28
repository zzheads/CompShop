package com.zzheads.CompShop.service;

import com.zzheads.CompShop.dao.UserDao;
import com.zzheads.CompShop.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<User> findAll() {
        return (List<User>) userDao.findAll();
    }

    @Override
    public User findById(Long id) {
        return userDao.findOne(id);
    }

    @Override
    public User save(User user) {
        return userDao.save(user);
    }

    @Override
    public void delete(User user) {
        userDao.delete(user);
    }

    @Override
    public List<String> getRegisteredUsernames() {
        List<String> result = new ArrayList<>();
        for (User user : findAll())
            result.add(user.getUsername());
        return result;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Load user from the database (throw exception if not found)
        User user = userDao.findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Return user object
        return user;
    }
}
