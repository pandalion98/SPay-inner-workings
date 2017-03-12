package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.RSAKeyParameters;

public class DefaultTlsEncryptionCredentials extends AbstractTlsEncryptionCredentials {
    protected Certificate certificate;
    protected TlsContext context;
    protected AsymmetricKeyParameter privateKey;

    public DefaultTlsEncryptionCredentials(TlsContext tlsContext, Certificate certificate, AsymmetricKeyParameter asymmetricKeyParameter) {
        if (certificate == null) {
            throw new IllegalArgumentException("'certificate' cannot be null");
        } else if (certificate.isEmpty()) {
            throw new IllegalArgumentException("'certificate' cannot be empty");
        } else if (asymmetricKeyParameter == null) {
            throw new IllegalArgumentException("'privateKey' cannot be null");
        } else if (!asymmetricKeyParameter.isPrivate()) {
            throw new IllegalArgumentException("'privateKey' must be private");
        } else if (asymmetricKeyParameter instanceof RSAKeyParameters) {
            this.context = tlsContext;
            this.certificate = certificate;
            this.privateKey = asymmetricKeyParameter;
        } else {
            throw new IllegalArgumentException("'privateKey' type not supported: " + asymmetricKeyParameter.getClass().getName());
        }
    }

    public byte[] decryptPreMasterSecret(byte[] bArr) {
        return TlsRSAUtils.safeDecryptPreMasterSecret(this.context, (RSAKeyParameters) this.privateKey, bArr);
    }

    public Certificate getCertificate() {
        return this.certificate;
    }
}
