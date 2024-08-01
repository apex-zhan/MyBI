package com.yupi.springbootinit.MQ;

import com.rabbitmq.client.*;

public class DirectConsumer {

    private static final String EXCHANGE_NAME = "direct_exchange";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        // 创建两个通道
        Channel channel1 = connection.createChannel();
        Channel channel2= connection.createChannel();
        // 创建交换机
        channel1.exchangeDeclare(EXCHANGE_NAME, "direct");

        // 创建两个队列
        String queueName1 = "thiszxw_queue";
        channel1.queueDeclare(queueName1, true, false, false, null);;
        // 绑定队列
        channel1.queueBind(queueName1, EXCHANGE_NAME, "thiszxw");
        String queueName2 = "ikun_queue";
        channel2.queueDeclare(queueName2, true, false, false, null);
        // 绑定队列
        channel1.queueBind(queueName2, EXCHANGE_NAME, "ikun");


        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback1 = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [thiszxw] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        DeliverCallback deliverCallback2 = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [ikun] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };

        channel2.basicConsume(queueName2, true, deliverCallback2, consumerTag -> {
        });
        channel1.basicConsume(queueName1, true, deliverCallback1, consumerTag -> {});
    }
}
