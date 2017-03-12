package org.bouncycastle.crypto.tls;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.DHParameters;
import org.bouncycastle.crypto.params.DHPrivateKeyParameters;
import org.bouncycastle.crypto.params.DHPublicKeyParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.io.Streams;

public class TlsPSKKeyExchange extends AbstractTlsKeyExchange {
    protected short[] clientECPointFormats;
    protected DHPrivateKeyParameters dhAgreePrivateKey;
    protected DHPublicKeyParameters dhAgreePublicKey;
    protected DHParameters dhParameters;
    protected ECPrivateKeyParameters ecAgreePrivateKey;
    protected ECPublicKeyParameters ecAgreePublicKey;
    protected int[] namedCurves;
    protected byte[] premasterSecret;
    protected byte[] psk;
    protected TlsPSKIdentity pskIdentity;
    protected TlsPSKIdentityManager pskIdentityManager;
    protected byte[] psk_identity_hint;
    protected RSAKeyParameters rsaServerPublicKey;
    protected TlsEncryptionCredentials serverCredentials;
    protected short[] serverECPointFormats;
    protected AsymmetricKeyParameter serverPublicKey;

    public TlsPSKKeyExchange(int i, Vector vector, TlsPSKIdentity tlsPSKIdentity, TlsPSKIdentityManager tlsPSKIdentityManager, DHParameters dHParameters, int[] iArr, short[] sArr, short[] sArr2) {
        super(i, vector);
        this.psk_identity_hint = null;
        this.psk = null;
        this.dhAgreePrivateKey = null;
        this.dhAgreePublicKey = null;
        this.ecAgreePrivateKey = null;
        this.ecAgreePublicKey = null;
        this.serverPublicKey = null;
        this.rsaServerPublicKey = null;
        this.serverCredentials = null;
        switch (i) {
            case NamedCurve.sect571k1 /*13*/:
            case NamedCurve.sect571r1 /*14*/:
            case NamedCurve.secp160k1 /*15*/:
            case NamedCurve.secp384r1 /*24*/:
                this.pskIdentity = tlsPSKIdentity;
                this.pskIdentityManager = tlsPSKIdentityManager;
                this.dhParameters = dHParameters;
                this.namedCurves = iArr;
                this.clientECPointFormats = sArr;
                this.serverECPointFormats = sArr2;
            default:
                throw new IllegalArgumentException("unsupported key exchange algorithm");
        }
    }

    public void generateClientKeyExchange(OutputStream outputStream) {
        if (this.psk_identity_hint == null) {
            this.pskIdentity.skipIdentityHint();
        } else {
            this.pskIdentity.notifyIdentityHint(this.psk_identity_hint);
        }
        byte[] pSKIdentity = this.pskIdentity.getPSKIdentity();
        if (pSKIdentity == null) {
            throw new TlsFatalAlert((short) 80);
        }
        this.psk = this.pskIdentity.getPSK();
        if (this.psk == null) {
            throw new TlsFatalAlert((short) 80);
        }
        TlsUtils.writeOpaque16(pSKIdentity, outputStream);
        this.context.getSecurityParameters().pskIdentity = Arrays.clone(pSKIdentity);
        if (this.keyExchange == 14) {
            this.dhAgreePrivateKey = TlsDHUtils.generateEphemeralClientKeyExchange(this.context.getSecureRandom(), this.dhParameters, outputStream);
        } else if (this.keyExchange == 24) {
            this.ecAgreePrivateKey = TlsECCUtils.generateEphemeralClientKeyExchange(this.context.getSecureRandom(), this.serverECPointFormats, this.ecAgreePublicKey.getParameters(), outputStream);
        } else if (this.keyExchange == 15) {
            this.premasterSecret = TlsRSAUtils.generateEncryptedPreMasterSecret(this.context, this.rsaServerPublicKey, outputStream);
        }
    }

    protected byte[] generateOtherSecret(int i) {
        if (this.keyExchange == 14) {
            if (this.dhAgreePrivateKey != null) {
                return TlsDHUtils.calculateDHBasicAgreement(this.dhAgreePublicKey, this.dhAgreePrivateKey);
            }
            throw new TlsFatalAlert((short) 80);
        } else if (this.keyExchange != 24) {
            return this.keyExchange == 15 ? this.premasterSecret : new byte[i];
        } else {
            if (this.ecAgreePrivateKey != null) {
                return TlsECCUtils.calculateECDHBasicAgreement(this.ecAgreePublicKey, this.ecAgreePrivateKey);
            }
            throw new TlsFatalAlert((short) 80);
        }
    }

    public byte[] generatePremasterSecret() {
        byte[] generateOtherSecret = generateOtherSecret(this.psk.length);
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream((generateOtherSecret.length + 4) + this.psk.length);
        TlsUtils.writeOpaque16(generateOtherSecret, byteArrayOutputStream);
        TlsUtils.writeOpaque16(this.psk, byteArrayOutputStream);
        Arrays.fill(this.psk, (byte) 0);
        this.psk = null;
        return byteArrayOutputStream.toByteArray();
    }

