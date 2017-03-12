package org.bouncycastle.crypto.tls;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.prng.RandomGenerator;
import org.bouncycastle.crypto.tls.SessionParameters.Builder;
import org.bouncycastle.jcajce.spec.SkeinParameterSpec;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Integers;

public abstract class TlsProtocol {
    protected static final short CS_CERTIFICATE_REQUEST = (short) 7;
    protected static final short CS_CERTIFICATE_STATUS = (short) 5;
    protected static final short CS_CERTIFICATE_VERIFY = (short) 12;
    protected static final short CS_CLIENT_CERTIFICATE = (short) 10;
    protected static final short CS_CLIENT_FINISHED = (short) 13;
    protected static final short CS_CLIENT_HELLO = (short) 1;
    protected static final short CS_CLIENT_KEY_EXCHANGE = (short) 11;
    protected static final short CS_CLIENT_SUPPLEMENTAL_DATA = (short) 9;
    protected static final short CS_END = (short) 16;
    protected static final short CS_SERVER_CERTIFICATE = (short) 4;
    protected static final short CS_SERVER_FINISHED = (short) 15;
    protected static final short CS_SERVER_HELLO = (short) 2;
    protected static final short CS_SERVER_HELLO_DONE = (short) 8;
    protected static final short CS_SERVER_KEY_EXCHANGE = (short) 6;
    protected static final short CS_SERVER_SESSION_TICKET = (short) 14;
    protected static final short CS_SERVER_SUPPLEMENTAL_DATA = (short) 3;
    protected static final short CS_START = (short) 0;
    protected static final Integer EXT_RenegotiationInfo;
    protected static final Integer EXT_SessionTicket;
    private static final String TLS_ERROR_MESSAGE = "Internal TLS error, this could be an attack";
    private ByteQueue alertQueue;
    protected boolean allowCertificateStatus;
    private volatile boolean appDataReady;
    private ByteQueue applicationDataQueue;
    protected Hashtable clientExtensions;
    private volatile boolean closed;
    protected short connection_state;
    protected boolean expectSessionTicket;
    private byte[] expected_verify_data;
    private volatile boolean failedWithError;
    private ByteQueue handshakeQueue;
    protected int[] offeredCipherSuites;
    protected short[] offeredCompressionMethods;
    protected Certificate peerCertificate;
    protected boolean receivedChangeCipherSpec;
    RecordStream recordStream;
    protected boolean resumedSession;
    protected SecureRandom secureRandom;
    protected boolean secure_renegotiation;
    protected SecurityParameters securityParameters;
    protected Hashtable serverExtensions;
    protected SessionParameters sessionParameters;
    private volatile boolean splitApplicationDataRecords;
    private TlsInputStream tlsInputStream;
    private TlsOutputStream tlsOutputStream;
    protected TlsSession tlsSession;

    class HandshakeMessage extends ByteArrayOutputStream {
        HandshakeMessage(TlsProtocol tlsProtocol, short s) {
            this(s, 60);
        }

        HandshakeMessage(short s, int i) {
            super(i + 4);
            TlsUtils.writeUint8(s, (OutputStream) this);
            this.count += 3;
        }

        void writeToRecordStream() {
            int i = this.count - 4;
            TlsUtils.checkUint24(i);
            TlsUtils.writeUint24(i, this.buf, 1);
            TlsProtocol.this.writeHandshakeMessage(this.buf, 0, this.count);
            this.buf = null;
        }
    }

    static {
        EXT_RenegotiationInfo = Integers.valueOf(NamedCurve.arbitrary_explicit_prime_curves);
        EXT_SessionTicket = Integers.valueOf(35);
    }

