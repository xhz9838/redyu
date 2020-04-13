package com.czxy.redyu.core;

import com.czxy.redyu.exception.AuthenticationException;
import com.czxy.redyu.exception.BadRequestException;
import com.czxy.redyu.model.vo.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/15
 */
@RestControllerAdvice
@Slf4j
public class ControllerHandlerException {

    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    //在这个方法里定义我们需要返回的格式
    public BaseResponse handleUserNotExistException(AuthenticationException ex){
        BaseResponse<?> response = BaseResponse.ok(ex.getMessage(),ex.getErrorData());

        response.setStatus(ex.getStatus().value());
        return response;
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    //在这个方法里定义我们需要返回的格式
    public BaseResponse Demo2(BadRequestException ex){
        BaseResponse<?> response = BaseResponse.ok(ex.getMessage(),ex.getErrorData());

        response.setStatus(ex.getStatus().value());
        return response;
    }
}
