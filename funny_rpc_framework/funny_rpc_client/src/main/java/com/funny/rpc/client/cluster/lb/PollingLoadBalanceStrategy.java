package com.funny.rpc.client.cluster.lb;

import com.funny.rpc.client.cluster.LoadBalanceStrategy;
import com.funny.rpc.core.annotation.FRpcLoadBalance;
import com.funny.rpc.core.provider.ServiceProvider;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author frankq
 * @date 2021/8/15
 */
@Slf4j
@FRpcLoadBalance(strategy = "polling")
public class PollingLoadBalanceStrategy implements LoadBalanceStrategy {

    private int index;
    private ReentrantLock lock = new ReentrantLock();

    @Override
    public ServiceProvider select(List<ServiceProvider> serviceProviders) {
        try {
            // 涉及到一个并发执行，所以需要先加锁
            boolean lockResult = lock.tryLock(10, TimeUnit.SECONDS);
            if (lockResult) {
                if (index >= serviceProviders.size()) {
                    index = 0;
                }
                ServiceProvider serviceProvider = serviceProviders.get(index);
                index = (index + 1) % serviceProviders.size();
                return serviceProvider;
            }
        } catch (Exception e) {
            log.error("轮询策略获取锁失败", e);
        } finally {
            lock.unlock();
        }
        return null;
    }
}
