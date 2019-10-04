/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.String
 */
package org.bouncycastle.crypto.generators;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class PKCS5S1ParametersGenerator
extends PBEParametersGenerator {
    private Digest digest;

    public PKCS5S1ParametersGenerator(Digest digest) {
        this.digest = digest;
    }

    private byte[] generateDerivedKey() {
        byte[] arrby = new byte[this.digest.getDigestSize()];
        this.digest.update(this.password, 0, this.password.length);
        this.digest.update(this.salt, 0, this.salt.length);
        this.digest.doFinal(arrby, 0);
        for (int i2 = 1; i2 < this.iterationCount; ++i2) {
            this.digest.update(arrby, 0, arrby.length);
            this.digest.doFinal(arrby, 0);
        }
        return arrby;
    }

    @Override
    public CipherParameters generateDerivedMacParameters(int n2) {
        return this.generateDerivedParameters(n2);
    }

    @Override
    public CipherParameters generateDerivedParameters(int n2) {
        int n3 = n2 / 8;
        if (n3 > this.digest.getDigestSize()) {
            throw new IllegalArgumentException("Can't generate a derived key " + n3 + " bytes long.");
        }
        return new KeyParameter(this.generateDerivedKey(), 0, n3);
    }

    @Override
    public CipherParameters generateDerivedParameters(int n2, int n3) {
        int n4 = n2 / 8;
        int n5 = n3 / 8;
        if (n4 + n5 > this.digest.getDigestSize()) {
            throw new IllegalArgumentException("Can't generate a derived key " + (n4 + n5) + " bytes long.");
        }
        byte[] arrby = this.generateDerivedKey();
        return new ParametersWithIV(new KeyParameter(arrby, 0, n4), arrby, n4, n5);
    }
}

