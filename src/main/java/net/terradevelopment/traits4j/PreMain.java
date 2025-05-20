package net.terradevelopment.traits4j;

import java.lang.instrument.Instrumentation;

public class PreMain {

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("MEOW! permain");
        Main.readAllClasses();
    }

}
