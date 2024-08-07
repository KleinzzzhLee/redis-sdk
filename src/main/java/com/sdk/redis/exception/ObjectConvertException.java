package com.sdk.redis.exception;

import com.sdk.redis.exception.code.ErrorCode;

/**
 * 类型转换异常
 *    在调用Class.cast()方法时可能抛出的异常
 * @author Lzzh
 * @version 1.0
 */
public class ObjectConvertException extends BaseException{
    public ObjectConvertException(int code) {
        super(code);
    }

    public ObjectConvertException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ObjectConvertException(int code, String msg) {
        super(code, msg);
    }

    public ObjectConvertException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public ObjectConvertException(int code, String msg, Throwable throwable) {
        super(code, msg, throwable);
    }

    public ObjectConvertException(ErrorCode errorCode, Throwable throwable) {
        super(errorCode, throwable);
    }
}
