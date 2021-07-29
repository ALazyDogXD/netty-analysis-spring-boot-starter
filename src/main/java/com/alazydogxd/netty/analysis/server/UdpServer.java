package com.alazydogxd.netty.analysis.server;

import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author Mr_W
 * @date 2021/7/27 23:17
 * @description Netty Upd 服务端
 */
public class UdpServer extends AbstractNettyServer {

    @Override
    protected void preInit() {
        channel(NioServerSocketChannel.class);

    }
}
