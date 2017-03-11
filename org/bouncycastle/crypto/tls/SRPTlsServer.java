package org.bouncycastle.crypto.tls;

import java.util.Hashtable;

public class SRPTlsServer extends AbstractTlsServer {
    protected TlsSRPLoginParameters loginParameters;
    protected byte[] srpIdentity;
    protected TlsSRPIdentityManager srpIdentityManager;

    public SRPTlsServer(TlsCipherFactory tlsCipherFactory, TlsSRPIdentityManager tlsSRPIdentityManager) {
        super(tlsCipherFactory);
        this.srpIdentity = null;
        this.loginParameters = null;
        this.srpIdentityManager = tlsSRPIdentityManager;
    }

    public SRPTlsServer(TlsSRPIdentityManager tlsSRPIdentityManager) {
        this(new DefaultTlsCipherFactory(), tlsSRPIdentityManager);
    }

    protected TlsKeyExchange createSRPKeyExchange(int i) {
        return new TlsSRPKeyExchange(i, this.supportedSignatureAlgorithms, this.srpIdentity, this.loginParameters);
    }

    public TlsCipher getCipher() {
        switch (this.selectedCipherSuite) {
            case CipherSuite.TLS_SRP_SHA_WITH_3DES_EDE_CBC_SHA /*49178*/:
            case CipherSuite.TLS_SRP_SHA_RSA_WITH_3DES_EDE_CBC_SHA /*49179*/:
            case CipherSuite.TLS_SRP_SHA_DSS_WITH_3DES_EDE_CBC_SHA /*49180*/:
                return this.cipherFactory.createCipher(this.context, 7, 2);
            case CipherSuite.TLS_SRP_SHA_WITH_AES_128_CBC_SHA /*49181*/:
            case CipherSuite.TLS_SRP_SHA_RSA_WITH_AES_128_CBC_SHA /*49182*/:
            case CipherSuite.TLS_SRP_SHA_DSS_WITH_AES_128_CBC_SHA /*49183*/:
                return this.cipherFactory.createCipher(this.context, 8, 2);
            case CipherSuite.TLS_SRP_SHA_WITH_AES_256_CBC_SHA /*49184*/:
            case CipherSuite.TLS_SRP_SHA_RSA_WITH_AES_256_CBC_SHA /*49185*/:
            case CipherSuite.TLS_SRP_SHA_DSS_WITH_AES_256_CBC_SHA /*49186*/:
                return this.cipherFactory.createCipher(this.context, 9, 2);
            default:
                throw new TlsFatalAlert((short) 80);
        }
    }

    protected int[] getCipherSuites() {
        return new int[]{CipherSuite.TLS_SRP_SHA_DSS_WITH_AES_256_CBC_SHA, CipherSuite.TLS_SRP_SHA_DSS_WITH_AES_128_CBC_SHA, CipherSuite.TLS_SRP_SHA_RSA_WITH_AES_256_CBC_SHA, CipherSuite.TLS_SRP_SHA_RSA_WITH_AES_128_CBC_SHA, CipherSuite.TLS_SRP_SHA_WITH_AES_256_CBC_SHA, CipherSuite.TLS_SRP_SHA_WITH_AES_128_CBC_SHA};
    }

    public TlsCredentials getCredentials() {
        switch (this.selectedCipherSuite) {
            case CipherSuite.TLS_SRP_SHA_WITH_3DES_EDE_CBC_SHA /*49178*/:
            case CipherSuite.TLS_SRP_SHA_WITH_AES_128_CBC_SHA /*49181*/:
            case CipherSuite.TLS_SRP_SHA_WITH_AES_256_CBC_SHA /*49184*/:
                return null;
            case CipherSuite.TLS_SRP_SHA_RSA_WITH_3DES_EDE_CBC_SHA /*49179*/:
            case CipherSuite.TLS_SRP_SHA_RSA_WITH_AES_128_CBC_SHA /*49182*/:
            case CipherSuite.TLS_SRP_SHA_RSA_WITH_AES_256_CBC_SHA /*49185*/:
                return getRSASignerCredentials();
            case CipherSuite.TLS_SRP_SHA_DSS_WITH_3DES_EDE_CBC_SHA /*49180*/:
            case CipherSuite.TLS_SRP_SHA_DSS_WITH_AES_128_CBC_SHA /*49183*/:
            case CipherSuite.TLS_SRP_SHA_DSS_WITH_AES_256_CBC_SHA /*49186*/:
                return getDSASignerCredentials();
            default:
                throw new TlsFatalAlert((short) 80);
        }
    }

    protected TlsSignerCredentials getDSASignerCredentials() {
        throw new TlsFatalAlert((short) 80);
    }

    public TlsKeyExchange getKeyExchange() {
        switch (this.selectedCipherSuite) {
            case CipherSuite.TLS_SRP_SHA_WITH_3DES_EDE_CBC_SHA /*49178*/:
            case CipherSuite.TLS_SRP_SHA_WITH_AES_128_CBC_SHA /*49181*/:
            case CipherSuite.TLS_SRP_SHA_WITH_AES_256_CBC_SHA /*49184*/:
                return createSRPKeyExchange(21);
            case CipherSuite.TLS_SRP_SHA_RSA_WITH_3DES_EDE_CBC_SHA /*49179*/:
            case CipherSuite.TLS_SRP_SHA_RSA_WITH_AES_128_CBC_SHA /*49182*/:
            case CipherSuite.TLS_SRP_SHA_RSA_WITH_AES_256_CBC_SHA /*49185*/:
                return createSRPKeyExchange(23);
            case CipherSuite.TLS_SRP_SHA_DSS_WITH_3DES_EDE_CBC_SHA /*49180*/:
            case CipherSuite.TLS_SRP_SHA_DSS_WITH_AES_128_CBC_SHA /*49183*/:
            case CipherSuite.TLS_SRP_SHA_DSS_WITH_AES_256_CBC_SHA /*49186*/:
                return createSRPKeyExchange(22);
            default:
                throw new TlsFatalAlert((short) 80);
        }
    }

    protected TlsSignerCredentials getRSASignerCredentials() {
        throw new TlsFatalAlert((short) 80);
    }

    public int getSelectedCipherSuite() {
        int selectedCipherSuite = super.getSelectedCipherSuite();
        if (TlsSRPUtils.isSRPCipherSuite(selectedCipherSuite)) {
            if (this.srpIdentity != null) {
                this.loginParameters = this.srpIdentityManager.getLoginParameters(this.srpIdentity);
            }
            if (this.loginParameters == null) {
                throw new TlsFatalAlert(AlertDescription.unknown_psk_identity);
            }
        }
        return selectedCipherSuite;
    }

    public void processClientExtensions(Hashtable hashtable) {
        super.processClientExtensions(hashtable);
        this.srpIdentity = TlsSRPUtils.getSRPExtension(hashtable);
    }
}
