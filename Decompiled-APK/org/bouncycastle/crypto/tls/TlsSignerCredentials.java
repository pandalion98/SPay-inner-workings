package org.bouncycastle.crypto.tls;

public interface TlsSignerCredentials extends TlsCredentials {
    byte[] generateCertificateSignature(byte[] bArr);

    SignatureAndHashAlgorithm getSignatureAndHashAlgorithm();
}
