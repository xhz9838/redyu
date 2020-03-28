package com.czxy.redyu.model.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/20
 */
@Data
@ToString
public class ErrorData {

    private String errorId;
    private String errorTime;
    private String ErrorUser;
}
