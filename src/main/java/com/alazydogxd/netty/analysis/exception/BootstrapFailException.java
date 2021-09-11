package com.alazydogxd.netty.analysis.exception;

/**
 * @author Mr_W
 * @date 2021/9/11 14:03
 * @description 启动失败
 */
public class BootstrapFailException extends Exception {
    public BootstrapFailException(String message) {
        super(message);
    }

    public BootstrapFailException(String message, Throwable cause) {
        super(message, cause);
    }
}
