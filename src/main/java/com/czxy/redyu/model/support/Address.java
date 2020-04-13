package com.czxy.redyu.model.support;

import lombok.Data;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2020/4/2
 */
@Data
public class Address {

    private String nation;

    private String province;

    private String city;

    private String district;

    private Integer adcode;
}
