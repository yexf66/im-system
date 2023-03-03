package com.yexf.imtcp.config.redis;


import com.yexf.imtcp.config.BootstrapConfig;
import com.yexf.imtcp.listener.UserLoginMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;

/**
 * Redis 配置类
 */
@Slf4j
public class RedisManager {

    private static RedissonClient redissonClient;

    public static void init(BootstrapConfig config) {
        SingleClientStrategy singleClientStrategy = new SingleClientStrategy();
        redissonClient = singleClientStrategy.getRedissonClient(config.getApp().getRedis());
        UserLoginMessageListener userLoginMessageListener = new UserLoginMessageListener(config.getApp().getLoginModel());
        userLoginMessageListener.listen();
        log.info("Redis connect success");
    }

    public static RedissonClient getRedissonClient() {
        return redissonClient;
    }

}
