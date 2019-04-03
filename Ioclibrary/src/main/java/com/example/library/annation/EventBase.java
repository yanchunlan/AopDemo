package com.example.library.annation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 事件需要触发的三要素
 * 事件的拓展
 */

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventBase {

    String listenerSetter(); // 监听方法   类似于 setOnclickListener

    Class<?> listenerType(); // 监听类型  View.onXXXListener

    String callBackMethod(); // 回调方法  onclick  可能存在多个

}
