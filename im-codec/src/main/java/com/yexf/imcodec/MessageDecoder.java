package com.yexf.imcodec;

import com.alibaba.fastjson.JSONObject;
import com.yexf.imcodec.proto.Message;
import com.yexf.imcodec.proto.MessageHeader;
import com.yexf.imcodec.utils.ByteBuf2MessageUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 消息解码类
 */
public class MessageDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        //请求头（指令|版本|clientType|消息解析类型|appId|imei长度|bodylen）+ imei号 + 请求体
        //请求头中每个字段占4个byte
        Message message = ByteBuf2MessageUtil.transition(in);
        if (message != null) {
            out.add(message);
        }
    }
}
