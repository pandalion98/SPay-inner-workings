/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.InputStream
 *  java.io.OutputStream
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.Throwable
 *  java.math.BigInteger
 *  java.security.SecureRandom
 *  java.util.Vector
 *  org.bouncycastle.util.Arrays
 *  org.bouncycastle.util.BigIntegers
 *  org.bouncycastle.util.io.TeeInputStream
 */
package org.bouncycastle.crypto.tls;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Vector;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Signer;
import org.bouncycastle.crypto.agreement.srp.SRP6Client;
import org.bouncycastle.crypto.agreement.srp.SRP6Server;
import org.bouncycastle.crypto.agreement.srp.SRP6Util;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.SRP6GroupParameters;
import org.bouncycastle.crypto.tls.AbstractTlsKeyExchange;
import org.bouncycastle.crypto.tls.Certificate;
import org.bouncycastle.crypto.tls.CertificateRequest;
import org.bouncycastle.crypto.tls.DefaultTlsSRPGroupVerifier;
import org.bouncycastle.crypto.tls.DigestInputBuffer;
import org.bouncycastle.crypto.tls.DigitallySigned;
import org.bouncycastle.crypto.tls.SecurityParameters;
import org.bouncycastle.crypto.tls.ServerSRPParams;
import org.bouncycastle.crypto.tls.SignatureAndHashAlgorithm;
import org.bouncycastle.crypto.tls.SignerInputBuffer;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsCredentials;
import org.bouncycastle.crypto.tls.TlsDSSSigner;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsRSASigner;
import org.bouncycastle.crypto.tls.TlsSRPGroupVerifier;
import org.bouncycastle.crypto.tls.TlsSRPLoginParameters;
import org.bouncycastle.crypto.tls.TlsSRPUtils;
import org.bouncycastle.crypto.tls.TlsSigner;
import org.bouncycastle.crypto.tls.TlsSignerCredentials;
import org.bouncycastle.crypto.tls.TlsUtils;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.BigIntegers;
import org.bouncycastle.util.io.TeeInputStream;

