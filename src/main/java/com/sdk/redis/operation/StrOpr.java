package com.sdk.redis.operation;


import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 关于String操作的接口
 * @author Lzzh
 * @date 2024/8/5
 */
public interface StrOpr {



    /**
     * 设置键值对
     * @param key 键
     * @param value 值
     */
    <T> void set(String key, T value);
    /**
     * 设置过期的键值对
     * @param key 键
     * @param value 值
     * @param time 过期时间
     * @param timeUnit 时间单位
     */
    <T> void set(String key, T value, long time, TimeUnit timeUnit);
    /**
     * 设置键值对，如果不存在的话
     * @param key 键
     * @param value 值
     * @return 设置成功？
     */
    <T> boolean setIfAbsent(String key, T value);
    /**
     * 设置过期键值，如果不存在的话
     * @param key 键
     * @param value 值
     * @param time 时间
     * @param timeUnit 时间单位
     * @return 设置成功？
     */
    <T> boolean setIfAbsent(String key, T value, long time, TimeUnit timeUnit);
    /**
     * 以对象某字段为key，整体为value
     * @param list 集合
     * @param getKey 获取key
     * @return key集
     */
    <T> Set<String> multiSet(Collection<T> list, Function<T, String> getKey);
    /**
     * 以对象某字段为key，某字段为value
     * @param list 集合
     * @param getKey 键
     * @param getValue 值
     * @return key集
     */
    <T> Set<String> multiSet(Collection<T> list, Function<T,String> getKey, Function<T,Object> getValue);
    /**
     * 根据k-v设置
     * @param map 键值对集合
     */
    <T> void multiSet(Map<String, ? extends T> map);


    /**
     * 获取value
     * @param key 键
     * @return 获取成功？
     */
    Object get(String key);
    /**
     * 获取到value
     * @param key 键
     * @param clazz value类型
     * @return 获取成功？
     */
    <T> T get(String key, Class<T> clazz);
    /**
     * 获取并删除
     * @param key key
     * @param clazz 类型
     * @return value
     */
    <T> T getAndDelete(String key, Class<T> clazz);
    /**
     * 赋新值
     * @param key key
     * @param value 原值
     * @param clazz Class类型
     * @return value
     */
    <T> T getAndSet(String key, T value, Class<T> clazz);

    /**
     * 获取values
     * @param keys key集合
     * @param clazz value的class
     * @return values
     * @param <T> value的类型
     */
    <T> List<T> getValueList(List<String> keys, Class<T> clazz);
    /**
     * 获取到全部value
     * @param keys key集合
     * @param clazz 类型
     * @return values
     */
    <T> Map<String,T> getKVMap(List<String> keys, Class<T> clazz );

    /**
     * 自增
     * @param key 键
     * @return 自增后的值
     */
    Long increment(String key);
    /**
     * 自增
     * @param key 键
     * @param delta 增量
     * @return 自增后的值
     */
    Long increment(String key, Long delta);
    /**
     * 自增
     * @param key 键
     * @param delta 增量
     * @return 自增后的值
     */
    Double increment(String key, Double delta);

    /**
     * 自减
     * @param key 键
     * @return 自减后的值
     */
    Long decrement(String key);
    /**
     * 自减
     * @param key 键
     * @param delta 减少量
     * @return 自减后的值
     */
    Long decrement(String key, Long delta);

}