    public TlsProtocol(InputStream inputStream, OutputStream outputStream, SecureRandom secureRandom) {
        this.applicationDataQueue = new ByteQueue();
        this.alertQueue = new ByteQueue(2);
        this.handshakeQueue = new ByteQueue();
        this.tlsInputStream = null;
        this.tlsOutputStream = null;
        this.closed = false;
        this.failedWithError = false;
        this.appDataReady = false;
        this.splitApplicationDataRecords = true;
        this.expected_verify_data = null;
        this.tlsSession = null;
        this.sessionParameters = null;
        this.securityParameters = null;
        this.peerCertificate = null;
        this.offeredCipherSuites = null;
        this.offeredCompressionMethods = null;
        this.clientExtensions = null;
        this.serverExtensions = null;
        this.connection_state = CS_START;
        this.resumedSession = false;
        this.receivedChangeCipherSpec = false;
        this.secure_renegotiation = false;
        this.allowCertificateStatus = false;
        this.expectSessionTicket = false;
        this.recordStream = new RecordStream(this, inputStream, outputStream);
        this.secureRandom = secureRandom;
    }

    protected static void assertEmpty(ByteArrayInputStream byteArrayInputStream) {
        if (byteArrayInputStream.available() > 0) {
            throw new TlsFatalAlert((short) 50);
        }
    }

    protected static byte[] createRandomBlock(boolean z, RandomGenerator randomGenerator) {
        byte[] bArr = new byte[32];
        randomGenerator.nextBytes(bArr);
        if (z) {
            TlsUtils.writeGMTUnixTime(bArr, 0);
        }
        return bArr;
    }

    protected static byte[] createRenegotiationInfo(byte[] bArr) {
        return TlsUtils.encodeOpaque8(bArr);
    }

    protected static void establishMasterSecret(TlsContext tlsContext, TlsKeyExchange tlsKeyExchange) {
        byte[] generatePremasterSecret = tlsKeyExchange.generatePremasterSecret();
        try {
            tlsContext.getSecurityParameters().masterSecret = TlsUtils.calculateMasterSecret(tlsContext, generatePremasterSecret);
            if (generatePremasterSecret != null) {
                Arrays.fill(generatePremasterSecret, (byte) 0);
            }
        } catch (Throwable th) {
            if (generatePremasterSecret != null) {
                Arrays.fill(generatePremasterSecret, (byte) 0);
            }
        }
    }

    protected static byte[] getCurrentPRFHash(TlsContext tlsContext, TlsHandshakeHash tlsHandshakeHash, byte[] bArr) {
        Digest forkPRFHash = tlsHandshakeHash.forkPRFHash();
        if (bArr != null && TlsUtils.isSSL(tlsContext)) {
            forkPRFHash.update(bArr, 0, bArr.length);
        }
        byte[] bArr2 = new byte[forkPRFHash.getDigestSize()];
        forkPRFHash.doFinal(bArr2, 0);
        return bArr2;
    }

