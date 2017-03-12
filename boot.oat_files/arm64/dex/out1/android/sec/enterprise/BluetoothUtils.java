package android.sec.enterprise;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothUuid;
import android.os.Binder;
import android.os.ParcelUuid;
import android.os.Process;
import android.os.UserHandle;
import android.sec.enterprise.BluetoothPolicy.BluetoothUUID;
import android.sec.enterprise.auditlog.AuditEvents;
import android.sec.enterprise.auditlog.AuditLog;
import android.util.Log;

public class BluetoothUtils {
    public static final int NO_PROFILE = -1;
    private static final String TAG = "BluetoothUtils";
    static final int TYPE_L2CAP = 3;
    static final int TYPE_RFCOMM = 1;
    static final int TYPE_SCO = 2;

    public static boolean isSocketAllowedBySecurityPolicy(BluetoothDevice device, int aPortNum, int aPortType, ParcelUuid uuid) {
        long token;
        BluetoothPolicy service = EnterpriseDeviceManager.getInstance().getBluetoothPolicy();
        if (1 == aPortType && (!service.getAllowBluetoothDataTransfer(true) || !service.isProfileEnabled(128))) {
            Log.w("BluetoothUtils", "isSocketAllowedBySecurityPolicy : device requesting for spp, block it");
            token = Binder.clearCallingIdentity();
            try {
                AuditLog.logAsUser(5, 5, false, Process.myPid(), "BluetoothUtils", AuditEvents.BT_EXCHANGING_DATA_FAILED, UserHandle.getUserId(Binder.getCallingUid()));
                return false;
            } finally {
                Binder.restoreCallingIdentity(token);
            }
        } else if (uuid != null && (!service.isBluetoothUUIDAllowed(uuid.toString()) || !getBluetoothProfileEnabled(uuid))) {
            Log.d("BluetoothUtils", "MDM: profile UUID = " + uuid.toString() + " is disabled");
            return false;
        } else if (device == null || aPortNum < 0) {
            Log.d("BluetoothUtils", "isSocketAllowedBySecurityPolicy start : device null");
            return true;
        } else {
            ParcelUuid[] ids = device.getUuids();
            if (ids == null) {
                return true;
            }
            for (int i = 0; i < ids.length; i++) {
            }
            if (1 == aPortType) {
                token = Binder.clearCallingIdentity();
                try {
                    AuditLog.logAsUser(5, 5, true, Process.myPid(), "BluetoothUtils", AuditEvents.BT_EXCHANGING_DATA_SUCCEEDED, UserHandle.getUserId(Binder.getCallingUid()));
                } finally {
                    Binder.restoreCallingIdentity(token);
                }
            }
            return true;
        }
    }

    private static boolean getBluetoothProfileEnabled(ParcelUuid uuid) {
        int profile = -1;
        if (BluetoothUuid.isSerialPort(uuid)) {
            profile = 128;
        } else if (BluetoothUuid.PBAP_PSE.equals(uuid) || BluetoothUuid.isPbap(uuid)) {
            profile = 4;
        } else if (BluetoothUuid.isSap(uuid)) {
            profile = 256;
        } else if (BluetoothUuid.BasicPrinting.equals(uuid)) {
            profile = 512;
        } else if (BluetoothUuid.isAudioSource(uuid) || BluetoothUuid.isAdvAudioDist(uuid)) {
            profile = 8;
        } else if (BluetoothUuid.HSP.equals(uuid) || BluetoothUuid.HSP_AG.equals(uuid)) {
            profile = 1;
        } else if (BluetoothUuid.Handsfree.equals(uuid) || BluetoothUuid.Handsfree_AG.equals(uuid)) {
            profile = 2;
        }
        if (profile != -1) {
            return EnterpriseDeviceManager.getInstance().getBluetoothPolicy().isProfileEnabled(profile);
        }
        return true;
    }

