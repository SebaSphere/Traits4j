package net.terradevelopment.traits4j;

import net.terradevelopment.traits4j.annotations.Trait;
import net.terradevelopment.traits4j.clazz.TraitClassVisitor;
import net.terradevelopment.traits4j.test2.TraitExample;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("MAIN");
        new Main().run();

    }

    public int test = 2;

    public void run() {

        class TestTrait implements TraitExample {

            public void test() {
                System.out.println(meow2().get());
            }
        }
        System.out.println("_____");
        TestTrait testTrait = new TestTrait();
        System.out.println("The main object hash is " + testTrait.hashCode());
        testTrait.test();

        TestTrait testTraitTwo = new TestTrait();
        System.out.println("The second main object hash is " + testTraitTwo.hashCode());
        testTraitTwo.test();

    }

}