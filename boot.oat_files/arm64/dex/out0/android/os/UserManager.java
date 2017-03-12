package android.os;

import android.app.ActivityManager;
import android.app.ActivityManagerNative;
import android.content.Context;
import android.content.pm.UserInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.ProxyInfo;
import android.net.wifi.WifiEnterpriseConfig;
import android.provider.Settings.Global;
import android.provider.Settings.Secure;
import android.util.Log;
import com.android.internal.util.UserIcons;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    public static final String ALLOW_PARENT_PROFILE_APP_LINKING = "allow_parent_profile_app_linking";
    public static final String DISALLOW_ADD_USER = "no_add_user";
    public static final String DISALLOW_ADJUST_VOLUME = "no_adjust_volume";
    public static final String DISALLOW_APPS_CONTROL = "no_control_apps";
    public static final String DISALLOW_CONFIG_BLUETOOTH = "no_config_bluetooth";
    public static final String DISALLOW_CONFIG_CELL_BROADCASTS = "no_config_cell_broadcasts";
    public static final String DISALLOW_CONFIG_CREDENTIALS = "no_config_credentials";
    public static final String DISALLOW_CONFIG_MOBILE_NETWORKS = "no_config_mobile_networks";
    public static final String DISALLOW_CONFIG_TETHERING = "no_config_tethering";
    public static final String DISALLOW_CONFIG_VPN = "no_config_vpn";
    public static final String DISALLOW_CONFIG_WIFI = "no_config_wifi";
    public static final String DISALLOW_CREATE_WINDOWS = "no_create_windows";
    public static final String DISALLOW_CROSS_PROFILE_COPY_PASTE = "no_cross_profile_copy_paste";
    public static final String DISALLOW_DEBUGGING_FEATURES = "no_debugging_features";
    public static final String DISALLOW_FACTORY_RESET = "no_factory_reset";
    public static final String DISALLOW_FUN = "no_fun";
    public static final String DISALLOW_INSTALL_APPS = "no_install_apps";
    public static final String DISALLOW_INSTALL_UNKNOWN_SOURCES = "no_install_unknown_sources";
    public static final String DISALLOW_MODIFY_ACCOUNTS = "no_modify_accounts";
    public static final String DISALLOW_MOUNT_PHYSICAL_MEDIA = "no_physical_media";
    public static final String DISALLOW_NETWORK_RESET = "no_network_reset";
    public static final String DISALLOW_OUTGOING_BEAM = "no_outgoing_beam";
    public static final String DISALLOW_OUTGOING_CALLS = "no_outgoing_calls";
    public static final String DISALLOW_RECORD_AUDIO = "no_record_audio";
    public static final String DISALLOW_REMOVE_USER = "no_remove_user";
    public static final String DISALLOW_SAFE_BOOT = "no_safe_boot";
    public static final String DISALLOW_SHARE_LOCATION = "no_share_location";
    public static final String DISALLOW_SMS = "no_sms";
    public static final String DISALLOW_UNINSTALL_APPS = "no_uninstall_apps";
    public static final String DISALLOW_UNMUTE_MICROPHONE = "no_unmute_microphone";
    public static final String DISALLOW_USB_FILE_TRANSFER = "no_usb_file_transfer";
    public static final String DISALLOW_WALLPAPER = "no_wallpaper";
    public static final String ENSURE_VERIFY_APPS = "ensure_verify_apps";
    public static final String KEY_RESTRICTIONS_PENDING = "restrictions_pending";
    public static final int PIN_VERIFICATION_FAILED_INCORRECT = -3;
    public static final int PIN_VERIFICATION_FAILED_NOT_SET = -2;
    public static final int PIN_VERIFICATION_SUCCESS = -1;
    private static String TAG = "UserManager";
    private static UserManager sInstance = null;
    private final Context mContext;
    private final IUserManager mService;

    public static synchronized UserManager get(Context context) {
        UserManager userManager;
        synchronized (UserManager.class) {
            if (sInstance == null) {
                sInstance = (UserManager) context.getSystemService(Context.USER_SERVICE);
            }
            userManager = sInstance;
        }
        return userManager;
    }

    public UserManager(Context context, IUserManager service) {
        this.mService = service;
        this.mContext = context;
    }

    public static boolean supportsMultipleUsers() {
        boolean config = SystemProperties.getBoolean("persist.sys.show_multiuserui", Resources.getSystem().getBoolean(17956983));
        if (getMaxSupportedUsers() <= 1 || !SystemProperties.getBoolean("fw.show_multiuserui", config)) {
            return false;
        }
        return true;
    }

    public int getUserHandle() {
        return UserHandle.myUserId();
    }

    public String getUserName() {
        try {
            return this.mService.getUserInfo(getUserHandle()).name;
        } catch (RemoteException re) {
            Log.w(TAG, "Could not get user name", re);
            return ProxyInfo.LOCAL_EXCL_LIST;
        }
    }

    public boolean isUserAGoat() {
        return this.mContext.getPackageManager().isPackageAvailable("com.coffeestainstudios.goatsimulator");
    }

    public boolean isSystemUser() {
        return UserHandle.myUserId() == 0;
    }

    public boolean isAdminUser() {
        UserInfo user = getUserInfo(UserHandle.myUserId());
        return user != null ? user.isAdmin() : false;
    }

    public boolean isLinkedUser() {
        try {
            return this.mService.isRestricted();
        } catch (RemoteException re) {
            Log.w(TAG, "Could not check if user is limited ", re);
            return false;
        }
    }

    public boolean isGuestUser() {
        UserInfo user = getUserInfo(UserHandle.myUserId());
        return user != null ? user.isGuest() : false;
    }

    public boolean isManagedProfile() {
        if (PersonaManager.isKnoxId(UserHandle.myUserId())) {
            String callingPackage = this.mContext.getPackageManager().getNameForUid(Binder.getCallingUid());
            if (callingPackage.equals("com.google.android.packageinstaller") || callingPackage.equals("com.samsung.android.packageinstaller")) {
                return false;
            }
        }
        UserInfo user = getUserInfo(UserHandle.myUserId());
        if (user != null) {
            return user.isManagedProfile();
        }
        return false;
    }

    public boolean isUserRunning(UserHandle user) {
        boolean z = false;
        try {
            z = ActivityManagerNative.getDefault().isUserRunning(user.getIdentifier(), false);
        } catch (RemoteException e) {
        }
        return z;
    }

    public boolean isUserRunningOrStopping(UserHandle user) {
        try {
            return ActivityManagerNative.getDefault().isUserRunning(user.getIdentifier(), true);
        } catch (RemoteException e) {
            return false;
        }
    }

    public UserInfo getUserInfo(int userHandle) {
        try {
            return this.mService.getUserInfo(userHandle);
        } catch (RemoteException re) {
            Log.w(TAG, "Could not get user info", re);
            return null;
        }
    }

    public Bundle getUserRestrictions() {
        return getUserRestrictions(Process.myUserHandle());
    }

    public Bundle getUserRestrictions(UserHandle userHandle) {
        try {
            return this.mService.getUserRestrictions(userHandle.getIdentifier());
        } catch (RemoteException re) {
            Log.w(TAG, "Could not get user restrictions", re);
            return Bundle.EMPTY;
        }
    }

    @Deprecated
    public void setUserRestrictions(Bundle restrictions) {
        setUserRestrictions(restrictions, Process.myUserHandle());
    }

    @Deprecated
    public void setUserRestrictions(Bundle restrictions, UserHandle userHandle) {
        try {
            this.mService.setUserRestrictions(restrictions, userHandle.getIdentifier());
        } catch (RemoteException re) {
            Log.w(TAG, "Could not set user restrictions", re);
        }
    }

    @Deprecated
    public void setUserRestriction(String key, boolean value) {
        Bundle bundle = getUserRestrictions();
        bundle.putBoolean(key, value);
        setUserRestrictions(bundle);
    }

    @Deprecated
    public void setUserRestriction(String key, boolean value, UserHandle userHandle) {
        try {
            this.mService.setUserRestriction(key, value, userHandle.getIdentifier());
        } catch (RemoteException re) {
            Log.w(TAG, "Could not set user restriction", re);
        }
    }

    public void setKnoxRestrictions(Bundle restrictions, int userId) {
        try {
            this.mService.setKnoxRestrictions(restrictions, userId);
        } catch (Exception re) {
            Log.w(TAG, "Could not set Knox restriction", re);
        }
    }

    public boolean hasUserRestriction(String restrictionKey) {
        return hasUserRestriction(restrictionKey, Process.myUserHandle());
    }

    public boolean hasUserRestriction(String restrictionKey, UserHandle userHandle) {
        try {
            return this.mService.hasUserRestriction(restrictionKey, userHandle.getIdentifier());
        } catch (RemoteException re) {
            Log.w(TAG, "Could not check user restrictions", re);
            return false;
        }
    }

    public long getSerialNumberForUser(UserHandle user) {
        return (long) getUserSerialNumber(user.getIdentifier());
    }

    public UserHandle getUserForSerialNumber(long serialNumber) {
        int ident = getUserHandle((int) serialNumber);
        return ident >= 0 ? new UserHandle(ident) : null;
    }

    public UserInfo createUser(String name, int flags) {
        try {
            return this.mService.createUser(name, flags);
        } catch (RemoteException re) {
            Log.w(TAG, "Could not create a user", re);
            return null;
        }
    }

    public UserInfo createGuest(Context context, String name) {
        UserInfo guest = createUser(name, 4);
        if (guest != null) {
            Secure.putStringForUser(context.getContentResolver(), Secure.SKIP_FIRST_USE_HINTS, WifiEnterpriseConfig.ENGINE_ENABLE, guest.id);
            try {
                Bundle guestRestrictions = this.mService.getDefaultGuestRestrictions();
                guestRestrictions.putBoolean(DISALLOW_SMS, true);
                guestRestrictions.putBoolean(DISALLOW_INSTALL_UNKNOWN_SOURCES, true);
                this.mService.setUserRestrictions(guestRestrictions, guest.id);
            } catch (RemoteException e) {
                Log.w(TAG, "Could not update guest restrictions");
            }
        }
        return guest;
    }

    public UserInfo createSecondaryUser(String name, int flags) {
        try {
            UserInfo user = this.mService.createUser(name, flags);
            if (user == null) {
                return null;
            }
            Bundle userRestrictions = this.mService.getUserRestrictions(user.id);
            addDefaultUserRestrictions(userRestrictions);
            this.mService.setUserRestrictions(userRestrictions, user.id);
            return user;
        } catch (RemoteException re) {
            Log.w(TAG, "Could not create a user", re);
            return null;
        }
    }

    private static void addDefaultUserRestrictions(Bundle restrictions) {
        restrictions.putBoolean(DISALLOW_OUTGOING_CALLS, true);
        restrictions.putBoolean(DISALLOW_SMS, true);
    }

    public UserInfo createProfileForUser(String name, int flags, int userHandle) {
        try {
            return this.mService.createProfileForUser(name, flags, userHandle);
        } catch (RemoteException re) {
            Log.w(TAG, "Could not create a user", re);
            return null;
        }
    }

    public boolean markGuestForDeletion(int userHandle) {
        try {
            return this.mService.markGuestForDeletion(userHandle);
        } catch (RemoteException re) {
            Log.w(TAG, "Could not mark guest for deletion", re);
            return false;
        }
    }

    public void setUserEnabled(int userHandle) {
        try {
            this.mService.setUserEnabled(userHandle);
        } catch (RemoteException e) {
            Log.w(TAG, "Could not enable the profile", e);
        }
    }

    public int getUserCount() {
        List<UserInfo> users = getUsers();
        return users != null ? users.size() : 1;
    }

    public List<UserInfo> getUsers() {
        try {
            return this.mService.getUsers(false);
        } catch (RemoteException re) {
            Log.w(TAG, "Could not get user list", re);
            return null;
        }
    }

    public boolean canAddMoreUsers() {
        List<UserInfo> users = getUsers(true);
        int totalUserCount = users.size();
        int aliveUserCount = 0;
        for (int i = 0; i < totalUserCount; i++) {
            UserInfo user = (UserInfo) users.get(i);
            if (!(user.isGuest() || user.isKnoxWorkspace() || user.isBMode())) {
                aliveUserCount++;
            }
        }
        if (aliveUserCount < getMaxSupportedUsers()) {
            return true;
        }
        return false;
    }

    public boolean canAddMoreManagedProfiles() {
        try {
            return this.mService.canAddMoreManagedProfiles();
        } catch (RemoteException re) {
            Log.w(TAG, "Could not check if we can add more managed profiles", re);
            return false;
        }
    }

    public List<UserInfo> getProfiles(int userHandle) {
        try {
            return this.mService.getProfiles(userHandle, false);
        } catch (RemoteException re) {
            Log.w(TAG, "Could not get user list", re);
            return null;
        }
    }

    public List<UserInfo> getEnabledProfiles(int userHandle) {
        try {
            return this.mService.getProfiles(userHandle, true);
        } catch (RemoteException re) {
            Log.w(TAG, "Could not get user list", re);
            return null;
        }
    }

    public List<UserHandle> getUserProfiles() {
        ArrayList<UserHandle> profiles = new ArrayList();
        List<UserInfo> users = new ArrayList();
        try {
            for (UserInfo info : this.mService.getProfiles(UserHandle.myUserId(), true)) {
                profiles.add(new UserHandle(info.id));
            }
            return profiles;
        } catch (RemoteException re) {
            Log.w(TAG, "Could not get user list", re);
            return null;
        }
    }

    public int getCredentialOwnerProfile(int userHandle) {
        try {
            return this.mService.getCredentialOwnerProfile(userHandle);
        } catch (RemoteException re) {
            Log.w(TAG, "Could not get credential owner", re);
            return -1;
        }
    }

    public UserInfo getProfileParent(int userHandle) {
        try {
            return this.mService.getProfileParent(userHandle);
        } catch (RemoteException re) {
            Log.w(TAG, "Could not get profile parent", re);
            return null;
        }
    }

    public Drawable getBadgedIconForUser(Drawable icon, UserHandle user) {
        return this.mContext.getPackageManager().getUserBadgedIcon(icon, user);
    }

    public Drawable getBadgedDrawableForUser(Drawable badgedDrawable, UserHandle user, Rect badgeLocation, int badgeDensity) {
        return this.mContext.getPackageManager().getUserBadgedDrawableForDensity(badgedDrawable, user, badgeLocation, badgeDensity);
    }

    public CharSequence getBadgedLabelForUser(CharSequence label, UserHandle user) {
        return this.mContext.getPackageManager().getUserBadgedLabel(label, user);
    }

    public List<UserInfo> getUsers(boolean excludeDying) {
        try {
            return this.mService.getUsers(excludeDying);
        } catch (RemoteException re) {
            Log.w(TAG, "Could not get user list", re);
            return null;
        }
    }

    public boolean removeUser(int userHandle) {
        try {
            return this.mService.removeUser(userHandle);
        } catch (RemoteException re) {
            Log.w(TAG, "Could not remove user ", re);
            return false;
        }
    }

    public void setUserName(int userHandle, String name) {
        try {
            this.mService.setUserName(userHandle, name);
        } catch (RemoteException re) {
            Log.w(TAG, "Could not set the user name ", re);
        }
    }

    public void setUserIcon(int userHandle, Bitmap icon) {
        try {
            this.mService.setUserIcon(userHandle, icon);
        } catch (RemoteException re) {
            Log.w(TAG, "Could not set the user icon ", re);
        }
    }

    public Bitmap getUserIcon(int userHandle) {
        ParcelFileDescriptor fd;
        try {
            fd = this.mService.getUserIcon(userHandle);
            Bitmap decodeFileDescriptor;
            if (fd != null) {
                decodeFileDescriptor = BitmapFactory.decodeFileDescriptor(fd.getFileDescriptor());
                try {
                    fd.close();
                    return decodeFileDescriptor;
                } catch (IOException e) {
                    return decodeFileDescriptor;
                }
            }
            decodeFileDescriptor = this.mService.getUserIconForBitmap(userHandle);
            if (decodeFileDescriptor == null) {
                return UserIcons.convertToBitmap(UserIcons.getDefaultUserIcon(userHandle, false));
            }
            return decodeFileDescriptor;
        } catch (RemoteException re) {
            Log.w(TAG, "Could not get the user icon ", re);
            return null;
        } catch (Throwable th) {
            try {
                fd.close();
            } catch (IOException e2) {
            }
        }
    }

    public static int getMaxSupportedUsers() {
        if (Build.ID.startsWith("JVP") || ActivityManager.isLowRamDeviceStatic()) {
            return 1;
        }
        return SystemProperties.getInt("fw.max_users", SystemProperties.getInt("persist.sys.max_users", Resources.getSystem().getInteger(17694853)));
    }

    public boolean isUserSwitcherEnabled() {
        List<UserInfo> users = getUsers(true);
        if (users == null) {
            return false;
        }
        int switchableUserCount = 0;
        for (UserInfo user : users) {
            if (user.supportsSwitchTo()) {
                switchableUserCount++;
            }
        }
        boolean guestEnabled;
        if (Global.getInt(this.mContext.getContentResolver(), Global.GUEST_USER_ENABLED, 0) == 1) {
            guestEnabled = true;
        } else {
            guestEnabled = false;
        }
        if (switchableUserCount > 1 || guestEnabled) {
            return true;
        }
        return false;
    }

    public int getUserSerialNumber(int userHandle) {
        try {
            return this.mService.getUserSerialNumber(userHandle);
        } catch (RemoteException e) {
            Log.w(TAG, "Could not get serial number for user " + userHandle);
            return -1;
        }
    }

    public int getUserHandle(int userSerialNumber) {
        try {
            return this.mService.getUserHandle(userSerialNumber);
        } catch (RemoteException e) {
            Log.w(TAG, "Could not get userHandle for user " + userSerialNumber);
            return -1;
        }
    }

    public Bundle getApplicationRestrictions(String packageName) {
        try {
            return this.mService.getApplicationRestrictions(packageName);
        } catch (RemoteException e) {
            Log.w(TAG, "Could not get application restrictions for package " + packageName);
            return null;
        }
    }

    public Bundle getApplicationRestrictions(String packageName, UserHandle user) {
        try {
            return this.mService.getApplicationRestrictionsForUser(packageName, user.getIdentifier());
        } catch (RemoteException e) {
            Log.w(TAG, "Could not get application restrictions for user " + user.getIdentifier());
            return null;
        }
    }

    public void setApplicationRestrictions(String packageName, Bundle restrictions, UserHandle user) {
        try {
            this.mService.setApplicationRestrictions(packageName, restrictions, user.getIdentifier());
        } catch (RemoteException e) {
            Log.w(TAG, "Could not set application restrictions for user " + user.getIdentifier());
        }
    }

    public boolean setRestrictionsChallenge(String newPin) {
        return false;
    }

    public void removeRestrictions() {
        try {
            this.mService.removeRestrictions();
        } catch (RemoteException e) {
            Log.w(TAG, "Could not change restrictions pin");
        }
    }

    public void setDefaultGuestRestrictions(Bundle restrictions) {
        try {
            this.mService.setDefaultGuestRestrictions(restrictions);
        } catch (RemoteException e) {
            Log.w(TAG, "Could not set guest restrictions");
        }
    }

    public Bundle getDefaultGuestRestrictions() {
        try {
            return this.mService.getDefaultGuestRestrictions();
        } catch (RemoteException e) {
            Log.w(TAG, "Could not set guest restrictions");
            return new Bundle();
        }
    }

    public long getUserCreationTime(UserHandle userHandle) {
        try {
            return this.mService.getUserCreationTime(userHandle.getIdentifier());
        } catch (RemoteException re) {
            Log.w(TAG, "Could not get user creation time", re);
            return 0;
        }
    }
}
