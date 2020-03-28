package com.czxy.redyu.model.dto;

import com.czxy.redyu.model.dto.base.OutputConverter;
import com.czxy.redyu.model.entity.Post;
import com.czxy.redyu.model.enums.PostStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2020/1/31
 */

@Data
@ToString
@EqualsAndHashCode
public class BasePostMinimalDTO implements OutputConverter<BasePostMinimalDTO, Post> {

    private Integer id;

    private String title;

    private PostStatus status;

    private String url;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
