package com.samsung.android.multidisplay.common;

import java.util.ArrayList;
import java.util.Collection;

public class FallbackArrayList<E> extends ArrayList<E> {
    public static final boolean FALLBACK_ENABLED = true;
    public static int mFallbackIndex = 0;

    public FallbackArrayList(Collection<? extends E> collection) {
        super(collection);
    }

    public FallbackArrayList(int capacity) {
        super(capacity);
    }

    public void add(int index, E object) {
        try {
            super.add(index, object);
        } catch (IndexOutOfBoundsException e) {
            super.add(mFallbackIndex, object);
        }
    }

    public boolean addAll(int index, Collection<? extends E> collection) {
        try {
            return super.addAll(index, collection);
        } catch (IndexOutOfBoundsException e) {
            return super.addAll(mFallbackIndex, collection);
        }
    }

    public E get(int index) {
        try {
            return super.get(index);
        } catch (IndexOutOfBoundsException e) {
            return super.get(mFallbackIndex);
        }
    }

    public E remove(int index) {
        try {
            return super.remove(index);
        } catch (IndexOutOfBoundsException e) {
            return super.remove(mFallbackIndex);
        }
    }

    public E set(int index, E object) {
        try {
            return super.set(index, object);
        } catch (IndexOutOfBoundsException e) {
            return super.set(mFallbackIndex, object);
        }
    }

    public void setFallbackIndex(int index) {
        mFallbackIndex = index;
    }
}
