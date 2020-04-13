package com.czxy.redyu.model.support;

import lombok.Data;
import lombok.ToString;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2020/4/2
 */
@Data
@ToString
public class IpResult {

    private Integer status;
    private String message;
    private IpResultVo result;
}
