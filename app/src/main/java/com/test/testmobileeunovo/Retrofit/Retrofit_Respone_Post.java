package com.test.testmobileeunovo.Retrofit;

public class Retrofit_Respone_Post <T> {
    private T value;

    public Retrofit_Respone_Post(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
