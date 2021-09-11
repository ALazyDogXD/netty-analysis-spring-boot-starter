package com.alazydogxd.netty.analysis.endpoint;

import com.alazydogxd.netty.analysis.message.Configuration;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.SocketAddress;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mr_W
 * @date 2021/7/27 23:18
 * @description Netty Server
 */
public class NettyClient extends AbstractNettyEndpoint<Bootstrap, Channel> {

    private Map<String, SocketAddress> address;

    private final Map<String, ChannelFuture> CONNECT_MAP = new ConcurrentHashMap<>(16);

    public NettyClient(String name, Configuration configuration, Map<String, SocketAddress> address) {
        super(name, configuration);
        this.address = address;
    }

    @Override
    protected Bootstrap initBootstrap() {
        Bootstrap bootstrap = new Bootstrap()
                .group(Optional.of(worker).orElse(worker = new NioEventLoopGroup(10)))
                .channel(channelClass == null ? (channelClass = NioServerSocketChannel.class) : channelClass)
                .handler(handler == null ? (handler = new NettyServer.DefaultHandler()) : handler);
        options.forEach(bootstrap::option);

        address.forEach((name, address) -> {
            ChannelFuture connect = bootstrap.connect(address);
            connect.channel().closeFuture().syncUninterruptibly();
            CONNECT_MAP.put(name, connect);
        });

        return bootstrap;
    }

    public ChannelFuture getConnect(String connectName) {
        return CONNECT_MAP.get(connectName);
    }

    /**
     * 销毁
     */
    @Override
    protected void doDestroy() {
        worker.shutdownGracefully();
    }

}
