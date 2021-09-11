package com.alazydogxd.netty.analysis.endpoint;

import com.alazydogxd.netty.analysis.handler.MessageDecodeHandler;
import com.alazydogxd.netty.analysis.message.Configuration;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.SocketAddress;
import java.util.Optional;

/**
 * @author Mr_W
 * @date 2021/7/27 23:18
 * @description Netty Server
 */
public class NettyServer extends AbstractNettyEndpoint<ServerBootstrap, ServerChannel> {

    protected Channel channel;

    protected SocketAddress address;

    public NettyServer(String name, Configuration configuration) {
        super(name, configuration);
    }

    @Override
    protected ServerBootstrap initBootstrap() {
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(Optional.of(boss).orElse(boss = new NioEventLoopGroup(1)),
                        Optional.of(worker).orElse(worker = new NioEventLoopGroup(10)))
                .channel(channelClass == null ? (channelClass = NioServerSocketChannel.class) : channelClass)
                .handler(handler == null ? (handler = new DefaultHandler()) : handler)
                .childHandler(childHandler == null ? (childHandler = new DefaultChildHandler(name, configuration)) : childHandler);
        options.forEach(bootstrap::option);
        childOptions.forEach(bootstrap::childOption);


        channel = configChannelFuture((address == null ? bootstrap.bind() : bootstrap.bind(address))
                .syncUninterruptibly())
                .channel();
        channel.closeFuture().syncUninterruptibly();
        return bootstrap;
    }

    /**
     * 设置 IP 地址和端口
     *
     * @param address address
     * @return AbstractNettyEndpoint
     */
    public final NettyServer socketAddress(SocketAddress address) {
        this.address = address;
        return this;
    }

    /**
     * 销毁
     */
    @Override
    protected void doDestroy() {
        boss.shutdownGracefully();
        worker.shutdownGracefully();
    }

    /**
     * 配置 ChannelFuture
     *
     * @param future ChannelFuture
     */
    protected ChannelFuture configChannelFuture(ChannelFuture future) {
        return future;
    }

    public static class DefaultHandler extends ChannelInitializer<NioServerSocketChannel> {

        @Override
        protected void initChannel(NioServerSocketChannel ch) throws Exception {
            ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
        }

    }

    public static class DefaultChildHandler extends ChannelInitializer<NioServerSocketChannel> {

        private String endpointName;

        private Configuration configuration;

        public DefaultChildHandler(String endpointName, Configuration configuration) {
            this.endpointName = endpointName;
            this.configuration = configuration;
        }

        @Override
        protected void initChannel(NioServerSocketChannel ch) throws Exception {
            ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO), new MessageDecodeHandler(endpointName, configuration));
        }

    }
}
