package com.czxy.redyu.model.dto;

import com.czxy.redyu.model.dto.base.OutputConverter;
import com.czxy.redyu.model.entity.Tag;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/24
 */
@Data
public class TagDTO implements OutputConverter<TagDTO, Tag> {

    private Integer id;

    private String name;

    private String slugName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createTime;
}
