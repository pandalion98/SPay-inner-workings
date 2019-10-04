/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.security.SecureRandom
 */
package org.bouncycastle.crypto.paddings;

import java.security.SecureRandom;

public interface BlockCipherPadding {
    public int addPadding(byte[] var1, int var2);

    public String getPaddingName();

    public void init(SecureRandom var1);

    public int padCount(byte[] var1);
}

