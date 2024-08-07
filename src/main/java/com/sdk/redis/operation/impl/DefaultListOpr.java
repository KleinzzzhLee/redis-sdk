package com.sdk.redis.operation.impl;

import com.sdk.redis.exception.ParamException;
import com.sdk.redis.exception.code.ErrorCode;
import com.sdk.redis.exception.code.RedisErrorCode;
import com.sdk.redis.operation.ListOpr;

import java.util.*;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * list类型的默认实现
 * @author Lzzh
 * @version 1.0
 */
public class DefaultListOpr implements ListOpr {

    private final RedisTemplate<String,Object> redisTemplate;

    public DefaultListOpr(RedisTemplate<String, Object> redisTemplate)  {
        this.redisTemplate = redisTemplate;
    }
    @Override
    public <T> T index(String key, Long index, Class<T> clazz) {
        Object obj = redisTemplate.opsForList().index(key, index);
        return  clazz.cast(obj);
    }

    @Override
    public <T> long indexOf(String key, T value) {
        return redisTemplate.opsForList().indexOf(key,value);
    }

    @Override
    public <T> void lPush(String key, T value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    @Override
    public <T> void rPush(String key, T value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    @Override
    public <T> Long leftPushAll(String key, Collection<T> values) {
        if(values.isEmpty()) {
            return 0L;
        }
        return redisTemplate.opsForList().leftPushAll(key, values);
    }

    @Override
    public <T> Long rightPushAll(String key, Collection<T> values) {
        if(values.isEmpty()) {
            return 0L;
        }
        return redisTemplate.opsForList().rightPushAll(key, values);
    }

    @Override
    public  <T> Long batchLeftPush(String key, Collection<T> values) {
        if(values.isEmpty()) {
            return 0L;
        }
        return redisTemplate.execute(new SessionCallback<Long>() {
            Long temp = 0L;
            @Override
            public <K, V> Long execute(RedisOperations<K, V> operations) throws DataAccessException {
                ListOperations<String, Object> listOperations = redisTemplate.opsForList();
                for (T value : values) {
                    listOperations.leftPush(key, value);
                    temp++;
                }
                return temp;
            }
        });
    }
    @Override
    public  <T> Long batchRightPush(String key, Collection<T> values) {
        if(values.isEmpty()){
            return 0L;
        }
        return redisTemplate.execute(new SessionCallback<Long>() {
            Long temp = 0L;
            @Override
            public <K, V> Long execute(RedisOperations<K, V> operations) throws DataAccessException {
                ListOperations<String, Object> listOperations = redisTemplate.opsForList();
                for (T value : values) {
                    listOperations.rightPush(key, value);
                    temp++;
                }
                return temp;
            }
        });
    }


    @Override
    public <T> Long batchLeftPush(Map<String, T> map) {
        if(map.isEmpty()) {
            return 0L;
        }
        return redisTemplate.execute(new SessionCallback<Long>() {
            Long amount = 0L;
            @Override
            public <K, V> Long execute(RedisOperations<K, V> operations) throws DataAccessException {
                ListOperations<String, Object> listOperations = redisTemplate.opsForList();
                map.forEach((key, value) -> {
                    listOperations.leftPush(key, value);
                    amount++;

                });
                return amount;
            }
        });

    }

    @Override
    public <T> Long batchRightPush(Map<String, T> map) {
        if(map.isEmpty()) {
            return 0L;
        }
        return redisTemplate.execute(new SessionCallback<Long>() {
            Long amount = 0L;
            @Override
            public <K, V> Long execute(RedisOperations<K, V> operations) throws DataAccessException {
                ListOperations<String, Object> listOperations = redisTemplate.opsForList();
                map.forEach((key, value) -> {
                    listOperations.rightPush(key, value);
                    amount++;

                });
                return amount;
            }
        });
    }

    @Override
    public <T> Long batchLeftPush(Collection<T> list, Function<T, String> getKey, Function<T, Object> getValue) {
        if(list.isEmpty()) {
            return 0L;
        }
        return redisTemplate.execute(new SessionCallback<Long>() {
            Long amount = 0L;
            @Override
            public <K, V> Long execute(RedisOperations<K, V> operations) throws DataAccessException {
                ListOperations<String, Object> listOperations = redisTemplate.opsForList();
                list.forEach((obj) -> {
                    String key = getKey.apply(obj);
                    Object value = getValue.apply(obj);
                    listOperations.leftPush(key, value);
                    amount++;
                });
                return amount;
            }
        });

    }

    @Override
    public <T> Long batchRightPush(Collection<T> list, Function<T, String> getKey, Function<T, Object> getValue) {
        if(list.isEmpty()) {
            return 0L;
        }
        return redisTemplate.execute(new SessionCallback<Long>() {
            Long amount = 0L;
            @Override
            public <K, V> Long execute(RedisOperations<K, V> operations) throws DataAccessException {
                ListOperations<String, Object> listOperations = redisTemplate.opsForList();
                list.forEach((obj) -> {
                    String key = getKey.apply(obj);
                    Object value = getValue.apply(obj);
                    listOperations.rightPush(key, value);
                    amount++;
                });
                return amount;
            }
        });
    }

    @Override
    public <T> T lPop(String key, Class<T> clazz) {
        Object obj = redisTemplate.opsForList().leftPop(key);
        return clazz.cast(obj);
    }

    @Override
    public <T> T rPop(String key, Class<T> clazz) {
        Object obj = redisTemplate.opsForList().rightPop(key);
        return clazz.cast(obj);
    }

    @Override
    public <T> List<T> batchLeftPop(String key, Long amount, Class<T> clazz) {

        return redisTemplate.execute(new SessionCallback<List<T>>() {
            List<T> list = new ArrayList<>();
            @Override
            public <K, V> List<T> execute(RedisOperations<K, V> operations) throws DataAccessException {
                for(int i = 0; i < amount; i++) {
                    Object obj = redisTemplate.opsForList().leftPop(key);
                    if(obj == null) {
                        return list;
                    }
                    list.add(clazz.cast(obj));
                }
                return list;
            }
        });
    }

    @Override
    public <T> List<T> batchRightPop(String key, Long amount, Class<T> clazz) {
        return redisTemplate.execute(new SessionCallback<List<T>>() {
            List<T> list = new ArrayList<>();
            @Override
            public <K, V> List<T> execute(RedisOperations<K, V> operations) throws DataAccessException {
                for(int i = 0; i < amount; i++) {
                    Object obj = redisTemplate.opsForList().rightPop(key);
                    if(obj == null) {
                        return list;
                    }
                    list.add(clazz.cast(obj));
                }
                return list;
            }
        });
    }

    @Override
    public <T> List<T> range(String key, Long start, Long end, Class<T> clazz) {
        List<Object> res = redisTemplate.opsForList().range(key, start, end);
        if(res == null) {
            return Collections.emptyList();
        }
        return res.stream().map(clazz::cast).collect(Collectors.toList());
    }

    @Override
    public Long size(String key) {
        return redisTemplate.opsForList().size(key);
    }

    @Override
    public Boolean move(String sourceKey, String targetKey, boolean leftPop, boolean leftPush) {
         return  redisTemplate.execute(new SessionCallback<Boolean>() {
            @Override
            public <K, V> Boolean execute(RedisOperations<K, V> operations) throws DataAccessException {
                redisTemplate.multi();

                ListOperations<String, Object> listOperations = redisTemplate.opsForList();
                Object obj = leftPop ? listOperations.leftPop(sourceKey) : listOperations.rightPop(sourceKey);
                if(obj == null) {
                    return Boolean.FALSE;
                }
                Long size = leftPush ? listOperations.leftPush(targetKey, obj) : listOperations.rightPush(targetKey, obj);
                redisTemplate.exec();
                if (size == 0) {
                    return Boolean.FALSE;
                }
                return Boolean.TRUE;
            }
        });
    }

    @Override
    public Integer move(String sourceKey, String targetKey, Integer size, boolean leftPop, boolean leftPush) {
        return null;
    }
}
