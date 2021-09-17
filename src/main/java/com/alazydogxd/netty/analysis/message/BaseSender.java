package com.alazydogxd.netty.analysis.message;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mr_W
 * @date 2021/9/18 1:11
 * @description 报文发送
 */
public abstract class BaseSender<T> {

    protected final Map<String, Channel> channels = new ConcurrentHashMap<>(16);

    /**
     * 报文发送
     *
     * @param name 远程地址名
     * @param msg  报文
     */
    public abstract void send(String name, T msg);

    /**
     * 添加管道
     *
     * @param name    远程地址名
     * @param channel 管道
     */
    public final void addChannel(String name, Channel channel) {
        synchronized (channels) {
            channels.put(name, channel);
        }
    }

    /**
     * 移除管道
     * @param name 远程地址名
     */
    public final void removeChannel(String name) {
        synchronized (channels) {
            channels.remove(name);
        }
    }

}
