package com.jay.util;

import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.config.Config;

/**
 * @author xuweijie
 */
public class RedissonUtil {

    private static RedissonUtil redissonUtil = new RedissonUtil();

    private RedissonUtil() {
    }

    public static RedissonUtil getInstance() {
        return redissonUtil;
    }

    public RedissonClient getRedissonClient(Config config) {
        return Redisson.create(config);
    }

    public RedissonClient getRedissonClient(String host, int port) {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + host + ":" + port); // 单节点
        return Redisson.create(config);
    }

    public void closeRedissonClient(RedissonClient client) {
        client.shutdown();
    }
}
