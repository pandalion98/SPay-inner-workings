/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.System
 *  org.bouncycastle.util.Pack
 */
package org.bouncycastle.crypto.digests;

import org.bouncycastle.crypto.ExtendedDigest;
import org.bouncycastle.crypto.digests.EncodableDigest;
import org.bouncycastle.util.Memoable;
import org.bouncycastle.util.Pack;

public abstract class LongDigest
implements ExtendedDigest,
EncodableDigest,
Memoable {
    private static final int BYTE_LENGTH = 128;
    static final long[] K = new long[]{4794697086780616226L, 8158064640168781261L, -5349999486874862801L, -1606136188198331460L, 4131703408338449720L, 6480981068601479193L, -7908458776815382629L, -6116909921290321640L, -2880145864133508542L, 1334009975649890238L, 2608012711638119052L, 6128411473006802146L, 8268148722764581231L, -9160688886553864527L, -7215885187991268811L, -4495734319001033068L, -1973867731355612462L, -1171420211273849373L, 1135362057144423861L, 2597628984639134821L, 3308224258029322869L, 5365058923640841347L, 6679025012923562964L, 8573033837759648693L, -7476448914759557205L, -6327057829258317296L, -5763719355590565569L, -4658551843659510044L, -4116276920077217854L, -3051310485924567259L, 489312712824947311L, 1452737877330783856L, 2861767655752347644L, 3322285676063803686L, 5560940570517711597L, 5996557281743188959L, 7280758554555802590L, 8532644243296465576L, -9096487096722542874L, -7894198246740708037L, -6719396339535248540L, -6333637450476146687L, -4446306890439682159L, -4076793802049405392L, -3345356375505022440L, -2983346525034927856L, -860691631967231958L, 1182934255886127544L, 1847814050463011016L, 2177327727835720531L, 2830643537854262169L, 3796741975233480872L, 4115178125766777443L, 5681478168544905931L, 6601373596472566643L, 7507060721942968483L, 8399075790359081724L, 8693463985226723168L, -8878714635349349518L, -8302665154208450068L, -8016688836872298968L, -6606660893046293015L, -4685533653050689259L, -4147400797238176981L, -3880063495543823972L, -3348786107499101689L, -1523767162380948706L, -757361751448694408L, 500013540394364858L, 748580250866718886L, 1242879168328830382L, 1977374033974150939L, 2944078676154940804L, 3659926193048069267L, 4368137639120453308L, 4836135668995329356L, 5532061633213252278L, 6448918945643986474L, 6902733635092675308L, 7801388544844847127L};
    protected long H1;
    protected long H2;
    protected long H3;
    protected long H4;
    protected long H5;
    protected long H6;
    protected long H7;
    protected long H8;
    private long[] W = new long[80];
    private long byteCount1;
    private long byteCount2;
    private int wOff;
    private byte[] xBuf = new byte[8];
    private int xBufOff;

    protected LongDigest() {
        this.xBufOff = 0;
        this.reset();
    }

    protected LongDigest(LongDigest longDigest) {
        this.copyIn(longDigest);
    }

    private long Ch(long l2, long l3, long l4) {
        return l2 & l3 ^ l4 & (-1L ^ l2);
    }

    private long Maj(long l2, long l3, long l4) {
        return l2 & l3 ^ l2 & l4 ^ l3 & l4;
    }

    private long Sigma0(long l2) {
        return (l2 << 63 | l2 >>> 1) ^ (l2 << 56 | l2 >>> 8) ^ l2 >>> 7;
    }

    private long Sigma1(long l2) {
        return (l2 << 45 | l2 >>> 19) ^ (l2 << 3 | l2 >>> 61) ^ l2 >>> 6;
    }

    private long Sum0(long l2) {
        return (l2 << 36 | l2 >>> 28) ^ (l2 << 30 | l2 >>> 34) ^ (l2 << 25 | l2 >>> 39);
    }

    private long Sum1(long l2) {
        return (l2 << 50 | l2 >>> 14) ^ (l2 << 46 | l2 >>> 18) ^ (l2 << 23 | l2 >>> 41);
    }

    private void adjustByteCounts() {
        if (this.byteCount1 > 0x1FFFFFFFFFFFFFFFL) {
            this.byteCount2 += this.byteCount1 >>> 61;
            this.byteCount1 = 0x1FFFFFFFFFFFFFFFL & this.byteCount1;
        }
    }

    protected void copyIn(LongDigest longDigest) {
        System.arraycopy((Object)longDigest.xBuf, (int)0, (Object)this.xBuf, (int)0, (int)longDigest.xBuf.length);
        this.xBufOff = longDigest.xBufOff;
        this.byteCount1 = longDigest.byteCount1;
        this.byteCount2 = longDigest.byteCount2;
        this.H1 = longDigest.H1;
        this.H2 = longDigest.H2;
        this.H3 = longDigest.H3;
        this.H4 = longDigest.H4;
        this.H5 = longDigest.H5;
        this.H6 = longDigest.H6;
        this.H7 = longDigest.H7;
        this.H8 = longDigest.H8;
        System.arraycopy((Object)longDigest.W, (int)0, (Object)this.W, (int)0, (int)longDigest.W.length);
        this.wOff = longDigest.wOff;
    }

    public void finish() {
        this.adjustByteCounts();
        long l2 = this.byteCount1 << 3;
        long l3 = this.byteCount2;
        this.update((byte)-128);
        while (this.xBufOff != 0) {
            this.update((byte)0);
        }
        this.processLength(l2, l3);
        this.processBlock();
    }

    @Override
    public int getByteLength() {
        return 128;
    }

    protected int getEncodedStateSize() {
        return 96 + 8 * this.wOff;
    }

    protected void populateState(byte[] arrby) {
        System.arraycopy((Object)this.xBuf, (int)0, (Object)arrby, (int)0, (int)this.xBufOff);
        Pack.intToBigEndian((int)this.xBufOff, (byte[])arrby, (int)8);
        Pack.longToBigEndian((long)this.byteCount1, (byte[])arrby, (int)12);
        Pack.longToBigEndian((long)this.byteCount2, (byte[])arrby, (int)20);
        Pack.longToBigEndian((long)this.H1, (byte[])arrby, (int)28);
        Pack.longToBigEndian((long)this.H2, (byte[])arrby, (int)36);
        Pack.longToBigEndian((long)this.H3, (byte[])arrby, (int)44);
        Pack.longToBigEndian((long)this.H4, (byte[])arrby, (int)52);
        Pack.longToBigEndian((long)this.H5, (byte[])arrby, (int)60);
        Pack.longToBigEndian((long)this.H6, (byte[])arrby, (int)68);
        Pack.longToBigEndian((long)this.H7, (byte[])arrby, (int)76);
        Pack.longToBigEndian((long)this.H8, (byte[])arrby, (int)84);
        Pack.intToBigEndian((int)this.wOff, (byte[])arrby, (int)92);
        for (int i2 = 0; i2 < this.wOff; ++i2) {
            Pack.longToBigEndian((long)this.W[i2], (byte[])arrby, (int)(96 + i2 * 8));
        }
    }

    protected void processBlock() {
        this.adjustByteCounts();
        for (int i2 = 16; i2 <= 79; ++i2) {
            this.W[i2] = this.Sigma1(this.W[i2 - 2]) + this.W[i2 - 7] + this.Sigma0(this.W[i2 - 15]) + this.W[i2 - 16];
        }
        long l2 = this.H1;
        long l3 = this.H2;
        long l4 = this.H3;
        long l5 = this.H4;
        long l6 = this.H5;
        long l7 = this.H6;
        long l8 = this.H7;
        long l9 = this.H8;
        long l10 = l9;
        long l11 = l2;
        int n2 = 0;
        for (int i3 = 0; i3 < 10; ++i3) {
            long l12 = this.Sum1(l6) + this.Ch(l6, l7, l8) + K[n2];
            long[] arrl = this.W;
            int n3 = n2 + 1;
            long l13 = l10 + (l12 + arrl[n2]);
            long l14 = l5 + l13;
            long l15 = l13 + (this.Sum0(l11) + this.Maj(l11, l3, l4));
            long l16 = this.Sum1(l14) + this.Ch(l14, l6, l7) + K[n3];
            long[] arrl2 = this.W;
            int n4 = n3 + 1;
            long l17 = l8 + (l16 + arrl2[n3]);
            long l18 = l4 + l17;
            long l19 = l17 + (this.Sum0(l15) + this.Maj(l15, l11, l3));
            long l20 = this.Sum1(l18) + this.Ch(l18, l14, l6) + K[n4];
            long[] arrl3 = this.W;
            int n5 = n4 + 1;
            long l21 = l7 + (l20 + arrl3[n4]);
            long l22 = l3 + l21;
            long l23 = l21 + (this.Sum0(l19) + this.Maj(l19, l15, l11));
            long l24 = this.Sum1(l22) + this.Ch(l22, l18, l14) + K[n5];
            long[] arrl4 = this.W;
            int n6 = n5 + 1;
            long l25 = l6 + (l24 + arrl4[n5]);
            long l26 = l11 + l25;
            long l27 = l25 + (this.Sum0(l23) + this.Maj(l23, l19, l15));
            long l28 = this.Sum1(l26) + this.Ch(l26, l22, l18) + K[n6];
            long[] arrl5 = this.W;
            int n7 = n6 + 1;
            long l29 = l14 + (l28 + arrl5[n6]);
            long l30 = l15 + l29;
            l5 = l29 + (this.Sum0(l27) + this.Maj(l27, l23, l19));
            long l31 = this.Sum1(l30) + this.Ch(l30, l26, l22) + K[n7];
            long[] arrl6 = this.W;
            int n8 = n7 + 1;
            long l32 = l18 + (l31 + arrl6[n7]);
            long l33 = l19 + l32;
            long l34 = l32 + (this.Sum0(l5) + this.Maj(l5, l27, l23));
            long l35 = this.Sum1(l33) + this.Ch(l33, l30, l26) + K[n8];
            long[] arrl7 = this.W;
            int n9 = n8 + 1;
            long l36 = l22 + (l35 + arrl7[n8]);
            long l37 = l23 + l36;
            long l38 = l36 + (this.Sum0(l34) + this.Maj(l34, l5, l27));
            long l39 = this.Sum1(l37) + this.Ch(l37, l33, l30) + K[n9];
            long[] arrl8 = this.W;
            int n10 = n9 + 1;
            long l40 = l26 + (l39 + arrl8[n9]);
            l6 = l27 + l40;
            long l41 = l40 + (this.Sum0(l38) + this.Maj(l38, l34, l5));
            l3 = l38;
            l4 = l34;
            l10 = l30;
            l11 = l41;
            l7 = l37;
            l8 = l33;
            n2 = n10;
        }
        this.H1 = l11 + this.H1;
        this.H2 = l3 + this.H2;
        this.H3 = l4 + this.H3;
        this.H4 = l5 + this.H4;
        this.H5 = l6 + this.H5;
        this.H6 = l7 + this.H6;
        this.H7 = l8 + this.H7;
        this.H8 = l10 + this.H8;
        this.wOff = 0;
        for (int i4 = 0; i4 < 16; ++i4) {
            this.W[i4] = 0L;
        }
    }

    protected void processLength(long l2, long l3) {
        if (this.wOff > 14) {
            this.processBlock();
        }
        this.W[14] = l3;
        this.W[15] = l2;
    }

    protected void processWord(byte[] arrby, int n2) {
        int n3;
        this.W[this.wOff] = Pack.bigEndianToLong((byte[])arrby, (int)n2);
        this.wOff = n3 = 1 + this.wOff;
        if (n3 == 16) {
            this.processBlock();
        }
    }

    @Override
    public void reset() {
        int n2 = 0;
        this.byteCount1 = 0L;
        this.byteCount2 = 0L;
        this.xBufOff = 0;
        for (int i2 = 0; i2 < this.xBuf.length; ++i2) {
            this.xBuf[i2] = 0;
        }
        this.wOff = 0;
        while (n2 != this.W.length) {
            this.W[n2] = 0L;
            ++n2;
        }
    }

    protected void restoreState(byte[] arrby) {
        this.xBufOff = Pack.bigEndianToInt((byte[])arrby, (int)8);
        System.arraycopy((Object)arrby, (int)0, (Object)this.xBuf, (int)0, (int)this.xBufOff);
        this.byteCount1 = Pack.bigEndianToLong((byte[])arrby, (int)12);
        this.byteCount2 = Pack.bigEndianToLong((byte[])arrby, (int)20);
        this.H1 = Pack.bigEndianToLong((byte[])arrby, (int)28);
        this.H2 = Pack.bigEndianToLong((byte[])arrby, (int)36);
        this.H3 = Pack.bigEndianToLong((byte[])arrby, (int)44);
        this.H4 = Pack.bigEndianToLong((byte[])arrby, (int)52);
        this.H5 = Pack.bigEndianToLong((byte[])arrby, (int)60);
        this.H6 = Pack.bigEndianToLong((byte[])arrby, (int)68);
        this.H7 = Pack.bigEndianToLong((byte[])arrby, (int)76);
        this.H8 = Pack.bigEndianToLong((byte[])arrby, (int)84);
        this.wOff = Pack.bigEndianToInt((byte[])arrby, (int)92);
        for (int i2 = 0; i2 < this.wOff; ++i2) {
            this.W[i2] = Pack.bigEndianToLong((byte[])arrby, (int)(96 + i2 * 8));
        }
    }

    @Override
    public void update(byte by) {
        byte[] arrby = this.xBuf;
        int n2 = this.xBufOff;
        this.xBufOff = n2 + 1;
        arrby[n2] = by;
        if (this.xBufOff == this.xBuf.length) {
            this.processWord(this.xBuf, 0);
            this.xBufOff = 0;
        }
        this.byteCount1 = 1L + this.byteCount1;
    }

    @Override
    public void update(byte[] arrby, int n2, int n3) {
        while (this.xBufOff != 0 && n3 > 0) {
            this.update(arrby[n2]);
            ++n2;
            --n3;
        }
        while (n3 > this.xBuf.length) {
            this.processWord(arrby, n2);
            n2 += this.xBuf.length;
            n3 -= this.xBuf.length;
            this.byteCount1 += (long)this.xBuf.length;
        }
        while (n3 > 0) {
            this.update(arrby[n2]);
            ++n2;
            --n3;
        }
    }
}

