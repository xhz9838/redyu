package com.czxy.redyu.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2020/2/13
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TagWithPostCountDTO extends TagDTO {

    private Long postCount;
}
