package android.dirEncryption;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.os.storage.IMountService;
import android.os.storage.IMountService.Stub;
import android.os.storage.StorageEventListener;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.util.Slog;
import com.android.internal.widget.LockPatternUtils;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DirEncryptionWrapper {
    private static final String CRYPT_PREFERENCES_FILE = "DirEncryption.Pref";
    private static final boolean LOCAL_LOGD = Build.TYPE.contains("eng");
    private static final boolean LOCAL_LOGE = Build.TYPE.contains("eng");
    private static final String TAG = "DirEncryptWrapper";
    private static String mExternalSDvolFsUuid = null;
    private static String mExternalSDvolId = null;
    private static String mExternalSDvolState = null;
    private static int mSavedUserId = 0;
    private static boolean mUserDiff = false;
    private Context mContext = null;
    private IMountService mMountService = null;
    private StorageManager mStorageManager = null;

    private static void logD(String msg) {
        if (LOCAL_LOGD) {
            Slog.d(TAG, msg);
        }
    }

    private static void logE(String msg) {
        if (LOCAL_LOGE) {
            Slog.e(TAG, msg);
        }
    }

    private StorageVolume[] getVolumeList() {
        StorageManager storageManager = null;
        try {
            storageManager = (StorageManager) this.mContext.getSystemService(Context.STORAGE_SERVICE);
        } catch (Exception e) {
            logE("Exception:: unable to get Storage Service");
            e.printStackTrace();
        }
        if (storageManager == null) {
            return null;
        }
        return storageManager.getVolumeList();
    }

    private String getSubSystem(StorageVolume storageVolume) {
        return storageVolume.getSubSystem();
    }

    public DirEncryptionWrapper(Context context) {
        this.mContext = context;
    }

    public IMountService getMountService() {
        if (this.mMountService == null) {
            IBinder service = ServiceManager.getService("mount");
            if (service != null) {
                this.mMountService = Stub.asInterface(service);
            } else {
                logD("Can't get mount service");
            }
        }
        return this.mMountService;
    }

    public boolean mountVolume() {
        try {
            getMountService().mountVolume(getExternalSdPath());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean unmountVolume() {
        try {
            getMountService().unmountVolume(getExternalSdPath(), true, false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean unmountHiddenVolume() {
        try {
            getMountService().unmountVolume(getExternalSdPath() + " hidden", true, false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getVolumeState() {
        StorageManager storageManager = (StorageManager) this.mContext.getSystemService(Context.STORAGE_SERVICE);
        String sdPath = getExternalSdPath();
        if (storageManager == null || sdPath == null) {
            return null;
        }
        return storageManager.getVolumeState(sdPath);
    }

    public int getActivePasswordQuality() {
        return new LockPatternUtils(this.mContext).getActivePasswordQuality(getCurrentUserID());
    }

    public int getKeyguardStoredPasswordQuality() {
        return new LockPatternUtils(this.mContext).getKeyguardStoredPasswordQuality(getCurrentUserID());
    }

    public boolean isSecure() {
        return new LockPatternUtils(this.mContext).isSecure(getCurrentUserID());
    }

    public boolean isExternalSDRemovable() {
        StorageVolume[] storageVolumes = getVolumeList();
        if (storageVolumes == null) {
            return false;
        }
        for (StorageVolume storageVolume : storageVolumes) {
            String subsystem = getSubSystem(storageVolume);
            if (subsystem != null && subsystem.equals("sd") && storageVolume.isRemovable()) {
                return true;
            }
        }
        return false;
    }

    public String getExternalSdPath() {
        StorageVolume[] storageVolumes = getVolumeList();
        if (storageVolumes == null) {
            return null;
        }
        for (StorageVolume storageVolume : storageVolumes) {
            String subsystem = getSubSystem(storageVolume);
            if (subsystem != null && subsystem.equals("sd") && storageVolume.isRemovable()) {
                return storageVolume.getPath();
            }
        }
        return null;
    }

    public boolean registerStorageEventListener(StorageEventListener listner) {
        if (this.mStorageManager != null) {
            return false;
        }
        this.mStorageManager = (StorageManager) this.mContext.getSystemService(Context.STORAGE_SERVICE);
        if (this.mStorageManager == null) {
            return false;
        }
        this.mStorageManager.registerListener(listner);
        return true;
    }

    public int getCurrentUserID() {
        return UserHandle.myUserId();
    }

    public void setUserDiff(boolean in) {
        mUserDiff = in;
    }

    public boolean getUserDiff() {
        return mUserDiff;
    }

    public void setSavedUserID(int in) {
        mSavedUserId = in;
    }

    public int getSavedUserID() {
        return mSavedUserId;
    }

    public void setExternalSDvolId(String volId) {
        mExternalSDvolId = volId;
    }

    public String getExternalSDvolId() {
        return mExternalSDvolId;
    }

    public void setExternalSDvolFsUuid(String volFsUuid) {
        mExternalSDvolFsUuid = volFsUuid;
    }

    public String getExternalSDvolFsUuid() {
        return mExternalSDvolFsUuid;
    }

    public void setExternalSDvolState(String volState) {
        mExternalSDvolState = volState;
    }

    public String getExternalSDvolState() {
        return mExternalSDvolState;
    }

    public boolean unmountVolumeByDiffUser() {
        String state = getExternalSDvolState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mUserDiff = true;
            return unmountVolume();
        } else if (DirEncryptionManager.VOLUME_STATE_HIDDEN.equals(state)) {
            mUserDiff = true;
            return unmountHiddenVolume();
        } else if ((Environment.MEDIA_UNMOUNTED.equals(state) || Environment.MEDIA_BAD_REMOVAL.equals(state)) && getCurrentUserID() == 0) {
            return mountVolume();
        } else {
            return false;
        }
    }

    public void updateSDcardState() {
        if (getSavedUserID() != getCurrentUserID() && (getSavedUserID() == 0 || getCurrentUserID() == 0)) {
            SDCardEncryptionPolicies sdPolicies = getSDCardEncryptionPrefs();
            if (sdPolicies == null) {
                sdPolicies = new SDCardEncryptionPolicies();
            }
            if (2 == sdPolicies.mEnc) {
                unmountVolumeByDiffUser();
            }
        }
        setSavedUserID(getCurrentUserID());
    }

    private SDCardEncryptionPolicies getSDCardEncryptionPrefs() {
        if (haveEncPrefs()) {
            logD("EncPrefs found");
            return restorePrefs();
        }
        logE("EncPrefs not found");
        return null;
    }

    private boolean haveEncPrefs() {
        if (new File(DirEncryptionManager.FLE_KEY_STORE, CRYPT_PREFERENCES_FILE).exists()) {
            return true;
        }
        return false;
    }

    private SDCardEncryptionPolicies restorePrefs() {
        IOException e;
        Throwable th;
        SDCardEncryptionPolicies policies = new SDCardEncryptionPolicies();
        File f = new File(DirEncryptionManager.FLE_KEY_STORE, CRYPT_PREFERENCES_FILE);
        if (f.canRead() && f.exists()) {
            FileReader fileReader = null;
            try {
                FileReader reader = new FileReader(f);
                try {
                    char[] buf = new char[((int) f.length())];
                    if (reader.read(buf) > 0) {
                        policies = SDCardEncryptionPolicies.unflattenFromString(new String(buf));
                    } else {
                        logE("There is no data to read for enc properties.");
                    }
                    try {
                        reader.close();
                        fileReader = reader;
                    } catch (Exception e2) {
                        fileReader = reader;
                    }
                } catch (IOException e3) {
                    e = e3;
                    fileReader = reader;
                    try {
                        logE("IOException:: unable to get enc properties");
                        e.printStackTrace();
                        try {
                            fileReader.close();
                        } catch (Exception e4) {
                        }
                        return policies;
                    } catch (Throwable th2) {
                        th = th2;
                        try {
                            fileReader.close();
                        } catch (Exception e5) {
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fileReader = reader;
                    fileReader.close();
                    throw th;
                }
            } catch (IOException e6) {
                e = e6;
                logE("IOException:: unable to get enc properties");
                e.printStackTrace();
                fileReader.close();
                return policies;
            }
            return policies;
        }
        logE("unable to get enc properties");
        return policies;
    }
}
