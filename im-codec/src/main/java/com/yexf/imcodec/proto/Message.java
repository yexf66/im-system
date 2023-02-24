package com.yexf.imcodec.proto;

import lombok.Data;

/**
 * 自定义协议:消息
 */
@Data
public class Message {

    /**
     * 消息头
     */
    private MessageHeader messageHeader;

    /**
     * 消息体
     */
    private Object messagePack;

    @Override
    public String toString() {
        return "Message{" +
                "messageHeader=" + messageHeader +
                ", messagePack=" + messagePack +
                '}';
    }
}
