//package com.alazydogxd.netty.analysis.decode.pattern;
//
//import com.alazydogxd.netty.analysis.decode.Decode;
//import com.alazydogxd.netty.analysis.decode.AbstractDecodePattern;
//import io.netty.buffer.ByteBuf;
//import io.netty.channel.ChannelHandlerContext;
//
///**
// * @author Mr_W
// * @date 2021/7/30 23:03
// * @description 固定包长度，多余补 0
// */
//public class FixedLenAbstractDecodePattern extends AbstractDecodePattern {
//
//    private int len;
//
//    public FixedLenAbstractDecodePattern() {
//        this(200);
//    }
//
//    public FixedLenAbstractDecodePattern(int len) {
//        this.len = len;
//    }
//
//    @Override
//    public Object processDecode(ChannelHandlerContext ctx, ByteBuf in, Decode decode) {
//        // TODO 解析策略
//        ByteBuf byteBuf = in.readBytes(len);
//        return null;
//    }
//}
