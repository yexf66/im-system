package com.yexf.imcommon.enums;

/**
 * 设备多端登录枚举
 */
public enum DeviceMultiLoginEnum {

    /**
     * 单端登录 仅允许 Windows、Web、Android 或 iOS 单端登录。
     */
    ONE(1),

    /**
     * 双端登录 允许 Windows、Mac、Android 或 iOS 单端登录，同时允许与 Web 端同时在线。
     */
    TWO(2),

    /**
     * 三端登录 允许 Android 或 iOS 单端登录(互斥)，Windows 或者 Mac 单聊登录(互斥)，同时允许 Web 端同时在线
     */
    THREE(3),

    /**
     * 多端同时在线 允许 Windows、Mac、Web、Android 或 iOS 多端或全端同时在线登录
     */
    ALL(4);

    public final int mode;


    DeviceMultiLoginEnum(int mode) {
        this.mode = mode;
    }

//    public int getMode() {
//        return mode;
//    }

}
