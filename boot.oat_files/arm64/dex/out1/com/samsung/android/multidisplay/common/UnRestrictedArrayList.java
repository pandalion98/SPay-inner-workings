package com.samsung.android.multidisplay.common;

import java.util.ArrayList;
import java.util.Collection;

public class UnRestrictedArrayList<E> extends ArrayList<E> {
    public static final boolean UNRESTRICTED_ENABLED = true;

    public UnRestrictedArrayList(Collection<? extends E> collection) {
        super(collection);
    }

    public UnRestrictedArrayList(int capacity) {
        super(capacity);
    }

    public void add(int index, E object) {
        while (index >= super.size()) {
            super.add(super.size(), null);
        }
        super.set(index, object);
    }

    public E get(int index) {
        try {
            return super.get(index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public E set(int index, E object) {
        while (index >= super.size()) {
            super.add(super.size(), null);
        }
        return super.set(index, object);
    }
}
