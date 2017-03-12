package android.sec.enterprise;

import android.sec.enterprise.EnterpriseDeviceManager.EDMProxyServiceHelper;
import android.util.Log;

public class RestrictionPolicy {
    public static final String SVOICE_PACKAGE1 = "com.vlingo.midas";
    public static final String SVOICE_PACKAGE2 = "com.samsung.voiceserviceplatform";
    private static String TAG = "RestrictionPolicy";
    public static final String WFD_DISABLE = "edm.intent.action.internal.RESTRICTION_DISABLE_WFD";
    public static final String[] settingsExceptions = new String[]{"com.android.settings.ActivityPicker", "com.android.settings.AppWidgetPickActivity", "com.android.settings.widget.SettingsAppWidgetProvider", "com.android.settings.ChooseLockAdditionalPin", "com.android.settings.ChooseLockFaceWarning", "com.android.settings.ChooseLockGeneric", "com.android.settings.ChooseLockMotion", "com.android.settings.ChooseLockPassword", "com.android.settings.ChooseLockPattern", "com.android.settings.ConfirmLockPassword", "com.android.settings.ConfirmLockPattern", "com.android.settings.DeviceAdminAdd", "com.android.settings.bluetooth.DevicePickerActivity", "com.android.settings.wifi.p2p.WifiP2pDeviceList", "com.android.settings.Settings$WifiP2pDevicePickerActivity", "com.android.settings.wfd.WfdPickerActivity", "com.android.settings.bluetooth.BluetoothPairingDialog", "com.samsung.settings.bluetooth.CheckBluetoothStateActivity", "com.android.settings.bluetooth.BluetoothEnableActivity", "com.android.settings.bluetooth.BluetoothEnablingActivity", "com.android.settings.fingerprint.FingerprintLockSettings", "com.android.settings.fingerprint.RegisterFingerprint", "com.android.settings.KnoxSetLockFingerprintPassword", "com.android.settings.KnoxChooseLockFingerprintPassword", "com.android.settings.notification.RedactionInterstitial", "com.android.settings.KnoxFingerprintNotice", "com.samsung.settings.PRIVATEBOX_SETTINGS"};

    public boolean isSettingsChangesAllowed(boolean showMsg) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isSettingsChangesAllowed(showMsg);
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-isSettingsChangesAllowed returning true");
        }
        return true;
    }

    public boolean isNonMarketAppAllowed() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isNonMarketAppAllowed();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-isNonMarketAppAllowed returning default value");
        }
        return true;
    }

    public boolean isCameraEnabled(boolean showMsg) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isCameraEnabled(showMsg);
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-isCameraEnabled returning default value");
        }
        return true;
    }

    public boolean isNFCEnabled() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isNFCEnabled();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-isNFCEnabled returning default value");
        }
        return true;
    }

    public boolean isNFCEnabledWithMsg(boolean showMsg) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isNFCEnabledWithMsg(showMsg);
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-isNFCEnabled returning default value");
        }
        return true;
    }

    public boolean isClipboardAllowed(boolean showMsg) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isClipboardAllowed(showMsg);
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-isClipboardAllowed returning default value");
        }
        return true;
    }

    public boolean isMicrophoneEnabled(boolean showMsg) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isMicrophoneEnabled(showMsg);
            }
        } catch (Exception e) {
        }
        return true;
    }

    public boolean isGoogleCrashReportAllowed() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isGoogleCrashReportAllowed();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-isGoogleCrashReportAllowed returning default value");
        }
        return true;
    }

    public boolean isBackupAllowed(boolean showMsg) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isBackupAllowed(showMsg);
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-isBackupAllowed returning default value");
        }
        return true;
    }

    public boolean isScreenCaptureEnabled(boolean showMsg) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isScreenCaptureEnabled(showMsg);
            }
        } catch (Exception e) {
        }
        return true;
    }

    public boolean isPowerOffAllowed(boolean showMsg) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isPowerOffAllowed(showMsg);
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-isPowerOffAllowed returning default value");
        }
        return true;
    }

    public boolean isAudioRecordAllowed(boolean showMsg) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isAudioRecordAllowed(showMsg);
            }
        } catch (Exception e) {
        }
        return true;
    }

    public boolean isVideoRecordAllowed(boolean showMsg) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isVideoRecordAllowed(showMsg);
            }
        } catch (Exception e) {
        }
        return true;
    }

    public boolean isWifiDirectAllowed(boolean showMsg) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isWifiDirectAllowed(showMsg);
            }
        } catch (Exception e) {
        }
        return true;
    }

    public boolean isBackgroundProcessLimitAllowed() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isBackgroundProcessLimitAllowed();
            }
        } catch (Exception e) {
        }
        return true;
    }

    public boolean isKillingActivitiesOnLeaveAllowed() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isKillingActivitiesOnLeaveAllowed();
            }
        } catch (Exception e) {
        }
        return true;
    }

    public boolean isClipboardShareAllowed() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isClipboardShareAllowed();
            }
        } catch (Exception e) {
        }
        return true;
    }

    public boolean isSVoiceAllowed(boolean showMsg) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isSVoiceAllowed(showMsg);
            }
        } catch (Exception e) {
        }
        return true;
    }

    public boolean isSBeamAllowed(boolean showMsg) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isSBeamAllowed(showMsg);
            }
        } catch (Exception e) {
        }
        return true;
    }

    public boolean isAndroidBeamAllowed(boolean showMsg) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isAndroidBeamAllowed(showMsg);
            }
        } catch (Exception e) {
        }
        return true;
    }
}
