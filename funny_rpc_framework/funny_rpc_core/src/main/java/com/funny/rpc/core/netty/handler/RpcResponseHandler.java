package com.funny.rpc.core.netty.handler;

import com.funny.rpc.core.data.RpcResponse;
import com.funny.rpc.core.netty.request.RequestPromise;
import com.funny.rpc.core.netty.request.RpcRequestHolder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author frankq
 * @date 2021/8/15
 */
@Slf4j
@ChannelHandler.Sharable
public class RpcResponseHandler extends SimpleChannelInboundHandler<RpcResponse> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception {
        log.info("--- rpc 调用成功, 返回结果为:{}", response);
        final RequestPromise requestPromise = RpcRequestHolder.getRequestPromise(response.getRequestId());
        if (requestPromise != null) {
            requestPromise.setSuccess(response);
        }
    }
}
