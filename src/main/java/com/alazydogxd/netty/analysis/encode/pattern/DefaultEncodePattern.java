package com.alazydogxd.netty.analysis.encode.pattern;

import com.alazydogxd.netty.analysis.encode.AbstractEncodePattern;
import com.alazydogxd.netty.analysis.encode.Restore;
import com.alazydogxd.netty.analysis.exception.EncodeFailException;
import com.alazydogxd.netty.analysis.exception.MessageRestoreFailException;
import com.alazydogxd.netty.analysis.message.MessageField;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * @author Mr_W
 * @date 2021/9/17 22:11
 * @description 默认编码模式
 */
public class DefaultEncodePattern extends AbstractEncodePattern {
    @Override
    public void encode(ChannelHandlerContext ctx, List<MessageField> msg, ByteBuf out, Restore restore) throws MessageRestoreFailException, EncodeFailException {
        for (MessageField messageField : msg) {
            restore.restore(messageField, out);
        }
    }

    @Override
    public String getPatternName() {
        return "default";
    }
}
