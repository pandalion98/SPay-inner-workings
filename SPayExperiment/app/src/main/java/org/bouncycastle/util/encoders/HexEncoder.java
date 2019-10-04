/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.io.OutputStream
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.util.encoders;

import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.util.encoders.Encoder;

public class HexEncoder
implements Encoder {
    protected final byte[] decodingTable = new byte[128];
    protected final byte[] encodingTable = new byte[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};

    public HexEncoder() {
        this.initialiseDecodingTable();
    }

    private static boolean ignore(char c) {
        return c == '\n' || c == '\r' || c == '\t' || c == ' ';
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public int decode(String string, OutputStream outputStream) {
        int n = 0;
        int n2 = string.length();
        do {
            if (n2 <= 0 || !HexEncoder.ignore(string.charAt(n2 - 1))) break;
            --n2;
        } while (true);
        int n3 = 0;
        while (n < n2) {
            int n4;
            int n5;
            for (n5 = n; n5 < n2 && HexEncoder.ignore(string.charAt(n5)); ++n5) {
            }
            byte[] arrby = this.decodingTable;
            byte by = arrby[string.charAt(n5)];
            for (n4 = n5 + 1; n4 < n2 && HexEncoder.ignore(string.charAt(n4)); ++n4) {
            }
            byte[] arrby2 = this.decodingTable;
            int n6 = n4 + 1;
            byte by2 = arrby2[string.charAt(n4)];
            if ((by | by2) < 0) {
                throw new IOException("invalid characters encountered in Hex string");
            }
            outputStream.write(by2 | by << 4);
            ++n3;
            n = n6;
        }
        return n3;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public int decode(byte[] arrby, int n, int n2, OutputStream outputStream) {
        int n3 = n + n2;
        do {
            if (n3 <= n || !HexEncoder.ignore((char)arrby[n3 - 1])) break;
            --n3;
        } while (true);
        int n4 = 0;
        int n5 = n;
        while (n5 < n3) {
            int n6;
            int n7;
            for (n7 = n5; n7 < n3 && HexEncoder.ignore((char)arrby[n7]); ++n7) {
            }
            byte[] arrby2 = this.decodingTable;
            byte by = arrby2[arrby[n7]];
            for (n6 = n7 + 1; n6 < n3 && HexEncoder.ignore((char)arrby[n6]); ++n6) {
            }
            byte[] arrby3 = this.decodingTable;
            int n8 = n6 + 1;
            byte by2 = arrby3[arrby[n6]];
            if ((by | by2) < 0) {
                throw new IOException("invalid characters encountered in Hex data");
            }
            outputStream.write(by2 | by << 4);
            ++n4;
            n5 = n8;
        }
        return n4;
    }

    @Override
    public int encode(byte[] arrby, int n, int n2, OutputStream outputStream) {
        for (int i = n; i < n + n2; ++i) {
            int n3 = 255 & arrby[i];
            outputStream.write((int)this.encodingTable[n3 >>> 4]);
            outputStream.write((int)this.encodingTable[n3 & 15]);
        }
        return n2 * 2;
    }

    protected void initialiseDecodingTable() {
        int n = 0;
        do {
            int n2 = this.decodingTable.length;
            if (n >= n2) break;
            this.decodingTable[n] = -1;
            ++n;
        } while (true);
        for (int i = 0; i < this.encodingTable.length; ++i) {
            this.decodingTable[this.encodingTable[i]] = (byte)i;
        }
        this.decodingTable[65] = this.decodingTable[97];
        this.decodingTable[66] = this.decodingTable[98];
        this.decodingTable[67] = this.decodingTable[99];
        this.decodingTable[68] = this.decodingTable[100];
        this.decodingTable[69] = this.decodingTable[101];
        this.decodingTable[70] = this.decodingTable[102];
    }
}

