package com.funny.rpc.client.request;

import com.funny.rpc.client.cluster.DefaultStrategyProvider;
import com.funny.rpc.client.config.RpcClientConfiguration;
import com.funny.rpc.core.cache.ServiceProviderCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 *
 *  * 维护一个映射表，
 *  * key   -> ip+port
 *  * value -> 维护对应的channel
 *
 * @author frankq
 * @date 2021/12/30
 */
@Slf4j
@Component
public class RpcRequestManager {

    @Resource
    private RpcClientConfiguration configuration;
    @Resource
    private ServiceProviderCache serviceProviderCache;
    @Resource
    private DefaultStrategyProvider strategyProvider;

}
