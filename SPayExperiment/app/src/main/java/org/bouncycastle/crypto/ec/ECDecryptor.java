/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  org.bouncycastle.math.ec.ECPoint
 */
package org.bouncycastle.crypto.ec;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.ec.ECPair;
import org.bouncycastle.math.ec.ECPoint;

public interface ECDecryptor {
    public ECPoint decrypt(ECPair var1);

    public void init(CipherParameters var1);
}

