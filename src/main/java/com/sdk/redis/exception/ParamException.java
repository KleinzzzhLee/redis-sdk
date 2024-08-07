package com.sdk.redis.exception;

import com.sdk.redis.exception.code.ErrorCode;

/**
 * 参数异常
 * @author Lzzh
 * @version 1.0
 */
public class ParamException extends BaseException {
    public ParamException(int code) {
        super(code);
    }

    public ParamException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ParamException(int code, String msg) {
        super(code, msg);
    }

    public ParamException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public ParamException(int code, String msg, Throwable throwable) {
        super(code, msg, throwable);
    }

    public ParamException(ErrorCode errorCode, Throwable throwable) {
        super(errorCode, throwable);
    }
}
