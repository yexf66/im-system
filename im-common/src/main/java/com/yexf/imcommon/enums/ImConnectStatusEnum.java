package com.yexf.imcommon.enums;

public enum ImConnectStatusEnum {

    /**
     * 管道链接状态,1=在线，2=离线。。
     */
    ONLINE(1),

    OFFLINE(2),
    ;

    private final int code;

    ImConnectStatusEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
