package com.yexf.imtcp.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yexf.imcodec.pack.LoginPack;
import com.yexf.imcodec.proto.Message;
import com.yexf.imcommon.constants.RedisConstants;
import com.yexf.imcommon.enums.ImConnectStatusEnum;
import com.yexf.imcommon.enums.command.SystemCommand;
import com.yexf.imcommon.model.UserSession;
import com.yexf.imtcp.redis.RedisManager;
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
            System.out.println(loginPack);
            String userId = loginPack.getUserId();
            ctx.channel().attr(AttributeKey.valueOf("userId")).set(userId);//为channel设置用户id属性
            Integer appId = message.getMessageHeader().getAppId();

            UserSession userSession = new UserSession();
            userSession.setUserId(userId);
            userSession.setAppId(appId);
            userSession.setClientType(message.getMessageHeader().getClientType());
            userSession.setVersion(message.getMessageHeader().getVersion());
            userSession.setConnectState(ImConnectStatusEnum.ONLINE.getCode());
            RedissonClient redissonClient = RedisManager.getRedissonClient();
            RMap<String, String> map = redissonClient.getMap(String.format(RedisConstants.USER_SESSION, userId, appId));
            map.put(message.getMessageHeader().getClientType() + "", JSONObject.toJSONString(userSession));
            SessionSocketHolder.put(userId, (NioSocketChannel) ctx.channel());//存储channel
        }
    }
}
