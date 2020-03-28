package com.czxy.redyu.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/11/14
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ResultEnum {

    LOGIN_ERROR(0,"用户名或密码错误，登录失败"),
    LOGIN_SUCCESS(1,"身份验证成功"),
    GET_PRI_KEY_ERROR(3,"私钥获取错误"),
    USER_ERROR(4,"账户状态异常")
    ;
    private Integer code;
    private String message;


}
