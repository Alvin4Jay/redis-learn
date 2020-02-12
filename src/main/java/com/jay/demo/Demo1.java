package com.jay.demo;

import com.alibaba.fastjson.JSON;
import com.jay.bean.User;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import redis.clients.jedis.Jedis;

/**
 * @author xuweijie
 */
public class Demo1 {

    public static void main(String[] args) {
        // test01();
        test02();
    }

    // Jedis
    private static void test01() {
        Jedis jedis = new Jedis("localhost", 6379);

        User user = new User();
        user.setId(1L);
        user.setName("zhangsan");
        user.setEmail("zhangsan@qq.com");
        String result = jedis.set("user", JSON.toJSONString(user)); // 存
        System.out.printf("result: %s\n", result);

        String value = jedis.get("user"); // 取
        User user2 = JSON.parseObject(value, User.class);
        System.out.println(user.equals(user2));

        jedis.close();
    }

    // Redisson
    private static void test02() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");

        RedissonClient client = Redisson.create(config);
        RBucket<String> user = client.getBucket("user");
        System.out.println(user.get());

        client.shutdown();
    }
}
