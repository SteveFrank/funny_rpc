package com.funny.rpc.server.boot;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author frankq
 * @date 2021/12/28
 */
@Configuration
public class RpcServerBootStrap {

    @Resource
    private RpcServerRunner rpcServerRunner;

    @PostConstruct
    public void initRpcServer() {
        // 初始化整个rpc服务端，引导项目的启动
    }

}
