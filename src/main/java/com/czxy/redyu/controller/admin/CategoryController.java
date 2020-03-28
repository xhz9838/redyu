package com.czxy.redyu.controller.admin;

import com.czxy.redyu.model.dto.CategoryDTO;
import com.czxy.redyu.model.dto.CategoryTreeDTO;
import com.czxy.redyu.model.dto.CategoryWithPostCountDTO;
import com.czxy.redyu.model.entity.Category;
import com.czxy.redyu.model.params.CategoryPageParam;
import com.czxy.redyu.model.params.CategoryParam;
import com.czxy.redyu.service.CategoryService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/10
 */
@RestController
@RequestMapping("/admin/category")
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    @GetMapping("/listAllTree")
    public List<CategoryTreeDTO> listAllTree(){
      return categoryService.listAllTree();
    }


    @GetMapping
    public List<CategoryDTO> listAll(){
        return categoryService.listAll();
    }
     @GetMapping("/listAllForPage")
    public PageInfo<CategoryWithPostCountDTO> listAllForPage(CategoryPageParam categoryPageParam){
        return categoryService.listAllForPage(categoryPageParam);
    }

    @PostMapping("/addCategory")
    public CategoryDTO addCategory(@RequestBody CategoryParam categoryParam){
        Category category = categoryParam.convertTo();
        return categoryService.convertTo( categoryService.addCategory(category));

    }
    @PutMapping("/updateCategory")
    public CategoryDTO updateCategory(@RequestBody CategoryParam categoryParam){
        Category category = categoryParam.convertTo();
        return categoryService.convertTo( categoryService.updateCategory(category));
    }
    @GetMapping("/findParentId/{id}")
    public List<Integer> findParentId(@PathVariable Integer id){
        return categoryService.findParentId(id);
    }
    @DeleteMapping("/deleteCategory/{id}")
    public void deleteCategory(@PathVariable Integer id){
         categoryService.deleteCategory(id);
    }
}
