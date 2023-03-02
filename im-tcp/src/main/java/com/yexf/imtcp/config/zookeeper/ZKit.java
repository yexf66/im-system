package com.yexf.imtcp.config.zookeeper;

import com.yexf.imcommon.constants.ZkConstants;
import com.yexf.imcommon.utils.DateUtil;
import org.I0Itec.zkclient.ZkClient;

import java.util.Date;

public class ZKit {
    private final ZkClient zkClient;

    public ZKit(ZkClient zkClient) {
        this.zkClient = zkClient;
    }

    public void initRootNode() {
        boolean exist = zkClient.exists(ZkConstants.ZkRoot);
        if (!exist) {
            zkClient.createPersistent(ZkConstants.ZkRoot);
        }
        exist = zkClient.exists(ZkConstants.ZkRoot + ZkConstants.ZkRootWeb);
        if (!exist) {
            zkClient.createPersistent(ZkConstants.ZkRoot + ZkConstants.ZkRootWeb);
        }
        exist = zkClient.exists(ZkConstants.ZkRoot + ZkConstants.ZkRootTcp);
        if (!exist) {
            zkClient.createPersistent(ZkConstants.ZkRoot + ZkConstants.ZkRootTcp);
        }
    }

    public void register(String path) {
        if (!zkClient.exists(path)) {
            zkClient.createEphemeral(path, DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        }


    }


}
