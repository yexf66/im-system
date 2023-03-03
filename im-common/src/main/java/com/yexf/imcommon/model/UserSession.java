package com.yexf.imcommon.model;

import lombok.Data;

@Data
public class UserSession {
    private String userId;

    private Integer appId;
    /**
     * 客户端类型
     */
    private Integer clientType;
    /**
     * sdk 版本号
     */
    private Integer version;
    /**
     * 连接状态
     * 1：在线 0: 离线
     */
    private Integer connectState;

    /**
     * 服务启动时候设置每台服务的唯一id，用来记录用户在哪台机器上
     */
    private String nodeId;

    private String nodeIp;


}
