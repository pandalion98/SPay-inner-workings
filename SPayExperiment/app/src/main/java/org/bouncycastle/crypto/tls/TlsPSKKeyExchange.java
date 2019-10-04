/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayOutputStream
 *  java.io.InputStream
 *  java.io.OutputStream
 *  java.lang.IllegalArgumentException
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.Throwable
 *  java.math.BigInteger
 *  java.security.SecureRandom
 *  java.util.Vector
 *  org.bouncycastle.util.Arrays
 *  org.bouncycastle.util.io.Streams
 */
package org.bouncycastle.crypto.tls;

import java.io.ByteArrayOutputStream;
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
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.tls.AbstractTlsKeyExchange;
import org.bouncycastle.crypto.tls.Certificate;
import org.bouncycastle.crypto.tls.CertificateRequest;
import org.bouncycastle.crypto.tls.SecurityParameters;
import org.bouncycastle.crypto.tls.ServerDHParams;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsCredentials;
import org.bouncycastle.crypto.tls.TlsDHUtils;
import org.bouncycastle.crypto.tls.TlsECCUtils;
import org.bouncycastle.crypto.tls.TlsEncryptionCredentials;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsPSKIdentity;
import org.bouncycastle.crypto.tls.TlsPSKIdentityManager;
import org.bouncycastle.crypto.tls.TlsRSAUtils;
import org.bouncycastle.crypto.tls.TlsUtils;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.io.Streams;

