package com.yexf.imtcp.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class BootstrapConfig {

    private AppConfig app;

    @Data
    public static class AppConfig {
        private String nodeId;//节点id 唯一，且固定，每次启动都固定这个nodeId,当前应用为有状态服务
        private Integer tcpPort;
        private Integer webSocketPort;
        private Integer bossThreadSize;
        private Integer workThreadSize;
        private Integer loginModel;
        private Integer heartBeatTimeout;//心跳超时时间（毫秒）

        /**
         * redis配置
         */
        private RedisConfig redis;

        /**
         * rabbitMQ 配置
         */
        private RabbitMqConfig rabbitmq;

        /**
         * zookeeper 配置
         */
        private ZookeeperConfig zookeeper;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RedisConfig {

        /**
         * 单机模式：single 哨兵模式：sentinel 集群模式：cluster
         */
        private String mode;
        /**
         * 数据库
         */
        private Integer database;
        /**
         * 密码
         */
        private String password;
        /**
         * 超时时间
         */
        private Integer timeout;
        /**
         * 最小空闲数
         */
        private Integer poolMinIdle;
        /**
         * 连接超时时间(毫秒)
         */
        private Integer poolConnTimeout;
        /**
         * 连接池大小
         */
        private Integer poolSize;

        /**
         * redis单机配置
         */
        private RedisSingle single;

    }

    /**
     * redis单机配置
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RedisSingle {
        /**
         * 地址
         */
        private String address;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RabbitMqConfig {
        private String host;
        private Integer port;
        private String virtualHost;
        private String userName;
        private String password;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ZookeeperConfig {
        private String zkAddr;
        private Integer zkConnectTimeOut;
    }
}
