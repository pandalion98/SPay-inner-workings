package android.app.admin;

import android.app.AlarmManager;
import android.app.admin.IDevicePolicyManager.Stub;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.dirEncryption.DirEncryptionManager;
import android.graphics.Bitmap;
import android.net.ProxyInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.os.Process;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.security.Credentials;
import android.util.Log;
import com.android.internal.widget.LockPatternUtils;
import com.android.org.conscrypt.TrustedCertificateStore;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.xmlpull.v1.XmlPullParserException;

public class DevicePolicyManager {
    public static final String ACTION_ADD_DEVICE_ADMIN = "android.app.action.ADD_DEVICE_ADMIN";
    public static final String ACTION_DEVICE_OWNER_CHANGED = "android.app.action.DEVICE_OWNER_CHANGED";
    public static final String ACTION_DEVICE_POLICY_MANAGER_PASSWORD_CHANGED = "android.app.action.DEVICE_POLICY_MANAGER_PASSWORD_CHANGED";
    public static final String ACTION_DEVICE_POLICY_MANAGER_STATE_CHANGED = "android.app.action.DEVICE_POLICY_MANAGER_STATE_CHANGED";
    public static final String ACTION_MANAGED_PROFILE_PROVISIONED = "android.app.action.MANAGED_PROFILE_PROVISIONED";
    public static final String ACTION_PROVISION_MANAGED_DEVICE = "android.app.action.PROVISION_MANAGED_DEVICE";
    public static final String ACTION_PROVISION_MANAGED_PROFILE = "android.app.action.PROVISION_MANAGED_PROFILE";
    public static final String ACTION_SET_NEW_PASSWORD = "android.app.action.SET_NEW_PASSWORD";
    public static final String ACTION_SET_PROFILE_OWNER = "android.app.action.SET_PROFILE_OWNER";
    public static final String ACTION_START_ENCRYPTION = "android.app.action.START_ENCRYPTION";
    public static final String ACTION_SYSTEM_UPDATE_POLICY_CHANGED = "android.app.action.SYSTEM_UPDATE_POLICY_CHANGED";
    public static final int ENCRYPTION_STATUS_ACTIVATING = 2;
    public static final int ENCRYPTION_STATUS_ACTIVE = 3;
    public static final int ENCRYPTION_STATUS_ACTIVE_DEFAULT_KEY = 4;
    public static final int ENCRYPTION_STATUS_DEFAULT_ENCRYPED_INTERNAL = 3;
    public static final int ENCRYPTION_STATUS_FAST_ENCRYPED_INTERNAL = 0;
    public static final int ENCRYPTION_STATUS_FULLY_ENCRYPED_BOTH = 1;
    public static final int ENCRYPTION_STATUS_FULLY_ENCRYPED_ERROR = -1;
    public static final int ENCRYPTION_STATUS_FULLY_ENCRYPED_INTERNAL = 2;
    public static final int ENCRYPTION_STATUS_INACTIVE = 1;
    public static final int ENCRYPTION_STATUS_UNSUPPORTED = 0;
    public static final String EXTRA_ADD_EXPLANATION = "android.app.extra.ADD_EXPLANATION";
    public static final String EXTRA_DEVICE_ADMIN = "android.app.extra.DEVICE_ADMIN";
    public static final String EXTRA_DPM_STATE_CHANGE_USERHANDLE = "userhandle";
    public static final String EXTRA_IS_BT_CHANGED = "isBTChanged";
    public static final String EXTRA_NOTIFY_FROM_LOCKSCREEN = "isNotiFromLockScreen";
    public static final String EXTRA_PROFILE_OWNER_NAME = "android.app.extra.PROFILE_OWNER_NAME";
    public static final String EXTRA_PROVISIONING_ACCOUNT_TO_MIGRATE = "android.app.extra.PROVISIONING_ACCOUNT_TO_MIGRATE";
    public static final String EXTRA_PROVISIONING_ADMIN_EXTRAS_BUNDLE = "android.app.extra.PROVISIONING_ADMIN_EXTRAS_BUNDLE";
    public static final String EXTRA_PROVISIONING_DEVICE_ADMIN_COMPONENT_NAME = "android.app.extra.PROVISIONING_DEVICE_ADMIN_COMPONENT_NAME";
    public static final String EXTRA_PROVISIONING_DEVICE_ADMIN_MINIMUM_VERSION_CODE = "android.app.extra.PROVISIONING_DEVICE_ADMIN_MINIMUM_VERSION_CODE";
    public static final String EXTRA_PROVISIONING_DEVICE_ADMIN_PACKAGE_CHECKSUM = "android.app.extra.PROVISIONING_DEVICE_ADMIN_PACKAGE_CHECKSUM";
    public static final String EXTRA_PROVISIONING_DEVICE_ADMIN_PACKAGE_DOWNLOAD_COOKIE_HEADER = "android.app.extra.PROVISIONING_DEVICE_ADMIN_PACKAGE_DOWNLOAD_COOKIE_HEADER";
    public static final String EXTRA_PROVISIONING_DEVICE_ADMIN_PACKAGE_DOWNLOAD_LOCATION = "android.app.extra.PROVISIONING_DEVICE_ADMIN_PACKAGE_DOWNLOAD_LOCATION";
    @Deprecated
    public static final String EXTRA_PROVISIONING_DEVICE_ADMIN_PACKAGE_NAME = "android.app.extra.PROVISIONING_DEVICE_ADMIN_PACKAGE_NAME";
    public static final String EXTRA_PROVISIONING_DEVICE_ADMIN_SIGNATURE_CHECKSUM = "android.app.extra.PROVISIONING_DEVICE_ADMIN_SIGNATURE_CHECKSUM";
    public static final String EXTRA_PROVISIONING_DEVICE_INITIALIZER_COMPONENT_NAME = "android.app.extra.PROVISIONING_DEVICE_INITIALIZER_COMPONENT_NAME";
    public static final String EXTRA_PROVISIONING_DEVICE_INITIALIZER_MINIMUM_VERSION_CODE = "android.app.extra.PROVISIONING_DEVICE_INITIALIZER_MINIMUM_VERSION_CODE";
    public static final String EXTRA_PROVISIONING_DEVICE_INITIALIZER_PACKAGE_CHECKSUM = "android.app.extra.PROVISIONING_DEVICE_INITIALIZER_PACKAGE_CHECKSUM";
    public static final String EXTRA_PROVISIONING_DEVICE_INITIALIZER_PACKAGE_DOWNLOAD_COOKIE_HEADER = "android.app.extra.PROVISIONING_DEVICE_INITIALIZER_PACKAGE_DOWNLOAD_COOKIE_HEADER";
    public static final String EXTRA_PROVISIONING_DEVICE_INITIALIZER_PACKAGE_DOWNLOAD_LOCATION = "android.app.extra.PROVISIONING_DEVICE_INITIALIZER_PACKAGE_DOWNLOAD_LOCATION";
    public static final String EXTRA_PROVISIONING_DEVICE_INITIALIZER_SIGNATURE_CHECKSUM = "android.app.extra.PROVISIONING_DEVICE_INITIALIZER_SIGNATURE_CHECKSUM";
    public static final String EXTRA_PROVISIONING_EMAIL_ADDRESS = "android.app.extra.PROVISIONING_EMAIL_ADDRESS";
    public static final String EXTRA_PROVISIONING_LEAVE_ALL_SYSTEM_APPS_ENABLED = "android.app.extra.PROVISIONING_LEAVE_ALL_SYSTEM_APPS_ENABLED";
    public static final String EXTRA_PROVISIONING_LOCALE = "android.app.extra.PROVISIONING_LOCALE";
    public static final String EXTRA_PROVISIONING_LOCAL_TIME = "android.app.extra.PROVISIONING_LOCAL_TIME";
    public static final String EXTRA_PROVISIONING_SKIP_ENCRYPTION = "android.app.extra.PROVISIONING_SKIP_ENCRYPTION";
    public static final String EXTRA_PROVISIONING_TIME_ZONE = "android.app.extra.PROVISIONING_TIME_ZONE";
    public static final String EXTRA_PROVISIONING_WIFI_HIDDEN = "android.app.extra.PROVISIONING_WIFI_HIDDEN";
    public static final String EXTRA_PROVISIONING_WIFI_PAC_URL = "android.app.extra.PROVISIONING_WIFI_PAC_URL";
    public static final String EXTRA_PROVISIONING_WIFI_PASSWORD = "android.app.extra.PROVISIONING_WIFI_PASSWORD";
    public static final String EXTRA_PROVISIONING_WIFI_PROXY_BYPASS = "android.app.extra.PROVISIONING_WIFI_PROXY_BYPASS";
    public static final String EXTRA_PROVISIONING_WIFI_PROXY_HOST = "android.app.extra.PROVISIONING_WIFI_PROXY_HOST";
    public static final String EXTRA_PROVISIONING_WIFI_PROXY_PORT = "android.app.extra.PROVISIONING_WIFI_PROXY_PORT";
    public static final String EXTRA_PROVISIONING_WIFI_SECURITY_TYPE = "android.app.extra.PROVISIONING_WIFI_SECURITY_TYPE";
    public static final String EXTRA_PROVISIONING_WIFI_SSID = "android.app.extra.PROVISIONING_WIFI_SSID";
    public static final int FLAG_MANAGED_CAN_ACCESS_PARENT = 2;
    public static final int FLAG_PARENT_CAN_ACCESS_MANAGED = 1;
    public static final int KEYGUARD_DISABLE_FEATURES_ALL = Integer.MAX_VALUE;
    public static final int KEYGUARD_DISABLE_FEATURES_NONE = 0;
    public static final int KEYGUARD_DISABLE_FINGERPRINT = 32;
    public static final int KEYGUARD_DISABLE_SECURE_CAMERA = 2;
    public static final int KEYGUARD_DISABLE_SECURE_NOTIFICATIONS = 4;
    public static final int KEYGUARD_DISABLE_TRUST_AGENTS = 16;
    public static final int KEYGUARD_DISABLE_UNREDACTED_NOTIFICATIONS = 8;
    public static final int KEYGUARD_DISABLE_WIDGETS_ALL = 1;
    public static final String MIME_TYPE_PROVISIONING_NFC = "application/com.android.managedprovisioning";
    public static final String MIME_TYPE_PROVISIONING_NFC_V2 = "application/com.android.managedprovisioning.v2";
    public static final int MINIMUM_BATTERY_LEVEL_FOR_ODE = 80;
    public static final int MINIMUM_ODE_PASSWORD_LENGTH = 4;
    public static final int MINIMUM_ODE_PASSWORD_QUALITY = 0;
    public static final int PASSWORD_QUALITY_ALPHABETIC = 262144;
    public static final int PASSWORD_QUALITY_ALPHANUMERIC = 327680;
    public static final int PASSWORD_QUALITY_AUTOLOCK = 37120;
    public static final int PASSWORD_QUALITY_BIOMETRIC_WEAK = 32768;
    public static final int PASSWORD_QUALITY_COMPLEX = 393216;
    public static final int PASSWORD_QUALITY_FINGERPRINT = 397312;
    public static final int PASSWORD_QUALITY_FINGERPRINT_OLD = 69632;
    public static final int PASSWORD_QUALITY_NUMERIC = 131072;
    public static final int PASSWORD_QUALITY_NUMERIC_COMPLEX = 196608;
    public static final int PASSWORD_QUALITY_SIGNATURE = 36864;
    public static final int PASSWORD_QUALITY_SMARTCARDNUMERIC = 458752;
    public static final int PASSWORD_QUALITY_SMARTUNLOCK = 589824;
    public static final int PASSWORD_QUALITY_SOMETHING = 65536;
    public static final int PASSWORD_QUALITY_UNIVERSAL_LOCK = 4096;
    public static final int PASSWORD_QUALITY_UNSPECIFIED = 0;
    public static final int PERMISSION_GRANT_STATE_DEFAULT = 0;
    public static final int PERMISSION_GRANT_STATE_DENIED = 2;
    public static final int PERMISSION_GRANT_STATE_GRANTED = 1;
    public static final int PERMISSION_POLICY_AUTO_DENY = 2;
    public static final int PERMISSION_POLICY_AUTO_GRANT = 1;
    public static final int PERMISSION_POLICY_PROMPT = 0;
    public static final int RESET_PASSWORD_DO_NOT_ASK_CREDENTIALS_ON_BOOT = 2;
    public static final int RESET_PASSWORD_REQUIRE_ENTRY = 1;
    private static String TAG = "DevicePolicyManager";
    public static final int WIPE_EXTERNAL_STORAGE = 1;
    public static final int WIPE_RESET_PROTECTION_DATA = 2;
    private final Context mContext;
    private final IDevicePolicyManager mService = Stub.asInterface(ServiceManager.getService(Context.DEVICE_POLICY_SERVICE));

