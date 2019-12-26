package com.oo.blog.web.admin;

import com.oo.blog.po.Blog;
import com.oo.blog.po.User;
import com.oo.blog.service.BlogService;
import com.oo.blog.service.TagService;
import com.oo.blog.service.TypeService;
import com.oo.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
//import java.awt.print.Pageable;

@Controller
@RequestMapping("/admin")
public class BlogController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    @GetMapping("/blogs")
    public String blogs(@PageableDefault(size = 3,sort ={"updateTime"},direction= Sort.Direction.DESC) Pageable pageable, BlogQuery blog, Model model){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("page",blogService.listBlog(pageable,blog));
        return "admin/blogs";
    }

    //ajax 只刷新blogList段
    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 3,sort ={"updateTime"},direction= Sort.Direction.DESC) Pageable pageable, BlogQuery blog, Model model){
        System.out.println(blog);
        model.addAttribute("page",blogService.listBlog(pageable,blog));
        return "admin/blogs :: blogList";
    }

    //进入新增界面
    @GetMapping("/blogs/input")
    public String blogInput(Model model){
        model.addAttribute("tags",tagService.listTag());
        model.addAttribute("types",typeService.listType());
        model.addAttribute("blog",new Blog());
        model.addAttribute("message",null);
        return "admin/blogs-input";
    }

    //修改
    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id,Model model){
        model.addAttribute("tags",tagService.listTag());
        model.addAttribute("types",typeService.listType());

        Blog blog=blogService.findBlog(id);
        blog.init();
        model.addAttribute("blog",blog);
        return "admin/blogs-input";
    }

    //删除
    @GetMapping("/blogs/{id}/delete")  //  admin/blogs/{id}/delete(id=${blog.id})
    public String deleteTags(@PathVariable long id,RedirectAttributes attributes){
        blogService.deleteBlog(id);
        if(blogService.findBlog(id)!=null){
            attributes.addFlashAttribute("message","删除失败");
        }else{
            attributes.addFlashAttribute("message","删除成功");
        }
        return "redirect:/admin/blogs";
    }

    //提交发布或保存
    @PostMapping("/blogs/submit")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session){
        System.out.println(blog);
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        //当博客为草稿时则将type、tag设为空，前台无法根据type、tag找到
        if(!blog.isPublished()){
            blog.setTagIds(null);
            blog.setTags(null);
            blog.setType(typeService.getType(0));
        }

        Blog b;
        if(blog.getId()==null){
            b=blogService.saveBlog(blog);
        }else{
            b=blogService.updateBlog(blog.getId(),blog);
        }

        if(b==null){
            attributes.addFlashAttribute("message","操作失败");
        }
        else{
            attributes.addFlashAttribute("message","操作成功");
        }
        return "redirect:/admin/blogs";
    }


}

