package com.wuweibi.bullet.protocol;


import java.lang.annotation.*;

/**
 * 消息类型注解
 *
 * @author marker
 * @version 1.0
 */

@Retention(RetentionPolicy.RUNTIME)//运行时注解
@Target({ElementType.TYPE})// 类注解
public @interface MessageType {
   int value() default -1;
}
