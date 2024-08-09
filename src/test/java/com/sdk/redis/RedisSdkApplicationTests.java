package com.sdk.redis;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.hash.Hash;
import cn.hutool.json.JSONUtil;
import com.sdk.redis.operation.HashOpr;
import com.sdk.redis.operation.impl.DefaultListOpr;
import com.sdk.redis.service.RedisServer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@SpringBootTest
class RedisSdkApplicationTests {
    static class User {
        Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        String name;
        String sex;
        String phone;
        String address;
        String identityId;
        Long loginTime;

        public User(Long id, String name, String sex, String phone, String address, String identityId, Long loginTime) {
            this.id = id;
            this.name = name;
            this.sex = sex;
            this.phone = phone;
            this.address = address;
            this.identityId = identityId;
            this.loginTime = loginTime;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getIdentityId() {
            return identityId;
        }

        public void setIdentityId(String identityId) {
            this.identityId = identityId;
        }

        public Long getLoginTime() {
            return loginTime;
        }

        public void setLoginTime(Long loginTime) {
            this.loginTime = loginTime;
        }

        public User() {
        }

        public User(Long id,String name, String sex) {
            this.id = id;
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
            return "id:" + id + ",name:" + name + ",sex:" +sex;
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

        List<User> users = new ArrayList<>();
        System.out.println(redisServer.existingKeys(list).size());
        System.out.println(redisServer.emptyKeys(list).size());
//        System.out.println(redisServer.delete("1"));
        System.out.println(redisServer.delete(users,User::getName));
        System.out.println(redisServer.expire("3", 1234, TimeUnit.SECONDS));
    }

    @Test
    void testString() {
        Long a = 1234L;
        String str = a.toString();
        System.out.println(str);
    }


    @Test
    void testHash() {
//        User user1 = new User(1L,"qwer","zxcv");
//        User user2 = new User(2L,"qwer","zxcv");
//        Set<User> set = new HashSet<>();
//        set.add(user2);
//        set.add(user1);
//        redisServer.hashOpr().putAllObject("user","",set,User::getId);
//        user1.setId(3L);
//        user2.setId(4L);
//        redisServer.hashOpr().delete("user","",set,User::getId);
//        Class<User> clazz = User.class;
//        redisServer.hashOpr().delete("user","5:id","5:name","5:sex");
//        redisServer.hashOpr().putIfAbsent("2", "asdf","zxcv");
//        redisServer.hashOpr().inc("user", "123");
//        redisServer.hashOpr().inc("user", "123",123L);
//        System.out.println(redisServer.hashOpr().lengthOfValue("user", "123"));
//        System.out.println(redisServer.hashOpr().lengthOfValue("user", "1234"));
//        Map<Integer, Integer> map = new HashMap<>();
//        for(int i = 0; i < 4; i++) {
//            map.put(i,i);
//        }
//        redisServer.hashOpr().putAll("num",map);
//        redisServer.hashOpr().put("num","1",2);
//        redisServer.hashOpr().putIfAbsent("num","12",13);
//        Map<String, Object> num = redisServer.hashOpr().entries("num");
//        num.forEach((k,v)->{
//            System.out.println(k + "," + v);
//        });
//        List<String> list = new ArrayList<>();
//        list.add("1");
//        list.add("1");
//        list.add("3");
//        Map<String, Object> res = redisServer
//                .hashOpr().multiGet("num", list);
//        res.forEach((k,v)->{
//            System.out.println(k + "," + v);
//        });
//        System.out.println(redisServer.hashOpr().getHashValue("num", "-1"));
//        Set<String> num = redisServer.hashOpr().keys("123");
//        num.forEach((obj)->{
//            System.out.println(obj);
//        });
//        redisServer.hashOpr().size("num");
//        System.out.println(redisServer.hashOpr().hasKey("num", "123"));
//        System.out.println(redisServer.hashOpr().hasKey("nusm", "123"));
//        Map<String, Object> num = redisServer.hashOpr().entries("num");
//        num.forEach((k,v)->{
//            System.out.println(k + " " + v);
//        });
//        Set<String> fields = new HashSet<>();
//        fields.add("id");
//        fields.add("name");
//        fields.add("sex");
//        fields.add("phone");
//        fields.add("address");
//        fields.add("identityId");
//        fields.add("loginTime");
//        //        System.out.println(Arrays.toString(clazz.getFields()));
//        User user3 = new User(1234L,"李四","男", "12341234231","1234asdfzxcv","123456123412341234",System.currentTimeMillis());
//        User user4 = new User(1235L,"张三","男", "12341234231","1234asdfzxcv","123456123412341234",System.currentTimeMillis());
//        redisServer.hashOpr().put("user","",user3, User::getId,User.class);
//        redisServer.hashOpr().put("user","",user4, User::getId,User.class);
//
//        List<Object> list = new ArrayList<>();
//        list.add("id");
//        list.add("name");
//        list.add("sex");
//        list.add("phone");
//        list.add("address");
//        list.add("identityId");
//        list.add("loginTime");
         }

    /**
     * 测试： putObj
     *      将一个对象存入hash表，形成如下结构
     *          prefix:id:field  (前缀：主键唯一标识：字段)
     * 结果：
     *                       1
     *      redisTemplate ： 17
     *      redisServer：    90
     * kp: 虽然只是单个插入，封装效果不太好
     */
    @Test
    void testPutByRedisTemplate() {
        redisTemplate.hasKey("");
        redisTemplate.hasKey("");
        redisTemplate.hasKey("");
        User user = new User(1L,"asdf","zxcv","zxcvasdf","asdfqwersd","zxcvasdfqwerdfasdf",System.currentTimeMillis());
        long begin = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<>();
        for(int i = 0; i < 1000; i++) {
            user.setId(user.getId()+1);
            map.put("login:" + i + "id", user.getId());
            map.put("login:" + i + "nane", user.getName());
            map.put("login:" + i + "sex", user.getSex());
            map.put("login:" + i + "phone", user.getPhone());
            map.put("login:" + i + "identityId", user.getIdentityId());
            map.put("login:" + i + "address", user.getAddress());
            map.put("login:" + i + "loginTime", user.getLoginTime());
            redisTemplate.opsForHash().putAll("user2",map);
            map.clear();
        }
        System.out.println(System.currentTimeMillis()-begin);
    }
    @Test
    void testPutObjByRedisServer() {
        redisTemplate.hasKey("");
        redisTemplate.hasKey("");
        redisTemplate.hasKey("");
        User user = new User(0L,"asdf","zxcv","zxcvasdf","asdfqwersd","zxcvasdfqwerdfasdf",System.currentTimeMillis());
        long begin = System.currentTimeMillis();
        for(int i = 0; i < 1; i++) {
            redisServer.hashOpr().putObj("user1", "login",user,User::getId,User.class);
            user.setId(i+1L);
        }
        System.out.println(System.currentTimeMillis()-begin );
    }

    /**
     * 测试： putAllObject
     *      Hash表中批量添加对象信息
     * 结果：
     *                         10000
     *      redisTemplate ：  535 495 472
     *      redisServer：     506 472 449
     */
    @Test
    void testAddByRedisTemplate() {
        redisTemplate.hasKey("");
        redisTemplate.hasKey("");
        redisTemplate.hasKey("");
        User user = new User(12345L,"asdf","zxcv","zxcvasdf","asdfqwersd","zxcvasdfqwerdfasdf",System.currentTimeMillis());
        long begin = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<>();
        for(int i = 0; i < 10000; i++) {
            user.setId(user.getId()+1);
            map.put("login:" + i + "id", user.getId());
            map.put("login:" + i + "nane", user.getName());
            map.put("login:" + i + "sex", user.getSex());
            map.put("login:" + i + "phone", user.getPhone());
            map.put("login:" + i + "identityId", user.getIdentityId());
            map.put("login:" + i + "address", user.getAddress());
            map.put("login:" + i + "loginTime", user.getLoginTime());
        }
        redisTemplate.opsForHash().putAll("user2",map);
        System.out.println(System.currentTimeMillis()-begin);
        // 10000: 535 495 442
    }

    @Test
    void testPutAllObject() {
        redisServer.has("");
        redisServer.has("");
        redisServer.has("");
        List<User> users = new ArrayList<>();
        long begin = System.currentTimeMillis();
        for(int i = 0; i < 10000; i++) {
            User user = new User((long) i,"asdf","zxcv","zxcvasdf","asdfqwersd","zxcvasdfqwerdfasdf",System.currentTimeMillis());
            users.add(user);
        }

        redisServer.hashOpr().putAllObject("user1","login",users,User::getId);
        System.out.println(System.currentTimeMillis() - begin);
        // 10000: 7791 7708 -> 506 472 449
    }
//    @Test
//    void testAdd2() {
//        redisServer.has("");
//        List<User> users = new ArrayList<>();
//        for(int i = 0; i < 10000; i++) {
//            User user = new User((long) i,"asdf","zxcv","zxcvasdf","asdfqwersd","zxcvasdfqwerdfasdf",System.currentTimeMillis());
//            users.add(user);
//        }
//        long begin = System.currentTimeMillis();
//        redisServer.hashOpr().putAllObject2("user1","login:",users,User::getId);
//        System.out.println(System.currentTimeMillis() - begin);
//        // 10000:  1000: 1337  500: 854   100:308
//    }

    /**
     * 测试： getObjBySet getObjByClazz
     *      从Hash表中获取到单个对象信息
     *          如：上述的User对象，七个字段
     * 结果：
     *                         1000个对象(7000条)
     *      redisTemplate ：     355 364 353
     *   redisServer.getObjBySet：  506 472 449
     *   redisServer.getObjByClazz： 495 489 486
     */
    @Test
    void testGetByRedisTemplate() {
        redisTemplate.keys("");
        redisTemplate.keys("");
        /*redisTemplate*/

        System.out.println("redisTemplate 开始测试");
        long start = System.currentTimeMillis();

        for(int i = 0; i < 1000; i++) {
            List<Object> list = new ArrayList<>();

            list.add(i + ":id");
            list.add(i + ":name");
            list.add(i + ":sex");
            list.add(i + ":phone");
            list.add(i + ":address");
            list.add(i + ":identityId");
            list.add(i + ":loginTime");
            List<Object> hashKeys = list.stream()
                    .map(value -> (Object) ("login:" + value)).collect(Collectors.toList());
            List<Object> res = redisTemplate
                    .opsForHash().multiGet("user1", hashKeys);

            User user = new User();
            user.setId(Long.valueOf(res.get(0).toString()));
            user.setName(res.get(1).toString());
            user.setSex(res.get(2).toString());
            user.setPhone(res.get(3).toString());
            user.setAddress(res.get(4).toString());
            user.setIdentityId(res.get(5).toString());
            user.setLoginTime(Long.valueOf(res.get(6).toString()));

        }
        System.out.println(System.currentTimeMillis()-start);
        // 1000: 355 364 353
    }
    @Test
    void testGetBySet() {
        redisTemplate.keys("");
        redisTemplate.keys("");
        redisTemplate.keys("");
        /*BySet*/
        System.out.println("multiGetBySet 开始测试");
        Long start = System.currentTimeMillis();
        Set<String> fields = new HashSet<>();
        fields.add("id");
        fields.add("name");
        fields.add("sex");
        fields.add("phone");
        fields.add("address");
        fields.add("identityId");
        fields.add("loginTime");

        for(int i = 1000; i < 2000; i++) {
            User objBySet = redisServer.hashOpr()
                    .getObjBySet("user1", "login", String.valueOf(i), fields, User.class);
        }
        System.out.println(System.currentTimeMillis() - start);
        // 1000 481 500 481
    }
    @Test
    void testGetByClazz() {
        redisTemplate.keys("");
        redisTemplate.keys("");
        /*ByClazz*/
        System.out.println("multiGetByClazz 开始测试");
        Long start = System.currentTimeMillis();
        for(int i = 0; i < 1000; i++) {
            User objByClazz = redisServer.hashOpr()
                    .getObjByClazz("user1", "login", String.valueOf(i), User.class);
        }
        System.out.println(System.currentTimeMillis() - start);
        // 1000 495 489 486
    }

    /**
     * 测试：
     *      从Hash表中批量删除对象信息
     * 结果：
     *                       1   1000
     *      redisTemplate ： 5    310
     *      redisServer：    4    295
     */
    @Test
    void testDelByRedisTemplate() {
        redisTemplate.hasKey("");
        redisTemplate.hasKey("");
        redisTemplate.hasKey("");
        long begin = System.currentTimeMillis();
        Set<String> fields = new HashSet<>();
        fields.add("id");
        fields.add("name");
        fields.add("sex");
        fields.add("phone");
        fields.add("address");
        fields.add("identityId");
        fields.add("loginTime");
        List<String> hashKeys = new ArrayList<>();
        for(int i = 0; i < 10000; i++) {
            hashKeys.add("login:" + i + ":id");
            hashKeys.add("login:" + i + ":name");
            hashKeys.add("login:" + i + ":sex");
            hashKeys.add("login:" + i + ":phone");
            hashKeys.add("login:" + i + ":address");
            hashKeys.add("login:" + i + ":identityId");
            hashKeys.add("login:" + i + ":loginTime");
        }
        redisTemplate.opsForHash().delete("user1", hashKeys.toArray());
        System.out.println(System.currentTimeMillis() - begin);
        // 210
    }

    @Test
    void testBatchDel() {
        redisServer.has("");
        long begin = System.currentTimeMillis();
        List<String> list = new ArrayList<>();
        for(int i = 0; i < 10000; i++) {
            list.add(String.valueOf(i));
        }
        redisServer.hashOpr().batchDelete("user1","login",list,User.class);
        System.out.println(System.currentTimeMillis()-begin);
        // 226
    }

    /**
     * 测试：
     *      从Hash表中删除一个对象信息
     * 结果：
     *                       1   1000
     *      redisTemplate ： 5    310
     *      redisServer：    4    295
     */
    @Test
    void testDeleteObjByRedisTemplate() {
        redisTemplate.hasKey("");
        redisTemplate.hasKey("");
        redisTemplate.hasKey("");
        redisTemplate.hasKey("");
        long begin = System.currentTimeMillis();
        Set<String> fields = new HashSet<>();
        fields.add("id");
        fields.add("name");
        fields.add("sex");
        fields.add("phone");
        fields.add("address");
        fields.add("identityId");
        fields.add("loginTime");
        for(int i =3001;i < 3002;i++) {
            List<String> hashKeys = new ArrayList<>();
            hashKeys.add("login:" + i + ":id");
            hashKeys.add("login:" + i + ":name");
            hashKeys.add("login:" + i + ":sex");
            hashKeys.add("login:" + i + ":phone");
            hashKeys.add("login:" + i + ":address");
            hashKeys.add("login:" + i + ":identityId");
            hashKeys.add("login:" + i + ":loginTime");
            redisTemplate.opsForHash().delete("user1",hashKeys.toArray());
        }
        System.out.println(System.currentTimeMillis()-begin);
        // 1000:310
        // 1:5
    }

    @Test
    void testDeleteObj() {
        redisServer.has("");
        redisServer.has("");
        redisServer.has("");
        redisServer.has("");
        long begin = System.currentTimeMillis();
        for(int i =3002;i < 3003;i++) {
            redisServer.hashOpr().deleteObj("user1","login",String.valueOf(i),User.class);
        }
        System.out.println(System.currentTimeMillis()-begin);
        // 1000:295
        // 1:4
    }





}
