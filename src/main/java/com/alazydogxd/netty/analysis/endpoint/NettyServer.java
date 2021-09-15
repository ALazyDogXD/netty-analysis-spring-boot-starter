package com.alazydogxd.netty.analysis.endpoint;

import com.alazydogxd.netty.analysis.decode.AbstractDecodePattern;
import com.alazydogxd.netty.analysis.handler.MessageDecodeHandler;
import com.alazydogxd.netty.analysis.message.MessageAnalysisConfiguration;
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
import java.util.Objects;
import java.util.Optional;

/**
 * @author Mr_W
 * @date 2021/7/27 23:18
 * @description Netty Server
 */
public class NettyServer extends AbstractNettyEndpoint<ServerBootstrap, ServerChannel> {

    protected Channel channel;

    protected SocketAddress address;

    protected AbstractDecodePattern decodePattern;

    public NettyServer(String name, MessageAnalysisConfiguration configuration) {
        super(name, configuration);
    }

    @Override
    protected ServerBootstrap initBootstrap() {
        Objects.requireNonNull(decodePattern, name + " 解析模式为空");
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(Optional.ofNullable(boss).orElse(boss = new NioEventLoopGroup(1)),
                Optional.ofNullable(worker).orElse(worker = new NioEventLoopGroup(10)));
        bootstrap.channel(channelClass == null ? (channelClass = NioServerSocketChannel.class) : channelClass);
        bootstrap.handler(handler == null ? (handler = new DefaultHandler()) : handler);
        bootstrap.childHandler(childHandler == null ? (childHandler = new DefaultChildHandler(decodePattern, configuration)) : childHandler);
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
     * 解析模式
     *
     * @param decodePattern 解析模式
     * @return AbstractNettyEndpoint
     */
    public final NettyServer decodePattern(AbstractDecodePattern decodePattern) {
        this.decodePattern = decodePattern.setEndpointName(name);
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

    public static class DefaultHandler extends ChannelInitializer<Channel> {

        @Override
        protected void initChannel(Channel ch) throws Exception {
            ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
        }

    }

    public static class DefaultChildHandler extends ChannelInitializer<Channel> {

        private AbstractDecodePattern decodePattern;

        private MessageAnalysisConfiguration configuration;

        public DefaultChildHandler(AbstractDecodePattern decodePattern, MessageAnalysisConfiguration configuration) {
            this.decodePattern = decodePattern;
            this.configuration = configuration;
        }

        @Override
        protected void initChannel(Channel ch) throws Exception {
            ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO), new MessageDecodeHandler(decodePattern, configuration));
        }

    }
}
