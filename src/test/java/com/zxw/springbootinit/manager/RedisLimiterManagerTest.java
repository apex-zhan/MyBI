package com.zxw.springbootinit.manager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class RedisLimiterManagerTest {
    @Resource
    private RedisLimiterManager redisLimiterManager;
    @Test
    void doRateLimit() throws InterruptedException {
        //模拟一下操作
        String key= "1";
        //模拟操作
        for (int i = 0; i < 5; i++) {
            redisLimiterManager.doRateLimit(key);
            System.out.println("操作成功");
        }
        Thread.sleep(1000);
        for (int i = 0; i < 5; i++) {
            redisLimiterManager.doRateLimit(key);
            System.out.println("操作成功");
        }
    }
}