    public static boolean isSvcRfComPortNumberBlockedBySecurityPolicy(int aPortNum) {
        try {
            BluetoothPolicy service = EnterpriseDeviceManager.getInstance().getBluetoothPolicy();
            if (service.isProfileEnabled(128)) {
                RESERVED_RFCOMM_CHANNELS = new String[2][];
                RESERVED_RFCOMM_CHANNELS[0] = new String[]{"12", BluetoothUUID.OBEXOBJECTPUSH_UUID};
                RESERVED_RFCOMM_CHANNELS[1] = new String[]{"19", BluetoothUUID.PBAP_UUID};
                int i = 0;
                while (i < RESERVED_RFCOMM_CHANNELS.length) {
                    String port = RESERVED_RFCOMM_CHANNELS[i][0];
                    String uuid = RESERVED_RFCOMM_CHANNELS[i][1];
                    if (Integer.parseInt(port) != aPortNum || service.isBluetoothUUIDAllowed(uuid)) {
                        i++;
                    } else {
                        Log.d("BluetoothUtils", "MDM: Profile UUID = " + uuid + " Blocked");
                        return true;
                    }
                }
                return false;
            }
            Log.d("BluetoothUtils", "MDM - SPP Profile is disabled");
            return false;
        } catch (Exception e) {
            Log.w("BluetoothUtils", e.toString());
            return false;
        }
    }

    public static boolean isHeadsetAllowedBySecurityPolicy(BluetoothDevice device) {
        if (device == null) {
            return true;
        }
        BluetoothPolicy service = EnterpriseDeviceManager.getInstance().getBluetoothPolicy();
        if (!service.isProfileEnabled(128)) {
            Log.d("BluetoothUtils", "MDM - SPP Profile is disabled");
            return false;
        } else if (!service.isProfileEnabled(1)) {
            Log.d("BluetoothUtils", "MDM: HSP profile  is disabled");
            return false;
        } else if (!service.isProfileEnabled(2)) {
            Log.d("BluetoothUtils", "MDM: HFP profile is disabled");
            return false;
        } else if (service.isBluetoothDeviceAllowed(device.toString())) {
            return true;
        } else {
            Log.d("BluetoothUtils", "MDM: Remote Device Blocked");
            return false;
        }
    }

    public static boolean isPairingAllowedbySecurityPolicy(String address) {
        BluetoothPolicy service = EnterpriseDeviceManager.getInstance().getBluetoothPolicy();
        if (!service.isPairingEnabled()) {
            Log.d("BluetoothUtils", "MDM: Pairing Blocked");
            return false;
        } else if (service.isBluetoothDeviceAllowed(address)) {
            return true;
        } else {
            Log.d("BluetoothUtils", "MDM: Remote Device Blocked");
            return false;
        }
    }

    public static boolean isProfileAuthorizedBySecurityPolicy(ParcelUuid uuid) {
        return isProfileAuthorizedBySecurityPolicy(uuid, 1);
    }

    public static boolean isProfileAuthorizedBySecurityPolicy(ParcelUuid uuid, int portType) {
        long token;
        BluetoothPolicy service = EnterpriseDeviceManager.getInstance().getBluetoothPolicy();
        if (2 == portType && !service.isOutgoingCallsAllowed()) {
            Log.d("BluetoothUtils", "MDM: Outgoing Call is Disabled");
            return false;
        } else if ((BluetoothUuid.isAudioSource(uuid) || BluetoothUuid.isAdvAudioDist(uuid)) && !(service.isProfileEnabled(8) && service.isProfileEnabled(128))) {
            Log.d("BluetoothUtils", "MDM: SPP or A2DP profile is disabled");
            return false;
        } else if ((BluetoothUuid.isAvrcpTarget(uuid) || BluetoothUuid.isAvrcpController(uuid)) && !(service.isProfileEnabled(16) && service.isProfileEnabled(128))) {
            Log.d("BluetoothUtils", "MDM: AVRCP profile is disabled");
            return false;
        } else if (uuid.equals(BluetoothUuid.ObexObjectPush) && (!service.getAllowBluetoothDataTransfer(true) || !service.isProfileEnabled(128))) {
            Log.d("BluetoothUtils", "MDM: OPP profile is disabled");
            token = Binder.clearCallingIdentity();
            try {
                AuditLog.logAsUser(5, 5, false, Process.myPid(), "BluetoothUtils", AuditEvents.BT_EXCHANGING_DATA_FAILED, UserHandle.getUserId(Binder.getCallingUid()));
                return false;
            } finally {
                Binder.restoreCallingIdentity(token);
            }
        } else if (BluetoothUuid.isMap(uuid) && !service.isProfileEnabled(128)) {
            Log.d("BluetoothUtils", "MDM: MAP profile is disabled");
            return false;
        } else if (BluetoothUuid.isSap(uuid) && (!service.isProfileEnabled(256) || !service.isProfileEnabled(128))) {
            Log.d("BluetoothUtils", "MDM: SAP profile is disabled");
            return false;
        } else if (service.isBluetoothUUIDAllowed(uuid.toString()) && getBluetoothProfileEnabled(uuid)) {
            if (uuid.equals(BluetoothUuid.ObexObjectPush)) {
                token = Binder.clearCallingIdentity();
                try {
                    AuditLog.logAsUser(5, 5, true, Process.myPid(), "BluetoothUtils", AuditEvents.BT_EXCHANGING_DATA_SUCCEEDED, UserHandle.getUserId(Binder.getCallingUid()));
                } finally {
                    Binder.restoreCallingIdentity(token);
                }
            }
            return true;
        } else {
            Log.d("BluetoothUtils", "MDM: profile UUID = " + uuid.toString() + " is disabled");
            return false;
        }
    }

