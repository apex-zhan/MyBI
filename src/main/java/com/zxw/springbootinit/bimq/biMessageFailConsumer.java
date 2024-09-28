package com.zxw.springbootinit.bimq;

import com.rabbitmq.client.Channel;
import com.zxw.springbootinit.constant.biconstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class biMessageFailConsumer {

    /**
     * 监听死信队列
     * @param message
     * @param channel
     * @param deliveryTag
     */
    @RabbitListener(queues = {biconstant.BI_QUEUE_NAME}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag){

    }
}
