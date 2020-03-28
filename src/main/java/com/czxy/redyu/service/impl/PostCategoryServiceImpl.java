package com.czxy.redyu.service.impl;

import com.czxy.redyu.dao.CategoryMapper;
import com.czxy.redyu.dao.PostCategoryMapper;
import com.czxy.redyu.model.entity.Category;
import com.czxy.redyu.model.entity.PostCategory;
import com.czxy.redyu.service.PostCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/10
 */
@Service
@Transactional
public class PostCategoryServiceImpl implements PostCategoryService {
    @Resource
    private PostCategoryMapper postCategoryMapper;
    @Resource
    private CategoryMapper categoryMapper;
    @Override
    public void removeByPostId(Integer id) {
        Example example = new Example(PostCategory.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("postId",id);
        postCategoryMapper.deleteByExample(example);
    }

    @Override
    public List<PostCategory> mergeOrCreateByIfAbsent(Integer postId, Set<Integer> categoryIds) {
        if (CollectionUtils.isEmpty(categoryIds)) {
            return Collections.emptyList();
        }
        return categoryIds.stream().map(categoryId -> {
            PostCategory postCategory = new PostCategory();
            postCategory.setPostId(postId);
            postCategory.setCategoryId(categoryId);
            postCategoryMapper.insert(postCategory);
            return postCategory;
        }).collect(Collectors.toList());


    }

    @Override
    public List<PostCategory> findPCSByCid(Integer categoryId) {
        Example example = new Example(PostCategory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("categoryId",categoryId);
        return postCategoryMapper.selectByExample(example);
    }

    @Override
    public List<Category> listCategoriesBy(Integer id) {
        Example example = new Example(PostCategory.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("postId",id);
        List<PostCategory> postCategories = postCategoryMapper.selectByExample(example);
        List<Category> categories = new ArrayList<>();
        for (PostCategory postCategory : postCategories) {
            Category category = categoryMapper.selectByPrimaryKey(postCategory.getCategoryId());
            categories.add(category);
        }
        return categories;
    }
}
