package com.samsung.android.spayfw.fraud;

import com.samsung.android.spayfw.fraud.p013b.FraudRiskModels;
import java.lang.ref.SoftReference;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/* renamed from: com.samsung.android.spayfw.fraud.f */
public class FraudRiskMachine {
    static List<String> nm;
    List<String> nn;
    HashMap<String, SoftReference<RiskModelSkeleton>> no;
    private String np;

    static {
        nm = Arrays.asList(new String[]{"enrollcard.simple-risk-score-1.model"});
    }

    FraudRiskMachine(String str) {
        this.no = new HashMap();
        this.np = str;
        this.nn = ModelCache.ac(this.np);
    }

    public List<FraudModelSnapshot> bx() {
        List<FraudModelSnapshot> linkedList = new LinkedList();
        for (String aa : this.nn) {
            linkedList.add(aa(aa).bA());
        }
        if (linkedList.isEmpty()) {
            linkedList.add(FraudRiskModels.bJ().bA());
        }
        return linkedList;
    }

    private RiskModelSkeleton m747Z(String str) {
        RiskModelSkeleton a = FraudRiskModels.m725a(ModelCache.ab(str));
        this.no.put(str, new SoftReference(a));
        return a;
    }

    private RiskModelSkeleton aa(String str) {
        if (!this.no.containsKey(str)) {
            return m747Z(str);
        }
        RiskModelSkeleton riskModelSkeleton = (RiskModelSkeleton) ((SoftReference) this.no.get(str)).get();
        if (riskModelSkeleton == null) {
            return m747Z(str);
        }
        return riskModelSkeleton;
    }
}
