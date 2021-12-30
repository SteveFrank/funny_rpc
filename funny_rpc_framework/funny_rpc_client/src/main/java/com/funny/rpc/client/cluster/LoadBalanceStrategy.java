package com.funny.rpc.client.cluster;


import com.funny.rpc.core.provider.ServiceProvider;

import java.util.List;

/**
 * 路由策略具体实现的接口
 */
public interface LoadBalanceStrategy {

    /**
     * 根据对应的策略进行负载均衡
     * @param serviceProviders
     * @return
     */
    ServiceProvider select(List<ServiceProvider> serviceProviders);

}
