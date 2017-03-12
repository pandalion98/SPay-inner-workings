package android.os;

import android.content.Context;
import android.media.AudioParameter;
import android.provider.Settings.System;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.io.File;
import java.io.IOException;

public final class FactoryTest {
    public static final int FACTORY_TEST_HIGH_LEVEL = 2;
    public static final int FACTORY_TEST_LOW_LEVEL = 1;
    public static final int FACTORY_TEST_OFF = 0;
    public static final int OPTION_FACTORY_APP = 1;
    public static final int OPTION_SCREEN_LOCK = 0;
    private static final String TAG = "FactoryTest";
    private static String mFactoryMode = null;
    private static boolean mIsFactoryMode = false;

    public static int getMode() {
        return SystemProperties.getInt("ro.factorytest", 0);
    }

    public static boolean isLongPressOnPowerOffEnabled() {
        return SystemProperties.getInt("factory.long_press_power_off", 0) != 0;
    }

    public static boolean isFactoryPBAPhase() {
        return SystemProperties.get("ril.factory_mode").equals("PBA");
    }

    public static boolean isFactoryMode(Context mContext, TelephonyManager mTelephonyManager) {
        if (isFactoryBinary()) {
            Log.d(TAG, "Binary type is Factory by Case #0");
            return true;
        } else if (mContext != null && System.getInt(mContext.getContentResolver(), "SHOULD_SHUT_DOWN", 0) == 1) {
            Log.d(TAG, "Factory mode is enabled by Case #1");
            return true;
        } else if (mTelephonyManager == null || !"999999999999999".equals(mTelephonyManager.getSubscriberId())) {
            return getFactoryModeFromFile();
        } else {
            Log.d(TAG, "Factory mode is enabled by Case #2");
            return true;
        }
    }

    public static boolean isAutomaticTestMode(Context context) {
        if (context == null || System.getInt(context.getContentResolver(), "SHOULD_SHUT_DOWN", 0) != 1) {
            return false;
        }
        Log.d(TAG, "The AutomaticTest mode was enabled.");
        return true;
    }

    public static boolean checkAutomationTestOption(Context context, int option) {
        Log.d(TAG, "checkAutomationTestOption() : option=" + option);
        boolean result = false;
        if (context == null) {
            return false;
        }
        int value;
        switch (option) {
            case 0:
                value = System.getInt(context.getContentResolver(), "OPTION_SCREEN_LOCK", 0);
                Log.d(TAG, "checkAutomationTestOption() : mode_screenlock=" + value);
                if (value == 1) {
                    result = true;
                    break;
                }
                break;
            case 1:
                value = System.getInt(context.getContentResolver(), "OPTION_FACTORY_APP", 0);
                Log.d(TAG, "checkAutomationTestOption() : mode_factoryapp=" + value);
                if (value == 1) {
                    result = true;
                    break;
                }
                break;
            default:
                result = false;
                break;
        }
        return result;
    }

    public static boolean isFactoryMode() {
        if (isFactoryBinary()) {
            return true;
        }
        return getFactoryModeFromFile();
    }

    private static boolean getFactoryModeFromFile() {
        if (mFactoryMode == null) {
            try {
                mFactoryMode = FileUtils.readTextFile(new File("/efs/FactoryApp/factorymode"), 32, null);
            } catch (IOException e) {
                Log.e(TAG, "cannot open file : /efs/FactoryApp/factorymode");
                mIsFactoryMode = false;
            }
            if (mFactoryMode == null) {
                Log.e(TAG, "mFactoryMode is null. so it is USER mode");
                mFactoryMode = "ON";
            }
            if (mFactoryMode.contains("OFF")) {
                Log.d(TAG, "Factory mode");
                mIsFactoryMode = true;
            } else {
                Log.d(TAG, "User mode");
                mIsFactoryMode = false;
            }
        }
        return mIsFactoryMode;
    }

    public static boolean isFactoryBinary() {
        return "factory".equalsIgnoreCase(SystemProperties.get("ro.factory.factory_binary", "Unknown"));
    }

    public String getBuildType() {
        String type = SystemProperties.get("ro.build.type", "Unknown");
        Log.i(TAG, "getBuildType=" + type);
        return type;
    }

    public static boolean isRunningFactoryApp() {
        String state = SystemProperties.get("sys.factory.runningFactoryApp", AudioParameter.AUDIO_PARAMETER_VALUE_false);
        Boolean result = Boolean.valueOf(Boolean.parseBoolean(state));
        if (result.booleanValue()) {
            Log.i(TAG, "isRunningFactoryApp=" + state);
        }
        return result.booleanValue();
    }

    public static boolean setRunningFactoryApp(Context context, boolean status) {
        if (context.checkCallingOrSelfPermission("com.sec.factory.permission.KEYSTRING") != 0) {
            throw new SecurityException("Requires com.sec.factory.permission.KEYSTRING permission");
        }
        SystemProperties.set("sys.factory.runningFactoryApp", String.valueOf(status));
        Log.i(TAG, "setRunningFactoryApp=" + isRunningFactoryApp());
        return true;
    }

    public static boolean needBlockingPowerKey() {
        if (!isFactoryBinary()) {
            return false;
        }
        String state = SystemProperties.get("sys.factory.blockingPowerKey", AudioParameter.AUDIO_PARAMETER_VALUE_false);
        Boolean result = Boolean.valueOf(Boolean.parseBoolean(state));
        if (result.booleanValue()) {
            Log.i(TAG, "needBlockingPowerKey=" + state);
        }
        return result.booleanValue();
    }

    public static boolean setBlockingPowerKey(Context context, boolean status) {
        if (!isFactoryBinary()) {
            return false;
        }
        if (context.checkCallingOrSelfPermission("com.sec.factory.permission.KEYSTRING") != 0) {
            throw new SecurityException("Requires com.sec.factory.permission.KEYSTRING permission");
        }
        SystemProperties.set("sys.factory.blockingPowerKey", String.valueOf(status));
        Log.i(TAG, "setBlockingPowerKey=" + needBlockingPowerKey());
        return true;
    }
}
