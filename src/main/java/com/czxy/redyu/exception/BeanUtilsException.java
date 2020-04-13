package com.czxy.redyu.exception;

import org.springframework.http.HttpStatus;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/10
 */
public class BeanUtilsException extends RedyuException {

    public BeanUtilsException(String message) {
        super(message);
    }

    public BeanUtilsException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
