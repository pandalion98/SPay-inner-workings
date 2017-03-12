package com.android.internal.telephony;

import android.os.SystemProperties;
import android.security.keystore.KeyProperties;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.android.internal.content.NativeLibraryHelper;
import com.sec.android.app.CscFeature;

public class TelephonyFeatures {
    static final String LOG_TAG = "TelephonyFeatures";
    public static final int NTCTYPE_COUNTRY = 3;
    public static final int NTCTYPE_MAINOPERATOR = 0;
    public static final int NTCTYPE_OPERATORTYPE = 2;
    public static final int NTCTYPE_SUBOPERATOR = 1;
    public static final String SALES_CODE = SystemProperties.get("ro.csc.sales_code", KeyProperties.DIGEST_NONE);
    public static String mConfigNetworkTypeCapability = CscFeature.getInstance().getString("CscFeature_RIL_ConfigNetworkTypeCapability");
    public static String mUsedNetworkType = "EUR";

    public static String getNtcValue(int ntcType) {
        if (TextUtils.isEmpty(mConfigNetworkTypeCapability)) {
            return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
        }
        String[] ntcValue = mConfigNetworkTypeCapability.split(NativeLibraryHelper.CLEAR_ABI_OVERRIDE);
        if (ntcValue.length != 4) {
            return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
        }
        return ntcValue[ntcType];
    }

    public static boolean isUsaCdmaModel() {
        if ("USA".equals(getNtcValue(3)) && ("CDM".equals(getNtcValue(2)) || "GLB".equals(getNtcValue(2)))) {
            return true;
        }
        return false;
    }

    public static boolean isUsaGlobalModel() {
        if ("USA".equals(getNtcValue(3)) && "GLB".equals(getNtcValue(2))) {
            return true;
        }
        return false;
    }

    public static boolean displaySpnRulePlmnAtAbout(int id) {
        String iccOperatorNumeric = TelephonyManager.getTelephonyProperty(id, TelephonyProperties.PROPERTY_ICC_OPERATOR_NUMERIC, "");
        String countryName = getNtcValue(3);
        if ("CHN".equals(countryName) || "HKG".equals(countryName) || "TPE".equals(countryName) || iccOperatorNumeric.equals("52501")) {
            return false;
        }
        return true;
    }

    public static boolean showEpdgNetNameWhenEpdgRegiAndCSOos() {
        if (isUsaCdmaModel() || "EUR".equals(mUsedNetworkType)) {
            return true;
        }
        return false;
    }

    public static boolean forceSetEpdgNetName() {
        if ("DTM".equals(SALES_CODE)) {
            return true;
        }
        if (isUsaCdmaModel() || "EUR".equals(mUsedNetworkType)) {
            return false;
        }
        return true;
    }

    public static boolean showOnlyPlmnWhenSpnPlmnSame() {
        if (CscFeature.getInstance().getEnableStatus("CscFeature_RIL_OperatorNameRuleForDcm")) {
            return false;
        }
        return true;
    }

    public static boolean needToRunLteRoaming(int id) {
        if (!"KTT".equals(mUsedNetworkType) && !"LGT".equals(mUsedNetworkType) && !"SKT".equals(mUsedNetworkType) && !"KOO".equals(mUsedNetworkType)) {
            return false;
        }
        String simType = TelephonyManager.getTelephonyProperty(id, "ril.simtype", "");
        if (("2".equals(simType) && "KTT".equals(mUsedNetworkType)) || (("3".equals(simType) && "LGT".equals(mUsedNetworkType)) || ("4".equals(simType) && "SKT".equals(mUsedNetworkType)))) {
            return true;
        }
        if (!"KOO".equals(mUsedNetworkType)) {
            return false;
        }
        if ("2".equals(simType) || "3".equals(simType) || "4".equals(simType)) {
            return true;
        }
        return false;
    }

    public static boolean alwaysTerminalbasedCW(int phoneId) {
        return false;
    }

    public static boolean TerminalbasedCWForCallreject(int phoneId) {
        String simNumeric = TelephonyManager.getTelephonyProperty(phoneId, TelephonyProperties.PROPERTY_ICC_OPERATOR_NUMERIC, "");
        if (CscFeature.getInstance().getEnableStatus("CscFeature_RIL_SupportVolte") && "21403".equals(simNumeric)) {
            return true;
        }
        return false;
    }
}
