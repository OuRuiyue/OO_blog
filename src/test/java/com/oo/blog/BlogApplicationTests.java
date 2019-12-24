package com.oo.blog;

import com.oo.blog.dao.TypeDao;
import com.oo.blog.po.*;
import com.oo.blog.service.BlogService;
import com.oo.blog.service.TypeService;
import com.oo.blog.util.MD5Utils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class BlogApplicationTests {
    @Autowired
    private TypeDao typeDao;
    @Autowired
    private TypeService typeService;
    @Autowired
    private BlogService blogService;

    @Test
    void contextLoads() {
        Type byId = typeDao.findById(2);
        System.out.println(byId);
    }

    @Test
    void Test1(){
        Type type2 = typeService.getTypeByName("1213");
        //Type type2 = typeDao.findByName("1213");
        System.out.println(type2);
    }

    @Test
    void Test2(){
        System.out.println(MD5Utils.code("1594636525"));
    }

}
