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
    default Var<Integer> meow2() {
        return new Var<>(3);
    }

    default int changeNumber(int num) {
        meow2().set(num);

        return meow2().get();
    }

}
