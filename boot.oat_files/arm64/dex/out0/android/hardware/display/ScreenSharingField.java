package android.hardware.display;

public final class ScreenSharingField {
    public static final String LOGGING_APP_ID = "com.samsung.android.screenmirroring";
    public static final String LOGGING_DEVICE_TYPE_DLNA = "DLNA";
    public static final String LOGGING_DEVICE_TYPE_SCREEN_MIRRORING = "SCR_MIR";
    public static final String LOGGING_DEVICE_TYPE_SCREEN_SHARING = "SCR_SHA";
    public static final String LOGGING_FEATURE_CONNECT = "CONN";
    public static final String LOGGING_FEATURE_CONNECT_TYPE = "CNTP";
    public static final String LOGGING_FEATURE_DISCONNET = "DCON";
    public static final String LOGGING_FEATURE_FLOATING_ICON_COUNT = "FCNT";
    public static final String LOGGING_FEATURE_STOP_AUTO_CONNECTION = "STAC";
    public static final String LOGGING_INTENT = "com.sec.android.screensharing.LOGGING";
    public static final String LOGGING_TYPE_AUTO_QUICK_PANNEL = "AUTO_QIC_PAN";
    public static final String LOGGING_TYPE_AUTO_SHARE_PANNEL = "AUTO_SHA_PAN";
    public static final String LOGGING_TYPE_CHANGE_DEVICE = "CHA_DEV";
    public static final String LOGGING_TYPE_FLOATING_ICON = "FIC_ICON";
    public static final String LOGGING_TYPE_NOTIFICATION = "NOTI";
    public static final String LOGGING_TYPE_QUICK_PANNEL = "QIC_PAN";
    public static final String LOGGING_TYPE_SHARE_PANNEL = "SHA_PAN";
    public static final int SCREEN_SHARING_NOT_SUPPORT = -1;
    public static final int SCREEN_SHARING_STATE_PAUSED = 7;
    public static final int SCREEN_SHARING_STATE_RESUMED = 6;
    public static final int SCREEN_SHARING_SUPPORT_ALL = 0;
    public static final int SCREEN_SHARING_SUPPORT_DLNA = 2;
    public static final int SCREEN_SHARING_SUPPORT_MIRRORING = 1;
    public static final int SCREEN_SHARING_TYPE_IMAGE = 1;
    public static final int SCREEN_SHARING_TYPE_MUSIC = 2;
    public static final int SCREEN_SHARING_TYPE_VIDEO = 0;

    private ScreenSharingField() {
    }
}
