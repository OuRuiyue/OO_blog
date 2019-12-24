package com.oo.blog.service;

import com.oo.blog.po.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TypeService {
    Type saveType(Type type);//新增
    Type getType(long id);//根据id查询
    Page<Type> listType(Pageable pageable);   //分页查询
    Type updateType(long id, Type type);//修改
    void deleteType(Long id);//删除
    Type getTypeByName(String name);

    List<Type> listType();

    List<Type> listTypeTop(Integer size);

}
