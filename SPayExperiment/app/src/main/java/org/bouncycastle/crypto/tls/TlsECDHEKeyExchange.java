/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.InputStream
 *  java.io.OutputStream
 *  java.security.SecureRandom
 *  java.util.Vector
 *  org.bouncycastle.util.io.TeeInputStream
 */
package org.bouncycastle.crypto.tls;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.Vector;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Signer;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.tls.Certificate;
import org.bouncycastle.crypto.tls.CertificateRequest;
import org.bouncycastle.crypto.tls.DigestInputBuffer;
import org.bouncycastle.crypto.tls.DigitallySigned;
import org.bouncycastle.crypto.tls.SecurityParameters;
import org.bouncycastle.crypto.tls.SignatureAndHashAlgorithm;
import org.bouncycastle.crypto.tls.SignerInputBuffer;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsCredentials;
import org.bouncycastle.crypto.tls.TlsECCUtils;
import org.bouncycastle.crypto.tls.TlsECDHKeyExchange;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsSigner;
import org.bouncycastle.crypto.tls.TlsSignerCredentials;
import org.bouncycastle.crypto.tls.TlsUtils;
import org.bouncycastle.util.io.TeeInputStream;

public class TlsECDHEKeyExchange
extends TlsECDHKeyExchange {
    protected TlsSignerCredentials serverCredentials = null;

    public TlsECDHEKeyExchange(int n2, Vector vector, int[] arrn, short[] arrs, short[] arrs2) {
        super(n2, vector, arrn, arrs, arrs2);
    }

    @Override
    public byte[] generateServerKeyExchange() {
        DigestInputBuffer digestInputBuffer = new DigestInputBuffer();
        this.ecAgreePrivateKey = TlsECCUtils.generateEphemeralServerKeyExchange(this.context.getSecureRandom(), this.namedCurves, this.clientECPointFormats, (OutputStream)digestInputBuffer);
        SignatureAndHashAlgorithm signatureAndHashAlgorithm = TlsUtils.getSignatureAndHashAlgorithm(this.context, this.serverCredentials);
        Digest digest = TlsUtils.createHash(signatureAndHashAlgorithm);
        SecurityParameters securityParameters = this.context.getSecurityParameters();
        digest.update(securityParameters.clientRandom, 0, securityParameters.clientRandom.length);
        digest.update(securityParameters.serverRandom, 0, securityParameters.serverRandom.length);
        digestInputBuffer.updateDigest(digest);
        byte[] arrby = new byte[digest.getDigestSize()];
        digest.doFinal(arrby, 0);
        new DigitallySigned(signatureAndHashAlgorithm, this.serverCredentials.generateCertificateSignature(arrby)).encode((OutputStream)digestInputBuffer);
        return digestInputBuffer.toByteArray();
    }

    protected Signer initVerifyer(TlsSigner tlsSigner, SignatureAndHashAlgorithm signatureAndHashAlgorithm, SecurityParameters securityParameters) {
        Signer signer = tlsSigner.createVerifyer(signatureAndHashAlgorithm, this.serverPublicKey);
        signer.update(securityParameters.clientRandom, 0, securityParameters.clientRandom.length);
        signer.update(securityParameters.serverRandom, 0, securityParameters.serverRandom.length);
        return signer;
    }

    @Override
    public void processClientCredentials(TlsCredentials tlsCredentials) {
        if (tlsCredentials instanceof TlsSignerCredentials) {
            return;
        }
        throw new TlsFatalAlert(80);
    }

    @Override
    public void processServerCredentials(TlsCredentials tlsCredentials) {
        if (!(tlsCredentials instanceof TlsSignerCredentials)) {
            throw new TlsFatalAlert(80);
        }
        this.processServerCertificate(tlsCredentials.getCertificate());
        this.serverCredentials = (TlsSignerCredentials)tlsCredentials;
    }

    @Override
    public void processServerKeyExchange(InputStream inputStream) {
        SecurityParameters securityParameters = this.context.getSecurityParameters();
        SignerInputBuffer signerInputBuffer = new SignerInputBuffer();
        TeeInputStream teeInputStream = new TeeInputStream(inputStream, (OutputStream)signerInputBuffer);
        ECDomainParameters eCDomainParameters = TlsECCUtils.readECParameters(this.namedCurves, this.clientECPointFormats, (InputStream)teeInputStream);
        byte[] arrby = TlsUtils.readOpaque8((InputStream)teeInputStream);
        DigitallySigned digitallySigned = DigitallySigned.parse(this.context, inputStream);
        Signer signer = this.initVerifyer(this.tlsSigner, digitallySigned.getAlgorithm(), securityParameters);
        signerInputBuffer.updateSigner(signer);
        if (!signer.verifySignature(digitallySigned.getSignature())) {
            throw new TlsFatalAlert(51);
        }
        this.ecAgreePublicKey = TlsECCUtils.validateECPublicKey(TlsECCUtils.deserializeECPublicKey(this.clientECPointFormats, eCDomainParameters, arrby));
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
}

