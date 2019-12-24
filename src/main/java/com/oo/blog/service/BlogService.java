package com.oo.blog.service;

import com.oo.blog.po.Blog;
import com.oo.blog.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BlogService {
    Blog findBlog(long id);
    Blog saveBlog(Blog blog);
    Blog updateBlog(long id, Blog blog);
    void deleteBlog(long id);

    Blog getAndConvert(long id);

    //后台查看博客列表
    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);

    Page<Blog> listBlog(Pageable pageable);

    Page<Blog> listBlogTag(Long tagId,Pageable pageable);//前端标签页查询博客列表
    Page<Blog> listBlogType(Long typeId,Pageable pageable);//前端分类页查询博客列表
    Page<Blog> listBlog(String query,Pageable pageable);//博客搜索分页
    List<Blog> listRecommendBlogTop(Integer size);//首页推荐博客

    Map<String,List<Blog>> archiveBlog();//博客归档
    Long countBlog();//博客条数
}
