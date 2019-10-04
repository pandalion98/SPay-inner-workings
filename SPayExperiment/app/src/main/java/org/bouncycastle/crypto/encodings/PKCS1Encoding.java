/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.security.AccessController
 *  java.security.PrivilegedAction
 *  java.security.SecureRandom
 */
package org.bouncycastle.crypto.encodings;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.SecureRandom;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ParametersWithRandom;

public class PKCS1Encoding
implements AsymmetricBlockCipher {
    private static final int HEADER_LENGTH = 10;
    public static final String STRICT_LENGTH_ENABLED_PROPERTY = "org.bouncycastle.pkcs1.strict";
    private AsymmetricBlockCipher engine;
    private byte[] fallback = null;
    private boolean forEncryption;
    private boolean forPrivateKey;
    private int pLen = -1;
    private SecureRandom random;
    private boolean useStrictLength;

    public PKCS1Encoding(AsymmetricBlockCipher asymmetricBlockCipher) {
        this.engine = asymmetricBlockCipher;
        this.useStrictLength = this.useStrict();
    }

    public PKCS1Encoding(AsymmetricBlockCipher asymmetricBlockCipher, int n2) {
        this.engine = asymmetricBlockCipher;
        this.useStrictLength = this.useStrict();
        this.pLen = n2;
    }

    public PKCS1Encoding(AsymmetricBlockCipher asymmetricBlockCipher, byte[] arrby) {
        this.engine = asymmetricBlockCipher;
        this.useStrictLength = this.useStrict();
        this.fallback = arrby;
        this.pLen = arrby.length;
    }

    private static int checkPkcs1Encoding(byte[] arrby, int n2) {
        int n3 = 0 | 2 ^ arrby[0];
        int n4 = arrby.length - (n2 + 1);
        for (int i2 = 1; i2 < n4; ++i2) {
            byte by = arrby[i2];
            int n5 = by | by >> 1;
            int n6 = n5 | n5 >> 2;
            n3 |= -1 + (1 & (n6 | n6 >> 4));
        }
        int n7 = n3 | arrby[arrby.length - (n2 + 1)];
        int n8 = n7 | n7 >> 1;
        int n9 = n8 | n8 >> 2;
        return -1 ^ -1 + (1 & (n9 | n9 >> 4));
    }

    private byte[] decodeBlock(byte[] arrby, int n2, int n3) {
        int n4;
        if (this.pLen != -1) {
            return this.decodeBlockOrRandom(arrby, n2, n3);
        }
        byte[] arrby2 = this.engine.processBlock(arrby, n2, n3);
        if (arrby2.length < this.getOutputBlockSize()) {
            throw new InvalidCipherTextException("block truncated");
        }
        byte by = arrby2[0];
        if (this.forPrivateKey ? by != 2 : by != 1) {
            throw new InvalidCipherTextException("unknown block type");
        }
        if (this.useStrictLength && arrby2.length != this.engine.getOutputBlockSize()) {
            throw new InvalidCipherTextException("block incorrect size");
        }
        int n5 = 1;
        do {
            byte by2;
            if (n5 == arrby2.length || (by2 = arrby2[n5]) == 0) {
                n4 = n5 + 1;
                if (n4 <= arrby2.length && n4 >= 10) break;
                throw new InvalidCipherTextException("no data in block");
            }
            if (by == 1 && by2 != -1) {
                throw new InvalidCipherTextException("block padding incorrect");
            }
            ++n5;
        } while (true);
        byte[] arrby3 = new byte[arrby2.length - n4];
        System.arraycopy((Object)arrby2, (int)n4, (Object)arrby3, (int)0, (int)arrby3.length);
        return arrby3;
    }

    /*
     * Enabled aggressive block sorting
     */
    private byte[] decodeBlockOrRandom(byte[] arrby, int n2, int n3) {
        byte[] arrby2;
        if (!this.forPrivateKey) {
            throw new InvalidCipherTextException("sorry, this method is only for decryption, not for signing");
        }
        byte[] arrby3 = this.engine.processBlock(arrby, n2, n3);
        if (this.fallback == null) {
            arrby2 = new byte[this.pLen];
            this.random.nextBytes(arrby2);
        } else {
            arrby2 = this.fallback;
        }
        if (arrby3.length < this.getOutputBlockSize()) {
            throw new InvalidCipherTextException("block truncated");
        }
        if (this.useStrictLength && arrby3.length != this.engine.getOutputBlockSize()) {
            throw new InvalidCipherTextException("block incorrect size");
        }
        int n4 = PKCS1Encoding.checkPkcs1Encoding(arrby3, this.pLen);
        byte[] arrby4 = new byte[this.pLen];
        int n5 = 0;
        while (n5 < this.pLen) {
            arrby4[n5] = (byte)(arrby3[n5 + (arrby3.length - this.pLen)] & ~n4 | n4 & arrby2[n5]);
            ++n5;
        }
        return arrby4;
    }

    private byte[] encodeBlock(byte[] arrby, int n2, int n3) {
        int n4;
        if (n3 > this.getInputBlockSize()) {
            throw new IllegalArgumentException("input data too large");
        }
        byte[] arrby2 = new byte[this.engine.getInputBlockSize()];
        if (this.forPrivateKey) {
            arrby2[0] = (byte)n4;
            for (n4 = 1; n4 != -1 + (arrby2.length - n3); ++n4) {
                arrby2[n4] = -1;
            }
        } else {
            this.random.nextBytes(arrby2);
            arrby2[0] = 2;
            while (n4 != -1 + (arrby2.length - n3)) {
                while (arrby2[n4] == 0) {
                    arrby2[n4] = (byte)this.random.nextInt();
                }
                ++n4;
            }
        }
        arrby2[-1 + (arrby2.length - n3)] = 0;
        System.arraycopy((Object)arrby, (int)n2, (Object)arrby2, (int)(arrby2.length - n3), (int)n3);
        return this.engine.processBlock(arrby2, 0, arrby2.length);
    }

    private boolean useStrict() {
        String string = (String)AccessController.doPrivileged((PrivilegedAction)new PrivilegedAction(){

            public Object run() {
                return System.getProperty((String)PKCS1Encoding.STRICT_LENGTH_ENABLED_PROPERTY);
            }
        });
        return string == null || string.equals((Object)"true");
    }

    @Override
    public int getInputBlockSize() {
        int n2 = this.engine.getInputBlockSize();
        if (this.forEncryption) {
            n2 -= 10;
        }
        return n2;
    }

    @Override
    public int getOutputBlockSize() {
        int n2 = this.engine.getOutputBlockSize();
        if (this.forEncryption) {
            return n2;
        }
        return n2 - 10;
    }

    public AsymmetricBlockCipher getUnderlyingCipher() {
        return this.engine;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        AsymmetricKeyParameter asymmetricKeyParameter;
        if (cipherParameters instanceof ParametersWithRandom) {
            ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
            this.random = parametersWithRandom.getRandom();
            asymmetricKeyParameter = (AsymmetricKeyParameter)parametersWithRandom.getParameters();
        } else {
            this.random = new SecureRandom();
            asymmetricKeyParameter = (AsymmetricKeyParameter)cipherParameters;
        }
        this.engine.init(bl, cipherParameters);
        this.forPrivateKey = asymmetricKeyParameter.isPrivate();
        this.forEncryption = bl;
    }

    @Override
    public byte[] processBlock(byte[] arrby, int n2, int n3) {
        if (this.forEncryption) {
            return this.encodeBlock(arrby, n2, n3);
        }
        return this.decodeBlock(arrby, n2, n3);
    }

}

