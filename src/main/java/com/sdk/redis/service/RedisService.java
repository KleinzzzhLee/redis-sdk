package com.sdk.redis.service;

import com.sdk.redis.operation.DefaultRedisService;
import com.sdk.redis.operation.StrOpr;
import com.sdk.redis.operation.impl.DefaultStrOpr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Lzzh
 * @version 1.0
 * @description: Redis服务类，注册为Service组件提供服务
 * @date 2024/8/4 21:56
 */
@Service
public class RedisService implements DefaultRedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private StrOpr strOpr = new DefaultStrOpr();

    // todo 在此封装redis的基础操作
}
