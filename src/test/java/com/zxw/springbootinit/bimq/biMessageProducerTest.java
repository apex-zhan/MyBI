package com.zxw.springbootinit.bimq;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class biMessageProducerTest {
    @Resource
    private biMessageProducer biMessageProducer;

    @Test
    void sendMessage() {
        biMessageProducer.sendMessage("hello");
    }
}