package com.alazydogxd.netty.analysis.decode;

import com.alazydogxd.netty.analysis.annotation.CharFormat;
import com.alazydogxd.netty.analysis.exception.DecodeFailException;
import com.alazydogxd.netty.analysis.message.MessageField;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;

import java.nio.charset.Charset;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Mr_W
 * @date 2021/7/29 23:42
 * @description 字符解析
 */
public class StringFieldDecoder implements MessageFieldDecoder<String> {

    private static final Logger LOGGER = getLogger(StringFieldDecoder.class);

    @Override
    public String type() {
        return "CHAR";
    }

    @Override
    public String decode(MessageField msg, ByteBuf in) throws DecodeFailException {
        CharFormat charFormat;
        try {
            charFormat = msg.getClass().getDeclaredField(msg.toString()).getAnnotation(CharFormat.class);
        } catch (NoSuchFieldException e) {
            throw new DecodeFailException(String.format("字段 %s 解析失败", msg.getFieldName()), e);
        }
        return in.readCharSequence(msg.getLen(), charFormat == null ? UTF_8 : Charset.forName(charFormat.value())).toString();
    }

}
