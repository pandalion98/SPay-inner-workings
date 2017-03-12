package com.samsung.android.contextaware.aggregator.lpp.algorithm;

import android.util.Log;

public class KalmanFilter {
    Matrix F;
    Matrix H;
    int MeasurementSize;
    Matrix P_k;
    Matrix Q_k;
    Matrix R_k;
    int StateSize;
    String TAG = "KalmanFilter";
    Matrix X_k;
    boolean flagInitializeCovariance = false;
    boolean flagInitializeMeasurementM = false;
    boolean flagInitializeState = false;
    boolean flagMeasurementNoise = false;
    boolean flagProcessNoise = false;
    boolean flagTransitionMatrix = false;

    public KalmanFilter(int mState, int mMeasurement) {
        this.StateSize = mState;
        this.MeasurementSize = mMeasurement;
        this.X_k = new Matrix(this.StateSize, 1);
        this.P_k = new Matrix(this.StateSize, this.StateSize);
        this.Q_k = new Matrix(this.StateSize, this.StateSize);
        this.R_k = new Matrix(this.MeasurementSize, this.MeasurementSize);
        this.F = new Matrix(this.StateSize, this.StateSize);
        this.H = new Matrix(this.MeasurementSize, this.StateSize);
    }

    public boolean setInitialState(double[] vector) {
        if (vector.length != this.StateSize) {
            return false;
        }
        for (int inx = 0; inx < vector.length; inx++) {
            this.X_k.set(inx, 0, vector[inx]);
        }
        this.flagInitializeState = true;
        return true;
    }

    public boolean setInitialCovariance(double[][] mMatrix) {
        if (mMatrix[0].length != this.StateSize || mMatrix.length != this.StateSize) {
            return false;
        }
        for (int inx1 = 0; inx1 < this.StateSize; inx1++) {
            for (int inx2 = 0; inx2 < this.StateSize; inx2++) {
                this.P_k.set(inx1, inx2, mMatrix[inx1][inx2]);
            }
        }
        this.flagProcessNoise = true;
        return true;
    }

    public boolean setTransitionMatrix(double[][] mMatrix) {
        if (mMatrix[0].length != this.StateSize || mMatrix.length != this.StateSize) {
            return false;
        }
        for (int inx1 = 0; inx1 < this.StateSize; inx1++) {
            for (int inx2 = 0; inx2 < this.StateSize; inx2++) {
                this.F.set(inx1, inx2, mMatrix[inx1][inx2]);
            }
        }
        this.flagTransitionMatrix = true;
        return true;
    }

    public boolean setMeasurementMatrix(double[][] mMatrix) {
        if (mMatrix[0].length != this.StateSize || mMatrix.length != this.MeasurementSize) {
            return false;
        }
        for (int inx1 = 0; inx1 < this.MeasurementSize; inx1++) {
            for (int inx2 = 0; inx2 < this.StateSize; inx2++) {
                this.H.set(inx1, inx2, mMatrix[inx1][inx2]);
            }
        }
        this.flagInitializeMeasurementM = true;
        return true;
    }

    public boolean setProcessNoise(double[][] mMatrix) {
        if (mMatrix[0].length != this.StateSize || mMatrix.length != this.StateSize) {
            return false;
        }
        for (int inx1 = 0; inx1 < this.StateSize; inx1++) {
            for (int inx2 = 0; inx2 < this.StateSize; inx2++) {
                this.Q_k.set(inx1, inx2, mMatrix[inx1][inx2]);
            }
        }
        this.flagProcessNoise = true;
        return true;
    }

    public boolean setMeasurementNoise(double[][] mMatrix) {
        if (mMatrix[0].length != this.MeasurementSize || mMatrix.length != this.MeasurementSize) {
            return false;
        }
        for (int inx1 = 0; inx1 < this.MeasurementSize; inx1++) {
            for (int inx2 = 0; inx2 < this.MeasurementSize; inx2++) {
                this.R_k.set(inx1, inx2, mMatrix[inx1][inx2]);
            }
        }
        this.flagMeasurementNoise = true;
        return true;
    }

