/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.pqc.crypto;

import org.bouncycastle.crypto.CipherParameters;

public interface MessageEncryptor {
    public void init(boolean var1, CipherParameters var2);

    public byte[] messageDecrypt(byte[] var1);

    public byte[] messageEncrypt(byte[] var1);
}

