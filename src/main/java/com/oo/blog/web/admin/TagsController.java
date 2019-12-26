package com.oo.blog.web.admin;

import com.oo.blog.po.Tag;
import com.oo.blog.service.TagService;
import com.oo.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TagsController {

    @Autowired
    private TagService tagService;

    //列表页，注解默认列表分页
    @GetMapping("/tags")
    public String Tags(@PageableDefault(size = 5,sort = {"id"},direction = Sort.Direction.ASC)Pageable pageable,
                       Model model){
        model.addAttribute("page",tagService.listTag(pageable));
        return "admin/tags";
    }

    //新增页
    @GetMapping("/tags/input")
    public String TagsInput(Model model){
        model.addAttribute("tag",new Tag());
        return "admin/tags-input";
    }
    //新增提交
    @PostMapping("/tags/submit")
    public String PostTags(@Valid Tag tag, BindingResult result, RedirectAttributes attributes){
        Tag tag2 = tagService.getTagByName(tag.getName());
        if(tag2!=null){
            result.rejectValue("name","nameError","该标签已存在，请重新添加");
        }
        if(result.hasErrors()){
            return "admin/tags-input";
        }
        //System.out.println(tag);
        Tag tag1 = tagService.saveTag(tag);
        if(tag1==null){
            attributes.addFlashAttribute("message","添加标签失败");
        }
        else{
            attributes.addFlashAttribute("message","添加成功");
        }
        return "redirect:/admin/tags";
    }

    ///tags/1/input
    @GetMapping("/tags/{id}/input")
    public String editInput(Model model, @PathVariable long id){
        //传入tag类型到前端
        model.addAttribute("tag",tagService.getTag(id));
        return "admin/tags-input";
    }
    @PostMapping("/tags/{id}")
    public String editTags(@Valid Tag tag, BindingResult result,@PathVariable long id, RedirectAttributes attributes){
        Tag tag2 = tagService.getTagByName(tag.getName());
        if(tag2!=null){
            result.rejectValue("name","nameError","该标签已存在，请重新修改");
        }
        if(result.hasErrors()){
            return "admin/tags-input";
        }

        Tag tag1 = tagService.updateTag(id,tag);
        if(tag1==null){
            attributes.addFlashAttribute("message","修改标签失败");
        }
        else{
            attributes.addFlashAttribute("message","修改成功");
        }
        return "redirect:/admin/tags";
    }

    ///admin/tags/{id}/delete(id=${tag.id})
    @GetMapping("/tags/{id}/delete")
    public String deleteTags(@PathVariable long id,RedirectAttributes attributes){
        tagService.deleteTag(id);
        if(tagService.getTag(id)!=null){
            attributes.addFlashAttribute("message","删除失败");
        }else{
            attributes.addFlashAttribute("message","删除成功");
        }
        return "redirect:/admin/tags";
    }

}
