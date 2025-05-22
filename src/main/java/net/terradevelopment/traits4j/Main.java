package net.terradevelopment.traits4j;

import net.terradevelopment.traits4j.annotations.Trait;
import net.terradevelopment.traits4j.clazz.TraitTester;
import net.terradevelopment.traits4j.data.Var;
import net.terradevelopment.traits4j.test2.TraitExample;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // TraitTester.getOrCreateTraitVariable(new Var(new Object()), new Object(), "test");

        System.out.println("MAIN");
        new Main().run();

    }

    public int test = 2;

    public void run() {

        class TestTrait implements TraitExample {

            public String test() {
                return "The value is " + meow2().get();
            }

        }
        System.out.println("_____");
        TestTrait testTraitOne = new TestTrait();
        System.out.println("The testTraitOne object hash is " + testTraitOne.hashCode());
        System.out.println("Initial set value is " + testTraitOne.meow2().get());
        testTraitOne.meow2().set(32);
        System.out.println("After set value is " + testTraitOne.meow2().get());

        System.out.println("_____");
        TestTrait testTraitTwo = new TestTrait();
        System.out.println("The testTraitTwo object hash is " + testTraitTwo.hashCode());
        System.out.println("Initial set value is " + testTraitTwo.meow2().get());

        System.out.println("_____");
        System.out.println("testTraitOne still has the value of " + testTraitOne.meow2().get());

    }

}