    protected static int getPRFAlgorithm(TlsContext tlsContext, int i) {
        boolean isTLSv12 = TlsUtils.isTLSv12(tlsContext);
        switch (i) {
            case CipherSuite.TLS_RSA_WITH_NULL_SHA256 /*59*/:
            case CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA256 /*60*/:
            case CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA256 /*61*/:
            case CipherSuite.TLS_DH_DSS_WITH_AES_128_CBC_SHA256 /*62*/:
            case SkeinParameterSpec.PARAM_TYPE_OUTPUT /*63*/:
            case X509KeyUsage.nonRepudiation /*64*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA256 /*103*/:
            case CipherSuite.TLS_DH_DSS_WITH_AES_256_CBC_SHA256 /*104*/:
            case CipherSuite.TLS_DH_RSA_WITH_AES_256_CBC_SHA256 /*105*/:
            case CipherSuite.TLS_DHE_DSS_WITH_AES_256_CBC_SHA256 /*106*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA256 /*107*/:
            case CipherSuite.TLS_RSA_WITH_AES_128_GCM_SHA256 /*156*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256 /*158*/:
            case CipherSuite.TLS_DH_RSA_WITH_AES_128_GCM_SHA256 /*160*/:
            case CipherSuite.TLS_DHE_DSS_WITH_AES_128_GCM_SHA256 /*162*/:
            case CipherSuite.TLS_DH_DSS_WITH_AES_128_GCM_SHA256 /*164*/:
            case CipherSuite.TLS_PSK_WITH_AES_128_GCM_SHA256 /*168*/:
            case CipherSuite.TLS_DHE_PSK_WITH_AES_128_GCM_SHA256 /*170*/:
            case CipherSuite.TLS_RSA_PSK_WITH_AES_128_GCM_SHA256 /*172*/:
            case CipherSuite.TLS_RSA_WITH_CAMELLIA_128_CBC_SHA256 /*186*/:
            case CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_128_CBC_SHA256 /*187*/:
            case CipherSuite.TLS_DH_RSA_WITH_CAMELLIA_128_CBC_SHA256 /*188*/:
            case CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_128_CBC_SHA256 /*189*/:
            case CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_128_CBC_SHA256 /*190*/:
            case CipherSuite.TLS_DH_anon_WITH_CAMELLIA_128_CBC_SHA256 /*191*/:
            case CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256 /*192*/:
            case CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_256_CBC_SHA256 /*193*/:
            case CipherSuite.TLS_DH_RSA_WITH_CAMELLIA_256_CBC_SHA256 /*194*/:
            case CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_256_CBC_SHA256 /*195*/:
            case CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_256_CBC_SHA256 /*196*/:
            case CipherSuite.TLS_DH_anon_WITH_CAMELLIA_256_CBC_SHA256 /*197*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256 /*49187*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA256 /*49189*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256 /*49191*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_AES_128_CBC_SHA256 /*49193*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256 /*49195*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_AES_128_GCM_SHA256 /*49197*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256 /*49199*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_AES_128_GCM_SHA256 /*49201*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_CAMELLIA_128_CBC_SHA256 /*49266*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_CAMELLIA_128_CBC_SHA256 /*49268*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_CAMELLIA_128_CBC_SHA256 /*49270*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_CAMELLIA_128_CBC_SHA256 /*49272*/:
            case CipherSuite.TLS_RSA_WITH_CAMELLIA_128_GCM_SHA256 /*49274*/:
            case CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_128_GCM_SHA256 /*49276*/:
            case CipherSuite.TLS_DH_RSA_WITH_CAMELLIA_128_GCM_SHA256 /*49278*/:
            case CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_128_GCM_SHA256 /*49280*/:
            case CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_128_GCM_SHA256 /*49282*/:
            case CipherSuite.TLS_DH_anon_WITH_CAMELLIA_128_GCM_SHA256 /*49284*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_CAMELLIA_128_GCM_SHA256 /*49286*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_CAMELLIA_128_GCM_SHA256 /*49288*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_CAMELLIA_128_GCM_SHA256 /*49290*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_CAMELLIA_128_GCM_SHA256 /*49292*/:
            case CipherSuite.TLS_PSK_WITH_CAMELLIA_128_GCM_SHA256 /*49294*/:
            case CipherSuite.TLS_DHE_PSK_WITH_CAMELLIA_128_GCM_SHA256 /*49296*/:
            case CipherSuite.TLS_RSA_PSK_WITH_CAMELLIA_128_GCM_SHA256 /*49298*/:
            case CipherSuite.TLS_RSA_WITH_AES_128_CCM /*49308*/:
            case CipherSuite.TLS_RSA_WITH_AES_256_CCM /*49309*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_128_CCM /*49310*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_256_CCM /*49311*/:
            case CipherSuite.TLS_RSA_WITH_AES_128_CCM_8 /*49312*/:
            case CipherSuite.TLS_RSA_WITH_AES_256_CCM_8 /*49313*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_128_CCM_8 /*49314*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_256_CCM_8 /*49315*/:
            case CipherSuite.TLS_PSK_WITH_AES_128_CCM /*49316*/:
            case CipherSuite.TLS_PSK_WITH_AES_256_CCM /*49317*/:
            case CipherSuite.TLS_DHE_PSK_WITH_AES_128_CCM /*49318*/:
            case CipherSuite.TLS_DHE_PSK_WITH_AES_256_CCM /*49319*/:
            case CipherSuite.TLS_PSK_WITH_AES_128_CCM_8 /*49320*/:
            case CipherSuite.TLS_PSK_WITH_AES_256_CCM_8 /*49321*/:
            case CipherSuite.TLS_PSK_DHE_WITH_AES_128_CCM_8 /*49322*/:
            case CipherSuite.TLS_PSK_DHE_WITH_AES_256_CCM_8 /*49323*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CCM /*49324*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CCM /*49325*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CCM_8 /*49326*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CCM_8 /*49327*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256 /*52243*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256 /*52244*/:
            case CipherSuite.TLS_DHE_RSA_WITH_CHACHA20_POLY1305_SHA256 /*52245*/:
                if (isTLSv12) {
                    return 1;
                }
                throw new TlsFatalAlert((short) 47);
            case CipherSuite.TLS_RSA_WITH_AES_256_GCM_SHA384 /*157*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_256_GCM_SHA384 /*159*/:
            case CipherSuite.TLS_DH_RSA_WITH_AES_256_GCM_SHA384 /*161*/:
            case CipherSuite.TLS_DHE_DSS_WITH_AES_256_GCM_SHA384 /*163*/:
            case CipherSuite.TLS_DH_DSS_WITH_AES_256_GCM_SHA384 /*165*/:
            case CipherSuite.TLS_PSK_WITH_AES_256_GCM_SHA384 /*169*/:
            case CipherSuite.TLS_DHE_PSK_WITH_AES_256_GCM_SHA384 /*171*/:
            case CipherSuite.TLS_RSA_PSK_WITH_AES_256_GCM_SHA384 /*173*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384 /*49188*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA384 /*49190*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384 /*49192*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_AES_256_CBC_SHA384 /*49194*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384 /*49196*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_AES_256_GCM_SHA384 /*49198*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384 /*49200*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_AES_256_GCM_SHA384 /*49202*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_CAMELLIA_256_CBC_SHA384 /*49267*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_CAMELLIA_256_CBC_SHA384 /*49269*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_CAMELLIA_256_CBC_SHA384 /*49271*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_CAMELLIA_256_CBC_SHA384 /*49273*/:
            case CipherSuite.TLS_RSA_WITH_CAMELLIA_256_GCM_SHA384 /*49275*/:
            case CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_256_GCM_SHA384 /*49277*/:
            case CipherSuite.TLS_DH_RSA_WITH_CAMELLIA_256_GCM_SHA384 /*49279*/:
            case CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_256_GCM_SHA384 /*49281*/:
            case CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_256_GCM_SHA384 /*49283*/:
            case CipherSuite.TLS_DH_anon_WITH_CAMELLIA_256_GCM_SHA384 /*49285*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_CAMELLIA_256_GCM_SHA384 /*49287*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_CAMELLIA_256_GCM_SHA384 /*49289*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_CAMELLIA_256_GCM_SHA384 /*49291*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_CAMELLIA_256_GCM_SHA384 /*49293*/:
            case CipherSuite.TLS_PSK_WITH_CAMELLIA_256_GCM_SHA384 /*49295*/:
            case CipherSuite.TLS_DHE_PSK_WITH_CAMELLIA_256_GCM_SHA384 /*49297*/:
            case CipherSuite.TLS_RSA_PSK_WITH_CAMELLIA_256_GCM_SHA384 /*49299*/:
                if (isTLSv12) {
                    return 2;
                }
                throw new TlsFatalAlert((short) 47);
            case CipherSuite.TLS_PSK_WITH_AES_256_CBC_SHA384 /*175*/:
            case CipherSuite.TLS_PSK_WITH_NULL_SHA384 /*177*/:
            case CipherSuite.TLS_DHE_PSK_WITH_AES_256_CBC_SHA384 /*179*/:
            case CipherSuite.TLS_DHE_PSK_WITH_NULL_SHA384 /*181*/:
            case CipherSuite.TLS_RSA_PSK_WITH_AES_256_CBC_SHA384 /*183*/:
            case CipherSuite.TLS_RSA_PSK_WITH_NULL_SHA384 /*185*/:
            case CipherSuite.TLS_ECDHE_PSK_WITH_AES_256_CBC_SHA384 /*49208*/:
            case CipherSuite.TLS_ECDHE_PSK_WITH_NULL_SHA384 /*49211*/:
            case CipherSuite.TLS_PSK_WITH_CAMELLIA_256_CBC_SHA384 /*49301*/:
            case CipherSuite.TLS_DHE_PSK_WITH_CAMELLIA_256_CBC_SHA384 /*49303*/:
            case CipherSuite.TLS_RSA_PSK_WITH_CAMELLIA_256_CBC_SHA384 /*49305*/:
            case CipherSuite.TLS_ECDHE_PSK_WITH_CAMELLIA_256_CBC_SHA384 /*49307*/:
                return isTLSv12 ? 2 : 0;
            default:
                return isTLSv12 ? 1 : 0;
        }
    }

