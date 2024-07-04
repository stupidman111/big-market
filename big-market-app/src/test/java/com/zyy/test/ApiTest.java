package com.zyy.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiTest {

    @Resource
    private RedissonClient redissonClient;

    @Test
    public void setValue() {
        redissonClient.getBucket("activity_sku_stock_count_key_9011").set(1000);
        log.info("测试结果：{}", redissonClient.getBucket("activity_sku_stock_count_key_9011").get());
    }

    @Test
    public void test_decr() {
        redissonClient.getAtomicLong("activity_sku_stock_count_key_9011").set(1000);
        redissonClient.getAtomicLong("activity_sku_stock_count_key_9011").decrementAndGet();
        log.info("测试结果：{}", redissonClient.getAtomicLong("activity_sku_stock_count_key_9011").get());
    }

}
