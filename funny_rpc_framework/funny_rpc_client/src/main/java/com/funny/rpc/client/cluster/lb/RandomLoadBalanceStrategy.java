package com.funny.rpc.client.cluster.lb;

import com.funny.rpc.client.cluster.LoadBalanceStrategy;
import com.funny.rpc.core.annotation.FRpcLoadBalance;
import com.funny.rpc.core.provider.ServiceProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;

/**
 * 随机策略
 * @author frankq
 * @date 2021/8/15
 */
@Slf4j
@FRpcLoadBalance(strategy = "random")
public class RandomLoadBalanceStrategy implements LoadBalanceStrategy {

    @Override
    public ServiceProvider select(List<ServiceProvider> serviceProviders) {
        /*
         * [0, len)
         */
        int len = serviceProviders.size();
        int index = RandomUtils.nextInt(0, len);
        return serviceProviders.get(index);
    }
    
}
