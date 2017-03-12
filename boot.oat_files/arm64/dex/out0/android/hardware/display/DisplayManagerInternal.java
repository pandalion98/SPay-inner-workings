package android.hardware.display;

import android.hardware.SensorManager;
import android.os.Handler;
import android.view.Display;
import android.view.DisplayInfo;

public abstract class DisplayManagerInternal {

    public interface DisplayPowerCallbacks {
        void acquireSuspendBlocker();

        void onColorFadeExit(boolean z);

        void onDisplayStateChange(int i, int i2);

        void onProximityNegative();

        void onProximityPositive();

        void onSetInteractiveNeeded(int i, int i2);

        void onStateChanged();

        void releaseSuspendBlocker();
    }

    public static final class DisplayPowerRequest {
        public static final int POLICY_BRIGHT = 3;
        public static final int POLICY_DIM = 2;
        public static final int POLICY_DOZE = 1;
        public static final int POLICY_OFF = 0;
        public boolean autoBrightnessForEbookOnly;
        public int autoBrightnessLowerLimit;
        public int autoBrightnessUpperLimit;
        public boolean blockScreenOn;
        public boolean boostScreenBrightness;
        public boolean brightnessSetByUser;
        public boolean coverClosed;
        public int dozeScreenBrightness;
        public int dozeScreenState;
        public boolean forceDimBrightness;
        public boolean forceLcdBacklightOffEnabled;
        public boolean hasRetailModeApp;
        public boolean isAlpmMode;
        public boolean isLidClosed;
        public boolean isOutdoorMode;
        public int lastGoToSleepReason;
        public int lastMultiScreenState;
        public long lastUpdateCoverStateTime;
        public boolean lowPowerMode;
        public boolean mFTAMode;
        public int maxBrightness;
        public int minBrightness;
        public int policy;
        public int policySub;
        public float screenAutoBrightnessAdjustment;
        public int screenBrightness;
        public int temporaryScreenBrightnessSettingOverride;
        public boolean useAutoBrightness;
        public boolean useClearViewBrightnessMode;
        public boolean useColorWeaknessMode;
        public boolean useProximitySensor;
        public boolean useTemporaryScreenBrightnessSettingOverride;
        public boolean wakeUpEvenThoughProximityPositive;

        public DisplayPowerRequest() {
            this.autoBrightnessLowerLimit = -1;
            this.autoBrightnessUpperLimit = -1;
            this.maxBrightness = -1;
            this.minBrightness = -1;
            this.forceLcdBacklightOffEnabled = false;
            this.isAlpmMode = false;
            this.autoBrightnessForEbookOnly = false;
            this.lastGoToSleepReason = 0;
            this.wakeUpEvenThoughProximityPositive = false;
            this.hasRetailModeApp = false;
            this.isLidClosed = false;
            this.isOutdoorMode = false;
            this.policy = 3;
            this.policySub = 3;
            this.lastMultiScreenState = -1;
            this.useProximitySensor = false;
            this.screenBrightness = 255;
            this.screenAutoBrightnessAdjustment = 0.0f;
            this.useAutoBrightness = false;
            this.blockScreenOn = false;
            this.dozeScreenBrightness = -1;
            this.dozeScreenState = 0;
        }

        public DisplayPowerRequest(DisplayPowerRequest other) {
            this.autoBrightnessLowerLimit = -1;
            this.autoBrightnessUpperLimit = -1;
            this.maxBrightness = -1;
            this.minBrightness = -1;
            this.forceLcdBacklightOffEnabled = false;
            this.isAlpmMode = false;
            this.autoBrightnessForEbookOnly = false;
            this.lastGoToSleepReason = 0;
            this.wakeUpEvenThoughProximityPositive = false;
            this.hasRetailModeApp = false;
            this.isLidClosed = false;
            this.isOutdoorMode = false;
            copyFrom(other);
        }

        public boolean isBrightOrDim() {
            return this.policy == 3 || this.policy == 2;
        }

