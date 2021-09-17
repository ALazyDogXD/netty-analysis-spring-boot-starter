package com.alazydogxd.netty.analysis.message;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author Mr_W
 * @date 2021/9/18 0:05
 * @description 接收报文
 */
public interface Receive<T> {

    /**
     * 获取终端名
     *
     * @return 终端名
     */
    String name();

    /**
     * 接收报文
     *
     * @param ctx ChannelHandlerContext
     * @param msg 报文
     */
    void receive(ChannelHandlerContext ctx, T msg);

}
