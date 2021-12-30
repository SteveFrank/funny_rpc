package com.funny.rpc.client.cluster.lb;

import com.funny.rpc.client.cluster.LoadBalanceStrategy;
import com.funny.rpc.core.annotation.FRpcLoadBalance;
import com.funny.rpc.core.provider.ServiceProvider;
import com.funny.rpc.core.utils.IpUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 哈希负载策略
 * @author frankq
 * @date 2021/8/15
 */
@Slf4j
@FRpcLoadBalance(strategy = "hash")
public class HashLoadBalanceStrategy implements LoadBalanceStrategy {

    @Override
    public ServiceProvider select(List<ServiceProvider> serviceProviders) {
        /*
         * 1、获取客户端ip
         * 2、获取ip hash
         * 3、index=hash%serviceProviders.size()
         * 4、get(index)
         * */
        String ip= IpUtil.getRealIp();
        int hashCode=ip.hashCode();
        int index=Math.abs(hashCode % serviceProviders.size());
        return serviceProviders.get(index);
    }

}
