package com.funny.rpc.core.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * @author frankq
 * @date 2021/12/28
 */
@Component
public class SpringBeanFactory implements ApplicationContextAware {

    /**
     * ioc 容器
     */
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * 根据Class获取bean
     */
    public static   <T> T getBean(Class<T> cls) {
        return context.getBean(cls);
    }

    /**
     * 根据beanName获取bean
     */
    public static Object getBean(String beanName) {
        return context.getBean(beanName);
    }

    /***
     * 获取有指定注解的对象
     */
    public static Map<String, Object> getBeanListByAnnotationClass(Class<? extends Annotation> annotationClass) {
        return context.getBeansWithAnnotation(annotationClass);
    }

    /**
     * 向容器注册单例bean
     */
    public static void registerSingleton(Object bean) {
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getAutowireCapableBeanFactory();
        // 让bean完成Spring初始化过程中所有增强器检验，只是不重新创建bean
        beanFactory.applyBeanPostProcessorsAfterInitialization(bean,bean.getClass().getName());
        //将bean以单例的形式入驻到容器中，此时通过bean.getClass().getName()或bean.getClass()都可以拿到放入Spring容器的Bean
        beanFactory.registerSingleton(bean.getClass().getName(),bean);
    }

}
