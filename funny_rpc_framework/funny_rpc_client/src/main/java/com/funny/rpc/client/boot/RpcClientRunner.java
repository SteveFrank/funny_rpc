package com.funny.rpc.client.boot;

import com.funny.rpc.client.discovery.RpcServiceDiscovery;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author frankq
 * @date 2021/8/14
 */
@Component
public class RpcClientRunner {

    @Resource
    private RpcServiceDiscovery rpcServiceDiscovery;

    public void run() {
        /*
         * 1、进行服务发现(获取服务的基础元信息), 获取+订阅两步操作
         * 2、为标注了@HrpcRemote的相关接口生产出对应的代理类
         * （Bean的后置处理器）
         */
        rpcServiceDiscovery.serviceDiscovery();
    }

}
