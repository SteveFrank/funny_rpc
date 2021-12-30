package com.funny.rpc.core.netty.codec;

import com.funny.rpc.core.data.RpcResponse;
import com.funny.rpc.core.utils.ProtostuffUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author frankq
 * @date 2021/12/30
 */
@Slf4j
public class RpcResponseEncoder extends MessageToMessageEncoder<RpcResponse> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcResponse response, List<Object> out)
            throws Exception {
        try {
            byte[] bytes = ProtostuffUtil.serialize(response);
            ByteBuf byteBuf = ctx.alloc().buffer(bytes.length);
            byteBuf.writeBytes(bytes);
            out.add(byteBuf);
        } catch (Exception e) {
            log.error("RpcResponseEncoder encode error!", e);
        }
    }

}
