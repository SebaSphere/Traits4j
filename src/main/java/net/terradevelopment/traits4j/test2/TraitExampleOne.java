package net.terradevelopment.traits4j.test2;

import net.terradevelopment.traits4j.annotations.Trait;
import net.terradevelopment.traits4j.data.Var;

@Trait
public interface TraitExampleOne {

    default Var<Integer> exampleOneTraitVariable() {
        return new Var<>(23);
    }

}
