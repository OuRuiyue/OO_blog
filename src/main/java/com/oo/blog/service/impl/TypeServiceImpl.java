package com.oo.blog.service.impl;


import com.oo.blog.NotFoundException;
import com.oo.blog.dao.TypeDao;
import com.oo.blog.po.Type;
import com.oo.blog.service.TypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    TypeDao typeDao;

    @Transactional
    @Override
    public Type saveType(Type type) {
        return typeDao.save(type);
    }

    @Transactional
    @Override
    public Type getType(long id) {
        return typeDao.findById(id);
    }

    @Override
    public Page<Type> listType(Pageable pageable) {
        return typeDao.findAll(pageable);
    }

    @Transactional
    @Override
    public Type updateType(long id, Type type) {
        Type t = typeDao.findById(id);
        if(t==null){
            throw new NotFoundException("没找到给分类");
        }
        //将type值赋值到t中
        BeanUtils.copyProperties(type,t);
        return typeDao.save(t);

    }

    @Transactional
    @Override
    public void deleteType(Long id) {
        typeDao.deleteById(id);
    }

    @Transactional
    @Override
    public Type getTypeByName(String name) {
        return typeDao.findByName(name);
    }

    @Override
    public List<Type> listType() {
        return typeDao.findAll();
    }

    @Override
    public List<Type> listTypeTop(Integer size) {
        Sort sort=Sort.by(Sort.Direction.DESC,"blogs.size");
        Pageable pageable=PageRequest.of(0,size,sort);
        return typeDao.findTop(pageable);
    }


}
