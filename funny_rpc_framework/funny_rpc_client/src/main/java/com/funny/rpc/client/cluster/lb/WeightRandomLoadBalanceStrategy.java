package com.funny.rpc.client.cluster.lb;

import com.funny.rpc.client.cluster.LoadBalanceStrategy;
import com.funny.rpc.core.annotation.FRpcLoadBalance;
import com.funny.rpc.core.provider.ServiceProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author frankq
 * @date 2021/8/15
 */
@Slf4j
@FRpcLoadBalance(strategy = "weight_random")
public class WeightRandomLoadBalanceStrategy implements LoadBalanceStrategy {

    @Override
    public ServiceProvider select(List<ServiceProvider> serviceProviders) {
        /*
         * 按照权重创建一个新的待选集合
         * 在新的集合中随机选择
         */
        List<ServiceProvider> newList = new ArrayList<>();
        for (ServiceProvider serviceProvider : serviceProviders) {
            // 获取该节点的权重
            int weight = serviceProvider.getWeight();
            // 根据权重想新集合中添加节点
            for (int i = 0; i < weight; i++) {
                newList.add(serviceProvider);
            }
        }
        // 在新集合中进行随机选择
        int index = RandomUtils.nextInt(0, newList.size());
        return newList.get(index);
    }

}