    private DevicePolicyManager(Context context, Handler handler) {
        this.mContext = context;
    }

    public static DevicePolicyManager create(Context context, Handler handler) {
        DevicePolicyManager me = new DevicePolicyManager(context, handler);
        return me.mService != null ? me : null;
    }

    public boolean isAdminActive(ComponentName admin) {
        return isAdminActiveAsUser(admin, UserHandle.myUserId());
    }

    public boolean isAdminActiveAsUser(ComponentName admin, int userId) {
        if (this.mService != null) {
            try {
                return this.mService.isAdminActive(admin, userId);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return false;
    }

    public boolean isRemovingAdmin(ComponentName admin, int userId) {
        if (this.mService != null) {
            try {
                return this.mService.isRemovingAdmin(admin, userId);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return false;
    }

    public List<ComponentName> getActiveAdmins() {
        return getActiveAdminsAsUser(UserHandle.myUserId());
    }

    public List<ComponentName> getActiveAdminsAsUser(int userId) {
        if (this.mService != null) {
            try {
                return this.mService.getActiveAdmins(userId);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return null;
    }

    public boolean packageHasActiveAdmins(String packageName) {
        if (this.mService != null) {
            try {
                return this.mService.packageHasActiveAdmins(packageName, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return false;
    }

    public void removeActiveAdmin(ComponentName admin) {
        if (this.mService != null) {
            try {
                this.mService.removeActiveAdmin(admin, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public boolean hasGrantedPolicy(ComponentName admin, int usesPolicy) {
        if (this.mService != null) {
            try {
                return this.mService.hasGrantedPolicy(admin, usesPolicy, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return false;
    }

    public void setPasswordQuality(ComponentName admin, int quality) {
        if (this.mService != null) {
            try {
                this.mService.setPasswordQuality(admin, quality);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public int getPasswordQuality(ComponentName admin) {
        return getPasswordQuality(admin, UserHandle.myUserId());
    }

    public int getPasswordQuality(ComponentName admin, int userHandle) {
        if (this.mService != null) {
            try {
                return this.mService.getPasswordQuality(admin, userHandle);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return 0;
    }

    public void setPasswordMinimumLength(ComponentName admin, int length) {
        if (this.mService != null) {
            try {
                this.mService.setPasswordMinimumLength(admin, length);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public int getPasswordMinimumLength(ComponentName admin) {
        return getPasswordMinimumLength(admin, UserHandle.myUserId());
    }

    public int getPasswordMinimumLength(ComponentName admin, int userHandle) {
        if (this.mService != null) {
            try {
                return this.mService.getPasswordMinimumLength(admin, userHandle);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return 0;
    }

    public void setPasswordMinimumUpperCase(ComponentName admin, int length) {
        if (this.mService != null) {
            try {
                this.mService.setPasswordMinimumUpperCase(admin, length);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public int getPasswordMinimumUpperCase(ComponentName admin) {
        return getPasswordMinimumUpperCase(admin, UserHandle.myUserId());
    }

    public int getPasswordMinimumUpperCase(ComponentName admin, int userHandle) {
        if (this.mService != null) {
            try {
                return this.mService.getPasswordMinimumUpperCase(admin, userHandle);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return 0;
    }

    public void setPasswordMinimumLowerCase(ComponentName admin, int length) {
        if (this.mService != null) {
            try {
                this.mService.setPasswordMinimumLowerCase(admin, length);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public int getPasswordMinimumLowerCase(ComponentName admin) {
        return getPasswordMinimumLowerCase(admin, UserHandle.myUserId());
    }

    public int getPasswordMinimumLowerCase(ComponentName admin, int userHandle) {
        if (this.mService != null) {
            try {
                return this.mService.getPasswordMinimumLowerCase(admin, userHandle);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return 0;
    }

    public void setPasswordMinimumLetters(ComponentName admin, int length) {
        if (this.mService != null) {
            try {
                this.mService.setPasswordMinimumLetters(admin, length);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public int getPasswordMinimumLetters(ComponentName admin) {
        return getPasswordMinimumLetters(admin, UserHandle.myUserId());
    }

    public int getPasswordMinimumLetters(ComponentName admin, int userHandle) {
        if (this.mService != null) {
            try {
                return this.mService.getPasswordMinimumLetters(admin, userHandle);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return 0;
    }

    public void setPasswordMinimumNumeric(ComponentName admin, int length) {
        if (this.mService != null) {
            try {
                this.mService.setPasswordMinimumNumeric(admin, length);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public int getPasswordMinimumNumeric(ComponentName admin) {
        return getPasswordMinimumNumeric(admin, UserHandle.myUserId());
    }

    public int getPasswordMinimumNumeric(ComponentName admin, int userHandle) {
        if (this.mService != null) {
            try {
                return this.mService.getPasswordMinimumNumeric(admin, userHandle);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return 0;
    }

    public void setPasswordMinimumSymbols(ComponentName admin, int length) {
        if (this.mService != null) {
            try {
                this.mService.setPasswordMinimumSymbols(admin, length);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public int getPasswordMinimumSymbols(ComponentName admin) {
        return getPasswordMinimumSymbols(admin, UserHandle.myUserId());
    }

    public int getPasswordMinimumSymbols(ComponentName admin, int userHandle) {
        if (this.mService != null) {
            try {
                return this.mService.getPasswordMinimumSymbols(admin, userHandle);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return 0;
    }

    public void setPasswordMinimumNonLetter(ComponentName admin, int length) {
        if (this.mService != null) {
            try {
                this.mService.setPasswordMinimumNonLetter(admin, length);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public int getPasswordMinimumNonLetter(ComponentName admin) {
        return getPasswordMinimumNonLetter(admin, UserHandle.myUserId());
    }

    public int getPasswordMinimumNonLetter(ComponentName admin, int userHandle) {
        if (this.mService != null) {
            try {
                return this.mService.getPasswordMinimumNonLetter(admin, userHandle);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return 0;
    }

    public void setPasswordHistoryLength(ComponentName admin, int length) {
        if (this.mService != null) {
            try {
                this.mService.setPasswordHistoryLength(admin, length);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public void setPasswordExpirationTimeout(ComponentName admin, long timeout) {
        if (this.mService != null) {
            try {
                this.mService.setPasswordExpirationTimeout(admin, timeout);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public long getPasswordExpirationTimeout(ComponentName admin) {
        if (this.mService != null) {
            try {
                return this.mService.getPasswordExpirationTimeout(admin, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return 0;
    }

    public long getPasswordExpiration(ComponentName admin) {
        if (this.mService != null) {
            try {
                return this.mService.getPasswordExpiration(admin, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return 0;
    }

    public int getPasswordHistoryLength(ComponentName admin) {
        return getPasswordHistoryLength(admin, UserHandle.myUserId());
    }

    public int getPasswordHistoryLength(ComponentName admin, int userHandle) {
        if (this.mService != null) {
            try {
                return this.mService.getPasswordHistoryLength(admin, userHandle);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return 0;
    }

    public int getPasswordMaximumLength(int quality) {
        return 16;
    }

    public boolean isActivePasswordSufficient() {
        if (this.mService != null) {
            try {
                return this.mService.isActivePasswordSufficient(UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return false;
    }

    public int getCurrentFailedPasswordAttempts() {
        if (this.mService != null) {
            try {
                return this.mService.getCurrentFailedPasswordAttempts(UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return -1;
    }

    public boolean getDoNotAskCredentialsOnBoot() {
        if (this.mService != null) {
            try {
                return this.mService.getDoNotAskCredentialsOnBoot();
            } catch (RemoteException e) {
                Log.w(TAG, "Failed to call getDoNotAskCredentialsOnBoot()", e);
            }
        }
        return false;
    }

    public void setMaximumFailedPasswordsForWipe(ComponentName admin, int num) {
        if (this.mService != null) {
            try {
                this.mService.setMaximumFailedPasswordsForWipe(admin, num);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public int getMaximumFailedPasswordsForWipe(ComponentName admin) {
        return getMaximumFailedPasswordsForWipe(admin, UserHandle.myUserId());
    }

    public int getMaximumFailedPasswordsForWipe(ComponentName admin, int userHandle) {
        if (this.mService != null) {
            try {
                return this.mService.getMaximumFailedPasswordsForWipe(admin, userHandle);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return 0;
    }

    public int getProfileWithMinimumFailedPasswordsForWipe(int userHandle) {
        if (this.mService != null) {
            try {
                return this.mService.getProfileWithMinimumFailedPasswordsForWipe(userHandle);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return -10000;
    }

    public boolean resetPassword(String password, int flags) {
        if (this.mService != null) {
            try {
                return this.mService.resetPassword(password, flags);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return false;
    }

    public void setMaximumTimeToLock(ComponentName admin, long timeMs) {
        if (this.mService != null) {
            try {
                this.mService.setMaximumTimeToLock(admin, timeMs);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public long getMaximumTimeToLock(ComponentName admin) {
        return getMaximumTimeToLock(admin, UserHandle.myUserId());
    }

    public long getMaximumTimeToLock(ComponentName admin, int userHandle) {
        if (this.mService != null) {
            try {
                return this.mService.getMaximumTimeToLock(admin, userHandle);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return 0;
    }

    public void lockNow() {
        if (this.mService != null) {
            try {
                this.mService.lockNow();
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public void wipeData(int flags) {
        if (this.mService != null) {
            try {
                this.mService.wipeData(flags, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public ComponentName setGlobalProxy(ComponentName admin, Proxy proxySpec, List<String> exclusionList) {
        if (proxySpec == null) {
            throw new NullPointerException();
        }
        if (this.mService != null) {
            try {
                String hostSpec;
                String exclSpec;
                if (proxySpec.equals(Proxy.NO_PROXY)) {
                    hostSpec = null;
                    exclSpec = null;
                } else if (proxySpec.type().equals(Type.HTTP)) {
                    InetSocketAddress sa = (InetSocketAddress) proxySpec.address();
                    String hostName = sa.getHostName();
                    int port = sa.getPort();
                    hostSpec = hostName + ":" + Integer.toString(port);
                    if (exclusionList == null) {
                        exclSpec = ProxyInfo.LOCAL_EXCL_LIST;
                    } else {
                        StringBuilder listBuilder = new StringBuilder();
                        boolean firstDomain = true;
                        for (String exclDomain : exclusionList) {
                            if (firstDomain) {
                                firstDomain = false;
                            } else {
                                listBuilder = listBuilder.append(",");
                            }
                            listBuilder = listBuilder.append(exclDomain.trim());
                        }
                        exclSpec = listBuilder.toString();
                    }
                    if (android.net.Proxy.validate(hostName, Integer.toString(port), exclSpec) != 0) {
                        throw new IllegalArgumentException();
                    }
                } else {
                    throw new IllegalArgumentException();
                }
                return this.mService.setGlobalProxy(admin, hostSpec, exclSpec);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return null;
    }

    public void setRecommendedGlobalProxy(ComponentName admin, ProxyInfo proxyInfo) {
        if (this.mService != null) {
            try {
                this.mService.setRecommendedGlobalProxy(admin, proxyInfo);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public ComponentName getGlobalProxyAdmin() {
        if (this.mService != null) {
            try {
                return this.mService.getGlobalProxyAdmin(UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return null;
    }

    public int setStorageEncryption(ComponentName admin, boolean encrypt) {
        if (this.mService != null) {
            try {
                return this.mService.setStorageEncryption(admin, encrypt);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return 0;
    }

    public boolean getStorageEncryption(ComponentName admin) {
        if (this.mService != null) {
            try {
                return this.mService.getStorageEncryption(admin, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return false;
    }

    public int getStorageEncryptionStatus() {
        return getStorageEncryptionStatus(UserHandle.myUserId());
    }

    public int getStorageEncryptionStatus(int userHandle) {
        if (this.mService != null) {
            try {
                return this.mService.getStorageEncryptionStatus(userHandle);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return 0;
    }

    public int getExternalSdCardEncryptionStatus() {
        DirEncryptionManager dem = new DirEncryptionManager(this.mContext);
        if (!dem.isEncryptionSupported()) {
            return 0;
        }
        if (dem.isStorageCardEncryptionPoliciesApplied()) {
            return 3;
        }
        return 1;
    }

    public boolean satisfyFIPSPassword(int userHandle) {
        if (this.mService != null) {
            try {
                return this.mService.satisfyFIPSPassword(userHandle);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return false;
    }

    public boolean getSamsungEncryptionStatus(ComponentName admin) {
        if (this.mService != null) {
            try {
                return this.mService.getSamsungEncryptionStatus(admin, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return false;
    }

    public boolean getSamsungSDcardEncryptionStatus(ComponentName admin) {
        if (this.mService != null) {
            try {
                return this.mService.getSamsungSDcardEncryptionStatus(admin, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return false;
    }

    public int getSamsungEncryptionStatusForCC(ComponentName admin) {
        if (this.mService != null) {
            try {
                return this.mService.getSamsungEncryptionStatusForCC(admin, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return -1;
    }

    public boolean isSupportTrustZoneForODE(ComponentName admin) {
        if (this.mService != null) {
            try {
                return this.mService.isSupportTrustZoneForODE(admin, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return false;
    }

    public boolean isSupportTrustedBootForODE(ComponentName admin) {
        if (this.mService != null) {
            try {
                return this.mService.isSupportTrustedBootForODE(admin, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return false;
    }

    public boolean setPropertyIntoFooter(ComponentName admin, String name, String value) {
        if (this.mService != null) {
            try {
                return this.mService.setPropertyIntoFooter(admin, name, value, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return false;
    }

    public String getPropertyFromFooter(ComponentName admin, String name) {
        if (this.mService != null) {
            try {
                return this.mService.getPropertyFromFooter(admin, name, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return null;
    }

    public boolean isSupportFingerprintForODE(ComponentName admin) {
        if (this.mService != null) {
            try {
                return this.mService.isSupportFingerprintForODE(admin, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return false;
    }

    public void setNeedToGetAlternativePasswdForODE(ComponentName admin, boolean value) {
        if (this.mService != null) {
            try {
                this.mService.setNeedToGetAlternativePasswdForODE(admin, value, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public boolean getNeedToGetAlternativePasswdForODE(ComponentName admin) {
        if (this.mService != null) {
            try {
                return this.mService.getNeedToGetAlternativePasswdForODE(admin, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return false;
    }

    public boolean isEncryptedWithForceEncrypt(ComponentName admin) {
        if (this.mService != null) {
            try {
                return this.mService.isEncryptedWithForceEncrypt(admin, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return false;
    }

    public boolean installCaCert(ComponentName admin, byte[] certBuffer) {
        if (this.mService != null) {
            try {
                return this.mService.installCaCert(admin, certBuffer);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return false;
    }

    public void uninstallCaCert(ComponentName admin, byte[] certBuffer) {
        if (this.mService != null) {
            try {
                String alias = getCaCertAlias(certBuffer);
                this.mService.uninstallCaCerts(admin, new String[]{alias});
            } catch (CertificateException e) {
                Log.w(TAG, "Unable to parse certificate", e);
            } catch (RemoteException e2) {
                Log.w(TAG, "Failed talking with device policy service", e2);
            }
        }
    }

    public List<byte[]> getInstalledCaCerts(ComponentName admin) {
        List<byte[]> certs = new ArrayList();
        if (this.mService != null) {
            try {
                this.mService.enforceCanManageCaCerts(admin);
                TrustedCertificateStore certStore = new TrustedCertificateStore();
                for (String alias : certStore.userAliases()) {
                    try {
                        certs.add(certStore.getCertificate(alias).getEncoded());
                    } catch (CertificateException ce) {
                        Log.w(TAG, "Could not encode certificate: " + alias, ce);
                    }
                }
            } catch (RemoteException re) {
                Log.w(TAG, "Failed talking with device policy service", re);
            }
        }
        return certs;
    }

    public void uninstallAllUserCaCerts(ComponentName admin) {
        if (this.mService != null) {
            try {
                this.mService.uninstallCaCerts(admin, (String[]) new TrustedCertificateStore().userAliases().toArray(new String[0]));
            } catch (RemoteException re) {
                Log.w(TAG, "Failed talking with device policy service", re);
            }
        }
    }

    public boolean hasCaCertInstalled(ComponentName admin, byte[] certBuffer) {
        if (this.mService == null) {
            return false;
        }
        try {
            this.mService.enforceCanManageCaCerts(admin);
            if (getCaCertAlias(certBuffer) != null) {
                return true;
            }
            return false;
        } catch (RemoteException re) {
            Log.w(TAG, "Failed talking with device policy service", re);
            return false;
        } catch (CertificateException ce) {
            Log.w(TAG, "Could not parse certificate", ce);
            return false;
        }
    }

    public boolean installKeyPair(ComponentName admin, PrivateKey privKey, Certificate cert, String alias) {
        GeneralSecurityException e;
        Exception e2;
        try {
            byte[] pemCert = Credentials.convertToPem(new Certificate[]{cert});
            return this.mService.installKeyPair(admin, ((PKCS8EncodedKeySpec) KeyFactory.getInstance(privKey.getAlgorithm()).getKeySpec(privKey, PKCS8EncodedKeySpec.class)).getEncoded(), pemCert, alias);
        } catch (RemoteException e3) {
            Log.w(TAG, "Failed talking with device policy service", e3);
            return false;
        } catch (NoSuchAlgorithmException e4) {
            e = e4;
            Log.w(TAG, "Failed to obtain private key material", e);
            return false;
        } catch (InvalidKeySpecException e5) {
            e = e5;
            Log.w(TAG, "Failed to obtain private key material", e);
            return false;
        } catch (CertificateException e6) {
            e2 = e6;
            Log.w(TAG, "Could not pem-encode certificate", e2);
            return false;
        } catch (IOException e7) {
            e2 = e7;
            Log.w(TAG, "Could not pem-encode certificate", e2);
            return false;
        }
    }

    private static String getCaCertAlias(byte[] certBuffer) throws CertificateException {
        return new TrustedCertificateStore().getCertificateAlias((X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(certBuffer)));
    }

    public void setCertInstallerPackage(ComponentName admin, String installerPackage) throws SecurityException {
        if (this.mService != null) {
            try {
                this.mService.setCertInstallerPackage(admin, installerPackage);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public String getCertInstallerPackage(ComponentName admin) throws SecurityException {
        if (this.mService != null) {
            try {
                return this.mService.getCertInstallerPackage(admin);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return null;
    }

    public void setCameraDisabled(ComponentName admin, boolean disabled) {
        if (this.mService != null) {
            try {
                this.mService.setCameraDisabled(admin, disabled);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public boolean getCameraDisabled(ComponentName admin) {
        return getCameraDisabled(admin, UserHandle.myUserId());
    }

    public boolean getCameraDisabled(ComponentName admin, int userHandle) {
        if (this.mService != null) {
            try {
                return this.mService.getCameraDisabled(admin, userHandle);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return false;
    }

    public void setScreenCaptureDisabled(ComponentName admin, boolean disabled) {
        if (this.mService != null) {
            try {
                this.mService.setScreenCaptureDisabled(admin, disabled);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public boolean getScreenCaptureDisabled(ComponentName admin) {
        return getScreenCaptureDisabled(admin, UserHandle.myUserId());
    }

    public boolean getScreenCaptureDisabled(ComponentName admin, int userHandle) {
        if (this.mService != null) {
            try {
                return this.mService.getScreenCaptureDisabled(admin, userHandle);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return false;
    }

    public void setAutoTimeRequired(ComponentName admin, boolean required) {
        if (this.mService != null) {
            try {
                this.mService.setAutoTimeRequired(admin, required);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public boolean getAutoTimeRequired() {
        if (this.mService != null) {
            try {
                return this.mService.getAutoTimeRequired();
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return false;
    }

    public void setKeyguardDisabledFeatures(ComponentName admin, int which) {
        if (this.mService != null) {
            try {
                this.mService.setKeyguardDisabledFeatures(admin, which);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public int getKeyguardDisabledFeatures(ComponentName admin) {
        return getKeyguardDisabledFeatures(admin, UserHandle.myUserId());
    }

    public int getKeyguardDisabledFeatures(ComponentName admin, int userHandle) {
        if (this.mService != null) {
            try {
                return this.mService.getKeyguardDisabledFeatures(admin, userHandle);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return 0;
    }

    public void setActiveAdmin(ComponentName policyReceiver, boolean refreshing, int userHandle) {
        if (this.mService != null) {
            try {
                this.mService.setActiveAdmin(policyReceiver, refreshing, userHandle);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public void setActiveAdmin(ComponentName policyReceiver, boolean refreshing) {
        setActiveAdmin(policyReceiver, refreshing, UserHandle.myUserId());
    }

    public DeviceAdminInfo getAdminInfo(ComponentName cn) {
        try {
            ActivityInfo ai = this.mContext.getPackageManager().getReceiverInfo(cn, 128);
            ResolveInfo ri = new ResolveInfo();
            ri.activityInfo = ai;
            try {
                return new DeviceAdminInfo(this.mContext, ri);
            } catch (XmlPullParserException e) {
                Log.w(TAG, "Unable to parse device policy " + cn, e);
                return null;
            } catch (IOException e2) {
                Log.w(TAG, "Unable to parse device policy " + cn, e2);
                return null;
            }
        } catch (NameNotFoundException e3) {
            Log.w(TAG, "Unable to retrieve device policy " + cn, e3);
            return null;
        }
    }

    public void getRemoveWarning(ComponentName admin, RemoteCallback result) {
        if (this.mService != null) {
            try {
                this.mService.getRemoveWarning(admin, result, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public void copyAlternativeToActivePasswordState(int userHandle) {
        if (this.mService != null) {
            try {
                this.mService.copyAlternativeToActivePasswordState(userHandle);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public void setAlternativePasswordState(int length, int letters, int uppercase, int lowercase, int numbers, int symbols, int nonletter, int userHandle) {
        if (this.mService != null) {
            try {
                this.mService.setAlternativePasswordState(length, letters, uppercase, lowercase, numbers, symbols, nonletter, userHandle);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public void setActivePasswordState(int quality, int length, int letters, int uppercase, int lowercase, int numbers, int symbols, int nonletter, int userHandle) {
        if (this.mService != null) {
            try {
                this.mService.setActivePasswordState(quality, length, letters, uppercase, lowercase, numbers, symbols, nonletter, userHandle);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public void setActivePasswordStateForEAS(int quality, int length, int letters, int uppercase, int lowercase, int numbers, int symbols, int nonletter, int userHandle) {
        if (this.mService != null) {
            try {
                this.mService.setActivePasswordStateForEAS(quality, length, letters, uppercase, lowercase, numbers, symbols, nonletter, userHandle);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public void reportFailedPasswordAttempt(int userHandle) {
        if (this.mService != null) {
            try {
                this.mService.reportFailedPasswordAttempt(userHandle);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public void reportSuccessfulPasswordAttempt(int userHandle) {
        if (this.mService != null) {
            try {
                this.mService.reportSuccessfulPasswordAttempt(userHandle);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public boolean setDeviceOwner(String packageName) throws IllegalArgumentException, IllegalStateException {
        return setDeviceOwner(packageName, null);
    }

    public boolean setDeviceOwner(String packageName, String ownerName) throws IllegalArgumentException, IllegalStateException {
        if (this.mService != null) {
            try {
                return this.mService.setDeviceOwner(packageName, ownerName);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed to set device owner");
            }
        }
        return false;
    }

    public boolean isDeviceOwnerApp(String packageName) {
        if (this.mService != null) {
            try {
                return this.mService.isDeviceOwner(packageName);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed to check device owner");
            }
        }
        return false;
    }

    public boolean isDeviceOwner(String packageName) {
        return isDeviceOwnerApp(packageName);
    }

    public void clearDeviceOwnerApp(String packageName) {
        if (this.mService != null) {
            try {
                this.mService.clearDeviceOwner(packageName);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed to clear device owner");
            }
        }
    }

    public String getDeviceOwner() {
        if (this.mService != null) {
            try {
                return this.mService.getDeviceOwner();
            } catch (RemoteException e) {
                Log.w(TAG, "Failed to get device owner");
            }
        }
        return null;
    }

    public void setPasswordRecoverable(ComponentName admin, boolean value) {
        if (this.mService != null) {
            try {
                this.mService.setPasswordRecoverable(admin, value, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public boolean getPasswordRecoverable(ComponentName admin) {
        return getPasswordRecoverable(admin, UserHandle.myUserId());
    }

    public boolean getPasswordRecoverable(ComponentName admin, int userHandle) {
        if (this.mService != null) {
            try {
                return this.mService.getPasswordRecoverable(admin, userHandle);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return false;
    }

    public void setAllowStorageCard(ComponentName admin, boolean value) {
        if (this.mService != null) {
            try {
                this.mService.setAllowStorageCard(admin, value, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public boolean getAllowStorageCard(ComponentName admin) {
        if (this.mService != null) {
            try {
                return this.mService.getAllowStorageCard(admin, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return true;
    }

    public void setSimplePasswordEnabled(ComponentName admin, boolean value) {
        if (this.mService != null) {
            try {
                this.mService.setSimplePasswordEnabled(admin, value, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public boolean getSimplePasswordEnabled(ComponentName admin) {
        if (this.mService != null) {
            try {
                return this.mService.getSimplePasswordEnabled(admin, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return true;
    }

    public void setAllowWifi(ComponentName admin, boolean value) {
        if (this.mService != null) {
            try {
                this.mService.setAllowWifi(admin, value, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public boolean getAllowWifi(ComponentName admin) {
        int userId = UserHandle.myUserId();
        if (this.mService != null) {
            try {
                return this.mService.getAllowWifi(admin, userId);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return true;
    }

    public void setAllowTextMessaging(ComponentName admin, boolean value) {
        if (this.mService != null) {
            try {
                this.mService.setAllowTextMessaging(admin, value, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public boolean getAllowTextMessaging(ComponentName admin) {
        if (this.mService != null) {
            try {
                return this.mService.getAllowTextMessaging(admin, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return true;
    }

    public void setAllowPOPIMAPEmail(ComponentName admin, boolean value) {
        if (this.mService != null) {
            try {
                this.mService.setAllowPOPIMAPEmail(admin, value, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public boolean getAllowPOPIMAPEmail(ComponentName admin) {
        if (this.mService != null) {
            try {
                return this.mService.getAllowPOPIMAPEmail(admin, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return true;
    }

    public void setAllowBrowser(ComponentName admin, boolean value) {
        if (this.mService != null) {
            try {
                this.mService.setAllowBrowser(admin, value, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public boolean getAllowBrowser(ComponentName admin) {
        if (this.mService != null) {
            try {
                return this.mService.getAllowBrowser(admin, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return true;
    }

    public void setAllowInternetSharing(ComponentName admin, boolean value) {
        if (this.mService != null) {
            try {
                this.mService.setAllowInternetSharing(admin, value, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public boolean getAllowInternetSharing(ComponentName admin) {
        if (this.mService != null) {
            try {
                return this.mService.getAllowInternetSharing(admin, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return true;
    }

    public void setAllowBluetoothMode(ComponentName admin, int size) {
        if (this.mService != null) {
            try {
                this.mService.setAllowBluetoothMode(admin, size, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public int getAllowBluetoothMode(ComponentName admin) {
        int userId = UserHandle.myUserId();
        if (this.mService != null) {
            try {
                return this.mService.getAllowBluetoothMode(admin, userId);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return 2;
    }

    public void setAllowDesktopSync(ComponentName admin, boolean value) {
        if (this.mService != null) {
            try {
                this.mService.setAllowDesktopSync(admin, value, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public boolean getAllowDesktopSync(ComponentName admin) {
        if (this.mService != null) {
            try {
                return this.mService.getAllowDesktopSync(admin, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return true;
    }

    public void setAllowIrDA(ComponentName admin, boolean value) {
        if (this.mService != null) {
            try {
                this.mService.setAllowIrDA(admin, value, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public boolean getAllowIrDA(ComponentName admin) {
        if (this.mService != null) {
            try {
                return this.mService.getAllowIrDA(admin, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return true;
    }

    public boolean checkPassword(ComponentName admin, String password) {
        if (this.mService != null) {
            try {
                return this.mService.checkPassword(admin, password, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return false;
    }

    public void setRequireStorageCardEncryption(ComponentName admin, boolean value) {
        if (this.mService != null) {
            try {
                this.mService.setRequireStorageCardEncryption(admin, value, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public boolean getRequireStorageCardEncryption(ComponentName admin) {
        if (this.mService != null) {
            try {
                return this.mService.getRequireStorageCardEncryption(admin, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return false;
    }

    public void recoverPassword() {
        recoverPassword(UserHandle.myUserId());
    }

    public void recoverPassword(int userHandle) {
        if (this.mService != null) {
            try {
                this.mService.recoverPassword(userHandle);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public void setRecoveryPasswordState(ComponentName who, boolean value) {
        int userId = UserHandle.myUserId();
        if (this.mService != null) {
            try {
                this.mService.setRecoveryPasswordState(who, value, userId);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public void notifyChanges(ComponentName admin, boolean notifyChanges) {
        if (this.mService != null) {
            try {
                this.mService.notifyChanges(admin, notifyChanges, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public void setAllowCamera(ComponentName admin, boolean value) {
        setCameraDisabled(admin, !value);
    }

    public boolean getAllowCamera(ComponentName admin) {
        return !getCameraDisabled(admin, UserHandle.myUserId());
    }

    public boolean getCameraDisabledWithUID(ComponentName admin, int uid) {
        int userHandle = UserHandle.getUserId(uid);
        if (this.mService != null) {
            try {
                return this.mService.getCameraDisabled(admin, userHandle);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return false;
    }

    public void setAdminPermissions(ComponentName policyReceiver) {
        if (this.mService != null) {
            try {
                this.mService.setAdminPermissions(policyReceiver, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public void setAllowAppListThirdParty(ComponentName admin, String value) {
        if (this.mService != null) {
            try {
                this.mService.setAllowAppListThirdParty(admin, value, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public String getAllowAppListThirdParty(ComponentName admin) {
        if (this.mService != null) {
            try {
                return this.mService.getAllowAppListThirdParty(admin, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return ProxyInfo.LOCAL_EXCL_LIST.intern();
    }

    public String getRecoveryPassword() {
        return new LockPatternUtils(this.mContext).getRecoveryPassword();
    }

    public void removeRecoveryPasswords() {
        new LockPatternUtils(this.mContext).removeRecoveryPasswords();
    }

    public void setBlockListInRom(ComponentName admin, String value) {
        if (this.mService != null) {
            try {
                this.mService.setBlockListInRom(admin, value, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public String getBlockListInRom(ComponentName admin) {
        if (this.mService != null) {
            try {
                return this.mService.getBlockListInRom(admin, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return ProxyInfo.LOCAL_EXCL_LIST.intern();
    }

    public void setAllowUnsignedApp(ComponentName cp, boolean flags) {
        if (this.mService != null) {
            try {
                this.mService.setAllowUnsignedApp(cp, flags, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public boolean getAllowUnsignedApp(ComponentName admin) {
        if (this.mService != null) {
            try {
                return this.mService.getAllowUnsignedApp(admin, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return true;
    }

    public void setAllowUnsignedInstallationPkg(ComponentName cp, boolean flags) {
        if (this.mService != null) {
            try {
                this.mService.setAllowUnsignedInstallationPkg(cp, flags, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public boolean getAllowUnsignedInstallationPkg(ComponentName admin) {
        if (this.mService != null) {
            try {
                return this.mService.getAllowUnsignedInstallationPkg(admin, UserHandle.myUserId());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return true;
    }

    public String getDeviceOwnerName() {
        if (this.mService != null) {
            try {
                return this.mService.getDeviceOwnerName();
            } catch (RemoteException e) {
                Log.w(TAG, "Failed to get device owner");
            }
        }
        return null;
    }

    public boolean setDeviceInitializer(ComponentName admin, ComponentName initializer) throws IllegalArgumentException, IllegalStateException {
        if (this.mService != null) {
            try {
                return this.mService.setDeviceInitializer(admin, initializer);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed to set device initializer");
            }
        }
        return false;
    }

    public boolean isDeviceInitializerApp(String packageName) {
        if (this.mService != null) {
            try {
                return this.mService.isDeviceInitializer(packageName);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed to check device initializer");
            }
        }
        return false;
    }

    public void clearDeviceInitializerApp(ComponentName admin) {
        if (this.mService != null) {
            try {
                this.mService.clearDeviceInitializer(admin);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed to clear device initializer");
            }
        }
    }

    public String getDeviceInitializerApp() {
        if (this.mService != null) {
            try {
                return this.mService.getDeviceInitializer();
            } catch (RemoteException e) {
                Log.w(TAG, "Failed to get device initializer");
            }
        }
        return null;
    }

    public ComponentName getDeviceInitializerComponent() {
        if (this.mService != null) {
            try {
                return this.mService.getDeviceInitializerComponent();
            } catch (RemoteException e) {
                Log.w(TAG, "Failed to get device initializer");
            }
        }
        return null;
    }

    public boolean setUserEnabled(ComponentName admin) {
        if (this.mService != null) {
            try {
                return this.mService.setUserEnabled(admin);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return false;
    }

    public boolean setActiveProfileOwner(ComponentName admin, @Deprecated String ownerName) throws IllegalArgumentException {
        boolean z = false;
        if (this.mService != null) {
            try {
                int myUserId = UserHandle.myUserId();
                this.mService.setActiveAdmin(admin, false, myUserId);
                z = this.mService.setProfileOwner(admin, ownerName, myUserId);
            } catch (RemoteException re) {
                Log.w(TAG, "Failed to set profile owner " + re);
                throw new IllegalArgumentException("Couldn't set profile owner.", re);
            }
        }
        return z;
    }

    public void clearProfileOwner(ComponentName admin) {
        if (this.mService != null) {
            try {
                this.mService.clearProfileOwner(admin);
            } catch (RemoteException re) {
                Log.w(TAG, "Failed to clear profile owner " + admin + re);
            }
        }
    }

    public boolean hasUserSetupCompleted() {
        if (this.mService != null) {
            try {
                return this.mService.hasUserSetupCompleted();
            } catch (RemoteException e) {
                Log.w(TAG, "Failed to check whether user setup has completed");
            }
        }
        return true;
    }

    public boolean setProfileOwner(ComponentName admin, @Deprecated String ownerName, int userHandle) throws IllegalArgumentException {
        if (admin == null) {
            throw new NullPointerException("admin cannot be null");
        } else if (this.mService == null) {
            return false;
        } else {
            if (ownerName == null) {
                try {
                    ownerName = ProxyInfo.LOCAL_EXCL_LIST;
                } catch (RemoteException re) {
                    Log.w(TAG, "Failed to set profile owner", re);
                    throw new IllegalArgumentException("Couldn't set profile owner.", re);
                }
            }
            return this.mService.setProfileOwner(admin, ownerName, userHandle);
        }
    }

    public void setProfileEnabled(ComponentName admin) {
        if (this.mService != null) {
            try {
                this.mService.setProfileEnabled(admin);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public void setProfileName(ComponentName admin, String profileName) {
        if (this.mService != null) {
            try {
                this.mService.setProfileName(admin, profileName);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public boolean isProfileOwnerApp(String packageName) {
        if (this.mService == null) {
            return false;
        }
        try {
            ComponentName profileOwner = this.mService.getProfileOwner(Process.myUserHandle().getIdentifier());
            if (profileOwner == null || !profileOwner.getPackageName().equals(packageName)) {
                return false;
            }
            return true;
        } catch (RemoteException e) {
            Log.w(TAG, "Failed to check profile owner");
            return false;
        }
    }

    public ComponentName getProfileOwner() throws IllegalArgumentException {
        return getProfileOwnerAsUser(Process.myUserHandle().getIdentifier());
    }

    public ComponentName getProfileOwnerAsUser(int userId) throws IllegalArgumentException {
        if (this.mService == null) {
            return null;
        }
        try {
            return this.mService.getProfileOwner(userId);
        } catch (RemoteException re) {
            Log.w(TAG, "Failed to get profile owner");
            throw new IllegalArgumentException("Requested profile owner for invalid userId", re);
        }
    }

    public String getProfileOwnerName() throws IllegalArgumentException {
        if (this.mService == null) {
            return null;
        }
        try {
            return this.mService.getProfileOwnerName(Process.myUserHandle().getIdentifier());
        } catch (RemoteException re) {
            Log.w(TAG, "Failed to get profile owner");
            throw new IllegalArgumentException("Requested profile owner for invalid userId", re);
        }
    }

    public String getProfileOwnerNameAsUser(int userId) throws IllegalArgumentException {
        if (this.mService == null) {
            return null;
        }
        try {
            return this.mService.getProfileOwnerName(userId);
        } catch (RemoteException re) {
            Log.w(TAG, "Failed to get profile owner");
            throw new IllegalArgumentException("Requested profile owner for invalid userId", re);
        }
    }

    public void addPersistentPreferredActivity(ComponentName admin, IntentFilter filter, ComponentName activity) {
        if (this.mService != null) {
            try {
                this.mService.addPersistentPreferredActivity(admin, filter, activity);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public void clearPackagePersistentPreferredActivities(ComponentName admin, String packageName) {
        if (this.mService != null) {
            try {
                this.mService.clearPackagePersistentPreferredActivities(admin, packageName);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public void setApplicationRestrictions(ComponentName admin, String packageName, Bundle settings) {
        if (this.mService != null) {
            try {
                this.mService.setApplicationRestrictions(admin, packageName, settings);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public void setTrustAgentConfiguration(ComponentName admin, ComponentName target, PersistableBundle configuration) {
        if (this.mService != null) {
            try {
                this.mService.setTrustAgentConfiguration(admin, target, configuration);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public List<PersistableBundle> getTrustAgentConfiguration(ComponentName admin, ComponentName agent) {
        return getTrustAgentConfiguration(admin, agent, UserHandle.myUserId());
    }

    public List<PersistableBundle> getTrustAgentConfiguration(ComponentName admin, ComponentName agent, int userHandle) {
        if (this.mService != null) {
            try {
                return this.mService.getTrustAgentConfiguration(admin, agent, userHandle);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return new ArrayList();
    }

    public void setCrossProfileCallerIdDisabled(ComponentName admin, boolean disabled) {
        if (this.mService != null) {
            try {
                this.mService.setCrossProfileCallerIdDisabled(admin, disabled);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public boolean getCrossProfileCallerIdDisabled(ComponentName admin) {
        if (this.mService != null) {
            try {
                return this.mService.getCrossProfileCallerIdDisabled(admin);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return false;
    }

    public boolean getCrossProfileCallerIdDisabled(UserHandle userHandle) {
        if (this.mService != null) {
            try {
                return this.mService.getCrossProfileCallerIdDisabledForUser(userHandle.getIdentifier());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return false;
    }

    public void startManagedQuickContact(String actualLookupKey, long actualContactId, Intent originalIntent) {
        if (this.mService != null) {
            try {
                this.mService.startManagedQuickContact(actualLookupKey, actualContactId, originalIntent);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public void setBluetoothContactSharingDisabled(ComponentName admin, boolean disabled) {
        if (this.mService != null) {
            try {
                this.mService.setBluetoothContactSharingDisabled(admin, disabled);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public boolean getBluetoothContactSharingDisabled(ComponentName admin) {
        if (this.mService != null) {
            try {
                return this.mService.getBluetoothContactSharingDisabled(admin);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return true;
    }

    public boolean getBluetoothContactSharingDisabled(UserHandle userHandle) {
        if (this.mService != null) {
            try {
                return this.mService.getBluetoothContactSharingDisabledForUser(userHandle.getIdentifier());
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return true;
    }

    public void addCrossProfileIntentFilter(ComponentName admin, IntentFilter filter, int flags) {
        if (this.mService != null) {
            try {
                this.mService.addCrossProfileIntentFilter(admin, filter, flags);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public void clearCrossProfileIntentFilters(ComponentName admin) {
        if (this.mService != null) {
            try {
                this.mService.clearCrossProfileIntentFilters(admin);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public boolean setPermittedAccessibilityServices(ComponentName admin, List<String> packageNames) {
        if (this.mService != null) {
            try {
                return this.mService.setPermittedAccessibilityServices(admin, packageNames);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return false;
    }

    public List<String> getPermittedAccessibilityServices(ComponentName admin) {
        if (this.mService != null) {
            try {
                return this.mService.getPermittedAccessibilityServices(admin);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return null;
    }

    public List<String> getPermittedAccessibilityServices(int userId) {
        if (this.mService != null) {
            try {
                return this.mService.getPermittedAccessibilityServicesForUser(userId);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return null;
    }

    public boolean setPermittedInputMethods(ComponentName admin, List<String> packageNames) {
        if (this.mService != null) {
            try {
                return this.mService.setPermittedInputMethods(admin, packageNames);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return false;
    }

    public List<String> getPermittedInputMethods(ComponentName admin) {
        if (this.mService != null) {
            try {
                return this.mService.getPermittedInputMethods(admin);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return null;
    }

    public List<String> getPermittedInputMethodsForCurrentUser() {
        if (this.mService != null) {
            try {
                return this.mService.getPermittedInputMethodsForCurrentUser();
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return null;
    }

    @Deprecated
    public UserHandle createUser(ComponentName admin, String name) {
        try {
            return this.mService.createUser(admin, name);
        } catch (RemoteException re) {
            Log.w(TAG, "Could not create a user", re);
            return null;
        }
    }

    @Deprecated
    public UserHandle createAndInitializeUser(ComponentName admin, String name, String ownerName, ComponentName profileOwnerComponent, Bundle adminExtras) {
        try {
            return this.mService.createAndInitializeUser(admin, name, ownerName, profileOwnerComponent, adminExtras);
        } catch (RemoteException re) {
            Log.w(TAG, "Could not create a user", re);
            return null;
        }
    }

    public boolean removeUser(ComponentName admin, UserHandle userHandle) {
        try {
            return this.mService.removeUser(admin, userHandle);
        } catch (RemoteException re) {
            Log.w(TAG, "Could not remove user ", re);
            return false;
        }
    }

    public boolean switchUser(ComponentName admin, UserHandle userHandle) {
        try {
            return this.mService.switchUser(admin, userHandle);
        } catch (RemoteException re) {
            Log.w(TAG, "Could not switch user ", re);
            return false;
        }
    }

    public Bundle getApplicationRestrictions(ComponentName admin, String packageName) {
        if (this.mService != null) {
            try {
                return this.mService.getApplicationRestrictions(admin, packageName);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return null;
    }

    public void addUserRestriction(ComponentName admin, String key) {
        if (this.mService != null) {
            try {
                this.mService.setUserRestriction(admin, key, true);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public void clearUserRestriction(ComponentName admin, String key) {
        if (this.mService != null) {
            try {
                this.mService.setUserRestriction(admin, key, false);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public boolean setApplicationHidden(ComponentName admin, String packageName, boolean hidden) {
        if (this.mService != null) {
            try {
                return this.mService.setApplicationHidden(admin, packageName, hidden);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return false;
    }

    public boolean isApplicationHidden(ComponentName admin, String packageName) {
        if (this.mService != null) {
            try {
                return this.mService.isApplicationHidden(admin, packageName);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return false;
    }

    public void enableSystemApp(ComponentName admin, String packageName) {
        if (this.mService != null) {
            try {
                this.mService.enableSystemApp(admin, packageName);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed to install package: " + packageName);
            }
        }
    }

    public int enableSystemApp(ComponentName admin, Intent intent) {
        if (this.mService != null) {
            try {
                return this.mService.enableSystemAppWithIntent(admin, intent);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed to install packages matching filter: " + intent);
            }
        }
        return 0;
    }

    public void setAccountManagementDisabled(ComponentName admin, String accountType, boolean disabled) {
        if (this.mService != null) {
            try {
                this.mService.setAccountManagementDisabled(admin, accountType, disabled);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public String[] getAccountTypesWithManagementDisabled() {
        return getAccountTypesWithManagementDisabledAsUser(UserHandle.myUserId());
    }

    public String[] getAccountTypesWithManagementDisabledAsUser(int userId) {
        if (this.mService != null) {
            try {
                return this.mService.getAccountTypesWithManagementDisabledAsUser(userId);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return null;
    }

    public void setLockTaskPackages(ComponentName admin, String[] packages) throws SecurityException {
        if (this.mService != null) {
            try {
                this.mService.setLockTaskPackages(admin, packages);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public String[] getLockTaskPackages(ComponentName admin) {
        if (this.mService != null) {
            try {
                return this.mService.getLockTaskPackages(admin);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return null;
    }

    public boolean isLockTaskPermitted(String pkg) {
        if (this.mService != null) {
            try {
                return this.mService.isLockTaskPermitted(pkg);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return false;
    }

    public void setGlobalSetting(ComponentName admin, String setting, String value) {
        if (this.mService != null) {
            try {
                this.mService.setGlobalSetting(admin, setting, value);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public void setSecureSetting(ComponentName admin, String setting, String value) {
        if (this.mService != null) {
            try {
                this.mService.setSecureSetting(admin, setting, value);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public void setRestrictionsProvider(ComponentName admin, ComponentName provider) {
        if (this.mService != null) {
            try {
                this.mService.setRestrictionsProvider(admin, provider);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed to set permission provider on device policy service");
            }
        }
    }

    public void setMasterVolumeMuted(ComponentName admin, boolean on) {
        if (this.mService != null) {
            try {
                this.mService.setMasterVolumeMuted(admin, on);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed to setMasterMute on device policy service");
            }
        }
    }

    public boolean isMasterVolumeMuted(ComponentName admin) {
        if (this.mService != null) {
            try {
                return this.mService.isMasterVolumeMuted(admin);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed to get isMasterMute on device policy service");
            }
        }
        return false;
    }

    public void setUninstallBlocked(ComponentName admin, String packageName, boolean uninstallBlocked) {
        if (this.mService != null) {
            try {
                this.mService.setUninstallBlocked(admin, packageName, uninstallBlocked);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed to call block uninstall on device policy service");
            }
        }
    }

    public boolean isUninstallBlocked(ComponentName admin, String packageName) {
        if (this.mService != null) {
            try {
                return this.mService.isUninstallBlocked(admin, packageName);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed to call block uninstall on device policy service");
            }
        }
        return false;
    }

    public boolean addCrossProfileWidgetProvider(ComponentName admin, String packageName) {
        if (this.mService != null) {
            try {
                return this.mService.addCrossProfileWidgetProvider(admin, packageName);
            } catch (RemoteException re) {
                Log.w(TAG, "Error calling addCrossProfileWidgetProvider", re);
            }
        }
        return false;
    }

    public boolean removeCrossProfileWidgetProvider(ComponentName admin, String packageName) {
        if (this.mService != null) {
            try {
                return this.mService.removeCrossProfileWidgetProvider(admin, packageName);
            } catch (RemoteException re) {
                Log.w(TAG, "Error calling removeCrossProfileWidgetProvider", re);
            }
        }
        return false;
    }

    public List<String> getCrossProfileWidgetProviders(ComponentName admin) {
        if (this.mService != null) {
            try {
                List<String> providers = this.mService.getCrossProfileWidgetProviders(admin);
                if (providers != null) {
                    return providers;
                }
            } catch (RemoteException re) {
                Log.w(TAG, "Error calling getCrossProfileWidgetProviders", re);
            }
        }
        return Collections.emptyList();
    }

    public void setUserIcon(ComponentName admin, Bitmap icon) {
        try {
            this.mService.setUserIcon(admin, icon);
        } catch (RemoteException re) {
            Log.w(TAG, "Could not set the user icon ", re);
        }
    }

    public void setSystemUpdatePolicy(ComponentName admin, SystemUpdatePolicy policy) {
        if (this.mService != null) {
            try {
                this.mService.setSystemUpdatePolicy(admin, policy);
            } catch (RemoteException re) {
                Log.w(TAG, "Error calling setSystemUpdatePolicy", re);
            }
        }
    }

    public SystemUpdatePolicy getSystemUpdatePolicy() {
        if (this.mService != null) {
            try {
                return this.mService.getSystemUpdatePolicy();
            } catch (RemoteException re) {
                Log.w(TAG, "Error calling getSystemUpdatePolicy", re);
            }
        }
        return null;
    }

    public boolean setKeyguardDisabled(ComponentName admin, boolean disabled) {
        try {
            return this.mService.setKeyguardDisabled(admin, disabled);
        } catch (RemoteException re) {
            Log.w(TAG, "Failed talking with device policy service", re);
            return false;
        }
    }

    public boolean setStatusBarDisabled(ComponentName admin, boolean disabled) {
        try {
            return this.mService.setStatusBarDisabled(admin, disabled);
        } catch (RemoteException re) {
            Log.w(TAG, "Failed talking with device policy service", re);
            return false;
        }
    }

    public void notifyPendingSystemUpdate(long updateReceivedTime) {
        if (this.mService != null) {
            try {
                this.mService.notifyPendingSystemUpdate(updateReceivedTime);
            } catch (RemoteException re) {
                Log.w(TAG, "Could not notify device owner about pending system update", re);
            }
        }
    }

    public void setPermissionPolicy(ComponentName admin, int policy) {
        try {
            this.mService.setPermissionPolicy(admin, policy);
        } catch (RemoteException re) {
            Log.w(TAG, "Failed talking with device policy service", re);
        }
    }

    public int getPermissionPolicy(ComponentName admin) {
        try {
            return this.mService.getPermissionPolicy(admin);
        } catch (RemoteException e) {
            return 0;
        }
    }

    public boolean setPermissionGrantState(ComponentName admin, String packageName, String permission, int grantState) {
        try {
            return this.mService.setPermissionGrantState(admin, packageName, permission, grantState);
        } catch (RemoteException re) {
            Log.w(TAG, "Failed talking with device policy service", re);
            return false;
        }
    }

    public int getPermissionGrantState(ComponentName admin, String packageName, String permission) {
        try {
            return this.mService.getPermissionGrantState(admin, packageName, permission);
        } catch (RemoteException re) {
            Log.w(TAG, "Failed talking with device policy service", re);
            return 0;
        }
    }

    public String getPassword(ComponentName admin) {
        if (this.mService != null) {
            try {
                return this.mService.getPassword(admin);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
        return null;
    }

    public void reboot(String reason) {
        if (this.mService != null) {
            try {
                this.mService.reboot(reason);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed talking with device policy service", e);
            }
        }
    }

    public void setPasswordExpires(ComponentName admin, int value) {
        setPasswordExpirationTimeout(admin, ((long) value) * AlarmManager.INTERVAL_DAY);
    }

    public int getPasswordExpires(ComponentName admin) {
        long timeout = getPasswordExpirationTimeout(admin);
        if (timeout > 0) {
            return (int) (timeout / AlarmManager.INTERVAL_DAY);
        }
        return 0;
    }

    public void setPasswordHistory(ComponentName admin, int value) {
        setPasswordHistoryLength(admin, value);
    }

    public int getPasswordHistory(ComponentName admin) {
        return getPasswordHistoryLength(admin);
    }

    public void setMinPasswordComplexChars(ComponentName admin, int size) {
        setPasswordMinimumNonLetter(admin, size);
    }

    public int getMinPasswordComplexChars(ComponentName admin) {
        return getPasswordMinimumNonLetter(admin);
    }
}
