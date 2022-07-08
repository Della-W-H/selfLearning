package org.itstack.demo.design.mediator;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class Resources {

    //拿到资源路径下的 xml文件 读取到内存中

    public static Reader getResourceAsReader(String resource) throws IOException {
        //将 字节流转换成字符流
        return new InputStreamReader(getResourceAsStream(resource));
    }

    private static InputStream getResourceAsStream(String resource) throws IOException {
        //使用加载 本对象class文件的 类加载器 尝试加载 xml文件 为stream流对象

        ClassLoader[] classLoaders = getClassLoaders();
        for (ClassLoader classLoader : classLoaders) {
            InputStream inputStream = classLoader.getResourceAsStream(resource);
            if (null != inputStream) {
                return inputStream;
            }
        }
        throw new IOException("Could not find resource " + resource);
    }

    private static ClassLoader[] getClassLoaders() {
        return new ClassLoader[]{
                //这个拿到的就是系统类加载器 即AppClassLoader 我们开发人员开发 的类用到的字节码加载器 最顶层的加载器 我们java开发一般接触不到 c写的
                ClassLoader.getSystemClassLoader(),
                //获得上下文类加载器 这是一个虚构的加载器 他的具体形式视 类的加载情况而定
                Thread.currentThread().getContextClassLoader()};
    }

}
