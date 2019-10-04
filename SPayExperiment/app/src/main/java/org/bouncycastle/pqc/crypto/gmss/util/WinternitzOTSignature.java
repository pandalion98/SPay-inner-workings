/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Byte
 *  java.lang.Class
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.System
 *  java.lang.reflect.Array
 */
package org.bouncycastle.pqc.crypto.gmss.util;

import java.lang.reflect.Array;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.pqc.crypto.gmss.util.GMSSRandom;

public class WinternitzOTSignature {
    private int checksumsize;
    private GMSSRandom gmssRandom;
    private int keysize;
    private int mdsize;
    private Digest messDigestOTS;
    private int messagesize;
    private byte[][] privateKeyOTS;
    private int w;

    public WinternitzOTSignature(byte[] arrby, Digest digest, int n) {
        this.w = n;
        this.messDigestOTS = digest;
        this.gmssRandom = new GMSSRandom(this.messDigestOTS);
        this.mdsize = this.messDigestOTS.getDigestSize();
        this.messagesize = (int)Math.ceil((double)((double)(this.mdsize << 3) / (double)n));
        this.checksumsize = this.getLog(1 + (this.messagesize << n));
        this.keysize = this.messagesize + (int)Math.ceil((double)((double)this.checksumsize / (double)n));
        int[] arrn = new int[]{this.keysize, this.mdsize};
        this.privateKeyOTS = (byte[][])Array.newInstance((Class)Byte.TYPE, (int[])arrn);
        byte[] arrby2 = new byte[this.mdsize];
        System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)0, (int)arrby2.length);
        for (int i = 0; i < this.keysize; ++i) {
            this.privateKeyOTS[i] = this.gmssRandom.nextSeed(arrby2);
        }
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

    public byte[][] getPrivateKey() {
        return this.privateKeyOTS;
    }

    public byte[] getPublicKey() {
        byte[] arrby = new byte[this.keysize * this.mdsize];
        new byte[this.mdsize];
        int n = 1 << this.w;
        for (int i = 0; i < this.keysize; ++i) {
            this.messDigestOTS.update(this.privateKeyOTS[i], 0, this.privateKeyOTS[i].length);
            byte[] arrby2 = new byte[this.messDigestOTS.getDigestSize()];
            this.messDigestOTS.doFinal(arrby2, 0);
            for (int j = 2; j < n; ++j) {
                this.messDigestOTS.update(arrby2, 0, arrby2.length);
                arrby2 = new byte[this.messDigestOTS.getDigestSize()];
                this.messDigestOTS.doFinal(arrby2, 0);
            }
            System.arraycopy((Object)arrby2, (int)0, (Object)arrby, (int)(i * this.mdsize), (int)this.mdsize);
        }
        this.messDigestOTS.update(arrby, 0, arrby.length);
        byte[] arrby3 = new byte[this.messDigestOTS.getDigestSize()];
        this.messDigestOTS.doFinal(arrby3, 0);
        return arrby3;
    }

    /*
     * Enabled aggressive block sorting
     */
    public byte[] getSignature(byte[] arrby) {
        int n;
        byte[] arrby2;
        int n2;
        int n3;
        byte[] arrby3;
        byte[] arrby4;
        int n4;
        int n5;
        int n6;
        byte[] arrby5;
        block30 : {
            int n7;
            int n8;
            int n9;
            int n10;
            byte[] arrby6;
            int n11;
            int n12;
            byte[] arrby7;
            int n13;
            int n14;
            block29 : {
                int n15;
                int n16;
                block27 : {
                    block28 : {
                        arrby5 = new byte[this.keysize * this.mdsize];
                        new byte[this.mdsize];
                        n15 = 0;
                        n16 = 0;
                        this.messDigestOTS.update(arrby, 0, arrby.length);
                        arrby2 = new byte[this.messDigestOTS.getDigestSize()];
                        this.messDigestOTS.doFinal(arrby2, 0);
                        if (8 % this.w == 0) break block27;
                        if (this.w >= 8) break block28;
                        n14 = this.mdsize / this.w;
                        int n17 = -1 + (1 << this.w);
                        arrby6 = new byte[this.mdsize];
                        n7 = 0;
                        n9 = 0;
                        n11 = 0;
                        n8 = 0;
                        break block29;
                    }
                    if (this.w >= 57) return arrby5;
                    n5 = (this.mdsize << 3) - this.w;
                    int n18 = -1 + (1 << this.w);
                    arrby3 = new byte[this.mdsize];
                    n2 = 0;
                    n3 = 0;
                    n6 = 0;
                    break block30;
                }
                int n19 = 8 / this.w;
                int n20 = -1 + (1 << this.w);
                byte[] arrby8 = new byte[this.mdsize];
                int n21 = 0;
                do {
                    if (n21 < arrby2.length) {
                    } else {
                        int n22 = (this.messagesize << this.w) - n16;
                        break;
                    }
                    for (int i = 0; i < n19; ++n15, ++i) {
                        int n23 = n20 & arrby2[n21];
                        int n24 = n16 + n23;
                        System.arraycopy((Object)this.privateKeyOTS[n15], (int)0, (Object)arrby8, (int)0, (int)this.mdsize);
                        for (int j = n23; j > 0; --j) {
                            this.messDigestOTS.update(arrby8, 0, arrby8.length);
                            arrby8 = new byte[this.messDigestOTS.getDigestSize()];
                            this.messDigestOTS.doFinal(arrby8, 0);
                        }
                        System.arraycopy((Object)arrby8, (int)0, (Object)arrby5, (int)(n15 * this.mdsize), (int)this.mdsize);
                        arrby2[n21] = (byte)(arrby2[n21] >>> this.w);
                        n16 = n24;
                    }
                    ++n21;
                } while (true);
                for (int i = 0; i < this.checksumsize; n22 >>>= this.w, ++n15, i += this.w) {
                    System.arraycopy((Object)this.privateKeyOTS[n15], (int)0, (Object)arrby8, (int)0, (int)this.mdsize);
                    for (int j = n22 & n20; j > 0; --j) {
                        this.messDigestOTS.update(arrby8, 0, arrby8.length);
                        arrby8 = new byte[this.messDigestOTS.getDigestSize()];
                        this.messDigestOTS.doFinal(arrby8, 0);
                    }
                    System.arraycopy((Object)arrby8, (int)0, (Object)arrby5, (int)(n15 * this.mdsize), (int)this.mdsize);
                }
                return arrby5;
            }
            do {
                byte[] arrby9;
                int n25;
                int n26;
                if (n9 < n14) {
                    long l = 0L;
                    for (int i = 0; i < this.w; l ^= (long)((255 & arrby2[n7]) << (i << 3)), ++n7, ++i) {
                    }
                    n26 = n8;
                    n25 = n11;
                    byte[] arrby10 = arrby6;
                    long l2 = l;
                    arrby9 = arrby10;
                } else {
                    int n27 = this.mdsize % this.w;
                    long l = 0L;
                    for (int i = 0; i < n27; l ^= (long)((255 & arrby2[n7]) << (i << 3)), ++n7, ++i) {
                    }
                    n10 = n27 << 3;
                    arrby7 = arrby6;
                    long l3 = l;
                    n12 = n8;
                    n13 = n11;
                    long l4 = l3;
                    break;
                }
                for (int i = 0; i < 8; l2 >>>= this.w, ++n26, ++i) {
                    int n28;
                    n25 += n28;
                    System.arraycopy((Object)this.privateKeyOTS[n26], (int)0, (Object)arrby9, (int)0, (int)this.mdsize);
                    for (n28 = (int)(l2 & (long)n17); n28 > 0; --n28) {
                        this.messDigestOTS.update(arrby9, 0, arrby9.length);
                        arrby9 = new byte[this.messDigestOTS.getDigestSize()];
                        this.messDigestOTS.doFinal(arrby9, 0);
                    }
                    System.arraycopy((Object)arrby9, (int)0, (Object)arrby5, (int)(n26 * this.mdsize), (int)this.mdsize);
                }
                ++n9;
                arrby6 = arrby9;
                n11 = n25;
                n8 = n26;
            } while (true);
            for (int i = 0; i < n10; l4 >>>= this.w, ++n12, i += this.w) {
                int n29;
                n13 += n29;
                System.arraycopy((Object)this.privateKeyOTS[n12], (int)0, (Object)arrby7, (int)0, (int)this.mdsize);
                for (n29 = (int)(l4 & (long)n17); n29 > 0; --n29) {
                    this.messDigestOTS.update(arrby7, 0, arrby7.length);
                    arrby7 = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(arrby7, 0);
                }
                System.arraycopy((Object)arrby7, (int)0, (Object)arrby5, (int)(n12 * this.mdsize), (int)this.mdsize);
            }
            int n30 = (this.messagesize << this.w) - n13;
            for (int i = 0; i < this.checksumsize; n30 >>>= this.w, ++n12, i += this.w) {
                System.arraycopy((Object)this.privateKeyOTS[n12], (int)0, (Object)arrby7, (int)0, (int)this.mdsize);
                for (int j = n30 & n17; j > 0; --j) {
                    this.messDigestOTS.update(arrby7, 0, arrby7.length);
                    arrby7 = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(arrby7, 0);
                }
                System.arraycopy((Object)arrby7, (int)0, (Object)arrby5, (int)(n12 * this.mdsize), (int)this.mdsize);
            }
            return arrby5;
        }
        while (n2 <= n5) {
            long l;
            int n31 = n2 % 8;
            int n32 = n2 + this.w;
            int n33 = n32 + 7 >>> 3;
            long l5 = 0L;
            int n34 = 0;
            for (int i = n2 >>> 3; i < n33; l5 ^= (long)((255 & arrby2[i]) << (n34 << 3)), ++n34, ++i) {
            }
            n3 = (int)(l + (long)n3);
            System.arraycopy((Object)this.privateKeyOTS[n6], (int)0, (Object)arrby3, (int)0, (int)this.mdsize);
            for (l = l5 >>> n31 & (long)n18; l > 0L; --l) {
                this.messDigestOTS.update(arrby3, 0, arrby3.length);
                arrby3 = new byte[this.messDigestOTS.getDigestSize()];
                this.messDigestOTS.doFinal(arrby3, 0);
            }
            System.arraycopy((Object)arrby3, (int)0, (Object)arrby5, (int)(n6 * this.mdsize), (int)this.mdsize);
            ++n6;
            n2 = n32;
        }
        int n35 = n2 >>> 3;
        if (n35 < this.mdsize) {
            long l;
            int n36 = n2 % 8;
            long l6 = 0L;
            int n37 = 0;
            while (n35 < this.mdsize) {
                l6 ^= (long)((255 & arrby2[n35]) << (n37 << 3));
                ++n37;
                ++n35;
            }
            n = (int)(l + (long)n3);
            System.arraycopy((Object)this.privateKeyOTS[n6], (int)0, (Object)arrby3, (int)0, (int)this.mdsize);
            arrby4 = arrby3;
            for (l = l6 >>> n36 & (long)n18; l > 0L; --l) {
                this.messDigestOTS.update(arrby4, 0, arrby4.length);
                arrby4 = new byte[this.messDigestOTS.getDigestSize()];
                this.messDigestOTS.doFinal(arrby4, 0);
            }
            System.arraycopy((Object)arrby4, (int)0, (Object)arrby5, (int)(n6 * this.mdsize), (int)this.mdsize);
            n4 = n6 + 1;
        } else {
            arrby4 = arrby3;
            n = n3;
            n4 = n6;
        }
        int n38 = (this.messagesize << this.w) - n;
        byte[] arrby11 = arrby4;
        int n39 = n4;
        int n40 = n38;
        for (int i = 0; i < this.checksumsize; n40 >>>= this.w, ++n39, i += this.w) {
            System.arraycopy((Object)this.privateKeyOTS[n39], (int)0, (Object)arrby11, (int)0, (int)this.mdsize);
            for (long j = (long)(n40 & n18); j > 0L; --j) {
                this.messDigestOTS.update(arrby11, 0, arrby11.length);
                arrby11 = new byte[this.messDigestOTS.getDigestSize()];
                this.messDigestOTS.doFinal(arrby11, 0);
            }
            System.arraycopy((Object)arrby11, (int)0, (Object)arrby5, (int)(n39 * this.mdsize), (int)this.mdsize);
        }
        return arrby5;
    }
}

