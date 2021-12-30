package com.funny.rpc.client.proxy;

import com.funny.rpc.core.proxy.ProxyFactory;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;
import org.springframework.stereotype.Component;


/**
 * @description
 * @author: ts
 * @create:2021-05-10 09:42
 */
@Component
@Slf4j
public class RequestProxyFactory implements ProxyFactory {

    /**
     * 创建新的代理实例-CGLib动态代理
     * @param cls
     * @param <T>
     * @return
     */
    @Override
    public  <T> T newProxyInstance(Class<T> cls) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(cls);
        enhancer.setCallback(new CglibProxyCallBackHandler());
        return (T) enhancer.create();
    }

}
