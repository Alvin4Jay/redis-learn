package com.jay.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.redisson.api.*;
import org.redisson.client.protocol.ScoredEntry;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author xuweijie
 */
public class RedissonTest {

    private RedissonClient redissonClient;

    @Before
    public void setUp() {
        redissonClient = RedissonUtil.getInstance().getRedissonClient("localhost", 6379);
    }

    @After
    public void tearDown() {
        RedissonUtil.getInstance().closeRedissonClient(redissonClient);
    }

    @Test
    public void getBucket() {
        RBucket<String> rBucket = redissonClient.getBucket("testBucket");
        // 同步设置
        rBucket.set("rBucketSync");
        // 异步设置
        rBucket.setAsync("rBucketAsync");
        System.out.printf("result : %s\n", rBucket.get());
        //System.out.printf("result : %s\n", rBucket.getAndDelete()); // 获取并删除key
    }

    @Test
    public void getMap() throws ExecutionException, InterruptedException {
        RMap<String, Integer> map = redissonClient.getMap("testMap");
        // 清除map
        map.clear();
        // 添加KV，返回旧值
        Integer first = map.put("111", 111);
        System.out.println(first);

        Integer second = map.put("222", 222);
        System.out.println(second);
        // 移除KV
        Integer third = map.remove("222");
        System.out.println(third);

        // 添加KV，不返回旧值
        boolean four = map.fastPut("333", 333);
        System.out.println(four);

        // 异步添加KV
        Future<Boolean> five = map.fastPutAsync("444", 444);
        System.out.println(five.isDone());

        // 异步删除KV
        Future<Long> six = map.fastRemoveAsync("444");
        System.out.println(six.get());

        // 遍历
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }

    @Test
    public void getSet() {
        // 普通集合
        RSet<Integer> set = redissonClient.getSet("testSet");

        set.clear();
        set.addAll(Arrays.asList(12,45,12,34,56,78));

        System.out.println(Arrays.toString(set.toArray()));
    }

    @Test
    public void getSortedSet() {
        // 排序集合
        RSortedSet<Integer> sortedSet = redissonClient.getSortedSet("testSortedSet");

        //清除集合
        sortedSet.clear();
        sortedSet.add(45);
        sortedSet.add(12);
        sortedSet.addAsync(45); // 去重
        sortedSet.add(100);
        //输出结果集
        System.out.println(Arrays.toString(sortedSet.toArray()));
    }

    @Test
    public void getScoredSortedSet() {
        // 优先队列
        RScoredSortedSet<String> scoredSortedSet = redissonClient.getScoredSortedSet("testScoredSortedSet");

        scoredSortedSet.clear();
        scoredSortedSet.add(5.5, "Jim");
        scoredSortedSet.add(1.5, "Lucy");
        scoredSortedSet.add(7, "Ben");
        scoredSortedSet.add(6.1, "May");
        // 从小到大排序
        for (ScoredEntry<String> entry : scoredSortedSet.entryRange(0, -1)) {
            System.out.println(entry.getScore() + "-" + entry.getValue());
        }
        // 从大到小排序(反序)
        System.out.println("------------");
        for (ScoredEntry<String> entry : scoredSortedSet.entryRangeReversed(0, -1)) {
            System.out.println(entry.getScore() + "-" + entry.getValue());
        }
        System.out.println("------------");
        // 正排名
        System.out.println(scoredSortedSet.rank("Lucy"));
        // 反排序
        System.out.println(scoredSortedSet.revRank("Lucy"));
    }

    @Test
    public void getList() {
        RList<Integer> list = redissonClient.getList("testList");

        list.clear();
        list.add(12);
        list.add(45);
        list.add(12);
        list.add(34);
        list.add(56);
        list.add(78);

        System.out.println(Arrays.toString(list.toArray()));
    }

    @Test
    public void getQueue() {
        RQueue<Integer> queue = redissonClient.getQueue("testQueue");

        queue.clear();
        queue.addAll(Arrays.asList(12,45,12,34,56,78));

        //查看队列元素
        System.out.println(queue.peek());
        System.out.println(queue.element());
        //移除队列元素
        System.out.println(queue.poll());
        System.out.println(queue.remove());
        //输出队列
        System.out.println(Arrays.toString(queue.toArray()));
    }

    @Test
    public void getLock() throws InterruptedException {
        RLock lock = redissonClient.getLock("lock1");
        lock.lock();
        System.out.println("sleep 5s");
        Thread.sleep(5000);
        lock.unlock();
    }

    @Test
    public void getAtomicLong() {
        RAtomicLong atomicLong= redissonClient.getAtomicLong("testAtomicLong");
        atomicLong.set(100);
        System.out.println(atomicLong.addAndGet(200));
        System.out.println(atomicLong.decrementAndGet());
        System.out.println(atomicLong.get());

        atomicLong.getAndDelete(); // 删除缓存
    }

    @Test
    public void getCountDownLatch() throws InterruptedException {
        RCountDownLatch countDownLatch = redissonClient.getCountDownLatch("countdown");
        countDownLatch.trySetCount(1);
        Thread.sleep(5000);
        countDownLatch.countDown();
    }
}