package net.terradevelopment.traits4j.clazz;

import net.terradevelopment.traits4j.data.Var;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.*;
import java.util.Iterator;

public class TraitClassVisitor {

    private final Class<?> sourceClazz;
    private ClassReader classReader;
    private ClassWriter classWriter;

    private ClassNode classNode;

    public TraitClassVisitor(Class<?> clazz) {
        this.sourceClazz = clazz;

        classNode = new ClassNode(Opcodes.ASM9);

        //These properties of the classNode must be set
        classNode.version = Opcodes.V21;
        classNode.access = Opcodes.ACC_PUBLIC;
        classNode.signature = "L" + sourceClazz.getTypeName().replace('.', '/') + ";";
        classNode.name = sourceClazz.getTypeName().replace('.', '/');

        // TODO: make it set a super object
        classNode.superName = "java/lang/Object";

        try {
            InputStream inputStream = sourceClazz.getResourceAsStream(sourceClazz.getSimpleName() + ".class");
            if (inputStream != null) {
                this.classReader = new ClassReader(inputStream);
                this.classReader.accept(this.classNode, 0);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void init() {
        if (classReader != null) {
            for (var field : classNode.fields) {
                if (Var.class.descriptorString().equals(field.desc)) {

                }
            }
            for (var method : classNode.methods) {

                if (("()" + Var.class.descriptorString()).equals(method.desc)) {
                    method.access &= ~Opcodes.ACC_STATIC; // this line removes the static access
                    // run hello before a field is accessed
                    InsnList beginList = new InsnList();
                    System.out.println("INSERT");
                    beginList.add(new LdcInsnNode(method.name));

                    beginList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    beginList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/terradevelopment/traits4j/clazz/TraitTester", "printSuperCaller","(Ljava/lang/Object;)V", false));

                    method.instructions.insert(beginList);

                }
            }
        }
    }

    public void complete() {
        //We are done now. so dump the class
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(classWriter);

        try {
            File outDir = new File("out/" + sourceClazz.getPackage().getName().replace('.', '/'));
            outDir.mkdirs();
            DataOutputStream dout = new DataOutputStream(new FileOutputStream(new File(outDir, sourceClazz.getSimpleName() + ".class")));
            dout.write(classWriter.toByteArray());
            dout.flush();
            dout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static byte[] readBytesFromFile(File file) throws IOException {
        try (InputStream is = new FileInputStream(file)) {
            return is.readAllBytes();
        }
    }

}
