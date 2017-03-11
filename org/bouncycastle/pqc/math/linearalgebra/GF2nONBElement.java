package org.bouncycastle.pqc.math.linearalgebra;

import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.math.BigInteger;
import java.util.Random;

public class GF2nONBElement extends GF2nElement {
    private static final int MAXLONG = 64;
    private static final long[] mBitmask;
    private static final int[] mIBY64;
    private static final long[] mMaxmask;
    private int mBit;
    private int mLength;
    private long[] mPol;

    static {
        mBitmask = new long[]{1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072, 262144, 524288, 1048576, 2097152, 4194304, 8388608, 16777216, 33554432, 67108864, 134217728, 268435456, 536870912, 1073741824, 2147483648L, 4294967296L, 8589934592L, 17179869184L, 34359738368L, 68719476736L, 137438953472L, 274877906944L, 549755813888L, 1099511627776L, 2199023255552L, 4398046511104L, 8796093022208L, 17592186044416L, 35184372088832L, 70368744177664L, 140737488355328L, 281474976710656L, 562949953421312L, 1125899906842624L, 2251799813685248L, 4503599627370496L, 9007199254740992L, 18014398509481984L, 36028797018963968L, 72057594037927936L, 144115188075855872L, 288230376151711744L, 576460752303423488L, 1152921504606846976L, 2305843009213693952L, 4611686018427387904L, Long.MIN_VALUE};
        mMaxmask = new long[]{1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383, 32767, 65535, 131071, 262143, 524287, 1048575, 2097151, 4194303, 8388607, 16777215, 33554431, 67108863, 134217727, 268435455, 536870911, 1073741823, 2147483647L, 4294967295L, 8589934591L, 17179869183L, 34359738367L, 68719476735L, 137438953471L, 274877906943L, 549755813887L, 1099511627775L, 2199023255551L, 4398046511103L, 8796093022207L, 17592186044415L, 35184372088831L, 70368744177663L, 140737488355327L, 281474976710655L, 562949953421311L, 1125899906842623L, 2251799813685247L, 4503599627370495L, 9007199254740991L, 18014398509481983L, 36028797018963967L, 72057594037927935L, 144115188075855871L, 288230376151711743L, 576460752303423487L, 1152921504606846975L, 2305843009213693951L, 4611686018427387903L, Long.MAX_VALUE, -1};
        mIBY64 = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5};
    }

    public GF2nONBElement(GF2nONBElement gF2nONBElement) {
        this.mField = gF2nONBElement.mField;
        this.mDegree = this.mField.getDegree();
        this.mLength = ((GF2nONBField) this.mField).getONBLength();
        this.mBit = ((GF2nONBField) this.mField).getONBBit();
        this.mPol = new long[this.mLength];
        assign(gF2nONBElement.getElement());
    }

    public GF2nONBElement(GF2nONBField gF2nONBField, BigInteger bigInteger) {
        this.mField = gF2nONBField;
        this.mDegree = this.mField.getDegree();
        this.mLength = gF2nONBField.getONBLength();
        this.mBit = gF2nONBField.getONBBit();
        this.mPol = new long[this.mLength];
        assign(bigInteger);
    }

    public GF2nONBElement(GF2nONBField gF2nONBField, Random random) {
        int i = 0;
        this.mField = gF2nONBField;
        this.mDegree = this.mField.getDegree();
        this.mLength = gF2nONBField.getONBLength();
        this.mBit = gF2nONBField.getONBBit();
        this.mPol = new long[this.mLength];
        if (this.mLength > 1) {
            while (i < this.mLength - 1) {
                this.mPol[i] = random.nextLong();
                i++;
            }
            this.mPol[this.mLength - 1] = random.nextLong() >>> (64 - this.mBit);
            return;
        }
        this.mPol[0] = random.nextLong();
        this.mPol[0] = this.mPol[0] >>> (64 - this.mBit);
    }

    public GF2nONBElement(GF2nONBField gF2nONBField, byte[] bArr) {
        this.mField = gF2nONBField;
        this.mDegree = this.mField.getDegree();
        this.mLength = gF2nONBField.getONBLength();
        this.mBit = gF2nONBField.getONBBit();
        this.mPol = new long[this.mLength];
        assign(bArr);
    }

    private GF2nONBElement(GF2nONBField gF2nONBField, long[] jArr) {
        this.mField = gF2nONBField;
        this.mDegree = this.mField.getDegree();
        this.mLength = gF2nONBField.getONBLength();
        this.mBit = gF2nONBField.getONBBit();
        this.mPol = jArr;
    }

    public static GF2nONBElement ONE(GF2nONBField gF2nONBField) {
        int oNBLength = gF2nONBField.getONBLength();
        long[] jArr = new long[oNBLength];
        for (int i = 0; i < oNBLength - 1; i++) {
            jArr[i] = -1;
        }
        jArr[oNBLength - 1] = mMaxmask[gF2nONBField.getONBBit() - 1];
        return new GF2nONBElement(gF2nONBField, jArr);
    }

    public static GF2nONBElement ZERO(GF2nONBField gF2nONBField) {
        return new GF2nONBElement(gF2nONBField, new long[gF2nONBField.getONBLength()]);
    }

    private void assign(BigInteger bigInteger) {
        assign(bigInteger.toByteArray());
    }

    private void assign(byte[] bArr) {
        this.mPol = new long[this.mLength];
        for (int i = 0; i < bArr.length; i++) {
            long[] jArr = this.mPol;
            int i2 = i >>> 3;
            jArr[i2] = jArr[i2] | ((((long) bArr[(bArr.length - 1) - i]) & 255) << ((i & 7) << 3));
        }
    }

    private void assign(long[] jArr) {
        System.arraycopy(jArr, 0, this.mPol, 0, this.mLength);
    }

    private long[] getElement() {
        Object obj = new long[this.mPol.length];
        System.arraycopy(this.mPol, 0, obj, 0, this.mPol.length);
        return obj;
    }

    private long[] getElementReverseOrder() {
        long[] jArr = new long[this.mPol.length];
        for (int i = 0; i < this.mDegree; i++) {
            if (testBit((this.mDegree - i) - 1)) {
                int i2 = i >>> 6;
                jArr[i2] = jArr[i2] | mBitmask[i & 63];
            }
        }
        return jArr;
    }

    public GFElement add(GFElement gFElement) {
        GFElement gF2nONBElement = new GF2nONBElement(this);
        gF2nONBElement.addToThis(gFElement);
        return gF2nONBElement;
    }

    public void addToThis(GFElement gFElement) {
        if (!(gFElement instanceof GF2nONBElement)) {
            throw new RuntimeException();
        } else if (this.mField.equals(((GF2nONBElement) gFElement).mField)) {
            for (int i = 0; i < this.mLength; i++) {
                long[] jArr = this.mPol;
                jArr[i] = jArr[i] ^ ((GF2nONBElement) gFElement).mPol[i];
            }
        } else {
            throw new RuntimeException();
        }
    }

    void assignOne() {
        for (int i = 0; i < this.mLength - 1; i++) {
            this.mPol[i] = -1;
        }
        this.mPol[this.mLength - 1] = mMaxmask[this.mBit - 1];
    }

    void assignZero() {
        this.mPol = new long[this.mLength];
    }

    public Object clone() {
        return new GF2nONBElement(this);
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof GF2nONBElement)) {
            return false;
        }
        GF2nONBElement gF2nONBElement = (GF2nONBElement) obj;
        for (int i = 0; i < this.mLength; i++) {
            if (this.mPol[i] != gF2nONBElement.mPol[i]) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        return this.mPol.hashCode();
    }

    public GF2nElement increase() {
        GF2nElement gF2nONBElement = new GF2nONBElement(this);
        gF2nONBElement.increaseThis();
        return gF2nONBElement;
    }

    public void increaseThis() {
        addToThis(ONE((GF2nONBField) this.mField));
    }

    public GFElement invert() {
        GFElement gF2nONBElement = new GF2nONBElement(this);
        gF2nONBElement.invertThis();
        return gF2nONBElement;
    }

    public void invertThis() {
        if (isZero()) {
            throw new ArithmeticException();
        }
        int i = 31;
        Object obj = null;
        while (obj == null && i >= 0) {
            if ((((long) (this.mDegree - 1)) & mBitmask[i]) != 0) {
                obj = 1;
            }
            i--;
        }
        i++;
        ZERO((GF2nONBField) this.mField);
        GF2nElement gF2nONBElement = new GF2nONBElement(this);
        i--;
        int i2 = 1;
        while (i >= 0) {
            GF2nElement gF2nElement = (GF2nElement) gF2nONBElement.clone();
            for (int i3 = 1; i3 <= i2; i3++) {
                gF2nElement.squareThis();
            }
            gF2nONBElement.multiplyThisBy(gF2nElement);
            int i4 = i2 << 1;
            if ((((long) (this.mDegree - 1)) & mBitmask[i]) != 0) {
                gF2nONBElement.squareThis();
                gF2nONBElement.multiplyThisBy(this);
                i4++;
            }
            i--;
            i2 = i4;
        }
        gF2nONBElement.squareThis();
    }

    public boolean isOne() {
        boolean z = true;
        for (int i = 0; i < this.mLength - 1 && z; i++) {
            z = z && (this.mPol[i] & -1) == -1;
        }
        return z ? z && (this.mPol[this.mLength - 1] & mMaxmask[this.mBit - 1]) == mMaxmask[this.mBit - 1] : z;
    }

    public boolean isZero() {
        boolean z = true;
        for (int i = 0; i < this.mLength && z; i++) {
            z = z && (this.mPol[i] & -1) == 0;
        }
        return z;
    }

    public GFElement multiply(GFElement gFElement) {
        GFElement gF2nONBElement = new GF2nONBElement(this);
        gF2nONBElement.multiplyThisBy(gFElement);
        return gF2nONBElement;
    }

    public void multiplyThisBy(GFElement gFElement) {
        if (!(gFElement instanceof GF2nONBElement)) {
            throw new RuntimeException("The elements have different representation: not yet implemented");
        } else if (!this.mField.equals(((GF2nONBElement) gFElement).mField)) {
            throw new RuntimeException();
        } else if (equals(gFElement)) {
            squareThis();
        } else {
            long[] jArr = this.mPol;
            long[] jArr2 = ((GF2nONBElement) gFElement).mPol;
            long[] jArr3 = new long[this.mLength];
            int[][] iArr = ((GF2nONBField) this.mField).mMult;
            int i = this.mLength - 1;
            int i2 = this.mBit - 1;
            long j = mBitmask[63];
            long j2 = mBitmask[i2];
            for (i2 = 0; i2 < this.mDegree; i2++) {
                int i3;
                int i4;
                int i5 = 0;
                for (i3 = 0; i3 < this.mDegree; i3++) {
                    i4 = mIBY64[i3];
                    int i6 = i3 & 63;
                    int i7 = mIBY64[iArr[i3][0]];
                    int i8 = iArr[i3][0] & 63;
                    if ((jArr[i4] & mBitmask[i6]) != 0) {
                        if ((mBitmask[i8] & jArr2[i7]) != 0) {
                            i5 ^= 1;
                        }
                        if (iArr[i3][1] != -1) {
                            if ((jArr2[mIBY64[iArr[i3][1]]] & mBitmask[iArr[i3][1] & 63]) != 0) {
                                i5 ^= 1;
                            }
                        }
                    }
                }
                i3 = mIBY64[i2];
                i4 = i2 & 63;
                if (i5 != 0) {
                    jArr3[i3] = mBitmask[i4] ^ jArr3[i3];
                }
                Object obj;
                if (this.mLength > 1) {
                    i5 = i - 1;
                    Object obj2 = (jArr[i] & 1) == 1 ? 1 : null;
                    while (i5 >= 0) {
                        obj = (jArr[i5] & 1) != 0 ? 1 : null;
                        jArr[i5] = jArr[i5] >>> 1;
                        if (obj2 != null) {
                            jArr[i5] = jArr[i5] ^ j;
                        }
                        i5--;
                        obj2 = obj;
                    }
                    jArr[i] = jArr[i] >>> 1;
                    if (obj2 != null) {
                        jArr[i] = jArr[i] ^ j2;
                    }
                    i5 = i - 1;
                    obj2 = (jArr2[i] & 1) == 1 ? 1 : null;
                    while (i5 >= 0) {
                        obj = (jArr2[i5] & 1) != 0 ? 1 : null;
                        jArr2[i5] = jArr2[i5] >>> 1;
                        if (obj2 != null) {
                            jArr2[i5] = jArr2[i5] ^ j;
                        }
                        i5--;
                        obj2 = obj;
                    }
                    jArr2[i] = jArr2[i] >>> 1;
                    if (obj2 != null) {
                        jArr2[i] = jArr2[i] ^ j2;
                    }
                } else {
                    obj = (jArr[0] & 1) == 1 ? 1 : null;
                    jArr[0] = jArr[0] >>> 1;
                    if (obj != null) {
                        jArr[0] = jArr[0] ^ j2;
                    }
                    obj = (jArr2[0] & 1) == 1 ? 1 : null;
                    jArr2[0] = jArr2[0] >>> 1;
                    if (obj != null) {
                        jArr2[0] = jArr2[0] ^ j2;
                    }
                }
            }
            assign(jArr3);
        }
    }

    void reverseOrder() {
        this.mPol = getElementReverseOrder();
    }

    public GF2nElement solveQuadraticEquation() {
        if (trace() == 1) {
            throw new RuntimeException();
        }
        long j = mBitmask[63];
        long[] jArr = new long[this.mLength];
        long j2 = 0;
        int i = 0;
        while (i < this.mLength - 1) {
            int i2 = 1;
            while (i2 < MAXLONG) {
                if (((mBitmask[i2] & this.mPol[i]) == 0 || (mBitmask[i2 - 1] & j2) == 0) && !((this.mPol[i] & mBitmask[i2]) == 0 && (mBitmask[i2 - 1] & j2) == 0)) {
                    j2 ^= mBitmask[i2];
                }
                i2++;
            }
            jArr[i] = j2;
            j2 = (((j & j2) == 0 || (this.mPol[i + 1] & 1) != 1) && !((j2 & j) == 0 && (this.mPol[i + 1] & 1) == 0)) ? 1 : 0;
            i++;
        }
        int i3 = this.mDegree & 63;
        j = this.mPol[this.mLength - 1];
        long j3 = j2;
        int i4 = 1;
        while (i4 < i3) {
            if (((mBitmask[i4] & j) == 0 || (mBitmask[i4 - 1] & j3) == 0) && !((mBitmask[i4] & j) == 0 && (mBitmask[i4 - 1] & j3) == 0)) {
                j3 ^= mBitmask[i4];
            }
            i4++;
        }
        jArr[this.mLength - 1] = j3;
        return new GF2nONBElement((GF2nONBField) this.mField, jArr);
    }

    public GF2nElement square() {
        GF2nElement gF2nONBElement = new GF2nONBElement(this);
        gF2nONBElement.squareThis();
        return gF2nONBElement;
    }

    public GF2nElement squareRoot() {
        GF2nElement gF2nONBElement = new GF2nONBElement(this);
        gF2nONBElement.squareRootThis();
        return gF2nONBElement;
    }

    public void squareRootThis() {
        long[] element = getElement();
        int i = this.mLength - 1;
        int i2 = this.mBit - 1;
        long j = mBitmask[63];
        int i3 = i;
        Object obj = (element[0] & 1) != 0 ? 1 : null;
        while (i3 >= 0) {
            Object obj2 = (element[i3] & 1) != 0 ? 1 : null;
            element[i3] = element[i3] >>> 1;
            if (obj != null) {
                if (i3 == i) {
                    element[i3] = element[i3] ^ mBitmask[i2];
                } else {
                    element[i3] = element[i3] ^ j;
                }
            }
            i3--;
            obj = obj2;
        }
        assign(element);
    }

    public void squareThis() {
        Object obj;
        long[] element = getElement();
        int i = this.mLength - 1;
        int i2 = this.mBit - 1;
        long j = mBitmask[63];
        int i3 = 0;
        Object obj2 = (element[i] & mBitmask[i2]) != 0 ? 1 : null;
        while (i3 < i) {
            obj = (element[i3] & j) != 0 ? 1 : null;
            element[i3] = element[i3] << 1;
            if (obj2 != null) {
                element[i3] = element[i3] ^ 1;
            }
            i3++;
            obj2 = obj;
        }
        obj = (element[i] & mBitmask[i2]) != 0 ? 1 : null;
        element[i] = element[i] << 1;
        if (obj2 != null) {
            element[i] = element[i] ^ 1;
        }
        if (obj != null) {
            element[i] = element[i] ^ mBitmask[i2 + 1];
        }
        assign(element);
    }

    boolean testBit(int i) {
        return i >= 0 && i <= this.mDegree && (this.mPol[i >>> 6] & mBitmask[i & 63]) != 0;
    }

    public boolean testRightmostBit() {
        return (this.mPol[this.mLength + -1] & mBitmask[this.mBit + -1]) != 0;
    }

    public byte[] toByteArray() {
        int i = ((this.mDegree - 1) >> 3) + 1;
        byte[] bArr = new byte[i];
        for (int i2 = 0; i2 < i; i2++) {
            bArr[(i - i2) - 1] = (byte) ((int) ((this.mPol[i2 >>> 3] & (255 << ((i2 & 7) << 3))) >>> ((i2 & 7) << 3)));
        }
        return bArr;
    }

    public BigInteger toFlexiBigInt() {
        return new BigInteger(1, toByteArray());
    }

    public String toString() {
        return toString(16);
    }

    public String toString(int i) {
        String str = BuildConfig.FLAVOR;
        long[] element = getElement();
        int i2 = this.mBit;
        if (i == 2) {
            int i3 = i2 - 1;
            String str2 = str;
            while (i3 >= 0) {
                str = (element[element.length + -1] & (1 << i3)) == 0 ? str2 + TransactionInfo.VISA_TRANSACTIONTYPE_PURCHASE : str2 + TransactionInfo.VISA_TRANSACTIONTYPE_REFUND;
                i3--;
                str2 = str;
            }
            str = str2;
            for (i3 = element.length - 2; i3 >= 0; i3--) {
                for (i2 = 63; i2 >= 0; i2--) {
                    str = (element[i3] & mBitmask[i2]) == 0 ? str + TransactionInfo.VISA_TRANSACTIONTYPE_PURCHASE : str + TransactionInfo.VISA_TRANSACTIONTYPE_REFUND;
                }
            }
        } else if (i == 16) {
            char[] cArr = new char[]{LLVARUtil.EMPTY_STRING, LLVARUtil.PLAIN_TEXT, LLVARUtil.HEX_STRING, '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
            i2 = element.length - 1;
            while (i2 >= 0) {
                i2--;
                str = ((((((((((((((((str + cArr[((int) (element[i2] >>> 60)) & 15]) + cArr[((int) (element[i2] >>> 56)) & 15]) + cArr[((int) (element[i2] >>> 52)) & 15]) + cArr[((int) (element[i2] >>> 48)) & 15]) + cArr[((int) (element[i2] >>> 44)) & 15]) + cArr[((int) (element[i2] >>> 40)) & 15]) + cArr[((int) (element[i2] >>> 36)) & 15]) + cArr[((int) (element[i2] >>> 32)) & 15]) + cArr[((int) (element[i2] >>> 28)) & 15]) + cArr[((int) (element[i2] >>> 24)) & 15]) + cArr[((int) (element[i2] >>> 20)) & 15]) + cArr[((int) (element[i2] >>> 16)) & 15]) + cArr[((int) (element[i2] >>> 12)) & 15]) + cArr[((int) (element[i2] >>> 8)) & 15]) + cArr[((int) (element[i2] >>> 4)) & 15]) + cArr[((int) element[i2]) & 15]) + " ";
            }
        }
        return str;
    }

    public int trace() {
        int i;
        int i2 = 0;
        int i3 = this.mLength - 1;
        int i4 = 0;
        for (int i5 = 0; i5 < i3; i5++) {
            for (i = 0; i < MAXLONG; i++) {
                if ((this.mPol[i5] & mBitmask[i]) != 0) {
                    i4 ^= 1;
                }
            }
        }
        i = this.mBit;
        while (i2 < i) {
            if ((this.mPol[i3] & mBitmask[i2]) != 0) {
                i4 ^= 1;
            }
            i2++;
        }
        return i4;
    }
}
