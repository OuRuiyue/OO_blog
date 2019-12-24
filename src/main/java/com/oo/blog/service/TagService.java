package com.oo.blog.service;


import com.oo.blog.po.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {
    Tag saveTag(Tag tag);//新增
    Tag getTag(long id);//根据id查询
    Page<Tag> listTag(Pageable pageable); //分页查询
    Tag updateTag(long id, Tag tag);//修改
    void deleteTag(Long id);//删除
    Tag getTagByName(String name);
    List<Tag> listTag();

    List<Tag> listTag(String ids);
    List<Tag> listTagTop(Integer size);
}
