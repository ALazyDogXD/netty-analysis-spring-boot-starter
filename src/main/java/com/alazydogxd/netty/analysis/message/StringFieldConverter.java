package com.alazydogxd.netty.analysis.message;

import com.alazydogxd.netty.analysis.annotation.CharFormat;
import com.alazydogxd.netty.analysis.exception.DecodeFailException;
import com.alazydogxd.netty.analysis.exception.EncodeFailException;
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
public class StringFieldConverter implements MessageFieldConverter<String> {

    private static final Logger LOGGER = getLogger(StringFieldConverter.class);

    @Override
    public String type() {
        return "CHAR";
    }

    @Override
    public void encode(MessageField msg, ByteBuf out) throws EncodeFailException {
        CharFormat charFormat;
        try {
            charFormat = msg.getClass().getDeclaredField(msg.toString()).getAnnotation(CharFormat.class);
        } catch (NoSuchFieldException e) {
            throw new EncodeFailException(String.format("字段 %s 解析失败", msg.getFieldName()), e);
        }
        if (msg.getValue() instanceof String) {
            out.writeCharSequence(msg.getValue().toString(), charFormat == null ? UTF_8 : Charset.forName(charFormat.value()));
        } else {
            throw new EncodeFailException(String.format("%s 类型错误 - %s, 需要字符类型", msg.getFieldName(), msg.getValue()));
        }
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
