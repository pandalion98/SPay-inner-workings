package android.app;

import android.Manifest.permission;
import android.bluetooth.IBluetoothSecureManagerService.Stub;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.net.ProxyInfo;
import android.os.Environment;
import android.util.Log;
import java.io.File;

public class BluetoothSecureManagerService extends Stub {
    private static final String BLUETOOTH_ADMIN_PERM = "android.permission.BLUETOOTH_ADMIN";
    private static final boolean DBG = true;
    public static final String Name = "bluetooth_secure_mode_manager";
    public static final String SECURE_MANAGER_ENABLE = "secure_mode_enable";
    public static final String SECURE_MANAGER_WHITE_LIST_ENABLE = "white_list_enable";
    public static final String SECURE_MANAGER_WHITE_LIST_MAX = "white_list_max_index";
    public static final String SECURE_MANAGER_WHITE_LIST_PREFIX = "white_list";
    private static final String TAG = "BluetoothSecureManagerService";
    private final String SECURE_CONFIG_PATH = (Environment.getDataDirectory() + "/system/bt_secure_manager_config.xml");
    private final Context mContext;
    private SharedPreferencesImpl mPrefs;

    private static final class WhiteListValue {
        int cod;
        String name;
        String[] uuids;

        private WhiteListValue() {
        }
    }

    public BluetoothSecureManagerService(Context context) {
        this.mContext = context;
        this.mPrefs = new SharedPreferencesImpl(new File(this.SECURE_CONFIG_PATH), 0);
    }

    private void enforcePermission() {
        this.mContext.enforceCallingOrSelfPermission(permission.BLUETOOTH_SECUREMODE_INTERNAL, null);
    }

    public static final String getSecureSettingName(String valueName) {
        return valueName;
    }

    private final int findWhiteListIndex(String name, int cod) {
        int max = this.mPrefs.getInt(SECURE_MANAGER_WHITE_LIST_MAX, 0);
        for (int i = 1; i <= max; i++) {
            String key = SECURE_MANAGER_WHITE_LIST_PREFIX + Integer.toString(i);
            if (this.mPrefs.contains(key)) {
                WhiteListValue v = unpackWhiteListValue(this.mPrefs.getString(key, ProxyInfo.LOCAL_EXCL_LIST));
                if (v != null && v.name.equals(name) && v.cod == cod) {
                    return i;
                }
            }
        }
        return -1;
    }

    private final int findWhiteListFreeIndex() {
        int max = this.mPrefs.getInt(SECURE_MANAGER_WHITE_LIST_MAX, 0);
        int i = 1;
        while (i <= max) {
            if (!this.mPrefs.contains(getWhiteListKey(i))) {
                break;
            }
            i++;
        }
        return i;
    }

    private final String getWhiteListKey(int index) {
        return SECURE_MANAGER_WHITE_LIST_PREFIX + Integer.toString(index);
    }

    private final String packWhiteListValue(String name, int cod, String[] uuids) {
        StringBuffer sb = new StringBuffer(256);
        sb.append(name);
        sb.append(';');
        sb.append(Integer.toHexString(cod));
        sb.append(';');
        for (String u : uuids) {
            sb.append(u);
            sb.append(';');
        }
        return sb.toString();
    }

    private final WhiteListValue unpackWhiteListValue(String packedValues) {
        String[] values = packedValues.split(";");
        Log.d(TAG, "unpackWhiteListValue v: " + packedValues + "; split len: " + values.length);
        if (values.length == 0) {
            return null;
        }
        WhiteListValue v = new WhiteListValue();
        if (values.length >= 1) {
            v.name = values[0];
        }
        if (values.length >= 2) {
            v.cod = Integer.parseInt(values[1], 16);
        }
        if (values.length <= 2) {
            return v;
        }
        v.uuids = new String[(values.length - 2)];
        for (int i = 2; i < values.length; i++) {
            v.uuids[i - 2] = values[i];
        }
        return v;
    }

    public boolean enableSecureMode(boolean enable) {
        Log.d(TAG, "enableSecureMode enable: " + enable);
        return setSecureModeSetting(SECURE_MANAGER_ENABLE, enable ? 1 : 0);
    }

    public boolean isSecureModeEnabled() {
        Log.d(TAG, "isSecureModeEnabled");
        if (getSecureModeSetting(SECURE_MANAGER_ENABLE) == 1) {
            return true;
        }
        return false;
    }

    public int getSecureModeSetting(String valueName) {
        int i = 0;
        Log.d(TAG, "getSecureModeSetting, name: " + valueName);
        try {
            i = this.mPrefs.getInt(getSecureSettingName(valueName), 0);
        } catch (ClassCastException e) {
            Log.d(TAG, "getSecureModeSetting name:" + valueName + "; " + e);
        }
        return i;
    }

