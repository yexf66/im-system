package com.yexf.imtcp;

import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;

public class RedissonTest {


    @Test
    public void testRedisson() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.172.135:6379");
        StringCodec stringCodec = new StringCodec();
        config.setCodec(stringCodec);
        RedissonClient redissonClient = Redisson.create(config);
        RBucket<Object> im = redissonClient.getBucket("im");
        System.out.println(im.get());
        im.set("im");
        System.out.println(im.get());
    }
}
