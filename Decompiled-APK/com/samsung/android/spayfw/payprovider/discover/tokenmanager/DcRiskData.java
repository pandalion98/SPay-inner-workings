package com.samsung.android.spayfw.payprovider.discover.tokenmanager;

import com.samsung.android.spayfw.appinterface.ActivationData;
import com.samsung.android.spayfw.appinterface.ProvisionTokenInfo;
import com.samsung.android.spayfw.fraud.FraudDataProvider;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.RiskDataParam;
import com.samsung.android.spayfw.payprovider.discover.DiscoverPayProvider;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.util.ArrayList;

/* renamed from: com.samsung.android.spayfw.payprovider.discover.tokenmanager.a */
public class DcRiskData {
    private static DcRiskData wt;

    public static DcRiskData eg() {
        if (wt == null) {
            wt = new DcRiskData();
        }
        return wt;
    }

    public ArrayList<RiskDataParam> m1008b(ProvisionTokenInfo provisionTokenInfo) {
        int i = 99;
        ArrayList<RiskDataParam> arrayList = new ArrayList();
        FraudDataProvider fraudDataProvider = new FraudDataProvider(DiscoverPayProvider.cC());
        String str = (String) provisionTokenInfo.getActivationParams().get(ActivationData.DEVICE_COUNTRY_CODE);
        String str2 = (String) provisionTokenInfo.getActivationParams().get(ActivationData.CARD_INFO_BILLING_COUNTRY);
        Log.m285d("DCSDK_DcRiskData", "composeRiskData");
        try {
            String toLowerCase;
            boolean z;
            int x = fraudDataProvider.m743x(1);
            if (x < 0) {
                Log.m285d("DCSDK_DcRiskData", "Total provision count < 0, using default value: 0");
                x = 0;
            }
            if (x > 99) {
                Log.m285d("DCSDK_DcRiskData", "Total provision count: " + x + ", using MAX value: " + 99);
                x = 99;
            }
            arrayList.add(new RiskDataParam("totalProvisioningAttempts", BuildConfig.FLAVOR + x));
            x = fraudDataProvider.m743x(10);
            if (x < 0) {
                Log.m285d("DCSDK_DcRiskData", "Total provision count < 0, using default value: 0");
                x = 0;
            }
            if (x > 99) {
                Log.m285d("DCSDK_DcRiskData", "Total provision count: " + x + ", using MAX value: " + 99);
            } else {
                i = x;
            }
            arrayList.add(new RiskDataParam("tenDayProvisionCount", BuildConfig.FLAVOR + i));
            if (str == null) {
                toLowerCase = DeviceInfo.getDeviceCountry().toLowerCase();
            } else {
                toLowerCase = str;
            }
            if (toLowerCase == null || toLowerCase.isEmpty() || str2 == null) {
                z = true;
            } else {
                z = !toLowerCase.equalsIgnoreCase(str2);
                Log.m285d("DCSDK_DcRiskData", "deviceCountryCode: " + toLowerCase + ", billingCountryCode: " + str2);
            }
            arrayList.add(new RiskDataParam("countryMismatch", BuildConfig.FLAVOR + z));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }
}
