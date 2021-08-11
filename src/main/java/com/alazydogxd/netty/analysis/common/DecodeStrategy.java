package com.alazydogxd.netty.analysis.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author Mr_W
 * @date 2021/7/30 22:53
 * @description 自定义解码策略
 */
public interface DecodeStrategy {

    /**
     * 解码
     *
     * @param ctx    ChannelHandlerContext
     * @param in     ByteBuf
     * @param decode Decode
     * @return 解析结果
     */
    Object decode(ChannelHandlerContext ctx, ByteBuf in, Decode decode);

}
