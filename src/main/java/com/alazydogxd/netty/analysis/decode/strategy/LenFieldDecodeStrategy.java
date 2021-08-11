package com.alazydogxd.netty.analysis.decode.strategy;

import com.alazydogxd.netty.analysis.common.Decode;
import com.alazydogxd.netty.analysis.common.DecodeStrategy;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author Mr_W
 * @date 2021/7/30 23:33
 * @description 包头包含包长度字段
 */
public class LenFieldDecodeStrategy implements DecodeStrategy {
    @Override
    public Object decode(ChannelHandlerContext ctx, ByteBuf in, Decode decode) {
        // TODO 解析策略
        return null;
    }
}
