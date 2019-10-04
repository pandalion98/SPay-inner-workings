/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.System
 */
package org.bouncycastle.pqc.crypto.gmss.util;

import org.bouncycastle.crypto.Digest;

public class WinternitzOTSVerify {
    private Digest messDigestOTS;
    private int w;

    public WinternitzOTSVerify(Digest digest, int n) {
        this.w = n;
        this.messDigestOTS = digest;
    }

    public byte[] Verify(byte[] arrby, byte[] arrby2) {
        int n = this.messDigestOTS.getDigestSize();
        new byte[n];
        this.messDigestOTS.update(arrby, 0, arrby.length);
        byte[] arrby3 = new byte[this.messDigestOTS.getDigestSize()];
        this.messDigestOTS.doFinal(arrby3, 0);
        int n2 = ((n << 3) + (-1 + this.w)) / this.w;
        int n3 = this.getLog(1 + (n2 << this.w));
        int n4 = n * (n2 + (-1 + (n3 + this.w)) / this.w);
        if (n4 != arrby2.length) {
            return null;
        }
        byte[] arrby4 = new byte[n4];
        int n5 = 0;
        int n6 = 0;
        if (8 % this.w == 0) {
            int n7 = 8 / this.w;
            int n8 = -1 + (1 << this.w);
            byte[] arrby5 = new byte[n];
            for (int i = 0; i < arrby3.length; ++i) {
                for (int j = 0; j < n7; ++j) {
                    int n9 = n8 & arrby3[i];
                    int n10 = n5 + n9;
                    System.arraycopy((Object)arrby2, (int)(n6 * n), (Object)arrby5, (int)0, (int)n);
                    for (int k = n9; k < n8; ++k) {
                        this.messDigestOTS.update(arrby5, 0, arrby5.length);
                        arrby5 = new byte[this.messDigestOTS.getDigestSize()];
                        this.messDigestOTS.doFinal(arrby5, 0);
                    }
                    System.arraycopy((Object)arrby5, (int)0, (Object)arrby4, (int)(n6 * n), (int)n);
                    arrby3[i] = (byte)(arrby3[i] >>> this.w);
                    int n11 = n6 + 1;
                    n6 = n11;
                    n5 = n10;
                }
            }
            int n12 = (n2 << this.w) - n5;
            int n13 = n6;
            byte[] arrby6 = arrby5;
            for (int i = 0; i < n3; i += this.w) {
                System.arraycopy((Object)arrby2, (int)(n13 * n), (Object)arrby6, (int)0, (int)n);
                for (int j = n12 & n8; j < n8; ++j) {
                    this.messDigestOTS.update(arrby6, 0, arrby6.length);
                    arrby6 = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(arrby6, 0);
                }
                System.arraycopy((Object)arrby6, (int)0, (Object)arrby4, (int)(n13 * n), (int)n);
                n12 >>>= this.w;
                ++n13;
            }
        } else if (this.w < 8) {
            int n14 = n / this.w;
            int n15 = -1 + (1 << this.w);
            byte[] arrby7 = new byte[n];
            int n16 = 0;
            int n17 = 0;
            int n18 = 0;
            for (int i = 0; i < n14; ++i) {
                long l = 0L;
                for (int j = 0; j < this.w; ++j) {
                    l ^= (long)((255 & arrby3[n16]) << (j << 3));
                    ++n16;
                }
                int n19 = n18;
                int n20 = n17;
                byte[] arrby8 = arrby7;
                long l2 = l;
                byte[] arrby9 = arrby8;
                for (int j = 0; j < 8; ++j) {
                    int n21;
                    n19 += n21;
                    System.arraycopy((Object)arrby2, (int)(n20 * n), (Object)arrby9, (int)0, (int)n);
                    for (n21 = (int)(l2 & (long)n15); n21 < n15; ++n21) {
                        this.messDigestOTS.update(arrby9, 0, arrby9.length);
                        arrby9 = new byte[this.messDigestOTS.getDigestSize()];
                        this.messDigestOTS.doFinal(arrby9, 0);
                    }
                    System.arraycopy((Object)arrby9, (int)0, (Object)arrby4, (int)(n20 * n), (int)n);
                    l2 >>>= this.w;
                    ++n20;
                }
                arrby7 = arrby9;
                n17 = n20;
                n18 = n19;
            }
            int n22 = n % this.w;
            long l = 0L;
            for (int i = 0; i < n22; ++i) {
                l ^= (long)((255 & arrby3[n16]) << (i << 3));
                ++n16;
            }
            int n23 = n22 << 3;
            byte[] arrby10 = arrby7;
            long l3 = l;
            int n24 = n17;
            long l4 = l3;
            for (int i = 0; i < n23; i += this.w) {
                int n25;
                n18 += n25;
                System.arraycopy((Object)arrby2, (int)(n24 * n), (Object)arrby10, (int)0, (int)n);
                for (n25 = (int)(l4 & (long)n15); n25 < n15; ++n25) {
                    this.messDigestOTS.update(arrby10, 0, arrby10.length);
                    arrby10 = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(arrby10, 0);
                }
                System.arraycopy((Object)arrby10, (int)0, (Object)arrby4, (int)(n24 * n), (int)n);
                l4 >>>= this.w;
                ++n24;
            }
            int n26 = (n2 << this.w) - n18;
            int n27 = n24;
            byte[] arrby11 = arrby10;
            for (int i = 0; i < n3; i += this.w) {
                System.arraycopy((Object)arrby2, (int)(n27 * n), (Object)arrby11, (int)0, (int)n);
                for (int j = n26 & n15; j < n15; ++j) {
                    this.messDigestOTS.update(arrby11, 0, arrby11.length);
                    arrby11 = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(arrby11, 0);
                }
                System.arraycopy((Object)arrby11, (int)0, (Object)arrby4, (int)(n27 * n), (int)n);
                n26 >>>= this.w;
                ++n27;
            }
        } else if (this.w < 57) {
            int n28 = (n << 3) - this.w;
            int n29 = -1 + (1 << this.w);
            byte[] arrby12 = new byte[n];
            int n30 = 0;
            byte[] arrby13 = arrby12;
            int n31 = 0;
            int n32 = 0;
            while (n30 <= n28) {
                int n33 = n30 % 8;
                int n34 = n30 + this.w;
                int n35 = n34 + 7 >>> 3;
                long l = 0L;
                int n36 = 0;
                for (int i = n30 >>> 3; i < n35; ++i) {
                    l ^= (long)((255 & arrby3[i]) << (n36 << 3));
                    ++n36;
                }
                long l5 = l >>> n33 & (long)n29;
                int n37 = (int)(l5 + (long)n32);
                System.arraycopy((Object)arrby2, (int)(n31 * n), (Object)arrby13, (int)0, (int)n);
                for (long i = l5; i < (long)n29; ++i) {
                    this.messDigestOTS.update(arrby13, 0, arrby13.length);
                    arrby13 = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(arrby13, 0);
                }
                System.arraycopy((Object)arrby13, (int)0, (Object)arrby4, (int)(n31 * n), (int)n);
                ++n31;
                n30 = n34;
                n32 = n37;
            }
            int n38 = n30 >>> 3;
            if (n38 < n) {
                long l;
                int n39 = n30 % 8;
                long l6 = 0L;
                int n40 = 0;
                while (n38 < n) {
                    l6 ^= (long)((255 & arrby3[n38]) << (n40 << 3));
                    ++n40;
                    ++n38;
                }
                n32 = (int)(l + (long)n32);
                System.arraycopy((Object)arrby2, (int)(n31 * n), (Object)arrby13, (int)0, (int)n);
                for (l = l6 >>> n39 & (long)n29; l < (long)n29; ++l) {
                    this.messDigestOTS.update(arrby13, 0, arrby13.length);
                    arrby13 = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(arrby13, 0);
                }
                System.arraycopy((Object)arrby13, (int)0, (Object)arrby4, (int)(n31 * n), (int)n);
                ++n31;
            }
            int n41 = (n2 << this.w) - n32;
            int n42 = n31;
            int n43 = n41;
            byte[] arrby14 = arrby13;
            for (int i = 0; i < n3; i += this.w) {
                System.arraycopy((Object)arrby2, (int)(n42 * n), (Object)arrby14, (int)0, (int)n);
                for (long j = (long)(n43 & n29); j < (long)n29; ++j) {
                    this.messDigestOTS.update(arrby14, 0, arrby14.length);
                    arrby14 = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(arrby14, 0);
                }
                System.arraycopy((Object)arrby14, (int)0, (Object)arrby4, (int)(n42 * n), (int)n);
                n43 >>>= this.w;
                int n44 = n42 + 1;
                n42 = n44;
            }
        }
        new byte[n];
        this.messDigestOTS.update(arrby4, 0, arrby4.length);
        byte[] arrby15 = new byte[this.messDigestOTS.getDigestSize()];
        this.messDigestOTS.doFinal(arrby15, 0);
        return arrby15;
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

    public int getSignatureLength() {
        int n = this.messDigestOTS.getDigestSize();
        int n2 = ((n << 3) + (-1 + this.w)) / this.w;
        return n * (n2 + (-1 + (this.getLog(1 + (n2 << this.w)) + this.w)) / this.w);
    }
}

