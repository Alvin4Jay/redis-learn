package com.jay.redis.spring;

import com.jay.redis.spring.bean.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisApplicationTests {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void test01() {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set("hello", "redis");
        System.out.println("useRedisDao = " + valueOperations.get("hello"));
    }

    @Test
    public void test02() {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        User user = new User();
        user.setName("xuan");
        user.setPassword("123");
        valueOperations.set("user", user);
        System.out.println("useRedisDao = " + valueOperations.get("user"));
    }
}
