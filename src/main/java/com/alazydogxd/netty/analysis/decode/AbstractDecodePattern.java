package com.alazydogxd.netty.analysis.decode;

import com.alazydogxd.netty.analysis.exception.DecodeFailException;
import com.alazydogxd.netty.analysis.exception.LostEndpointNameException;
import com.alazydogxd.netty.analysis.message.Configuration;
import com.alazydogxd.netty.analysis.message.MessageField;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.StringUtils;

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

    private String endpointName;

    private Configuration configuration;

    private List<MessageFieldSupport> messageFieldSupports = new ArrayList<>(15);

    public AbstractDecodePattern(List<MessageField> initMessageField) {
        this.initMessageField = new ArrayList<>(initMessageField);
    }

    public AbstractDecodePattern setEndpointName(String endpointName) {
        this.endpointName = endpointName;
        return this;
    }

    public String getEndpointName() {
        if (StringUtils.isEmpty(endpointName.trim())) {
            throw new LostEndpointNameException(String.format("%s 丢失终端名称", this.getClass().getSimpleName()));
        }
        return endpointName;
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
            if (addMessageField != null && addMessageField.endpointName().equals(getEndpointName())) {
                messageFieldSupports.add((MessageFieldSupport) bean);
            }
        }
        return bean;
    }
}
