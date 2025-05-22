package net.terradevelopment.traits4j.clazz;

import net.terradevelopment.traits4j.Main;
import net.terradevelopment.traits4j.data.Var;
import net.terradevelopment.traits4j.test2.TraitExample;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TraitTester {

    public static void printSuperCaller(Object object) {
        System.out.println("THE OBJECT OF " + object.getClass() + " HASH IS: " + object.hashCode());
    }


    public static class VariableData<T> {
        private final int hashCode;
        private final Var<T> var;

        // takes in string and gets hash
        public VariableData(int hashCode, Var<T> var) {
            this.hashCode = hashCode;
            this.var = var;
        }

        public Var<T> getVar() {
            return var;
        }

        public int getHashCode() {
            // This method returns the hash code of the object
            return hashCode;
        }
    }

    public static HashMap<Integer, ArrayList<VariableData<?>>> variables = new HashMap<>();

    public static Var<?> getOrCreateTraitVariable(Var<?> var, Object instance, String name) {

        // convert varClazz to Var instance

        System.out.println(name);

        System.out.println("TE2ST!");
        return var;
    }

}
