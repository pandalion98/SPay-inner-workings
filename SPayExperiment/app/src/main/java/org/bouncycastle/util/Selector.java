/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Cloneable
 *  java.lang.Object
 */
package org.bouncycastle.util;

public interface Selector<T>
extends Cloneable {
    public Object clone();

    public boolean match(T var1);
}

