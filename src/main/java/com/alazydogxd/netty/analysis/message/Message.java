//package com.alazydogxd.netty.analysis.message;
//
//import java.util.*;
//
///**
// * @author Mr_W
// * @date 2021/8/12 0:35
// * @description 报文
// */
//public class Message extends LinkedHashSet<Enum<? extends MessageField>> {
//
//    /** 报文唯一标识 */
//    private String name;
//
//    private Message pre;
//
//    private final Map<String, Message> nextMessages = new HashMap<>(16);
//
//    public static Message createMessage(String name, Class<Enum<? extends MessageField>> type) {
//        List<Enum<? extends MessageField>> messageField = Arrays.asList(type.getEnumConstants());
//        messageField.sort((Comparator.comparingInt(o -> ((MessageField) o).getOrder())));
//        Collections.reverse(messageField);
//        return new Message(name, messageField);
//    }
//
//    public Message(String name, Collection<? extends Enum<? extends MessageField>> c) {
//        super(c);
//        this.name = name;
//    }
//
//    public Message next(Message message) {
//        message.pre = this;
//        this.nextMessages.put(message.name, message);
//        return this;
//    }
//
//    public Message getNext(String name) {
//        return nextMessages.get(name);
//    }
//
//    /**
//     * 是否为报头
//     * @return true 报头
//     */
//    public boolean isHead() {
//        return pre == null;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) {return true;}
//        if (!(o instanceof Message)) {return false;}
//        Message enums = (Message) o;
//        return name.equals(enums.name);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(name);
//    }
//}
