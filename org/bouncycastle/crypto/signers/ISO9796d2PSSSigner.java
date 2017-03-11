package org.bouncycastle.crypto.signers;

import java.security.SecureRandom;
import java.util.Hashtable;
import org.bouncycastle.asn1.eac.CertificateBody;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.SignerWithRecovery;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.params.ParametersWithSalt;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Integers;

public class ISO9796d2PSSSigner implements SignerWithRecovery {
    public static final int TRAILER_IMPLICIT = 188;
    public static final int TRAILER_RIPEMD128 = 13004;
    public static final int TRAILER_RIPEMD160 = 12748;
    public static final int TRAILER_SHA1 = 13260;
    public static final int TRAILER_SHA256 = 13516;
    public static final int TRAILER_SHA384 = 14028;
    public static final int TRAILER_SHA512 = 13772;
    public static final int TRAILER_WHIRLPOOL = 14284;
    private static Hashtable trailerMap;
    private byte[] block;
    private AsymmetricBlockCipher cipher;
    private Digest digest;
    private boolean fullMessage;
    private int hLen;
    private int keyBits;
    private byte[] mBuf;
    private int messageLength;
    private byte[] preBlock;
    private int preMStart;
    private byte[] preSig;
    private int preTLength;
    private SecureRandom random;
    private byte[] recoveredMessage;
    private int saltLength;
    private byte[] standardSalt;
    private int trailer;

    static {
        trailerMap = new Hashtable();
        trailerMap.put("RIPEMD128", Integers.valueOf(TRAILER_RIPEMD128));
        trailerMap.put("RIPEMD160", Integers.valueOf(TRAILER_RIPEMD160));
        trailerMap.put("SHA-1", Integers.valueOf(TRAILER_SHA1));
        trailerMap.put("SHA-256", Integers.valueOf(TRAILER_SHA256));
        trailerMap.put("SHA-384", Integers.valueOf(TRAILER_SHA384));
        trailerMap.put("SHA-512", Integers.valueOf(TRAILER_SHA512));
        trailerMap.put("Whirlpool", Integers.valueOf(TRAILER_WHIRLPOOL));
    }

    public ISO9796d2PSSSigner(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest, int i) {
        this(asymmetricBlockCipher, digest, i, false);
    }

