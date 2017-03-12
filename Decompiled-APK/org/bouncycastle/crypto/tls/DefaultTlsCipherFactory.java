package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.engines.CamelliaEngine;
import org.bouncycastle.crypto.engines.DESedeEngine;
import org.bouncycastle.crypto.engines.RC4Engine;
import org.bouncycastle.crypto.engines.SEEDEngine;
import org.bouncycastle.crypto.engines.Salsa20Engine;
import org.bouncycastle.crypto.modes.AEADBlockCipher;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.modes.CCMBlockCipher;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.jcajce.spec.SkeinParameterSpec;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class DefaultTlsCipherFactory extends AbstractTlsCipherFactory {
    protected AEADBlockCipher createAEADBlockCipher_AES_CCM() {
        return new CCMBlockCipher(createAESEngine());
    }

    protected AEADBlockCipher createAEADBlockCipher_AES_GCM() {
        return new GCMBlockCipher(createAESEngine());
    }

    protected AEADBlockCipher createAEADBlockCipher_Camellia_GCM() {
        return new GCMBlockCipher(createCamelliaEngine());
    }

    protected BlockCipher createAESBlockCipher() {
        return new CBCBlockCipher(createAESEngine());
    }

    protected TlsBlockCipher createAESCipher(TlsContext tlsContext, int i, int i2) {
        return new TlsBlockCipher(tlsContext, createAESBlockCipher(), createAESBlockCipher(), createHMACDigest(i2), createHMACDigest(i2), i);
    }

    protected BlockCipher createAESEngine() {
        return new AESEngine();
    }

    protected BlockCipher createCamelliaBlockCipher() {
        return new CBCBlockCipher(createCamelliaEngine());
    }

    protected TlsBlockCipher createCamelliaCipher(TlsContext tlsContext, int i, int i2) {
        return new TlsBlockCipher(tlsContext, createCamelliaBlockCipher(), createCamelliaBlockCipher(), createHMACDigest(i2), createHMACDigest(i2), i);
    }

    protected BlockCipher createCamelliaEngine() {
        return new CamelliaEngine();
    }

    protected TlsCipher createChaCha20Poly1305(TlsContext tlsContext) {
        return new Chacha20Poly1305(tlsContext);
    }

    public TlsCipher createCipher(TlsContext tlsContext, int i, int i2) {
        switch (i) {
            case ECCurve.COORD_AFFINE /*0*/:
                return createNullCipher(tlsContext, i2);
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                return createRC4Cipher(tlsContext, 16, i2);
            case ECCurve.COORD_SKEWED /*7*/:
                return createDESedeCipher(tlsContext, i2);
            case X509KeyUsage.keyAgreement /*8*/:
                return createAESCipher(tlsContext, 16, i2);
            case NamedCurve.sect283k1 /*9*/:
                return createAESCipher(tlsContext, 32, i2);
            case NamedCurve.sect283r1 /*10*/:
                return createCipher_AES_GCM(tlsContext, 16, 16);
            case CertStatus.UNREVOKED /*11*/:
                return createCipher_AES_GCM(tlsContext, 32, 16);
            case CertStatus.UNDETERMINED /*12*/:
                return createCamelliaCipher(tlsContext, 16, i2);
            case NamedCurve.sect571k1 /*13*/:
                return createCamelliaCipher(tlsContext, 32, i2);
            case NamedCurve.sect571r1 /*14*/:
                return createSEEDCipher(tlsContext, i2);
            case NamedCurve.secp160k1 /*15*/:
                return createCipher_AES_CCM(tlsContext, 16, 16);
            case X509KeyUsage.dataEncipherment /*16*/:
                return createCipher_AES_CCM(tlsContext, 16, 8);
            case NamedCurve.secp160r2 /*17*/:
                return createCipher_AES_CCM(tlsContext, 32, 16);
            case NamedCurve.secp192k1 /*18*/:
                return createCipher_AES_CCM(tlsContext, 32, 8);
            case NamedCurve.secp192r1 /*19*/:
                return createCipher_Camellia_GCM(tlsContext, 16, 16);
            case SkeinParameterSpec.PARAM_TYPE_NONCE /*20*/:
                return createCipher_Camellia_GCM(tlsContext, 32, 16);
            case EncryptionAlgorithm.ESTREAM_SALSA20 /*100*/:
                return createSalsa20Cipher(tlsContext, 12, 32, i2);
            case ExtensionType.negotiated_ff_dhe_groups /*101*/:
                return createSalsa20Cipher(tlsContext, 20, 32, i2);
            case EncryptionAlgorithm.AEAD_CHACHA20_POLY1305 /*102*/:
                return createChaCha20Poly1305(tlsContext);
            default:
                throw new TlsFatalAlert((short) 80);
        }
    }

    protected TlsAEADCipher createCipher_AES_CCM(TlsContext tlsContext, int i, int i2) {
        return new TlsAEADCipher(tlsContext, createAEADBlockCipher_AES_CCM(), createAEADBlockCipher_AES_CCM(), i, i2);
    }

    protected TlsAEADCipher createCipher_AES_GCM(TlsContext tlsContext, int i, int i2) {
        return new TlsAEADCipher(tlsContext, createAEADBlockCipher_AES_GCM(), createAEADBlockCipher_AES_GCM(), i, i2);
    }

    protected TlsAEADCipher createCipher_Camellia_GCM(TlsContext tlsContext, int i, int i2) {
        return new TlsAEADCipher(tlsContext, createAEADBlockCipher_Camellia_GCM(), createAEADBlockCipher_Camellia_GCM(), i, i2);
    }

    protected BlockCipher createDESedeBlockCipher() {
        return new CBCBlockCipher(new DESedeEngine());
    }

    protected TlsBlockCipher createDESedeCipher(TlsContext tlsContext, int i) {
        return new TlsBlockCipher(tlsContext, createDESedeBlockCipher(), createDESedeBlockCipher(), createHMACDigest(i), createHMACDigest(i), 24);
    }

    protected Digest createHMACDigest(int i) {
        switch (i) {
            case ECCurve.COORD_AFFINE /*0*/:
                return null;
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                return TlsUtils.createHash((short) 1);
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                return TlsUtils.createHash((short) 2);
            case F2m.PPB /*3*/:
                return TlsUtils.createHash((short) 4);
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                return TlsUtils.createHash((short) 5);
            case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                return TlsUtils.createHash((short) 6);
            default:
                throw new TlsFatalAlert((short) 80);
        }
    }

    protected TlsNullCipher createNullCipher(TlsContext tlsContext, int i) {
        return new TlsNullCipher(tlsContext, createHMACDigest(i), createHMACDigest(i));
    }

    protected TlsStreamCipher createRC4Cipher(TlsContext tlsContext, int i, int i2) {
        return new TlsStreamCipher(tlsContext, createRC4StreamCipher(), createRC4StreamCipher(), createHMACDigest(i2), createHMACDigest(i2), i, false);
    }

    protected StreamCipher createRC4StreamCipher() {
        return new RC4Engine();
    }

    protected BlockCipher createSEEDBlockCipher() {
        return new CBCBlockCipher(new SEEDEngine());
    }

    protected TlsBlockCipher createSEEDCipher(TlsContext tlsContext, int i) {
        return new TlsBlockCipher(tlsContext, createSEEDBlockCipher(), createSEEDBlockCipher(), createHMACDigest(i), createHMACDigest(i), 16);
    }

    protected TlsStreamCipher createSalsa20Cipher(TlsContext tlsContext, int i, int i2, int i3) {
        return new TlsStreamCipher(tlsContext, createSalsa20StreamCipher(i), createSalsa20StreamCipher(i), createHMACDigest(i3), createHMACDigest(i3), i2, true);
    }

    protected StreamCipher createSalsa20StreamCipher(int i) {
        return new Salsa20Engine(i);
    }
}
