package com.alazydogxd.netty.analysis.handler;

import com.alazydogxd.netty.analysis.decode.AbstractDecodePattern;
import com.alazydogxd.netty.analysis.decode.pattern.DefaultDecodePattern;
import com.alazydogxd.netty.analysis.exception.DecodeFailException;
import com.alazydogxd.netty.analysis.exception.UnExistMessageTypeException;
import com.alazydogxd.netty.analysis.message.MessageAnalysisConfiguration;
import com.alazydogxd.netty.analysis.message.MessageField;
import io.netty.buffer.ByteBuf;
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

    private String endpointName;

    public MessageDecodeHandler(String endpointName, MessageAnalysisConfiguration configuration) {
        // TODO 获取报头
        this(endpointName, new DefaultDecodePattern(configuration.getMessageField("1", "1")).setEndpointName(endpointName), configuration);
    }

    public MessageDecodeHandler(String endpointName,
                                AbstractDecodePattern pattern,
                                MessageAnalysisConfiguration configuration) {
        this.endpointName = endpointName;
        this.pattern = pattern;
        this.configuration = configuration;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws DecodeFailException {
        Object result = pattern.decode(ctx, in, this::analysis);
        out.add(result);
    }

    /**
     * 解析
     *
     * @param msg 报文
     * @param in  ByteBuf
     * @return 解析结果
     */
    private Object analysis(MessageField msg, ByteBuf in) throws DecodeFailException {
        Object result;
        if ((result = baseAnalysis(msg.getType(), in)) != null) {
            return result;
        }
        if (!configuration.isHaveDecoder(msg.getType())) {
            throw new UnExistMessageTypeException("不存在报文类型");
        }
        return configuration.getMessageDecoder(msg.getType()).decode(msg, in);
    }

}
