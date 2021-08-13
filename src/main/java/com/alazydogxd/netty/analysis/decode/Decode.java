package com.alazydogxd.netty.analysis.decode;

import com.alazydogxd.netty.analysis.exception.DecodeFailException;
import com.alazydogxd.netty.analysis.message.MessageField;
import io.netty.buffer.ByteBuf;

/**
 * @author Mr_W
 * @date 2021/7/30 23:20
 * @description 解码
 */
@FunctionalInterface
public interface Decode {

    /**
     * 解码
     *
     * @param msg 报文
     * @param in  ByteBuf
     * @return 解析结果
     * @throws DecodeFailException 解码失败
     */
    Object decode(Enum<? extends MessageField> msg, ByteBuf in) throws DecodeFailException;

}
