package org.bouncycastle.crypto.tls;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.jcajce.spec.SkeinParameterSpec;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class TlsECDHKeyExchange extends AbstractTlsKeyExchange {
    protected TlsAgreementCredentials agreementCredentials;
    protected short[] clientECPointFormats;
    protected ECPrivateKeyParameters ecAgreePrivateKey;
    protected ECPublicKeyParameters ecAgreePublicKey;
    protected int[] namedCurves;
    protected short[] serverECPointFormats;
    protected AsymmetricKeyParameter serverPublicKey;
    protected TlsSigner tlsSigner;

    public TlsECDHKeyExchange(int i, Vector vector, int[] iArr, short[] sArr, short[] sArr2) {
        super(i, vector);
        switch (i) {
            case X509KeyUsage.dataEncipherment /*16*/:
            case NamedCurve.secp192k1 /*18*/:
                this.tlsSigner = null;
                break;
            case NamedCurve.secp160r2 /*17*/:
                this.tlsSigner = new TlsECDSASigner();
                break;
            case NamedCurve.secp192r1 /*19*/:
                this.tlsSigner = new TlsRSASigner();
                break;
            default:
                throw new IllegalArgumentException("unsupported key exchange algorithm");
        }
        this.namedCurves = iArr;
        this.clientECPointFormats = sArr;
        this.serverECPointFormats = sArr2;
    }

    public void generateClientKeyExchange(OutputStream outputStream) {
        if (this.agreementCredentials == null) {
            this.ecAgreePrivateKey = TlsECCUtils.generateEphemeralClientKeyExchange(this.context.getSecureRandom(), this.serverECPointFormats, this.ecAgreePublicKey.getParameters(), outputStream);
        }
    }

    public byte[] generatePremasterSecret() {
        if (this.agreementCredentials != null) {
            return this.agreementCredentials.generateAgreement(this.ecAgreePublicKey);
        }
        if (this.ecAgreePrivateKey != null) {
            return TlsECCUtils.calculateECDHBasicAgreement(this.ecAgreePublicKey, this.ecAgreePrivateKey);
        }
        throw new TlsFatalAlert((short) 80);
    }

    public void init(TlsContext tlsContext) {
        super.init(tlsContext);
        if (this.tlsSigner != null) {
            this.tlsSigner.init(tlsContext);
        }
    }

    public void processClientCertificate(Certificate certificate) {
    }

    public void processClientCredentials(TlsCredentials tlsCredentials) {
        if (tlsCredentials instanceof TlsAgreementCredentials) {
            this.agreementCredentials = (TlsAgreementCredentials) tlsCredentials;
        } else if (!(tlsCredentials instanceof TlsSignerCredentials)) {
            throw new TlsFatalAlert((short) 80);
        }
    }

    public void processClientKeyExchange(InputStream inputStream) {
        if (this.ecAgreePublicKey == null) {
            byte[] readOpaque8 = TlsUtils.readOpaque8(inputStream);
            this.ecAgreePublicKey = TlsECCUtils.validateECPublicKey(TlsECCUtils.deserializeECPublicKey(this.serverECPointFormats, this.ecAgreePrivateKey.getParameters(), readOpaque8));
        }
    }

    public void processServerCertificate(Certificate certificate) {
        if (certificate.isEmpty()) {
            throw new TlsFatalAlert((short) 42);
        }
        Certificate certificateAt = certificate.getCertificateAt(0);
        try {
            this.serverPublicKey = PublicKeyFactory.createKey(certificateAt.getSubjectPublicKeyInfo());
            if (this.tlsSigner == null) {
                try {
                    this.ecAgreePublicKey = TlsECCUtils.validateECPublicKey((ECPublicKeyParameters) this.serverPublicKey);
                    TlsUtils.validateKeyUsage(certificateAt, 8);
                } catch (Throwable e) {
                    throw new TlsFatalAlert((short) 46, e);
                }
            } else if (this.tlsSigner.isValidPublicKey(this.serverPublicKey)) {
                TlsUtils.validateKeyUsage(certificateAt, X509KeyUsage.digitalSignature);
            } else {
                throw new TlsFatalAlert((short) 46);
            }
            super.processServerCertificate(certificate);
        } catch (Throwable e2) {
            throw new TlsFatalAlert((short) 43, e2);
        }
    }

    public boolean requiresServerKeyExchange() {
        switch (this.keyExchange) {
            case NamedCurve.secp160r2 /*17*/:
            case NamedCurve.secp192r1 /*19*/:
            case SkeinParameterSpec.PARAM_TYPE_NONCE /*20*/:
                return true;
            default:
                return false;
        }
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
                case CipherSuite.TLS_RSA_WITH_CAMELLIA_128_CBC_SHA /*65*/:
                case CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_128_CBC_SHA /*66*/:
                    i++;
                default:
                    throw new TlsFatalAlert((short) 47);
            }
        }
    }
}
