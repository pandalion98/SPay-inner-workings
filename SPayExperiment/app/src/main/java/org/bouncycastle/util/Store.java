/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.Collection
 */
package org.bouncycastle.util;

import java.util.Collection;
import org.bouncycastle.util.Selector;

public interface Store<T> {
    public Collection<T> getMatches(Selector<T> var1);
}

