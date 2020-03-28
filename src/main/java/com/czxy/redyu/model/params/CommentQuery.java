package com.czxy.redyu.model.params;

import lombok.Data;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2020/3/8
 */
@Data
public class CommentQuery {

    private Integer pageNum;

    private Integer pageSize;

    private String keyword;

    private Integer status;
}
