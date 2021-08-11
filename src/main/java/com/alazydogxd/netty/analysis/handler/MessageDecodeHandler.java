package com.alazydogxd.netty.analysis.handler;

import com.alazydogxd.netty.analysis.common.DecodeStrategy;
import com.alazydogxd.netty.analysis.exception.DecodeFailException;
import com.alazydogxd.netty.analysis.message.MessageField;
import com.alazydogxd.netty.analysis.common.MessageDecoder;
import com.alazydogxd.netty.analysis.exception.UnExistMessageTypeException;
import com.alazydogxd.netty.analysis.decode.strategy.FixedLenDecodeStrategy;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alazydogxd.netty.analysis.util.DecodeUtil.baseAnalysis;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Mr_W
 * @date 2021/7/29 20:42
 * @description 报文解析
 */
public class MessageDecodeHandler extends ByteToMessageDecoder {

    private static final Logger LOGGER = getLogger(MessageDecodeHandler.class);

    private final Map<String, MessageDecoder<Object>> msgDecoders = new HashMap<>(16);

    private DecodeStrategy strategy;

    public MessageDecodeHandler() {
        this(new FixedLenDecodeStrategy());
    }

    public MessageDecodeHandler(DecodeStrategy strategy) {
        this(strategy, null);
    }

    public MessageDecodeHandler(DecodeStrategy strategy, Map<String, MessageDecoder<Object>> msgDecoders) {
        this.strategy = strategy;
        if (msgDecoders != null) {
            this.msgDecoders.putAll(msgDecoders);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Object result = strategy.decode(ctx, in, this::analysis);
        out.add(result);
    }

    /**
     * 添加解码器
     *
     * @param type       类型
     * @param msgDecoder 解码器
     */
    public MessageDecodeHandler addMsgDecoders(String type, MessageDecoder<Object> msgDecoder) {
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
    private Object analysis(Enum<? extends MessageField> msg, ByteBuf in) throws DecodeFailException {
        Object result;
        if ((result = baseAnalysis(((MessageField) msg).getType(), in)) != null) {
            return result;
        }
        if (!msgDecoders.containsKey(((MessageField) msg).getType())) {
            throw new UnExistMessageTypeException("不存在报文类型");
        }
        return msgDecoders.get(((MessageField) msg).getType()).decode(msg, in);
    }

}
