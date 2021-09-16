package com.alazydogxd.netty.analysis.handler;

import com.alazydogxd.netty.analysis.exception.MessageAnalysisFailException;
import com.alazydogxd.netty.analysis.exception.UnExistMessageTypeException;
import com.alazydogxd.netty.analysis.message.MessageField;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.List;

import static com.alazydogxd.netty.analysis.util.EncodeUtil.baseRestore;

/**
 * @author Mr_W
 * @date 2021/9/16 2:16
 * @description 编码处理器
 */
public class MessageEncodeHandler extends MessageToByteEncoder<List<MessageField>> {

    @Override
    protected void encode(ChannelHandlerContext ctx, List<MessageField> msg, ByteBuf out) {

    }

//    private void encode(MessageField messageField, ByteBuf out) {
//        try {
//            if (baseRestore(messageField, out)) {
//                return;
//            }
//            if (!configuration.isHaveDecoder(messageField.getType())) {
//                throw new UnExistMessageTypeException("不存在报文类型");
//            }
//        } catch (Exception e) {
//            throw new MessageAnalysisFailException(String.format("字段 [%s] 解析失败", messageField.getFieldName()), e);
//        }
//        configuration.getMessageDecoder(messageField.getType()).decode(messageField, in);
//    }

}
