package com.oo.blog.web;

import com.oo.blog.NotFoundException;
import com.oo.blog.aspect.LogAspect;
import com.oo.blog.service.BlogService;
import com.oo.blog.service.TagService;
import com.oo.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class indexController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private TagService tagService;
    @Autowired
    private TypeService typeService;

    @GetMapping("/")
    public String index(@PageableDefault(size = 8,sort={"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                        Model model){
        //博客列表
        model.addAttribute("page", blogService.listBlog(pageable));
        //分类列表
        model.addAttribute("types",typeService.listTypeTop(6));//只检索publiced为true博客，屏蔽typeId为0的博客
        //标签列表
        model.addAttribute("tags",tagService.listTagTop(10));//
        //推荐博客列表
        model.addAttribute("recommendBlogs",blogService.listRecommendBlogTop(5));
        return "index";
    }

    //搜索功能 query为搜索值
    @GetMapping("/search")
    public String search(@PageableDefault(size = 5,sort={"updateTime"},direction = Sort.Direction.DESC)Pageable pageable,
                         Model model,@RequestParam String query){
        model.addAttribute("pages",blogService.listBlog("%"+query+"%",pageable));
        model.addAttribute("query",query);
        return "search";
    }

    //博客详情页面
    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id,Model model){
        model.addAttribute("blog",blogService.getAndConvert(id));
        return "blog";
    }

    //页脚推荐博客
    @GetMapping("/footer/newblog")
    public String newblogs(Model model){
        model.addAttribute("newblogs",blogService.listRecommendBlogTop(3));
        return "_fragments::newblogList";
    }



}
