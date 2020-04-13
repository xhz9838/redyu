package com.czxy.redyu.model.params;

import com.czxy.redyu.model.entity.Comment;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/10
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PostCommentParam extends BaseCommentParam<Comment> {

}
