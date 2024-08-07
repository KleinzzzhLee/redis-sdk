package com.sdk.redis.service;

import cn.hutool.core.util.BooleanUtil;
import com.sdk.redis.aop.annotation.ValidateParams;
import com.sdk.redis.operation.ListOpr;
import com.sdk.redis.operation.RedisOpr;
import com.sdk.redis.operation.StrOpr;
import com.sdk.redis.operation.impl.DefaultListOpr;
import com.sdk.redis.operation.impl.DefaultStrOpr;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * redis服务类
 *  // todo 进行参数检查, 异常抛出
 *  // todo 在 SessionCallback 的 execute 方法中，异常处理是关键。例如，可以捕捉并处理 DataAccessException，并将其转化为自定义异常，以便于调用者理解发生了什么问题。
 *  // todo 确保你使用 clazz.cast(obj) 时，obj 类型确实能被转换为 clazz 类型。如果类型不匹配，可能会引发 ClassCastException。
 * @author Lzzh
 * @version 1.0
 * @description: Redis服务类，注册为Service组件提供服务
 * @date 2024/8/4 21:56
 */
@Service
@Slf4j
public class  RedisServer implements RedisOpr {

    private RedisTemplate<String, Object> redisTemplate;

    private StrOpr strOpr;
    private ListOpr listOpr;
    @Autowired
    public RedisServer(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;

    }

    @PostConstruct
    private void init() {
        this.strOpr = new DefaultStrOpr(redisTemplate);
        this.listOpr = new DefaultListOpr(redisTemplate);
    }

    public StrOpr strOpr() {
        return strOpr;
    }

    public ListOpr listOpr() {
        return listOpr;
    }

    @Override
    public boolean has(String key) {
        return BooleanUtil.isTrue(redisTemplate.hasKey(key));
    }


    @Override
    public List<String> existingKeys(Collection<String> keys) {
        List<String> res = new ArrayList<>();
        redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                for (String key : keys) {
                    if(BooleanUtil.isTrue(redisTemplate.hasKey(key))) {
                        res.add(key);
                    }
                }
                return null;
            }
        });
        return res;
    }

    @Override
    public List<String> emptyKeys(Collection<String> keys) {
        List<String> res = new ArrayList<>();
        redisTemplate.execute(new SessionCallback<Object>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                for (String key : keys) {
                    if(BooleanUtil.isFalse(redisTemplate.hasKey(key))) {
                        res.add(key);
                    }
                }
                return null;
            }

        });
        return res;
    }

    @ValidateParams
    @Override
    public boolean delete(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    @Override
    public Long delete(Collection<String> keys) {
        return redisTemplate.execute(new SessionCallback<Long>() {
            Long count = 0L;

            @Override
            public <K, V> Long execute(RedisOperations<K, V> operations) throws DataAccessException {
                for (String key : keys) {
                    if (Boolean.TRUE.equals(redisTemplate.delete(key))) {
                        count++;
                    }
                }
                return count;
            }
        });
    }
    @Override
    public <T> Long delete(Collection<T> keys, Function<T, String> getKey) {
        return redisTemplate.execute(new SessionCallback<Long>() {
            Long count = 0L;
            @Override
            public <K, V> Long execute(RedisOperations<K, V> operations) throws DataAccessException {
                for (T data : keys) {
                    String key = getKey.apply(data);
                    if (Boolean.TRUE.equals(redisTemplate.delete(key))) {
                        count++;
                    }
                }
                return count;
            }
        });
    }

    @Override
    public boolean expire(String key, long time, TimeUnit timeUnit) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, time, timeUnit));
    }

    @Override
    public boolean expireAt(String key, Date date) {
        return Boolean.TRUE.equals(redisTemplate.expireAt(key, date));

    }

    @Override
    public boolean isExpireKey(String key) {
        return this.has(key);
    }
}
