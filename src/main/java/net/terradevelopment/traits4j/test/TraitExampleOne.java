package net.terradevelopment.traits4j.test;

public interface TraitExampleOne {

    default void meow() {
        System.out.println("meow1");
    }

}
