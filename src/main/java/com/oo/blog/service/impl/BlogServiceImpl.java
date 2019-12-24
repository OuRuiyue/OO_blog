package com.oo.blog.service.impl;

import com.oo.blog.NotFoundException;
import com.oo.blog.dao.BlogDao;
import com.oo.blog.po.Blog;
import com.oo.blog.po.Tag;
import com.oo.blog.po.Type;
import com.oo.blog.service.BlogService;
import com.oo.blog.util.MarkDownUtils;
import com.oo.blog.util.MyBeanUtils;
import com.oo.blog.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogDao blogDao;


    @Override
    public Blog findBlog(long id) {
        return blogDao.findById(id);
    }

    @Override
    public Blog getAndConvert(long id) {
        Blog blog =blogDao.findByIdAndPublishedTrue(id);
        if(blog==null){
            throw new NotFoundException("该博客不存在");
        }
        Blog b=new Blog();
        BeanUtils.copyProperties(blog,b);
        String content=b.getContent();
        b.setContent(MarkDownUtils.markdownToHtmlExtensions(content));
        blogDao.updateViews(id);
        return b;
    }


    //后台管理博客列表
    @Transactional
    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogDao.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates=new ArrayList<>();
                if(!"".equals(blog.getTitle())&&blog.getTitle()!=null){
                    predicates.add(cb.like(root.<String>get("title"),"%"+blog.getTitle()+"%"));
                }
                if (blog.getTypeId() != null){
                    predicates.add(cb.equal(root.<Type>get("type").get("id"),blog.getTypeId()));
                }
                if(blog.isRecommend()){
                    predicates.add(cb.equal(root.<Boolean>get("recommend"),blog.isRecommend()));
                }

                cq.where(predicates.toArray(new Predicate[predicates.size()]));

                return null;
            }
        },pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogDao.findAllPublished(pageable);
    }


    /*     前端标签查询博客列表    */
    @Override
    public Page<Blog> listBlogTag(Long tagId, Pageable pageable) {
        return blogDao.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates=new ArrayList<>();

                predicates.add(cb.equal(root.join("tags").get("id"),tagId));
                predicates.add(cb.equal(root.<Boolean>get("published"),true));

                cq.where(predicates.toArray(new Predicate[predicates.size()]));

                return null;
                /*Join join=root.join("tags");

                return cb.equal(join.get("id"),tagId);*/
            }
        },pageable);
    }

    /*     前端分类查询博客列表    */
    @Override
    public Page<Blog> listBlogType(Long typeId, Pageable pageable) {
        return blogDao.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates=new ArrayList<>();
                //都可
                //predicates.add(cb.equal(root.join("type").get("id"),typeId));
                predicates.add(cb.equal(root.<Type>get("type").get("id"),typeId));
                predicates.add(cb.equal(root.<Boolean>get("published"),true));
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    //搜索页面
    @Override
    public Page<Blog> listBlog(String query,Pageable pageable) {
        return blogDao.findByQuery(query,pageable);
    }

    //首页推荐博客排序
    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort=Sort.by(Sort.Direction.DESC,"updateTime");
        Pageable pageable= PageRequest.of(0,size,sort);
        return blogDao.findTop(pageable);
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years=blogDao.findGroupYear();
        Map<String,List<Blog>> map=new HashMap<>();
        for(String year:years){
            map.put(year,blogDao.findByYear(year));
        }
        return map;
    }

    //博客计数
    @Override
    public Long countBlog() {
        return blogDao.countBlog();
    }

    //保存博客，设置博客属性
    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if(blog.getId()==null){
            //保存初始化
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        }else {
            blog.setUpdateTime(new Date());
        }
        return blogDao.save(blog);
    }

    //更新博客
    @Transactional
    @Override
    public Blog updateBlog(long id, Blog blog) {
        Blog byId = blogDao.findById(id);
        if(byId==null){
            throw new NotFoundException("该博客不存在");
        }

        //通过MyBeanUtils.getNullPropertyNames(blog)方法过滤blog里空属性值，只将有值的属性进行更新
        BeanUtils.copyProperties(blog,byId, MyBeanUtils.getNullPropertyNames(blog));
        byId.setUpdateTime(new Date());

        return blogDao.save(byId);
    }

    //删除博客
    @Transactional
    @Override
    public void deleteBlog(long id) {
        blogDao.deleteById(id);
    }


}
