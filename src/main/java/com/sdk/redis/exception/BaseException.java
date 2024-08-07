package com.sdk.redis.exception;

import com.sdk.redis.exception.code.ErrorCode;
import org.apache.tomcat.util.codec.binary.BaseNCodec;

/**
 * 自定义异常基类
 *      其他自定义异常序继承此类
 * @author Lzzh
 * @version 1.0
 */
public class BaseException extends RuntimeException {
    /**
     * 错误码
     */
    int code;
    int getCode() {
        return this.code;
    }

    private BaseException() {}

    public BaseException(int code) {
        this.code = code;
    }

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BaseException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public BaseException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public BaseException(int code, String msg, Throwable throwable) {
        super(msg,throwable);
        this.code = code;
    }

    public BaseException(ErrorCode errorCode, Throwable throwable) {
        super(errorCode.getMessage(), throwable);
        this.code = errorCode.getCode();
    }


}
