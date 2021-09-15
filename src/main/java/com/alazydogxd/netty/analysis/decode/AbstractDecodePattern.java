package com.alazydogxd.netty.analysis.decode;

import com.alazydogxd.netty.analysis.exception.DecodeFailException;
import com.alazydogxd.netty.analysis.exception.MessageAnalysisFailException;
import com.alazydogxd.netty.analysis.message.MessageAnalysisConfiguration;
import com.alazydogxd.netty.analysis.message.MessageField;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.*;

/**
 * @author Mr_W
 * @date 2021/7/30 22:53
 * @description 自定义解码策略
 */
public abstract class AbstractDecodePattern implements BeanPostProcessor {

    private List<MessageField> initMessageField;

    private Set<String> endpointNames = new HashSet<>(16);

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

    public boolean isHave(String[] endpointName) {
        return endpointNames.containsAll(Arrays.asList(endpointName));
    }

    /**
     * 解码
     *
     * @param ctx            ChannelHandlerContext
     * @param in             ByteBuf
     * @param decode         解析方法
     * @param messageDecoder 解码器
     * @return 解析结果
     * @throws DecodeFailException          解码失败
     * @throws MessageAnalysisFailException 解析失败
     */
    public abstract Object processDecode(ChannelHandlerContext ctx,
                                         ByteBuf in,
                                         Decode decode,
                                         MessageDecoder messageDecoder) throws DecodeFailException, MessageAnalysisFailException;

    public Object decode(ChannelHandlerContext ctx, ByteBuf in, Decode decode) throws DecodeFailException, MessageAnalysisFailException {
        Map<String, Map<String, Object>> result = new LinkedHashMap<>(16);
        MessageDecoder messageDecoder = MessageDecoder.createMessageDecoder(initMessageField);

        while (messageDecoder.isHaveMessageField()) {
            Object resultField = processDecode(ctx, in, decode, messageDecoder);
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
}
