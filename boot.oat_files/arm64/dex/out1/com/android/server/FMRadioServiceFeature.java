package com.android.server;

import com.sec.android.app.CscFeature;

public class FMRadioServiceFeature {
    public static final String BANDWIDTHAS_76000_108000 = "76000_108000";
    public static final String BANDWIDTHAS_76000_90000 = "76000_90000";
    public static final String BANDWIDTHAS_87500_108000 = "87500_108000";
    public static final String FEATURE_BANDWIDTH = sCscFeature.getString("CscFeature_FMRadio_BandWidthAs");
    public static final int FEATURE_DECONSTANT = sCscFeature.getInteger("CscFeature_FMRadio_DeconstantAs");
    public static final boolean FEATURE_DISABLEDNS = true;
    public static final int FEATURE_FREQUENCYSPACE = sCscFeature.getInteger("CscFeature_FMRadio_FrequencySpaceAs");
    public static final String FEATURE_SETLOCALTUNNING = sCscFeature.getString("CscFeature_FMRadio_SetLocalTunning");
    public static final String FEATURE_SOFTMUTE = sCscFeature.getString("CscFeature_FMRadio_DefaultSoftMuteValue");
    public static final boolean FEATURE_WAIT_PID_DURING_SCAN = false;
    public static CscFeature sCscFeature = CscFeature.getInstance();
}
