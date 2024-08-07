package com.sdk.redis.operation;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 操作list类型的接口
 * @author Lzzh
 */
public interface ListOpr {


    /**
     * 获取对应索引的value
     * @param key 键
     * @param index 位置
     * @param clazz 类型
     * @return value
     * @param <T> value类型
     */
    <T> T index(String key,  Long index, Class<T> clazz);

    /**
     * 获取所在list的位置
     * @param key 键
     * @param value 值
     * @return 下标
     * @param <T> value类型
     */
    <T> long indexOf(String key, T value);

    /**
     * 左插
     * @param key key
     * @param value value
     */
    <T> void lPush(String key, T value);

    /**
     * 右插
     * @param key key
     * @param value value
     */
    <T> void rPush(String key, T value);
    /**
     * 将集合左插入一行
     * @param key key
     * @param values 集合
     * @return 插入数量为1
     * @param <T> value类型
     */
    <T> Long leftPushAll(String key, Collection<T> values);
    /**
     * 将集合左插入一行
     * @param key key
     * @param values 集合
     * @return 插入数量为1
     * @param <T> value类型
     */
    <T> Long rightPushAll(String key, Collection<T> values);

    /**
     * 批量左插
     *
     * @param values 插入集合
     * @param <T>    value类型
     * @return 插入数量
     */
    <T> Long batchLeftPush(String key, Collection<T> values);
    /**
     * 批量右插
     *
     * @param values 插入集合
     * @param <T>    value类型
     * @return 插入数量
     */
    <T> Long batchRightPush(String key, Collection<T> values);

    /**
     * 批量左插
     * @param map map
     * @return 插入数量
     * @param <T> value类型
     */
    <T> Long batchLeftPush(Map<String, T> map);
    /**
     * 批量右插
     * @param map map
     * @return 插入数量
     * @param <T> value类型
     */
    <T> Long batchRightPush(Map<String, T> map);


    /**
     * 批量左插， 依据T的字段作为key和value
     *      kp 使用位置示例： 数据库中表的字段 商品类型和商店编号  可以暂存数据库中 以商店编号为key
     *
     * @param list     集合
     * @param getKey   通过T获取到key
     * @param getValue 通过T获取到value
     * @param <T>      集合元素类型
     * @return 插入数量
     */
    <T> Long batchLeftPush(Collection<T> list, Function<T, String> getKey, Function<T, Object> getValue);
    /**
     * 批量左插， 依据T的字段作为key和value
     *
     * @param list     集合
     * @param getKey   通过T获取到key
     * @param getValue 通过T获取到value
     * @param <T>      集合元素类型
     * @return 插入数量
     */
    <T> Long batchRightPush(Collection<T> list, Function<T, String> getKey, Function<T, Object> getValue);

    /**
     * 左删
     * @param key 键
     * @param clazz value的类对象
     * @return value
     * @param <T> value类
     */
    <T> T lPop(String key, Class<T> clazz);
    /**
     * 右删
     * @param key 键
     * @param clazz value的类对象
     * @return value
     * @param <T> value类
     */
    <T> T rPop(String key, Class<T> clazz);


    /**
     * 批量左删
     * @param key key名
     * @param amount 删除数量
     * @param clazz 类对象
     * @return 删除集合
     * @param <T> 类
     */
    <T> List<T> batchLeftPop(String key, Long amount, Class<T> clazz);
    /**
     * 批量右删
     * @param key key名
     * @param amount 删除数量
     * @param clazz 类对象
     * @return 删除集合
     * @param <T> 类
     */
    <T> List<T> batchRightPop(String key, Long amount, Class<T> clazz);


    /**
     * 取出对应范围的value
     * @param key key
     * @param start 起始位置
     * @param end 结束位置
     * @param clazz 类对象
     * @return values
     * @param <T> 类
     */
    <T> List<T> range(String key, Long start, Long end, Class<T> clazz);

    /**
     * 当前list大小
     *
     * @param key key
     * @return 表的大小
     */
    Long size(String key);

    /**
     * 移动value
     *
     * @param sourceKey 源
     * @param targetKey 目标
     * @param leftPop   true:左删  false:右删
     * @param leftPush  true:左增  false:右增
     * @return 是否成功
     */
    Boolean move(String sourceKey, String targetKey, boolean leftPop, boolean leftPush);
    /**
     * 批量移动value
     * @param sourceKey 源
     * @param targetKey 目标
     * @param size 移动个数
     * @param leftPop true:左删  false:右删
     * @param leftPush true:左增  false:右增
     * @return 成功移动个数
     */
    Integer move(String sourceKey, String targetKey, Integer size, boolean leftPop, boolean leftPush);

}
