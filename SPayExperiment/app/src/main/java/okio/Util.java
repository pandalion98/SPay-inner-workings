/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.ArrayIndexOutOfBoundsException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.nio.charset.Charset
 */
package okio;

import java.nio.charset.Charset;

final class Util {
    public static final Charset UTF_8 = Charset.forName((String)"UTF-8");

    private Util() {
    }

    public static boolean arrayRangeEquals(byte[] arrby, int n2, byte[] arrby2, int n3, int n4) {
        for (int i2 = 0; i2 < n4; ++i2) {
            if (arrby[i2 + n2] == arrby2[i2 + n3]) continue;
            return false;
        }
        return true;
    }

    public static void checkOffsetAndCount(long l2, long l3, long l4) {
        if ((l3 | l4) < 0L || l3 > l2 || l2 - l3 < l4) {
            Object[] arrobject = new Object[]{l2, l3, l4};
            throw new ArrayIndexOutOfBoundsException(String.format((String)"size=%s offset=%s byteCount=%s", (Object[])arrobject));
        }
    }

    public static int reverseBytesInt(int n2) {
        return (-16777216 & n2) >>> 24 | (16711680 & n2) >>> 8 | (65280 & n2) << 8 | (n2 & 255) << 24;
    }

    public static long reverseBytesLong(long l2) {
        return (-72057594037927936L & l2) >>> 56 | (0xFF000000000000L & l2) >>> 40 | (0xFF0000000000L & l2) >>> 24 | (0xFF00000000L & l2) >>> 8 | (0xFF000000L & l2) << 8 | (0xFF0000L & l2) << 24 | (65280L & l2) << 40 | (255L & l2) << 56;
    }

    public static short reverseBytesShort(short s2) {
        int n2 = 65535 & s2;
        return (short)((65280 & n2) >>> 8 | (n2 & 255) << 8);
    }

    public static void sneakyRethrow(Throwable throwable) {
        Util.sneakyThrow2(throwable);
    }

    private static <T extends Throwable> void sneakyThrow2(Throwable throwable) {
        throw throwable;
    }
}

