/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  java.util.Hashtable
 *  org.bouncycastle.util.Arrays
 *  org.bouncycastle.util.BigIntegers
 *  org.bouncycastle.util.Integers
 */
package org.bouncycastle.crypto.signers;

import java.math.BigInteger;
import java.util.Hashtable;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Signer;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.BigIntegers;
import org.bouncycastle.util.Integers;

public class X931Signer
implements Signer {
    public static final int TRAILER_IMPLICIT = 188;
    public static final int TRAILER_RIPEMD128 = 13004;
    public static final int TRAILER_RIPEMD160 = 12748;
    public static final int TRAILER_SHA1 = 13260;
    public static final int TRAILER_SHA224 = 14540;
    public static final int TRAILER_SHA256 = 13516;
    public static final int TRAILER_SHA384 = 14028;
    public static final int TRAILER_SHA512 = 13772;
    public static final int TRAILER_WHIRLPOOL = 14284;
    private static Hashtable trailerMap = new Hashtable();
    private byte[] block;
    private AsymmetricBlockCipher cipher;
    private Digest digest;
    private RSAKeyParameters kParam;
    private int keyBits;
    private int trailer;

    static {
        trailerMap.put((Object)"RIPEMD128", (Object)Integers.valueOf((int)13004));
        trailerMap.put((Object)"RIPEMD160", (Object)Integers.valueOf((int)12748));
        trailerMap.put((Object)"SHA-1", (Object)Integers.valueOf((int)13260));
        trailerMap.put((Object)"SHA-224", (Object)Integers.valueOf((int)14540));
        trailerMap.put((Object)"SHA-256", (Object)Integers.valueOf((int)13516));
        trailerMap.put((Object)"SHA-384", (Object)Integers.valueOf((int)14028));
        trailerMap.put((Object)"SHA-512", (Object)Integers.valueOf((int)13772));
        trailerMap.put((Object)"Whirlpool", (Object)Integers.valueOf((int)14284));
    }

    public X931Signer(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest) {
        this(asymmetricBlockCipher, digest, false);
    }

    public X931Signer(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest, boolean bl) {
        this.cipher = asymmetricBlockCipher;
        this.digest = digest;
        if (bl) {
            this.trailer = 188;
            return;
        }
        Integer n2 = (Integer)trailerMap.get((Object)digest.getAlgorithmName());
        if (n2 != null) {
            this.trailer = n2;
            return;
        }
        throw new IllegalArgumentException("no valid trailer for digest");
    }

    private void clearBlock(byte[] arrby) {
        for (int i2 = 0; i2 != arrby.length; ++i2) {
            arrby[i2] = 0;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void createSignatureBlock() {
        int n2;
        int n3 = this.digest.getDigestSize();
        if (this.trailer == 188) {
            n2 = -1 + (this.block.length - n3);
            this.digest.doFinal(this.block, n2);
            this.block[-1 + this.block.length] = -68;
        } else {
            n2 = -2 + (this.block.length - n3);
            this.digest.doFinal(this.block, n2);
            this.block[-2 + this.block.length] = (byte)(this.trailer >>> 8);
            this.block[-1 + this.block.length] = (byte)this.trailer;
        }
        this.block[0] = 107;
        int n4 = n2 - 2;
        do {
            if (n4 == 0) {
                this.block[n2 - 1] = -70;
                return;
            }
            this.block[n4] = -69;
            --n4;
        } while (true);
    }

    @Override
    public byte[] generateSignature() {
        this.createSignatureBlock();
        BigInteger bigInteger = new BigInteger(1, this.cipher.processBlock(this.block, 0, this.block.length));
        BigInteger bigInteger2 = this.kParam.getModulus().subtract(bigInteger);
        this.clearBlock(this.block);
        this.kParam.getModulus().shiftRight(2);
        if (bigInteger.compareTo(bigInteger2) > 0) {
            return BigIntegers.asUnsignedByteArray((int)((7 + this.kParam.getModulus().bitLength()) / 8), (BigInteger)bigInteger2);
        }
        return BigIntegers.asUnsignedByteArray((int)((7 + this.kParam.getModulus().bitLength()) / 8), (BigInteger)bigInteger);
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        this.kParam = (RSAKeyParameters)cipherParameters;
        this.cipher.init(bl, this.kParam);
        this.keyBits = this.kParam.getModulus().bitLength();
        this.block = new byte[(7 + this.keyBits) / 8];
        this.reset();
    }

    @Override
    public void reset() {
        this.digest.reset();
    }

    @Override
    public void update(byte by) {
        this.digest.update(by);
    }

    @Override
    public void update(byte[] arrby, int n2, int n3) {
        this.digest.update(arrby, n2, n3);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean verifySignature(byte[] arrby) {
        block6 : {
            BigInteger bigInteger;
            block5 : {
                block4 : {
                    try {
                        this.block = this.cipher.processBlock(arrby, 0, arrby.length);
                    }
                    catch (Exception exception) {
                        return false;
                    }
                    bigInteger = new BigInteger(this.block);
                    if (!bigInteger.mod(BigInteger.valueOf((long)16L)).equals((Object)BigInteger.valueOf((long)12L))) break block4;
                    break block5;
                }
                bigInteger = this.kParam.getModulus().subtract(bigInteger);
                if (!bigInteger.mod(BigInteger.valueOf((long)16L)).equals((Object)BigInteger.valueOf((long)12L))) break block6;
            }
            this.createSignatureBlock();
            byte[] arrby2 = BigIntegers.asUnsignedByteArray((int)this.block.length, (BigInteger)bigInteger);
            boolean bl = Arrays.constantTimeAreEqual((byte[])this.block, (byte[])arrby2);
            this.clearBlock(this.block);
            this.clearBlock(arrby2);
            return bl;
        }
        return false;
    }
}

