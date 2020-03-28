package com.czxy.redyu.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2020/3/23
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class BaseCommentWithParentVO extends BaseCommentDTO implements Cloneable {

    /**
     * Parent comment.
     */
    private BaseCommentWithParentVO parent;

    @Override
    public BaseCommentWithParentVO clone() {
        try {
            return (BaseCommentWithParentVO) super.clone();
        } catch (CloneNotSupportedException e) {
            log.error("Clone not support exception", e);
            return null;
        }
    }
}