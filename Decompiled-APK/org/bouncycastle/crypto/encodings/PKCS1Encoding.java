package org.bouncycastle.crypto.encodings;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.SecureRandom;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ParametersWithRandom;

public class PKCS1Encoding implements AsymmetricBlockCipher {
    private static final int HEADER_LENGTH = 10;
    public static final String STRICT_LENGTH_ENABLED_PROPERTY = "org.bouncycastle.pkcs1.strict";
    private AsymmetricBlockCipher engine;
    private byte[] fallback;
    private boolean forEncryption;
    private boolean forPrivateKey;
    private int pLen;
    private SecureRandom random;
    private boolean useStrictLength;

    /* renamed from: org.bouncycastle.crypto.encodings.PKCS1Encoding.1 */
    class C07451 implements PrivilegedAction {
        C07451() {
        }

        public Object run() {
            return System.getProperty(PKCS1Encoding.STRICT_LENGTH_ENABLED_PROPERTY);
        }
    }

    public PKCS1Encoding(AsymmetricBlockCipher asymmetricBlockCipher) {
        this.pLen = -1;
        this.fallback = null;
        this.engine = asymmetricBlockCipher;
        this.useStrictLength = useStrict();
    }

    public PKCS1Encoding(AsymmetricBlockCipher asymmetricBlockCipher, int i) {
        this.pLen = -1;
        this.fallback = null;
        this.engine = asymmetricBlockCipher;
        this.useStrictLength = useStrict();
        this.pLen = i;
    }

    public PKCS1Encoding(AsymmetricBlockCipher asymmetricBlockCipher, byte[] bArr) {
        this.pLen = -1;
        this.fallback = null;
        this.engine = asymmetricBlockCipher;
        this.useStrictLength = useStrict();
        this.fallback = bArr;
        this.pLen = bArr.length;
    }

    private static int checkPkcs1Encoding(byte[] bArr, int i) {
        int i2;
        int i3 = 0 | (bArr[0] ^ 2);
        int length = bArr.length - (i + 1);
        for (i2 = 1; i2 < length; i2++) {
            byte b = bArr[i2];
            int i4 = b | (b >> 1);
            i4 |= i4 >> 2;
            i3 |= ((i4 | (i4 >> 4)) & 1) - 1;
        }
        i2 = bArr[bArr.length - (i + 1)] | i3;
        i2 |= i2 >> 1;
        i2 |= i2 >> 2;
        return (((i2 | (i2 >> 4)) & 1) - 1) ^ -1;
    }

    private byte[] decodeBlock(byte[] bArr, int i, int i2) {
        if (this.pLen != -1) {
            return decodeBlockOrRandom(bArr, i, i2);
        }
        Object processBlock = this.engine.processBlock(bArr, i, i2);
        if (processBlock.length < getOutputBlockSize()) {
            throw new InvalidCipherTextException("block truncated");
        }
        byte b = processBlock[0];
        if (this.forPrivateKey) {
            if (b != 2) {
                throw new InvalidCipherTextException("unknown block type");
            }
        } else if (b != (byte) 1) {
            throw new InvalidCipherTextException("unknown block type");
        }
        if (!this.useStrictLength || processBlock.length == this.engine.getOutputBlockSize()) {
            int i3 = 1;
            while (i3 != processBlock.length) {
                byte b2 = processBlock[i3];
                if (b2 == null) {
                    break;
                } else if (b != (byte) 1 || b2 == (byte) -1) {
                    i3++;
                } else {
                    throw new InvalidCipherTextException("block padding incorrect");
                }
            }
            int i4 = i3 + 1;
            if (i4 > processBlock.length || i4 < HEADER_LENGTH) {
                throw new InvalidCipherTextException("no data in block");
            }
            Object obj = new byte[(processBlock.length - i4)];
            System.arraycopy(processBlock, i4, obj, 0, obj.length);
            return obj;
        }
        throw new InvalidCipherTextException("block incorrect size");
    }

    private byte[] decodeBlockOrRandom(byte[] bArr, int i, int i2) {
        if (this.forPrivateKey) {
            byte[] bArr2;
            byte[] processBlock = this.engine.processBlock(bArr, i, i2);
            if (this.fallback == null) {
                bArr2 = new byte[this.pLen];
                this.random.nextBytes(bArr2);
            } else {
                bArr2 = this.fallback;
            }
            if (processBlock.length < getOutputBlockSize()) {
                throw new InvalidCipherTextException("block truncated");
            } else if (!this.useStrictLength || processBlock.length == this.engine.getOutputBlockSize()) {
                int checkPkcs1Encoding = checkPkcs1Encoding(processBlock, this.pLen);
                byte[] bArr3 = new byte[this.pLen];
                for (int i3 = 0; i3 < this.pLen; i3++) {
                    bArr3[i3] = (byte) ((processBlock[(processBlock.length - this.pLen) + i3] & (checkPkcs1Encoding ^ -1)) | (bArr2[i3] & checkPkcs1Encoding));
                }
                return bArr3;
            } else {
                throw new InvalidCipherTextException("block incorrect size");
            }
        }
        throw new InvalidCipherTextException("sorry, this method is only for decryption, not for signing");
    }

    private byte[] encodeBlock(byte[] bArr, int i, int i2) {
        int i3 = 1;
        if (i2 > getInputBlockSize()) {
            throw new IllegalArgumentException("input data too large");
        }
        Object obj = new byte[this.engine.getInputBlockSize()];
        if (this.forPrivateKey) {
            obj[0] = 1;
            while (i3 != (obj.length - i2) - 1) {
                obj[i3] = (byte) -1;
                i3++;
            }
        } else {
            this.random.nextBytes(obj);
            obj[0] = (byte) 2;
            while (i3 != (obj.length - i2) - 1) {
                while (obj[i3] == null) {
                    obj[i3] = (byte) this.random.nextInt();
                }
                i3++;
            }
        }
        obj[(obj.length - i2) - 1] = null;
        System.arraycopy(bArr, i, obj, obj.length - i2, i2);
        return this.engine.processBlock(obj, 0, obj.length);
    }

    private boolean useStrict() {
        String str = (String) AccessController.doPrivileged(new C07451());
        return str == null || str.equals("true");
    }

    public int getInputBlockSize() {
        int inputBlockSize = this.engine.getInputBlockSize();
        return this.forEncryption ? inputBlockSize - 10 : inputBlockSize;
    }

    public int getOutputBlockSize() {
        int outputBlockSize = this.engine.getOutputBlockSize();
        return this.forEncryption ? outputBlockSize : outputBlockSize - 10;
    }

    public AsymmetricBlockCipher getUnderlyingCipher() {
        return this.engine;
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        AsymmetricKeyParameter asymmetricKeyParameter;
        if (cipherParameters instanceof ParametersWithRandom) {
            ParametersWithRandom parametersWithRandom = (ParametersWithRandom) cipherParameters;
            this.random = parametersWithRandom.getRandom();
            asymmetricKeyParameter = (AsymmetricKeyParameter) parametersWithRandom.getParameters();
        } else {
            this.random = new SecureRandom();
            asymmetricKeyParameter = (AsymmetricKeyParameter) cipherParameters;
        }
        this.engine.init(z, cipherParameters);
        this.forPrivateKey = asymmetricKeyParameter.isPrivate();
        this.forEncryption = z;
    }

    public byte[] processBlock(byte[] bArr, int i, int i2) {
        return this.forEncryption ? encodeBlock(bArr, i, i2) : decodeBlock(bArr, i, i2);
    }
}
