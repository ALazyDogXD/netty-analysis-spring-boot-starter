package com.alazydogxd.netty.analysis.encode;

import com.alazydogxd.netty.analysis.exception.EncodeFailException;
import com.alazydogxd.netty.analysis.exception.MessageRestoreFailException;
import com.alazydogxd.netty.analysis.message.MessageAnalysisConfiguration;
import com.alazydogxd.netty.analysis.message.MessageField;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Mr_W
 * @date 2021/9/16 21:33
 * @description 抽象协议还原模式
 */
public abstract class AbstractEncodePattern {

    protected Set<String> endpointNames = new HashSet<>(16);

    protected MessageAnalysisConfiguration configuration;

    /**
     * 编码
     *
     * @param ctx     ChannelHandlerContext
     * @param msg     List<MessageField>
     * @param out     ByteBuf
     * @param restore Restore
     * @throws MessageRestoreFailException 还原失败
     * @throws EncodeFailException         编码失败
     */
    public abstract void encode(ChannelHandlerContext ctx, List<MessageField> msg, ByteBuf out, Restore restore) throws MessageRestoreFailException, EncodeFailException;

    /**
     * 策略名称
     *
     * @return 策略名称
     */
    public abstract String getPatternName();

    public AbstractEncodePattern setEndpointName(String endpointName) {
        this.endpointNames.add(endpointName);
        return this;
    }

    public AbstractEncodePattern setConfiguration(MessageAnalysisConfiguration configuration) {
        if (this.configuration == null) {
            this.configuration = configuration;
        }
        return this;
    }

    public boolean isHave(String[] endpointName) {
        return endpointNames.containsAll(Arrays.asList(endpointName));
    }
}
