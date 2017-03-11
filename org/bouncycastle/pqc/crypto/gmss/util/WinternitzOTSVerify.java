package org.bouncycastle.pqc.crypto.gmss.util;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class WinternitzOTSVerify {
    private Digest messDigestOTS;
    private int f405w;

    public WinternitzOTSVerify(Digest digest, int i) {
        this.f405w = i;
        this.messDigestOTS = digest;
    }

    public byte[] Verify(byte[] bArr, byte[] bArr2) {
        int digestSize = this.messDigestOTS.getDigestSize();
        byte[] bArr3 = new byte[digestSize];
        this.messDigestOTS.update(bArr, 0, bArr.length);
        byte[] bArr4 = new byte[this.messDigestOTS.getDigestSize()];
        this.messDigestOTS.doFinal(bArr4, 0);
        int i = ((digestSize << 3) + (this.f405w - 1)) / this.f405w;
        int log = getLog((i << this.f405w) + 1);
        int i2 = ((((this.f405w + log) - 1) / this.f405w) + i) * digestSize;
        if (i2 != bArr2.length) {
            return null;
        }
        Object obj = new byte[i2];
        int i3 = 0;
        int i4 = 0;
        int i5;
        int i6;
        Object obj2;
        int i7;
        int i8;
        int i9;
        Object obj3;
        int i10;
        if (8 % this.f405w == 0) {
            i5 = 8 / this.f405w;
            i6 = (1 << this.f405w) - 1;
            obj2 = new byte[digestSize];
            for (i2 = 0; i2 < bArr4.length; i2++) {
                i7 = 0;
                while (i7 < i5) {
                    i8 = bArr4[i2] & i6;
                    i9 = i3 + i8;
                    System.arraycopy(bArr2, i4 * digestSize, obj2, 0, digestSize);
                    for (i3 = i8; i3 < i6; i3++) {
                        this.messDigestOTS.update(obj2, 0, obj2.length);
                        obj2 = new byte[this.messDigestOTS.getDigestSize()];
                        this.messDigestOTS.doFinal(obj2, 0);
                    }
                    System.arraycopy(obj2, 0, obj, i4 * digestSize, digestSize);
                    bArr4[i2] = (byte) (bArr4[i2] >>> this.f405w);
                    i7++;
                    i4++;
                    i3 = i9;
                }
            }
            i8 = (i << this.f405w) - i3;
            i3 = i4;
            i4 = 0;
            obj3 = obj2;
            while (i4 < log) {
                System.arraycopy(bArr2, i3 * digestSize, obj3, 0, digestSize);
                for (i10 = i8 & i6; i10 < i6; i10++) {
                    this.messDigestOTS.update(obj3, 0, obj3.length);
                    obj3 = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(obj3, 0);
                }
                System.arraycopy(obj3, 0, obj, i3 * digestSize, digestSize);
                i8 >>>= this.f405w;
                i3++;
                i4 = this.f405w + i4;
            }
        } else if (this.f405w < 8) {
            r18 = digestSize / this.f405w;
            r19 = (1 << this.f405w) - 1;
            Object obj4 = new byte[digestSize];
            i10 = 0;
            i6 = 0;
            i7 = 0;
            i9 = 0;
            while (i6 < r18) {
                r6 = 0;
                for (i2 = 0; i2 < this.f405w; i2++) {
                    r6 ^= (long) ((bArr4[i10] & GF2Field.MASK) << (i2 << 3));
                    i10++;
                }
                i5 = i9;
                i9 = i7;
                Object obj5 = obj4;
                r8 = r6;
                obj3 = obj5;
                for (i3 = 0; i3 < 8; i3++) {
                    i4 = (int) (((long) r19) & r8);
                    i5 += i4;
                    System.arraycopy(bArr2, i9 * digestSize, obj3, 0, digestSize);
                    while (i4 < r19) {
                        this.messDigestOTS.update(obj3, 0, obj3.length);
                        obj3 = new byte[this.messDigestOTS.getDigestSize()];
                        this.messDigestOTS.doFinal(obj3, 0);
                        i4++;
                    }
                    System.arraycopy(obj3, 0, obj, i9 * digestSize, digestSize);
                    r8 >>>= this.f405w;
                    i9++;
                }
                i6++;
                obj4 = obj3;
                i7 = i9;
                i9 = i5;
            }
            i5 = digestSize % this.f405w;
            r6 = 0;
            for (i2 = 0; i2 < i5; i2++) {
                r6 ^= (long) ((bArr4[i10] & GF2Field.MASK) << (i2 << 3));
                i10++;
            }
            i5 <<= 3;
            obj2 = obj4;
            long j = r6;
            i3 = 0;
            i4 = i7;
            r8 = j;
            while (i3 < i5) {
                i2 = (int) (((long) r19) & r8);
                i9 += i2;
                System.arraycopy(bArr2, i4 * digestSize, obj2, 0, digestSize);
                while (i2 < r19) {
                    this.messDigestOTS.update(obj2, 0, obj2.length);
                    obj2 = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(obj2, 0);
                    i2++;
                }
                System.arraycopy(obj2, 0, obj, i4 * digestSize, digestSize);
                r8 >>>= this.f405w;
                i4++;
                i3 = this.f405w + i3;
            }
            i8 = (i << this.f405w) - i9;
            i3 = i4;
            i4 = 0;
            obj3 = obj2;
            while (i4 < log) {
                System.arraycopy(bArr2, i3 * digestSize, obj3, 0, digestSize);
                for (i10 = i8 & r19; i10 < r19; i10++) {
                    this.messDigestOTS.update(obj3, 0, obj3.length);
                    obj3 = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(obj3, 0);
                }
                System.arraycopy(obj3, 0, obj, i3 * digestSize, digestSize);
                i8 >>>= this.f405w;
                i3++;
                i4 = this.f405w + i4;
            }
        } else if (this.f405w < 57) {
            long j2;
            r18 = (digestSize << 3) - this.f405w;
            r19 = (1 << this.f405w) - 1;
            i8 = 0;
            obj3 = new byte[digestSize];
            i10 = 0;
            i4 = 0;
            while (i8 <= r18) {
                i7 = i8 % 8;
                i6 = i8 + this.f405w;
                int i11 = (i6 + 7) >>> 3;
                j2 = 0;
                i8 = 0;
                for (i3 = i8 >>> 3; i3 < i11; i3++) {
                    j2 ^= (long) ((bArr4[i3] & GF2Field.MASK) << (i8 << 3));
                    i8++;
                }
                r8 = (j2 >>> i7) & ((long) r19);
                i9 = (int) (((long) i4) + r8);
                System.arraycopy(bArr2, i10 * digestSize, obj3, 0, digestSize);
                for (r6 = r8; r6 < ((long) r19); r6++) {
                    this.messDigestOTS.update(obj3, 0, obj3.length);
                    obj3 = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(obj3, 0);
                }
                System.arraycopy(obj3, 0, obj, i10 * digestSize, digestSize);
                i10++;
                i8 = i6;
                i4 = i9;
            }
            i3 = i8 >>> 3;
            if (i3 < digestSize) {
                i7 = i8 % 8;
                j2 = 0;
                i8 = 0;
                while (i3 < digestSize) {
                    j2 ^= (long) ((bArr4[i3] & GF2Field.MASK) << (i8 << 3));
                    i8++;
                    i3++;
                }
                r8 = (j2 >>> i7) & ((long) r19);
                i4 = (int) (((long) i4) + r8);
                System.arraycopy(bArr2, i10 * digestSize, obj3, 0, digestSize);
                while (r8 < ((long) r19)) {
                    this.messDigestOTS.update(obj3, 0, obj3.length);
                    obj3 = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(obj3, 0);
                    r8++;
                }
                System.arraycopy(obj3, 0, obj, i10 * digestSize, digestSize);
                i10++;
            }
            i8 = i10;
            i7 = (i << this.f405w) - i4;
            i3 = 0;
            Object obj6 = obj3;
            while (i3 < log) {
                System.arraycopy(bArr2, i8 * digestSize, obj6, 0, digestSize);
                for (long j3 = (long) (i7 & r19); j3 < ((long) r19); j3++) {
                    this.messDigestOTS.update(obj6, 0, obj6.length);
                    obj6 = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(obj6, 0);
                }
                System.arraycopy(obj6, 0, obj, i8 * digestSize, digestSize);
                i7 >>>= this.f405w;
                i3 = this.f405w + i3;
                i8++;
            }
        }
        bArr3 = new byte[digestSize];
        this.messDigestOTS.update(obj, 0, obj.length);
        bArr3 = new byte[this.messDigestOTS.getDigestSize()];
        this.messDigestOTS.doFinal(bArr3, 0);
        return bArr3;
    }

    public int getLog(int i) {
        int i2 = 1;
        int i3 = 2;
        while (i3 < i) {
            i3 <<= 1;
            i2++;
        }
        return i2;
    }

    public int getSignatureLength() {
        int digestSize = this.messDigestOTS.getDigestSize();
        int i = ((digestSize << 3) + (this.f405w - 1)) / this.f405w;
        return digestSize * (i + (((getLog((i << this.f405w) + 1) + this.f405w) - 1) / this.f405w));
    }
}
