package net.terradevelopment.traits4j.data;

public class SuperClass<T> {

    private final T value;

    public SuperClass(T value) {
        this.value = value;
    }

    public T get() { return value; }
}
