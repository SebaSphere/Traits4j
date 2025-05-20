package net.terradevelopment.traits4j.clazz;

import java.io.IOException;
import java.io.InputStream;

public class DynamicClassLoader extends ClassLoader {

    byte[] classNodeBytes;

    public DynamicClassLoader(byte[] classNodeBytes) {
        super(ClassLoader.getPlatformClassLoader());
        this.classNodeBytes = classNodeBytes;
    }

    public Class<?> defineClass(String name, byte[] b) {
        System.out.println("MEOW");
        System.out.println(name);
        return defineClass(name, b, 0, b.length);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return super.loadClass(name);
    }

    protected Class<?> findClass(String name) {
        System.out.println(name + " YEST");
        InputStream inputStream = getClass().getResourceAsStream("/" + name.replaceAll("\\.", "/") + ".class");
        byte[] process = null;
        try {
            process = inputStream.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return this.defineClass(name, process);

    }

}