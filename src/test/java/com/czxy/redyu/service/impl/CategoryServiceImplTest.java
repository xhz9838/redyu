package com.czxy.redyu.service.impl;

import com.czxy.redyu.dao.CategoryMapper;
import com.czxy.redyu.model.dto.CategoryTreeDTO;
import com.czxy.redyu.model.entity.Category;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2020/4/4
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CategoryServiceImplTest {

    @Resource
    private CategoryMapper categoryMapper;
    @Test
    public void listAllTree() {
        long start = System.currentTimeMillis();
//        List<Category> categories = categoryMapper.selectAll();
//        ArrayList<CategoryTreeDTO> treeDTOS = new ArrayList<>();
//        List<CategoryTreeDTO> categoryTreeDTOS = categories.stream().map(this::convertToTree).collect(Collectors.toList());
//        HashMap<Integer, CategoryTreeDTO> hashMap = new HashMap<>();
//        for (CategoryTreeDTO categoryTreeDTO : categoryTreeDTOS) {
//            if(categoryTreeDTO.getParentId()==0){
//                treeDTOS.add(categoryTreeDTO);
//            }
//
//            hashMap.put(categoryTreeDTO.getId(),categoryTreeDTO);
//            CategoryTreeDTO parent = hashMap.get(categoryTreeDTO.getParentId());
//            if(parent!=null){
//                parent.getChildren().add(categoryTreeDTO);
//            }
//        }

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
            System.out.println(categoryTreeDTOList);
        }


        long end = System.currentTimeMillis();
        System.out.println(end-start);
    }
    public CategoryTreeDTO convertToTree(Category category) {

        Assert.notNull(category, "分类不能为null");

        return new CategoryTreeDTO().convertFrom(category);
    }
}