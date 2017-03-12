package com.samsung.android.contextaware.aggregator.lpp.algorithm;

import java.io.Serializable;

public class MatrixLUDecomposition implements Serializable {
    private static final long serialVersionUID = 1;
    private double[][] LU;
    private int m;
    private int n;
    private int[] piv = new int[this.m];
    private int pivsign;

    public MatrixLUDecomposition(Matrix A) {
        int i;
        this.LU = A.getArrayCopy();
        this.m = A.getRowDimension();
        this.n = A.getColumnDimension();
        for (i = 0; i < this.m; i++) {
            this.piv[i] = i;
        }
        this.pivsign = 1;
        double[] LUcolj = new double[this.m];
        int j = 0;
        while (j < this.n) {
            for (i = 0; i < this.m; i++) {
                LUcolj[i] = this.LU[i][j];
            }
            for (i = 0; i < this.m; i++) {
                int k;
                double[] LUrowi = this.LU[i];
                double s = 0.0d;
                for (k = 0; k < Math.min(i, j); k++) {
                    s += LUrowi[k] * LUcolj[k];
                }
                double d = LUcolj[i] - s;
                LUcolj[i] = d;
                LUrowi[j] = d;
            }
            int p = j;
            for (i = j + 1; i < this.m; i++) {
                if (Math.abs(LUcolj[i]) > Math.abs(LUcolj[p])) {
                    p = i;
                }
            }
            if (p != j) {
                for (k = 0; k < this.n; k++) {
                    double t = this.LU[p][k];
                    this.LU[p][k] = this.LU[j][k];
                    this.LU[j][k] = t;
                }
                k = this.piv[p];
                this.piv[p] = this.piv[j];
                this.piv[j] = k;
                this.pivsign = -this.pivsign;
            }
            if (j < this.m && this.LU[j][j] != 0.0d) {
                for (i = j + 1; i < this.m; i++) {
                    double[] dArr = this.LU[i];
                    dArr[j] = dArr[j] / this.LU[j][j];
                }
            }
            j++;
        }
    }

    public boolean isNonsingular() {
        for (int j = 0; j < this.n; j++) {
            if (this.LU[j][j] == 0.0d) {
                return false;
            }
        }
        return true;
    }

    public Matrix getL() {
        Matrix X = new Matrix(this.m, this.n);
        double[][] L = X.getArray();
        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j < this.n; j++) {
                if (i > j) {
                    L[i][j] = this.LU[i][j];
                } else if (i == j) {
                    L[i][j] = 1.0d;
                } else {
                    L[i][j] = 0.0d;
                }
            }
        }
        return X;
    }

    public Matrix getU() {
        Matrix X = new Matrix(this.n, this.n);
        double[][] U = X.getArray();
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (i <= j) {
                    U[i][j] = this.LU[i][j];
                } else {
                    U[i][j] = 0.0d;
                }
            }
        }
        return X;
    }

    public int[] getPivot() {
        int[] p = new int[this.m];
        for (int i = 0; i < this.m; i++) {
            p[i] = this.piv[i];
        }
        return p;
    }

    public double[] getDoublePivot() {
        double[] vals = new double[this.m];
        for (int i = 0; i < this.m; i++) {
            vals[i] = (double) this.piv[i];
        }
        return vals;
    }

    public double det() {
        if (this.m != this.n) {
            throw new IllegalArgumentException("Matrix must be square.");
        }
        double d = (double) this.pivsign;
        for (int j = 0; j < this.n; j++) {
            d *= this.LU[j][j];
        }
        return d;
    }

    public Matrix solve(Matrix B) {
        if (B.getRowDimension() != this.m) {
            throw new IllegalArgumentException("Matrix row dimensions must agree.");
        } else if (isNonsingular()) {
            int k;
            int i;
            int j;
            double[] dArr;
            int nx = B.getColumnDimension();
            Matrix Xmat = B.getMatrix(this.piv, 0, nx - 1);
            double[][] X = Xmat.getArray();
            for (k = 0; k < this.n; k++) {
                for (i = k + 1; i < this.n; i++) {
                    for (j = 0; j < nx; j++) {
                        dArr = X[i];
                        dArr[j] = dArr[j] - (X[k][j] * this.LU[i][k]);
                    }
                }
            }
            for (k = this.n - 1; k >= 0; k--) {
                for (j = 0; j < nx; j++) {
                    dArr = X[k];
                    dArr[j] = dArr[j] / this.LU[k][k];
                }
                for (i = 0; i < k; i++) {
                    for (j = 0; j < nx; j++) {
                        dArr = X[i];
                        dArr[j] = dArr[j] - (X[k][j] * this.LU[i][k]);
                    }
                }
            }
            return Xmat;
        } else {
            throw new RuntimeException("Matrix is singular.");
        }
    }
}
