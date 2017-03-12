package android.util.secutil;

import android.os.SystemProperties;
import com.samsung.android.smartface.SmartFaceManager;

public final class LogSwitcher {
    public static boolean isShowingGlobalLog;
    public static boolean isShowingSecDLog;
    public static boolean isShowingSecELog;
    public static boolean isShowingSecILog;
    public static boolean isShowingSecVLog;
    public static boolean isShowingSecWLog;
    public static boolean isShowingSecWtfLog;

    static {
        isShowingGlobalLog = false;
        isShowingSecVLog = false;
        isShowingSecDLog = false;
        isShowingSecILog = false;
        isShowingSecWLog = false;
        isShowingSecELog = false;
        isShowingSecWtfLog = false;
        try {
            isShowingGlobalLog = SmartFaceManager.PAGE_BOTTOM.equals(SystemProperties.get("persist.log.seclevel", SmartFaceManager.PAGE_MIDDLE));
            isShowingSecVLog = isShowingGlobalLog;
            isShowingSecDLog = isShowingGlobalLog;
            isShowingSecILog = isShowingGlobalLog;
            isShowingSecWLog = isShowingGlobalLog;
            isShowingSecELog = isShowingGlobalLog;
            isShowingSecWtfLog = isShowingGlobalLog;
        } catch (Exception e) {
        }
    }
}
