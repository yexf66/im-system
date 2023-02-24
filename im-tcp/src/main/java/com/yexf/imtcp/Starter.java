package com.yexf.imtcp;

import com.yexf.imtcp.config.BootstrapConfig;
import com.yexf.imtcp.service.ImServer;
import com.yexf.imtcp.service.ImWebsocketServer;
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
        } catch (Exception e) {
            logger.error("IM 服务启动失败", e);
            System.exit(500);
        }

    }
}
