package net.terradevelopment.traits4j;

import net.terradevelopment.traits4j.test2.TraitExample;

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
                return "The value is " + exampleTraitVariable().get();
            }

        }
        System.out.println("_____");
        TestTrait testTraitOne = new TestTrait();
        System.out.println("The testTraitOne object hash is " + testTraitOne.hashCode());
        System.out.println("Initial exampleTraitVariable set value is " + testTraitOne.exampleTraitVariable().get());
        testTraitOne.exampleTraitVariable().set(32);
        System.out.println("After exampleTraitVariable set value is " + testTraitOne.exampleTraitVariable().get());

        System.out.println("_____");
        TestTrait testTraitTwo = new TestTrait();
        System.out.println("The testTraitTwo object hash is " + testTraitTwo.hashCode());
        System.out.println("Initial exampleTraitVariable set value is " + testTraitTwo.exampleTraitVariable().get());

        System.out.println("_____");
        System.out.println("testTraitOne exampleTraitVariable still has the value of " + testTraitOne.exampleTraitVariable().get());


        System.out.println("_____");
        System.out.println("testTraitOne exampleOneTraitVariable is " + testTraitOne.exampleOneTraitVariable().get());
        System.out.println("testTraitTwo exampleOneTraitVariable is " + testTraitTwo.exampleOneTraitVariable().get());
        testTraitOne.exampleOneTraitVariable().set(94);
        System.out.println("exampleOneTraitVariable set to 94");
        System.out.println("testTraitOne exampleOneTraitVariable is " + testTraitOne.exampleOneTraitVariable().get());
        System.out.println("testTraitTwo exampleOneTraitVariable is " + testTraitTwo.exampleOneTraitVariable().get());
    }

}