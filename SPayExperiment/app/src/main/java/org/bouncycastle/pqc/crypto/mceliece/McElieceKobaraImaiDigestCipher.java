/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.pqc.crypto.mceliece;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.pqc.crypto.MessageEncryptor;

public class McElieceKobaraImaiDigestCipher {
    private boolean forEncrypting;
    private final MessageEncryptor mcElieceCCA2Cipher;
    private final Digest messDigest;

    public McElieceKobaraImaiDigestCipher(MessageEncryptor messageEncryptor, Digest digest) {
        this.mcElieceCCA2Cipher = messageEncryptor;
        this.messDigest = digest;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void init(boolean bl, CipherParameters cipherParameters) {
        this.forEncrypting = bl;
        AsymmetricKeyParameter asymmetricKeyParameter = cipherParameters instanceof ParametersWithRandom ? (AsymmetricKeyParameter)((ParametersWithRandom)cipherParameters).getParameters() : (AsymmetricKeyParameter)cipherParameters;
        if (bl && asymmetricKeyParameter.isPrivate()) {
            throw new IllegalArgumentException("Encrypting Requires Public Key.");
        }
        if (!bl && !asymmetricKeyParameter.isPrivate()) {
            throw new IllegalArgumentException("Decrypting Requires Private Key.");
        }
        this.reset();
        this.mcElieceCCA2Cipher.init(bl, cipherParameters);
    }

    public byte[] messageDecrypt(byte[] arrby) {
        if (this.forEncrypting) {
            throw new IllegalStateException("McElieceKobaraImaiDigestCipher not initialised for decrypting.");
        }
        try {
            byte[] arrby2 = this.mcElieceCCA2Cipher.messageDecrypt(arrby);
            return arrby2;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public byte[] messageEncrypt() {
        if (!this.forEncrypting) {
            throw new IllegalStateException("McElieceKobaraImaiDigestCipher not initialised for encrypting.");
        }
        byte[] arrby = new byte[this.messDigest.getDigestSize()];
        this.messDigest.doFinal(arrby, 0);
        try {
            byte[] arrby2 = this.mcElieceCCA2Cipher.messageEncrypt(arrby);
            return arrby2;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public void reset() {
        this.messDigest.reset();
    }

    public void update(byte by) {
        this.messDigest.update(by);
    }

    public void update(byte[] arrby, int n, int n2) {
        this.messDigest.update(arrby, n, n2);
    }
}

