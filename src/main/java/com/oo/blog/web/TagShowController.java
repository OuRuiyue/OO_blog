package com.oo.blog.web;

import com.oo.blog.po.Tag;
import com.oo.blog.service.BlogService;
import com.oo.blog.service.TagService;
import com.oo.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class TagShowController {
    @Autowired
    private TagService tagService;
    @Autowired
    private BlogService blogService;

    @GetMapping("/tags/{id}")///tags/-1
    public String tags(@PageableDefault(size = 5,sort={"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                        @PathVariable Long id, Model model){
        List<Tag> tags=tagService.listTagTop(500);
        if(id==-1){
            id=tags.get(0).getId();
        }

        model.addAttribute("tags",tags);
        model.addAttribute("page",blogService.listBlogTag(id,pageable));
        model.addAttribute("activeTagId",id);//当前选中TagId
        return "tags";
    }


}
