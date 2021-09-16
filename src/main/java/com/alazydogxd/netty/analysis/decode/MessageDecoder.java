package com.alazydogxd.netty.analysis.decode;

import com.alazydogxd.netty.analysis.exception.DecodeFailException;
import com.alazydogxd.netty.analysis.exception.MessageAnalysisFailException;
import com.alazydogxd.netty.analysis.message.MessageField;
import io.netty.buffer.ByteBuf;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mr_W
 * @date 2021/8/29 22:00
 * @description 报文解析器
 */
public class MessageDecoder {

    private List<List<MessageField>> messageFields = new LinkedList<>();

    private int index;

    private List<MessageField> currentMessageFields;

    private int currentIndex = -1;

    private List<List<MessageField>> prepareToAdd;

    private MessageDecoder() {
    }

    public static MessageDecoder createMessageDecoder(List<MessageField> messageFields) {
        MessageDecoder messageDecoder = new MessageDecoder();
        messageDecoder.messageFields.add(messageFields);
        messageDecoder.currentMessageFields = messageFields;
        return messageDecoder;
    }

    public void addMessageField(List<MessageField> messageFields) {
        messageFields.sort(Comparator.comparingInt(MessageField::getOrder));
        if (prepareToAdd == null) {
            prepareToAdd = new LinkedList<>();
        }
        prepareToAdd.add(messageFields);
    }

    public Object decode(ByteBuf in, Decode decode) throws DecodeFailException, MessageAnalysisFailException {
        return decode.decode(getMessageField(), in);
    }

    public MessageField getCurrentMessageField() {
        return currentMessageFields.get(currentIndex);
    }

    private MessageField getMessageField() {
        if (!isHaveCurrentMessageField()) {
            if (prepareToAdd != null) {
                messageFields.addAll(++index, prepareToAdd);
                currentMessageFields = messageFields.get(index);
                prepareToAdd = null;
            } else if (isHaveMessageField()) {
                currentMessageFields = messageFields.get(++index);
            } else {
                return null;
            }
            currentIndex = -1;
        }
        return currentMessageFields.get(++currentIndex);
    }

    public boolean isHaveMessageField() {
        return currentIndex < currentMessageFields.size() - 1 || index < messageFields.size() - 1 || (prepareToAdd != null && index < messageFields.size() + prepareToAdd.size() - 1);
    }

    public boolean isHaveCurrentMessageField() {
        return currentIndex < currentMessageFields.size() - 1;
    }

}
