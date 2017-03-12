package org.bouncycastle.crypto.tls;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.DHParameters;
import org.bouncycastle.crypto.params.DHPrivateKeyParameters;
import org.bouncycastle.crypto.params.DHPublicKeyParameters;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class TlsDHKeyExchange extends AbstractTlsKeyExchange {
    protected TlsAgreementCredentials agreementCredentials;
    protected DHPrivateKeyParameters dhAgreePrivateKey;
    protected DHPublicKeyParameters dhAgreePublicKey;
    protected DHParameters dhParameters;
    protected AsymmetricKeyParameter serverPublicKey;
    protected TlsSigner tlsSigner;

    public TlsDHKeyExchange(int i, Vector vector, DHParameters dHParameters) {
        super(i, vector);
        switch (i) {
            case F2m.PPB /*3*/:
                this.tlsSigner = new TlsDSSSigner();
                break;
            case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                this.tlsSigner = new TlsRSASigner();
                break;
            case ECCurve.COORD_SKEWED /*7*/:
            case NamedCurve.sect283k1 /*9*/:
                this.tlsSigner = null;
                break;
            default:
                throw new IllegalArgumentException("unsupported key exchange algorithm");
        }
        this.dhParameters = dHParameters;
    }

    public void generateClientKeyExchange(OutputStream outputStream) {
        if (this.agreementCredentials == null) {
            this.dhAgreePrivateKey = TlsDHUtils.generateEphemeralClientKeyExchange(this.context.getSecureRandom(), this.dhParameters, outputStream);
        }
    }

    public byte[] generatePremasterSecret() {
        if (this.agreementCredentials != null) {
            return this.agreementCredentials.generateAgreement(this.dhAgreePublicKey);
        }
        if (this.dhAgreePrivateKey != null) {
            return TlsDHUtils.calculateDHBasicAgreement(this.dhAgreePublicKey, this.dhAgreePrivateKey);
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
        if (this.dhAgreePublicKey == null) {
            this.dhAgreePublicKey = TlsDHUtils.validateDHPublicKey(new DHPublicKeyParameters(TlsDHUtils.readDHParameter(inputStream), this.dhParameters));
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
                    this.dhAgreePublicKey = TlsDHUtils.validateDHPublicKey((DHPublicKeyParameters) this.serverPublicKey);
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
            case F2m.PPB /*3*/:
            case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
            case CertStatus.UNREVOKED /*11*/:
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
                case F2m.PPB /*3*/:
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                case X509KeyUsage.nonRepudiation /*64*/:
                    i++;
                default:
                    throw new TlsFatalAlert((short) 47);
            }
        }
    }
}
