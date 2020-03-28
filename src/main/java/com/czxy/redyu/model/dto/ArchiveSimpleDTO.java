package com.czxy.redyu.model.dto;

import com.czxy.redyu.model.entity.Post;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Base post detail output dto.
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class ArchiveSimpleDTO extends BasePostMinimalDTO {


    private String summary;
    private String thumbnail;
    private Integer commentCount;

    private Integer topPriority;
    private Long visits;
    @Override
    public  ArchiveSimpleDTO convertFrom(Post post) {
        commentCount=post.getComments().size();
        return super.convertFrom(post);
    }
}
