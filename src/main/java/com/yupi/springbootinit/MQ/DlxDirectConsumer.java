package com.yupi.springbootinit.MQ;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

public class DlxDirectConsumer {
    private static final String WORK_EXCHANGE_NAME = "direct2_exchange";
    private static final String DLX_EXCHANGE_NAME = "Dlx_direct_exchange";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        // 创建两个通道
        Channel channel1 = connection.createChannel();
        Channel channel2 = connection.createChannel();
        // 创建交换机
        channel1.exchangeDeclare(WORK_EXCHANGE_NAME, "direct");

        // 指定死信队列参数
        Map<String, Object> args = new HashMap<>();
        //绑定死信交换机
        args.put("x-dead-letter-exchange", DLX_EXCHANGE_NAME);
        // 指定死信要转发到那个队列（如果dog队列任务失败，则将消息转发到WaiBao死信队列）
        args.put("x-dead-letter-routing-key", "WaiBao");

        Map<String, Object> args2 = new HashMap<>();
        args2.put("x-dead-letter-exchange", DLX_EXCHANGE_NAME);
        // 如果cat队列任务失败，则将消息转发到LaoBan死信队列
        args2.put("x-dead-letter-routing-key", "LaoBan");

        // 创建两个队列
        String queueName1 = "cat_queue";
        //指定死信要转发到那个队列（如果cat队列任务失败，并且任务报错时会向LaoBan这个队列发信息）
        channel1.queueDeclare(queueName1, true, false, false, args2);
        channel1.queueBind(queueName1, WORK_EXCHANGE_NAME, "cat");


        String queueName2 = "dog_queue";
        //指定死信要转发到那个队列（绑定到dog队列任务失败，并且任务报错时会向WaiBao这个队列发信息）
        channel2.queueDeclare(queueName2, true, false, false, args);
        channel1.queueBind(queueName2, WORK_EXCHANGE_NAME, "dog");


        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback1 = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            //拒绝信息
            channel1.basicNack(delivery.getEnvelope().getDeliveryTag(), false, false);
            System.out.println(" [cat] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        DeliverCallback deliverCallback2 = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            //拒绝信息
            channel2.basicNack(delivery.getEnvelope().getDeliveryTag(), false, false);
            System.out.println(" [dog] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        //自动确认改成false
        channel2.basicConsume(queueName2, false, deliverCallback2, consumerTag -> {
        });
        channel1.basicConsume(queueName1, false, deliverCallback1, consumerTag -> {
        });

    }
}
