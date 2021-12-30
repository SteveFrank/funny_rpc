package com.funny.rpc.server.registry.zk;

import com.funny.rpc.core.annotation.FRpcService;
import com.funny.rpc.core.spring.SpringBeanFactory;
import com.funny.rpc.core.utils.IpUtil;
import com.funny.rpc.server.config.RpcServerConfiguration;
import com.funny.rpc.server.registry.RpcRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author frankq
 * @date 2021/12/28
 */
@Slf4j
@Component
public class ZkRegistry implements RpcRegistry {

    @Resource
    private ServerZKClient serverZKClient;
    @Resource
    private RpcServerConfiguration rpcServerConfiguration;

    @Override
    public void serviceRegister() {
        // 实现服务注册逻辑 == 需要注册哪些接口
        // 1、创建根节点 /funny_rpc
        // 2、在根节点下创建代表各个接口的子节点
        // 3、在代表接口的子节点下创建代表节点提供者的节点 （IP.PORT）
        Map<String, Object> beanListByAnnotationClass
                = SpringBeanFactory.getBeanListByAnnotationClass(FRpcService.class);
        if (beanListByAnnotationClass != null && beanListByAnnotationClass.size() > 0) {
            // 创建根节点（持久节点）
            serverZKClient.createRootNode();
            String ip = IpUtil.getRealIp();
            // 进行注解扫描
            for (Object bean : beanListByAnnotationClass.values()) {
                FRpcService fRpcService = bean.getClass().getAnnotation(FRpcService.class);
                // 获取接口信息
                Class<?> serviceClass = fRpcService.interfaceClass();
                // 创建代表接口的节点（持久节点）
                String serviceClassName = serviceClass.getName();
                // 使用类名称作为对应接口的持久节点名称
                serverZKClient.createPersistentNode(serviceClassName);
                // 接口下的节点
                String node = ip + ":" + rpcServerConfiguration.getRpcPort();
                String path = serviceClassName + "/" + node;
                serverZKClient.createNode(path);
                log.info("zookeeper register path info : {}", path);
            }
        }
    }

}
