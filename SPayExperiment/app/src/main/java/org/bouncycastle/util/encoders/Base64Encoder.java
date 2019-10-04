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

public class Base64Encoder
implements Encoder {
    protected final byte[] decodingTable = new byte[128];
    protected final byte[] encodingTable = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
    protected byte padding = (byte)61;

    public Base64Encoder() {
        this.initialiseDecodingTable();
    }

    private int decodeLastBlock(OutputStream outputStream, char c, char c2, char c3, char c4) {
        if (c3 == this.padding) {
            byte by = this.decodingTable[c];
            byte by2 = this.decodingTable[c2];
            if ((by | by2) < 0) {
                throw new IOException("invalid characters encountered at end of base64 data");
            }
            outputStream.write(by << 2 | by2 >> 4);
            return 1;
        }
        if (c4 == this.padding) {
            byte by = this.decodingTable[c3];
            byte by3 = this.decodingTable[c];
            byte by4 = this.decodingTable[c2];
            if ((by | (by3 | by4)) < 0) {
                throw new IOException("invalid characters encountered at end of base64 data");
            }
            outputStream.write(by3 << 2 | by4 >> 4);
            outputStream.write(by4 << 4 | by >> 2);
            return 2;
        }
        byte by = this.decodingTable[c4];
        byte by5 = this.decodingTable[c3];
        byte by6 = this.decodingTable[c];
        byte by7 = this.decodingTable[c2];
        if ((by | (by5 | (by6 | by7))) < 0) {
            throw new IOException("invalid characters encountered at end of base64 data");
        }
        outputStream.write(by6 << 2 | by7 >> 4);
        outputStream.write(by7 << 4 | by5 >> 2);
        outputStream.write(by | by5 << 6);
        return 3;
    }

    private boolean ignore(char c) {
        return c == '\n' || c == '\r' || c == '\t' || c == ' ';
    }

    private int nextI(String string, int n, int n2) {
        while (n < n2 && this.ignore(string.charAt(n))) {
            ++n;
        }
        return n;
    }

    private int nextI(byte[] arrby, int n, int n2) {
        while (n < n2 && this.ignore((char)arrby[n])) {
            ++n;
        }
        return n;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public int decode(String string, OutputStream outputStream) {
        int n = string.length();
        do {
            if (n <= 0 || !this.ignore(string.charAt(n - 1))) break;
            --n;
        } while (true);
        int n2 = n - 4;
        int n3 = this.nextI(string, 0, n2);
        int n4 = 0;
        while (n3 < n2) {
            byte[] arrby = this.decodingTable;
            int n5 = n3 + 1;
            byte by = arrby[string.charAt(n3)];
            int n6 = this.nextI(string, n5, n2);
            byte[] arrby2 = this.decodingTable;
            int n7 = n6 + 1;
            byte by2 = arrby2[string.charAt(n6)];
            int n8 = this.nextI(string, n7, n2);
            byte[] arrby3 = this.decodingTable;
            int n9 = n8 + 1;
            byte by3 = arrby3[string.charAt(n8)];
            int n10 = this.nextI(string, n9, n2);
            byte[] arrby4 = this.decodingTable;
            int n11 = n10 + 1;
            byte by4 = arrby4[string.charAt(n10)];
            if ((by4 | (by3 | (by | by2))) < 0) {
                throw new IOException("invalid characters encountered in base64 data");
            }
            outputStream.write(by << 2 | by2 >> 4);
            outputStream.write(by2 << 4 | by3 >> 2);
            outputStream.write(by4 | by3 << 6);
            int n12 = n4 + 3;
            n3 = this.nextI(string, n11, n2);
            n4 = n12;
        }
        return n4 + this.decodeLastBlock(outputStream, string.charAt(n - 4), string.charAt(n - 3), string.charAt(n - 2), string.charAt(n - 1));
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public int decode(byte[] arrby, int n, int n2, OutputStream outputStream) {
        int n3 = n + n2;
        do {
            if (n3 <= n || !this.ignore((char)arrby[n3 - 1])) break;
            --n3;
        } while (true);
        int n4 = n3 - 4;
        int n5 = this.nextI(arrby, n, n4);
        int n6 = 0;
        while (n5 < n4) {
            byte[] arrby2 = this.decodingTable;
            int n7 = n5 + 1;
            byte by = arrby2[arrby[n5]];
            int n8 = this.nextI(arrby, n7, n4);
            byte[] arrby3 = this.decodingTable;
            int n9 = n8 + 1;
            byte by2 = arrby3[arrby[n8]];
            int n10 = this.nextI(arrby, n9, n4);
            byte[] arrby4 = this.decodingTable;
            int n11 = n10 + 1;
            byte by3 = arrby4[arrby[n10]];
            int n12 = this.nextI(arrby, n11, n4);
            byte[] arrby5 = this.decodingTable;
            int n13 = n12 + 1;
            byte by4 = arrby5[arrby[n12]];
            if ((by4 | (by3 | (by | by2))) < 0) {
                throw new IOException("invalid characters encountered in base64 data");
            }
            outputStream.write(by << 2 | by2 >> 4);
            outputStream.write(by2 << 4 | by3 >> 2);
            outputStream.write(by4 | by3 << 6);
            int n14 = n6 + 3;
            n5 = this.nextI(arrby, n13, n4);
            n6 = n14;
        }
        return n6 + this.decodeLastBlock(outputStream, (char)arrby[n3 - 4], (char)arrby[n3 - 3], (char)arrby[n3 - 2], (char)arrby[n3 - 1]);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public int encode(byte[] arrby, int n, int n2, OutputStream outputStream) {
        int n3;
        int n4 = n2 % 3;
        int n5 = n2 - n4;
        for (int i = n; i < n + n5; i += 3) {
            int n6 = 255 & arrby[i];
            int n7 = 255 & arrby[i + 1];
            int n8 = 255 & arrby[i + 2];
            outputStream.write((int)this.encodingTable[63 & n6 >>> 2]);
            outputStream.write((int)this.encodingTable[63 & (n6 << 4 | n7 >>> 4)]);
            outputStream.write((int)this.encodingTable[63 & (n7 << 2 | n8 >>> 6)]);
            outputStream.write((int)this.encodingTable[n8 & 63]);
        }
        switch (n4) {
            case 1: {
                int n9 = 255 & arrby[n + n5];
                int n10 = 63 & n9 >>> 2;
                int n11 = 63 & n9 << 4;
                outputStream.write((int)this.encodingTable[n10]);
                outputStream.write((int)this.encodingTable[n11]);
                outputStream.write((int)this.padding);
                outputStream.write((int)this.padding);
                break;
            }
            case 2: {
                int n12 = 255 & arrby[n + n5];
                int n13 = 255 & arrby[1 + (n + n5)];
                int n14 = 63 & n12 >>> 2;
                int n15 = 63 & (n12 << 4 | n13 >>> 4);
                int n16 = 63 & n13 << 2;
                outputStream.write((int)this.encodingTable[n14]);
                outputStream.write((int)this.encodingTable[n15]);
                outputStream.write((int)this.encodingTable[n16]);
                outputStream.write((int)this.padding);
            }
        }
        int n17 = 4 * (n5 / 3);
        if (n4 == 0) {
            n3 = 0;
            return n3 + n17;
        }
        n3 = 4;
        return n3 + n17;
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
    }
}

