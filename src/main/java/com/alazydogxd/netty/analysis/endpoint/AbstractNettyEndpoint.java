package com.alazydogxd.netty.analysis.endpoint;

import com.alazydogxd.netty.analysis.exception.BootstrapFailException;
import com.alazydogxd.netty.analysis.message.MessageAnalysisConfiguration;
import io.netty.bootstrap.AbstractBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.internal.ObjectUtil;
import org.slf4j.Logger;

import javax.annotation.PreDestroy;
import java.util.*;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Mr_W
 * @date 2021/9/11 11:57
 * @description 终端
 */
public abstract class AbstractNettyEndpoint<T extends AbstractBootstrap<T, C>, C extends Channel> {

    private static final Logger LOGGER = getLogger(AbstractNettyEndpoint.class);

    protected T bootstrap;

    protected String name;

    protected EventLoopGroup boss;

    protected EventLoopGroup worker;

    protected Class<? extends C> channelClass;

    protected ChannelInitializer<? extends Channel> handler;

    protected ChannelInitializer<? extends Channel> childHandler;

    protected final List<ChannelHandler> handlers = new ArrayList<>(5);

    protected final List<ChannelHandler> childHandlers = new ArrayList<>(5);

    protected final Map<ChannelOption<Object>, Object> options = new LinkedHashMap<>(16);

    protected final Map<ChannelOption<Object>, Object> childOptions = new LinkedHashMap<>(16);

    protected MessageAnalysisConfiguration configuration;

    protected AbstractNettyEndpoint(String name, MessageAnalysisConfiguration configuration) {
        this.name = name;
        this.configuration = configuration;
    }

    /**
     * 启动服务
     */
    public final void bootstrap() throws BootstrapFailException {
        preInit();
        try {
            if (bootstrap == null) {
                bootstrap = initBootstrap();
            }
        } catch (Exception e) {
            LOGGER.error("Netty 初始化失败", e);
            destroy();
            initException(e);
        }
    }

    /**
     * 初始化之前的配置
     */
    protected void preInit() {
    }

    /**
     * 初始化 ServerBootstrap
     *
     * @return ServerBootstrap
     */
    protected abstract T initBootstrap();

    /**
     * 销毁之前
     */
    protected void preDestroy() {
    }

    /**
     * 销毁
     */
    protected abstract void doDestroy();

    /**
     * 销毁
     */
    @PreDestroy
    public final void destroy() {
        preDestroy();
        doDestroy();
    }

    /**
     * 初始化错误
     *
     * @param e 报错
     * @throws BootstrapFailException 启动失败
     */
    protected void initException(Exception e) throws BootstrapFailException {
        throw new BootstrapFailException(String.format("%s 启动失败", name), e);
    }

    /**
     * 接收请求
     *
     * @param boss 接收请求
     * @return AbstractNettyEndpoint
     */
    public final AbstractNettyEndpoint<T, C> boss(NioEventLoopGroup boss) {
        this.boss = boss;
        return this;
    }

    /**
     * IO 线程池
     *
     * @param worker IO 线程池
     * @return AbstractNettyEndpoint
     */
    public final AbstractNettyEndpoint<T, C> worker(NioEventLoopGroup worker) {
        this.worker = worker;
        return this;
    }

    /**
     * 设置管道
     *
     * @param channelClass channelClass
     * @return AbstractNettyEndpoint
     */
    public final AbstractNettyEndpoint<T, C> channel(Class<C> channelClass) {
        this.channelClass = channelClass;
        return this;
    }

    /**
     * IO 线程池
     *
     * @param handler      处理器
     * @param childHandler 子处理器
     * @return AbstractNettyEndpoint
     */
    public final AbstractNettyEndpoint<T, C> handler(ChannelInitializer<C> handler,
                                                     ChannelInitializer<C> childHandler) {
        this.handler = handler;
        this.childHandler = childHandler;
        return this;
    }

    public final AbstractNettyEndpoint<T, C> addHandler(ChannelHandler... handler) {
        handlers.addAll(Arrays.asList(handler));
        return this;
    }

    public final AbstractNettyEndpoint<T, C> addChildHandler(ChannelHandler... childHandler) {
        childHandlers.addAll(Arrays.asList(childHandler));
        return this;
    }

    /**
     * 配置项
     *
     * @param option 配置项
     * @param value  配置值
     * @return AbstractNettyEndpoint
     */
    @SuppressWarnings("unchecked")
    public final <V> AbstractNettyEndpoint<T, C> option(ChannelOption<V> option, V value) {
        ObjectUtil.checkNotNull(option, "option");
        synchronized (options) {
            options.put((ChannelOption<Object>) option, value);
        }
        return this;
    }

    /**
     * 子配置项
     *
     * @param childOption 子配置项
     * @param value       子配置值
     * @return AbstractNettyEndpoint
     */
    @SuppressWarnings("unchecked")
    public final <V> AbstractNettyEndpoint<T, C> childOption(ChannelOption<V> childOption, V value) {
        ObjectUtil.checkNotNull(childOption, "childOption");
        synchronized (childOptions) {
            childOptions.put((ChannelOption<Object>) childOption, value);
        }
        return this;
    }

    /**
     * 终端名
     *
     * @return 终端名
     */
    public String getName() {
        return name;
    }


}
