package net.terradevelopment.traits4j.clazz;

import net.terradevelopment.traits4j.data.Var;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

public class TraitImplementationUtil {

    public static void modifyClassNode(ClassNode cn) {
        for (MethodNode methodNode : cn.methods) {
            if (("()" + Var.class.descriptorString()).equals(methodNode.desc)) {
                methodNode.access &= ~Opcodes.ACC_STATIC;

                // methodNode.instructions.clear();

                Type methodType = Type.getMethodType(methodNode.desc);
                Type returnType = methodType.getReturnType();  // This is what we want

                System.out.println("REEFR");
                System.out.println(returnType);


                InsnList beforeReturnInsert = new InsnList();

                // new Var(3)
                // it inserts what should be in the return value just before returning so it should already have the old variable in memory

                // inserts instance of object
                beforeReturnInsert.add(new VarInsnNode(Opcodes.ALOAD, 0));

                // inputs variable name
                beforeReturnInsert.add(new LdcInsnNode(methodNode.name));

                // call TraitTester.getOrCreateTraitVariable(...)
                beforeReturnInsert.add(new MethodInsnNode(
                        Opcodes.INVOKESTATIC,
                        "net/terradevelopment/traits4j/clazz/TraitTester",
                        "getOrCreateTraitVariable",
                        "(Lnet/terradevelopment/traits4j/data/Var;Ljava/lang/Object;Ljava/lang/String;)Lnet/terradevelopment/traits4j/data/Var;",
                        false
                ));
                // return the var
                beforeReturnInsert.add(new InsnNode(Opcodes.ARETURN));

                for (var instruction : methodNode.instructions) {
                    if (instruction.getOpcode() == Opcodes.ARETURN) {
                        // insert just before this instruction
                        methodNode.instructions.insertBefore(instruction, beforeReturnInsert);
                    }
                }

                // methodNode.instructions.insert(beforeReturnInsert);
            }

        }
    }

}
