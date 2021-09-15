package com.alazydogxd.netty.analysis.decode;

import com.alazydogxd.netty.analysis.exception.DecodeFailException;
import com.alazydogxd.netty.analysis.message.MessageAnalysisConfiguration;
import com.alazydogxd.netty.analysis.message.MessageField;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mr_W
 * @date 2021/7/30 22:53
 * @description 自定义解码策略
 */
public abstract class AbstractDecodePattern implements BeanPostProcessor {

    private List<MessageField> initMessageField;

    private List<String> endpointNames = new ArrayList<>(10);

    private MessageAnalysisConfiguration configuration;

    private List<MessageFieldSupport> messageFieldSupports = new ArrayList<>(15);

    public AbstractDecodePattern(List<MessageField> initMessageField) {
        this.initMessageField = new ArrayList<>(initMessageField);
    }

    /**
     * 策略名称
     *
     * @return 策略名称
     */
    public abstract String getPatternName();

    public AbstractDecodePattern setEndpointName(String endpointName) {
        this.endpointNames.add(endpointName);
        return this;
    }

    public boolean isHave(String endpointName) {
        return endpointNames.contains(endpointName);
    }

    /**
     * 解码
     *
     * @param ctx            ChannelHandlerContext
     * @param in             ByteBuf
     * @param decode         解析方法
     * @param messageDecoder 解码器
     * @return 解析结果
     * @throws DecodeFailException 解析失败
     */
    public abstract Object processDecode(ChannelHandlerContext ctx,
                                         ByteBuf in,
                                         Decode decode,
                                         MessageDecoder messageDecoder) throws DecodeFailException;

    public Object decode(ChannelHandlerContext ctx, ByteBuf in, Decode decode) throws DecodeFailException {
        Map<String, Object> result = new HashMap<>(64);
        MessageDecoder messageDecoder = MessageDecoder.createMessageDecoder(initMessageField);

        while (messageDecoder.isHaveMessageField()) {
            Object resultField = processDecode(ctx, in, decode, messageDecoder);
            result.put(messageDecoder.getCurrentMessageField().getFieldName(), resultField);
            handleMessageFields(messageDecoder.getCurrentMessageField(), resultField, messageDecoder);
        }

        return buildResult(result);
    }

    private void handleMessageFields(MessageField messageField,
                                     Object resultField,
                                     MessageDecoder messageDecoder) {
        messageFieldSupports.forEach(messageFieldSupport -> {
            if (messageFieldSupport.isThis(messageField)) {
                messageFieldSupport.addMessageFields(messageField, resultField, messageDecoder);
            }
        });
    }

    protected Object buildResult(Map<String, Object> result) {
        return result;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof MessageFieldSupport) {
            AddMessageField addMessageField = bean.getClass().getAnnotation(AddMessageField.class);
            if (addMessageField != null && isHave(addMessageField.endpointName())) {
                messageFieldSupports.add((MessageFieldSupport) bean);
            }
        }
        return bean;
    }
}
