package net.terradevelopment.traits4j;

import net.terradevelopment.traits4j.annotations.Trait;
import net.terradevelopment.traits4j.clazz.TraitClassVisitor;
import net.terradevelopment.traits4j.clazz.TraitTester;
import net.terradevelopment.traits4j.test.ExampleClass;
import net.terradevelopment.traits4j.test.TraitExample;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("MAI");
        new Main().run();

    }

    public int test = 2;

    public void run() {

        class TestTrait implements TraitExample {

            public void test() {
                System.out.println(meow2().get());
            }
        }
        System.out.println("_____");
        TestTrait testTrait = new TestTrait();
        testTrait.test();

    }

    public static void readAllClasses() {
        var classes = getAllClasses();
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
            System.out.println("TRAIT DATA ADDED TO " + clazz.getName());
            TraitClassVisitor traitClassVisitor = new TraitClassVisitor(clazz);
            traitClassVisitor.init();
            traitClassVisitor.complete();
        }
    }


    // New method to recursively find all class files
    private static void findClasses(File directory, String packageName, List < Class < ?>>classes){
        String[] allFiles = directory.list();
        if (allFiles != null) {
            for (String fileName : allFiles) {
                File file = new File(directory, fileName);
                if (file.isDirectory()) {
                    findClasses(file, packageName + '.' + fileName, classes);
                } else if (fileName.endsWith(".class")) {
                    String className = packageName + '.' + fileName.substring(0, fileName.length() - 6);
                    try {
                        classes.add(Class.forName(className));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    // Call the new method from within the existing method
    private static List<Class<?>> getAllClasses () {
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
            findClasses(directory, packageName, classes);  // Call the new method here

        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }
}