package com.sdk.redis.operation.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdk.redis.operation.HashOpr;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Lzzh
 * @version 1.0
 */
public class DefaultHashOpr implements HashOpr {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public DefaultHashOpr(RedisTemplate<String,Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    private Map<String, Object> objectToMap(Object obj) {
        return objectMapper.convertValue(obj, new TypeReference<Map<String, Object>>() {});
    }
    private<T> T mapToObject(Map<String, Object> map, Class<T> clazz) {
        return objectMapper.convertValue(map, clazz);
    }

    private<T> Set<String> getAllHashKeys(String prefix, String primaryKey, Class<T> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        Set<String> hashKeys = new HashSet<>();
        for(Field field: fields) {
            hashKeys.add(prefix + ":" + primaryKey + ":" + field.getName());
        }
        return hashKeys;
    }

    @Override
    public Long size(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    @Override
    public Boolean hasKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    @Override
    public Set<String> keys(String key) {
        return redisTemplate.opsForHash().keys(key)
                .stream()
                .map(Object::toString).collect(Collectors.toSet());
    }

    @Override
    public Object getHashValue(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    @Override
    public <T>Map<String, Object> multiGet(String key, Collection<T> hashKeys) {
        HashOperations<String, T, Object> hashOperations = redisTemplate.opsForHash();
        List<Object> res = hashOperations.multiGet(key, hashKeys);
        if(hashKeys.size() != res.size()) {
            // todo 异常
        }
        Map<String, Object> map = new HashMap<>();
        Iterator<T> keyIterator = hashKeys.iterator();
        Iterator<Object> resultIterator = res.iterator();
        while (keyIterator.hasNext() && resultIterator.hasNext()) {
            map.put(keyIterator.next().toString(), resultIterator.next());
        }
        return map;
    }

    @Override
    public <T> T getObjBySet(String key, String prefix,
                               String primaryKey, Set<String> fields,
                               Class<T> clazz) {
        Set<String> hashKeys = fields.stream()
                .map((str) -> prefix + ":" + primaryKey + ":" + str)
                .collect(Collectors.toSet());
        Map<String, Object> map = this.multiGet(key, hashKeys)
                .entrySet().stream()
                .collect(Collectors.toMap(
                        t -> {
                            String[] strs = t.getKey().split(":");
                            return strs[strs.length-1];
                        },
                        Map.Entry::getValue));
        return mapToObject(map, clazz);
    }

    @Override
    public <T> T getObjByClazz(String key, String prefix, String primaryKey, Class<T> clazz) {
        Set<String> set = this.getAllHashKeys(prefix, primaryKey, clazz);
        Map<String, Object> map = this.multiGet(key, set)
                .entrySet().stream()
                .collect(Collectors.toMap(
                        entity-> {
                            String[] split = entity.getKey().split(":");
                            return split[split.length-1];
                        },
                        Map.Entry::getValue
                ));
        return mapToObject(map, clazz);
    }

    @Override
    public Map<String, Object> entries(String key) {
        return redisTemplate.opsForHash().entries(key)
                .entrySet().stream()
                .collect(Collectors.toMap(
                        e->e.getKey().toString(),
                        Map.Entry::getValue
                ));
    }
    // todo 获取Hash表中全部信息，封装为对象

    @Override
    public<T> void put(String key, String hashKey, T value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    @Override
    public <T> void putObj(String key, String prefix, T obj, Function<T, Object> getHashKey,Class<T> clazz) {
        String primaryKey =  getHashKey.apply(obj).toString();
        Map<byte[], byte[]> hashInfo = objectToMap(obj).entrySet().stream()
                .collect(Collectors.toMap(
                        entity -> (prefix + ":" + primaryKey + ":" + entity.getKey()).getBytes(),
                        entity -> entity.getValue().toString().getBytes()
                ));
        redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.hashCommands().hMSet(key.getBytes(), hashInfo);
                return null;
            }
        });
    }

    @Override
    public<T> void putIfAbsent(String key, String hashKey, T value) {
        redisTemplate.opsForHash().putIfAbsent(key, hashKey, value);
    }

    @Override
    public<R> void putAll(String key,  Map<String, R> map) {
        byte[] keyBytes = key.getBytes();
        redisTemplate.opsForHash().putAll(key, map);
    }

//    @Override
//    public <T,R> void putAllObject(String key, String prefix,
//                                 Collection<T> set, Function<T, R> getKey) {
//        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
//            HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
//            for (T t : set) {
//                R hKey = getKey.apply(t);
//                Map<String, Object> map = objectToMap(t);
//                map.forEach((k, v) -> {
//                    String hashKey = prefix + ":" + hKey.toString() + ":" + k;
//                    hashOperations.put(key,hashKey,v);
//                });
//            }
//            return null;
//        });
//    }

    @Override
    public <T,P> void putAllObject(String key, String prefix,
                                    Collection<T> set,
                                   Function<T, P> getKey) {
        int count = 0;
        Map<String, Object> map = new HashMap<>();
        List<Map<String,Object>> list = new ArrayList<>();
        int size = set.size();
        for(T t: set) {
            P hKey = getKey.apply(t);
            map.putAll(objectToMap(t).entrySet().stream()
                    .collect(Collectors.toMap(
                            entity->prefix + ":" + hKey + ":" + entity.getKey(),
                            Map.Entry::getValue)));
            count++;
            if(count == 3000) {
                redisTemplate.opsForHash().putAll(key, map);
//                executePipelinedBatch(key, map);
                count = 0;
                map.clear();
            }
        }
        if(!map.isEmpty()) {
            redisTemplate.opsForHash().putAll(key, map);
//            executePipelinedBatch(key,map);
        }
    }
//    private void executePipelinedBatch(String key, Map<String,Object> list) {
//        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
//            HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
//            for (Map<String, Object> map : list) {
//                map.forEach((k, v) -> {
//                    hashOperations.put(key, k, v);
//                });
//            }
//            return null;
//        });
//    }

    @Override
    public Long delete(String key, String... hashKey) {
        return redisTemplate.opsForHash().delete(key, (Object[]) hashKey);
    }

    @Override
    public Long delete(String key, Collection<String> hashKeys) {
        Set<String> set = new HashSet<>(hashKeys);
        return this.delete(key, set.toArray(new String[0]));
    }

    @Override
    public <T> Long deleteObj(String key, String prefix, String primaryKey, Class<T> clazz) {
        Set<String> hashKeys = this.getAllHashKeys(prefix, primaryKey, clazz);
        return this.delete(key, hashKeys.toArray(new String[0]));
    }

    @Override
    public <T> Long batchDelete(String key, String prefix,
                           Collection<String> primaryKeys,
                             Class<T> clazz){
        Field[] fields = clazz.getDeclaredFields();
        Set<String> fieldNames = new HashSet<>();
        for (Field field : fields) {
            fieldNames.add(field.getName());
        }
        List<byte []> list = new ArrayList<>();
        byte[] keyBytes = key.getBytes();
        // 使用 Redis 管道批量删除
        Long amount = (Long) redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                for (String primaryKey : primaryKeys) {
                    for (String fieldName : fieldNames) {
                        list.add((prefix + ":" + primaryKey + ":" + fieldName).getBytes());
                    }

                }
                connection.hashCommands().hDel(keyBytes, list.toArray(new byte[0][]));
                return null;
            }
        }).get(0);
        // todo 异常处理
        return amount;

//        redisTemplate.executePipelined(new RedisCallback<Object>() {
//            @Override
//            public Object doInRedis(RedisConnection connection) throws DataAccessException {
//                HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
//                for (T t : set) {
//                    P primaryKey = getHashKey.apply(t);
//                    Map<String, Object> map = objectToMap(t);
//                    map.forEach((k,v)->{
//                        String hashKey = prefix + primaryKey.toString() +":" + k;
//                        hashOperations.delete(key, hashKey);
//                    });
//                }
//                return null;
//            }
//        });
    }



    @Override
    public Long lengthOfValue(String key, String hashKey) {
        return redisTemplate.opsForHash().lengthOfValue(key,hashKey);
    }

    @Override
    public Long inc(String key, String hashKey) {
        return this.inc(key,hashKey,1L);
    }

    @Override
    public Long inc(String key, String hashKey, Long delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }
}
