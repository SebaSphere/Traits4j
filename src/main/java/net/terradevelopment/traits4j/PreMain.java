package net.terradevelopment.traits4j;

import net.terradevelopment.traits4j.annotations.Trait;
import net.terradevelopment.traits4j.data.Var;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.*;
import java.security.ProtectionDomain;

public class PreMain {

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("MEOW! permain");
        // Main.readAllClasses();
        var classes = Main.getAllClasses();
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Trait.class)) {
                System.out.println("MEOW! trait");

                try {
                    var modifiedClassBytes = modifyClassBytes(getClassBytes(clazz));
                    inst.redefineClasses(new ClassDefinition(clazz, modifiedClassBytes));
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

    private static byte[] modifyClassBytes(byte[] classfileBuffer) {
        ClassReader cr = new ClassReader(classfileBuffer);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);

        // Do your same method manipulation here:
        for (MethodNode method : cn.methods) {
            if (("()" + Var.class.descriptorString()).equals(method.desc)) {
                method.access &= ~Opcodes.ACC_STATIC;
                InsnList beginList = new InsnList();
                beginList.add(new LdcInsnNode(method.name));
                beginList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                beginList.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                        "net/terradevelopment/traits4j/clazz/TraitTester",
                        "printSuperCaller", "(Ljava/lang/Object;)V", false));
                method.instructions.insert(beginList);
            }
        }

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        cn.accept(cw);
        return cw.toByteArray();
    }
}
