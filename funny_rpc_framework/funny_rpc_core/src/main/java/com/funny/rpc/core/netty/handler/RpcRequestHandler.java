package com.funny.rpc.core.netty.handler;

import com.funny.rpc.core.data.RpcRequest;
import com.funny.rpc.core.data.RpcResponse;
import com.funny.rpc.core.spring.SpringBeanFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * 请求处理器
 *
 * @author frankq
 * @date 2021/12/28
 */
@Slf4j
@ChannelHandler.Sharable
public class RpcRequestHandler extends SimpleChannelInboundHandler<RpcRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
        log.info("rpc server received msg = {}", request);
        RpcResponse response = new RpcResponse();

        try {
            response.setRequestId(request.getRequestId());
            // 最终目的是，调用对应服务器上的接口返回数据
            String interfaceName = request.getClassName();
            String methodName = request.getMethodName();
            // 参数
            Class<?>[] parameterTypes = request.getParameterTypes();
            Object[] parameters = request.getParameters();
            // 从容器中获取到对象
            final Object bean = SpringBeanFactory.getBean(Class.forName(interfaceName));
            // 组装方法对象
            final Method method = bean.getClass().getMethod(methodName, parameterTypes);
            // 执行方法，获取结果
            Object result = method.invoke(bean, parameters);
            response.setResult(result);
        } catch (Exception e) {
            log.error("RpcRequestHandler read error!", e);
            response.setCause(e);
        } finally {
            log.error("rpc server response msg={}", response);
            // 数据出站
            ctx.channel().writeAndFlush(response);
        }

    }

}
