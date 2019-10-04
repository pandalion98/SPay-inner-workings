/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.mastercard.mobile_api.utils.tlv;

public class BERTLVUtils {
    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static int getNbBytes(int n2) {
        int n3 = 5;
        if (n2 < 0) return n3;
        if (n2 < 128) {
            return 1;
        }
        if (n2 < 256) {
            return 2;
        }
        if (n2 < 65536) {
            return 3;
        }
        if (n2 >= 16777216) return n3;
        return 4;
    }

    public static int getTLVLength(byte[] arrby, int n2) {
        if (arrby[n2] > 0 && (255 & arrby[n2]) < 128) {
            return 255 & arrby[n2];
        }
        if (arrby[n2] == -127) {
            return 255 & arrby[n2 + 1];
        }
        if (arrby[n2] == -126) {
            return (255 & arrby[n2 + 1]) << 8 | 255 & arrby[n2 + 2];
        }
        if (arrby[n2] == -125) {
            return (255 & arrby[n2 + 1]) << 16 | (255 & arrby[n2 + 2]) << 8 | 255 & arrby[n2 + 3];
        }
        if (arrby[n2] == -124) {
            return (255 & arrby[n2 + 0]) << 24 | (255 & arrby[n2 + 1]) << 16 | (255 & arrby[n2 + 2]) << 8 | 255 & arrby[n2 + 3];
        }
        return 255 & arrby[n2];
    }

    public static int getTLVLengthByte(byte[] arrby, int n2) {
        if (arrby[n2] == -127) {
            return 2;
        }
        if (arrby[n2] == -126) {
            return 3;
        }
        if (arrby[n2] == -125) {
            return 4;
        }
        if (arrby[n2] == -124) {
            return 5;
        }
        return 1;
    }

    public static int setBERTLVLength(int n2, byte[] arrby, int n3) {
        if (n2 < 128) {
            arrby[n3] = (byte)n2;
            return n3 + 1;
        }
        if (n2 < 256) {
            arrby[n3] = -127;
            arrby[n3 + 1] = (byte)n2;
            return n3 + 2;
        }
        if (n2 < 65536) {
            arrby[n3] = -126;
            arrby[n3 + 1] = (byte)(n2 >> 8);
            arrby[n3 + 2] = (byte)(n2 & 255);
            return n3 + 3;
        }
        if (n2 < 16777216) {
            arrby[n3] = -125;
            arrby[n3 + 1] = (byte)(n2 >> 16);
            arrby[n3 + 2] = (byte)(255 & n2 >> 8);
            arrby[n3 + 3] = (byte)(n2 & 255);
            return n3 + 4;
        }
        arrby[n3] = -124;
        arrby[n3 + 1] = (byte)(n2 >> 24);
        arrby[n3 + 2] = (byte)(255 & n2 >> 16);
        arrby[n3 + 3] = (byte)(255 & n2 >> 8);
        arrby[n3 + 4] = (byte)(n2 & 255);
        return n3 + 5;
    }
}

