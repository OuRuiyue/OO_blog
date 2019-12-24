package com.oo.blog.service;


import com.oo.blog.po.User;

public interface UserService {

    User checkUser(String username, String password);

}
