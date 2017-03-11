package org.bouncycastle.crypto.tls;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Hashtable;
import org.bouncycastle.util.BigIntegers;
import org.bouncycastle.util.Integers;

public class TlsSRPUtils {
    public static final Integer EXT_SRP;

    static {
        EXT_SRP = Integers.valueOf(12);
    }

    public static void addSRPExtension(Hashtable hashtable, byte[] bArr) {
        hashtable.put(EXT_SRP, createSRPExtension(bArr));
    }

    public static byte[] createSRPExtension(byte[] bArr) {
        if (bArr != null) {
            return TlsUtils.encodeOpaque8(bArr);
        }
        throw new TlsFatalAlert((short) 80);
    }

    public static byte[] getSRPExtension(Hashtable hashtable) {
        byte[] extensionData = TlsUtils.getExtensionData(hashtable, EXT_SRP);
        return extensionData == null ? null : readSRPExtension(extensionData);
    }

    public static boolean isSRPCipherSuite(int i) {
        switch (i) {
            case CipherSuite.TLS_SRP_SHA_WITH_3DES_EDE_CBC_SHA /*49178*/:
            case CipherSuite.TLS_SRP_SHA_RSA_WITH_3DES_EDE_CBC_SHA /*49179*/:
            case CipherSuite.TLS_SRP_SHA_DSS_WITH_3DES_EDE_CBC_SHA /*49180*/:
            case CipherSuite.TLS_SRP_SHA_WITH_AES_128_CBC_SHA /*49181*/:
            case CipherSuite.TLS_SRP_SHA_RSA_WITH_AES_128_CBC_SHA /*49182*/:
            case CipherSuite.TLS_SRP_SHA_DSS_WITH_AES_128_CBC_SHA /*49183*/:
            case CipherSuite.TLS_SRP_SHA_WITH_AES_256_CBC_SHA /*49184*/:
            case CipherSuite.TLS_SRP_SHA_RSA_WITH_AES_256_CBC_SHA /*49185*/:
            case CipherSuite.TLS_SRP_SHA_DSS_WITH_AES_256_CBC_SHA /*49186*/:
                return true;
            default:
                return false;
        }
    }

    public static byte[] readSRPExtension(byte[] bArr) {
        if (bArr == null) {
            throw new IllegalArgumentException("'extensionData' cannot be null");
        }
        InputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        byte[] readOpaque8 = TlsUtils.readOpaque8(byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        return readOpaque8;
    }

    public static BigInteger readSRPParameter(InputStream inputStream) {
        return new BigInteger(1, TlsUtils.readOpaque16(inputStream));
    }

    public static void writeSRPParameter(BigInteger bigInteger, OutputStream outputStream) {
        TlsUtils.writeOpaque16(BigIntegers.asUnsignedByteArray(bigInteger), outputStream);
    }
}
