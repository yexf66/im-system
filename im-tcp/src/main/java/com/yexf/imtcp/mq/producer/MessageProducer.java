package com.yexf.imtcp.mq.producer;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.yexf.imcodec.proto.Message;
import com.yexf.imtcp.config.mq.MQFactory;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class MessageProducer {

    //    public static void sendMessage(Message message, Integer command) {
    public static void sendMessage(Object message) {
        String channelName = "";
        try {
            Channel channel = MQFactory.getChannel(channelName);
            channel.basicPublish(channelName, "", null,
                    JSONObject.toJSONString(message).getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error("发送消息出现异常{}", e.getMessage());
            e.printStackTrace();
        }

    }
}
