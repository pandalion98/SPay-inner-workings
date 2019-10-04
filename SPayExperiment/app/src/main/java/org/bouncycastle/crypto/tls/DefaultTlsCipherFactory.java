/*
 * Decompiled with CFR 0.0.
 */
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
import org.bouncycastle.crypto.tls.AbstractTlsCipherFactory;
import org.bouncycastle.crypto.tls.Chacha20Poly1305;
import org.bouncycastle.crypto.tls.TlsAEADCipher;
import org.bouncycastle.crypto.tls.TlsBlockCipher;
import org.bouncycastle.crypto.tls.TlsCipher;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsNullCipher;
import org.bouncycastle.crypto.tls.TlsStreamCipher;
import org.bouncycastle.crypto.tls.TlsUtils;

public class DefaultTlsCipherFactory
extends AbstractTlsCipherFactory {
    protected AEADBlockCipher createAEADBlockCipher_AES_CCM() {
        return new CCMBlockCipher(this.createAESEngine());
    }

    protected AEADBlockCipher createAEADBlockCipher_AES_GCM() {
        return new GCMBlockCipher(this.createAESEngine());
    }

    protected AEADBlockCipher createAEADBlockCipher_Camellia_GCM() {
        return new GCMBlockCipher(this.createCamelliaEngine());
    }

    protected BlockCipher createAESBlockCipher() {
        return new CBCBlockCipher(this.createAESEngine());
    }

    protected TlsBlockCipher createAESCipher(TlsContext tlsContext, int n2, int n3) {
        return new TlsBlockCipher(tlsContext, this.createAESBlockCipher(), this.createAESBlockCipher(), this.createHMACDigest(n3), this.createHMACDigest(n3), n2);
    }

    protected BlockCipher createAESEngine() {
        return new AESEngine();
    }

    protected BlockCipher createCamelliaBlockCipher() {
        return new CBCBlockCipher(this.createCamelliaEngine());
    }

    protected TlsBlockCipher createCamelliaCipher(TlsContext tlsContext, int n2, int n3) {
        return new TlsBlockCipher(tlsContext, this.createCamelliaBlockCipher(), this.createCamelliaBlockCipher(), this.createHMACDigest(n3), this.createHMACDigest(n3), n2);
    }

    protected BlockCipher createCamelliaEngine() {
        return new CamelliaEngine();
    }

    protected TlsCipher createChaCha20Poly1305(TlsContext tlsContext) {
        return new Chacha20Poly1305(tlsContext);
    }

    @Override
    public TlsCipher createCipher(TlsContext tlsContext, int n2, int n3) {
        switch (n2) {
            default: {
                throw new TlsFatalAlert(80);
            }
            case 7: {
                return this.createDESedeCipher(tlsContext, n3);
            }
            case 102: {
                return this.createChaCha20Poly1305(tlsContext);
            }
            case 8: {
                return this.createAESCipher(tlsContext, 16, n3);
            }
            case 15: {
                return this.createCipher_AES_CCM(tlsContext, 16, 16);
            }
            case 16: {
                return this.createCipher_AES_CCM(tlsContext, 16, 8);
            }
            case 17: {
                return this.createCipher_AES_CCM(tlsContext, 32, 16);
            }
            case 18: {
                return this.createCipher_AES_CCM(tlsContext, 32, 8);
            }
            case 10: {
                return this.createCipher_AES_GCM(tlsContext, 16, 16);
            }
            case 9: {
                return this.createAESCipher(tlsContext, 32, n3);
            }
            case 11: {
                return this.createCipher_AES_GCM(tlsContext, 32, 16);
            }
            case 12: {
                return this.createCamelliaCipher(tlsContext, 16, n3);
            }
            case 19: {
                return this.createCipher_Camellia_GCM(tlsContext, 16, 16);
            }
            case 13: {
                return this.createCamelliaCipher(tlsContext, 32, n3);
            }
            case 20: {
                return this.createCipher_Camellia_GCM(tlsContext, 32, 16);
            }
            case 100: {
                return this.createSalsa20Cipher(tlsContext, 12, 32, n3);
            }
            case 0: {
                return this.createNullCipher(tlsContext, n3);
            }
            case 2: {
                return this.createRC4Cipher(tlsContext, 16, n3);
            }
            case 101: {
                return this.createSalsa20Cipher(tlsContext, 20, 32, n3);
            }
            case 14: 
        }
        return this.createSEEDCipher(tlsContext, n3);
    }

    protected TlsAEADCipher createCipher_AES_CCM(TlsContext tlsContext, int n2, int n3) {
        return new TlsAEADCipher(tlsContext, this.createAEADBlockCipher_AES_CCM(), this.createAEADBlockCipher_AES_CCM(), n2, n3);
    }

    protected TlsAEADCipher createCipher_AES_GCM(TlsContext tlsContext, int n2, int n3) {
        return new TlsAEADCipher(tlsContext, this.createAEADBlockCipher_AES_GCM(), this.createAEADBlockCipher_AES_GCM(), n2, n3);
    }

    protected TlsAEADCipher createCipher_Camellia_GCM(TlsContext tlsContext, int n2, int n3) {
        return new TlsAEADCipher(tlsContext, this.createAEADBlockCipher_Camellia_GCM(), this.createAEADBlockCipher_Camellia_GCM(), n2, n3);
    }

    protected BlockCipher createDESedeBlockCipher() {
        return new CBCBlockCipher(new DESedeEngine());
    }

    protected TlsBlockCipher createDESedeCipher(TlsContext tlsContext, int n2) {
        return new TlsBlockCipher(tlsContext, this.createDESedeBlockCipher(), this.createDESedeBlockCipher(), this.createHMACDigest(n2), this.createHMACDigest(n2), 24);
    }

    protected Digest createHMACDigest(int n2) {
        switch (n2) {
            default: {
                throw new TlsFatalAlert(80);
            }
            case 0: {
                return null;
            }
            case 1: {
                return TlsUtils.createHash((short)1);
            }
            case 2: {
                return TlsUtils.createHash((short)2);
            }
            case 3: {
                return TlsUtils.createHash((short)4);
            }
            case 4: {
                return TlsUtils.createHash((short)5);
            }
            case 5: 
        }
        return TlsUtils.createHash((short)6);
    }

    protected TlsNullCipher createNullCipher(TlsContext tlsContext, int n2) {
        return new TlsNullCipher(tlsContext, this.createHMACDigest(n2), this.createHMACDigest(n2));
    }

    protected TlsStreamCipher createRC4Cipher(TlsContext tlsContext, int n2, int n3) {
        return new TlsStreamCipher(tlsContext, this.createRC4StreamCipher(), this.createRC4StreamCipher(), this.createHMACDigest(n3), this.createHMACDigest(n3), n2, false);
    }

    protected StreamCipher createRC4StreamCipher() {
        return new RC4Engine();
    }

    protected BlockCipher createSEEDBlockCipher() {
        return new CBCBlockCipher(new SEEDEngine());
    }

    protected TlsBlockCipher createSEEDCipher(TlsContext tlsContext, int n2) {
        return new TlsBlockCipher(tlsContext, this.createSEEDBlockCipher(), this.createSEEDBlockCipher(), this.createHMACDigest(n2), this.createHMACDigest(n2), 16);
    }

    protected TlsStreamCipher createSalsa20Cipher(TlsContext tlsContext, int n2, int n3, int n4) {
        return new TlsStreamCipher(tlsContext, this.createSalsa20StreamCipher(n2), this.createSalsa20StreamCipher(n2), this.createHMACDigest(n4), this.createHMACDigest(n4), n3, true);
    }

    protected StreamCipher createSalsa20StreamCipher(int n2) {
        return new Salsa20Engine(n2);
    }
}

