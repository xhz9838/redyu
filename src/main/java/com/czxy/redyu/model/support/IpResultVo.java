package com.czxy.redyu.model.support;

import lombok.Data;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2020/4/2
 */
@Data
public class IpResultVo {

    private String ip;
    private Object location;
    private Address ad_info;
}
