package com.funny.rpc.server.registry.zk;

import com.funny.rpc.server.config.RpcServerConfiguration;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * @author frankq
 * @date 2021/12/28
 */
@Component
public class ServerZKClient {

    @Resource
    private ZkClient zkClient;

    @Resource
    private RpcServerConfiguration rpcServerConfiguration;

    /**
     * 根节点创建
     * 一般标识了这个业务下的处理节点
     */
    public void createRootNode() {
        if (StringUtils.isEmpty(rpcServerConfiguration.getZkRoot())) {
            rpcServerConfiguration.setZkRoot("/funny_root");
        }
        boolean exists = zkClient.exists(rpcServerConfiguration.getZkRoot());
        if (!exists) {
            zkClient.createPersistent(rpcServerConfiguration.getZkRoot());
        }
    }

    /**
     * 创建其他持久化节点
     */
    public void createPersistentNode(String path) {
        String pathName = rpcServerConfiguration.getZkRoot() + "/" + path;
        boolean exists = zkClient.exists(pathName);
        if (!exists) {
            zkClient.createPersistent(pathName);
        }
    }

    /**
     * 创建非持久化节点
     * @param path
     */
    public void createNode(String path) {
        String pathName = rpcServerConfiguration.getZkRoot() + "/" + path;
        boolean exists = zkClient.exists(pathName);
        if (!exists) {
            zkClient.createEphemeral(pathName);
        }
    }

}
