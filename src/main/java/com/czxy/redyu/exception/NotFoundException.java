package com.czxy.redyu.exception;

import org.springframework.http.HttpStatus;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/19
 */
public class NotFoundException extends RedyuException{
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
