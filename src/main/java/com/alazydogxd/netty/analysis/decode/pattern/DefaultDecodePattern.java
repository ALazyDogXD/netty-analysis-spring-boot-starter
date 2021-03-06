package com.alazydogxd.netty.analysis.decode.pattern;

import com.alazydogxd.netty.analysis.decode.*;
import com.alazydogxd.netty.analysis.exception.DecodeFailException;
import com.alazydogxd.netty.analysis.exception.MessageAnalysisFailException;
import com.alazydogxd.netty.analysis.message.MessageField;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mr_W
 * @date 2021/8/29 21:32
 * @description 默认解析模式
 */
public class DefaultDecodePattern extends AbstractDecodePattern implements BeanPostProcessor {

    private List<MessageField> initMessageField;

    private List<MessageFieldSupport> messageFieldSupports = new ArrayList<>(15);

    public DefaultDecodePattern(List<MessageField> initMessageField) {
        this.initMessageField = new ArrayList<>(initMessageField);
    }

    @Override
    public String getPatternName() {
        return "default";
    }

    @Override
    public Object decode(ChannelHandlerContext ctx, ByteBuf in, Analysis analysis) throws DecodeFailException, MessageAnalysisFailException {
        Map<String, Map<String, Object>> result = new LinkedHashMap<>(16);
        MessageDecoder messageDecoder = MessageDecoder.createMessageDecoder(initMessageField);

        while (messageDecoder.isHaveMessageField()) {
            Object resultField = processDecode(ctx, in, analysis, messageDecoder);
            MessageField currentMessageField = messageDecoder.getCurrentMessageField();
            if (!result.containsKey(currentMessageField.getUniqueMark())) {
                result.put(currentMessageField.getUniqueMark(), new LinkedHashMap<>(32));
            }
            result.get(currentMessageField.getUniqueMark()).put(messageDecoder.getCurrentMessageField().getFieldName(), resultField);
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

    protected Object buildResult(Map<String, Map<String, Object>> result) {
        return result;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof MessageFieldSupport) {
            AddMessageField addMessageField = bean.getClass().getAnnotation(AddMessageField.class);
            if (addMessageField != null && isHave(addMessageField.endpointNames())) {
                messageFieldSupports.add((MessageFieldSupport) bean);
            }
        }
        return bean;
    }

    /**
     * 解码
     *
     * @param ctx            ChannelHandlerContext
     * @param in             ByteBuf
     * @param analysis         解析方法
     * @param messageDecoder 解码器
     * @return 解析结果
     * @throws DecodeFailException          解码失败
     * @throws MessageAnalysisFailException 解析失败
     */
    private Object processDecode(ChannelHandlerContext ctx,
                                 ByteBuf in,
                                 Analysis analysis,
                                 MessageDecoder messageDecoder) throws DecodeFailException, MessageAnalysisFailException {
        return messageDecoder.decode(in, analysis);
    }
}
