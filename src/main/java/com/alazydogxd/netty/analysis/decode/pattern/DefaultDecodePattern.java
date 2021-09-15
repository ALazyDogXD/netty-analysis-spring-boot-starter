package com.alazydogxd.netty.analysis.decode.pattern;

import com.alazydogxd.netty.analysis.decode.Decode;
import com.alazydogxd.netty.analysis.decode.AbstractDecodePattern;
import com.alazydogxd.netty.analysis.decode.MessageDecoder;
import com.alazydogxd.netty.analysis.exception.DecodeFailException;
import com.alazydogxd.netty.analysis.message.MessageField;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * @author Mr_W
 * @date 2021/8/29 21:32
 * @description 默认解析模式
 */
public class DefaultDecodePattern extends AbstractDecodePattern {

    public DefaultDecodePattern(List<MessageField> initMessageField) {
        super(initMessageField);
    }

    @Override
    public String getPatternName() {
        return "default";
    }

    @Override
    public Object processDecode(ChannelHandlerContext ctx,
                                ByteBuf in,
                                Decode decode,
                                MessageDecoder messageDecoder) throws DecodeFailException {
        return messageDecoder.decode(in, decode);
    }
}
