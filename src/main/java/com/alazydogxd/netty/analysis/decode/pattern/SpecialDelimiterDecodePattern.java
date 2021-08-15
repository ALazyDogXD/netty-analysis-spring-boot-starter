package com.alazydogxd.netty.analysis.decode.pattern;

import com.alazydogxd.netty.analysis.decode.Decode;
import com.alazydogxd.netty.analysis.decode.DecodePattern;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author Mr_W
 * @date 2021/7/30 23:32
 * @description 特殊分隔符
 */
public class SpecialDelimiterDecodePattern implements DecodePattern {
    @Override
    public Object decode(ChannelHandlerContext ctx, ByteBuf in, Decode decode) {

        // TODO 解析策略
        return null;
    }
}
