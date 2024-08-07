package com.sdk.redis;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.hash.Hash;
import cn.hutool.json.JSONUtil;
import com.sdk.redis.operation.impl.DefaultListOpr;
import com.sdk.redis.service.RedisServer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class RedisSdkApplicationTests {
    static class User {
        String name;
        String sex;

        public User() {
        }

        public User(String name, String sex) {
            this.name = name;
            this.sex = sex;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }
        public String toString() {
            return "name:" + name + ",sex:" +sex;
        }
    }
    @Resource
    RedisTemplate<String,String> redisTemplate;

    @Resource
    RedisTemplate<String, Object> objectRedisTemplate;

    @Resource
    RedisServer redisServer;
    @Test
    void contextLoads() {
        User user = new User();
        user.setName("李小");
        user.setSex("男");
        long now = System.currentTimeMillis();
        for(int i = 0; i < 1000; i++) {
//            String userJson = JSONUtil.toJsonStr(user);
//            redisTemplate.opsForValue().set(String.valueOf(i),userJson);
            String s = redisTemplate.opsForValue().get(String.valueOf(i));
            JSONUtil.toBean(s,User.class);
        }
        System.out.println(System.currentTimeMillis() - now);
        now = System.currentTimeMillis();
        for(int i = 0; i < 1000; i++) {
//            objectRedisTemplate.opsForValue().set(String.valueOf(i),user);
            objectRedisTemplate.opsForValue().get(String.valueOf(i));
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
    void  testList() {
//        System.out.println(redisServer.listOpr().move("1", "2", true, true));
//        System.out.println(redisServer.has("1234"));
//        Integer index = redisServer.listOpr().index("1", 0L, Integer.class);
//        System.out.println(index);
//        long l = redisServer.listOpr().indexOf("1", 1);
//        System.out.println(l);
        User  user = new User();
//        redisServer.listOpr().lPush("1",user);
//        redisServer.listOpr().rPush("1",user);
        user.setSex("qwer");
//        redisServer.listOpr().lPush("1",user);
        user.setName("qwer");
//        redisServer.listOpr().rPush("1",user);
        User second = new User();
        second.setName("asdf");
        second.setSex("asdf");
        List<User> list = new ArrayList<>();
        list.add(user);
        list.add(user);
        list.add(second);
//        Map<String, User> map = new HashMap<>();
//        map.put("1",user);
//        map.put("2",second);
//        redisServer.listOpr().batchRightPush(list,User::getName,User::getSex);
//        List<String> res = redisServer.listOpr().range("asdf", 0L, 8L, String.class);
//        System.out.println(res.size());
//        System.out.println(redisServer.listOpr().batchRightPush(map));
//        redisServer.listOpr().lPush("1","asdfzxcv");
//        System.out.println(redisServer.listOpr().lPop("1", String.class));
//        System.out.println(redisServer.listOpr().rPop("1", String.class));
//        redisServer.listOpr().lPush("2", user);
//        System.out.println(redisServer.listOpr().lPop("2", User.class));

        redisServer.listOpr().batchRightPush("1",list);
        redisServer.listOpr().batchLeftPush("1",list);
        List<User> users = redisServer.listOpr().batchLeftPop("1", 2L, User.class);
        List<User> asdf = redisServer.listOpr().batchRightPop("1", 2L, User.class);
        System.out.println("asdf");

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
        User user = new User("1","1234");
        List<User> users = new ArrayList<>();
        users.add(user);
        System.out.println(redisServer.existingKeys(list).size());
        System.out.println(redisServer.emptyKeys(list).size());
//        System.out.println(redisServer.delete("1"));
        System.out.println(redisServer.delete(users,User::getName));
        System.out.println(redisServer.expire("3", 1234, TimeUnit.SECONDS));
    }

    @Test
    void testString() {

    }

}
