package com.funny.rpc.core.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 负载均衡策略
 * @author frankq
 */
@Component
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FRpcLoadBalance {

    @AliasFor(annotation = Component.class)
    String value() default "";

    /**
     * load balance 策略
     */
    String strategy() default "random";

}
