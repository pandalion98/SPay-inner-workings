package android.hardware.usb;

import android.app.PendingIntent;
import android.content.Context;
import android.os.Binder;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;
import android.util.Slog;
import com.android.internal.util.Preconditions;
import java.util.HashMap;

public class UsbManager {
    public static final String ACTION_USB_ACCESSORY_ATTACHED = "android.hardware.usb.action.USB_ACCESSORY_ATTACHED";
    public static final String ACTION_USB_ACCESSORY_DETACHED = "android.hardware.usb.action.USB_ACCESSORY_DETACHED";
    public static final String ACTION_USB_CABLE_STATE = "android.hardware.usb.action.USB_CABLE_STATE";
    public static final String ACTION_USB_DEVICE_ATTACHED = "android.hardware.usb.action.USB_DEVICE_ATTACHED";
    public static final String ACTION_USB_DEVICE_DETACHED = "android.hardware.usb.action.USB_DEVICE_DETACHED";
    public static final String ACTION_USB_PORT_CHANGED = "android.hardware.usb.action.USB_PORT_CHANGED";
    public static final String ACTION_USB_STATE = "android.hardware.usb.action.USB_STATE";
    public static final String EXTRA_ACCESSORY = "accessory";
    public static final String EXTRA_DEVICE = "device";
    public static final String EXTRA_PERMISSION_GRANTED = "permission";
    public static final String EXTRA_PORT = "port";
    public static final String EXTRA_PORT_STATUS = "portStatus";
    private static final String TAG = "UsbManager";
    public static final String USB30_AVAILABLE = "usb30available";
    public static final String USB_BCD = "usb_bcd";
    public static final String USB_CONFIGURED = "configured";
    public static final String USB_CONNECTED = "connected";
    public static final String USB_DATA_UNLOCKED = "unlocked";
    public static final String USB_FUNCTION_ACCESSORY = "accessory";
    public static final String USB_FUNCTION_ADB = "adb";
    public static final String USB_FUNCTION_ASKON = "askon";
    public static final String USB_FUNCTION_AUDIO_SOURCE = "audio_source";
    public static final String USB_FUNCTION_CHARGING = "charging";
    public static final String USB_FUNCTION_DIAG_ACM = "diag,acm";
    public static final String USB_FUNCTION_DM_ACM_ADB = "dm,acm,adb";
    public static final String USB_FUNCTION_MASS_MTP = "mass_storage,mtp";
    public static final String USB_FUNCTION_MASS_STORAGE = "mass_storage";
    public static final String USB_FUNCTION_MIDI = "midi";
    public static final String USB_FUNCTION_MTP = "mtp";
    public static final String USB_FUNCTION_MTP_ADB = "mtp,adb";
    public static final String USB_FUNCTION_NONE = "none";
    public static final String USB_FUNCTION_PTP = "ptp";
    public static final String USB_FUNCTION_PTP_ADB = "ptp,adb";
    public static final String USB_FUNCTION_RNDIS = "rndis";
    public static final String USB_FUNCTION_RNDIS_ACM_DIAG = "rndis,acm,diag";
    public static final String USB_FUNCTION_RNDIS_ACM_DM = "rndis,acm,dm";
    public static final String USB_FUNCTION_RNDIS_ACM_DM_ADB = "rndis,acm,dm,adb";
    public static final String USB_FUNCTION_RNDIS_ADB = "rndis,adb";
    public static final String USB_FUNCTION_RNDIS_DIAG = "rndis,diag";
    public static final String USB_FUNCTION_RNDIS_DM = "rndis,dm";
    public static final String USB_FUNCTION_SEC_CHARGING = "sec_charging";
    public static final String USB_FUNCTION_VZW_CHARGING = "vzw_charging";
    private final Context mContext;
    private final IUsbManager mService;

    public UsbManager(Context context, IUsbManager service) {
        this.mContext = context;
        this.mService = service;
    }

    public HashMap<String, UsbDevice> getDeviceList() {
        Bundle bundle = new Bundle();
        try {
            this.mService.getDeviceList(bundle);
            HashMap<String, UsbDevice> hashMap = new HashMap();
            for (String name : bundle.keySet()) {
                hashMap.put(name, (UsbDevice) bundle.get(name));
            }
            return hashMap;
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in getDeviceList", e);
            return null;
        }
    }

