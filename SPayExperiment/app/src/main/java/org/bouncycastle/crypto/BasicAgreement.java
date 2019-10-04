/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 */
package org.bouncycastle.crypto;

import java.math.BigInteger;
import org.bouncycastle.crypto.CipherParameters;

public interface BasicAgreement {
    public BigInteger calculateAgreement(CipherParameters var1);

    public int getFieldSize();

    public void init(CipherParameters var1);
}

