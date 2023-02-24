package com.yexf.imtcp.utils;

import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 会话channel存储器
 */
public class SessionSocketHolder {
    private static final Map<String, NioSocketChannel> CHANNELS = new ConcurrentHashMap<>();

    public static void put(String userId, NioSocketChannel channel) {
        CHANNELS.put(userId, channel);
    }

    public static NioSocketChannel get(String userId) {
        return CHANNELS.get(userId);
    }

}
