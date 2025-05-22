package net.terradevelopment.traits4j.data;

import java.io.Closeable;

public class Var<T> implements Cloneable {


    // we clone it because if not, it'd be trying to add a reference to the one static variable
    @Override
    public Var<?> clone() throws CloneNotSupportedException {
        return (Var<?>) super.clone();
    }

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
