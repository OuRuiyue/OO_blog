package com.oo.blog.service.impl;

import com.oo.blog.dao.UserDao;
import com.oo.blog.po.User;
import com.oo.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public User checkUser(String username, String password) {
        User byUsernameAndPassword = userDao.findByUsernameAndPassword(username, password);
        return userDao.findByUsernameAndPassword(username,password);
    }
}
