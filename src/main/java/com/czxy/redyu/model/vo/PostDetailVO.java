package com.czxy.redyu.model.vo;

import com.czxy.redyu.model.dto.CategoryDTO;
import com.czxy.redyu.model.dto.TagDTO;
import com.czxy.redyu.model.dto.base.OutputConverter;
import com.czxy.redyu.model.entity.Post;
import com.czxy.redyu.model.enums.PostStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/24
 */
@Data
@ToString
@EqualsAndHashCode
public class PostDetailVO implements OutputConverter<PostDetailVO, Post> {

    private Integer id;

    private String title;

    private PostStatus status;

    private String url;

    private Date updateTime;

    private Date createTime;

    private String summary;

    private String thumbnail;

    private Long visits = 0L;

    private Integer topPriority = 0;

    private String originalContent;

    private String formatContent;

    private Set<Integer> tagIds;

    private List<TagDTO> tags;

    private Set<Integer> categoryIds;

    private List<CategoryDTO> categories;
}
