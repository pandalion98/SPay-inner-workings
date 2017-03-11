package com.samsung.android.spayfw.fraud.p013b;

import com.samsung.android.spayfw.fraud.FraudModelInfo;
import com.samsung.android.spayfw.fraud.RiskModelSkeleton;
import com.samsung.android.spayfw.utils.Utils;
import java.util.HashMap;

/* renamed from: com.samsung.android.spayfw.fraud.b.a */
public class FraudRiskModels {
    private static final HashMap<String, Integer> om;

    static {
        om = new HashMap();
        om.put("simpleriskscore-v1", Integer.valueOf(1));
        om.put("neuralnet-v1", Integer.valueOf(2));
    }

    public static RiskModelSkeleton m725a(FraudModelInfo fraudModelInfo) {
        if (fraudModelInfo == null) {
            return FraudRiskModels.bJ();
        }
        if (Utils.DEBUG && "neuralnet-v1".equals(fraudModelInfo.ng)) {
            return new NeuralNetV1(fraudModelInfo);
        }
        if ("simpleriskscore-v1".equals(fraudModelInfo.ng)) {
            return new SimpleRiskScoreV1(fraudModelInfo);
        }
        Utils.m1274a(new IllegalArgumentException("Illegal modelInfo.modelBase = " + fraudModelInfo.ng + ". Must be one of FraudRiskModels.MODEL_BASE_*."));
        return FraudRiskModels.bJ();
    }

    public static RiskModelSkeleton bJ() {
        FraudModelInfo fraudModelInfo = new FraudModelInfo();
        fraudModelInfo.ng = "simpleriskscore-v1";
        fraudModelInfo.nh = "simpleriskscore-default";
        return FraudRiskModels.m725a(fraudModelInfo);
    }

    public static Integer af(String str) {
        return (Integer) om.get(str);
    }
}
