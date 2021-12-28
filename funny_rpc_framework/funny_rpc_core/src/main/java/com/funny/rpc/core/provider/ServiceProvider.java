package com.funny.rpc.core.provider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * 服务提供者的基础数据
 *
 * @author frankq
 * @date 2021/12/28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProvider {
    /**
     * 服务名称
     */
    private String serviceName;
    /**
     * 服务IP
     */
    private String serverIp;
    /**
     * RPC端口
     */
    private int rpcPort;
    /**
     * 网络通信端口
     */
    private int networkPort;
    /**
     * 超时时间
     */
    private long timeout;
    /**
     * the weight of service provider
     */
    private int weight;

}
