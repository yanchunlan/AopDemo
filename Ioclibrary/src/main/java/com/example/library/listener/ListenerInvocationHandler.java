package com.example.library.listener;

import android.content.Context;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态代理 代理点击事件
 * 主要是对事件里的方法代理
 */
public class ListenerInvocationHandler implements InvocationHandler {
    private Context context;
    private Map<String, Method> methodMap = new HashMap<>(); // 存储监听方法的集合

    public ListenerInvocationHandler(Context context ) {
        this.context = context;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String name = method.getName();
        Method m = methodMap.get(name);
        // 有就拦截他的方法，否则执行自己的方法
        if (m != null) {
            return m.invoke(context, args);
        } else {
            return  m.invoke(proxy, args);
        }
    }

    public void addMethod(String methodName, Method method) {
        methodMap.put(methodName, method);
    }
}
