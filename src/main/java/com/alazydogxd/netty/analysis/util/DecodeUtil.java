package com.alazydogxd.netty.analysis.util;

import com.alazydogxd.netty.analysis.enums.BaseMessageType;
import io.netty.buffer.ByteBuf;

import java.util.Arrays;

/**
 * @author Mr_W
 * @date 2021/7/30 22:58
 * @description 解码工具
 */
public class DecodeUtil {

    private DecodeUtil() {}

    /**
     * 基本类型解析
     *
     * @param type 类型， 见 com.alazydogxd.netty.analysis.constant.MessageTypeConstant
     * @param in   ByteBuf
     * @return 解析结果
     */
    public static Object baseAnalysis(String type, ByteBuf in) {
        if (Arrays.stream(BaseMessageType.values()).anyMatch(messageType -> messageType.toString().equals(type))) {
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
