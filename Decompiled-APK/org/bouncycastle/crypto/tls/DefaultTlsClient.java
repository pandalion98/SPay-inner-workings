package org.bouncycastle.crypto.tls;

import org.bouncycastle.jcajce.spec.SkeinParameterSpec;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.pqc.jcajce.spec.ECCKeyGenParameterSpec;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public abstract class DefaultTlsClient extends AbstractTlsClient {
    public DefaultTlsClient(TlsCipherFactory tlsCipherFactory) {
        super(tlsCipherFactory);
    }

    protected TlsKeyExchange createDHEKeyExchange(int i) {
        return new TlsDHEKeyExchange(i, this.supportedSignatureAlgorithms, null);
    }

    protected TlsKeyExchange createDHKeyExchange(int i) {
        return new TlsDHKeyExchange(i, this.supportedSignatureAlgorithms, null);
    }

    protected TlsKeyExchange createECDHEKeyExchange(int i) {
        return new TlsECDHEKeyExchange(i, this.supportedSignatureAlgorithms, this.namedCurves, this.clientECPointFormats, this.serverECPointFormats);
    }

    protected TlsKeyExchange createECDHKeyExchange(int i) {
        return new TlsECDHKeyExchange(i, this.supportedSignatureAlgorithms, this.namedCurves, this.clientECPointFormats, this.serverECPointFormats);
    }

    protected TlsKeyExchange createRSAKeyExchange() {
        return new TlsRSAKeyExchange(this.supportedSignatureAlgorithms);
    }

    public TlsCipher getCipher() {
        switch (this.selectedCipherSuite) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                return this.cipherFactory.createCipher(this.context, 0, 1);
            case CipherSpiExt.DECRYPT_MODE /*2*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_NULL_SHA /*49153*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_NULL_SHA /*49158*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_NULL_SHA /*49163*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_NULL_SHA /*49168*/:
                return this.cipherFactory.createCipher(this.context, 0, 2);
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                return this.cipherFactory.createCipher(this.context, 2, 1);
            case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_RC4_128_SHA /*49154*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_RC4_128_SHA /*49159*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_RC4_128_SHA /*49164*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_RC4_128_SHA /*49169*/:
                return this.cipherFactory.createCipher(this.context, 2, 2);
            case NamedCurve.sect283r1 /*10*/:
            case NamedCurve.sect571k1 /*13*/:
            case X509KeyUsage.dataEncipherment /*16*/:
            case NamedCurve.secp192r1 /*19*/:
            case NamedCurve.secp256k1 /*22*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_3DES_EDE_CBC_SHA /*49155*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_3DES_EDE_CBC_SHA /*49160*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_3DES_EDE_CBC_SHA /*49165*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_3DES_EDE_CBC_SHA /*49170*/:
                return this.cipherFactory.createCipher(this.context, 7, 2);
            case CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA /*47*/:
            case SkeinParameterSpec.PARAM_TYPE_MESSAGE /*48*/:
            case CipherSuite.TLS_DH_RSA_WITH_AES_128_CBC_SHA /*49*/:
            case ECCKeyGenParameterSpec.DEFAULT_T /*50*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA /*51*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA /*49156*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA /*49161*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_AES_128_CBC_SHA /*49166*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA /*49171*/:
                return this.cipherFactory.createCipher(this.context, 8, 2);
            case CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA /*53*/:
            case CipherSuite.TLS_DH_DSS_WITH_AES_256_CBC_SHA /*54*/:
            case CipherSuite.TLS_DH_RSA_WITH_AES_256_CBC_SHA /*55*/:
            case CipherSuite.TLS_DHE_DSS_WITH_AES_256_CBC_SHA /*56*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA /*57*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA /*49157*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA /*49162*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_AES_256_CBC_SHA /*49167*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA /*49172*/:
                return this.cipherFactory.createCipher(this.context, 9, 2);
            case CipherSuite.TLS_RSA_WITH_NULL_SHA256 /*59*/:
                return this.cipherFactory.createCipher(this.context, 0, 3);
            case CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA256 /*60*/:
            case CipherSuite.TLS_DH_DSS_WITH_AES_128_CBC_SHA256 /*62*/:
            case SkeinParameterSpec.PARAM_TYPE_OUTPUT /*63*/:
            case X509KeyUsage.nonRepudiation /*64*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA256 /*103*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256 /*49187*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA256 /*49189*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256 /*49191*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_AES_128_CBC_SHA256 /*49193*/:
                return this.cipherFactory.createCipher(this.context, 8, 3);
            case CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA256 /*61*/:
            case CipherSuite.TLS_DH_DSS_WITH_AES_256_CBC_SHA256 /*104*/:
            case CipherSuite.TLS_DH_RSA_WITH_AES_256_CBC_SHA256 /*105*/:
            case CipherSuite.TLS_DHE_DSS_WITH_AES_256_CBC_SHA256 /*106*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA256 /*107*/:
                return this.cipherFactory.createCipher(this.context, 9, 3);
            case CipherSuite.TLS_RSA_WITH_CAMELLIA_128_CBC_SHA /*65*/:
            case CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_128_CBC_SHA /*66*/:
            case CipherSuite.TLS_DH_RSA_WITH_CAMELLIA_128_CBC_SHA /*67*/:
            case CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_128_CBC_SHA /*68*/:
            case CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_128_CBC_SHA /*69*/:
                return this.cipherFactory.createCipher(this.context, 12, 2);
            case CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA /*132*/:
            case CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_256_CBC_SHA /*133*/:
            case CipherSuite.TLS_DH_RSA_WITH_CAMELLIA_256_CBC_SHA /*134*/:
            case CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_256_CBC_SHA /*135*/:
            case CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_256_CBC_SHA /*136*/:
                return this.cipherFactory.createCipher(this.context, 13, 2);
            case CipherSuite.TLS_RSA_WITH_SEED_CBC_SHA /*150*/:
            case CipherSuite.TLS_DH_DSS_WITH_SEED_CBC_SHA /*151*/:
            case CipherSuite.TLS_DH_RSA_WITH_SEED_CBC_SHA /*152*/:
            case CipherSuite.TLS_DHE_DSS_WITH_SEED_CBC_SHA /*153*/:
            case CipherSuite.TLS_DHE_RSA_WITH_SEED_CBC_SHA /*154*/:
                return this.cipherFactory.createCipher(this.context, 14, 2);
            case CipherSuite.TLS_RSA_WITH_AES_128_GCM_SHA256 /*156*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256 /*158*/:
            case CipherSuite.TLS_DH_RSA_WITH_AES_128_GCM_SHA256 /*160*/:
            case CipherSuite.TLS_DHE_DSS_WITH_AES_128_GCM_SHA256 /*162*/:
            case CipherSuite.TLS_DH_DSS_WITH_AES_128_GCM_SHA256 /*164*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256 /*49195*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_AES_128_GCM_SHA256 /*49197*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256 /*49199*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_AES_128_GCM_SHA256 /*49201*/:
                return this.cipherFactory.createCipher(this.context, 10, 0);
            case CipherSuite.TLS_RSA_WITH_AES_256_GCM_SHA384 /*157*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_256_GCM_SHA384 /*159*/:
            case CipherSuite.TLS_DH_RSA_WITH_AES_256_GCM_SHA384 /*161*/:
            case CipherSuite.TLS_DHE_DSS_WITH_AES_256_GCM_SHA384 /*163*/:
            case CipherSuite.TLS_DH_DSS_WITH_AES_256_GCM_SHA384 /*165*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384 /*49196*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_AES_256_GCM_SHA384 /*49198*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384 /*49200*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_AES_256_GCM_SHA384 /*49202*/:
                return this.cipherFactory.createCipher(this.context, 11, 0);
            case CipherSuite.TLS_RSA_WITH_CAMELLIA_128_CBC_SHA256 /*186*/:
            case CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_128_CBC_SHA256 /*187*/:
            case CipherSuite.TLS_DH_RSA_WITH_CAMELLIA_128_CBC_SHA256 /*188*/:
            case CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_128_CBC_SHA256 /*189*/:
            case CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_128_CBC_SHA256 /*190*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_CAMELLIA_128_CBC_SHA256 /*49266*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_CAMELLIA_128_CBC_SHA256 /*49268*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_CAMELLIA_128_CBC_SHA256 /*49270*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_CAMELLIA_128_CBC_SHA256 /*49272*/:
                return this.cipherFactory.createCipher(this.context, 12, 3);
            case CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256 /*192*/:
            case CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_256_CBC_SHA256 /*193*/:
            case CipherSuite.TLS_DH_RSA_WITH_CAMELLIA_256_CBC_SHA256 /*194*/:
            case CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_256_CBC_SHA256 /*195*/:
            case CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_256_CBC_SHA256 /*196*/:
                return this.cipherFactory.createCipher(this.context, 13, 3);
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384 /*49188*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA384 /*49190*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384 /*49192*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_AES_256_CBC_SHA384 /*49194*/:
                return this.cipherFactory.createCipher(this.context, 9, 4);
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_CAMELLIA_256_CBC_SHA384 /*49267*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_CAMELLIA_256_CBC_SHA384 /*49269*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_CAMELLIA_256_CBC_SHA384 /*49271*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_CAMELLIA_256_CBC_SHA384 /*49273*/:
                return this.cipherFactory.createCipher(this.context, 13, 4);
            case CipherSuite.TLS_RSA_WITH_CAMELLIA_128_GCM_SHA256 /*49274*/:
            case CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_128_GCM_SHA256 /*49276*/:
            case CipherSuite.TLS_DH_RSA_WITH_CAMELLIA_128_GCM_SHA256 /*49278*/:
            case CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_128_GCM_SHA256 /*49280*/:
            case CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_128_GCM_SHA256 /*49282*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_CAMELLIA_128_GCM_SHA256 /*49286*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_CAMELLIA_128_GCM_SHA256 /*49288*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_CAMELLIA_128_GCM_SHA256 /*49290*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_CAMELLIA_128_GCM_SHA256 /*49292*/:
                return this.cipherFactory.createCipher(this.context, 19, 0);
            case CipherSuite.TLS_RSA_WITH_CAMELLIA_256_GCM_SHA384 /*49275*/:
            case CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_256_GCM_SHA384 /*49277*/:
            case CipherSuite.TLS_DH_RSA_WITH_CAMELLIA_256_GCM_SHA384 /*49279*/:
            case CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_256_GCM_SHA384 /*49281*/:
            case CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_256_GCM_SHA384 /*49283*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_CAMELLIA_256_GCM_SHA384 /*49287*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_CAMELLIA_256_GCM_SHA384 /*49289*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_CAMELLIA_256_GCM_SHA384 /*49291*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_CAMELLIA_256_GCM_SHA384 /*49293*/:
                return this.cipherFactory.createCipher(this.context, 20, 0);
            case CipherSuite.TLS_RSA_WITH_AES_128_CCM /*49308*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_128_CCM /*49310*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CCM /*49324*/:
                return this.cipherFactory.createCipher(this.context, 15, 0);
            case CipherSuite.TLS_RSA_WITH_AES_256_CCM /*49309*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_256_CCM /*49311*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CCM /*49325*/:
                return this.cipherFactory.createCipher(this.context, 17, 0);
            case CipherSuite.TLS_RSA_WITH_AES_128_CCM_8 /*49312*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_128_CCM_8 /*49314*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CCM_8 /*49326*/:
                return this.cipherFactory.createCipher(this.context, 16, 0);
            case CipherSuite.TLS_RSA_WITH_AES_256_CCM_8 /*49313*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_256_CCM_8 /*49315*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CCM_8 /*49327*/:
                return this.cipherFactory.createCipher(this.context, 18, 0);
            case CipherSuite.TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256 /*52243*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256 /*52244*/:
            case CipherSuite.TLS_DHE_RSA_WITH_CHACHA20_POLY1305_SHA256 /*52245*/:
                return this.cipherFactory.createCipher(this.context, EncryptionAlgorithm.AEAD_CHACHA20_POLY1305, 0);
            case CipherSuite.TLS_RSA_WITH_ESTREAM_SALSA20_SHA1 /*58384*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_ESTREAM_SALSA20_SHA1 /*58386*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_ESTREAM_SALSA20_SHA1 /*58388*/:
            case CipherSuite.TLS_DHE_RSA_WITH_ESTREAM_SALSA20_SHA1 /*58398*/:
                return this.cipherFactory.createCipher(this.context, 100, 2);
            case CipherSuite.TLS_RSA_WITH_SALSA20_SHA1 /*58385*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_SALSA20_SHA1 /*58387*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_SALSA20_SHA1 /*58389*/:
            case CipherSuite.TLS_DHE_RSA_WITH_SALSA20_SHA1 /*58399*/:
                return this.cipherFactory.createCipher(this.context, ExtensionType.negotiated_ff_dhe_groups, 2);
            default:
                throw new TlsFatalAlert((short) 80);
        }
    }

    public int[] getCipherSuites() {
        return new int[]{CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256, CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA, CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256, CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA, CipherSuite.TLS_DHE_DSS_WITH_AES_128_GCM_SHA256, 64, 50, CipherSuite.TLS_RSA_WITH_AES_128_GCM_SHA256, 60, 47};
    }

    public TlsKeyExchange getKeyExchange() {
        switch (this.selectedCipherSuite) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
            case CipherSpiExt.DECRYPT_MODE /*2*/:
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
            case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
            case NamedCurve.sect283r1 /*10*/:
            case CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA /*47*/:
            case CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA /*53*/:
            case CipherSuite.TLS_RSA_WITH_NULL_SHA256 /*59*/:
            case CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA256 /*60*/:
            case CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA256 /*61*/:
            case CipherSuite.TLS_RSA_WITH_CAMELLIA_128_CBC_SHA /*65*/:
            case CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA /*132*/:
            case CipherSuite.TLS_RSA_WITH_SEED_CBC_SHA /*150*/:
            case CipherSuite.TLS_RSA_WITH_AES_128_GCM_SHA256 /*156*/:
            case CipherSuite.TLS_RSA_WITH_AES_256_GCM_SHA384 /*157*/:
            case CipherSuite.TLS_RSA_WITH_CAMELLIA_128_CBC_SHA256 /*186*/:
            case CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256 /*192*/:
            case CipherSuite.TLS_RSA_WITH_CAMELLIA_128_GCM_SHA256 /*49274*/:
            case CipherSuite.TLS_RSA_WITH_CAMELLIA_256_GCM_SHA384 /*49275*/:
            case CipherSuite.TLS_RSA_WITH_AES_128_CCM /*49308*/:
            case CipherSuite.TLS_RSA_WITH_AES_256_CCM /*49309*/:
            case CipherSuite.TLS_RSA_WITH_AES_128_CCM_8 /*49312*/:
            case CipherSuite.TLS_RSA_WITH_AES_256_CCM_8 /*49313*/:
            case CipherSuite.TLS_RSA_WITH_ESTREAM_SALSA20_SHA1 /*58384*/:
            case CipherSuite.TLS_RSA_WITH_SALSA20_SHA1 /*58385*/:
                return createRSAKeyExchange();
            case NamedCurve.sect571k1 /*13*/:
            case SkeinParameterSpec.PARAM_TYPE_MESSAGE /*48*/:
            case CipherSuite.TLS_DH_DSS_WITH_AES_256_CBC_SHA /*54*/:
            case CipherSuite.TLS_DH_DSS_WITH_AES_128_CBC_SHA256 /*62*/:
            case CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_128_CBC_SHA /*66*/:
            case CipherSuite.TLS_DH_DSS_WITH_AES_256_CBC_SHA256 /*104*/:
            case CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_256_CBC_SHA /*133*/:
            case CipherSuite.TLS_DH_DSS_WITH_SEED_CBC_SHA /*151*/:
            case CipherSuite.TLS_DH_DSS_WITH_AES_128_GCM_SHA256 /*164*/:
            case CipherSuite.TLS_DH_DSS_WITH_AES_256_GCM_SHA384 /*165*/:
            case CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_128_CBC_SHA256 /*187*/:
            case CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_256_CBC_SHA256 /*193*/:
            case CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_128_GCM_SHA256 /*49282*/:
            case CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_256_GCM_SHA384 /*49283*/:
                return createDHKeyExchange(7);
            case X509KeyUsage.dataEncipherment /*16*/:
            case CipherSuite.TLS_DH_RSA_WITH_AES_128_CBC_SHA /*49*/:
            case CipherSuite.TLS_DH_RSA_WITH_AES_256_CBC_SHA /*55*/:
            case SkeinParameterSpec.PARAM_TYPE_OUTPUT /*63*/:
            case CipherSuite.TLS_DH_RSA_WITH_CAMELLIA_128_CBC_SHA /*67*/:
            case CipherSuite.TLS_DH_RSA_WITH_AES_256_CBC_SHA256 /*105*/:
            case CipherSuite.TLS_DH_RSA_WITH_CAMELLIA_256_CBC_SHA /*134*/:
            case CipherSuite.TLS_DH_RSA_WITH_SEED_CBC_SHA /*152*/:
            case CipherSuite.TLS_DH_RSA_WITH_AES_128_GCM_SHA256 /*160*/:
            case CipherSuite.TLS_DH_RSA_WITH_AES_256_GCM_SHA384 /*161*/:
            case CipherSuite.TLS_DH_RSA_WITH_CAMELLIA_128_CBC_SHA256 /*188*/:
            case CipherSuite.TLS_DH_RSA_WITH_CAMELLIA_256_CBC_SHA256 /*194*/:
            case CipherSuite.TLS_DH_RSA_WITH_CAMELLIA_128_GCM_SHA256 /*49278*/:
            case CipherSuite.TLS_DH_RSA_WITH_CAMELLIA_256_GCM_SHA384 /*49279*/:
                return createDHKeyExchange(9);
            case NamedCurve.secp192r1 /*19*/:
            case ECCKeyGenParameterSpec.DEFAULT_T /*50*/:
            case CipherSuite.TLS_DHE_DSS_WITH_AES_256_CBC_SHA /*56*/:
            case X509KeyUsage.nonRepudiation /*64*/:
            case CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_128_CBC_SHA /*68*/:
            case CipherSuite.TLS_DHE_DSS_WITH_AES_256_CBC_SHA256 /*106*/:
            case CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_256_CBC_SHA /*135*/:
            case CipherSuite.TLS_DHE_DSS_WITH_SEED_CBC_SHA /*153*/:
            case CipherSuite.TLS_DHE_DSS_WITH_AES_128_GCM_SHA256 /*162*/:
            case CipherSuite.TLS_DHE_DSS_WITH_AES_256_GCM_SHA384 /*163*/:
            case CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_128_CBC_SHA256 /*189*/:
            case CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_256_CBC_SHA256 /*195*/:
            case CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_128_GCM_SHA256 /*49280*/:
            case CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_256_GCM_SHA384 /*49281*/:
                return createDHEKeyExchange(3);
            case NamedCurve.secp256k1 /*22*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA /*51*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA /*57*/:
            case CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_128_CBC_SHA /*69*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA256 /*103*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA256 /*107*/:
            case CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_256_CBC_SHA /*136*/:
            case CipherSuite.TLS_DHE_RSA_WITH_SEED_CBC_SHA /*154*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256 /*158*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_256_GCM_SHA384 /*159*/:
            case CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_128_CBC_SHA256 /*190*/:
            case CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_256_CBC_SHA256 /*196*/:
            case CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_128_GCM_SHA256 /*49276*/:
            case CipherSuite.TLS_DHE_RSA_WITH_CAMELLIA_256_GCM_SHA384 /*49277*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_128_CCM /*49310*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_256_CCM /*49311*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_128_CCM_8 /*49314*/:
            case CipherSuite.TLS_DHE_RSA_WITH_AES_256_CCM_8 /*49315*/:
            case CipherSuite.TLS_DHE_RSA_WITH_CHACHA20_POLY1305_SHA256 /*52245*/:
            case CipherSuite.TLS_DHE_RSA_WITH_ESTREAM_SALSA20_SHA1 /*58398*/:
            case CipherSuite.TLS_DHE_RSA_WITH_SALSA20_SHA1 /*58399*/:
                return createDHEKeyExchange(5);
            case CipherSuite.TLS_ECDH_ECDSA_WITH_NULL_SHA /*49153*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_RC4_128_SHA /*49154*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_3DES_EDE_CBC_SHA /*49155*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA /*49156*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA /*49157*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA256 /*49189*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA384 /*49190*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_AES_128_GCM_SHA256 /*49197*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_AES_256_GCM_SHA384 /*49198*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_CAMELLIA_128_CBC_SHA256 /*49268*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_CAMELLIA_256_CBC_SHA384 /*49269*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_CAMELLIA_128_GCM_SHA256 /*49288*/:
            case CipherSuite.TLS_ECDH_ECDSA_WITH_CAMELLIA_256_GCM_SHA384 /*49289*/:
                return createECDHKeyExchange(16);
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_NULL_SHA /*49158*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_RC4_128_SHA /*49159*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_3DES_EDE_CBC_SHA /*49160*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA /*49161*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA /*49162*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256 /*49187*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384 /*49188*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256 /*49195*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384 /*49196*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_CAMELLIA_128_CBC_SHA256 /*49266*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_CAMELLIA_256_CBC_SHA384 /*49267*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_CAMELLIA_128_GCM_SHA256 /*49286*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_CAMELLIA_256_GCM_SHA384 /*49287*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CCM /*49324*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CCM /*49325*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CCM_8 /*49326*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CCM_8 /*49327*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256 /*52244*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_ESTREAM_SALSA20_SHA1 /*58388*/:
            case CipherSuite.TLS_ECDHE_ECDSA_WITH_SALSA20_SHA1 /*58389*/:
                return createECDHEKeyExchange(17);
            case CipherSuite.TLS_ECDH_RSA_WITH_NULL_SHA /*49163*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_RC4_128_SHA /*49164*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_3DES_EDE_CBC_SHA /*49165*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_AES_128_CBC_SHA /*49166*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_AES_256_CBC_SHA /*49167*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_AES_128_CBC_SHA256 /*49193*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_AES_256_CBC_SHA384 /*49194*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_AES_128_GCM_SHA256 /*49201*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_AES_256_GCM_SHA384 /*49202*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_CAMELLIA_128_CBC_SHA256 /*49272*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_CAMELLIA_256_CBC_SHA384 /*49273*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_CAMELLIA_128_GCM_SHA256 /*49292*/:
            case CipherSuite.TLS_ECDH_RSA_WITH_CAMELLIA_256_GCM_SHA384 /*49293*/:
                return createECDHKeyExchange(18);
            case CipherSuite.TLS_ECDHE_RSA_WITH_NULL_SHA /*49168*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_RC4_128_SHA /*49169*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_3DES_EDE_CBC_SHA /*49170*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA /*49171*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA /*49172*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256 /*49191*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384 /*49192*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256 /*49199*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384 /*49200*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_CAMELLIA_128_CBC_SHA256 /*49270*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_CAMELLIA_256_CBC_SHA384 /*49271*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_CAMELLIA_128_GCM_SHA256 /*49290*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_CAMELLIA_256_GCM_SHA384 /*49291*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256 /*52243*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_ESTREAM_SALSA20_SHA1 /*58386*/:
            case CipherSuite.TLS_ECDHE_RSA_WITH_SALSA20_SHA1 /*58387*/:
                return createECDHEKeyExchange(19);
            default:
                throw new TlsFatalAlert((short) 80);
        }
    }
}
