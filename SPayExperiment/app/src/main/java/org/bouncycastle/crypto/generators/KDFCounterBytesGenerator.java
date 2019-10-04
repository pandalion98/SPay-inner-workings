/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.math.BigInteger
 */
package org.bouncycastle.crypto.generators;

import java.math.BigInteger;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.DerivationParameters;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.MacDerivationFunction;
import org.bouncycastle.crypto.params.KDFCounterParameters;
import org.bouncycastle.crypto.params.KeyParameter;

public class KDFCounterBytesGenerator
implements MacDerivationFunction {
    private static final BigInteger INTEGER_MAX = BigInteger.valueOf((long)Integer.MAX_VALUE);
    private static final BigInteger TWO = BigInteger.valueOf((long)2L);
    private byte[] fixedInputDataCtrPrefix;
    private byte[] fixedInputData_afterCtr;
    private int generatedBytes;
    private final int h;
    private byte[] ios;
    private byte[] k;
    private int maxSizeExcl;
    private final Mac prf;

    public KDFCounterBytesGenerator(Mac mac) {
        this.prf = mac;
        this.h = mac.getMacSize();
        this.k = new byte[this.h];
    }

    private void generateNext() {
        int n2 = 1 + this.generatedBytes / this.h;
        switch (this.ios.length) {
            default: {
                throw new IllegalStateException("Unsupported size of counter i");
            }
            case 4: {
                this.ios[0] = (byte)(n2 >>> 24);
            }
            case 3: {
                this.ios[-3 + this.ios.length] = (byte)(n2 >>> 16);
            }
            case 2: {
                this.ios[-2 + this.ios.length] = (byte)(n2 >>> 8);
            }
            case 1: 
        }
        this.ios[-1 + this.ios.length] = (byte)n2;
        this.prf.update(this.fixedInputDataCtrPrefix, 0, this.fixedInputDataCtrPrefix.length);
        this.prf.update(this.ios, 0, this.ios.length);
        this.prf.update(this.fixedInputData_afterCtr, 0, this.fixedInputData_afterCtr.length);
        this.prf.doFinal(this.k, 0);
    }

    @Override
    public int generateBytes(byte[] arrby, int n2, int n3) {
        int n4 = n3 + this.generatedBytes;
        if (n4 < 0 || n4 >= this.maxSizeExcl) {
            throw new DataLengthException("Current KDFCTR may only be used for " + this.maxSizeExcl + " bytes");
        }
        if (this.generatedBytes % this.h == 0) {
            this.generateNext();
        }
        int n5 = this.generatedBytes % this.h;
        int n6 = Math.min((int)(this.h - this.generatedBytes % this.h), (int)n3);
        System.arraycopy((Object)this.k, (int)n5, (Object)arrby, (int)n2, (int)n6);
        this.generatedBytes = n6 + this.generatedBytes;
        int n7 = n3 - n6;
        int n8 = n6 + n2;
        while (n7 > 0) {
            this.generateNext();
            int n9 = Math.min((int)this.h, (int)n7);
            System.arraycopy((Object)this.k, (int)0, (Object)arrby, (int)n8, (int)n9);
            this.generatedBytes = n9 + this.generatedBytes;
            n7 -= n9;
            n8 += n9;
        }
        return n3;
    }

    @Override
    public Mac getMac() {
        return this.prf;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void init(DerivationParameters derivationParameters) {
        if (!(derivationParameters instanceof KDFCounterParameters)) {
            throw new IllegalArgumentException("Wrong type of arguments given");
        }
        KDFCounterParameters kDFCounterParameters = (KDFCounterParameters)derivationParameters;
        this.prf.init(new KeyParameter(kDFCounterParameters.getKI()));
        this.fixedInputDataCtrPrefix = kDFCounterParameters.getFixedInputDataCounterPrefix();
        this.fixedInputData_afterCtr = kDFCounterParameters.getFixedInputDataCounterSuffix();
        int n2 = kDFCounterParameters.getR();
        this.ios = new byte[n2 / 8];
        BigInteger bigInteger = TWO.pow(n2).multiply(BigInteger.valueOf((long)this.h));
        int n3 = bigInteger.compareTo(INTEGER_MAX) == 1 ? Integer.MAX_VALUE : bigInteger.intValue();
        this.maxSizeExcl = n3;
        this.generatedBytes = 0;
    }
}

