/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.util;

import org.bouncycastle.util.Iterable;

public interface StringList
extends Iterable<String> {
    public boolean add(String var1);

    public String get(int var1);

    public int size();

    public String[] toStringArray();

    public String[] toStringArray(int var1, int var2);
}

