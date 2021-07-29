package com.alazydogxd.netty.analysis.handler.decoder;

import com.alazydogxd.netty.analysis.common.Message;
import com.alazydogxd.netty.analysis.common.MessageDecoder;
import io.netty.buffer.ByteBuf;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author Mr_W
 * @date 2021/7/29 23:47
 * @description 日期解析
 */
public class DateTimeDecoder implements MessageDecoder<LocalDateTime> {
    @Override
    public LocalDateTime decode(Message msg, ByteBuf in) {
        long timestamp = in.readLong();
        return Instant.ofEpochMilli(timestamp).atOffset(ZoneOffset.of("+8")).toLocalDateTime();
    }
}
