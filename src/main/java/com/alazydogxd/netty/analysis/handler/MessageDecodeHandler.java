package com.alazydogxd.netty.analysis.handler;

import com.alazydogxd.netty.analysis.common.Message;
import com.alazydogxd.netty.analysis.common.MessageDecoder;
import com.alazydogxd.netty.analysis.enums.BaseMessageType;
import com.alazydogxd.netty.analysis.exception.UnExistMessageTypeException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Mr_W
 * @date 2021/7/29 20:42
 * @description 报文解析
 */
public class MessageDecodeHandler extends ByteToMessageDecoder {

    private static final Logger LOGGER = getLogger(MessageDecodeHandler.class);

    private final Map<String, MessageDecoder<Object>> msgDecoders = new HashMap<>(16);

    private String strategy;

    public MessageDecodeHandler(String strategy) {

    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // TODO 报文解析
    }

    private Object analysis(Message msg, ByteBuf in) {
        Object result;
        if ((result = analysisBase(msg.getType(), in)) != null) {
            return result;
        }
        if (!msgDecoders.containsKey(msg.getType())) {
            throw new UnExistMessageTypeException("不存在报文类型");
        }
        return msgDecoders.get(msg.getType()).decode(msg, in);
    }

    private Object analysisBase(String type, ByteBuf in) {
        if (Arrays.stream(BaseMessageType.values()).map(Enum::toString).anyMatch(messageType -> messageType.equals(type))) {
            return null;
        }
        switch (BaseMessageType.valueOf(type)) {
            case BYTE:
                return in.readByte();
            case UNSIGNED_BYTE:
                return in.readUnsignedByte();
            case SHORT:
                return in.readShort();
            case UNSIGNED_SHORT:
                return in.readUnsignedShort();
            case INTEGER:
                return in.readInt();
            case UNSIGNED_INTEGER:
                return in.readUnsignedInt();
            case LONG:
                return in.readLong();
            case FLOAT:
                return in.readFloat();
            case DOUBLE:
                return in.readDouble();
            default:
                return null;
        }
    }

}
