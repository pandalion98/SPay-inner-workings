/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Character
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.util.ArrayList
 */
package com.americanexpress.mobilepayments.hceclient.utils.common;

import java.util.ArrayList;

public class LLVARUtil {
    public static final char EMPTY_STRING = '0';
    public static final char HEX_STRING = '2';
    public static final char PLAIN_TEXT = '1';

    public static String byteArrayToString(byte[] arrby) {
        String string = null;
        if (arrby != null) {
            int n2 = arrby.length;
            string = null;
            if (n2 > 0) {
                String string2 = new String(arrby);
                int n3 = Integer.parseInt((String)string2.substring(1, 1 + Integer.parseInt((String)String.valueOf((char)string2.charAt(0)))));
                string = string2.substring(string2.length() - n3);
            }
        }
        return string;
    }

    public static byte[] emptyByteArray() {
        return "10".getBytes();
    }

    public static int getBufferLength(String string) {
        int n2;
        char[] arrc = string.toCharArray();
        String string2 = "";
        if (Character.isDigit((char)arrc[3]) && arrc.length >= (n2 = Character.getNumericValue((char)arrc[3]))) {
            int n3 = 0;
            for (int i2 = 0; i2 < n2; ++i2) {
                StringBuilder stringBuilder = new StringBuilder().append(string2);
                String string3 = stringBuilder.append(arrc[++n3 + 3]).toString();
                string2 = string3;
            }
        }
        return Integer.parseInt((String)string2);
    }

    private static String getCharBytesFromHexBytes(String string) {
        int n2;
        if (string == null) {
            throw new IllegalArgumentException("Empty Or Null Input: " + string);
        }
        if (string.charAt(0) == '2') {
            StringBuilder stringBuilder = new StringBuilder();
            for (n2 = 1; n2 < string.length(); n2 += 2) {
                StringBuilder stringBuilder2 = new StringBuilder(2);
                stringBuilder2.append(string.charAt(n2)).append(string.charAt(n2 + 1));
                stringBuilder.append((char)Integer.parseInt((String)stringBuilder2.toString(), (int)16));
            }
            return stringBuilder.toString();
        }
        return string.substring(n2);
    }

    public static Object llVarToObject(byte[] arrby, int n2) {
        Object[] arrobject = LLVARUtil.llVarToObjects(arrby);
        if (arrobject.length > 0 && arrobject[n2] != null) {
            return arrobject[n2];
        }
        return null;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static Object[] llVarToObjects(byte[] arrby) {
        char[] arrc = new String(arrby).toCharArray();
        ArrayList arrayList = new ArrayList();
        int n2 = 0;
        do {
            int n3;
            block11 : {
                block10 : {
                    block9 : {
                        String string;
                        if (n2 >= arrc.length) break block9;
                        if (!Character.isDigit((char)arrc[n2])) break block10;
                        int n4 = Character.getNumericValue((char)arrc[n2]);
                        if (arrc.length >= n4) {
                            n3 = n2;
                            string = "";
                            for (int i2 = 0; i2 < n4; ++i2) {
                                StringBuilder stringBuilder = new StringBuilder().append(string);
                                int n5 = n3 + 1;
                                String string2 = stringBuilder.append(arrc[n5]).toString();
                                string = string2;
                                n3 = n5;
                            }
                        } else {
                            n3 = n2;
                            string = "";
                        }
                        int n6 = Integer.parseInt((String)string);
                        String string3 = "";
                        if (arrc.length >= n6) {
                            for (int i3 = 0; i3 < n6; ++i3) {
                                StringBuilder stringBuilder = new StringBuilder().append(string3);
                                int n7 = n3 + 1;
                                String string4 = stringBuilder.append(arrc[n7]).toString();
                                string3 = string4;
                                n3 = n7;
                            }
                        }
                        arrayList.add((Object)string3);
                        break block11;
                    }
                    arrayList.remove(-1 + arrayList.size());
                    int n8 = Integer.parseInt((String)((String)arrayList.remove(0)));
                    int n9 = arrayList.size();
                    int n10 = 0;
                    if (n8 != n9) {
                        return null;
                    }
                    do {
                        if (n10 >= arrayList.size()) {
                            return arrayList.toArray();
                        }
                        arrayList.set(n10, (Object)LLVARUtil.getCharBytesFromHexBytes((String)arrayList.get(n10)));
                        ++n10;
                    } while (true);
                }
                n3 = n2;
            }
            n2 = n3 + 1;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static /* varargs */ byte[] objectsToLLVar(Object ... arrobject) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(LLVARUtil.toLLVar(String.valueOf((int)arrobject.length)));
        int n2 = arrobject.length;
        int n3 = 0;
        do {
            if (n3 >= n2) {
                stringBuilder.append("10");
                return stringBuilder.toString().getBytes();
            }
            String string = (String)arrobject[n3];
            if (string == null || string.length() == 0 || string.charAt(0) == '0') {
                stringBuilder.append("110");
            } else {
                char c2 = string.charAt(0);
                if (c2 != '1' && c2 != '2') {
                    throw new IllegalArgumentException("Not correct format");
                }
                stringBuilder.append(LLVARUtil.toLLVar(string));
            }
            ++n3;
        } while (true);
    }

    public static byte[] stringToByteArray(String string) {
        byte[] arrby = null;
        if (string != null) {
            int n2 = string.length();
            arrby = null;
            if (n2 != 0) {
                string.trim();
                String string2 = String.valueOf((int)string.length());
                StringBuilder stringBuilder = new StringBuilder(String.valueOf((int)string2.length()));
                stringBuilder.append(string2);
                stringBuilder.append(string);
                arrby = stringBuilder.toString().getBytes();
            }
        }
        return arrby;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static String toLLVar(String string) {
        StringBuilder stringBuilder;
        block4 : {
            block3 : {
                stringBuilder = null;
                if (string == null) break block3;
                if (string.length() == 0) break block4;
                string.trim();
                String string2 = String.valueOf((int)string.length());
                stringBuilder = new StringBuilder(String.valueOf((int)string2.length()));
                stringBuilder.append(string2);
                stringBuilder.append(string);
            }
            do {
                return stringBuilder.toString();
                break;
            } while (true);
        }
        stringBuilder = new StringBuilder("10");
        return stringBuilder.toString();
    }

    public static byte[] zeroByteArray(int n2) {
        int n3 = String.valueOf((int)n2).length();
        StringBuilder stringBuilder = new StringBuilder("111").append(String.valueOf((int)n3)).append(n2);
        for (int i2 = 0; i2 < n2; ++i2) {
            stringBuilder.append('0');
        }
        stringBuilder.append("10");
        return stringBuilder.toString().getBytes();
    }
}

