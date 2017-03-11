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

public class X931Signer implements Signer {
    public static final int TRAILER_IMPLICIT = 188;
    public static final int TRAILER_RIPEMD128 = 13004;
    public static final int TRAILER_RIPEMD160 = 12748;
    public static final int TRAILER_SHA1 = 13260;
    public static final int TRAILER_SHA224 = 14540;
    public static final int TRAILER_SHA256 = 13516;
    public static final int TRAILER_SHA384 = 14028;
    public static final int TRAILER_SHA512 = 13772;
    public static final int TRAILER_WHIRLPOOL = 14284;
    private static Hashtable trailerMap;
    private byte[] block;
    private AsymmetricBlockCipher cipher;
    private Digest digest;
    private RSAKeyParameters kParam;
    private int keyBits;
    private int trailer;

    static {
        trailerMap = new Hashtable();
        trailerMap.put("RIPEMD128", Integers.valueOf(TRAILER_RIPEMD128));
        trailerMap.put("RIPEMD160", Integers.valueOf(TRAILER_RIPEMD160));
        trailerMap.put("SHA-1", Integers.valueOf(TRAILER_SHA1));
        trailerMap.put("SHA-224", Integers.valueOf(TRAILER_SHA224));
        trailerMap.put("SHA-256", Integers.valueOf(TRAILER_SHA256));
        trailerMap.put("SHA-384", Integers.valueOf(TRAILER_SHA384));
        trailerMap.put("SHA-512", Integers.valueOf(TRAILER_SHA512));
        trailerMap.put("Whirlpool", Integers.valueOf(TRAILER_WHIRLPOOL));
    }

    public X931Signer(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest) {
        this(asymmetricBlockCipher, digest, false);
    }

    public X931Signer(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest, boolean z) {
        this.cipher = asymmetricBlockCipher;
        this.digest = digest;
        if (z) {
            this.trailer = TRAILER_IMPLICIT;
            return;
        }
        Integer num = (Integer) trailerMap.get(digest.getAlgorithmName());
        if (num != null) {
            this.trailer = num.intValue();
            return;
        }
        throw new IllegalArgumentException("no valid trailer for digest");
    }

    private void clearBlock(byte[] bArr) {
        for (int i = 0; i != bArr.length; i++) {
            bArr[i] = (byte) 0;
        }
    }

    private void createSignatureBlock() {
        int digestSize = this.digest.getDigestSize();
        if (this.trailer == TRAILER_IMPLICIT) {
            digestSize = (this.block.length - digestSize) - 1;
            this.digest.doFinal(this.block, digestSize);
            this.block[this.block.length - 1] = PSSSigner.TRAILER_IMPLICIT;
        } else {
            digestSize = (this.block.length - digestSize) - 2;
            this.digest.doFinal(this.block, digestSize);
            this.block[this.block.length - 2] = (byte) (this.trailer >>> 8);
            this.block[this.block.length - 1] = (byte) this.trailer;
        }
        this.block[0] = (byte) 107;
        for (int i = digestSize - 2; i != 0; i--) {
            this.block[i] = (byte) -69;
        }
        this.block[digestSize - 1] = (byte) -70;
    }

    public byte[] generateSignature() {
        createSignatureBlock();
        BigInteger bigInteger = new BigInteger(1, this.cipher.processBlock(this.block, 0, this.block.length));
        BigInteger subtract = this.kParam.getModulus().subtract(bigInteger);
        clearBlock(this.block);
        this.kParam.getModulus().shiftRight(2);
        return bigInteger.compareTo(subtract) > 0 ? BigIntegers.asUnsignedByteArray((this.kParam.getModulus().bitLength() + 7) / 8, subtract) : BigIntegers.asUnsignedByteArray((this.kParam.getModulus().bitLength() + 7) / 8, bigInteger);
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        this.kParam = (RSAKeyParameters) cipherParameters;
        this.cipher.init(z, this.kParam);
        this.keyBits = this.kParam.getModulus().bitLength();
        this.block = new byte[((this.keyBits + 7) / 8)];
        reset();
    }

    public void reset() {
        this.digest.reset();
    }

    public void update(byte b) {
        this.digest.update(b);
    }

    public void update(byte[] bArr, int i, int i2) {
        this.digest.update(bArr, i, i2);
    }

    public boolean verifySignature(byte[] bArr) {
        try {
            this.block = this.cipher.processBlock(bArr, 0, bArr.length);
            BigInteger bigInteger = new BigInteger(this.block);
            if (!bigInteger.mod(BigInteger.valueOf(16)).equals(BigInteger.valueOf(12))) {
                bigInteger = this.kParam.getModulus().subtract(bigInteger);
                if (!bigInteger.mod(BigInteger.valueOf(16)).equals(BigInteger.valueOf(12))) {
                    return false;
                }
            }
            createSignatureBlock();
            byte[] asUnsignedByteArray = BigIntegers.asUnsignedByteArray(this.block.length, bigInteger);
            boolean constantTimeAreEqual = Arrays.constantTimeAreEqual(this.block, asUnsignedByteArray);
            clearBlock(this.block);
            clearBlock(asUnsignedByteArray);
            return constantTimeAreEqual;
        } catch (Exception e) {
            return false;
        }
    }
}
