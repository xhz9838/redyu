package com.czxy.redyu.model.dto;

import com.czxy.redyu.model.entity.Category;
import com.czxy.redyu.model.entity.Post;
import com.czxy.redyu.model.entity.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2020/1/31
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class BasePostSimpleDTO extends BasePostMinimalDTO {

    private Long visits = 0L;

    private Integer topPriority = 0;

    /**
     * 文章概述
     */
    private String summary;

    private List<Category> categories;

    private List<Tag> tags;

    private Integer commentCount;

    @Override
    public <T extends BasePostMinimalDTO> T convertFrom(Post post) {
        commentCount=post.getComments().size();
        return super.convertFrom(post);
    }

}
