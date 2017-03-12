package org.bouncycastle.util;

import com.google.android.gms.location.places.Place;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Vector;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.pqc.math.linearalgebra.Matrix;

public final class Strings {

    private static class StringListImpl extends ArrayList<String> implements StringList {
        private StringListImpl() {
        }

        public void add(int i, String str) {
            super.add(i, str);
        }

        public boolean add(String str) {
            return super.add(str);
        }

        public /* bridge */ /* synthetic */ String get(int i) {
            return (String) super.get(i);
        }

        public String set(int i, String str) {
            return (String) super.set(i, str);
        }

        public String[] toStringArray() {
            String[] strArr = new String[size()];
            for (int i = 0; i != strArr.length; i++) {
                strArr[i] = (String) get(i);
            }
            return strArr;
        }

        public String[] toStringArray(int i, int i2) {
            String[] strArr = new String[(i2 - i)];
            int i3 = i;
            while (i3 != size() && i3 != i2) {
                strArr[i3 - i] = (String) get(i3);
                i3++;
            }
            return strArr;
        }
    }

    public static char[] asCharArray(byte[] bArr) {
        char[] cArr = new char[bArr.length];
        for (int i = 0; i != cArr.length; i++) {
            cArr[i] = (char) (bArr[i] & GF2Field.MASK);
        }
        return cArr;
    }

    public static String fromByteArray(byte[] bArr) {
        return new String(asCharArray(bArr));
    }

    public static String fromUTF8ByteArray(byte[] bArr) {
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        while (i3 < bArr.length) {
            i2++;
            if ((bArr[i3] & 240) == 240) {
                i2++;
                i3 += 4;
            } else {
                i3 = (bArr[i3] & 224) == 224 ? i3 + 3 : (bArr[i3] & CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256) == CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256 ? i3 + 2 : i3 + 1;
            }
        }
        char[] cArr = new char[i2];
        i2 = 0;
        while (i2 < bArr.length) {
            char c;
            int i4;
            if ((bArr[i2] & 240) == 240) {
                i3 = (((((bArr[i2] & 3) << 18) | ((bArr[i2 + 1] & 63) << 12)) | ((bArr[i2 + 2] & 63) << 6)) | (bArr[i2 + 3] & 63)) - PKIFailureInfo.notAuthorized;
                char c2 = (char) (55296 | (i3 >> 10));
                c = (char) ((i3 & Place.TYPE_SUBLOCALITY_LEVEL_1) | 56320);
                i4 = i + 1;
                cArr[i] = c2;
                i2 += 4;
            } else if ((bArr[i2] & 224) == 224) {
                c = (char) ((((bArr[i2] & 15) << 12) | ((bArr[i2 + 1] & 63) << 6)) | (bArr[i2 + 2] & 63));
                i2 += 3;
                i4 = i;
            } else if ((bArr[i2] & 208) == 208) {
                c = (char) (((bArr[i2] & 31) << 6) | (bArr[i2 + 1] & 63));
                i2 += 2;
                i4 = i;
            } else if ((bArr[i2] & CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256) == CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256) {
                c = (char) (((bArr[i2] & 31) << 6) | (bArr[i2 + 1] & 63));
                i2 += 2;
                i4 = i;
            } else {
                c = (char) (bArr[i2] & GF2Field.MASK);
                i2++;
                i4 = i;
            }
            i = i4 + 1;
            cArr[i4] = c;
        }
        return new String(cArr);
    }

    public static StringList newList() {
        return new StringListImpl();
    }

    public static String[] split(String str, char c) {
        int i = 0;
        Vector vector = new Vector();
        int i2 = 1;
        String str2 = str;
        while (i2 != 0) {
            int indexOf = str2.indexOf(c);
            if (indexOf > 0) {
                vector.addElement(str2.substring(0, indexOf));
                str2 = str2.substring(indexOf + 1);
            } else {
                vector.addElement(str2);
                i2 = 0;
            }
        }
        String[] strArr = new String[vector.size()];
        while (i != strArr.length) {
            strArr[i] = (String) vector.elementAt(i);
            i++;
        }
        return strArr;
    }

    public static int toByteArray(String str, byte[] bArr, int i) {
        int length = str.length();
        for (int i2 = 0; i2 < length; i2++) {
            bArr[i + i2] = (byte) str.charAt(i2);
        }
        return length;
    }

    public static byte[] toByteArray(String str) {
        byte[] bArr = new byte[str.length()];
        for (int i = 0; i != bArr.length; i++) {
            bArr[i] = (byte) str.charAt(i);
        }
        return bArr;
    }

    public static byte[] toByteArray(char[] cArr) {
        byte[] bArr = new byte[cArr.length];
        for (int i = 0; i != bArr.length; i++) {
            bArr[i] = (byte) cArr[i];
        }
        return bArr;
    }

    public static String toLowerCase(String str) {
        int i = 0;
        char[] toCharArray = str.toCharArray();
        int i2 = 0;
        while (i != toCharArray.length) {
            char c = toCharArray[i];
            if ('A' <= c && Matrix.MATRIX_TYPE_ZERO >= c) {
                i2 = 1;
                toCharArray[i] = (char) ((c - 65) + 97);
            }
            i++;
        }
        return i2 != 0 ? new String(toCharArray) : str;
    }

    public static void toUTF8ByteArray(char[] cArr, OutputStream outputStream) {
        int i = 0;
        while (i < cArr.length) {
            char c = cArr[i];
            if (c < '\u0080') {
                outputStream.write(c);
            } else if (c < '\u0800') {
                outputStream.write((c >> 6) | CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256);
                outputStream.write((c & 63) | X509KeyUsage.digitalSignature);
            } else if (c < '\ud800' || c > '\udfff') {
                outputStream.write((c >> 12) | 224);
                outputStream.write(((c >> 6) & 63) | X509KeyUsage.digitalSignature);
                outputStream.write((c & 63) | X509KeyUsage.digitalSignature);
            } else if (i + 1 >= cArr.length) {
                throw new IllegalStateException("invalid UTF-16 codepoint");
            } else {
                i++;
                char c2 = cArr[i];
                if (c > '\udbff') {
                    throw new IllegalStateException("invalid UTF-16 codepoint");
                }
                int i2 = (((c & Place.TYPE_SUBLOCALITY_LEVEL_1) << 10) | (c2 & Place.TYPE_SUBLOCALITY_LEVEL_1)) + PKIFailureInfo.notAuthorized;
                outputStream.write((i2 >> 18) | 240);
                outputStream.write(((i2 >> 12) & 63) | X509KeyUsage.digitalSignature);
                outputStream.write(((i2 >> 6) & 63) | X509KeyUsage.digitalSignature);
                outputStream.write((i2 & 63) | X509KeyUsage.digitalSignature);
            }
            i++;
        }
    }

    public static byte[] toUTF8ByteArray(String str) {
        return toUTF8ByteArray(str.toCharArray());
    }

    public static byte[] toUTF8ByteArray(char[] cArr) {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            toUTF8ByteArray(cArr, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("cannot encode string to byte array!");
        }
    }

    public static String toUpperCase(String str) {
        int i = 0;
        char[] toCharArray = str.toCharArray();
        int i2 = 0;
        while (i != toCharArray.length) {
            char c = toCharArray[i];
            if ('a' <= c && 'z' >= c) {
                i2 = 1;
                toCharArray[i] = (char) ((c - 97) + 65);
            }
            i++;
        }
        return i2 != 0 ? new String(toCharArray) : str;
    }
}
