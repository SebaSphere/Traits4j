package net.terradevelopment.traits4j;

import net.terradevelopment.traits4j.annotations.Trait;
import net.terradevelopment.traits4j.clazz.TraitImplementationUtil;
import net.terradevelopment.traits4j.data.Var;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.*;
import java.net.URL;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

public class PreMain {

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("MEOW! premain");
        var classes = PreMain.getAllClasses();
        for (Class<?> originalClazz : classes) {
            if (originalClazz.isAnnotationPresent(Trait.class)) {
                System.out.println("MEOW! trait");

                try {
                    var modifiedClassBytes = modifyClassBytes(getClassBytes(originalClazz), originalClazz);
                    inst.redefineClasses(new ClassDefinition(originalClazz, modifiedClassBytes));
                } catch (IOException | ClassNotFoundException | UnmodifiableClassException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }

    private static byte[] getClassBytes(Class<?> clazz) throws IOException {
        String classPath = clazz.getName().replace('.', '/') + ".class";
        try (InputStream inputStream = clazz.getClassLoader().getResourceAsStream(classPath)) {
            if (inputStream == null) {
                throw new IOException("Class not found as resource: " + classPath);
            }
            return inputStream.readAllBytes();
        }
    }


    private static byte[] modifyClassBytes(byte[] classfileBuffer, Class<?> originalClazz) {
        ClassReader cr = new ClassReader(classfileBuffer);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);

        TraitImplementationUtil.modifyClassNode(cn);

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        cn.accept(cw);
        return cw.toByteArray();
    }

    // New method to recursively find all class files
    private static void findClasses(File directory, String packageName, List< Class < ?>> classes){
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
    public static List<Class<?>> getAllClasses () {
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
