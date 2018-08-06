package ar.edu.itba.ss;

public class Property<T> {
    T property;

    public Property(T property){
        this.property = property;
    }

    public T getProperty() {
        return property;
    }
}
