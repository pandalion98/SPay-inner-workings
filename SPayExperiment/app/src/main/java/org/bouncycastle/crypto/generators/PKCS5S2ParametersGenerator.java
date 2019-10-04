/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package org.bouncycastle.crypto.generators;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class PKCS5S2ParametersGenerator
extends PBEParametersGenerator {
    private Mac hMac;
    private byte[] state;

    public PKCS5S2ParametersGenerator() {
        this(new SHA1Digest());
    }

    public PKCS5S2ParametersGenerator(Digest digest) {
        this.hMac = new HMac(digest);
        this.state = new byte[this.hMac.getMacSize()];
    }

    private void F(byte[] arrby, int n2, byte[] arrby2, byte[] arrby3, int n3) {
        if (n2 == 0) {
            throw new IllegalArgumentException("iteration count must be at least 1.");
        }
        if (arrby != null) {
            this.hMac.update(arrby, 0, arrby.length);
        }
        this.hMac.update(arrby2, 0, arrby2.length);
        this.hMac.doFinal(this.state, 0);
        System.arraycopy((Object)this.state, (int)0, (Object)arrby3, (int)n3, (int)this.state.length);
        for (int i2 = 1; i2 < n2; ++i2) {
            this.hMac.update(this.state, 0, this.state.length);
            this.hMac.doFinal(this.state, 0);
            for (int i3 = 0; i3 != this.state.length; ++i3) {
                int n4 = n3 + i3;
                arrby3[n4] = (byte)(arrby3[n4] ^ this.state[i3]);
            }
        }
    }

    private byte[] generateDerivedKey(int n2) {
        int n3 = this.hMac.getMacSize();
        int n4 = (-1 + (n2 + n3)) / n3;
        byte[] arrby = new byte[4];
        byte[] arrby2 = new byte[n4 * n3];
        int n5 = 0;
        KeyParameter keyParameter = new KeyParameter(this.password);
        this.hMac.init(keyParameter);
        for (int i2 = 1; i2 <= n4; ++i2) {
            int n6 = 3;
            do {
                byte by;
                arrby[n6] = by = (byte)(1 + arrby[n6]);
                if (by != 0) break;
                --n6;
            } while (true);
            this.F(this.salt, this.iterationCount, arrby, arrby2, n5);
            n5 += n3;
        }
        return arrby2;
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
}

