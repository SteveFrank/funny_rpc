package com.example.provider.impl;

import com.example.api.OrderService;
import com.funny.rpc.core.annotation.FRpcService;
import com.funny.rpc.server.config.RpcServerConfiguration;

import javax.annotation.Resource;

/**
 * @author frankq
 * @date 2021/12/30
 */
@FRpcService(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {

    @Resource
    private RpcServerConfiguration serverConfiguration;

    @Override
    public String getOrder(String userId, String orderNo) {
        return serverConfiguration.getServerPort()
                + "---"
                + serverConfiguration.getRpcPort()
                + "---Congratulations, The RPC call succeeded,orderNo is "
                + orderNo
                + ",userId is "
                + userId;
    }
}
