package com.sec.enterprise.knoxcustom;

import android.sec.enterprise.EnterpriseDeviceManager.EDMProxyServiceHelper;
import android.sec.enterprise.IEDMProxy;
import android.util.Log;

public class KnoxCustomManager {
    public static final int KEYBOARD_MODE_NORMAL = 0;
    public static final int KEYBOARD_MODE_PREDICTION_OFF = 1;
    public static final int KEYBOARD_MODE_SETTINGS_OFF = 2;
    public static final int NOTIFICATIONS_ALL = 31;
    public static final int NOTIFICATIONS_BATTERY_FULL = 2;
    public static final int NOTIFICATIONS_BATTERY_LOW = 1;
    public static final int NOTIFICATIONS_NITZ_SET_TIME = 16;
    public static final int NOTIFICATIONS_SAFE_VOLUME = 4;
    public static final int NOTIFICATIONS_STATUS_BAR = 8;
    public static final int SENSOR_ACCELEROMETER = 2;
    public static final int SENSOR_ALL = 63;
    public static final int SENSOR_GYROSCOPE = 1;
    public static final int SENSOR_LIGHT = 4;
    public static final int SENSOR_MAGNETIC = 32;
    public static final int SENSOR_ORIENTATION = 8;
    public static final int SENSOR_PROXIMITY = 16;
    private static final String TAG = "KnoxCustomManager";
    public static final int VOLUME_CONTROL_STREAM_DEFAULT = 0;
    public static final int VOLUME_CONTROL_STREAM_MUSIC = 3;
    public static final int VOLUME_CONTROL_STREAM_NOTIFICATION = 4;
    public static final int VOLUME_CONTROL_STREAM_RING = 2;
    public static final int VOLUME_CONTROL_STREAM_SYSTEM = 1;

    public boolean getSealedState() {
        try {
            if (EDMProxyServiceHelper.getService() != null) {
                return EDMProxyServiceHelper.getService().getSealedState();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getSealedState returning default value");
        }
        return false;
    }

    public boolean getSealedNotificationMessagesState() {
        try {
            if (EDMProxyServiceHelper.getService() != null) {
                return EDMProxyServiceHelper.getService().getSealedNotificationMessagesState();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getSealedNotificationMessagesState returning default value");
        }
        return true;
    }

    public int getSealedHideNotificationMessages() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.getSealedHideNotificationMessages();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getSealedHideNotificationMessages returning default value");
        }
        return 0;
    }

    public int getVolumeControlStream() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.getVolumeControlStream();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getVolumeControlStream returning default value");
        }
        return 0;
    }

    public boolean getToastEnabledState() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.getToastEnabledState();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getToastEnabledState returning default value");
        }
        return true;
    }

    public boolean getToastShowPackageNameState() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.getToastShowPackageNameState();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getToastShowPackageNameState returning default value");
        }
        return false;
    }

    public int getSensorDisabled() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.getSensorDisabled();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getSensorDisabled returning default value");
        }
        return 0;
    }

    public boolean getVolumePanelEnabledState() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.getVolumePanelEnabledState();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getVolumePanelEnabledState returning default value");
        }
        return true;
    }

    public boolean getVolumeButtonRotationState() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.getVolumeButtonRotationState();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getVolumeButtonRotationState returning default value");
        }
        return false;
    }

    public boolean getWifiAutoSwitchState() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.getWifiAutoSwitchState();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getWifiAutoSwitchState returning default value");
        }
        return false;
    }

    public int getWifiAutoSwitchThreshold() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.getWifiAutoSwitchThreshold();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getWifiAutoSwitchThreshold returning default value");
        }
        return -200;
    }

    public int getWifiAutoSwitchDelay() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.getWifiAutoSwitchDelay();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getWifiAutoSwitchDelay returning default value");
        }
        return 20;
    }

    public boolean getToastGravityEnabledState() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.getToastGravityEnabledState();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getToastGravityEnabledState returning default value");
        }
        return false;
    }

    public int getToastGravity() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.getToastGravity();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getToastGravity returning default value");
        }
        return 0;
    }

    public int getToastGravityXOffset() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.getToastGravityXOffset();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getToastGravityXOffset returning default value");
        }
        return 0;
    }

    public int getToastGravityYOffset() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.getToastGravityYOffset();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getToastGravityYOffset returning default value");
        }
        return 0;
    }

    public int getKeyboardMode() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.getKeyboardMode();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getKeyboardMode returning default value");
        }
        return 0;
    }

    public boolean getWifiState() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.getWifiState();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getWifiState() FAIL");
        }
        return false;
    }
}
