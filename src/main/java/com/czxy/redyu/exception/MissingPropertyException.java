package com.czxy.redyu.exception;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2020/2/22
 */
public class MissingPropertyException extends BadRequestException{
    public MissingPropertyException(String msg) {
        super(msg);
    }

    public MissingPropertyException(String message, Throwable cause) {
        super(message, cause);
    }
}
