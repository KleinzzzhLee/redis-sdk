package com.sdk.redis.aop;

import cn.hutool.core.bean.BeanUtil;
import com.sdk.redis.aop.annotation.ValidateParams;
import com.sdk.redis.exception.ParamException;
import com.sdk.redis.exception.code.ErrorCode;
import com.sdk.redis.exception.code.RedisErrorCode;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * 异常切面类
 * @author Lzzh
 * @version 1.0
 */
@Aspect
@Component
public class ExceptionAspect {

    @Before("@annotation(validateParams)")
    public void validateParams(JoinPoint joinPoint, ValidateParams validateParams) {
        Object[] args = joinPoint.getArgs();
        for(Object object: args) {
            if(BeanUtil.isEmpty(object)) {
                throw new ParamException(RedisErrorCode.NULL_PARAMS_ERROR);
            }
        }
    }
}
