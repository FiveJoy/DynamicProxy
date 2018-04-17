package com.fivejoy.base;

import javax.security.auth.Subject;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;


//参考链接：http://www.cnblogs.com/xiaoluo501395377/p/3383130.html
//https://blog.csdn.net/likewindy/article/details/51352179
//http://zhuwx.iteye.com/blog/2368356
public class Main {
    public static void main(String[] args){
        //    我们要代理的真实对象
       Hellor realHellor=new ActualSubject();

        //    我们要代理哪个真实对象，就将该对象传进去，最后是通过该真实对象来调用其方法的
        InvocationHandler handler = new DynamicProxy(realHellor);

        /*
         * 通过Proxy的newProxyInstance方法来创建我们的代理对象，我们来看看其三个参数
         * 第一个参数 handler.getClass().getClassLoader() ，我们这里使用handler这个类的ClassLoader对象来加载我们的代理对象
         * 第二个参数realSubject.getClass().getInterfaces()，我们这里为代理对象提供的接口是真实对象所实行的接口，表示我要代理的是该真实对象，这样我就能调用这组接口中的方法了
         * 第三个参数handler， 我们这里将这个代理对象关联到了上方的 InvocationHandler 这个对象上
         */
        Hellor proxyHellor = (Hellor) Proxy.newProxyInstance(
                handler.getClass().getClassLoader(),
                realHellor.getClass().getInterfaces(),
                handler
        );
        //三个参数分别是：
        // 使用哪个类家在其对代理类进行加载
        // 要给需要代理的对象提供什么接口
        // 需要代理的对象 将会关联的handler


        System.out.println(proxyHellor.getClass().getName());
        //打印结果为：com.sun.proxy.$Proxy0
        //本以为返回的这个代理对象会是Hellor类型的对象，或者是InvocationHandler的对象，结果都不是
        //第一点：为什么我们这里可以将其转化为Subject类型的对象？
        //      原因就是在newProxyInstance这个方法的第二个参数上，
        //      我们给这个代理对象提供了一组什么接口，那么我这个代理对象就会实现了这组接口
        //      这个时候我们当然可以将这个代理对象强制类型转化为这组接口中的任意一个
        //      因为这里的接口是Subject类型，所以就可以将其转化为Subject类型了

        //同时要记住，通过 Proxy.newProxyInstance 创建的代理对象是在jvm运行时动态生成的一个对象
        // 它并不是我们的InvocationHandler类型，也不是我们定义的那组接口的类型，
        // 而是在运行是动态生成的一个对象，并且命名方式都是这样的形式，以$开头，proxy居中，最后一个数字表示对象的标号
        System.out.println("开始实验：");
       proxyHellor.sayHello();//这里是 利用代理对象执行方法
        //通过代理对象来调用实现的Hellor接口中的方法，
        // 这个时候程序就会跳转到由这个代理对象关联到的 handler 中的invoke方法去执行，
        // 而我们的这个 handler 对象又接受了一个 RealSubject类型的参数，
        // 表示我要代理的就是这个真实对象，所以在调用method.invoke()的方法时执行了real对象的方法：
        HashMap<String,String> map=new HashMap<>();
        map.put("01","unatural");



    }
}
