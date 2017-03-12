package com.samsung.android.spayfw.payprovider.mastercard.tokenmanagement;

import android.location.Location;
import android.os.Bundle;
import com.samsung.android.spayfw.appinterface.ActivationData;
import com.samsung.android.spayfw.appinterface.ProvisionTokenInfo;
import com.samsung.android.spayfw.fraud.FraudDataProvider;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.ProviderRequestData;
import com.samsung.android.spayfw.payprovider.RiskDataParam;
import com.samsung.android.spayfw.payprovider.mastercard.McProvider;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
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
    private final String ACCOUNT_HASH;
    private final String ACCOUNT_SCORE;
    private final String DEVICE_CURRENT_LOCATION;
    private final String DEVICE_SCORE;
    private final String MOBILE_NUMBER_SUFFIX;
    private final String RECOMMENDATION_REASONS;
    private final String TAG;
    private Map<String, String> mAddAuthDataMap;
    private String mDeviceCountryCode;
    private FraudDataProvider mfdp;

    /* renamed from: com.samsung.android.spayfw.payprovider.mastercard.tokenmanagement.McDecisioningData.1 */
    static /* synthetic */ class C05711 {
        static final /* synthetic */ int[] f15xd288556e;

        static {
            f15xd288556e = new int[AddAuthRecommendations.values().length];
            try {
                f15xd288556e[AddAuthRecommendations.HAS_SUSPENDED_TOKENS.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f15xd288556e[AddAuthRecommendations.TOO_MANY_RECENT_ATTEMPTS.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f15xd288556e[AddAuthRecommendations.TOO_MANY_RECENT_TOKENS.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f15xd288556e[AddAuthRecommendations.TOO_MANY_DIFFERENT_CARDHOLDERS.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                f15xd288556e[AddAuthRecommendations.OUTSIDE_HOME_TERRITORY.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    private enum AddAuthRecommendations {
        HAS_SUSPENDED_TOKENS("HAS_SUSPENDED_TOKENS"),
        TOO_MANY_RECENT_ATTEMPTS("TOO_MANY_RECENT_ATTEMPTS"),
        TOO_MANY_RECENT_TOKENS("TOO_MANY_RECENT_TOKENS"),
        TOO_MANY_DIFFERENT_CARDHOLDERS("TOO_MANY_DIFFERENT_CARDHOLDERS"),
        OUTSIDE_HOME_TERRITORY(ActivationData.CARD_INFO_BILLING_COUNTRY);
        
        static Map<String, AddAuthRecommendations> mAddAuthRecommendationsMap;
        private String mRiskDataName;

        static {
            mAddAuthRecommendationsMap = new HashMap();
            AddAuthRecommendations[] values = values();
            int length = values.length;
            int i;
            while (i < length) {
                AddAuthRecommendations addAuthRecommendations = values[i];
                mAddAuthRecommendationsMap.put(addAuthRecommendations.getRiskDataName(), addAuthRecommendations);
                i++;
            }
        }

        private AddAuthRecommendations(String str) {
            this.mRiskDataName = str;
        }

        public String getRiskDataName() {
            return this.mRiskDataName;
        }

        public static AddAuthRecommendations getAddAuthRecommendations(String str) {
            return (AddAuthRecommendations) mAddAuthRecommendationsMap.get(str);
        }
    }

    public static McDecisioningData getInstance() {
        McDecisioningData mcDecisioningData;
        synchronized (McDecisioningData.class) {
            if (mMcDecisioningData == null) {
                mMcDecisioningData = new McDecisioningData();
            }
            mcDecisioningData = mMcDecisioningData;
        }
        return mcDecisioningData;
    }

    private McDecisioningData() {
        this.TAG = getClass().getSimpleName();
        this.DEVICE_SCORE = ActivationData.DEVICE_SCORE;
        this.ACCOUNT_SCORE = ActivationData.WALLET_ACCOUNT_SCORE;
        this.RECOMMENDATION_REASONS = "recommendationReasons";
        this.DEVICE_CURRENT_LOCATION = "deviceCurrentLocation";
        this.MOBILE_NUMBER_SUFFIX = "mobileNumberSuffix";
        this.ACCOUNT_HASH = "accountIdHash";
    }

    public McDecisioningData setRiskData(ProvisionTokenInfo provisionTokenInfo) {
        this.mAddAuthDataMap = new HashMap();
        this.mfdp = new FraudDataProvider(McProvider.getContext());
        for (AddAuthRecommendations addAuthRecommendations : AddAuthRecommendations.values()) {
            String riskDataName = addAuthRecommendations.getRiskDataName();
            Log.m285d(this.TAG, " AddAuthRecommendations name : " + addAuthRecommendations.name() + ", is " + "equal : " + addAuthRecommendations.name().equals(riskDataName));
            if (addAuthRecommendations.name().equals(riskDataName)) {
                this.mAddAuthDataMap.put(riskDataName, FRAUD_DATA_PROVIDER_VALUE);
            } else {
                String str = (String) provisionTokenInfo.getActivationParams().get(riskDataName);
                if (isStringValid(str)) {
                    this.mAddAuthDataMap.put(riskDataName, str);
                }
                Log.m285d(this.TAG, " AddAuth Data, riskName : " + riskDataName + " , data : " + str);
            }
        }
        this.mDeviceCountryCode = (String) provisionTokenInfo.getActivationParams().get(ActivationData.DEVICE_COUNTRY_CODE);
        if (this.mDeviceCountryCode == null) {
            this.mDeviceCountryCode = DeviceInfo.getDeviceCountry().toLowerCase();
        }
        Log.m285d(this.TAG, " mDeviceCountryCode : " + this.mDeviceCountryCode);
        return this;
    }

    public ProviderRequestData populateRiskData() {
        String valueOf;
        Serializable arrayList = new ArrayList();
        String str = TransactionInfo.VISA_TRANSACTIONSTATUS_DECLINED;
        str = TransactionInfo.VISA_TRANSACTIONSTATUS_DECLINED;
        str = (String) this.mAddAuthDataMap.get(ActivationData.DEVICE_SCORE);
        if (str == null) {
            valueOf = String.valueOf(this.mfdp.m742E(DEVICE_SCORE_PERIOD_IN_DAYS).nk);
        } else {
            valueOf = str;
        }
        str = (String) this.mAddAuthDataMap.get(ActivationData.WALLET_ACCOUNT_SCORE);
        if (str == null) {
            str = String.valueOf((int) this.mfdp.m741D(DEVICE_SCORE_PERIOD_IN_DAYS));
        }
        Log.m285d(this.TAG, "deviceScore: " + valueOf + ", accountScore: " + str);
        if (isStringValid(valueOf)) {
            arrayList.add(new RiskDataParam(ActivationData.DEVICE_SCORE, valueOf));
        }
        if (isStringValid(str)) {
            arrayList.add(new RiskDataParam(ActivationData.WALLET_ACCOUNT_SCORE, str));
        }
        List additionalAuthenticationReasons = getAdditionalAuthenticationReasons();
        if (isStringArrayValid(additionalAuthenticationReasons)) {
            arrayList.add(new RiskDataParam("recommendationReasons", additionalAuthenticationReasons));
        }
        Location deviceLocation = getDeviceLocation();
        if (deviceLocation != null) {
            arrayList.add(new RiskDataParam("deviceCurrentLocation", getDecimal(deviceLocation.getLatitude()) + "," + getDecimal(deviceLocation.getLongitude())));
        }
        str = McProvider.getEmailHash();
        if (isStringValid(str)) {
            arrayList.add(new RiskDataParam("accountIdHash", str));
        }
        str = getMobileNumberSuffix();
        if (isStringValid(str)) {
            arrayList.add(new RiskDataParam("mobileNumberSuffix", str));
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("riskData", arrayList);
        ProviderRequestData providerRequestData = new ProviderRequestData();
        providerRequestData.m823e(bundle);
        return providerRequestData;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.util.List<java.lang.String> getAdditionalAuthenticationReasons() {
        /*
        r7 = this;
        r2 = new java.util.ArrayList;
        r2.<init>();
        r0 = r7.mAddAuthDataMap;
        r0 = r0.entrySet();
        r3 = r0.iterator();
    L_0x000f:
        r0 = r3.hasNext();
        if (r0 == 0) goto L_0x0194;
    L_0x0015:
        r0 = r3.next();
        r0 = (java.util.Map.Entry) r0;
        r1 = r0.getKey();
        r1 = (java.lang.String) r1;
        r1 = com.samsung.android.spayfw.payprovider.mastercard.tokenmanagement.McDecisioningData.AddAuthRecommendations.getAddAuthRecommendations(r1);
        r0 = r0.getValue();
        r0 = (java.lang.String) r0;
        r4 = r7.TAG;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "recommendation: ";
        r5 = r5.append(r6);
        r6 = r1.name();
        r5 = r5.append(r6);
        r6 = ", getRiskDataName: ";
        r5 = r5.append(r6);
        r6 = r1.getRiskDataName();
        r5 = r5.append(r6);
        r6 = ", value: ";
        r5 = r5.append(r6);
        r5 = r5.append(r0);
        r5 = r5.toString();
        com.samsung.android.spayfw.p002b.Log.m285d(r4, r5);
        r4 = com.samsung.android.spayfw.payprovider.mastercard.tokenmanagement.McDecisioningData.C05711.f15xd288556e;	 Catch:{ Exception -> 0x009a }
        r5 = r1.ordinal();	 Catch:{ Exception -> 0x009a }
        r4 = r4[r5];	 Catch:{ Exception -> 0x009a }
        switch(r4) {
            case 1: goto L_0x006b;
            case 2: goto L_0x00bc;
            case 3: goto L_0x00f1;
            case 4: goto L_0x0125;
            case 5: goto L_0x0157;
            default: goto L_0x006a;
        };	 Catch:{ Exception -> 0x009a }
    L_0x006a:
        goto L_0x000f;
    L_0x006b:
        r0 = r7.mfdp;	 Catch:{ Exception -> 0x009a }
        r0 = r0.bw();	 Catch:{ Exception -> 0x009a }
        if (r0 <= 0) goto L_0x007a;
    L_0x0073:
        r0 = r1.name();	 Catch:{ Exception -> 0x009a }
        r2.add(r0);	 Catch:{ Exception -> 0x009a }
    L_0x007a:
        r0 = r7.TAG;	 Catch:{ Exception -> 0x009a }
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x009a }
        r4.<init>();	 Catch:{ Exception -> 0x009a }
        r5 = " getSuspendedCardsCount : ";
        r4 = r4.append(r5);	 Catch:{ Exception -> 0x009a }
        r5 = r7.mfdp;	 Catch:{ Exception -> 0x009a }
        r5 = r5.bw();	 Catch:{ Exception -> 0x009a }
        r4 = r4.append(r5);	 Catch:{ Exception -> 0x009a }
        r4 = r4.toString();	 Catch:{ Exception -> 0x009a }
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r4);	 Catch:{ Exception -> 0x009a }
        goto L_0x000f;
    L_0x009a:
        r0 = move-exception;
        r4 = r7.TAG;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "Wrong data format : ";
        r5 = r5.append(r6);
        r1 = r1.name();
        r1 = r5.append(r1);
        r1 = r1.toString();
        com.samsung.android.spayfw.p002b.Log.m286e(r4, r1);
        r0.printStackTrace();
        goto L_0x000f;
    L_0x00bc:
        r0 = r7.mfdp;	 Catch:{ Exception -> 0x009a }
        r4 = 10;
        r0 = r0.m743x(r4);	 Catch:{ Exception -> 0x009a }
        r4 = 9;
        if (r0 <= r4) goto L_0x00cf;
    L_0x00c8:
        r0 = r1.name();	 Catch:{ Exception -> 0x009a }
        r2.add(r0);	 Catch:{ Exception -> 0x009a }
    L_0x00cf:
        r0 = r7.TAG;	 Catch:{ Exception -> 0x009a }
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x009a }
        r4.<init>();	 Catch:{ Exception -> 0x009a }
        r5 = " getProvisioningAttemptCount in 10 days : ";
        r4 = r4.append(r5);	 Catch:{ Exception -> 0x009a }
        r5 = r7.mfdp;	 Catch:{ Exception -> 0x009a }
        r6 = 10;
        r5 = r5.m743x(r6);	 Catch:{ Exception -> 0x009a }
        r4 = r4.append(r5);	 Catch:{ Exception -> 0x009a }
        r4 = r4.toString();	 Catch:{ Exception -> 0x009a }
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r4);	 Catch:{ Exception -> 0x009a }
        goto L_0x000f;
    L_0x00f1:
        r0 = r7.mfdp;	 Catch:{ Exception -> 0x009a }
        r4 = 10;
        r0 = r0.m744y(r4);	 Catch:{ Exception -> 0x009a }
        r4 = 6;
        if (r0 <= r4) goto L_0x0103;
    L_0x00fc:
        r0 = r1.name();	 Catch:{ Exception -> 0x009a }
        r2.add(r0);	 Catch:{ Exception -> 0x009a }
    L_0x0103:
        r0 = r7.TAG;	 Catch:{ Exception -> 0x009a }
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x009a }
        r4.<init>();	 Catch:{ Exception -> 0x009a }
        r5 = " getProvisionedCardCount in 10 days : ";
        r4 = r4.append(r5);	 Catch:{ Exception -> 0x009a }
        r5 = r7.mfdp;	 Catch:{ Exception -> 0x009a }
        r6 = 10;
        r5 = r5.m743x(r6);	 Catch:{ Exception -> 0x009a }
        r4 = r4.append(r5);	 Catch:{ Exception -> 0x009a }
        r4 = r4.toString();	 Catch:{ Exception -> 0x009a }
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r4);	 Catch:{ Exception -> 0x009a }
        goto L_0x000f;
    L_0x0125:
        r0 = r7.mfdp;	 Catch:{ Exception -> 0x009a }
        r4 = -1;
        r0 = r0.m738A(r4);	 Catch:{ Exception -> 0x009a }
        r4 = 3;
        if (r0 <= r4) goto L_0x0136;
    L_0x012f:
        r0 = r1.name();	 Catch:{ Exception -> 0x009a }
        r2.add(r0);	 Catch:{ Exception -> 0x009a }
    L_0x0136:
        r0 = r7.TAG;	 Catch:{ Exception -> 0x009a }
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x009a }
        r4.<init>();	 Catch:{ Exception -> 0x009a }
        r5 = " getProvisionedNameCount: ";
        r4 = r4.append(r5);	 Catch:{ Exception -> 0x009a }
        r5 = r7.mfdp;	 Catch:{ Exception -> 0x009a }
        r6 = -1;
        r5 = r5.m738A(r6);	 Catch:{ Exception -> 0x009a }
        r4 = r4.append(r5);	 Catch:{ Exception -> 0x009a }
        r4 = r4.toString();	 Catch:{ Exception -> 0x009a }
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r4);	 Catch:{ Exception -> 0x009a }
        goto L_0x000f;
    L_0x0157:
        r4 = r7.mDeviceCountryCode;	 Catch:{ Exception -> 0x009a }
        r4 = r7.isStringValid(r4);	 Catch:{ Exception -> 0x009a }
        if (r4 == 0) goto L_0x000f;
    L_0x015f:
        r4 = r7.mDeviceCountryCode;	 Catch:{ Exception -> 0x009a }
        r4 = r4.equalsIgnoreCase(r0);	 Catch:{ Exception -> 0x009a }
        if (r4 != 0) goto L_0x000f;
    L_0x0167:
        r4 = r1.name();	 Catch:{ Exception -> 0x009a }
        r2.add(r4);	 Catch:{ Exception -> 0x009a }
        r4 = r7.TAG;	 Catch:{ Exception -> 0x009a }
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x009a }
        r5.<init>();	 Catch:{ Exception -> 0x009a }
        r6 = "mDeviceCountryCode: ";
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x009a }
        r6 = r7.mDeviceCountryCode;	 Catch:{ Exception -> 0x009a }
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x009a }
        r6 = ", value: ";
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x009a }
        r0 = r5.append(r0);	 Catch:{ Exception -> 0x009a }
        r0 = r0.toString();	 Catch:{ Exception -> 0x009a }
        com.samsung.android.spayfw.p002b.Log.m285d(r4, r0);	 Catch:{ Exception -> 0x009a }
        goto L_0x000f;
    L_0x0194:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.mastercard.tokenmanagement.McDecisioningData.getAdditionalAuthenticationReasons():java.util.List<java.lang.String>");
    }

    private Location getDeviceLocation() {
        return DeviceInfo.getLastKnownLocation(McProvider.getContext());
    }

    private String getMobileNumberSuffix() {
        String msisdn = DeviceInfo.getMsisdn(McProvider.getContext());
        if (!isStringValid(msisdn) || msisdn.length() < 4) {
            return null;
        }
        int length = msisdn.length();
        int i = length - 4;
        if (i < 0) {
            i = 0;
        }
        return msisdn.substring(i, length);
    }

    private boolean isStringArrayValid(List<String> list) {
        return (list == null || list.isEmpty()) ? false : true;
    }

    private String getDecimal(double d) {
        return String.format(new Locale("en", "US"), "%.2f", new Object[]{Double.valueOf(d)});
    }

    private boolean isStringValid(String str) {
        return (str == null || str.isEmpty()) ? false : true;
    }
}