    private void processAlert() {
        while (this.alertQueue.available() >= 2) {
            byte[] removeData = this.alertQueue.removeData(2, 0);
            short s = (short) removeData[0];
            short s2 = (short) removeData[1];
            getPeer().notifyAlertReceived(s, s2);
            if (s == CS_SERVER_HELLO) {
                invalidateSession();
                this.failedWithError = true;
                this.closed = true;
                this.recordStream.safeClose();
                throw new IOException(TLS_ERROR_MESSAGE);
            }
            if (s2 == (short) 0) {
                handleClose(false);
            }
            handleWarningMessage(s2);
        }
    }

    private void processApplicationData() {
    }

    private void processChangeCipherSpec(byte[] bArr, int i, int i2) {
        int i3 = 0;
        while (i3 < i2) {
            if (TlsUtils.readUint8(bArr, i + i3) != CS_CLIENT_HELLO) {
                throw new TlsFatalAlert((short) 50);
            } else if (this.receivedChangeCipherSpec || this.alertQueue.available() > 0 || this.handshakeQueue.available() > 0) {
                throw new TlsFatalAlert(CS_CLIENT_CERTIFICATE);
            } else {
                this.recordStream.receivedReadCipherSpec();
                this.receivedChangeCipherSpec = true;
                handleChangeCipherSpecMessage();
                i3++;
            }
        }
    }

