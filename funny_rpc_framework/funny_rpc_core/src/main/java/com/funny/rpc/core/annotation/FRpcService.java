package com.funny.rpc.core.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 服务发布
 * @author frankq
 */
@Component
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FRpcService {

    /**
     * 等同于@Component的value
     * @return
     */
    @AliasFor(annotation = Component.class)
    String value() default "";

    /**
     * 服务接口Class
     * @return
     */
    Class<?> interfaceClass() default void.class;

    /**
     * 服务接口名称
     * @return
     */
    String interfaceName() default "";

    /**
     * 服务版本号
     * @return
     */
    String version() default "";

    /**
     * 服务分组
     * @return
     */
    String group() default "";

}
