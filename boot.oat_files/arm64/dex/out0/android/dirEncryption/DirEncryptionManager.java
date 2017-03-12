package android.dirEncryption;

import android.content.Context;
import android.media.AudioParameter;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.storage.IDirEncryptService;
import android.os.storage.IDirEncryptService.Stub;
import android.os.storage.IDirEncryptServiceListener;
import android.util.Log;

public class DirEncryptionManager {
    public static final String ADMIN_START = "adminStart";
    public static final int CHANGE_ECRYPTFS_PASSWORD = 11;
    private static final boolean DEBUG = true;
    public static final int DECRYPT = 3;
    private static final boolean DIR_ENCRYPTION = SystemProperties.get("ro.sec.fle.encryption", AudioParameter.AUDIO_PARAMETER_VALUE_false).equals(AudioParameter.AUDIO_PARAMETER_VALUE_true);
    public static final int ENCRYPT = 2;
    public static final int ENCRYPT_FULL_OFF = 5;
    public static final int ENCRYPT_FULL_ON = 4;
    public static final int ERR_FEATURE_UNAVAILABLE = 200;
    public static final int ERR_INVALID_PARAMETER = 203;
    public static final int ERR_INVALID_PERMISSION = 204;
    public static final int ERR_NOK = 201;
    public static final int ERR_SD_NOT_MOUNTED = 202;
    public static final int EXCL_MEDIA_OFF = 7;
    public static final int EXCL_MEDIA_ON = 6;
    public static final String FLE_KEY_STORE = "/efs/";
    public static final String FLE_KEY_STORE_LEGACY = "/data/system/";
    public static final String INTERNAL_STORAGE_PATH = "/mnt/sdcard";
    public static final int MOUNT_PATH_STATUS = 12;
    public static final String MOVE_MOUNT = "MoveMount";
    private static final int MSG_BASE = 0;
    private static final int MSG_ERR_BASE = 200;
    public static final String NAME = "DirEncryptService";
    public static final int OK = 13;
    public static final int POLICY_ALREADY_SET = 10;
    public static final int POLICY_CAN_NOT_BE_SET_UNDER_BUSY_STATE = 15;
    public static final int POLICY_NOT_SAVED = 9;
    public static final int POLICY_SAVED = 8;
    public static final String SD_CARD_ENCRYPTION_ACTION = "com.sec.app.action.START_SDCARD_ENCRYPTION";
    public static int SECURITY_POLICY_NOTIFICATION_ID = -889275714;
    public static final String STATUS_BUSY = "busy";
    public static final String STATUS_DONE = "done";
    public static final String STATUS_FREE = "free";
    public static final String STATUS_MOUNT = "Mount";
    private static final String TAG = "DirEncryptionManager";
    public static final String VOLUME_STATE_HIDDEN = "HiddenMount";
    private static boolean mPolicyChanged = false;
    private Context mContext = null;
    private DirEncryptionWrapper mDew = null;
    private IDirEncryptService m_InstDirEncSvc = null;

    public static final class Error {
        public static final int CHANGE_ECRYPTFS_PASSWORD = 9;
        public static final int DECRYPT = 6;
        public static final int ENCRYPT = 5;
        public static final int FILE_OPEN = 11;
        public static final int MOUNT = 7;
        public static final int MOUNT_PATH_STATUS = 10;
        public static final int NO = 0;
        public static final int PRESCAN_FULL = 4;
        public static final int PWD_CREATE = 1;
        public static final int PWD_DELETE = 3;
        public static final int PWD_UPDATE = 2;
        public static final int UNMOUNT = 8;

        private Error() {
        }
    }

    public static final class Status {
        public static final int DECRYPTING = 3;
        public static final int ENCRYPTING = 2;
        public static final int FREE = 0;
        public static final int READY = 1;

        private Status() {
        }
    }

    private static void log(String msg) {
        Log.d(TAG, msg);
    }

