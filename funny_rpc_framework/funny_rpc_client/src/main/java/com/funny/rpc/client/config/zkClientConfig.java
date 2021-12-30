package com.funny.rpc.client.config;

import org.I0Itec.zkclient.ZkClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;


@Configuration
public class zkClientConfig {

    private static final int EXPIRE_SECONDS = 86400;

    @Resource
    private RpcClientConfiguration configuration;

    @Bean
    public ZkClient zkClient() {
        return new ZkClient(configuration.getZkAddr(), configuration.getConnectTimeout());

    }
}
