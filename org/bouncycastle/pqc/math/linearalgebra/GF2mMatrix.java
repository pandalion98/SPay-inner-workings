package org.bouncycastle.pqc.math.linearalgebra;

import java.lang.reflect.Array;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class GF2mMatrix extends Matrix {
    protected GF2mField field;
    protected int[][] matrix;

    public GF2mMatrix(GF2mField gF2mField, byte[] bArr) {
        this.field = gF2mField;
        int i = 8;
        int i2 = 1;
        while (gF2mField.getDegree() > i) {
            i2++;
            i += 8;
        }
        if (bArr.length < 5) {
            throw new IllegalArgumentException(" Error: given array is not encoded matrix over GF(2^m)");
        }
        this.numRows = (((bArr[1] & GF2Field.MASK) << 8) ^ (((bArr[3] & GF2Field.MASK) << 24) ^ ((bArr[2] & GF2Field.MASK) << 16))) ^ (bArr[0] & GF2Field.MASK);
        i2 *= this.numRows;
        if (this.numRows <= 0 || (bArr.length - 4) % i2 != 0) {
            throw new IllegalArgumentException(" Error: given array is not encoded matrix over GF(2^m)");
        }
        this.numColumns = (bArr.length - 4) / i2;
        this.matrix = (int[][]) Array.newInstance(Integer.TYPE, new int[]{this.numRows, this.numColumns});
        int i3 = 4;
        for (i2 = 0; i2 < this.numRows; i2++) {
            int i4 = 0;
            while (i4 < this.numColumns) {
                int i5 = i3;
                i3 = 0;
                while (i3 < i) {
                    int[] iArr = this.matrix[i2];
                    int i6 = i5 + 1;
                    iArr[i4] = ((bArr[i5] & GF2Field.MASK) << i3) ^ iArr[i4];
                    i3 += 8;
                    i5 = i6;
                }
                if (this.field.isElementOfThisField(this.matrix[i2][i4])) {
                    i4++;
                    i3 = i5;
                } else {
                    throw new IllegalArgumentException(" Error: given array is not encoded matrix over GF(2^m)");
                }
            }
        }
    }

    protected GF2mMatrix(GF2mField gF2mField, int[][] iArr) {
        this.field = gF2mField;
        this.matrix = iArr;
        this.numRows = iArr.length;
        this.numColumns = iArr[0].length;
    }

    public GF2mMatrix(GF2mMatrix gF2mMatrix) {
        this.numRows = gF2mMatrix.numRows;
        this.numColumns = gF2mMatrix.numColumns;
        this.field = gF2mMatrix.field;
        this.matrix = new int[this.numRows][];
        for (int i = 0; i < this.numRows; i++) {
            this.matrix[i] = IntUtils.clone(gF2mMatrix.matrix[i]);
        }
    }

    private void addToRow(int[] iArr, int[] iArr2) {
        for (int length = iArr2.length - 1; length >= 0; length--) {
            iArr2[length] = this.field.add(iArr[length], iArr2[length]);
        }
    }

    private int[] multRowWithElement(int[] iArr, int i) {
        int[] iArr2 = new int[iArr.length];
        for (int length = iArr.length - 1; length >= 0; length--) {
            iArr2[length] = this.field.mult(iArr[length], i);
        }
        return iArr2;
    }

    private void multRowWithElementThis(int[] iArr, int i) {
        for (int length = iArr.length - 1; length >= 0; length--) {
            iArr[length] = this.field.mult(iArr[length], i);
        }
    }

    private static void swapColumns(int[][] iArr, int i, int i2) {
        int[] iArr2 = iArr[i];
        iArr[i] = iArr[i2];
        iArr[i2] = iArr2;
    }

    public Matrix computeInverse() {
        if (this.numRows != this.numColumns) {
            throw new ArithmeticException("Matrix is not invertible.");
        }
        int i;
        int[][] iArr = (int[][]) Array.newInstance(Integer.TYPE, new int[]{this.numRows, this.numRows});
        for (int i2 = this.numRows - 1; i2 >= 0; i2--) {
            iArr[i2] = IntUtils.clone(this.matrix[i2]);
        }
        int[][] iArr2 = (int[][]) Array.newInstance(Integer.TYPE, new int[]{this.numRows, this.numRows});
        for (i = this.numRows - 1; i >= 0; i--) {
            iArr2[i][i] = 1;
        }
        for (i = 0; i < this.numRows; i++) {
            int i3;
            if (iArr[i][i] == 0) {
                i3 = i + 1;
                Object obj = null;
                while (i3 < this.numRows) {
                    if (iArr[i3][i] != 0) {
                        swapColumns(iArr, i, i3);
                        swapColumns(iArr2, i, i3);
                        i3 = this.numRows;
                        obj = 1;
                    }
                    i3++;
                }
                if (obj == null) {
                    throw new ArithmeticException("Matrix is not invertible.");
                }
            }
            i3 = this.field.inverse(iArr[i][i]);
            multRowWithElementThis(iArr[i], i3);
            multRowWithElementThis(iArr2[i], i3);
            for (i3 = 0; i3 < this.numRows; i3++) {
                if (i3 != i) {
                    int i4 = iArr[i3][i];
                    if (i4 != 0) {
                        int[] multRowWithElement = multRowWithElement(iArr[i], i4);
                        int[] multRowWithElement2 = multRowWithElement(iArr2[i], i4);
                        addToRow(multRowWithElement, iArr[i3]);
                        addToRow(multRowWithElement2, iArr2[i3]);
                    }
                }
            }
        }
        return new GF2mMatrix(this.field, iArr2);
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof GF2mMatrix)) {
            return false;
        }
        GF2mMatrix gF2mMatrix = (GF2mMatrix) obj;
        if (!this.field.equals(gF2mMatrix.field) || gF2mMatrix.numRows != this.numColumns || gF2mMatrix.numColumns != this.numColumns) {
            return false;
        }
        for (int i = 0; i < this.numRows; i++) {
            for (int i2 = 0; i2 < this.numColumns; i2++) {
                if (this.matrix[i][i2] != gF2mMatrix.matrix[i][i2]) {
                    return false;
                }
            }
        }
        return true;
    }

    public byte[] getEncoded() {
        int i = 8;
        int i2 = 1;
        while (this.field.getDegree() > i) {
            i2++;
            i += 8;
        }
        byte[] bArr = new byte[((i2 * (this.numRows * this.numColumns)) + 4)];
        bArr[0] = (byte) (this.numRows & GF2Field.MASK);
        bArr[1] = (byte) ((this.numRows >>> 8) & GF2Field.MASK);
        bArr[2] = (byte) ((this.numRows >>> 16) & GF2Field.MASK);
        bArr[3] = (byte) ((this.numRows >>> 24) & GF2Field.MASK);
        int i3 = 4;
        for (i2 = 0; i2 < this.numRows; i2++) {
            int i4 = 0;
            while (i4 < this.numColumns) {
                int i5 = i3;
                i3 = 0;
                while (i3 < i) {
                    int i6 = i5 + 1;
                    bArr[i5] = (byte) (this.matrix[i2][i4] >>> i3);
                    i3 += 8;
                    i5 = i6;
                }
                i4++;
                i3 = i5;
            }
        }
        return bArr;
    }

    public int hashCode() {
        int hashCode = (((this.field.hashCode() * 31) + this.numRows) * 31) + this.numColumns;
        for (int i = 0; i < this.numRows; i++) {
            int i2 = 0;
            while (i2 < this.numColumns) {
                int i3 = this.matrix[i][i2] + (hashCode * 31);
                i2++;
                hashCode = i3;
            }
        }
        return hashCode;
    }

    public boolean isZero() {
        for (int i = 0; i < this.numRows; i++) {
            for (int i2 = 0; i2 < this.numColumns; i2++) {
                if (this.matrix[i][i2] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public Vector leftMultiply(Vector vector) {
        throw new RuntimeException("Not implemented.");
    }

    public Matrix rightMultiply(Matrix matrix) {
        throw new RuntimeException("Not implemented.");
    }

    public Matrix rightMultiply(Permutation permutation) {
        throw new RuntimeException("Not implemented.");
    }

    public Vector rightMultiply(Vector vector) {
        throw new RuntimeException("Not implemented.");
    }

    public String toString() {
        String str = this.numRows + " x " + this.numColumns + " Matrix over " + this.field.toString() + ": \n";
        for (int i = 0; i < this.numRows; i++) {
            String str2 = str;
            for (int i2 = 0; i2 < this.numColumns; i2++) {
                str2 = str2 + this.field.elementToStr(this.matrix[i][i2]) + " : ";
            }
            str = str2 + "\n";
        }
        return str;
    }
}
