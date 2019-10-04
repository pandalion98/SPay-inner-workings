/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.ArithmeticException
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.System
 *  java.math.BigInteger
 *  java.util.Random
 */
package org.bouncycastle.pqc.math.linearalgebra;

import java.math.BigInteger;
import java.util.Random;
import org.bouncycastle.pqc.math.linearalgebra.GF2nElement;
import org.bouncycastle.pqc.math.linearalgebra.GF2nField;
import org.bouncycastle.pqc.math.linearalgebra.GF2nONBField;
import org.bouncycastle.pqc.math.linearalgebra.GFElement;

public class GF2nONBElement
extends GF2nElement {
    private static final int MAXLONG = 64;
    private static final long[] mBitmask = new long[]{1L, 2L, 4L, 8L, 16L, 32L, 64L, 128L, 256L, 512L, 1024L, 2048L, 4096L, 8192L, 16384L, 32768L, 65536L, 131072L, 262144L, 524288L, 0x100000L, 0x200000L, 0x400000L, 0x800000L, 0x1000000L, 0x2000000L, 0x4000000L, 0x8000000L, 0x10000000L, 0x20000000L, 0x40000000L, 0x80000000L, 0x100000000L, 0x200000000L, 0x400000000L, 0x800000000L, 0x1000000000L, 0x2000000000L, 0x4000000000L, 0x8000000000L, 0x10000000000L, 0x20000000000L, 0x40000000000L, 0x80000000000L, 0x100000000000L, 0x200000000000L, 0x400000000000L, 0x800000000000L, 0x1000000000000L, 0x2000000000000L, 0x4000000000000L, 0x8000000000000L, 0x10000000000000L, 0x20000000000000L, 0x40000000000000L, 0x80000000000000L, 0x100000000000000L, 0x200000000000000L, 0x400000000000000L, 0x800000000000000L, 0x1000000000000000L, 0x2000000000000000L, 0x4000000000000000L, Long.MIN_VALUE};
    private static final int[] mIBY64;
    private static final long[] mMaxmask;
    private int mBit;
    private int mLength;
    private long[] mPol;

    static {
        mMaxmask = new long[]{1L, 3L, 7L, 15L, 31L, 63L, 127L, 255L, 511L, 1023L, 2047L, 4095L, 8191L, 16383L, 32767L, 65535L, 131071L, 262143L, 524287L, 1048575L, 0x1FFFFFL, 0x3FFFFFL, 0x7FFFFFL, 0xFFFFFFL, 0x1FFFFFFL, 0x3FFFFFFL, 0x7FFFFFFL, 0xFFFFFFFL, 0x1FFFFFFFL, 0x3FFFFFFFL, Integer.MAX_VALUE, 0xFFFFFFFFL, 0x1FFFFFFFFL, 0x3FFFFFFFFL, 0x7FFFFFFFFL, 0xFFFFFFFFFL, 0x1FFFFFFFFFL, 0x3FFFFFFFFFL, 0x7FFFFFFFFFL, 0xFFFFFFFFFFL, 0x1FFFFFFFFFFL, 0x3FFFFFFFFFFL, 0x7FFFFFFFFFFL, 0xFFFFFFFFFFFL, 0x1FFFFFFFFFFFL, 0x3FFFFFFFFFFFL, 0x7FFFFFFFFFFFL, 0xFFFFFFFFFFFFL, 0x1FFFFFFFFFFFFL, 0x3FFFFFFFFFFFFL, 0x7FFFFFFFFFFFFL, 0xFFFFFFFFFFFFFL, 0x1FFFFFFFFFFFFFL, 0x3FFFFFFFFFFFFFL, 0x7FFFFFFFFFFFFFL, 0xFFFFFFFFFFFFFFL, 0x1FFFFFFFFFFFFFFL, 0x3FFFFFFFFFFFFFFL, 0x7FFFFFFFFFFFFFFL, 0xFFFFFFFFFFFFFFFL, 0x1FFFFFFFFFFFFFFFL, 0x3FFFFFFFFFFFFFFFL, Long.MAX_VALUE, -1L};
        mIBY64 = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5};
    }

    public GF2nONBElement(GF2nONBElement gF2nONBElement) {
        this.mField = gF2nONBElement.mField;
        this.mDegree = this.mField.getDegree();
        this.mLength = ((GF2nONBField)this.mField).getONBLength();
        this.mBit = ((GF2nONBField)this.mField).getONBBit();
        this.mPol = new long[this.mLength];
        this.assign(gF2nONBElement.getElement());
    }

    public GF2nONBElement(GF2nONBField gF2nONBField, BigInteger bigInteger) {
        this.mField = gF2nONBField;
        this.mDegree = this.mField.getDegree();
        this.mLength = gF2nONBField.getONBLength();
        this.mBit = gF2nONBField.getONBBit();
        this.mPol = new long[this.mLength];
        this.assign(bigInteger);
    }

    public GF2nONBElement(GF2nONBField gF2nONBField, Random random) {
        this.mField = gF2nONBField;
        this.mDegree = this.mField.getDegree();
        this.mLength = gF2nONBField.getONBLength();
        this.mBit = gF2nONBField.getONBBit();
        this.mPol = new long[this.mLength];
        int n = this.mLength;
        if (n > 1) {
            for (int i = 0; i < -1 + this.mLength; ++i) {
                this.mPol[i] = random.nextLong();
            }
            long l = random.nextLong();
            this.mPol[-1 + this.mLength] = l >>> 64 - this.mBit;
            return;
        }
        this.mPol[0] = random.nextLong();
        this.mPol[0] = this.mPol[0] >>> 64 - this.mBit;
    }

    public GF2nONBElement(GF2nONBField gF2nONBField, byte[] arrby) {
        this.mField = gF2nONBField;
        this.mDegree = this.mField.getDegree();
        this.mLength = gF2nONBField.getONBLength();
        this.mBit = gF2nONBField.getONBBit();
        this.mPol = new long[this.mLength];
        this.assign(arrby);
    }

    private GF2nONBElement(GF2nONBField gF2nONBField, long[] arrl) {
        this.mField = gF2nONBField;
        this.mDegree = this.mField.getDegree();
        this.mLength = gF2nONBField.getONBLength();
        this.mBit = gF2nONBField.getONBBit();
        this.mPol = arrl;
    }

    public static GF2nONBElement ONE(GF2nONBField gF2nONBField) {
        int n = gF2nONBField.getONBLength();
        long[] arrl = new long[n];
        for (int i = 0; i < n - 1; ++i) {
            arrl[i] = -1L;
        }
        arrl[n - 1] = mMaxmask[-1 + gF2nONBField.getONBBit()];
        return new GF2nONBElement(gF2nONBField, arrl);
    }

    public static GF2nONBElement ZERO(GF2nONBField gF2nONBField) {
        return new GF2nONBElement(gF2nONBField, new long[gF2nONBField.getONBLength()]);
    }

    private void assign(BigInteger bigInteger) {
        this.assign(bigInteger.toByteArray());
    }

    private void assign(byte[] arrby) {
        this.mPol = new long[this.mLength];
        for (int i = 0; i < arrby.length; ++i) {
            long[] arrl = this.mPol;
            int n = i >>> 3;
            arrl[n] = arrl[n] | (255L & (long)arrby[-1 + arrby.length - i]) << ((i & 7) << 3);
        }
    }

    private void assign(long[] arrl) {
        System.arraycopy((Object)arrl, (int)0, (Object)this.mPol, (int)0, (int)this.mLength);
    }

    private long[] getElement() {
        long[] arrl = new long[this.mPol.length];
        System.arraycopy((Object)this.mPol, (int)0, (Object)arrl, (int)0, (int)this.mPol.length);
        return arrl;
    }

    private long[] getElementReverseOrder() {
        long[] arrl = new long[this.mPol.length];
        for (int i = 0; i < this.mDegree; ++i) {
            if (!this.testBit(-1 + (this.mDegree - i))) continue;
            int n = i >>> 6;
            arrl[n] = arrl[n] | mBitmask[i & 63];
        }
        return arrl;
    }

    @Override
    public GFElement add(GFElement gFElement) {
        GF2nONBElement gF2nONBElement = new GF2nONBElement(this);
        gF2nONBElement.addToThis(gFElement);
        return gF2nONBElement;
    }

    @Override
    public void addToThis(GFElement gFElement) {
        if (!(gFElement instanceof GF2nONBElement)) {
            throw new RuntimeException();
        }
        if (!this.mField.equals(((GF2nONBElement)gFElement).mField)) {
            throw new RuntimeException();
        }
        for (int i = 0; i < this.mLength; ++i) {
            long[] arrl = this.mPol;
            arrl[i] = arrl[i] ^ ((GF2nONBElement)gFElement).mPol[i];
        }
    }

    @Override
    void assignOne() {
        for (int i = 0; i < -1 + this.mLength; ++i) {
            this.mPol[i] = -1L;
        }
        this.mPol[-1 + this.mLength] = mMaxmask[-1 + this.mBit];
    }

    @Override
    void assignZero() {
        this.mPol = new long[this.mLength];
    }

    @Override
    public Object clone() {
        return new GF2nONBElement(this);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean equals(Object object) {
        if (object != null && object instanceof GF2nONBElement) {
            GF2nONBElement gF2nONBElement = (GF2nONBElement)object;
            int n = 0;
            do {
                if (n >= this.mLength) {
                    return true;
                }
                if (this.mPol[n] != gF2nONBElement.mPol[n]) break;
                ++n;
            } while (true);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.mPol.hashCode();
    }

    @Override
    public GF2nElement increase() {
        GF2nONBElement gF2nONBElement = new GF2nONBElement(this);
        gF2nONBElement.increaseThis();
        return gF2nONBElement;
    }

    @Override
    public void increaseThis() {
        this.addToThis(GF2nONBElement.ONE((GF2nONBField)this.mField));
    }

    @Override
    public GFElement invert() {
        GF2nONBElement gF2nONBElement = new GF2nONBElement(this);
        gF2nONBElement.invertThis();
        return gF2nONBElement;
    }

    public void invertThis() {
        int n;
        if (this.isZero()) {
            throw new ArithmeticException();
        }
        boolean bl = false;
        for (n = 31; !bl && n >= 0; --n) {
            if (((long)(-1 + this.mDegree) & mBitmask[n]) == 0L) continue;
            bl = true;
        }
        int n2 = n + 1;
        GF2nONBElement.ZERO((GF2nONBField)this.mField);
        GF2nONBElement gF2nONBElement = new GF2nONBElement(this);
        int n3 = 1;
        for (int i = n2 - 1; i >= 0; --i) {
            GF2nElement gF2nElement = (GF2nElement)((GF2nElement)gF2nONBElement).clone();
            for (int j = 1; j <= n3; ++j) {
                gF2nElement.squareThis();
            }
            gF2nONBElement.multiplyThisBy((GFElement)gF2nElement);
            int n4 = n3 << 1;
            if (((long)(-1 + this.mDegree) & mBitmask[i]) != 0L) {
                ((GF2nElement)gF2nONBElement).squareThis();
                gF2nONBElement.multiplyThisBy((GFElement)this);
            }
            n3 = ++n4;
        }
        ((GF2nElement)gF2nONBElement).squareThis();
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean isOne() {
        boolean bl = true;
        for (int i = 0; i < -1 + this.mLength && bl; ++i) {
            bl = bl && (-1L & this.mPol[i]) == -1L;
        }
        if (!bl) {
            return bl;
        }
        return bl && (this.mPol[-1 + this.mLength] & mMaxmask[-1 + this.mBit]) == mMaxmask[-1 + this.mBit];
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean isZero() {
        boolean bl = true;
        for (int i = 0; i < this.mLength && bl; ++i) {
            bl = bl && (-1L & this.mPol[i]) == 0L;
        }
        return bl;
    }

    @Override
    public GFElement multiply(GFElement gFElement) {
        GF2nONBElement gF2nONBElement = new GF2nONBElement(this);
        gF2nONBElement.multiplyThisBy(gFElement);
        return gF2nONBElement;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void multiplyThisBy(GFElement gFElement) {
        if (!(gFElement instanceof GF2nONBElement)) {
            throw new RuntimeException("The elements have different representation: not yet implemented");
        }
        if (!this.mField.equals(((GF2nONBElement)gFElement).mField)) {
            throw new RuntimeException();
        }
        if (this.equals(gFElement)) {
            this.squareThis();
            return;
        }
        long[] arrl = this.mPol;
        long[] arrl2 = ((GF2nONBElement)gFElement).mPol;
        long[] arrl3 = new long[this.mLength];
        int[][] arrn = ((GF2nONBField)this.mField).mMult;
        int n = -1 + this.mLength;
        int n2 = -1 + this.mBit;
        long l = mBitmask[63];
        long l2 = mBitmask[n2];
        int n3 = 0;
        do {
            if (n3 >= this.mDegree) {
                this.assign(arrl3);
                return;
            }
            boolean bl = false;
            for (int i = 0; i < this.mDegree; ++i) {
                int n4;
                int n5;
                int n6 = mIBY64[i];
                int n7 = i & 63;
                int n8 = mIBY64[arrn[i][0]];
                int n9 = 63 & arrn[i][0];
                if ((arrl[n6] & mBitmask[n7]) == 0L) continue;
                if ((arrl2[n8] & mBitmask[n9]) != 0L) {
                    bl ^= true;
                }
                if (arrn[i][1] == -1 || (arrl2[n5 = mIBY64[arrn[i][1]]] & mBitmask[n4 = 63 & arrn[i][1]]) == 0L) continue;
                bl ^= true;
            }
            int n10 = mIBY64[n3];
            int n11 = n3 & 63;
            if (bl) {
                arrl3[n10] = arrl3[n10] ^ mBitmask[n11];
            }
            if (this.mLength <= 1) {
                boolean bl2 = (1L & arrl[0]) == 1L;
                arrl[0] = arrl[0] >>> 1;
                if (bl2) {
                    arrl[0] = l2 ^ arrl[0];
                }
                boolean bl3 = (1L & arrl2[0]) == 1L;
                arrl2[0] = arrl2[0] >>> 1;
                if (bl3) {
                    arrl2[0] = l2 ^ arrl2[0];
                }
            } else {
                boolean bl4 = (1L & arrl[n]) == 1L;
                boolean bl5 = bl4;
                for (int i = n - 1; i >= 0; --i) {
                    boolean bl6 = (1L & arrl[i]) != 0L;
                    arrl[i] = arrl[i] >>> 1;
                    if (bl5) {
                        arrl[i] = l ^ arrl[i];
                    }
                    bl5 = bl6;
                }
                arrl[n] = arrl[n] >>> 1;
                if (bl5) {
                    arrl[n] = l2 ^ arrl[n];
                }
                boolean bl7 = (1L & arrl2[n]) == 1L;
                boolean bl8 = bl7;
                for (int i = n - 1; i >= 0; --i) {
                    boolean bl9 = (1L & arrl2[i]) != 0L;
                    arrl2[i] = arrl2[i] >>> 1;
                    if (bl8) {
                        arrl2[i] = l ^ arrl2[i];
                    }
                    bl8 = bl9;
                }
                arrl2[n] = arrl2[n] >>> 1;
                if (bl8) {
                    arrl2[n] = l2 ^ arrl2[n];
                }
            }
            ++n3;
        } while (true);
    }

    void reverseOrder() {
        this.mPol = this.getElementReverseOrder();
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public GF2nElement solveQuadraticEquation() {
        if (this.trace() == 1) {
            throw new RuntimeException();
        }
        long l = mBitmask[63];
        long[] arrl = new long[this.mLength];
        long l2 = 0L;
        for (int i = 0; i < -1 + this.mLength; ++i) {
            for (int j = 1; j < 64; ++j) {
                if ((mBitmask[j] & this.mPol[i]) != 0L && (l2 & mBitmask[j - 1]) != 0L || (this.mPol[i] & mBitmask[j]) == 0L && (l2 & mBitmask[j - 1]) == 0L) continue;
                l2 ^= mBitmask[j];
            }
            arrl[i] = l2;
            l2 = (l & l2) != 0L && (1L & this.mPol[i + 1]) == 1L || (l2 & l) == 0L && (1L & this.mPol[i + 1]) == 0L ? 0L : 1L;
        }
        int n = 63 & this.mDegree;
        long l3 = this.mPol[-1 + this.mLength];
        long l4 = l2;
        int n2 = 1;
        do {
            if (n2 >= n) {
                arrl[-1 + this.mLength] = l4;
                return new GF2nONBElement((GF2nONBField)this.mField, arrl);
            }
            if (!((l3 & mBitmask[n2]) != 0L && (l4 & mBitmask[n2 - 1]) != 0L || (l3 & mBitmask[n2]) == 0L && (l4 & mBitmask[n2 - 1]) == 0L)) {
                l4 ^= mBitmask[n2];
            }
            ++n2;
        } while (true);
    }

    @Override
    public GF2nElement square() {
        GF2nONBElement gF2nONBElement = new GF2nONBElement(this);
        gF2nONBElement.squareThis();
        return gF2nONBElement;
    }

    @Override
    public GF2nElement squareRoot() {
        GF2nONBElement gF2nONBElement = new GF2nONBElement(this);
        gF2nONBElement.squareRootThis();
        return gF2nONBElement;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void squareRootThis() {
        long[] arrl = this.getElement();
        int n = -1 + this.mLength;
        int n2 = -1 + this.mBit;
        long l = mBitmask[63];
        boolean bl = (1L & arrl[0]) != 0L;
        int n3 = n;
        boolean bl2 = bl;
        do {
            if (n3 < 0) {
                this.assign(arrl);
                return;
            }
            boolean bl3 = (1L & arrl[n3]) != 0L;
            arrl[n3] = arrl[n3] >>> 1;
            if (bl2) {
                arrl[n3] = n3 == n ? arrl[n3] ^ mBitmask[n2] : l ^ arrl[n3];
            }
            --n3;
            bl2 = bl3;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void squareThis() {
        long[] arrl = this.getElement();
        int n = -1 + this.mLength;
        int n2 = -1 + this.mBit;
        long l = mBitmask[63];
        boolean bl = (arrl[n] & mBitmask[n2]) != 0L;
        boolean bl2 = bl;
        for (int i = 0; i < n; ++i) {
            boolean bl3 = (l & arrl[i]) != 0L;
            arrl[i] = arrl[i] << 1;
            if (bl2) {
                arrl[i] = 1L ^ arrl[i];
            }
            bl2 = bl3;
        }
        boolean bl4 = (arrl[n] & mBitmask[n2]) != 0L;
        arrl[n] = arrl[n] << 1;
        if (bl2) {
            arrl[n] = 1L ^ arrl[n];
        }
        if (bl4) {
            arrl[n] = arrl[n] ^ mBitmask[n2 + 1];
        }
        this.assign(arrl);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    boolean testBit(int n) {
        return n >= 0 && n <= this.mDegree && (this.mPol[n >>> 6] & mBitmask[n & 63]) != 0L;
    }

    @Override
    public boolean testRightmostBit() {
        return (this.mPol[-1 + this.mLength] & mBitmask[-1 + this.mBit]) != 0L;
    }

    @Override
    public byte[] toByteArray() {
        int n = 1 + (-1 + this.mDegree >> 3);
        byte[] arrby = new byte[n];
        for (int i = 0; i < n; ++i) {
            arrby[-1 + (n - i)] = (byte)((this.mPol[i >>> 3] & 255L << ((i & 7) << 3)) >>> ((i & 7) << 3));
        }
        return arrby;
    }

    @Override
    public BigInteger toFlexiBigInt() {
        return new BigInteger(1, this.toByteArray());
    }

    @Override
    public String toString() {
        return this.toString(16);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public String toString(int n) {
        String string;
        String string2 = "";
        long[] arrl = this.getElement();
        int n2 = this.mBit;
        if (n == 2) {
            string = string2;
        } else {
            if (n != 16) return string2;
            char[] arrc = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
            for (int i = -1 + arrl.length; i >= 0; --i) {
                String string3 = string2 + arrc[15 & (int)(arrl[i] >>> 60)];
                String string4 = string3 + arrc[15 & (int)(arrl[i] >>> 56)];
                String string5 = string4 + arrc[15 & (int)(arrl[i] >>> 52)];
                String string6 = string5 + arrc[15 & (int)(arrl[i] >>> 48)];
                String string7 = string6 + arrc[15 & (int)(arrl[i] >>> 44)];
                String string8 = string7 + arrc[15 & (int)(arrl[i] >>> 40)];
                String string9 = string8 + arrc[15 & (int)(arrl[i] >>> 36)];
                String string10 = string9 + arrc[15 & (int)(arrl[i] >>> 32)];
                String string11 = string10 + arrc[15 & (int)(arrl[i] >>> 28)];
                String string12 = string11 + arrc[15 & (int)(arrl[i] >>> 24)];
                String string13 = string12 + arrc[15 & (int)(arrl[i] >>> 20)];
                String string14 = string13 + arrc[15 & (int)(arrl[i] >>> 16)];
                String string15 = string14 + arrc[15 & (int)(arrl[i] >>> 12)];
                String string16 = string15 + arrc[15 & (int)(arrl[i] >>> 8)];
                String string17 = string16 + arrc[15 & (int)(arrl[i] >>> 4)];
                String string18 = string17 + arrc[15 & (int)arrl[i]];
                String string19 = string18 + " ";
                string2 = string19;
            }
            return string2;
        }
        for (int i = n2 - 1; i >= 0; --i) {
            String string20 = (arrl[-1 + arrl.length] & 1L << i) == 0L ? string + "0" : string + "1";
            string = string20;
        }
        string2 = string;
        for (int i = -2 + arrl.length; i >= 0; --i) {
            for (int j = 63; j >= 0; --j) {
                string2 = (arrl[i] & mBitmask[j]) == 0L ? string2 + "0" : string2 + "1";
            }
        }
        return string2;
    }

    @Override
    public int trace() {
        int n = 0;
        int n2 = -1 + this.mLength;
        int n3 = 0;
        for (int i = 0; i < n2; ++i) {
            for (int j = 0; j < 64; ++j) {
                if ((this.mPol[i] & mBitmask[j]) == 0L) continue;
                n3 ^= 1;
            }
        }
        int n4 = this.mBit;
        while (n < n4) {
            if ((this.mPol[n2] & mBitmask[n]) != 0L) {
                n3 ^= 1;
            }
            ++n;
        }
        return n3;
    }
}

