package com.zxw.springbootinit.manager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * 测试 AiManager
 *
 * @SpringBootTest注解，我们可以方便地对Spring Boot应用程序进行集成测试和端到端测试。
 */
@SpringBootTest
class AiManagerTest {
    @Resource
    private AiManager AiManager;

    @Test
    void doChat() {
        String answer = AiManager.doChat( 1809441063995113473L,
                "分析需求:\n" +
                "分析网站用户的增长情况\n" +
                "原始数据:\n" +
                "日期,用户数\n" +
                "1号,10\n" +
                "2号,20\n" +
                "3号,3日");
        System.out.println(answer);
    }
}