    private void processHandshake() {
        int i;
        do {
            if (this.handshakeQueue.available() >= 4) {
                byte[] bArr = new byte[4];
                this.handshakeQueue.read(bArr, 0, 4, 0);
                short readUint8 = TlsUtils.readUint8(bArr, 0);
                int readUint24 = TlsUtils.readUint24(bArr, 1);
                if (this.handshakeQueue.available() >= readUint24 + 4) {
                    byte[] removeData = this.handshakeQueue.removeData(readUint24, 4);
                    switch (readUint8) {
                        case ECCurve.COORD_AFFINE /*0*/:
                            break;
                        case SkeinParameterSpec.PARAM_TYPE_NONCE /*20*/:
                            if (this.expected_verify_data == null) {
                                this.expected_verify_data = createVerifyData(!getContext().isServer());
                                break;
                            }
                            break;
                    }
                    this.recordStream.updateHandshakeData(bArr, 0, 4);
                    this.recordStream.updateHandshakeData(removeData, 0, readUint24);
                    handleHandshakeMessage(readUint8, removeData);
                    i = 1;
                    continue;
                }
            }
            i = 0;
            continue;
        } while (i != 0);
    }

    protected static Hashtable readExtensions(ByteArrayInputStream byteArrayInputStream) {
        if (byteArrayInputStream.available() < 1) {
            return null;
        }
        byte[] readOpaque16 = TlsUtils.readOpaque16(byteArrayInputStream);
        assertEmpty(byteArrayInputStream);
        InputStream byteArrayInputStream2 = new ByteArrayInputStream(readOpaque16);
        Hashtable hashtable = new Hashtable();
        while (byteArrayInputStream2.available() > 0) {
            if (hashtable.put(Integers.valueOf(TlsUtils.readUint16(byteArrayInputStream2)), TlsUtils.readOpaque16(byteArrayInputStream2)) != null) {
                throw new TlsFatalAlert((short) 47);
            }
        }
        return hashtable;
    }

    protected static Vector readSupplementalDataMessage(ByteArrayInputStream byteArrayInputStream) {
        byte[] readOpaque24 = TlsUtils.readOpaque24(byteArrayInputStream);
        assertEmpty(byteArrayInputStream);
        InputStream byteArrayInputStream2 = new ByteArrayInputStream(readOpaque24);
        Vector vector = new Vector();
        while (byteArrayInputStream2.available() > 0) {
            vector.addElement(new SupplementalDataEntry(TlsUtils.readUint16(byteArrayInputStream2), TlsUtils.readOpaque16(byteArrayInputStream2)));
        }
        return vector;
    }

