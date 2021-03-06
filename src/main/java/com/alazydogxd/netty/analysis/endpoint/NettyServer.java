package com.alazydogxd.netty.analysis.endpoint;

import com.alazydogxd.netty.analysis.decode.AbstractDecodePattern;
import com.alazydogxd.netty.analysis.handler.MessageReceiveHandler;
import com.alazydogxd.netty.analysis.message.MessageAnalysisConfiguration;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.net.SocketAddress;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Mr_W
 * @date 2021/7/27 23:18
 * @description Netty Server
 */
public class NettyServer extends AbstractNettyEndpoint<ServerBootstrap, ServerChannel> {

    @Resource
    private ThreadPoolTaskExecutor pool;

    protected Channel channel;

    protected SocketAddress address;

    public NettyServer(String name, MessageAnalysisConfiguration configuration) {
        super(name, configuration);
    }

    @Override
    protected ServerBootstrap initBootstrap() {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(Optional.ofNullable(boss).orElse(boss = new NioEventLoopGroup(1)),
                Optional.ofNullable(worker).orElse(worker = new NioEventLoopGroup(10)));
        bootstrap.channel(channelClass == null ? (channelClass = NioServerSocketChannel.class) : channelClass);
        bootstrap.handler(handler == null ? (handler = new DefaultHandler()) : handler);
        bootstrap.childHandler(childHandler == null ? (childHandler = new DefaultChildHandler()) : childHandler);
        options.forEach(bootstrap::option);
        childOptions.forEach(bootstrap::childOption);


        channel = configChannelFuture((address == null ? bootstrap.bind() : bootstrap.bind(address))
                .syncUninterruptibly())
                .channel();
        pool.execute(() -> channel.closeFuture().syncUninterruptibly());
        return bootstrap;
    }

    /**
     * ?????? IP ???????????????
     *
     * @param address address
     * @return AbstractNettyEndpoint
     */
    public final NettyServer socketAddress(SocketAddress address) {
        this.address = address;
        return this;
    }

    /**
     * ??????
     */
    @Override
    protected void doDestroy() {
        boss.shutdownGracefully();
        worker.shutdownGracefully();
    }

    /**
     * ?????? ChannelFuture
     *
     * @param future ChannelFuture
     */
    protected ChannelFuture configChannelFuture(ChannelFuture future) {
        return future;
    }

    private class DefaultHandler extends ChannelInitializer<Channel> {

        @Override
        protected void initChannel(Channel ch) {
            if (handlers.isEmpty()) {
                ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
            } else {
                ch.pipeline().addLast(handlers.toArray(new ChannelHandler[0]));
            }
        }

    }

    private class DefaultChildHandler extends ChannelInitializer<Channel> {

        @Override
        protected void initChannel(Channel ch) {
            if (childHandlers.isEmpty()) {
                ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
            } else {
                ch.pipeline().addLast(childHandlers.toArray(new ChannelHandler[0]));
            }
        }

    }
}
