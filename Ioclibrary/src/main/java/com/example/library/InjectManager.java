package com.example.library;

import android.app.Activity;
import android.view.View;
import com.example.library.annation.ContentView;
import com.example.library.annation.EventBase;
import com.example.library.annation.InjectView;
import com.example.library.listener.ListenerInvocationHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InjectManager {

    public static void inject(Activity activity) {
        injectLayout(activity);
        injectViews(activity);
        injectEvents(activity);
    }

    private static void injectViews(Activity activity) {
        Class<?> clazz = activity.getClass();
        Field[] fields = clazz.getDeclaredFields();// 得到所有成员变量
        for (Field field : fields) {

            InjectView viewInject = field.getAnnotation(InjectView.class);
            if (viewInject != null) {
                int valueId = viewInject.value();

                try {
                    Method method = clazz.getMethod("findViewById", int.class);
                    method.setAccessible(true);
                    Object viewId = method.invoke(activity, valueId); // 需要对返回值赋值
                    if (viewId == null) {
                        continue;
                    }

                    // 设置属性
                    field.setAccessible(true);
                    field.set(activity, viewId);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    // 点击事件需要用到动态代理 三要素
    private static void injectEvents(Activity activity) {
        Class<?> clazz = activity.getClass();
        Method[] methods = clazz.getDeclaredMethods();// 得到自己类的所有方法
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations(); // 存在多个注解，便利所有注解
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> annotationType = annotation.annotationType();// 获取注解的类
                EventBase eventBase = annotationType.getAnnotation(EventBase.class);
                if (eventBase == null) {
                    continue;
                }
                // 获取三要素
                String listenerSetter = eventBase.listenerSetter();
                Class<?> listenerType = eventBase.listenerType();
                String callBackMethod = eventBase.callBackMethod();


                // 主要是对 view.setXXXListener 监听
                ListenerInvocationHandler handler = new ListenerInvocationHandler(activity);
                // 存储监听方法的集合
                handler.addMethod(callBackMethod,method);
                Object proxy = Proxy.newProxyInstance(listenerType.getClassLoader(),
                        new Class[]{listenerType},
                        handler);


                try {
                    // 获取注解的value
                    Method valueMethod = annotationType.getDeclaredMethod("value");
                    int[] valueIds = (int[]) valueMethod.invoke(annotation);

                    for (int viewId : valueIds) {

                        View view = activity.findViewById(viewId);
                        if (view == null) {
                            continue;
                        }

                        // 对View设置点击事件，并监听
                        Method setListener = view.getClass().getMethod(listenerSetter, listenerType);



                        // 设置点击事件  类似view.setonXXXXClickListener
                        setListener.invoke(view, proxy);
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void injectLayout(Activity activity) {
        Class<?> clazz = activity.getClass();
        ContentView contentView = clazz.getAnnotation(ContentView.class);
        if (contentView != null) {
            int layoutId = contentView.value();

            try {
                Method method = clazz.getMethod("setContentView", int.class);

                method.setAccessible(true);
                method.invoke(activity, layoutId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
