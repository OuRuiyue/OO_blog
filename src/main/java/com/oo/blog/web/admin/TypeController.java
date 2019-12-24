package com.oo.blog.web.admin;

import com.oo.blog.po.Type;
import com.oo.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
@RequestMapping(value = "/admin")
public class TypeController {

    @Autowired
    private TypeService typeService;

    @GetMapping("/types")
    private String types(@PageableDefault(size = 5,sort = {"id"},direction = Sort.Direction.ASC) Pageable pageable,
                         Model model){
        model.addAttribute("page",typeService.listType(pageable));
        //分页查询
        //typeService.listType(pageable);
        return "/admin/types";
    }

    @GetMapping("/types/input")
    private String typeInput(Model model){
        model.addAttribute("type", new Type());
        return "/admin/types-input";
    }

    @PostMapping("/types/submit")/*使用RedirectAttributes attributes转发数据到另一页面*//*BindingResult result, */
    public String postTypes(@Valid Type type,BindingResult result,RedirectAttributes attributes, HttpServletResponse response){
        Type type2 = typeService.getTypeByName(type.getName());
        System.out.println(type2);
        if(type2!=null){
            result.rejectValue("name","nameError","该分类已存在，请重新添加");
        }
        if(result.hasErrors()){
            return "/admin/types-input";
        }
        Type type1=typeService.saveType(type);
        System.out.println(type1);
        if(type1==null){
            attributes.addFlashAttribute("message","添加分类失败");
        }else{
            attributes.addFlashAttribute("message","添加成功");
        }
        return "redirect:/admin/types";
    }

    //处理删除
    @GetMapping(value = "types/{id}/delete")
    public String deleteInput(@PathVariable long id,RedirectAttributes attributes){
        typeService.deleteType(id);
        if(typeService.getType(id)!=null){
            attributes.addFlashAttribute("message","删除失败");
        }else{
            attributes.addFlashAttribute("message","删除成功");
        }
        return "redirect:/admin/types";
    }

    //处理编辑
    @GetMapping("/types/{id}/input")
    public String editInput(Model model, @PathVariable long id){
        model.addAttribute("type",typeService.getType(id));
        return "/admin/types-input";
    }
    //*使用RedirectAttributes attributes转发数据到另一页面*//**//*BindingResult result, */
    @PostMapping("/types/{id}")
    public String editTypes(@Valid Type type,BindingResult result,@PathVariable long id, RedirectAttributes attributes){
        Type type2 = typeService.getTypeByName(type.getName());
        System.out.println(type2);
        if(type2!=null){
            result.rejectValue("name","nameError","该分类已存在，请重新修改");
        }
        if(result.hasErrors()){
            return "/admin/types-input";
        }
        Type type1=typeService.updateType(id,type);
        System.out.println(type1);
        if(type1==null){
            attributes.addFlashAttribute("message","修改分类失败");
        }else{
            attributes.addFlashAttribute("message","修改成功");
        }
        return "redirect:/admin/types";
    }



}
