/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayInputStream
 *  java.io.ByteArrayOutputStream
 *  java.io.InputStream
 *  java.io.OutputStream
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Hashtable
 *  org.bouncycastle.util.Integers
 */
package org.bouncycastle.crypto.tls;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsProtocol;
import org.bouncycastle.crypto.tls.TlsUtils;
import org.bouncycastle.crypto.tls.UseSRTPData;
import org.bouncycastle.util.Integers;

public class TlsSRTPUtils {
    public static final Integer EXT_use_srtp = Integers.valueOf((int)14);

    public static void addUseSRTPExtension(Hashtable hashtable, UseSRTPData useSRTPData) {
        hashtable.put((Object)EXT_use_srtp, (Object)TlsSRTPUtils.createUseSRTPExtension(useSRTPData));
    }

    public static byte[] createUseSRTPExtension(UseSRTPData useSRTPData) {
        if (useSRTPData == null) {
            throw new IllegalArgumentException("'useSRTPData' cannot be null");
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TlsUtils.writeUint16ArrayWithUint16Length(useSRTPData.getProtectionProfiles(), (OutputStream)byteArrayOutputStream);
        TlsUtils.writeOpaque8(useSRTPData.getMki(), (OutputStream)byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static UseSRTPData getUseSRTPExtension(Hashtable hashtable) {
        byte[] arrby = TlsUtils.getExtensionData(hashtable, EXT_use_srtp);
        if (arrby == null) {
            return null;
        }
        return TlsSRTPUtils.readUseSRTPExtension(arrby);
    }

    public static UseSRTPData readUseSRTPExtension(byte[] arrby) {
        if (arrby == null) {
            throw new IllegalArgumentException("'extensionData' cannot be null");
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arrby);
        int n2 = TlsUtils.readUint16((InputStream)byteArrayInputStream);
        if (n2 < 2 || (n2 & 1) != 0) {
            throw new TlsFatalAlert(50);
        }
        int[] arrn = TlsUtils.readUint16Array(n2 / 2, (InputStream)byteArrayInputStream);
        byte[] arrby2 = TlsUtils.readOpaque8((InputStream)byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        return new UseSRTPData(arrn, arrby2);
    }
}

