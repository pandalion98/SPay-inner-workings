package android.sec.enterprise;

import android.sec.enterprise.EnterpriseDeviceManager.EDMProxyServiceHelper;
import android.sec.enterprise.content.SecContentProviderURI;
import android.util.Log;

public class BluetoothPolicy {
    public static final int NO_PROFILE = -1;
    private static String TAG = SecContentProviderURI.BLUETOOTHPOLICY;

    public class BluetoothProfile {
        public static final int BLUETOOTH_A2DP_PROFILE = 8;
        public static final int BLUETOOTH_AVRCP_PROFILE = 16;
        public static final int BLUETOOTH_BPP_PROFILE = 512;
        public static final int BLUETOOTH_DUN_PROFILE = 32;
        public static final int BLUETOOTH_FTP_PROFILE = 64;
        public static final int BLUETOOTH_HFP_PROFILE = 2;
        public static final int BLUETOOTH_HSP_PROFILE = 1;
        public static final int BLUETOOTH_PBAP_PROFILE = 4;
        public static final int BLUETOOTH_SAP_PROFILE = 256;
        public static final int BLUETOOTH_SPP_PROFILE = 128;
    }

    public class BluetoothUUID {
        public static final String A2DP_ADVAUDIODIST_UUID = "0000110D-0000-1000-8000-00805F9B34FB";
        public static final String A2DP_AUDIOSINK_UUID = "0000110B-0000-1000-8000-00805F9B34FB";
        public static final String A2DP_AUDIOSOURCE_UUID = "0000110A-0000-1000-8000-00805F9B34FB";
        public static final String AVRCP_CONTROLLER_UUID = "0000110E-0000-1000-8000-00805F9B34FB";
        public static final String AVRCP_TARGET_UUID = "0000110C-0000-1000-8000-00805F9B34FB";
        public static final String BNEP_UUID = "0000000f-0000-1000-8000-00805F9B34FB";
        public static final String BPP_UUID = "00001122-0000-1000-8000-00805f9b34fb";
        public static final String DUN_UUID = "00001103-0000-1000-8000-00805f9b34fb";
        public static final String FTP_UUID = "00001106-0000-1000-8000-00805f9b34fb";
        public static final String HFP_AG_UUID = "0000111F-0000-1000-8000-00805F9B34FB";
        public static final String HFP_UUID = "0000111E-0000-1000-8000-00805F9B34FB";
        public static final String HID_UUID = "00001124-0000-1000-8000-00805f9b34fb";
        public static final String HSP_AG_UUID = "00001112-0000-1000-8000-00805F9B34FB";
        public static final String HSP_UUID = "00001108-0000-1000-8000-00805F9B34FB";
        public static final String NAP_UUID = "00001116-0000-1000-8000-00805F9B34FB";
        public static final String OBEXOBJECTPUSH_UUID = "00001105-0000-1000-8000-00805f9b34fb";
        public static final String PANU_UUID = "00001115-0000-1000-8000-00805F9B34FB";
        public static final String PBAP_PSE_UUID = "0000112f-0000-1000-8000-00805F9B34FB";
        public static final String PBAP_UUID = "00001130-0000-1000-8000-00805f9b34fb";
        public static final String SAP_UUID = "0000112D-0000-1000-8000-00805F9B34FB";
        public static final String SPP_UUID = "00001101-0000-1000-8000-00805f9b34fb";
    }

    public boolean getAllowBluetoothDataTransfer(boolean showMsg) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.getAllowBluetoothDataTransfer(showMsg);
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-getAllowBluetoothDataTransfer returning default value");
        }
        return true;
    }

    public boolean isOutgoingCallsAllowed() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isOutgoingCallsAllowed();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-isOutgoingCallsAllowed returning default value");
        }
        return true;
    }

    public boolean isDesktopConnectivityEnabled() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isDesktopConnectivityEnabled();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-isDesktopConnectivityEnabled returning default value");
        }
        return true;
    }

    public boolean isBluetoothUUIDAllowed(String uuid) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isBluetoothUUIDAllowed(uuid);
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-isBluetoothUUIDAllowed returning default value");
        }
        return true;
    }

    public boolean isProfileEnabled(int profile) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isProfileEnabled(profile);
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-isProfileEnabled returning default value");
        }
        return true;
    }

    public boolean isBluetoothDeviceAllowed(String deviceAddress) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isBluetoothDeviceAllowed(deviceAddress);
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-isBluetoothDeviceAllowed returning default value");
        }
        return true;
    }

    public boolean isPairingEnabled() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isPairingEnabled();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-isPairingEnabled returning default value");
        }
        return true;
    }

    public boolean isDiscoverableEnabled() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isDiscoverableEnabled();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-isDiscoverableEnabled returning default value");
        }
        return true;
    }

    public boolean isLimitedDiscoverableEnabled() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isLimitedDiscoverableEnabled();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-isLimitedDiscoverableEnabled returning default value");
        }
        return true;
    }

    public boolean isBluetoothEnabled() {
        boolean enabled = true;
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isBluetoothEnabled();
            }
        } catch (Exception e) {
            enabled = true;
            Log.d(TAG, "PXY-isBluetoothEnabled returning default value");
        }
        return enabled;
    }

    public boolean isBluetoothLogEnabled() {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isBluetoothLogEnabled();
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-isBluetoothLogEnabled returning default value");
        }
        return false;
    }

    public void bluetoothLog(String tag, String msg) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                lService.bluetoothLog(tag, msg);
            }
        } catch (Exception e) {
            Log.d(TAG, "PXY-bluetoothLog returning default value");
        }
    }
}
