package com.alazydogxd.netty.analysis.decode.pattern;

import com.alazydogxd.netty.analysis.decode.Decode;
import com.alazydogxd.netty.analysis.decode.DecodePattern;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author Mr_W
 * @date 2021/7/30 23:03
 * @description 固定包长度，多余补 0
 */
public class FixedLenDecodePattern implements DecodePattern {

    private int len;

    public FixedLenDecodePattern() {
        this(200);
    }

    public FixedLenDecodePattern(int len) {
        this.len = len;
    }

    @Override
    public Object decode(ChannelHandlerContext ctx, ByteBuf in, Decode decode) {
        // TODO 解析策略
        ByteBuf byteBuf = in.readBytes(len);
        return null;
    }
}
