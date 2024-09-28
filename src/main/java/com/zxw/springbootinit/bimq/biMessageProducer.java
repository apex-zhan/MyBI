package com.zxw.springbootinit.bimq;

import com.zxw.springbootinit.constant.biconstant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * bi 消息生产者
 */
@Component
public class biMessageProducer {
    @Resource
    private RabbitTemplate rabbitmqTemplate;

    /**
     * 发送信息的方法
     */
    public void sendMessage(String message) {
        rabbitmqTemplate.convertAndSend(biconstant.BI_EXCHANGE_NAME, biconstant.BI_ROUTING_KEY, message);
    }

}
