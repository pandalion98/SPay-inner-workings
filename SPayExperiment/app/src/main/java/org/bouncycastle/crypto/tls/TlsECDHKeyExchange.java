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
 *  java.security.SecureRandom
 *  java.util.Vector
 */
package org.bouncycastle.crypto.tls;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.Vector;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.tls.AbstractTlsKeyExchange;
import org.bouncycastle.crypto.tls.Certificate;
import org.bouncycastle.crypto.tls.CertificateRequest;
import org.bouncycastle.crypto.tls.TlsAgreementCredentials;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsCredentials;
import org.bouncycastle.crypto.tls.TlsECCUtils;
import org.bouncycastle.crypto.tls.TlsECDSASigner;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsRSASigner;
import org.bouncycastle.crypto.tls.TlsSigner;
import org.bouncycastle.crypto.tls.TlsSignerCredentials;
import org.bouncycastle.crypto.tls.TlsUtils;
import org.bouncycastle.crypto.util.PublicKeyFactory;

public class TlsECDHKeyExchange
extends AbstractTlsKeyExchange {
    protected TlsAgreementCredentials agreementCredentials;
    protected short[] clientECPointFormats;
    protected ECPrivateKeyParameters ecAgreePrivateKey;
    protected ECPublicKeyParameters ecAgreePublicKey;
    protected int[] namedCurves;
    protected short[] serverECPointFormats;
    protected AsymmetricKeyParameter serverPublicKey;
    protected TlsSigner tlsSigner;

    /*
     * Enabled aggressive block sorting
     */
    public TlsECDHKeyExchange(int n2, Vector vector, int[] arrn, short[] arrs, short[] arrs2) {
        super(n2, vector);
        switch (n2) {
            default: {
                throw new IllegalArgumentException("unsupported key exchange algorithm");
            }
            case 19: {
                this.tlsSigner = new TlsRSASigner();
                break;
            }
            case 17: {
                this.tlsSigner = new TlsECDSASigner();
                break;
            }
            case 16: 
            case 18: {
                this.tlsSigner = null;
            }
        }
        this.namedCurves = arrn;
        this.clientECPointFormats = arrs;
        this.serverECPointFormats = arrs2;
    }

    @Override
    public void generateClientKeyExchange(OutputStream outputStream) {
        if (this.agreementCredentials == null) {
            this.ecAgreePrivateKey = TlsECCUtils.generateEphemeralClientKeyExchange(this.context.getSecureRandom(), this.serverECPointFormats, this.ecAgreePublicKey.getParameters(), outputStream);
        }
    }

    @Override
    public byte[] generatePremasterSecret() {
        if (this.agreementCredentials != null) {
            return this.agreementCredentials.generateAgreement(this.ecAgreePublicKey);
        }
        if (this.ecAgreePrivateKey != null) {
            return TlsECCUtils.calculateECDHBasicAgreement(this.ecAgreePublicKey, this.ecAgreePrivateKey);
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
        if (this.ecAgreePublicKey != null) {
            return;
        }
        byte[] arrby = TlsUtils.readOpaque8(inputStream);
        ECDomainParameters eCDomainParameters = this.ecAgreePrivateKey.getParameters();
        this.ecAgreePublicKey = TlsECCUtils.validateECPublicKey(TlsECCUtils.deserializeECPublicKey(this.serverECPointFormats, eCDomainParameters, arrby));
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
                    this.ecAgreePublicKey = TlsECCUtils.validateECPublicKey((ECPublicKeyParameters)this.serverPublicKey);
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
            case 17: 
            case 19: 
            case 20: 
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
                case 64: 
                case 65: 
                case 66: 
            }
        }
    }
}

