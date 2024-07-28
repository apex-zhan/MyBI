package com.yupi.springbootinit.MQ;

import com.rabbitmq.client.*;

public class FanoutConsumer {


    private static final String EXCHANGE_NAME = "fanout-exchange";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        // 创建两个频道
        Channel channel1 = connection.createChannel();
        Channel channel2 = connection.createChannel();
        // 声明一个fanout交换机
        channel1.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        // 创建两个队列
        String queueName1 = "zxw";
        channel1.queueDeclare(queueName1, true, false, false, null);;
        channel1.queueBind(queueName1, EXCHANGE_NAME, "");
        String queueName2 = "kun";
        channel2.queueDeclare(queueName2, true, false, false, null);
        channel1.queueBind(queueName2, EXCHANGE_NAME, "");


        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        // 创建交付回调函数
        DeliverCallback deliverCallback1 = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("[zxw]Received " + message + "'");
        };
        DeliverCallback deliverCallback2 = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("[kun]Received " + message + "'");
        };
        // 创建消费者
        channel1.basicConsume(queueName1, true, deliverCallback1, consumerTag -> {
        });
        channel2.basicConsume(queueName2, true, deliverCallback2, consumerTag -> {
        });

    }
}

