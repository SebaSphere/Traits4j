package net.terradevelopment.traits4j.data;

public class Var<T> {

    // TODO: maybe we could have some logic that can be set on the access modifier?

    private T value;

    public Var(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }

}
