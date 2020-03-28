package com.czxy.redyu.model.enums;

/**
 * 文章状态
 *
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/24
 */

public enum PostStatus implements ValueEnum<Integer> {
    /**
     * 发布
     */
    PUBLISHED(0),

    /**
     * 草稿
     */
    DRAFT(1),

    /**
     * 回收站
     */
    RECYCLE(2),

    /**
     * 私密
     */
    INTIMATE(3);


    private final Integer value;

    PostStatus(Integer value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }


}
