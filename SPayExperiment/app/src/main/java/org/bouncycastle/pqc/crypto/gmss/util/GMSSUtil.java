/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.PrintStream
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package org.bouncycastle.pqc.crypto.gmss.util;

import java.io.PrintStream;

public class GMSSUtil {
    public int bytesToIntLittleEndian(byte[] arrby) {
        return 255 & arrby[0] | (255 & arrby[1]) << 8 | (255 & arrby[2]) << 16 | (255 & arrby[3]) << 24;
    }

    public int bytesToIntLittleEndian(byte[] arrby, int n) {
        int n2 = n + 1;
        int n3 = 255 & arrby[n];
        int n4 = n2 + 1;
        int n5 = n3 | (255 & arrby[n2]) << 8;
        int n6 = n4 + 1;
        return n5 | (255 & arrby[n4]) << 16 | (255 & arrby[n6]) << 24;
    }

    public byte[] concatenateArray(byte[][] arrby) {
        byte[] arrby2 = new byte[arrby.length * arrby[0].length];
        int n = 0;
        for (int i = 0; i < arrby.length; ++i) {
            System.arraycopy((Object)arrby[i], (int)0, (Object)arrby2, (int)n, (int)arrby[i].length);
            n += arrby[i].length;
        }
        return arrby2;
    }

    public int getLog(int n) {
        int n2 = 1;
        int n3 = 2;
        while (n3 < n) {
            n3 <<= 1;
            ++n2;
        }
        return n2;
    }

    public byte[] intToBytesLittleEndian(int n) {
        byte[] arrby = new byte[]{(byte)(n & 255), (byte)(255 & n >> 8), (byte)(255 & n >> 16), (byte)(255 & n >> 24)};
        return arrby;
    }

    public void printArray(String string, byte[] arrby) {
        System.out.println(string);
        int n = 0;
        for (int i = 0; i < arrby.length; ++i) {
            System.out.println(n + "; " + arrby[i]);
            ++n;
        }
    }

    public void printArray(String string, byte[][] arrby) {
        System.out.println(string);
        int n = 0;
        for (int i = 0; i < arrby.length; ++i) {
            for (int j = 0; j < arrby[0].length; ++j) {
                System.out.println(n + "; " + arrby[i][j]);
                int n2 = n + 1;
                n = n2;
            }
        }
    }

    public boolean testPowerOfTwo(int n) {
        int n2;
        for (n2 = 1; n2 < n; n2 <<= 1) {
        }
        return n == n2;
    }
}

