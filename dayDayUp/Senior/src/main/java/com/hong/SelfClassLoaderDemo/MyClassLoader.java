package com.hong.SelfClassLoaderDemo;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author wanghong
 * @date 2022/5/30
 * @apiNotec 炫码课堂 008-面试题 jar包冲突的解决方案
 * 当idea 并不能很好的解决 jar冲突 尤其是在 一个项目迭代时 老部件依赖的一个老的jar，而新部件开发依赖这个jar包的新的版本，新老版本合并时就会出现jar包冲突 而这还是idea无法解决的
 */
public class MyClassLoader extends ClassLoader {

    private Map<String, byte[]> clazz;

    public MyClassLoader(String... files) throws IOException {
        clazz = new HashMap<>();
        for (String file : files) {
            //解析出来 xxx/com/test/a.class  ===> xxx.com.test.a
            JarFile jarFile = new JarFile(file);
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                if (jarEntry.getName().endsWith(".class")) {
                    try (InputStream inputStream = jarFile.getInputStream(jarEntry)) {
                        int pos = 0;
                        int len;
                        byte[] bytes = new byte[inputStream.available()];
                        while ((len = inputStream.read(bytes, pos, bytes.length - pos)) > 0) {
                            pos += len;
                        }
                        String className = jarEntry.getName().replace("/", ".")
                                .replace(".class", "");
                        clazz.put(className, bytes);
                    }
                }
            }
        }
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            Class<?> loadedClass = findLoadedClass(name);
            //如果loadedClass==null，则直接从map拿
            if (loadedClass == null) {
                byte[] bytes = clazz.get(name);
                if (bytes == null) {
                    //map中也没有即找父类加载
                    loadedClass = super.loadClass(name);
                } else {
                    //二进制转化为class文件
                    loadedClass = defineClass(name, bytes, 0, bytes.length);
                }
            }
            return loadedClass;
        }
    }

    public static void main(String[] args) {
        test1();
        try {
            test2();
        } catch (IOException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void test1() {
        System.out.println(StringUtils.isEmpty("asd"));
        System.out.println(StringUtils.class.getClassLoader().getClass().getName());
    }

    public static void test2() throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        MyClassLoader classLoader = new MyClassLoader("D:\\SelfLearning\\jar\\commons-lang3-3.11.jar");
        Class<?> aClass = classLoader.loadClass("org.apache.commons.lang3.StringUtils");
        System.out.println(aClass.getClassLoader().getClass().getName());
        Method isEmpty = aClass.getMethod("isEmpty", CharSequence.class);
        System.out.println(isEmpty.invoke(null, ""));

    }
}
