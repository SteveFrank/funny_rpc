package com.funny.rpc.core.netty.codec;

import com.funny.rpc.core.data.RpcRequest;
import com.funny.rpc.core.utils.ProtostuffUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author frankq
 * @date 2021/8/15
 */
@Slf4j
public class RpcRequestEncoder extends MessageToMessageEncoder<RpcRequest> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcRequest request, List<Object> out) throws Exception {
        try {
            // 序列化为二进制数组
            byte[] bytes = ProtostuffUtil.serialize(request);
            // 将数组放置到对应的ByteBuff中
            ByteBuf byteBuf = ctx.alloc().buffer(bytes.length);
            byteBuf.writeBytes(bytes);
            out.add(byteBuf);
        } catch (Exception e) {
            log.error("RpcRequestEncoder encoder error!", e);
        }
    }
}
