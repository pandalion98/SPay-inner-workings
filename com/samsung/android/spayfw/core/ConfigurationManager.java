package com.samsung.android.spayfw.core;

import android.content.Context;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.core.retry.JwtRetryRequester;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo;
import com.samsung.android.spayfw.storage.ConfigStorage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

/* renamed from: com.samsung.android.spayfw.core.e */
public class ConfigurationManager {
    private static ConfigurationManager jc;
    private Map<String, String> jd;
    private ConfigStorage je;
    private Context mContext;

    public static final synchronized ConfigurationManager m581h(Context context) {
        ConfigurationManager configurationManager;
        synchronized (ConfigurationManager.class) {
            if (jc == null) {
                jc = new ConfigurationManager(context);
            }
            configurationManager = jc;
        }
        return configurationManager;
    }

    public static boolean m582i(Context context) {
        String config = ConfigurationManager.m581h(context).getConfig(PaymentFramework.CONFIG_ENABLE_TAP_N_GO);
        if (config == null) {
            Log.m285d("ConfigurationManager", "Tap N Go Null");
            return false;
        } else if (!config.equals(PaymentFramework.CONFIG_VALUE_FALSE)) {
            return true;
        } else {
            Log.m285d("ConfigurationManager", "Tap N Go False");
            return false;
        }
    }

    private ConfigurationManager(Context context) {
        this.jd = new HashMap();
        this.mContext = context;
        this.je = ConfigStorage.ab(this.mContext);
    }

    public final String getConfig(String str) {
        String deviceId;
        if (str.equals(PaymentFramework.CONFIG_DEVICE_ID)) {
            deviceId = DeviceInfo.getDeviceId(this.mContext);
            Log.m285d("ConfigurationManager", "DeviceId: " + deviceId);
            return deviceId;
        } else if (str.equals(PaymentFramework.CONFIG_RESET_REASON)) {
            return this.mContext.getSharedPreferences(PaymentFramework.CONFIG_RESET_REASON, 0).getString(PaymentFramework.CONFIG_RESET_REASON, null);
        } else {
            af();
            deviceId = (String) this.jd.get(str);
            if (deviceId == null) {
                return m583z(str);
            }
            return deviceId;
        }
    }

    public final int setConfig(String str, String str2) {
        if (str == null) {
            return -5;
        }
        if (str.equals(PaymentFramework.CONFIG_RESET_REASON)) {
            this.mContext.getSharedPreferences(PaymentFramework.CONFIG_RESET_REASON, 0).edit().putString(PaymentFramework.CONFIG_RESET_REASON, str2).commit();
            return 0;
        }
        af();
        int a = m580a(str, str2);
        if (a == 0) {
            this.je.setConfig(str, str2);
            this.jd.put(str, str2);
        }
        if (!PaymentFramework.CONFIG_JWT_TOKEN.equals(str)) {
            return a;
        }
        Log.m287i("ConfigurationManager", "JWT Token Updated");
        JwtRetryRequester.bj().bk();
        return a;
    }

    void ae() {
        Map fr = this.je.fr();
        if (!(fr == null || fr.isEmpty())) {
            this.jd = fr;
        }
        String str = this.mContext.getFilesDir().getParent() + "/shared_prefs/" + "SpayFw" + ".xml";
        Log.m285d("ConfigurationManager", "prepareConfigCache: shared prefs file path = " + str);
        File file = new File(str);
        if (file != null && file.exists()) {
            str = this.mContext.getSharedPreferences("SpayFw", 0).getString("jwtToken", null);
            if (str != null) {
                setConfig(PaymentFramework.CONFIG_JWT_TOKEN, str);
            }
            str = this.mContext.getSharedPreferences("SpayFw", 0).getString("walletId", null);
            if (str != null) {
                setConfig(PaymentFramework.CONFIG_WALLET_ID, str);
            }
            file.delete();
        }
    }

    private String m583z(String str) {
        Object obj = -1;
        switch (str.hashCode()) {
            case -1039518220:
                if (str.equals(PaymentFramework.CONFIG_ENABLE_TAP_N_GO)) {
                    obj = null;
                    break;
                }
                break;
        }
        switch (obj) {
            case ECCurve.COORD_AFFINE /*0*/:
                return PaymentFramework.CONFIG_VALUE_FALSE;
            default:
                return null;
        }
    }

    private void af() {
        String str = (String) this.jd.get(PaymentFramework.CONFIG_DEFAULT_CARD);
        if (str == null) {
            Log.m285d("ConfigurationManager", "No Default Card");
            return;
        }
        Account a = Account.m551a(this.mContext, null);
        if (a == null) {
            Log.m286e("ConfigurationManager", "reconcileDefaultCardConfig- account is null");
        } else if (a.m559r(str) == null) {
            Log.m290w("ConfigurationManager", "Removing Default Card");
            this.je.bm(PaymentFramework.CONFIG_DEFAULT_CARD);
            this.je.bm(PaymentFramework.CONFIG_ENABLE_TAP_N_GO);
            this.jd.remove(PaymentFramework.CONFIG_DEFAULT_CARD);
            this.jd.remove(PaymentFramework.CONFIG_ENABLE_TAP_N_GO);
        }
    }

    private int m580a(String str, String str2) {
        Object obj = -1;
        switch (str.hashCode()) {
            case -1039518220:
                if (str.equals(PaymentFramework.CONFIG_ENABLE_TAP_N_GO)) {
                    obj = 1;
                    break;
                }
                break;
            case -1003383079:
                if (str.equals(PaymentFramework.CONFIG_PF_INSTANCE_ID)) {
                    obj = 3;
                    break;
                }
                break;
            case -368294837:
                if (str.equals(PaymentFramework.CONFIG_DEFAULT_CARD)) {
                    obj = null;
                    break;
                }
                break;
            case -284377500:
                if (str.equals(PaymentFramework.CONFIG_WALLET_ID)) {
                    obj = 2;
                    break;
                }
                break;
            case 492548996:
                if (str.equals(PaymentFramework.CONFIG_JWT_TOKEN)) {
                    obj = 4;
                    break;
                }
                break;
            case 1017556498:
                if (str.equals(PaymentFramework.CONFIG_USER_ID)) {
                    obj = 5;
                    break;
                }
                break;
        }
        String str3;
        switch (obj) {
            case ECCurve.COORD_AFFINE /*0*/:
                Account a = Account.m551a(this.mContext, null);
                if (a == null) {
                    Log.m286e("ConfigurationManager", "reconcileDefaultCardConfig- account is null");
                    return -5;
                } else if (a.m559r(str2) != null) {
                    return 0;
                } else {
                    return -5;
                }
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                if ((str2.equals(PaymentFramework.CONFIG_VALUE_TRUE) || str2.equals(PaymentFramework.CONFIG_VALUE_FALSE)) && this.je.getConfig(PaymentFramework.CONFIG_DEFAULT_CARD) != null) {
                    return 0;
                }
                return -5;
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                str3 = (String) this.jd.get(str);
                if (str3 == null || str3.isEmpty()) {
                    return 0;
                }
                Log.m287i("ConfigurationManager", "setConfig - Wallet id cannot be set more than once - current value = " + str3);
                return -3;
            case F2m.PPB /*3*/:
                str3 = (String) this.jd.get(str);
                if (str3 == null || str3.isEmpty()) {
                    return 0;
                }
                Log.m287i("ConfigurationManager", "setConfig - Instance id cannot be set more than once - current value = " + str3);
                return -3;
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
            case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                return 0;
            default:
                Log.m290w("ConfigurationManager", "validateInput: Invalid Key = " + str);
                return -5;
        }
    }
}
