package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.Signer;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

public interface TlsSigner {
    Signer createSigner(AsymmetricKeyParameter asymmetricKeyParameter);

    Signer createSigner(SignatureAndHashAlgorithm signatureAndHashAlgorithm, AsymmetricKeyParameter asymmetricKeyParameter);

    Signer createVerifyer(AsymmetricKeyParameter asymmetricKeyParameter);

    Signer createVerifyer(SignatureAndHashAlgorithm signatureAndHashAlgorithm, AsymmetricKeyParameter asymmetricKeyParameter);

    byte[] generateRawSignature(AsymmetricKeyParameter asymmetricKeyParameter, byte[] bArr);

    byte[] generateRawSignature(SignatureAndHashAlgorithm signatureAndHashAlgorithm, AsymmetricKeyParameter asymmetricKeyParameter, byte[] bArr);

    void init(TlsContext tlsContext);

    boolean isValidPublicKey(AsymmetricKeyParameter asymmetricKeyParameter);

    boolean verifyRawSignature(SignatureAndHashAlgorithm signatureAndHashAlgorithm, byte[] bArr, AsymmetricKeyParameter asymmetricKeyParameter, byte[] bArr2);

    boolean verifyRawSignature(byte[] bArr, AsymmetricKeyParameter asymmetricKeyParameter, byte[] bArr2);
}
