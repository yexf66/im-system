package com.yexf.imtcp.config;

import lombok.Data;

@Data
public class BootstrapConfig {

    private AppConfig app;

    @Data
    public static class AppConfig {
        private Integer tcpPort;
        private Integer webSocketPort;
        private Integer bossThreadSize;
        private Integer workThreadSize;
    }
}
