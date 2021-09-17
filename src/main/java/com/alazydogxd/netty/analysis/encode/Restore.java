package com.alazydogxd.netty.analysis.encode;

import com.alazydogxd.netty.analysis.exception.EncodeFailException;
import com.alazydogxd.netty.analysis.exception.MessageRestoreFailException;
import com.alazydogxd.netty.analysis.message.MessageField;
import io.netty.buffer.ByteBuf;

/**
 * @author Mr_W
 * @date 2021/9/17 22:02
 * @description 编码
 */
@FunctionalInterface
public interface Restore {

    /**
     * 编码
     *
     * @param msg 报文
     * @param in  ByteBuf
     * @throws MessageRestoreFailException 还原失败
     * @throws EncodeFailException         编码失败
     */
    void restore(MessageField msg, ByteBuf in) throws MessageRestoreFailException, EncodeFailException;

}
