package net.terradevelopment.traits4j;

import net.terradevelopment.traits4j.test2.TraitExample;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TraitTest {

    static class TestTrait implements TraitExample {

        public void test() {
            System.out.println(meow2().get());
        }
    }

    // I should probably make a test to make sure traits are actually loaded
    // makes sure traits actually work as intended
    @Test
    public void testTraitValidity() {
//        System.out.println("test!");
//        TestTrait firstTrait = new TestTrait();
//        TestTrait secondTrait = new TestTrait();
//
//        System.out.println(firstTrait.meow2());
//
//        assertEquals(firstTrait.meow2().get(), secondTrait.meow2().get());
//
//        secondTrait.meow2().set(7);
//        System.out.println(firstTrait.meow2().hashCode());
//        System.out.println(secondTrait.meow2().hashCode());

    }
}