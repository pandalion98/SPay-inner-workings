package com.samsung.android.spayfw.fraud.p013b;

import com.samsung.android.spayfw.fraud.FraudModelInfo;
import com.samsung.android.spayfw.fraud.RiskModelSkeleton;

/* renamed from: com.samsung.android.spayfw.fraud.b.c */
class NeuralNetV1 extends RiskModelSkeleton {
    public NeuralNetV1(FraudModelInfo fraudModelInfo) {
        super(fraudModelInfo);
    }

    protected float by() {
        return 0.0055f;
    }

    protected byte[] bz() {
        return null;
    }
}
