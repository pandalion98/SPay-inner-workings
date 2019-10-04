/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayInputStream
 *  java.io.InputStream
 *  java.io.OutputStream
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.security.SecureRandom
 *  java.util.Enumeration
 *  java.util.Hashtable
 *  java.util.Vector
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.crypto.tls;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle.crypto.prng.RandomGenerator;
import org.bouncycastle.crypto.tls.AbstractTlsContext;
import org.bouncycastle.crypto.tls.Certificate;
import org.bouncycastle.crypto.tls.CertificateRequest;
import org.bouncycastle.crypto.tls.CertificateStatus;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.crypto.tls.DigitallySigned;
import org.bouncycastle.crypto.tls.NewSessionTicket;
import org.bouncycastle.crypto.tls.ProtocolVersion;
import org.bouncycastle.crypto.tls.RecordStream;
import org.bouncycastle.crypto.tls.SecurityParameters;
import org.bouncycastle.crypto.tls.SessionParameters;
import org.bouncycastle.crypto.tls.SignatureAndHashAlgorithm;
import org.bouncycastle.crypto.tls.TlsAuthentication;
import org.bouncycastle.crypto.tls.TlsCipher;
import org.bouncycastle.crypto.tls.TlsClient;
import org.bouncycastle.crypto.tls.TlsClientContext;
import org.bouncycastle.crypto.tls.TlsClientContextImpl;
import org.bouncycastle.crypto.tls.TlsCompression;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsCredentials;
import org.bouncycastle.crypto.tls.TlsExtensionsUtils;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsHandshakeHash;
import org.bouncycastle.crypto.tls.TlsKeyExchange;
import org.bouncycastle.crypto.tls.TlsPeer;
import org.bouncycastle.crypto.tls.TlsProtocol;
import org.bouncycastle.crypto.tls.TlsSession;
import org.bouncycastle.crypto.tls.TlsSessionImpl;
import org.bouncycastle.crypto.tls.TlsSignerCredentials;
import org.bouncycastle.crypto.tls.TlsUtils;
import org.bouncycastle.util.Arrays;

