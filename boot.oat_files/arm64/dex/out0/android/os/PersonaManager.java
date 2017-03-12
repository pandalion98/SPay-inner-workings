package android.os;

import android.R;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.bluetooth.BluetoothSecureManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.CustomCursor;
import android.content.IRCPGlobalContactsDir;
import android.content.IRCPInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.IKnoxModeChangeObserver;
import android.content.pm.IKnoxUnlockAction;
import android.content.pm.IPersonaCallback;
import android.content.pm.IPersonaPolicyManager.Stub;
import android.content.pm.ISystemPersonaObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PersonaAttribute;
import android.content.pm.PersonaInfo;
import android.content.pm.PersonaNewEvent;
import android.content.pm.PersonaState;
import android.content.pm.UserInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.hardware.Camera.Parameters;
import android.hardware.display.DisplayManager;
import android.media.AudioParameter;
import android.net.ProxyInfo;
import android.net.Uri;
import android.net.Uri.Builder;
import android.net.wifi.WifiEnterpriseConfig;
import android.sec.enterprise.EnterpriseDeviceManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

public class PersonaManager {
    public static final String ACCESS_TYPE_BLUETOOTH = "bluetooth";
    public static final String ACCESS_TYPE_SDCARD = "sdcard";
    private static final String ADAPT_SOUND_PACKAGE_NAME = "com.sec.hearingadjust";
    private static final String BBC_METADATA = "com.samsung.android.knoxenabled";
    public static final String BRIDGE_COMPONENT1 = "com.sec.knox.bridge/com.sec.knox.bridge.activity.SwitchB2BActivity";
    public static final String BRIDGE_COMPONENT2 = "com.sec.knox.bridge/com.sec.knox.bridge.activity.SwitchB2BActivity2";
    public static final String BRIDGE_PKG = "com.sec.knox.bridge";
    public static final String CALLER_DISPLAY_NAME = "caller_display_name";
    public static final String CALLER_PHONE_NUMBER = "caller_phone_number";
    public static final String CALLER_PHOTO = "caller_photo";
    public static final String CONTACT_OWNER_ID = "contact_owner_id";
    public static final int CONTAINER_COM_TYPE = 3;
    public static final int CONTAINER_DEFAULT_TYPE = 1;
    public static final int CONTAINER_LWC_TYPE = 2;
    private static final boolean DEBUG = false;
    public static String DEFAULT_KNOX_NAME = "KNOX";
    public static final int DEFAULT_SDP_ACTIVATION_TIME = 5000;
    public static final String ENABLE_EULA = "enable_eula";
    public static final int ERROR_CREATE_PERSONA_ADMIN_ACTIVATION_FAILED = -1009;
    public static final int ERROR_CREATE_PERSONA_ADMIN_INSTALLATION_FAILED = -1008;
    public static final int ERROR_CREATE_PERSONA_EMERGENCY_MODE_FAILED = -1031;
    public static final int ERROR_CREATE_PERSONA_FILESYSTEM_ERROR = -1011;
    public static final int ERROR_CREATE_PERSONA_GENERATE_CMK_FAILED = -1034;
    public static final int ERROR_CREATE_PERSONA_HANDLER_INSTALLATION_FAILED = -1006;
    public static final int ERROR_CREATE_PERSONA_INTERNAL_ERROR = -1014;
    public static final int ERROR_CREATE_PERSONA_MAX_PERSONA_LIMIT_REACHED = -1012;
    public static final int ERROR_CREATE_PERSONA_NO_HANDLER_APK = -1002;
    public static final int ERROR_CREATE_PERSONA_NO_NAME = -1001;
    public static final int ERROR_CREATE_PERSONA_NO_PERSONA_ADMIN_APK = -1004;
    public static final int ERROR_CREATE_PERSONA_NO_PERSONA_TYPE = -1005;
    public static final int ERROR_CREATE_PERSONA_NO_SETUPWIZARD_APK = -1003;
    public static final int ERROR_CREATE_PERSONA_RUNTIME_PERMISSION_GRANT = -1035;
    public static final int ERROR_CREATE_PERSONA_SETUPWIZARD_INSTALLATION_FAILED = -1007;
    public static final int ERROR_CREATE_PERSONA_SUB_USER_FAILED = -1027;
    public static final int ERROR_CREATE_PERSONA_SYSTEM_APP_INSTALLATION_FAILED = -1010;
    public static final int ERROR_CREATE_PERSONA_TIMA_PWD_KEY_FAILED = -1033;
    public static final int ERROR_CREATE_PERSONA_USER_INFO_INVALID = -1032;
    public static final int ERROR_PERSONA_APP_INSTALLATION_FAILED = -2001;
    public static final int ERROR_REMOVE_NOT_PERSONA_OWNER = -1203;
    public static final int ERROR_REMOVE_PERSONA_FAILED = -1201;
    public static final int ERROR_REMOVE_PERSONA_NOT_EXIST = -1202;
    public static final int ERROR_SWITCH_EQUALS_CURRENT_USER = -1105;
    public static final int ERROR_SWITCH_INVALID_PERSONA_ID = -1100;
    public static final int ERROR_SWITCH_OUTSIDE_PERSONA_GROUP = -1106;
    public static final int ERROR_SWITCH_PERSONA_FILESYSTEM = -1103;
    public static final int ERROR_SWITCH_PERSONA_HANDLER_NOT_RESPONDING = -1104;
    public static final int ERROR_SWITCH_PERSONA_LOCKED = -1102;
    public static final int ERROR_SWITCH_PERSONA_NOT_INITIALIZED = -1101;
    public static final int FLAG_ADMIN_TYPE_APK = 16;
    public static final int FLAG_ADMIN_TYPE_NONE = 64;
    public static final int FLAG_ADMIN_TYPE_PACKAGENAME = 32;
    private static final int FLAG_BASE = 1;
    public static final int FLAG_BBC_CONTAINER = 4096;
    public static final int FLAG_CREATOR_SELF_DESTROY = 8;
    public static final int FLAG_ECRYPT_FILESYSTEM = 2;
    public static final int FLAG_KIOSK_ENABLED = 1024;
    public static final int FLAG_LIGHT_WEIGHT_CONTAINER = 512;
    public static final int FLAG_MIGRATION = 256;
    public static final int FLAG_PURE_ENABLED = 2048;
    public static final int FLAG_TIMA_STORAGE = 4;
    public static final int FLAG_USER_MANAGED_CONTAINER = 128;
    public static final String INTENT_ACCESS_EXT_SDCARD = "com.sec.knox.container.access.extsdcard";
    public static final String INTENT_ACTION_CONTAINER_REMOVAL_STARTED = "com.sec.knox.container.action.containerremovalstarted";
    public static final String INTENT_ACTION_LAUNCH_INFO = "com.sec.knox.container.action.launchinfo";
    public static final String INTENT_ACTION_NFC_POLICY = "com.sec.knox.container.action.nfc.policy";
    public static final String INTENT_ACTION_OBSERVER = "com.sec.knox.container.action.observer";
    public static final String INTENT_ACTION_SDP_TIMEOUT = "com.sec.knox.container.INTENT_KNOX_SDP_ACTIVATED";
    public static final String INTENT_CATEGORY_OBSERVER_CONTAINERID = "com.sec.knox.container.category.observer.containerid";
    public static final String INTENT_CATEGORY_OBSERVER_ONATTRIBUTECHANGE = "com.sec.knox.container.category.observer.onattributechange";
    public static final String INTENT_CATEGORY_OBSERVER_ONKEYGUARDSTATECHANGED = "com.sec.knox.container.category.observer.onkeyguardstatechanged";
    public static final String INTENT_CATEGORY_OBSERVER_ONPERSONASWITCH = "com.sec.knox.container.category.observer.onpersonaswitch";
    public static final String INTENT_CATEGORY_OBSERVER_ONSESSIONEXPIRED = "com.sec.knox.container.category.observer.onsessionexpired";
    public static final String INTENT_CATEGORY_OBSERVER_ONSTATECHANGE = "com.sec.knox.container.category.observer.onstatechange";
    public static final String INTENT_CONTAINER_NEED_RESTART = "com.sec.knox.container.need.restart";
    public static final String INTENT_EXTRA_CONTAINER_ID = "containerId";
    public static final String INTENT_EXTRA_OBSERVER_ATTRIBUTE = "com.sec.knox.container.extra.observer.attribute";
    public static final String INTENT_EXTRA_OBSERVER_ATTRIBUTE_STATE = "com.sec.knox.container.extra.observer.attribute.state";
    public static final String INTENT_EXTRA_OBSERVER_KEYGUARDSTATE = "com.sec.knox.container.extra.observer.keyguardstate";
    public static final String INTENT_EXTRA_OBSERVER_NEWSTATE = "com.sec.knox.container.extra.observer.newstate";
    public static final String INTENT_EXTRA_OBSERVER_PREVIOUSSTATE = "com.sec.knox.container.extra.observer.previousstate";
    public static final String INTENT_EXTRA_SOURCE = "source";
    public static final int INTENT_EXTRA_SOURCE_SBA = 1;
    public static final int INTENT_EXTRA_SOURCE_SBA_BLACKLIST = 2;
    public static final int INTENT_EXTRA_SOURCE_WHITELIST = 0;
    public static final String INTENT_EXTRA_UPDATED_VALUE = "com.sec.knox.container.extra.updated.value";
    public static final String INTENT_PERMISSION_LAUNCH_INFO = "com.samsung.container.LAUNCH_INFO";
    public static final String INTENT_PERMISSION_OBSERVER = "com.samsung.container.OBSERVER";
    public static final String INTENT_PERMISSION_RECEIVE_KNOX_APPS_UPDATE = "com.sec.knox.container.permission.RECEIVE_KNOX_APPS_UPDATE";
    private static final String KNOX_SETTINGS_PACKAGE_NAME = "com.sec.knox.containeragent2";
    public static final String KNOX_SWITCH1_PKG = "com.sec.knox.switchknoxI";
    public static final String KNOX_SWITCH2_PKG = "com.sec.knox.switchknoxII";
    public static final String KNOX_SWITCHER_PKG = "com.sec.knox.switcher";
    public static final String KNOX_SWITCH_COMPONENT1 = "com.sec.knox.switcher/com.sec.knox.switcher.SwitchB2bActivityI";
    public static final String KNOX_SWITCH_COMPONENT2 = "com.sec.knox.switcher/com.sec.knox.switcher.SwitchB2bActivityII";
    public static final int MAX_ACTIVE_BUTTONS_ZERO_FOR_KIOSK = 10;
    public static final int MAX_BBC_ID = 199;
    public static final int MAX_PERSONA_ALLOWED;
    public static final int MAX_PERSONA_ID = 200;
    private static final String MESSAGE_PACKAGE_NAME = "com.sec.knox.shortcutsms";
    public static final int MINIMUM_SCREEN_OFF_TIMEOUT = 5000;
    public static final int MIN_BBC_ID = 195;
    public static final int MIN_PERSONA_ID = 100;
    public static final int MOVE_TO_APP_TYPE_GALLERY = 1;
    public static final int MOVE_TO_APP_TYPE_MUSIC = 3;
    public static final int MOVE_TO_APP_TYPE_MYFILES = 4;
    public static final int MOVE_TO_APP_TYPE_VIDEO = 2;
    public static final String NOTIFICATION_LIST_FOR_KIOSK = "Wifi;Location;SilentMode;AutoRotate;Bluetooth;NetworkBooster;MobileData;AirplaneMode;Nfc;SmartStay;PowerSaving;TorchLight;WiFiHotspot;";
    public static final String NOTIFICATION_LIST_FOR_KIOSK_M = "Wifi,Location,SilentMode,RotationLock,Bluetooth,MobileData,PowerSaving,AirplaneMode,Flashlight,WifiHotspot,SmartStay,Nfc";
    public static final String PERMISSION_KEYGUARD_ACCESS = "com.sec.knox.container.keyguard.ACCESS";
    public static final String PERMISSION_PERIPHERAL_POLICY_UPDATE = "com.sec.knox.container.peripheral.POLICY_UPDATE";
    private static final String PERSONAL_HOME_PACKAGE_NAME = "com.sec.knox.switcher";
    public static final String PERSONA_ID = "persona_id";
    public static final String PERSONA_POLICY_SERVICE = "persona_policy";
    public static final int PERSONA_TIMA_ECRPTFS_INDEX1 = 100;
    public static final int PERSONA_TIMA_ECRPTFS_INDEX2 = 102;
    public static final int PERSONA_TIMA_PASSWORDHINT_INDEX = 104;
    public static final int PERSONA_TIMA_PASSWORD_INDEX1 = 101;
    public static final int PERSONA_TIMA_PASSWORD_INDEX2 = 103;
    public static final String PERSONA_VALIDATOR_HANDLER = "persona_validator";
    private static final String PHONE_PACKAGE_NAME = "com.sec.knox.shortcutsms";
    public static final int REMOVE_OP_SUCCESS = 0;
    public static String SECOND_KNOX_NAME = "KNOX II";
    public static final String SETUP_WIZARD_PKG_NAME = "com.sec.knox.setup";
    static final String[] SHORTCUT_FILTER = new String[]{"com.sec.knox.shortcutsms", "com.sec.knox.shortcutsms", "com.sec.knox.switcher", ADAPT_SOUND_PACKAGE_NAME, KNOX_SETTINGS_PACKAGE_NAME};
    private static String TAG = "PersonaManager";
    public static final int TIMA_COMPROMISED_TYPE1 = 65548;
    public static final int TIMA_COMPROMISED_TYPE2 = 65549;
    public static final int TIMA_COMPROMISED_TYPE3 = 65550;
    public static final int TIMA_COMPROMISED_TYPE4 = 65551;
    public static final int TIMA_VALIDATION_SUCCESS = 0;
    private static BluetoothSecureManager mBTSecureManager = null;
    private static Object mBTSecureManagerSync = new Object();
    private static Bundle mKnoxInfo = null;
    public static boolean m_bIsKnoxInfoInitialized = false;
    private static SparseArray<PathStrategy> pathstrategy = new SparseArray(1);
    private static PersonaManager personaManager = null;
    private static PersonaPolicyManager personaPolicyMgr = null;
    private static RCPManager rcpManager = null;
    private String[] NFCblackList = new String[]{"com.google.android.gms.smartdevice.setup.d2d:nfc2bt_bootstrap", "com.google.android.gms.auth.setup.d2d:nfc2bt_bootstrap"};
    private final Context mContext;
    private final IPersonaManager mService;

    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$android$os$PersonaManager$KnoxContainerVersion = new int[KnoxContainerVersion.values().length];