    public static void bluetoothLog(String tag, String msg) {
        try {
            EnterpriseDeviceManager.getInstance().getBluetoothPolicy().bluetoothLog(tag, msg);
        } catch (Exception e) {
            Log.e("BluetoothUtils", "Exception on blutoothLog");
        }
    }

    public static void bluetoothSocketLog(String tag, BluetoothDevice device, int aPortNum, int aPortType) {
        if (device != null) {
            switch (aPortType) {
                case 1:
                    try {
                        bluetoothLog("RFCOMM " + tag, device.getName(), device.getAddress());
                        return;
                    } catch (Exception e) {
                        Log.e("BluetoothUtils", "Exception on bluetoothLogSocket");
                    }
                case 2:
                    bluetoothLog("SCO " + tag, device.getName(), device.getAddress());
                    return;
                case 3:
                    bluetoothLog("L2CAP " + tag, device.getName(), device.getAddress());
                    return;
                default:
                    return;
            }
            Log.e("BluetoothUtils", "Exception on bluetoothLogSocket");
        }
    }

    public static void bluetoothLog(String tag, String remoteName, String remoteAddress) {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        StringBuilder logMsg = new StringBuilder();
        if (adapter != null) {
            logMsg.append("Local Name: ");
            logMsg.append(adapter.getName());
            logMsg.append('\n');
            logMsg.append("Local Address: ");
            logMsg.append(adapter.getAddress());
            logMsg.append('\n');
        }
        if (remoteName != null && remoteName.length() > 0) {
            logMsg.append("Remote Name: ");
            logMsg.append(remoteName);
            logMsg.append('\n');
        }
        if (remoteAddress != null && remoteAddress.length() > 0) {
            logMsg.append("Remote Address: ");
            logMsg.append(remoteAddress);
            logMsg.append('\n');
        }
        bluetoothLog(tag, logMsg.toString());
    }

    public static boolean isBluetoothLogEnabled() {
        try {
            return EnterpriseDeviceManager.getInstance().getBluetoothPolicy().isBluetoothLogEnabled();
        } catch (Exception e) {
            Log.e("BluetoothUtils", "Exception on isBluetoothLogEnabled");
            return false;
        }
    }

    public static void bluetoothLog(String tag, int profile, BluetoothDevice device) {
        String localName = "";
        String localAddress = "";
        String remoteName = "";
        String remoteAddress = "";
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null) {
            localName = adapter.getName();
            localAddress = adapter.getAddress();
        }
        if (device != null) {
            remoteName = device.getName();
            remoteAddress = device.getAddress();
        }
        StringBuilder logMsg = new StringBuilder("");
        if (profile != -1) {
            logMsg.append(convertBluetoothProfile(profile));
        }
        if (remoteAddress != null && remoteAddress.length() > 0) {
            logMsg.append("Remote Address: ");
            logMsg.append(remoteAddress);
            logMsg.append('\n');
        }
        if (remoteAddress != null && remoteAddress.length() > 0) {
            logMsg.append("Remote Name: ");
            logMsg.append(remoteName);
            logMsg.append('\n');
        }
        if (localAddress != null && localAddress.length() > 0) {
            logMsg.append("Local Address: ");
            logMsg.append(localAddress);
            logMsg.append('\n');
        }
        if (localAddress != null && localAddress.length() > 0) {
            logMsg.append("Local Name: ");
            logMsg.append(localName);
            logMsg.append('\n');
        }
        bluetoothLog(tag, logMsg.toString());
    }

    private static String convertBluetoothProfile(int profile) {
        switch (profile) {
            case 1:
                return "Profile: Headset and Handsfree\n";
            case 2:
                return "Profile: A2DP\n";
            case 3:
                return "Profile: HEALTH\n";
            case 4:
                return "Profile: INPUT DEVICE\n";
            case 5:
                return "Profile: PAN\n";
            case 6:
                return "Profile: PBAP\n";
            case 9:
                return "Profile: MAP\n";
            default:
                return "";
        }
    }
}
