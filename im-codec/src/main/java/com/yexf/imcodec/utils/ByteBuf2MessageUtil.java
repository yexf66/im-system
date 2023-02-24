package com.yexf.imcodec.utils;

import com.alibaba.fastjson.JSONObject;
import com.yexf.imcodec.proto.Message;
import com.yexf.imcodec.proto.MessageHeader;
import io.netty.buffer.ByteBuf;

/**
 * 自定义协议 数据包解析工具类
 */
public class ByteBuf2MessageUtil {
    public static Message transition(ByteBuf in) {
        //请求头（指令|版本|clientType|消息解析类型|appId|imei长度|bodylen）+ imei号 + 请求体
        //请求头中每个字段占4个byte
        if (in.readableBytes() > 28) {
            int command = in.readInt();
            int version = in.readInt();
            int clientType = in.readInt();
            int messageType = in.readInt();
            int appId = in.readInt();
            int imeiLength = in.readInt();
            int bodyLength = in.readInt();

            if (in.readableBytes() < imeiLength + bodyLength) {
                in.resetReaderIndex();
                return null;
            }
            byte[] imeiData = new byte[imeiLength];
            in.readBytes(imeiData);
            byte[] bodyData = new byte[bodyLength];
            in.readBytes(bodyData);
            MessageHeader header = new MessageHeader();
            header.setCommand(command);
            header.setVersion(version);
            header.setClientType(clientType);
            header.setAppId(appId);
            header.setMessageType(messageType);
            header.setImeiLength(imeiLength);
            header.setLength(bodyLength);
            header.setImei(new String(imeiData));

            Message message = new Message();
            message.setMessageHeader(header);

            if (messageType == 0x0) {
                String body = new String(bodyData);
                JSONObject parse = (JSONObject) JSONObject.parse(body);
                message.setMessagePack(parse);
            }
            in.markReaderIndex();
            return message;
        }
        return null;
    }

}