    public DirEncryptionManager(Context context) {
        this.mContext = context;
        this.mDew = new DirEncryptionWrapper(this.mContext);
        this.m_InstDirEncSvc = Stub.asInterface(ServiceManager.getService("DirEncryptService"));
        if (this.m_InstDirEncSvc == null) {
            log("Unable to get DirEncryptService instance.");
        }
        if (!DIR_ENCRYPTION) {
            log("Dir Encryption not available");
            this.m_InstDirEncSvc = null;
        }
    }

    public void registerListener(IDirEncryptServiceListener listener) {
        if (this.m_InstDirEncSvc != null) {
            try {
                this.m_InstDirEncSvc.registerListener(listener);
            } catch (RemoteException e) {
                log("Unable to communicate with DirEncryptService");
            }
        }
    }

    public void unregisterListener(IDirEncryptServiceListener listener) {
        if (this.m_InstDirEncSvc != null) {
            try {
                this.m_InstDirEncSvc.unregisterListener(listener);
            } catch (RemoteException e) {
                log("Unable to communicate with DirEncryptService");
            }
        }
    }

    public static boolean isEncryptionFeatureEnabled() {
        if (DIR_ENCRYPTION) {
            return true;
        }
        return false;
    }

    public boolean isEncryptionSupported() {
        if (DIR_ENCRYPTION && getCurrentUserID() == 0) {
            return true;
        }
        return false;
    }

    public boolean isExternalSDRemovable() {
        return this.mDew.isExternalSDRemovable();
    }

    public String getExternalSdPath() {
        return this.mDew.getExternalSdPath();
    }

    public String getVolumeState() {
        return this.mDew.getVolumeState();
    }

    public int getKeyguardStoredPasswordQuality() {
        return this.mDew.getKeyguardStoredPasswordQuality();
    }

    public boolean mountVolume() {
        return this.mDew.mountVolume();
    }

    public boolean unmountVolumeByDiffUser() {
        SDCardEncryptionPolicies sdPolicies = getSDCardEncryptionPrefs();
        if (sdPolicies == null) {
            sdPolicies = new SDCardEncryptionPolicies();
        }
        if (2 == sdPolicies.mEnc) {
            return this.mDew.unmountVolumeByDiffUser();
        }
        return false;
    }

    public void setUserDiff(boolean in) {
        this.mDew.setUserDiff(in);
    }

    public boolean getUserDiff() {
        return this.mDew.getUserDiff();
    }

    public void setSavedUserID(int in) {
        this.mDew.setSavedUserID(in);
    }

    public int getSavedUserID() {
        return this.mDew.getSavedUserID();
    }

    public int getCurrentUserID() {
        return this.mDew.getCurrentUserID();
    }

    public void updateSDcardState() {
        this.mDew.updateSDcardState();
    }

    public void setNeedToCreateKey(boolean in) {
        if (isEncryptionSupported() && this.m_InstDirEncSvc != null) {
            try {
                this.m_InstDirEncSvc.setNeedToCreateKey(in);
            } catch (RemoteException e) {
                log("Unable to communicate with DirEncryptService");
            }
        }
    }

    public void revertSecureStorageForKnoxMigration(String password, int container_id) {
        if (this.m_InstDirEncSvc == null) {
            log("No DirEncSvc for Knox migration.");
            return;
        }
        try {
            this.m_InstDirEncSvc.revertSecureStorageForKnoxMigration(password, container_id);
        } catch (RemoteException e) {
            log("Unable to communicate with DirEncryptService");
        }
    }

    public void SetMountSDcardToHelper(boolean in) {
        if (isEncryptionSupported() && this.m_InstDirEncSvc != null) {
            try {
                this.m_InstDirEncSvc.SetMountSDcardToHelper(in);
            } catch (RemoteException e) {
                log("Unable to communicate with DirEncryptService");
            }
        }
    }

