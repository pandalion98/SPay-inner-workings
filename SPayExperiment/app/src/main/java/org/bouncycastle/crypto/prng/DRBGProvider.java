/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto.prng;

import org.bouncycastle.crypto.prng.EntropySource;
import org.bouncycastle.crypto.prng.drbg.SP80090DRBG;

interface DRBGProvider {
    public SP80090DRBG get(EntropySource var1);
}

