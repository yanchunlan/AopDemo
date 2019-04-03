package com.example.aopdemo.aspectj;


import android.content.Context;
import android.widget.Toast;
import com.example.aopdemo.aspectj.annotation.CheckNet;
import com.example.aopdemo.utils.NetWorkUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * 切面
 * 你想要切下来的部分（代码逻辑功能重复模块）
 */
@Aspect
public class NetAspect {
    /**
     * 根据切点 切成什么样子
     * * *(..)  可以处理所有的方法
     */
    @Pointcut("execution(@com.example.aopdemo.aspectj.annotation.CheckNet * *(..))")
    public void checkNetBehavior() {

    }

    /**
     * 切成什么样子之后，怎么去处理
     */
    @Around("checkNetBehavior()")
    public Object checkNet(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        if (signature != null) {
            CheckNet checkNet = signature.getMethod().getAnnotation(CheckNet.class);
            if (checkNet != null) {
                Object obj = joinPoint.getThis();
                Context context = NetWorkUtils.getContext(obj);
                if (context != null) {
                    if (!NetWorkUtils.isNetWorkAvailable(context)) {
                        Toast.makeText(context, "请检测网络", Toast.LENGTH_LONG).show();
                        return null;
                    }
                }
            }
        }
        return joinPoint.proceed();
    }
}
