package com.czxy.redyu.service;

import com.czxy.redyu.model.entity.Category;
import com.czxy.redyu.model.entity.PostCategory;

import java.util.List;
import java.util.Set;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/10
 */
public interface PostCategoryService {
    void removeByPostId(Integer id);

    List<PostCategory> mergeOrCreateByIfAbsent( Integer postId, Set<Integer> categoryIds);

    List<PostCategory> findPCSByCid(Integer categoryId);

    List<Category> listCategoriesBy(Integer id);
}
