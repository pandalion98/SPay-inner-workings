package com.samsung.android.spayfw.fraud;

import com.samsung.android.spayfw.fraud.p013b.FraudRiskModels;
import com.samsung.android.spayfw.utils.Utils;

/* renamed from: com.samsung.android.spayfw.fraud.h */
public abstract class RiskModelSkeleton {
    protected final float nA;
    protected final float nB;
    protected final float nC;
    protected final float nD;
    protected final float nE;
    protected final float nF;
    private String nv;
    private String nw;
    protected final float nx;
    protected final float ny;
    protected final float nz;

    protected abstract float by();

    protected abstract byte[] bz();

    public RiskModelSkeleton(FraudModelInfo fraudModelInfo) {
        this.nx = 1.0E-4f;
        this.ny = 0.001f;
        this.nz = 0.01f;
        this.nA = 0.1f;
        this.nB = 0.0f;
        this.nC = 5.5E-4f;
        this.nD = 0.0055f;
        this.nE = 0.055f;
        this.nF = 1.0f;
        this.nv = fraudModelInfo.nh;
        this.nw = fraudModelInfo.ng;
    }

    public FraudModelSnapshot bA() {
        FraudModelSnapshot fraudModelSnapshot = new FraudModelSnapshot();
        Integer af = FraudRiskModels.af(this.nw);
        if (af == null) {
            Utils.m1274a(new NullPointerException("No version number associated with model " + this.nw + ". Make sure a version number is set in FraudRiskModels."));
            return FraudRiskModels.bJ().bA();
        }
        fraudModelSnapshot.nj = af.intValue();
        fraudModelSnapshot.nh = this.nv;
        float by = by();
        fraudModelSnapshot.nl = bz();
        if (by < 1.0E-4f) {
            fraudModelSnapshot.nk = 5;
        } else if (by < 0.001f) {
            fraudModelSnapshot.nk = 4;
        } else if (by < 0.01f) {
            fraudModelSnapshot.nk = 3;
        } else if (by < 0.1f) {
            fraudModelSnapshot.nk = 2;
        } else {
            fraudModelSnapshot.nk = 1;
        }
        if (fraudModelSnapshot.nk != 1) {
            return fraudModelSnapshot;
        }
        fraudModelSnapshot.nk = 2;
        return fraudModelSnapshot;
    }
}
