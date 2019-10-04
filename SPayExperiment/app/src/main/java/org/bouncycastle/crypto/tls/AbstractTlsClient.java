/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Integer
 *  java.util.Hashtable
 *  java.util.Vector
 */
package org.bouncycastle.crypto.tls;

import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle.crypto.tls.AbstractTlsPeer;
import org.bouncycastle.crypto.tls.DefaultTlsCipherFactory;
import org.bouncycastle.crypto.tls.NewSessionTicket;
import org.bouncycastle.crypto.tls.ProtocolVersion;
import org.bouncycastle.crypto.tls.TlsCipherFactory;
import org.bouncycastle.crypto.tls.TlsClient;
import org.bouncycastle.crypto.tls.TlsClientContext;
import org.bouncycastle.crypto.tls.TlsCompression;
import org.bouncycastle.crypto.tls.TlsECCUtils;
import org.bouncycastle.crypto.tls.TlsExtensionsUtils;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsNullCompression;
import org.bouncycastle.crypto.tls.TlsSession;
import org.bouncycastle.crypto.tls.TlsUtils;

public abstract class AbstractTlsClient
extends AbstractTlsPeer
implements TlsClient {
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

    protected boolean allowUnexpectedServerExtension(Integer n2, byte[] arrby) {
        switch (n2) {
            default: {
                return false;
            }
            case 10: 
        }
        TlsECCUtils.readSupportedEllipticCurvesExtension(arrby);
        return true;
    }

    protected void checkForUnexpectedServerExtension(Hashtable hashtable, Integer n2) {
        byte[] arrby = TlsUtils.getExtensionData(hashtable, n2);
        if (arrby != null && !this.allowUnexpectedServerExtension(n2, arrby)) {
            throw new TlsFatalAlert(47);
        }
    }

    @Override
    public Hashtable getClientExtensions() {
        boolean bl = TlsUtils.isSignatureAlgorithmsExtensionAllowed(this.context.getClientVersion());
        Hashtable hashtable = null;
        if (bl) {
            this.supportedSignatureAlgorithms = TlsUtils.getDefaultSupportedSignatureAlgorithms();
            hashtable = TlsExtensionsUtils.ensureExtensionsInitialised(null);
            TlsUtils.addSignatureAlgorithmsExtension(hashtable, this.supportedSignatureAlgorithms);
        }
        if (TlsECCUtils.containsECCCipherSuites(this.getCipherSuites())) {
            this.namedCurves = new int[]{23, 24};
            this.clientECPointFormats = new short[]{0, 1, 2};
            hashtable = TlsExtensionsUtils.ensureExtensionsInitialised(hashtable);
            TlsECCUtils.addSupportedEllipticCurvesExtension(hashtable, this.namedCurves);
            TlsECCUtils.addSupportedPointFormatsExtension(hashtable, this.clientECPointFormats);
        }
        return hashtable;
    }

    @Override
    public ProtocolVersion getClientHelloRecordLayerVersion() {
        return this.getClientVersion();
    }

    @Override
    public Vector getClientSupplementalData() {
        return null;
    }

    @Override
    public ProtocolVersion getClientVersion() {
        return ProtocolVersion.TLSv12;
    }

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

    @Override
    public short[] getCompressionMethods() {
        return new short[]{0};
    }

    public ProtocolVersion getMinimumVersion() {
        return ProtocolVersion.TLSv10;
    }

    @Override
    public TlsSession getSessionToResume() {
        return null;
    }

    @Override
    public void init(TlsClientContext tlsClientContext) {
        this.context = tlsClientContext;
    }

    @Override
    public boolean isFallback() {
        return false;
    }

    @Override
    public void notifyNewSessionTicket(NewSessionTicket newSessionTicket) {
    }

    @Override
    public void notifySelectedCipherSuite(int n2) {
        this.selectedCipherSuite = n2;
    }

    @Override
    public void notifySelectedCompressionMethod(short s2) {
        this.selectedCompressionMethod = s2;
    }

    @Override
    public void notifyServerVersion(ProtocolVersion protocolVersion) {
        if (!this.getMinimumVersion().isEqualOrEarlierVersionOf(protocolVersion)) {
            throw new TlsFatalAlert(70);
        }
    }

    @Override
    public void notifySessionID(byte[] arrby) {
    }

    @Override
    public void processServerExtensions(Hashtable hashtable) {
        block3 : {
            block2 : {
                if (hashtable == null) break block2;
                this.checkForUnexpectedServerExtension(hashtable, TlsUtils.EXT_signature_algorithms);
                this.checkForUnexpectedServerExtension(hashtable, TlsECCUtils.EXT_elliptic_curves);
                if (!TlsECCUtils.isECCCipherSuite(this.selectedCipherSuite)) break block3;
                this.serverECPointFormats = TlsECCUtils.getSupportedPointFormatsExtension(hashtable);
            }
            return;
        }
        this.checkForUnexpectedServerExtension(hashtable, TlsECCUtils.EXT_ec_point_formats);
    }

    @Override
    public void processServerSupplementalData(Vector vector) {
        if (vector != null) {
            throw new TlsFatalAlert(10);
        }
    }
}

