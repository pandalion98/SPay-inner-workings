package org.bouncycastle.crypto.tls;

import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle.math.ec.ECCurve;

public abstract class AbstractTlsClient extends AbstractTlsPeer implements TlsClient {
    protected TlsCipherFactory cipherFactory;
    protected short[] clientECPointFormats;
    protected TlsClientContext context;
    protected int[] namedCurves;
    protected int selectedCipherSuite;
    protected short selectedCompressionMethod;
    protected short[] serverECPointFormats;
    protected Vector supportedSignatureAlgorithms;

    public AbstractTlsClient() {
        this(new DefaultTlsCipherFactory());
    }

    public AbstractTlsClient(TlsCipherFactory tlsCipherFactory) {
        this.cipherFactory = tlsCipherFactory;
    }

    protected boolean allowUnexpectedServerExtension(Integer num, byte[] bArr) {
        switch (num.intValue()) {
            case NamedCurve.sect283r1 /*10*/:
                TlsECCUtils.readSupportedEllipticCurvesExtension(bArr);
                return true;
            default:
                return false;
        }
    }

    protected void checkForUnexpectedServerExtension(Hashtable hashtable, Integer num) {
        byte[] extensionData = TlsUtils.getExtensionData(hashtable, num);
        if (extensionData != null && !allowUnexpectedServerExtension(num, extensionData)) {
            throw new TlsFatalAlert((short) 47);
        }
    }

    public Hashtable getClientExtensions() {
        Hashtable hashtable = null;
        if (TlsUtils.isSignatureAlgorithmsExtensionAllowed(this.context.getClientVersion())) {
            this.supportedSignatureAlgorithms = TlsUtils.getDefaultSupportedSignatureAlgorithms();
            hashtable = TlsExtensionsUtils.ensureExtensionsInitialised(null);
            TlsUtils.addSignatureAlgorithmsExtension(hashtable, this.supportedSignatureAlgorithms);
        }
        if (!TlsECCUtils.containsECCCipherSuites(getCipherSuites())) {
            return hashtable;
        }
        this.namedCurves = new int[]{23, 24};
        this.clientECPointFormats = new short[]{(short) 0, (short) 1, (short) 2};
        hashtable = TlsExtensionsUtils.ensureExtensionsInitialised(hashtable);
        TlsECCUtils.addSupportedEllipticCurvesExtension(hashtable, this.namedCurves);
        TlsECCUtils.addSupportedPointFormatsExtension(hashtable, this.clientECPointFormats);
        return hashtable;
    }

    public ProtocolVersion getClientHelloRecordLayerVersion() {
        return getClientVersion();
    }

    public Vector getClientSupplementalData() {
        return null;
    }

    public ProtocolVersion getClientVersion() {
        return ProtocolVersion.TLSv12;
    }

    public TlsCompression getCompression() {
        switch (this.selectedCompressionMethod) {
            case ECCurve.COORD_AFFINE /*0*/:
                return new TlsNullCompression();
            default:
                throw new TlsFatalAlert((short) 80);
        }
    }

    public short[] getCompressionMethods() {
        return new short[]{(short) 0};
    }

    public ProtocolVersion getMinimumVersion() {
        return ProtocolVersion.TLSv10;
    }

    public TlsSession getSessionToResume() {
        return null;
    }

    public void init(TlsClientContext tlsClientContext) {
        this.context = tlsClientContext;
    }

    public boolean isFallback() {
        return false;
    }

    public void notifyNewSessionTicket(NewSessionTicket newSessionTicket) {
    }

    public void notifySelectedCipherSuite(int i) {
        this.selectedCipherSuite = i;
    }

    public void notifySelectedCompressionMethod(short s) {
        this.selectedCompressionMethod = s;
    }

    public void notifyServerVersion(ProtocolVersion protocolVersion) {
        if (!getMinimumVersion().isEqualOrEarlierVersionOf(protocolVersion)) {
            throw new TlsFatalAlert((short) 70);
        }
    }

    public void notifySessionID(byte[] bArr) {
    }

    public void processServerExtensions(Hashtable hashtable) {
        if (hashtable != null) {
            checkForUnexpectedServerExtension(hashtable, TlsUtils.EXT_signature_algorithms);
            checkForUnexpectedServerExtension(hashtable, TlsECCUtils.EXT_elliptic_curves);
            if (TlsECCUtils.isECCCipherSuite(this.selectedCipherSuite)) {
                this.serverECPointFormats = TlsECCUtils.getSupportedPointFormatsExtension(hashtable);
            } else {
                checkForUnexpectedServerExtension(hashtable, TlsECCUtils.EXT_ec_point_formats);
            }
        }
    }

    public void processServerSupplementalData(Vector vector) {
        if (vector != null) {
            throw new TlsFatalAlert((short) 10);
        }
    }
}
