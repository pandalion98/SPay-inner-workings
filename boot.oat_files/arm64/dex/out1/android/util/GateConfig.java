package android.util;

import android.os.Build;
import android.os.SystemProperties;
import com.android.ims.ImsConferenceState;
import com.samsung.android.smartface.SmartFaceManager;

public final class GateConfig {
    private static final String DEBUG_LEVEL_HIGH = "0x4948";
    private static final String DEBUG_LEVEL_LOW = "0x4f4c";
    private static final String DEBUG_LEVEL_MID = "0x494d";
    public static final String GATE_INTENT_V1_ACTION = "com.sec.android.app.GateAgent.GATE_CONFIG";
    public static final String GATE_INTENT_V1_EXTRA_GATE_ENABLED = "GATE_ENABLED";
    public static final String GATE_INTENT_V1_EXTRA_LCDTEXT_ENABLED = "GATE_LCDTEXT_ENABLED";
    public static final String GATE_INTENT_V1_EXTRA_TRACE_TAG = "GATE_TRACE_TAG";
    public static final String GATE_INTENT_V2_ACTION = "com.sec.android.gate.GATE";
    public static final String GATE_INTENT_V2_EXTRA_ENABLED = "ENABLED";
    public static final String GATE_V1_SYS_PROP_GATE_ENABLED = "service.gate.enabled";
    public static final String GATE_V1_SYS_PROP_LCDTEXT_ENABLED = "service.gate.lcdtexton";
    public static final String GATE_V1_SYS_PROP_TRACE_FILTER = "service.gate.filter";
    public static final String GATE_V2_SYS_PROP_GATE_ENABLED = "service.gate.enabled";
    public static final String GATE_V2_SYS_PROP_LCDTEXT_ENABLED = "service.gate.lcdtexton";
    public static final int GATE_VERSION_1 = 1;
    public static final int GATE_VERSION_2 = 2;
    public static final int GATE_VERSION_LATEST = 2;
    public static final String LCDTEXT_INTENT_V2_ACTION = "com.sec.android.gate.LCDTEXT";
    public static final String LCDTEXT_INTENT_V2_EXTRA_ENABLED = "ENABLED";
    private static final String LOG_TAG = "GATE";
    private static boolean sGateEnabled = false;
    private static boolean sGateLcdtextEnabled = false;

    public static void setGateEnabled(boolean gateEnabled) {
        sGateEnabled = gateEnabled;
        Log.i(LOG_TAG, "GateConfig.setGateEnabled. GATE = " + sGateEnabled + ", LCDTEXT = " + sGateLcdtextEnabled);
    }

    public static void setGateLcdtextEnabled(boolean lcdTextEnabled) {
        sGateLcdtextEnabled = lcdTextEnabled;
        Log.i(LOG_TAG, "GateConfig.setGateLcdtextEnabled. GATE = " + sGateEnabled + ", LCDTEXT = " + sGateLcdtextEnabled);
    }

    public static boolean isGateEnabled() {
        String debugLevel = String.valueOf(SystemProperties.get("ro.debug_level"));
        if (!ImsConferenceState.USER.equals(Build.TYPE)) {
            return SystemProperties.get("service.gate.enabled").equals(SmartFaceManager.PAGE_BOTTOM);
        }
        if (debugLevel.equals(DEBUG_LEVEL_LOW)) {
            return false;
        }
        return SystemProperties.get("service.gate.enabled").equals(SmartFaceManager.PAGE_BOTTOM);
    }

    public static boolean isGateLcdtextEnabled() {
        String debugLevel = String.valueOf(SystemProperties.get("ro.debug_level"));
        if (!ImsConferenceState.USER.equals(Build.TYPE)) {
            return SystemProperties.get("service.gate.lcdtexton").equals(SmartFaceManager.PAGE_BOTTOM);
        }
        if (debugLevel.equals(DEBUG_LEVEL_LOW)) {
            return false;
        }
        return SystemProperties.get("service.gate.lcdtexton").equals(SmartFaceManager.PAGE_BOTTOM);
    }
}
