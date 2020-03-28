package com.czxy.redyu.model.params;

import lombok.Data;
import lombok.ToString;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2020/2/12
 */
@Data
@ToString
public class CategoryPageParam {

    private Integer pageNum;

    private Integer pageSize;
}