    protected static void writeExtensions(OutputStream outputStream, Hashtable hashtable) {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Enumeration keys = hashtable.keys();
        while (keys.hasMoreElements()) {
            Integer num = (Integer) keys.nextElement();
            int intValue = num.intValue();
            byte[] bArr = (byte[]) hashtable.get(num);
            TlsUtils.checkUint16(intValue);
            TlsUtils.writeUint16(intValue, byteArrayOutputStream);
            TlsUtils.writeOpaque16(bArr, byteArrayOutputStream);
        }
        TlsUtils.writeOpaque16(byteArrayOutputStream.toByteArray(), outputStream);
    }

    protected static void writeSupplementalData(OutputStream outputStream, Vector vector) {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (int i = 0; i < vector.size(); i++) {
            SupplementalDataEntry supplementalDataEntry = (SupplementalDataEntry) vector.elementAt(i);
            int dataType = supplementalDataEntry.getDataType();
            TlsUtils.checkUint16(dataType);
            TlsUtils.writeUint16(dataType, byteArrayOutputStream);
            TlsUtils.writeOpaque16(supplementalDataEntry.getData(), byteArrayOutputStream);
        }
        TlsUtils.writeOpaque24(byteArrayOutputStream.toByteArray(), outputStream);
    }

    protected int applicationDataAvailable() {
        return this.applicationDataQueue.available();
    }

    protected void cleanupHandshake() {
        if (this.expected_verify_data != null) {
            Arrays.fill(this.expected_verify_data, (byte) 0);
            this.expected_verify_data = null;
        }
        this.securityParameters.clear();
        this.peerCertificate = null;
        this.offeredCipherSuites = null;
        this.offeredCompressionMethods = null;
        this.clientExtensions = null;
        this.serverExtensions = null;
        this.resumedSession = false;
        this.receivedChangeCipherSpec = false;
        this.secure_renegotiation = false;
        this.allowCertificateStatus = false;
        this.expectSessionTicket = false;
    }

    public void close() {
        handleClose(true);
    }

    protected void completeHandshake() {
        boolean z = true;
        while (this.connection_state != CS_END) {
            try {
                if (this.closed) {
                    safeReadRecord();
                } else {
                    safeReadRecord();
                }
            } finally {
                cleanupHandshake();
            }
        }
        this.recordStream.finaliseHandshake();
        if (TlsUtils.isTLSv11(getContext())) {
            z = false;
        }
        this.splitApplicationDataRecords = z;
        if (!this.appDataReady) {
            this.appDataReady = true;
            this.tlsInputStream = new TlsInputStream(this);
            this.tlsOutputStream = new TlsOutputStream(this);
        }
        if (this.tlsSession != null) {
            if (this.sessionParameters == null) {
                this.sessionParameters = new Builder().setCipherSuite(this.securityParameters.cipherSuite).setCompressionAlgorithm(this.securityParameters.compressionAlgorithm).setMasterSecret(this.securityParameters.masterSecret).setPeerCertificate(this.peerCertificate).setPSKIdentity(this.securityParameters.pskIdentity).setSRPIdentity(this.securityParameters.srpIdentity).setServerExtensions(this.serverExtensions).build();
                this.tlsSession = new TlsSessionImpl(this.tlsSession.getSessionID(), this.sessionParameters);
            }
            getContextAdmin().setResumableSession(this.tlsSession);
        }
        getPeer().notifyHandshakeComplete();
    }

    protected byte[] createVerifyData(boolean z) {
        TlsContext context = getContext();
        return TlsUtils.calculateVerifyData(context, z ? ExporterLabel.server_finished : ExporterLabel.client_finished, getCurrentPRFHash(context, this.recordStream.getHandshakeHash(), z ? TlsUtils.SSL_SERVER : TlsUtils.SSL_CLIENT));
    }

