package com.funny.rpc.core.proxy;

/**
 * @author frankq
 * @date 2021/12/28
 */
public interface ProxyFactory {

    public <T> T newProxyInstance(Class<T> cls);

}
