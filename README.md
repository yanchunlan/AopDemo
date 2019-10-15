# AopDemo
IOC ,Aop ,aop的AST清除log <br>

####  Aop ： 主要使用到了AspectJ，编译时代码注入<br>

####  使用流程：<br>

#####   1> 下载 [http://www.eclipse.org/aspectj/downloads.php](http://www.eclipse.org/aspectj/downloads.php) ,获取 aspectj-1.9.2.jar ,解压得到 aspectjrt.jar，并导入项目lib<br>

#####   2> 配置build.gradle , [http://fernandocejas.com/2014/08/03/aspect-oriented-programming-in-android/](http://fernandocejas.com/2014/08/03/aspect-oriented-programming-in-android/)<br>

module:

    mavenCentral()


    classpath 'org.aspectj:aspectjtools:1.8.9'
    classpath 'org.aspectj:aspectjweaver:1.8.9'

app：

    import org.aspectj.bridge.IMessage
    import org.aspectj.bridge.MessageHandler
    import org.aspectj.tools.ajc.Main


    final def log = project.logger
    final def variants = project.android.applicationVariants

    variants.all { variant ->
        if (!variant.buildType.isDebuggable()) {
            log.debug("Skipping non-debuggable build type '${variant.buildType.name}'.")
            return;
        }

        JavaCompile javaCompile = variant.javaCompile
        javaCompile.doLast {
            String[] args = ["-showWeaveInfo",
                             "-1.8",
                             "-inpath", javaCompile.destinationDir.toString(),
                             "-aspectpath", javaCompile.classpath.asPath,
                             "-d", javaCompile.destinationDir.toString(),
                             "-classpath", javaCompile.classpath.asPath,
                             "-bootclasspath", project.android.bootClasspath.join(
                    File.pathSeparator)]

            log.debug "ajc args: " + Arrays.toString(args)

            MessageHandler handler = new MessageHandler(true);
            new Main().run(args, handler);

            for (IMessage message : handler.getMessages(null, true)) {
                switch (message.getKind()) {
                    case IMessage.ABORT:
                    case IMessage.ERROR:
                    case IMessage.FAIL:
                        log.error message.message, message.thrown
                        break;
                    case IMessage.WARNING:
                        log.warn message.message, message.thrown
                        break;
                    case IMessage.INFO:
                        log.info message.message, message.thrown
                        break;
                    case IMessage.DEBUG:
                        log.debug message.message, message.thrown
                        break;
                }
            }
        }
    }

#####   3> 使用配置ioc<br>

CheckNet：

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface CheckNet {
    }

NetAspect：

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

MainActivity:

    @CheckNet
    public void click(View view) {
        //正常的业务逻辑
        Toast.makeText(this, "网络已连接", Toast.LENGTH_LONG).show();
    }

####   AST清除LOG
clearloglibrary：利用AST扫描语句块，清除LOG信息

####   自动捕获异常
[https://github.com/yanchunlan/AutoTryCatchDemo](https://github.com/yanchunlan/AutoTryCatchDemo)