package com.czxy.redyu.model.dto;

import com.czxy.redyu.model.dto.base.OutputConverter;
import com.czxy.redyu.model.entity.Category;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2020/1/8
 */
@Data
@ToString
public class CategoryTreeDTO implements OutputConverter<CategoryTreeDTO, Category> {
    private Integer id;

    private String name;

     private Integer parentId;
    private List<CategoryTreeDTO> children = new ArrayList<>();
}
