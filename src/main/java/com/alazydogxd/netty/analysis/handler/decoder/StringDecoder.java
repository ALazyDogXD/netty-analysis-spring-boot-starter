package com.alazydogxd.netty.analysis.handler.decoder;

import com.alazydogxd.netty.analysis.annotation.CharFormat;
import com.alazydogxd.netty.analysis.common.Message;
import com.alazydogxd.netty.analysis.common.MessageDecoder;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author Mr_W
 * @date 2021/7/29 23:42
 * @description 字符解析
 */
public class StringDecoder implements MessageDecoder<String> {

    @Override
    public String decode(Message msg, ByteBuf in) {
        CharFormat charFormat = msg.getClass().getAnnotation(CharFormat.class);
        return in.readCharSequence(msg.getLen(), charFormat == null ? UTF_8 : Charset.forName(charFormat.value())).toString();
    }

}