    protected void failWithError(short s, short s2, String str, Throwable th) {
        if (!this.closed) {
            this.closed = true;
            if (s == CS_SERVER_HELLO) {
                invalidateSession();
                this.failedWithError = true;
            }
            raiseAlert(s, s2, str, th);
            this.recordStream.safeClose();
            if (s != CS_SERVER_HELLO) {
                return;
            }
        }
        throw new IOException(TLS_ERROR_MESSAGE);
    }

    protected void flush() {
        this.recordStream.flush();
    }

    protected abstract TlsContext getContext();

    abstract AbstractTlsContext getContextAdmin();

    public InputStream getInputStream() {
        return this.tlsInputStream;
    }

    public OutputStream getOutputStream() {
        return this.tlsOutputStream;
    }

    protected abstract TlsPeer getPeer();

    protected void handleChangeCipherSpecMessage() {
    }

    protected void handleClose(boolean z) {
        if (!this.closed) {
            if (z && !this.appDataReady) {
                raiseWarning((short) 90, "User canceled handshake");
            }
            failWithError(CS_CLIENT_HELLO, CS_START, "Connection closed", null);
        }
    }

    protected abstract void handleHandshakeMessage(short s, byte[] bArr);

    protected void handleWarningMessage(short s) {
    }

    protected void invalidateSession() {
        if (this.sessionParameters != null) {
            this.sessionParameters.clear();
            this.sessionParameters = null;
        }
        if (this.tlsSession != null) {
            this.tlsSession.invalidate();
            this.tlsSession = null;
        }
    }

    protected boolean isClosed() {
        return this.closed;
    }

    protected void processFinishedMessage(ByteArrayInputStream byteArrayInputStream) {
        byte[] readFully = TlsUtils.readFully(this.expected_verify_data.length, (InputStream) byteArrayInputStream);
        assertEmpty(byteArrayInputStream);
        if (!Arrays.constantTimeAreEqual(this.expected_verify_data, readFully)) {
            throw new TlsFatalAlert((short) 51);
        }
    }

    protected short processMaxFragmentLengthExtension(Hashtable hashtable, Hashtable hashtable2, short s) {
        short maxFragmentLengthExtension = TlsExtensionsUtils.getMaxFragmentLengthExtension(hashtable2);
        if (maxFragmentLengthExtension < (short) 0 || this.resumedSession || maxFragmentLengthExtension == TlsExtensionsUtils.getMaxFragmentLengthExtension(hashtable)) {
            return maxFragmentLengthExtension;
        }
        throw new TlsFatalAlert(s);
    }

    protected void processRecord(short s, byte[] bArr, int i, int i2) {
        switch (s) {
            case SkeinParameterSpec.PARAM_TYPE_NONCE /*20*/:
                processChangeCipherSpec(bArr, i, i2);
            case NamedCurve.secp224r1 /*21*/:
                this.alertQueue.addData(bArr, i, i2);
                processAlert();
            case NamedCurve.secp256k1 /*22*/:
                this.handshakeQueue.addData(bArr, i, i2);
                processHandshake();
            case NamedCurve.secp256r1 /*23*/:
                if (this.appDataReady) {
                    this.applicationDataQueue.addData(bArr, i, i2);
                    processApplicationData();
                    return;
                }
                throw new TlsFatalAlert(CS_CLIENT_CERTIFICATE);
            case NamedCurve.secp384r1 /*24*/:
                if (!this.appDataReady) {
                    throw new TlsFatalAlert(CS_CLIENT_CERTIFICATE);
                }
            default:
        }
    }

    protected void raiseAlert(short s, short s2, String str, Throwable th) {
        getPeer().notifyAlertRaised(s, s2, str, th);
        safeWriteRecord((short) 21, new byte[]{(byte) s, (byte) s2}, 0, 2);
    }

    protected void raiseWarning(short s, String str) {
        raiseAlert(CS_CLIENT_HELLO, s, str, null);
    }

    protected int readApplicationData(byte[] bArr, int i, int i2) {
        if (i2 < 1) {
            return 0;
        }
        while (this.applicationDataQueue.available() == 0) {
            if (!this.closed) {
                safeReadRecord();
            } else if (!this.failedWithError) {
                return -1;
            } else {
                throw new IOException(TLS_ERROR_MESSAGE);
            }
        }
        int min = Math.min(i2, this.applicationDataQueue.available());
        this.applicationDataQueue.removeData(bArr, i, min, 0);
        return min;
    }

