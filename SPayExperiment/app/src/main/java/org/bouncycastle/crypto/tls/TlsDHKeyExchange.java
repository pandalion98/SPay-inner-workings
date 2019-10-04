/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.InputStream
 *  java.io.OutputStream
 *  java.lang.ClassCastException
 *  java.lang.IllegalArgumentException
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.Throwable
 *  java.math.BigInteger
 *  java.security.SecureRandom
 *  java.util.Vector
 */
package org.bouncycastle.crypto.tls;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Vector;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.DHParameters;
import org.bouncycastle.crypto.params.DHPrivateKeyParameters;
import org.bouncycastle.crypto.params.DHPublicKeyParameters;
import org.bouncycastle.crypto.tls.AbstractTlsKeyExchange;
import org.bouncycastle.crypto.tls.Certificate;
import org.bouncycastle.crypto.tls.CertificateRequest;
import org.bouncycastle.crypto.tls.TlsAgreementCredentials;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsCredentials;
import org.bouncycastle.crypto.tls.TlsDHUtils;
import org.bouncycastle.crypto.tls.TlsDSSSigner;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsRSASigner;
import org.bouncycastle.crypto.tls.TlsSigner;
import org.bouncycastle.crypto.tls.TlsSignerCredentials;
import org.bouncycastle.crypto.tls.TlsUtils;
import org.bouncycastle.crypto.util.PublicKeyFactory;

public class TlsDHKeyExchange
extends AbstractTlsKeyExchange {
    protected TlsAgreementCredentials agreementCredentials;
    protected DHPrivateKeyParameters dhAgreePrivateKey;
    protected DHPublicKeyParameters dhAgreePublicKey;
    protected DHParameters dhParameters;
    protected AsymmetricKeyParameter serverPublicKey;
    protected TlsSigner tlsSigner;

    /*
     * Enabled aggressive block sorting
     */
    public TlsDHKeyExchange(int n2, Vector vector, DHParameters dHParameters) {
        super(n2, vector);
        switch (n2) {
            default: {
                throw new IllegalArgumentException("unsupported key exchange algorithm");
            }
            case 7: 
            case 9: {
                this.tlsSigner = null;
                break;
            }
            case 5: {
                this.tlsSigner = new TlsRSASigner();
                break;
            }
            case 3: {
                this.tlsSigner = new TlsDSSSigner();
            }
        }
        this.dhParameters = dHParameters;
    }

    @Override
    public void generateClientKeyExchange(OutputStream outputStream) {
        if (this.agreementCredentials == null) {
            this.dhAgreePrivateKey = TlsDHUtils.generateEphemeralClientKeyExchange(this.context.getSecureRandom(), this.dhParameters, outputStream);
        }
    }

    @Override
    public byte[] generatePremasterSecret() {
        if (this.agreementCredentials != null) {
            return this.agreementCredentials.generateAgreement(this.dhAgreePublicKey);
        }
        if (this.dhAgreePrivateKey != null) {
            return TlsDHUtils.calculateDHBasicAgreement(this.dhAgreePublicKey, this.dhAgreePrivateKey);
        }
        throw new TlsFatalAlert(80);
    }

    @Override
    public void init(TlsContext tlsContext) {
        super.init(tlsContext);
        if (this.tlsSigner != null) {
            this.tlsSigner.init(tlsContext);
        }
    }

    @Override
    public void processClientCertificate(Certificate certificate) {
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void processClientCredentials(TlsCredentials tlsCredentials) {
        if (tlsCredentials instanceof TlsAgreementCredentials) {
            this.agreementCredentials = (TlsAgreementCredentials)tlsCredentials;
            return;
        } else {
            if (tlsCredentials instanceof TlsSignerCredentials) return;
            {
                throw new TlsFatalAlert(80);
            }
        }
    }

    @Override
    public void processClientKeyExchange(InputStream inputStream) {
        if (this.dhAgreePublicKey != null) {
            return;
        }
        this.dhAgreePublicKey = TlsDHUtils.validateDHPublicKey(new DHPublicKeyParameters(TlsDHUtils.readDHParameter(inputStream), this.dhParameters));
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void processServerCertificate(Certificate certificate) {
        block7 : {
            org.bouncycastle.asn1.x509.Certificate certificate2;
            block6 : {
                if (certificate.isEmpty()) {
                    throw new TlsFatalAlert(42);
                }
                certificate2 = certificate.getCertificateAt(0);
                SubjectPublicKeyInfo subjectPublicKeyInfo = certificate2.getSubjectPublicKeyInfo();
                try {
                    this.serverPublicKey = PublicKeyFactory.createKey(subjectPublicKeyInfo);
                    if (this.tlsSigner != null) break block6;
                }
                catch (RuntimeException runtimeException) {
                    throw new TlsFatalAlert(43, runtimeException);
                }
                try {
                    this.dhAgreePublicKey = TlsDHUtils.validateDHPublicKey((DHPublicKeyParameters)this.serverPublicKey);
                }
                catch (ClassCastException classCastException) {
                    throw new TlsFatalAlert(46, classCastException);
                }
                TlsUtils.validateKeyUsage(certificate2, 8);
                break block7;
            }
            if (!this.tlsSigner.isValidPublicKey(this.serverPublicKey)) {
                throw new TlsFatalAlert(46);
            }
            TlsUtils.validateKeyUsage(certificate2, 128);
        }
        super.processServerCertificate(certificate);
    }

    @Override
    public boolean requiresServerKeyExchange() {
        switch (this.keyExchange) {
            default: {
                return false;
            }
            case 3: 
            case 5: 
            case 11: 
        }
        return true;
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
                case 3: 
                case 4: 
                case 64: 
            }
        }
    }
}

