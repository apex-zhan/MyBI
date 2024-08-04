package com.zxw.springbootinit.MQ;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DlxDirectProducer {
    private static final String WORK_EXCHANGE_NAME = "direct2_exchange";
    private static final String DLX_EXCHANGE_NAME = "Dlx_direct_exchange";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(DLX_EXCHANGE_NAME, "direct");

            String queueName = "laoban_dlx_queue";
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, DLX_EXCHANGE_NAME, "LaoBan");

            String queueName2 = "waibao_dlx_queue";
            channel.queueDeclare(queueName2, true, false, false, null);
            channel.queueBind(queueName2, DLX_EXCHANGE_NAME, "WaiBao");

            //监听
            DeliverCallback laobandeliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                //拒绝信息
                channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, false);
                System.out.println(" [laoban] Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
            };
            DeliverCallback waibapdeliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                //拒绝信息
                channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, false);
                System.out.println(" [waibao] Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
            };
            //自动确认改成false
            channel.basicConsume(queueName2, false, waibapdeliverCallback, consumerTag -> {
            });
            channel.basicConsume(queueName, false, laobandeliverCallback, consumerTag -> {
            });


            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String userInput = scanner.nextLine();
                String[] split = userInput.split(" ");
                if (split.length < 1) {
                    continue;
                }
                String message = split[0];
                String routingKey = split[1];

                channel.basicPublish(WORK_EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
                System.out.println(" [x] Sent '" + message + "' with routing:'" + routingKey + "'");
            }
        }
    }

}