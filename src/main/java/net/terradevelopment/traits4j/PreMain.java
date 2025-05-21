package net.terradevelopment.traits4j;

import net.terradevelopment.traits4j.annotations.Trait;
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

        for (MethodNode methodNode : cn.methods) {
            if (("()" + Var.class.descriptorString()).equals(methodNode.desc)) {
                methodNode.access &= ~Opcodes.ACC_STATIC;

                // methodNode.instructions.clear();

                Type methodType = Type.getMethodType(methodNode.desc);
                Type returnType = methodType.getReturnType();  // This is what we want

                System.out.println("REEFR");
                System.out.println(returnType);


                InsnList insns = new InsnList();

                // new Var(3)
                // TODO: should be fixed to take in the previous loaded "Opcodes.ARETURN" value instead of "new Var(3)"
                insns.add(new TypeInsnNode(Opcodes.NEW, "net/terradevelopment/traits4j/data/Var"));
                insns.add(new InsnNode(Opcodes.DUP));
                insns.add(new InsnNode(Opcodes.ICONST_3)); // Push int 3
                insns.add(new MethodInsnNode(
                        Opcodes.INVOKESTATIC,
                        "java/lang/Integer",
                        "valueOf",
                        "(I)Ljava/lang/Integer;",
                        false
                ));
                insns.add(new MethodInsnNode(
                        Opcodes.INVOKESPECIAL,
                        "net/terradevelopment/traits4j/data/Var",
                        "<init>",
                        "(Ljava/lang/Object;)V",
                        false
                ));

                // new Object()
                insns.add(new TypeInsnNode(Opcodes.NEW, "java/lang/Object"));
                insns.add(new InsnNode(Opcodes.DUP));
                insns.add(new MethodInsnNode(
                        Opcodes.INVOKESPECIAL,
                        "java/lang/Object",
                        "<init>",
                        "()V",
                        false
                ));

                // ldc "test"
                insns.add(new LdcInsnNode("test22123"));

                // call TraitTester.getOrCreateTraitVariable(...)
                insns.add(new MethodInsnNode(
                        Opcodes.INVOKESTATIC,
                        "net/terradevelopment/traits4j/clazz/TraitTester",
                        "getOrCreateTraitVariable",
                        "(Lnet/terradevelopment/traits4j/data/Var;Ljava/lang/Object;Ljava/lang/String;)Lnet/terradevelopment/traits4j/data/Var;",
                        false
                ));
                // return the var
                insns.add(new InsnNode(Opcodes.ARETURN));

                methodNode.instructions.insert(insns);
            }

        }

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
