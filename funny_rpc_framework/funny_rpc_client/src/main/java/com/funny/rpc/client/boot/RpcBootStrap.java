package com.funny.rpc.client.boot;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author frankq
 * @date 2021/8/14
 */
@Configuration
public class RpcBootStrap {

    @Resource
    private RpcClientRunner rpcClientRunner;

    @PostConstruct
    public void init() {

        rpcClientRunner.run();
    }

}