    public int setStorageCardEncryptionPolicy(int encType, int fullEnc, int excludeMediaFiles) {
        int result = 200;
        if (!isEncryptionSupported()) {
            return result;
        }
        if (this.m_InstDirEncSvc == null) {
            return result;
        }
        try {
            result = this.m_InstDirEncSvc.setStorageCardEncryptionPolicy(encType, fullEnc, excludeMediaFiles);
        } catch (RemoteException e) {
            log("Unable to communicate with DirEncryptService");
        }
        log("setStorageCardEncryptionPolicy result : " + result);
        if (result == 8 || result == 10) {
            log("result : POLICY_SAVED || POLICY_ALREADY_SET");
            unmountSDCardByAdmin();
        }
        setPolicyChanged(true);
        return result;
    }

    public boolean isStorageCardEncryptionPoliciesApplied() {
        boolean result = false;
        if (!isEncryptionSupported()) {
            return 0;
        }
        if (this.m_InstDirEncSvc == null) {
            return 0;
        }
        try {
            result = this.m_InstDirEncSvc.isStorageCardEncryptionPoliciesApplied() == 1;
        } catch (RemoteException e) {
            log("Unable to communicate with DirEncryptService");
        }
        return result;
    }

    public int setPassword(String password) {
        int result = 200;
        if (!isEncryptionSupported()) {
            return result;
        }
        if (this.m_InstDirEncSvc == null) {
            return result;
        }
        try {
            result = this.m_InstDirEncSvc.setPassword(password);
        } catch (RemoteException e) {
            log("Unable to communicate with DirEncryptService");
        }
        return result;
    }

    public int encryptStorage(String path) {
        int result = 200;
        if (this.m_InstDirEncSvc == null) {
            return result;
        }
        try {
            result = this.m_InstDirEncSvc.encryptStorage(path);
        } catch (RemoteException e) {
            log("Unable to communicate with DirEncryptService");
        }
        return result;
    }

    public int getCurrentStatus() {
        int result = 200;
        if (!isEncryptionSupported()) {
            return result;
        }
        if (this.m_InstDirEncSvc == null) {
            return result;
        }
        try {
            result = this.m_InstDirEncSvc.getCurrentStatus();
        } catch (RemoteException e) {
            log("Unable to communicate with DirEncryptService");
        }
        return result;
    }

    public int getLastError() {
        int result = 200;
        if (!isEncryptionSupported()) {
            return result;
        }
        if (this.m_InstDirEncSvc == null) {
            return result;
        }
        try {
            result = this.m_InstDirEncSvc.getLastError();
        } catch (RemoteException e) {
            log("Unable to communicate with DirEncryptService");
        }
        return result;
    }

    public int getAdditionalSpaceRequired() {
        int result = 200;
        if (!isEncryptionSupported()) {
            return result;
        }
        if (this.m_InstDirEncSvc == null) {
            return result;
        }
        try {
            result = this.m_InstDirEncSvc.getAdditionalSpaceRequired();
        } catch (RemoteException e) {
            log("Unable to communicate with DirEncryptService");
        }
        return result;
    }

    public SDCardEncryptionPolicies getSDCardEncryptionPrefs() {
        if (this.m_InstDirEncSvc == null) {
            return null;
        }
        SDCardEncryptionPolicies policies = null;
        try {
            return this.m_InstDirEncSvc.getSDCardEncryptionPrefs();
        } catch (RemoteException e) {
            log("Unable to communicate with DirEncryptService");
            return policies;
        }
    }

    public void unmountSDCardByAdmin() {
        if (this.m_InstDirEncSvc != null) {
            try {
                this.m_InstDirEncSvc.unmountSDCardByAdmin();
            } catch (RemoteException e) {
                log("Unable to communicate with DirEncryptService");
            }
        }
    }

    public int setNullSDCardPassword() {
        int result = 200;
        if (this.m_InstDirEncSvc == null) {
            return result;
        }
        try {
            result = this.m_InstDirEncSvc.setNullSDCardPassword();
        } catch (RemoteException e) {
            log("Unable to communicate with DirEncryptService");
        }
        return result;
    }

    public void setPolicyChanged(boolean in) {
        mPolicyChanged = in;
    }

    public boolean getPolicyChanged() {
        return mPolicyChanged;
    }
}
