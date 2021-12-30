package com.funny.rpc.core.netty.codec;

import com.funny.rpc.core.data.RpcRequest;
import com.funny.rpc.core.utils.ProtostuffUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author frankq
 * @date 2021/12/30
 */
@Slf4j
public class RpcRequestDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out)
            throws Exception {
        try {
            // 获取数据的读取长度
            int length = msg.readableBytes();
            byte[] bytes = new byte[length];
            // 将数据从ByteBuff中读取到的字节数组
            final RpcRequest request = ProtostuffUtil.deserialize(bytes, RpcRequest.class);
            out.add(request);
        } catch (Exception e) {
            log.error("RpcRequestDecoder decode error!", e);
        }
    }
}
