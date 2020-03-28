package com.czxy.redyu.service;

import com.czxy.redyu.model.dto.CategoryDTO;
import com.czxy.redyu.model.dto.CategoryTreeDTO;
import com.czxy.redyu.model.dto.CategoryWithPostCountDTO;
import com.czxy.redyu.model.entity.Category;
import com.czxy.redyu.model.params.CategoryPageParam;
import com.github.pagehelper.PageInfo;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Set;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/10
 */
public interface CategoryService {


    List<CategoryTreeDTO> listAllTree();

    Category addCategory(Category category);

    CategoryDTO convertTo(Category category);
    @NonNull
    List<CategoryDTO> convertTo(@Nullable List<Category> categories);

    List<Category> listAllByIds(Set<Integer> categoryIds);

    List<CategoryDTO> listAll();

    PageInfo<CategoryWithPostCountDTO> listAllForPage(CategoryPageParam categoryPageParam);

    Category updateCategory(Category category);

    List<Integer> findParentId(Integer id);

    void deleteCategory(Integer id);


    List<CategoryWithPostCountDTO> listAllWithPostCountDTO();
}
