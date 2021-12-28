package com.funny.rpc.core.cache;

import com.funny.rpc.core.provider.ServiceProvider;

import java.util.List;

/**
 *
 * 在本地处理服务提供者的缓存数据
 *
 * @author frankq
 * @date 2021/12/28
 */
public interface ServiceProviderCache {

    /**
     * 向缓存中添加数据
     * @param key
     * @param value
     */
    void put(String key, List<ServiceProvider> value);

    /**
     * 获取缓存
     * @param key
     * @return
     */
    List<ServiceProvider> get(String key);

    /**
     * 缓存清除
     * @param key
     */
    void evict(String key);


    /**
     * 缓存更新
     * @param key
     * @param value
     */
    void update(String key,List<ServiceProvider> value);

}
