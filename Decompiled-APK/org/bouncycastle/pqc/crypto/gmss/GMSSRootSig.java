package org.bouncycastle.pqc.crypto.gmss;

import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.lang.reflect.Array;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.pqc.crypto.gmss.util.GMSSRandom;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.util.encoders.Hex;

public class GMSSRootSig {
    private long big8;
    private int checksum;
    private int counter;
    private GMSSRandom gmssRandom;
    private byte[] hash;
    private int height;
    private int ii;
    private int f402k;
    private int keysize;
    private int mdsize;
    private Digest messDigestOTS;
    private int messagesize;
    private byte[] privateKeyOTS;
    private int f403r;
    private byte[] seed;
    private byte[] sign;
    private int steps;
    private int test;
    private long test8;
    private int f404w;

    public GMSSRootSig(Digest digest, int i, int i2) {
        this.messDigestOTS = digest;
        this.gmssRandom = new GMSSRandom(this.messDigestOTS);
        this.mdsize = this.messDigestOTS.getDigestSize();
        this.f404w = i;
        this.height = i2;
        this.f402k = (1 << i) - 1;
        this.messagesize = (int) Math.ceil(((double) (this.mdsize << 3)) / ((double) i));
    }

    public GMSSRootSig(Digest digest, byte[][] bArr, int[] iArr) {
        this.messDigestOTS = digest;
        this.gmssRandom = new GMSSRandom(this.messDigestOTS);
        this.counter = iArr[0];
        this.test = iArr[1];
        this.ii = iArr[2];
        this.f403r = iArr[3];
        this.steps = iArr[4];
        this.keysize = iArr[5];
        this.height = iArr[6];
        this.f404w = iArr[7];
        this.checksum = iArr[8];
        this.mdsize = this.messDigestOTS.getDigestSize();
        this.f402k = (1 << this.f404w) - 1;
        this.messagesize = (int) Math.ceil(((double) (this.mdsize << 3)) / ((double) this.f404w));
        this.privateKeyOTS = bArr[0];
        this.seed = bArr[1];
        this.hash = bArr[2];
        this.sign = bArr[3];
        this.test8 = ((((((((long) (bArr[4][0] & GF2Field.MASK)) | (((long) (bArr[4][1] & GF2Field.MASK)) << 8)) | (((long) (bArr[4][2] & GF2Field.MASK)) << 16)) | (((long) (bArr[4][3] & GF2Field.MASK)) << 24)) | (((long) (bArr[4][4] & GF2Field.MASK)) << 32)) | (((long) (bArr[4][5] & GF2Field.MASK)) << 40)) | (((long) (bArr[4][6] & GF2Field.MASK)) << 48)) | (((long) (bArr[4][7] & GF2Field.MASK)) << 56);
        this.big8 = ((((((((long) (bArr[4][8] & GF2Field.MASK)) | (((long) (bArr[4][9] & GF2Field.MASK)) << 8)) | (((long) (bArr[4][10] & GF2Field.MASK)) << 16)) | (((long) (bArr[4][11] & GF2Field.MASK)) << 24)) | (((long) (bArr[4][12] & GF2Field.MASK)) << 32)) | (((long) (bArr[4][13] & GF2Field.MASK)) << 40)) | (((long) (bArr[4][14] & GF2Field.MASK)) << 48)) | (((long) (bArr[4][15] & GF2Field.MASK)) << 56);
    }

