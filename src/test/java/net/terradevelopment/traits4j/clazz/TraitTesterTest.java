package net.terradevelopment.traits4j.clazz;

import net.terradevelopment.traits4j.data.Var;
import net.terradevelopment.traits4j.test2.TraitExample;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TraitTesterTest {

    static class DataTrait implements TraitExample {

        public void test() {
            System.out.println("TEST");
        }
    }


    @BeforeAll
    static void before() {

    }


    @Test
    void testTraitEquals() {
        DataTrait traitOne = new DataTrait();
        DataTrait traitTwo = new DataTrait();

        System.out.println(traitOne.meow2().get());

        traitOne.meow2().set(34);



    }

}