public class TlsSRPKeyExchange
extends AbstractTlsKeyExchange {
    protected TlsSRPGroupVerifier groupVerifier;
    protected byte[] identity;
    protected byte[] password;
    protected TlsSignerCredentials serverCredentials = null;
    protected AsymmetricKeyParameter serverPublicKey = null;
    protected SRP6Client srpClient = null;
    protected SRP6GroupParameters srpGroup = null;
    protected BigInteger srpPeerCredentials = null;
    protected byte[] srpSalt = null;
    protected SRP6Server srpServer = null;
    protected BigInteger srpVerifier = null;
    protected TlsSigner tlsSigner;

    public TlsSRPKeyExchange(int n2, Vector vector, TlsSRPGroupVerifier tlsSRPGroupVerifier, byte[] arrby, byte[] arrby2) {
        super(n2, vector);
        this.tlsSigner = TlsSRPKeyExchange.createSigner(n2);
        this.groupVerifier = tlsSRPGroupVerifier;
        this.identity = arrby;
        this.password = arrby2;
        this.srpClient = new SRP6Client();
    }

    public TlsSRPKeyExchange(int n2, Vector vector, byte[] arrby, TlsSRPLoginParameters tlsSRPLoginParameters) {
        super(n2, vector);
        this.tlsSigner = TlsSRPKeyExchange.createSigner(n2);
        this.identity = arrby;
        this.srpServer = new SRP6Server();
        this.srpGroup = tlsSRPLoginParameters.getGroup();
        this.srpVerifier = tlsSRPLoginParameters.getVerifier();
        this.srpSalt = tlsSRPLoginParameters.getSalt();
    }

    public TlsSRPKeyExchange(int n2, Vector vector, byte[] arrby, byte[] arrby2) {
        this(n2, vector, new DefaultTlsSRPGroupVerifier(), arrby, arrby2);
    }

    protected static TlsSigner createSigner(int n2) {
        switch (n2) {
            default: {
                throw new IllegalArgumentException("unsupported key exchange algorithm");
            }
            case 21: {
                return null;
            }
            case 23: {
                return new TlsRSASigner();
            }
            case 22: 
        }
        return new TlsDSSSigner();
    }

    @Override
    public void generateClientKeyExchange(OutputStream outputStream) {
        TlsSRPUtils.writeSRPParameter(this.srpClient.generateClientCredentials(this.srpSalt, this.identity, this.password), outputStream);
        this.context.getSecurityParameters().srpIdentity = Arrays.clone((byte[])this.identity);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public byte[] generatePremasterSecret() {
        try {
            BigInteger bigInteger;
            BigInteger bigInteger2;
            if (this.srpServer != null) {
                bigInteger2 = this.srpServer.calculateSecret(this.srpPeerCredentials);
                do {
                    return BigIntegers.asUnsignedByteArray((BigInteger)bigInteger2);
                    break;
                } while (true);
            }
            bigInteger2 = bigInteger = this.srpClient.calculateSecret(this.srpPeerCredentials);
            return BigIntegers.asUnsignedByteArray((BigInteger)bigInteger2);
        }
        catch (CryptoException cryptoException) {
            throw new TlsFatalAlert(47, (Throwable)((Object)cryptoException));
        }
    }

    @Override
    public byte[] generateServerKeyExchange() {
        this.srpServer.init(this.srpGroup, this.srpVerifier, TlsUtils.createHash((short)2), this.context.getSecureRandom());
        BigInteger bigInteger = this.srpServer.generateServerCredentials();
        ServerSRPParams serverSRPParams = new ServerSRPParams(this.srpGroup.getN(), this.srpGroup.getG(), this.srpSalt, bigInteger);
        DigestInputBuffer digestInputBuffer = new DigestInputBuffer();
        serverSRPParams.encode((OutputStream)digestInputBuffer);
        if (this.serverCredentials != null) {
            SignatureAndHashAlgorithm signatureAndHashAlgorithm = TlsUtils.getSignatureAndHashAlgorithm(this.context, this.serverCredentials);
            Digest digest = TlsUtils.createHash(signatureAndHashAlgorithm);
            SecurityParameters securityParameters = this.context.getSecurityParameters();
            digest.update(securityParameters.clientRandom, 0, securityParameters.clientRandom.length);
            digest.update(securityParameters.serverRandom, 0, securityParameters.serverRandom.length);
            digestInputBuffer.updateDigest(digest);
            byte[] arrby = new byte[digest.getDigestSize()];
            digest.doFinal(arrby, 0);
            new DigitallySigned(signatureAndHashAlgorithm, this.serverCredentials.generateCertificateSignature(arrby)).encode((OutputStream)digestInputBuffer);
        }
        return digestInputBuffer.toByteArray();
    }

    @Override
    public void init(TlsContext tlsContext) {
        super.init(tlsContext);
        if (this.tlsSigner != null) {
            this.tlsSigner.init(tlsContext);
        }
    }

    protected Signer initVerifyer(TlsSigner tlsSigner, SignatureAndHashAlgorithm signatureAndHashAlgorithm, SecurityParameters securityParameters) {
        Signer signer = tlsSigner.createVerifyer(signatureAndHashAlgorithm, this.serverPublicKey);
        signer.update(securityParameters.clientRandom, 0, securityParameters.clientRandom.length);
        signer.update(securityParameters.serverRandom, 0, securityParameters.serverRandom.length);
        return signer;
    }

    @Override
    public void processClientCredentials(TlsCredentials tlsCredentials) {
        throw new TlsFatalAlert(80);
    }

    @Override
    public void processClientKeyExchange(InputStream inputStream) {
        try {
            this.srpPeerCredentials = SRP6Util.validatePublicValue(this.srpGroup.getN(), TlsSRPUtils.readSRPParameter(inputStream));
        }
        catch (CryptoException cryptoException) {
            throw new TlsFatalAlert(47, (Throwable)((Object)cryptoException));
        }
        this.context.getSecurityParameters().srpIdentity = Arrays.clone((byte[])this.identity);
    }

    @Override
    public void processServerCertificate(Certificate certificate) {
        if (this.tlsSigner == null) {
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
        if (!this.tlsSigner.isValidPublicKey(this.serverPublicKey)) {
            throw new TlsFatalAlert(46);
        }
        TlsUtils.validateKeyUsage(certificate2, 128);
        super.processServerCertificate(certificate);
    }

    @Override
    public void processServerCredentials(TlsCredentials tlsCredentials) {
        if (this.keyExchange == 21 || !(tlsCredentials instanceof TlsSignerCredentials)) {
            throw new TlsFatalAlert(80);
        }
        this.processServerCertificate(tlsCredentials.getCertificate());
        this.serverCredentials = (TlsSignerCredentials)tlsCredentials;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void processServerKeyExchange(InputStream inputStream) {
        InputStream inputStream2;
        SignerInputBuffer signerInputBuffer;
        SecurityParameters securityParameters = this.context.getSecurityParameters();
        if (this.tlsSigner != null) {
            signerInputBuffer = new SignerInputBuffer();
            inputStream2 = new TeeInputStream(inputStream, (OutputStream)signerInputBuffer);
        } else {
            inputStream2 = inputStream;
            signerInputBuffer = null;
        }
        ServerSRPParams serverSRPParams = ServerSRPParams.parse(inputStream2);
        if (signerInputBuffer != null) {
            DigitallySigned digitallySigned = DigitallySigned.parse(this.context, inputStream);
            Signer signer = this.initVerifyer(this.tlsSigner, digitallySigned.getAlgorithm(), securityParameters);
            signerInputBuffer.updateSigner(signer);
            if (!signer.verifySignature(digitallySigned.getSignature())) {
                throw new TlsFatalAlert(51);
            }
        }
        this.srpGroup = new SRP6GroupParameters(serverSRPParams.getN(), serverSRPParams.getG());
        if (!this.groupVerifier.accept(this.srpGroup)) {
            throw new TlsFatalAlert(71);
        }
        this.srpSalt = serverSRPParams.getS();
        try {
            this.srpPeerCredentials = SRP6Util.validatePublicValue(this.srpGroup.getN(), serverSRPParams.getB());
        }
        catch (CryptoException cryptoException) {
            throw new TlsFatalAlert(47, (Throwable)((Object)cryptoException));
        }
        this.srpClient.init(this.srpGroup, TlsUtils.createHash((short)2), this.context.getSecureRandom());
    }

    @Override
    public boolean requiresServerKeyExchange() {
        return true;
    }

    @Override
    public void skipServerCredentials() {
        if (this.tlsSigner != null) {
            throw new TlsFatalAlert(10);
        }
    }

    @Override
    public void validateCertificateRequest(CertificateRequest certificateRequest) {
        throw new TlsFatalAlert(10);
    }
}

