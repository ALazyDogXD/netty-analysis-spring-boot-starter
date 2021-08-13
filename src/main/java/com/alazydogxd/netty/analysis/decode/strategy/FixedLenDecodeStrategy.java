package com.alazydogxd.netty.analysis.decode.strategy;

import com.alazydogxd.netty.analysis.decode.Decode;
import com.alazydogxd.netty.analysis.decode.DecodeStrategy;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author Mr_W
 * @date 2021/7/30 23:03
 * @description 固定包长度，多余补 0
 */
public class FixedLenDecodeStrategy implements DecodeStrategy {

    private int len;

    public FixedLenDecodeStrategy() {
        this(200);
    }

    public FixedLenDecodeStrategy(int len) {
        this.len = len;
    }

    @Override
    public Object decode(ChannelHandlerContext ctx, ByteBuf in, Decode decode) {
        // TODO 解析策略
        ByteBuf byteBuf = in.readBytes(len);
        return null;
    }
}
