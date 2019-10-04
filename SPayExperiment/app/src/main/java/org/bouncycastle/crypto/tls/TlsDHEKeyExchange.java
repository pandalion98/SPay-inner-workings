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
import org.bouncycastle.crypto.params.DHParameters;
import org.bouncycastle.crypto.params.DHPrivateKeyParameters;
import org.bouncycastle.crypto.params.DHPublicKeyParameters;
import org.bouncycastle.crypto.tls.Certificate;
import org.bouncycastle.crypto.tls.DigestInputBuffer;
import org.bouncycastle.crypto.tls.DigitallySigned;
import org.bouncycastle.crypto.tls.SecurityParameters;
import org.bouncycastle.crypto.tls.ServerDHParams;
import org.bouncycastle.crypto.tls.SignatureAndHashAlgorithm;
import org.bouncycastle.crypto.tls.SignerInputBuffer;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsCredentials;
import org.bouncycastle.crypto.tls.TlsDHKeyExchange;
import org.bouncycastle.crypto.tls.TlsDHUtils;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsSigner;
import org.bouncycastle.crypto.tls.TlsSignerCredentials;
import org.bouncycastle.crypto.tls.TlsUtils;
import org.bouncycastle.util.io.TeeInputStream;

public class TlsDHEKeyExchange
extends TlsDHKeyExchange {
    protected TlsSignerCredentials serverCredentials = null;

    public TlsDHEKeyExchange(int n2, Vector vector, DHParameters dHParameters) {
        super(n2, vector, dHParameters);
    }

    @Override
    public byte[] generateServerKeyExchange() {
        if (this.dhParameters == null) {
            throw new TlsFatalAlert(80);
        }
        DigestInputBuffer digestInputBuffer = new DigestInputBuffer();
        this.dhAgreePrivateKey = TlsDHUtils.generateEphemeralServerKeyExchange(this.context.getSecureRandom(), this.dhParameters, (OutputStream)digestInputBuffer);
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
        ServerDHParams serverDHParams = ServerDHParams.parse((InputStream)new TeeInputStream(inputStream, (OutputStream)signerInputBuffer));
        DigitallySigned digitallySigned = DigitallySigned.parse(this.context, inputStream);
        Signer signer = this.initVerifyer(this.tlsSigner, digitallySigned.getAlgorithm(), securityParameters);
        signerInputBuffer.updateSigner(signer);
        if (!signer.verifySignature(digitallySigned.getSignature())) {
            throw new TlsFatalAlert(51);
        }
        this.dhAgreePublicKey = TlsDHUtils.validateDHPublicKey(serverDHParams.getPublicKey());
        this.dhParameters = this.dhAgreePublicKey.getParameters();
    }
}

