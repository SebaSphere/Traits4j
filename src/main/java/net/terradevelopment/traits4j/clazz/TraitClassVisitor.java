package net.terradevelopment.traits4j.clazz;

import net.terradevelopment.traits4j.data.Var;
import net.terradevelopment.traits4j.test2.TraitExample;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.*;
import java.util.Arrays;

public class TraitClassVisitor {

    private Class<?> sourceClazz;
    private ClassReader classReader;

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

                    beginList.add(new LdcInsnNode(method.name));


                    System.out.println("INSERTED");

                    beginList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    beginList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/terradevelopment/traits4j/clazz/TraitTester", "printSuperCaller","(Ljava/lang/Object;)V", false));

                    method.instructions.insert(beginList);

                }
            }
        }
    }

    public void complete() {

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(classWriter);


        String outputPath = sourceClazz.getProtectionDomain().getCodeSource().getLocation().getPath();
        outputPath += sourceClazz.getName().replace('.', '/') + ".class";
        try {
            DataOutputStream dout = new DataOutputStream(new FileOutputStream(outputPath));
            dout.write(classWriter.toByteArray());
            dout.flush();
            dout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            System.out.println("TRYING");

            DynamicClassLoader classLoader = new DynamicClassLoader(classWriter.toByteArray());

            Class<?> clazz = classLoader.loadClass(classNode.name.replaceAll("/", "."));


            System.out.println("TIME");
            System.out.println(clazz);
            System.out.println("CALLED");


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

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

            return this.defineClass(name, classNodeBytes);

        }

    }




}
