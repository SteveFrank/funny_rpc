package com.funny.rpc.client.proxy;

import com.funny.rpc.client.request.RpcRequestManager;
import com.funny.rpc.core.data.RpcRequest;
import com.funny.rpc.core.data.RpcResponse;
import com.funny.rpc.core.exception.RpcException;
import com.funny.rpc.core.spring.SpringBeanFactory;
import com.funny.rpc.core.utils.RequestIdUtil;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * 代理生成方法的时候拦截
 */
public class CglibProxyCallBackHandler implements MethodInterceptor {

    /**
     * 方法拦截处理
     * @param o
     * @param method      当前正在执行的方法
     * @param parameters  方法的入口参数
     * @param methodProxy
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object o, Method method, Object[] parameters, MethodProxy methodProxy) throws Throwable {
        // 首先需要放过Object的本身，toString、hashcode、equals等一系列方法，采用spring的工具类
        if (ReflectionUtils.isObjectMethod(method)) {
            return method.invoke(method.getDeclaringClass().newInstance(), parameters);
        }
        /*
         * 走底层的网络调用
         * 1、封装RpcRequest对象
         */
        String requestId = RequestIdUtil.requestId();
        String interfaceClassName = method.getDeclaringClass().getName();
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        final RpcRequest request = RpcRequest.builder()
                .requestId(requestId)
                .className(interfaceClassName)
                .methodName(methodName)
                .parameterTypes(parameterTypes)
                .parameters(parameters)
                .build();
        // 发送RPC，获取请求结果
        RpcRequestManager rpcRequestManager = SpringBeanFactory.getBean(RpcRequestManager.class);
        if (rpcRequestManager == null) {
            throw new RpcException("spring ioc exception");
        }
        RpcResponse response = rpcRequestManager.sendRequest(request);
        return response.getResult();
    }
}
