package com.alazydogxd.netty.analysis.exception;

/**
 * @author Mr_W
 * @date 2021/9/17 21:38
 * @description 报文还原失败
 */
public class MessageRestoreFailException extends Exception {
    public MessageRestoreFailException(String message) {
        super(message);
    }

    public MessageRestoreFailException(String message, Throwable cause) {
        super(message, cause);
    }
}
