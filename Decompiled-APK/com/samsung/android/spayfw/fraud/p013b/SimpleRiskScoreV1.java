package com.samsung.android.spayfw.fraud.p013b;

import com.samsung.android.spayfw.fraud.FraudModelInfo;
import com.samsung.android.spayfw.fraud.RiskModelSkeleton;
import com.samsung.android.spayfw.fraud.p013b.ModelUtils.ModelUtils;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.utils.Utils;
import java.util.List;

/* renamed from: com.samsung.android.spayfw.fraud.b.d */
class SimpleRiskScoreV1 extends RiskModelSkeleton {
    public SimpleRiskScoreV1(FraudModelInfo fraudModelInfo) {
        super(fraudModelInfo);
    }

    protected float by() {
        int[] iArr = new int[6];
        ModelUtils bK = ModelUtils.bK();
        ModelUtils bL = ModelUtils.bL();
        if (bK == null || bL == null) {
            Utils.m1274a(new NullPointerException("The database does not contain cardholder info tied to this enrollment."));
            return 0.0055f;
        }
        List bQ = ModelUtils.bM().bV().bR().bW().bQ();
        List bQ2 = ModelUtils.bM().bU().bV().bR().bQ();
        List bQ3 = ModelUtils.bM().bV().bS().bP().bQ();
        List bQ4 = ModelUtils.bM().bU().bV().bS().bQ();
        int a = ModelUtils.m727a(bK, bQ2);
        int b = ModelUtils.m732b(bK, bQ);
        if (a >= 2) {
            iArr[5] = iArr[5] + 1;
        } else if (a == 1) {
            iArr[4] = iArr[4] + 1;
        } else if (b >= 2) {
            return 1.0f;
        } else {
            if (b == 1) {
                iArr[2] = iArr[2] + 1;
            }
        }
        int a2 = ModelUtils.m726a(bL, bQ4);
        int b2 = ModelUtils.m731b(bL, bQ3);
        Log.m285d("SimpleRiskScoreV1", "matchingNameCount=" + a + ", nonmatchingNameCount=" + b + ", matchingAddressCount= " + a2 + ", nonmatchingAddressCount= " + b2);
        if (a2 >= 2) {
            iArr[5] = iArr[5] + 1;
        } else if (a2 == 1) {
            iArr[4] = iArr[4] + 1;
        } else if (b2 >= 3) {
            iArr[1] = iArr[1] + 1;
        } else if (b2 == 2) {
            iArr[2] = iArr[2] + 1;
        }
        b = ModelUtils.ag("24 hour");
        Log.m285d("SimpleRiskScoreV1", "recent provisioning attempts: " + b);
        if (b >= 6) {
            iArr[2] = iArr[2] + 1;
        }
        if (b >= 9) {
            iArr[1] = iArr[1] + 1;
        }
        b = ModelUtils.ah("7 day");
        Log.m285d("SimpleRiskScoreV1", "recent reset count: " + b);
        if (b > 2) {
            iArr[2] = iArr[2] + 1;
        }
        if (b > 10) {
            iArr[1] = iArr[1] + 1;
        }
        b = ModelUtils.ai("6 month");
        Log.m285d("SimpleRiskScoreV1", "6 month token count: " + b);
        if (b > 0) {
            iArr[4] = iArr[4] + 1;
        }
        b = ModelUtils.ai("12 month");
        Log.m285d("SimpleRiskScoreV1", "12 month token count: " + b);
        if (b > 0) {
            iArr[5] = iArr[5] + 1;
        }
        if (!Utils.DEBUG || (iArr[0] == 0 && iArr[3] == 0)) {
            for (b = 0; b < iArr.length; b++) {
                Log.m285d("SimpleRiskScoreV1", "bucket[" + b + "] = " + iArr[b]);
            }
            if (iArr[1] >= 2) {
                return 1.0f;
            }
            if (iArr[1] + iArr[2] >= 2) {
                return 0.055f;
            }
            if (iArr[5] >= 2) {
                return 0.0f;
            }
            return iArr[4] + iArr[5] >= 2 ? 5.5E-4f : 0.0055f;
        } else {
            throw new IllegalStateException("Buckets 0 and 3 only exist to make bucket indexes intuitive. They should not be used.");
        }
    }

    protected byte[] bz() {
        return null;
    }
}
