package com.czxy.redyu.model.params;

import lombok.Data;
import lombok.ToString;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/11
 */
@Data
@ToString
public class LoginParam {

    private String username;

    private String password;
}
