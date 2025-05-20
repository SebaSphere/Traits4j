package net.terradevelopment.traits4j.clazz;

import net.terradevelopment.traits4j.Main;
import net.terradevelopment.traits4j.data.Var;

import java.util.HashMap;
import java.util.List;

public class TraitTester {

    public static void printSuperCaller(Object object) {
        System.out.println("THE OBJECT OF " + object.getClass() + " HASH IS " + object.hashCode());
    }


    public static class VariableData<T> {
        private final long hashCode;
        private final Var<T> var;

        public VariableData(long hashCode, Var<T> var) {
            this.hashCode = hashCode;
            this.var = var;
        }
    }

    public static HashMap<Long, List<VariableData<?>>> variables = new HashMap<>();

    public static Var<?> getOrCreateTraitVariable(Object object) {
        return null;
    }

}
