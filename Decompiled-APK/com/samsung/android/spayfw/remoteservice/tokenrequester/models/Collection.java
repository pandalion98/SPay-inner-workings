package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

public class Collection<T> {
    T[] elements;
    String href;
    int limit;
    String next;
    String prev;
    int start;

    public T[] getElements() {
        return this.elements;
    }
}
