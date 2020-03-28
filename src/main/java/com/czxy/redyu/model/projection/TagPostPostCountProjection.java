package com.czxy.redyu.model.projection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2020/2/13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagPostPostCountProjection {

    /**
     * Post count.
     */
    private Long postCount;

    /**
     * Tag id
     */
    private Integer tagId;

}
