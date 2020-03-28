package com.czxy.redyu.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Base exception of the project.
 *
 * @author johnniang
 */
public abstract class RedyuException extends RuntimeException {

    /**
     * Error errorData.
     */
    private Object errorData;

    public RedyuException(String message) {
        super(message);
    }

    public RedyuException(String message, Throwable cause) {
        super(message, cause);
    }

    @NonNull
    public abstract HttpStatus getStatus();

    @Nullable
    public Object getErrorData() {
        return errorData;
    }

    /**
     * Sets error errorData.
     *
     * @param errorData error data
     * @return current exception.
     */
    @NonNull
    public RedyuException setErrorData(@Nullable Object errorData) {
        this.errorData = errorData;
        return this;
    }
}
