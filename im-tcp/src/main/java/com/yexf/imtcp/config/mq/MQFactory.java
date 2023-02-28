package com.yexf.imtcp.config.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.yexf.imtcp.config.BootstrapConfig;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

/**
 * MQ 连接工厂
 */
public class MQFactory {

    private static ConnectionFactory factory = null;

    private static Channel defaultChannel;

    private static final Map<String, Channel> channelMap = new ConcurrentHashMap<>();

    private static Connection getConnection() throws IOException, TimeoutException {
        return factory.newConnection();
    }

    public static Channel getChannel(String channelName) throws IOException, TimeoutException {
        Channel channel = channelMap.get(channelName);
        if (channel == null) {
            channel = getConnection().createChannel();
            channelMap.put(channelName, channel);
        }
        return channel;
    }

    public static void init(BootstrapConfig.RabbitMqConfig config) {
        if (factory == null) {
            factory = new ConnectionFactory();
            factory.setHost(config.getHost());
            factory.setPort(config.getPort());
            factory.setUsername(config.getUserName());
            factory.setPassword(config.getPassword());
            factory.setVirtualHost(config.getVirtualHost());
        }
    }
}
