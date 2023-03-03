package com.yexf.imcommon.enums;

/**
 * 设备端类型
 */
public enum ClientTypeEnum {
    WEBAPI(0, "webApi"),
    WEB(1, "web"),
    IOS(2, "Ios"),
    ANDROID(3, "android"),
    WINDOWS(4, "windows"),
    MAC(5, "mac"),
    ;

    private final int code;
    private final String value;

    ClientTypeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }


}
