package com.czxy.redyu.model.params;

import com.czxy.redyu.model.dto.base.InputConverter;
import com.czxy.redyu.model.entity.Category;
import com.czxy.redyu.utils.SlugUtils;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2020/1/15
 */
@Data
@ToString
public class CategoryParam implements InputConverter<Category> {

    private Integer id;
    /**
     * 分类名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 父分类id
     */

    private Integer parentId ;

    /**
     * 别称
     */
    private String slugName;

    @Override
    public Category convertTo() {
        slugName = StringUtils.isBlank(slugName) ? SlugUtils.slug(name) : SlugUtils.slug(slugName);

        return InputConverter.super.convertTo();
    }
}
