package com.yexf.imtcp.utils;

import com.alibaba.fastjson.JSONObject;
import com.yexf.imcommon.constants.Constants;
import com.yexf.imcommon.constants.RedisConstants;
import com.yexf.imcommon.enums.ImConnectStatusEnum;
import com.yexf.imcommon.model.UserClientDTO;
import com.yexf.imcommon.model.UserSession;
import com.yexf.imtcp.redis.RedisManager;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 会话channel存储器
 */
public class SessionSocketHolder {
    private static final Map<UserClientDTO, NioSocketChannel> CHANNELS = new ConcurrentHashMap<>();

    public static void put(int appId, String userId, int clientType, NioSocketChannel channel) {
        CHANNELS.put(buildKey(appId, userId, clientType), channel);
    }

    public static NioSocketChannel get(int appId, String userId, int clientType) {
        return CHANNELS.get(buildKey(appId, userId, clientType));
    }

    public static void remove(int appId, String userId, int clientType) {
        CHANNELS.remove(buildKey(appId, userId, clientType));
    }

    public static void remove(NioSocketChannel channel) {
        CHANNELS.entrySet().removeIf(
                userClientDTONioSocketChannelEntry -> userClientDTONioSocketChannelEntry.getValue() == channel);
    }


    public static UserClientDTO buildKey(int appId, String userId, int clientType) {
        UserClientDTO key = new UserClientDTO();
        key.setAppId(appId);
        key.setUserId(userId);
        key.setClientType(clientType);
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
        SessionSocketHolder.remove(appId, userId, clientType);
        //删除redis 中的session
        RedissonClient redissonClient = RedisManager.getRedissonClient();
        RMap<String, String> map = redissonClient.getMap(String.format(RedisConstants.USER_SESSION, appId, userId));
        map.remove(clientType.toString());
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
        SessionSocketHolder.remove(appId, userId, clientType);
        //删除redis 中的session
        RedissonClient redissonClient = RedisManager.getRedissonClient();
        RMap<String, String> map = redissonClient.getMap(String.format(RedisConstants.USER_SESSION, appId, userId));
        String sessionJson = map.get(clientType + "");
        if (StringUtils.isNotBlank(sessionJson)) {
            UserSession userSession = JSONObject.parseObject(sessionJson, UserSession.class);
            userSession.setConnectState(ImConnectStatusEnum.OFFLINE.getCode());
            map.put(clientType + "", JSONObject.toJSONString(userSession));
        }
        //关闭channel
        channel.close();
    }
}
