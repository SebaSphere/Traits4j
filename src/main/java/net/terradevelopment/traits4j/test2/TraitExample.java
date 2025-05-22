package net.terradevelopment.traits4j.test2;

import net.terradevelopment.traits4j.annotations.Trait;
import net.terradevelopment.traits4j.data.SuperClass;
import net.terradevelopment.traits4j.data.Var;

// extends acts as scala's "with"
@Trait
public interface TraitExample extends TraitExampleOne, TraitExampleTwo {

    // should automagically apply at runtime
    SuperClass<ExampleClass> example = new SuperClass<>(new ExampleClass());

    // acts as a variable
    default Var<Integer> exampleTraitVariable() {
        return new Var<>(44);
    }

}
