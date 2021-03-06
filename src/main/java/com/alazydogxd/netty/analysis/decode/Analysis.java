package com.alazydogxd.netty.analysis.decode;

import com.alazydogxd.netty.analysis.exception.DecodeFailException;
import com.alazydogxd.netty.analysis.exception.MessageAnalysisFailException;
import com.alazydogxd.netty.analysis.message.MessageField;
import io.netty.buffer.ByteBuf;

/**
 * @author Mr_W
 * @date 2021/7/30 23:20
 * @description 解码
 */
@FunctionalInterface
public interface Analysis {

    /**
     * 解码
     *
     * @param msg 报文
     * @param in  ByteBuf
     * @return 解析结果
     * @throws MessageAnalysisFailException 解析失败
     * @throws DecodeFailException          解码失败
     */
    Object analysis(MessageField msg, ByteBuf in) throws MessageAnalysisFailException, DecodeFailException;

}
