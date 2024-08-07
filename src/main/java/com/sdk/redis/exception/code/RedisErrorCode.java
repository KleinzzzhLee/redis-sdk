package com.sdk.redis.exception.code;

/**
 * redis服务的异常枚举
 * @author Lzzh
 * @version 1.0
 * @description: 用来定义redis服务内部出现的异常情况
 * @date 2024/8/4 21:55
 */
public enum RedisErrorCode implements ErrorCode {
    /**
     * 传入参数为空异常
     */
    NULL_PARAMS_ERROR(101, "参数为空异常"),
    /**
     * 类型不匹配异常
     */
    GENERICITY_NOT_MATCH_ERROR(102,"Class与value不匹配"),
    /**
     *
     */
    TYPE_TRANSFER_ERROR(103,"");
    int code;
    String message;

    private RedisErrorCode(int code, String msg) {
        this.code = code;
        this.message = msg;
    }
    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
