package com.samsung.android.contextaware.aggregator.lpp.algorithm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StreamTokenizer;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Vector;

public class Matrix implements Cloneable, Serializable {
    private static final long serialVersionUID = 1;
    private double[][] A;
    private int m;
    private int n;

    public Matrix(int m, int n) {
        this.m = m;
        this.n = n;
        this.A = (double[][]) Array.newInstance(Double.TYPE, new int[]{m, n});
    }

    public Matrix(int m, int n, double s) {
        this.m = m;
        this.n = n;
        this.A = (double[][]) Array.newInstance(Double.TYPE, new int[]{m, n});
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                this.A[i][j] = s;
            }
        }
    }

    public Matrix(double[][] A) {
        this.m = A.length;
        this.n = A[0].length;
        for (int i = 0; i < this.m; i++) {
            if (A[i].length != this.n) {
                throw new IllegalArgumentException("All rows must have the same length.");
            }
        }
        this.A = A;
    }

    public Matrix(double[][] A, int m, int n) {
        this.A = A;
        this.m = m;
        this.n = n;
    }

    public Matrix(double[] vals, int m) {
        this.m = m;
        this.n = m != 0 ? vals.length / m : 0;
        if (this.n * m != vals.length) {
            throw new IllegalArgumentException("Array length must be a multiple of m.");
        }
        this.A = (double[][]) Array.newInstance(Double.TYPE, new int[]{m, this.n});
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < this.n; j++) {
                this.A[i][j] = vals[(j * m) + i];
            }
        }
    }

    public static Matrix constructWithCopy(double[][] A) {
        int m = A.length;
        int n = A[0].length;
        Matrix X = new Matrix(m, n);
        double[][] C = X.getArray();
        for (int i = 0; i < m; i++) {
            if (A[i].length != n) {
                throw new IllegalArgumentException("All rows must have the same length.");
            }
            for (int j = 0; j < n; j++) {
                C[i][j] = A[i][j];
            }
        }
        return X;
    }

    public Matrix copy() {
        Matrix X = new Matrix(this.m, this.n);
        double[][] C = X.getArray();
        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j < this.n; j++) {
                C[i][j] = this.A[i][j];
            }
        }
        return X;
    }

    public Object clone() {
        return copy();
    }

    public double[][] getArray() {
        return this.A;
    }

    public double[][] getArrayCopy() {
        double[][] C = (double[][]) Array.newInstance(Double.TYPE, new int[]{this.m, this.n});
        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j < this.n; j++) {
                C[i][j] = this.A[i][j];
            }
        }
        return C;
    }

    public double[] getColumnPackedCopy() {
        double[] vals = new double[(this.m * this.n)];
        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j < this.n; j++) {
                vals[(this.m * j) + i] = this.A[i][j];
            }
        }
        return vals;
    }

    public double[] getRowPackedCopy() {
        double[] vals = new double[(this.m * this.n)];
        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j < this.n; j++) {
                vals[(this.n * i) + j] = this.A[i][j];
            }
        }
        return vals;
    }

    public int getRowDimension() {
        return this.m;
    }

    public int getColumnDimension() {
        return this.n;
    }

    public double get(int i, int j) {
        return this.A[i][j];
    }

    public Matrix getMatrix(int i0, int i1, int j0, int j1) {
        Matrix X = new Matrix((i1 - i0) + 1, (j1 - j0) + 1);
        double[][] B = X.getArray();
        for (int i = i0; i <= i1; i++) {
            int j = j0;
            while (j <= j1) {
                try {
                    B[i - i0][j - j0] = this.A[i][j];
                    j++;
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new ArrayIndexOutOfBoundsException("Submatrix indices");
                }
            }
        }
        return X;
    }

    public Matrix getMatrix(int[] r, int[] c) {
        Matrix X = new Matrix(r.length, c.length);
        double[][] B = X.getArray();
        int i = 0;
        while (i < r.length) {
            try {
                for (int j = 0; j < c.length; j++) {
                    B[i][j] = this.A[r[i]][c[j]];
                }
                i++;
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new ArrayIndexOutOfBoundsException("Submatrix indices");
            }
        }
        return X;
    }

    public Matrix getMatrix(int i0, int i1, int[] c) {
        Matrix X = new Matrix((i1 - i0) + 1, c.length);
        double[][] B = X.getArray();
        for (int i = i0; i <= i1; i++) {
            int j = 0;
            while (j < c.length) {
                try {
                    B[i - i0][j] = this.A[i][c[j]];
                    j++;
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new ArrayIndexOutOfBoundsException("Submatrix indices");
                }
            }
        }
        return X;
    }

    public Matrix getMatrix(int[] r, int j0, int j1) {
        Matrix X = new Matrix(r.length, (j1 - j0) + 1);
        double[][] B = X.getArray();
        int i = 0;
        while (i < r.length) {
            try {
                for (int j = j0; j <= j1; j++) {
                    B[i][j - j0] = this.A[r[i]][j];
                }
                i++;
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new ArrayIndexOutOfBoundsException("Submatrix indices");
            }
        }
        return X;
    }

    public void set(int i, int j, double s) {
        this.A[i][j] = s;
    }

    public void setMatrix(int i0, int i1, int j0, int j1, Matrix X) {
        for (int i = i0; i <= i1; i++) {
            int j = j0;
            while (j <= j1) {
                try {
                    this.A[i][j] = X.get(i - i0, j - j0);
                    j++;
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new ArrayIndexOutOfBoundsException("Submatrix indices");
                }
            }
        }
    }

    public void setMatrix(int[] r, int[] c, Matrix X) {
        int i = 0;
        while (i < r.length) {
            try {
                for (int j = 0; j < c.length; j++) {
                    this.A[r[i]][c[j]] = X.get(i, j);
                }
                i++;
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new ArrayIndexOutOfBoundsException("Submatrix indices");
            }
        }
    }

    public void setMatrix(int[] r, int j0, int j1, Matrix X) {
        int i = 0;
        while (i < r.length) {
            try {
                for (int j = j0; j <= j1; j++) {
                    this.A[r[i]][j] = X.get(i, j - j0);
                }
                i++;
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new ArrayIndexOutOfBoundsException("Submatrix indices");
            }
        }
    }

    public void setMatrix(int i0, int i1, int[] c, Matrix X) {
        for (int i = i0; i <= i1; i++) {
            int j = 0;
            while (j < c.length) {
                try {
                    this.A[i][c[j]] = X.get(i - i0, j);
                    j++;
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new ArrayIndexOutOfBoundsException("Submatrix indices");
                }
            }
        }
    }

    public Matrix transpose() {
        Matrix X = new Matrix(this.n, this.m);
        double[][] C = X.getArray();
        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j < this.n; j++) {
                C[j][i] = this.A[i][j];
            }
        }
        return X;
    }

    public double norm1() {
        double f = 0.0d;
        for (int j = 0; j < this.n; j++) {
            double s = 0.0d;
            for (int i = 0; i < this.m; i++) {
                s += Math.abs(this.A[i][j]);
            }
            f = Math.max(f, s);
        }
        return f;
    }

    public double norm2() {
        return new MatrixSingularValueDecomposition(this).norm2();
    }

    public double normInf() {
        double f = 0.0d;
        for (int i = 0; i < this.m; i++) {
            double s = 0.0d;
            for (int j = 0; j < this.n; j++) {
                s += Math.abs(this.A[i][j]);
            }
            f = Math.max(f, s);
        }
        return f;
    }

    public double normF() {
        double f = 0.0d;
        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j < this.n; j++) {
                f = hypot(f, this.A[i][j]);
            }
        }
        return f;
    }

    public static double hypot(double a, double b) {
        double r;
        if (Math.abs(a) > Math.abs(b)) {
            r = b / a;
            return Math.abs(a) * Math.sqrt((r * r) + 1.0d);
        } else if (b == 0.0d) {
            return 0.0d;
        } else {
            r = a / b;
            return Math.abs(b) * Math.sqrt((r * r) + 1.0d);
        }
    }

    public Matrix uminus() {
        Matrix X = new Matrix(this.m, this.n);
        double[][] C = X.getArray();
        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j < this.n; j++) {
                C[i][j] = -this.A[i][j];
            }
        }
        return X;
    }

    public Matrix plus(Matrix B) {
        checkMatrixDimensions(B);
        Matrix X = new Matrix(this.m, this.n);
        double[][] C = X.getArray();
        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j < this.n; j++) {
                C[i][j] = this.A[i][j] + B.A[i][j];
            }
        }
        return X;
    }

    public Matrix plusEquals(Matrix B) {
        checkMatrixDimensions(B);
        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j < this.n; j++) {
                this.A[i][j] = this.A[i][j] + B.A[i][j];
            }
        }
        return this;
    }

    public Matrix minus(Matrix B) {
        checkMatrixDimensions(B);
        Matrix X = new Matrix(this.m, this.n);
        double[][] C = X.getArray();
        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j < this.n; j++) {
                C[i][j] = this.A[i][j] - B.A[i][j];
            }
        }
        return X;
    }

    public Matrix minusEquals(Matrix B) {
        checkMatrixDimensions(B);
        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j < this.n; j++) {
                this.A[i][j] = this.A[i][j] - B.A[i][j];
            }
        }
        return this;
    }

    public Matrix arrayTimes(Matrix B) {
        checkMatrixDimensions(B);
        Matrix X = new Matrix(this.m, this.n);
        double[][] C = X.getArray();
        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j < this.n; j++) {
                C[i][j] = this.A[i][j] * B.A[i][j];
            }
        }
        return X;
    }

    public Matrix arrayTimesEquals(Matrix B) {
        checkMatrixDimensions(B);
        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j < this.n; j++) {
                this.A[i][j] = this.A[i][j] * B.A[i][j];
            }
        }
        return this;
    }

    public Matrix arrayRightDivide(Matrix B) {
        checkMatrixDimensions(B);
        Matrix X = new Matrix(this.m, this.n);
        double[][] C = X.getArray();
        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j < this.n; j++) {
                C[i][j] = this.A[i][j] / B.A[i][j];
            }
        }
        return X;
    }

    public Matrix arrayRightDivideEquals(Matrix B) {
        checkMatrixDimensions(B);
        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j < this.n; j++) {
                this.A[i][j] = this.A[i][j] / B.A[i][j];
            }
        }
        return this;
    }

    public Matrix arrayLeftDivide(Matrix B) {
        checkMatrixDimensions(B);
        Matrix X = new Matrix(this.m, this.n);
        double[][] C = X.getArray();
        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j < this.n; j++) {
                C[i][j] = B.A[i][j] / this.A[i][j];
            }
        }
        return X;
    }

    public Matrix arrayLeftDivideEquals(Matrix B) {
        checkMatrixDimensions(B);
        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j < this.n; j++) {
                this.A[i][j] = B.A[i][j] / this.A[i][j];
            }
        }
        return this;
    }

    public Matrix times(double s) {
        Matrix X = new Matrix(this.m, this.n);
        double[][] C = X.getArray();
        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j < this.n; j++) {
                C[i][j] = this.A[i][j] * s;
            }
        }
        return X;
    }

    public Matrix timesEquals(double s) {
        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j < this.n; j++) {
                this.A[i][j] = this.A[i][j] * s;
            }
        }
        return this;
    }

    public Matrix times(Matrix B) {
        if (B.m != this.n) {
            throw new IllegalArgumentException("Matrix inner dimensions must agree.");
        }
        Matrix X = new Matrix(this.m, B.n);
        double[][] C = X.getArray();
        double[] Bcolj = new double[this.n];
        for (int j = 0; j < B.n; j++) {
            int k;
            for (k = 0; k < this.n; k++) {
                Bcolj[k] = B.A[k][j];
            }
            for (int i = 0; i < this.m; i++) {
                double[] Arowi = this.A[i];
                double s = 0.0d;
                for (k = 0; k < this.n; k++) {
                    s += Arowi[k] * Bcolj[k];
                }
                C[i][j] = s;
            }
        }
        return X;
    }

    public MatrixSingularValueDecomposition svd() {
        return new MatrixSingularValueDecomposition(this);
    }

    public Matrix solve(Matrix B) {
        return this.m == this.n ? new MatrixLUDecomposition(this).solve(B) : new MatrixQRDecomposition(this).solve(B);
    }

    public Matrix solveTranspose(Matrix B) {
        return transpose().solve(B.transpose());
    }

    public Matrix inverse() {
        return solve(identity(this.m, this.m));
    }

    public double det() {
        return new MatrixLUDecomposition(this).det();
    }

    public int rank() {
        return new MatrixSingularValueDecomposition(this).rank();
    }

    public double cond() {
        return new MatrixSingularValueDecomposition(this).cond();
    }

    public double trace() {
        double t = 0.0d;
        for (int i = 0; i < Math.min(this.m, this.n); i++) {
            t += this.A[i][i];
        }
        return t;
    }

    public static Matrix random(int m, int n) {
        Matrix A = new Matrix(m, n);
        double[][] X = A.getArray();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                X[i][j] = Math.random();
            }
        }
        return A;
    }

    public static Matrix identity(int m, int n) {
        Matrix A = new Matrix(m, n);
        double[][] X = A.getArray();
        int i = 0;
        while (i < m) {
            int j = 0;
            while (j < n) {
                X[i][j] = i == j ? 1.0d : 0.0d;
                j++;
            }
            i++;
        }
        return A;
    }

    public void print(int w, int d) {
        print(new PrintWriter(System.out, true), w, d);
    }

    public void print(PrintWriter output, int w, int d) {
        NumberFormat format = new DecimalFormat();
        format.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
        format.setMinimumIntegerDigits(1);
        format.setMaximumFractionDigits(d);
        format.setMinimumFractionDigits(d);
        format.setGroupingUsed(false);
        print(output, format, w + 2);
    }

    public void print(NumberFormat format, int width) {
        print(new PrintWriter(System.out, true), format, width);
    }

    public void print(PrintWriter output, NumberFormat format, int width) {
        output.println();
        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j < this.n; j++) {
                String s = format.format(this.A[i][j]);
                int padding = Math.max(1, width - s.length());
                for (int k = 0; k < padding; k++) {
                    output.print(' ');
                }
                output.print(s);
            }
            output.println();
        }
        output.println();
    }

    public static Matrix read(BufferedReader input) throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(input);
        tokenizer.resetSyntax();
        tokenizer.wordChars(0, 255);
        tokenizer.whitespaceChars(0, 32);
        tokenizer.eolIsSignificant(true);
        Vector<Double> vD = new Vector();
        do {
        } while (tokenizer.nextToken() == 10);
        if (tokenizer.ttype == -1) {
            throw new IOException("Unexpected EOF on matrix read.");
        }
        int j;
        do {
            vD.addElement(Double.valueOf(tokenizer.sval));
        } while (tokenizer.nextToken() == -3);
        int n = vD.size();
        double[] row = new double[n];
        for (j = 0; j < n; j++) {
            row[j] = ((Double) vD.elementAt(j)).doubleValue();
        }
        Vector<double[]> v = new Vector();
        v.addElement(row);
        while (tokenizer.nextToken() == -3) {
            row = new double[n];
            v.addElement(row);
            int i = 0;
            while (i < n) {
                j = i + 1;
                row[i] = Double.valueOf(tokenizer.sval).doubleValue();
                if (tokenizer.nextToken() == -3) {
                    i = j;
                } else if (j < n) {
                    throw new IOException("Row " + v.size() + " is too short.");
                }
            }
            throw new IOException("Row " + v.size() + " is too long.");
        }
        double[][] A = new double[v.size()][];
        v.copyInto(A);
        return new Matrix(A);
    }

    private void checkMatrixDimensions(Matrix B) {
        if (B.m != this.m || B.n != this.n) {
            throw new IllegalArgumentException("Matrix dimensions must agree.");
        }
    }
}
