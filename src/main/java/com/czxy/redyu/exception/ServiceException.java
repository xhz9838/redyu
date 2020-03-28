package com.czxy.redyu.exception;

import org.springframework.http.HttpStatus;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/15
 */
public class ServiceException extends RedyuException{

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
