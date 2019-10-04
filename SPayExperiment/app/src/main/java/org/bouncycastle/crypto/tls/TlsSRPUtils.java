/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayInputStream
 *  java.io.InputStream
 *  java.io.OutputStream
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  java.util.Hashtable
 *  org.bouncycastle.util.BigIntegers
 *  org.bouncycastle.util.Integers
 */
package org.bouncycastle.crypto.tls;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Hashtable;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsProtocol;
import org.bouncycastle.crypto.tls.TlsUtils;
import org.bouncycastle.util.BigIntegers;
import org.bouncycastle.util.Integers;

public class TlsSRPUtils {
    public static final Integer EXT_SRP = Integers.valueOf((int)12);

    public static void addSRPExtension(Hashtable hashtable, byte[] arrby) {
        hashtable.put((Object)EXT_SRP, (Object)TlsSRPUtils.createSRPExtension(arrby));
    }

    public static byte[] createSRPExtension(byte[] arrby) {
        if (arrby == null) {
            throw new TlsFatalAlert(80);
        }
        return TlsUtils.encodeOpaque8(arrby);
    }

    public static byte[] getSRPExtension(Hashtable hashtable) {
        byte[] arrby = TlsUtils.getExtensionData(hashtable, EXT_SRP);
        if (arrby == null) {
            return null;
        }
        return TlsSRPUtils.readSRPExtension(arrby);
    }

    public static boolean isSRPCipherSuite(int n2) {
        switch (n2) {
            default: {
                return false;
            }
            case 49178: 
            case 49179: 
            case 49180: 
            case 49181: 
            case 49182: 
            case 49183: 
            case 49184: 
            case 49185: 
            case 49186: 
        }
        return true;
    }

    public static byte[] readSRPExtension(byte[] arrby) {
        if (arrby == null) {
            throw new IllegalArgumentException("'extensionData' cannot be null");
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arrby);
        byte[] arrby2 = TlsUtils.readOpaque8((InputStream)byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        return arrby2;
    }

    public static BigInteger readSRPParameter(InputStream inputStream) {
        return new BigInteger(1, TlsUtils.readOpaque16(inputStream));
    }

    public static void writeSRPParameter(BigInteger bigInteger, OutputStream outputStream) {
        TlsUtils.writeOpaque16(BigIntegers.asUnsignedByteArray((BigInteger)bigInteger), outputStream);
    }
}

