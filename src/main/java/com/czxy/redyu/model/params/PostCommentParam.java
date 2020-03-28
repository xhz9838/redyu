package com.czxy.redyu.model.params;

import com.czxy.redyu.model.entity.Comment;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * PostComment param.
 *
 * @author johnniang
 * @date 3/22/19
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PostCommentParam extends BaseCommentParam<Comment> {

}