    public boolean setSecureModeSetting(String valueName, int value) {
        Log.d(TAG, "setSecureModeSetting, name: " + valueName + ", value: " + value);
        enforcePermission();
        Editor ed = this.mPrefs.edit();
        ed.putInt(getSecureSettingName(valueName), value);
        Log.d(TAG, "setSecureModeSetting calling apply()");
        ed.apply();
        Log.d(TAG, "setSecureModeSetting apply() returned");
        return true;
    }

    public boolean enableWhiteList(boolean enable) {
        Log.d(TAG, "enableWhiteList, enable: " + enable);
        return setSecureModeSetting(SECURE_MANAGER_WHITE_LIST_ENABLE, enable ? 1 : 0);
    }

    public boolean isWhiteListEnabled() {
        Log.d(TAG, "isWhiteListEnabled");
        if (getSecureModeSetting(SECURE_MANAGER_WHITE_LIST_ENABLE) == 1) {
            return true;
        }
        return false;
    }

    public boolean addWhiteList(String name, int cod, String[] uuids) {
        boolean z = false;
        Log.d(TAG, "addWhiteList: " + name + "; cod: " + cod);
        if (uuids == null) {
            Log.d(TAG, "addWhiteList: uuids is NULL");
        } else {
            Log.d(TAG, "addWhiteList: uuids: " + uuids.toString());
            enforcePermission();
            synchronized (this) {
                Editor ed = this.mPrefs.edit();
                Log.d(TAG, "addWhiteList 1");
                int index = findWhiteListIndex(name, cod);
                Log.d(TAG, "addWhiteList findWhiteListIndex: " + index);
                if (index < 0) {
                    index = findWhiteListFreeIndex();
                }
                int max = this.mPrefs.getInt(SECURE_MANAGER_WHITE_LIST_MAX, 0);
                Log.d(TAG, "addWhiteList max index: " + max);
                if (index > max) {
                    ed.putInt(SECURE_MANAGER_WHITE_LIST_MAX, index);
                }
                String v = packWhiteListValue(name, cod, uuids);
                Log.d(TAG, "addWhiteList index: " + index + "; packed v:" + v);
                ed.putString(getWhiteListKey(index), v).apply();
                z = true;
            }
        }
        return z;
    }

    public boolean removeWhiteList(String name, int cod) {
        boolean z = false;
        Log.d(TAG, "removeWhiteList");
        enforcePermission();
        synchronized (this) {
            int index = findWhiteListIndex(name, cod);
            if (index >= 0) {
                Editor ed = this.mPrefs.edit();
                if (this.mPrefs.getInt(SECURE_MANAGER_WHITE_LIST_MAX, 0) == index) {
                    ed.putInt(SECURE_MANAGER_WHITE_LIST_MAX, index - 1);
                }
                ed.remove(getWhiteListKey(index)).apply();
                z = true;
            }
        }
        return z;
    }

    public int getWhiteListFirstIndex() {
        int i;
        Log.d(TAG, "getWhiteListFirstIndex");
        synchronized (this) {
            int max = this.mPrefs.getInt(SECURE_MANAGER_WHITE_LIST_MAX, 0);
            Log.d(TAG, "getWhiteListFirstIndex: max: " + max);
            i = 1;
            while (i <= max) {
                if (this.mPrefs.contains(getWhiteListKey(i))) {
                    break;
                }
                i++;
            }
            i = -1;
        }
        return i;
    }

    public int getWhiteListNextIndex(int currentIndex) {
        Log.d(TAG, "getWhiteListNextIndex");
        if (currentIndex == -1) {
            return -1;
        }
        synchronized (this) {
            int max = this.mPrefs.getInt(SECURE_MANAGER_WHITE_LIST_MAX, 0);
            for (int i = currentIndex + 1; i <= max; i++) {
                if (this.mPrefs.contains(getWhiteListKey(i))) {
                    return i;
                }
            }
            return -1;
        }
    }

    public String getWhiteListName(int index) {
        Log.d(TAG, "getWhiteListName, index: " + index);
        String values = this.mPrefs.getString(getWhiteListKey(index), ProxyInfo.LOCAL_EXCL_LIST);
        Log.d(TAG, "getWhiteListName, index: " + index + "; values: " + values);
        WhiteListValue v = unpackWhiteListValue(values);
        if (v != null) {
            return v.name;
        }
        return null;
    }

    public int getWhiteListCod(int index) {
        Log.d(TAG, "getWhiteListCod");
        WhiteListValue v = unpackWhiteListValue(this.mPrefs.getString(getWhiteListKey(index), ProxyInfo.LOCAL_EXCL_LIST));
        if (v != null) {
            return v.cod;
        }
        return 0;
    }

    public String[] getWhiteListUuids(int index) {
        Log.d(TAG, "getWhiteListUuids");
        WhiteListValue v = unpackWhiteListValue(this.mPrefs.getString(getWhiteListKey(index), ProxyInfo.LOCAL_EXCL_LIST));
        if (v != null) {
            return v.uuids;
        }
        return null;
    }
}
