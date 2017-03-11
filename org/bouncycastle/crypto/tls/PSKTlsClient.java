package org.bouncycastle.crypto.tls;

public class PSKTlsClient extends AbstractTlsClient {
    protected TlsPSKIdentity pskIdentity;

    public PSKTlsClient(TlsCipherFactory tlsCipherFactory, TlsPSKIdentity tlsPSKIdentity) {
        super(tlsCipherFactory);
        this.pskIdentity = tlsPSKIdentity;
    }

    public PSKTlsClient(TlsPSKIdentity tlsPSKIdentity) {
        this(new DefaultTlsCipherFactory(), tlsPSKIdentity);
    }

    protected TlsKeyExchange createPSKKeyExchange(int i) {
        return new TlsPSKKeyExchange(i, this.supportedSignatureAlgorithms, this.pskIdentity, null, null, this.namedCurves, this.clientECPointFormats, this.serverECPointFormats);
    }

    public TlsAuthentication getAuthentication() {
        throw new TlsFatalAlert((short) 80);
    }

    public TlsCipher getCipher() {
        switch (this.selectedCipherSuite) {
            case CipherSuite.TLS_PSK_WITH_NULL_SHA /*44*/:
            case CipherSuite.TLS_DHE_PSK_WITH_NULL_SHA /*45*/:
            case CipherSuite.TLS_RSA_PSK_WITH_NULL_SHA /*46*/:
            case CipherSuite.TLS_ECDHE_PSK_WITH_NULL_SHA /*49209*/:
                return this.cipherFactory.createCipher(this.context, 0, 2);
            case CipherSuite.TLS_PSK_WITH_RC4_128_SHA /*138*/:
            case CipherSuite.TLS_DHE_PSK_WITH_RC4_128_SHA /*142*/:
            case CipherSuite.TLS_RSA_PSK_WITH_RC4_128_SHA /*146*/:
            case CipherSuite.TLS_ECDHE_PSK_WITH_RC4_128_SHA /*49203*/:
                return this.cipherFactory.createCipher(this.context, 2, 2);
            case CipherSuite.TLS_PSK_WITH_3DES_EDE_CBC_SHA /*139*/:
            case CipherSuite.TLS_DHE_PSK_WITH_3DES_EDE_CBC_SHA /*143*/:
            case CipherSuite.TLS_RSA_PSK_WITH_3DES_EDE_CBC_SHA /*147*/:
            case CipherSuite.TLS_ECDHE_PSK_WITH_3DES_EDE_CBC_SHA /*49204*/:
                return this.cipherFactory.createCipher(this.context, 7, 2);
            case CipherSuite.TLS_PSK_WITH_AES_128_CBC_SHA /*140*/:
            case CipherSuite.TLS_DHE_PSK_WITH_AES_128_CBC_SHA /*144*/:
            case CipherSuite.TLS_RSA_PSK_WITH_AES_128_CBC_SHA /*148*/:
            case CipherSuite.TLS_ECDHE_PSK_WITH_AES_128_CBC_SHA /*49205*/:
                return this.cipherFactory.createCipher(this.context, 8, 2);
            case CipherSuite.TLS_PSK_WITH_AES_256_CBC_SHA /*141*/:
            case CipherSuite.TLS_DHE_PSK_WITH_AES_256_CBC_SHA /*145*/:
            case CipherSuite.TLS_RSA_PSK_WITH_AES_256_CBC_SHA /*149*/:
            case CipherSuite.TLS_ECDHE_PSK_WITH_AES_256_CBC_SHA /*49206*/:
                return this.cipherFactory.createCipher(this.context, 9, 2);
            case CipherSuite.TLS_PSK_WITH_AES_128_GCM_SHA256 /*168*/:
            case CipherSuite.TLS_DHE_PSK_WITH_AES_128_GCM_SHA256 /*170*/:
            case CipherSuite.TLS_RSA_PSK_WITH_AES_128_GCM_SHA256 /*172*/:
                return this.cipherFactory.createCipher(this.context, 10, 0);
            case CipherSuite.TLS_PSK_WITH_AES_256_GCM_SHA384 /*169*/:
            case CipherSuite.TLS_DHE_PSK_WITH_AES_256_GCM_SHA384 /*171*/:
            case CipherSuite.TLS_RSA_PSK_WITH_AES_256_GCM_SHA384 /*173*/:
                return this.cipherFactory.createCipher(this.context, 11, 0);
            case CipherSuite.TLS_PSK_WITH_AES_128_CBC_SHA256 /*174*/:
            case CipherSuite.TLS_DHE_PSK_WITH_AES_128_CBC_SHA256 /*178*/:
            case CipherSuite.TLS_RSA_PSK_WITH_AES_128_CBC_SHA256 /*182*/:
            case CipherSuite.TLS_ECDHE_PSK_WITH_AES_128_CBC_SHA256 /*49207*/:
                return this.cipherFactory.createCipher(this.context, 8, 3);
            case CipherSuite.TLS_PSK_WITH_AES_256_CBC_SHA384 /*175*/:
            case CipherSuite.TLS_DHE_PSK_WITH_AES_256_CBC_SHA384 /*179*/:
            case CipherSuite.TLS_RSA_PSK_WITH_AES_256_CBC_SHA384 /*183*/:
            case CipherSuite.TLS_ECDHE_PSK_WITH_AES_256_CBC_SHA384 /*49208*/:
                return this.cipherFactory.createCipher(this.context, 9, 4);
            case CipherSuite.TLS_PSK_WITH_NULL_SHA256 /*176*/:
            case CipherSuite.TLS_DHE_PSK_WITH_NULL_SHA256 /*180*/:
            case CipherSuite.TLS_RSA_PSK_WITH_NULL_SHA256 /*184*/:
            case CipherSuite.TLS_ECDHE_PSK_WITH_NULL_SHA256 /*49210*/:
                return this.cipherFactory.createCipher(this.context, 0, 3);
            case CipherSuite.TLS_PSK_WITH_NULL_SHA384 /*177*/:
            case CipherSuite.TLS_DHE_PSK_WITH_NULL_SHA384 /*181*/:
            case CipherSuite.TLS_RSA_PSK_WITH_NULL_SHA384 /*185*/:
            case CipherSuite.TLS_ECDHE_PSK_WITH_NULL_SHA384 /*49211*/:
                return this.cipherFactory.createCipher(this.context, 0, 4);
            case CipherSuite.TLS_PSK_WITH_CAMELLIA_128_GCM_SHA256 /*49294*/:
            case CipherSuite.TLS_DHE_PSK_WITH_CAMELLIA_128_GCM_SHA256 /*49296*/:
            case CipherSuite.TLS_RSA_PSK_WITH_CAMELLIA_128_GCM_SHA256 /*49298*/:
                return this.cipherFactory.createCipher(this.context, 19, 0);
            case CipherSuite.TLS_PSK_WITH_CAMELLIA_256_GCM_SHA384 /*49295*/:
            case CipherSuite.TLS_DHE_PSK_WITH_CAMELLIA_256_GCM_SHA384 /*49297*/:
            case CipherSuite.TLS_RSA_PSK_WITH_CAMELLIA_256_GCM_SHA384 /*49299*/:
                return this.cipherFactory.createCipher(this.context, 20, 0);
            case CipherSuite.TLS_PSK_WITH_CAMELLIA_128_CBC_SHA256 /*49300*/:
            case CipherSuite.TLS_DHE_PSK_WITH_CAMELLIA_128_CBC_SHA256 /*49302*/:
            case CipherSuite.TLS_RSA_PSK_WITH_CAMELLIA_128_CBC_SHA256 /*49304*/:
            case CipherSuite.TLS_ECDHE_PSK_WITH_CAMELLIA_128_CBC_SHA256 /*49306*/:
                return this.cipherFactory.createCipher(this.context, 12, 3);
            case CipherSuite.TLS_PSK_WITH_CAMELLIA_256_CBC_SHA384 /*49301*/:
            case CipherSuite.TLS_DHE_PSK_WITH_CAMELLIA_256_CBC_SHA384 /*49303*/:
            case CipherSuite.TLS_RSA_PSK_WITH_CAMELLIA_256_CBC_SHA384 /*49305*/:
            case CipherSuite.TLS_ECDHE_PSK_WITH_CAMELLIA_256_CBC_SHA384 /*49307*/:
                return this.cipherFactory.createCipher(this.context, 13, 4);
            case CipherSuite.TLS_PSK_WITH_AES_128_CCM /*49316*/:
            case CipherSuite.TLS_DHE_PSK_WITH_AES_128_CCM /*49318*/:
                return this.cipherFactory.createCipher(this.context, 15, 0);
            case CipherSuite.TLS_PSK_WITH_AES_256_CCM /*49317*/:
            case CipherSuite.TLS_DHE_PSK_WITH_AES_256_CCM /*49319*/:
                return this.cipherFactory.createCipher(this.context, 17, 0);
            case CipherSuite.TLS_PSK_WITH_AES_128_CCM_8 /*49320*/:
            case CipherSuite.TLS_PSK_DHE_WITH_AES_128_CCM_8 /*49322*/:
                return this.cipherFactory.createCipher(this.context, 16, 0);
            case CipherSuite.TLS_PSK_WITH_AES_256_CCM_8 /*49321*/:
            case CipherSuite.TLS_PSK_DHE_WITH_AES_256_CCM_8 /*49323*/:
                return this.cipherFactory.createCipher(this.context, 18, 0);
            case CipherSuite.TLS_PSK_WITH_ESTREAM_SALSA20_SHA1 /*58390*/:
            case CipherSuite.TLS_ECDHE_PSK_WITH_ESTREAM_SALSA20_SHA1 /*58392*/:
            case CipherSuite.TLS_RSA_PSK_WITH_ESTREAM_SALSA20_SHA1 /*58394*/:
            case CipherSuite.TLS_DHE_PSK_WITH_ESTREAM_SALSA20_SHA1 /*58396*/:
                return this.cipherFactory.createCipher(this.context, 100, 2);
            case CipherSuite.TLS_PSK_WITH_SALSA20_SHA1 /*58391*/:
            case CipherSuite.TLS_ECDHE_PSK_WITH_SALSA20_SHA1 /*58393*/:
            case CipherSuite.TLS_RSA_PSK_WITH_SALSA20_SHA1 /*58395*/:
            case CipherSuite.TLS_DHE_PSK_WITH_SALSA20_SHA1 /*58397*/:
                return this.cipherFactory.createCipher(this.context, ExtensionType.negotiated_ff_dhe_groups, 2);
            default:
                throw new TlsFatalAlert((short) 80);
        }
    }

