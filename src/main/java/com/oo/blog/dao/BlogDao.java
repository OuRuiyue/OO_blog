package com.oo.blog.dao;


import com.oo.blog.po.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BlogDao extends JpaRepository<Blog,Long>, JpaSpecificationExecutor<Blog> {

    Blog findById(long id);

    Blog findByIdAndPublishedTrue(long id);

    @Query("select b from Blog b where b.recommend=true and b.published=true")
    List<Blog> findTop(Pageable pageable);

    @Query("select b from Blog b where b.published=true")
    Page<Blog> findAllPublished(Pageable pageable);

    @Query("select b from Blog b where b.published=TRUE and b.title like ?1 or b.content like ?1")
    Page<Blog> findByQuery(String query,Pageable pageable);

    //博客归档页
    @Query("select function('date_format',b.updateTime,'%Y') as year from Blog b where b.published=true group by function('date_format',b.updateTime,'%Y') order by year desc")
    List<String> findGroupYear();
    @Query("select  b from Blog b where function('date_format',b.updateTime,'%Y') = ?1 and b.published=true")
    List<Blog> findByYear(String year);
    @Query("select count(title) from Blog b where b.published=true")
    long countBlog();//统计博客数


    @Transactional
    @Modifying //非查询需加Modiing注解
    @Query("update Blog b set b.views=b.views+1 where b.id=?1")
    int updateViews(long id);

}