    public byte[] generateServerKeyExchange() {
        this.psk_identity_hint = this.pskIdentityManager.getHint();
        if (this.psk_identity_hint == null && !requiresServerKeyExchange()) {
            return null;
        }
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (this.psk_identity_hint == null) {
            TlsUtils.writeOpaque16(TlsUtils.EMPTY_BYTES, byteArrayOutputStream);
        } else {
            TlsUtils.writeOpaque16(this.psk_identity_hint, byteArrayOutputStream);
        }
        if (this.keyExchange == 14) {
            if (this.dhParameters == null) {
                throw new TlsFatalAlert((short) 80);
            }
            this.dhAgreePrivateKey = TlsDHUtils.generateEphemeralServerKeyExchange(this.context.getSecureRandom(), this.dhParameters, byteArrayOutputStream);
        } else if (this.keyExchange == 24) {
            this.ecAgreePrivateKey = TlsECCUtils.generateEphemeralServerKeyExchange(this.context.getSecureRandom(), this.namedCurves, this.clientECPointFormats, byteArrayOutputStream);
        }
        return byteArrayOutputStream.toByteArray();
    }

    public void processClientCredentials(TlsCredentials tlsCredentials) {
        throw new TlsFatalAlert((short) 80);
    }

    public void processClientKeyExchange(InputStream inputStream) {
        byte[] readOpaque16 = TlsUtils.readOpaque16(inputStream);
        this.psk = this.pskIdentityManager.getPSK(readOpaque16);
        if (this.psk == null) {
            throw new TlsFatalAlert(AlertDescription.unknown_psk_identity);
        }
        this.context.getSecurityParameters().pskIdentity = readOpaque16;
        if (this.keyExchange == 14) {
            this.dhAgreePublicKey = TlsDHUtils.validateDHPublicKey(new DHPublicKeyParameters(TlsDHUtils.readDHParameter(inputStream), this.dhParameters));
        } else if (this.keyExchange == 24) {
            this.ecAgreePublicKey = TlsECCUtils.validateECPublicKey(TlsECCUtils.deserializeECPublicKey(this.serverECPointFormats, this.ecAgreePrivateKey.getParameters(), TlsUtils.readOpaque8(inputStream)));
        } else if (this.keyExchange == 15) {
            this.premasterSecret = this.serverCredentials.decryptPreMasterSecret(TlsUtils.isSSL(this.context) ? Streams.readAll(inputStream) : TlsUtils.readOpaque16(inputStream));
        }
    }

    public void processServerCertificate(Certificate certificate) {
        if (this.keyExchange != 15) {
            throw new TlsFatalAlert((short) 10);
        } else if (certificate.isEmpty()) {
            throw new TlsFatalAlert((short) 42);
        } else {
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
    }

    public void processServerCredentials(TlsCredentials tlsCredentials) {
        if (tlsCredentials instanceof TlsEncryptionCredentials) {
            processServerCertificate(tlsCredentials.getCertificate());
            this.serverCredentials = (TlsEncryptionCredentials) tlsCredentials;
            return;
        }
        throw new TlsFatalAlert((short) 80);
    }

    public void processServerKeyExchange(InputStream inputStream) {
        this.psk_identity_hint = TlsUtils.readOpaque16(inputStream);
        if (this.keyExchange == 14) {
            this.dhAgreePublicKey = TlsDHUtils.validateDHPublicKey(ServerDHParams.parse(inputStream).getPublicKey());
            this.dhParameters = this.dhAgreePublicKey.getParameters();
        } else if (this.keyExchange == 24) {
            this.ecAgreePublicKey = TlsECCUtils.validateECPublicKey(TlsECCUtils.deserializeECPublicKey(this.clientECPointFormats, TlsECCUtils.readECParameters(this.namedCurves, this.clientECPointFormats, inputStream), TlsUtils.readOpaque8(inputStream)));
        }
    }

    public boolean requiresServerKeyExchange() {
        switch (this.keyExchange) {
            case NamedCurve.sect571r1 /*14*/:
            case NamedCurve.secp384r1 /*24*/:
                return true;
            default:
                return false;
        }
    }

    public void skipServerCredentials() {
        if (this.keyExchange == 15) {
            throw new TlsFatalAlert((short) 10);
        }
    }

    public void validateCertificateRequest(CertificateRequest certificateRequest) {
        throw new TlsFatalAlert((short) 10);
    }

    protected RSAKeyParameters validateRSAPublicKey(RSAKeyParameters rSAKeyParameters) {
        if (rSAKeyParameters.getExponent().isProbablePrime(2)) {
            return rSAKeyParameters;
        }
        throw new TlsFatalAlert((short) 47);
    }
}
