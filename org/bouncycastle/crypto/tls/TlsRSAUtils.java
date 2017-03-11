package org.bouncycastle.crypto.tls;

import java.io.OutputStream;
import org.bouncycastle.crypto.encodings.PKCS1Encoding;
import org.bouncycastle.crypto.engines.RSABlindedEngine;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.util.Arrays;

public class TlsRSAUtils {
    public static byte[] generateEncryptedPreMasterSecret(TlsContext tlsContext, RSAKeyParameters rSAKeyParameters, OutputStream outputStream) {
        byte[] bArr = new byte[48];
        tlsContext.getSecureRandom().nextBytes(bArr);
        TlsUtils.writeVersion(tlsContext.getClientVersion(), bArr, 0);
        PKCS1Encoding pKCS1Encoding = new PKCS1Encoding(new RSABlindedEngine());
        pKCS1Encoding.init(true, new ParametersWithRandom(rSAKeyParameters, tlsContext.getSecureRandom()));
        try {
            byte[] processBlock = pKCS1Encoding.processBlock(bArr, 0, bArr.length);
            if (TlsUtils.isSSL(tlsContext)) {
                outputStream.write(processBlock);
            } else {
                TlsUtils.writeOpaque16(processBlock, outputStream);
            }
            return bArr;
        } catch (Throwable e) {
            throw new TlsFatalAlert((short) 80, e);
        }
    }

    public static byte[] safeDecryptPreMasterSecret(TlsContext tlsContext, RSAKeyParameters rSAKeyParameters, byte[] bArr) {
        int i = 0;
        ProtocolVersion clientVersion = tlsContext.getClientVersion();
        byte[] bArr2 = new byte[48];
        tlsContext.getSecureRandom().nextBytes(bArr2);
        byte[] clone = Arrays.clone(bArr2);
        try {
            PKCS1Encoding pKCS1Encoding = new PKCS1Encoding(new RSABlindedEngine(), bArr2);
            pKCS1Encoding.init(false, new ParametersWithRandom(rSAKeyParameters, tlsContext.getSecureRandom()));
            clone = pKCS1Encoding.processBlock(bArr, 0, bArr.length);
        } catch (Exception e) {
        }
        int minorVersion = (clientVersion.getMinorVersion() ^ (clone[1] & GF2Field.MASK)) | (clientVersion.getMajorVersion() ^ (clone[0] & GF2Field.MASK));
        minorVersion |= minorVersion >> 1;
        minorVersion |= minorVersion >> 2;
        minorVersion = (((minorVersion | (minorVersion >> 4)) & 1) - 1) ^ -1;
        while (i < 48) {
            clone[i] = (byte) ((clone[i] & (minorVersion ^ -1)) | (bArr2[i] & minorVersion));
            i++;
        }
        return clone;
    }
}
