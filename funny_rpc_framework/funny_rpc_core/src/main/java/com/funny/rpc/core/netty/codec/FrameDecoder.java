package com.funny.rpc.core.netty.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 1、解码器
 * 在数据流后，首先面对的是一个Frame，
 * 所以先通过frameDecoder解码获取到实际的数据内容
 *
 * @author frankq
 * @date 2021/12/28
 */
public class FrameDecoder extends LengthFieldBasedFrameDecoder {

    public FrameDecoder() {
        super(Integer.MAX_VALUE, 0, 4, 0,0);
    }

}
