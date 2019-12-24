package com.oo.blog.service.impl;


import com.oo.blog.NotFoundException;
import com.oo.blog.dao.TagDao;
import com.oo.blog.po.Tag;
import com.oo.blog.service.TagService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagDao tagDao;

    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        return tagDao.save(tag);
    }

    @Transactional
    @Override
    public Tag getTag(long id) {
        return tagDao.findById(id);
    }

    @Transactional
    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagDao.findAll(pageable);
    }

    @Transactional
    @Override
    public Tag updateTag(long id, Tag tag) {
        Tag t = tagDao.findById(id);
        if(t==null){
            throw new NotFoundException("找不到该标签");
        }
        //将tag值copy到t
        BeanUtils.copyProperties(tag,t);
        return tagDao.save(t);
    }

    @Transactional
    @Override
    public void deleteTag(Long id) {
        tagDao.deleteById(id);
    }

    @Transactional
    @Override
    public Tag getTagByName(String name) {
        return tagDao.findByName(name);
    }

    @Override
    public List<Tag> listTag() {
        return tagDao.findAll();
    }


     @Override
     public List<Tag> listTag(String ids) { //1,2,3
     return tagDao.findAllById(convertToList(ids));
     }

    @Override
    public List<Tag> listTagTop(Integer size) {
        Sort sort =Sort.by(Sort.Direction.DESC,"blogs.size");
        Pageable pageable=PageRequest.of(0,size,sort);
        return tagDao.findTop(pageable);
    }

    private List<Long> convertToList(String ids) {
        List<Long> list = new ArrayList<>();
        if (!"".equals(ids) && ids != null) {
            String[] idarray = ids.split(",");
            for (int i=0; i < idarray.length;i++) {
                list.add(new Long(idarray[i]));
            }
        }
        return list;
    }


}
