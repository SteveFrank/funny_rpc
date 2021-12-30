package com.funny.rpc.client.cluster;


/**
 * 服务端路由策略接口
 */
public interface StrategyProvider {

    /**
     * 获取路由策略
     * @return
     */
    LoadBalanceStrategy getStrategy();
}
