package com.czxy.redyu.model.vo;

import com.czxy.redyu.model.dto.BaseCommentDTO;
import com.czxy.redyu.model.entity.Comment;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * Comment including replied count.
 *
 * @author johnniang
 * @date 19-5-14
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CommentWithHasChildrenVO extends BaseCommentDTO {

    private boolean hasChildren;

    private List<Comment>  comments;
}
