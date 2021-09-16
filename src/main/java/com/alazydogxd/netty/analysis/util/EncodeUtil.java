package com.alazydogxd.netty.analysis.util;

import cn.hutool.core.convert.Convert;
import com.alazydogxd.netty.analysis.enums.BaseMessageType;
import com.alazydogxd.netty.analysis.message.CommonMessageField;
import com.alazydogxd.netty.analysis.message.MessageField;
import io.netty.buffer.ByteBuf;

import java.util.Arrays;

/**
 * @author Mr_W
 * @date 2021/9/16 21:35
 * @description 协议还原工具
 */
public class EncodeUtil {

    private EncodeUtil() {
    }

    /**
     * 基本类型还原
     *
     * @param messageField 报文字段
     * @param out          ByteBuf
     * @return true 解析成功
     */
    public static boolean baseRestore(MessageField messageField, ByteBuf out) {
        if (Arrays.stream(BaseMessageType.values()).noneMatch(messageType -> messageType.toString().equals(messageField.getType()))) {
            return false;
        }
        switch (BaseMessageType.valueOf(messageField.getType())) {
            case BYTE:
                out.writeByte(Convert.toByte(messageField.getValue()));
                return true;
            case UNSIGNED_BYTE:
            case SHORT:
                out.writeShort(Convert.toShort(messageField.getValue()));
                return true;
            case UNSIGNED_SHORT:
            case INTEGER:
                out.writeInt(Convert.toInt(messageField.getValue()));
                return true;
            case UNSIGNED_INTEGER:
            case LONG:
                out.writeLong(Convert.toLong(messageField.getValue()));
                return true;
            case FLOAT:
                out.writeFloat(Convert.toFloat(messageField.getValue()));
                return true;
            case DOUBLE:
                out.writeDouble(Convert.toDouble(messageField.getValue()));
                return true;
            case SHORT_LE:
                out.writeShortLE(Convert.toShort(messageField.getValue()));
                return true;
            case INTEGER_LE:
            case UNSIGNED_SHORT_LE:
                out.writeIntLE(Convert.toInt(messageField.getValue()));
                return true;
            case UNSIGNED_INTEGER_LE:
            case LONG_LE:
                out.writeLongLE(Convert.toLong(messageField.getValue()));
                return true;
            case FLOAT_LE:
                out.writeFloatLE(Convert.toFloat(messageField.getValue()));
                return true;
            case DOUBLE_LE:
                out.writeDoubleLE(Convert.toDouble(messageField.getValue()));
                return true;
            default:
                return false;
        }
    }

}
