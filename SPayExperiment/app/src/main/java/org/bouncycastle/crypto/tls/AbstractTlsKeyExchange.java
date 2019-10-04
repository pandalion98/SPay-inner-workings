/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.InputStream
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Vector
 */
package org.bouncycastle.crypto.tls;

import java.io.InputStream;
import java.util.Vector;
import org.bouncycastle.crypto.tls.Certificate;
import org.bouncycastle.crypto.tls.ProtocolVersion;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsCredentials;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsKeyExchange;
import org.bouncycastle.crypto.tls.TlsUtils;

public abstract class AbstractTlsKeyExchange
implements TlsKeyExchange {
    protected TlsContext context;
    protected int keyExchange;
    protected Vector supportedSignatureAlgorithms;

    protected AbstractTlsKeyExchange(int n2, Vector vector) {
        this.keyExchange = n2;
        this.supportedSignatureAlgorithms = vector;
    }

    @Override
    public byte[] generateServerKeyExchange() {
        if (this.requiresServerKeyExchange()) {
            throw new TlsFatalAlert(80);
        }
        return null;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void init(TlsContext tlsContext) {
        this.context = tlsContext;
        ProtocolVersion protocolVersion = tlsContext.getClientVersion();
        if (TlsUtils.isSignatureAlgorithmsExtensionAllowed(protocolVersion)) {
            if (this.supportedSignatureAlgorithms != null) return;
            switch (this.keyExchange) {
                default: {
                    throw new IllegalStateException("unsupported key exchange algorithm");
                }
                case 3: 
                case 7: 
                case 22: {
                    this.supportedSignatureAlgorithms = TlsUtils.getDefaultDSSSignatureAlgorithms();
                }
                case 13: 
                case 14: 
                case 21: 
                case 24: {
                    return;
                }
                case 16: 
                case 17: {
                    this.supportedSignatureAlgorithms = TlsUtils.getDefaultECDSASignatureAlgorithms();
                    return;
                }
                case 1: 
                case 5: 
                case 9: 
                case 15: 
                case 18: 
                case 19: 
                case 23: {
                    this.supportedSignatureAlgorithms = TlsUtils.getDefaultRSASignatureAlgorithms();
                    return;
                }
            }
        }
        if (this.supportedSignatureAlgorithms == null) return;
        {
            throw new IllegalStateException("supported_signature_algorithms not allowed for " + protocolVersion);
        }
    }

    @Override
    public void processClientCertificate(Certificate certificate) {
    }

    @Override
    public void processClientKeyExchange(InputStream inputStream) {
        throw new TlsFatalAlert(80);
    }

    @Override
    public void processServerCertificate(Certificate certificate) {
        if (this.supportedSignatureAlgorithms == null) {
            // empty if block
        }
    }

    @Override
    public void processServerCredentials(TlsCredentials tlsCredentials) {
        this.processServerCertificate(tlsCredentials.getCertificate());
    }

    @Override
    public void processServerKeyExchange(InputStream inputStream) {
        if (!this.requiresServerKeyExchange()) {
            throw new TlsFatalAlert(10);
        }
    }

    @Override
    public boolean requiresServerKeyExchange() {
        return false;
    }

    @Override
    public void skipClientCredentials() {
    }

    @Override
    public void skipServerKeyExchange() {
        if (this.requiresServerKeyExchange()) {
            throw new TlsFatalAlert(10);
        }
    }
}

