/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.ArrayIndexOutOfBoundsException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package org.bouncycastle.pqc.math.linearalgebra;

public final class ByteUtils {
    private static final char[] HEX_CHARS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private ByteUtils() {
    }

    public static byte[] clone(byte[] arrby) {
        if (arrby == null) {
            return null;
        }
        byte[] arrby2 = new byte[arrby.length];
        System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)0, (int)arrby.length);
        return arrby2;
    }

    public static byte[] concatenate(byte[] arrby, byte[] arrby2) {
        byte[] arrby3 = new byte[arrby.length + arrby2.length];
        System.arraycopy((Object)arrby, (int)0, (Object)arrby3, (int)0, (int)arrby.length);
        System.arraycopy((Object)arrby2, (int)0, (Object)arrby3, (int)arrby.length, (int)arrby2.length);
        return arrby3;
    }

    public static byte[] concatenate(byte[][] arrby) {
        int n = arrby[0].length;
        byte[] arrby2 = new byte[n * arrby.length];
        int n2 = 0;
        for (int i = 0; i < arrby.length; ++i) {
            System.arraycopy((Object)arrby[i], (int)0, (Object)arrby2, (int)n2, (int)n);
            n2 += n;
        }
        return arrby2;
    }

    public static int deepHashCode(byte[] arrby) {
        int n = 1;
        for (int i = 0; i < arrby.length; ++i) {
            n = n * 31 + arrby[i];
        }
        return n;
    }

    public static int deepHashCode(byte[][] arrby) {
        int n = 1;
        for (int i = 0; i < arrby.length; ++i) {
            n = n * 31 + ByteUtils.deepHashCode(arrby[i]);
        }
        return n;
    }

    public static int deepHashCode(byte[][][] arrby) {
        int n = 1;
        for (int i = 0; i < arrby.length; ++i) {
            n = n * 31 + ByteUtils.deepHashCode(arrby[i]);
        }
        return n;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static boolean equals(byte[] arrby, byte[] arrby2) {
        boolean bl = true;
        if (arrby == null) {
            if (arrby2 == null) return bl;
            return false;
        }
        boolean bl2 = false;
        if (arrby2 == null) return bl2;
        int n = arrby.length;
        int n2 = arrby2.length;
        bl2 = false;
        if (n != n2) return bl2;
        int n3 = -1 + arrby.length;
        boolean bl3 = bl;
        while (n3 >= 0) {
            boolean bl4 = arrby[n3] == arrby2[n3] ? bl : false;
            bl3 &= bl4;
            --n3;
        }
        return bl3;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean equals(byte[][] arrby, byte[][] arrby2) {
        if (arrby.length != arrby2.length) {
            return false;
        }
        int n = -1 + arrby.length;
        boolean bl = true;
        int n2 = n;
        while (n2 >= 0) {
            boolean bl2 = bl & ByteUtils.equals(arrby[n2], arrby2[n2]);
            --n2;
            bl = bl2;
        }
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static boolean equals(byte[][][] arrby, byte[][][] arrby2) {
        if (arrby.length == arrby2.length) {
            int n = -1 + arrby.length;
            boolean bl = true;
            do {
                if (n < 0) {
                    return bl;
                }
                if (arrby[n].length != arrby2[n].length) break;
                int n2 = -1 + arrby[n].length;
                boolean bl2 = bl;
                for (int i = n2; i >= 0; bl2 &= ByteUtils.equals((byte[])arrby[n][i], (byte[])arrby2[n][i]), --i) {
                }
                --n;
                bl = bl2;
            } while (true);
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static byte[] fromHexString(String string) {
        int n = 0;
        char[] arrc = string.toUpperCase().toCharArray();
        int n2 = 0;
        for (int i = 0; i < arrc.length; ++i) {
            if ((arrc[i] < '0' || arrc[i] > '9') && (arrc[i] < 'A' || arrc[i] > 'F')) continue;
            ++n2;
        }
        byte[] arrby = new byte[n2 + 1 >> 1];
        int n3 = n2 & 1;
        do {
            block8 : {
                block7 : {
                    block6 : {
                        if (n >= arrc.length) {
                            return arrby;
                        }
                        if (arrc[n] < '0' || arrc[n] > '9') break block6;
                        int n4 = n3 >> 1;
                        arrby[n4] = (byte)(arrby[n4] << 4);
                        int n5 = n3 >> 1;
                        arrby[n5] = (byte)(arrby[n5] | -48 + arrc[n]);
                        break block7;
                    }
                    if (arrc[n] < 'A' || arrc[n] > 'F') break block8;
                    int n6 = n3 >> 1;
                    arrby[n6] = (byte)(arrby[n6] << 4);
                    int n7 = n3 >> 1;
                    arrby[n7] = (byte)(arrby[n7] | 10 + (-65 + arrc[n]));
                }
                ++n3;
            }
            ++n;
        } while (true);
    }

    public static byte[][] split(byte[] arrby, int n) {
        if (n > arrby.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        byte[][] arrarrby = new byte[][]{new byte[n], new byte[arrby.length - n]};
        System.arraycopy((Object)arrby, (int)0, (Object)arrarrby[0], (int)0, (int)n);
        System.arraycopy((Object)arrby, (int)n, (Object)arrarrby[1], (int)0, (int)(arrby.length - n));
        return arrarrby;
    }

    public static byte[] subArray(byte[] arrby, int n) {
        return ByteUtils.subArray(arrby, n, arrby.length);
    }

    public static byte[] subArray(byte[] arrby, int n, int n2) {
        byte[] arrby2 = new byte[n2 - n];
        System.arraycopy((Object)arrby, (int)n, (Object)arrby2, (int)0, (int)(n2 - n));
        return arrby2;
    }

    public static String toBinaryString(byte[] arrby) {
        String string = "";
        for (int i = 0; i < arrby.length; ++i) {
            byte by = arrby[i];
            for (int j = 0; j < 8; ++j) {
                int n = 1 & by >>> j;
                String string2 = string + n;
                string = string2;
            }
            if (i == -1 + arrby.length) continue;
            string = string + " ";
        }
        return string;
    }

    public static char[] toCharArray(byte[] arrby) {
        char[] arrc = new char[arrby.length];
        for (int i = 0; i < arrby.length; ++i) {
            arrc[i] = (char)arrby[i];
        }
        return arrc;
    }

    public static String toHexString(byte[] arrby) {
        String string = "";
        for (int i = 0; i < arrby.length; ++i) {
            String string2 = string + HEX_CHARS[15 & arrby[i] >>> 4];
            string = string2 + HEX_CHARS[15 & arrby[i]];
        }
        return string;
    }

    public static String toHexString(byte[] arrby, String string, String string2) {
        String string3 = new String(string);
        for (int i = 0; i < arrby.length; ++i) {
            String string4 = string3 + HEX_CHARS[15 & arrby[i] >>> 4];
            string3 = string4 + HEX_CHARS[15 & arrby[i]];
            if (i >= -1 + arrby.length) continue;
            string3 = string3 + string2;
        }
        return string3;
    }

    public static byte[] xor(byte[] arrby, byte[] arrby2) {
        byte[] arrby3 = new byte[arrby.length];
        for (int i = -1 + arrby.length; i >= 0; --i) {
            arrby3[i] = (byte)(arrby[i] ^ arrby2[i]);
        }
        return arrby3;
    }
}

