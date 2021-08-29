package com.alazydogxd.netty.analysis.handler;

import com.alazydogxd.netty.analysis.decode.AbstractDecodePattern;
import com.alazydogxd.netty.analysis.decode.MessageFieldDecoder;
import com.alazydogxd.netty.analysis.decode.pattern.DefaultDecodePattern;
import com.alazydogxd.netty.analysis.exception.DecodeFailException;
import com.alazydogxd.netty.analysis.exception.UnExistMessageTypeException;
import com.alazydogxd.netty.analysis.message.Configuration;
import com.alazydogxd.netty.analysis.message.MessageField;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alazydogxd.netty.analysis.util.DecodeUtil.baseAnalysis;

/**
 * @author Mr_W
 * @date 2021/7/29 20:42
 * @description 报文解析
 */
public class MessageDecodeHandler extends ByteToMessageDecoder {

    private final Map<String, MessageFieldDecoder<Object>> msgDecoders = new HashMap<>(16);

    private AbstractDecodePattern pattern;

    private Configuration configuration;

    private String endpointName;

    public MessageDecodeHandler(String endpointName, Configuration configuration) {
        // TODO 获取报头
        this(endpointName, new DefaultDecodePattern(null).setEndpointName(endpointName), configuration);
    }

    public MessageDecodeHandler(String endpointName,
                                AbstractDecodePattern pattern,
                                Configuration configuration) {
        this.endpointName = endpointName;
        this.pattern = pattern;
        this.configuration = configuration;
        if (configuration.getMsgDecoders() != null && !configuration.getMsgDecoders().isEmpty()) {
            this.msgDecoders.putAll(configuration.getMsgDecoders());
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws DecodeFailException {
        Object result = pattern.decode(ctx, in, this::analysis);
        out.add(result);
    }

    /**
     * 添加解码器
     *
     * @param type       类型
     * @param msgDecoder 解码器
     */
    public MessageDecodeHandler addMsgDecoders(String type, MessageFieldDecoder<Object> msgDecoder) {
        this.msgDecoders.put(type, msgDecoder);
        return this;
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
        if (!msgDecoders.containsKey(msg.getType())) {
            throw new UnExistMessageTypeException("不存在报文类型");
        }
        return msgDecoders.get(msg.getType()).decode(msg, in);
    }

}
