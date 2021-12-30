package com.funny.rpc.client.request;

import com.funny.rpc.client.cluster.DefaultStrategyProvider;
import com.funny.rpc.client.config.RpcClientConfiguration;
import com.funny.rpc.core.cache.ServiceProviderCache;
import com.funny.rpc.core.data.RpcRequest;
import com.funny.rpc.core.data.RpcResponse;
import com.funny.rpc.core.enums.StatusEnum;
import com.funny.rpc.core.exception.RpcException;
import com.funny.rpc.core.netty.codec.FrameDecoder;
import com.funny.rpc.core.netty.codec.FrameEncoder;
import com.funny.rpc.core.netty.codec.RpcRequestEncoder;
import com.funny.rpc.core.netty.codec.RpcResponseDecoder;
import com.funny.rpc.core.netty.handler.RpcRequestHandler;
import com.funny.rpc.core.netty.handler.RpcResponseHandler;
import com.funny.rpc.core.netty.request.ChannelMapping;
import com.funny.rpc.core.netty.request.RequestPromise;
import com.funny.rpc.core.netty.request.RpcRequestHolder;
import com.funny.rpc.core.provider.ServiceProvider;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 *  * 维护一个映射表，
 *  * key   -> ip+port
 *  * value -> 维护对应的channel
 *
 * @author frankq
 * @date 2021/12/30
 */
@Slf4j
@Component
public class RpcRequestManager {

    @Resource
    private RpcClientConfiguration configuration;
    @Resource
    private ServiceProviderCache serviceProviderCache;
    @Resource
    private DefaultStrategyProvider strategyProvider;

    /**
     * 发送网络请求
     */
    public RpcResponse sendRequest(RpcRequest rpcRequest) {
        /**
         * 1、发送给对应的ip
         * 2、基于netty的调用
         */
        final List<ServiceProvider> serviceProviders
                = serviceProviderCache.get(rpcRequest.getClassName());
        if (CollectionUtils.isEmpty(serviceProviders)) {
            log.info("客户端没有可以使用的服务节点");
            throw new RpcException(StatusEnum.NOT_FOUND_SERVICE_PROVIDER);
        }

        // 获取到对应的集合后
        // 需要选择一个提供者，路由，负载均衡，版本的控制等
        // final ServiceProvider serviceProvider = serviceProviders.get(0);
        // 通过配置的负载均衡策略选择
        final ServiceProvider serviceProvider = strategyProvider.getStrategy().select(serviceProviders);
        // 基于Netty发送网络调用
        if (Objects.nonNull(serviceProvider)) {
            return requestByNetty(rpcRequest, serviceProvider);
        } else {
            throw new RpcException(StatusEnum.NOT_FOUND_SERVICE_PROVIDER);
        }

    }

    /**
     * 利用Netty发送数据 获取结果
     * @param rpcRequest
     * @param serviceProvider
     * @return
     */
    private RpcResponse requestByNetty(RpcRequest rpcRequest, ServiceProvider serviceProvider) {
        try {
            // 判断连接是否存在
            if (!RpcRequestHolder.channelExist(serviceProvider.getServerIp(), serviceProvider.getRpcPort())) {
                // 如果不存在才创建连接
                EventLoopGroup worker = new NioEventLoopGroup(0, new DefaultThreadFactory("rpc-worker-"));
                Bootstrap bootstrap = new Bootstrap();
                RpcResponseHandler rpcResponseHandler = new RpcResponseHandler();

                bootstrap.group(worker)
                        .channel(NioSocketChannel.class)
                        .handler(new LoggingHandler(LogLevel.INFO))
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                // 这里都是在netty的io线程池中进行执行的
                                final ChannelPipeline pipeline = ch.pipeline();
                                // 编解码器的处理

                                // 编码
                                pipeline.addLast("FrameEncoder", new FrameEncoder());
                                pipeline.addLast("RpcRequestEncoder", new RpcRequestEncoder());

                                // 解码器
                                pipeline.addLast("FrameDecoder", new FrameDecoder());
                                pipeline.addLast("RpcResponseDecoder", new RpcResponseDecoder());

                                // handler处理器
                                pipeline.addLast("rpcResponseHandler", rpcResponseHandler);
                            }
                        });
                // 连接服务远端提供者
                final ChannelFuture future = bootstrap.connect(serviceProvider.getServerIp(), serviceProvider.getRpcPort()).sync();
                if (future.isSuccess()) {
                    // 保存连接
                    ChannelMapping channelMapping = new ChannelMapping(serviceProvider.getServerIp(), serviceProvider.getRpcPort(), future.channel());
                    RpcRequestHolder.addChannelMapping(channelMapping);
                }
            }
            // 进行连接获取
            Channel channel = RpcRequestHolder.getChannel(serviceProvider.getServerIp(), serviceProvider.getRpcPort());
            if (Objects.nonNull(channel)) {
                // 这里都是在一条线程里面执行
                RequestPromise requestPromise = new RequestPromise(channel.eventLoop());
                RpcRequestHolder.addRequestPromise(rpcRequest.getRequestId(), requestPromise);
                // 向服务器发送数据
                ChannelFuture channelFuture = channel.writeAndFlush(rpcRequest);
                // 需要获取结果
                // 从netty的nio线程池中获取结果，此处是需要等待获取结果的（面临的问题：异步与同步 => 异步转同步）
                channelFuture.addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (!future.isSuccess()) {
                            // 如果没有写成功就将数据从hash表中移除
                            RpcRequestHolder.removeRequestPromise(rpcRequest.getRequestId());
                        }
                    }
                });
                requestPromise.addListener((FutureListener) future -> {
                    if (!future.isSuccess()) {
                        RpcRequestHolder.removeRequestPromise(rpcRequest.getRequestId());
                    }
                });
                // 等结果
                // 设置超时时间
                RpcResponse response = (RpcResponse) requestPromise.get(configuration.getConnectTimeout(), TimeUnit.SECONDS);
                RpcRequestHolder.removeRequestPromise(rpcRequest.getRequestId());
                return response;
            }
        } catch (Exception e) {
            log.error("remote rpc request error!", e);
            throw new RpcException(e);
        }

        return new RpcResponse();
    }

}
