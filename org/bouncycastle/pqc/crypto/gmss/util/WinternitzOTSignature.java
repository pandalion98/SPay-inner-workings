package org.bouncycastle.pqc.crypto.gmss.util;

import java.lang.reflect.Array;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class WinternitzOTSignature {
    private int checksumsize;
    private GMSSRandom gmssRandom;
    private int keysize;
    private int mdsize;
    private Digest messDigestOTS;
    private int messagesize;
    private byte[][] privateKeyOTS;
    private int f406w;

    public WinternitzOTSignature(byte[] bArr, Digest digest, int i) {
        this.f406w = i;
        this.messDigestOTS = digest;
        this.gmssRandom = new GMSSRandom(this.messDigestOTS);
        this.mdsize = this.messDigestOTS.getDigestSize();
        this.messagesize = (int) Math.ceil(((double) (this.mdsize << 3)) / ((double) i));
        this.checksumsize = getLog((this.messagesize << i) + 1);
        this.keysize = this.messagesize + ((int) Math.ceil(((double) this.checksumsize) / ((double) i)));
        this.privateKeyOTS = (byte[][]) Array.newInstance(Byte.TYPE, new int[]{this.keysize, this.mdsize});
        Object obj = new byte[this.mdsize];
        System.arraycopy(bArr, 0, obj, 0, obj.length);
        for (int i2 = 0; i2 < this.keysize; i2++) {
            this.privateKeyOTS[i2] = this.gmssRandom.nextSeed(obj);
        }
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

    public byte[][] getPrivateKey() {
        return this.privateKeyOTS;
    }

    public byte[] getPublicKey() {
        Object obj = new byte[(this.keysize * this.mdsize)];
        byte[] bArr = new byte[this.mdsize];
        int i = 1 << this.f406w;
        for (int i2 = 0; i2 < this.keysize; i2++) {
            this.messDigestOTS.update(this.privateKeyOTS[i2], 0, this.privateKeyOTS[i2].length);
            Object obj2 = new byte[this.messDigestOTS.getDigestSize()];
            this.messDigestOTS.doFinal(obj2, 0);
            for (int i3 = 2; i3 < i; i3++) {
                this.messDigestOTS.update(obj2, 0, obj2.length);
                obj2 = new byte[this.messDigestOTS.getDigestSize()];
                this.messDigestOTS.doFinal(obj2, 0);
            }
            System.arraycopy(obj2, 0, obj, this.mdsize * i2, this.mdsize);
        }
        this.messDigestOTS.update(obj, 0, obj.length);
        bArr = new byte[this.messDigestOTS.getDigestSize()];
        this.messDigestOTS.doFinal(bArr, 0);
        return bArr;
    }

    public byte[] getSignature(byte[] bArr) {
        Object obj = new byte[(this.keysize * this.mdsize)];
        byte[] bArr2 = new byte[this.mdsize];
        int i = 0;
        int i2 = 0;
        this.messDigestOTS.update(bArr, 0, bArr.length);
        byte[] bArr3 = new byte[this.messDigestOTS.getDigestSize()];
        this.messDigestOTS.doFinal(bArr3, 0);
        int i3;
        int i4;
        Object obj2;
        int i5;
        int i6;
        int i7;
        int i8;
        if (8 % this.f406w == 0) {
            i3 = 8 / this.f406w;
            i4 = (1 << this.f406w) - 1;
            obj2 = new byte[this.mdsize];
            for (i5 = 0; i5 < bArr3.length; i5++) {
                i6 = 0;
                while (i6 < i3) {
                    i7 = bArr3[i5] & i4;
                    i8 = i2 + i7;
                    System.arraycopy(this.privateKeyOTS[i], 0, obj2, 0, this.mdsize);
                    for (i2 = i7; i2 > 0; i2--) {
                        this.messDigestOTS.update(obj2, 0, obj2.length);
                        obj2 = new byte[this.messDigestOTS.getDigestSize()];
                        this.messDigestOTS.doFinal(obj2, 0);
                    }
                    System.arraycopy(obj2, 0, obj, this.mdsize * i, this.mdsize);
                    bArr3[i5] = (byte) (bArr3[i5] >>> this.f406w);
                    i++;
                    i6++;
                    i2 = i8;
                }
            }
            i2 = (this.messagesize << this.f406w) - i2;
            i5 = 0;
            while (i5 < this.checksumsize) {
                System.arraycopy(this.privateKeyOTS[i], 0, obj2, 0, this.mdsize);
                for (i7 = i2 & i4; i7 > 0; i7--) {
                    this.messDigestOTS.update(obj2, 0, obj2.length);
                    obj2 = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(obj2, 0);
                }
                System.arraycopy(obj2, 0, obj, this.mdsize * i, this.mdsize);
                i2 >>>= this.f406w;
                i++;
                i5 += this.f406w;
            }
        } else if (this.f406w < 8) {
            r15 = this.mdsize / this.f406w;
            r16 = (1 << this.f406w) - 1;
            r8 = new byte[this.mdsize];
            r5 = 0;
            i4 = 0;
            i6 = 0;
            i8 = 0;
            while (i4 < r15) {
                r6 = 0;
                for (i5 = 0; i5 < this.f406w; i5++) {
                    r6 ^= (long) ((bArr3[r5] & GF2Field.MASK) << (i5 << 3));
                    r5++;
                }
                i3 = i8;
                i8 = i6;
                Object obj3 = r8;
                r8 = r6;
                r4 = obj3;
                for (i = 0; i < 8; i++) {
                    i2 = (int) (((long) r16) & r8);
                    i8 += i2;
                    System.arraycopy(this.privateKeyOTS[i3], 0, r4, 0, this.mdsize);
                    while (i2 > 0) {
                        this.messDigestOTS.update(r4, 0, r4.length);
                        r4 = new byte[this.messDigestOTS.getDigestSize()];
                        this.messDigestOTS.doFinal(r4, 0);
                        i2--;
                    }
                    System.arraycopy(r4, 0, obj, this.mdsize * i3, this.mdsize);
                    r8 >>>= this.f406w;
                    i3++;
                }
                i4++;
                r8 = r4;
                i6 = i8;
                i8 = i3;
            }
            i3 = this.mdsize % this.f406w;
            r6 = 0;
            for (i5 = 0; i5 < i3; i5++) {
                r6 ^= (long) ((bArr3[r5] & GF2Field.MASK) << (i5 << 3));
                r5++;
            }
            i3 <<= 3;
            obj2 = r8;
            long j = r6;
            i2 = 0;
            i = i8;
            i8 = i6;
            r8 = j;
            while (i2 < i3) {
                i5 = (int) (((long) r16) & r8);
                i8 += i5;
                System.arraycopy(this.privateKeyOTS[i], 0, obj2, 0, this.mdsize);
                while (i5 > 0) {
                    this.messDigestOTS.update(obj2, 0, obj2.length);
                    obj2 = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(obj2, 0);
                    i5--;
                }
                System.arraycopy(obj2, 0, obj, this.mdsize * i, this.mdsize);
                r8 >>>= this.f406w;
                i++;
                i2 = this.f406w + i2;
            }
            i2 = (this.messagesize << this.f406w) - i8;
            i5 = 0;
            while (i5 < this.checksumsize) {
                System.arraycopy(this.privateKeyOTS[i], 0, obj2, 0, this.mdsize);
                for (i7 = i2 & r16; i7 > 0; i7--) {
                    this.messDigestOTS.update(obj2, 0, obj2.length);
                    obj2 = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(obj2, 0);
                }
                System.arraycopy(obj2, 0, obj, this.mdsize * i, this.mdsize);
                i2 >>>= this.f406w;
                i++;
                i5 += this.f406w;
            }
        } else if (this.f406w < 57) {
            i4 = (this.mdsize << 3) - this.f406w;
            r15 = (1 << this.f406w) - 1;
            r8 = new byte[this.mdsize];
            r5 = 0;
            i6 = 0;
            i8 = 0;
            while (r5 <= i4) {
                r16 = r5 % 8;
                i3 = r5 + this.f406w;
                int i9 = (i3 + 7) >>> 3;
                r6 = 0;
                r5 = 0;
                for (i5 = r5 >>> 3; i5 < i9; i5++) {
                    r6 ^= (long) ((bArr3[i5] & GF2Field.MASK) << (r5 << 3));
                    r5++;
                }
                long j2 = (r6 >>> r16) & ((long) r15);
                i6 = (int) (((long) i6) + j2);
                System.arraycopy(this.privateKeyOTS[i8], 0, r8, 0, this.mdsize);
                while (j2 > 0) {
                    this.messDigestOTS.update(r8, 0, r8.length);
                    r8 = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(r8, 0);
                    j2--;
                }
                System.arraycopy(r8, 0, obj, this.mdsize * i8, this.mdsize);
                i8++;
                r5 = i3;
            }
            i5 = r5 >>> 3;
            if (i5 < this.mdsize) {
                i3 = r5 % 8;
                r6 = 0;
                r5 = 0;
                while (i5 < this.mdsize) {
                    r6 ^= (long) ((bArr3[i5] & GF2Field.MASK) << (r5 << 3));
                    r5++;
                    i5++;
                }
                r6 = ((long) r15) & (r6 >>> i3);
                r5 = (int) (((long) i6) + r6);
                System.arraycopy(this.privateKeyOTS[i8], 0, r8, 0, this.mdsize);
                r4 = r8;
                while (r6 > 0) {
                    this.messDigestOTS.update(r4, 0, r4.length);
                    r4 = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(r4, 0);
                    r6--;
                }
                System.arraycopy(r4, 0, obj, this.mdsize * i8, this.mdsize);
                i2 = i8 + 1;
            } else {
                r4 = r8;
                r5 = i6;
                i2 = i8;
            }
            i = (this.messagesize << this.f406w) - r5;
            obj2 = r4;
            i5 = 0;
            int i10 = i;
            i = i2;
            i2 = i10;
            while (i5 < this.checksumsize) {
                System.arraycopy(this.privateKeyOTS[i], 0, obj2, 0, this.mdsize);
                for (r8 = (long) (i2 & r15); r8 > 0; r8--) {
                    this.messDigestOTS.update(obj2, 0, obj2.length);
                    obj2 = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(obj2, 0);
                }
                System.arraycopy(obj2, 0, obj, this.mdsize * i, this.mdsize);
                i2 >>>= this.f406w;
                i++;
                i5 += this.f406w;
            }
        }
        return obj;
    }
}
