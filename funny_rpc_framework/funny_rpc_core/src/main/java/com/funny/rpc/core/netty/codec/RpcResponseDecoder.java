package com.funny.rpc.core.netty.codec;

import com.funny.rpc.core.data.RpcResponse;
import com.funny.rpc.core.utils.ProtostuffUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author frankq
 * @date 2021/8/15
 */
@Slf4j
public class RpcResponseDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        try {
            // 数据的读取长度
            int length = msg.readableBytes();
            byte[] bytes = new byte[length];
            msg.readBytes(bytes);
            // 将数据从byteBuff中读取到字节数组中
            final RpcResponse rpcResponse = ProtostuffUtil.deserialize(bytes, RpcResponse.class);
            out.add(rpcResponse);
        } catch (Exception e) {
            log.error("RpcRequestDecoder decode error!", e);
        }
    }
}
