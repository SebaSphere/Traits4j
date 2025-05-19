package net.terradevelopment.traits4j;

import net.terradevelopment.traits4j.annotations.Trait;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        var classes = getAllClasses();
        System.out.println("MEOW");
        for (Class<?> clazz : classes) {
            addTraitData(clazz);
        }
    }

    // https://stackoverflow.com/questions/2557303/how-are-scala-traits-compiled-into-java-bytecode
    // worth a read

    // TODO: set a logical SuperClass<T> and make sure it isn't static for the interface.
    // TODO: make sure the Var<T> calls its own unique instance
    private static void addTraitData(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Trait.class)) {
            System.out.println(clazz.getName() + " is annotated with @Trait");
        }
    }

    private static List<Class<?>> getAllClasses() {
        List<Class<?>> classes = new ArrayList<>();
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String packageName = Main.class.getPackage().getName();
            String path = packageName.replace('.', '/');
            URL resource = classLoader.getResource(path);
            if (resource == null) {
                throw new IllegalArgumentException("No package found by the name of " + packageName);
            }
            File directory = new File(resource.getFile());
            String[] files = directory.list();

            for (String fileName : files) {
                if (fileName.endsWith(".class")) {
                    String className = packageName + '.' + fileName.substring(0, fileName.length() - 6);
                    classes.add(Class.forName(className));
                }
            }

            return classes;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }
}