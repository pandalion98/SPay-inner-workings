/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Byte
 *  java.lang.Class
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.reflect.Array
 */
package org.bouncycastle.pqc.crypto.gmss;

import java.lang.reflect.Array;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.pqc.crypto.gmss.util.GMSSRandom;
import org.bouncycastle.util.encoders.Hex;

public class GMSSRootSig {
    private long big8;
    private int checksum;
    private int counter;
    private GMSSRandom gmssRandom;
    private byte[] hash;
    private int height;
    private int ii;
    private int k;
    private int keysize;
    private int mdsize;
    private Digest messDigestOTS;
    private int messagesize;
    private byte[] privateKeyOTS;
    private int r;
    private byte[] seed;
    private byte[] sign;
    private int steps;
    private int test;
    private long test8;
    private int w;

    public GMSSRootSig(Digest digest, int n, int n2) {
        this.messDigestOTS = digest;
        this.gmssRandom = new GMSSRandom(this.messDigestOTS);
        this.mdsize = this.messDigestOTS.getDigestSize();
        this.w = n;
        this.height = n2;
        this.k = -1 + (1 << n);
        this.messagesize = (int)Math.ceil((double)((double)(this.mdsize << 3) / (double)n));
    }

    public GMSSRootSig(Digest digest, byte[][] arrby, int[] arrn) {
        this.messDigestOTS = digest;
        this.gmssRandom = new GMSSRandom(this.messDigestOTS);
        this.counter = arrn[0];
        this.test = arrn[1];
        this.ii = arrn[2];
        this.r = arrn[3];
        this.steps = arrn[4];
        this.keysize = arrn[5];
        this.height = arrn[6];
        this.w = arrn[7];
        this.checksum = arrn[8];
        this.mdsize = this.messDigestOTS.getDigestSize();
        this.k = -1 + (1 << this.w);
        this.messagesize = (int)Math.ceil((double)((double)(this.mdsize << 3) / (double)this.w));
        this.privateKeyOTS = arrby[0];
        this.seed = arrby[1];
        this.hash = arrby[2];
        this.sign = arrby[3];
        this.test8 = (long)(255 & arrby[4][0]) | (long)(255 & arrby[4][1]) << 8 | (long)(255 & arrby[4][2]) << 16 | (long)(255 & arrby[4][3]) << 24 | (long)(255 & arrby[4][4]) << 32 | (long)(255 & arrby[4][5]) << 40 | (long)(255 & arrby[4][6]) << 48 | (long)(255 & arrby[4][7]) << 56;
        this.big8 = (long)(255 & arrby[4][8]) | (long)(255 & arrby[4][9]) << 8 | (long)(255 & arrby[4][10]) << 16 | (long)(255 & arrby[4][11]) << 24 | (long)(255 & arrby[4][12]) << 32 | (long)(255 & arrby[4][13]) << 40 | (long)(255 & arrby[4][14]) << 48 | (long)(255 & arrby[4][15]) << 56;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void oneStep() {
        if (8 % this.w == 0) {
            if (this.test == 0) {
                this.privateKeyOTS = this.gmssRandom.nextSeed(this.seed);
                if (this.ii < this.mdsize) {
                    this.test = this.hash[this.ii] & this.k;
                    this.hash[this.ii] = (byte)(this.hash[this.ii] >>> this.w);
                } else {
                    this.test = this.checksum & this.k;
                    this.checksum >>>= this.w;
                }
            } else if (this.test > 0) {
                this.messDigestOTS.update(this.privateKeyOTS, 0, this.privateKeyOTS.length);
                this.privateKeyOTS = new byte[this.messDigestOTS.getDigestSize()];
                this.messDigestOTS.doFinal(this.privateKeyOTS, 0);
                this.test = -1 + this.test;
            }
            if (this.test != 0) return;
            {
                System.arraycopy((Object)this.privateKeyOTS, (int)0, (Object)this.sign, (int)(this.counter * this.mdsize), (int)this.mdsize);
                this.counter = 1 + this.counter;
                if (this.counter % (8 / this.w) != 0) return;
                {
                    this.ii = 1 + this.ii;
                    return;
                }
            }
        }
        if (this.w < 8) {
            if (this.test == 0) {
                if (this.counter % 8 == 0 && this.ii < this.mdsize) {
                    this.big8 = 0L;
                    if (this.counter < this.mdsize / this.w << 3) {
                        for (int i = 0; i < this.w; this.big8 ^= (long)((255 & this.hash[this.ii]) << (i << 3)), this.ii = 1 + this.ii, ++i) {
                        }
                    } else {
                        for (int i = 0; i < this.mdsize % this.w; this.big8 ^= (long)((255 & this.hash[this.ii]) << (i << 3)), this.ii = 1 + this.ii, ++i) {
                        }
                    }
                }
                if (this.counter == this.messagesize) {
                    this.big8 = this.checksum;
                }
                this.test = (int)(this.big8 & (long)this.k);
                this.privateKeyOTS = this.gmssRandom.nextSeed(this.seed);
            } else if (this.test > 0) {
                this.messDigestOTS.update(this.privateKeyOTS, 0, this.privateKeyOTS.length);
                this.privateKeyOTS = new byte[this.messDigestOTS.getDigestSize()];
                this.messDigestOTS.doFinal(this.privateKeyOTS, 0);
                this.test = -1 + this.test;
            }
            if (this.test != 0) return;
            {
                System.arraycopy((Object)this.privateKeyOTS, (int)0, (Object)this.sign, (int)(this.counter * this.mdsize), (int)this.mdsize);
                this.big8 >>>= this.w;
                this.counter = 1 + this.counter;
                return;
            }
        }
        if (this.w >= 57) return;
        {
            if (this.test8 == 0L) {
                this.big8 = 0L;
                this.ii = 0;
                int n = this.r % 8;
                int n2 = this.r >>> 3;
                if (n2 < this.mdsize) {
                    int n3;
                    if (this.r <= (this.mdsize << 3) - this.w) {
                        this.r += this.w;
                        n3 = 7 + this.r >>> 3;
                    } else {
                        n3 = this.mdsize;
                        this.r += this.w;
                    }
                    while (n2 < n3) {
                        this.big8 ^= (long)((255 & this.hash[n2]) << (this.ii << 3));
                        this.ii = 1 + this.ii;
                        ++n2;
                    }
                    this.big8 >>>= n;
                    this.test8 = this.big8 & (long)this.k;
                } else {
                    this.test8 = this.checksum & this.k;
                    this.checksum >>>= this.w;
                }
                this.privateKeyOTS = this.gmssRandom.nextSeed(this.seed);
            } else if (this.test8 > 0L) {
                this.messDigestOTS.update(this.privateKeyOTS, 0, this.privateKeyOTS.length);
                this.privateKeyOTS = new byte[this.messDigestOTS.getDigestSize()];
                this.messDigestOTS.doFinal(this.privateKeyOTS, 0);
                --this.test8;
            }
            if (this.test8 != 0L) return;
            {
                System.arraycopy((Object)this.privateKeyOTS, (int)0, (Object)this.sign, (int)(this.counter * this.mdsize), (int)this.mdsize);
                this.counter = 1 + this.counter;
                return;
            }
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

    public byte[] getSig() {
        return this.sign;
    }

    public byte[][] getStatByte() {
        int[] arrn = new int[]{5, this.mdsize};
        byte[][] arrby = (byte[][])Array.newInstance((Class)Byte.TYPE, (int[])arrn);
        arrby[0] = this.privateKeyOTS;
        arrby[1] = this.seed;
        arrby[2] = this.hash;
        arrby[3] = this.sign;
        arrby[4] = this.getStatLong();
        return arrby;
    }

    public int[] getStatInt() {
        int[] arrn = new int[]{this.counter, this.test, this.ii, this.r, this.steps, this.keysize, this.height, this.w, this.checksum};
        return arrn;
    }

    public byte[] getStatLong() {
        byte[] arrby = new byte[]{(byte)(255L & this.test8), (byte)(255L & this.test8 >> 8), (byte)(255L & this.test8 >> 16), (byte)(255L & this.test8 >> 24), (byte)(255L & this.test8 >> 32), (byte)(255L & this.test8 >> 40), (byte)(255L & this.test8 >> 48), (byte)(255L & this.test8 >> 56), (byte)(255L & this.big8), (byte)(255L & this.big8 >> 8), (byte)(255L & this.big8 >> 16), (byte)(255L & this.big8 >> 24), (byte)(255L & this.big8 >> 32), (byte)(255L & this.big8 >> 40), (byte)(255L & this.big8 >> 48), (byte)(255L & this.big8 >> 56)};
        return arrby;
    }

    public void initSign(byte[] arrby, byte[] arrby2) {
        this.hash = new byte[this.mdsize];
        this.messDigestOTS.update(arrby2, 0, arrby2.length);
        this.hash = new byte[this.messDigestOTS.getDigestSize()];
        this.messDigestOTS.doFinal(this.hash, 0);
        byte[] arrby3 = new byte[this.mdsize];
        System.arraycopy((Object)this.hash, (int)0, (Object)arrby3, (int)0, (int)this.mdsize);
        int n = 0;
        int n2 = this.getLog(1 + (this.messagesize << this.w));
        if (8 % this.w == 0) {
            int n3 = 8 / this.w;
            for (int i = 0; i < this.mdsize; ++i) {
                for (int j = 0; j < n3; ++j) {
                    int n4 = n + (arrby3[i] & this.k);
                    arrby3[i] = (byte)(arrby3[i] >>> this.w);
                    n = n4;
                }
            }
            int n5 = this.checksum = (this.messagesize << this.w) - n;
            for (int i = 0; i < n2; i += this.w) {
                n += n5 & this.k;
                n5 >>>= this.w;
            }
        } else if (this.w < 8) {
            int n6 = this.mdsize / this.w;
            int n7 = 0;
            int n8 = 0;
            for (int i = 0; i < n6; ++i) {
                long l = 0L;
                for (int j = 0; j < this.w; ++j) {
                    l ^= (long)((255 & arrby3[n8]) << (j << 3));
                    ++n8;
                }
                for (int j = 0; j < 8; ++j) {
                    n7 += (int)(l & (long)this.k);
                    l >>>= this.w;
                }
            }
            int n9 = this.mdsize % this.w;
            long l = 0L;
            for (int i = 0; i < n9; ++i) {
                l ^= (long)((255 & arrby3[n8]) << (i << 3));
                ++n8;
            }
            int n10 = n9 << 3;
            n = n7;
            for (int i = 0; i < n10; i += this.w) {
                n += (int)(l & (long)this.k);
                l >>>= this.w;
            }
            int n11 = this.checksum = (this.messagesize << this.w) - n;
            for (int i = 0; i < n2; i += this.w) {
                n += n11 & this.k;
                n11 >>>= this.w;
            }
        } else {
            int n12 = this.w;
            n = 0;
            if (n12 < 57) {
                int n13 = 0;
                while (n13 <= (this.mdsize << 3) - this.w) {
                    int n14 = n13 >>> 3;
                    int n15 = n13 % 8;
                    int n16 = (n13 += this.w) + 7 >>> 3;
                    long l = 0L;
                    int n17 = 0;
                    while (n14 < n16) {
                        l ^= (long)((255 & arrby3[n14]) << (n17 << 3));
                        ++n17;
                        ++n14;
                    }
                    long l2 = l >>> n15;
                    n = (int)((long)n + (l2 & (long)this.k));
                }
                int n18 = n13 >>> 3;
                if (n18 < this.mdsize) {
                    int n19 = n13 % 8;
                    long l = 0L;
                    int n20 = 0;
                    for (int i = n18; i < this.mdsize; ++i) {
                        l ^= (long)((255 & arrby3[i]) << (n20 << 3));
                        ++n20;
                    }
                    long l3 = l >>> n19;
                    n = (int)((long)n + (l3 & (long)this.k));
                }
                int n21 = this.checksum = (this.messagesize << this.w) - n;
                for (int i = 0; i < n2; i += this.w) {
                    n += n21 & this.k;
                    n21 >>>= this.w;
                }
            }
        }
        this.keysize = this.messagesize + (int)Math.ceil((double)((double)n2 / (double)this.w));
        this.steps = (int)Math.ceil((double)((double)(n + this.keysize) / (double)(1 << this.height)));
        this.sign = new byte[this.keysize * this.mdsize];
        this.counter = 0;
        this.test = 0;
        this.ii = 0;
        this.test8 = 0L;
        this.r = 0;
        this.privateKeyOTS = new byte[this.mdsize];
        this.seed = new byte[this.mdsize];
        System.arraycopy((Object)arrby, (int)0, (Object)this.seed, (int)0, (int)this.mdsize);
    }

    public String toString() {
        String string = "" + this.big8 + "  ";
        new int[9];
        int[] arrn = this.getStatInt();
        int[] arrn2 = new int[]{5, this.mdsize};
        (byte[][])Array.newInstance((Class)Byte.TYPE, (int[])arrn2);
        byte[][] arrby = this.getStatByte();
        String string2 = string;
        for (int i = 0; i < 9; ++i) {
            String string3 = string2 + arrn[i] + " ";
            string2 = string3;
        }
        String string4 = string2;
        for (int i = 0; i < 5; ++i) {
            string4 = string4 + new String(Hex.encode(arrby[i])) + " ";
        }
        return string4;
    }

    public boolean updateSign() {
        int n = 0;
        do {
            block6 : {
                boolean bl;
                block5 : {
                    int n2 = this.steps;
                    bl = false;
                    if (n >= n2) break block5;
                    if (this.counter < this.keysize) {
                        this.oneStep();
                    }
                    if (this.counter != this.keysize) break block6;
                    bl = true;
                }
                return bl;
            }
            ++n;
        } while (true);
    }
}

