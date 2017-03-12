package com.samsung.android.contextaware.aggregator.lpp.algorithm;

import java.io.Serializable;

public class MatrixQRDecomposition implements Serializable {
    private static final long serialVersionUID = 1;
    private double[][] QR;
    private double[] Rdiag = new double[this.n];
    private int m;
    private int n;

    public MatrixQRDecomposition(Matrix A) {
        this.QR = A.getArrayCopy();
        this.m = A.getRowDimension();
        this.n = A.getColumnDimension();
        for (int k = 0; k < this.n; k++) {
            int i;
            double nrm = 0.0d;
            for (i = k; i < this.m; i++) {
                nrm = Matrix.hypot(nrm, this.QR[i][k]);
            }
            if (nrm != 0.0d) {
                double[] dArr;
                if (this.QR[k][k] < 0.0d) {
                    nrm = -nrm;
                }
                for (i = k; i < this.m; i++) {
                    dArr = this.QR[i];
                    dArr[k] = dArr[k] / nrm;
                }
                dArr = this.QR[k];
                dArr[k] = dArr[k] + 1.0d;
                for (int j = k + 1; j < this.n; j++) {
                    double s = 0.0d;
                    for (i = k; i < this.m; i++) {
                        s += this.QR[i][k] * this.QR[i][j];
                    }
                    s = (-s) / this.QR[k][k];
                    for (i = k; i < this.m; i++) {
                        dArr = this.QR[i];
                        dArr[j] = dArr[j] + (this.QR[i][k] * s);
                    }
                }
            }
            this.Rdiag[k] = -nrm;
        }
    }

    public boolean isFullRank() {
        for (int j = 0; j < this.n; j++) {
            if (this.Rdiag[j] == 0.0d) {
                return false;
            }
        }
        return true;
    }

    public Matrix getH() {
        Matrix X = new Matrix(this.m, this.n);
        double[][] H = X.getArray();
        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j < this.n; j++) {
                if (i >= j) {
                    H[i][j] = this.QR[i][j];
                } else {
                    H[i][j] = 0.0d;
                }
            }
        }
        return X;
    }

    public Matrix getR() {
        Matrix X = new Matrix(this.n, this.n);
        double[][] R = X.getArray();
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (i < j) {
                    R[i][j] = this.QR[i][j];
                } else if (i == j) {
                    R[i][j] = this.Rdiag[i];
                } else {
                    R[i][j] = 0.0d;
                }
            }
        }
        return X;
    }

    public Matrix getQ() {
        Matrix X = new Matrix(this.m, this.n);
        double[][] Q = X.getArray();
        for (int k = this.n - 1; k >= 0; k--) {
            int i;
            for (i = 0; i < this.m; i++) {
                Q[i][k] = 0.0d;
            }
            Q[k][k] = 1.0d;
            for (int j = k; j < this.n; j++) {
                if (this.QR[k][k] != 0.0d) {
                    double s = 0.0d;
                    for (i = k; i < this.m; i++) {
                        s += this.QR[i][k] * Q[i][j];
                    }
                    s = (-s) / this.QR[k][k];
                    for (i = k; i < this.m; i++) {
                        double[] dArr = Q[i];
                        dArr[j] = dArr[j] + (this.QR[i][k] * s);
                    }
                }
            }
        }
        return X;
    }

    public Matrix solve(Matrix B) {
        if (B.getRowDimension() != this.m) {
            throw new IllegalArgumentException("Matrix row dimensions must agree.");
        } else if (isFullRank()) {
            int k;
            int j;
            int i;
            double[] dArr;
            int nx = B.getColumnDimension();
            double[][] X = B.getArrayCopy();
            for (k = 0; k < this.n; k++) {
                for (j = 0; j < nx; j++) {
                    double s = 0.0d;
                    for (i = k; i < this.m; i++) {
                        s += this.QR[i][k] * X[i][j];
                    }
                    s = (-s) / this.QR[k][k];
                    for (i = k; i < this.m; i++) {
                        dArr = X[i];
                        dArr[j] = dArr[j] + (this.QR[i][k] * s);
                    }
                }
            }
            for (k = this.n - 1; k >= 0; k--) {
                for (j = 0; j < nx; j++) {
                    dArr = X[k];
                    dArr[j] = dArr[j] / this.Rdiag[k];
                }
                for (i = 0; i < k; i++) {
                    for (j = 0; j < nx; j++) {
                        dArr = X[i];
                        dArr[j] = dArr[j] - (X[k][j] * this.QR[i][k]);
                    }
                }
            }
            return new Matrix(X, this.n, nx).getMatrix(0, this.n - 1, 0, nx - 1);
        } else {
            throw new RuntimeException("Matrix is rank deficient.");
        }
    }
}
