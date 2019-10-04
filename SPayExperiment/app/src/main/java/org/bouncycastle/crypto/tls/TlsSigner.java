/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.Signer;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.tls.SignatureAndHashAlgorithm;
import org.bouncycastle.crypto.tls.TlsContext;

public interface TlsSigner {
    public Signer createSigner(AsymmetricKeyParameter var1);

    public Signer createSigner(SignatureAndHashAlgorithm var1, AsymmetricKeyParameter var2);

    public Signer createVerifyer(AsymmetricKeyParameter var1);

    public Signer createVerifyer(SignatureAndHashAlgorithm var1, AsymmetricKeyParameter var2);

    public byte[] generateRawSignature(AsymmetricKeyParameter var1, byte[] var2);

    public byte[] generateRawSignature(SignatureAndHashAlgorithm var1, AsymmetricKeyParameter var2, byte[] var3);

    public void init(TlsContext var1);

    public boolean isValidPublicKey(AsymmetricKeyParameter var1);

    public boolean verifyRawSignature(SignatureAndHashAlgorithm var1, byte[] var2, AsymmetricKeyParameter var3, byte[] var4);

    public boolean verifyRawSignature(byte[] var1, AsymmetricKeyParameter var2, byte[] var3);
}

