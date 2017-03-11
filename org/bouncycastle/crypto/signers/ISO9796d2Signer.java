package org.bouncycastle.crypto.signers;

import java.util.Hashtable;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.SignerWithRecovery;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Integers;

public class ISO9796d2Signer implements SignerWithRecovery {
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
    private int keyBits;
    private byte[] mBuf;
    private int messageLength;
    private byte[] preBlock;
    private byte[] preSig;
    private byte[] recoveredMessage;
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

    public ISO9796d2Signer(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest) {
        this(asymmetricBlockCipher, digest, false);
    }

    public ISO9796d2Signer(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest, boolean z) {
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

    private boolean isSameAs(byte[] bArr, byte[] bArr2) {
        boolean z;
        boolean z2 = true;
        int i;
        if (this.messageLength > this.mBuf.length) {
            if (this.mBuf.length > bArr2.length) {
                z2 = false;
            }
            z = z2;
            for (i = 0; i != this.mBuf.length; i++) {
                if (bArr[i] != bArr2[i]) {
                    z = false;
                }
            }
        } else {
            if (this.messageLength != bArr2.length) {
                z2 = false;
            }
            z = z2;
            for (i = 0; i != bArr2.length; i++) {
                if (bArr[i] != bArr2[i]) {
                    z = false;
                }
            }
        }
        return z;
    }

    private boolean returnFalse(byte[] bArr) {
        clearBlock(this.mBuf);
        clearBlock(bArr);
        return false;
    }

    public byte[] generateSignature() {
        int i;
        int length;
        byte[] bArr;
        int digestSize = this.digest.getDigestSize();
        if (this.trailer == TRAILER_IMPLICIT) {
            i = 8;
            length = (this.block.length - digestSize) - 1;
            this.digest.doFinal(this.block, length);
            this.block[this.block.length - 1] = PSSSigner.TRAILER_IMPLICIT;
        } else {
            i = 16;
            length = (this.block.length - digestSize) - 2;
            this.digest.doFinal(this.block, length);
            this.block[this.block.length - 2] = (byte) (this.trailer >>> 8);
            this.block[this.block.length - 1] = (byte) this.trailer;
        }
        i = ((i + ((digestSize + this.messageLength) * 8)) + 4) - this.keyBits;
        if (i > 0) {
            digestSize = this.messageLength - ((i + 7) / 8);
            i = 96;
            length -= digestSize;
            System.arraycopy(this.mBuf, 0, this.block, length, digestSize);
            digestSize = length;
        } else {
            i = 64;
            length -= this.messageLength;
            System.arraycopy(this.mBuf, 0, this.block, length, this.messageLength);
            digestSize = length;
        }
        if (digestSize - 1 > 0) {
            for (length = digestSize - 1; length != 0; length--) {
                this.block[length] = (byte) -69;
            }
            bArr = this.block;
            digestSize--;
            bArr[digestSize] = (byte) (bArr[digestSize] ^ 1);
            this.block[0] = (byte) 11;
            bArr = this.block;
            bArr[0] = (byte) (i | bArr[0]);
        } else {
            this.block[0] = (byte) 10;
            bArr = this.block;
            bArr[0] = (byte) (i | bArr[0]);
        }
        bArr = this.cipher.processBlock(this.block, 0, this.block.length);
        clearBlock(this.mBuf);
        clearBlock(this.block);
        return bArr;
    }

    public byte[] getRecoveredMessage() {
        return this.recoveredMessage;
    }

    public boolean hasFullMessage() {
        return this.fullMessage;
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        RSAKeyParameters rSAKeyParameters = (RSAKeyParameters) cipherParameters;
        this.cipher.init(z, rSAKeyParameters);
        this.keyBits = rSAKeyParameters.getModulus().bitLength();
        this.block = new byte[((this.keyBits + 7) / 8)];
        if (this.trailer == TRAILER_IMPLICIT) {
            this.mBuf = new byte[((this.block.length - this.digest.getDigestSize()) - 2)];
        } else {
            this.mBuf = new byte[((this.block.length - this.digest.getDigestSize()) - 3)];
        }
        reset();
    }

    public void reset() {
        this.digest.reset();
        this.messageLength = 0;
        clearBlock(this.mBuf);
        if (this.recoveredMessage != null) {
            clearBlock(this.recoveredMessage);
        }
        this.recoveredMessage = null;
        this.fullMessage = false;
        if (this.preSig != null) {
            this.preSig = null;
            clearBlock(this.preBlock);
            this.preBlock = null;
        }
    }

    public void update(byte b) {
        this.digest.update(b);
        if (this.messageLength < this.mBuf.length) {
            this.mBuf[this.messageLength] = b;
        }
        this.messageLength++;
    }

    public void update(byte[] bArr, int i, int i2) {
        while (i2 > 0 && this.messageLength < this.mBuf.length) {
            update(bArr[i]);
            i++;
            i2--;
        }
        this.digest.update(bArr, i, i2);
        this.messageLength += i2;
    }

    public void updateWithRecoveredMessage(byte[] bArr) {
        Object processBlock = this.cipher.processBlock(bArr, 0, bArr.length);
        if (((processBlock[0] & CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256) ^ 64) != 0) {
            throw new InvalidCipherTextException("malformed signature");
        } else if (((processBlock[processBlock.length - 1] & 15) ^ 12) != 0) {
            throw new InvalidCipherTextException("malformed signature");
        } else {
            int i;
            int i2;
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
            i2 = 0;
            while (i2 != processBlock.length && ((processBlock[i2] & 15) ^ 10) != 0) {
                i2++;
            }
            i2++;
            i = (processBlock.length - i) - this.digest.getDigestSize();
            if (i - i2 <= 0) {
                throw new InvalidCipherTextException("malformed block");
            }
            if ((processBlock[0] & 32) == 0) {
                this.fullMessage = true;
                this.recoveredMessage = new byte[(i - i2)];
                System.arraycopy(processBlock, i2, this.recoveredMessage, 0, this.recoveredMessage.length);
            } else {
                this.fullMessage = false;
                this.recoveredMessage = new byte[(i - i2)];
                System.arraycopy(processBlock, i2, this.recoveredMessage, 0, this.recoveredMessage.length);
            }
            this.preSig = bArr;
            this.preBlock = processBlock;
            this.digest.update(this.recoveredMessage, 0, this.recoveredMessage.length);
            this.messageLength = this.recoveredMessage.length;
            System.arraycopy(this.recoveredMessage, 0, this.mBuf, 0, this.recoveredMessage.length);
        }
    }

    public boolean verifySignature(byte[] bArr) {
        Object processBlock;
        if (this.preSig == null) {
            try {
                processBlock = this.cipher.processBlock(bArr, 0, bArr.length);
            } catch (Exception e) {
                return false;
            }
        } else if (Arrays.areEqual(this.preSig, bArr)) {
            Object obj = this.preBlock;
            this.preSig = null;
            this.preBlock = null;
            processBlock = obj;
        } else {
            throw new IllegalStateException("updateWithRecoveredMessage called on different signature");
        }
        if (((processBlock[0] & CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256) ^ 64) != 0) {
            return returnFalse(processBlock);
        }
        if (((processBlock[processBlock.length - 1] & 15) ^ 12) != 0) {
            return returnFalse(processBlock);
        }
        int i;
        int i2;
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
        i2 = 0;
        while (i2 != processBlock.length && ((processBlock[i2] & 15) ^ 10) != 0) {
            i2++;
        }
        int i3 = i2 + 1;
        byte[] bArr2 = new byte[this.digest.getDigestSize()];
        int length = (processBlock.length - i) - bArr2.length;
        if (length - i3 <= 0) {
            return returnFalse(processBlock);
        }
        boolean z;
        int i4;
        if ((processBlock[0] & 32) == 0) {
            this.fullMessage = true;
            if (this.messageLength > length - i3) {
                return returnFalse(processBlock);
            }
            this.digest.reset();
            this.digest.update(processBlock, i3, length - i3);
            this.digest.doFinal(bArr2, 0);
            z = true;
            for (i = 0; i != bArr2.length; i++) {
                i4 = length + i;
                processBlock[i4] = (byte) (processBlock[i4] ^ bArr2[i]);
                if (processBlock[length + i] != null) {
                    z = false;
                }
            }
            if (!z) {
                return returnFalse(processBlock);
            }
            this.recoveredMessage = new byte[(length - i3)];
            System.arraycopy(processBlock, i3, this.recoveredMessage, 0, this.recoveredMessage.length);
        } else {
            this.fullMessage = false;
            this.digest.doFinal(bArr2, 0);
            z = true;
            for (i = 0; i != bArr2.length; i++) {
                i4 = length + i;
                processBlock[i4] = (byte) (processBlock[i4] ^ bArr2[i]);
                if (processBlock[length + i] != null) {
                    z = false;
                }
            }
            if (!z) {
                return returnFalse(processBlock);
            }
            this.recoveredMessage = new byte[(length - i3)];
            System.arraycopy(processBlock, i3, this.recoveredMessage, 0, this.recoveredMessage.length);
        }
        if (this.messageLength != 0 && !isSameAs(this.mBuf, this.recoveredMessage)) {
            return returnFalse(processBlock);
        }
        clearBlock(this.mBuf);
        clearBlock(processBlock);
        return true;
    }
}