    public int[] getCipherSuites() {
        return new int[]{CipherSuite.TLS_ECDHE_PSK_WITH_AES_128_CBC_SHA256, CipherSuite.TLS_ECDHE_PSK_WITH_AES_128_CBC_SHA, CipherSuite.TLS_DHE_PSK_WITH_AES_128_CBC_SHA256, CipherSuite.TLS_DHE_PSK_WITH_AES_128_CBC_SHA};
    }

    public TlsKeyExchange getKeyExchange() {
        switch (this.selectedCipherSuite) {
            case CipherSuite.TLS_PSK_WITH_NULL_SHA /*44*/:
            case CipherSuite.TLS_PSK_WITH_RC4_128_SHA /*138*/:
            case CipherSuite.TLS_PSK_WITH_3DES_EDE_CBC_SHA /*139*/:
            case CipherSuite.TLS_PSK_WITH_AES_128_CBC_SHA /*140*/:
            case CipherSuite.TLS_PSK_WITH_AES_256_CBC_SHA /*141*/:
            case CipherSuite.TLS_PSK_WITH_AES_128_GCM_SHA256 /*168*/:
            case CipherSuite.TLS_PSK_WITH_AES_256_GCM_SHA384 /*169*/:
            case CipherSuite.TLS_PSK_WITH_AES_128_CBC_SHA256 /*174*/:
            case CipherSuite.TLS_PSK_WITH_AES_256_CBC_SHA384 /*175*/:
            case CipherSuite.TLS_PSK_WITH_NULL_SHA256 /*176*/:
            case CipherSuite.TLS_PSK_WITH_NULL_SHA384 /*177*/:
            case CipherSuite.TLS_PSK_WITH_CAMELLIA_128_GCM_SHA256 /*49294*/:
            case CipherSuite.TLS_PSK_WITH_CAMELLIA_256_GCM_SHA384 /*49295*/:
            case CipherSuite.TLS_PSK_WITH_CAMELLIA_128_CBC_SHA256 /*49300*/:
            case CipherSuite.TLS_PSK_WITH_CAMELLIA_256_CBC_SHA384 /*49301*/:
            case CipherSuite.TLS_PSK_WITH_AES_128_CCM /*49316*/:
            case CipherSuite.TLS_PSK_WITH_AES_256_CCM /*49317*/:
            case CipherSuite.TLS_PSK_WITH_AES_128_CCM_8 /*49320*/:
            case CipherSuite.TLS_PSK_WITH_AES_256_CCM_8 /*49321*/:
            case CipherSuite.TLS_PSK_WITH_ESTREAM_SALSA20_SHA1 /*58390*/:
            case CipherSuite.TLS_PSK_WITH_SALSA20_SHA1 /*58391*/:
                return createPSKKeyExchange(13);
            case CipherSuite.TLS_DHE_PSK_WITH_NULL_SHA /*45*/:
            case CipherSuite.TLS_DHE_PSK_WITH_RC4_128_SHA /*142*/:
            case CipherSuite.TLS_DHE_PSK_WITH_3DES_EDE_CBC_SHA /*143*/:
            case CipherSuite.TLS_DHE_PSK_WITH_AES_128_CBC_SHA /*144*/:
            case CipherSuite.TLS_DHE_PSK_WITH_AES_256_CBC_SHA /*145*/:
            case CipherSuite.TLS_DHE_PSK_WITH_AES_128_GCM_SHA256 /*170*/:
            case CipherSuite.TLS_DHE_PSK_WITH_AES_256_GCM_SHA384 /*171*/:
            case CipherSuite.TLS_DHE_PSK_WITH_AES_128_CBC_SHA256 /*178*/:
            case CipherSuite.TLS_DHE_PSK_WITH_AES_256_CBC_SHA384 /*179*/:
            case CipherSuite.TLS_DHE_PSK_WITH_NULL_SHA256 /*180*/:
            case CipherSuite.TLS_DHE_PSK_WITH_NULL_SHA384 /*181*/:
            case CipherSuite.TLS_DHE_PSK_WITH_CAMELLIA_128_GCM_SHA256 /*49296*/:
            case CipherSuite.TLS_DHE_PSK_WITH_CAMELLIA_256_GCM_SHA384 /*49297*/:
            case CipherSuite.TLS_DHE_PSK_WITH_CAMELLIA_128_CBC_SHA256 /*49302*/:
            case CipherSuite.TLS_DHE_PSK_WITH_CAMELLIA_256_CBC_SHA384 /*49303*/:
            case CipherSuite.TLS_DHE_PSK_WITH_AES_128_CCM /*49318*/:
            case CipherSuite.TLS_DHE_PSK_WITH_AES_256_CCM /*49319*/:
            case CipherSuite.TLS_PSK_DHE_WITH_AES_128_CCM_8 /*49322*/:
            case CipherSuite.TLS_PSK_DHE_WITH_AES_256_CCM_8 /*49323*/:
            case CipherSuite.TLS_DHE_PSK_WITH_ESTREAM_SALSA20_SHA1 /*58396*/:
            case CipherSuite.TLS_DHE_PSK_WITH_SALSA20_SHA1 /*58397*/:
                return createPSKKeyExchange(14);
            case CipherSuite.TLS_RSA_PSK_WITH_NULL_SHA /*46*/:
            case CipherSuite.TLS_RSA_PSK_WITH_RC4_128_SHA /*146*/:
            case CipherSuite.TLS_RSA_PSK_WITH_3DES_EDE_CBC_SHA /*147*/:
            case CipherSuite.TLS_RSA_PSK_WITH_AES_128_CBC_SHA /*148*/:
            case CipherSuite.TLS_RSA_PSK_WITH_AES_256_CBC_SHA /*149*/:
            case CipherSuite.TLS_RSA_PSK_WITH_AES_128_GCM_SHA256 /*172*/:
            case CipherSuite.TLS_RSA_PSK_WITH_AES_256_GCM_SHA384 /*173*/:
            case CipherSuite.TLS_RSA_PSK_WITH_AES_128_CBC_SHA256 /*182*/:
            case CipherSuite.TLS_RSA_PSK_WITH_AES_256_CBC_SHA384 /*183*/:
            case CipherSuite.TLS_RSA_PSK_WITH_NULL_SHA256 /*184*/:
            case CipherSuite.TLS_RSA_PSK_WITH_NULL_SHA384 /*185*/:
            case CipherSuite.TLS_RSA_PSK_WITH_CAMELLIA_128_GCM_SHA256 /*49298*/:
            case CipherSuite.TLS_RSA_PSK_WITH_CAMELLIA_256_GCM_SHA384 /*49299*/:
            case CipherSuite.TLS_RSA_PSK_WITH_CAMELLIA_128_CBC_SHA256 /*49304*/:
            case CipherSuite.TLS_RSA_PSK_WITH_CAMELLIA_256_CBC_SHA384 /*49305*/:
            case CipherSuite.TLS_RSA_PSK_WITH_ESTREAM_SALSA20_SHA1 /*58394*/:
            case CipherSuite.TLS_RSA_PSK_WITH_SALSA20_SHA1 /*58395*/:
                return createPSKKeyExchange(15);
            case CipherSuite.TLS_ECDHE_PSK_WITH_RC4_128_SHA /*49203*/:
            case CipherSuite.TLS_ECDHE_PSK_WITH_3DES_EDE_CBC_SHA /*49204*/:
            case CipherSuite.TLS_ECDHE_PSK_WITH_AES_128_CBC_SHA /*49205*/:
            case CipherSuite.TLS_ECDHE_PSK_WITH_AES_256_CBC_SHA /*49206*/:
            case CipherSuite.TLS_ECDHE_PSK_WITH_AES_128_CBC_SHA256 /*49207*/:
            case CipherSuite.TLS_ECDHE_PSK_WITH_AES_256_CBC_SHA384 /*49208*/:
            case CipherSuite.TLS_ECDHE_PSK_WITH_NULL_SHA /*49209*/:
            case CipherSuite.TLS_ECDHE_PSK_WITH_NULL_SHA256 /*49210*/:
            case CipherSuite.TLS_ECDHE_PSK_WITH_NULL_SHA384 /*49211*/:
            case CipherSuite.TLS_ECDHE_PSK_WITH_CAMELLIA_128_CBC_SHA256 /*49306*/:
            case CipherSuite.TLS_ECDHE_PSK_WITH_CAMELLIA_256_CBC_SHA384 /*49307*/:
            case CipherSuite.TLS_ECDHE_PSK_WITH_ESTREAM_SALSA20_SHA1 /*58392*/:
            case CipherSuite.TLS_ECDHE_PSK_WITH_SALSA20_SHA1 /*58393*/:
                return createPSKKeyExchange(24);
            default:
                throw new TlsFatalAlert((short) 80);
        }
    }
}
