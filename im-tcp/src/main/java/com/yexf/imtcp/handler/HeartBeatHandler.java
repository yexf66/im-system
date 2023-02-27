package com.yexf.imtcp.handler;

import com.yexf.imcommon.constants.Constants;
import com.yexf.imtcp.utils.SessionSocketHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

/**
 * 客户端心跳handler
 */
@Slf4j
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    private final int heartBeatTimeout;

    public HeartBeatHandler(int heartBeatTimeout) {
        this.heartBeatTimeout = heartBeatTimeout;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 判断evt是否是IdleStateEvent（用于触发用户事件，包含 读空闲/写空闲/读写空闲 ）
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                log.info("读空闲");
            } else if (event.state() == IdleState.WRITER_IDLE) {
                log.info("进入写空闲");
            } else if (event.state() == IdleState.ALL_IDLE) {
                Long lastReadTime = (Long) ctx.channel()
                        .attr(AttributeKey.valueOf(Constants.LAST_PING_TIME)).get();
                long now = System.currentTimeMillis();
                if (lastReadTime != null && now - lastReadTime > heartBeatTimeout) {
                    SessionSocketHolder.offlineUserSession((NioSocketChannel) ctx.channel());
                }
            }
        }

    }


}
