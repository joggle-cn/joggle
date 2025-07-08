package com.wuweibi.bullet.protocol.strategy;

import com.wuweibi.bullet.protocol.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ResolvableType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * 消息处理策略
 *
 * @author marker
 * @version 1.0
 */
public interface  MessageHandlerStrategy<T> {
    // 日志
    Logger log = LoggerFactory.getLogger(MessageHandlerStrategy.class);

    /**
     * key:hander,value: msg 消息处理器缓存
     */
    Map<Class<?>, Class<?>> HANDLER_CLASS_CACHE = new HashMap<>();


    /**
     * 处理消息
     * @param msg
     * @throws IOException
     */
    void handle(T msg ) throws IOException;




    /**
     * 支持的命令类型
     * @param commandType
     * @return
     */
    default boolean supports(int commandType) {
        Class<T> clzz = getMessageType();
        MessageType messageType = clzz.getAnnotation(MessageType.class);
        if(messageType == null){
            log.error("{} @MessageType is null",clzz.getName());
//            throw new RuntimeException(String.format("%s @MessageType is null",clzz.getName()));
        }
        return messageType.value() == commandType;
    }

    /**
     * 通过handler注解T 获取消息类型
     * @return
     */
    default Class<T> getMessageType(){
        Class<T> tClass = (Class<T>) HANDLER_CLASS_CACHE.get(this.getClass());
        if (tClass != null) {
            return tClass;
        }

        ResolvableType resolvableType = ResolvableType.forClass(getClass())
                .as(MessageHandlerStrategy.class);
        @SuppressWarnings("unchecked")
        Class<T> type = (Class<T>) resolvableType.getGeneric(0).resolve();
        if (type == null) {
            throw new IllegalStateException("无法确定消息类型");
        }
        HANDLER_CLASS_CACHE.put(this.getClass(), type);
        return type;
    }

}
