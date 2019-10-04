/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalStateException
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.math.BigInteger
 *  java.security.SecureRandom
 *  org.bouncycastle.util.Arrays
 *  org.bouncycastle.util.BigIntegers
 */
package org.bouncycastle.crypto.signers;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.signers.DSAKCalculator;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.BigIntegers;

public class HMacDSAKCalculator
implements DSAKCalculator {
    private static final BigInteger ZERO = BigInteger.valueOf((long)0L);
    private final byte[] K;
    private final byte[] V;
    private final HMac hMac;
    private BigInteger n;

    public HMacDSAKCalculator(Digest digest) {
        this.hMac = new HMac(digest);
        this.V = new byte[this.hMac.getMacSize()];
        this.K = new byte[this.hMac.getMacSize()];
    }

    private BigInteger bitsToInt(byte[] arrby) {
        BigInteger bigInteger = new BigInteger(1, arrby);
        if (8 * arrby.length > this.n.bitLength()) {
            bigInteger = bigInteger.shiftRight(8 * arrby.length - this.n.bitLength());
        }
        return bigInteger;
    }

    @Override
    public void init(BigInteger bigInteger, BigInteger bigInteger2, byte[] arrby) {
        this.n = bigInteger;
        Arrays.fill((byte[])this.V, (byte)1);
        Arrays.fill((byte[])this.K, (byte)0);
        byte[] arrby2 = new byte[(7 + bigInteger.bitLength()) / 8];
        byte[] arrby3 = BigIntegers.asUnsignedByteArray((BigInteger)bigInteger2);
        System.arraycopy((Object)arrby3, (int)0, (Object)arrby2, (int)(arrby2.length - arrby3.length), (int)arrby3.length);
        byte[] arrby4 = new byte[(7 + bigInteger.bitLength()) / 8];
        BigInteger bigInteger3 = this.bitsToInt(arrby);
        if (bigInteger3.compareTo(bigInteger) >= 0) {
            bigInteger3 = bigInteger3.subtract(bigInteger);
        }
        byte[] arrby5 = BigIntegers.asUnsignedByteArray((BigInteger)bigInteger3);
        System.arraycopy((Object)arrby5, (int)0, (Object)arrby4, (int)(arrby4.length - arrby5.length), (int)arrby5.length);
        this.hMac.init(new KeyParameter(this.K));
        this.hMac.update(this.V, 0, this.V.length);
        this.hMac.update((byte)0);
        this.hMac.update(arrby2, 0, arrby2.length);
        this.hMac.update(arrby4, 0, arrby4.length);
        this.hMac.doFinal(this.K, 0);
        this.hMac.init(new KeyParameter(this.K));
        this.hMac.update(this.V, 0, this.V.length);
        this.hMac.doFinal(this.V, 0);
        this.hMac.update(this.V, 0, this.V.length);
        this.hMac.update((byte)1);
        this.hMac.update(arrby2, 0, arrby2.length);
        this.hMac.update(arrby4, 0, arrby4.length);
        this.hMac.doFinal(this.K, 0);
        this.hMac.init(new KeyParameter(this.K));
        this.hMac.update(this.V, 0, this.V.length);
        this.hMac.doFinal(this.V, 0);
    }

    @Override
    public void init(BigInteger bigInteger, SecureRandom secureRandom) {
        throw new IllegalStateException("Operation not supported");
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @Override
    public BigInteger nextK() {
        byte[] arrby = new byte[(7 + this.n.bitLength()) / 8];
        do {
            int n2;
            for (int i2 = 0; i2 < arrby.length; i2 += n2) {
                this.hMac.update(this.V, 0, this.V.length);
                this.hMac.doFinal(this.V, 0);
                n2 = Math.min((int)(arrby.length - i2), (int)this.V.length);
                System.arraycopy((Object)this.V, (int)0, (Object)arrby, (int)i2, (int)n2);
            }
            BigInteger bigInteger = this.bitsToInt(arrby);
            if (bigInteger.compareTo(ZERO) > 0 && bigInteger.compareTo(this.n) < 0) {
                return bigInteger;
            }
            this.hMac.update(this.V, 0, this.V.length);
            this.hMac.update((byte)0);
            this.hMac.doFinal(this.K, 0);
            this.hMac.init(new KeyParameter(this.K));
            this.hMac.update(this.V, 0, this.V.length);
            this.hMac.doFinal(this.V, 0);
        } while (true);
    }
}

