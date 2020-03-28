package com.czxy.redyu.model.params;

import com.czxy.redyu.model.dto.base.InputConverter;
import com.czxy.redyu.model.entity.Tag;
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
public class TagParam implements InputConverter<Tag> {

    private Integer id;

    private String name;

    private String  slugName;

    @Override
    public Tag convertTo() {
        slugName = StringUtils.isBlank(slugName) ? SlugUtils.slug(name):SlugUtils.slug(slugName);
        return InputConverter.super.convertTo();
    }
}
