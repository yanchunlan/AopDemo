package com.example.library.annation;



import android.widget.AdapterView;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EventBase(listenerSetter = "setOnItemClickListener",
        listenerType = AdapterView.OnItemClickListener.class,
        callBackMethod = "onItemClick")
public @interface OnItemClick {
    int[] value() default -1;
}
