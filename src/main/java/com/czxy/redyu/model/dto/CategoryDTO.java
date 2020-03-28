package com.czxy.redyu.model.dto;

import com.czxy.redyu.model.dto.base.OutputConverter;
import com.czxy.redyu.model.entity.Category;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/24
 */
@Data
@ToString
@EqualsAndHashCode
public class CategoryDTO implements OutputConverter<CategoryDTO, Category> {

    private Integer id;

    private String name;

    private String slugName;

    private String description;

    private Integer parentId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;


}
