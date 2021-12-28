package com.funny.rpc.server.boot;

import com.funny.rpc.server.registry.RpcRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 *
 * 发布服务的启动类
 * 负责进行rpc的启动
 *
 * @author frankq
 * @date 2021/12/28
 */
@Slf4j
@Component
public class RpcServerRunner {

    @Resource
    private RpcRegistry registry;
    @Resource
    private RpcServer rpcServer;

    public void run() {
        /*
         * 1、开始进行服务注册
         * 2、基于netty启动服务，监听端口
         * 接收连接 --> 解码与编码 --> 调用业务 --> 返回
         */
        registry.serviceRegister();
        rpcServer.start();
    }

}
