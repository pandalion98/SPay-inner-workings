package org.bouncycastle.pqc.math.linearalgebra;

import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import com.google.android.gms.location.places.Place;
import java.lang.reflect.Array;
import java.security.SecureRandom;
import org.bouncycastle.asn1.eac.EACTags;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class GF2Matrix extends Matrix {
    private int length;
    private int[][] matrix;

    public GF2Matrix(int i, char c) {
        this(i, c, new SecureRandom());
    }

    public GF2Matrix(int i, char c, SecureRandom secureRandom) {
        if (i <= 0) {
            throw new ArithmeticException("Size of matrix is non-positive.");
        }
        switch (c) {
            case EACTags.CARDHOLDER_PUBLIC_KEY_TEMPLATE /*73*/:
                assignUnitMatrix(i);
            case EACTags.CERTIFICATE_HOLDER_AUTHORIZATION_TEMPLATE /*76*/:
                assignRandomLowerTriangularMatrix(i, secureRandom);
            case EACTags.COMMAND_TO_PERFORM /*82*/:
                assignRandomRegularMatrix(i, secureRandom);
            case Place.TYPE_SPA /*85*/:
                assignRandomUpperTriangularMatrix(i, secureRandom);
            case EACTags.PRIMARY_ACCOUNT_NUMBER /*90*/:
                assignZeroMatrix(i, i);
            default:
                throw new ArithmeticException("Unknown matrix type.");
        }
    }

    private GF2Matrix(int i, int i2) {
        if (i2 <= 0 || i <= 0) {
            throw new ArithmeticException("size of matrix is non-positive");
        }
        assignZeroMatrix(i, i2);
    }

    public GF2Matrix(int i, int[][] iArr) {
        int i2 = 0;
        if (iArr[0].length != ((i + 31) >> 5)) {
            throw new ArithmeticException("Int array does not match given number of columns.");
        }
        this.numColumns = i;
        this.numRows = iArr.length;
        this.length = iArr[0].length;
        int i3 = i & 31;
        i3 = i3 == 0 ? -1 : (1 << i3) - 1;
        while (i2 < this.numRows) {
            int[] iArr2 = iArr[i2];
            int i4 = this.length - 1;
            iArr2[i4] = iArr2[i4] & i3;
            i2++;
        }
        this.matrix = iArr;
    }

    public GF2Matrix(GF2Matrix gF2Matrix) {
        this.numColumns = gF2Matrix.getNumColumns();
        this.numRows = gF2Matrix.getNumRows();
        this.length = gF2Matrix.length;
        this.matrix = new int[gF2Matrix.matrix.length][];
        for (int i = 0; i < this.matrix.length; i++) {
            this.matrix[i] = IntUtils.clone(gF2Matrix.matrix[i]);
        }
    }

    public GF2Matrix(byte[] bArr) {
        if (bArr.length < 9) {
            throw new ArithmeticException("given array is not an encoded matrix over GF(2)");
        }
        this.numRows = LittleEndianConversions.OS2IP(bArr, 0);
        this.numColumns = LittleEndianConversions.OS2IP(bArr, 4);
        int i = ((this.numColumns + 7) >>> 3) * this.numRows;
        if (this.numRows <= 0 || i != bArr.length - 8) {
            throw new ArithmeticException("given array is not an encoded matrix over GF(2)");
        }
        this.length = (this.numColumns + 31) >>> 5;
        this.matrix = (int[][]) Array.newInstance(Integer.TYPE, new int[]{this.numRows, this.length});
        int i2 = this.numColumns >> 5;
        int i3 = this.numColumns & 31;
        int i4 = 8;
        for (i = 0; i < this.numRows; i++) {
            int i5 = 0;
            while (i5 < i2) {
                this.matrix[i][i5] = LittleEndianConversions.OS2IP(bArr, i4);
                i5++;
                i4 += 4;
            }
            i5 = 0;
            while (i5 < i3) {
                int[] iArr = this.matrix[i];
                int i6 = i4 + 1;
                iArr[i2] = ((bArr[i4] & GF2Field.MASK) << i5) ^ iArr[i2];
                i5 += 8;
                i4 = i6;
            }
        }
    }

    private static void addToRow(int[] iArr, int[] iArr2, int i) {
        for (int length = iArr2.length - 1; length >= i; length--) {
            iArr2[length] = iArr[length] ^ iArr2[length];
        }
    }

    private void assignRandomLowerTriangularMatrix(int i, SecureRandom secureRandom) {
        this.numRows = i;
        this.numColumns = i;
        this.length = (i + 31) >>> 5;
        this.matrix = (int[][]) Array.newInstance(Integer.TYPE, new int[]{this.numRows, this.length});
        for (int i2 = 0; i2 < this.numRows; i2++) {
            int i3 = i2 >>> 5;
            int i4 = i2 & 31;
            int i5 = 31 - i4;
            int i6 = 1 << i4;
            for (i4 = 0; i4 < i3; i4++) {
                this.matrix[i2][i4] = secureRandom.nextInt();
            }
            this.matrix[i2][i3] = (secureRandom.nextInt() >>> i5) | i6;
            for (i4 = i3 + 1; i4 < this.length; i4++) {
                this.matrix[i2][i4] = 0;
            }
        }
    }

    private void assignRandomRegularMatrix(int i, SecureRandom secureRandom) {
        this.numRows = i;
        this.numColumns = i;
        this.length = (i + 31) >>> 5;
        this.matrix = (int[][]) Array.newInstance(Integer.TYPE, new int[]{this.numRows, this.length});
        GF2Matrix gF2Matrix = (GF2Matrix) new GF2Matrix(i, Matrix.MATRIX_TYPE_RANDOM_LT, secureRandom).rightMultiply(new GF2Matrix(i, Matrix.MATRIX_TYPE_RANDOM_UT, secureRandom));
        int[] vector = new Permutation(i, secureRandom).getVector();
        for (int i2 = 0; i2 < i; i2++) {
            System.arraycopy(gF2Matrix.matrix[i2], 0, this.matrix[vector[i2]], 0, this.length);
        }
    }

    private void assignRandomUpperTriangularMatrix(int i, SecureRandom secureRandom) {
        this.numRows = i;
        this.numColumns = i;
        this.length = (i + 31) >>> 5;
        this.matrix = (int[][]) Array.newInstance(Integer.TYPE, new int[]{this.numRows, this.length});
        int i2 = i & 31;
        i2 = i2 == 0 ? -1 : (1 << i2) - 1;
        for (int i3 = 0; i3 < this.numRows; i3++) {
            int i4;
            int i5 = i3 >>> 5;
            int i6 = i3 & 31;
            int i7 = 1 << i6;
            for (i4 = 0; i4 < i5; i4++) {
                this.matrix[i3][i4] = 0;
            }
            this.matrix[i3][i5] = (secureRandom.nextInt() << i6) | i7;
            for (i4 = i5 + 1; i4 < this.length; i4++) {
                this.matrix[i3][i4] = secureRandom.nextInt();
            }
            int[] iArr = this.matrix[i3];
            i5 = this.length - 1;
            iArr[i5] = iArr[i5] & i2;
        }
    }

    private void assignUnitMatrix(int i) {
        int i2 = 0;
        this.numRows = i;
        this.numColumns = i;
        this.length = (i + 31) >>> 5;
        this.matrix = (int[][]) Array.newInstance(Integer.TYPE, new int[]{this.numRows, this.length});
        for (int i3 = 0; i3 < this.numRows; i3++) {
            for (int i4 = 0; i4 < this.length; i4++) {
                this.matrix[i3][i4] = 0;
            }
        }
        while (i2 < this.numRows) {
            this.matrix[i2][i2 >>> 5] = 1 << (i2 & 31);
            i2++;
        }
    }

    private void assignZeroMatrix(int i, int i2) {
        this.numRows = i;
        this.numColumns = i2;
        this.length = (i2 + 31) >>> 5;
        this.matrix = (int[][]) Array.newInstance(Integer.TYPE, new int[]{this.numRows, this.length});
        for (int i3 = 0; i3 < this.numRows; i3++) {
            for (int i4 = 0; i4 < this.length; i4++) {
                this.matrix[i3][i4] = 0;
            }
        }
    }

    public static GF2Matrix[] createRandomRegularMatrixAndItsInverse(int i, SecureRandom secureRandom) {
        int i2;
        GF2Matrix[] gF2MatrixArr = new GF2Matrix[2];
        int i3 = (i + 31) >> 5;
        GF2Matrix gF2Matrix = new GF2Matrix(i, Matrix.MATRIX_TYPE_RANDOM_LT, secureRandom);
        Matrix gF2Matrix2 = new GF2Matrix(i, Matrix.MATRIX_TYPE_RANDOM_UT, secureRandom);
        GF2Matrix gF2Matrix3 = (GF2Matrix) gF2Matrix.rightMultiply(gF2Matrix2);
        Permutation permutation = new Permutation(i, secureRandom);
        int[] vector = permutation.getVector();
        int[][] iArr = (int[][]) Array.newInstance(Integer.TYPE, new int[]{i, i3});
        for (i2 = 0; i2 < i; i2++) {
            System.arraycopy(gF2Matrix3.matrix[vector[i2]], 0, iArr[i2], 0, i3);
        }
        gF2MatrixArr[0] = new GF2Matrix(i, iArr);
        GF2Matrix gF2Matrix4 = new GF2Matrix(i, (char) PolynomialGF2mSmallM.RANDOM_IRREDUCIBLE_POLYNOMIAL);
        for (i2 = 0; i2 < i; i2++) {
            int i4;
            int i5 = i2 >>> 5;
            int i6 = 1 << (i2 & 31);
            for (i4 = i2 + 1; i4 < i; i4++) {
                int i7;
                if ((gF2Matrix.matrix[i4][i5] & i6) != 0) {
                    for (i7 = 0; i7 <= i5; i7++) {
                        int[] iArr2 = gF2Matrix4.matrix[i4];
                        iArr2[i7] = iArr2[i7] ^ gF2Matrix4.matrix[i2][i7];
                    }
                }
            }
        }
        GF2Matrix gF2Matrix5 = new GF2Matrix(i, (char) PolynomialGF2mSmallM.RANDOM_IRREDUCIBLE_POLYNOMIAL);
        for (int i8 = i - 1; i8 >= 0; i8--) {
            i4 = i8 >>> 5;
            i6 = 1 << (i8 & 31);
            for (i2 = i8 - 1; i2 >= 0; i2--) {
                if ((gF2Matrix2.matrix[i2][i4] & i6) != 0) {
                    for (i7 = i4; i7 < i3; i7++) {
                        iArr2 = gF2Matrix5.matrix[i2];
                        iArr2[i7] = iArr2[i7] ^ gF2Matrix5.matrix[i8][i7];
                    }
                }
            }
        }
        gF2MatrixArr[1] = (GF2Matrix) gF2Matrix5.rightMultiply(gF2Matrix4.rightMultiply(permutation));
        return gF2MatrixArr;
    }

    private static void swapRows(int[][] iArr, int i, int i2) {
        int[] iArr2 = iArr[i];
        iArr[i] = iArr[i2];
        iArr[i2] = iArr2;
    }

    public Matrix computeInverse() {
        if (this.numRows != this.numColumns) {
            throw new ArithmeticException("Matrix is not invertible.");
        }
        int i;
        int[][] iArr = (int[][]) Array.newInstance(Integer.TYPE, new int[]{this.numRows, this.length});
        for (int i2 = this.numRows - 1; i2 >= 0; i2--) {
            iArr[i2] = IntUtils.clone(this.matrix[i2]);
        }
        int[][] iArr2 = (int[][]) Array.newInstance(Integer.TYPE, new int[]{this.numRows, this.length});
        for (i = this.numRows - 1; i >= 0; i--) {
            iArr2[i][i >> 5] = 1 << (i & 31);
        }
        for (i = 0; i < this.numRows; i++) {
            int i3;
            int i4 = i >> 5;
            int i5 = 1 << (i & 31);
            if ((iArr[i][i4] & i5) == 0) {
                i3 = i + 1;
                int i6 = 0;
                while (i3 < this.numRows) {
                    if ((iArr[i3][i4] & i5) != 0) {
                        swapRows(iArr, i, i3);
                        swapRows(iArr2, i, i3);
                        i3 = this.numRows;
                        i6 = 1;
                    }
                    i3++;
                }
                if (i6 == 0) {
                    throw new ArithmeticException("Matrix is not invertible.");
                }
            }
            i3 = this.numRows - 1;
            while (i3 >= 0) {
                if (!(i3 == i || (iArr[i3][i4] & i5) == 0)) {
                    addToRow(iArr[i], iArr[i3], i4);
                    addToRow(iArr2[i], iArr2[i3], 0);
                }
                i3--;
            }
        }
        return new GF2Matrix(this.numColumns, iArr2);
    }

    public Matrix computeTranspose() {
        int[][] iArr = (int[][]) Array.newInstance(Integer.TYPE, new int[]{this.numColumns, (this.numRows + 31) >>> 5});
        for (int i = 0; i < this.numRows; i++) {
            for (int i2 = 0; i2 < this.numColumns; i2++) {
                int i3 = i2 & 31;
                int i4 = (this.matrix[i][i2 >>> 5] >>> i3) & 1;
                i3 = i >>> 5;
                int i5 = i & 31;
                if (i4 == 1) {
                    int[] iArr2 = iArr[i2];
                    iArr2[i3] = (1 << i5) | iArr2[i3];
                }
            }
        }
        return new GF2Matrix(this.numRows, iArr);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof GF2Matrix)) {
            return false;
        }
        GF2Matrix gF2Matrix = (GF2Matrix) obj;
        if (this.numRows != gF2Matrix.numRows || this.numColumns != gF2Matrix.numColumns || this.length != gF2Matrix.length) {
            return false;
        }
        for (int i = 0; i < this.numRows; i++) {
            if (!IntUtils.equals(this.matrix[i], gF2Matrix.matrix[i])) {
                return false;
            }
        }
        return true;
    }

    public GF2Matrix extendLeftCompactForm() {
        GF2Matrix gF2Matrix = new GF2Matrix(this.numRows, this.numColumns + this.numRows);
        int i = this.numColumns + (this.numRows - 1);
        int i2 = this.numRows - 1;
        while (i2 >= 0) {
            System.arraycopy(this.matrix[i2], 0, gF2Matrix.matrix[i2], 0, this.length);
            int[] iArr = gF2Matrix.matrix[i2];
            int i3 = i >> 5;
            iArr[i3] = iArr[i3] | (1 << (i & 31));
            i2--;
            i--;
        }
        return gF2Matrix;
    }

    public GF2Matrix extendRightCompactForm() {
        GF2Matrix gF2Matrix = new GF2Matrix(this.numRows, this.numRows + this.numColumns);
        int i = this.numRows >> 5;
        int i2 = this.numRows & 31;
        for (int i3 = this.numRows - 1; i3 >= 0; i3--) {
            int[] iArr = gF2Matrix.matrix[i3];
            int i4 = i3 >> 5;
            iArr[i4] = iArr[i4] | (1 << (i3 & 31));
            if (i2 != 0) {
                int i5;
                int[] iArr2;
                int i6 = 0;
                i4 = i;
                while (i6 < this.length - 1) {
                    i5 = this.matrix[i3][i6];
                    int[] iArr3 = gF2Matrix.matrix[i3];
                    int i7 = i4 + 1;
                    iArr3[i4] = iArr3[i4] | (i5 << i2);
                    iArr2 = gF2Matrix.matrix[i3];
                    iArr2[i7] = (i5 >>> (32 - i2)) | iArr2[i7];
                    i6++;
                    i4 = i7;
                }
                i6 = this.matrix[i3][this.length - 1];
                int[] iArr4 = gF2Matrix.matrix[i3];
                i5 = i4 + 1;
                iArr4[i4] = iArr4[i4] | (i6 << i2);
                if (i5 < gF2Matrix.length) {
                    iArr2 = gF2Matrix.matrix[i3];
                    iArr2[i5] = (i6 >>> (32 - i2)) | iArr2[i5];
                }
            } else {
                System.arraycopy(this.matrix[i3], 0, gF2Matrix.matrix[i3], i, this.length);
            }
        }
        return gF2Matrix;
    }

    public byte[] getEncoded() {
        byte[] bArr = new byte[((((this.numColumns + 7) >>> 3) * this.numRows) + 8)];
        LittleEndianConversions.I2OSP(this.numRows, bArr, 0);
        LittleEndianConversions.I2OSP(this.numColumns, bArr, 4);
        int i = this.numColumns >>> 5;
        int i2 = this.numColumns & 31;
        int i3 = 8;
        for (int i4 = 0; i4 < this.numRows; i4++) {
            int i5 = 0;
            while (i5 < i) {
                LittleEndianConversions.I2OSP(this.matrix[i4][i5], bArr, i3);
                i5++;
                i3 += 4;
            }
            i5 = 0;
            while (i5 < i2) {
                int i6 = i3 + 1;
                bArr[i3] = (byte) ((this.matrix[i4][i] >>> i5) & GF2Field.MASK);
                i5 += 8;
                i3 = i6;
            }
        }
        return bArr;
    }

    public double getHammingWeight() {
        double d = 0.0d;
        double d2 = 0.0d;
        int i = this.numColumns & 31;
        int i2 = i == 0 ? this.length : this.length - 1;
        for (int i3 = 0; i3 < this.numRows; i3++) {
            double d3 = d;
            d = d2;
            for (int i4 = 0; i4 < i2; i4++) {
                int i5 = this.matrix[i3][i4];
                for (int i6 = 0; i6 < 32; i6++) {
                    d3 += (double) ((i5 >>> i6) & 1);
                    d += 1.0d;
                }
            }
            int i7 = this.matrix[i3][this.length - 1];
            d2 = d;
            d = d3;
            int i8 = 0;
            while (i8 < i) {
                double d4 = ((double) ((i7 >>> i8) & 1)) + d;
                i8++;
                d2 = 1.0d + d2;
                d = d4;
            }
        }
        return d / d2;
    }

    public int[][] getIntArray() {
        return this.matrix;
    }

    public GF2Matrix getLeftSubMatrix() {
        if (this.numColumns <= this.numRows) {
            throw new ArithmeticException("empty submatrix");
        }
        int i = (this.numRows + 31) >> 5;
        int[][] iArr = (int[][]) Array.newInstance(Integer.TYPE, new int[]{this.numRows, i});
        int i2 = (1 << (this.numRows & 31)) - 1;
        if (i2 == 0) {
            i2 = -1;
        }
        for (int i3 = this.numRows - 1; i3 >= 0; i3--) {
            System.arraycopy(this.matrix[i3], 0, iArr[i3], 0, i);
            int[] iArr2 = iArr[i3];
            int i4 = i - 1;
            iArr2[i4] = iArr2[i4] & i2;
        }
        return new GF2Matrix(this.numRows, iArr);
    }

    public int getLength() {
        return this.length;
    }

    public GF2Matrix getRightSubMatrix() {
        if (this.numColumns <= this.numRows) {
            throw new ArithmeticException("empty submatrix");
        }
        int i = this.numRows >> 5;
        int i2 = this.numRows & 31;
        GF2Matrix gF2Matrix = new GF2Matrix(this.numRows, this.numColumns - this.numRows);
        for (int i3 = this.numRows - 1; i3 >= 0; i3--) {
            if (i2 != 0) {
                int i4 = 0;
                int i5 = i;
                while (i4 < gF2Matrix.length - 1) {
                    int i6 = i5 + 1;
                    gF2Matrix.matrix[i3][i4] = (this.matrix[i3][i5] >>> i2) | (this.matrix[i3][i6] << (32 - i2));
                    i4++;
                    i5 = i6;
                }
                int i7 = i5 + 1;
                gF2Matrix.matrix[i3][gF2Matrix.length - 1] = this.matrix[i3][i5] >>> i2;
                if (i7 < this.length) {
                    int[] iArr = gF2Matrix.matrix[i3];
                    i5 = gF2Matrix.length - 1;
                    iArr[i5] = iArr[i5] | (this.matrix[i3][i7] << (32 - i2));
                }
            } else {
                System.arraycopy(this.matrix[i3], i, gF2Matrix.matrix[i3], 0, gF2Matrix.length);
            }
        }
        return gF2Matrix;
    }

    public int[] getRow(int i) {
        return this.matrix[i];
    }

    public int hashCode() {
        int i = this.length + (((this.numRows * 31) + this.numColumns) * 31);
        for (int i2 = 0; i2 < this.numRows; i2++) {
            i = (i * 31) + this.matrix[i2].hashCode();
        }
        return i;
    }

    public boolean isZero() {
        for (int i = 0; i < this.numRows; i++) {
            for (int i2 = 0; i2 < this.length; i2++) {
                if (this.matrix[i][i2] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public Matrix leftMultiply(Permutation permutation) {
        int[] vector = permutation.getVector();
        if (vector.length != this.numRows) {
            throw new ArithmeticException("length mismatch");
        }
        int[][] iArr = new int[this.numRows][];
        for (int i = this.numRows - 1; i >= 0; i--) {
            iArr[i] = IntUtils.clone(this.matrix[vector[i]]);
        }
        return new GF2Matrix(this.numRows, iArr);
    }

    public Vector leftMultiply(Vector vector) {
        int i = 1;
        if (!(vector instanceof GF2Vector)) {
            throw new ArithmeticException("vector is not defined over GF(2)");
        } else if (vector.length != this.numRows) {
            throw new ArithmeticException("length mismatch");
        } else {
            int i2;
            int[] vecArray = ((GF2Vector) vector).getVecArray();
            int[] iArr = new int[this.length];
            int i3 = this.numRows >> 5;
            int i4 = 1 << (this.numRows & 31);
            int i5 = 0;
            int i6 = 0;
            while (i5 < i3) {
                i2 = i6;
                i6 = 1;
                do {
                    if ((vecArray[i5] & i6) != 0) {
                        for (int i7 = 0; i7 < this.length; i7++) {
                            iArr[i7] = iArr[i7] ^ this.matrix[i2][i7];
                        }
                    }
                    i2++;
                    i6 <<= 1;
                } while (i6 != 0);
                i5++;
                i6 = i2;
            }
            i2 = i6;
            while (i != i4) {
                if ((vecArray[i3] & i) != 0) {
                    for (i6 = 0; i6 < this.length; i6++) {
                        iArr[i6] = iArr[i6] ^ this.matrix[i2][i6];
                    }
                }
                i <<= 1;
                i2++;
            }
            return new GF2Vector(iArr, this.numColumns);
        }
    }

    public Vector leftMultiplyLeftCompactForm(Vector vector) {
        if (!(vector instanceof GF2Vector)) {
            throw new ArithmeticException("vector is not defined over GF(2)");
        } else if (vector.length != this.numRows) {
            throw new ArithmeticException("length mismatch");
        } else {
            int i;
            int i2;
            int[] vecArray = ((GF2Vector) vector).getVecArray();
            int[] iArr = new int[(((this.numRows + this.numColumns) + 31) >>> 5)];
            int i3 = this.numRows >>> 5;
            int i4 = 0;
            int i5 = 0;
            while (i4 < i3) {
                i = i5;
                i5 = 1;
                do {
                    if ((vecArray[i4] & i5) != 0) {
                        for (i2 = 0; i2 < this.length; i2++) {
                            iArr[i2] = iArr[i2] ^ this.matrix[i][i2];
                        }
                        i2 = (this.numColumns + i) >>> 5;
                        iArr[i2] = (1 << ((this.numColumns + i) & 31)) | iArr[i2];
                    }
                    i++;
                    i5 <<= 1;
                } while (i5 != 0);
                i4++;
                i5 = i;
            }
            i4 = 1 << (this.numRows & 31);
            i2 = i5;
            for (i = 1; i != i4; i <<= 1) {
                if ((vecArray[i3] & i) != 0) {
                    for (i5 = 0; i5 < this.length; i5++) {
                        iArr[i5] = iArr[i5] ^ this.matrix[i2][i5];
                    }
                    i5 = (this.numColumns + i2) >>> 5;
                    iArr[i5] = (1 << ((this.numColumns + i2) & 31)) | iArr[i5];
                }
                i2++;
            }
            return new GF2Vector(iArr, this.numRows + this.numColumns);
        }
    }

    public Matrix rightMultiply(Matrix matrix) {
        if (!(matrix instanceof GF2Matrix)) {
            throw new ArithmeticException("matrix is not defined over GF(2)");
        } else if (matrix.numRows != this.numColumns) {
            throw new ArithmeticException("length mismatch");
        } else {
            GF2Matrix gF2Matrix = (GF2Matrix) matrix;
            Matrix gF2Matrix2 = new GF2Matrix(this.numRows, matrix.numColumns);
            int i = this.numColumns & 31;
            int i2 = i == 0 ? this.length : this.length - 1;
            for (int i3 = 0; i3 < this.numRows; i3++) {
                int i4;
                int i5;
                int i6 = 0;
                int i7 = 0;
                while (i7 < i2) {
                    int i8 = this.matrix[i3][i7];
                    i4 = i6;
                    for (i6 = 0; i6 < 32; i6++) {
                        if (((1 << i6) & i8) != 0) {
                            for (i5 = 0; i5 < gF2Matrix.length; i5++) {
                                int[] iArr = gF2Matrix2.matrix[i3];
                                iArr[i5] = iArr[i5] ^ gF2Matrix.matrix[i4][i5];
                            }
                        }
                        i4++;
                    }
                    i7++;
                    i6 = i4;
                }
                i7 = this.matrix[i3][this.length - 1];
                i4 = i6;
                for (i6 = 0; i6 < i; i6++) {
                    if (((1 << i6) & i7) != 0) {
                        for (i5 = 0; i5 < gF2Matrix.length; i5++) {
                            int[] iArr2 = gF2Matrix2.matrix[i3];
                            iArr2[i5] = iArr2[i5] ^ gF2Matrix.matrix[i4][i5];
                        }
                    }
                    i4++;
                }
            }
            return gF2Matrix2;
        }
    }

    public Matrix rightMultiply(Permutation permutation) {
        int[] vector = permutation.getVector();
        if (vector.length != this.numColumns) {
            throw new ArithmeticException("length mismatch");
        }
        Matrix gF2Matrix = new GF2Matrix(this.numRows, this.numColumns);
        for (int i = this.numColumns - 1; i >= 0; i--) {
            int i2 = i >>> 5;
            int i3 = i & 31;
            int i4 = vector[i] >>> 5;
            int i5 = vector[i] & 31;
            for (int i6 = this.numRows - 1; i6 >= 0; i6--) {
                int[] iArr = gF2Matrix.matrix[i6];
                iArr[i2] = iArr[i2] | (((this.matrix[i6][i4] >>> i5) & 1) << i3);
            }
        }
        return gF2Matrix;
    }

    public Vector rightMultiply(Vector vector) {
        if (!(vector instanceof GF2Vector)) {
            throw new ArithmeticException("vector is not defined over GF(2)");
        } else if (vector.length != this.numColumns) {
            throw new ArithmeticException("length mismatch");
        } else {
            int[] vecArray = ((GF2Vector) vector).getVecArray();
            int[] iArr = new int[((this.numRows + 31) >>> 5)];
            for (int i = 0; i < this.numRows; i++) {
                int i2;
                int i3 = 0;
                for (i2 = 0; i2 < this.length; i2++) {
                    i3 ^= this.matrix[i][i2] & vecArray[i2];
                }
                int i4 = 0;
                for (i2 = 0; i2 < 32; i2++) {
                    i4 ^= (i3 >>> i2) & 1;
                }
                if (i4 == 1) {
                    i2 = i >>> 5;
                    iArr[i2] = iArr[i2] | (1 << (i & 31));
                }
            }
            return new GF2Vector(iArr, this.numRows);
        }
    }

    public Vector rightMultiplyRightCompactForm(Vector vector) {
        if (!(vector instanceof GF2Vector)) {
            throw new ArithmeticException("vector is not defined over GF(2)");
        } else if (vector.length != this.numColumns + this.numRows) {
            throw new ArithmeticException("length mismatch");
        } else {
            int[] vecArray = ((GF2Vector) vector).getVecArray();
            int[] iArr = new int[((this.numRows + 31) >>> 5)];
            int i = this.numRows >> 5;
            int i2 = this.numRows & 31;
            for (int i3 = 0; i3 < this.numRows; i3++) {
                int i4;
                int i5;
                int i6 = (vecArray[i3 >> 5] >>> (i3 & 31)) & 1;
                if (i2 != 0) {
                    int i7;
                    i4 = i;
                    i5 = i6;
                    i6 = 0;
                    while (i6 < this.length - 1) {
                        i7 = i4 + 1;
                        i6++;
                        i5 = (((vecArray[i4] >>> i2) | (vecArray[i7] << (32 - i2))) & this.matrix[i3][i6]) ^ i5;
                        i4 = i7;
                    }
                    i7 = i4 + 1;
                    i6 = vecArray[i4] >>> i2;
                    if (i7 < vecArray.length) {
                        i6 |= vecArray[i7] << (32 - i2);
                    }
                    i5 ^= i6 & this.matrix[i3][this.length - 1];
                } else {
                    i4 = i;
                    i5 = i6;
                    i6 = 0;
                    while (i6 < this.length) {
                        int i8 = this.matrix[i3][i6];
                        i6++;
                        i5 = (vecArray[i4] & i8) ^ i5;
                        i4++;
                    }
                }
                i4 = 0;
                for (i6 = 0; i6 < 32; i6++) {
                    i4 ^= i5 & 1;
                    i5 >>>= 1;
                }
                if (i4 == 1) {
                    i6 = i3 >> 5;
                    iArr[i6] = iArr[i6] | (1 << (i3 & 31));
                }
            }
            return new GF2Vector(iArr, this.numRows);
        }
    }

    public String toString() {
        int i = this.numColumns & 31;
        int i2 = i == 0 ? this.length : this.length - 1;
        StringBuffer stringBuffer = new StringBuffer();
        for (int i3 = 0; i3 < this.numRows; i3++) {
            int i4;
            int i5;
            stringBuffer.append(i3 + ": ");
            for (i4 = 0; i4 < i2; i4++) {
                int i6 = this.matrix[i3][i4];
                for (i5 = 0; i5 < 32; i5++) {
                    if (((i6 >>> i5) & 1) == 0) {
                        stringBuffer.append(LLVARUtil.EMPTY_STRING);
                    } else {
                        stringBuffer.append(LLVARUtil.PLAIN_TEXT);
                    }
                }
                stringBuffer.append(' ');
            }
            i4 = this.matrix[i3][this.length - 1];
            for (i5 = 0; i5 < i; i5++) {
                if (((i4 >>> i5) & 1) == 0) {
                    stringBuffer.append(LLVARUtil.EMPTY_STRING);
                } else {
                    stringBuffer.append(LLVARUtil.PLAIN_TEXT);
                }
            }
            stringBuffer.append('\n');
        }
        return stringBuffer.toString();
    }
}
