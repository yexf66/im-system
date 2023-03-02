package com.yexf.imtcp.mq.consumer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.yexf.imcommon.constants.MQConstants;
import com.yexf.imtcp.config.mq.MQFactory;

import java.io.IOException;

public class MessageConsumer {

    public static void consume() {
        try {
            String queueName = MQConstants.MessageService2Im;
            Channel channel = MQFactory.getChannel(queueName);
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, queueName, "");
            channel.basicConsume(queueName, true, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    //todo 處理消息服務發來的消息
                    System.out.println("收到消息:" + new String(body));
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void init() {
        consume();
    }

}
