package com.funny.rpc.core.netty.codec;

import io.netty.handler.codec.LengthFieldPrepender;

/**
 * @author frankq
 * @date 2021/12/28
 */
public class FrameEncoder extends LengthFieldPrepender {

    public FrameEncoder() {
        super(4);
    }
}
