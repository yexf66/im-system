package com.yexf.imtcp.listener;

import com.alibaba.fastjson.JSONObject;
import com.yexf.imcommon.constants.Constants;
import com.yexf.imcommon.constants.RedisConstants;
import com.yexf.imcommon.enums.ClientTypeEnum;
import com.yexf.imcommon.enums.DeviceMultiLoginEnum;
import com.yexf.imcommon.model.UserClientDTO;
import com.yexf.imtcp.config.redis.RedisManager;
import com.yexf.imtcp.utils.SessionSocketHolder;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RTopic;

import java.util.List;

@Slf4j
public class UserLoginMessageListener {

    private final int loginModel;

    public UserLoginMessageListener(int loginModel) {
        this.loginModel = loginModel;
    }

    public void listen() {
        RTopic topic = RedisManager.getRedissonClient().getTopic(RedisConstants.UserLoginChannel);
        topic.addListener(String.class, (charSequence, msg) -> {
            log.info("收到用户上线通知：" + msg);
            UserClientDTO loginMsg = JSONObject.parseObject(msg, UserClientDTO.class);
            List<NioSocketChannel> channelsByUser = SessionSocketHolder.getChannelsByUser(loginMsg.getAppId(),
                    loginMsg.getUserId());
            if (loginModel == DeviceMultiLoginEnum.ONE.mode) {
                //单端登录，踢掉其它客户端
                channelsByUser.forEach(ch -> {
                    Integer clientType = (Integer) ch.attr(AttributeKey.valueOf(Constants.CLIENT_TYPE)).get();
                    String imei = (String) ch.attr(AttributeKey.valueOf(Constants.IMEI)).get();
                    if (!clientType.equals(loginMsg.getClientType()) || !imei.equals(loginMsg.getImei())) {
                        //channel 不是当前设备的，踢掉
                        //TODO 踢掉
                    }
                });

            } else if (loginModel == DeviceMultiLoginEnum.TWO.mode) {
                if (loginMsg.getClientType().equals(ClientTypeEnum.WEB.getCode())) {
                    //web的只踢其他web的
                    channelsByUser.forEach(ch -> {
                        Integer clientType = (Integer) ch.attr(AttributeKey.valueOf(Constants.CLIENT_TYPE)).get();
                        String imei = (String) ch.attr(AttributeKey.valueOf(Constants.IMEI)).get();
                        if (clientType.equals(ClientTypeEnum.WEB.getCode()) && !imei.equals(loginMsg.getImei())) {
                            //是web 但是不是当前设备的，踢掉
                            //TODO 踢掉
                        }
                    });
                } else {
                    //当前登录的不是web的，踢掉其他所有不是web，且IMEI不是当前的
                    channelsByUser.forEach(ch -> {
                        Integer clientType = (Integer) ch.attr(AttributeKey.valueOf(Constants.CLIENT_TYPE)).get();
                        if (clientType.equals(ClientTypeEnum.WEB.getCode())) {
                            //web 和 当前（不是web）的不互斥，不管
                            return;
                        }
                        String imei = (String) ch.attr(AttributeKey.valueOf(Constants.IMEI)).get();
                        if (!clientType.equals(loginMsg.getClientType()) || !imei.equals(loginMsg.getImei())) {
                            //channel 不是当前设备的，踢掉
                            //TODO 踢掉
                        }
                    });
                }


            } else if (loginModel == DeviceMultiLoginEnum.THREE.mode) {
                //三端登录：web、移动设备、PC
                // 移动设备 android和ios 互斥
                // PC windows和Mac互斥


            }


        });


    }


}
