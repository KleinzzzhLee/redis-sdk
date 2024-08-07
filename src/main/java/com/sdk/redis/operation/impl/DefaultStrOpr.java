package com.sdk.redis.operation.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.json.JSONUtil;
import com.sdk.redis.operation.StrOpr;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 *
 * todo 采用自定义注解+切面的形式进行参数检查
 * @author Lzzh
 * @date 2024/8/4
 */
public class DefaultStrOpr implements StrOpr {
    private final RedisTemplate<String, Object> redisTemplate;

    public DefaultStrOpr(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    /**
     * 设置键值对
     * @param key 键
     * @param value 值
     */
    @Override
    public  <T> void set(String key, T value){
        redisTemplate.opsForValue().set(key, value);
    }
    /**
     * 设置过期的键值对
     * @param key 键
     * @param value 值
     * @param time 过期时间
     * @param timeUnit 时间单位
     */
    @Override
    public  <T> void set(String key, T value, long time, TimeUnit timeUnit){
        redisTemplate.opsForValue().set(key,value,time,timeUnit);
    }
    /**
     * 设置键值对，如果不存在的话
     * @param key 键
     * @param value 值
     * @return 设置成功？
     */
    @Override
    public  <T> boolean setIfAbsent(String key, T value){
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, value));
    }
    /**
     * 设置过期键值，如果不存在的话
     * @param key 键
     * @param value 值
     * @param time 时间
     * @param timeUnit 时间单位
     * @return 设置成功
     */
    @Override
    public <T> boolean setIfAbsent(String key, T value, long time, TimeUnit timeUnit){
       return BooleanUtil.isTrue(redisTemplate.opsForValue().setIfAbsent(key, value, time, timeUnit));
    }
    /**
     * 以对象某字段为key，整体为value
     * @param list 集合
     * @param getKey get函数，获取到Key
     * @return key集
     */
    @Override
    public <T> Set<String> multiSet(Collection<T> list, Function<T, String> getKey){
        Set<String> keys = new HashSet<>();
        redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                ValueOperations<String, Object> strOperations = redisTemplate.opsForValue();
                for (T t : list) {
                    String key = getKey.apply(t);
                    Boolean absent = strOperations.setIfAbsent(key, t);
                    if(BooleanUtil.isTrue(absent)) {
                        keys.add(key);
                    }
                }
                return null;
            }
        });
        return keys;
    }
    /**
     * 以对象某字段为key，某字段为value
     * @param list 集合
     * @param getKey 键
     * @param getValue 值
     * @return key集
     */
    @Override
    public <T>Set<String> multiSet(Collection<T> list,
                                Function<T,String> getKey,
                                Function<T,Object> getValue){
        Set<String> keys = new HashSet<>();
        redisTemplate.executePipelined(new RedisCallback<Object>() {
            final Set<String> set = new HashSet<>();
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                ValueOperations<String, Object> operations = redisTemplate.opsForValue();
                list.forEach((t -> {
                    String key = String.valueOf(getKey.apply(t));
                    Object value = getValue.apply(t);
                    if(Boolean.TRUE.equals(operations.setIfAbsent(key, value))) {
                        set.add(key);
                    }

                }));
                return null;
            }
        });
        return keys;
    }
    /**
     * 根据k-v设置
     * @param map 键值对集合
     */
    @Override
    public <T> void multiSet(Map<String, ? extends T> map){
        redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                ValueOperations<String, Object> opr = redisTemplate.opsForValue();
                map.forEach((key,value)->
                        opr.set(key,JSONUtil.toJsonStr(value)));
                return null;
            }
        });
    }

    /**
     * 获取value
     * @param key 键
     * @return value
     */
    @Override
    public Object get(String key){
        Object object = redisTemplate.opsForValue().get(key);
        if(object == null ) {
            // todo 异常处理
        }
        return object;
    }
    /**
     * 获取到value
     * @param key 键
     * @param clazz value类型
     * @return value
     */
    @Override
    public <T> T get(String key, Class<T> clazz){
        Object obj = redisTemplate.opsForValue().get(key);
        return clazz.cast(obj);
    }
    /**
     * 获取并删除
     * @param key key
     * @return 原值
     */
    @Override
    public <T> T getAndDelete(String key, Class<T> clazz){
        Object object = redisTemplate.opsForValue().getAndDelete(key);
        return clazz.cast(object);
    }
    /**
     * 赋新值
     * @param key key
     * @param value 原值
     * @return 原值
     */
    @Override
    public <T> T getAndSet(String key, T value, Class<T> clazz){
        Object obj = redisTemplate.opsForValue().getAndSet(key, value);
        return clazz.cast(obj);
    }

    /**
     * 获取全部value
     * @param keys key集合
     * @return value集合
     */
    @Override
    public <T> List<T> getValueList(List<String> keys, Class<T> clazz){
        List<Object> objects = redisTemplate.opsForValue().multiGet(keys);
        return objects.stream().map((obj) -> clazz.cast(obj)).collect(Collectors.toList());
    }


    /**
     * 获取到全部value
     * @param keys key集合
     * @param clazz 类型
     * @return values
     */
    @Override
    public <T> Map<String, T> getKVMap(List<String> keys, Class<T> clazz){
        Map<String, T> map = new HashMap<>();
        redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                ValueOperations<String, Object> opr = redisTemplate.opsForValue();

                for (String key : keys) {
                    Object valueObj = opr.get(key);
                    if(!BeanUtil.isEmpty(valueObj)) {
                        map.put(key, clazz.cast(valueObj));
                    }
                }
                return null;
            }
        });
        return map;
    }

    /**
     * 自增
     * @param key 键
     * @return 自增后的值
     */
    @Override
    public Long increment(String key){
        return redisTemplate.opsForValue().increment(key);
    }
    /**
     * 自增
     * @param key 键
     * @param delta 增量
     * @return 自增后的值
     */
    @Override
    public Long increment(String key, Long delta){
        return redisTemplate.opsForValue().increment(key,delta);
    }
    /**
     * 自增
     * @param key 键
     * @param delta 增量
     * @return 自增后的值
     */
    @Override
    public Double increment(String key, Double delta){
        return redisTemplate.opsForValue().increment(key,delta);
    }

    /**
     * 自减
     * @param key 键
     * @return 自减后的值
     */
    @Override
    public Long decrement(String key){
        return redisTemplate.opsForValue().decrement(key);
    }
    /**
     * 自减
     * @param key 键
     * @return 自减后的值
     */
    @Override
    public Long decrement(String key, Long delta){
        return redisTemplate.opsForValue().decrement(key, delta);
    }
}
