package com.alazydogxd.netty.analysis.common.constant;

/**
 * @author Mr_W
 * @date 2021/7/29 20:49
 * @description 报文解析策略
 */
public interface AnalysisStrategyConstant {

    /** 报文长度固定 */
    String LENGTH_FIXED = "length fixed";

    /** 报文头包含报文总长度 */
    String HEADER = "header";

}
