/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.location.Location
 *  android.os.Bundle
 *  java.io.Serializable
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Locale
 *  java.util.Map
 */
package com.samsung.android.spayfw.payprovider.mastercard.tokenmanagement;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import com.samsung.android.spayfw.appinterface.ProvisionTokenInfo;
import com.samsung.android.spayfw.fraud.b;
import com.samsung.android.spayfw.fraud.d;
import com.samsung.android.spayfw.payprovider.RiskDataParam;
import com.samsung.android.spayfw.payprovider.c;
import com.samsung.android.spayfw.payprovider.mastercard.McProvider;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class McDecisioningData {
    private static final int ACCOUNT_SCORE_PERIOD_IN_DAYS = 30;
    private static final int DEVICE_SCORE_PERIOD_IN_DAYS = 30;
    private static final String FRAUD_DATA_PROVIDER_VALUE = "fraud data provider value";
    private static McDecisioningData mMcDecisioningData;
    private final String ACCOUNT_HASH = "accountIdHash";
    private final String ACCOUNT_SCORE = "accountScore";
    private final String DEVICE_CURRENT_LOCATION = "deviceCurrentLocation";
    private final String DEVICE_SCORE = "deviceScore";
    private final String MOBILE_NUMBER_SUFFIX = "mobileNumberSuffix";
    private final String RECOMMENDATION_REASONS = "recommendationReasons";
    private final String TAG = this.getClass().getSimpleName();
    private Map<String, String> mAddAuthDataMap;
    private String mDeviceCountryCode;
    private b mfdp;

    private McDecisioningData() {
    }

    /*
     * Exception decompiling
     */
    private List<String> getAdditionalAuthenticationReasons() {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Extractable last case doesn't follow previous
        // org.benf.cfr.reader.b.a.a.b.as.a(SwitchReplacer.java:478)
        // org.benf.cfr.reader.b.a.a.b.as.a(SwitchReplacer.java:61)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:372)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:182)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:127)
        // org.benf.cfr.reader.entities.attributes.f.c(AttributeCode.java:96)
        // org.benf.cfr.reader.entities.g.p(Method.java:396)
        // org.benf.cfr.reader.entities.d.e(ClassFile.java:890)
        // org.benf.cfr.reader.entities.d.b(ClassFile.java:792)
        // org.benf.cfr.reader.b.a(Driver.java:128)
        // org.benf.cfr.reader.a.a(CfrDriverImpl.java:63)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.decompileWithCFR(JavaExtractionWorker.kt:61)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.doWork(JavaExtractionWorker.kt:130)
        // com.njlabs.showjava.decompilers.BaseDecompiler.withAttempt(BaseDecompiler.kt:108)
        // com.njlabs.showjava.workers.DecompilerWorker$b.run(DecompilerWorker.kt:118)
        // java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
        // java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
        // java.lang.Thread.run(Thread.java:764)
        throw new IllegalStateException("Decompilation failed");
    }

    private String getDecimal(double d2) {
        Locale locale = new Locale("en", "US");
        Object[] arrobject = new Object[]{d2};
        return String.format((Locale)locale, (String)"%.2f", (Object[])arrobject);
    }

    private Location getDeviceLocation() {
        return DeviceInfo.getLastKnownLocation(McProvider.getContext());
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static McDecisioningData getInstance() {
        Class<McDecisioningData> class_ = McDecisioningData.class;
        synchronized (McDecisioningData.class) {
            if (mMcDecisioningData != null) return mMcDecisioningData;
            mMcDecisioningData = new McDecisioningData();
            return mMcDecisioningData;
        }
    }

    private String getMobileNumberSuffix() {
        String string = DeviceInfo.getMsisdn(McProvider.getContext());
        if (this.isStringValid(string) && string.length() >= 4) {
            int n2 = string.length();
            int n3 = n2 - 4;
            if (n3 < 0) {
                n3 = 0;
            }
            return string.substring(n3, n2);
        }
        return null;
    }

    private boolean isStringArrayValid(List<String> list) {
        return list != null && !list.isEmpty();
    }

    private boolean isStringValid(String string) {
        return string != null && !string.isEmpty();
    }

    /*
     * Enabled aggressive block sorting
     */
    public c populateRiskData() {
        Location location;
        List<String> list;
        String string;
        String string2;
        ArrayList arrayList = new ArrayList();
        String string3 = (String)this.mAddAuthDataMap.get((Object)"deviceScore");
        String string4 = string3 == null ? String.valueOf((int)this.mfdp.E((int)30).nk) : string3;
        String string5 = (String)this.mAddAuthDataMap.get((Object)"accountScore");
        if (string5 == null) {
            string5 = String.valueOf((int)((int)this.mfdp.D(30)));
        }
        com.samsung.android.spayfw.b.c.d(this.TAG, "deviceScore: " + string4 + ", accountScore: " + string5);
        if (this.isStringValid(string4)) {
            arrayList.add((Object)new RiskDataParam("deviceScore", string4));
        }
        if (this.isStringValid(string5)) {
            arrayList.add((Object)new RiskDataParam("accountScore", string5));
        }
        if (this.isStringArrayValid(list = this.getAdditionalAuthenticationReasons())) {
            arrayList.add((Object)new RiskDataParam("recommendationReasons", list));
        }
        if ((location = this.getDeviceLocation()) != null) {
            arrayList.add((Object)new RiskDataParam("deviceCurrentLocation", this.getDecimal(location.getLatitude()) + "," + this.getDecimal(location.getLongitude())));
        }
        if (this.isStringValid(string2 = McProvider.getEmailHash())) {
            arrayList.add((Object)new RiskDataParam("accountIdHash", string2));
        }
        if (this.isStringValid(string = this.getMobileNumberSuffix())) {
            arrayList.add((Object)new RiskDataParam("mobileNumberSuffix", string));
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("riskData", (Serializable)arrayList);
        c c2 = new c();
        c2.e(bundle);
        return c2;
    }

    /*
     * Enabled aggressive block sorting
     */
    public McDecisioningData setRiskData(ProvisionTokenInfo provisionTokenInfo) {
        this.mAddAuthDataMap = new HashMap();
        this.mfdp = new b(McProvider.getContext());
        for (AddAuthRecommendations addAuthRecommendations : AddAuthRecommendations.values()) {
            String string = addAuthRecommendations.getRiskDataName();
            com.samsung.android.spayfw.b.c.d(this.TAG, " AddAuthRecommendations name : " + addAuthRecommendations.name() + ", is " + "equal : " + addAuthRecommendations.name().equals((Object)string));
            if (addAuthRecommendations.name().equals((Object)string)) {
                this.mAddAuthDataMap.put((Object)string, (Object)FRAUD_DATA_PROVIDER_VALUE);
                continue;
            }
            String string2 = (String)provisionTokenInfo.getActivationParams().get((Object)string);
            if (this.isStringValid(string2)) {
                this.mAddAuthDataMap.put((Object)string, (Object)string2);
            }
            com.samsung.android.spayfw.b.c.d(this.TAG, " AddAuth Data, riskName : " + string + " , data : " + string2);
        }
        this.mDeviceCountryCode = (String)provisionTokenInfo.getActivationParams().get((Object)"deviceCountry");
        if (this.mDeviceCountryCode == null) {
            this.mDeviceCountryCode = DeviceInfo.getDeviceCountry().toLowerCase();
        }
        com.samsung.android.spayfw.b.c.d(this.TAG, " mDeviceCountryCode : " + this.mDeviceCountryCode);
        return this;
    }

    private static final class AddAuthRecommendations
    extends Enum<AddAuthRecommendations> {
        private static final /* synthetic */ AddAuthRecommendations[] $VALUES;
        public static final /* enum */ AddAuthRecommendations HAS_SUSPENDED_TOKENS = new AddAuthRecommendations("HAS_SUSPENDED_TOKENS");
        public static final /* enum */ AddAuthRecommendations OUTSIDE_HOME_TERRITORY;
        public static final /* enum */ AddAuthRecommendations TOO_MANY_DIFFERENT_CARDHOLDERS;
        public static final /* enum */ AddAuthRecommendations TOO_MANY_RECENT_ATTEMPTS;
        public static final /* enum */ AddAuthRecommendations TOO_MANY_RECENT_TOKENS;
        static Map<String, AddAuthRecommendations> mAddAuthRecommendationsMap;
        private String mRiskDataName;

        static {
            TOO_MANY_RECENT_ATTEMPTS = new AddAuthRecommendations("TOO_MANY_RECENT_ATTEMPTS");
            TOO_MANY_RECENT_TOKENS = new AddAuthRecommendations("TOO_MANY_RECENT_TOKENS");
            TOO_MANY_DIFFERENT_CARDHOLDERS = new AddAuthRecommendations("TOO_MANY_DIFFERENT_CARDHOLDERS");
            OUTSIDE_HOME_TERRITORY = new AddAuthRecommendations("billingCountryCode");
            AddAuthRecommendations[] arraddAuthRecommendations = new AddAuthRecommendations[]{HAS_SUSPENDED_TOKENS, TOO_MANY_RECENT_ATTEMPTS, TOO_MANY_RECENT_TOKENS, TOO_MANY_DIFFERENT_CARDHOLDERS, OUTSIDE_HOME_TERRITORY};
            $VALUES = arraddAuthRecommendations;
            mAddAuthRecommendationsMap = new HashMap();
            for (AddAuthRecommendations addAuthRecommendations : AddAuthRecommendations.values()) {
                mAddAuthRecommendationsMap.put((Object)addAuthRecommendations.getRiskDataName(), (Object)addAuthRecommendations);
            }
        }

        private AddAuthRecommendations(String string2) {
            this.mRiskDataName = string2;
        }

        public static AddAuthRecommendations getAddAuthRecommendations(String string) {
            return (AddAuthRecommendations)((Object)mAddAuthRecommendationsMap.get((Object)string));
        }

        public static AddAuthRecommendations valueOf(String string) {
            return (AddAuthRecommendations)Enum.valueOf(AddAuthRecommendations.class, (String)string);
        }

        public static AddAuthRecommendations[] values() {
            return (AddAuthRecommendations[])$VALUES.clone();
        }

        public String getRiskDataName() {
            return this.mRiskDataName;
        }
    }

}

