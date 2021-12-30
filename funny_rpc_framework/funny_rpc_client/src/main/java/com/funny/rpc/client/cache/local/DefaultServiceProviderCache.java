package com.funny.rpc.client.cache.local;

import com.funny.rpc.core.cache.ServiceProviderCache;
import com.funny.rpc.core.provider.ServiceProvider;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutionException;


@Component
public class DefaultServiceProviderCache implements ServiceProviderCache {

    @Resource
    private LoadingCache<String, List<ServiceProvider>> cache;

    @Override
    public void put(String key, List<ServiceProvider> value) {
        cache.put(key,value);
    }

    @Override
    public List<ServiceProvider> get(String key) {
        try {
            return cache.get(key);
        } catch (ExecutionException e) {
            return Lists.newArrayListWithCapacity(0);
        }
    }

    @Override
    public void evict(String key) {
        cache.invalidate(key);
    }

    @Override
    public void update(String key, List<ServiceProvider> value) {
        evict(key);
        put(key,value);
    }
}
