package com.android.internal.widget;

import android.app.ActivityManager;
import android.app.ActivityManagerNative;
import android.app.admin.DevicePolicyManager;
import android.app.trust.IStrongAuthTracker;
import android.app.trust.TrustManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.UserInfo;
import android.dirEncryption.DirEncryptionManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.os.Debug;
import android.os.Handler;
import android.os.IBinder;
import android.os.IUserManager;
import android.os.Looper;
import android.os.Message;
import android.os.PersonaManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.storage.IMountService;
import android.provider.SecurityContract.Tables.PASSWORDS;
import android.provider.Settings$System;
import android.provider.Settings.Global;
import android.provider.Settings.Secure;
import android.sec.enterprise.EnterpriseDeviceManager;
import android.sec.enterprise.PasswordPolicy;
import android.sec.enterprise.RestrictionPolicy;
import android.security.keystore.KeyProperties;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.util.SparseIntArray;
import com.android.ims.ImsConferenceState;
import com.android.internal.util.Protocol;
import com.android.internal.widget.LockPatternView.Cell;
import com.google.android.collect.Lists;
import com.samsung.android.fingerprint.FingerprintManager;
import com.samsung.android.security.CCManager;
import com.samsung.android.service.DeviceRootKeyService.DeviceRootKeyServiceManager;
import com.samsung.android.smartface.SmartFaceManager;
import com.sec.smartcard.pinservice.ISmartCardRegisterCallback;
import com.sec.smartcard.pinservice.ISmartCardVerifyCallback;
import com.sec.smartcard.pinservice.ISmartCardVerifyCallback.Stub;
import com.sec.smartcard.pinservice.SmartCardPinManager;
import com.sec.smartcard.pinservice.SmartCardPinManagerFactory;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import libcore.util.HexEncoding;

public class LockPatternUtils {
    public static final String APP_LOCK_FINGERPRINT_LOCKSCREEN_KEY = "lockscreen.applock_fingerprint";
    @Deprecated
    public static final String BIOMETRIC_WEAK_EVER_CHOSEN_KEY = "lockscreen.biometricweakeverchosen";
    private static final boolean DEBUG = false;
    public static final String DISABLE_LOCKSCREEN_KEY = "lockscreen.disabled";
    public static final String ENABLED_FINGERPRINT_LOCKSCREEN_KEY = "lockscreen.enabled_fingerprint";
    private static final String ENABLED_TRUST_AGENTS = "lockscreen.enabledtrustagents";
    public static final String ENTERPRISEID_TYPE_KEY = "lockscreen.enterpriseidentity_type";
    public static final int FAILED_ATTEMPTS_BEFORE_RESET = 20;
    public static final int FAILED_ATTEMPTS_BEFORE_TIMEOUT = 5;
    public static final int FAILED_ATTEMPTS_BEFORE_WIPE_GRACE = 5;
    public static final long FAILED_ATTEMPT_COUNTDOWN_INTERVAL_MS = 1000;
    private static final long FAILED_ATTEMPT_TIMEOUT_MS = 30000;
    private static final long FAILED_ATTEMPT_TIMEOUT_MS_LEVEL2 = 60000;
    private static final long FAILED_ATTEMPT_TIMEOUT_MS_LEVEL3 = 300000;
    private static final long FAILED_ATTEMPT_TIMEOUT_MS_LEVEL4 = 600000;
    private static final long FAILED_ATTEMPT_TIMEOUT_MS_LEVEL5 = 1800000;
    private static final long FAILED_ATTEMPT_TIMEOUT_MS_LEVEL6 = 3600000;
    public static final int FINGERPRINT_LOCKSCREEN_OFF = 0;
    public static final int FINGERPRINT_LOCKSCREEN_ON = 1;
    public static final int FINGERPRINT_LOCKSCREEN_OPTIONAL = 2;
    public static final String FLAG_ENTERPRISEID_SELECTED = "lockscreen.enterpriseidentity_selected";
    public static final int FMM_LOCK = 0;
    public static final String LOCKOUT_ATTEMPT_DEADLINE = "lockscreen.lockoutattemptdeadline";
    public static final String LOCKOUT_ATTEMPT_TIMEOUT_MS = "lockscreen.lockoutattempttimeoutmss";
    @Deprecated
    public static final String LOCKOUT_PERMANENT_KEY = "lockscreen.lockedoutpermanently";
    public static final String LOCKOUT_RECOVERY_KEY = "lockscreen.lockedoutRecovery";
    @Deprecated
    public static final String LOCKSCREEN_BIOMETRIC_WEAK_FALLBACK = "lockscreen.biometric_weak_fallback";
    public static final String LOCKSCREEN_OPTIONS = "lockscreen.options";
    public static final String LOCKSCREEN_POWER_BUTTON_INSTANTLY_LOCKS = "lockscreen.power_button_instantly_locks";
    @Deprecated
    public static final String LOCKSCREEN_WIDGETS_ENABLED = "lockscreen.widgets_enabled";
    public static final String LOCK_PASSWORD_SALT_KEY = "lockscreen.password_salt";
    private static final String LOCK_SCREEN_OWNER_INFO = "lock_screen_owner_info";
    private static final String LOCK_SCREEN_OWNER_INFO_ENABLED = "lock_screen_owner_info_enabled";
    public static final int MAX_ALLOWED_SEQUENCE = 3;
    public static final int MIN_LOCK_PASSWORD_SIZE = 4;
    public static final int MIN_LOCK_PATTERN_SIZE = 4;
    public static final int MIN_PATTERN_REGISTER_FAIL = 4;
    public static final String PASSWORD_HISTORY_KEY = "lockscreen.passwordhistory";
    @Deprecated
    public static final String PASSWORD_TYPE_ALTERNATE_KEY = "lockscreen.password_type_alternate";
    public static final String PASSWORD_TYPE_KEY = "lockscreen.password_type";
    private static final String PATH_PERMANENT_MEM_LOCK_FOLDER = "/efs/sec_efs/sktdm_mem";
    private static final String PATH_PERMANENT_MEM_LOCK_INFO = "/efs/sec_efs/sktdm_mem/lawlock.txt";
    private static final String PATH_PERMANENT_MEM_LOCK_INFO_ENC = "/efs/sec_efs/sktdm_mem/enclawlock.txt";
    private static final String PATH_PERMANENT_MEM_LOCK_MSG_INFO = "/efs/sec_efs/sktdm_mem/lawlockmsg.txt";
    private static final String PATH_PERMANENT_MEM_LOCK_MSG_INFO_ENC = "/efs/sec_efs/sktdm_mem/enclawlockmsg.txt";
    public static final String PATTERN_EVER_CHOSEN_KEY = "lockscreen.patterneverchosen";
    public static final String PRIVATE_MODE_FINGERPRINT_LOCKSCREEN_KEY = "lockscreen.privatemode_fingerprint";
    private static final boolean SECURE_DEBUG;
    public static final String SKTLOCKOUT_ATTEMPT_DEADLINE = "sktlockscreen.lockoutdeadline";
    private static final String SKT_CARRIERLOCK_MODE_FILE = "/efs/sms/sktcarrierlockmode";
    public static final int SKT_CARRIER_LOCK = 1;
    public static final long SKT_FAILED_ATTEMPT_TIMEOUT_MS = 600000;
    public static final String SMARTCARD_TYPE_KEY = "lockscreen.smartcard_type";
    private static final String TAG = "LockPatternUtils";
    private static final char TEXT_SEPERATOR = ':';
    public static final String UNIVERSAL_LOCK_ENABLED = "isUniversalLock";
    private static boolean cacPasswordSetProgress = false;
    private static long failedUnlockAttemptNumber = 0;
    private static final IvParameterSpec ivParamSpec = new IvParameterSpec("i_love_office_tg".getBytes());
    private static String lockChecksum = null;
    private static String lockChecksumUnlock = null;
    private static String lockMsg = "";
    private static String lockMsgUnlock = "";
    private static String lockPassword = null;
    private static String lockPasswordUnlock = null;
    private static String lockSaveMsg = "";
    private static String lockSaveMsgUnlock = "";
    private static String lockType = null;
    private static String lockTypeUnlock = null;
    private static boolean mIsCallbackCalled = false;
    private static final String mSCafeName = SystemProperties.get("ro.build.scafe");
    private static int mScVerifyStatus = 0;
    private static SmartCardPinManager mSmartcardPinMgr = null;
    private static final boolean mSmsPortAddressedMessage = false;
    private static boolean mSwipeSmartLock = false;
    private static volatile int sCurrentUserId = DeviceRootKeyServiceManager.ERR_SERVICE_ERROR;
    private static final String secretKey = "SKT : Find lost phone plus !!!";
    private final ContentResolver mContentResolver;
    private final Context mContext;
    private DevicePolicyManager mDevicePolicyManager;
    private ILockSettings mLockSettingsService;
    private final boolean mMultiUserMode;

    private static abstract class verifyCardCallback {
        ISmartCardVerifyCallback mVerifyCallback;

        public abstract void onComplete(int i);

        private verifyCardCallback() {
            this.mVerifyCallback = new Stub() {
                public void onComplete(int status) {
                    verifyCardCallback.this.onComplete(status);
                }
            };
        }
    }

    public static final class RequestThrottledException extends Exception {
        private int mTimeoutMs;

        public RequestThrottledException(int timeoutMs) {
            this.mTimeoutMs = timeoutMs;
        }

        public int getTimeoutMs() {
            return this.mTimeoutMs;
        }
    }

    public enum SecAppLockType {
        None,
        Pattern,
        Password,
        PIN,
        BackupPin,
        FingerPrint
    }

    public enum SecPrivateMode {
        None,
        Pattern,
        Password,
        PIN,
        BackupPin,
        FingerPrint
    }

    public static class StrongAuthTracker {
        private static final int ALLOWING_FINGERPRINT = 20;
        public static final int DEFAULT = 1;
        public static final int SOME_AUTH_REQUIRED_AFTER_USER_REQUEST = 4;
        public static final int SOME_AUTH_REQUIRED_AFTER_WRONG_CREDENTIAL = 16;
        public static final int STRONG_AUTH_NOT_REQUIRED = 0;
        public static final int STRONG_AUTH_REQUIRED_AFTER_BOOT = 1;
        public static final int STRONG_AUTH_REQUIRED_AFTER_DPM_LOCK_NOW = 2;
        public static final int STRONG_AUTH_REQUIRED_AFTER_LOCKOUT = 8;
        private final H mHandler;
        private final SparseIntArray mStrongAuthRequiredForUser;
        final IStrongAuthTracker.Stub mStub;

        private class H extends Handler {
            static final int MSG_ON_STRONG_AUTH_REQUIRED_CHANGED = 1;

