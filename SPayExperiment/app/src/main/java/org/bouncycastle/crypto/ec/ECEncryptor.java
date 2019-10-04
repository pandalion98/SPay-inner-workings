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

public interface ECEncryptor {
    public ECPair encrypt(ECPoint var1);

    public void init(CipherParameters var1);
}

