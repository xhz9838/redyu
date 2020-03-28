package com.czxy.redyu.exception;

import org.springframework.http.HttpStatus;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/14
 */
public class BadRequestException extends RedyuException{


    public BadRequestException(String msg) {
        super(msg);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
