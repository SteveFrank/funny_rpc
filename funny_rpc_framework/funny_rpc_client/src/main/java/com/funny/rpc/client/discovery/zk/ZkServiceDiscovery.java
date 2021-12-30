package com.funny.rpc.client.discovery.zk;

import com.funny.rpc.client.cache.local.DefaultServiceProviderCache;
import com.funny.rpc.client.config.RpcClientConfiguration;
import com.funny.rpc.client.discovery.RpcServiceDiscovery;
import com.funny.rpc.core.provider.ServiceProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author frankq
 * @date 2021/12/30
 */
@Slf4j
@Component
public class ZkServiceDiscovery implements RpcServiceDiscovery {

    @Resource
    private RpcClientConfiguration configuration;
    @Resource
    private ClientZkit clientZKit;
    @Resource
    private DefaultServiceProviderCache serviceProviderCache;

    @Override
    public void serviceDiscovery() {
        // 从注册中心获取所有的服务接口
        final List<String> serviceList = clientZKit.getServiceList();
        if (serviceList != null && !serviceList.isEmpty()) {
            for (String serviceName : serviceList) {
                // 获取接口的服务信息
                final List<ServiceProvider> serviceInfos = clientZKit.getServiceInfos(serviceName);
                // 接口的服务信息做本地缓存，避免注册中心挂掉，但是服务正常的情况，还是可以正常使用
                // 将接口与服务提供者信息计入缓存
                serviceProviderCache.put(serviceName, serviceInfos);
                // 如果zk的节点信息发生变化，更新缓存信息
                // 监听对应的服务信息名称
                clientZKit.subscribeZkEvent(serviceName);
                log.info("rpc client get services info: serviceName={}, providers={}", serviceList, serviceProviderCache.get(serviceName));
            }
        }
    }

}
