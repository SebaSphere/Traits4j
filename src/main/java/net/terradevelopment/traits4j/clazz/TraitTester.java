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




    // class hash to variable map
    public static HashMap<Integer, ArrayList<VariableData<?>>> variables = new HashMap<>();

    // TODO: this is o(n), ideally we want log(n) for fetching existing variables of an object
    public static Var<?> getOrCreateTraitVariable(Var<?> var, Object instance, String name) {
        int hash = instance.hashCode();
        var existingObjectVariables = variables.get(hash);

        if (existingObjectVariables == null) {
            // Instance has no variables yet, so create list and add cloned variable
            try {
                Var<?> varToSend;
                if (var != null) {
                    varToSend = var.clone();
                } else {
                    varToSend = new Var<>(null);
                }
                VariableData<?> variableData = new VariableData<>(name.hashCode(), varToSend);
                ArrayList<VariableData<?>> list = new ArrayList<>();
                list.add(variableData);
                variables.put(hash, list);
                return varToSend;
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException("Failed to clone Var", e);
            }
        } else {
            // Instance exists, check if variable already present
            for (VariableData<?> variableData : existingObjectVariables) {
                if (variableData.getHashCode() == name.hashCode()) {
                    return variableData.getVar(); // Return existing var
                }
            }

            // Not found, so clone and add new one
            try {
                Var<?> clone = var.clone();
                VariableData<?> variableData = new VariableData<>(name.hashCode(), clone);
                existingObjectVariables.add(variableData);
                return clone;
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException("Failed to clone Var", e);
            }
        }
    }

}
