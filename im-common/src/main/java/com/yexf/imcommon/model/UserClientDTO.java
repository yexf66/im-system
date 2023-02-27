package com.yexf.imcommon.model;

import lombok.Data;

/**
 * SessionSocketHolder 会话channel存储器 的key
 */
@Data
public class UserClientDTO {

    private Integer appId;

    private Integer clientType;

    private String userId;
//
//    private String imei;

}