    private void oneStep() {
        if (8 % this.f404w == 0) {
            if (this.test == 0) {
                this.privateKeyOTS = this.gmssRandom.nextSeed(this.seed);
                if (this.ii < this.mdsize) {
                    this.test = this.hash[this.ii] & this.f402k;
                    this.hash[this.ii] = (byte) (this.hash[this.ii] >>> this.f404w);
                } else {
                    this.test = this.checksum & this.f402k;
                    this.checksum >>>= this.f404w;
                }
            } else if (this.test > 0) {
                this.messDigestOTS.update(this.privateKeyOTS, 0, this.privateKeyOTS.length);
                this.privateKeyOTS = new byte[this.messDigestOTS.getDigestSize()];
                this.messDigestOTS.doFinal(this.privateKeyOTS, 0);
                this.test--;
            }
            if (this.test == 0) {
                System.arraycopy(this.privateKeyOTS, 0, this.sign, this.counter * this.mdsize, this.mdsize);
                this.counter++;
                if (this.counter % (8 / this.f404w) == 0) {
                    this.ii++;
                }
            }
        } else if (this.f404w < 8) {
            if (this.test == 0) {
                if (this.counter % 8 == 0 && this.ii < this.mdsize) {
                    this.big8 = 0;
                    if (this.counter < ((this.mdsize / this.f404w) << 3)) {
                        for (r0 = 0; r0 < this.f404w; r0++) {
                            this.big8 ^= (long) ((this.hash[this.ii] & GF2Field.MASK) << (r0 << 3));
                            this.ii++;
                        }
                    } else {
                        for (r0 = 0; r0 < this.mdsize % this.f404w; r0++) {
                            this.big8 ^= (long) ((this.hash[this.ii] & GF2Field.MASK) << (r0 << 3));
                            this.ii++;
                        }
                    }
                }
                if (this.counter == this.messagesize) {
                    this.big8 = (long) this.checksum;
                }
                this.test = (int) (this.big8 & ((long) this.f402k));
                this.privateKeyOTS = this.gmssRandom.nextSeed(this.seed);
            } else if (this.test > 0) {
                this.messDigestOTS.update(this.privateKeyOTS, 0, this.privateKeyOTS.length);
                this.privateKeyOTS = new byte[this.messDigestOTS.getDigestSize()];
                this.messDigestOTS.doFinal(this.privateKeyOTS, 0);
                this.test--;
            }
            if (this.test == 0) {
                System.arraycopy(this.privateKeyOTS, 0, this.sign, this.counter * this.mdsize, this.mdsize);
                this.big8 >>>= this.f404w;
                this.counter++;
            }
        } else if (this.f404w < 57) {
            if (this.test8 == 0) {
                this.big8 = 0;
                this.ii = 0;
                int i = this.f403r % 8;
                int i2 = this.f403r >>> 3;
                if (i2 < this.mdsize) {
                    if (this.f403r <= (this.mdsize << 3) - this.f404w) {
                        this.f403r += this.f404w;
                        r0 = (this.f403r + 7) >>> 3;
                    } else {
                        r0 = this.mdsize;
                        this.f403r += this.f404w;
                    }
                    while (i2 < r0) {
                        this.big8 ^= (long) ((this.hash[i2] & GF2Field.MASK) << (this.ii << 3));
                        this.ii++;
                        i2++;
                    }
                    this.big8 >>>= i;
                    this.test8 = this.big8 & ((long) this.f402k);
                } else {
                    this.test8 = (long) (this.checksum & this.f402k);
                    this.checksum >>>= this.f404w;
                }
                this.privateKeyOTS = this.gmssRandom.nextSeed(this.seed);
            } else if (this.test8 > 0) {
                this.messDigestOTS.update(this.privateKeyOTS, 0, this.privateKeyOTS.length);
                this.privateKeyOTS = new byte[this.messDigestOTS.getDigestSize()];
                this.messDigestOTS.doFinal(this.privateKeyOTS, 0);
                this.test8--;
            }
            if (this.test8 == 0) {
                System.arraycopy(this.privateKeyOTS, 0, this.sign, this.counter * this.mdsize, this.mdsize);
                this.counter++;
            }
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

    public byte[] getSig() {
        return this.sign;
    }

    public byte[][] getStatByte() {
        byte[][] bArr = (byte[][]) Array.newInstance(Byte.TYPE, new int[]{5, this.mdsize});
        bArr[0] = this.privateKeyOTS;
        bArr[1] = this.seed;
        bArr[2] = this.hash;
        bArr[3] = this.sign;
        bArr[4] = getStatLong();
        return bArr;
    }

    public int[] getStatInt() {
        return new int[]{this.counter, this.test, this.ii, this.f403r, this.steps, this.keysize, this.height, this.f404w, this.checksum};
    }

    public byte[] getStatLong() {
        return new byte[]{(byte) ((int) (this.test8 & 255)), (byte) ((int) ((this.test8 >> 8) & 255)), (byte) ((int) ((this.test8 >> 16) & 255)), (byte) ((int) ((this.test8 >> 24) & 255)), (byte) ((int) ((this.test8 >> 32) & 255)), (byte) ((int) ((this.test8 >> 40) & 255)), (byte) ((int) ((this.test8 >> 48) & 255)), (byte) ((int) ((this.test8 >> 56) & 255)), (byte) ((int) (this.big8 & 255)), (byte) ((int) ((this.big8 >> 8) & 255)), (byte) ((int) ((this.big8 >> 16) & 255)), (byte) ((int) ((this.big8 >> 24) & 255)), (byte) ((int) ((this.big8 >> 32) & 255)), (byte) ((int) ((this.big8 >> 40) & 255)), (byte) ((int) ((this.big8 >> 48) & 255)), (byte) ((int) ((this.big8 >> 56) & 255))};
    }

    public void initSign(byte[] bArr, byte[] bArr2) {
        this.hash = new byte[this.mdsize];
        this.messDigestOTS.update(bArr2, 0, bArr2.length);
        this.hash = new byte[this.messDigestOTS.getDigestSize()];
        this.messDigestOTS.doFinal(this.hash, 0);
        Object obj = new byte[this.mdsize];
        System.arraycopy(this.hash, 0, obj, 0, this.mdsize);
        int i = 0;
        int log = getLog((this.messagesize << this.f404w) + 1);
        int i2;
        int i3;
        int i4;
        int i5;
        if (8 % this.f404w == 0) {
            i2 = 8 / this.f404w;
            for (i3 = 0; i3 < this.mdsize; i3++) {
                i4 = 0;
                while (i4 < i2) {
                    i5 = (obj[i3] & this.f402k) + i;
                    obj[i3] = (byte) (obj[i3] >>> this.f404w);
                    i4++;
                    i = i5;
                }
            }
            this.checksum = (this.messagesize << this.f404w) - i;
            i4 = this.checksum;
            i3 = 0;
            while (i3 < log) {
                i += this.f402k & i4;
                i4 >>>= this.f404w;
                i3 += this.f404w;
            }
        } else if (this.f404w < 8) {
            int i6;
            r8 = this.mdsize / this.f404w;
            i2 = 0;
            i = 0;
            for (i6 = 0; i6 < r8; i6++) {
                r2 = 0;
                for (i3 = 0; i3 < this.f404w; i3++) {
                    r2 ^= (long) ((obj[i] & GF2Field.MASK) << (i3 << 3));
                    i++;
                }
                for (i3 = 0; i3 < 8; i3++) {
                    i2 += (int) (((long) this.f402k) & r2);
                    r2 >>>= this.f404w;
                }
            }
            i6 = this.mdsize % this.f404w;
            r2 = 0;
            for (i3 = 0; i3 < i6; i3++) {
                r2 ^= (long) ((obj[i] & GF2Field.MASK) << (i3 << 3));
                i++;
            }
            i3 = 0;
            i = i2;
            while (i3 < (i6 << 3)) {
                i += (int) (((long) this.f402k) & r2);
                r2 >>>= this.f404w;
                i3 += this.f404w;
            }
            this.checksum = (this.messagesize << this.f404w) - i;
            i4 = this.checksum;
            i3 = 0;
            while (i3 < log) {
                i += this.f402k & i4;
                i4 >>>= this.f404w;
                i3 += this.f404w;
            }
        } else if (this.f404w < 57) {
            long j;
            i3 = 0;
            while (i3 <= (this.mdsize << 3) - this.f404w) {
                r8 = i3 % 8;
                i3 += this.f404w;
                j = 0;
                i5 = 0;
                for (i4 = i3 >>> 3; i4 < ((i3 + 7) >>> 3); i4++) {
                    j ^= (long) ((obj[i4] & GF2Field.MASK) << (i5 << 3));
                    i5++;
                }
                r2 = j >>> r8;
                i = (int) ((r2 & ((long) this.f402k)) + ((long) i));
            }
            i4 = i3 >>> 3;
            if (i4 < this.mdsize) {
                i5 = i3 % 8;
                j = 0;
                int i7 = i4;
                i4 = 0;
                for (i3 = i7; i3 < this.mdsize; i3++) {
                    j ^= (long) ((obj[i3] & GF2Field.MASK) << (i4 << 3));
                    i4++;
                }
                i = (int) (((long) i) + ((j >>> i5) & ((long) this.f402k)));
            }
            this.checksum = (this.messagesize << this.f404w) - i;
            i4 = this.checksum;
            i3 = 0;
            while (i3 < log) {
                i += this.f402k & i4;
                i4 >>>= this.f404w;
                i3 += this.f404w;
            }
        }
        this.keysize = this.messagesize + ((int) Math.ceil(((double) log) / ((double) this.f404w)));
        this.steps = (int) Math.ceil(((double) (this.keysize + i)) / ((double) (1 << this.height)));
        this.sign = new byte[(this.keysize * this.mdsize)];
        this.counter = 0;
        this.test = 0;
        this.ii = 0;
        this.test8 = 0;
        this.f403r = 0;
        this.privateKeyOTS = new byte[this.mdsize];
        this.seed = new byte[this.mdsize];
        System.arraycopy(bArr, 0, this.seed, 0, this.mdsize);
    }

    public String toString() {
        String str = BuildConfig.FLAVOR + this.big8 + "  ";
        int[] iArr = new int[9];
        int[] statInt = getStatInt();
        byte[][] bArr = (byte[][]) Array.newInstance(Byte.TYPE, new int[]{5, this.mdsize});
        byte[][] statByte = getStatByte();
        String str2 = str;
        int i = 0;
        while (i < 9) {
            String str3 = str2 + statInt[i] + " ";
            i++;
            str2 = str3;
        }
        String str4 = str2;
        for (int i2 = 0; i2 < 5; i2++) {
            str4 = str4 + new String(Hex.encode(statByte[i2])) + " ";
        }
        return str4;
    }

    public boolean updateSign() {
        for (int i = 0; i < this.steps; i++) {
            if (this.counter < this.keysize) {
                oneStep();
            }
            if (this.counter == this.keysize) {
                return true;
            }
        }
        return false;
    }
}
