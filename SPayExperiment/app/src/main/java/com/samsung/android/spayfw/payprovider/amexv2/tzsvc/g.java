/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayOutputStream
 *  java.lang.Character
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.util.ArrayList
 *  java.util.Arrays
 */
package com.samsung.android.spayfw.payprovider.amexv2.tzsvc;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class g {
    private static byte[] byteArrayToLLVar(byte[] arrby) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] arrby2 = g.intToBytes(arrby.length);
        byteArrayOutputStream.write(g.intToBytes(arrby2.length));
        byteArrayOutputStream.write(arrby2);
        byteArrayOutputStream.write(arrby);
        return byteArrayOutputStream.toByteArray();
    }

    /*
     * Enabled aggressive block sorting
     */
    public static byte[] bytesToLLVar(byte[][] arrby) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(g.byteArrayToLLVar(g.intToBytes(arrby.length)));
        int n2 = arrby.length;
        int n3 = 0;
        do {
            if (n3 >= n2) {
                byteArrayOutputStream.write(g.intToBytes(10));
                return byteArrayOutputStream.toByteArray();
            }
            byte[] arrby2 = arrby[n3];
            if (arrby2 == null || arrby2.length == 0 || (char)arrby2[0] == '0') {
                byteArrayOutputStream.write(g.intToBytes(110));
            } else {
                char c2 = (char)arrby2[0];
                if (c2 != '1' && c2 != '2') {
                    throw new IllegalArgumentException("Not correct format");
                }
                byteArrayOutputStream.write(g.byteArrayToLLVar(arrby2));
            }
            ++n3;
        } while (true);
    }

    public static byte[] d(String[] arrstring) {
        byte[][] arrarrby = new byte[arrstring.length][];
        for (int i2 = 0; i2 < arrstring.length; ++i2) {
            arrarrby[i2] = arrstring[i2].getBytes();
        }
        try {
            byte[] arrby = g.bytesToLLVar(arrarrby);
            return arrby;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
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
        if (arrby == null || arrby.length == 0) {
            throw new IllegalArgumentException("Empty Or Null Input");
        }
        if (arrby[0] == 50) {
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
        char[] arrc = g.getCharArrayFromByteArray(arrby);
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
                            if (g.getIntFromByteArray((byte[])arrayList.get(0)) >= arrayList.size()) {
                                arrayList.add((Object)g.getDecBytesFromHexBytes(arrby2));
                            }
                        } else if (arrayList.size() == 0) {
                            arrayList.add((Object)arrby2);
                        }
                        break block13;
                    }
                    if (g.getIntFromByteArray((byte[])arrayList.remove(0)) != arrayList.size()) {
                        return null;
                    }
                    return (byte[][])arrayList.toArray((Object[])new byte[arrayList.size()][]);
                }
                n3 = n2;
            }
            n2 = n3 + 1;
        } while (true);
    }
}

