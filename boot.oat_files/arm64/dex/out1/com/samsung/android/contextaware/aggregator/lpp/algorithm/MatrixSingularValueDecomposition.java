package com.samsung.android.contextaware.aggregator.lpp.algorithm;

import java.io.Serializable;
import java.lang.reflect.Array;

public class MatrixSingularValueDecomposition implements Serializable {
    private static final long serialVersionUID = 1;
    private double[][] U;
    private double[][] V;
    private int m;
    private int n;
    private double[] s = new double[Math.min(this.m + 1, this.n)];

    public MatrixSingularValueDecomposition(Matrix Arg) {
        int i;
        double[] dArr;
        int j;
        double t;
        double[][] A = Arg.getArrayCopy();
        this.m = Arg.getRowDimension();
        this.n = Arg.getColumnDimension();
        int nu = Math.min(this.m, this.n);
        this.U = (double[][]) Array.newInstance(Double.TYPE, new int[]{this.m, nu});
        this.V = (double[][]) Array.newInstance(Double.TYPE, new int[]{this.n, this.n});
        double[] e = new double[this.n];
        double[] work = new double[this.m];
        int nct = Math.min(this.m - 1, this.n);
        int nrt = Math.max(0, Math.min(this.n - 2, this.m));
        int k = 0;
        while (k < Math.max(nct, nrt)) {
            if (k < nct) {
                this.s[k] = 0.0d;
                for (i = k; i < this.m; i++) {
                    this.s[k] = Matrix.hypot(this.s[k], A[i][k]);
                }
                if (this.s[k] != 0.0d) {
                    if (A[k][k] < 0.0d) {
                        this.s[k] = -this.s[k];
                    }
                    for (i = k; i < this.m; i++) {
                        dArr = A[i];
                        dArr[k] = dArr[k] / this.s[k];
                    }
                    dArr = A[k];
                    dArr[k] = dArr[k] + 1.0d;
                }
                this.s[k] = -this.s[k];
            }
            for (j = k + 1; j < this.n; j++) {
                if (k < nct && this.s[k] != 0.0d) {
                    t = 0.0d;
                    for (i = k; i < this.m; i++) {
                        t += A[i][k] * A[i][j];
                    }
                    t = (-t) / A[k][k];
                    for (i = k; i < this.m; i++) {
                        dArr = A[i];
                        dArr[j] = dArr[j] + (A[i][k] * t);
                    }
                }
                e[j] = A[k][j];
            }
            if (((k < nct ? 1 : 0) & 1) != 0) {
                for (i = k; i < this.m; i++) {
                    this.U[i][k] = A[i][k];
                }
            }
            if (k < nrt) {
                e[k] = 0.0d;
                for (i = k + 1; i < this.n; i++) {
                    e[k] = Matrix.hypot(e[k], e[i]);
                }
                if (e[k] != 0.0d) {
                    if (e[k + 1] < 0.0d) {
                        e[k] = -e[k];
                    }
                    for (i = k + 1; i < this.n; i++) {
                        e[i] = e[i] / e[k];
                    }
                    int i2 = k + 1;
                    e[i2] = e[i2] + 1.0d;
                }
                e[k] = -e[k];
                if (k + 1 < this.m && e[k] != 0.0d) {
                    for (i = k + 1; i < this.m; i++) {
                        work[i] = 0.0d;
                    }
                    for (j = k + 1; j < this.n; j++) {
                        for (i = k + 1; i < this.m; i++) {
                            work[i] = work[i] + (e[j] * A[i][j]);
                        }
                    }
                    for (j = k + 1; j < this.n; j++) {
                        t = (-e[j]) / e[k + 1];
                        for (i = k + 1; i < this.m; i++) {
                            dArr = A[i];
                            dArr[j] = dArr[j] + (work[i] * t);
                        }
                    }
                }
                if (1 != null) {
                    for (i = k + 1; i < this.n; i++) {
                        this.V[i][k] = e[i];
                    }
                }
            }
            k++;
        }
        int p = Math.min(this.n, this.m + 1);
        if (nct < this.n) {
            this.s[nct] = A[nct][nct];
        }
        if (this.m < p) {
            this.s[p - 1] = 0.0d;
        }
        if (nrt + 1 < p) {
            e[nrt] = A[nrt][p - 1];
        }
        e[p - 1] = 0.0d;
        if (1 != null) {
            for (j = nct; j < nu; j++) {
                for (i = 0; i < this.m; i++) {
                    this.U[i][j] = 0.0d;
                }
                this.U[j][j] = 1.0d;
            }
            for (k = nct - 1; k >= 0; k--) {
                if (this.s[k] != 0.0d) {
                    for (j = k + 1; j < nu; j++) {
                        t = 0.0d;
                        for (i = k; i < this.m; i++) {
                            t += this.U[i][k] * this.U[i][j];
                        }
                        t = (-t) / this.U[k][k];
                        for (i = k; i < this.m; i++) {
                            dArr = this.U[i];
                            dArr[j] = dArr[j] + (this.U[i][k] * t);
                        }
                    }
                    for (i = k; i < this.m; i++) {
                        this.U[i][k] = -this.U[i][k];
                    }
                    this.U[k][k] = 1.0d + this.U[k][k];
                    for (i = 0; i < k - 1; i++) {
                        this.U[i][k] = 0.0d;
                    }
                } else {
                    for (i = 0; i < this.m; i++) {
                        this.U[i][k] = 0.0d;
                    }
                    this.U[k][k] = 1.0d;
                }
            }
        }
        if (1 != null) {
            k = this.n - 1;
            while (k >= 0) {
                if (k < nrt && e[k] != 0.0d) {
                    for (j = k + 1; j < nu; j++) {
                        t = 0.0d;
                        for (i = k + 1; i < this.n; i++) {
                            t += this.V[i][k] * this.V[i][j];
                        }
                        t = (-t) / this.V[k + 1][k];
                        for (i = k + 1; i < this.n; i++) {
                            dArr = this.V[i];
                            dArr[j] = dArr[j] + (this.V[i][k] * t);
                        }
                    }
                }
                for (i = 0; i < this.n; i++) {
                    this.V[i][k] = 0.0d;
                }
                this.V[k][k] = 1.0d;
                k--;
            }
        }
        int pp = p - 1;
        int iter = 0;
        double eps = Math.pow(2.0d, -52.0d);
        double tiny = Math.pow(2.0d, -966.0d);
        while (p > 0) {
            int kase;
            k = p - 2;
            while (k >= -1 && k != -1) {
                if (Math.abs(e[k]) <= ((Math.abs(this.s[k]) + Math.abs(this.s[k + 1])) * eps) + tiny) {
                    e[k] = 0.0d;
                } else {
                    k--;
                }
            }
            if (k == p - 2) {
                kase = 4;
            } else {
                int ks = p - 1;
                while (ks >= k && ks != k) {
                    if (Math.abs(this.s[ks]) <= (eps * ((ks != p ? Math.abs(e[ks]) : 0.0d) + (ks != k + 1 ? Math.abs(e[ks - 1]) : 0.0d))) + tiny) {
                        this.s[ks] = 0.0d;
                    } else {
                        ks--;
                    }
                }
                if (ks == k) {
                    kase = 3;
                } else if (ks == p - 1) {
                    kase = 1;
                } else {
                    kase = 2;
                    k = ks;
                }
            }
            k++;
            double f;
            double cs;
            double sn;
            switch (kase) {
                case 1:
                    f = e[p - 2];
                    e[p - 2] = 0.0d;
                    for (j = p - 2; j >= k; j--) {
                        t = Matrix.hypot(this.s[j], f);
                        cs = this.s[j] / t;
                        sn = f / t;
                        this.s[j] = t;
                        if (j != k) {
                            f = (-sn) * e[j - 1];
                            e[j - 1] = e[j - 1] * cs;
                        }
                        if (1 != null) {
                            for (i = 0; i < this.n; i++) {
                                t = (this.V[i][j] * cs) + (this.V[i][p - 1] * sn);
                                this.V[i][p - 1] = ((-sn) * this.V[i][j]) + (this.V[i][p - 1] * cs);
                                this.V[i][j] = t;
                            }
                            break;
                        }
                    }
                    break;
                case 2:
                    f = e[k - 1];
                    e[k - 1] = 0.0d;
                    for (j = k; j < p; j++) {
                        t = Matrix.hypot(this.s[j], f);
                        cs = this.s[j] / t;
                        sn = f / t;
                        this.s[j] = t;
                        f = (-sn) * e[j];
                        e[j] = e[j] * cs;
                        if (1 != null) {
                            for (i = 0; i < this.m; i++) {
                                t = (this.U[i][j] * cs) + (this.U[i][k - 1] * sn);
                                this.U[i][k - 1] = ((-sn) * this.U[i][j]) + (this.U[i][k - 1] * cs);
                                this.U[i][j] = t;
                            }
                            break;
                        }
                    }
                    break;
                case 3:
                    int i3;
                    double scale = Math.max(Math.max(Math.max(Math.max(Math.abs(this.s[p - 1]), Math.abs(this.s[p - 2])), Math.abs(e[p - 2])), Math.abs(this.s[k])), Math.abs(e[k]));
                    double sp = this.s[p - 1] / scale;
                    double spm1 = this.s[p - 2] / scale;
                    double epm1 = e[p - 2] / scale;
                    double sk = this.s[k] / scale;
                    double ek = e[k] / scale;
                    double b = (((spm1 + sp) * (spm1 - sp)) + (epm1 * epm1)) / 2.0d;
                    double c = (sp * epm1) * (sp * epm1);
                    double shift = 0.0d;
                    if (b != 0.0d) {
                        i3 = 1;
                    } else {
                        i3 = 0;
                    }
                    if (c != 0.0d) {
                        i2 = 1;
                    } else {
                        i2 = 0;
                    }
                    if ((i2 | i3) != 0) {
                        shift = Math.sqrt((b * b) + c);
                        if (b < 0.0d) {
                            shift = -shift;
                        }
                        shift = c / (b + shift);
                    }
                    f = ((sk + sp) * (sk - sp)) + shift;
                    double g = sk * ek;
                    j = k;
                    while (j < p - 1) {
                        t = Matrix.hypot(f, g);
                        cs = f / t;
                        sn = g / t;
                        if (j != k) {
                            e[j - 1] = t;
                        }
                        f = (this.s[j] * cs) + (e[j] * sn);
                        e[j] = (e[j] * cs) - (this.s[j] * sn);
                        g = sn * this.s[j + 1];
                        this.s[j + 1] = this.s[j + 1] * cs;
                        if (1 != null) {
                            for (i = 0; i < this.n; i++) {
                                t = (this.V[i][j] * cs) + (this.V[i][j + 1] * sn);
                                this.V[i][j + 1] = ((-sn) * this.V[i][j]) + (this.V[i][j + 1] * cs);
                                this.V[i][j] = t;
                            }
                            break;
                        }
                        t = Matrix.hypot(f, g);
                        cs = f / t;
                        sn = g / t;
                        this.s[j] = t;
                        f = (e[j] * cs) + (this.s[j + 1] * sn);
                        this.s[j + 1] = ((-sn) * e[j]) + (this.s[j + 1] * cs);
                        g = sn * e[j + 1];
                        e[j + 1] = e[j + 1] * cs;
                        if (1 != null && j < this.m - 1) {
                            for (i = 0; i < this.m; i++) {
                                t = (this.U[i][j] * cs) + (this.U[i][j + 1] * sn);
                                this.U[i][j + 1] = ((-sn) * this.U[i][j]) + (this.U[i][j + 1] * cs);
                                this.U[i][j] = t;
                            }
                            break;
                        }
                        j++;
                    }
                    e[p - 2] = f;
                    iter++;
                    break;
                case 4:
                    if (this.s[k] <= 0.0d) {
                        this.s[k] = this.s[k] < 0.0d ? -this.s[k] : 0.0d;
                        if (1 != null) {
                            for (i = 0; i <= pp; i++) {
                                this.V[i][k] = -this.V[i][k];
                            }
                        }
                    }
                    while (k < pp && this.s[k] < this.s[k + 1]) {
                        t = this.s[k];
                        this.s[k] = this.s[k + 1];
                        this.s[k + 1] = t;
                        if (1 != null && k < this.n - 1) {
                            for (i = 0; i < this.n; i++) {
                                t = this.V[i][k + 1];
                                this.V[i][k + 1] = this.V[i][k];
                                this.V[i][k] = t;
                            }
                            break;
                        }
                        if (1 != null && k < this.m - 1) {
                            for (i = 0; i < this.m; i++) {
                                t = this.U[i][k + 1];
                                this.U[i][k + 1] = this.U[i][k];
                                this.U[i][k] = t;
                            }
                            break;
                        }
                        k++;
                    }
                    iter = 0;
                    p--;
                    break;
                default:
                    break;
            }
        }
    }

    public Matrix getU() {
        return new Matrix(this.U, this.m, Math.min(this.m + 1, this.n));
    }

    public Matrix getV() {
        return new Matrix(this.V, this.n, this.n);
    }

    public double[] getSingularValues() {
        return this.s;
    }

    public Matrix getS() {
        Matrix X = new Matrix(this.n, this.n);
        double[][] S = X.getArray();
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                S[i][j] = 0.0d;
            }
            S[i][i] = this.s[i];
        }
        return X;
    }

    public double norm2() {
        return this.s[0];
    }

    public double cond() {
        return this.s[0] / this.s[Math.min(this.m, this.n) - 1];
    }

    public int rank() {
        double tol = (((double) Math.max(this.m, this.n)) * this.s[0]) * Math.pow(2.0d, -52.0d);
        int r = 0;
        for (double d : this.s) {
            if (d > tol) {
                r++;
            }
        }
        return r;
    }
}
