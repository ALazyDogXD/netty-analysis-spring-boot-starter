package com.alazydogxd.netty.analysis.message;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import com.alazydogxd.netty.analysis.exception.EncodeFailException;
import com.alazydogxd.netty.analysis.message.MessageField;
import com.alazydogxd.netty.analysis.message.MessageFieldConverter;
import io.netty.buffer.ByteBuf;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author Mr_W
 * @date 2021/7/29 23:47
 * @description 日期解析
 */
public class DateTimeFieldConverter implements MessageFieldConverter<LocalDateTime> {
    @Override
    public String type() {
        return "DATETIME";
    }

    @Override
    public void encode(MessageField msg, ByteBuf out) throws EncodeFailException {
        Object v = msg.getValue();
        if (v instanceof String) {
            LocalDateTime time = DateUtil.parseLocalDateTime(v.toString());
            out.writeLong(time.toInstant(ZoneOffset.of("+8")).toEpochMilli());
        } else if (v instanceof Long) {
            out.writeLong(Convert.toLong(v));
        } else {
            throw new EncodeFailException(String.format("%s 类型错误 - %s, 需要时间类型", msg.getFieldName(), msg.getValue()));
        }
    }

    @Override
    public LocalDateTime decode(MessageField msg, ByteBuf in) {
        long timestamp = in.readLong();
        return Instant.ofEpochMilli(timestamp).atOffset(ZoneOffset.of("+8")).toLocalDateTime();
    }
}
