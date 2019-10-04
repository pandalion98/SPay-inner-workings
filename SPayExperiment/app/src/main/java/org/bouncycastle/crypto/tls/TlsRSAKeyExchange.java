/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.InputStream
 *  java.io.OutputStream
 *  java.lang.RuntimeException
 *  java.lang.Throwable
 *  java.math.BigInteger
 *  java.util.Vector
 *  org.bouncycastle.util.io.Streams
 */
package org.bouncycastle.crypto.tls;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Vector;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.tls.AbstractTlsKeyExchange;
import org.bouncycastle.crypto.tls.Certificate;
import org.bouncycastle.crypto.tls.CertificateRequest;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsCredentials;
import org.bouncycastle.crypto.tls.TlsEncryptionCredentials;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsRSAUtils;
import org.bouncycastle.crypto.tls.TlsSignerCredentials;
import org.bouncycastle.crypto.tls.TlsUtils;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.util.io.Streams;

public class TlsRSAKeyExchange
extends AbstractTlsKeyExchange {
    protected byte[] premasterSecret;
    protected RSAKeyParameters rsaServerPublicKey = null;
    protected TlsEncryptionCredentials serverCredentials = null;
    protected AsymmetricKeyParameter serverPublicKey = null;

    public TlsRSAKeyExchange(Vector vector) {
        super(1, vector);
    }

    @Override
    public void generateClientKeyExchange(OutputStream outputStream) {
        this.premasterSecret = TlsRSAUtils.generateEncryptedPreMasterSecret(this.context, this.rsaServerPublicKey, outputStream);
    }

    @Override
    public byte[] generatePremasterSecret() {
        if (this.premasterSecret == null) {
            throw new TlsFatalAlert(80);
        }
        byte[] arrby = this.premasterSecret;
        this.premasterSecret = null;
        return arrby;
    }

    @Override
    public void processClientCredentials(TlsCredentials tlsCredentials) {
        if (!(tlsCredentials instanceof TlsSignerCredentials)) {
            throw new TlsFatalAlert(80);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void processClientKeyExchange(InputStream inputStream) {
        byte[] arrby = TlsUtils.isSSL(this.context) ? Streams.readAll((InputStream)inputStream) : TlsUtils.readOpaque16(inputStream);
        this.premasterSecret = this.serverCredentials.decryptPreMasterSecret(arrby);
    }

    @Override
    public void processServerCertificate(Certificate certificate) {
        if (certificate.isEmpty()) {
            throw new TlsFatalAlert(42);
        }
        org.bouncycastle.asn1.x509.Certificate certificate2 = certificate.getCertificateAt(0);
        SubjectPublicKeyInfo subjectPublicKeyInfo = certificate2.getSubjectPublicKeyInfo();
        try {
            this.serverPublicKey = PublicKeyFactory.createKey(subjectPublicKeyInfo);
        }
        catch (RuntimeException runtimeException) {
            throw new TlsFatalAlert(43, runtimeException);
        }
        if (this.serverPublicKey.isPrivate()) {
            throw new TlsFatalAlert(80);
        }
        this.rsaServerPublicKey = this.validateRSAPublicKey((RSAKeyParameters)this.serverPublicKey);
        TlsUtils.validateKeyUsage(certificate2, 32);
        super.processServerCertificate(certificate);
    }

    @Override
    public void processServerCredentials(TlsCredentials tlsCredentials) {
        if (!(tlsCredentials instanceof TlsEncryptionCredentials)) {
            throw new TlsFatalAlert(80);
        }
        this.processServerCertificate(tlsCredentials.getCertificate());
        this.serverCredentials = (TlsEncryptionCredentials)tlsCredentials;
    }

    @Override
    public void skipServerCredentials() {
        throw new TlsFatalAlert(10);
    }

    @Override
    public void validateCertificateRequest(CertificateRequest certificateRequest) {
        short[] arrs = certificateRequest.getCertificateTypes();
        for (int i2 = 0; i2 < arrs.length; ++i2) {
            switch (arrs[i2]) {
                default: {
                    throw new TlsFatalAlert(47);
                }
                case 1: 
                case 2: 
                case 64: 
            }
        }
    }

    protected RSAKeyParameters validateRSAPublicKey(RSAKeyParameters rSAKeyParameters) {
        if (!rSAKeyParameters.getExponent().isProbablePrime(2)) {
            throw new TlsFatalAlert(47);
        }
        return rSAKeyParameters;
    }
}

