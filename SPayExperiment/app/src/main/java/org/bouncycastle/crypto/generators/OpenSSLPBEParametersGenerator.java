/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.System
 */
package org.bouncycastle.crypto.generators;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class OpenSSLPBEParametersGenerator
extends PBEParametersGenerator {
    private Digest digest = new MD5Digest();

    /*
     * Enabled aggressive block sorting
     */
    private byte[] generateDerivedKey(int n2) {
        byte[] arrby = new byte[this.digest.getDigestSize()];
        byte[] arrby2 = new byte[n2];
        int n3 = 0;
        do {
            this.digest.update(this.password, 0, this.password.length);
            this.digest.update(this.salt, 0, this.salt.length);
            this.digest.doFinal(arrby, 0);
            int n4 = n2 > arrby.length ? arrby.length : n2;
            System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)n3, (int)n4);
            n3 += n4;
            if ((n2 -= n4) == 0) {
                return arrby2;
            }
            this.digest.reset();
            this.digest.update(arrby, 0, arrby.length);
        } while (true);
    }

    @Override
    public CipherParameters generateDerivedMacParameters(int n2) {
        return this.generateDerivedParameters(n2);
    }

    @Override
    public CipherParameters generateDerivedParameters(int n2) {
        int n3 = n2 / 8;
        return new KeyParameter(this.generateDerivedKey(n3), 0, n3);
    }

    @Override
    public CipherParameters generateDerivedParameters(int n2, int n3) {
        int n4 = n2 / 8;
        int n5 = n3 / 8;
        byte[] arrby = this.generateDerivedKey(n4 + n5);
        return new ParametersWithIV(new KeyParameter(arrby, 0, n4), arrby, n4, n5);
    }

    public void init(byte[] arrby, byte[] arrby2) {
        super.init(arrby, arrby2, 1);
    }
}

