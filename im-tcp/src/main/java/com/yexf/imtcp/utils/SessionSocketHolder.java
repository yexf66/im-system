package com.yexf.imtcp.utils;

import com.alibaba.fastjson.JSONObject;
import com.yexf.imcommon.constants.Constants;
import com.yexf.imcommon.constants.RedisConstants;
import com.yexf.imcommon.enums.ImConnectStatusEnum;
import com.yexf.imcommon.model.UserClientDTO;
import com.yexf.imcommon.model.UserSession;
import com.yexf.imtcp.config.redis.RedisManager;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 会话channel存储器
 */
public class SessionSocketHolder {
    private static final Map<UserClientDTO, NioSocketChannel> CHANNELS = new ConcurrentHashMap<>();

    public static void put(int appId, String userId, int clientType, String imei, NioSocketChannel channel) {
        CHANNELS.put(buildUserClientInfo(appId, userId, clientType, imei), channel);
    }

    public static void put(UserClientDTO key, NioSocketChannel channel) {
        CHANNELS.put(key, channel);
    }


    public static NioSocketChannel get(int appId, String userId, int clientType, String imei) {
        return CHANNELS.get(buildUserClientInfo(appId, userId, clientType, imei));
    }

    public static void remove(int appId, String userId, int clientType, String imei) {
        CHANNELS.remove(buildUserClientInfo(appId, userId, clientType, imei));
    }

    public static List<NioSocketChannel> getChannelsByUser(int appId, String userId) {
        Set<UserClientDTO> sessions = CHANNELS.keySet();
        List<NioSocketChannel> channels = new ArrayList<>();
        sessions.forEach(session -> {
            if (session.getUserId().equals(userId) && session.getAppId().equals(appId)) {
                channels.add(CHANNELS.get(session));
            }
        });
        return channels;
    }

    public static void remove(NioSocketChannel channel) {
        CHANNELS.entrySet().removeIf(
                userClientDTONioSocketChannelEntry -> userClientDTONioSocketChannelEntry.getValue() == channel);
    }


    public static UserClientDTO buildUserClientInfo(int appId, String userId, int clientType, String imei) {
        UserClientDTO key = new UserClientDTO();
        key.setAppId(appId);
        key.setUserId(userId);
        key.setClientType(clientType);
        key.setImei(imei);
        return key;
    }

    /**
     * 用户登出， 删除内存中channel并关闭，删除redis会话
     */
    public static void removeUserSession(NioSocketChannel channel) {
        //删除内存中的channel
        String userId = (String) channel.attr(AttributeKey.valueOf(Constants.USER_ID)).get();
        Integer appId = (Integer) channel.attr(AttributeKey.valueOf(Constants.APP_ID)).get();
        Integer clientType = (Integer) channel.attr(AttributeKey.valueOf(Constants.CLIENT_TYPE)).get();
        String imei = (String) channel.attr(AttributeKey.valueOf(Constants.IMEI)).get();
        SessionSocketHolder.remove(appId, userId, clientType, imei);
        //删除redis 中的session
        RedissonClient redissonClient = RedisManager.getRedissonClient();
        RMap<String, String> map = redissonClient.getMap(String.format(RedisConstants.USER_SESSION, appId, userId));
        String itemKey = String.format(RedisConstants.USER_SESSION_ITEM, clientType, imei);
        map.remove(itemKey);
        //关闭channel
        channel.close();
    }


    /**
     * 用户离线，删除内存中channel并关闭，redis 中用户状态设为离线
     */
    public static void offlineUserSession(NioSocketChannel channel) {
        //删除内存中的channel
        String userId = (String) channel.attr(AttributeKey.valueOf(Constants.USER_ID)).get();
        Integer appId = (Integer) channel.attr(AttributeKey.valueOf(Constants.APP_ID)).get();
        Integer clientType = (Integer) channel.attr(AttributeKey.valueOf(Constants.CLIENT_TYPE)).get();
        String imei = (String) channel.attr(AttributeKey.valueOf(Constants.IMEI)).get();
        SessionSocketHolder.remove(appId, userId, clientType, imei);

        //删除redis 中的session
        RedissonClient redissonClient = RedisManager.getRedissonClient();
        RMap<String, String> map = redissonClient.getMap(String.format(RedisConstants.USER_SESSION, appId, userId));
        String itemKey = String.format(RedisConstants.USER_SESSION_ITEM, clientType, imei);
        String sessionJson = map.get(itemKey);
        if (StringUtils.isNotBlank(sessionJson)) {
            UserSession userSession = JSONObject.parseObject(sessionJson, UserSession.class);
            userSession.setConnectState(ImConnectStatusEnum.OFFLINE.getCode());
            map.put(itemKey, JSONObject.toJSONString(userSession));
        }
        //关闭channel
        channel.close();
    }
}
