package com.czxy.redyu.exception;

import org.springframework.http.HttpStatus;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/18
 */
public class AuthenticationException extends RedyuException{


    public AuthenticationException(String message) {

        super(message);
    }


    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