        public void copyFrom(DisplayPowerRequest other) {
            this.policy = other.policy;
            this.policySub = other.policySub;
            this.lastMultiScreenState = other.lastMultiScreenState;
            this.useProximitySensor = other.useProximitySensor;
            this.screenBrightness = other.screenBrightness;
            this.screenAutoBrightnessAdjustment = other.screenAutoBrightnessAdjustment;
            this.brightnessSetByUser = other.brightnessSetByUser;
            this.useAutoBrightness = other.useAutoBrightness;
            this.blockScreenOn = other.blockScreenOn;
            this.lowPowerMode = other.lowPowerMode;
            this.boostScreenBrightness = other.boostScreenBrightness;
            this.dozeScreenBrightness = other.dozeScreenBrightness;
            this.dozeScreenState = other.dozeScreenState;
            this.autoBrightnessLowerLimit = other.autoBrightnessLowerLimit;
            this.autoBrightnessUpperLimit = other.autoBrightnessUpperLimit;
            this.maxBrightness = other.maxBrightness;
            this.minBrightness = other.minBrightness;
            this.autoBrightnessForEbookOnly = other.autoBrightnessForEbookOnly;
            this.forceLcdBacklightOffEnabled = other.forceLcdBacklightOffEnabled;
            this.isAlpmMode = other.isAlpmMode;
            this.lastGoToSleepReason = other.lastGoToSleepReason;
            this.wakeUpEvenThoughProximityPositive = other.wakeUpEvenThoughProximityPositive;
            this.hasRetailModeApp = other.hasRetailModeApp;
            this.coverClosed = other.coverClosed;
            this.mFTAMode = other.mFTAMode;
            this.useTemporaryScreenBrightnessSettingOverride = other.useTemporaryScreenBrightnessSettingOverride;
            this.temporaryScreenBrightnessSettingOverride = other.temporaryScreenBrightnessSettingOverride;
            this.forceDimBrightness = other.forceDimBrightness;
            this.useClearViewBrightnessMode = other.useClearViewBrightnessMode;
            this.useColorWeaknessMode = other.useColorWeaknessMode;
            this.lastUpdateCoverStateTime = other.lastUpdateCoverStateTime;
            this.isLidClosed = other.isLidClosed;
            this.isOutdoorMode = other.isOutdoorMode;
        }

        public boolean equals(Object o) {
            return (o instanceof DisplayPowerRequest) && equals((DisplayPowerRequest) o);
        }

        public boolean equals(DisplayPowerRequest other) {
            return other != null && this.policy == other.policy && this.policySub == other.policySub && this.lastMultiScreenState == other.lastMultiScreenState && this.useProximitySensor == other.useProximitySensor && this.screenBrightness == other.screenBrightness && this.screenAutoBrightnessAdjustment == other.screenAutoBrightnessAdjustment && this.brightnessSetByUser == other.brightnessSetByUser && this.useAutoBrightness == other.useAutoBrightness && this.blockScreenOn == other.blockScreenOn && this.lowPowerMode == other.lowPowerMode && this.boostScreenBrightness == other.boostScreenBrightness && this.dozeScreenBrightness == other.dozeScreenBrightness && this.dozeScreenState == other.dozeScreenState && this.autoBrightnessLowerLimit == other.autoBrightnessLowerLimit && this.autoBrightnessUpperLimit == other.autoBrightnessUpperLimit && this.maxBrightness == other.maxBrightness && this.minBrightness == other.minBrightness && this.autoBrightnessForEbookOnly == other.autoBrightnessForEbookOnly && this.forceLcdBacklightOffEnabled == other.forceLcdBacklightOffEnabled && this.isAlpmMode == other.isAlpmMode && this.lastGoToSleepReason == other.lastGoToSleepReason && this.wakeUpEvenThoughProximityPositive == other.wakeUpEvenThoughProximityPositive && this.coverClosed == other.coverClosed && this.mFTAMode == other.mFTAMode && this.useTemporaryScreenBrightnessSettingOverride == other.useTemporaryScreenBrightnessSettingOverride && this.temporaryScreenBrightnessSettingOverride == other.temporaryScreenBrightnessSettingOverride && this.forceDimBrightness == other.forceDimBrightness && this.useClearViewBrightnessMode == other.useClearViewBrightnessMode && this.useColorWeaknessMode == other.useColorWeaknessMode && this.lastUpdateCoverStateTime == other.lastUpdateCoverStateTime && this.hasRetailModeApp == other.hasRetailModeApp && this.isLidClosed == other.isLidClosed && this.isOutdoorMode == other.isOutdoorMode;
        }

