package com.yexf.imcommon.constants;

public class RedisConstants {

    /**
     * 用户session:  UserSession:appId:userId UserSession:1000:xxxxx1
     */
    public static final String USER_SESSION = "UserSession:%d:%s";

    /**
     * redis 用户session 多设备的key  设备类型:imei 表示唯一端+设备
     */
    public static final String USER_SESSION_ITEM = "%d:%s";

    public static final String UserLoginChannel
            = "signal:channel:LOGIN_USER_INNER_QUEUE";


}
