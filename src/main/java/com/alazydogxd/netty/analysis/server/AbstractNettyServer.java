package com.alazydogxd.netty.analysis.server;

import com.alazydogxd.netty.analysis.handler.MessageDecodeHandler;
import com.alazydogxd.netty.analysis.message.Configuration;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.internal.ObjectUtil;
import org.slf4j.Logger;

import javax.annotation.PreDestroy;
import java.net.SocketAddress;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Mr_W
 * @date 2021/7/27 23:18
 * @description Netty Server
 */
public abstract class AbstractNettyServer {

    private static final Logger LOGGER = getLogger(AbstractNettyServer.class);

    private ServerBootstrap bootstrap;

    private EventLoopGroup boss;

    private EventLoopGroup worker;

    private Class<? extends ServerChannel> channelClass;

    private ChannelInitializer<? extends Channel> handler;

    private ChannelInitializer<? extends Channel> childHandler;

    private SocketAddress address;

    private final Map<ChannelOption<Object>, Object> options = new LinkedHashMap<>(16);

    private final Map<ChannelOption<Object>, Object> childOptions = new LinkedHashMap<>(16);

    private Channel channel;

    private String name;

    private Configuration configuration;

    public AbstractNettyServer(String name, Configuration configuration) {
        this.name = name;
        this.configuration = configuration;
    }

    /**
     * 启动服务
     */
    public final void bootstrap() {
        preInit();
        try {
            if ((bootstrap = initServerBootstrap()) == null) {

                bootstrap = new ServerBootstrap()
                        .group(Optional.of(boss).orElse(boss = new NioEventLoopGroup(1)),
                                Optional.of(worker).orElse(worker = new NioEventLoopGroup(10)))
                        .channel(channelClass == null ? (channelClass = NioServerSocketChannel.class) : channelClass)
                        .handler(handler == null ? (handler = new DefaultHandler()) : handler)
                        .childHandler(childHandler == null ? (childHandler = new DefaultChildHandler(name, configuration)) : childHandler);
                options.forEach((option, value) -> bootstrap.option(option, value));
                childOptions.forEach((childOption, value) -> bootstrap.childOption(childOption, value));

                configServerBootstrap(bootstrap);

                channel = configChannelFuture((address == null ? bootstrap.bind() : bootstrap.bind(address))
                        .syncUninterruptibly())
                        .channel();
                channel.closeFuture().syncUninterruptibly();
            }

        } catch (Exception e) {
            initException(e);
        }
    }

    /**
     * 初始化 ServerBootstrap
     *
     * @return ServerBootstrap
     */
    protected ServerBootstrap initServerBootstrap() {
        return null;
    }

    /**
     * 配置 ServerBootstrap
     *
     * @param bootstrap ServerBootstrap
     */
    protected void configServerBootstrap(ServerBootstrap bootstrap) {
    }

    /**
     * 配置 ChannelFuture
     *
     * @param future ChannelFuture
     */
    protected ChannelFuture configChannelFuture(ChannelFuture future) {
        return future;
    }

    /**
     * 初始化错误
     *
     * @param e 报错
     */
    protected void initException(Exception e) {
        LOGGER.error("Netty 初始化失败", e);
    }

    /**
     * 销毁
     */
    @PreDestroy
    public final void destroy() {
        preDestroy();
        doDestroy(boss, worker);
    }

    /**
     * 初始化之前的配置
     */
    protected void preInit() {
    }

    /**
     * 销毁之前
     */
    protected void preDestroy() {
    }

    /**
     * 销毁
     */
    protected void doDestroy(EventLoopGroup boss, EventLoopGroup worker) {
        boss.shutdownGracefully();
        worker.shutdownGracefully();
    }

    protected final AbstractNettyServer socketAddress(SocketAddress address) {
        this.address = address;
        return this;
    }

    protected final AbstractNettyServer channel(Class<? extends ServerChannel> channelClass) {
        this.channelClass = channelClass;
        return this;
    }

    protected final AbstractNettyServer boss(NioEventLoopGroup boss) {
        this.boss = boss;
        return this;
    }

    protected final AbstractNettyServer worker(NioEventLoopGroup worker) {
        this.worker = worker;
        return this;
    }

    protected final AbstractNettyServer handler(ChannelInitializer<? extends Channel> handler,
                                                ChannelInitializer<? extends Channel> childHandler) {
        this.handler = handler;
        this.childHandler = childHandler;
        return this;
    }

    @SuppressWarnings("unchecked")
    protected final <T> AbstractNettyServer option(ChannelOption<T> option, T value) {
        ObjectUtil.checkNotNull(option, "option");
        synchronized (options) {
            options.put((ChannelOption<Object>) option, value);
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    protected final <T> AbstractNettyServer childOption(ChannelOption<T> childOption, T value) {
        ObjectUtil.checkNotNull(childOption, "childOption");
        synchronized (childOptions) {
            childOptions.put((ChannelOption<Object>) childOption, value);
        }
        return this;
    }

    public Channel getChannel() {
        return channel;
    }

    public String getName() {
        return name;
    }

    public static class DefaultHandler extends ChannelInitializer<Channel> {

        @Override
        protected void initChannel(Channel ch) throws Exception {
            ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
        }

    }

    public static class DefaultChildHandler extends ChannelInitializer<Channel> {

        private String endpointName;

        private Configuration configuration;

        public DefaultChildHandler(String endpointName, Configuration configuration) {
            this.endpointName = endpointName;
            this.configuration = configuration;
        }

        @Override
        protected void initChannel(Channel ch) throws Exception {
            ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO), new MessageDecodeHandler(endpointName, configuration));
        }

    }
}
