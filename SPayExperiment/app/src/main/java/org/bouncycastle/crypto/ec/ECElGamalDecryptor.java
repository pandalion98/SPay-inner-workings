/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  org.bouncycastle.math.ec.ECPoint
 */
package org.bouncycastle.crypto.ec;

import java.math.BigInteger;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.ec.ECDecryptor;
import org.bouncycastle.crypto.ec.ECPair;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.math.ec.ECPoint;

public class ECElGamalDecryptor
implements ECDecryptor {
    private ECPrivateKeyParameters key;

    @Override
    public ECPoint decrypt(ECPair eCPair) {
        if (this.key == null) {
            throw new IllegalStateException("ECElGamalDecryptor not initialised");
        }
        ECPoint eCPoint = eCPair.getX().multiply(this.key.getD());
        return eCPair.getY().subtract(eCPoint).normalize();
    }

    @Override
    public void init(CipherParameters cipherParameters) {
        if (!(cipherParameters instanceof ECPrivateKeyParameters)) {
            throw new IllegalArgumentException("ECPrivateKeyParameters are required for decryption.");
        }
        this.key = (ECPrivateKeyParameters)cipherParameters;
    }
}

