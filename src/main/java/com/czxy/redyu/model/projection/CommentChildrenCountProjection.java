package com.czxy.redyu.model.projection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Comment children count projection.
 *
 * @author johnniang
 * @date 19-5-14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentChildrenCountProjection {

    private Long directChildrenCount;

    private Long commentId;
}
