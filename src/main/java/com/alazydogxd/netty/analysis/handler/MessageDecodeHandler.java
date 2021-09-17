package com.alazydogxd.netty.analysis.handler;

import com.alazydogxd.netty.analysis.decode.AbstractDecodePattern;
import com.alazydogxd.netty.analysis.exception.DecodeFailException;
import com.alazydogxd.netty.analysis.exception.MessageAnalysisFailException;
import com.alazydogxd.netty.analysis.exception.UnExistMessageTypeException;
import com.alazydogxd.netty.analysis.message.MessageAnalysisConfiguration;
import com.alazydogxd.netty.analysis.message.MessageField;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import static com.alazydogxd.netty.analysis.util.DecodeUtil.baseAnalysis;

/**
 * @author Mr_W
 * @date 2021/7/29 20:42
 * @description 报文解析
 */
public class MessageDecodeHandler extends ByteToMessageDecoder {

    private AbstractDecodePattern pattern;

    private MessageAnalysisConfiguration configuration;

    public MessageDecodeHandler(AbstractDecodePattern pattern,
                                MessageAnalysisConfiguration configuration) {
        this.pattern = pattern;
        this.configuration = configuration;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws DecodeFailException, MessageAnalysisFailException {
        Object result = pattern.decode(ctx, in, this::analysis);
        in.skipBytes(in.readableBytes());
        out.add(result);
    }

    /**
     * 解析
     *
     * @param messageField 报文
     * @param in           ByteBuf
     * @return 解析结果
     */
    private Object analysis(MessageField messageField, ByteBuf in) throws DecodeFailException, MessageAnalysisFailException {
        Object result;
        try {
            if ((result = baseAnalysis(messageField.getType(), in)) != null) {
                return result;
            }
            if (!configuration.isHaveConverter(messageField.getType())) {
                throw new UnExistMessageTypeException("不存在报文类型");
            }
        } catch (Exception e) {
            throw new MessageAnalysisFailException(String.format("字段 [%s] 解析失败", messageField.getFieldName()), e);
        }
        return configuration.getMessageDecoder(messageField.getType()).decode(messageField, in);
    }

}
