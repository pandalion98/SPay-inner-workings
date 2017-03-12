package android.app;

public interface IVRManager {
    public static final int HMT_EVENT_ABNORMAL = 4;
    public static final int HMT_EVENT_DOCK = 1;
    public static final int HMT_EVENT_MOUNT = 16;
    public static final int HMT_EVENT_SENSOR_BOOTING_WITHOUT_TA = 256;
    public static final int HMT_EVENT_SENSOR_BOOTING_WITH_TA = 512;
    public static final int HMT_EVENT_SENSOR_CONNECTED_TA = 1024;
    public static final int HMT_EVENT_UNDOCK = 2;
    public static final int HMT_EVENT_UNMOUNT = 32;
    public static final int VR_API_MINOR_VERSION_CODE = 1;
    public static final int VR_API_VERSION_CODE = 11;
    public static final String VR_BRIGHTNESS = "bright";
    public static final String VR_COMFORT_VIEW = "comfortable_view";
    public static final String VR_DO_NOT_DISTURB = "do_not_disturb_mode";
    public static final String VR_MANAGER = "vr";
    public static final String VR_OPTION_IPD = "ipd";
    public static final String VR_SYSTEM_EPEN_ENABLED = "epen_enabled";
    public static final String VR_SYSTEM_MOUSE_CONTROL_TYPE = "mouse_control_type";
    public static final String VR_SYSTEM_SHOW_MOUSE_POINTER = "show_mouse_pointer";
    public static final String VR_SYSTEM_TOUCHKEY_ENABLED = "touchkey_enabled";
    public static final String VR_SYSTEM_TSP_ENABLED = "tsp_enabled";

    int GetPowerLevelState();

    int[] SetVrClocks(String str, int i, int i2);

    void enforceCallingSelfPermission(String str);

    String getOption(String str);

    String getSystemOption(String str);

    int getVRBright();

    int getVRColorTemperature();

    boolean isConnected();

    boolean isVRComfortableViewEnabled();

    boolean isVRDarkAdaptationEnabled();

    boolean isVRLowPersistenceEnabled();

    boolean isVRMode();

    boolean relFreq(String str);

    void releaseCPUMhz(String str);

    void releaseGPUMhz(String str);

    int[] return2EnableFreqLev();

    int setAffinity(int i, int[] iArr);

    int[] setCPUClockMhz(String str, int[] iArr, int i);

    int setGPUClockMhz(String str, int i);

    void setOption(String str, String str2);

    void setSystemOption(String str, String str2);

    boolean setThreadSchedFifo(String str, int i, int i2, int i3);

    void setVRBright(int i);

    void setVRColorTemperature(int i);

    void setVRComfortableView(boolean z);

    void setVRDarkAdaptation(boolean z);

    void setVRLowPersistence(boolean z);

    void setVRMode(boolean z);

    boolean setVideoMode(String str, float f, boolean z);

    String vrManagerVersion();

    String vrOVRVersion();
}