    public UsbDeviceConnection openDevice(UsbDevice device) {
        try {
            String deviceName = device.getDeviceName();
            ParcelFileDescriptor pfd = this.mService.openDevice(deviceName);
            if (pfd != null) {
                UsbDeviceConnection connection = new UsbDeviceConnection(device);
                boolean result = connection.open(deviceName, pfd);
                pfd.close();
                if (result) {
                    return connection;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "exception in UsbManager.openDevice", e);
        }
        return null;
    }

    public UsbAccessory[] getAccessoryList() {
        try {
            if (this.mService.getCurrentAccessory() == null) {
                return null;
            }
            return new UsbAccessory[]{this.mService.getCurrentAccessory()};
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in getAccessoryList", e);
            return null;
        }
    }

    public ParcelFileDescriptor openAccessory(UsbAccessory accessory) {
        try {
            return this.mService.openAccessory(accessory);
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in openAccessory", e);
            return null;
        }
    }

    public boolean hasPermission(UsbDevice device) {
        try {
            return this.mService.hasDevicePermission(device);
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in hasPermission", e);
            return false;
        }
    }

    public boolean hasPermission(UsbAccessory accessory) {
        try {
            return this.mService.hasAccessoryPermission(accessory);
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in hasPermission", e);
            return false;
        }
    }

    public void requestPermission(UsbDevice device, PendingIntent pi) {
        try {
            this.mService.requestDevicePermission(device, this.mContext.getPackageName(), pi);
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in requestPermission", e);
        }
    }

    public void requestPermission(UsbAccessory accessory, PendingIntent pi) {
        try {
            this.mService.requestAccessoryPermission(accessory, this.mContext.getPackageName(), pi);
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in requestPermission", e);
        }
    }

    public boolean isFunctionEnabled(String function) {
        try {
            return this.mService.isFunctionEnabled(function);
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in setCurrentFunction", e);
            return false;
        }
    }

    public void setCurrentFunction(String function) {
        try {
            Log.d(TAG, "setCurrentFunction : " + function);
            this.mService.setCurrentFunction(function);
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in setCurrentFunction", e);
        }
    }

    public void setUsbDataUnlocked(boolean unlocked) {
        try {
            this.mService.setUsbDataUnlocked(unlocked);
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in setUsbDataUnlocked", e);
        }
    }

    public UsbPort[] getPorts() {
        try {
            return this.mService.getPorts();
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in getPorts", e);
            return null;
        }
    }

    public UsbPortStatus getPortStatus(UsbPort port) {
        Preconditions.checkNotNull(port, "port must not be null");
        try {
            return this.mService.getPortStatus(port.getId());
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in getPortStatus", e);
            return null;
        }
    }

    public void setPortRoles(UsbPort port, int powerRole, int dataRole) {
        Log.d(TAG, "setPortRoles() portId : " + port.getId() + ", powerRole : " + powerRole + ", dataRole : " + dataRole);
        Preconditions.checkNotNull(port, "port must not be null");
        UsbPort.checkRoles(powerRole, dataRole);
        try {
            this.mService.setPortRoles(port.getId(), powerRole, dataRole);
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in setPortRole", e);
        }
    }

    public static String addFunction(String functions, String function) {
        if ("none".equals(functions)) {
            return function;
        }
        if (!containsFunction(functions, function)) {
            if (functions.length() > 0) {
                functions = functions + ",";
            }
            functions = functions + function;
        }
        return functions;
    }

    public static String removeFunction(String functions, String function) {
        String[] split = functions.split(",");
        for (int i = 0; i < split.length; i++) {
            if (function.equals(split[i])) {
                split[i] = null;
            }
        }
        if (split.length == 1 && split[0] == null) {
            return "none";
        }
        StringBuilder builder = new StringBuilder();
        for (String s : split) {
            if (s != null) {
                if (builder.length() > 0) {
                    builder.append(",");
                }
                builder.append(s);
            }
        }
        return builder.toString();
    }

    public static boolean containsFunction(String functions, String function) {
        int index = functions.indexOf(function);
        if (index < 0) {
            return false;
        }
        if (index > 0 && functions.charAt(index - 1) != ',') {
            return false;
        }
        int charAfter = index + function.length();
        if (charAfter >= functions.length() || functions.charAt(charAfter) == ',') {
            return true;
        }
        return false;
    }

    public String getCurrentFunctions() {
        try {
            return this.mService.getCurrentFunctions();
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in getCurrentFunctions", e);
            return null;
        }
    }

    public boolean isUsbBlocked() {
        try {
            return this.mService.isUsbBlocked();
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in isUsbBlocked", e);
            return false;
        }
    }

    public void setUsb30Mode(boolean modeUSB30on) {
        Slog.d(TAG, " :::: setUsb30Mode :: mode = " + modeUSB30on + " from pid = " + Binder.getCallingPid());
        try {
            this.mService.setUsb30Mode(modeUSB30on);
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in setUsb30Mode", e);
        }
    }

    public boolean isUsb30Enabled() {
        try {
            boolean ret = this.mService.isUsb30Enabled();
            Slog.d(TAG, " :::: isUsb30Enabled :: return = " + ret + " from pid = " + Binder.getCallingPid());
            return ret;
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in isUsb30Enabled", e);
            return false;
        }
    }

    public boolean isUsb30Available() {
        try {
            boolean ret = this.mService.isUsb30Available();
            Slog.d(TAG, " :::: isUsb30Available :: return = " + ret + " from pid = " + Binder.getCallingPid());
            return ret;
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in isUsb30Available", e);
            return false;
        }
    }

    public int countKeyBoardConnectedviaUsbHost() {
        try {
            int ret = this.mService.countKeyBoardConnectedviaUsbHost();
            Slog.d(TAG, " :::: countKeyBoardConnectedviaUsbHost :: return = " + ret + " from pid = " + Binder.getCallingPid());
            return ret;
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in countKeyBoardConnectedviaUsbHost", e);
            return 0;
        }
    }

    public int countMouseConnectedviaUsbHost() {
        try {
            int ret = this.mService.countMouseConnectedviaUsbHost();
            Slog.d(TAG, " :::: countMouseConnectedviaUsbHost :: return = " + ret + " from pid = " + Binder.getCallingPid());
            return ret;
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in countMouseConnectedviaUsbHost", e);
            return 0;
        }
    }
}
