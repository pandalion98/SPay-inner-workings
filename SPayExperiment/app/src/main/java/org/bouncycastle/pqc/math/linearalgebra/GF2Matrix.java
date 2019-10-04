/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.ArithmeticException
 *  java.lang.Class
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.lang.System
 *  java.lang.reflect.Array
 *  java.security.SecureRandom
 */
package org.bouncycastle.pqc.math.linearalgebra;

import java.lang.reflect.Array;
import java.security.SecureRandom;
import org.bouncycastle.pqc.math.linearalgebra.GF2Vector;
import org.bouncycastle.pqc.math.linearalgebra.IntUtils;
import org.bouncycastle.pqc.math.linearalgebra.LittleEndianConversions;
import org.bouncycastle.pqc.math.linearalgebra.Matrix;
import org.bouncycastle.pqc.math.linearalgebra.Permutation;
import org.bouncycastle.pqc.math.linearalgebra.Vector;

public class GF2Matrix
extends Matrix {
    private int length;
    private int[][] matrix;

    public GF2Matrix(int n, char c) {
        this(n, c, new SecureRandom());
    }

    public GF2Matrix(int n, char c, SecureRandom secureRandom) {
        if (n <= 0) {
            throw new ArithmeticException("Size of matrix is non-positive.");
        }
        switch (c) {
            default: {
                throw new ArithmeticException("Unknown matrix type.");
            }
            case 'Z': {
                this.assignZeroMatrix(n, n);
                return;
            }
            case 'I': {
                this.assignUnitMatrix(n);
                return;
            }
            case 'L': {
                this.assignRandomLowerTriangularMatrix(n, secureRandom);
                return;
            }
            case 'U': {
                this.assignRandomUpperTriangularMatrix(n, secureRandom);
                return;
            }
            case 'R': 
        }
        this.assignRandomRegularMatrix(n, secureRandom);
    }

    private GF2Matrix(int n, int n2) {
        if (n2 <= 0 || n <= 0) {
            throw new ArithmeticException("size of matrix is non-positive");
        }
        this.assignZeroMatrix(n, n2);
    }

    /*
     * Enabled aggressive block sorting
     */
    public GF2Matrix(int n, int[][] arrn) {
        int n2;
        int n3 = 0;
        if (arrn[0].length != n + 31 >> 5) {
            throw new ArithmeticException("Int array does not match given number of columns.");
        }
        this.numColumns = n;
        this.numRows = arrn.length;
        this.length = arrn[0].length;
        int n4 = n & 31;
        if (n4 == 0) {
            n2 = -1;
        } else {
            n2 = -1 + (1 << n4);
            n3 = 0;
        }
        do {
            if (n3 >= this.numRows) {
                this.matrix = arrn;
                return;
            }
            int[] arrn2 = arrn[n3];
            int n5 = -1 + this.length;
            arrn2[n5] = n2 & arrn2[n5];
            ++n3;
        } while (true);
    }

    public GF2Matrix(GF2Matrix gF2Matrix) {
        this.numColumns = gF2Matrix.getNumColumns();
        this.numRows = gF2Matrix.getNumRows();
        this.length = gF2Matrix.length;
        this.matrix = new int[gF2Matrix.matrix.length][];
        for (int i = 0; i < this.matrix.length; ++i) {
            this.matrix[i] = IntUtils.clone(gF2Matrix.matrix[i]);
        }
    }

    public GF2Matrix(byte[] arrby) {
        if (arrby.length < 9) {
            throw new ArithmeticException("given array is not an encoded matrix over GF(2)");
        }
        this.numRows = LittleEndianConversions.OS2IP(arrby, 0);
        this.numColumns = LittleEndianConversions.OS2IP(arrby, 4);
        int n = (7 + this.numColumns >>> 3) * this.numRows;
        if (this.numRows <= 0 || n != -8 + arrby.length) {
            throw new ArithmeticException("given array is not an encoded matrix over GF(2)");
        }
        this.length = 31 + this.numColumns >>> 5;
        int[] arrn = new int[]{this.numRows, this.length};
        this.matrix = (int[][])Array.newInstance((Class)Integer.TYPE, (int[])arrn);
        int n2 = this.numColumns >> 5;
        int n3 = 31 & this.numColumns;
        int n4 = 8;
        for (int i = 0; i < this.numRows; ++i) {
            int n5 = 0;
            while (n5 < n2) {
                this.matrix[i][n5] = LittleEndianConversions.OS2IP(arrby, n4);
                ++n5;
                n4 += 4;
            }
            for (int j = 0; j < n3; j += 8) {
                int[] arrn2 = this.matrix[i];
                int n6 = arrn2[n2];
                int n7 = n4 + 1;
                arrn2[n2] = n6 ^ (255 & arrby[n4]) << j;
                n4 = n7;
            }
        }
    }

    private static void addToRow(int[] arrn, int[] arrn2, int n) {
        for (int i = -1 + arrn2.length; i >= n; --i) {
            arrn2[i] = arrn[i] ^ arrn2[i];
        }
    }

    private void assignRandomLowerTriangularMatrix(int n, SecureRandom secureRandom) {
        this.numRows = n;
        this.numColumns = n;
        this.length = n + 31 >>> 5;
        int[] arrn = new int[]{this.numRows, this.length};
        this.matrix = (int[][])Array.newInstance((Class)Integer.TYPE, (int[])arrn);
        for (int i = 0; i < this.numRows; ++i) {
            int n2 = i >>> 5;
            int n3 = i & 31;
            int n4 = 31 - n3;
            int n5 = 1 << n3;
            for (int j = 0; j < n2; ++j) {
                this.matrix[i][j] = secureRandom.nextInt();
            }
            this.matrix[i][n2] = n5 | secureRandom.nextInt() >>> n4;
            for (int j = n2 + 1; j < this.length; ++j) {
                this.matrix[i][j] = 0;
            }
        }
    }

    private void assignRandomRegularMatrix(int n, SecureRandom secureRandom) {
        this.numRows = n;
        this.numColumns = n;
        this.length = n + 31 >>> 5;
        int[] arrn = new int[]{this.numRows, this.length};
        this.matrix = (int[][])Array.newInstance((Class)Integer.TYPE, (int[])arrn);
        GF2Matrix gF2Matrix = (GF2Matrix)new GF2Matrix(n, 'L', secureRandom).rightMultiply(new GF2Matrix(n, 'U', secureRandom));
        int[] arrn2 = new Permutation(n, secureRandom).getVector();
        for (int i = 0; i < n; ++i) {
            System.arraycopy((Object)gF2Matrix.matrix[i], (int)0, (Object)this.matrix[arrn2[i]], (int)0, (int)this.length);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void assignRandomUpperTriangularMatrix(int n, SecureRandom secureRandom) {
        this.numRows = n;
        this.numColumns = n;
        this.length = n + 31 >>> 5;
        int[] arrn = new int[]{this.numRows, this.length};
        this.matrix = (int[][])Array.newInstance((Class)Integer.TYPE, (int[])arrn);
        int n2 = n & 31;
        int n3 = n2 == 0 ? -1 : -1 + (1 << n2);
        int n4 = 0;
        while (n4 < this.numRows) {
            int n5 = n4 >>> 5;
            int n6 = n4 & 31;
            int n7 = 1 << n6;
            for (int i = 0; i < n5; ++i) {
                this.matrix[n4][i] = 0;
            }
            this.matrix[n4][n5] = n7 | secureRandom.nextInt() << n6;
            for (int i = n5 + 1; i < this.length; ++i) {
                this.matrix[n4][i] = secureRandom.nextInt();
            }
            int[] arrn2 = this.matrix[n4];
            int n8 = -1 + this.length;
            arrn2[n8] = n3 & arrn2[n8];
            ++n4;
        }
        return;
    }

    private void assignUnitMatrix(int n) {
        this.numRows = n;
        this.numColumns = n;
        this.length = n + 31 >>> 5;
        int[] arrn = new int[]{this.numRows, this.length};
        this.matrix = (int[][])Array.newInstance((Class)Integer.TYPE, (int[])arrn);
        int n2 = 0;
        do {
            int n3 = this.numRows;
            if (n2 >= n3) break;
            for (int i = 0; i < this.length; ++i) {
                this.matrix[n2][i] = 0;
            }
            ++n2;
        } while (true);
        for (int i = 0; i < this.numRows; ++i) {
            int n4 = i & 31;
            this.matrix[i][i >>> 5] = 1 << n4;
        }
    }

    private void assignZeroMatrix(int n, int n2) {
        this.numRows = n;
        this.numColumns = n2;
        this.length = n2 + 31 >>> 5;
        int[] arrn = new int[]{this.numRows, this.length};
        this.matrix = (int[][])Array.newInstance((Class)Integer.TYPE, (int[])arrn);
        for (int i = 0; i < this.numRows; ++i) {
            for (int j = 0; j < this.length; ++j) {
                this.matrix[i][j] = 0;
            }
        }
    }

    public static GF2Matrix[] createRandomRegularMatrixAndItsInverse(int n, SecureRandom secureRandom) {
        GF2Matrix[] arrgF2Matrix = new GF2Matrix[2];
        int n2 = n + 31 >> 5;
        GF2Matrix gF2Matrix = new GF2Matrix(n, 'L', secureRandom);
        GF2Matrix gF2Matrix2 = new GF2Matrix(n, 'U', secureRandom);
        GF2Matrix gF2Matrix3 = (GF2Matrix)gF2Matrix.rightMultiply(gF2Matrix2);
        Permutation permutation = new Permutation(n, secureRandom);
        int[] arrn = permutation.getVector();
        int[] arrn2 = new int[]{n, n2};
        int[][] arrn3 = (int[][])Array.newInstance((Class)Integer.TYPE, (int[])arrn2);
        for (int i = 0; i < n; ++i) {
            System.arraycopy((Object)gF2Matrix3.matrix[arrn[i]], (int)0, (Object)arrn3[i], (int)0, (int)n2);
        }
        arrgF2Matrix[0] = new GF2Matrix(n, arrn3);
        GF2Matrix gF2Matrix4 = new GF2Matrix(n, 'I');
        for (int i = 0; i < n; ++i) {
            int n3 = i & 31;
            int n4 = i >>> 5;
            int n5 = 1 << n3;
            for (int j = i + 1; j < n; ++j) {
                if ((n5 & gF2Matrix.matrix[j][n4]) == 0) continue;
                for (int k = 0; k <= n4; ++k) {
                    int[] arrn4 = gF2Matrix4.matrix[j];
                    arrn4[k] = arrn4[k] ^ gF2Matrix4.matrix[i][k];
                }
            }
        }
        GF2Matrix gF2Matrix5 = new GF2Matrix(n, 'I');
        for (int i = n - 1; i >= 0; --i) {
            int n6 = i & 31;
            int n7 = i >>> 5;
            int n8 = 1 << n6;
            for (int j = i - 1; j >= 0; --j) {
                if ((n8 & gF2Matrix2.matrix[j][n7]) == 0) continue;
                for (int k = n7; k < n2; ++k) {
                    int[] arrn5 = gF2Matrix5.matrix[j];
                    arrn5[k] = arrn5[k] ^ gF2Matrix5.matrix[i][k];
                }
            }
        }
        arrgF2Matrix[1] = (GF2Matrix)gF2Matrix5.rightMultiply(gF2Matrix4.rightMultiply(permutation));
        return arrgF2Matrix;
    }

    private static void swapRows(int[][] arrn, int n, int n2) {
        int[] arrn2 = arrn[n];
        arrn[n] = arrn[n2];
        arrn[n2] = arrn2;
    }

    @Override
    public Matrix computeInverse() {
        if (this.numRows != this.numColumns) {
            throw new ArithmeticException("Matrix is not invertible.");
        }
        int[] arrn = new int[]{this.numRows, this.length};
        int[][] arrn2 = (int[][])Array.newInstance((Class)Integer.TYPE, (int[])arrn);
        for (int i = -1 + this.numRows; i >= 0; --i) {
            arrn2[i] = IntUtils.clone(this.matrix[i]);
        }
        int[] arrn3 = new int[]{this.numRows, this.length};
        int[][] arrn4 = (int[][])Array.newInstance((Class)Integer.TYPE, (int[])arrn3);
        for (int i = -1 + this.numRows; i >= 0; --i) {
            int n = i >> 5;
            int n2 = i & 31;
            arrn4[i][n] = 1 << n2;
        }
        for (int i = 0; i < this.numRows; ++i) {
            int n = 1 << (i & 31);
            int n3 = i >> 5;
            if ((n & arrn2[i][n3]) == 0) {
                boolean bl = false;
                for (int j = i + 1; j < this.numRows; ++j) {
                    if ((n & arrn2[j][n3]) == 0) continue;
                    GF2Matrix.swapRows(arrn2, i, j);
                    GF2Matrix.swapRows(arrn4, i, j);
                    j = this.numRows;
                    bl = true;
                }
                if (!bl) {
                    throw new ArithmeticException("Matrix is not invertible.");
                }
            }
            for (int j = -1 + this.numRows; j >= 0; --j) {
                if (j == i || (n & arrn2[j][n3]) == 0) continue;
                GF2Matrix.addToRow(arrn2[i], arrn2[j], n3);
                GF2Matrix.addToRow(arrn4[i], arrn4[j], 0);
            }
        }
        return new GF2Matrix(this.numColumns, arrn4);
    }

    public Matrix computeTranspose() {
        int[] arrn = new int[]{this.numColumns, 31 + this.numRows >>> 5};
        int[][] arrn2 = (int[][])Array.newInstance((Class)Integer.TYPE, (int[])arrn);
        for (int i = 0; i < this.numRows; ++i) {
            for (int j = 0; j < this.numColumns; ++j) {
                int n = j >>> 5;
                int n2 = j & 31;
                int n3 = 1 & this.matrix[i][n] >>> n2;
                int n4 = i >>> 5;
                int n5 = i & 31;
                if (n3 != 1) continue;
                int[] arrn3 = arrn2[j];
                arrn3[n4] = arrn3[n4] | 1 << n5;
            }
        }
        return new GF2Matrix(this.numRows, arrn2);
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        if (object instanceof GF2Matrix) {
            GF2Matrix gF2Matrix = (GF2Matrix)object;
            if (this.numRows == gF2Matrix.numRows && this.numColumns == gF2Matrix.numColumns && this.length == gF2Matrix.length) {
                int n = 0;
                do {
                    if (n >= this.numRows) {
                        return true;
                    }
                    if (!IntUtils.equals(this.matrix[n], gF2Matrix.matrix[n])) break;
                    ++n;
                } while (true);
            }
        }
        return false;
    }

    public GF2Matrix extendLeftCompactForm() {
        int n = this.numColumns + this.numRows;
        GF2Matrix gF2Matrix = new GF2Matrix(this.numRows, n);
        int n2 = -1 + this.numRows + this.numColumns;
        int n3 = -1 + this.numRows;
        while (n3 >= 0) {
            System.arraycopy((Object)this.matrix[n3], (int)0, (Object)gF2Matrix.matrix[n3], (int)0, (int)this.length);
            int[] arrn = gF2Matrix.matrix[n3];
            int n4 = n2 >> 5;
            arrn[n4] = arrn[n4] | 1 << (n2 & 31);
            --n3;
            --n2;
        }
        return gF2Matrix;
    }

    /*
     * Enabled aggressive block sorting
     */
    public GF2Matrix extendRightCompactForm() {
        GF2Matrix gF2Matrix = new GF2Matrix(this.numRows, this.numRows + this.numColumns);
        int n = this.numRows >> 5;
        int n2 = 31 & this.numRows;
        int n3 = -1 + this.numRows;
        while (n3 >= 0) {
            int[] arrn = gF2Matrix.matrix[n3];
            int n4 = n3 >> 5;
            arrn[n4] = arrn[n4] | 1 << (n3 & 31);
            if (n2 != 0) {
                int n5 = n;
                for (int i = 0; i < -1 + this.length; ++i) {
                    int n6 = this.matrix[n3][i];
                    int[] arrn2 = gF2Matrix.matrix[n3];
                    int n7 = n5 + 1;
                    arrn2[n5] = arrn2[n5] | n6 << n2;
                    int[] arrn3 = gF2Matrix.matrix[n3];
                    arrn3[n7] = arrn3[n7] | n6 >>> 32 - n2;
                    n5 = n7;
                }
                int n8 = this.matrix[n3][-1 + this.length];
                int[] arrn4 = gF2Matrix.matrix[n3];
                int n9 = n5 + 1;
                arrn4[n5] = arrn4[n5] | n8 << n2;
                if (n9 < gF2Matrix.length) {
                    int[] arrn5 = gF2Matrix.matrix[n3];
                    arrn5[n9] = arrn5[n9] | n8 >>> 32 - n2;
                }
            } else {
                System.arraycopy((Object)this.matrix[n3], (int)0, (Object)gF2Matrix.matrix[n3], (int)n, (int)this.length);
            }
            --n3;
        }
        return gF2Matrix;
    }

    @Override
    public byte[] getEncoded() {
        byte[] arrby = new byte[8 + (7 + this.numColumns >>> 3) * this.numRows];
        LittleEndianConversions.I2OSP(this.numRows, arrby, 0);
        LittleEndianConversions.I2OSP(this.numColumns, arrby, 4);
        int n = this.numColumns >>> 5;
        int n2 = 31 & this.numColumns;
        int n3 = 8;
        for (int i = 0; i < this.numRows; ++i) {
            int n4 = 0;
            while (n4 < n) {
                LittleEndianConversions.I2OSP(this.matrix[i][n4], arrby, n3);
                ++n4;
                n3 += 4;
            }
            for (int j = 0; j < n2; j += 8) {
                int n5 = n3 + 1;
                arrby[n3] = (byte)(255 & this.matrix[i][n] >>> j);
                n3 = n5;
            }
        }
        return arrby;
    }

    /*
     * Enabled aggressive block sorting
     */
    public double getHammingWeight() {
        double d = 0.0;
        double d2 = 0.0;
        int n = 31 & this.numColumns;
        int n2 = n == 0 ? this.length : -1 + this.length;
        int n3 = 0;
        while (n3 < this.numRows) {
            double d3 = d;
            double d4 = d2;
            for (int i = 0; i < n2; ++i) {
                int n4 = this.matrix[n3][i];
                for (int j = 0; j < 32; d3 += (double)(1 & n4 >>> j), d4 += 1.0, ++j) {
                }
            }
            int n5 = this.matrix[n3][-1 + this.length];
            d2 = d4;
            d = d3;
            for (int i = 0; i < n; ++i) {
                double d5 = d + (double)(1 & n5 >>> i);
                double d6 = 1.0 + d2;
                d2 = d6;
                d = d5;
            }
            ++n3;
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
        int n = 31 + this.numRows >> 5;
        int[] arrn = new int[]{this.numRows, n};
        int[][] arrn2 = (int[][])Array.newInstance((Class)Integer.TYPE, (int[])arrn);
        int n2 = -1 + (1 << (31 & this.numRows));
        if (n2 == 0) {
            n2 = -1;
        }
        for (int i = -1 + this.numRows; i >= 0; --i) {
            System.arraycopy((Object)this.matrix[i], (int)0, (Object)arrn2[i], (int)0, (int)n);
            int[] arrn3 = arrn2[i];
            int n3 = n - 1;
            arrn3[n3] = n2 & arrn3[n3];
        }
        return new GF2Matrix(this.numRows, arrn2);
    }

    public int getLength() {
        return this.length;
    }

    /*
     * Enabled aggressive block sorting
     */
    public GF2Matrix getRightSubMatrix() {
        if (this.numColumns <= this.numRows) {
            throw new ArithmeticException("empty submatrix");
        }
        int n = this.numRows >> 5;
        int n2 = 31 & this.numRows;
        GF2Matrix gF2Matrix = new GF2Matrix(this.numRows, this.numColumns - this.numRows);
        int n3 = -1 + this.numRows;
        while (n3 >= 0) {
            if (n2 != 0) {
                int n4 = n;
                for (int i = 0; i < -1 + gF2Matrix.length; ++i) {
                    int[] arrn = gF2Matrix.matrix[n3];
                    int[] arrn2 = this.matrix[n3];
                    int n5 = n4 + 1;
                    arrn[i] = arrn2[n4] >>> n2 | this.matrix[n3][n5] << 32 - n2;
                    n4 = n5;
                }
                int[] arrn = gF2Matrix.matrix[n3];
                int n6 = -1 + gF2Matrix.length;
                int[] arrn3 = this.matrix[n3];
                int n7 = n4 + 1;
                arrn[n6] = arrn3[n4] >>> n2;
                if (n7 < this.length) {
                    int[] arrn4 = gF2Matrix.matrix[n3];
                    int n8 = -1 + gF2Matrix.length;
                    arrn4[n8] = arrn4[n8] | this.matrix[n3][n7] << 32 - n2;
                }
            } else {
                System.arraycopy((Object)this.matrix[n3], (int)n, (Object)gF2Matrix.matrix[n3], (int)0, (int)gF2Matrix.length);
            }
            --n3;
        }
        return gF2Matrix;
    }

    public int[] getRow(int n) {
        return this.matrix[n];
    }

    public int hashCode() {
        int n = 31 * (31 * this.numRows + this.numColumns) + this.length;
        for (int i = 0; i < this.numRows; ++i) {
            n = n * 31 + this.matrix[i].hashCode();
        }
        return n;
    }

    @Override
    public boolean isZero() {
        for (int i = 0; i < this.numRows; ++i) {
            for (int j = 0; j < this.length; ++j) {
                if (this.matrix[i][j] == 0) continue;
                return false;
            }
        }
        return true;
    }

    public Matrix leftMultiply(Permutation permutation) {
        int[] arrn = permutation.getVector();
        if (arrn.length != this.numRows) {
            throw new ArithmeticException("length mismatch");
        }
        int[][] arrarrn = new int[this.numRows][];
        for (int i = -1 + this.numRows; i >= 0; --i) {
            arrarrn[i] = IntUtils.clone(this.matrix[arrn[i]]);
        }
        return new GF2Matrix(this.numRows, arrarrn);
    }

    @Override
    public Vector leftMultiply(Vector vector) {
        int n = 1;
        if (!(vector instanceof GF2Vector)) {
            throw new ArithmeticException("vector is not defined over GF(2)");
        }
        if (vector.length != this.numRows) {
            throw new ArithmeticException("length mismatch");
        }
        int[] arrn = ((GF2Vector)vector).getVecArray();
        int[] arrn2 = new int[this.length];
        int n2 = this.numRows >> 5;
        int n3 = n << (31 & this.numRows);
        int n4 = 0;
        for (int i = 0; i < n2; ++i) {
            int n5 = n4;
            int n6 = n;
            do {
                if ((n6 & arrn[i]) != 0) {
                    for (int j = 0; j < this.length; ++j) {
                        arrn2[j] = arrn2[j] ^ this.matrix[n5][j];
                    }
                }
                ++n5;
            } while ((n6 <<= 1) != 0);
            n4 = n5;
        }
        int n7 = n4;
        while (n != n3) {
            if ((n & arrn[n2]) != 0) {
                for (int i = 0; i < this.length; ++i) {
                    arrn2[i] = arrn2[i] ^ this.matrix[n7][i];
                }
            }
            int n8 = n7 + 1;
            n <<= 1;
            n7 = n8;
        }
        return new GF2Vector(arrn2, this.numColumns);
    }

    public Vector leftMultiplyLeftCompactForm(Vector vector) {
        if (!(vector instanceof GF2Vector)) {
            throw new ArithmeticException("vector is not defined over GF(2)");
        }
        if (vector.length != this.numRows) {
            throw new ArithmeticException("length mismatch");
        }
        int[] arrn = ((GF2Vector)vector).getVecArray();
        int[] arrn2 = new int[31 + (this.numRows + this.numColumns) >>> 5];
        int n = this.numRows >>> 5;
        int n2 = 0;
        for (int i = 0; i < n; ++i) {
            int n3 = n2;
            int n4 = 1;
            do {
                if ((n4 & arrn[i]) != 0) {
                    for (int j = 0; j < this.length; ++j) {
                        arrn2[j] = arrn2[j] ^ this.matrix[n3][j];
                    }
                    int n5 = n3 + this.numColumns >>> 5;
                    int n6 = 31 & n3 + this.numColumns;
                    arrn2[n5] = arrn2[n5] | 1 << n6;
                }
                ++n3;
            } while ((n4 <<= 1) != 0);
            n2 = n3;
        }
        int n7 = 1 << (31 & this.numRows);
        int n8 = n2;
        for (int i = 1; i != n7; i <<= 1) {
            if ((i & arrn[n]) != 0) {
                for (int j = 0; j < this.length; ++j) {
                    arrn2[j] = arrn2[j] ^ this.matrix[n8][j];
                }
                int n9 = n8 + this.numColumns >>> 5;
                int n10 = 31 & n8 + this.numColumns;
                arrn2[n9] = arrn2[n9] | 1 << n10;
            }
            ++n8;
        }
        return new GF2Vector(arrn2, this.numRows + this.numColumns);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public Matrix rightMultiply(Matrix matrix) {
        if (!(matrix instanceof GF2Matrix)) {
            throw new ArithmeticException("matrix is not defined over GF(2)");
        }
        if (matrix.numRows != this.numColumns) {
            throw new ArithmeticException("length mismatch");
        }
        GF2Matrix gF2Matrix = (GF2Matrix)matrix;
        GF2Matrix gF2Matrix2 = new GF2Matrix(this.numRows, matrix.numColumns);
        int n = 31 & this.numColumns;
        int n2 = n == 0 ? this.length : -1 + this.length;
        int n3 = 0;
        while (n3 < this.numRows) {
            int n4;
            int n5;
            int n6 = 0;
            int n7 = 0;
            do {
                int n8;
                int n9;
                if (n7 < n2) {
                    n8 = this.matrix[n3][n7];
                    n9 = n6;
                } else {
                    n4 = this.matrix[n3][-1 + this.length];
                    n5 = n6;
                    break;
                }
                for (int i = 0; i < 32; ++n9, ++i) {
                    if ((n8 & 1 << i) == 0) continue;
                    for (int j = 0; j < gF2Matrix.length; ++j) {
                        int[] arrn = gF2Matrix2.matrix[n3];
                        arrn[j] = arrn[j] ^ gF2Matrix.matrix[n9][j];
                    }
                }
                ++n7;
                n6 = n9;
            } while (true);
            for (int i = 0; i < n; ++n5, ++i) {
                if ((n4 & 1 << i) == 0) continue;
                for (int j = 0; j < gF2Matrix.length; ++j) {
                    int[] arrn = gF2Matrix2.matrix[n3];
                    arrn[j] = arrn[j] ^ gF2Matrix.matrix[n5][j];
                }
            }
            ++n3;
        }
        return gF2Matrix2;
    }

    @Override
    public Matrix rightMultiply(Permutation permutation) {
        int[] arrn = permutation.getVector();
        if (arrn.length != this.numColumns) {
            throw new ArithmeticException("length mismatch");
        }
        GF2Matrix gF2Matrix = new GF2Matrix(this.numRows, this.numColumns);
        for (int i = -1 + this.numColumns; i >= 0; --i) {
            int n = i >>> 5;
            int n2 = i & 31;
            int n3 = arrn[i] >>> 5;
            int n4 = 31 & arrn[i];
            for (int j = -1 + this.numRows; j >= 0; --j) {
                int[] arrn2 = gF2Matrix.matrix[j];
                arrn2[n] = arrn2[n] | (1 & this.matrix[j][n3] >>> n4) << n2;
            }
        }
        return gF2Matrix;
    }

    @Override
    public Vector rightMultiply(Vector vector) {
        if (!(vector instanceof GF2Vector)) {
            throw new ArithmeticException("vector is not defined over GF(2)");
        }
        if (vector.length != this.numColumns) {
            throw new ArithmeticException("length mismatch");
        }
        int[] arrn = ((GF2Vector)vector).getVecArray();
        int[] arrn2 = new int[31 + this.numRows >>> 5];
        for (int i = 0; i < this.numRows; ++i) {
            int n = 0;
            for (int j = 0; j < this.length; ++j) {
                n ^= this.matrix[i][j] & arrn[j];
            }
            int n2 = 0;
            for (int j = 0; j < 32; ++j) {
                n2 ^= 1 & n >>> j;
            }
            if (n2 != true) continue;
            int n3 = i >>> 5;
            arrn2[n3] = arrn2[n3] | 1 << (i & 31);
        }
        return new GF2Vector(arrn2, this.numRows);
    }

    /*
     * Enabled aggressive block sorting
     */
    public Vector rightMultiplyRightCompactForm(Vector vector) {
        if (!(vector instanceof GF2Vector)) {
            throw new ArithmeticException("vector is not defined over GF(2)");
        }
        if (vector.length != this.numColumns + this.numRows) {
            throw new ArithmeticException("length mismatch");
        }
        int[] arrn = ((GF2Vector)vector).getVecArray();
        int[] arrn2 = new int[31 + this.numRows >>> 5];
        int n = this.numRows >> 5;
        int n2 = 31 & this.numRows;
        int n3 = 0;
        while (n3 < this.numRows) {
            int n4;
            int n5 = 1 & arrn[n3 >> 5] >>> (n3 & 31);
            if (n2 != 0) {
                int n6 = n;
                int n7 = n5;
                for (int i = 0; i < -1 + this.length; ++i) {
                    int n8 = n6 + 1;
                    int n9 = n7 ^ (arrn[n6] >>> n2 | arrn[n8] << 32 - n2) & this.matrix[n3][i];
                    n7 = n9;
                    n6 = n8;
                }
                int n10 = n6 + 1;
                int n11 = arrn[n6] >>> n2;
                if (n10 < arrn.length) {
                    n11 |= arrn[n10] << 32 - n2;
                }
                n4 = n7 ^ n11 & this.matrix[n3][-1 + this.length];
            } else {
                int n12 = n;
                n4 = n5;
                for (int i = 0; i < this.length; ++i) {
                    int n13 = this.matrix[n3][i];
                    int n14 = n12 + 1;
                    int n15 = n4 ^ n13 & arrn[n12];
                    n4 = n15;
                    n12 = n14;
                }
            }
            int n16 = 0;
            for (int i = 0; i < 32; n16 ^= n4 & 1, n4 >>>= 1, ++i) {
            }
            if (n16 == 1) {
                int n17 = n3 >> 5;
                arrn2[n17] = arrn2[n17] | 1 << (n3 & 31);
            }
            ++n3;
        }
        return new GF2Vector(arrn2, this.numRows);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public String toString() {
        int n = 31 & this.numColumns;
        int n2 = n == 0 ? this.length : -1 + this.length;
        StringBuffer stringBuffer = new StringBuffer();
        int n3 = 0;
        while (n3 < this.numRows) {
            int n4;
            stringBuffer.append(n3 + ": ");
            int n5 = 0;
            do {
                int n6;
                if (n5 < n2) {
                    n6 = this.matrix[n3][n5];
                } else {
                    n4 = this.matrix[n3][-1 + this.length];
                    break;
                }
                for (int i = 0; i < 32; ++i) {
                    if ((1 & n6 >>> i) == 0) {
                        stringBuffer.append('0');
                        continue;
                    }
                    stringBuffer.append('1');
                }
                stringBuffer.append(' ');
                ++n5;
            } while (true);
            for (int i = 0; i < n; ++i) {
                if ((1 & n4 >>> i) == 0) {
                    stringBuffer.append('0');
                    continue;
                }
                stringBuffer.append('1');
            }
            stringBuffer.append('\n');
            ++n3;
        }
        return stringBuffer.toString();
    }
}

