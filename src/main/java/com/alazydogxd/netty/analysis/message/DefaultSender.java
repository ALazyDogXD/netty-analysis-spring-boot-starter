package com.alazydogxd.netty.analysis.message;

import java.util.List;

/**
 * @author Mr_W
 * @date 2021/9/18 1:21
 * @description 报文发送
 */
public class DefaultSender extends BaseSender<List<MessageField>> {
    @Override
    public void send(String name, List<MessageField> msg) {
        if (channels.containsKey(name)){
            channels.get(name).writeAndFlush(msg);
        }
    }
}
