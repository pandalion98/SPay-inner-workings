/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.crypto.signers;

import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Signer;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.util.Arrays;

public class GenericSigner
implements Signer {
    private final Digest digest;
    private final AsymmetricBlockCipher engine;
    private boolean forSigning;

    public GenericSigner(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest) {
        this.engine = asymmetricBlockCipher;
        this.digest = digest;
    }

    @Override
    public byte[] generateSignature() {
        if (!this.forSigning) {
            throw new IllegalStateException("GenericSigner not initialised for signature generation.");
        }
        byte[] arrby = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(arrby, 0);
        return this.engine.processBlock(arrby, 0, arrby.length);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        this.forSigning = bl;
        AsymmetricKeyParameter asymmetricKeyParameter = cipherParameters instanceof ParametersWithRandom ? (AsymmetricKeyParameter)((ParametersWithRandom)cipherParameters).getParameters() : (AsymmetricKeyParameter)cipherParameters;
        if (bl && !asymmetricKeyParameter.isPrivate()) {
            throw new IllegalArgumentException("signing requires private key");
        }
        if (!bl && asymmetricKeyParameter.isPrivate()) {
            throw new IllegalArgumentException("verification requires public key");
        }
        this.reset();
        this.engine.init(bl, cipherParameters);
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
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public boolean verifySignature(byte[] arrby) {
        byte[] arrby2;
        byte[] arrby3;
        if (this.forSigning) {
            throw new IllegalStateException("GenericSigner not initialised for verification");
        }
        byte[] arrby4 = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(arrby4, 0);
        try {
            arrby3 = this.engine.processBlock(arrby, 0, arrby.length);
            if (arrby3.length < arrby4.length) {
                arrby2 = new byte[arrby4.length];
                System.arraycopy((Object)arrby3, (int)0, (Object)arrby2, (int)(arrby2.length - arrby3.length), (int)arrby3.length);
                do {
                    return Arrays.constantTimeAreEqual((byte[])arrby2, (byte[])arrby4);
                    break;
                } while (true);
            }
        }
        catch (Exception exception) {
            return false;
        }
        arrby2 = arrby3;
        return Arrays.constantTimeAreEqual((byte[])arrby2, (byte[])arrby4);
    }
}

