package com.oo.blog.dao;

import com.oo.blog.po.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagDao extends JpaRepository<Tag,Long> {

    Tag findById(long id);
    Tag findByName(String name);

    //select distinct t from Type t, Blog b where t.id=b.type.id and b.published=TRUE
    //SELECT t.* FROM t_tag t, t_blog b ,t_blog_tags s where b.published=TRUE AND t.id=s.tags_id AND b.id=s.blogs_id
    //@Query("select t from Tag t , Blog b  where b.published=TRUE and t.id=b.tags.id and b.id=t.blogs.id")

    @Query("SELECT t FROM Tag t")
    List<Tag> findTop(Pageable pageable);
}

