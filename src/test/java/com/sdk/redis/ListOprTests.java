package com.sdk.redis;

import com.sdk.redis.service.RedisServer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lzzh
 * @version 1.0
 */
@SpringBootTest
public class ListOprTests {

    @Resource
    RedisServer redisServer;
    @Test
    void  testList() {
//        System.out.println(redisServer.listOpr().move("1", "2", true, true));
//        System.out.println(redisServer.has("1234"));
//        Integer index = redisServer.listOpr().index("1", 0L, Integer.class);
//        System.out.println(index);
//        long l = redisServer.listOpr().indexOf("1", 1);
//        System.out.println(l);
        HashOprTests.User user = new HashOprTests.User();
//        redisServer.listOpr().lPush("1",user);
//        redisServer.listOpr().rPush("1",user);
        user.setSex("qwer");
//        redisServer.listOpr().lPush("1",user);
        user.setName("qwer");
//        redisServer.listOpr().rPush("1",user);
        HashOprTests.User second = new HashOprTests.User();
        second.setName("asdf");
        second.setSex("asdf");
        List<HashOprTests.User> list = new ArrayList<>();
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
        List<HashOprTests.User> users = redisServer.listOpr().batchLeftPop("1", 2L, HashOprTests.User.class);
        List<HashOprTests.User> asdf = redisServer.listOpr().batchRightPop("1", 2L, HashOprTests.User.class);
        System.out.println("asdf");

    }

}