    public ISO9796d2PSSSigner(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest, int i, boolean z) {
        this.cipher = asymmetricBlockCipher;
        this.digest = digest;
        this.hLen = digest.getDigestSize();
        this.saltLength = i;
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

    private void ItoOSP(int i, byte[] bArr) {
        bArr[0] = (byte) (i >>> 24);
        bArr[1] = (byte) (i >>> 16);
        bArr[2] = (byte) (i >>> 8);
        bArr[3] = (byte) (i >>> 0);
    }

    private void LtoOSP(long j, byte[] bArr) {
        bArr[0] = (byte) ((int) (j >>> 56));
        bArr[1] = (byte) ((int) (j >>> 48));
        bArr[2] = (byte) ((int) (j >>> 40));
        bArr[3] = (byte) ((int) (j >>> 32));
        bArr[4] = (byte) ((int) (j >>> 24));
        bArr[5] = (byte) ((int) (j >>> 16));
        bArr[6] = (byte) ((int) (j >>> 8));
        bArr[7] = (byte) ((int) (j >>> 0));
    }

    private void clearBlock(byte[] bArr) {
        for (int i = 0; i != bArr.length; i++) {
            bArr[i] = (byte) 0;
        }
    }

    private boolean isSameAs(byte[] bArr, byte[] bArr2) {
        boolean z = true;
        if (this.messageLength != bArr2.length) {
            z = false;
        }
        boolean z2 = z;
        for (int i = 0; i != bArr2.length; i++) {
            if (bArr[i] != bArr2[i]) {
                z2 = false;
            }
        }
        return z2;
    }

    private byte[] maskGeneratorFunction1(byte[] bArr, int i, int i2, int i3) {
        Object obj = new byte[i3];
        Object obj2 = new byte[this.hLen];
        byte[] bArr2 = new byte[4];
        this.digest.reset();
        int i4 = 0;
        while (i4 < i3 / this.hLen) {
            ItoOSP(i4, bArr2);
            this.digest.update(bArr, i, i2);
            this.digest.update(bArr2, 0, bArr2.length);
            this.digest.doFinal(obj2, 0);
            System.arraycopy(obj2, 0, obj, this.hLen * i4, this.hLen);
            i4++;
        }
        if (this.hLen * i4 < i3) {
            ItoOSP(i4, bArr2);
            this.digest.update(bArr, i, i2);
            this.digest.update(bArr2, 0, bArr2.length);
            this.digest.doFinal(obj2, 0);
            System.arraycopy(obj2, 0, obj, this.hLen * i4, obj.length - (i4 * this.hLen));
        }
        return obj;
    }

    public byte[] generateSignature() {
        Object obj;
        byte[] bArr = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(bArr, 0);
        byte[] bArr2 = new byte[8];
        LtoOSP((long) (this.messageLength * 8), bArr2);
        this.digest.update(bArr2, 0, bArr2.length);
        this.digest.update(this.mBuf, 0, this.messageLength);
        this.digest.update(bArr, 0, bArr.length);
        if (this.standardSalt != null) {
            obj = this.standardSalt;
        } else {
            obj = new byte[this.saltLength];
            this.random.nextBytes(obj);
        }
        this.digest.update(obj, 0, obj.length);
        Object obj2 = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(obj2, 0);
        int i = 2;
        if (this.trailer == TRAILER_IMPLICIT) {
            i = 1;
        }
        int length = ((((this.block.length - this.messageLength) - obj.length) - this.hLen) - i) - 1;
        this.block[length] = (byte) 1;
        System.arraycopy(this.mBuf, 0, this.block, length + 1, this.messageLength);
        System.arraycopy(obj, 0, this.block, (length + 1) + this.messageLength, obj.length);
        byte[] maskGeneratorFunction1 = maskGeneratorFunction1(obj2, 0, obj2.length, (this.block.length - this.hLen) - i);
        for (int i2 = 0; i2 != maskGeneratorFunction1.length; i2++) {
            byte[] bArr3 = this.block;
            bArr3[i2] = (byte) (bArr3[i2] ^ maskGeneratorFunction1[i2]);
        }
        System.arraycopy(obj2, 0, this.block, (this.block.length - this.hLen) - i, this.hLen);
        if (this.trailer == TRAILER_IMPLICIT) {
            this.block[this.block.length - 1] = PSSSigner.TRAILER_IMPLICIT;
        } else {
            this.block[this.block.length - 2] = (byte) (this.trailer >>> 8);
            this.block[this.block.length - 1] = (byte) this.trailer;
        }
        bArr = this.block;
        bArr[0] = (byte) (bArr[0] & CertificateBody.profileType);
        bArr = this.cipher.processBlock(this.block, 0, this.block.length);
        clearBlock(this.mBuf);
        clearBlock(this.block);
        this.messageLength = 0;
        return bArr;
    }

    public byte[] getRecoveredMessage() {
        return this.recoveredMessage;
    }

    public boolean hasFullMessage() {
        return this.fullMessage;
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        Object obj;
        int i;
        int i2 = this.saltLength;
        RSAKeyParameters rSAKeyParameters;
        if (cipherParameters instanceof ParametersWithRandom) {
            ParametersWithRandom parametersWithRandom = (ParametersWithRandom) cipherParameters;
            rSAKeyParameters = (RSAKeyParameters) parametersWithRandom.getParameters();
            if (z) {
                this.random = parametersWithRandom.getRandom();
            }
            obj = rSAKeyParameters;
            i = i2;
        } else if (cipherParameters instanceof ParametersWithSalt) {
            ParametersWithSalt parametersWithSalt = (ParametersWithSalt) cipherParameters;
            rSAKeyParameters = (RSAKeyParameters) parametersWithSalt.getParameters();
            this.standardSalt = parametersWithSalt.getSalt();
            i2 = this.standardSalt.length;
            if (this.standardSalt.length != this.saltLength) {
                throw new IllegalArgumentException("Fixed salt is of wrong length");
            }
            r6 = rSAKeyParameters;
            i = i2;
        } else {
            r6 = (RSAKeyParameters) cipherParameters;
            if (z) {
                this.random = new SecureRandom();
            }
            i = i2;
        }
        this.cipher.init(z, obj);
        this.keyBits = obj.getModulus().bitLength();
        this.block = new byte[((this.keyBits + 7) / 8)];
        if (this.trailer == TRAILER_IMPLICIT) {
            this.mBuf = new byte[((((this.block.length - this.digest.getDigestSize()) - i) - 1) - 1)];
        } else {
            this.mBuf = new byte[((((this.block.length - this.digest.getDigestSize()) - i) - 1) - 2)];
        }
        reset();
    }

    public void reset() {
        this.digest.reset();
        this.messageLength = 0;
        if (this.mBuf != null) {
            clearBlock(this.mBuf);
        }
        if (this.recoveredMessage != null) {
            clearBlock(this.recoveredMessage);
            this.recoveredMessage = null;
        }
        this.fullMessage = false;
        if (this.preSig != null) {
            this.preSig = null;
            clearBlock(this.preBlock);
            this.preBlock = null;
        }
    }

    public void update(byte b) {
        if (this.preSig != null || this.messageLength >= this.mBuf.length) {
            this.digest.update(b);
            return;
        }
        byte[] bArr = this.mBuf;
        int i = this.messageLength;
        this.messageLength = i + 1;
        bArr[i] = b;
    }

    public void update(byte[] bArr, int i, int i2) {
        if (this.preSig == null) {
            while (i2 > 0 && this.messageLength < this.mBuf.length) {
                update(bArr[i]);
                i++;
                i2--;
            }
        }
        if (i2 > 0) {
            this.digest.update(bArr, i, i2);
        }
    }

    public void updateWithRecoveredMessage(byte[] bArr) {
        int i;
        int i2;
        boolean z = true;
        Object processBlock = this.cipher.processBlock(bArr, 0, bArr.length);
        if (processBlock.length < (this.keyBits + 7) / 8) {
            Object obj = new byte[((this.keyBits + 7) / 8)];
            System.arraycopy(processBlock, 0, obj, obj.length - processBlock.length, processBlock.length);
            clearBlock(processBlock);
            processBlock = obj;
        }
        if (((processBlock[processBlock.length - 1] & GF2Field.MASK) ^ TRAILER_IMPLICIT) == 0) {
            i = 1;
        } else {
            i2 = (processBlock[processBlock.length - 1] & GF2Field.MASK) | ((processBlock[processBlock.length - 2] & GF2Field.MASK) << 8);
            Integer num = (Integer) trailerMap.get(this.digest.getAlgorithmName());
            if (num == null) {
                throw new IllegalArgumentException("unrecognised hash in signature");
            } else if (i2 != num.intValue()) {
                throw new IllegalStateException("signer initialised with wrong digest for trailer " + i2);
            } else {
                i = 2;
            }
        }
        this.digest.doFinal(new byte[this.hLen], 0);
        byte[] maskGeneratorFunction1 = maskGeneratorFunction1(processBlock, (processBlock.length - this.hLen) - i, this.hLen, (processBlock.length - this.hLen) - i);
        for (i2 = 0; i2 != maskGeneratorFunction1.length; i2++) {
            processBlock[i2] = (byte) (processBlock[i2] ^ maskGeneratorFunction1[i2]);
        }
        processBlock[0] = (byte) (processBlock[0] & CertificateBody.profileType);
        i2 = 0;
        while (i2 != processBlock.length && processBlock[i2] != (byte) 1) {
            i2++;
        }
        i2++;
        if (i2 >= processBlock.length) {
            clearBlock(processBlock);
        }
        if (i2 <= 1) {
            z = false;
        }
        this.fullMessage = z;
        this.recoveredMessage = new byte[((maskGeneratorFunction1.length - i2) - this.saltLength)];
        System.arraycopy(processBlock, i2, this.recoveredMessage, 0, this.recoveredMessage.length);
        System.arraycopy(this.recoveredMessage, 0, this.mBuf, 0, this.recoveredMessage.length);
        this.preSig = bArr;
        this.preBlock = processBlock;
        this.preMStart = i2;
        this.preTLength = i;
    }

    public boolean verifySignature(byte[] bArr) {
        byte[] bArr2 = new byte[this.hLen];
        this.digest.doFinal(bArr2, 0);
        if (this.preSig == null) {
            try {
                updateWithRecoveredMessage(bArr);
            } catch (Exception e) {
                return false;
            }
        } else if (!Arrays.areEqual(this.preSig, bArr)) {
            throw new IllegalStateException("updateWithRecoveredMessage called on different signature");
        }
        byte[] bArr3 = this.preBlock;
        int i = this.preMStart;
        int i2 = this.preTLength;
        this.preSig = null;
        this.preBlock = null;
        byte[] bArr4 = new byte[8];
        LtoOSP((long) (this.recoveredMessage.length * 8), bArr4);
        this.digest.update(bArr4, 0, bArr4.length);
        if (this.recoveredMessage.length != 0) {
            this.digest.update(this.recoveredMessage, 0, this.recoveredMessage.length);
        }
        this.digest.update(bArr2, 0, bArr2.length);
        this.digest.update(bArr3, i + this.recoveredMessage.length, this.saltLength);
        bArr4 = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(bArr4, 0);
        i2 = (bArr3.length - i2) - bArr4.length;
        boolean z = true;
        for (int i3 = 0; i3 != bArr4.length; i3++) {
            if (bArr4[i3] != bArr3[i2 + i3]) {
                z = false;
            }
        }
        clearBlock(bArr3);
        clearBlock(bArr4);
        if (z) {
            if (this.messageLength != 0) {
                if (isSameAs(this.mBuf, this.recoveredMessage)) {
                    this.messageLength = 0;
                } else {
                    clearBlock(this.mBuf);
                    return false;
                }
            }
            clearBlock(this.mBuf);
            return true;
        }
        this.fullMessage = false;
        clearBlock(this.recoveredMessage);
        return false;
    }
}
