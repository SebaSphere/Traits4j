package net.terradevelopment.traits4j.test;

import net.terradevelopment.traits4j.annotations.Trait;
import net.terradevelopment.traits4j.data.SuperClass;
import net.terradevelopment.traits4j.data.Var;

// extends acts as scala's "with"
@Trait
public interface TraitExample extends TraitExampleOne, TraitExampleTwo {

    @Override
    default void meow() {
        // should complain if you don't call super
        TraitExampleOne.super.meow();
    }

    // should automagically apply at runtime
    SuperClass<ExampleClass> example = new SuperClass<>(new ExampleClass());

    // takes in a variable
    Var<Integer> meow = new Var<>(2);

    default void changeNumber() {
        meow.set(4);
        // should say 4
        System.out.println(meow.get());
        // prints a method in the superclass
        example.get().print();
    }

}
