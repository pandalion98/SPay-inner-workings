/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayOutputStream
 *  java.io.PrintStream
 *  java.lang.Character
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.System
 *  java.util.ArrayList
 *  java.util.Arrays
 */
package com.americanexpress.mobilepayments.hceclient.utils.common;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

public class LLVARByteUtil {
    private static final int DECIMAL_RADIX = 10;
    private static final char HEX = '2';
    private static final char PLAIN_TEXT = '1';
    private static final char ZERO = '0';

    private static byte[] byteArrayToLLVar(byte[] arrby) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] arrby2 = LLVARByteUtil.intToBytes(arrby.length);
        byte[] arrby3 = LLVARByteUtil.intToBytes(arrby2.length);
        byteArrayOutputStream.write(arrby3, 0, arrby3.length);
        byteArrayOutputStream.write(arrby2, 0, arrby2.length);
        byteArrayOutputStream.write(arrby, 0, arrby.length);
        return byteArrayOutputStream.toByteArray();
    }

    /*
     * Enabled aggressive block sorting
     */
    public static byte[] bytesToLLVar(byte[][] arrby) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] arrby2 = LLVARByteUtil.byteArrayToLLVar(LLVARByteUtil.intToBytes(arrby.length));
        byteArrayOutputStream.write(arrby2, 0, arrby2.length);
        int n2 = arrby.length;
        int n3 = 0;
        do {
            if (n3 >= n2) {
                byte[] arrby3 = LLVARByteUtil.intToBytes(10);
                byteArrayOutputStream.write(arrby3, 0, arrby3.length);
                return byteArrayOutputStream.toByteArray();
            }
            byte[] arrby4 = arrby[n3];
            if (arrby4 == null || arrby4.length == 0 || (char)arrby4[0] == '0') {
                byte[] arrby5 = LLVARByteUtil.intToBytes(110);
                byteArrayOutputStream.write(arrby5, 0, arrby5.length);
            } else {
                char c2 = (char)arrby4[0];
                if (c2 != '1' && c2 != '2') {
                    throw new IllegalArgumentException("Not correct format");
                }
                byte[] arrby6 = LLVARByteUtil.byteArrayToLLVar(arrby4);
                byteArrayOutputStream.write(arrby6, 0, arrby6.length);
            }
            ++n3;
        } while (true);
    }

    public static int getBufferLength(byte[] arrby) {
        char[] arrc = LLVARByteUtil.getCharArrayFromByteArray(arrby);
        boolean bl = Character.isDigit((char)arrc[3]);
        int n2 = 0;
        if (bl) {
            int n3 = Character.getNumericValue((char)arrc[3]);
            int n4 = arrc.length;
            n2 = 0;
            if (n4 >= n3) {
                int n5 = 0;
                for (int i2 = 0; i2 < n3; ++i2) {
                    int n6 = n2 * 10;
                    int n7 = n6 + Character.digit((char)arrc[++n5 + 3], (int)10);
                    n2 = n7;
                }
            }
        }
        return n2;
    }

    private static char[] getCharArrayFromByteArray(byte[] arrby) {
        char[] arrc = new char[arrby.length];
        int n2 = arrby.length;
        int n3 = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            byte by = arrby[i2];
            int n4 = n3 + 1;
            arrc[n3] = (char)by;
            n3 = n4;
        }
        return arrc;
    }

    private static byte[] getDecBytesFromHexBytes(byte[] arrby) {
        int n2;
        if (arrby[0] == 50) {
            if (arrby == null) {
                throw new IllegalArgumentException("Empty Or Null Input: " + arrby);
            }
            byte[] arrby2 = new byte[arrby.length / 2];
            for (n2 = 1; n2 < arrby.length; n2 += 2) {
                StringBuilder stringBuilder = new StringBuilder(2);
                stringBuilder.append((char)arrby[n2]).append((char)arrby[n2 + 1]);
                arrby2[n2 / 2] = (byte)Integer.parseInt((String)stringBuilder.toString(), (int)16);
            }
            return arrby2;
        }
        return Arrays.copyOfRange((byte[])arrby, (int)n2, (int)arrby.length);
    }

    private static int getIntFromByteArray(byte[] arrby) {
        int n2 = arrby.length;
        int n3 = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            byte by = arrby[i2];
            n3 = n3 * 10 + Character.digit((int)by, (int)10);
        }
        return n3;
    }

    private static byte[] intToBytes(int n2) {
        if (n2 > 0) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            char[] arrc = new char[(int)(1.0 + Math.log10((double)n2))];
            for (int i2 = -1 + arrc.length; i2 >= 0; --i2) {
                arrc[i2] = Character.forDigit((int)(n2 % 10), (int)10);
                n2 /= 10;
            }
            int n3 = arrc.length;
            for (int i3 = 0; i3 < n3; ++i3) {
                byteArrayOutputStream.write((int)arrc[i3]);
            }
            return byteArrayOutputStream.toByteArray();
        }
        if (n2 == 0) {
            return new byte[]{48};
        }
        return null;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static byte[][] llVarToBytes(byte[] arrby) {
        char[] arrc = LLVARByteUtil.getCharArrayFromByteArray(arrby);
        ArrayList arrayList = new ArrayList();
        int n2 = 0;
        do {
            int n3;
            block13 : {
                block12 : {
                    block11 : {
                        int n4;
                        if (n2 >= arrc.length) break block11;
                        if (!Character.isDigit((char)arrc[n2])) break block12;
                        int n5 = Character.getNumericValue((char)arrc[n2]);
                        if (arrc.length >= n5) {
                            n3 = n2;
                            n4 = 0;
                            for (int i2 = 0; i2 < n5; ++i2) {
                                int n6 = n4 * 10;
                                int n7 = n3 + 1;
                                int n8 = n6 + Character.digit((char)arrc[n7], (int)10);
                                n4 = n8;
                                n3 = n7;
                            }
                        } else {
                            n3 = n2;
                            n4 = 0;
                        }
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        if (arrc.length >= n4) {
                            for (int i3 = 0; i3 < n4; ++i3) {
                                int n9 = n3 + 1;
                                byteArrayOutputStream.write((int)arrc[n9]);
                                n3 = n9;
                            }
                        }
                        byte[] arrby2 = byteArrayOutputStream.toByteArray();
                        if (arrayList.size() > 0) {
                            if (LLVARByteUtil.getIntFromByteArray((byte[])arrayList.get(0)) >= arrayList.size()) {
                                arrayList.add((Object)LLVARByteUtil.getDecBytesFromHexBytes(arrby2));
                            }
                        } else if (arrayList.size() == 0) {
                            arrayList.add((Object)arrby2);
                        }
                        break block13;
                    }
                    if (LLVARByteUtil.getIntFromByteArray((byte[])arrayList.remove(0)) != arrayList.size()) {
                        return null;
                    }
                    return (byte[][])arrayList.toArray((Object[])new byte[arrayList.size()][]);
                }
                n3 = n2;
            }
            n2 = n3 + 1;
        } while (true);
    }

    public static void main(String[] arrstring) {
        byte[][] arrarrby = new byte[][]{"1STRING".getBytes(), "268656c6c6f".getBytes(), "1FLOAT".getBytes(), "1LONG".getBytes(), "1DECIMAL".getBytes()};
        byte[] arrby = LLVARByteUtil.bytesToLLVar(arrarrby);
        System.out.println(new String(arrby));
        for (byte[] arrby2 : LLVARByteUtil.llVarToBytes(arrby)) {
            System.out.println("byteArray ::String---" + new String(arrby2));
            System.out.println("byteArray ::Content---" + Arrays.toString((byte[])arrby2));
        }
    }

    public static byte[] zeroByteArray(int n2) {
        if (n2 > 0) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] arrby = LLVARByteUtil.intToBytes(111);
            byteArrayOutputStream.write(arrby, 0, arrby.length);
            byte[] arrby2 = LLVARByteUtil.intToBytes((int)(1.0 + Math.log10((double)n2)));
            byteArrayOutputStream.write(arrby2, 0, arrby2.length);
            byte[] arrby3 = LLVARByteUtil.intToBytes(n2);
            byteArrayOutputStream.write(arrby3, 0, arrby3.length);
            for (int i2 = 0; i2 < n2; ++i2) {
                byteArrayOutputStream.write(48);
            }
            byte[] arrby4 = LLVARByteUtil.intToBytes(10);
            byteArrayOutputStream.write(arrby4, 0, arrby4.length);
            return byteArrayOutputStream.toByteArray();
        }
        return null;
    }
}

