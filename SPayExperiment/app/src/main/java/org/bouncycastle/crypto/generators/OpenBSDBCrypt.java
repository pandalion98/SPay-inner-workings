/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayOutputStream
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.lang.System
 *  org.bouncycastle.util.Arrays
 *  org.bouncycastle.util.Strings
 */
package org.bouncycastle.crypto.generators;

import java.io.ByteArrayOutputStream;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.generators.BCrypt;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Strings;

public class OpenBSDBCrypt {
    private static final byte[] decodingTable;
    private static final byte[] encodingTable;
    private static final String version = "2a";

    static {
        encodingTable = new byte[]{46, 47, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57};
        decodingTable = new byte[128];
        int n2 = 0;
        do {
            int n3 = decodingTable.length;
            if (n2 >= n3) break;
            OpenBSDBCrypt.decodingTable[n2] = -1;
            ++n2;
        } while (true);
        for (int i2 = 0; i2 < encodingTable.length; ++i2) {
            OpenBSDBCrypt.decodingTable[OpenBSDBCrypt.encodingTable[i2]] = (byte)i2;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static boolean checkPassword(String var0, char[] var1_1) {
        if (var0.length() != 60) {
            throw new DataLengthException("Bcrypt String length: " + var0.length() + ", 60 required.");
        }
        if (var0.charAt(0) != '$') throw new IllegalArgumentException("Invalid Bcrypt String format.");
        if (var0.charAt(3) != '$') throw new IllegalArgumentException("Invalid Bcrypt String format.");
        if (var0.charAt(6) != '$') {
            throw new IllegalArgumentException("Invalid Bcrypt String format.");
        }
        if (!var0.substring(1, 3).equals((Object)"2a")) {
            throw new IllegalArgumentException("Wrong Bcrypt version, 2a expected.");
        }
        try {
            var3_2 = Integer.parseInt((String)var0.substring(4, 6));
            if (var3_2 < 4) throw new IllegalArgumentException("Invalid cost factor: " + var3_2 + ", 4 < cost < 31 expected.");
            ** if (var3_2 <= 31) goto lbl-1000
        }
        catch (NumberFormatException var2_3) {
            throw new IllegalArgumentException("Invalid cost factor:" + var0.substring(4, 6));
        }
lbl-1000: // 1 sources:
        {
            throw new IllegalArgumentException("Invalid cost factor: " + var3_2 + ", 4 < cost < 31 expected.");
        }
lbl-1000: // 1 sources:
        {
        }
        if (var1_1 != null) return var0.equals((Object)OpenBSDBCrypt.generate(var1_1, OpenBSDBCrypt.decodeSaltString(var0.substring(1 + var0.lastIndexOf(36), -31 + var0.length())), var3_2));
        throw new IllegalArgumentException("Missing password.");
    }

    /*
     * Enabled aggressive block sorting
     */
    private static String createBcryptString(byte[] arrby, byte[] arrby2, int n2) {
        StringBuffer stringBuffer = new StringBuffer(60);
        stringBuffer.append('$');
        stringBuffer.append(version);
        stringBuffer.append('$');
        String string = n2 < 10 ? "0" + n2 : Integer.toString((int)n2);
        stringBuffer.append(string);
        stringBuffer.append('$');
        stringBuffer.append(OpenBSDBCrypt.encodeData(arrby2));
        stringBuffer.append(OpenBSDBCrypt.encodeData(BCrypt.generate(arrby, arrby2, n2)));
        return stringBuffer.toString();
    }

    private static byte[] decodeSaltString(String string) {
        char[] arrc = string.toCharArray();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(16);
        if (arrc.length != 22) {
            throw new DataLengthException("Invalid base64 salt length: " + arrc.length + " , 22 required.");
        }
        for (int i2 = 0; i2 < arrc.length; ++i2) {
            char c2 = arrc[i2];
            if (c2 <= 'z' && c2 >= '.' && (c2 <= '9' || c2 >= 'A')) continue;
            throw new IllegalArgumentException("Salt string contains invalid character: " + c2);
        }
        char[] arrc2 = new char[24];
        System.arraycopy((Object)arrc, (int)0, (Object)arrc2, (int)0, (int)arrc.length);
        int n2 = arrc2.length;
        for (int i3 = 0; i3 < n2; i3 += 4) {
            byte by = decodingTable[arrc2[i3]];
            byte by2 = decodingTable[arrc2[i3 + 1]];
            byte by3 = decodingTable[arrc2[i3 + 2]];
            byte by4 = decodingTable[arrc2[i3 + 3]];
            byteArrayOutputStream.write(by << 2 | by2 >> 4);
            byteArrayOutputStream.write(by2 << 4 | by3 >> 2);
            byteArrayOutputStream.write(by4 | by3 << 6);
        }
        byte[] arrby = byteArrayOutputStream.toByteArray();
        byte[] arrby2 = new byte[16];
        System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)0, (int)arrby2.length);
        return arrby2;
    }

    /*
     * Enabled aggressive block sorting
     */
    private static String encodeData(byte[] arrby) {
        boolean bl;
        if (arrby.length != 24 && arrby.length != 16) {
            throw new DataLengthException("Invalid length: " + arrby.length + ", 24 for key or 16 for salt expected");
        }
        if (arrby.length == 16) {
            byte[] arrby2 = new byte[18];
            System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)0, (int)arrby.length);
            arrby = arrby2;
            bl = true;
        } else {
            arrby[-1 + arrby.length] = 0;
            bl = false;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int n2 = arrby.length;
        for (int i2 = 0; i2 < n2; i2 += 3) {
            int n3 = 255 & arrby[i2];
            int n4 = 255 & arrby[i2 + 1];
            int n5 = 255 & arrby[i2 + 2];
            byteArrayOutputStream.write((int)encodingTable[63 & n3 >>> 2]);
            byteArrayOutputStream.write((int)encodingTable[63 & (n3 << 4 | n4 >>> 4)]);
            byteArrayOutputStream.write((int)encodingTable[63 & (n4 << 2 | n5 >>> 6)]);
            byteArrayOutputStream.write((int)encodingTable[n5 & 63]);
        }
        String string = Strings.fromByteArray((byte[])byteArrayOutputStream.toByteArray());
        if (bl) {
            return string.substring(0, 22);
        }
        return string.substring(0, -1 + string.length());
    }

    /*
     * Enabled aggressive block sorting
     */
    public static String generate(char[] arrc, byte[] arrby, int n2) {
        byte[] arrby2;
        int n3 = 72;
        if (arrc == null) {
            throw new IllegalArgumentException("Password required.");
        }
        if (arrby == null) {
            throw new IllegalArgumentException("Salt required.");
        }
        if (arrby.length != 16) {
            throw new DataLengthException("16 byte salt required: " + arrby.length);
        }
        if (n2 < 4 || n2 > 31) {
            throw new IllegalArgumentException("Invalid cost factor.");
        }
        byte[] arrby3 = Strings.toUTF8ByteArray((char[])arrc);
        if (arrby3.length < n3) {
            n3 = 1 + arrby3.length;
        }
        if ((arrby2 = new byte[n3]).length > arrby3.length) {
            System.arraycopy((Object)arrby3, (int)0, (Object)arrby2, (int)0, (int)arrby3.length);
        } else {
            System.arraycopy((Object)arrby3, (int)0, (Object)arrby2, (int)0, (int)arrby2.length);
        }
        Arrays.fill((byte[])arrby3, (byte)0);
        String string = OpenBSDBCrypt.createBcryptString(arrby2, arrby, n2);
        Arrays.fill((byte[])arrby2, (byte)0);
        return string;
    }
}