    protected void safeReadRecord() {
        try {
            if (!this.recordStream.readRecord()) {
                throw new EOFException();
            }
        } catch (Throwable e) {
            if (!this.closed) {
                failWithError(CS_SERVER_HELLO, e.getAlertDescription(), "Failed to read record", e);
            }
            throw e;
        } catch (Throwable e2) {
            if (!this.closed) {
                failWithError(CS_SERVER_HELLO, (short) 80, "Failed to read record", e2);
            }
            throw e2;
        } catch (Throwable e22) {
            if (!this.closed) {
                failWithError(CS_SERVER_HELLO, (short) 80, "Failed to read record", e22);
            }
            throw e22;
        }
    }

    protected void safeWriteRecord(short s, byte[] bArr, int i, int i2) {
        try {
            this.recordStream.writeRecord(s, bArr, i, i2);
        } catch (Throwable e) {
            if (!this.closed) {
                failWithError(CS_SERVER_HELLO, e.getAlertDescription(), "Failed to write record", e);
            }
            throw e;
        } catch (Throwable e2) {
            if (!this.closed) {
                failWithError(CS_SERVER_HELLO, (short) 80, "Failed to write record", e2);
            }
            throw e2;
        } catch (Throwable e22) {
            if (!this.closed) {
                failWithError(CS_SERVER_HELLO, (short) 80, "Failed to write record", e22);
            }
            throw e22;
        }
    }

    protected void sendCertificateMessage(Certificate certificate) {
        if (certificate == null) {
            certificate = Certificate.EMPTY_CHAIN;
        }
        if (certificate.isEmpty() && !getContext().isServer()) {
            ProtocolVersion serverVersion = getContext().getServerVersion();
            if (serverVersion.isSSL()) {
                raiseWarning((short) 41, serverVersion.toString() + " client didn't provide credentials");
                return;
            }
        }
        OutputStream handshakeMessage = new HandshakeMessage(this, CS_CLIENT_KEY_EXCHANGE);
        certificate.encode(handshakeMessage);
        handshakeMessage.writeToRecordStream();
    }

    protected void sendChangeCipherSpecMessage() {
        byte[] bArr = new byte[]{(byte) 1};
        safeWriteRecord((short) 20, bArr, 0, bArr.length);
        this.recordStream.sentWriteCipherSpec();
    }

    protected void sendFinishedMessage() {
        byte[] createVerifyData = createVerifyData(getContext().isServer());
        HandshakeMessage handshakeMessage = new HandshakeMessage((short) 20, createVerifyData.length);
        handshakeMessage.write(createVerifyData);
        handshakeMessage.writeToRecordStream();
    }

    protected void sendSupplementalDataMessage(Vector vector) {
        OutputStream handshakeMessage = new HandshakeMessage(this, (short) 23);
        writeSupplementalData(handshakeMessage, vector);
        handshakeMessage.writeToRecordStream();
    }

    protected void writeData(byte[] bArr, int i, int i2) {
        if (!this.closed) {
            int i3 = i2;
            int i4 = i;
            while (i3 > 0) {
                if (this.splitApplicationDataRecords) {
                    safeWriteRecord((short) 23, bArr, i4, 1);
                    i4++;
                    i3--;
                }
                if (i3 > 0) {
                    int min = Math.min(i3, this.recordStream.getPlaintextLimit());
                    safeWriteRecord((short) 23, bArr, i4, min);
                    i4 += min;
                    i3 -= min;
                }
            }
        } else if (this.failedWithError) {
            throw new IOException(TLS_ERROR_MESSAGE);
        } else {
            throw new IOException("Sorry, connection has been closed, you cannot write more data");
        }
    }

    protected void writeHandshakeMessage(byte[] bArr, int i, int i2) {
        while (i2 > 0) {
            int min = Math.min(i2, this.recordStream.getPlaintextLimit());
            safeWriteRecord((short) 22, bArr, i, min);
            i += min;
            i2 -= min;
        }
    }
}
