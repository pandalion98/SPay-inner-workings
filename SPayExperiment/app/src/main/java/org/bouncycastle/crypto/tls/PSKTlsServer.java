/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.util.Vector
 */
package org.bouncycastle.crypto.tls;

import java.util.Vector;
import org.bouncycastle.crypto.agreement.DHStandardGroups;
import org.bouncycastle.crypto.params.DHParameters;
import org.bouncycastle.crypto.tls.AbstractTlsServer;
import org.bouncycastle.crypto.tls.DefaultTlsCipherFactory;
import org.bouncycastle.crypto.tls.TlsCipher;
import org.bouncycastle.crypto.tls.TlsCipherFactory;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsCredentials;
import org.bouncycastle.crypto.tls.TlsEncryptionCredentials;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsKeyExchange;
import org.bouncycastle.crypto.tls.TlsPSKIdentity;
import org.bouncycastle.crypto.tls.TlsPSKIdentityManager;
import org.bouncycastle.crypto.tls.TlsPSKKeyExchange;
import org.bouncycastle.crypto.tls.TlsServerContext;

public class PSKTlsServer
extends AbstractTlsServer {
    protected TlsPSKIdentityManager pskIdentityManager;

    public PSKTlsServer(TlsCipherFactory tlsCipherFactory, TlsPSKIdentityManager tlsPSKIdentityManager) {
        super(tlsCipherFactory);
        this.pskIdentityManager = tlsPSKIdentityManager;
    }

    public PSKTlsServer(TlsPSKIdentityManager tlsPSKIdentityManager) {
        this(new DefaultTlsCipherFactory(), tlsPSKIdentityManager);
    }

    protected TlsKeyExchange createPSKKeyExchange(int n2) {
        return new TlsPSKKeyExchange(n2, this.supportedSignatureAlgorithms, null, this.pskIdentityManager, this.getDHParameters(), this.namedCurves, this.clientECPointFormats, this.serverECPointFormats);
    }

    @Override
    public TlsCipher getCipher() {
        switch (this.selectedCipherSuite) {
            default: {
                throw new TlsFatalAlert(80);
            }
            case 139: 
            case 143: 
            case 147: 
            case 49204: {
                return this.cipherFactory.createCipher(this.context, 7, 2);
            }
            case 140: 
            case 144: 
            case 148: 
            case 49205: {
                return this.cipherFactory.createCipher(this.context, 8, 2);
            }
            case 174: 
            case 178: 
            case 182: 
            case 49207: {
                return this.cipherFactory.createCipher(this.context, 8, 3);
            }
            case 49316: 
            case 49318: {
                return this.cipherFactory.createCipher(this.context, 15, 0);
            }
            case 49320: 
            case 49322: {
                return this.cipherFactory.createCipher(this.context, 16, 0);
            }
            case 168: 
            case 170: 
            case 172: {
                return this.cipherFactory.createCipher(this.context, 10, 0);
            }
            case 141: 
            case 145: 
            case 149: 
            case 49206: {
                return this.cipherFactory.createCipher(this.context, 9, 2);
            }
            case 175: 
            case 179: 
            case 183: 
            case 49208: {
                return this.cipherFactory.createCipher(this.context, 9, 4);
            }
            case 49317: 
            case 49319: {
                return this.cipherFactory.createCipher(this.context, 17, 0);
            }
            case 49321: 
            case 49323: {
                return this.cipherFactory.createCipher(this.context, 18, 0);
            }
            case 169: 
            case 171: 
            case 173: {
                return this.cipherFactory.createCipher(this.context, 11, 0);
            }
            case 49300: 
            case 49302: 
            case 49304: 
            case 49306: {
                return this.cipherFactory.createCipher(this.context, 12, 3);
            }
            case 49294: 
            case 49296: 
            case 49298: {
                return this.cipherFactory.createCipher(this.context, 19, 0);
            }
            case 49301: 
            case 49303: 
            case 49305: 
            case 49307: {
                return this.cipherFactory.createCipher(this.context, 13, 4);
            }
            case 49295: 
            case 49297: 
            case 49299: {
                return this.cipherFactory.createCipher(this.context, 20, 0);
            }
            case 58390: 
            case 58392: 
            case 58394: 
            case 58396: {
                return this.cipherFactory.createCipher(this.context, 100, 2);
            }
            case 44: 
            case 45: 
            case 46: 
            case 49209: {
                return this.cipherFactory.createCipher(this.context, 0, 2);
            }
            case 176: 
            case 180: 
            case 184: 
            case 49210: {
                return this.cipherFactory.createCipher(this.context, 0, 3);
            }
            case 177: 
            case 181: 
            case 185: 
            case 49211: {
                return this.cipherFactory.createCipher(this.context, 0, 4);
            }
            case 138: 
            case 142: 
            case 146: 
            case 49203: {
                return this.cipherFactory.createCipher(this.context, 2, 2);
            }
            case 58391: 
            case 58393: 
            case 58395: 
            case 58397: 
        }
        return this.cipherFactory.createCipher(this.context, 101, 2);
    }

    @Override
    protected int[] getCipherSuites() {
        return new int[]{49207, 49205, 178, 144};
    }

    @Override
    public TlsCredentials getCredentials() {
        switch (this.selectedCipherSuite) {
            default: {
                throw new TlsFatalAlert(80);
            }
            case 44: 
            case 45: 
            case 138: 
            case 139: 
            case 140: 
            case 141: 
            case 142: 
            case 143: 
            case 144: 
            case 145: 
            case 168: 
            case 169: 
            case 170: 
            case 171: 
            case 174: 
            case 175: 
            case 176: 
            case 177: 
            case 178: 
            case 179: 
            case 180: 
            case 181: 
            case 49203: 
            case 49204: 
            case 49205: 
            case 49206: 
            case 49207: 
            case 49208: 
            case 49209: 
            case 49210: 
            case 49211: 
            case 49316: 
            case 49317: 
            case 49318: 
            case 49319: 
            case 49320: 
            case 49321: 
            case 49322: 
            case 49323: 
            case 58390: 
            case 58391: 
            case 58392: 
            case 58393: 
            case 58396: 
            case 58397: {
                return null;
            }
            case 46: 
            case 146: 
            case 147: 
            case 148: 
            case 149: 
            case 172: 
            case 173: 
            case 182: 
            case 183: 
            case 184: 
            case 185: 
            case 58394: 
            case 58395: 
        }
        return this.getRSAEncryptionCredentials();
    }

    protected DHParameters getDHParameters() {
        return DHStandardGroups.rfc5114_1024_160;
    }

    @Override
    public TlsKeyExchange getKeyExchange() {
        switch (this.selectedCipherSuite) {
            default: {
                throw new TlsFatalAlert(80);
            }
            case 45: 
            case 142: 
            case 143: 
            case 144: 
            case 145: 
            case 170: 
            case 171: 
            case 178: 
            case 179: 
            case 180: 
            case 181: 
            case 49296: 
            case 49297: 
            case 49302: 
            case 49303: 
            case 49318: 
            case 49319: 
            case 49322: 
            case 49323: 
            case 58396: 
            case 58397: {
                return this.createPSKKeyExchange(14);
            }
            case 49203: 
            case 49204: 
            case 49205: 
            case 49206: 
            case 49207: 
            case 49208: 
            case 49209: 
            case 49210: 
            case 49211: 
            case 49306: 
            case 49307: 
            case 58392: 
            case 58393: {
                return this.createPSKKeyExchange(24);
            }
            case 44: 
            case 138: 
            case 139: 
            case 140: 
            case 141: 
            case 168: 
            case 169: 
            case 174: 
            case 175: 
            case 176: 
            case 177: 
            case 49294: 
            case 49295: 
            case 49300: 
            case 49301: 
            case 49316: 
            case 49317: 
            case 49320: 
            case 49321: 
            case 58390: 
            case 58391: {
                return this.createPSKKeyExchange(13);
            }
            case 46: 
            case 146: 
            case 147: 
            case 148: 
            case 149: 
            case 172: 
            case 173: 
            case 182: 
            case 183: 
            case 184: 
            case 185: 
            case 49298: 
            case 49299: 
            case 49304: 
            case 49305: 
            case 58394: 
            case 58395: 
        }
        return this.createPSKKeyExchange(15);
    }

    protected TlsEncryptionCredentials getRSAEncryptionCredentials() {
        throw new TlsFatalAlert(80);
    }
}

