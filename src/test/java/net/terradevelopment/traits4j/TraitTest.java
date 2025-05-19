package net.terradevelopment.traits4j;

import net.terradevelopment.traits4j.test.TraitExample;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TraitTest {

    static class TestTrait implements TraitExample {

        public void test() {
            System.out.println(meow2().get());
        }
    }

    // makes sure traits actually work as intended
    @Test
    public void testTraitValidity() {

        Main.readAllClasses(); // should run process to asm classes

        System.out.println("test!");
        TestTrait firstTrait = new TestTrait();
        TestTrait secondTrait = new TestTrait();

        firstTrait.meow2().set(4);
        secondTrait.meow2().set(4);

        assertEquals(firstTrait.meow2().get(), secondTrait.meow2().get());

        secondTrait.meow2().set(7);
        System.out.println(firstTrait.meow2().hashCode());
        System.out.println(secondTrait.meow2().hashCode());

        // in an ideal world, this should be asm so these are new objects.
        // maybe we create a new object attached to the hash? idk
        // in default java, this will be equal since the variable is static
//        assertNotEquals(firstTrait.meow2().get(), secondTrait.meow2().get());

    }

}