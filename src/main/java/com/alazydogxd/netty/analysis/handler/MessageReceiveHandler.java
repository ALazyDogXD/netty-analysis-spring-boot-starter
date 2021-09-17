package com.alazydogxd.netty.analysis.handler;

import com.alazydogxd.netty.analysis.decode.AbstractDecodePattern;
import com.alazydogxd.netty.analysis.message.CommonMessageField;
import com.alazydogxd.netty.analysis.message.MessageAnalysisConfiguration;
import com.alazydogxd.netty.analysis.message.MessageField;
import com.alazydogxd.netty.analysis.message.Receive;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Mr_W
 * @date 2021/9/17 23:53
 * @description 解码处理器
 */
@ChannelHandler.Sharable
public class MessageReceiveHandler extends ChannelInboundHandlerAdapter implements BeanPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageReceiveHandler.class);

    private final List<Receive<Map<String, Map<String, Object>>>> receives = new ArrayList<>(10);

    private String endpointName;

    private AbstractDecodePattern pattern;

    private MessageAnalysisConfiguration configuration;

    public MessageReceiveHandler(String endpointName,
                                 AbstractDecodePattern pattern,
                                 MessageAnalysisConfiguration configuration) {
        this.endpointName = endpointName;
        this.pattern = pattern;
        this.configuration = configuration;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        ctx.channel().pipeline().addFirst(new MessageDecodeHandler(pattern, configuration));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        LOGGER.debug(msg.toString());
        if (msg instanceof Map &&
                !((Map<?, ?>)msg).isEmpty() &&
                ((Map<?, ?>) msg).keySet().toArray()[0] instanceof String &&
                ((Map<?, ?>) msg).keySet().toArray()[0] instanceof Map) {
            for (Receive<Map<String, Map<String, Object>>> receive : receives) {
                receive.receive(ctx, (Map<String, Map<String, Object>>) msg);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Receive && endpointName.equals(((Receive<?>) bean).name())) {
            receives.add((Receive<Map<String, Map<String, Object>>>) bean);
        }
        return bean;
    }

}
