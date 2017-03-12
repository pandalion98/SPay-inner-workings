package org.bouncycastle.crypto.tls;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.util.io.Streams;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class TlsRSAKeyExchange extends AbstractTlsKeyExchange {
    protected byte[] premasterSecret;
    protected RSAKeyParameters rsaServerPublicKey;
    protected TlsEncryptionCredentials serverCredentials;
    protected AsymmetricKeyParameter serverPublicKey;

    public TlsRSAKeyExchange(Vector vector) {
        super(1, vector);
        this.serverPublicKey = null;
        this.rsaServerPublicKey = null;
        this.serverCredentials = null;
    }

    public void generateClientKeyExchange(OutputStream outputStream) {
        this.premasterSecret = TlsRSAUtils.generateEncryptedPreMasterSecret(this.context, this.rsaServerPublicKey, outputStream);
    }

    public byte[] generatePremasterSecret() {
        if (this.premasterSecret == null) {
            throw new TlsFatalAlert((short) 80);
        }
        byte[] bArr = this.premasterSecret;
        this.premasterSecret = null;
        return bArr;
    }

    public void processClientCredentials(TlsCredentials tlsCredentials) {
        if (!(tlsCredentials instanceof TlsSignerCredentials)) {
            throw new TlsFatalAlert((short) 80);
        }
    }

    public void processClientKeyExchange(InputStream inputStream) {
        this.premasterSecret = this.serverCredentials.decryptPreMasterSecret(TlsUtils.isSSL(this.context) ? Streams.readAll(inputStream) : TlsUtils.readOpaque16(inputStream));
    }

    public void processServerCertificate(Certificate certificate) {
        if (certificate.isEmpty()) {
            throw new TlsFatalAlert((short) 42);
        }
        Certificate certificateAt = certificate.getCertificateAt(0);
        try {
            this.serverPublicKey = PublicKeyFactory.createKey(certificateAt.getSubjectPublicKeyInfo());
            if (this.serverPublicKey.isPrivate()) {
                throw new TlsFatalAlert((short) 80);
            }
            this.rsaServerPublicKey = validateRSAPublicKey((RSAKeyParameters) this.serverPublicKey);
            TlsUtils.validateKeyUsage(certificateAt, 32);
            super.processServerCertificate(certificate);
        } catch (Throwable e) {
            throw new TlsFatalAlert((short) 43, e);
        }
    }

    public void processServerCredentials(TlsCredentials tlsCredentials) {
        if (tlsCredentials instanceof TlsEncryptionCredentials) {
            processServerCertificate(tlsCredentials.getCertificate());
            this.serverCredentials = (TlsEncryptionCredentials) tlsCredentials;
            return;
        }
        throw new TlsFatalAlert((short) 80);
    }

    public void skipServerCredentials() {
        throw new TlsFatalAlert((short) 10);
    }

    public void validateCertificateRequest(CertificateRequest certificateRequest) {
        short[] certificateTypes = certificateRequest.getCertificateTypes();
        int i = 0;
        while (i < certificateTypes.length) {
            switch (certificateTypes[i]) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                case X509KeyUsage.nonRepudiation /*64*/:
                    i++;
                default:
                    throw new TlsFatalAlert((short) 47);
            }
        }
    }

    protected RSAKeyParameters validateRSAPublicKey(RSAKeyParameters rSAKeyParameters) {
        if (rSAKeyParameters.getExponent().isProbablePrime(2)) {
            return rSAKeyParameters;
        }
        throw new TlsFatalAlert((short) 47);
    }
}
