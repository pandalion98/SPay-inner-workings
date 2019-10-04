/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto;

public interface SkippingCipher {
    public long getPosition();

    public long seekTo(long var1);

    public long skip(long var1);
}

