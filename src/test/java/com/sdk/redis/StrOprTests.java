package com.sdk.redis;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.sdk.redis.service.RedisServer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Lzzh
 * @version 1.0
 */
@SpringBootTest
public class StrOprTests {
    @Resource
    RedisServer redisServer;
    @Resource
    RedisTemplate<String,Object> redisTemplate;

    @Test
    void contextLoads() {
        HashOprTests.User user = new HashOprTests.User();
        user.setName("李小");
        user.setSex("男");
        long now = System.currentTimeMillis();
        for(int i = 0; i < 1000; i++) {
//            String userJson = JSONUtil.toJsonStr(user);
//            redisTemplate.opsForValue().set(String.valueOf(i),userJson);
//            String s = redisTemplate.opsForValue().get(String.valueOf(i));
//            JSONUtil.toBean(s, HashOprTests.User.class);
        }
        System.out.println(System.currentTimeMillis() - now);
        now = System.currentTimeMillis();
        for(int i = 0; i < 1000; i++) {
//            objectRedisTemplate.opsForValue().set(String.valueOf(i),user);
//            objectRedisTemplate.opsForValue().get(String.valueOf(i));
        }
        System.out.println(System.currentTimeMillis()-now);

    }

    @Test
    void testRedisException() {
        List<String> list = new ArrayList<>();
        Object obj = list;
        if(BeanUtil.isEmpty(list) || list.size() == 0) {
            System.out.println("list is empty");
        }
        if(obj instanceof Collection) {
            System.out.println("aaaaaaaaaaaaaaaaaaa");
            Collection collection = (Collection) obj;
            if(collection.size() == 0) {
                System.out.println("size = 0");
            }
        } else if(obj instanceof Map) {

        }
        Map<String, String> map = new HashMap<>();

        List nullList = null;
        System.out.println(BeanUtil.isEmpty(nullList));
    }

    @Test
    void testRedisServer() {
//        System.out.println(redisServer.has("1"));
        redisServer.strOpr().set("1","2");
        redisServer.strOpr().set("2","3");
        redisServer.strOpr().set("3","4");
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");

        List<HashOprTests.User> users = new ArrayList<>();
        System.out.println(redisServer.existingKeys(list).size());
        System.out.println(redisServer.emptyKeys(list).size());
//        System.out.println(redisServer.delete("1"));
        System.out.println(redisServer.delete(users, HashOprTests.User::getName));
        System.out.println(redisServer.expire("3", 1234, TimeUnit.SECONDS));
    }

    @Test
    void testString() {
        Long a = 1234L;
        String str = a.toString();
        System.out.println(str);
    }


}
