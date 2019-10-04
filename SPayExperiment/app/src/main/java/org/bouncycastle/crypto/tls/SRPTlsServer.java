/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.util.Hashtable
 *  java.util.Vector
 */
package org.bouncycastle.crypto.tls;

import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle.crypto.tls.AbstractTlsServer;
import org.bouncycastle.crypto.tls.DefaultTlsCipherFactory;
import org.bouncycastle.crypto.tls.TlsCipher;
import org.bouncycastle.crypto.tls.TlsCipherFactory;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsCredentials;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsKeyExchange;
import org.bouncycastle.crypto.tls.TlsSRPIdentityManager;
import org.bouncycastle.crypto.tls.TlsSRPKeyExchange;
import org.bouncycastle.crypto.tls.TlsSRPLoginParameters;
import org.bouncycastle.crypto.tls.TlsSRPUtils;
import org.bouncycastle.crypto.tls.TlsServerContext;
import org.bouncycastle.crypto.tls.TlsSignerCredentials;

public class SRPTlsServer
extends AbstractTlsServer {
    protected TlsSRPLoginParameters loginParameters = null;
    protected byte[] srpIdentity = null;
    protected TlsSRPIdentityManager srpIdentityManager;

    public SRPTlsServer(TlsCipherFactory tlsCipherFactory, TlsSRPIdentityManager tlsSRPIdentityManager) {
        super(tlsCipherFactory);
        this.srpIdentityManager = tlsSRPIdentityManager;
    }

    public SRPTlsServer(TlsSRPIdentityManager tlsSRPIdentityManager) {
        this(new DefaultTlsCipherFactory(), tlsSRPIdentityManager);
    }

    protected TlsKeyExchange createSRPKeyExchange(int n2) {
        return new TlsSRPKeyExchange(n2, this.supportedSignatureAlgorithms, this.srpIdentity, this.loginParameters);
    }

    @Override
    public TlsCipher getCipher() {
        switch (this.selectedCipherSuite) {
            default: {
                throw new TlsFatalAlert(80);
            }
            case 49178: 
            case 49179: 
            case 49180: {
                return this.cipherFactory.createCipher(this.context, 7, 2);
            }
            case 49181: 
            case 49182: 
            case 49183: {
                return this.cipherFactory.createCipher(this.context, 8, 2);
            }
            case 49184: 
            case 49185: 
            case 49186: 
        }
        return this.cipherFactory.createCipher(this.context, 9, 2);
    }

    @Override
    protected int[] getCipherSuites() {
        return new int[]{49186, 49183, 49185, 49182, 49184, 49181};
    }

    @Override
    public TlsCredentials getCredentials() {
        switch (this.selectedCipherSuite) {
            default: {
                throw new TlsFatalAlert(80);
            }
            case 49178: 
            case 49181: 
            case 49184: {
                return null;
            }
            case 49180: 
            case 49183: 
            case 49186: {
                return this.getDSASignerCredentials();
            }
            case 49179: 
            case 49182: 
            case 49185: 
        }
        return this.getRSASignerCredentials();
    }

    protected TlsSignerCredentials getDSASignerCredentials() {
        throw new TlsFatalAlert(80);
    }

    @Override
    public TlsKeyExchange getKeyExchange() {
        switch (this.selectedCipherSuite) {
            default: {
                throw new TlsFatalAlert(80);
            }
            case 49178: 
            case 49181: 
            case 49184: {
                return this.createSRPKeyExchange(21);
            }
            case 49179: 
            case 49182: 
            case 49185: {
                return this.createSRPKeyExchange(23);
            }
            case 49180: 
            case 49183: 
            case 49186: 
        }
        return this.createSRPKeyExchange(22);
    }

    protected TlsSignerCredentials getRSASignerCredentials() {
        throw new TlsFatalAlert(80);
    }

    @Override
    public int getSelectedCipherSuite() {
        int n2 = super.getSelectedCipherSuite();
        if (TlsSRPUtils.isSRPCipherSuite(n2)) {
            if (this.srpIdentity != null) {
                this.loginParameters = this.srpIdentityManager.getLoginParameters(this.srpIdentity);
            }
            if (this.loginParameters == null) {
                throw new TlsFatalAlert(115);
            }
        }
        return n2;
    }

    @Override
    public void processClientExtensions(Hashtable hashtable) {
        super.processClientExtensions(hashtable);
        this.srpIdentity = TlsSRPUtils.getSRPExtension(hashtable);
    }
}

