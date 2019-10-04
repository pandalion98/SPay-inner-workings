/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.ArithmeticException
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.reflect.Array
 */
package org.bouncycastle.pqc.math.linearalgebra;

import java.lang.reflect.Array;
import org.bouncycastle.pqc.math.linearalgebra.GF2mField;
import org.bouncycastle.pqc.math.linearalgebra.IntUtils;
import org.bouncycastle.pqc.math.linearalgebra.Matrix;
import org.bouncycastle.pqc.math.linearalgebra.Permutation;
import org.bouncycastle.pqc.math.linearalgebra.Vector;

public class GF2mMatrix
extends Matrix {
    protected GF2mField field;
    protected int[][] matrix;

    public GF2mMatrix(GF2mField gF2mField, byte[] arrby) {
        int n;
        this.field = gF2mField;
        int n2 = 1;
        for (n = 8; gF2mField.getDegree() > n; n += 8) {
            ++n2;
        }
        if (arrby.length < 5) {
            throw new IllegalArgumentException(" Error: given array is not encoded matrix over GF(2^m)");
        }
        this.numRows = (255 & arrby[3]) << 24 ^ (255 & arrby[2]) << 16 ^ (255 & arrby[1]) << 8 ^ 255 & arrby[0];
        int n3 = n2 * this.numRows;
        if (this.numRows <= 0 || (-4 + arrby.length) % n3 != 0) {
            throw new IllegalArgumentException(" Error: given array is not encoded matrix over GF(2^m)");
        }
        this.numColumns = (-4 + arrby.length) / n3;
        int[] arrn = new int[]{this.numRows, this.numColumns};
        this.matrix = (int[][])Array.newInstance((Class)Integer.TYPE, (int[])arrn);
        int n4 = 4;
        for (int i = 0; i < this.numRows; ++i) {
            for (int j = 0; j < this.numColumns; ++j) {
                int n5 = n4;
                for (int k = 0; k < n; k += 8) {
                    int[] arrn2 = this.matrix[i];
                    int n6 = arrn2[j];
                    int n7 = n5 + 1;
                    arrn2[j] = n6 ^ (255 & arrby[n5]) << k;
                    n5 = n7;
                }
                if (!this.field.isElementOfThisField(this.matrix[i][j])) {
                    throw new IllegalArgumentException(" Error: given array is not encoded matrix over GF(2^m)");
                }
                n4 = n5;
            }
        }
    }

    protected GF2mMatrix(GF2mField gF2mField, int[][] arrn) {
        this.field = gF2mField;
        this.matrix = arrn;
        this.numRows = arrn.length;
        this.numColumns = arrn[0].length;
    }

    public GF2mMatrix(GF2mMatrix gF2mMatrix) {
        this.numRows = gF2mMatrix.numRows;
        this.numColumns = gF2mMatrix.numColumns;
        this.field = gF2mMatrix.field;
        this.matrix = new int[this.numRows][];
        for (int i = 0; i < this.numRows; ++i) {
            this.matrix[i] = IntUtils.clone(gF2mMatrix.matrix[i]);
        }
    }

    private void addToRow(int[] arrn, int[] arrn2) {
        for (int i = -1 + arrn2.length; i >= 0; --i) {
            arrn2[i] = this.field.add(arrn[i], arrn2[i]);
        }
    }

    private int[] multRowWithElement(int[] arrn, int n) {
        int[] arrn2 = new int[arrn.length];
        for (int i = -1 + arrn.length; i >= 0; --i) {
            arrn2[i] = this.field.mult(arrn[i], n);
        }
        return arrn2;
    }

    private void multRowWithElementThis(int[] arrn, int n) {
        for (int i = -1 + arrn.length; i >= 0; --i) {
            arrn[i] = this.field.mult(arrn[i], n);
        }
    }

    private static void swapColumns(int[][] arrn, int n, int n2) {
        int[] arrn2 = arrn[n];
        arrn[n] = arrn[n2];
        arrn[n2] = arrn2;
    }

    @Override
    public Matrix computeInverse() {
        if (this.numRows != this.numColumns) {
            throw new ArithmeticException("Matrix is not invertible.");
        }
        int[] arrn = new int[]{this.numRows, this.numRows};
        int[][] arrn2 = (int[][])Array.newInstance((Class)Integer.TYPE, (int[])arrn);
        for (int i = -1 + this.numRows; i >= 0; --i) {
            arrn2[i] = IntUtils.clone(this.matrix[i]);
        }
        int[] arrn3 = new int[]{this.numRows, this.numRows};
        int[][] arrn4 = (int[][])Array.newInstance((Class)Integer.TYPE, (int[])arrn3);
        for (int i = -1 + this.numRows; i >= 0; --i) {
            arrn4[i][i] = 1;
        }
        for (int i = 0; i < this.numRows; ++i) {
            if (arrn2[i][i] == 0) {
                boolean bl = false;
                for (int j = i + 1; j < this.numRows; ++j) {
                    if (arrn2[j][i] == 0) continue;
                    GF2mMatrix.swapColumns(arrn2, i, j);
                    GF2mMatrix.swapColumns(arrn4, i, j);
                    j = this.numRows;
                    bl = true;
                }
                if (!bl) {
                    throw new ArithmeticException("Matrix is not invertible.");
                }
            }
            int n = arrn2[i][i];
            int n2 = this.field.inverse(n);
            this.multRowWithElementThis(arrn2[i], n2);
            this.multRowWithElementThis(arrn4[i], n2);
            for (int j = 0; j < this.numRows; ++j) {
                int n3;
                if (j == i || (n3 = arrn2[j][i]) == 0) continue;
                int[] arrn5 = this.multRowWithElement(arrn2[i], n3);
                int[] arrn6 = this.multRowWithElement(arrn4[i], n3);
                this.addToRow(arrn5, arrn2[j]);
                this.addToRow(arrn6, arrn4[j]);
            }
        }
        return new GF2mMatrix(this.field, arrn4);
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        if (object != null && object instanceof GF2mMatrix) {
            GF2mMatrix gF2mMatrix = (GF2mMatrix)object;
            if (this.field.equals(gF2mMatrix.field) && gF2mMatrix.numRows == this.numColumns && gF2mMatrix.numColumns == this.numColumns) {
                int n = 0;
                block0 : do {
                    if (n >= this.numRows) {
                        return true;
                    }
                    for (int i = 0; i < this.numColumns; ++i) {
                        if (this.matrix[n][i] != gF2mMatrix.matrix[n][i]) break block0;
                    }
                    ++n;
                } while (true);
            }
        }
        return false;
    }

    @Override
    public byte[] getEncoded() {
        int n;
        int n2 = 1;
        for (n = 8; this.field.getDegree() > n; n += 8) {
            ++n2;
        }
        byte[] arrby = new byte[4 + n2 * (this.numRows * this.numColumns)];
        arrby[0] = (byte)(255 & this.numRows);
        arrby[1] = (byte)(255 & this.numRows >>> 8);
        arrby[2] = (byte)(255 & this.numRows >>> 16);
        arrby[3] = (byte)(255 & this.numRows >>> 24);
        int n3 = 4;
        for (int i = 0; i < this.numRows; ++i) {
            for (int j = 0; j < this.numColumns; ++j) {
                int n4 = n3;
                for (int k = 0; k < n; k += 8) {
                    int n5 = n4 + 1;
                    arrby[n4] = (byte)(this.matrix[i][j] >>> k);
                    n4 = n5;
                }
                n3 = n4;
            }
        }
        return arrby;
    }

    public int hashCode() {
        int n = 31 * (31 * this.field.hashCode() + this.numRows) + this.numColumns;
        for (int i = 0; i < this.numRows; ++i) {
            for (int j = 0; j < this.numColumns; ++j) {
                int n2 = n * 31 + this.matrix[i][j];
                n = n2;
            }
        }
        return n;
    }

    @Override
    public boolean isZero() {
        for (int i = 0; i < this.numRows; ++i) {
            for (int j = 0; j < this.numColumns; ++j) {
                if (this.matrix[i][j] == 0) continue;
                return false;
            }
        }
        return true;
    }

    @Override
    public Vector leftMultiply(Vector vector) {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public Matrix rightMultiply(Matrix matrix) {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public Matrix rightMultiply(Permutation permutation) {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public Vector rightMultiply(Vector vector) {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public String toString() {
        String string = this.numRows + " x " + this.numColumns + " Matrix over " + this.field.toString() + ": \n";
        for (int i = 0; i < this.numRows; ++i) {
            String string2 = string;
            for (int j = 0; j < this.numColumns; ++j) {
                string2 = string2 + this.field.elementToStr(this.matrix[i][j]) + " : ";
            }
            string = string2 + "\n";
        }
        return string;
    }
}

