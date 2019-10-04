/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.util.Hashtable
 *  java.util.Vector
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.crypto.tls;

import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle.crypto.tls.AbstractTlsPeer;
import org.bouncycastle.crypto.tls.Certificate;
import org.bouncycastle.crypto.tls.CertificateRequest;
import org.bouncycastle.crypto.tls.CertificateStatus;
import org.bouncycastle.crypto.tls.DefaultTlsCipherFactory;
import org.bouncycastle.crypto.tls.MaxFragmentLength;
import org.bouncycastle.crypto.tls.NamedCurve;
import org.bouncycastle.crypto.tls.NewSessionTicket;
import org.bouncycastle.crypto.tls.ProtocolVersion;
import org.bouncycastle.crypto.tls.TlsCipherFactory;
import org.bouncycastle.crypto.tls.TlsCompression;
import org.bouncycastle.crypto.tls.TlsECCUtils;
import org.bouncycastle.crypto.tls.TlsExtensionsUtils;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsNullCompression;
import org.bouncycastle.crypto.tls.TlsServer;
import org.bouncycastle.crypto.tls.TlsServerContext;
import org.bouncycastle.crypto.tls.TlsUtils;
import org.bouncycastle.util.Arrays;

public abstract class AbstractTlsServer
extends AbstractTlsPeer
implements TlsServer {
    protected TlsCipherFactory cipherFactory;
    protected short[] clientECPointFormats;
    protected Hashtable clientExtensions;
    protected ProtocolVersion clientVersion;
    protected TlsServerContext context;
    protected boolean eccCipherSuitesOffered;
    protected boolean encryptThenMACOffered;
    protected short maxFragmentLengthOffered;
    protected int[] namedCurves;
    protected int[] offeredCipherSuites;
    protected short[] offeredCompressionMethods;
    protected int selectedCipherSuite;
    protected short selectedCompressionMethod;
    protected short[] serverECPointFormats;
    protected Hashtable serverExtensions;
    protected ProtocolVersion serverVersion;
    protected Vector supportedSignatureAlgorithms;
    protected boolean truncatedHMacOffered;

    public AbstractTlsServer() {
        this(new DefaultTlsCipherFactory());
    }

    public AbstractTlsServer(TlsCipherFactory tlsCipherFactory) {
        this.cipherFactory = tlsCipherFactory;
    }

    protected boolean allowEncryptThenMAC() {
        return true;
    }

    protected boolean allowTruncatedHMac() {
        return false;
    }

    protected Hashtable checkServerExtensions() {
        Hashtable hashtable;
        this.serverExtensions = hashtable = TlsExtensionsUtils.ensureExtensionsInitialised(this.serverExtensions);
        return hashtable;
    }

    @Override
    public CertificateRequest getCertificateRequest() {
        return null;
    }

    @Override
    public CertificateStatus getCertificateStatus() {
        return null;
    }

    protected abstract int[] getCipherSuites();

    @Override
    public TlsCompression getCompression() {
        switch (this.selectedCompressionMethod) {
            default: {
                throw new TlsFatalAlert(80);
            }
            case 0: 
        }
        return new TlsNullCompression();
    }

    protected short[] getCompressionMethods() {
        return new short[]{0};
    }

    protected ProtocolVersion getMaximumVersion() {
        return ProtocolVersion.TLSv11;
    }

    protected ProtocolVersion getMinimumVersion() {
        return ProtocolVersion.TLSv10;
    }

    @Override
    public NewSessionTicket getNewSessionTicket() {
        return new NewSessionTicket(0L, TlsUtils.EMPTY_BYTES);
    }

    @Override
    public int getSelectedCipherSuite() {
        boolean bl = this.supportsClientECCCapabilities(this.namedCurves, this.clientECPointFormats);
        int[] arrn = this.getCipherSuites();
        for (int i2 = 0; i2 < arrn.length; ++i2) {
            int n2 = arrn[i2];
            if (!Arrays.contains((int[])this.offeredCipherSuites, (int)n2) || !bl && TlsECCUtils.isECCCipherSuite(n2) || !TlsUtils.isValidCipherSuiteForVersion(n2, this.serverVersion)) continue;
            this.selectedCipherSuite = n2;
            return n2;
        }
        throw new TlsFatalAlert(40);
    }

    @Override
    public short getSelectedCompressionMethod() {
        short[] arrs = this.getCompressionMethods();
        for (int i2 = 0; i2 < arrs.length; ++i2) {
            short s2;
            if (!Arrays.contains((short[])this.offeredCompressionMethods, (short)arrs[i2])) continue;
            this.selectedCompressionMethod = s2 = arrs[i2];
            return s2;
        }
        throw new TlsFatalAlert(40);
    }

    @Override
    public Hashtable getServerExtensions() {
        if (this.encryptThenMACOffered && this.allowEncryptThenMAC() && TlsUtils.isBlockCipherSuite(this.selectedCipherSuite)) {
            TlsExtensionsUtils.addEncryptThenMACExtension(this.checkServerExtensions());
        }
        if (this.maxFragmentLengthOffered >= 0 && MaxFragmentLength.isValid(this.maxFragmentLengthOffered)) {
            TlsExtensionsUtils.addMaxFragmentLengthExtension(this.checkServerExtensions(), this.maxFragmentLengthOffered);
        }
        if (this.truncatedHMacOffered && this.allowTruncatedHMac()) {
            TlsExtensionsUtils.addTruncatedHMacExtension(this.checkServerExtensions());
        }
        if (this.clientECPointFormats != null && TlsECCUtils.isECCCipherSuite(this.selectedCipherSuite)) {
            this.serverECPointFormats = new short[]{0, 1, 2};
            TlsECCUtils.addSupportedPointFormatsExtension(this.checkServerExtensions(), this.serverECPointFormats);
        }
        return this.serverExtensions;
    }

    @Override
    public Vector getServerSupplementalData() {
        return null;
    }

    @Override
    public ProtocolVersion getServerVersion() {
        if (this.getMinimumVersion().isEqualOrEarlierVersionOf(this.clientVersion)) {
            ProtocolVersion protocolVersion = this.getMaximumVersion();
            if (this.clientVersion.isEqualOrEarlierVersionOf(protocolVersion)) {
                ProtocolVersion protocolVersion2;
                this.serverVersion = protocolVersion2 = this.clientVersion;
                return protocolVersion2;
            }
            if (this.clientVersion.isLaterVersionOf(protocolVersion)) {
                this.serverVersion = protocolVersion;
                return protocolVersion;
            }
        }
        throw new TlsFatalAlert(70);
    }

    @Override
    public void init(TlsServerContext tlsServerContext) {
        this.context = tlsServerContext;
    }

    @Override
    public void notifyClientCertificate(Certificate certificate) {
        throw new TlsFatalAlert(80);
    }

    @Override
    public void notifyClientVersion(ProtocolVersion protocolVersion) {
        this.clientVersion = protocolVersion;
    }

    @Override
    public void notifyFallback(boolean bl) {
        if (bl && this.getMaximumVersion().isLaterVersionOf(this.clientVersion)) {
            throw new TlsFatalAlert(86);
        }
    }

    @Override
    public void notifyOfferedCipherSuites(int[] arrn) {
        this.offeredCipherSuites = arrn;
        this.eccCipherSuitesOffered = TlsECCUtils.containsECCCipherSuites(this.offeredCipherSuites);
    }

    @Override
    public void notifyOfferedCompressionMethods(short[] arrs) {
        this.offeredCompressionMethods = arrs;
    }

    @Override
    public void processClientExtensions(Hashtable hashtable) {
        this.clientExtensions = hashtable;
        if (hashtable != null) {
            this.encryptThenMACOffered = TlsExtensionsUtils.hasEncryptThenMACExtension(hashtable);
            this.maxFragmentLengthOffered = TlsExtensionsUtils.getMaxFragmentLengthExtension(hashtable);
            this.truncatedHMacOffered = TlsExtensionsUtils.hasTruncatedHMacExtension(hashtable);
            this.supportedSignatureAlgorithms = TlsUtils.getSignatureAlgorithmsExtension(hashtable);
            if (this.supportedSignatureAlgorithms != null && !TlsUtils.isSignatureAlgorithmsExtensionAllowed(this.clientVersion)) {
                throw new TlsFatalAlert(47);
            }
            this.namedCurves = TlsECCUtils.getSupportedEllipticCurvesExtension(hashtable);
            this.clientECPointFormats = TlsECCUtils.getSupportedPointFormatsExtension(hashtable);
        }
        if (!(this.eccCipherSuitesOffered || this.namedCurves == null && this.clientECPointFormats == null)) {
            throw new TlsFatalAlert(47);
        }
    }

    @Override
    public void processClientSupplementalData(Vector vector) {
        if (vector != null) {
            throw new TlsFatalAlert(10);
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected boolean supportsClientECCCapabilities(int[] arrn, short[] arrs) {
        if (arrn == null) {
            return TlsECCUtils.hasAnySupportedNamedCurves();
        }
        int n2 = 0;
        do {
            int n3 = arrn.length;
            boolean bl = false;
            if (n2 >= n3) return bl;
            int n4 = arrn[n2];
            if (NamedCurve.isValid(n4)) {
                if (!NamedCurve.refersToASpecificNamedCurve(n4)) return true;
                if (TlsECCUtils.isSupportedNamedCurve(n4)) {
                    return true;
                }
            }
            ++n2;
        } while (true);
    }
}