public class TlsClientProtocol
extends TlsProtocol {
    protected TlsAuthentication authentication = null;
    protected CertificateRequest certificateRequest = null;
    protected CertificateStatus certificateStatus = null;
    protected TlsKeyExchange keyExchange = null;
    protected byte[] selectedSessionID = null;
    protected TlsClient tlsClient = null;
    TlsClientContextImpl tlsClientContext = null;

    public TlsClientProtocol(InputStream inputStream, OutputStream outputStream, SecureRandom secureRandom) {
        super(inputStream, outputStream, secureRandom);
    }

    @Override
    protected void cleanupHandshake() {
        super.cleanupHandshake();
        this.selectedSessionID = null;
        this.keyExchange = null;
        this.authentication = null;
        this.certificateStatus = null;
        this.certificateRequest = null;
    }

    public void connect(TlsClient tlsClient) {
        SessionParameters sessionParameters;
        if (tlsClient == null) {
            throw new IllegalArgumentException("'tlsClient' cannot be null");
        }
        if (this.tlsClient != null) {
            throw new IllegalStateException("'connect' can only be called once");
        }
        this.tlsClient = tlsClient;
        this.securityParameters = new SecurityParameters();
        this.securityParameters.entity = 1;
        this.tlsClientContext = new TlsClientContextImpl(this.secureRandom, this.securityParameters);
        this.securityParameters.clientRandom = TlsClientProtocol.createRandomBlock(tlsClient.shouldUseGMTUnixTime(), this.tlsClientContext.getNonceRandomGenerator());
        this.tlsClient.init(this.tlsClientContext);
        this.recordStream.init(this.tlsClientContext);
        TlsSession tlsSession = tlsClient.getSessionToResume();
        if (tlsSession != null && (sessionParameters = tlsSession.exportSessionParameters()) != null) {
            this.tlsSession = tlsSession;
            this.sessionParameters = sessionParameters;
        }
        this.sendClientHelloMessage();
        this.connection_state = 1;
        this.completeHandshake();
    }

    @Override
    protected TlsContext getContext() {
        return this.tlsClientContext;
    }

    @Override
    AbstractTlsContext getContextAdmin() {
        return this.tlsClientContext;
    }

    @Override
    protected TlsPeer getPeer() {
        return this.tlsClient;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    protected void handleHandshakeMessage(short s2, byte[] arrby) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arrby);
        if (this.resumedSession) {
            if (s2 != 20 || this.connection_state != 2) {
                throw new TlsFatalAlert(10);
            }
            this.processFinishedMessage(byteArrayInputStream);
            this.connection_state = (short)15;
            this.sendFinishedMessage();
            this.connection_state = (short)13;
            this.connection_state = (short)16;
            return;
        }
        switch (s2) {
            default: {
                throw new TlsFatalAlert(10);
            }
            case 11: {
                switch (this.connection_state) {
                    default: {
                        throw new TlsFatalAlert(10);
                    }
                    case 2: {
                        this.handleSupplementalData(null);
                    }
                    case 3: 
                }
                this.peerCertificate = Certificate.parse((InputStream)byteArrayInputStream);
                TlsClientProtocol.assertEmpty(byteArrayInputStream);
                if (this.peerCertificate == null || this.peerCertificate.isEmpty()) {
                    this.allowCertificateStatus = false;
                }
                this.keyExchange.processServerCertificate(this.peerCertificate);
                this.authentication = this.tlsClient.getAuthentication();
                this.authentication.notifyServerCertificate(this.peerCertificate);
                this.connection_state = (short)4;
                return;
            }
            case 22: {
                switch (this.connection_state) {
                    default: {
                        throw new TlsFatalAlert(10);
                    }
                    case 4: 
                }
                if (!this.allowCertificateStatus) {
                    throw new TlsFatalAlert(10);
                }
                this.certificateStatus = CertificateStatus.parse((InputStream)byteArrayInputStream);
                TlsClientProtocol.assertEmpty(byteArrayInputStream);
                this.connection_state = (short)5;
                return;
            }
            case 20: {
                switch (this.connection_state) {
                    default: {
                        throw new TlsFatalAlert(10);
                    }
                    case 13: {
                        if (!this.expectSessionTicket) break;
                        throw new TlsFatalAlert(10);
                    }
                    case 14: 
                }
                this.processFinishedMessage(byteArrayInputStream);
                this.connection_state = (short)15;
                this.connection_state = (short)16;
                return;
            }
            case 2: {
                switch (this.connection_state) {
                    default: {
                        throw new TlsFatalAlert(10);
                    }
                    case 1: 
                }
                this.receiveServerHelloMessage(byteArrayInputStream);
                this.connection_state = (short)2;
                if (this.securityParameters.maxFragmentLength >= 0) {
                    int n2 = 1 << 8 + this.securityParameters.maxFragmentLength;
                    this.recordStream.setPlaintextLimit(n2);
                }
                this.securityParameters.prfAlgorithm = TlsClientProtocol.getPRFAlgorithm(this.getContext(), this.securityParameters.getCipherSuite());
                this.securityParameters.verifyDataLength = 12;
                this.recordStream.notifyHelloComplete();
                if (this.resumedSession) {
                    this.securityParameters.masterSecret = Arrays.clone((byte[])this.sessionParameters.getMasterSecret());
                    this.recordStream.setPendingConnectionState(this.getPeer().getCompression(), this.getPeer().getCipher());
                    this.sendChangeCipherSpecMessage();
                    return;
                }
                this.invalidateSession();
                if (this.selectedSessionID.length <= 0) return;
                this.tlsSession = new TlsSessionImpl(this.selectedSessionID, null);
                return;
            }
            case 23: {
                switch (this.connection_state) {
                    default: {
                        throw new TlsFatalAlert(10);
                    }
                    case 2: 
                }
                this.handleSupplementalData(TlsClientProtocol.readSupplementalDataMessage(byteArrayInputStream));
                return;
            }
            case 14: {
                TlsCredentials tlsCredentials;
                switch (this.connection_state) {
                    default: {
                        throw new TlsFatalAlert(40);
                    }
                    case 2: {
                        this.handleSupplementalData(null);
                    }
                    case 3: {
                        this.keyExchange.skipServerCredentials();
                        this.authentication = null;
                    }
                    case 4: 
                    case 5: {
                        this.keyExchange.skipServerKeyExchange();
                    }
                    case 6: 
                    case 7: 
                }
                TlsClientProtocol.assertEmpty(byteArrayInputStream);
                this.connection_state = (short)8;
                this.recordStream.getHandshakeHash().sealHashAlgorithms();
                Vector vector = this.tlsClient.getClientSupplementalData();
                if (vector != null) {
                    this.sendSupplementalDataMessage(vector);
                }
                this.connection_state = (short)9;
                if (this.certificateRequest == null) {
                    this.keyExchange.skipClientCredentials();
                    tlsCredentials = null;
                } else {
                    tlsCredentials = this.authentication.getClientCredentials(this.certificateRequest);
                    if (tlsCredentials == null) {
                        this.keyExchange.skipClientCredentials();
                        this.sendCertificateMessage(Certificate.EMPTY_CHAIN);
                    } else {
                        this.keyExchange.processClientCredentials(tlsCredentials);
                        this.sendCertificateMessage(tlsCredentials.getCertificate());
                    }
                }
                this.connection_state = (short)10;
                this.sendClientKeyExchangeMessage();
                this.connection_state = (short)11;
                TlsHandshakeHash tlsHandshakeHash = this.recordStream.prepareToFinish();
                this.securityParameters.sessionHash = TlsClientProtocol.getCurrentPRFHash(this.getContext(), tlsHandshakeHash, null);
                TlsClientProtocol.establishMasterSecret(this.getContext(), this.keyExchange);
                this.recordStream.setPendingConnectionState(this.getPeer().getCompression(), this.getPeer().getCipher());
                if (tlsCredentials != null && tlsCredentials instanceof TlsSignerCredentials) {
                    TlsSignerCredentials tlsSignerCredentials = (TlsSignerCredentials)tlsCredentials;
                    SignatureAndHashAlgorithm signatureAndHashAlgorithm = TlsUtils.getSignatureAndHashAlgorithm(this.getContext(), tlsSignerCredentials);
                    byte[] arrby2 = signatureAndHashAlgorithm == null ? this.securityParameters.getSessionHash() : tlsHandshakeHash.getFinalHash(signatureAndHashAlgorithm.getHash());
                    this.sendCertificateVerifyMessage(new DigitallySigned(signatureAndHashAlgorithm, tlsSignerCredentials.generateCertificateSignature(arrby2)));
                    this.connection_state = (short)12;
                }
                this.sendChangeCipherSpecMessage();
                this.sendFinishedMessage();
                this.connection_state = (short)13;
                return;
            }
            case 12: {
                switch (this.connection_state) {
                    default: {
                        throw new TlsFatalAlert(10);
                    }
                    case 2: {
                        this.handleSupplementalData(null);
                    }
                    case 3: {
                        this.keyExchange.skipServerCredentials();
                        this.authentication = null;
                    }
                    case 4: 
                    case 5: 
                }
                this.keyExchange.processServerKeyExchange((InputStream)byteArrayInputStream);
                TlsClientProtocol.assertEmpty(byteArrayInputStream);
                this.connection_state = (short)6;
                return;
            }
            case 13: {
                switch (this.connection_state) {
                    default: {
                        throw new TlsFatalAlert(10);
                    }
                    case 4: 
                    case 5: {
                        this.keyExchange.skipServerKeyExchange();
                    }
                    case 6: 
                }
                if (this.authentication == null) {
                    throw new TlsFatalAlert(40);
                }
                this.certificateRequest = CertificateRequest.parse(this.getContext(), (InputStream)byteArrayInputStream);
                TlsClientProtocol.assertEmpty(byteArrayInputStream);
                this.keyExchange.validateCertificateRequest(this.certificateRequest);
                TlsUtils.trackHashAlgorithms(this.recordStream.getHandshakeHash(), this.certificateRequest.getSupportedSignatureAlgorithms());
                this.connection_state = (short)7;
                return;
            }
            case 4: {
                switch (this.connection_state) {
                    default: {
                        throw new TlsFatalAlert(10);
                    }
                    case 13: 
                }
                if (!this.expectSessionTicket) {
                    throw new TlsFatalAlert(10);
                }
                this.invalidateSession();
                this.receiveNewSessionTicketMessage(byteArrayInputStream);
                this.connection_state = (short)14;
                return;
            }
            case 0: {
                TlsClientProtocol.assertEmpty(byteArrayInputStream);
                if (this.connection_state != 16) return;
                if (TlsUtils.isSSL(this.getContext())) {
                    throw new TlsFatalAlert(40);
                } else {
                    break;
                }
            }
        }
        this.raiseWarning((short)100, "Renegotiation not supported");
    }

    protected void handleSupplementalData(Vector vector) {
        this.tlsClient.processServerSupplementalData(vector);
        this.connection_state = (short)3;
        this.keyExchange = this.tlsClient.getKeyExchange();
        this.keyExchange.init(this.getContext());
    }

    protected void receiveNewSessionTicketMessage(ByteArrayInputStream byteArrayInputStream) {
        NewSessionTicket newSessionTicket = NewSessionTicket.parse((InputStream)byteArrayInputStream);
        TlsClientProtocol.assertEmpty(byteArrayInputStream);
        this.tlsClient.notifyNewSessionTicket(newSessionTicket);
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void receiveServerHelloMessage(ByteArrayInputStream byteArrayInputStream) {
        byte[] arrby;
        boolean bl = true;
        ProtocolVersion protocolVersion = TlsUtils.readVersion((InputStream)byteArrayInputStream);
        if (protocolVersion.isDTLS()) {
            throw new TlsFatalAlert(47);
        }
        if (!protocolVersion.equals(this.recordStream.getReadVersion())) {
            throw new TlsFatalAlert(47);
        }
        if (!protocolVersion.isEqualOrEarlierVersionOf(this.getContext().getClientVersion())) {
            throw new TlsFatalAlert(47);
        }
        this.recordStream.setWriteVersion(protocolVersion);
        this.getContextAdmin().setServerVersion(protocolVersion);
        this.tlsClient.notifyServerVersion(protocolVersion);
        this.securityParameters.serverRandom = TlsUtils.readFully(32, (InputStream)byteArrayInputStream);
        this.selectedSessionID = TlsUtils.readOpaque8((InputStream)byteArrayInputStream);
        if (this.selectedSessionID.length > 32) {
            throw new TlsFatalAlert(47);
        }
        this.tlsClient.notifySessionID(this.selectedSessionID);
        boolean bl2 = this.selectedSessionID.length > 0 && this.tlsSession != null && Arrays.areEqual((byte[])this.selectedSessionID, (byte[])this.tlsSession.getSessionID()) ? bl : false;
        this.resumedSession = bl2;
        int n2 = TlsUtils.readUint16((InputStream)byteArrayInputStream);
        if (!Arrays.contains((int[])this.offeredCipherSuites, (int)n2) || n2 == 0 || CipherSuite.isSCSV(n2) || !TlsUtils.isValidCipherSuiteForVersion(n2, protocolVersion)) {
            throw new TlsFatalAlert(47);
        }
        this.tlsClient.notifySelectedCipherSuite(n2);
        short s2 = TlsUtils.readUint8((InputStream)byteArrayInputStream);
        if (!Arrays.contains((short[])this.offeredCompressionMethods, (short)s2)) {
            throw new TlsFatalAlert(47);
        }
        this.tlsClient.notifySelectedCompressionMethod(s2);
        this.serverExtensions = TlsClientProtocol.readExtensions(byteArrayInputStream);
        if (TlsExtensionsUtils.hasExtendedMasterSecretExtension(this.serverExtensions) != this.securityParameters.extendedMasterSecret) {
            throw new TlsFatalAlert(40);
        }
        if (this.serverExtensions != null) {
            Enumeration enumeration = this.serverExtensions.keys();
            while (enumeration.hasMoreElements()) {
                Integer n3 = (Integer)enumeration.nextElement();
                if (n3.equals((Object)EXT_RenegotiationInfo)) continue;
                if (TlsUtils.getExtensionData(this.clientExtensions, n3) == null) {
                    throw new TlsFatalAlert(110);
                }
                if (n3.equals((Object)TlsExtensionsUtils.EXT_extended_master_secret) || !this.resumedSession) continue;
            }
        }
        if ((arrby = TlsUtils.getExtensionData(this.serverExtensions, EXT_RenegotiationInfo)) != null) {
            this.secure_renegotiation = bl;
            if (!Arrays.constantTimeAreEqual((byte[])arrby, (byte[])TlsClientProtocol.createRenegotiationInfo(TlsUtils.EMPTY_BYTES))) {
                throw new TlsFatalAlert(40);
            }
        }
        this.tlsClient.notifySecureRenegotiation(this.secure_renegotiation);
        Hashtable hashtable = this.clientExtensions;
        Hashtable hashtable2 = this.serverExtensions;
        if (this.resumedSession) {
            if (n2 != this.sessionParameters.getCipherSuite() || s2 != this.sessionParameters.getCompressionAlgorithm()) {
                throw new TlsFatalAlert(47);
            }
            hashtable = null;
            hashtable2 = this.sessionParameters.readServerExtensions();
            this.securityParameters.extendedMasterSecret = TlsExtensionsUtils.hasExtendedMasterSecretExtension(hashtable2);
        }
        this.securityParameters.cipherSuite = n2;
        this.securityParameters.compressionAlgorithm = s2;
        if (hashtable2 != null) {
            boolean bl3 = TlsExtensionsUtils.hasEncryptThenMACExtension(hashtable2);
            if (bl3 && !TlsUtils.isBlockCipherSuite(n2)) {
                throw new TlsFatalAlert(47);
            }
            this.securityParameters.encryptThenMAC = bl3;
            this.securityParameters.maxFragmentLength = this.processMaxFragmentLengthExtension(hashtable, hashtable2, (short)47);
            this.securityParameters.truncatedHMac = TlsExtensionsUtils.hasTruncatedHMacExtension(hashtable2);
            boolean bl4 = !this.resumedSession && TlsUtils.hasExpectedEmptyExtensionData(hashtable2, TlsExtensionsUtils.EXT_status_request, (short)47) ? bl : false;
            this.allowCertificateStatus = bl4;
            if (this.resumedSession || !TlsUtils.hasExpectedEmptyExtensionData(hashtable2, TlsProtocol.EXT_SessionTicket, (short)47)) {
                bl = false;
            }
            this.expectSessionTicket = bl;
        }
        if (hashtable != null) {
            this.tlsClient.processServerExtensions(hashtable2);
        }
    }

    protected void sendCertificateVerifyMessage(DigitallySigned digitallySigned) {
        TlsProtocol.HandshakeMessage handshakeMessage = new TlsProtocol.HandshakeMessage(15);
        digitallySigned.encode((OutputStream)handshakeMessage);
        handshakeMessage.writeToRecordStream();
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void sendClientHelloMessage() {
        short s2 = 1;
        this.recordStream.setWriteVersion(this.tlsClient.getClientHelloRecordLayerVersion());
        ProtocolVersion protocolVersion = this.tlsClient.getClientVersion();
        if (protocolVersion.isDTLS()) {
            throw new TlsFatalAlert(80);
        }
        this.getContextAdmin().setClientVersion(protocolVersion);
        byte[] arrby = TlsUtils.EMPTY_BYTES;
        if (this.tlsSession != null && ((arrby = this.tlsSession.getSessionID()) == null || arrby.length > 32)) {
            arrby = TlsUtils.EMPTY_BYTES;
        }
        boolean bl = this.tlsClient.isFallback();
        this.offeredCipherSuites = this.tlsClient.getCipherSuites();
        this.offeredCompressionMethods = this.tlsClient.getCompressionMethods();
        if (!(arrby.length <= 0 || this.sessionParameters == null || Arrays.contains((int[])this.offeredCipherSuites, (int)this.sessionParameters.getCipherSuite()) && Arrays.contains((short[])this.offeredCompressionMethods, (short)this.sessionParameters.getCompressionAlgorithm()))) {
            arrby = TlsUtils.EMPTY_BYTES;
        }
        this.clientExtensions = this.tlsClient.getClientExtensions();
        this.securityParameters.extendedMasterSecret = TlsExtensionsUtils.hasExtendedMasterSecretExtension(this.clientExtensions);
        TlsProtocol.HandshakeMessage handshakeMessage = new TlsProtocol.HandshakeMessage(s2);
        TlsUtils.writeVersion(protocolVersion, (OutputStream)handshakeMessage);
        handshakeMessage.write(this.securityParameters.getClientRandom());
        TlsUtils.writeOpaque8(arrby, (OutputStream)handshakeMessage);
        short s3 = TlsUtils.getExtensionData(this.clientExtensions, EXT_RenegotiationInfo) == null ? s2 : (short)0;
        if (Arrays.contains((int[])this.offeredCipherSuites, (int)255)) {
            s2 = 0;
        }
        if (s3 != 0 && s2 != 0) {
            this.offeredCipherSuites = Arrays.append((int[])this.offeredCipherSuites, (int)255);
        }
        if (bl && !Arrays.contains((int[])this.offeredCipherSuites, (int)22016)) {
            this.offeredCipherSuites = Arrays.append((int[])this.offeredCipherSuites, (int)22016);
        }
        TlsUtils.writeUint16ArrayWithUint16Length(this.offeredCipherSuites, (OutputStream)handshakeMessage);
        TlsUtils.writeUint8ArrayWithUint8Length(this.offeredCompressionMethods, (OutputStream)handshakeMessage);
        if (this.clientExtensions != null) {
            TlsClientProtocol.writeExtensions((OutputStream)handshakeMessage, this.clientExtensions);
        }
        handshakeMessage.writeToRecordStream();
    }

    protected void sendClientKeyExchangeMessage() {
        TlsProtocol.HandshakeMessage handshakeMessage = new TlsProtocol.HandshakeMessage(16);
        this.keyExchange.generateClientKeyExchange((OutputStream)handshakeMessage);
        handshakeMessage.writeToRecordStream();
    }
}

