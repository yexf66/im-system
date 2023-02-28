package com.yexf.imtcp.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yexf.imcodec.pack.LoginPack;
import com.yexf.imcodec.proto.Message;
import com.yexf.imcommon.constants.Constants;
import com.yexf.imcommon.constants.RedisConstants;
import com.yexf.imcommon.enums.ImConnectStatusEnum;
import com.yexf.imcommon.enums.command.SystemCommand;
import com.yexf.imcommon.model.UserSession;
import com.yexf.imtcp.config.redis.RedisManager;
import com.yexf.imtcp.utils.SessionSocketHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<Message> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception {
        Integer command = message.getMessageHeader().getCommand();
        if (command == SystemCommand.LOGIN.getCommand()) {
            LoginPack loginPack = JSON.toJavaObject((JSONObject) message.getMessagePack(), LoginPack.class);
            String userId = loginPack.getUserId();
            Integer appId = message.getMessageHeader().getAppId();
            Integer clientType = message.getMessageHeader().getClientType();
            //为channel设置属性
            ctx.channel().attr(AttributeKey.valueOf(Constants.USER_ID)).set(userId);
            ctx.channel().attr(AttributeKey.valueOf(Constants.APP_ID)).set(appId);
            ctx.channel().attr(AttributeKey.valueOf(Constants.CLIENT_TYPE)).set(clientType);
//            ctx.channel().attr(AttributeKey.valueOf(Constants.LAST_PING_TIME)).set(System.currentTimeMillis());
            UserSession userSession = new UserSession();
            userSession.setUserId(userId);
            userSession.setAppId(appId);
            userSession.setClientType(clientType);
            userSession.setVersion(message.getMessageHeader().getVersion());
            userSession.setConnectState(ImConnectStatusEnum.ONLINE.getCode());
            RedissonClient redissonClient = RedisManager.getRedissonClient();
            RMap<String, String> map = redissonClient.getMap(String.format(RedisConstants.USER_SESSION, appId, userId));
            map.put(clientType.toString(), JSONObject.toJSONString(userSession));
            SessionSocketHolder.put(appId, userId, clientType, (NioSocketChannel) ctx.channel());//存储channel
        } else if (command == SystemCommand.LOGOUT.getCommand()) {
            SessionSocketHolder.removeUserSession((NioSocketChannel) ctx.channel());
        } else if (command == SystemCommand.PING.getCommand()) {
            //往channel 里面设置上次ping 的时间
            ctx.channel().attr(AttributeKey.valueOf(Constants.LAST_PING_TIME)).set(System.currentTimeMillis());
        }
    }
}
