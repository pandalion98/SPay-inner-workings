/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.math.BigInteger
 */
package org.bouncycastle.crypto.encodings;

import java.math.BigInteger;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.params.RSAKeyParameters;

public class ISO9796d1Encoding
implements AsymmetricBlockCipher {
    private static final BigInteger SIX;
    private static final BigInteger SIXTEEN;
    private static byte[] inverse;
    private static byte[] shadows;
    private int bitSize;
    private AsymmetricBlockCipher engine;
    private boolean forEncryption;
    private BigInteger modulus;
    private int padBits = 0;

    static {
        SIXTEEN = BigInteger.valueOf((long)16L);
        SIX = BigInteger.valueOf((long)6L);
        shadows = new byte[]{14, 3, 5, 8, 9, 4, 2, 15, 0, 13, 11, 6, 7, 10, 12, 1};
        inverse = new byte[]{8, 15, 6, 1, 5, 2, 11, 12, 3, 4, 13, 10, 14, 9, 0, 7};
    }

    public ISO9796d1Encoding(AsymmetricBlockCipher asymmetricBlockCipher) {
        this.engine = asymmetricBlockCipher;
    }

    private static byte[] convertOutputDecryptOnly(BigInteger bigInteger) {
        byte[] arrby = bigInteger.toByteArray();
        if (arrby[0] == 0) {
            byte[] arrby2 = new byte[-1 + arrby.length];
            System.arraycopy((Object)arrby, (int)1, (Object)arrby2, (int)0, (int)arrby2.length);
            return arrby2;
        }
        return arrby;
    }

    /*
     * Enabled aggressive block sorting
     */
    private byte[] decodeBlock(byte[] arrby, int n2, int n3) {
        byte[] arrby2;
        int n4 = 0;
        byte[] arrby3 = this.engine.processBlock(arrby, n2, n3);
        int n5 = (13 + this.bitSize) / 16;
        BigInteger bigInteger = new BigInteger(1, arrby3);
        if (!bigInteger.mod(SIXTEEN).equals((Object)SIX)) {
            if (!this.modulus.subtract(bigInteger).mod(SIXTEEN).equals((Object)SIX)) {
                throw new InvalidCipherTextException("resulting integer iS or (modulus - iS) is not congruent to 6 mod 16");
            }
            bigInteger = this.modulus.subtract(bigInteger);
        }
        if ((15 & (arrby2 = ISO9796d1Encoding.convertOutputDecryptOnly(bigInteger))[-1 + arrby2.length]) != 6) {
            throw new InvalidCipherTextException("invalid forcing byte in block");
        }
        arrby2[-1 + arrby2.length] = (byte)((255 & arrby2[-1 + arrby2.length]) >>> 4 | inverse[(255 & arrby2[-2 + arrby2.length]) >> 4] << 4);
        arrby2[0] = (byte)(shadows[(255 & arrby2[1]) >>> 4] << 4 | shadows[15 & arrby2[1]]);
        int n6 = 0;
        boolean bl = false;
        int n7 = 1;
        for (int i2 = -1 + arrby2.length; i2 >= arrby2.length - n5 * 2; i2 -= 2) {
            int n8 = shadows[(255 & arrby2[i2]) >>> 4] << 4 | shadows[15 & arrby2[i2]];
            if ((255 & (n8 ^ arrby2[i2 - 1])) == 0) continue;
            if (bl) {
                throw new InvalidCipherTextException("invalid tsums in block");
            }
            n7 = 255 & (n8 ^ arrby2[i2 - 1]);
            n6 = i2 - 1;
            bl = true;
        }
        arrby2[n6] = 0;
        byte[] arrby4 = new byte[(arrby2.length - n6) / 2];
        do {
            if (n4 >= arrby4.length) {
                this.padBits = n7 - 1;
                return arrby4;
            }
            arrby4[n4] = arrby2[1 + (n6 + n4 * 2)];
            ++n4;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     */
    private byte[] encodeBlock(byte[] arrby, int n2, int n3) {
        int n4;
        byte[] arrby2 = new byte[(7 + this.bitSize) / 8];
        int n5 = 1 + this.padBits;
        int n6 = (13 + this.bitSize) / 16;
        for (int i2 = 0; i2 < n6; i2 += n3) {
            if (i2 > n6 - n3) {
                System.arraycopy((Object)arrby, (int)(n2 + n3 - (n6 - i2)), (Object)arrby2, (int)(arrby2.length - n6), (int)(n6 - i2));
                continue;
            }
            System.arraycopy((Object)arrby, (int)n2, (Object)arrby2, (int)(arrby2.length - (i2 + n3)), (int)n3);
        }
        for (int i3 = arrby2.length - n6 * 2; i3 != arrby2.length; i3 += 2) {
            byte by = arrby2[arrby2.length - n6 + i3 / 2];
            arrby2[i3] = (byte)(shadows[(by & 255) >>> 4] << 4 | shadows[by & 15]);
            arrby2[i3 + 1] = by;
        }
        int n7 = arrby2.length - n3 * 2;
        arrby2[n7] = (byte)(n5 ^ arrby2[n7]);
        arrby2[-1 + arrby2.length] = (byte)(6 | arrby2[-1 + arrby2.length] << 4);
        int n8 = 8 - (-1 + this.bitSize) % 8;
        if (n8 != 8) {
            arrby2[0] = (byte)(arrby2[0] & 255 >>> n8);
            arrby2[0] = (byte)(arrby2[0] | 128 >>> n8);
            n4 = 0;
            return this.engine.processBlock(arrby2, n4, arrby2.length - n4);
        }
        arrby2[0] = 0;
        arrby2[1] = (byte)(128 | arrby2[1]);
        n4 = 1;
        return this.engine.processBlock(arrby2, n4, arrby2.length - n4);
    }

    @Override
    public int getInputBlockSize() {
        int n2 = this.engine.getInputBlockSize();
        if (this.forEncryption) {
            n2 = (n2 + 1) / 2;
        }
        return n2;
    }

    @Override
    public int getOutputBlockSize() {
        int n2 = this.engine.getOutputBlockSize();
        if (this.forEncryption) {
            return n2;
        }
        return (n2 + 1) / 2;
    }

    public int getPadBits() {
        return this.padBits;
    }

    public AsymmetricBlockCipher getUnderlyingCipher() {
        return this.engine;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        RSAKeyParameters rSAKeyParameters = cipherParameters instanceof ParametersWithRandom ? (RSAKeyParameters)((ParametersWithRandom)cipherParameters).getParameters() : (RSAKeyParameters)cipherParameters;
        this.engine.init(bl, cipherParameters);
        this.modulus = rSAKeyParameters.getModulus();
        this.bitSize = this.modulus.bitLength();
        this.forEncryption = bl;
    }

    @Override
    public byte[] processBlock(byte[] arrby, int n2, int n3) {
        if (this.forEncryption) {
            return this.encodeBlock(arrby, n2, n3);
        }
        return this.decodeBlock(arrby, n2, n3);
    }

    public void setPadBits(int n2) {
        if (n2 > 7) {
            throw new IllegalArgumentException("padBits > 7");
        }
        this.padBits = n2;
    }
}