    public boolean TimePropagation(double delT) {
        Matrix X_k_1 = new Matrix(this.StateSize, 1);
        Matrix P_k_1 = new Matrix(this.StateSize, this.StateSize);
        Matrix Temp1 = new Matrix(this.StateSize, this.StateSize);
        Matrix Temp2 = new Matrix(this.StateSize, this.StateSize);
        if (this.flagTransitionMatrix && this.flagInitializeState && this.flagProcessNoise) {
            X_k_1.setMatrix(0, this.StateSize - 1, 0, 0, this.F.times(this.X_k));
            this.X_k.setMatrix(0, this.StateSize - 1, 0, 0, X_k_1);
            Temp1.setMatrix(0, this.StateSize - 1, 0, this.StateSize - 1, this.F.times(this.P_k));
            Temp2.setMatrix(0, this.StateSize - 1, 0, this.StateSize - 1, Temp1.times(this.F.inverse()));
            P_k_1.setMatrix(0, this.StateSize - 1, 0, this.StateSize - 1, Temp2.plus(this.Q_k));
            this.P_k.setMatrix(0, this.StateSize - 1, 0, this.StateSize - 1, P_k_1);
            return true;
        }
        Log.e(this.TAG, "cannot execute TimePropagation(), check initialization ");
        return false;
    }

    public boolean MeasurementUpdate(Matrix z) {
        if (z.getRowDimension() == this.MeasurementSize && z.getColumnDimension() == 1) {
            Matrix matrix = new Matrix(this.StateSize, this.MeasurementSize);
            Matrix Temp1 = new Matrix(this.StateSize, this.MeasurementSize);
            matrix = new Matrix(this.MeasurementSize, this.MeasurementSize);
            matrix = new Matrix(this.MeasurementSize, this.MeasurementSize);
            matrix = new Matrix(this.MeasurementSize, 1);
            matrix = new Matrix(this.MeasurementSize, 1);
            Matrix X_k_1 = new Matrix(this.StateSize, 1);
            Matrix P_ = new Matrix(this.StateSize, this.StateSize);
            if (this.flagProcessNoise && this.flagMeasurementNoise && this.flagInitializeMeasurementM) {
                Temp1.setMatrix(0, this.StateSize - 1, 0, this.MeasurementSize - 1, this.P_k.times(this.H.transpose()));
                matrix.setMatrix(0, this.MeasurementSize - 1, 0, this.MeasurementSize - 1, this.H.times(Temp1));
                Matrix matrix2 = matrix;
                matrix2.setMatrix(0, this.MeasurementSize - 1, 0, this.MeasurementSize - 1, matrix.plus(this.R_k));
                matrix.setMatrix(0, this.StateSize - 1, 0, this.MeasurementSize - 1, Temp1.times(matrix.inverse()));
                matrix.setMatrix(0, this.StateSize - 1, 0, 0, this.H.times(this.X_k));
                matrix.setMatrix(0, this.MeasurementSize - 1, 0, 0, z.minus(matrix));
                X_k_1.setMatrix(0, this.StateSize - 1, 0, 0, this.X_k.plus(matrix.times(matrix)));
                new Matrix(this.MeasurementSize, 1).setMatrix(0, this.MeasurementSize - 1, 0, 0, matrix.times(matrix));
                this.X_k.setMatrix(0, this.StateSize - 1, 0, 0, X_k_1);
                P_.setMatrix(0, this.StateSize - 1, 0, this.StateSize - 1, this.P_k.minus(matrix.times(this.H.times(this.P_k))));
                this.P_k.setMatrix(0, this.StateSize - 1, 0, this.StateSize - 1, P_);
                return true;
            }
            Log.e(this.TAG, "cannot execute MeasurementUpdate(), check initialization ");
            return false;
        }
        Log.e(this.TAG, "Error in MeasurementUpdate(), meauserement matrix size is wrong!");
        return false;
    }

    public double[] getCurrentState() {
        double[] mState = new double[this.StateSize];
        for (int inx = 0; inx < this.StateSize; inx++) {
            mState[inx] = this.X_k.get(inx, 0);
        }
        return mState;
    }
}
