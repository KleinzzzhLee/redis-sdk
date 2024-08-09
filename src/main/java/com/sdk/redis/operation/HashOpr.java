package com.sdk.redis.operation;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * Hash接口
 *      规定，此Hash数据类型，
 *         key:   String
 *         hKey:  String
 *         hValue:Object
 *         通过Class进行hValue的类型转换
 * @author Lzzh
 */
public interface HashOpr {
    /**
     * hash的size
     * @param key key
     * @return hashKey-hashValue的行数
     */
    Long size(String key);

    /**
     * 查看key是否存在
     * @param key key
     * @param hashKey hashKey
     * @return boolean
     */
    Boolean hasKey(String key, String hashKey);

    /**
     * 获取全部的HashKey
     * @param key key
     * @return set<String>
     */
    Set<String> keys(String key);

    /**
     * 获取到hashValue
     * @param key key
     * @param hashKey hashKey
     * @return hashValue
     */
    Object getHashValue(String key, String hashKey);
    /**
     * 批量获取
     * @param key key
     * @param hashKeys hashKey集合
     * @return Map<String, Object>
     */
    <T> Map<String,Object> multiGet(String key, Collection<T> hashKeys);

    /**
     * 依据hashKey，获取到单个对象信息
     * @param key hash表
     * @param prefix hashKey前缀
     * @param hashKey 对象id，唯一标识一个对象的字段
     * @param fields 类的字段名
     * @param clazz 类对象
     * @return 对象
     * @param <T> 对象类型
     */
    <T> T getObjBySet(String key, String prefix, String hashKey, Set<String> fields,Class<T> clazz);

    /**
     * 依据hashKey，获取单个对象信息
     * @param key hash表
     * @param prefix hashKey前缀
     * @param hashKey 对象id，唯一标识一个对象的字段
     * @param clazz 类对象
     * @return 对象
     * @param <T> 对象类型
     */
    <T> T getObjByClazz(String key, String prefix, String hashKey, Class<T> clazz);


    /**
     * 获取hash中全部的key-value
     * @param key key
     * @return map
     */
    Map<String, Object> entries(String key);

    /**
     * 像hash中增加key-value
     * @param key key
     * @param hashKey hashKey
     * @param value value
     */
    <T> void put(String key, String hashKey, T value);

    /**
     * 存取对象信息
     * @param key hash表
     * @param prefix hashKey的前缀
     * @param obj 对象
     * @param getHashKey 方法：从对象获取到主键
     * @param clazz 类对象
     * @param <T> 对象类型
     */
    <T> void putObj(String key, String prefix, T obj,
                 Function<T,Object> getHashKey, Class<T> clazz);

    /**
     * 向hash中增加key 一个不存在的
     * @param key key
     * @param hashKey hashKey
     * @param value hashValue
     */
    <T> void putIfAbsent(String key, String hashKey, T value);

    /**
     * 将Map中的全部推送进入
     * @param key key
     * @param map 添加内容
     */
    <R> void putAll(String key, Map<String, R> map);

    /**
     * 向hash表中增加集合所有内容
     * @param key hash表名
     * @param prefix hashKey前缀
     * @param set 集合
     * @param getKey 对集合中的各个元素获取到key
     * @param <T> set集合元素类型
     */
    <T,R>void putAllObject(String key, String prefix, Collection<T> set, Function<T, R> getKey);


//    <T,P> void putAllObject2(String key, String prefix, Collection<T> set, Function<T, P> getKey);

    /**
     * 删除hashKey
     * @param key key
     * @param hashKey hashKey
     * @return 删除数量
     */
    Long delete(String key, String... hashKey);

    /**
     * 批量删除
     * @param key key
     * @param hashKeys 删除的hashKey集
     * @return 删除数量
     */
    Long delete(String key, Collection<String> hashKeys);

    /**
     * 删除对应主键的所有字段
     * @param key hash表
     * @param prefix hashKey前缀
     * @param primaryKey 主键
     * @param clazz 类对象
     * @return 删除数量 = 字段个数
     * @param <T> 泛型
     */
    <T> Long deleteObj(String key, String prefix, String primaryKey, Class<T> clazz);

    /**
     * 删除保存的hash信息，删除主键对应的集合
     * @param key 键
     * @param prefix hashKey前缀
     * @param primaryKey 主键
     * @param clazz 类对象
     * @return Long 删除数量
     * @param <T> 泛型
     */
    <T> Long batchDelete(String key, String prefix, Collection<String> primaryKey, Class<T> clazz) ;

    /**
     * hashValue的长度
     * @param key key
     * @param hashKey hashKey
     * @return 长度
     */
    Long lengthOfValue(String key, String hashKey);

    /**
     * 增加
     * @param key key
     * @param hashKey hashKey
     * @return 增后
     */
    Long inc(String key, String hashKey);

    /**
     * 增加
     * @param key key
     * @param hashKey hashKey
     * @param delta 增量
     * @return 增后
     */
    Long inc(String key, String hashKey, Long delta);


}
