package com.funny.rpc.server.boot.impl.netty;

import com.funny.rpc.core.netty.handler.RpcRequestHandler;
import com.funny.rpc.server.boot.RpcServer;
import com.funny.rpc.server.config.RpcServerConfiguration;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.NettyRuntime;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.UnorderedThreadPoolEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 *
 * 基于Netty的实现
 *
 * @author frankq
 * @date 2021/12/28
 */
@Slf4j
@Component
public class NettyServer implements RpcServer {

    @Resource
    private RpcServerConfiguration rpcServerConfiguration;

    @Override
    public void start() {
        // 启动Netty服务
        // 1、创建Netty线程组
        EventLoopGroup boss
                = new NioEventLoopGroup(1, new DefaultThreadFactory("funny-rpc-boss-"));
        EventLoopGroup worker
                = new NioEventLoopGroup(8, new DefaultThreadFactory("funny-rpc-worker-"));
        // 工作业务线程池
        UnorderedThreadPoolEventExecutor business = new UnorderedThreadPoolEventExecutor(
                NettyRuntime.availableProcessors() * 2, new DefaultThreadFactory("funny-rpc-business-"));
        // 2、创建Netty的服务启动器
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        // 3、创建RPC请求处理器的对象
        RpcRequestHandler rpcRequestHandler = new RpcRequestHandler();
        // 4、开始设置Netty的具体信息
        serverBootstrap
                .group(boss, worker)
                // Server端Channel
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                // 禁用TCP的Nagle算法，允许小包发送，数据量小的较好
                .option(ChannelOption.TCP_NODELAY, true)
                // 开启底层的keepAlive
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new LoggingHandler(LogLevel.INFO));

                        // 解码器在前

                        // 编码器在后

                        // 处理器始终是在编码解码处理后
                        pipeline.addLast("rpcRequestHandler", rpcRequestHandler);
                    }
                });
    }
}
