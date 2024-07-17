package com.yupi.springbootinit.controller;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 队列测试
 * @Profile({"dev","local"})只在特定的环境或配置文件下生效
 */
@RestController
@Slf4j
@RequestMapping("/queue")
@Profile({"dev","local"})
public class QueueController {
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @GetMapping("/add")
    //接受一个参数name，然后将任务添加到线程池中
    public void add(String name) {
        CompletableFuture.runAsync(() -> {
            log.info("任务执行中" + name + "线程名称：" + Thread.currentThread().getName());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // 异步任务在threadPoolExecutor中执行
        }, threadPoolExecutor);
    }

    @GetMapping("/get")
    // 该方法返回线程池的状态信息
    public String get() {
        // 创建一个HashMap存储线程池的状态信息
        HashMap<String, Object> HashMap = new HashMap<>();
        //获取线程池的队列长度，并放入HashMap中
        HashMap.put("queueSize", threadPoolExecutor.getQueue().size());
        // 获取线程池已接收的任务总数，并放入HashMap中
        HashMap.put("taskCount", threadPoolExecutor.getTaskCount());
        // 获取线程池中正在执行任务的线程数
        HashMap.put("activeCount", threadPoolExecutor.getActiveCount());
        // 获取线程池已完成的任务数
        HashMap.put("completedTaskCount", threadPoolExecutor.getCompletedTaskCount());
        //将hashmap转化成json字符串返回
        return JSONUtil.toJsonStr(HashMap);

    }
}
