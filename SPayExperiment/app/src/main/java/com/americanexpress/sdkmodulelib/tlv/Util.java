/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.BufferedWriter
 *  java.io.ByteArrayOutputStream
 *  java.io.FileWriter
 *  java.io.InputStream
 *  java.io.InputStreamReader
 *  java.io.PrintStream
 *  java.io.PrintWriter
 *  java.io.StringWriter
 *  java.io.Writer
 *  java.lang.Class
 *  java.lang.ClassNotFoundException
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.SecurityManager
 *  java.lang.StackTraceElement
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.System
 *  java.lang.Thread
 *  java.lang.Throwable
 *  java.lang.UnsupportedOperationException
 *  java.math.BigInteger
 *  java.security.MessageDigest
 *  java.security.SecureRandom
 *  java.text.SimpleDateFormat
 *  java.util.BitSet
 *  java.util.Date
 *  java.util.StringTokenizer
 */
package com.americanexpress.sdkmodulelib.tlv;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.BitSet;
import java.util.Date;
import java.util.StringTokenizer;

public class Util {
    private Util() {
        throw new UnsupportedOperationException("Not allowed to instantiate");
    }

    public static int binaryCodedDecimalToInt(byte by) {
        String string = Util.byte2Hex(by);
        try {
            int n2 = Integer.parseInt((String)string);
            return n2;
        }
        catch (NumberFormatException numberFormatException) {
            throw new IllegalArgumentException("The hex representation of argument b must be digits only, and integer", (Throwable)numberFormatException);
        }
    }

    public static int binaryHexCodedDecimalToInt(String string) {
        if (string == null) {
            throw new IllegalArgumentException("Param hex cannot be null");
        }
        String string2 = Util.removeSpaces(string);
        if (string2.length() > 10) {
            throw new IllegalArgumentException("There must be a maximum of 5 hex octets. hex=" + string2);
        }
        try {
            int n2 = Integer.parseInt((String)string2);
            return n2;
        }
        catch (NumberFormatException numberFormatException) {
            throw new IllegalArgumentException("Argument hex must be all digits. hex=" + string2, (Throwable)numberFormatException);
        }
    }

    public static int binaryHexCodedDecimalToInt(byte[] arrby) {
        if (arrby == null) {
            throw new IllegalArgumentException("Param bcdArray cannot be null");
        }
        return Util.binaryHexCodedDecimalToInt(Util.byteArrayToHexString(arrby));
    }

    public static byte[] bitSet2ByteArray(BitSet bitSet) {
        byte[] arrby = new byte[1 + bitSet.length() / 8];
        for (int i2 = 0; i2 < bitSet.length(); ++i2) {
            if (!bitSet.get(i2)) continue;
            int n2 = -1 + (arrby.length - i2 / 8);
            arrby[n2] = (byte)(arrby[n2] | 1 << i2 % 8);
        }
        return arrby;
    }

