/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.util.encoders;

import org.bouncycastle.util.encoders.Translator;

public class HexTranslator
implements Translator {
    private static final byte[] hexTable = new byte[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public int decode(byte[] arrby, int n, int n2, byte[] arrby2, int n3) {
        int n4 = n2 / 2;
        int n5 = 0;
        while (n5 < n4) {
            byte by = arrby[n + n5 * 2];
            byte by2 = arrby[1 + (n + n5 * 2)];
            arrby2[n3] = by < 97 ? (byte)(by - 48 << 4) : (byte)(10 + (by - 97) << 4);
            arrby2[n3] = by2 < 97 ? (byte)(arrby2[n3] + (byte)(by2 - 48)) : (byte)(arrby2[n3] + (byte)(10 + (by2 - 97)));
            ++n3;
            ++n5;
        }
        return n4;
    }

    @Override
    public int encode(byte[] arrby, int n, int n2, byte[] arrby2, int n3) {
        int n4 = 0;
        int n5 = 0;
        while (n5 < n2) {
            arrby2[n3 + n4] = hexTable[15 & arrby[n] >> 4];
            arrby2[1 + (n3 + n4)] = hexTable[15 & arrby[n]];
            ++n;
            ++n5;
            n4 += 2;
        }
        return n2 * 2;
    }

    @Override
    public int getDecodedBlockSize() {
        return 1;
    }

    @Override
    public int getEncodedBlockSize() {
        return 2;
    }
}