        static {
            try {
                $SwitchMap$android$os$PersonaManager$KnoxContainerVersion[KnoxContainerVersion.KNOX_CONTAINER_VERSION_1_0_0.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$os$PersonaManager$KnoxContainerVersion[KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_0_0.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$os$PersonaManager$KnoxContainerVersion[KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_1_0.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$android$os$PersonaManager$KnoxContainerVersion[KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_2_0.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$android$os$PersonaManager$KnoxContainerVersion[KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_3_0.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$android$os$PersonaManager$KnoxContainerVersion[KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_3_1.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$android$os$PersonaManager$KnoxContainerVersion[KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_4_0.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$android$os$PersonaManager$KnoxContainerVersion[KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_4_1.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$android$os$PersonaManager$KnoxContainerVersion[KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_5_0.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$android$os$PersonaManager$KnoxContainerVersion[KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_5_1.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$android$os$PersonaManager$KnoxContainerVersion[KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_5_2.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$android$os$PersonaManager$KnoxContainerVersion[KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_6_0.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$android$os$PersonaManager$KnoxContainerVersion[KnoxContainerVersion.KNOX_CONTAINER_VERSION_NONE.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
        }
    }

    public enum AppType {
        IME("TYPE_IME"),
        INSTALLER_WHITELIST("installerWhitelist"),
        DISABLED_LAUNCHERS("disabledLaunchers"),
        COM_DISABLED_OWNER_LAUNCHERS("comDisabledOwnerLaunchers");
        
        private final String mName;

        private AppType(String name) {
            this.mName = name;
        }

        public String getName() {
            return this.mName;
        }

        public AppType fromName(String name) {
            for (AppType type : values()) {
                if (type.mName.equals(name)) {
                    return type;
                }
            }
            return null;
        }
    }

    public enum KnoxContainerVersion {
        KNOX_CONTAINER_VERSION_NONE,
        KNOX_CONTAINER_VERSION_1_0_0,
        KNOX_CONTAINER_VERSION_2_0_0,
        KNOX_CONTAINER_VERSION_2_1_0,
        KNOX_CONTAINER_VERSION_2_2_0,
        KNOX_CONTAINER_VERSION_2_3_0,
        KNOX_CONTAINER_VERSION_2_3_1,
        KNOX_CONTAINER_VERSION_2_4_0,
        KNOX_CONTAINER_VERSION_2_4_1,
        KNOX_CONTAINER_VERSION_2_5_0,
        KNOX_CONTAINER_VERSION_2_5_1,
        KNOX_CONTAINER_VERSION_2_5_2,
        KNOX_CONTAINER_VERSION_2_6_0;

        public String toString() {
            switch (AnonymousClass1.$SwitchMap$android$os$PersonaManager$KnoxContainerVersion[ordinal()]) {
                case 1:
                    return "1.0.0";
                case 2:
                    return "2.0.0";
                case 3:
                    return "2.1.0";
                case 4:
                    return "2.2.0";
                case 5:
                    return "2.3.0";
                case 6:
                    return "2.3.1";
                case 7:
                    return "2.4.0";
                case 8:
                    return "2.4.1";
                case 9:
                    return "2.5.0";
                case 10:
                    return "2.5.1";
                case 11:
                    return "2.5.2";
                case 12:
                    return "2.6.0";
                default:
                    return "N/A";
            }
        }

        public int getVersionNumber() {
            switch (AnonymousClass1.$SwitchMap$android$os$PersonaManager$KnoxContainerVersion[ordinal()]) {
                case 1:
                    return 100;
                case 2:
                    return 200;
                case 3:
                    return 210;
                case 4:
                    return 220;
                case 5:
                    return 230;
                case 6:
                    return 231;
                case 7:
                    return 240;
                case 8:
                    return 241;
                case 9:
                    return R.styleable.Theme_timePickerDialogTheme;
                case 10:
                    return R.styleable.Theme_toolbarStyle;
                case 11:
                    return 252;
                case 12:
                    return 260;
                default:
                    return -1;
            }
        }
    }

    public static class PathStrategy {
        private final String mAuthority;
        private final HashMap<String, File> mRoots = new HashMap();

        public PathStrategy(String authority, int userId) {
            this.mAuthority = authority;
            addRoot(PersonaManager.ACCESS_TYPE_SDCARD, getexternalStorage(userId));
        }

        public void addRoot(String name, File root) {
            if (TextUtils.isEmpty(name)) {
                throw new IllegalArgumentException("Name must not be empty");
            }
            try {
                this.mRoots.put(name, root.getCanonicalFile());
            } catch (IOException e) {
                throw new IllegalArgumentException("Failed to resolve canonical path for " + root, e);
            }
        }

        public Uri getUriForFile(Uri fileuri) {
            try {
                String rootPath;
                String path = new File(fileuri.getPath()).getCanonicalPath();
                Entry<String, File> mostSpecific = null;
                for (Entry<String, File> root : this.mRoots.entrySet()) {
                    rootPath = ((File) root.getValue()).getPath();
                    if (path.startsWith(rootPath) && (mostSpecific == null || rootPath.length() > ((File) mostSpecific.getValue()).getPath().length())) {
                        mostSpecific = root;
                    }
                }
                if (mostSpecific == null) {
                    return fileuri;
                }
                rootPath = ((File) mostSpecific.getValue()).getPath();
                if (rootPath.endsWith("/")) {
                    path = path.substring(rootPath.length());
                } else {
                    path = path.substring(rootPath.length() + 1);
                }
                return new Builder().scheme("content").authority(this.mAuthority).encodedPath(Uri.encode((String) mostSpecific.getKey()) + '/' + Uri.encode(path, "/")).build();
            } catch (IOException e) {
                return fileuri;
            }
        }

        private static File buildPath(File base, String... segments) {
            File cur = base;
            String[] arr$ = segments;
            int len$ = arr$.length;
            int i$ = 0;
            File cur2 = cur;
            while (i$ < len$) {
                String segment = arr$[i$];
                if (cur2 == null) {
                    cur = new File(segment);
                } else {
                    cur = new File(cur2, segment);
                }
                i$++;
                cur2 = cur;
            }
            return cur2;
        }

        private static File getexternalStorage(int userId) {
            String ENV_EMULATED_STORAGE_TARGET = "EMULATED_STORAGE_TARGET";
            String rawEmulatedTarget = System.getenv("EMULATED_STORAGE_TARGET");
            if (TextUtils.isEmpty(rawEmulatedTarget)) {
                return Environment.getExternalStorageDirectory();
            }
            return buildPath(new File(rawEmulatedTarget), String.valueOf(userId));
        }
    }

    public static class StateManager {
        private final Context mContext;
        private final IPersonaManager mService;
        private int userId;

        private StateManager(Context context, IPersonaManager service, int userId) {
            this.mService = service;
            this.mContext = context;
            this.userId = userId;
        }

        public PersonaState getState() {
            if (this.mService != null) {
                try {
                    return this.mService.getState(this.userId);
                } catch (RemoteException re) {
                    Log.w(PersonaManager.TAG, "getState() Cannot make call", re);
                }
            }
            return null;
        }

        public PersonaState getPreviousState() {
            Log.d(PersonaManager.TAG, "StateManager.getState()");
            if (this.mService != null) {
                try {
                    return this.mService.getPreviousState(this.userId);
                } catch (RemoteException re) {
                    Log.w(PersonaManager.TAG, "getPreviousState() Cannot make call", re);
                }
            }
            return null;
        }

        public boolean inState(PersonaState state) {
            if (this.mService != null) {
                try {
                    return this.mService.inState(state, this.userId);
                } catch (RemoteException re) {
                    Log.w(PersonaManager.TAG, "getState() Cannot make call", re);
                }
            }
            return false;
        }

        public PersonaState fireEvent(PersonaNewEvent event) {
            Log.d(PersonaManager.TAG, "StateManager.fireEvent()");
            if (this.mService != null) {
                try {
                    return this.mService.fireEvent(event, this.userId);
                } catch (RemoteException re) {
                    Log.w(PersonaManager.TAG, "getState() Cannot make call", re);
                }
            }
            return null;
        }

        public boolean setAttribute(PersonaAttribute attribute, boolean enabled) {
            Log.d(PersonaManager.TAG, "StateManager.setAttribute()");
            if (this.mService != null) {
                try {
                    return this.mService.setAttribute(attribute, enabled, this.userId);
                } catch (RemoteException re) {
                    Log.w(PersonaManager.TAG, "getState() Cannot make call", re);
                }
            }
            return false;
        }

        public boolean isAttribute(PersonaAttribute attribute) {
            Log.d(PersonaManager.TAG, "StateManager.isAttribute()");
            if (this.mService != null) {
                try {
                    return this.mService.isAttribute(attribute, this.userId);
                } catch (RemoteException re) {
                    Log.w(PersonaManager.TAG, "getState() Cannot make call", re);
                }
            }
            return false;
        }
    }

    static {
        int i;
        if ("DCM".equals(readOMCSalesCode())) {
            i = 1;
        } else {
            i = 2;
        }
        MAX_PERSONA_ALLOWED = i;
    }

    public PersonaManager(Context context, IPersonaManager service) {
        this.mService = service;
        this.mContext = context;
    }

    public boolean registerUser(IPersonaCallback client) {
        if (this.mService != null) {
            try {
                Log.w(TAG, "in PersonaManager, registerUser(), calling service API");
                return this.mService.registerUser(client);
            } catch (RemoteException re) {
                Log.w(TAG, "Could not create a user", re);
            }
        }
        return false;
    }

    public int createPersona(String name, String password, long flags, String personaType, String admin, Uri setupWizardApkUri, String mdmSecretKey, int lockType) {
        if (this.mService != null) {
            try {
                return this.mService.createPersona(name, password, flags, personaType, admin, setupWizardApkUri, mdmSecretKey, lockType);
            } catch (RemoteException re) {
                Log.w(TAG, "Could not create a user", re);
            }
        }
        return -1;
    }

    public boolean registerSystemPersonaObserver(ISystemPersonaObserver mSystemPersonaObserver) {
        if (this.mService != null) {
            try {
                return this.mService.registerSystemPersonaObserver(mSystemPersonaObserver);
            } catch (RemoteException re) {
                Log.w(TAG, "Could not registerSystemPersonaObserver a callback", re);
            }
        }
        return false;
    }

    public boolean registerKnoxModeChangeObserver(IKnoxModeChangeObserver mKnoxModeChangeObserver) {
        if (this.mService != null) {
            try {
                return this.mService.registerKnoxModeChangeObserver(mKnoxModeChangeObserver);
            } catch (RemoteException re) {
                Log.w(TAG, "Could not registerKnoxModeChangeObserver a callback", re);
            }
        }
        return false;
    }

    public boolean switchPersonaAndLaunch(int personaId, Intent intent) {
        if (this.mService != null) {
            try {
                return this.mService.switchPersonaAndLaunch(personaId, intent);
            } catch (RemoteException re) {
                Log.w(TAG, "Could not switch to persona and launch", re);
            }
        }
        return false;
    }

    public boolean launchPersonaHome(int personaId) {
        if (this.mService != null) {
            try {
                return this.mService.launchPersonaHome(personaId);
            } catch (RemoteException re) {
                Log.w(TAG, "Could not switch to profile user", re);
            }
        }
        return false;
    }

    public int removePersona(int personaHandle) {
        if (this.mService != null) {
            try {
                return this.mService.removePersona(personaHandle);
            } catch (RemoteException re) {
                Log.w(TAG, "Could not remove Persona", re);
            }
        }
        return ERROR_REMOVE_PERSONA_FAILED;
    }

    public boolean isFOTAUpgrade() {
        if (this.mService != null) {
            try {
                return this.mService.isFOTAUpgrade();
            } catch (RemoteException re) {
                Log.w(TAG, "Could not get FOTAUpgrade", re);
            }
        }
        return false;
    }

    public boolean needToSkipResetOnReboot() {
        if (this.mService != null) {
            try {
                return this.mService.needToSkipResetOnReboot();
            } catch (RemoteException re) {
                Log.w(TAG, "Could not get needToSkipResetOnReboot", re);
            }
        }
        return false;
    }

    public List<PersonaInfo> getPersonas() {
        if (this.mService != null) {
            try {
                return this.mService.getPersonas(false);
            } catch (RemoteException re) {
                Log.w(TAG, "Could not get persona list", re);
            }
        }
        return null;
    }

    public List<PersonaInfo> getPersonasForUser(int userId) {
        if (this.mService != null) {
            try {
                return this.mService.getPersonasForUser(userId, true);
            } catch (RemoteException re) {
                Log.w(TAG, "Could not get persona list for user", re);
            }
        }
        return null;
    }

    public int getParentUserForCurrentPersona() {
        if (this.mService != null) {
            try {
                return this.mService.getParentUserForCurrentPersona();
            } catch (RemoteException re) {
                Log.w(TAG, "Could not get parent of persona", re);
            }
        }
        return -1;
    }

    public List<PersonaInfo> getPersonas(boolean excludeDying) {
        if (this.mService != null) {
            try {
                return this.mService.getPersonas(excludeDying);
            } catch (RemoteException re) {
                Log.w(TAG, "Could not get persona list", re);
            }
        }
        return null;
    }

    public PersonaInfo getPersonaInfo(int userHandle) {
        if (this.mService != null) {
            try {
                return this.mService.getPersonaInfo(userHandle);
            } catch (RemoteException re) {
                Log.w(TAG, "Could not get persona info", re);
            }
        }
        return null;
    }

    public int getParentId(int personaId) {
        if (this.mService != null) {
            try {
                return this.mService.getParentId(personaId);
            } catch (RemoteException re) {
                Log.w(TAG, "Could not get parent id", re);
            }
        }
        return -1;
    }

    public boolean getMoveToKnoxStatus() {
        if (this.mService != null) {
            try {
                return this.mService.getMoveToKnoxStatus();
            } catch (RemoteException re) {
                Log.w(TAG, "Could not get move to knox status", re);
            }
        }
        return false;
    }

    public void setMoveToKnoxStatus(boolean isProgressing) {
        if (this.mService != null) {
            try {
                this.mService.setMoveToKnoxStatus(isProgressing);
            } catch (RemoteException re) {
                Log.w(TAG, "Could not set move to knox status", re);
            }
        }
    }

    public void setPersonaName(int personaId, String name) {
        if (this.mService != null) {
            try {
                this.mService.setPersonaName(personaId, name);
            } catch (RemoteException re) {
                Log.w(TAG, "Could not set persona name", re);
            }
        }
    }

    public String getPersonaType(int personaId) {
        if (this.mService != null) {
            try {
                return this.mService.getPersonaType(personaId);
            } catch (RemoteException re) {
                Log.w(TAG, "Could not retrieve persona Type", re);
            }
        }
        return ProxyInfo.LOCAL_EXCL_LIST;
    }

    public void setPersonaType(int personaId, String personaType) {
        if (this.mService != null) {
            try {
                this.mService.setPersonaType(personaId, personaType);
            } catch (RemoteException re) {
                Log.w(TAG, "Could not set persona Type", re);
            }
        }
    }

    public String getPersonaSamsungAccount(int personaId) {
        try {
            return this.mService.getPersonaSamsungAccount(personaId);
        } catch (RemoteException re) {
            Log.w(TAG, "Could not retrieve persona SamsungAccount", re);
            return ProxyInfo.LOCAL_EXCL_LIST;
        }
    }

    public void setPersonaSamsungAccount(int personaId, String samsungAccount) {
        try {
            this.mService.setPersonaSamsungAccount(personaId, samsungAccount);
        } catch (RemoteException re) {
            Log.w(TAG, "Could not set persona SamsungAccount", re);
        }
    }

    public void setPersonaIcon(int personaHandle, Bitmap icon) {
        if (this.mService != null) {
            try {
                this.mService.setPersonaIcon(personaHandle, icon);
            } catch (RemoteException re) {
                Log.w(TAG, "Could not set the persona icon ", re);
            }
        }
    }

    public Bitmap getPersonaIcon(int personaHandle) {
        if (this.mService != null) {
            try {
                return this.mService.getPersonaIcon(personaHandle);
            } catch (RemoteException re) {
                Log.w(TAG, "Could not get the persona icon ", re);
            }
        }
        return null;
    }

    public List<PersonaInfo> getPersonasForCreator(int creatorUid, boolean excludeDying) {
        if (this.mService != null) {
            try {
                return this.mService.getPersonasForCreator(creatorUid, excludeDying);
            } catch (RemoteException re) {
                Log.w(TAG, "Could not get the personas for a creator uid ", re);
            }
        }
        return null;
    }

    public boolean exists(int personaId) {
        if (this.mService == null) {
            return false;
        }
        try {
            String value = SystemProperties.get("sys.knox.exists", ProxyInfo.LOCAL_EXCL_LIST);
            if (personaId < 100) {
                return false;
            }
            if (value == null || value.length() <= 0) {
                return this.mService.exists(personaId);
            }
            return containerExists(value, personaId);
        } catch (RemoteException re) {
            Log.w(TAG, "Could not get user info", re);
            return false;
        }
    }

    public boolean isPersonaActivated() {
        int[] personaIds = getPersonaIds();
        if (personaIds == null) {
            return false;
        }
        for (int personaId : personaIds) {
            if (getStateManager(personaId).inState(PersonaState.ACTIVE) || getStateManager(personaId).inState(PersonaState.LOCKED) || getStateManager(personaId).isAttribute(PersonaAttribute.PASSWORD_CHANGE_REQUEST)) {
                return true;
            }
        }
        return false;
    }

    public void setFsMountState(int personaId, boolean mountState) {
        if (this.mService != null) {
            try {
                this.mService.setFsMountState(personaId, mountState);
            } catch (RemoteException re) {
                Log.w(TAG, "Could not setFsMountState", re);
            }
        }
    }

    public void forceRollup(Bundle metadata) {
        Log.i(TAG, "about to force switch to owner!");
    }

    public int getNormalizedState(int personaId) {
        if (this.mService != null) {
            try {
                return this.mService.getNormalizedState(personaId);
            } catch (RemoteException re) {
                Log.w(TAG, "Could not get user info", re);
            }
        }
        return -1;
    }

    public int resetPersona(int personaId) {
        if (this.mService != null) {
            try {
                return this.mService.resetPersona(personaId);
            } catch (RemoteException re) {
                Log.w(TAG, "Could not reset the persona ", re);
            }
        }
        return 0;
    }

    public boolean installApplications(int personaId, List<String> packages) {
        if (this.mService != null) {
            try {
                Log.w(TAG, "in PersonaManager, installDefaultApplications(), calling service API");
                return this.mService.installApplications(personaId, packages);
            } catch (RemoteException re) {
                Log.w(TAG, "Could not install default apps into persona with exception:", re);
            }
        }
        return false;
    }

    public int unInstallSystemApplications(int personaId, List<String> packages) {
        int result = -1;
        if (this.mService != null) {
            try {
                Log.w(TAG, "in PersonaManager, unInstallSystemApplications(), calling service API");
                result = this.mService.unInstallSystemApplications(personaId, packages);
            } catch (RemoteException re) {
                Log.w(TAG, "Could not uninstall system package into persona with exception:", re);
            }
        }
        return result;
    }

    public Object getPersonaService(String name) {
        Log.i(TAG, "getPersonaService() name " + name);
        if (this.mService == null || !name.equals(PERSONA_POLICY_SERVICE)) {
            return null;
        }
        return new PersonaPolicyManager(this.mContext, Stub.asInterface(ServiceManager.getService(PERSONA_POLICY_SERVICE)));
    }

    public Bitmap addLockOnImage(Bitmap icon) {
        if (this.mService != null) {
            try {
                return this.mService.addLockOnImage(icon);
            } catch (RemoteException re) {
                Log.d(TAG, "Could not get addLockOnImage , inside PersonaManager with exception:", re);
            }
        }
        return null;
    }

    public CustomCursor getCallerInfo(String contactRefUriAsString) {
        Log.d(TAG, "PM.getCallerInfo()");
        RCPManager rcpm = (RCPManager) this.mContext.getSystemService(Context.RCP_SERVICE);
        if (rcpm == null) {
            Log.e(TAG, "Received mRCPGlobalContactsDir as null from bridge manager: rcpm == null");
            return null;
        }
        IRCPGlobalContactsDir mRCPGlobalContactsDir = rcpm.getRCPProxy();
        if (mRCPGlobalContactsDir == null) {
            Log.e(TAG, "Received result as null from bridge manager for getCallerInfo: mRCPGlobalContactsDir == null");
            return null;
        }
        try {
            CustomCursor result = mRCPGlobalContactsDir.getCallerInfo(contactRefUriAsString);
            Log.d(TAG, "PM.getCallerInfo(): Received result: " + result);
            return result;
        } catch (RemoteException re) {
            Log.e(TAG, "Couldn't complete call to getCallerInfo , inside PersonaManager with exception:", re);
            return null;
        }
    }

    public IRCPInterface getRCPInterface() {
        Log.d(TAG, "in getRCPInterface");
        RCPManager rcpm = (RCPManager) this.mContext.getSystemService(Context.RCP_SERVICE);
        if (rcpm != null) {
            IRCPInterface rcpInterface = rcpm.getRCPInterface();
            Log.d(TAG, "in getRCPInterface rcpInterface: " + rcpInterface);
            return rcpInterface;
        }
        Log.e(TAG, "Received getRCPInterface as null from bridge manager");
        return null;
    }

    public static Bundle getKnoxInfo() {
        synchronized (PersonaManager.class) {
            if (mKnoxInfo == null) {
                mKnoxInfo = new Bundle();
                try {
                    String version = SystemProperties.get("ro.config.knox");
                    if (version == null || version.isEmpty()) {
                        mKnoxInfo.putString("version", ProxyInfo.LOCAL_EXCL_LIST);
                    } else {
                        if ("v30".equals(version)) {
                            version = "2.0";
                        } else if (WifiEnterpriseConfig.ENGINE_ENABLE.equals(version)) {
                            version = "1.0";
                        }
                        mKnoxInfo.putString("version", version);
                    }
                    mKnoxInfo.putString("isSupportCallerInfo", AudioParameter.AUDIO_PARAMETER_VALUE_false);
                } catch (Exception e) {
                    e.printStackTrace();
                    mKnoxInfo.putString("version", ProxyInfo.LOCAL_EXCL_LIST);
                }
            }
        }
        return mKnoxInfo;
    }

    public static boolean isKnoxVersionSupported(int version) {
        if (version > 0) {
            KnoxContainerVersion currentVersion = getKnoxContainerVersion();
            if (currentVersion != null && currentVersion.getVersionNumber() >= version) {
                return true;
            }
        }
        return false;
    }

    public static boolean isKnoxVersionSupported(KnoxContainerVersion version) {
        if (version != null) {
            KnoxContainerVersion currentVersion = getKnoxContainerVersion();
            if (currentVersion != null && currentVersion.compareTo(version) >= 0) {
                return true;
            }
        }
        return false;
    }

    public static KnoxContainerVersion getKnoxContainerVersion() {
        Bundle mKnoxInfo = getKnoxInfo();
        if (mKnoxInfo == null) {
            return null;
        }
        if ("2.0".equals(mKnoxInfo.getString("version"))) {
            if (Integer.parseInt("10") == 0) {
                return KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_0_0;
            }
            if (Integer.parseInt("10") == 1) {
                return KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_1_0;
            }
            if (Integer.parseInt("10") == 2) {
                return KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_2_0;
            }
            if (Integer.parseInt("10") == 3) {
                return KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_3_0;
            }
            if (Integer.parseInt("10") == 4) {
                return KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_3_1;
            }
            if (Integer.parseInt("10") == 5) {
                return KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_4_0;
            }
            if (Integer.parseInt("10") == 6) {
                return KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_4_1;
            }
            if (Integer.parseInt("10") == 7) {
                return KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_5_0;
            }
            if (Integer.parseInt("10") == 8) {
                return KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_5_1;
            }
            if (Integer.parseInt("10") == 9) {
                return KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_5_2;
            }
            if (Integer.parseInt("10") == 10) {
                return KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_6_0;
            }
            return null;
        } else if ("1.0".equals(mKnoxInfo.getString("version"))) {
            Log.i(TAG, "mKnoxInfo returns 1.0");
            return KnoxContainerVersion.KNOX_CONTAINER_VERSION_1_0_0;
        } else {
            Log.i(TAG, "mKnoxInfo is empty");
            return KnoxContainerVersion.KNOX_CONTAINER_VERSION_NONE;
        }
    }

    public static boolean isSupported(Context context, String tag, String packageName, int userId) {
        if (packageName != null && packageName.startsWith("sec_container_1")) {
            return false;
        }
        if ("2.0".equals(getKnoxInfo().getString("version"))) {
            synchronized (PersonaManager.class) {
                if (personaManager == null) {
                    personaManager = (PersonaManager) context.getSystemService(Context.PERSONA_SERVICE);
                }
            }
            synchronized (PersonaPolicyManager.class) {
                if (personaPolicyMgr == null) {
                    personaPolicyMgr = (PersonaPolicyManager) personaManager.getPersonaService(PERSONA_POLICY_SERVICE);
                }
            }
            if (userId >= 100) {
                if (PersonaInfo.PERSONA_TYPE_DEFAULT.equals(tag)) {
                    return false;
                }
                if (tag.equals(PersonaPolicyManager.CAMERA_MODE)) {
                    return personaPolicyMgr.getCameraModeChangeEnabled(userId);
                }
                if (tag.equals(PersonaPolicyManager.DLNA_DATATRANSFER)) {
                    return personaPolicyMgr.getAllowDLNADataTransfer(userId);
                }
                if (tag.equals("print")) {
                    return personaPolicyMgr.getAllowPrint(userId);
                }
                if (tag.equals(PersonaPolicyManager.ALLSHARE)) {
                    return personaPolicyMgr.getAllowAllShare(userId);
                }
                if (tag.equals(PersonaPolicyManager.GEAR_SUPPORT)) {
                    return personaPolicyMgr.getGearSupportEnabled(userId);
                }
                if (tag.equals(PersonaPolicyManager.PENWINDOW)) {
                    return personaPolicyMgr.getPenWindowEnabled(userId);
                }
                if (tag.equals(PersonaPolicyManager.AIRCOMMAND)) {
                    return personaPolicyMgr.getAirCommandEnabled(userId);
                }
                if (tag.equals(PersonaPolicyManager.IMPORT_FILES)) {
                    return personaPolicyMgr.getAllowImportFiles(userId);
                }
                if (tag.equals(PersonaPolicyManager.EXPORT_FILES)) {
                    return personaPolicyMgr.getAllowExportFiles(userId);
                }
                if (tag.equals(PersonaPolicyManager.EXPORT_AND_DELETE_FILES)) {
                    return personaPolicyMgr.getAllowExportAndDeleteFiles(userId);
                }
                if (tag.equals("print")) {
                    return personaPolicyMgr.getAllowPrint(userId);
                }
                if (tag.equals(PersonaPolicyManager.CONTACTS_IMPORT_EXPORT)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isKioskModeEnabled(Context context) {
        boolean z = false;
        String value = SystemProperties.get("sys.knox.exists", ProxyInfo.LOCAL_EXCL_LIST);
        if (value != null && value.length() > 0) {
            return value.startsWith("5");
        }
        if (context == null) {
            return z;
        }
        PersonaManager pm = (PersonaManager) context.getSystemService(Context.PERSONA_SERVICE);
        if (pm == null || pm.mService == null) {
            return z;
        }
        try {
            return pm.mService.isKioskContainerExistOnDevice();
        } catch (RemoteException re) {
            Log.w(TAG, "failed to isKioskContainerExistOnDevice", re);
            return z;
        }
    }

    public static Bundle getKnoxInfoForApp(Context ctx, String req) {
        synchronized (PersonaManager.class) {
            if (mKnoxInfo == null) {
                getKnoxInfo();
            }
            int userid = UserHandle.myUserId();
            try {
                mKnoxInfo.putInt("userId", userid);
                mKnoxInfo.putInt("checkVersion", getKnoxContainerVersion().ordinal());
                if (!m_bIsKnoxInfoInitialized) {
                    if (userid >= 100) {
                        mKnoxInfo.putString("isKnoxMode", AudioParameter.AUDIO_PARAMETER_VALUE_true);
                        mKnoxInfo.putString("isBlockExternalSD", AudioParameter.AUDIO_PARAMETER_VALUE_false);
                        mKnoxInfo.putString("isBlockBluetoothMenu", AudioParameter.AUDIO_PARAMETER_VALUE_true);
                        mKnoxInfo.putString("isSamsungAccountBlocked", AudioParameter.AUDIO_PARAMETER_VALUE_true);
                    }
                    if (isKioskModeEnabled(ctx)) {
                        mKnoxInfo.putString("isKioskModeEnabled", AudioParameter.AUDIO_PARAMETER_VALUE_true);
                    } else {
                        mKnoxInfo.putString("isKioskModeEnabled", AudioParameter.AUDIO_PARAMETER_VALUE_false);
                    }
                    m_bIsKnoxInfoInitialized = true;
                }
                if (!AudioParameter.AUDIO_PARAMETER_VALUE_true.equals(mKnoxInfo.getString("isKioskModeEnabled")) && "isSupportMoveTo".equals(req)) {
                    setMoveToKnoxInfo(ctx, mKnoxInfo, userid);
                }
                if ("isKnoxModeActive".equals(req)) {
                    if (ActivityManager.getCurrentUser() >= 100) {
                        mKnoxInfo.putString("isKnoxModeActive", AudioParameter.AUDIO_PARAMETER_VALUE_true);
                    } else {
                        mKnoxInfo.putString("isKnoxModeActive", AudioParameter.AUDIO_PARAMETER_VALUE_false);
                    }
                }
                if ("isKioskModeEnabled".equals(req)) {
                    if (isKioskModeEnabled(ctx)) {
                        mKnoxInfo.putString("isKioskModeEnabled", AudioParameter.AUDIO_PARAMETER_VALUE_true);
                    } else {
                        mKnoxInfo.putString("isKioskModeEnabled", AudioParameter.AUDIO_PARAMETER_VALUE_false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mKnoxInfo;
    }

    public static Bundle getKnoxInfoForApp(Context ctx) {
        if (mKnoxInfo == null) {
            getKnoxInfo();
        }
        try {
            if ("2.0".equals(mKnoxInfo.getString("version"))) {
                getKnoxInfoForApp(ctx, "isSupportMoveTo");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mKnoxInfo;
    }

    private static void setMoveToKnoxInfo(Context ctx, Bundle bundle, int userid) {
        bundle.putString("isSupportMoveTo", AudioParameter.AUDIO_PARAMETER_VALUE_false);
        String pkgName;
        if (userid == 0) {
            Bundle bd1 = new Bundle();
            int[] personaIds = null;
            String[] personaNames = null;
            bd1.putString(Parameters.SCENE_MODE_ACTION, "RequestProxy");
            bd1.putString("cmd", "queryPersonaInfos");
            Bundle resp = exchangeData(ctx, bd1);
            if (resp != null) {
                personaIds = resp.getIntArray("personaIds");
                String[] personaTypes = resp.getStringArray("personaTypes");
                personaNames = resp.getStringArray("personaNames");
                mKnoxInfo.putIntArray("personaIds", personaIds);
                mKnoxInfo.putStringArray("personaTypes", personaTypes);
                mKnoxInfo.putStringArray("personaNames", personaNames);
            }
            try {
                LinkedHashMap<Integer, String> KnoxIdNamePair = new LinkedHashMap();
                if (personaIds != null && personaIds.length > 0) {
                    for (int i = 0; i < personaIds.length; i++) {
                        KnoxIdNamePair.put(Integer.valueOf(personaIds[i]), personaNames[i]);
                    }
                    pkgName = ctx.getPackageName();
                    if (!("com.sec.android.app.voicenote".equalsIgnoreCase(pkgName) || "com.samsung.android.snote".equalsIgnoreCase(pkgName))) {
                        bundle.putString("isSupportMoveTo", AudioParameter.AUDIO_PARAMETER_VALUE_true);
                    }
                }
                bundle.putSerializable("KnoxIdNamePair", KnoxIdNamePair);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (userid >= 100) {
            try {
                pkgName = ctx.getPackageName();
                if (!"com.sec.android.app.voicenote".equalsIgnoreCase(pkgName) && !"com.samsung.android.snote".equalsIgnoreCase(pkgName)) {
                    bundle.putString("isSupportMoveTo", AudioParameter.AUDIO_PARAMETER_VALUE_true);
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public static Bundle exchangeData(Context ctx, Bundle bundle) {
        Exception e;
        if (bundle == null) {
            return null;
        }
        Bundle bundle2 = null;
        try {
            if ("RequestProxy".equals(bundle.getString(Parameters.SCENE_MODE_ACTION))) {
                RCPManager rcpm = getRCPManager(ctx);
                if (rcpm != null) {
                    return rcpm.exchangeData(ctx, 0, bundle);
                }
                return null;
            } else if (!"MoveTo".equals(bundle.getString(Parameters.SCENE_MODE_ACTION))) {
                return null;
            } else {
                Bundle ret = new Bundle();
                try {
                    Intent intent = new Intent("action.com.sec.knox.container.exchangeData");
                    intent.putExtras(bundle);
                    intent.putExtra("launchFromPersonaManager", true);
                    intent.setClassName("com.samsung.knox.rcp.components", "com.sec.knox.bridge.operations.ExchangeDataReceiver");
                    ctx.sendBroadcast(intent);
                    ret.putInt(DisplayManager.EXTRA_RESULT_RET, 0);
                    return ret;
                } catch (Exception e2) {
                    e = e2;
                    bundle2 = ret;
                    e.printStackTrace();
                    return bundle2;
                }
            }
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            return bundle2;
        }
    }

    public static RCPManager getRCPManager(Context context) {
        synchronized (PersonaManager.class) {
            if (rcpManager == null) {
                rcpManager = (RCPManager) context.getSystemService(Context.RCP_SERVICE);
            }
        }
        return rcpManager;
    }

    public static boolean isPossibleAddToPersonal(String pkgName) {
        if (pkgName == null || ProxyInfo.LOCAL_EXCL_LIST.equalsIgnoreCase(pkgName) || "null".equalsIgnoreCase(pkgName)) {
            return false;
        }
        for (String pkg : SHORTCUT_FILTER) {
            if (pkg.equalsIgnoreCase(pkgName)) {
                return false;
            }
        }
        return true;
    }

    public long getPersonaBackgroundTime(int personaId) {
        if (this.mService != null) {
            try {
                return this.mService.getPersonaBackgroundTime(personaId);
            } catch (RemoteException re) {
                Log.d(TAG, "Could not get getPersonaBackgroundTime , inside PersonaManager with exception:", re);
            }
        }
        return -1;
    }

    public String getPersonaIdentification(int personaId) {
        if (this.mService != null) {
            try {
                Log.w(TAG, "in PersonaManager, getPersonaIdentification()");
                return this.mService.getPersonaIdentification(personaId);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to get getPersonaIdentification id", re);
            }
        }
        return null;
    }

    public int copyFileBNR(int srcContainerId, String srcFilePath, int destContainerId, String destFilePath) {
        try {
            Log.d(TAG, "PersonaManager.copyFileBNR() srcContainerId=" + srcContainerId + "; srcFilePath=" + srcFilePath + "; destContainerId=" + destContainerId + "; destFilePath=" + destFilePath);
            return ((RCPManager) this.mContext.getSystemService(Context.RCP_SERVICE)).copyFile(srcContainerId, srcFilePath, destContainerId, destFilePath);
        } catch (RemoteException re) {
            Log.w(TAG, "PersonaManager.copyFileBNR(), inside persona manager with exception:", re);
            return -1;
        }
    }

    public boolean deleteFile(String path, int containerId) {
        try {
            Log.d(TAG, "PersonaManager.deleteFile() ContainerId=" + containerId + "; FilePath=" + path);
            return ((RCPManager) this.mContext.getSystemService(Context.RCP_SERVICE)).deleteFile(path, containerId);
        } catch (RemoteException re) {
            Log.w(TAG, "PersonaManager.deleteFile(), inside persona manager with exception:", re);
            return false;
        }
    }

    public int getAdminUidForPersona(int personaId) {
        if (this.mService != null) {
            try {
                Log.w(TAG, "in PersonaManager, getAdminUidForPersona()");
                return this.mService.getAdminUidForPersona(personaId);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to get getAdminUidForPersona id", re);
            }
        }
        return -1;
    }

    public void markForRemoval(int personaId, ComponentName removalActivity) {
        if (this.mService != null) {
            try {
                Log.w(TAG, "markForRemoval() " + personaId);
                this.mService.markForRemoval(personaId, removalActivity);
            } catch (RemoteException re) {
                Log.w(TAG, "markForRemoval()", re);
            }
        }
    }

    public void unmarkForRemoval(int personaId) {
        if (this.mService != null) {
            try {
                Log.w(TAG, "unmarkForRemoval() " + personaId);
                this.mService.unmarkForRemoval(personaId);
            } catch (RemoteException re) {
                Log.w(TAG, "failed unmarkForRemoval()", re);
            }
        }
    }

    public List<PersonaInfo> getUserManagedPersonas(boolean excludeDying) {
        if (this.mService != null) {
            try {
                Log.w(TAG, "getUserManagedPersonas() excludeDying is " + excludeDying);
                return this.mService.getUserManagedPersonas(excludeDying);
            } catch (RemoteException re) {
                Log.w(TAG, "failed getUserManagedPersonas()", re);
            }
        }
        return null;
    }

    public void lockPersona(int personaId) {
        if (this.mService != null) {
            try {
                Log.w(TAG, "lockPersona() " + personaId);
                this.mService.lockPersona(personaId);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to execute lockPersona", re);
            }
        }
    }

    public boolean isSessionExpired(int personaId) {
        if (this.mService != null) {
            try {
                Log.w(TAG, "isSessionExpired() " + personaId);
                return this.mService.isSessionExpired(personaId);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to execute lockPersona", re);
            }
        }
        return true;
    }

    public boolean handleHomeShow() {
        try {
            Log.d(TAG, "in PersonaManager, handleHomeShow()");
            return this.mService.handleHomeShow();
        } catch (RemoteException re) {
            Log.w(TAG, "failed to get handleHomeShow ", re);
            return true;
        }
    }

    public boolean disablePersonaKeyGuard(int personaId) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "disablePersonaKeyGuard  persona " + personaId);
                return this.mService.disablePersonaKeyGuard(personaId);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to disablePersonaKeyGuard", re);
            }
        }
        return false;
    }

    public boolean enablePersonaKeyGuard(int personaId) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "enablePersonaKeyGuard  persona " + personaId);
                return this.mService.enablePersonaKeyGuard(personaId);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to enablePersonaKeyGuard", re);
            }
        }
        return false;
    }

    public boolean adminLockPersona(int personaId, String password) {
        boolean result = false;
        if (this.mService != null) {
            try {
                result = this.mService.adminLockPersona(personaId, password);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to execute adminLockPersona", re);
            }
        }
        return result;
    }

    public boolean adminUnLockPersona(int personaId) {
        boolean result = false;
        if (this.mService != null) {
            try {
                result = this.mService.adminUnLockPersona(personaId);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to execute adminUnLockPersona", re);
            }
        }
        return result;
    }

    public int[] getPersonaIds() {
        if (this.mService != null) {
            try {
                return this.mService.getPersonaIds();
            } catch (RemoteException re) {
                Log.w(TAG, "failed to getPersonaIds", re);
            }
        }
        return null;
    }

    public boolean settingSyncAllowed(int personaId) {
        boolean result = false;
        if (this.mService != null) {
            try {
                result = this.mService.settingSyncAllowed(personaId);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to execute settingSyncAllowed", re);
            }
        }
        return result;
    }

    public void addAppForPersona(AppType type, String packageName, int personaId) {
        if (this.mService != null) {
            try {
                this.mService.addAppForPersona(type.getName(), packageName, personaId);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to addAppForPersona", re);
            }
        }
    }

    public void removeAppForPersona(AppType type, String packageName, int personaId) {
        if (this.mService != null) {
            try {
                this.mService.removeAppForPersona(type.getName(), packageName, personaId);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to removeAppForPersona", re);
            }
        }
    }

    public List<String> getAppListForPersona(AppType type, int personaId) {
        if (this.mService != null) {
            try {
                return this.mService.getAppListForPersona(type.getName(), personaId);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to getAppListForPersona", re);
            }
        }
        return null;
    }

    public void addPackageToInstallWhiteList(String packageName, int personaId) {
        if (this.mService != null) {
            try {
                this.mService.addPackageToInstallWhiteList(packageName, personaId);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to addPackageToInstallWhiteList", re);
            }
        }
    }

    public void removePackageFromInstallWhiteList(String packageName, int personaId) {
        if (this.mService != null) {
            try {
                this.mService.removePackageFromInstallWhiteList(packageName, personaId);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to removePackageFromInstallWhiteList", re);
            }
        }
    }

    public boolean resetPassword(String newPassword) {
        boolean result = false;
        if (this.mService != null) {
            try {
                result = this.mService.resetPassword(newPassword);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to resetPassword", re);
            }
        }
        return result;
    }

    public boolean savePasswordInTima(int personaId, String newPassword) {
        boolean result = false;
        if (this.mService != null) {
            try {
                result = this.mService.savePasswordInTima(personaId, newPassword);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to savePasswordInTima", re);
            }
        }
        return result;
    }

    public void setMaximumScreenOffTimeoutFromDeviceAdmin(long timeMs, int personaId) {
        if (this.mService != null) {
            try {
                this.mService.setMaximumScreenOffTimeoutFromDeviceAdmin(timeMs, personaId);
            } catch (RemoteException re) {
                Log.e(TAG, "failed to setMaximumScreenOffTimeoutFromDeviceAdmin" + Log.getStackTraceString(re));
            }
        }
    }

    public long getScreenOffTime(int personaId) {
        if (this.mService != null) {
            try {
                this.mService.getScreenOffTime(personaId);
            } catch (RemoteException re) {
                Log.e(TAG, "failed to screenOffTime" + Log.getStackTraceString(re));
            }
        }
        return 5000;
    }

    public void refreshTimer(int personaId) {
        if (this.mService != null) {
            try {
                this.mService.refreshTimer(personaId);
            } catch (RemoteException re) {
                Log.e(TAG, "failed to refreshTimer" + Log.getStackTraceString(re));
            }
        }
    }

    public void userActivity(int event) {
        if (this.mService != null) {
            try {
                this.mService.userActivity(event);
            } catch (RemoteException re) {
                Log.e(TAG, "failed to userActivity" + Log.getStackTraceString(re));
            }
        }
    }

    public void onWakeLockChange(boolean isAcquired, int flags, int ownerUid, int ownerPid, String packageName) {
        if (this.mService != null) {
            try {
                this.mService.onWakeLockChange(isAcquired, flags, ownerUid, ownerPid, packageName);
            } catch (RemoteException re) {
                Log.e(TAG, "failed to onWakeLockChange" + Log.getStackTraceString(re));
            }
        }
    }

    public List<String> getDisabledHomeLaunchers(int personaId, boolean clearList) {
        if (this.mService != null) {
            try {
                return this.mService.getDisabledHomeLaunchers(personaId, clearList);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to getDisabledHomeLaunchers", re);
            }
        }
        return null;
    }

    public void clearAppListForPersona(AppType type, int personaId) {
        if (this.mService != null) {
            try {
                this.mService.clearAppListForPersona(type.getName(), personaId);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to clearAppListForPersona", re);
            }
        }
    }

    public boolean isPackageInInstallWhiteList(String packageName, int personaId) {
        if (this.mService != null) {
            try {
                return this.mService.isPackageInInstallWhiteList(packageName, personaId);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to isPackageInInstallWhiteList", re);
            }
        }
        return false;
    }

    public List<String> getPackagesFromInstallWhiteList(int personaId) {
        if (this.mService != null) {
            try {
                return this.mService.getPackagesFromInstallWhiteList(personaId);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to getPackagesFromInstallWhiteList", re);
            }
        }
        return null;
    }

    public String getPasswordHint() {
        String result = null;
        if (this.mService != null) {
            try {
                Log.d(TAG, "getPasswordHint");
                result = this.mService.getPasswordHint();
            } catch (RemoteException re) {
                Log.w(TAG, "failed to getPasswordHint", re);
            }
        }
        return result;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean isCACEnabled(int r8) {
        /*
        r4 = 0;
        r5 = 1;
        r3 = 0;
        r2 = 0;
        r6 = mBTSecureManagerSync;
        monitor-enter(r6);
        r7 = mBTSecureManager;	 Catch:{ all -> 0x003d }
        if (r7 != 0) goto L_0x0011;
    L_0x000b:
        r7 = android.bluetooth.BluetoothSecureManager.getInstant();	 Catch:{ all -> 0x003d }
        mBTSecureManager = r7;	 Catch:{ all -> 0x003d }
    L_0x0011:
        r7 = mBTSecureManager;	 Catch:{ RemoteException -> 0x0033 }
        r3 = r7.isSecureModeEnabled();	 Catch:{ RemoteException -> 0x0033 }
        r7 = 100;
        if (r8 < r7) goto L_0x0030;
    L_0x001b:
        r7 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r8 > r7) goto L_0x0030;
    L_0x001f:
        r0 = android.sec.enterprise.EnterpriseDeviceManager.EDMProxyServiceHelper.getService();	 Catch:{ RemoteException -> 0x0033 }
        if (r0 == 0) goto L_0x0029;
    L_0x0025:
        r2 = r0.isBTSecureAccessAllowedAsUser(r8);	 Catch:{ RemoteException -> 0x0033 }
    L_0x0029:
        if (r3 != r5) goto L_0x002e;
    L_0x002b:
        if (r2 != r5) goto L_0x002e;
    L_0x002d:
        r4 = r5;
    L_0x002e:
        monitor-exit(r6);	 Catch:{ all -> 0x003d }
    L_0x002f:
        return r4;
    L_0x0030:
        monitor-exit(r6);	 Catch:{ all -> 0x003d }
        r4 = r3;
        goto L_0x002f;
    L_0x0033:
        r1 = move-exception;
        r5 = TAG;	 Catch:{ all -> 0x003d }
        r7 = "failed to isCACEnabled";
        android.util.Log.w(r5, r7, r1);	 Catch:{ all -> 0x003d }
        monitor-exit(r6);	 Catch:{ all -> 0x003d }
        goto L_0x002f;
    L_0x003d:
        r4 = move-exception;
        monitor-exit(r6);	 Catch:{ all -> 0x003d }
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.PersonaManager.isCACEnabled(int):boolean");
    }

    public boolean isKioskModeEnabled(int personaId) {
        if (this.mService == null) {
            return false;
        }
        try {
            String value = SystemProperties.get("sys.knox.exists", ProxyInfo.LOCAL_EXCL_LIST);
            String FLAG_KIOSK = "5";
            Log.d(TAG, "isKioskModeEnabled  persona ");
            if (value == null || value.length() <= 0) {
                return this.mService.isKioskModeEnabled(personaId);
            }
            return value.startsWith("5") && containerExists(value, personaId);
        } catch (RemoteException re) {
            Log.w(TAG, "failed to isKioskModeEnabled", re);
            return false;
        }
    }

    public boolean isKioskContainerExistOnDevice() {
        if (this.mService == null) {
            return false;
        }
        try {
            String value = SystemProperties.get("sys.knox.exists", ProxyInfo.LOCAL_EXCL_LIST);
            String FLAG_KIOSK = "5";
            if (value == null || value.length() <= 0) {
                return this.mService.isKioskContainerExistOnDevice();
            }
            return value.startsWith("5");
        } catch (RemoteException re) {
            Log.w(TAG, "failed to isKioskContainerExistOnDevice", re);
            return false;
        }
    }

    public void setBackPressed(int personaId, boolean isBackPressed) {
        if (this.mService != null) {
            try {
                this.mService.setBackPressed(personaId, isBackPressed);
            } catch (RemoteException re) {
                Log.w(TAG, "Could not set back pressed", re);
            }
        }
    }

    public boolean resetPersonaOnReboot(int personaId, boolean enable) {
        boolean result = false;
        if (this.mService != null) {
            try {
                Log.d(TAG, "resetPersonaOnReboot  persona ");
                result = this.mService.resetPersonaOnReboot(personaId, enable);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to resetPersonaOnReboot", re);
            }
        }
        return result;
    }

    public boolean updatePersonaInfo(int personaId, String packageName, int adminUid, int creatorUid) {
        boolean result = false;
        if (this.mService != null) {
            try {
                result = this.mService.updatePersonaInfo(personaId, packageName, adminUid, creatorUid);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to updatePersonaInfo", re);
            }
        }
        Log.d(TAG, "updatePersonaInfo : " + result);
        return result;
    }

    public boolean isResetPersonaOnRebootEnabled(int personaId) {
        boolean result = false;
        if (this.mService != null) {
            try {
                Log.d(TAG, "isResetPersonaOnRebootEnabled  persona ");
                result = this.mService.isResetPersonaOnRebootEnabled(personaId);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to isResetPersonaOnRebootEnabled", re);
            }
        }
        return result;
    }

    public void showKeyguard(int personaId, int flags) {
        if (this.mService != null) {
            try {
                this.mService.showKeyguard(personaId, flags);
            } catch (RemoteException re) {
                Log.w(TAG, "Could not showKeyguard", re);
            }
        }
    }

    public void doWhenUnlock(int personaId, IKnoxUnlockAction r) {
        if (this.mService != null) {
            try {
                this.mService.doWhenUnlock(personaId, r);
            } catch (RemoteException re) {
                Log.w(TAG, "Could not showKeyguard", re);
            }
        }
    }

    public void notifyKeyguardShow(int personaId, boolean isShown) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "notifyKeyguardShow for " + personaId + " is shown" + isShown);
                this.mService.notifyKeyguardShow(personaId, isShown);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to notifyKeyguardShow", re);
            }
        }
    }

    public boolean isKnoxKeyguardShown(int personaId) {
        if (this.mService != null) {
            try {
                return this.mService.isKnoxKeyguardShown(personaId);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to isKnoxKeyguardShown", re);
            }
        }
        return false;
    }

    public boolean getKeyguardShowState(int personaId) {
        if (this.mService != null) {
            try {
                return this.mService.getKeyguardShowState(personaId);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to getKeyguardShowState", re);
            }
        }
        return false;
    }

    public boolean getKeyguardShowState() {
        List<PersonaInfo> l = getPersonas();
        if (l == null || l.size() == 0) {
            return false;
        }
        for (PersonaInfo i : l) {
            if (getKeyguardShowState(i.id)) {
                return true;
            }
        }
        return false;
    }

    public void hideScrim() {
        if (this.mService != null) {
            try {
                this.mService.hideScrim();
            } catch (RemoteException re) {
                Log.w(TAG, "failed to hideScrim()", re);
            }
        }
    }

    private static boolean containerExists(String value, int personaId) {
        if ("0".equals(value)) {
            return false;
        }
        String SEPARATOR = ":";
        String[] arr = value.split(":");
        for (int i = 1; i < arr.length; i++) {
            if (arr[i].equals(String.valueOf(personaId))) {
                return true;
            }
        }
        return false;
    }

    public void setAccessPermission(String type, int personaId, boolean canAccess) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "setAccessPermission for type " + type + " personaId " + personaId + " canAccess " + canAccess);
                this.mService.setAccessPermission(type, personaId, canAccess);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to setAccessPermission", re);
            }
        }
    }

    public boolean canAccess(String type, int personaId) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "canAccess for type " + type + " personaId " + personaId);
                return this.mService.canAccess(type, personaId);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to get access permission", re);
            }
        }
        return false;
    }

    public void setShownHelp(int personaId, int containerType, boolean value) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "setShownHelp for " + personaId + ", " + containerType + ":" + value);
                this.mService.setShownHelp(personaId, containerType, value);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to setShownHelp", re);
            }
        }
    }

    public void convertContainerType(int personaId, int containerType) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "convertContainerType for " + personaId + ":" + containerType);
                this.mService.convertContainerType(personaId, containerType);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to convertContainerType", re);
            }
        }
    }

    public boolean getIsFingerAsSupplement() {
        if (this.mService != null) {
            try {
                Log.d(TAG, "getIsFingerAsSupplement");
                return this.mService.getIsFingerAsSupplement(UserHandle.myUserId());
            } catch (RemoteException re) {
                Log.w(TAG, "can't read getIsFingerAsSupplement from PMS", re);
            }
        }
        return false;
    }

    public void setIsFingerAsSupplement(int userId, boolean isFingerprintAsSupplement) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "setIsFingerAsSupplement");
                this.mService.setIsFingerAsSupplement(userId, isFingerprintAsSupplement);
            } catch (RemoteException re) {
                Log.w(TAG, "can't read setIsFingerAsSupplement from PMS", re);
            }
        }
    }

    public long getLastKeyguardUnlockTime() {
        if (this.mService != null) {
            try {
                Log.d(TAG, "getLastKeyguadUnlockTime");
                return this.mService.getLastKeyguardUnlockTime(UserHandle.myUserId());
            } catch (RemoteException re) {
                Log.w(TAG, "can't read getLastKeyguardUnlockTime from PMS", re);
            }
        }
        return -1;
    }

    public void setLastKeyguardUnlockTime(int userId, long lastKeyguardUnlockTime) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "setLastKeyguardUnlockTime");
                this.mService.setLastKeyguardUnlockTime(userId, lastKeyguardUnlockTime);
            } catch (RemoteException re) {
                Log.w(TAG, "can't read setLastKeyguardUnlockTime from PMS", re);
            }
        }
    }

    public boolean getIsUnlockedAfterTurnOn() {
        if (this.mService != null) {
            try {
                Log.d(TAG, "getIsUnlockedAfterTurnOn");
                return this.mService.getIsUnlockedAfterTurnOn(UserHandle.myUserId());
            } catch (RemoteException re) {
                Log.w(TAG, "can't read getIsUnlockedAfterTurnOn from PMS", re);
            }
        }
        return false;
    }

    public void setIsUnlockedAfterTurnOn(int userId, boolean isUnlockBefore) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "setIsUnlockedAfterTurnOn");
                this.mService.setIsUnlockedAfterTurnOn(userId, isUnlockBefore);
            } catch (RemoteException re) {
                Log.w(TAG, "can't read setIsUnlockedAfterTurnOn from PMS", re);
            }
        }
    }

    public boolean getIsQuickAccessUIEnabled() {
        if (this.mService != null) {
            try {
                Log.d(TAG, "getIsQuickAccessUIEnabled");
                return this.mService.getIsQuickAccessUIEnabled(UserHandle.myUserId());
            } catch (RemoteException re) {
                Log.w(TAG, "can't read getIsQuickAccessUIEnabled from PMS", re);
            }
        }
        return false;
    }

    public void setIsQuickAccessUIEnabled(int userId, boolean isUnlockBefore) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "setIsQuickAccessUIEnabled");
                this.mService.setIsQuickAccessUIEnabled(userId, isUnlockBefore);
            } catch (RemoteException re) {
                Log.w(TAG, "can't read setIsQuickAccessUIEnabled from PMS", re);
            }
        }
    }

    public boolean getIsFingerTimeout() {
        if (this.mService != null) {
            try {
                Log.d(TAG, "getIsFingerTimeout");
                return this.mService.getIsFingerTimeout(UserHandle.myUserId());
            } catch (RemoteException re) {
                Log.w(TAG, "can't read getIsFingerTimeout from PMS", re);
            }
        }
        return false;
    }

    public void setIsFingerTimeout(int userId, boolean isFingerTimeout) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "setIsFingerTimeout");
                this.mService.setIsFingerTimeout(userId, isFingerTimeout);
            } catch (RemoteException re) {
                Log.w(TAG, "can't read setIsFingerTimeout from PMS", re);
            }
        }
    }

    public boolean getIsFingerReset() {
        if (this.mService != null) {
            try {
                Log.d(TAG, "getIsFingerReset");
                return this.mService.getIsFingerReset(UserHandle.myUserId());
            } catch (RemoteException re) {
                Log.w(TAG, "can't read getIsFingerReset from PMS", re);
            }
        }
        return false;
    }

    public void setIsFingerReset(int userId, boolean isFingerReset) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "setIsFingerReset");
                this.mService.setIsFingerReset(userId, isFingerReset);
            } catch (RemoteException re) {
                Log.w(TAG, "can't read setIsFingerReset from PMS", re);
            }
        }
    }

    public boolean getIsAdminLockedJustBefore() {
        if (this.mService != null) {
            try {
                Log.d(TAG, "getIsAdminLockedJustBefore");
                return this.mService.getIsAdminLockedJustBefore(UserHandle.myUserId());
            } catch (RemoteException re) {
                Log.w(TAG, "can't read getIsAdminLockedJustBefore from PMS", re);
            }
        }
        return false;
    }

    public void setIsAdminLockedJustBefore(int userId, boolean isAdminLockedJustBefore) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "setIsAdminLockedJustBefore");
                this.mService.setIsAdminLockedJustBefore(userId, isAdminLockedJustBefore);
            } catch (RemoteException re) {
                Log.w(TAG, "can't read setIsAdminLockedJustBefore from PMS", re);
            }
        }
    }

    public boolean getIsFingerIdentifyFailed() {
        if (this.mService != null) {
            try {
                Log.d(TAG, "getIsFingerIdentifyFailed");
                return this.mService.getIsFingerIdentifyFailed(UserHandle.myUserId());
            } catch (RemoteException re) {
                Log.w(TAG, "can't read getIsFingerIdentifyFailed from PMS", re);
            }
        }
        return false;
    }

    public void setIsFingerIdentifyFailed(boolean isFingerIdentifyFailed) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "setIsFingerIdentifyFailed");
                this.mService.setIsFingerIdentifyFailed(UserHandle.myUserId(), isFingerIdentifyFailed);
            } catch (RemoteException re) {
                Log.w(TAG, "can't read setIsFingerIdentifyFailed from PMS", re);
            }
        }
    }

    public int getFingerCount() {
        if (this.mService != null) {
            try {
                Log.d(TAG, "getFingerCount");
                return this.mService.getFingerCount(UserHandle.myUserId());
            } catch (RemoteException re) {
                Log.w(TAG, "can't read getFingerCount from PMS", re);
            }
        }
        return 0;
    }

    public void setFingerCount(int setFingerCount) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "setFingerCount");
                this.mService.setFingerCount(UserHandle.myUserId(), setFingerCount);
            } catch (RemoteException re) {
                Log.w(TAG, "can't read setFingerCount from PMS", re);
            }
        }
    }

    public int getForegroundUser() {
        if (this.mService != null) {
            try {
                Log.d(TAG, "getForegroundUser");
                return this.mService.getForegroundUser();
            } catch (RemoteException re) {
                Log.w(TAG, "getForegroundUser error", re);
            }
        }
        return 0;
    }

    public int getFocusedUser() {
        if (this.mService != null) {
            try {
                return this.mService.getFocusedUser();
            } catch (RemoteException re) {
                Log.w(TAG, "getFocusedUser error", re);
            }
        }
        return 0;
    }

    public void setFocusedUser(int userId) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "setFocusedUser");
                this.mService.setFocusedUser(userId);
            } catch (RemoteException re) {
                Log.w(TAG, "setFocusedUser error", re);
            }
        }
    }

    public boolean isFingerSupplementActivated() {
        if (this.mService != null) {
            try {
                Log.d(TAG, "isFingerSupplementActivated, pid : " + UserHandle.myUserId());
                return this.mService.isFingerSupplementActivated(UserHandle.myUserId());
            } catch (RemoteException re) {
                Log.w(TAG, "can't read finger activated value from PMS", re);
            }
        }
        return false;
    }

    public boolean isFingerLockscreenActivated() {
        if (this.mService != null) {
            try {
                Log.d(TAG, "isFingerLockscreenActivated, pid : " + UserHandle.myUserId());
                return this.mService.isFingerLockscreenActivated(UserHandle.myUserId());
            } catch (RemoteException re) {
                Log.w(TAG, "can't read finger activated value from PMS", re);
            }
        }
        return false;
    }

    public static String getPersonaName(Context mContext, int personaId) {
        if (personaId >= 100) {
            UserInfo ui = ((UserManager) mContext.getSystemService(Context.USER_SERVICE)).getUserInfo(personaId);
            if (ui != null) {
                return ui.name;
            }
            Log.e(TAG, "User doesn't exist. : " + personaId);
        }
        return null;
    }

    public StateManager getStateManager(int userId) {
        Log.d(TAG, "getStateManager()");
        return new StateManager(this.mContext, this.mService, userId);
    }

    public int getKnoxSecurityTimeout() {
        if (this.mService != null) {
            try {
                Log.d(TAG, "getKnoxSecurityTimeout");
                return this.mService.getKnoxSecurityTimeout(UserHandle.myUserId());
            } catch (RemoteException re) {
                Log.w(TAG, "can't read getKnoxSecurityTimeout from PMS. return default value", re);
            }
        }
        return PersonaInfo.KNOX_SECURITY_TIMEOUT_DEFAULT;
    }

    public void setKnoxSecurityTimeout(int timeout) {
        setKnoxSecurityTimeout(UserHandle.myUserId(), timeout);
    }

    public void setKnoxSecurityTimeout(int personaId, int timeout) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "setKnoxSecurityTimeout");
                this.mService.setKnoxSecurityTimeout(personaId, timeout);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to setKnoxSecurityTimeout from PMS", re);
            }
        }
    }

    public void onKeyguardBackPressed(int personaId) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "onKeyguardBackPressed for " + personaId);
                this.mService.onKeyguardBackPressed(personaId);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to onKeyguardBackPressed from PMS", re);
            }
        }
    }

    public boolean mountOldContainer(String password, String srcPath, String dstPath, int excludeMediaTypes, int containerId) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "mountOldContainer, pid : " + UserHandle.myUserId());
                return this.mService.mountOldContainer(password, srcPath, dstPath, excludeMediaTypes, containerId);
            } catch (RemoteException re) {
                Log.w(TAG, "can't mount Knox 1.0 partition from PMS", re);
            }
        }
        return false;
    }

    public boolean unmountOldContainer(String dstPath) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "unmountOldContainer, pid : " + UserHandle.myUserId());
                return this.mService.unmountOldContainer(dstPath);
            } catch (RemoteException re) {
                Log.w(TAG, "can't unmount Knox 1.0 partition from PMS", re);
            }
        }
        return false;
    }

    public boolean verifyKnoxBackupPin(int personaId, String backupPin) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "verifyKnoxBackupPin");
                return this.mService.verifyKnoxBackupPin(personaId, backupPin);
            } catch (RemoteException re) {
                Log.w(TAG, "can't verify Knox Backup Pin from PMS. return default value", re);
            }
        }
        return false;
    }

    public void setKnoxBackupPin(int personaId, String backupPin) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "setKnoxBackupPin");
                this.mService.setKnoxBackupPin(personaId, backupPin);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to verifyKnoxBackupPin from PMS", re);
            }
        }
    }

    public String getKnoxNameChanged(String component, int personaId) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "getKnoxNameChanged");
                return this.mService.getKnoxNameChanged(component, personaId);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to getKnoxNameChanged from PMS", re);
            }
        }
        return ProxyInfo.LOCAL_EXCL_LIST;
    }

    public String getKnoxNameChangedAsUser(int personaId) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "getKnoxNameChangedAsUser");
                return this.mService.getKnoxNameChangedAsUser(personaId);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to getKnoxNameChangedAsUser from PMS", re);
            }
        }
        return ProxyInfo.LOCAL_EXCL_LIST;
    }

    public static boolean isKnoxId(int userId) {
        return userId >= 100 && userId <= 200;
    }

    public Bitmap getKnoxIconChanged(String packageName, int personaId) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "getKnoxIconChanged");
                return this.mService.getKnoxIconChanged(packageName, personaId);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to getKnoxIconChanged from PMS", re);
            }
        }
        return null;
    }

    public Bitmap getKnoxIconChangedAsUser(int personaId) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "getKnoxIconChangedAsUser");
                return this.mService.getKnoxIconChangedAsUser(personaId);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to getKnoxIconChangedAsUser from PMS", re);
            }
        }
        return null;
    }

    public static int getBbcEnabled() {
        String value = SystemProperties.get("sys.knox.bbcid", null);
        int id = -10000;
        if (value != null) {
            try {
                if (!value.isEmpty()) {
                    id = Integer.valueOf(value).intValue();
                }
            } catch (Exception e) {
            }
        }
        return id;
    }

    public static boolean isBBCContainer(int userId) {
        return userId >= 195 && userId <= 199;
    }

    public static Drawable getBBCBadgeIcon(ActivityInfo info, int userId) {
        Bundle metadata = info.applicationInfo.metaData;
        if (!(metadata == null || metadata.getInt(BBC_METADATA) == 0 || (userId != 0 && !isBBCContainer(userId)))) {
            try {
                byte[] imageData = EnterpriseDeviceManager.getInstance().getApplicationPolicy().getApplicationIconFromDb(info.packageName, 0);
                if (imageData != null) {
                    ByteArrayInputStream is = new ByteArrayInputStream(imageData);
                    TypedValue typedValue = new TypedValue();
                    typedValue.density = 0;
                    Drawable drw = Drawable.createFromResourceStream(null, typedValue, is, null);
                    Log.i(TAG, "EDM:ApplicationIcon got from EDM database ");
                    return drw;
                }
            } catch (Exception e) {
                Log.w(TAG, "EDM: Get Icon EX: " + e);
            }
        }
        return null;
    }

    public static PathStrategy getPathStrategy(int userid) {
        PathStrategy ret = (PathStrategy) pathstrategy.get(userid);
        if (ret != null) {
            return ret;
        }
        ret = new PathStrategy("bbcfileprovider", userid);
        pathstrategy.put(userid, ret);
        return ret;
    }

    public boolean isNFCAllowed(int userId) {
        return isNFCAllowed(userId, null);
    }

    public boolean isNFCAllowed(int userId, Intent intent) {
        if (intent != null) {
            String data = intent.getDataString();
            if (data != null) {
                for (String intentData : this.NFCblackList) {
                    if (data.contains(intentData)) {
                        Log.d(TAG, "NFC action is in blacklist. return false");
                        return false;
                    }
                }
            }
        }
        if (this.mService != null) {
            try {
                Log.d(TAG, "isNFCAllowed");
                return this.mService.isNFCAllowed(userId);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to isNFCAllowed from PMS", re);
            }
        }
        return true;
    }

    public void setFingerprintIndex(int personaId, boolean enable, int[] list) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "setFingerprintIndex");
                this.mService.setFingerprintIndex(personaId, enable, list);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to setFingerprintIndex from PMS", re);
            }
        }
    }

    public boolean isEnabledFingerprintIndex(int personaId) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "isEnabledFingerprintIndex");
                return this.mService.isEnabledFingerprintIndex(personaId);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to isEnabledFingerprintIndex from PMS", re);
            }
        }
        return false;
    }

    public int[] getFingerprintIndex(int personaId) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "getFingerprintIndex");
                return this.mService.getFingerprintIndex(personaId);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to getFingerprintIndex from PMS", re);
            }
        }
        return null;
    }

    public void setFingerprintHash(int personaId, List<String> list) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "setFingerprintHash");
                this.mService.setFingerprintHash(personaId, list);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to setFingerprintHash from PMS", re);
            }
        }
    }

    public List<String> getFingerprintHash(int personaId) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "getFingerprintHash");
                return this.mService.getFingerprintHash(personaId);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to getFingerprintHash from PMS", re);
            }
        }
        return null;
    }

    public void resetPersonaPassword(int personaId, String pwdResetToken, int timeout) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "resetPersonaPassword for " + personaId);
                this.mService.resetPersonaPassword(personaId, pwdResetToken, timeout);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to resetPersonaPassword from PMS", re);
            }
        }
    }

    public void setupComplete(int personaId) {
        if (this.mService != null) {
            try {
                Log.d(TAG, "setupComplete");
                this.mService.setupComplete(personaId);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to setupComplete", re);
            }
        }
    }

    public static boolean isKnoxAppRunning(Context context) {
        boolean result;
        int userId = ((RunningTaskInfo) ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1).get(0)).userId;
        if (userId < 100 || userId > 200) {
            result = false;
        } else {
            result = true;
        }
        Log.d(TAG, "isKnoxAppRunning userId = " + userId + " result = " + result);
        return result;
    }

    public Drawable getCustomBadgedIconifRequired(Context mContext, Drawable originalIcon, String pkgName, UserHandle mUser) {
        return getCustomBadgedIconifRequired(mContext, originalIcon, pkgName, mUser, 4);
    }

    public Drawable getCustomBadgedIconifRequired(Context mContext, Drawable originalIcon, String pkgName, UserHandle mUser, int position) {
        int userId = mUser.getIdentifier();
        if (isKnoxId(userId) && !(isKioskContainerExistOnDevice() && isKnoxVersionSupported(KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_5_0))) {
            try {
                int badgeResourceId = this.mService.getCustomBadgedResourceIdIconifRequired(pkgName, userId);
                if (badgeResourceId != 0) {
                    originalIcon = mContext.getPackageManager().getCustomBadgedIcon(originalIcon, mUser, badgeResourceId, position);
                }
            } catch (RemoteException e) {
            }
        }
        return originalIcon;
    }

    public static boolean isKnoxMultiwindowsSupported() {
        return isKnoxVersionSupported(KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_6_0);
    }

    public String getDefaultQuickSettings() {
        if (this.mService != null) {
            try {
                return this.mService.getDefaultQuickSettings();
            } catch (RemoteException re) {
                Log.w(TAG, "failed to getDefaultQuickSettings from PMS", re);
            }
        }
        return ProxyInfo.LOCAL_EXCL_LIST;
    }

    public PackageInfo getPackageInfo(String packageName, int flags, int userId) {
        if (this.mService != null) {
            try {
                return this.mService.getPackageInfo(packageName, flags, userId);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to getPackageInfo from PMS", re);
            }
        }
        return null;
    }

    public static String readOMCSalesCode() {
        String sales_code = ProxyInfo.LOCAL_EXCL_LIST;
        try {
            sales_code = SystemProperties.get("persist.omc.sales_code");
            if (ProxyInfo.LOCAL_EXCL_LIST.equals(sales_code) || sales_code == null) {
                sales_code = SystemProperties.get("ro.csc.sales_code");
                if (ProxyInfo.LOCAL_EXCL_LIST.equals(sales_code) || sales_code == null) {
                    sales_code = SystemProperties.get("ril.sales_code");
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "readOMCSalesCode failed");
        }
        if (sales_code == null) {
            return ProxyInfo.LOCAL_EXCL_LIST;
        }
        return sales_code;
    }

    public List<String> getContainerHideUsageStatsApps() {
        if (this.mService != null) {
            try {
                return this.mService.getContainerHideUsageStatsApps();
            } catch (RemoteException re) {
                Log.w(TAG, "Failed to get usage stats app hide list ", re);
            }
        }
        return Collections.EMPTY_LIST;
    }

    public void addPackageToNonSecureAppList(String packageName) {
        if (this.mService != null) {
            try {
                this.mService.addPackageToNonSecureAppList(packageName);
            } catch (RemoteException re) {
                Log.w(TAG, "failed to addPackageToNonSecureAppList from PMS", re);
            }
        }
    }

    public List<String> getNonSecureAppList() {
        if (this.mService != null) {
            try {
                return this.mService.getNonSecureAppList();
            } catch (RemoteException re) {
                Log.w(TAG, "failed to getNonSecureAppList from PMS", re);
            }
        }
        return null;
    }

    public void clearNonSecureAppList() {
        if (this.mService != null) {
            try {
                this.mService.clearNonSecureAppList();
            } catch (RemoteException re) {
                Log.w(TAG, "failed to clearNonSecureAppList from PMS", re);
            }
        }
    }

    public boolean isFotaUpgradeVersionChanged() {
        if (this.mService != null) {
            try {
                return this.mService.isFotaUpgradeVersionChanged();
            } catch (RemoteException re) {
                Log.w(TAG, "Could not get isFotaUpgradeVersionChanged", re);
            }
        }
        return false;
    }

    public void removeKnoxAppsinFota(int userId) {
        if (this.mService != null) {
            try {
                this.mService.removeKnoxAppsinFota(userId);
            } catch (RemoteException re) {
                Log.w(TAG, "Could not removeKnoxAppsinFota", re);
            }
        }
    }
}
