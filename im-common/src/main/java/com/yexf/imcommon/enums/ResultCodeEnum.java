package com.yexf.imcommon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public enum ResultCodeEnum {

    SUCCESS(200, "success"),

    /**
     * 请求未授权
     */
    UNAUTHORIZED(401, "unauthorized"),
    /**
     * 不支持当前请求方法
     */
    METHOD_NOT_SUPPORTED(405, "Request method not support"),

    /**
     * 不支持当前媒体类型
     */
    MEDIA_TYPE_NOT_SUPPORTED(415, "media type not support"),

    /**
     * 请求被拒绝, 表示用户得到授权（与401错误相对），但是访问是被禁止的
     */
    FORBIDDEN(403, "forbidden"),


    /**
     * 服务器异常
     */
    INTERNAL_SERVER_ERROR(500, "server is busy,please try again later!"),

    /**
     * 缺少必要的请求参数
     */
    PARAM_MISS(400, "param miss"),

    /**
     * 请求参数类型错误
     */
    PARAM_TYPE_ERROR(400, "param error");


    /**
     * code编码
     */
    private final int code;
    /**
     * 中文信息描述
     */
    private final String message;
}
