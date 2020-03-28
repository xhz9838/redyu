package com.czxy.redyu.exception;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2020/1/15
 */
public class AlreadyExistsException extends BadRequestException{
    public AlreadyExistsException(String msg) {
        super(msg);
    }

    public AlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
