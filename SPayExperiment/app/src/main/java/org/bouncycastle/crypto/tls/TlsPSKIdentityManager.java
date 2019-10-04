/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto.tls;

public interface TlsPSKIdentityManager {
    public byte[] getHint();

    public byte[] getPSK(byte[] var1);
}

