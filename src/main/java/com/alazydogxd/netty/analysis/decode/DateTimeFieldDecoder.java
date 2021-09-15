package com.alazydogxd.netty.analysis.decode;

import com.alazydogxd.netty.analysis.message.MessageField;
import io.netty.buffer.ByteBuf;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author Mr_W
 * @date 2021/7/29 23:47
 * @description 日期解析
 */
public class DateTimeFieldDecoder implements MessageFieldDecoder<LocalDateTime> {
    @Override
    public String type() {
        return "DATETIME";
    }

    @Override
    public LocalDateTime decode(MessageField msg, ByteBuf in) {
        long timestamp = in.readLong();
        return Instant.ofEpochMilli(timestamp).atOffset(ZoneOffset.of("+8")).toLocalDateTime();
    }
}
