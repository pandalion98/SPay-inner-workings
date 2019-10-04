/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.OutputStream
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.Throwable
 *  java.security.SecureRandom
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.crypto.tls;

import java.io.OutputStream;
import java.security.SecureRandom;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.encodings.PKCS1Encoding;
import org.bouncycastle.crypto.engines.RSABlindedEngine;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.tls.ProtocolVersion;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsUtils;
import org.bouncycastle.util.Arrays;

public class TlsRSAUtils {
    public static byte[] generateEncryptedPreMasterSecret(TlsContext tlsContext, RSAKeyParameters rSAKeyParameters, OutputStream outputStream) {
        byte[] arrby;
        byte[] arrby2;
        block3 : {
            arrby = new byte[48];
            tlsContext.getSecureRandom().nextBytes(arrby);
            TlsUtils.writeVersion(tlsContext.getClientVersion(), arrby, 0);
            PKCS1Encoding pKCS1Encoding = new PKCS1Encoding(new RSABlindedEngine());
            pKCS1Encoding.init(true, new ParametersWithRandom(rSAKeyParameters, tlsContext.getSecureRandom()));
            try {
                arrby2 = pKCS1Encoding.processBlock(arrby, 0, arrby.length);
                if (!TlsUtils.isSSL(tlsContext)) break block3;
                outputStream.write(arrby2);
                return arrby;
            }
            catch (InvalidCipherTextException invalidCipherTextException) {
                throw new TlsFatalAlert(80, (Throwable)((Object)invalidCipherTextException));
            }
        }
        TlsUtils.writeOpaque16(arrby2, outputStream);
        return arrby;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static byte[] safeDecryptPreMasterSecret(TlsContext tlsContext, RSAKeyParameters rSAKeyParameters, byte[] arrby) {
        byte[] arrby2;
        ProtocolVersion protocolVersion;
        byte[] arrby3;
        int n2;
        n2 = 0;
        protocolVersion = tlsContext.getClientVersion();
        arrby2 = new byte[48];
        tlsContext.getSecureRandom().nextBytes(arrby2);
        arrby3 = Arrays.clone((byte[])arrby2);
        try {
            PKCS1Encoding pKCS1Encoding = new PKCS1Encoding((AsymmetricBlockCipher)new RSABlindedEngine(), arrby2);
            pKCS1Encoding.init(false, new ParametersWithRandom(rSAKeyParameters, tlsContext.getSecureRandom()));
            byte[] arrby4 = pKCS1Encoding.processBlock(arrby, 0, arrby.length);
            arrby3 = arrby4;
        }
        catch (Exception exception) {}
        int n3 = protocolVersion.getMajorVersion() ^ 255 & arrby3[0] | protocolVersion.getMinorVersion() ^ 255 & arrby3[1];
        int n4 = n3 | n3 >> 1;
        int n5 = n4 | n4 >> 2;
        int n6 = -1 ^ -1 + (1 & (n5 | n5 >> 4));
        while (n2 < 48) {
            arrby3[n2] = (byte)(arrby3[n2] & ~n6 | n6 & arrby2[n2]);
            ++n2;
        }
        return arrby3;
    }
}

