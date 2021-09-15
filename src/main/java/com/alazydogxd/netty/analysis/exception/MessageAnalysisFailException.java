package com.alazydogxd.netty.analysis.exception;

/**
 * @author Mr_W
 * @date 2021/9/16 0:18
 * @description 报文解析失败
 */
public class MessageAnalysisFailException extends Exception {
    public MessageAnalysisFailException(String message) {
        super(message);
    }

    public MessageAnalysisFailException(String message, Throwable cause) {
        super(message, cause);
    }
}
