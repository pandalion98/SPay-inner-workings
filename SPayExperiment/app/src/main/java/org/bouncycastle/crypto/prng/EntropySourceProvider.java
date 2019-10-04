/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto.prng;

import org.bouncycastle.crypto.prng.EntropySource;

public interface EntropySourceProvider {
    public EntropySource get(int var1);
}