public class TlsPSKKeyExchange
extends AbstractTlsKeyExchange {
    protected short[] clientECPointFormats;
    protected DHPrivateKeyParameters dhAgreePrivateKey = null;
    protected DHPublicKeyParameters dhAgreePublicKey = null;
    protected DHParameters dhParameters;
    protected ECPrivateKeyParameters ecAgreePrivateKey = null;
    protected ECPublicKeyParameters ecAgreePublicKey = null;
    protected int[] namedCurves;
    protected byte[] premasterSecret;
    protected byte[] psk = null;
    protected TlsPSKIdentity pskIdentity;
    protected TlsPSKIdentityManager pskIdentityManager;
    protected byte[] psk_identity_hint = null;
    protected RSAKeyParameters rsaServerPublicKey = null;
    protected TlsEncryptionCredentials serverCredentials = null;
    protected short[] serverECPointFormats;
    protected AsymmetricKeyParameter serverPublicKey = null;

    public TlsPSKKeyExchange(int n2, Vector vector, TlsPSKIdentity tlsPSKIdentity, TlsPSKIdentityManager tlsPSKIdentityManager, DHParameters dHParameters, int[] arrn, short[] arrs, short[] arrs2) {
        super(n2, vector);
        switch (n2) {
            default: {
                throw new IllegalArgumentException("unsupported key exchange algorithm");
            }
            case 13: 
            case 14: 
            case 15: 
            case 24: 
        }
        this.pskIdentity = tlsPSKIdentity;
        this.pskIdentityManager = tlsPSKIdentityManager;
        this.dhParameters = dHParameters;
        this.namedCurves = arrn;
        this.clientECPointFormats = arrs;
        this.serverECPointFormats = arrs2;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void generateClientKeyExchange(OutputStream outputStream) {
        byte[] arrby;
        if (this.psk_identity_hint == null) {
            this.pskIdentity.skipIdentityHint();
        } else {
            this.pskIdentity.notifyIdentityHint(this.psk_identity_hint);
        }
        if ((arrby = this.pskIdentity.getPSKIdentity()) == null) {
            throw new TlsFatalAlert(80);
        }
        this.psk = this.pskIdentity.getPSK();
        if (this.psk == null) {
            throw new TlsFatalAlert(80);
        }
        TlsUtils.writeOpaque16(arrby, outputStream);
        this.context.getSecurityParameters().pskIdentity = Arrays.clone((byte[])arrby);
        if (this.keyExchange == 14) {
            this.dhAgreePrivateKey = TlsDHUtils.generateEphemeralClientKeyExchange(this.context.getSecureRandom(), this.dhParameters, outputStream);
            return;
        } else {
            if (this.keyExchange == 24) {
                this.ecAgreePrivateKey = TlsECCUtils.generateEphemeralClientKeyExchange(this.context.getSecureRandom(), this.serverECPointFormats, this.ecAgreePublicKey.getParameters(), outputStream);
                return;
            }
            if (this.keyExchange != 15) return;
            {
                this.premasterSecret = TlsRSAUtils.generateEncryptedPreMasterSecret(this.context, this.rsaServerPublicKey, outputStream);
                return;
            }
        }
    }

    protected byte[] generateOtherSecret(int n2) {
        if (this.keyExchange == 14) {
            if (this.dhAgreePrivateKey != null) {
                return TlsDHUtils.calculateDHBasicAgreement(this.dhAgreePublicKey, this.dhAgreePrivateKey);
            }
            throw new TlsFatalAlert(80);
        }
        if (this.keyExchange == 24) {
            if (this.ecAgreePrivateKey != null) {
                return TlsECCUtils.calculateECDHBasicAgreement(this.ecAgreePublicKey, this.ecAgreePrivateKey);
            }
            throw new TlsFatalAlert(80);
        }
        if (this.keyExchange == 15) {
            return this.premasterSecret;
        }
        return new byte[n2];
    }

    @Override
    public byte[] generatePremasterSecret() {
        byte[] arrby = this.generateOtherSecret(this.psk.length);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(4 + arrby.length + this.psk.length);
        TlsUtils.writeOpaque16(arrby, (OutputStream)byteArrayOutputStream);
        TlsUtils.writeOpaque16(this.psk, (OutputStream)byteArrayOutputStream);
        Arrays.fill((byte[])this.psk, (byte)0);
        this.psk = null;
        return byteArrayOutputStream.toByteArray();
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public byte[] generateServerKeyExchange() {
        this.psk_identity_hint = this.pskIdentityManager.getHint();
        if (this.psk_identity_hint == null && !this.requiresServerKeyExchange()) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (this.psk_identity_hint == null) {
            TlsUtils.writeOpaque16(TlsUtils.EMPTY_BYTES, (OutputStream)byteArrayOutputStream);
        } else {
            TlsUtils.writeOpaque16(this.psk_identity_hint, (OutputStream)byteArrayOutputStream);
        }
        if (this.keyExchange != 14) {
            if (this.keyExchange != 24) return byteArrayOutputStream.toByteArray();
            this.ecAgreePrivateKey = TlsECCUtils.generateEphemeralServerKeyExchange(this.context.getSecureRandom(), this.namedCurves, this.clientECPointFormats, (OutputStream)byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
        if (this.dhParameters == null) {
            throw new TlsFatalAlert(80);
        }
        this.dhAgreePrivateKey = TlsDHUtils.generateEphemeralServerKeyExchange(this.context.getSecureRandom(), this.dhParameters, (OutputStream)byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public void processClientCredentials(TlsCredentials tlsCredentials) {
        throw new TlsFatalAlert(80);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void processClientKeyExchange(InputStream inputStream) {
        byte[] arrby;
        byte[] arrby2 = TlsUtils.readOpaque16(inputStream);
        this.psk = this.pskIdentityManager.getPSK(arrby2);
        if (this.psk == null) {
            throw new TlsFatalAlert(115);
        }
        this.context.getSecurityParameters().pskIdentity = arrby2;
        if (this.keyExchange == 14) {
            this.dhAgreePublicKey = TlsDHUtils.validateDHPublicKey(new DHPublicKeyParameters(TlsDHUtils.readDHParameter(inputStream), this.dhParameters));
            return;
        } else {
            if (this.keyExchange == 24) {
                byte[] arrby3 = TlsUtils.readOpaque8(inputStream);
                ECDomainParameters eCDomainParameters = this.ecAgreePrivateKey.getParameters();
                this.ecAgreePublicKey = TlsECCUtils.validateECPublicKey(TlsECCUtils.deserializeECPublicKey(this.serverECPointFormats, eCDomainParameters, arrby3));
                return;
            }
            if (this.keyExchange != 15) return;
            {
                arrby = TlsUtils.isSSL(this.context) ? Streams.readAll((InputStream)inputStream) : TlsUtils.readOpaque16(inputStream);
            }
        }
        this.premasterSecret = this.serverCredentials.decryptPreMasterSecret(arrby);
    }

    @Override
    public void processServerCertificate(Certificate certificate) {
        if (this.keyExchange != 15) {
            throw new TlsFatalAlert(10);
        }
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

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void processServerKeyExchange(InputStream inputStream) {
        this.psk_identity_hint = TlsUtils.readOpaque16(inputStream);
        if (this.keyExchange == 14) {
            this.dhAgreePublicKey = TlsDHUtils.validateDHPublicKey(ServerDHParams.parse(inputStream).getPublicKey());
            this.dhParameters = this.dhAgreePublicKey.getParameters();
            return;
        } else {
            if (this.keyExchange != 24) return;
            {
                ECDomainParameters eCDomainParameters = TlsECCUtils.readECParameters(this.namedCurves, this.clientECPointFormats, inputStream);
                byte[] arrby = TlsUtils.readOpaque8(inputStream);
                this.ecAgreePublicKey = TlsECCUtils.validateECPublicKey(TlsECCUtils.deserializeECPublicKey(this.clientECPointFormats, eCDomainParameters, arrby));
                return;
            }
        }
    }

    @Override
    public boolean requiresServerKeyExchange() {
        switch (this.keyExchange) {
            default: {
                return false;
            }
            case 14: 
            case 24: 
        }
        return true;
    }

    @Override
    public void skipServerCredentials() {
        if (this.keyExchange == 15) {
            throw new TlsFatalAlert(10);
        }
    }

    @Override
    public void validateCertificateRequest(CertificateRequest certificateRequest) {
        throw new TlsFatalAlert(10);
    }

    protected RSAKeyParameters validateRSAPublicKey(RSAKeyParameters rSAKeyParameters) {
        if (!rSAKeyParameters.getExponent().isProbablePrime(2)) {
            throw new TlsFatalAlert(47);
        }
        return rSAKeyParameters;
    }
}

