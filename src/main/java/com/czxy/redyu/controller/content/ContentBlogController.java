package com.czxy.redyu.controller.content;

import com.czxy.redyu.model.dto.BlogInformation;
import com.czxy.redyu.model.dto.CategoryWithPostCountDTO;
import com.czxy.redyu.model.dto.TagDTO;
import com.czxy.redyu.service.CategoryService;
import com.czxy.redyu.service.PostService;
import com.czxy.redyu.service.TagService;
import com.czxy.redyu.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2020/3/21
 */
@RestController
@RequestMapping("/content/blog")
public class ContentBlogController {

    @Resource
    private UserService userService;

    @Resource
    private TagService tagService;

    @Resource
    private CategoryService categoryService;

    @Resource
    private PostService postService;

    @GetMapping("/blogInformation")
    public BlogInformation blogInformation(){

        return userService.blogInformation();
    }

    @GetMapping("/tagList")
    public List<TagDTO> tagList(){
        return tagService.tagList();
    }

    @GetMapping("/history")
    public int[] history(){
        return postService.history();
    }

    @GetMapping("/categoryList")
    public List<CategoryWithPostCountDTO> categoryDTOList(){
        return categoryService.listAllWithPostCountDTO();
    }
}
