package net.terradevelopment.traits4j.test;

import net.terradevelopment.traits4j.annotations.Trait;

@Trait
public interface TraitExampleOne {

    default void meow() {
        System.out.println("meow1");
    }

}
