/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Map
 */
package com.samsung.android.spayfw.payprovider.discover.tokenmanager;

import com.samsung.android.spayfw.appinterface.ProvisionTokenInfo;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.fraud.b;
import com.samsung.android.spayfw.payprovider.RiskDataParam;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo;
import java.util.ArrayList;

public class a {
    private static a wt;

    public static a eg() {
        if (wt == null) {
            wt = new a();
        }
        return wt;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public ArrayList<RiskDataParam> b(ProvisionTokenInfo provisionTokenInfo) {
        ArrayList arrayList;
        boolean bl;
        block8 : {
            block7 : {
                int n2 = 99;
                arrayList = new ArrayList();
                b b2 = new b(com.samsung.android.spayfw.payprovider.discover.a.cC());
                String string = (String)provisionTokenInfo.getActivationParams().get((Object)"deviceCountry");
                String string2 = (String)provisionTokenInfo.getActivationParams().get((Object)"billingCountryCode");
                Log.d("DCSDK_DcRiskData", "composeRiskData");
                try {
                    int n3 = b2.x(1);
                    if (n3 < 0) {
                        Log.d("DCSDK_DcRiskData", "Total provision count < 0, using default value: 0");
                        n3 = 0;
                    }
                    if (n3 > n2) {
                        Log.d("DCSDK_DcRiskData", "Total provision count: " + n3 + ", using MAX value: " + 99);
                        n3 = n2;
                    }
                    arrayList.add((Object)new RiskDataParam("totalProvisioningAttempts", "" + n3));
                    int n4 = b2.x(10);
                    if (n4 < 0) {
                        Log.d("DCSDK_DcRiskData", "Total provision count < 0, using default value: 0");
                        n4 = 0;
                    }
                    if (n4 > n2) {
                        Log.d("DCSDK_DcRiskData", "Total provision count: " + n4 + ", using MAX value: " + 99);
                    } else {
                        n2 = n4;
                    }
                    arrayList.add((Object)new RiskDataParam("tenDayProvisionCount", "" + n2));
                    String string3 = string == null ? DeviceInfo.getDeviceCountry().toLowerCase() : string;
                    if (string3 == null || string3.isEmpty() || string2 == null) break block7;
                    bl = !string3.equalsIgnoreCase(string2);
                    Log.d("DCSDK_DcRiskData", "deviceCountryCode: " + string3 + ", billingCountryCode: " + string2);
                    break block8;
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                    return arrayList;
                }
            }
            bl = true;
        }
        arrayList.add((Object)new RiskDataParam("countryMismatch", "" + bl));
        return arrayList;
    }
}

