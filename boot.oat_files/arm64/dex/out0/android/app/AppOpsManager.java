package android.app;

import android.Manifest.permission;
import android.content.Context;
import android.net.ProxyInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.ArrayMap;
import com.android.internal.app.IAppOpsCallback;
import com.android.internal.app.IAppOpsCallback.Stub;
import com.android.internal.app.IAppOpsService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AppOpsManager {
    public static final int MODE_ALLOWED = 0;
    public static final int MODE_ASK = 4;
    public static final int MODE_DEFAULT = 3;
    public static final int MODE_ERRORED = 2;
    public static final int MODE_IGNORED = 1;
    private static final String OPSTR_ACCESS_NOTIFICATIONS = "android:access_notifications";
    public static final String OPSTR_ACTIVATE_VPN = "android:activate_vpn";
    public static final String OPSTR_ADD_VOICEMAIL = "android:add_voicemail";
    private static final String OPSTR_AUDIO_ALARM_VOLUME = "android:audio_alarm_volume";
    private static final String OPSTR_AUDIO_BLUETOOTH_VOLUME = "android:audio_bluetooth_volume";
    private static final String OPSTR_AUDIO_MASTER_VOLUME = "android:audio_master_volume";
    private static final String OPSTR_AUDIO_MEDIA_VOLUME = "android:audio_media_volume";
    private static final String OPSTR_AUDIO_NOTIFICATION_VOLUME = "android:audio_notification_volume";
    private static final String OPSTR_AUDIO_RING_VOLUME = "android:audio_ring_volume";
    private static final String OPSTR_AUDIO_VOICE_VOLUME = "android:audio_voice_volume";
    private static final String OPSTR_BLUETOOTH_CHANGE = "android:bluetooth_change";
    public static final String OPSTR_BODY_SENSORS = "android:body_sensors";
    private static final String OPSTR_BOOT_COMPLETED = "android:boot_completed";
    public static final String OPSTR_CALL_PHONE = "android:call_phone";
    public static final String OPSTR_CAMERA = "android:camera";
    public static final String OPSTR_COARSE_LOCATION = "android:coarse_location";
    private static final String OPSTR_DATA_CONNECT_CHANGE = "android:data_connect_change";
    private static final String OPSTR_DELETE_CALL_LOG = "android:delete_call_log";
    private static final String OPSTR_DELETE_CONTACTS = "android:delete_contacts";
    private static final String OPSTR_DELETE_MMS = "android:delete_mms";
    private static final String OPSTR_DELETE_SMS = "android:delete_sms";
    public static final String OPSTR_FINE_LOCATION = "android:fine_location";
    public static final String OPSTR_GET_ACCOUNTS = "android:get_accounts";
    public static final String OPSTR_GET_USAGE_STATS = "android:get_usage_stats";
    private static final String OPSTR_GPS = "android:gps";
    public static final String OPSTR_MOCK_LOCATION = "android:mock_location";
    public static final String OPSTR_MONITOR_HIGH_POWER_LOCATION = "android:monitor_location_high_power";
    public static final String OPSTR_MONITOR_LOCATION = "android:monitor_location";
    private static final String OPSTR_MUTE_MICROPHONE = "android:mute_microphone";
    private static final String OPSTR_NEIGHBORING_CELLS = "android:neighboring_cells";
    private static final String OPSTR_NFC_CHANGE = "android:nfc_change";
    private static final String OPSTR_PLAY_AUDIO = "android:play_audio";
    private static final String OPSTR_POST_NOTIFICATION = "android:post_notification";
    private static final String OPSTR_PROJECT_MEDIA = "android:project_media";
    public static final String OPSTR_READ_CALENDAR = "android:read_calendar";
    public static final String OPSTR_READ_CALL_LOG = "android:read_call_log";
    public static final String OPSTR_READ_CELL_BROADCASTS = "android:read_cell_broadcasts";
    private static final String OPSTR_READ_CLIPBOARD = "android:read_clipboard";
    public static final String OPSTR_READ_CONTACTS = "android:read_contacts";
    public static final String OPSTR_READ_EXTERNAL_STORAGE = "android:read_external_storage";
    private static final String OPSTR_READ_ICC_SMS = "android:read_icc_sms";
    private static final String OPSTR_READ_MMS = "android:read_mms";
    public static final String OPSTR_READ_PHONE_STATE = "android:read_phone_state";
    public static final String OPSTR_READ_SMS = "android:read_sms";
    private static final String OPSTR_RECEIVE_EMERGECY_SMS = "android:receive_emergecy_sms";
    public static final String OPSTR_RECEIVE_MMS = "android:receive_mms";
    public static final String OPSTR_RECEIVE_SMS = "android:receive_sms";
    public static final String OPSTR_RECEIVE_WAP_PUSH = "android:receive_wap_push";
    public static final String OPSTR_RECORD_AUDIO = "android:record_audio";
    private static final String OPSTR_SEND_MMS = "android:send_mms";
    public static final String OPSTR_SEND_SMS = "android:send_sms";
    public static final String OPSTR_SYSTEM_ALERT_WINDOW = "android:system_alert_window";
    private static final String OPSTR_TAKE_AUDIO_FOCUS = "android:take_audio_focus";
    private static final String OPSTR_TAKE_MEDIA_BUTTONS = "android:take_media_buttons";
    private static final String OPSTR_TOAST_WINDOW = "android:toast_window";
    public static final String OPSTR_USE_FINGERPRINT = "android:use_fingerprint";
    public static final String OPSTR_USE_SIP = "android:use_sip";
    private static final String OPSTR_VIBRATE = "android:vibrate";
    private static final String OPSTR_WAKE_LOCK = "android:wake_lock";
    private static final String OPSTR_WIFI_CHANGE = "android:wifi_change";
    private static final String OPSTR_WIFI_SCAN = "android:wifi_scan";
    public static final String OPSTR_WRITE_CALENDAR = "android:write_calendar";
    public static final String OPSTR_WRITE_CALL_LOG = "android:write_call_log";
    private static final String OPSTR_WRITE_CLIPBOARD = "android:write_clipboard";
    public static final String OPSTR_WRITE_CONTACTS = "android:write_contacts";
    public static final String OPSTR_WRITE_EXTERNAL_STORAGE = "android:write_external_storage";
    private static final String OPSTR_WRITE_ICC_SMS = "android:write_icc_sms";
    private static final String OPSTR_WRITE_MMS = "android:write_mms";
    public static final String OPSTR_WRITE_SETTINGS = "android:write_settings";
    private static final String OPSTR_WRITE_SMS = "android:write_sms";
    public static final int OP_ACCESS_NOTIFICATIONS = 25;
    public static final int OP_ACTIVATE_VPN = 47;
    public static final int OP_ADD_VOICEMAIL = 52;
    public static final int OP_ASSIST_SCREENSHOT = 50;
    public static final int OP_ASSIST_STRUCTURE = 49;
    public static final int OP_AUDIO_ALARM_VOLUME = 37;
    public static final int OP_AUDIO_BLUETOOTH_VOLUME = 39;
    public static final int OP_AUDIO_MASTER_VOLUME = 33;
    public static final int OP_AUDIO_MEDIA_VOLUME = 36;
    public static final int OP_AUDIO_NOTIFICATION_VOLUME = 38;
    public static final int OP_AUDIO_RING_VOLUME = 35;
    public static final int OP_AUDIO_VOICE_VOLUME = 34;
    public static final int OP_BLUETOOTH_CHANGE = 63;
    public static final int OP_BODY_SENSORS = 56;
    public static final int OP_BOOT_COMPLETED = 67;
    public static final int OP_CALL_PHONE = 13;
    public static final int OP_CAMERA = 26;
    public static final int OP_COARSE_LOCATION = 0;
    public static final int OP_DATA_CONNECT_CHANGE = 73;
    public static final int OP_DELETE_CALL_LOG = 72;
    public static final int OP_DELETE_CONTACTS = 71;
    public static final int OP_DELETE_MMS = 70;
    public static final int OP_DELETE_SMS = 69;
    public static final int OP_FINE_LOCATION = 1;
    public static final int OP_GET_ACCOUNTS = 74;
    public static final int OP_GET_USAGE_STATS = 43;
    public static final int OP_GPS = 2;
    public static final int OP_MOCK_LOCATION = 58;
    public static final int OP_MONITOR_HIGH_POWER_LOCATION = 42;
    public static final int OP_MONITOR_LOCATION = 41;
    public static final int OP_MUTE_MICROPHONE = 44;
    public static final int OP_NEIGHBORING_CELLS = 12;
    public static final int OP_NFC_CHANGE = 68;
    public static final int OP_NONE = -1;
    public static final int OP_PLAY_AUDIO = 28;
    public static final int OP_POST_NOTIFICATION = 11;
    public static final int OP_PROCESS_OUTGOING_CALLS = 54;
    public static final int OP_PROJECT_MEDIA = 46;
    public static final int OP_READ_CALENDAR = 8;
    public static final int OP_READ_CALL_LOG = 6;
    public static final int OP_READ_CELL_BROADCASTS = 57;
    public static final int OP_READ_CLIPBOARD = 29;
    public static final int OP_READ_CONTACTS = 4;
    public static final int OP_READ_EXTERNAL_STORAGE = 59;
    public static final int OP_READ_ICC_SMS = 21;
    public static final int OP_READ_MMS = 65;
    public static final int OP_READ_PHONE_STATE = 51;
    public static final int OP_READ_SMS = 14;
    public static final int OP_RECEIVE_EMERGECY_SMS = 17;
    public static final int OP_RECEIVE_MMS = 18;
    public static final int OP_RECEIVE_SMS = 16;
    public static final int OP_RECEIVE_WAP_PUSH = 19;
    public static final int OP_RECORD_AUDIO = 27;
    public static final int OP_SEND_MMS = 64;
    public static final int OP_SEND_SMS = 20;
    public static final int OP_SYSTEM_ALERT_WINDOW = 24;
    public static final int OP_TAKE_AUDIO_FOCUS = 32;
    public static final int OP_TAKE_MEDIA_BUTTONS = 31;
    public static final int OP_TOAST_WINDOW = 45;
    public static final int OP_TURN_SCREEN_ON = 61;
    public static final int OP_USE_FINGERPRINT = 55;
    public static final int OP_USE_SIP = 53;
    public static final int OP_VIBRATE = 3;
    public static final int OP_WAKE_LOCK = 40;
    public static final int OP_WIFI_CHANGE = 62;
    public static final int OP_WIFI_SCAN = 10;
    public static final int OP_WRITE_CALENDAR = 9;
    public static final int OP_WRITE_CALL_LOG = 7;
    public static final int OP_WRITE_CLIPBOARD = 30;
    public static final int OP_WRITE_CONTACTS = 5;
    public static final int OP_WRITE_EXTERNAL_STORAGE = 60;
    public static final int OP_WRITE_ICC_SMS = 22;
    public static final int OP_WRITE_MMS = 66;
    public static final int OP_WRITE_SETTINGS = 23;
    public static final int OP_WRITE_SMS = 15;
    public static final int OP_WRITE_WALLPAPER = 48;
    public static final int _NUM_OP = 75;
    private static boolean[] sOpAllowSystemRestrictionBypass = new boolean[]{false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
    private static int[] sOpDefaultMode = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private static int[] sOpDefaultStrictMode = new int[]{4, 4, 4, 0, 4, 4, 4, 4, 0, 0, 4, 0, 0, 4, 4, 4, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 3, 0, 0, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 0, 0, 0, 4, 4, 4, 4, 4, 0, 4, 4, 4, 4, 4, 4, 0};
    private static boolean[] sOpDisableReset = new boolean[]{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
    private static String[] sOpNames = new String[]{"COARSE_LOCATION", "FINE_LOCATION", "GPS", "VIBRATE", "READ_CONTACTS", "WRITE_CONTACTS", "READ_CALL_LOG", "WRITE_CALL_LOG", "READ_CALENDAR", "WRITE_CALENDAR", "WIFI_SCAN", "POST_NOTIFICATION", "NEIGHBORING_CELLS", "CALL_PHONE", "READ_SMS", "WRITE_SMS", "RECEIVE_SMS", "RECEIVE_EMERGECY_SMS", "RECEIVE_MMS", "RECEIVE_WAP_PUSH", "SEND_SMS", "READ_ICC_SMS", "WRITE_ICC_SMS", "WRITE_SETTINGS", "SYSTEM_ALERT_WINDOW", "ACCESS_NOTIFICATIONS", "CAMERA", "RECORD_AUDIO", "PLAY_AUDIO", "READ_CLIPBOARD", "WRITE_CLIPBOARD", "TAKE_MEDIA_BUTTONS", "TAKE_AUDIO_FOCUS", "AUDIO_MASTER_VOLUME", "AUDIO_VOICE_VOLUME", "AUDIO_RING_VOLUME", "AUDIO_MEDIA_VOLUME", "AUDIO_ALARM_VOLUME", "AUDIO_NOTIFICATION_VOLUME", "AUDIO_BLUETOOTH_VOLUME", "WAKE_LOCK", "MONITOR_LOCATION", "MONITOR_HIGH_POWER_LOCATION", "GET_USAGE_STATS", "MUTE_MICROPHONE", "TOAST_WINDOW", "PROJECT_MEDIA", "ACTIVATE_VPN", "WRITE_WALLPAPER", "ASSIST_STRUCTURE", "ASSIST_SCREENSHOT", "OP_READ_PHONE_STATE", "ADD_VOICEMAIL", "USE_SIP", "PROCESS_OUTGOING_CALLS", "USE_FINGERPRINT", "BODY_SENSORS", "READ_CELL_BROADCASTS", "MOCK_LOCATION", "READ_EXTERNAL_STORAGE", "WRITE_EXTERNAL_STORAGE", "TURN_ON_SCREEN", "WIFI_CHANGE", "BLUETOOTH_CHANGE", "SEND_MMS", "READ_MMS", "WRITE_MMS", "BOOT_COMPLETED", "NFC_CHANGE", "DELETE_SMS", "DELETE_MMS", "DELETE_CONTACTS", "DELETE_CALL_LOG", "DATA_CONNECT_CHANGE", "GET_ACCOUNTS"};
    private static String[] sOpPerms = new String[]{permission.ACCESS_COARSE_LOCATION, permission.ACCESS_FINE_LOCATION, null, permission.VIBRATE, permission.READ_CONTACTS, permission.WRITE_CONTACTS, permission.READ_CALL_LOG, permission.WRITE_CALL_LOG, permission.READ_CALENDAR, permission.WRITE_CALENDAR, permission.ACCESS_WIFI_STATE, null, null, permission.CALL_PHONE, permission.READ_SMS, null, permission.RECEIVE_SMS, permission.RECEIVE_EMERGENCY_BROADCAST, permission.RECEIVE_MMS, permission.RECEIVE_WAP_PUSH, permission.SEND_SMS, permission.READ_SMS, null, permission.WRITE_SETTINGS, permission.SYSTEM_ALERT_WINDOW, permission.ACCESS_NOTIFICATIONS, permission.CAMERA, permission.RECORD_AUDIO, null, null, null, null, null, null, null, null, null, null, null, null, permission.WAKE_LOCK, null, null, permission.PACKAGE_USAGE_STATS, null, null, null, null, null, null, null, permission.READ_PHONE_STATE, permission.ADD_VOICEMAIL, permission.USE_SIP, permission.PROCESS_OUTGOING_CALLS, permission.USE_FINGERPRINT, permission.BODY_SENSORS, permission.READ_CELL_BROADCASTS, null, permission.READ_EXTERNAL_STORAGE, permission.WRITE_EXTERNAL_STORAGE, null, permission.CHANGE_WIFI_STATE, null, null, permission.READ_SMS, null, permission.RECEIVE_BOOT_COMPLETED, permission.NFC, null, null, permission.WRITE_CONTACTS, permission.WRITE_CALL_LOG, permission.MODIFY_PHONE_STATE, permission.GET_ACCOUNTS};
    private static String[] sOpRestrictions = new String[]{UserManager.DISALLOW_SHARE_LOCATION, UserManager.DISALLOW_SHARE_LOCATION, UserManager.DISALLOW_SHARE_LOCATION, null, null, null, UserManager.DISALLOW_OUTGOING_CALLS, UserManager.DISALLOW_OUTGOING_CALLS, null, null, UserManager.DISALLOW_SHARE_LOCATION, null, null, null, UserManager.DISALLOW_SMS, UserManager.DISALLOW_SMS, UserManager.DISALLOW_SMS, null, UserManager.DISALLOW_SMS, null, UserManager.DISALLOW_SMS, UserManager.DISALLOW_SMS, UserManager.DISALLOW_SMS, null, UserManager.DISALLOW_CREATE_WINDOWS, null, null, UserManager.DISALLOW_RECORD_AUDIO, null, null, null, null, null, UserManager.DISALLOW_ADJUST_VOLUME, UserManager.DISALLOW_ADJUST_VOLUME, UserManager.DISALLOW_ADJUST_VOLUME, UserManager.DISALLOW_ADJUST_VOLUME, UserManager.DISALLOW_ADJUST_VOLUME, UserManager.DISALLOW_ADJUST_VOLUME, UserManager.DISALLOW_ADJUST_VOLUME, null, UserManager.DISALLOW_SHARE_LOCATION, UserManager.DISALLOW_SHARE_LOCATION, null, UserManager.DISALLOW_UNMUTE_MICROPHONE, UserManager.DISALLOW_CREATE_WINDOWS, null, UserManager.DISALLOW_CONFIG_VPN, UserManager.DISALLOW_WALLPAPER, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
    private static HashMap<String, Integer> sOpStrToOp = new HashMap();
    private static final boolean[] sOpStrictMode = new boolean[]{true, true, true, false, true, true, true, true, false, false, true, false, false, true, true, true, false, false, false, false, true, false, false, false, false, false, true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, true, true, true, true, false, true, true, true, true, true, true, false};
    private static HashMap<String, Integer> sOpStringToOp = new HashMap();
    private static String[] sOpToOpString = new String[]{OPSTR_COARSE_LOCATION, OPSTR_FINE_LOCATION, OPSTR_GPS, OPSTR_VIBRATE, OPSTR_READ_CONTACTS, OPSTR_WRITE_CONTACTS, OPSTR_READ_CALL_LOG, OPSTR_WRITE_CALL_LOG, OPSTR_READ_CALENDAR, OPSTR_WRITE_CALENDAR, OPSTR_WIFI_SCAN, OPSTR_POST_NOTIFICATION, OPSTR_NEIGHBORING_CELLS, OPSTR_CALL_PHONE, OPSTR_READ_SMS, OPSTR_WRITE_SMS, OPSTR_RECEIVE_SMS, OPSTR_RECEIVE_EMERGECY_SMS, OPSTR_RECEIVE_MMS, OPSTR_RECEIVE_WAP_PUSH, OPSTR_SEND_SMS, OPSTR_READ_ICC_SMS, OPSTR_WRITE_ICC_SMS, OPSTR_WRITE_SETTINGS, OPSTR_SYSTEM_ALERT_WINDOW, OPSTR_ACCESS_NOTIFICATIONS, OPSTR_CAMERA, OPSTR_RECORD_AUDIO, OPSTR_PLAY_AUDIO, OPSTR_READ_CLIPBOARD, OPSTR_WRITE_CLIPBOARD, OPSTR_TAKE_MEDIA_BUTTONS, OPSTR_TAKE_AUDIO_FOCUS, OPSTR_AUDIO_MASTER_VOLUME, OPSTR_AUDIO_VOICE_VOLUME, OPSTR_AUDIO_RING_VOLUME, OPSTR_AUDIO_MEDIA_VOLUME, OPSTR_AUDIO_ALARM_VOLUME, OPSTR_AUDIO_NOTIFICATION_VOLUME, OPSTR_AUDIO_BLUETOOTH_VOLUME, OPSTR_WAKE_LOCK, OPSTR_MONITOR_LOCATION, OPSTR_MONITOR_HIGH_POWER_LOCATION, OPSTR_GET_USAGE_STATS, OPSTR_MUTE_MICROPHONE, OPSTR_TOAST_WINDOW, OPSTR_PROJECT_MEDIA, OPSTR_ACTIVATE_VPN, ProxyInfo.LOCAL_EXCL_LIST, ProxyInfo.LOCAL_EXCL_LIST, ProxyInfo.LOCAL_EXCL_LIST, OPSTR_READ_PHONE_STATE, OPSTR_ADD_VOICEMAIL, OPSTR_USE_SIP, ProxyInfo.LOCAL_EXCL_LIST, OPSTR_USE_FINGERPRINT, OPSTR_BODY_SENSORS, OPSTR_READ_CELL_BROADCASTS, OPSTR_MOCK_LOCATION, OPSTR_READ_EXTERNAL_STORAGE, OPSTR_WRITE_EXTERNAL_STORAGE, ProxyInfo.LOCAL_EXCL_LIST, OPSTR_WIFI_CHANGE, OPSTR_BLUETOOTH_CHANGE, OPSTR_SEND_MMS, OPSTR_READ_MMS, OPSTR_WRITE_MMS, OPSTR_BOOT_COMPLETED, OPSTR_NFC_CHANGE, OPSTR_DELETE_SMS, OPSTR_DELETE_MMS, OPSTR_DELETE_CONTACTS, OPSTR_DELETE_CALL_LOG, OPSTR_DATA_CONNECT_CHANGE, OPSTR_GET_ACCOUNTS};
    private static String[] sOpToString = new String[]{OPSTR_COARSE_LOCATION, OPSTR_FINE_LOCATION, null, null, OPSTR_READ_CONTACTS, OPSTR_WRITE_CONTACTS, OPSTR_READ_CALL_LOG, OPSTR_WRITE_CALL_LOG, OPSTR_READ_CALENDAR, OPSTR_WRITE_CALENDAR, null, null, null, OPSTR_CALL_PHONE, OPSTR_READ_SMS, null, OPSTR_RECEIVE_SMS, null, OPSTR_RECEIVE_MMS, OPSTR_RECEIVE_WAP_PUSH, OPSTR_SEND_SMS, null, null, OPSTR_WRITE_SETTINGS, OPSTR_SYSTEM_ALERT_WINDOW, null, OPSTR_CAMERA, OPSTR_RECORD_AUDIO, null, null, null, null, null, null, null, null, null, null, null, null, null, OPSTR_MONITOR_LOCATION, OPSTR_MONITOR_HIGH_POWER_LOCATION, OPSTR_GET_USAGE_STATS, null, null, null, OPSTR_ACTIVATE_VPN, null, null, null, OPSTR_READ_PHONE_STATE, OPSTR_ADD_VOICEMAIL, OPSTR_USE_SIP, null, OPSTR_USE_FINGERPRINT, OPSTR_BODY_SENSORS, OPSTR_READ_CELL_BROADCASTS, OPSTR_MOCK_LOCATION, OPSTR_READ_EXTERNAL_STORAGE, OPSTR_WRITE_EXTERNAL_STORAGE, null, null, null, null, null, null, null, null, null, null, null, null, null, OPSTR_GET_ACCOUNTS};
    private static int[] sOpToSwitch = new int[]{0, 0, 0, 3, 4, 5, 6, 7, 8, 9, 0, 11, 0, 13, 14, 15, 16, 16, 16, 16, 20, 14, 15, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 0, 0, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74};
    private static HashMap<String, Integer> sPermToOp = new HashMap();
    static IBinder sToken;
    final Context mContext;
    final ArrayMap<OnOpChangedListener, IAppOpsCallback> mModeWatchers = new ArrayMap();
    final IAppOpsService mService;

    public interface OnOpChangedListener {
        void onOpChanged(String str, String str2);
    }

    public static class OnOpChangedInternalListener implements OnOpChangedListener {
        public void onOpChanged(String op, String packageName) {
        }

        public void onOpChanged(int op, String packageName) {
        }
    }

    public static class OpEntry implements Parcelable {
        public static final Creator<OpEntry> CREATOR = new Creator<OpEntry>() {
            public OpEntry createFromParcel(Parcel source) {
                return new OpEntry(source);
            }

            public OpEntry[] newArray(int size) {
                return new OpEntry[size];
            }
        };
        private final int mDuration;
        private final int mMode;
        private final int mOp;
        private final String mProxyPackageName;
        private final int mProxyUid;
        private final long mRejectTime;
        private final long mTime;

        public OpEntry(int op, int mode, long time, long rejectTime, int duration, int proxyUid, String proxyPackage) {
            this.mOp = op;
            this.mMode = mode;
            this.mTime = time;
            this.mRejectTime = rejectTime;
            this.mDuration = duration;
            this.mProxyUid = proxyUid;
            this.mProxyPackageName = proxyPackage;
        }

        public int getOp() {
            return this.mOp;
        }

        public int getMode() {
            return this.mMode;
        }

        public long getTime() {
            return this.mTime;
        }

        public long getRejectTime() {
            return this.mRejectTime;
        }

        public boolean isRunning() {
            return this.mDuration == -1;
        }

        public int getDuration() {
            return this.mDuration == -1 ? (int) (System.currentTimeMillis() - this.mTime) : this.mDuration;
        }

        public int getProxyUid() {
            return this.mProxyUid;
        }

        public String getProxyPackageName() {
            return this.mProxyPackageName;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.mOp);
            dest.writeInt(this.mMode);
            dest.writeLong(this.mTime);
            dest.writeLong(this.mRejectTime);
            dest.writeInt(this.mDuration);
            dest.writeInt(this.mProxyUid);
            dest.writeString(this.mProxyPackageName);
        }

        OpEntry(Parcel source) {
            this.mOp = source.readInt();
            this.mMode = source.readInt();
            this.mTime = source.readLong();
            this.mRejectTime = source.readLong();
            this.mDuration = source.readInt();
            this.mProxyUid = source.readInt();
            this.mProxyPackageName = source.readString();
        }
    }

    public static class PackageOps implements Parcelable {
        public static final Creator<PackageOps> CREATOR = new Creator<PackageOps>() {
            public PackageOps createFromParcel(Parcel source) {
                return new PackageOps(source);
            }

            public PackageOps[] newArray(int size) {
                return new PackageOps[size];
            }
        };
        private final List<OpEntry> mEntries;
        private final String mPackageName;
        private final int mUid;

        public PackageOps(String packageName, int uid, List<OpEntry> entries) {
            this.mPackageName = packageName;
            this.mUid = uid;
            this.mEntries = entries;
        }

        public String getPackageName() {
            return this.mPackageName;
        }

        public int getUid() {
            return this.mUid;
        }

        public List<OpEntry> getOps() {
            return this.mEntries;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.mPackageName);
            dest.writeInt(this.mUid);
            dest.writeInt(this.mEntries.size());
            for (int i = 0; i < this.mEntries.size(); i++) {
                ((OpEntry) this.mEntries.get(i)).writeToParcel(dest, flags);
            }
        }

        PackageOps(Parcel source) {
            this.mPackageName = source.readString();
            this.mUid = source.readInt();
            this.mEntries = new ArrayList();
            int N = source.readInt();
            for (int i = 0; i < N; i++) {
                this.mEntries.add(OpEntry.CREATOR.createFromParcel(source));
            }
        }
    }

    static {
        if (sOpToSwitch.length != 75) {
            throw new IllegalStateException("sOpToSwitch length " + sOpToSwitch.length + " should be " + 75);
        } else if (sOpToString.length != 75) {
            throw new IllegalStateException("sOpToString length " + sOpToString.length + " should be " + 75);
        } else if (sOpToOpString.length != 75) {
            throw new IllegalStateException("sOpToOpString length " + sOpToOpString.length + " should be " + 75);
        } else if (sOpNames.length != 75) {
            throw new IllegalStateException("sOpNames length " + sOpNames.length + " should be " + 75);
        } else if (sOpPerms.length != 75) {
            throw new IllegalStateException("sOpPerms length " + sOpPerms.length + " should be " + 75);
        } else if (sOpDefaultMode.length != 75) {
            throw new IllegalStateException("sOpDefaultMode length " + sOpDefaultMode.length + " should be " + 75);
        } else if (sOpDefaultStrictMode.length != 75) {
            throw new IllegalStateException("sOpDefaultStrictMode length " + sOpDefaultStrictMode.length + " should be " + 75);
        } else if (sOpDisableReset.length != 75) {
            throw new IllegalStateException("sOpDisableReset length " + sOpDisableReset.length + " should be " + 75);
        } else if (sOpRestrictions.length != 75) {
            throw new IllegalStateException("sOpRestrictions length " + sOpRestrictions.length + " should be " + 75);
        } else if (sOpAllowSystemRestrictionBypass.length != 75) {
            throw new IllegalStateException("sOpAllowSYstemRestrictionsBypass length " + sOpRestrictions.length + " should be " + 75);
        } else if (sOpStrictMode.length != 75) {
            throw new IllegalStateException("sOpStrictMode length " + sOpStrictMode.length + " should be " + 75);
        } else {
            int i;
            for (i = 0; i < 75; i++) {
                if (sOpToString[i] != null) {
                    sOpStrToOp.put(sOpToString[i], Integer.valueOf(i));
                }
                if (sOpToOpString[i] != null) {
                    sOpStringToOp.put(sOpToOpString[i], Integer.valueOf(i));
                }
            }
            for (i = 0; i < 75; i++) {
                if (sOpPerms[i] != null) {
                    sPermToOp.put(sOpPerms[i], Integer.valueOf(i));
                }
            }
        }
    }

    public static int opToSwitch(int op) {
        return sOpToSwitch[op];
    }

    public static String opToName(int op) {
        if (op == -1) {
            return "NONE";
        }
        return op < sOpNames.length ? sOpNames[op] : "Unknown(" + op + ")";
    }

    public static int strDebugOpToOp(String op) {
        for (int i = 0; i < sOpNames.length; i++) {
            if (sOpNames[i].equals(op)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Unknown operation string: " + op);
    }

    public static String opToPermission(int op) {
        return sOpPerms[op];
    }

    public static String opToRestriction(int op) {
        return sOpRestrictions[op];
    }

    public static int permissionToOpCode(String permission) {
        Integer boxedOpCode = (Integer) sPermToOp.get(permission);
        return boxedOpCode != null ? boxedOpCode.intValue() : -1;
    }

    public static boolean opAllowSystemBypassRestriction(int op) {
        return sOpAllowSystemRestrictionBypass[op];
    }

    public static int opToDefaultMode(int op, boolean isStrict) {
        if (isStrict) {
            return sOpDefaultStrictMode[op];
        }
        return sOpDefaultMode[op];
    }

    public static boolean opAllowsReset(int op) {
        return !sOpDisableReset[op];
    }

    AppOpsManager(Context context, IAppOpsService service) {
        this.mContext = context;
        this.mService = service;
    }

    public List<PackageOps> getPackagesForOps(int[] ops) {
        try {
            return this.mService.getPackagesForOps(ops);
        } catch (RemoteException e) {
            return null;
        }
    }

    public List<PackageOps> getOpsForPackage(int uid, String packageName, int[] ops) {
        try {
            return this.mService.getOpsForPackage(uid, packageName, ops);
        } catch (RemoteException e) {
            return null;
        }
    }

    public void setUidMode(int code, int uid, int mode) {
        try {
            this.mService.setUidMode(code, uid, mode);
        } catch (RemoteException e) {
        }
    }

    public void setMode(int code, int uid, String packageName, int mode) {
        try {
            this.mService.setMode(code, uid, packageName, mode);
        } catch (RemoteException e) {
        }
    }

    public void setRestriction(int code, int usage, int mode, String[] exceptionPackages) {
        try {
            this.mService.setAudioRestriction(code, usage, Binder.getCallingUid(), mode, exceptionPackages);
        } catch (RemoteException e) {
        }
    }

    public void resetAllModes() {
        try {
            this.mService.resetAllModes(UserHandle.myUserId(), null);
        } catch (RemoteException e) {
        }
    }

    public static String permissionToOp(String permission) {
        Integer opCode = (Integer) sPermToOp.get(permission);
        if (opCode == null) {
            return null;
        }
        return sOpToString[opCode.intValue()];
    }

    public void startWatchingMode(String op, String packageName, OnOpChangedListener callback) {
        startWatchingMode(strOpToOp(op), packageName, callback);
    }

    public void startWatchingMode(int op, String packageName, final OnOpChangedListener callback) {
        synchronized (this.mModeWatchers) {
            IAppOpsCallback cb = (IAppOpsCallback) this.mModeWatchers.get(callback);
            if (cb == null) {
                cb = new Stub() {
                    public void opChanged(int op, String packageName) {
                        if (callback instanceof OnOpChangedInternalListener) {
                            ((OnOpChangedInternalListener) callback).onOpChanged(op, packageName);
                        }
                        if (AppOpsManager.sOpToString[op] != null) {
                            callback.onOpChanged(AppOpsManager.sOpToString[op], packageName);
                        }
                    }
                };
                this.mModeWatchers.put(callback, cb);
            }
            try {
                this.mService.startWatchingMode(op, packageName, cb);
            } catch (RemoteException e) {
            }
        }
    }

    public void stopWatchingMode(OnOpChangedListener callback) {
        synchronized (this.mModeWatchers) {
            IAppOpsCallback cb = (IAppOpsCallback) this.mModeWatchers.get(callback);
            if (cb != null) {
                try {
                    this.mService.stopWatchingMode(cb);
                } catch (RemoteException e) {
                }
            }
        }
    }

    private String buildSecurityExceptionMsg(int op, int uid, String packageName) {
        return packageName + " from uid " + uid + " not allowed to perform " + sOpNames[op];
    }

    public static int strOpToOp(String op) {
        Integer val = (Integer) sOpStrToOp.get(op);
        if (val != null) {
            return val.intValue();
        }
        throw new IllegalArgumentException("Unknown operation string: " + op);
    }

    public int checkOp(String op, int uid, String packageName) {
        return checkOp(strOpToOp(op), uid, packageName);
    }

    public int checkOpNoThrow(String op, int uid, String packageName) {
        return checkOpNoThrow(strOpToOp(op), uid, packageName);
    }

    public int noteOp(String op, int uid, String packageName) {
        return noteOp(strOpToOp(op), uid, packageName);
    }

    public int noteOpNoThrow(String op, int uid, String packageName) {
        return noteOpNoThrow(strOpToOp(op), uid, packageName);
    }

    public int noteProxyOp(String op, String proxiedPackageName) {
        return noteProxyOp(strOpToOp(op), proxiedPackageName);
    }

    public int noteProxyOpNoThrow(String op, String proxiedPackageName) {
        return noteProxyOpNoThrow(strOpToOp(op), proxiedPackageName);
    }

    public int startOp(String op, int uid, String packageName) {
        return startOp(strOpToOp(op), uid, packageName);
    }

    public int startOpNoThrow(String op, int uid, String packageName) {
        return startOpNoThrow(strOpToOp(op), uid, packageName);
    }

    public void finishOp(String op, int uid, String packageName) {
        finishOp(strOpToOp(op), uid, packageName);
    }

    public int checkOp(int op, int uid, String packageName) {
        try {
            int checkOperation = this.mService.checkOperation(op, uid, packageName);
            if (checkOperation != 2) {
                return checkOperation;
            }
            throw new SecurityException(buildSecurityExceptionMsg(op, uid, packageName));
        } catch (RemoteException e) {
            return 1;
        }
    }

    public int checkOpNoThrow(int op, int uid, String packageName) {
        try {
            return this.mService.checkOperation(op, uid, packageName);
        } catch (RemoteException e) {
            return 2;
        }
    }

    public void checkPackage(int uid, String packageName) {
        try {
            if (this.mService.checkPackage(uid, packageName) != 0) {
                throw new SecurityException("Package " + packageName + " does not belong to " + uid);
            }
        } catch (RemoteException e) {
            throw new SecurityException("Unable to verify package ownership", e);
        }
    }

    public int checkAudioOp(int op, int stream, int uid, String packageName) {
        try {
            int checkAudioOperation = this.mService.checkAudioOperation(op, stream, uid, packageName);
            if (checkAudioOperation != 2) {
                return checkAudioOperation;
            }
            throw new SecurityException(buildSecurityExceptionMsg(op, uid, packageName));
        } catch (RemoteException e) {
            return 1;
        }
    }

    public int checkAudioOpNoThrow(int op, int stream, int uid, String packageName) {
        try {
            return this.mService.checkAudioOperation(op, stream, uid, packageName);
        } catch (RemoteException e) {
            return 2;
        }
    }

    public int noteOp(int op, int uid, String packageName) {
        try {
            int noteOperation = this.mService.noteOperation(op, uid, packageName);
            if (noteOperation != 2) {
                return noteOperation;
            }
            throw new SecurityException(buildSecurityExceptionMsg(op, uid, packageName));
        } catch (RemoteException e) {
            return 1;
        }
    }

    public int noteProxyOp(int op, String proxiedPackageName) {
        int mode = noteProxyOpNoThrow(op, proxiedPackageName);
        if (mode != 2) {
            return mode;
        }
        throw new SecurityException("Proxy package " + this.mContext.getOpPackageName() + " from uid " + Process.myUid() + " or calling package " + proxiedPackageName + " from uid " + Binder.getCallingUid() + " not allowed to perform " + sOpNames[op]);
    }

    public int noteProxyOpNoThrow(int op, String proxiedPackageName) {
        try {
            return this.mService.noteProxyOperation(op, this.mContext.getOpPackageName(), Binder.getCallingUid(), proxiedPackageName);
        } catch (RemoteException e) {
            return 2;
        }
    }

    public int noteOpNoThrow(int op, int uid, String packageName) {
        try {
            return this.mService.noteOperation(op, uid, packageName);
        } catch (RemoteException e) {
            return 2;
        }
    }

    public int noteOp(int op) {
        return noteOp(op, Process.myUid(), this.mContext.getOpPackageName());
    }

    public static IBinder getToken(IAppOpsService service) {
        IBinder iBinder;
        synchronized (AppOpsManager.class) {
            if (sToken != null) {
                iBinder = sToken;
            } else {
                try {
                    sToken = service.getToken(new Binder());
                } catch (RemoteException e) {
                }
                iBinder = sToken;
            }
        }
        return iBinder;
    }

    public int startOp(int op, int uid, String packageName) {
        try {
            int startOperation = this.mService.startOperation(getToken(this.mService), op, uid, packageName);
            if (startOperation != 2) {
                return startOperation;
            }
            throw new SecurityException(buildSecurityExceptionMsg(op, uid, packageName));
        } catch (RemoteException e) {
            return 1;
        }
    }

    public int startOpNoThrow(int op, int uid, String packageName) {
        try {
            return this.mService.startOperation(getToken(this.mService), op, uid, packageName);
        } catch (RemoteException e) {
            return 2;
        }
    }

    public int startOp(int op) {
        return startOp(op, Process.myUid(), this.mContext.getOpPackageName());
    }

    public void finishOp(int op, int uid, String packageName) {
        try {
            this.mService.finishOperation(getToken(this.mService), op, uid, packageName);
        } catch (RemoteException e) {
        }
    }

    public void finishOp(int op) {
        finishOp(op, Process.myUid(), this.mContext.getOpPackageName());
    }

    public static boolean isStrictEnable() {
        return SystemProperties.getBoolean("persist.sys.strict_op_enable", false);
    }

    public static boolean isStrictOp(int code) {
        return sOpStrictMode[code];
    }

    public static int stringToMode(String permission) {
        if ("allowed".equalsIgnoreCase(permission)) {
            return 0;
        }
        if ("ignored".equalsIgnoreCase(permission)) {
            return 1;
        }
        if ("ask".equalsIgnoreCase(permission)) {
            return 4;
        }
        return 2;
    }

    public static int stringOpToOp(String op) {
        Integer val = (Integer) sOpStringToOp.get(op);
        if (val == null) {
            val = Integer.valueOf(-1);
        }
        return val.intValue();
    }

    public boolean isControlAllowed(int op, String packageName) {
        boolean isShow = true;
        try {
            isShow = this.mService.isControlAllowed(op, packageName);
        } catch (RemoteException e) {
        }
        return isShow;
    }
}