        public int hashCode() {
            return 0;
        }

        public String toString() {
            return "policy=" + policyToString(this.policy) + ", policySub=" + this.policySub + ", lastMultiScreenState=" + this.lastMultiScreenState + ", useProximitySensor=" + this.useProximitySensor + ", screenBrightness=" + this.screenBrightness + ", screenAutoBrightnessAdjustment=" + this.screenAutoBrightnessAdjustment + ", brightnessSetByUser=" + this.brightnessSetByUser + ", useAutoBrightness=" + this.useAutoBrightness + ", blockScreenOn=" + this.blockScreenOn + ", lowPowerMode=" + this.lowPowerMode + ", boostScreenBrightness=" + this.boostScreenBrightness + ", dozeScreenBrightness=" + this.dozeScreenBrightness + ", dozeScreenState=" + Display.stateToString(this.dozeScreenState) + ", autoBrightnessLowerLimit=" + this.autoBrightnessLowerLimit + ", autoBrightnessUpperLimit=" + this.autoBrightnessUpperLimit + ", maxBrightness=" + this.maxBrightness + ", minBrightness=" + this.minBrightness + ", autoBrightnessForEbookOnly=" + this.autoBrightnessForEbookOnly + ", forceLcdBacklightOffEnabled=" + this.forceLcdBacklightOffEnabled + ", isAlpmMode=" + this.isAlpmMode + ", lastGoToSleepReason=" + this.lastGoToSleepReason + ", wakeUpEvenThoughProximityPositive=" + this.wakeUpEvenThoughProximityPositive + ", coverClosed=" + this.coverClosed + ", FTAMode=" + this.mFTAMode + ", useTemporaryScreenBrightnessSettingOverride=" + this.useTemporaryScreenBrightnessSettingOverride + ", temporaryScreenBrightnessSettingOverride=" + this.temporaryScreenBrightnessSettingOverride + ", forceDimBrightness=" + this.forceDimBrightness + ", useClearViewBrightnessMode=" + this.useClearViewBrightnessMode + ", useColorWeaknessMode=" + this.useColorWeaknessMode + ", lastUpdateCoverStateTime=" + this.lastUpdateCoverStateTime + ", hasRetailModeApp=" + this.hasRetailModeApp + ", isLidClosed=" + this.isLidClosed + ", isOutdoorMode= " + this.isOutdoorMode;
        }

        public static String policyToString(int policy) {
            switch (policy) {
                case 0:
                    return "OFF";
                case 1:
                    return "DOZE";
                case 2:
                    return "DIM";
                case 3:
                    return "BRIGHT";
                default:
                    return Integer.toString(policy);
            }
        }
    }

    public interface DisplayTransactionListener {
        void onDisplayTransaction();
    }

    public abstract void addScaledPidFromWindowManager(int i, String str);

    public abstract void blankAllDisplaysFromPowerManager();

    public abstract int getCurrentScreenBrightnessBeforeFinal();

    public abstract DisplayInfo getDisplayInfo(int i);

    public abstract void initPowerManagement(DisplayPowerCallbacks displayPowerCallbacks, Handler handler, SensorManager sensorManager);

    public abstract boolean isForceUnblankDisplay();

    public abstract boolean isProximitySensorAvailable();

    public abstract void performTraversalInTransactionFromWindowManager();

    public abstract void registerDisplayTransactionListener(DisplayTransactionListener displayTransactionListener);

    public abstract void removeScaledPidFromWindowManager(int i);

    public abstract void removeScaledPidsFromWindowManager();

    public abstract boolean requestPowerState(DisplayPowerRequest displayPowerRequest, boolean z);

    public abstract void setDisplayInfoOverrideFromWindowManager(int i, DisplayInfo displayInfo);

    public abstract void setDisplayOffsets(int i, int i2, int i3);

    public abstract void setDisplayProperties(int i, boolean z, float f, int i2, boolean z2);

    public abstract void setInputMethodDisplayEnabled(boolean z, int i);

    public abstract void unblankAllDisplaysFromPowerManager();

    public abstract void unregisterDisplayTransactionListener(DisplayTransactionListener displayTransactionListener);
}
