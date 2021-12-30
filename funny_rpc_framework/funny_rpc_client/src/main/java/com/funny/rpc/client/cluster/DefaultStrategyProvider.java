package com.funny.rpc.client.cluster;

import com.funny.rpc.client.config.RpcClientConfiguration;
import com.funny.rpc.core.annotation.FRpcLoadBalance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author frankq
 * @date 2021/8/15
 */
@Slf4j
@Component
public class DefaultStrategyProvider implements StrategyProvider, ApplicationContextAware {

    private LoadBalanceStrategy loadBalanceStrategy;

    @Resource
    private RpcClientConfiguration configuration;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        final Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(FRpcLoadBalance.class);
        String strategy = configuration.getRpcClientClusterStrategy();
        for (Object bean : beansWithAnnotation.values()) {
            FRpcLoadBalance hrpcLoadBalance = bean.getClass().getAnnotation(FRpcLoadBalance.class);
            if (hrpcLoadBalance.strategy().equalsIgnoreCase(strategy)) {
                this.loadBalanceStrategy = (LoadBalanceStrategy) bean;
                break;
            }
        }
    }

    @Override
    public LoadBalanceStrategy getStrategy() {
        return this.loadBalanceStrategy;
    }
}
