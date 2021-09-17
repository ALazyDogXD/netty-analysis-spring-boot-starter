package com.alazydogxd.netty.analysis.handler;

import com.alazydogxd.netty.analysis.encode.AbstractEncodePattern;
import com.alazydogxd.netty.analysis.exception.*;
import com.alazydogxd.netty.analysis.message.MessageAnalysisConfiguration;
import com.alazydogxd.netty.analysis.message.MessageField;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.List;

import static com.alazydogxd.netty.analysis.util.EncodeUtil.baseRestore;

/**
 * @author Mr_W
 * @date 2021/9/16 2:16
 * @description 编码处理器
 */
@ChannelHandler.Sharable
public class MessageEncodeHandler extends MessageToByteEncoder<List<MessageField>> {

    private AbstractEncodePattern pattern;

    private MessageAnalysisConfiguration configuration;

    public MessageEncodeHandler(AbstractEncodePattern pattern,
                                MessageAnalysisConfiguration configuration) {
        this.pattern = pattern;
        this.configuration = configuration;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, List<MessageField> msg, ByteBuf out) throws Exception {
        pattern.encode(ctx, msg, out, this::restore);
    }

    private void restore(MessageField messageField, ByteBuf out) throws MessageRestoreFailException, EncodeFailException {
        try {
            if (baseRestore(messageField, out)) {
                return;
            }
            if (!configuration.isHaveConverter(messageField.getType())) {
                throw new UnExistMessageTypeException("不存在报文类型");
            }
        } catch (Exception e) {
            throw new MessageRestoreFailException(String.format("字段 [%s] 还原失败", messageField.getFieldName()), e);
        }
        configuration.getMessageDecoder(messageField.getType()).encode(messageField, out);
    }

}
