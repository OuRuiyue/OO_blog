package com.oo.blog.dao;

import com.oo.blog.po.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TypeDao extends JpaRepository<Type,Long> {
    Type findById(long id);
    Type findByIdAndAndName(long id, String name);
    Type findByName(String name);

     @Query("select distinct t from Type t, Blog b where t.id=b.type.id and b.published=TRUE")
     List<Type> findTop(Pageable pageable);

}

