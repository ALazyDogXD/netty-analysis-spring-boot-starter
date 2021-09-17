package com.alazydogxd.netty.analysis.message;

import com.alazydogxd.netty.analysis.exception.DecodeFailException;
import com.alazydogxd.netty.analysis.exception.EncodeFailException;
import com.alazydogxd.netty.analysis.message.MessageField;
import io.netty.buffer.ByteBuf;

/**
 * @author Mr_W
 * @date 2021/7/29 23:33
 * @description 报文字段解析
 */
public interface MessageFieldConverter<T> {

    /**
     * 解析类型
     *
     * @return 类型
     */
    String type();

    /**
     * 报文解析
     *
     * @param msg 报文
     * @param in  buffer
     * @return 解析结果
     * @throws DecodeFailException 解码失败
     */
    T decode(MessageField msg, ByteBuf in) throws DecodeFailException;

    /**
     * 报文还原
     *
     * @param msg 报文
     * @param out buffer
     * @throws EncodeFailException 还原失败
     */
    void encode(MessageField msg, ByteBuf out) throws EncodeFailException;

}
