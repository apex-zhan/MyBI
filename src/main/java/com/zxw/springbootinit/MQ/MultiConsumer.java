package com.zxw.springbootinit.MQ;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.jetbrains.annotations.NotNull;

public class MultiConsumer {

    private static final String TASK_QUEUE_NAME = "multi_queue";

    public static void main(String[] argv) throws Exception {
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        final Connection connection = factory.newConnection();
        for (int i = 0; i < 2; i++) {
            final Channel channel = connection.createChannel();
            // durable 参数设置为 true，服务器重启后队列不丢失
            channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
            // 参数设置为 1, 确保一次只处理一条消息
            channel.basicQos(1);
            DeliverCallback deliverCallback = getDeliverCallback(i, channel);
            // 开始消费消息,传入队列名称,是否自动确认,投递回调和消费者取消回调
            channel.basicConsume(TASK_QUEUE_NAME, false, deliverCallback, consumerTag -> {
            });
        }
    }

    @NotNull
    private static DeliverCallback getDeliverCallback(int i, Channel channel) {
        int finalI = i;
        // 消费回调
        return (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            try {
                System.out.println(" [x] Received '" + "编号" + finalI + "：" + message + "'");
                // 消息确认,第一个参数为消息的标识,第二个参数为是否批量确认
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                // 处理工作,模拟处理消息所花费的时间,机器处理能力有限(接收一条消息,20秒后再接收下一条消息)
                Thread.sleep(20000);
//                doWork(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
                //发生异常后,消息确认,第一个参数为消息的标识,第二个参数为是否批量确认,第三个参数为是否重新入队
                channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, false);
            } finally {
                System.out.println(" [x] Done");
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };
    }
}

//    private static void doWork(String task) {
//        for (char ch : task.toCharArray()) {
//            if (ch == '.') {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException _ignored) {
//                    Thread.currentThread().interrupt();
//                }
//            }
//        }
//    }
//}