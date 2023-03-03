package com.yexf.imtcp;

import com.yexf.imcommon.constants.Constants;
import com.yexf.imtcp.config.BootstrapConfig;
import com.yexf.imtcp.config.redis.RedisManager;
import com.yexf.imtcp.config.zookeeper.ZKRegistry;
import com.yexf.imtcp.config.zookeeper.ZKit;
import com.yexf.imtcp.mq.consumer.MessageConsumer;
import com.yexf.imtcp.service.ImServer;
import com.yexf.imtcp.service.ImWebsocketServer;
import com.yexf.imtcp.config.mq.MQFactory;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.net.InetAddress;

public class Starter {

    private final static Logger logger = LoggerFactory.getLogger(Starter.class);

    public static void main(String[] args) {
        try {
            Yaml yaml = new Yaml();
            InputStream inputStream = Starter.class.getResourceAsStream("/config.yaml");
            BootstrapConfig bootstrapConfig = yaml.loadAs(inputStream, BootstrapConfig.class);
            Constants.cache.put("nodeId", bootstrapConfig.getApp().getNodeId());
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            Constants.cache.put("ip", hostAddress);
            new ImServer(bootstrapConfig.getApp()).start();
            new ImWebsocketServer(bootstrapConfig.getApp()).start();
            RedisManager.init(bootstrapConfig);//init Redis
            MQFactory.init(bootstrapConfig.getApp().getRabbitmq());//init rabbitMQ
            MessageConsumer.init();
            registerZK(bootstrapConfig);
        } catch (Exception e) {
            logger.error("IM 服务启动失败", e);
            System.exit(500);
        }

    }

    public static void registerZK(BootstrapConfig config) {
        ZkClient zkClient = new ZkClient(config.getApp().getZookeeper().getZkAddr(),
                config.getApp().getZookeeper().getZkConnectTimeOut());
        ZKit zKit = new ZKit(zkClient);
        ZKRegistry registryZK = new ZKRegistry(zKit, config.getApp());
        Thread thread = new Thread(registryZK);
        thread.start();
    }
}
