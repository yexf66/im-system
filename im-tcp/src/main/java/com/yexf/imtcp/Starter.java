package com.yexf.imtcp;

import com.yexf.imtcp.config.BootstrapConfig;
import com.yexf.imtcp.config.redis.RedisManager;
import com.yexf.imtcp.mq.consumer.MessageConsumer;
import com.yexf.imtcp.service.ImServer;
import com.yexf.imtcp.service.ImWebsocketServer;
import com.yexf.imtcp.config.mq.MQFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

public class Starter {

    private final static Logger logger = LoggerFactory.getLogger(Starter.class);

    public static void main(String[] args) {
        try {
            Yaml yaml = new Yaml();
            InputStream inputStream = Starter.class.getResourceAsStream("/config.yaml");
            BootstrapConfig bootstrapConfig = yaml.loadAs(inputStream, BootstrapConfig.class);
            new ImServer(bootstrapConfig.getApp()).start();
            new ImWebsocketServer(bootstrapConfig.getApp()).start();
            RedisManager.init(bootstrapConfig);//init Redis
            MQFactory.init(bootstrapConfig.getApp().getRabbitmq());//init rabbitMQ
            MessageConsumer.init();
        } catch (Exception e) {
            logger.error("IM 服务启动失败", e);
            System.exit(500);
        }

    }
}