    public static String byte2BinaryLiteral(byte by) {
        String string = Integer.toBinaryString((int)Util.byteToInt(by));
        if (string.length() < 8) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i2 = 0; i2 < 8 - string.length(); ++i2) {
                stringBuilder.append('0');
            }
            stringBuilder.append(string);
            return stringBuilder.toString();
        }
        return string;
    }

    public static String byte2Hex(byte by) {
        String[] arrstring = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
        int n2 = by & 255;
        int n3 = 15 & n2 >>> 4;
        int n4 = n2 & 15;
        return arrstring[n3] + arrstring[n4];
    }

    public static short byte2Short(byte by, byte by2) {
        return (short)(by << 8 | by2 & 255);
    }

    public static BitSet byteArray2BitSet(byte[] arrby) {
        BitSet bitSet = new BitSet();
        for (int i2 = 0; i2 < 8 * arrby.length; ++i2) {
            if ((arrby[-1 + (arrby.length - i2 / 8)] & 1 << i2 % 8) <= 0) continue;
            bitSet.set(i2);
        }
        return bitSet;
    }

    public static String byteArrayToHexString(byte[] arrby) {
        if (arrby == null) {
            return "";
        }
        return Util.byteArrayToHexString(arrby, 0, arrby.length);
    }

    public static String byteArrayToHexString(byte[] arrby, int n2, int n3) {
        if (arrby == null) {
            return "";
        }
        if (arrby.length < n2 + n3) {
            throw new IllegalArgumentException("startPos(" + n2 + ")+length(" + n3 + ") > byteArray.length(" + arrby.length + ")");
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i2 = 0; i2 < n3; ++i2) {
            stringBuilder.append(Integer.toHexString((int)(-256 | 255 & arrby[n2 + i2])).substring(6));
        }
        return stringBuilder.toString();
    }

    public static int byteArrayToInt(byte[] arrby) {
        return Util.byteArrayToInt(arrby, 0, arrby.length);
    }

    public static int byteArrayToInt(byte[] arrby, int n2, int n3) {
        if (arrby == null) {
            throw new IllegalArgumentException("Parameter 'byteArray' cannot be null");
        }
        if (n3 <= 0 || n3 > 4) {
            throw new IllegalArgumentException("Length must be between 1 and 4. Length = " + n3);
        }
        if (n3 == 4 && Util.isBitSet(arrby[n2], 8)) {
            throw new IllegalArgumentException("Signed bit is set (leftmost bit): " + Util.byte2Hex(arrby[n2]));
        }
        int n4 = 0;
        for (int i2 = 0; i2 < n3; ++i2) {
            n4 += (255 & arrby[n2 + i2]) << 8 * (-1 + (n3 - i2));
        }
        return n4;
    }

    public static long byteArrayToLong(byte[] arrby, int n2, int n3) {
        if (arrby == null) {
            throw new IllegalArgumentException("Parameter 'byteArray' cannot be null");
        }
        if (n3 <= 0 || n3 > 8) {
            throw new IllegalArgumentException("Length must be between 1 and 4. Length = " + n3);
        }
        if (n3 == 8 && Util.isBitSet(arrby[n2], 8)) {
            throw new IllegalArgumentException("Signed bit is set (leftmost bit): " + Util.byte2Hex(arrby[n2]));
        }
        long l2 = 0L;
        for (int i2 = 0; i2 < n3; ++i2) {
            l2 += (255L & (long)arrby[n2 + i2]) << 8 * (-1 + (n3 - i2));
        }
        return l2;
    }

    public static int byteToInt(byte by) {
        return by & 255;
    }

    public static int byteToInt(byte by, byte by2) {
        return ((by & 255) << 8) + (by2 & 255);
    }

    public static byte[] calculateSHA1(byte[] arrby) {
        return MessageDigest.getInstance((String)"SHA-1").digest(arrby);
    }

    public static byte[] copyByteArray(byte[] arrby) {
        if (arrby == null) {
            throw new IllegalArgumentException("Argument 'array2Copy' cannot be null");
        }
        return Util.copyByteArray(arrby, 0, arrby.length);
    }

    public static byte[] copyByteArray(byte[] arrby, int n2, int n3) {
        if (arrby == null) {
            throw new IllegalArgumentException("Argument 'array2Copy' cannot be null");
        }
        if (arrby.length < n2 + n3) {
            throw new IllegalArgumentException("startPos(" + n2 + ")+length(" + n3 + ") > byteArray.length(" + arrby.length + ")");
        }
        byte[] arrby2 = new byte[arrby.length];
        System.arraycopy((Object)arrby, (int)n2, (Object)arrby2, (int)0, (int)n3);
        return arrby2;
    }

    public static byte[] fromHexString(String string) {
        String string2 = Util.removeSpaces(string);
        if (string2.length() == 0) {
            return new byte[0];
        }
        if (string2.length() % 2 != 0) {
            throw new IllegalArgumentException("Input string must contain an even number of characters: " + string2);
        }
        byte[] arrby = new byte[string2.length() / 2];
        char[] arrc = string2.toCharArray();
        for (int i2 = 0; i2 < arrc.length; i2 += 2) {
            StringBuilder stringBuilder = new StringBuilder(2);
            stringBuilder.append(arrc[i2]).append(arrc[i2 + 1]);
            arrby[i2 / 2] = (byte)Integer.parseInt((String)stringBuilder.toString(), (int)16);
        }
        return arrby;
    }

    public static byte[] generateRandomBytes(int n2) {
        byte[] arrby = new byte[n2];
        new SecureRandom().nextBytes(arrby);
        return arrby;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static Class getCallerClass(int n2) {
        int n3;
        StackTraceElement[] arrstackTraceElement;
        block7 : {
            Class[] arrclass = new SecurityManager(){

                public Class[] getClassContext() {
                    return super.getClassContext();
                }
            }.getClassContext();
            n3 = 0;
            if (arrclass == null) {
                try {
                    arrstackTraceElement = Thread.currentThread().getStackTrace();
                    break block7;
                }
                catch (ClassNotFoundException classNotFoundException) {
                    // empty catch block
                    return null;
                }
            }
            while (n3 < arrclass.length) {
                if (arrclass[n3] == Util.class) {
                    return arrclass[n3 + n2];
                }
                ++n3;
            }
            return null;
        }
        while (n3 < arrstackTraceElement.length) {
            if (Class.forName((String)arrstackTraceElement[n3].getClassName()) == Util.class) {
                return Class.forName((String)arrstackTraceElement[n3 + n2].getClassName());
            }
            ++n3;
        }
        return null;
    }

    public static byte[] getCurrentDateAsNumericEncodedByteArray() {
        return Util.fromHexString(new SimpleDateFormat("yyMMdd").format(new Date()));
    }

    public static String getFormattedNanoTime(long l2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append((int)(l2 / 1000000L));
        stringBuilder.append("ms ");
        stringBuilder.append(l2 % 1000000L);
        stringBuilder.append("ns");
        return stringBuilder.toString();
    }

    public static String getSafePrintChars(byte[] arrby) {
        if (arrby == null) {
            return "";
        }
        return Util.getSafePrintChars(arrby, 0, arrby.length);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static String getSafePrintChars(byte[] arrby, int n2, int n3) {
        if (arrby == null) {
            return "";
        }
        if (arrby.length < n2 + n3) {
            throw new IllegalArgumentException("startPos(" + n2 + ")+length(" + n3 + ") > byteArray.length(" + arrby.length + ")");
        }
        StringBuilder stringBuilder = new StringBuilder();
        int n4 = n2;
        while (n4 < n2 + n3) {
            if (arrby[n4] >= 32 && arrby[n4] < 127) {
                stringBuilder.append((char)arrby[n4]);
            } else {
                stringBuilder.append(".");
            }
            ++n4;
        }
        return stringBuilder.toString();
    }

    public static String getSpaces(int n2) {
        StringBuilder stringBuilder = new StringBuilder(n2);
        for (int i2 = 0; i2 < n2; ++i2) {
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    public static String getStackTrace(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter((Writer)stringWriter));
        return stringWriter.toString();
    }

    public static String int2Hex(int n2) {
        String string = Integer.toHexString((int)n2);
        if (string.length() % 2 != 0) {
            string = "0" + string;
        }
        return string;
    }

    public static String int2HexZeroPad(int n2) {
        String string = Util.int2Hex(n2);
        if (string.length() % 2 != 0) {
            string = "0" + string;
        }
        return string;
    }

    public static byte[] intToBinaryEncodedDecimalByteArray(int n2) {
        String string = String.valueOf((int)n2);
        if (string.length() % 2 != 0) {
            string = "0" + string;
        }
        return Util.fromHexString(string);
    }

    public static byte[] intToByteArray(int n2) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte by = (byte)(n2 >>> 24);
        byte by2 = (byte)(n2 >>> 16);
        byte by3 = (byte)(n2 >>> 8);
        byte by4 = (byte)n2;
        boolean bl = false;
        if (by > 0) {
            byteArrayOutputStream.write((int)by);
            bl = true;
        }
        if (bl || by2 > 0) {
            byteArrayOutputStream.write((int)by2);
            bl = true;
        }
        if (bl || by3 > 0) {
            byteArrayOutputStream.write((int)by3);
        }
        byteArrayOutputStream.write((int)by4);
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] intToByteArray4(int n2) {
        byte[] arrby = new byte[]{(byte)(n2 >>> 24), (byte)(n2 >>> 16), (byte)(n2 >>> 8), (byte)n2};
        return arrby;
    }

    public static boolean isBitSet(byte by, int n2) {
        if (n2 < 1 || n2 > 8) {
            throw new IllegalArgumentException("parameter 'bitPos' must be between 1 and 8. bitPos=" + n2);
        }
        return (1 & by >>> n2 - 1) == 1;
    }

    public static InputStream loadResource(Class class_, String string) {
        return class_.getResourceAsStream(string);
    }

    public static void main(String[] arrstring) {
        System.out.println(Util.prettyPrintHexNoWrap(Util.resizeArray(new byte[]{1}, 0)));
        System.out.println(Util.prettyPrintHexNoWrap(Util.resizeArray(new byte[]{1}, 1)));
        System.out.println(Util.prettyPrintHexNoWrap(Util.resizeArray(new byte[]{1}, 2)));
        System.out.println(Util.prettyPrintHexNoWrap(Util.resizeArray(new byte[]{1, 2}, 1)));
        System.out.println(Util.prettyPrintHexNoWrap(Util.resizeArray(new byte[]{1, 2}, 4)));
    }

    public static byte[] performRSA(byte[] arrby, byte[] arrby2, byte[] arrby3) {
        BigInteger bigInteger;
        BigInteger bigInteger2;
        byte[] arrby4;
        int n2 = arrby.length;
        if (arrby2[0] >= -128) {
            byte[] arrby5 = new byte[1 + arrby2.length];
            arrby5[0] = 0;
            System.arraycopy((Object)arrby2, (int)0, (Object)arrby5, (int)1, (int)arrby2.length);
            arrby2 = arrby5;
        }
        if (arrby3[0] >= -128) {
            byte[] arrby6 = new byte[1 + arrby3.length];
            arrby6[0] = 0;
            System.arraycopy((Object)arrby3, (int)0, (Object)arrby6, (int)1, (int)arrby3.length);
            arrby3 = arrby6;
        }
        if (arrby[0] >= -128) {
            byte[] arrby7 = new byte[1 + arrby.length];
            arrby7[0] = 0;
            System.arraycopy((Object)arrby, (int)0, (Object)arrby7, (int)1, (int)arrby.length);
            arrby = arrby7;
        }
        if ((arrby4 = new BigInteger(arrby).modPow(bigInteger2 = new BigInteger(arrby2), bigInteger = new BigInteger(arrby3)).toByteArray()).length == n2 + 1 && arrby4[0] == 0) {
            byte[] arrby8 = new byte[n2];
            System.arraycopy((Object)arrby4, (int)1, (Object)arrby8, (int)0, (int)n2);
            return arrby8;
        }
        return arrby4;
    }

    public static String prettyPrintHex(String string) {
        return Util.prettyPrintHex(string, 0, true);
    }

    public static String prettyPrintHex(String string, int n2) {
        return Util.prettyPrintHex(string, n2, true);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static String prettyPrintHex(String string, int n2, boolean bl) {
        StringBuilder stringBuilder = new StringBuilder();
        int n3 = 0;
        while (n3 < string.length()) {
            stringBuilder.append(string.charAt(n3));
            int n4 = n3 + 1;
            if (bl && n4 % 32 == 0 && n4 != string.length()) {
                stringBuilder.append("\n").append(Util.getSpaces(n2));
            } else if (n4 % 2 == 0 && n4 != string.length()) {
                stringBuilder.append(" ");
            }
            ++n3;
        }
        return stringBuilder.toString();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static String prettyPrintHex(BigInteger bigInteger) {
        byte[] arrby;
        byte[] arrby2 = bigInteger.toByteArray();
        if (arrby2[0] == 0) {
            arrby = new byte[-1 + arrby2.length];
            System.arraycopy((Object)arrby2, (int)1, (Object)arrby, (int)0, (int)(-1 + arrby2.length));
            do {
                return Util.prettyPrintHex(arrby);
                break;
            } while (true);
        }
        arrby = arrby2;
        return Util.prettyPrintHex(arrby);
    }

    public static String prettyPrintHex(byte[] arrby) {
        return Util.prettyPrintHex(Util.byteArrayToHexString(arrby), 0, true);
    }

    public static String prettyPrintHex(byte[] arrby, int n2) {
        return Util.prettyPrintHex(Util.byteArrayToHexString(arrby), n2, true);
    }

    public static String prettyPrintHex(byte[] arrby, int n2, int n3) {
        return Util.prettyPrintHex(Util.byteArrayToHexString(arrby, n2, n3), 0, true);
    }

    public static String prettyPrintHexNoWrap(String string) {
        return Util.prettyPrintHex(string, 0, false);
    }

    public static String prettyPrintHexNoWrap(byte[] arrby) {
        return Util.prettyPrintHex(Util.byteArrayToHexString(arrby), 0, false);
    }

    public static String prettyPrintHexNoWrap(byte[] arrby, int n2, int n3) {
        return Util.prettyPrintHex(Util.byteArrayToHexString(arrby, n2, n3), 0, false);
    }

    public static String readInputStreamToString(InputStream inputStream, String string) {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, string);
        char[] arrc = new char[5000];
        StringBuilder stringBuilder = new StringBuilder(5000);
        int n2 = inputStreamReader.read(arrc, 0, arrc.length);
        while (n2 != -1) {
            stringBuilder.append(arrc, 0, n2);
            n2 = inputStreamReader.read(arrc, 0, arrc.length);
        }
        return stringBuilder.toString();
    }

    public static String removeCRLFTab(String string) {
        StringTokenizer stringTokenizer = new StringTokenizer(string, "\r\n\t", false);
        StringBuilder stringBuilder = new StringBuilder();
        while (stringTokenizer.hasMoreElements()) {
            stringBuilder.append(stringTokenizer.nextElement());
        }
        return stringBuilder.toString();
    }

    public static String removeSpaces(String string) {
        return string.replaceAll(" ", "");
    }

    /*
     * Enabled aggressive block sorting
     */
    public static byte[] resizeArray(byte[] arrby, int n2) {
        if (arrby == null) {
            throw new IllegalArgumentException("byte array cannot be null");
        }
        if (n2 < 0) {
            throw new IllegalArgumentException("Illegal new length: " + n2 + ". Must be >= 0");
        }
        if (n2 == 0) {
            return new byte[0];
        }
        byte[] arrby2 = new byte[n2];
        int n3 = arrby2.length > arrby.length ? 0 : arrby.length - arrby2.length;
        int n4 = arrby2.length;
        int n5 = arrby.length;
        int n6 = 0;
        if (n4 > n5) {
            n6 = arrby2.length - arrby.length;
        }
        int n7 = arrby2.length > arrby.length ? arrby.length : arrby2.length;
        System.arraycopy((Object)arrby, (int)n3, (Object)arrby2, (int)n6, (int)n7);
        return arrby2;
    }

    public static byte setBit(byte by, int n2, boolean bl) {
        if (n2 < 1 || n2 > 8) {
            throw new IllegalArgumentException("parameter 'bitPos' must be between 1 and 8. bitPos=" + n2);
        }
        if (bl) {
            return (byte)(by | 1 << n2 - 1);
        }
        return (byte)(by & (-1 ^ 1 << n2 - 1));
    }

    public static String short2Hex(short s2) {
        byte by = (byte)(s2 >>> 8);
        byte by2 = (byte)(s2 & 255);
        return Util.byte2Hex(by) + Util.byte2Hex(by2);
    }

    public static void writeStringToFile(String string, String string2, boolean bl) {
        BufferedWriter bufferedWriter = new BufferedWriter((Writer)new FileWriter(string2, bl));
        bufferedWriter.write(string);
        bufferedWriter.close();
    }

}

