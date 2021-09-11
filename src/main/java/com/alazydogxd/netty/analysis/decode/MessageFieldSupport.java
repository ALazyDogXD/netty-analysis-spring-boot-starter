package com.alazydogxd.netty.analysis.decode;

import com.alazydogxd.netty.analysis.message.MessageField;

import java.util.List;

/**
 * @author Mr_W
 * @date 2021/8/30 1:00
 * @description 报文字段解析
 */
public interface MessageFieldSupport {

    /**
     * 是否是判断字段
     *
     * @param m   报文字段
     * @param <M> 报文字段
     * @return true 是判断字段
     */
    <M extends MessageField> boolean isThis(M m);

    /**
     * 追加报文解析字段（多批追加）
     *
     * @param m              报文字段
     * @param value          报文字段值
     * @param messageDecoder 报文解析器
     * @param <M>            报文字段
     * @return 追加报文解析字段
     */
    <M extends MessageField> List<List<MessageField>> addMessageFields(M m, Object value, MessageDecoder messageDecoder);

}
