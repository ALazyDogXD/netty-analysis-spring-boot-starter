package com.alazydogxd.netty.analysis.endpoint;

import com.alazydogxd.netty.analysis.message.BaseSender;
import com.alazydogxd.netty.analysis.message.CommonMessageField;
import com.alazydogxd.netty.analysis.message.MessageAnalysisConfiguration;
import com.alazydogxd.netty.analysis.message.MessageField;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.net.SocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Mr_W
 * @date 2021/7/27 23:18
 * @description Netty Server
 */
public class NettyClient extends AbstractNettyEndpoint<Bootstrap, Channel> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyClient.class);

    @Resource
    private ThreadPoolTaskExecutor pool;

    private Map<String, ? extends SocketAddress> address;

    private BaseSender<?> sender;

    private final Map<String, ChannelFuture> CONNECT_MAP = new ConcurrentHashMap<>(16);

    public NettyClient(String name,
                       MessageAnalysisConfiguration configuration,
                       Map<String, ? extends SocketAddress> address,
                       BaseSender<?> sender) {
        super(name, configuration);
        this.sender = sender;
        if (address == null) {
            this.address = new HashMap<>(0);
        } else {
            this.address = address;
        }
    }

    @Override
    protected Bootstrap initBootstrap() {
        Bootstrap bootstrap = new Bootstrap()
                .group(Optional.ofNullable(worker).orElse(worker = new NioEventLoopGroup(10)))
                .channel(channelClass == null ? (channelClass = NioSocketChannel.class) : channelClass)
                .handler(handler == null ? (handler = new DefaultHandler()) : handler);
        options.forEach(bootstrap::option);


        this.bootstrap = bootstrap;
        address.forEach(this::doConnect);
        return bootstrap;
    }

    private void doConnect(String name, SocketAddress address) {
        pool.execute(() -> connect(name, address));
    }

    private void connect(String name, SocketAddress address) {
        ChannelFuture connect = bootstrap.connect(address);
        connect.addListener((ChannelFutureListener) future -> {
            if (!future.isSuccess()) {
                future.channel().eventLoop().schedule(() -> {
                    LOGGER.warn("[{}] 连接失败, 10 秒后重连", name);
                    connect(name, address);
                }, 10, TimeUnit.SECONDS);
            }
        }).syncUninterruptibly();

        Channel channel = connect.channel();
        sender.addChannel(name, channel);
        CONNECT_MAP.put(name, connect);
        channel.closeFuture().addListener((ChannelFutureListener) closeFuture -> {
            sender.removeChannel(name);
            CONNECT_MAP.remove(name);
        }).syncUninterruptibly();
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

}
