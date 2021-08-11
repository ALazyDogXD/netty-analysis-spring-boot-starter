package com.alazydogxd.netty.analysis.common;

import com.alazydogxd.netty.analysis.exception.DecodeFailException;
import com.alazydogxd.netty.analysis.message.MessageField;
import io.netty.buffer.ByteBuf;

/**
 * @author Mr_W
 * @date 2021/7/29 23:33
 * @description 报文解析
 */
public interface MessageDecoder<T> {

    /**
     * 报文解析
     *
     * @param msg 报文
     * @param in  buffer
     * @return 解析结果
     * @throws DecodeFailException 解码失败
     */
    T decode(Enum<? extends MessageField> msg, ByteBuf in) throws DecodeFailException;

}
