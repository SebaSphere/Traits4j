package net.terradevelopment.traits4j.test;

public interface TraitExampleTwo {

    default void meow() {
        System.out.println("meow2");
    }
}
