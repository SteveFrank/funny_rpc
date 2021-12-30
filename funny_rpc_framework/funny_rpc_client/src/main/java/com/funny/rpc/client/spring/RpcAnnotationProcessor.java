package com.funny.rpc.client.spring;

import com.funny.rpc.core.annotation.FRpcRemote;
import com.funny.rpc.core.proxy.ProxyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * @author frankq
 * @date 2021/12/30
 */
@Slf4j
@Component
public class RpcAnnotationProcessor implements BeanPostProcessor, ApplicationContextAware {

    /**
     * 获取代理工厂
     */
    private ProxyFactory proxyFactory;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.proxyFactory = applicationContext.getBean(ProxyFactory.class);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    /**
     * Bean 初始化完成之后操作
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 属性上是否标注了注解，如果标注了生成代理用于后续代理调用远程接口（通过Netty）
        final Field[] fields = bean.getClass().getDeclaredFields();
        // Field 代表的是OrderService属性
        for (Field field : fields) {
            try {
                // 判断Field是否可以操作
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                // 参数上是否有这个注解
                FRpcRemote fRpcRemote = field.getAnnotation(FRpcRemote.class);
                if (fRpcRemote != null) {
                    // 为该注解的接口生成代理类，然后将代理对象注入到属性上
                    // 为接口调用，生成代理类进行处理
                    Object serviceProxy = proxyFactory.newProxyInstance(field.getType());
                    log.info("为{}生成了代理, proxy={}", field.getType().getName(), serviceProxy);
                    if (serviceProxy != null) {
                        // 注入到对应的属性上
                        field.set(bean, serviceProxy);
                    }
                }
            } catch (IllegalAccessException e) {
                log.error("postProcessAfterInitialization error!", e);
            }
        }
        return bean;
    }
}
