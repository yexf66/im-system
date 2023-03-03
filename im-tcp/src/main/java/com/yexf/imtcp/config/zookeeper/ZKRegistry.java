package com.yexf.imtcp.config.zookeeper;

import com.yexf.imcommon.constants.Constants;
import com.yexf.imcommon.constants.ZkConstants;
import com.yexf.imtcp.config.BootstrapConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ZKRegistry implements Runnable {
    private final ZKit zKit;

    private final BootstrapConfig.AppConfig appConfig;

    public ZKRegistry(ZKit zKit, BootstrapConfig.AppConfig appConfig) {
        this.zKit = zKit;
        this.appConfig = appConfig;
    }

    @Override
    public void run() {
        zKit.initRootNode();
        String ip = Constants.cache.get("ip");
        String tcpPath = ZkConstants.ZkRoot + ZkConstants.ZkRootTcp + "/" + ip + ":" + appConfig.getTcpPort();
        zKit.register(tcpPath);
        log.info("Registry zookeeper tcpPath success, msg=[{}]", tcpPath);

        String webPath =
                ZkConstants.ZkRoot + ZkConstants.ZkRootWeb + "/" + ip + ":" + appConfig.getWebSocketPort();
        zKit.register(webPath);
        log.info("Registry zookeeper webPath success, msg=[{}]", webPath);

    }
}
