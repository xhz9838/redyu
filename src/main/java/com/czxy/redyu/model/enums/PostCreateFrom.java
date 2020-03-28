package com.czxy.redyu.model.enums;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2020/1/31
 */
public enum PostCreateFrom implements ValueEnum<Integer> {

    /**
     * 发布来源：管理后台
     */
    ADMIN(0),

    /**
     * 发布来源：微信
     */
    WECHAT(1);

    private final Integer value;

    PostCreateFrom(Integer value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
