package com.funny.rpc.server.config;

import org.I0Itec.zkclient.ZkClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author frankq
 * @date 2021/12/28
 */
@Configuration
public class ServerZkClientConfig {

    /**
     * RPC服务端配置
     * zk 注册中心地址等信息
     */
    @Resource
    private RpcServerConfiguration rpcServerConfiguration;

    /**
     * 声音ZK客户端
     * @return
     */
    @Bean
    public ZkClient zkClient() {
        return new ZkClient(rpcServerConfiguration.getZkAddr(), rpcServerConfiguration.getConnectTimeout());
    }

}
