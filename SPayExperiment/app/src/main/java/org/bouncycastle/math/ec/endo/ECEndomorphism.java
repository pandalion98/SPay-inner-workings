/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.math.ec.endo;

import org.bouncycastle.math.ec.ECPointMap;

public interface ECEndomorphism {
    public ECPointMap getPointMap();

    public boolean hasEfficientPointMap();
}

