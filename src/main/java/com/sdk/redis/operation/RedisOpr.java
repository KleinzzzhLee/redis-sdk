package com.sdk.redis.operation;


import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 定义redis的直接接口
 * @author Lzzh
 */
public interface RedisOpr {
    /**
     * 查看当前键是否存在
     * @param key 键名
     * @return ture 存在
     */
    boolean has(String key);

    /**
     * 存在的键
     * @param keys keys集合，限定包装类
     * @return 存在value的key
     */
    List<String> existingKeys(Collection<String> keys);

    /**
     * 空的键
     * @param keys keys集合，限定包装类
     * @return 不存在value的key
     */
    List<String> emptyKeys(Collection<String> keys);


    /**
     * 删除键值对
     * @param key 键
     * @return 删除成功
     */
    boolean delete(String key);

    /**
     * 删除键
     *
     * @param keys 键集合
     * @return 返回成功的数量
     */
    Long delete(Collection<String> keys);
    /**
     * 删除键
     *
     * @param keys   键集合
     * @param getKey 获取到key的方法
     * @param <T>    集合元素类型
     * @return 返回成功的数量
     */
    <T> Long delete(Collection<T> keys, Function<T, String> getKey);
    /**
     * 设置过期时间
     * @param key 键
     * @param time 时间长度
     * @param timeUnit 时间单位
     * @return 成功？
     */
    boolean expire(String key, long time, TimeUnit timeUnit);

    /**
     * 设置过期点
     * @param key key
     * @param date 日期
     * @return 成功？
     */
    boolean expireAt(String key, Date date);

    /**
     * 查看键已过期
     * @param key 键
     * @return 是否过期
     */
    boolean isExpireKey(String key);


}
