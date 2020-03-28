package com.czxy.redyu.model.projection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2020/2/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryPostCountProjection {

    private Long postCount;

    private Integer categoryId;
}
