package com.czxy.redyu.service.impl;

import com.czxy.redyu.dao.CategoryMapper;
import com.czxy.redyu.dao.PostCategoryMapper;
import com.czxy.redyu.exception.AlreadyExistsException;
import com.czxy.redyu.model.dto.CategoryDTO;
import com.czxy.redyu.model.dto.CategoryTreeDTO;
import com.czxy.redyu.model.dto.CategoryWithPostCountDTO;
import com.czxy.redyu.model.entity.Category;
import com.czxy.redyu.model.entity.PostCategory;
import com.czxy.redyu.model.params.CategoryPageParam;
import com.czxy.redyu.model.projection.CategoryPostCountProjection;
import com.czxy.redyu.service.CategoryService;
import com.czxy.redyu.service.PostCategoryService;
import com.czxy.redyu.utils.ServiceUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/10
 */
@Service
@Transactional
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private PostCategoryService postCategoryService;

    @Resource
    private PostCategoryMapper postCategoryMapper;

    @Override
    public List<CategoryTreeDTO> listAllTree() {
        // TODO 代码不优，存在隐患
        List<Category> categories = categoryMapper.findParent(0);
        if (!CollectionUtils.isEmpty(categories)) {
            List<CategoryTreeDTO> categoryTreeDTOList = categories.stream().map(this::convertToTree).collect(Collectors.toList());
            for (CategoryTreeDTO treeDTO : categoryTreeDTOList) {
                List<Category> parent = new ArrayList<>();
                parent = categoryMapper.findParent(treeDTO.getId());
                while (!CollectionUtils.isEmpty(parent)) {
                    List<CategoryTreeDTO> categoryTreeDTOS = parent.stream().map(this::convertToTree).collect(Collectors.toList());
                    treeDTO.setChildren(categoryTreeDTOS);

                    for (CategoryTreeDTO categoryTreeDTO : categoryTreeDTOS) {
                        parent = categoryMapper.findParent(categoryTreeDTO.getId());
//                        treeDTO = categoryTreeDTO;
                    }
                }
            }
            return categoryTreeDTOList;
        }
        return new ArrayList<>();
    }

    @Override
    public Category addCategory(Category category) {
        Assert.notNull(category, "分类不能为null");
        Example countExample = new Example(Category.class);
        Example.Criteria countCriteria = countExample.createCriteria();
        countCriteria.andEqualTo("name", category.getName());
        countCriteria.orEqualTo("slugName",category.getSlugName());
        int count = categoryMapper.selectCountByExample(countExample);
        if (count > 0) {
            log.error("分类已存在:[{}]", category);
            throw new AlreadyExistsException("该分类已存在");
        }

        if (ServiceUtils.isEmptyId(category.getParentId())) {
            category.setParentId(0);
        }
        categoryMapper.insertSelective(category);
        return categoryMapper.selectByPrimaryKey(category.getId());
    }

    @Override
    public CategoryDTO convertTo(Category category) {
        Assert.notNull(category, "Category must not be null");

        return new CategoryDTO().convertFrom(category);
    }

    @Override
    public List<CategoryDTO> convertTo(List<Category> categories) {
        if (CollectionUtils.isEmpty(categories)) {
            return Collections.emptyList();
        }

        return categories.stream()
                .map(this::convertTo)
                .collect(Collectors.toList());
    }

    @Override
    public List<Category> listAllByIds(Set<Integer> categoryIds) {
        List<Category> categories = new ArrayList<>();
        for (Integer categoryId : categoryIds) {

            categories.add(categoryMapper.selectByPrimaryKey(categoryId));
        }
        return categories;
    }

    @Override
    public List<CategoryDTO> listAll() {
        List<Category> categories = categoryMapper.selectAll();
        return categories.stream().map(this::convertTo).collect(Collectors.toList());
    }

    @Override
    public PageInfo<CategoryWithPostCountDTO> listAllForPage(CategoryPageParam categoryPageParam) {
        PageHelper.startPage(categoryPageParam.getPageNum(), categoryPageParam.getPageSize());
        List<Category> categories = categoryMapper.selectAll();
        PageInfo<Category> categoryPageInfo = new PageInfo<>(categories);
        Map<Integer, Long> categoryPostCountMap = ServiceUtils.convertToMap(postCategoryMapper.findPostCount(), CategoryPostCountProjection::getCategoryId, CategoryPostCountProjection::getPostCount);

        List<CategoryWithPostCountDTO> categoryWithPostCountDTOS = categories.stream()
                .map(category -> {
                    // Create category post count dto
                    CategoryWithPostCountDTO categoryWithPostCountDTO = new CategoryWithPostCountDTO().convertFrom(category);
                    // Set post count
                    categoryWithPostCountDTO.setPostCount(categoryPostCountMap.getOrDefault(category.getId(), 0L));
                    return categoryWithPostCountDTO;
                })
                .collect(Collectors.toList());
        PageInfo<CategoryWithPostCountDTO> categoryWithPostCountDTOPageInfo = new PageInfo<>();

        BeanUtils.copyProperties(categoryPageInfo, categoryWithPostCountDTOPageInfo);
        categoryWithPostCountDTOPageInfo.setList(categoryWithPostCountDTOS);
        return categoryWithPostCountDTOPageInfo;
    }

    @Override
    public Category updateCategory(Category category) {
        Assert.notNull(category, "分类不能为null");

        if (ServiceUtils.isEmptyId(category.getParentId())) {
            category.setParentId(0);
        }
        categoryMapper.updateByPrimaryKey(category);
        return categoryMapper.selectByPrimaryKey(category.getId());

    }

    @Override
    public List<Integer> findParentId(Integer id) {
        LinkedList<Integer> ids = new LinkedList<>();
        while (id != 0) {
            ids.addFirst(id);
            Category category = categoryMapper.selectByPrimaryKey(id);
            id = category.getParentId();
        }
        return ids;
    }

    @Override
    public void deleteCategory(Integer id) {
        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("parentId",id);
        List<Category> categories = categoryMapper.selectByExample(example);
        if (null != categories && categories.size() > 0) {
            categories.forEach(category -> {
                category.setParentId(0);
                categoryMapper.updateByPrimaryKey(category);
            });
        }
        categoryMapper.deleteByPrimaryKey(id);
        Example e2 = new Example(PostCategory.class);
        Example.Criteria c2 = e2.createCriteria();
        c2.andEqualTo("categoryId",id);
        postCategoryMapper.deleteByExample(e2);
    }

    @Override
    public List<CategoryWithPostCountDTO> listAllWithPostCountDTO() {
        List<Category> categories = categoryMapper.selectAll();

        Map<Integer, Long> categoryPostCountMap = ServiceUtils.convertToMap(postCategoryMapper.findPostCount(), CategoryPostCountProjection::getCategoryId, CategoryPostCountProjection::getPostCount);

        return categories.stream()
                .map(category -> {
                    // Create category post count dto
                    CategoryWithPostCountDTO categoryWithPostCountDTO = new CategoryWithPostCountDTO().convertFrom(category);
                    // Set post count
                    categoryWithPostCountDTO.setPostCount(categoryPostCountMap.getOrDefault(category.getId(), 0L));
                    return categoryWithPostCountDTO;
                })
                .collect(Collectors.toList());
    }


    public CategoryTreeDTO convertToTree(Category category) {

        Assert.notNull(category, "分类不能为null");

        return new CategoryTreeDTO().convertFrom(category);
    }


}
