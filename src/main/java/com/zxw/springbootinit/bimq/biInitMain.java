package com.zxw.springbootinit.bimq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.zxw.springbootinit.constant.biconstant;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * bimq初始化
 */
@Component
public class biInitMain {
    public static void main(String[] args) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(biconstant.BI_SERVER_HOST);
            // 创建连接
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            String EXCHANGE_NAME = biconstant.BI_EXCHANGE_NAME;
            channel.exchangeDeclare(EXCHANGE_NAME, biconstant.BI_EXCHANGE_TYPE);
            // 创建队列
            String QUEUE_NAME = biconstant.BI_QUEUE_NAME;
            Map<String, Object> map = new HashMap<>();
            //普通队列绑定死信交换机
            map.put("x-dead-letter-exchange", biconstant.BI_EXCHANGE_NAME_DLX);
            map.put("x-dead-letter-routing-key", biconstant.BI_ROUTING_KEY_DLX);
            channel.queueDeclare(QUEUE_NAME, true, false, false, map);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, biconstant.BI_ROUTING_KEY);

            // 创建死信队列
            String QUEUE_NAME_DLX = biconstant.BI_QUEUE_NAME_DLX;
            channel.queueDeclare(QUEUE_NAME_DLX, true, false, false, null);
            channel.exchangeDeclare(biconstant.BI_EXCHANGE_NAME_DLX, biconstant.BI_EXCHANGE_TYPE);
            channel.queueBind(QUEUE_NAME_DLX, biconstant.BI_EXCHANGE_NAME_DLX, biconstant.BI_ROUTING_KEY_DLX);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