            public H(Looper looper) {
                super(looper);
            }

            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        StrongAuthTracker.this.handleStrongAuthRequiredChanged(msg.arg1, msg.arg2);
                        return;
                    default:
                        return;
                }
            }
        }

        public StrongAuthTracker() {
            this(Looper.myLooper());
        }

        public StrongAuthTracker(Looper looper) {
            this.mStrongAuthRequiredForUser = new SparseIntArray();
            this.mStub = new IStrongAuthTracker.Stub() {
                public void onStrongAuthRequiredChanged(int strongAuthFlags, int userId) {
                    StrongAuthTracker.this.mHandler.obtainMessage(1, strongAuthFlags, userId).sendToTarget();
                }
            };
            this.mHandler = new H(looper);
        }

        public int getStrongAuthForUser(int userId) {
            return this.mStrongAuthRequiredForUser.get(userId, 1);
        }

        public boolean isTrustAllowedForUser(int userId) {
            return getStrongAuthForUser(userId) == 0;
        }

        public boolean isFingerprintAllowedForUser(int userId) {
            return (getStrongAuthForUser(userId) & -29) == 0;
        }

        public void onStrongAuthRequiredChanged(int userId) {
        }

        void handleStrongAuthRequiredChanged(int strongAuthFlags, int userId) {
            if (strongAuthFlags != getStrongAuthForUser(userId)) {
                if (strongAuthFlags == 1) {
                    this.mStrongAuthRequiredForUser.delete(userId);
                } else {
                    this.mStrongAuthRequiredForUser.put(userId, strongAuthFlags);
                }
                onStrongAuthRequiredChanged(userId);
            }
        }
    }

    private static abstract class registerCardCallback {
        ISmartCardRegisterCallback mVerifyCallback = new ISmartCardRegisterCallback.Stub() {
            public void onComplete(int status) {
                registerCardCallback.this.onComplete(status);
            }
        };

        public abstract void onComplete(int i);

        private registerCardCallback() {
        }
    }

    static {
        boolean z;
        if (Debug.isProductShip() == 0) {
            z = true;
        } else {
            z = false;
        }
        SECURE_DEBUG = z;
    }

    public DevicePolicyManager getDevicePolicyManager() {
        if (this.mDevicePolicyManager == null) {
            this.mDevicePolicyManager = (DevicePolicyManager) this.mContext.getSystemService("device_policy");
            if (this.mDevicePolicyManager == null) {
                Log.e(TAG, "Can't get DevicePolicyManagerService: is it running?", new IllegalStateException("Stack trace:"));
            }
        }
        return this.mDevicePolicyManager;
    }

    private TrustManager getTrustManager() {
        TrustManager trust = (TrustManager) this.mContext.getSystemService("trust");
        if (trust == null) {
            Log.e(TAG, "Can't get TrustManagerService: is it running?", new IllegalStateException("Stack trace:"));
        }
        return trust;
    }

    private boolean isFollowLegacyTimeoutPolicy() {
        if ("mocha".equals(mSCafeName) || "americano".equals(mSCafeName)) {
            return true;
        }
        return false;
    }

    public LockPatternUtils(Context context) {
        this.mContext = context;
        this.mContentResolver = context.getContentResolver();
        this.mMultiUserMode = context.checkCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL") == 0;
        failedUnlockAttemptNumber = Global.getLong(this.mContext.getContentResolver(), "lockscreen.failedUnlockAttemptNumber", failedUnlockAttemptNumber);
    }

    private ILockSettings getLockSettings() {
        if (this.mLockSettingsService == null) {
            this.mLockSettingsService = ILockSettings.Stub.asInterface(ServiceManager.getService("lock_settings"));
        }
        return this.mLockSettingsService;
    }

    public int getRequestedMinimumPasswordLength(int userId) {
        return getDevicePolicyManager().getPasswordMinimumLength(null, userId);
    }

    public int getRequestedPasswordQuality(int userId) {
        return getDevicePolicyManager().getPasswordQuality(null, userId);
    }

    private int getRequestedPasswordHistoryLength(int userId) {
        return getDevicePolicyManager().getPasswordHistoryLength(null, userId);
    }

    public int getRequestedPasswordMinimumLetters(int userId) {
        return getDevicePolicyManager().getPasswordMinimumLetters(null, userId);
    }

    public int getRequestedPasswordMinimumUpperCase(int userId) {
        return getDevicePolicyManager().getPasswordMinimumUpperCase(null, userId);
    }

    public int getRequestedPasswordMinimumLowerCase(int userId) {
        return getDevicePolicyManager().getPasswordMinimumLowerCase(null, userId);
    }

    public int getRequestedPasswordMinimumNumeric(int userId) {
        return getDevicePolicyManager().getPasswordMinimumNumeric(null, userId);
    }

    public int getRequestedPasswordMinimumSymbols(int userId) {
        return getDevicePolicyManager().getPasswordMinimumSymbols(null, userId);
    }

    public int getRequestedPasswordMinimumNonLetter(int userId) {
        return getDevicePolicyManager().getPasswordMinimumNonLetter(null, userId);
    }

    public void reportFailedPasswordAttempt(int userId) {
        getDevicePolicyManager().reportFailedPasswordAttempt(userId);
        getTrustManager().reportUnlockAttempt(false, userId);
        requireStrongAuth(16, userId);
    }

    public void reportSuccessfulPasswordAttempt(int userId) {
        getDevicePolicyManager().reportSuccessfulPasswordAttempt(userId);
        getTrustManager().reportUnlockAttempt(true, userId);
        failedUnlockAttemptNumber = 0;
        Global.putLong(this.mContext.getContentResolver(), "lockscreen.failedUnlockAttemptNumber", 0);
        setLong(LOCKOUT_ATTEMPT_DEADLINE, 0, userId);
        setLong(LOCKOUT_ATTEMPT_TIMEOUT_MS, 0, userId);
    }

    private boolean isKnoxUser(int userId) {
        if (userId < 100 || userId > 200) {
            return false;
        }
        PersonaManager pm = (PersonaManager) this.mContext.getSystemService("persona");
        if (pm != null) {
            return pm.exists(userId);
        }
        return false;
    }

    public byte[] verifyPattern(List<Cell> pattern, long challenge, int userId) throws RequestThrottledException {
        try {
            VerifyCredentialResponse response = getLockSettings().verifyPattern(patternToString(pattern), challenge, userId);
            if (response == null) {
                return null;
            }
            if (response.getResponseCode() == 0) {
                return response.getPayload();
            }
            if (response.getResponseCode() != 1) {
                return null;
            }
            throw new RequestThrottledException(response.getTimeout());
        } catch (RemoteException e) {
            return null;
        }
    }

    public boolean checkPattern(List<Cell> pattern, int userId) throws RequestThrottledException {
        try {
            VerifyCredentialResponse response = getLockSettings().checkPattern(patternToString(pattern), userId);
            if (response.getResponseCode() == 0) {
                return true;
            }
            if (response.getResponseCode() != 1) {
                return false;
            }
            throw new RequestThrottledException(response.getTimeout());
        } catch (RemoteException re) {
            if (!isKnoxUser(userId)) {
                return false;
            }
            re.printStackTrace();
            return false;
        }
    }

    private void updateEnterpriseIdentityLock(int userId) {
        try {
            int eIdChoosenByUser = Settings$System.getIntForUser(this.mContentResolver, FLAG_ENTERPRISEID_SELECTED, 0, userId);
            Log.d(TAG, "updateEnterpriseIdentityLock: eIdChoosenByUser: " + eIdChoosenByUser + "user: " + userId + "Enterprise ID status:" + isEnterpriseIdentityLockSet(userId));
            boolean isEidLockNeedToSet = false;
            if (eIdChoosenByUser > 0) {
                isEidLockNeedToSet = true;
                setEnterpriseIdentitySelected(false, userId);
            }
            if (isEnterpriseIdentityLockSet(userId) != isEidLockNeedToSet) {
                setEnterpriseIdentityLock(isEidLockNeedToSet, userId);
            }
            Log.d(TAG, "updateEnterpriseIdentityLock: EidState: " + isEnterpriseIdentityLockSet(userId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isEnterpriseIdentityLockSet() {
        return isEnterpriseIdentityLockSet(getCurrentOrCallingUserId());
    }

    public boolean setEnterpriseIdentitySelected(boolean select, int userId) {
        int i = 1;
        if (userId < 100) {
            return false;
        }
        ContentResolver contentResolver = this.mContentResolver;
        String str = FLAG_ENTERPRISEID_SELECTED;
        if (!select) {
            i = 0;
        }
        return Settings$System.putIntForUser(contentResolver, str, i, userId);
    }

    public boolean setEnterpriseIdentityLock(boolean status) {
        setEnterpriseIdentityLock(status, getCurrentOrCallingUserId());
        return true;
    }

    public boolean isEnterpriseIdentityLockSet(int userId) {
        Log.d(TAG, "IsEnterpriseIdSet = " + getBoolean(ENTERPRISEID_TYPE_KEY, false, userId) + " userId:" + userId);
        return getBoolean(ENTERPRISEID_TYPE_KEY, false, userId);
    }

    public boolean setEnterpriseIdentityLock(boolean status, int userId) {
        setBoolean(ENTERPRISEID_TYPE_KEY, status, userId);
        return true;
    }

    public void setCurrentUser(int userId) {
        sCurrentUserId = userId;
    }

    public int getCurrentUser() {
        if (sCurrentUserId != DeviceRootKeyServiceManager.ERR_SERVICE_ERROR) {
            return sCurrentUserId;
        }
        try {
            return ActivityManagerNative.getDefault().getCurrentUser().id;
        } catch (RemoteException e) {
            return 0;
        }
    }

    private int getCurrentOrCallingUserId() {
        if (this.mMultiUserMode) {
            return getCurrentUser();
        }
        return UserHandle.getCallingUserId();
    }

    public byte[] verifyPassword(String password, long challenge, int userId) throws RequestThrottledException {
        try {
            VerifyCredentialResponse response = getLockSettings().verifyPassword(password, challenge, userId);
            if (response.getResponseCode() == 0) {
                return response.getPayload();
            }
            if (response.getResponseCode() != 1) {
                return null;
            }
            throw new RequestThrottledException(response.getTimeout());
        } catch (RemoteException e) {
            return null;
        }
    }

    public boolean checkPassword(String password, int userId) throws RequestThrottledException {
        try {
            VerifyCredentialResponse response = getLockSettings().checkPassword(password, userId);
            if (response.getResponseCode() == 0) {
                return true;
            }
            if (response.getResponseCode() == 1) {
                throw new RequestThrottledException(response.getTimeout());
            }
            Log.e(TAG, "checkPassword resCode " + response.getResponseCode());
            return false;
        } catch (RemoteException re) {
            if (!isKnoxUser(userId)) {
                return false;
            }
            re.printStackTrace();
            return false;
        }
    }

    public boolean checkVoldPassword(int userId) {
        try {
            return getLockSettings().checkVoldPassword(userId);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean checkPasswordHistory(String password, int userId) {
        String passwordHashString = new String(passwordToHash(password, userId), StandardCharsets.UTF_8);
        String passwordHistory = getString(PASSWORD_HISTORY_KEY, userId);
        if (passwordHistory == null) {
            return false;
        }
        int passwordHashLength = passwordHashString.length();
        int passwordHistoryLength = getRequestedPasswordHistoryLength(userId);
        if (passwordHistoryLength == 0) {
            return false;
        }
        int neededPasswordHistoryLength = ((passwordHashLength * passwordHistoryLength) + passwordHistoryLength) - 1;
        if (passwordHistory.length() > neededPasswordHistoryLength) {
            passwordHistory = passwordHistory.substring(0, neededPasswordHistoryLength);
        }
        return passwordHistory.contains(passwordHashString);
    }

    public boolean savedPatternExists(int userId) {
        try {
            return getLockSettings().havePattern(userId);
        } catch (RemoteException e) {
            return false;
        }
    }

    private boolean savedPasswordExists(int userId) {
        try {
            return getLockSettings().havePassword(userId);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean isPatternEverChosen(int userId) {
        return getBoolean(PATTERN_EVER_CHOSEN_KEY, false, userId);
    }

    public int getActivePasswordQuality(int userId) {
        int quality = getKeyguardStoredPasswordQuality(userId);
        if (isLockPasswordEnabled(quality, userId) || isLockPatternEnabled(quality, userId)) {
            return quality;
        }
        if (isLockUniversalEnabled(quality, userId)) {
            return 4096;
        }
        if (isSmartUnlockEnabled(quality, userId)) {
            return 37120;
        }
        if (isFingerPrintLockscreen(quality, userId)) {
            return 397312;
        }
        return 0;
    }

    public void clearLock(int userHandle) {
        Log.d(TAG, "clearLock : " + userHandle);
        setFingerPrintLockscreen(false, userHandle);
        setLong(PASSWORD_TYPE_KEY, 0, userHandle);
        try {
            getLockSettings().setLockPassword(null, null, userHandle);
            getLockSettings().setLockPattern(null, null, userHandle);
            getLockSettings().setLockFMMPassword(null, 0);
            getLockSettings().setLockBackupPin(null, null, userHandle);
        } catch (RemoteException e) {
        }
        if (userHandle == 0) {
            updateEncryptionPassword(1, null);
            if (userHandle == 0 && DirEncryptionManager.isEncryptionFeatureEnabled()) {
                DirEncryptionManager dem = new DirEncryptionManager(this.mContext);
                dem.setNeedToCreateKey(true);
                dem.setPassword(null);
            }
        }
        setCredentialRequiredToDecrypt(false);
        getDevicePolicyManager().setActivePasswordState(0, 0, 0, 0, 0, 0, 0, 0, userHandle);
        onAfterChangingPassword(userHandle);
    }

    private void clearLockExceptPwd() {
        clearLockExceptPwd(getCurrentOrCallingUserId());
    }

    private void clearLockExceptPwd(int userHandle) {
        updateCACAuthentication(0);
    }

    public void clearLockUniversal(boolean isFallback) {
        setLong(PASSWORD_TYPE_KEY, 0, getCurrentOrCallingUserId());
        try {
            getLockSettings().setLockPattern(null, null, getCurrentOrCallingUserId());
        } catch (RemoteException e) {
        }
        clearLockExceptPwd();
    }

    public void setLockScreenDisabled(boolean disable, int userId) {
        setBoolean("lockscreen.disabled", disable, userId);
    }

    public boolean isLockScreenDisabled(int userId) {
        PersonaManager pm = (PersonaManager) this.mContext.getSystemService("persona");
        if ((isSecure(userId) || !getBoolean("lockscreen.disabled", false, userId)) && (pm == null || !PersonaManager.isKioskModeEnabled(this.mContext))) {
            return false;
        }
        return true;
    }

    public boolean usingBiometricWeak(int userId) {
        return ((int) getLong(PASSWORD_TYPE_KEY, 0, userId)) == 32768;
    }

    public boolean isBiometricWeakInstalled() {
        PackageManager pm = this.mContext.getPackageManager();
        try {
            pm.getPackageInfo("com.android.facelock", 1);
            if (!pm.hasSystemFeature("android.hardware.camera.front") || getDevicePolicyManager().getCameraDisabled(null, getCurrentOrCallingUserId())) {
                return false;
            }
            RestrictionPolicy restrictionPolicy = EnterpriseDeviceManager.getInstance().getRestrictionPolicy();
            if (restrictionPolicy != null) {
                return restrictionPolicy.isCameraEnabled(false);
            }
            return false;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public void saveLockUniversal(List<Cell> pattern) {
        saveLockPattern(pattern, patternToString(pattern), getCurrentOrCallingUserId(), true);
    }

    public void saveLockPattern(List<Cell> pattern, int userId) {
        saveLockPattern(pattern, null, userId);
    }

    public void saveLockPattern(List<Cell> pattern, String savedPattern, int userId) {
        saveLockPattern(pattern, savedPattern, userId, false);
    }

    public void saveLockPattern(List<Cell> pattern, String savedPattern, int userId, boolean isDirectionLock) {
        saveLockPattern(pattern, savedPattern, userId, isDirectionLock, false);
    }

    public void saveLockPattern(List<Cell> pattern, String savedPattern, int userId, boolean isDirectionLock, boolean isSmartUnlock) {
        Log.d(TAG, "LockPatternUtils.saveLockPattern() userId: " + userId + " isSmartUnlock: " + isSmartUnlock + " isDirectionLock " + isDirectionLock);
        if (pattern != null) {
            try {
                if (pattern.size() >= 4) {
                    getLockSettings().setLockPattern(patternToString(pattern), savedPattern, userId);
                    DevicePolicyManager dpm = getDevicePolicyManager();
                    if (userId == 0 && isDeviceEncryptionEnabled()) {
                        if (shouldEncryptWithCredentials(true)) {
                            updateEncryptionPassword(2, patternToString(pattern));
                        } else {
                            clearEncryptionPassword();
                        }
                    }
                    setBoolean(PATTERN_EVER_CHOSEN_KEY, true, userId);
                    updateCACAuthentication(65536);
                    int passwordQuality = 65536;
                    if (isDirectionLock) {
                        passwordQuality = 4096;
                    } else if (isSmartUnlock) {
                        passwordQuality = 37120;
                    }
                    setLong(PASSWORD_TYPE_KEY, (long) passwordQuality, userId);
                    dpm.setActivePasswordState(passwordQuality, pattern.size(), 0, 0, 0, 0, 0, 0, userId);
                    onAfterChangingPassword(userId);
                    updateEnterpriseIdentityLock(userId);
                    if (userId == 0 && DirEncryptionManager.isEncryptionFeatureEnabled()) {
                        DirEncryptionManager dem = new DirEncryptionManager(this.mContext);
                        dem.setNeedToCreateKey(true);
                        dem.setPassword(patternToString(pattern));
                        return;
                    }
                    return;
                }
            } catch (RemoteException re) {
                Log.e(TAG, "Couldn't save lock pattern " + re);
                return;
            }
        }
        throw new IllegalArgumentException("pattern must not be null and at least 4 dots long.");
    }

    public void sanitizePassword() {
        try {
            getLockSettings().sanitizePassword();
        } catch (RemoteException re) {
            Log.e(TAG, "Couldn't sanitize password" + re);
        }
    }

    private void updateCryptoUserInfo(int userId) {
        if (userId == 0) {
            String ownerInfo = isOwnerInfoEnabled(userId) ? getOwnerInfo(userId) : "";
            IBinder service = ServiceManager.getService("mount");
            if (service == null) {
                Log.e(TAG, "Could not find the mount service to update the user info");
                return;
            }
            IMountService mountService = IMountService.Stub.asInterface(service);
            try {
                Log.d(TAG, "Setting owner info");
                mountService.setField("OwnerInfo", ownerInfo);
            } catch (RemoteException e) {
                Log.e(TAG, "Error changing user info", e);
            }
        }
    }

    public void setOwnerInfo(String info, int userId) {
        setString(LOCK_SCREEN_OWNER_INFO, info, userId);
        updateCryptoUserInfo(userId);
    }

    public void setOwnerInfoEnabled(boolean enabled, int userId) {
        setBoolean(LOCK_SCREEN_OWNER_INFO_ENABLED, enabled, userId);
        updateCryptoUserInfo(userId);
    }

    public String getOwnerInfo(int userId) {
        return getString(LOCK_SCREEN_OWNER_INFO, userId);
    }

    public boolean isOwnerInfoEnabled(int userId) {
        return getBoolean(LOCK_SCREEN_OWNER_INFO_ENABLED, false, userId);
    }

    public static int computePasswordQuality(String password) {
        boolean hasDigit = false;
        boolean hasNonDigit = false;
        int len = password.length();
        for (int i = 0; i < len; i++) {
            if (Character.isDigit(password.charAt(i))) {
                hasDigit = true;
            } else {
                hasNonDigit = true;
            }
        }
        if (hasNonDigit && hasDigit) {
            return Protocol.BASE_DNS_PINGER;
        }
        if (hasNonDigit) {
            return 262144;
        }
        if (hasDigit) {
            return maxLengthSequence(password) > 3 ? 131072 : 196608;
        } else {
            return 0;
        }
    }

    public boolean isLockUniversalEnabled(int userId) {
        return isLockUniversalEnabled(getKeyguardStoredPasswordQuality(userId), userId);
    }

    private boolean isLockUniversalEnabled(int mode, int userId) {
        return mode == 4096 && savedPatternExists(userId);
    }

    private static int categoryChar(char c) {
        if (DateFormat.AM_PM <= c && c <= DateFormat.TIME_ZONE) {
            return 0;
        }
        if (DateFormat.CAPITAL_AM_PM <= c && c <= 'Z') {
            return 1;
        }
        if ('0' > c || c > '9') {
            return 3;
        }
        return 2;
    }

    private static int maxDiffCategory(int category) {
        if (category == 0 || category == 1) {
            return 1;
        }
        if (category == 2) {
            return 10;
        }
        return 0;
    }

    public static int maxLengthSequence(String string) {
        if (string.length() == 0) {
            return 0;
        }
        char previousChar = string.charAt(0);
        int category = categoryChar(previousChar);
        int diff = 0;
        boolean hasDiff = false;
        int maxLength = 0;
        int startSequence = 0;
        for (int current = 1; current < string.length(); current++) {
            char currentChar = string.charAt(current);
            int categoryCurrent = categoryChar(currentChar);
            int currentDiff = currentChar - previousChar;
            if (categoryCurrent != category || Math.abs(currentDiff) > maxDiffCategory(category)) {
                maxLength = Math.max(maxLength, current - startSequence);
                startSequence = current;
                hasDiff = false;
                category = categoryCurrent;
            } else {
                if (hasDiff && currentDiff != diff) {
                    maxLength = Math.max(maxLength, current - startSequence);
                    startSequence = current - 1;
                }
                diff = currentDiff;
                hasDiff = true;
            }
            previousChar = currentChar;
        }
        return Math.max(maxLength, string.length() - startSequence);
    }

    private void updateEncryptionPassword(final int type, final String password) {
        if (isDeviceEncryptionEnabled()) {
            final IBinder service = ServiceManager.getService("mount");
            if (service == null) {
                Log.e(TAG, "Could not find the mount service to update the encryption password");
            } else {
                new AsyncTask<Void, Void, Void>() {
                    protected Void doInBackground(Void... dummy) {
                        try {
                            IMountService.Stub.asInterface(service).changeEncryptionPassword(type, password);
                        } catch (RemoteException e) {
                            Log.e(LockPatternUtils.TAG, "Error changing encryption password", e);
                        }
                        return null;
                    }
                }.execute(new Void[0]);
            }
        }
    }

    public void saveLockPassword(String password, String savedPassword, int quality, int userHandle) {
        try {
            DevicePolicyManager dpm = getDevicePolicyManager();
            if (password == null || password.length() < 4) {
                throw new IllegalArgumentException("password must not be null and at least of length 4");
            }
            getLockSettings().setLockPassword(password, savedPassword, userHandle);
            int computedQuality = computePasswordQuality(password);
            if (userHandle == 0 && isDeviceEncryptionEnabled()) {
                if (shouldEncryptWithCredentials(true)) {
                    int type = ((computedQuality == 131072) || (computedQuality == 196608)) ? 3 : 0;
                    updateEncryptionPassword(type, password);
                } else {
                    clearEncryptionPassword();
                }
            }
            updateCACAuthentication(quality);
            setLong(PASSWORD_TYPE_KEY, (long) Math.max(quality, computedQuality), userHandle);
            if (computedQuality != 0) {
                int i;
                char c;
                int letters = 0;
                int uppercase = 0;
                int lowercase = 0;
                int numbers = 0;
                int symbols = 0;
                int nonletter = 0;
                for (i = 0; i < password.length(); i++) {
                    c = password.charAt(i);
                    if (c >= 'A' && c <= 'Z') {
                        letters++;
                        uppercase++;
                    } else if (c >= 'a' && c <= 'z') {
                        letters++;
                        lowercase++;
                    } else if (c < '0' || c > '9') {
                        symbols++;
                        nonletter++;
                    } else {
                        numbers++;
                        nonletter++;
                    }
                }
                dpm.setActivePasswordState(Math.max(quality, computedQuality), password.length(), letters, uppercase, lowercase, numbers, symbols, nonletter, userHandle);
                if (savedPassword != null) {
                    int computedQuality_old = computePasswordQuality(savedPassword);
                    int letters_old = 0;
                    int uppercase_old = 0;
                    int lowercase_old = 0;
                    int numbers_old = 0;
                    int symbols_old = 0;
                    int nonletter_old = 0;
                    for (i = 0; i < savedPassword.length(); i++) {
                        c = savedPassword.charAt(i);
                        if (c >= 'A' && c <= 'Z') {
                            letters_old++;
                            uppercase_old++;
                        } else if (c >= 'a' && c <= 'z') {
                            letters_old++;
                            lowercase_old++;
                        } else if (c < '0' || c > '9') {
                            symbols_old++;
                            nonletter_old++;
                        } else {
                            numbers_old++;
                            nonletter_old++;
                        }
                    }
                    if (computedQuality_old == computedQuality && savedPassword.length() == password.length() && letters_old == letters && uppercase_old == uppercase && lowercase_old == lowercase && numbers_old == numbers && symbols_old == symbols && nonletter_old == nonletter && password != savedPassword) {
                        dpm.setActivePasswordStateForEAS(Math.max(quality, computedQuality), password.length(), letters, uppercase, lowercase, numbers, symbols, nonletter, userHandle);
                    }
                }
            } else {
                dpm.setActivePasswordState(0, 0, 0, 0, 0, 0, 0, 0, userHandle);
            }
            String passwordHistory = getString(PASSWORD_HISTORY_KEY, userHandle);
            if (passwordHistory == null) {
                passwordHistory = "";
            }
            int passwordHistoryLength = getRequestedPasswordHistoryLength(userHandle);
            if (passwordHistoryLength == 0) {
                passwordHistory = "";
            } else {
                byte[] hash = passwordToHash(password, userHandle);
                passwordHistory = new String(hash, StandardCharsets.UTF_8) + FingerprintManager.FINGER_PERMISSION_DELIMITER + passwordHistory;
                passwordHistory = passwordHistory.substring(0, Math.min(((hash.length * passwordHistoryLength) + passwordHistoryLength) - 1, passwordHistory.length()));
            }
            setString(PASSWORD_HISTORY_KEY, passwordHistory, userHandle);
            updateEnterpriseIdentityLock(userHandle);
            onAfterChangingPassword(userHandle);
            if (userHandle == 0 && DirEncryptionManager.isEncryptionFeatureEnabled()) {
                DirEncryptionManager dirEncryptionManager = new DirEncryptionManager(this.mContext);
                dirEncryptionManager.setNeedToCreateKey(true);
                dirEncryptionManager.setPassword(password);
            }
        } catch (RemoteException re) {
            Log.e(TAG, "Unable to save lock password " + re);
        }
    }

    public static boolean isDeviceEncrypted() {
        IMountService mountService = IMountService.Stub.asInterface(ServiceManager.getService("mount"));
        try {
            if (mountService.getEncryptionState() == 1 || mountService.getPasswordType() == 1) {
                return false;
            }
            return true;
        } catch (RemoteException re) {
            Log.e(TAG, "Error getting encryption state", re);
            return true;
        }
    }

    public static boolean isDeviceEncryptionEnabled() {
        return "encrypted".equalsIgnoreCase(SystemProperties.get("ro.crypto.state", "unsupported"));
    }

    public void clearEncryptionPassword() {
        updateEncryptionPassword(1, null);
    }

    public int getKeyguardStoredPasswordQuality() {
        return getKeyguardStoredPasswordQuality(getCurrentOrCallingUserId());
    }

    public int getKeyguardStoredPasswordQuality(int userHandle) {
        return (int) getLong(PASSWORD_TYPE_KEY, 0, userHandle);
    }

    public static List<Cell> stringToPattern(String string) {
        if (string == null) {
            return null;
        }
        List<Cell> result = Lists.newArrayList();
        byte[] bytes = string.getBytes();
        for (byte b : bytes) {
            byte b2 = (byte) (b - 49);
            result.add(Cell.of(b2 / 3, b2 % 3));
        }
        return result;
    }

    public static String patternToString(List<Cell> pattern) {
        if (pattern == null) {
            return "";
        }
        int patternSize = pattern.size();
        byte[] res = new byte[patternSize];
        for (int i = 0; i < patternSize; i++) {
            Cell cell = (Cell) pattern.get(i);
            res[i] = (byte) (((cell.getRow() * 3) + cell.getColumn()) + 49);
        }
        return new String(res);
    }

    public static String patternStringToBaseZero(String pattern) {
        if (pattern == null) {
            return "";
        }
        int patternSize = pattern.length();
        byte[] res = new byte[patternSize];
        byte[] bytes = pattern.getBytes();
        for (int i = 0; i < patternSize; i++) {
            res[i] = (byte) (bytes[i] - 49);
        }
        return new String(res);
    }

    public static byte[] patternToHash(List<Cell> pattern) {
        if (pattern == null) {
            return null;
        }
        int patternSize = pattern.size();
        byte[] res = new byte[patternSize];
        for (int i = 0; i < patternSize; i++) {
            Cell cell = (Cell) pattern.get(i);
            res[i] = (byte) ((cell.getRow() * 3) + cell.getColumn());
        }
        try {
            return MessageDigest.getInstance(KeyProperties.DIGEST_SHA1).digest(res);
        } catch (NoSuchAlgorithmException e) {
            return res;
        }
    }

    private String getSalt(int userId) {
        long salt = getLong(LOCK_PASSWORD_SALT_KEY, 0, userId);
        if (salt == 0) {
            try {
                salt = SecureRandom.getInstance("SHA1PRNG").nextLong();
                setLong(LOCK_PASSWORD_SALT_KEY, salt, userId);
                Log.v(TAG, "Initialized lock password salt for user: " + userId);
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException("Couldn't get SecureRandom number", e);
            }
        }
        return Long.toHexString(salt);
    }

    public byte[] passwordToHashOriginal(String password, int userId) {
        if (password == null) {
            return null;
        }
        try {
            byte[] saltedPassword = (password + getSalt(userId)).getBytes();
            byte[] sha1 = MessageDigest.getInstance(KeyProperties.DIGEST_SHA1).digest(saltedPassword);
            byte[] md5 = MessageDigest.getInstance(KeyProperties.DIGEST_MD5).digest(saltedPassword);
            byte[] combined = new byte[(sha1.length + md5.length)];
            System.arraycopy(sha1, 0, combined, 0, sha1.length);
            System.arraycopy(md5, 0, combined, sha1.length, md5.length);
            return new String(HexEncoding.encode(combined)).getBytes(StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError("Missing digest algorithm: ", e);
        }
    }

    public byte[] passwordToHash(String password, int userId) {
        if (password == null) {
            return null;
        }
        try {
            byte[] saltedPassword = (password + getSalt(userId)).getBytes();
            byte[] sha = null;
            MessageDigest md = MessageDigest.getInstance(KeyProperties.DIGEST_SHA1);
            long s = System.currentTimeMillis();
            for (int i = 0; i < 1024; i++) {
                if (sha != null) {
                    md.update(sha);
                }
                md.update(("" + i).getBytes());
                md.update(saltedPassword);
                sha = md.digest();
            }
            Log.w(TAG, "passwordToHash time = " + (System.currentTimeMillis() - s) + "ms");
            return toHex(sha).getBytes();
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError("Missing digest algorithm: ", e);
        }
    }

    private static String toHex(byte[] ary) {
        String hex = "0123456789ABCDEF";
        String ret = "";
        for (int i = 0; i < ary.length; i++) {
            ret = (ret + "0123456789ABCDEF".charAt((ary[i] >> 4) & 15)) + "0123456789ABCDEF".charAt(ary[i] & 15);
        }
        return ret;
    }

    public boolean isSecure(int userId) {
        int mode = getKeyguardStoredPasswordQuality(userId);
        return isLockPatternEnabled(mode, userId) || isLockPasswordEnabled(mode, userId) || isLockUniversalEnabled(mode, userId) || isFMMLockEnabled(userId) || isCarrierLockPlusEnabled(userId) || isFingerPrintLockscreen(userId) || isSmartUnlockEnabled(userId);
    }

    public boolean isLockPasswordEnabled(int userId) {
        return isLockPasswordEnabled(getKeyguardStoredPasswordQuality(userId), userId);
    }

    private boolean isLockPasswordEnabled(int mode, int userId) {
        boolean passwordEnabled;
        long backupMode = (long) ((int) getLong(PASSWORD_TYPE_ALTERNATE_KEY, 0, userId));
        if (mode == 262144 || mode == 131072 || mode == 196608 || mode == Protocol.BASE_DNS_PINGER || mode == 393216 || mode == 458752) {
            passwordEnabled = true;
        } else {
            passwordEnabled = false;
        }
        boolean backupEnabled;
        if (backupMode == 262144 || backupMode == 131072 || backupMode == 196608 || backupMode == 327680 || backupMode == 393216) {
            backupEnabled = true;
        } else {
            backupEnabled = false;
        }
        if (savedPasswordExists(userId)) {
            if (passwordEnabled) {
                return true;
            }
            if (usingBiometricWeak(userId) && backupEnabled) {
                return true;
            }
        }
        return false;
    }

    public boolean isLockPasswordEnabledNoCache(int userId) {
        long mode = (long) ((int) getLong(PASSWORD_TYPE_KEY, 0, userId));
        long backupMode = (long) ((int) getLong(PASSWORD_TYPE_ALTERNATE_KEY, 0, userId));
        boolean passwordEnabled = mode == 262144 || mode == 131072 || mode == 196608 || mode == 327680 || mode == 393216 || mode == 458752;
        boolean backupEnabled = backupMode == 262144 || backupMode == 131072 || backupMode == 196608 || backupMode == 327680 || backupMode == 393216;
        if (savedPasswordExistsNoCache() && (passwordEnabled || (usingBiometricWeak(userId) && backupEnabled))) {
            return true;
        }
        return false;
    }

    private boolean savedPasswordExistsNoCache() {
        ILockSettings lockSettingsService = ILockSettings.Stub.asInterface(ServiceManager.getService("lock_settings"));
        if (lockSettingsService != null) {
            try {
                return lockSettingsService.havePassword(getCurrentOrCallingUserId());
            } catch (RemoteException e) {
                Log.e(TAG, "Unable to reach LockSettingsService");
            }
        }
        return false;
    }

    public boolean isLockPatternEnabled(int userId) {
        return isLockPatternEnabled(getKeyguardStoredPasswordQuality(userId), userId);
    }

    private boolean isLockPatternEnabled(int mode, int userId) {
        return mode == 65536 && savedPatternExists(userId);
    }

    public void setLockPatternEnabled(boolean enabled) {
        setLockPatternEnabled(enabled, getCurrentOrCallingUserId());
    }

    public void setLockPatternEnabled(boolean enabled, int userHandle) {
        setBoolean(Settings$System.LOCK_PATTERN_ENABLED, enabled, userHandle);
    }

    public boolean isVisiblePatternEnabled(int userId) {
        return getBoolean(Settings$System.LOCK_PATTERN_VISIBLE, false, userId);
    }

    public boolean isVisiblePatternDisabledByMDM() {
        PasswordPolicy passPolicy = EnterpriseDeviceManager.getInstance().getPasswordPolicy();
        if (passPolicy == null || passPolicy.isScreenLockPatternVisibilityEnabled()) {
            return false;
        }
        return true;
    }

    public void setVisiblePatternEnabled(boolean enabled, int userId) {
        if (isVisiblePatternDisabledByMDM() && enabled) {
            Log.e(TAG, "Could not enable visible pattern by IT admin.");
            return;
        }
        setBoolean(Settings$System.LOCK_PATTERN_VISIBLE, enabled, userId);
        if (userId == 0) {
            IBinder service = ServiceManager.getService("mount");
            if (service == null) {
                Log.e(TAG, "Could not find the mount service to update the user info");
                return;
            }
            try {
                IMountService.Stub.asInterface(service).setField("PatternVisible", enabled ? SmartFaceManager.PAGE_BOTTOM : SmartFaceManager.PAGE_MIDDLE);
            } catch (RemoteException e) {
                Log.e(TAG, "Error changing pattern visible state", e);
            }
        }
    }

    public void setVisiblePasswordEnabled(boolean enabled, int userId) {
        if (userId == 0) {
            IBinder service = ServiceManager.getService("mount");
            if (service == null) {
                Log.e(TAG, "Could not find the mount service to update the user info");
                return;
            }
            try {
                IMountService.Stub.asInterface(service).setField("PasswordVisible", enabled ? SmartFaceManager.PAGE_BOTTOM : SmartFaceManager.PAGE_MIDDLE);
            } catch (RemoteException e) {
                Log.e(TAG, "Error changing password visible state", e);
            }
        }
    }

    public boolean isSmartUnlockEnabled() {
        return isSmartUnlockEnabled(getCurrentUser());
    }

    public boolean isSmartUnlockEnabled(int userid) {
        return getBoolean("lock_smart_unlock_enabled", false, userid) && usingSmartUnlock(userid);
    }

    public boolean isSmartUnlockEnabled(int quality, int userid) {
        return (quality == 37120 || quality == 589824) && savedPatternExists(userid) && getBoolean("lock_smart_unlock_enabled", false, userid);
    }

    public void setSmartUnlockEnabled(boolean enabled) {
        setBoolean("lock_smart_unlock_enabled", enabled);
    }

    public void setSmartUnlockEnabled(boolean enabled, int userid) {
        setBoolean("lock_smart_unlock_enabled", enabled, userid);
    }

    public boolean usingSmartUnlock(int userid) {
        int quality = (int) getLong(PASSWORD_TYPE_KEY, 65536, userid);
        if ((quality == 37120 || quality == 589824) && savedPatternExists(userid)) {
            return true;
        }
        return false;
    }

    public boolean isTactileFeedbackEnabled() {
        return Settings$System.getIntForUser(this.mContentResolver, Settings$System.HAPTIC_FEEDBACK_ENABLED, 1, -2) != 0;
    }

    public long setLockoutAttemptDeadline(int userId, int timeoutMs) {
        long deadline = System.currentTimeMillis() + ((long) timeoutMs);
        setLong(LOCKOUT_ATTEMPT_DEADLINE, deadline, userId);
        setLong(LOCKOUT_ATTEMPT_TIMEOUT_MS, (long) timeoutMs, userId);
        return deadline;
    }

    public long setLockoutAttemptDeadline(int userId) {
        failedUnlockAttemptNumber++;
        long deadline = System.currentTimeMillis();
        if (failedUnlockAttemptNumber == 1 || isFollowLegacyTimeoutPolicy()) {
            deadline += FAILED_ATTEMPT_TIMEOUT_MS;
        } else if (failedUnlockAttemptNumber == 2) {
            deadline += 60000;
        } else if (failedUnlockAttemptNumber == 3) {
            deadline += FAILED_ATTEMPT_TIMEOUT_MS_LEVEL3;
        } else if (failedUnlockAttemptNumber == 4) {
            deadline += 600000;
        } else if (failedUnlockAttemptNumber == 5) {
            deadline += FAILED_ATTEMPT_TIMEOUT_MS_LEVEL5;
        } else if (failedUnlockAttemptNumber > 5) {
            deadline += 3600000;
        }
        Global.putLong(this.mContext.getContentResolver(), "lockscreen.failedUnlockAttemptNumber", failedUnlockAttemptNumber);
        setLong(LOCKOUT_ATTEMPT_DEADLINE, deadline, userId);
        return deadline;
    }

    public long getLockoutAttemptDeadline(int userId) {
        long deadline = getLong(LOCKOUT_ATTEMPT_DEADLINE, 0, userId);
        long timeoutMs = getLong(LOCKOUT_ATTEMPT_TIMEOUT_MS, 0, userId);
        long now = System.currentTimeMillis();
        if (deadline < now && deadline != 0) {
            setLong(LOCKOUT_ATTEMPT_DEADLINE, 0, userId);
            setLong(LOCKOUT_ATTEMPT_TIMEOUT_MS, 0, userId);
            return 0;
        } else if (deadline == 0 || !isFollowLegacyTimeoutPolicy()) {
            if (deadline == 0) {
                return deadline;
            }
            if (timeoutMs != 0 && deadline > now + timeoutMs) {
                setLockoutAttemptDeadlineInAbnormalCase(userId, timeoutMs);
                return deadline;
            } else if (failedUnlockAttemptNumber == 1 && deadline > FAILED_ATTEMPT_TIMEOUT_MS + now) {
                setLockoutAttemptDeadlineInAbnormalCase(userId, FAILED_ATTEMPT_TIMEOUT_MS);
                return deadline;
            } else if (failedUnlockAttemptNumber == 2 && deadline > 60000 + now) {
                setLockoutAttemptDeadlineInAbnormalCase(userId, 60000);
                return deadline;
            } else if (failedUnlockAttemptNumber == 3 && deadline > FAILED_ATTEMPT_TIMEOUT_MS_LEVEL3 + now) {
                setLockoutAttemptDeadlineInAbnormalCase(userId, FAILED_ATTEMPT_TIMEOUT_MS_LEVEL3);
                return deadline;
            } else if (failedUnlockAttemptNumber == 4 && deadline > 600000 + now) {
                setLockoutAttemptDeadlineInAbnormalCase(userId, 600000);
                return deadline;
            } else if (failedUnlockAttemptNumber == 5 && deadline > FAILED_ATTEMPT_TIMEOUT_MS_LEVEL5 + now) {
                setLockoutAttemptDeadlineInAbnormalCase(userId, FAILED_ATTEMPT_TIMEOUT_MS_LEVEL5);
                return deadline;
            } else if (failedUnlockAttemptNumber <= 5 || deadline <= 3600000 + now) {
                return deadline;
            } else {
                setLockoutAttemptDeadlineInAbnormalCase(userId, 3600000);
                return deadline;
            }
        } else if (deadline <= FAILED_ATTEMPT_TIMEOUT_MS + now) {
            return deadline;
        } else {
            setLockoutAttemptDeadlineInAbnormalCase(userId, FAILED_ATTEMPT_TIMEOUT_MS);
            return deadline;
        }
    }

    private long setLockoutAttemptDeadlineInAbnormalCase(int userId, long timeout) {
        long deadline = System.currentTimeMillis() + timeout;
        setLong(LOCKOUT_ATTEMPT_DEADLINE, deadline, userId);
        return deadline;
    }

    public void clearLockoutAttemptDeadline() {
        failedUnlockAttemptNumber = 0;
        Global.putLong(this.mContext.getContentResolver(), "lockscreen.failedUnlockAttemptNumber", 0);
        setLong(LOCKOUT_ATTEMPT_TIMEOUT_MS, 0, getCurrentUser());
        setLong(LOCKOUT_ATTEMPT_DEADLINE, 0, getCurrentUser());
    }

    public boolean isPermanentlyLocked(int userId) {
        return getBoolean(LOCKOUT_PERMANENT_KEY, false, userId);
    }

    public void setPermanentlyLocked(boolean locked) {
        setBoolean(LOCKOUT_PERMANENT_KEY, locked);
    }

    private boolean getBoolean(String secureSettingKey, boolean defaultValue, int userId) {
        try {
            defaultValue = getLockSettings().getBoolean(secureSettingKey, defaultValue, userId);
        } catch (RemoteException e) {
        } catch (Exception e2) {
            Log.e(TAG, "Couldn't read boolean " + secureSettingKey + e2);
        }
        return defaultValue;
    }

    private void setBoolean(String secureSettingKey, boolean enabled, int userId) {
        try {
            getLockSettings().setBoolean(secureSettingKey, enabled, userId);
        } catch (RemoteException re) {
            Log.e(TAG, "Couldn't write boolean " + secureSettingKey + re);
        }
    }

    private void setBoolean(String secureSettingKey, boolean enabled) {
        setBoolean(secureSettingKey, enabled, getCurrentOrCallingUserId());
    }

    private long getLong(String secureSettingKey, long defaultValue, int userHandle) {
        try {
            defaultValue = getLockSettings().getLong(secureSettingKey, defaultValue, userHandle);
        } catch (RemoteException e) {
        } catch (Exception e2) {
            Log.e(TAG, "Couldn't read long " + secureSettingKey + e2);
        }
        return defaultValue;
    }

    private void setLong(String secureSettingKey, long value) {
        setLong(secureSettingKey, value, getCurrentOrCallingUserId());
    }

    private void setLong(String secureSettingKey, long value, int userHandle) {
        try {
            getLockSettings().setLong(secureSettingKey, value, userHandle);
        } catch (RemoteException re) {
            Log.e(TAG, "Couldn't write long " + secureSettingKey + re);
        }
    }

    private String getString(String secureSettingKey, int userHandle) {
        String str = null;
        try {
            str = getLockSettings().getString(secureSettingKey, null, userHandle);
        } catch (RemoteException e) {
        } catch (Exception e2) {
            Log.e(TAG, "Couldn't read String " + secureSettingKey + e2);
        }
        return str;
    }

    private void setString(String secureSettingKey, String value, int userHandle) {
        try {
            getLockSettings().setString(secureSettingKey, value, userHandle);
        } catch (RemoteException re) {
            Log.e(TAG, "Couldn't write string " + secureSettingKey + re);
        }
    }

    public void setPowerButtonInstantlyLocks(boolean enabled, int userId) {
        setBoolean(LOCKSCREEN_POWER_BUTTON_INSTANTLY_LOCKS, enabled, userId);
    }

    public boolean getPowerButtonInstantlyLocks(int userId) {
        return getBoolean(LOCKSCREEN_POWER_BUTTON_INSTANTLY_LOCKS, true, userId);
    }

    public boolean isDevicePasswordSimple(int userId) {
        return Secure.getIntForUser(this.mContentResolver, "is_smpw_key", 0, userId) == 1;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getRecoveryPassword() {
        /*
        r11 = this;
        r10 = 1;
        r5 = 0;
        r4 = 0;
        r8 = 0;
        r0 = r11.mContext;
        r0 = r0.getContentResolver();
        r1 = android.provider.SecurityContract.Tables.PASSWORDS.CONTENT_URI;
        r2 = new java.lang.String[r10];
        r3 = "password";
        r2[r5] = r3;
        r3 = "set_date IS NULL";
        r5 = r4;
        r6 = r0.query(r1, r2, r3, r4, r5);
        if (r6 == 0) goto L_0x002e;
    L_0x001d:
        r0 = r6.getCount();	 Catch:{ Exception -> 0x0056 }
        if (r0 != r10) goto L_0x002b;
    L_0x0023:
        r6.moveToFirst();	 Catch:{ Exception -> 0x0056 }
        r0 = 0;
        r8 = r6.getString(r0);	 Catch:{ Exception -> 0x0056 }
    L_0x002b:
        r6.close();
    L_0x002e:
        if (r8 != 0) goto L_0x0055;
    L_0x0030:
        r8 = r11.generateRecoveryPassword();
        r11.removeRecoveryPasswords();
        r9 = new android.content.ContentValues;
        r9.<init>();
        r0 = "password";
        r9.put(r0, r8);
        r0 = "set_date";
        r4 = (java.lang.Integer) r4;
        r9.put(r0, r4);
        r0 = r11.mContext;
        r0 = r0.getContentResolver();
        r1 = android.provider.SecurityContract.Tables.PASSWORDS.CONTENT_URI;
        r0.insert(r1, r9);
    L_0x0055:
        return r8;
    L_0x0056:
        r7 = move-exception;
        r7.printStackTrace();	 Catch:{ all -> 0x005e }
        r6.close();
        goto L_0x002e;
    L_0x005e:
        r0 = move-exception;
        r6.close();
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.widget.LockPatternUtils.getRecoveryPassword():java.lang.String");
    }

    public void removeRecoveryPasswords() {
        this.mContext.getContentResolver().delete(PASSWORDS.CONTENT_URI, "set_date IS NULL", null);
    }

    private String generateRecoveryPassword() {
        return RandomString.randomstring();
    }

    public void setEnabledTrustAgents(Collection<ComponentName> activeTrustAgents, int userId) {
        StringBuilder sb = new StringBuilder();
        for (ComponentName cn : activeTrustAgents) {
            if (sb.length() > 0) {
                sb.append(',');
            }
            sb.append(cn.flattenToShortString());
        }
        setString(ENABLED_TRUST_AGENTS, sb.toString(), userId);
        getTrustManager().reportEnabledTrustAgentsChanged(userId);
    }

    public List<ComponentName> getEnabledTrustAgents(int userId) {
        String serialized = getString(ENABLED_TRUST_AGENTS, userId);
        if (TextUtils.isEmpty(serialized)) {
            return null;
        }
        String[] split = serialized.split(FingerprintManager.FINGER_PERMISSION_DELIMITER);
        List<ComponentName> activeTrustAgents = new ArrayList(split.length);
        for (String s : split) {
            if (!TextUtils.isEmpty(s)) {
                activeTrustAgents.add(ComponentName.unflattenFromString(s));
            }
        }
        return activeTrustAgents;
    }

    public void requireCredentialEntry(int userId) {
        requireStrongAuth(4, userId);
    }

    public void requireStrongAuth(int strongAuthReason, int userId) {
        try {
            getLockSettings().requireStrongAuth(strongAuthReason, userId);
        } catch (RemoteException e) {
            Log.e(TAG, "Error while requesting strong auth: " + e);
        }
    }

    private void onAfterChangingPassword(int userHandle) {
        getTrustManager().reportEnabledTrustAgentsChanged(userHandle);
    }

    public boolean isCredentialRequiredToDecrypt(boolean defaultValue) {
        int value = Global.getInt(this.mContentResolver, "require_password_to_decrypt", -1);
        if (value == -1) {
            return defaultValue;
        }
        return value != 0;
    }

    public void setCredentialRequiredToDecrypt(boolean required) {
        if (ActivityManager.getCurrentUser() != 0) {
            Log.w(TAG, "Only device owner may call setCredentialRequiredForDecrypt()");
        } else if (isDeviceEncryptionEnabled()) {
            Global.putInt(this.mContext.getContentResolver(), "require_password_to_decrypt", required ? 1 : 0);
        }
    }

    private boolean isDoNotAskCredentialsOnBootSet() {
        return this.mDevicePolicyManager.getDoNotAskCredentialsOnBoot();
    }

    private boolean shouldEncryptWithCredentials(boolean defaultValue) {
        return isCredentialRequiredToDecrypt(defaultValue) && !isDoNotAskCredentialsOnBootSet();
    }

    public void registerStrongAuthTracker(StrongAuthTracker strongAuthTracker) {
        try {
            getLockSettings().registerStrongAuthTracker(strongAuthTracker.mStub);
        } catch (RemoteException e) {
            throw new RuntimeException("Could not register StrongAuthTracker");
        }
    }

    public void unregisterStrongAuthTracker(StrongAuthTracker strongAuthTracker) {
        try {
            getLockSettings().unregisterStrongAuthTracker(strongAuthTracker.mStub);
        } catch (RemoteException e) {
            Log.e(TAG, "Could not unregister StrongAuthTracker", e);
        }
    }

    public static List<UserInfo> getProfiles(int userId) {
        RemoteException e;
        Exception e2;
        List<UserInfo> profiles = null;
        try {
            IUserManager um = IUserManager.Stub.asInterface(ServiceManager.getService(ImsConferenceState.USER));
            if (um == null) {
                return null;
            }
            List<UserInfo> profiles2 = new ArrayList();
            try {
                Bundle versionInfo = PersonaManager.getKnoxInfo();
                if (versionInfo == null || !"2.0".equals(versionInfo.getString("version"))) {
                    return um.getProfiles(userId, false);
                }
                UserInfo uinfo = um.getUserInfo(userId);
                if (uinfo == null || !uinfo.isKnoxWorkspace()) {
                    for (UserInfo pi : um.getProfiles(userId, false)) {
                        if (!(pi == null || pi.isKnoxWorkspace())) {
                            profiles2.add(pi);
                        }
                    }
                } else {
                    profiles2.add(uinfo);
                }
                return profiles2;
            } catch (RemoteException e3) {
                e = e3;
                profiles = profiles2;
                e.printStackTrace();
                return profiles;
            } catch (Exception e4) {
                e2 = e4;
                profiles = profiles2;
                e2.printStackTrace();
                return profiles;
            }
        } catch (RemoteException e5) {
            e = e5;
            e.printStackTrace();
            return profiles;
        } catch (Exception e6) {
            e2 = e6;
            e2.printStackTrace();
            return profiles;
        }
    }

    public boolean savedBackupPinExists() {
        try {
            return getLockSettings().haveBackupPin(getCurrentOrCallingUserId());
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean checkBackupPin(String password, int userId) throws RequestThrottledException {
        try {
            VerifyCredentialResponse response = getLockSettings().checkBackupPin(password, userId);
            if (response.getResponseCode() == 0) {
                return true;
            }
            if (response.getResponseCode() == 1) {
                throw new RequestThrottledException(response.getTimeout());
            }
            Log.e(TAG, "checkBackupPin resCode " + response.getResponseCode());
            return false;
        } catch (RemoteException e) {
            return false;
        }
    }

    public byte[] verifyBackupPin(String password, long challenge, int userId) throws RequestThrottledException {
        try {
            VerifyCredentialResponse response = getLockSettings().verifyBackupPin(password, challenge, userId);
            if (response.getResponseCode() == 0) {
                return response.getPayload();
            }
            if (response.getResponseCode() != 1) {
                return null;
            }
            throw new RequestThrottledException(response.getTimeout());
        } catch (RemoteException e) {
            return null;
        }
    }

    public void saveLockBackupPin(String password, int quality) {
        saveLockBackupPin(password, quality, getCurrentOrCallingUserId());
    }

    public void saveLockBackupPin(String password, int quality, int userHandle) {
        try {
            getLockSettings().setLockBackupPin(password, null, userHandle);
        } catch (RemoteException re) {
            Log.e(TAG, "Unable to save lock Backup PIN " + re);
        }
    }

    public void saveLockBackupPin(String password, String savedPassword, int quality, int userHandle) {
        try {
            getLockSettings().setLockBackupPin(password, savedPassword, userHandle);
        } catch (RemoteException re) {
            Log.e(TAG, "Unable to save lock Backup PIN " + re);
        }
    }

    public boolean getRecoveryScreenLocked() {
        return getBoolean(LOCKOUT_RECOVERY_KEY, false, getCurrentOrCallingUserId());
    }

    public void savePrivateModePassword(String password, List<Cell> pattern, SecPrivateMode mode, int userId) {
        try {
            if (mode == SecPrivateMode.PIN) {
                getLockSettings().setPersonalModeLockPin(password, userId);
            } else if (mode == SecPrivateMode.Password) {
                getLockSettings().setPersonalModeLockPassword(password, userId);
            } else if (mode == SecPrivateMode.Pattern) {
                getLockSettings().setPersonalModeLockPattern(patternToString(pattern), userId);
            } else if (mode == SecPrivateMode.BackupPin) {
                getLockSettings().setPersonalModeLockBackupPin(password, userId);
            }
        } catch (RemoteException re) {
            Log.e(TAG, "Unable to save lock " + mode + " :: " + re);
        }
    }

    public boolean checkPrivateModePassword(String password, List<Cell> pattern, SecPrivateMode mode, int userId) {
        try {
            if (mode == SecPrivateMode.PIN) {
                return getLockSettings().checkPersonalModePin(password, userId);
            }
            if (mode == SecPrivateMode.Password) {
                return getLockSettings().checkPersonalModePassword(password, userId);
            }
            if (mode == SecPrivateMode.Pattern) {
                return getLockSettings().checkPersonalModePattern(patternToString(pattern), userId);
            }
            if (mode == SecPrivateMode.BackupPin) {
                return getLockSettings().checkPersonalModeBackupPin(password, userId);
            }
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean savedPrivateModePasswordExists(SecPrivateMode mode, int userId) {
        try {
            if (mode == SecPrivateMode.PIN) {
                return getLockSettings().havePersonalModePin(userId);
            }
            if (mode == SecPrivateMode.Password) {
                return getLockSettings().havePersonalModePassword(userId);
            }
            if (mode == SecPrivateMode.Pattern) {
                return getLockSettings().havePersonalModePattern(userId);
            }
            if (mode == SecPrivateMode.BackupPin) {
                return getLockSettings().havePersonalModeBackupPin(userId);
            }
            return false;
        } catch (RemoteException e) {
            return false;
        }
    }

    public void savePersonalModeLockPin(String pin) {
        try {
            getLockSettings().setPersonalModeLockPin(pin, getCurrentOrCallingUserId());
        } catch (RemoteException re) {
            Log.e(TAG, "Unable to save lock pin " + re);
        }
    }

    public boolean checkPersonalModeLockPin(String pin) {
        try {
            return getLockSettings().checkPersonalModePin(pin, getCurrentOrCallingUserId());
        } catch (RemoteException e) {
            return true;
        }
    }

    public boolean savedPersonalModePinExists() {
        try {
            return getLockSettings().havePersonalModePin(getCurrentOrCallingUserId());
        } catch (RemoteException e) {
            return false;
        }
    }

    public void savePersonalModeLockPassword(String password) {
        try {
            getLockSettings().setPersonalModeLockPassword(password, getCurrentOrCallingUserId());
        } catch (RemoteException re) {
            Log.e(TAG, "Unable to save lock password " + re);
        }
    }

    public boolean checkPersonalModeLockPassword(String password) {
        try {
            return getLockSettings().checkPersonalModePassword(password, getCurrentOrCallingUserId());
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean savedPersonalModePasswordExists() {
        try {
            return getLockSettings().havePersonalModePassword(getCurrentOrCallingUserId());
        } catch (RemoteException e) {
            return false;
        }
    }

    public void savePersonalModeLockPattern(List<Cell> pattern) {
        try {
            getLockSettings().setPersonalModeLockPattern(patternToString(pattern), getCurrentOrCallingUserId());
        } catch (RemoteException re) {
            Log.e(TAG, "Unable to save lock pattern " + re);
        }
    }

    public boolean checkPersonalModePattern(List<Cell> pattern) {
        try {
            return getLockSettings().checkPersonalModePattern(patternToString(pattern), getCurrentOrCallingUserId());
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean savedPersonalModePatternExists() {
        try {
            return getLockSettings().havePersonalModePattern(getCurrentOrCallingUserId());
        } catch (RemoteException e) {
            return false;
        }
    }

    public void savePersonalModeLockBackupPin(String pin) {
        try {
            getLockSettings().setPersonalModeLockBackupPin(pin, getCurrentOrCallingUserId());
        } catch (RemoteException re) {
            Log.e(TAG, "Unable to save lock pin " + re);
        }
    }

    public boolean checkPersonalModeLockBackupPin(String pin) {
        try {
            return getLockSettings().checkPersonalModeBackupPin(pin, getCurrentOrCallingUserId());
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean savedPersonalModeBackupPinExists() {
        try {
            return getLockSettings().havePersonalModeBackupPin(getCurrentOrCallingUserId());
        } catch (RemoteException e) {
            return false;
        }
    }

    public void savePersonalModeFingerPrintPassword(String password) {
        try {
            getLockSettings().setPersonalModeFingerprintPassword(password, getCurrentOrCallingUserId());
        } catch (RemoteException re) {
            Log.e(TAG, "Unable to save lock password " + re);
        }
    }

    public boolean checkPersonalModeFingerPrintPassword(String password) {
        try {
            return getLockSettings().checkPersonalModeFingerprintPassword(password, getCurrentOrCallingUserId());
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean savedPersonalModeFingerPrintPasswordExists() {
        try {
            return getLockSettings().havePersonalModeFingerprintPassword(getCurrentOrCallingUserId());
        } catch (RemoteException e) {
            return false;
        }
    }

    public void saveAppLockPassword(String password, SecAppLockType lockType, int userId) {
        try {
            if (lockType == SecAppLockType.PIN) {
                getLockSettings().setAppLockPin(password, userId);
            } else if (lockType == SecAppLockType.Password) {
                getLockSettings().setAppLockPassword(password, userId);
            } else if (lockType == SecAppLockType.Pattern) {
                getLockSettings().setAppLockPattern(password, userId);
            } else if (lockType == SecAppLockType.BackupPin) {
                getLockSettings().setAppLockBackupPin(password, userId);
            }
        } catch (RemoteException re) {
            Log.e(TAG, "Unable to save lock " + lockType + " :: " + re);
        }
    }

    public boolean checkAppLockPassword(String password, SecAppLockType lockType, int userId) {
        try {
            if (lockType == SecAppLockType.PIN) {
                return getLockSettings().checkAppLockPin(password, userId);
            }
            if (lockType == SecAppLockType.Password) {
                return getLockSettings().checkAppLockPassword(password, userId);
            }
            if (lockType == SecAppLockType.Pattern) {
                return getLockSettings().checkAppLockPattern(password, userId);
            }
            if (lockType == SecAppLockType.BackupPin) {
                return getLockSettings().checkAppLockBackupPin(password, userId);
            }
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean savedAppLockPasswordExists(SecAppLockType lockType, int userId) {
        try {
            if (lockType == SecAppLockType.PIN) {
                return getLockSettings().haveAppLockPin(userId);
            }
            if (lockType == SecAppLockType.Password) {
                return getLockSettings().haveAppLockPassword(userId);
            }
            if (lockType == SecAppLockType.Pattern) {
                return getLockSettings().haveAppLockPattern(userId);
            }
            if (lockType == SecAppLockType.BackupPin) {
                return getLockSettings().haveAppLockBackupPin(userId);
            }
            return false;
        } catch (RemoteException e) {
            return false;
        }
    }

    public void setAppLockFingerPrintLockscreen(boolean enabled, int userId) {
        setBoolean(APP_LOCK_FINGERPRINT_LOCKSCREEN_KEY, enabled, userId);
    }

    public boolean isAppLockFingerPrintLockscreen(int userId) {
        return getBoolean(APP_LOCK_FINGERPRINT_LOCKSCREEN_KEY, false, userId);
    }

    public boolean savedParentControlPasswordExists() {
        try {
            return getLockSettings().haveParentControlPassword(getCurrentOrCallingUserId());
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean checkParentControlPassword(String password) {
        int userId = getCurrentOrCallingUserId();
        try {
            int length = getLockSettings().getParentControlPasswordHashSize(userId);
            return getLockSettings().checkParentControlPassword(passwordToHash(password, userId), userId);
        } catch (RemoteException e) {
            return false;
        }
    }

    public void saveLockParentControlPassword(String spare, int quality) {
        try {
            getLockSettings().setLockParentControlPassword(passwordToHash(spare, getCurrentOrCallingUserId()), getCurrentOrCallingUserId());
        } catch (RemoteException re) {
            Log.e(TAG, "Unable to save lock parent control password " + re);
        }
    }

    public boolean isParentControlLockEnabled() {
        return false;
    }

    public boolean isFMMLockEnabled() {
        return isFMMLockEnabled(getCurrentOrCallingUserId());
    }

    public boolean isFMMLockEnabled(int userid) {
        try {
            return getLockSettings().haveFMMPassword(userid);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean isCarrierLockEnabled() {
        return false;
    }

    public boolean isCarrierLockPlusEnabled() {
        return isCarrierLockPlusEnabled(getCurrentOrCallingUserId());
    }

    public boolean isCarrierLockPlusEnabled(int userId) {
        try {
            return getLockSettings().getCarrierLockPlusMode(userId);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean updateCarrierLockPlus(int userId) {
        try {
            return getLockSettings().updateCarrierLockPlusMode(userId);
        } catch (RemoteException re) {
            Log.e(TAG, "Unable updateCarrierLockPlus " + re);
            return false;
        }
    }

    private SecretKeySpec getKey(Context context) throws Exception {
        MessageDigest digest = MessageDigest.getInstance(KeyProperties.DIGEST_SHA256);
        digest.update(secretKey.getBytes());
        return new SecretKeySpec(digest.digest(), KeyProperties.KEY_ALGORITHM_AES);
    }

    public String decryptCarrierLockPlusMsg(Context context, String input) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(2, getKey(this.mContext), ivParamSpec);
            return new String(cipher.doFinal(Base64.decode(input, 0)));
        } catch (NoSuchAlgorithmException e) {
            Log.i(TAG, "sec_encrypt.decrypt() NoSuchAlgorithmException = " + e.toString());
            return null;
        } catch (NoSuchPaddingException e2) {
            Log.i(TAG, "sec_encrypt.decrypt() NoSuchPaddingException = " + e2.toString());
            return null;
        } catch (InvalidKeyException e3) {
            Log.i(TAG, "sec_encrypt.decrypt() InvalidKeyException = " + e3.toString());
            return null;
        } catch (InvalidAlgorithmParameterException e4) {
            Log.i(TAG, "sec_encrypt.decrypt() InvalidAlgorithmParameterException = " + e4.toString());
            return null;
        } catch (Exception e5) {
            Log.i(TAG, "sec_encrypt.decrypt() Exception = " + e5.toString());
            return null;
        }
    }

    public long setCarrierLockoutAttemptDeadline(int userId) {
        long deadline = System.currentTimeMillis() + 600000;
        setLong(SKTLOCKOUT_ATTEMPT_DEADLINE, deadline, userId);
        return deadline;
    }

    public long getCarrierLockoutAttemptDeadline(int userId) {
        long deadline = getLong(SKTLOCKOUT_ATTEMPT_DEADLINE, 0, userId);
        if (deadline <= System.currentTimeMillis()) {
            return 0;
        }
        return deadline;
    }

    public void saveRemoteLockPassword(int whichlock, String password) {
        saveRemoteLockPassword(whichlock, password, getCurrentOrCallingUserId());
    }

    public void saveRemoteLockPassword(int whichlock, String password, int userHandle) {
        switch (whichlock) {
            case 0:
                try {
                    getLockSettings().setLockFMMPassword(password, 0);
                    return;
                } catch (RemoteException re) {
                    Log.e(TAG, "Unable to save lock FMM Password " + re);
                    return;
                }
            case 1:
                try {
                    getLockSettings().setLockCarrierPassword(password, userHandle);
                    return;
                } catch (RemoteException re2) {
                    Log.e(TAG, "Unable to save lock Carrier Password " + re2);
                    return;
                }
            default:
                return;
        }
    }

    public boolean checkRemoteLockPassword(int whichlock, String password, int userHandle) {
        if (whichlock == 0) {
            try {
                return getLockSettings().checkFMMPassword(Base64.encodeToString(MessageDigest.getInstance(KeyProperties.DIGEST_SHA1).digest(password.getBytes()), 2), userHandle);
            } catch (RemoteException re) {
                Log.e(TAG, "Unable to save lock (" + whichlock + ") Password " + re);
                return false;
            } catch (NoSuchAlgorithmException e) {
                Log.w(TAG, "Failed to encode string because of missing algorithm: SHA-1");
                return false;
            }
        } else if (whichlock == 1) {
            return getLockSettings().checkCarrierPassword(password, userHandle);
        } else {
            return true;
        }
    }

    public boolean isCarrierPasswordSaved() {
        try {
            return getLockSettings().haveCarrierPassword(getCurrentOrCallingUserId());
        } catch (RemoteException e) {
            return false;
        }
    }

    public void setSwipeSmartLock(boolean enabled) {
        mSwipeSmartLock = enabled;
    }

    public boolean isSwipeSmartLock() {
        return mSwipeSmartLock;
    }

    public void setContainerPasswordState(String password, int containerId) {
        DevicePolicyManager dpm = getDevicePolicyManager();
        int letters = 0;
        int uppercase = 0;
        int lowercase = 0;
        int numbers = 0;
        int symbols = 0;
        int nonletter = 0;
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (c >= DateFormat.CAPITAL_AM_PM && c <= 'Z') {
                letters++;
                uppercase++;
            } else if (c >= DateFormat.AM_PM && c <= DateFormat.TIME_ZONE) {
                letters++;
                lowercase++;
            } else if (c < '0' || c > '9') {
                symbols++;
                nonletter++;
            } else {
                numbers++;
                nonletter++;
            }
        }
        dpm.setAlternativePasswordState(password.length(), letters, uppercase, lowercase, numbers, symbols, nonletter, containerId);
        if (getActivePasswordQuality(containerId) == 397312) {
            dpm.copyAlternativeToActivePasswordState(containerId);
        }
    }

    public void setFingerPrintLockscreen(boolean enabled, int userId) {
        setLong(ENABLED_FINGERPRINT_LOCKSCREEN_KEY, (long) (enabled ? 1 : 0), userId);
    }

    public void setFingerPrintLockscreen(int status, int userId) {
        setLong(ENABLED_FINGERPRINT_LOCKSCREEN_KEY, (long) status, userId);
    }

    public boolean isFingerPrintLockscreen(int userId) {
        return getLong(ENABLED_FINGERPRINT_LOCKSCREEN_KEY, 0, userId) != 0;
    }

    public int getFingerPrintLockscreenState(int userId) {
        return (int) getLong(ENABLED_FINGERPRINT_LOCKSCREEN_KEY, 0, userId);
    }

    public boolean isFingerPrintLockscreen(int mode, int userId) {
        boolean isFingerPrintEnabled;
        if (mode == 397312) {
            isFingerPrintEnabled = true;
        } else {
            isFingerPrintEnabled = false;
        }
        return isFingerPrintEnabled && isFingerPrintLockscreen(userId);
    }

    public void saveLockFingerprint(int userHandle) {
        setLockFingerprintEnabled(true, userHandle);
        setPrevLockFingerprintEnabled(true, userHandle);
        setLong(PASSWORD_TYPE_KEY, 397312, userHandle);
        getDevicePolicyManager().copyAlternativeToActivePasswordState(userHandle);
    }

    public void setPrivateModeFingerPrintLockscreen(boolean enabled, int userId) {
        setBoolean(PRIVATE_MODE_FINGERPRINT_LOCKSCREEN_KEY, enabled, userId);
    }

    public void saveLockFingerprintPassword(String password, int userId) {
        saveLockFingerprintPasswordwithoutQuality(password, userId);
    }

    public void saveLockFingerprintPassword(String password, int userId, int quality, boolean skipUpdateEncryptionPassword) {
        saveLockFingerprintPassword(password, null, userId, quality, skipUpdateEncryptionPassword);
    }

    public void saveLockFingerprintPassword(String password, String savedpassword, int userId, int quality, boolean skipUpdateEncryptionPassword) {
        try {
            DevicePolicyManager dpm = getDevicePolicyManager();
            if (password != null) {
                getLockSettings().setLockBackupPassword(password, savedpassword, userId, true);
                if (userId == 0 && userId == UserHandle.myUserId() && !skipUpdateEncryptionPassword) {
                    updateEncryptionPassword(0, password);
                    if (DirEncryptionManager.isEncryptionFeatureEnabled()) {
                        DirEncryptionManager dem = new DirEncryptionManager(this.mContext);
                        dem.setNeedToCreateKey(true);
                        dem.setPassword(password);
                        dpm.setNeedToGetAlternativePasswdForODE(null, false);
                    }
                }
                int computedQuality = computePasswordQuality(password);
                setLong(PASSWORD_TYPE_ALTERNATE_KEY, (long) Math.max(quality, computedQuality));
                if (computedQuality != 0) {
                    setContainerPasswordState(password, userId);
                } else {
                    dpm.setActivePasswordState(0, 0, 0, 0, 0, 0, 0, 0, getCurrentOrCallingUserId());
                }
                if (!CCManager.isMdfEnforced()) {
                    String passwordHistory = getString(PASSWORD_HISTORY_KEY, getCurrentOrCallingUserId());
                    if (passwordHistory == null) {
                        passwordHistory = new String();
                    }
                    int passwordHistoryLength = getRequestedPasswordHistoryLength(userId);
                    if (passwordHistoryLength == 0) {
                        passwordHistory = "";
                    } else {
                        byte[] hash = passwordToHash(password, userId);
                        if (!passwordHistory.contains(new String(hash))) {
                            passwordHistory = new String(hash) + FingerprintManager.FINGER_PERMISSION_DELIMITER + passwordHistory;
                        }
                        passwordHistory = passwordHistory.substring(0, Math.min(((hash.length * passwordHistoryLength) + passwordHistoryLength) - 1, passwordHistory.length()));
                    }
                    setString(PASSWORD_HISTORY_KEY, passwordHistory, getCurrentOrCallingUserId());
                    return;
                }
                return;
            }
            dpm.setActivePasswordState(0, 0, 0, 0, 0, 0, 0, 0, getCurrentOrCallingUserId());
        } catch (Exception re) {
            Log.e(TAG, "Unable to save Fingerprint lock password " + re);
        }
    }

    private void saveLockFingerprintPasswordwithoutQuality(String password, int userId) {
        saveLockFingerprintPasswordwithoutQuality(password, null, userId);
    }

    private void saveLockFingerprintPasswordwithoutQuality(String password, String savedpassword, int userId) {
        try {
            DevicePolicyManager dpm = getDevicePolicyManager();
            if (password != null) {
                getLockSettings().setLockBackupPassword(password, savedpassword, userId, false);
                if (computePasswordQuality(password) != 0) {
                    setContainerPasswordState(password, userId);
                    return;
                } else {
                    dpm.setActivePasswordState(0, 0, 0, 0, 0, 0, 0, 0, getCurrentOrCallingUserId());
                    return;
                }
            }
            dpm.setActivePasswordState(0, 0, 0, 0, 0, 0, 0, 0, getCurrentOrCallingUserId());
        } catch (Exception re) {
            Log.e(TAG, "Unable to save Fingerprint lock password " + re);
        }
    }

    public boolean checkFingerprintPassword(String password, int userId) {
        return checkFingerprintPassword(password, userId, false);
    }

    public boolean checkFingerprintPassword(String password, int userId, boolean useKeystore) {
        try {
            VerifyCredentialResponse response = getLockSettings().checkBackupPassword(password, userId, useKeystore);
            Log.e(TAG, "checkBackupPassword resCode " + response.getResponseCode());
            if (response.getResponseCode() == 0) {
                return true;
            }
            Log.e(TAG, "checkBackupPassword resCode " + response.getResponseCode());
            return false;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean savedFingerprintPasswordExists(int userId) {
        try {
            return getLockSettings().haveBackupPassword(userId);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean isLockFingerprintEnabled(int userId) {
        return getBoolean(ENABLED_FINGERPRINT_LOCKSCREEN_KEY, false, userId) && getLong(PASSWORD_TYPE_KEY, 397312, userId) == 397312;
    }

    public void setLockFingerprintEnabled(boolean enabled, int userId) {
        setBoolean(ENABLED_FINGERPRINT_LOCKSCREEN_KEY, enabled, userId);
    }

    public boolean isPrevLockFingerprintEnabled(int userId) {
        return getBoolean("prev_lock_type", false, userId);
    }

    public void setPrevLockFingerprintEnabled(boolean enabled, int userId) {
        setBoolean("prev_lock_type", enabled, userId);
    }

    public boolean isPrivateModeFingerPrintLockscreen(int userId) {
        return getBoolean(PRIVATE_MODE_FINGERPRINT_LOCKSCREEN_KEY, false, userId);
    }

    public boolean getPasswordRecoverable(int userHandle) {
        return getDevicePolicyManager().getPasswordRecoverable(null, userHandle);
    }

    public void recoverPassword(int userHandle) {
        getDevicePolicyManager().recoverPassword(userHandle);
    }

    public void setRecoveryScreenLocked(boolean locked) {
        setBoolean(LOCKOUT_RECOVERY_KEY, locked);
    }

    private SmartCardPinManager getSmartcardPinManager() {
        mSmartcardPinMgr = SmartCardPinManagerFactory.getInstance().getSmartcardPinMgr(this.mContext.getApplicationContext() != null ? this.mContext.getApplicationContext() : this.mContext, new UserHandle(getCurrentUser()));
        return mSmartcardPinMgr;
    }

    public boolean isCACPasswordEnabled() {
        return getBoolean(SMARTCARD_TYPE_KEY, false, getCurrentUser());
    }

    public boolean setCACPasswordEnabled(boolean status) {
        setBoolean(SMARTCARD_TYPE_KEY, status);
        return true;
    }

    public void initializeCACAuthentication() {
        Log.i(TAG, "initializeCACAuthentication: ");
        if (isSmartcardAuthInstalled()) {
            getSmartcardPinManager();
        }
    }

    private SmartCardPinManager getSmartcardPinManager_Sync() {
        mSmartcardPinMgr = SmartCardPinManagerFactory.getInstance().getSmartcardPinMgr_Sync(this.mContext.getApplicationContext() != null ? this.mContext.getApplicationContext() : this.mContext, new UserHandle(getCurrentUser()));
        return mSmartcardPinMgr;
    }

    public void initializeCACAuthentication_Sync() {
        Log.i(TAG, "initializeCACAuthentication: ");
        if (isSmartcardAuthInstalled()) {
            getSmartcardPinManager_Sync();
        }
    }

    public void deinitializeCACAuthentication() {
        Log.i(TAG, "deinitializeCACAuthentication: ");
        SmartCardPinManagerFactory.getInstance().deinitializeCAC(new UserHandle(getCurrentUser()));
    }

    public int checkPasswordWithCAC(String password) {
        Log.i(TAG, "checkPasswordWithCAC: ");
        mIsCallbackCalled = false;
        final ConditionVariable cv = new ConditionVariable();
        try {
            getSmartcardPinManager().verifyCard(password.toCharArray(), new verifyCardCallback() {
                public void onComplete(int status) {
                    Log.i(LockPatternUtils.TAG, "checkPasswordWithCAC: onComplete " + status);
                    LockPatternUtils.mScVerifyStatus = status;
                    LockPatternUtils.mIsCallbackCalled = true;
                    cv.open();
                }
            }.mVerifyCallback);
            if (!mIsCallbackCalled) {
                cv.block();
            }
            Log.i(TAG, "checkPasswordWithCAC: " + mScVerifyStatus);
            return mScVerifyStatus;
        } catch (Exception e) {
            e.printStackTrace();
            return 5;
        }
    }

    public void showCardNotRegisteredDialog() {
        Intent intent = new Intent("com.sec.smartcard.pinservice.CARD_NOT_REGISTERED_ERROR_DIALOG");
        intent.addFlags(268435456);
        try {
            Log.d(TAG, "showCardNotRegisteredDialog called ");
            this.mContext.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.d(TAG, "showCardNotRegisteredDialog ", e);
        }
    }

    public boolean isCACCardRegistered() {
        boolean ret = false;
        try {
            ret = SmartCardPinManager.isCardRegistered(this.mContext);
        } catch (Exception e) {
        }
        Log.i(TAG, "isCardRegistered: " + ret);
        return ret;
    }

    public boolean isDeviceConnectedWithCACCard() {
        boolean ret = getSmartcardPinManager().isDeviceConnectedWithCard();
        Log.i(TAG, "isCACCardRegistred: " + ret);
        return ret;
    }

    public boolean isSmartcardAuthInstalled() {
        boolean ret = SmartCardPinManager.isSmartCardAuthenticationInstalled(this.mContext);
        Log.i(TAG, "isSmartCardAuthenticationAvailable: " + ret);
        return ret;
    }

    public boolean isSmartCardPasswordEnabled() {
        if (getLong(PASSWORD_TYPE_KEY, 0, getCurrentUser()) == 458752) {
            return true;
        }
        return false;
    }

    private void updateCACAuthentication() {
        if (isCACPasswordEnabled() && !cacPasswordSetProgress) {
            this.mContext.sendBroadcast(new Intent().setAction("com.sec.smartcard.pinservice.action.SMARTCARD_PIN_REMOVED"));
            Log.i(TAG, "updateCACAuthentication: send broadcast");
            cacPasswordSetProgress = false;
        }
    }

    private void update_lockscreen_type(int quality) {
        Intent intent = new Intent().setAction("com.sec.smartcard.pinservice.action.SMARTCARD_LOCKTYPE_CHANGED");
        if (quality == 458752) {
            intent.putExtra("Type", "Smartcard");
        } else {
            intent.putExtra("Type", "Other");
        }
        this.mContext.sendBroadcast(intent);
        Log.i(TAG, "updateCACAuthentication: send broadcast " + quality);
        cacPasswordSetProgress = false;
    }

    private void updateCACAuthentication(int quality) {
        boolean z;
        boolean z2 = true;
        Log.i(TAG, "updateCACAuthentication " + quality);
        updateCACAuthentication();
        update_lockscreen_type(quality);
        if (quality == 458752) {
            z = true;
        } else {
            z = false;
        }
        setCACPasswordEnabled(z);
        String str = TAG;
        StringBuilder append = new StringBuilder().append("setCACPasswordEnabled: ");
        if (quality != 458752) {
            z2 = false;
        }
        Log.i(str, append.append(z2).toString());
    }

    public void saveLockBackupPassword(String password, String savedPassword, int userHandle) {
        try {
            getLockSettings().setLockBackupPassword(password, savedPassword, userHandle, false);
        } catch (RemoteException re) {
            Log.e(TAG, "Unable to save lock Backup PIN " + re);
        }
    }

    public boolean savedBackupPasswordExists(int userId) {
        try {
            return getLockSettings().haveBackupPassword(userId);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean checkBackupPassword(String password, int userId, boolean useKeystore) throws RequestThrottledException {
        try {
            VerifyCredentialResponse response = getLockSettings().checkBackupPassword(password, userId, useKeystore);
            if (response.getResponseCode() == 0) {
                return true;
            }
            if (response.getResponseCode() == 1) {
                throw new RequestThrottledException(response.getTimeout());
            }
            Log.e(TAG, "checkBackupPassword resCode " + response.getResponseCode());
            return false;
        } catch (RemoteException e) {
            return false;
        }
    }

    public byte[] verifyBackupPassword(String password, long challenge, int userId, boolean useKeystore) throws RequestThrottledException {
        try {
            VerifyCredentialResponse response = getLockSettings().verifyBackupPassword(password, challenge, userId, useKeystore);
            if (response.getResponseCode() == 0) {
                return response.getPayload();
            }
            if (response.getResponseCode() != 1) {
                return null;
            }
            throw new RequestThrottledException(response.getTimeout());
        } catch (RemoteException e) {
            return null;
        }
    }
}
