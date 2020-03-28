package com.czxy.redyu.exception;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2020/2/22
 */
public class FileOperationException extends ServiceException{

    public FileOperationException(String message) {
        super(message);
    }

    public FileOperationException(String message, Throwable cause) {
        super(message, cause);
    }

}
