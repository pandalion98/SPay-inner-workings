/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.String
 */
package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.tls.AbstractTlsEncryptionCredentials;
import org.bouncycastle.crypto.tls.Certificate;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsRSAUtils;

public class DefaultTlsEncryptionCredentials
extends AbstractTlsEncryptionCredentials {
    protected Certificate certificate;
    protected TlsContext context;
    protected AsymmetricKeyParameter privateKey;

    public DefaultTlsEncryptionCredentials(TlsContext tlsContext, Certificate certificate, AsymmetricKeyParameter asymmetricKeyParameter) {
        if (certificate == null) {
            throw new IllegalArgumentException("'certificate' cannot be null");
        }
        if (certificate.isEmpty()) {
            throw new IllegalArgumentException("'certificate' cannot be empty");
        }
        if (asymmetricKeyParameter == null) {
            throw new IllegalArgumentException("'privateKey' cannot be null");
        }
        if (!asymmetricKeyParameter.isPrivate()) {
            throw new IllegalArgumentException("'privateKey' must be private");
        }
        if (asymmetricKeyParameter instanceof RSAKeyParameters) {
            this.context = tlsContext;
            this.certificate = certificate;
            this.privateKey = asymmetricKeyParameter;
            return;
        }
        throw new IllegalArgumentException("'privateKey' type not supported: " + asymmetricKeyParameter.getClass().getName());
    }

    @Override
    public byte[] decryptPreMasterSecret(byte[] arrby) {
        return TlsRSAUtils.safeDecryptPreMasterSecret(this.context, (RSAKeyParameters)this.privateKey, arrby);
    }

    @Override
    public Certificate getCertificate() {
        return this.certificate;
    }
}

