/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto.tls;

public interface TlsPSKIdentity {
    public byte[] getPSK();

    public byte[] getPSKIdentity();

    public void notifyIdentityHint(byte[] var1);

    public void skipIdentityHint();
}

