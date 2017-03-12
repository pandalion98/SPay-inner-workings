package android.os.storage;

import android.app.ActivityThread;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.IPackageMoveObserver;
import android.os.FileUtils;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.storage.IObbActionListener.Stub;
import android.provider.Settings.Global;
import android.text.TextUtils;
import android.util.Log;
import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.os.SomeArgs;
import com.android.internal.util.Preconditions;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class StorageManager {
    public static final int CRYPT_TYPE_DEFAULT = 1;
    public static final int CRYPT_TYPE_PASSWORD = 0;
    public static final int CRYPT_TYPE_PATTERN = 2;
    public static final int CRYPT_TYPE_PIN = 3;
    public static final int DEBUG_FORCE_ADOPTABLE = 1;
    private static final long DEFAULT_FULL_THRESHOLD_BYTES = 1048576;
    private static final long DEFAULT_THRESHOLD_MAX_BYTES = 524288000;
    private static final int DEFAULT_THRESHOLD_PERCENTAGE = 10;
    public static final int FLAG_FOR_WRITE = 1;
    public static final String OWNER_INFO_KEY = "OwnerInfo";
    public static final String PASSWORD_VISIBLE_KEY = "PasswordVisible";
    public static final String PATTERN_VISIBLE_KEY = "PatternVisible";
    public static final String PROP_FORCE_ADOPTABLE = "persist.fw.force_adoptable";
    public static final String PROP_HAS_ADOPTABLE = "vold.has_adoptable";
    public static final String PROP_PRIMARY_PHYSICAL = "ro.vold.primary_physical";
    public static final String SYSTEM_LOCALE_KEY = "SystemLocale";
    private static final String TAG = "StorageManager";
    public static final String UUID_PRIMARY_PHYSICAL = "primary_physical";
    public static final String UUID_PRIVATE_INTERNAL = null;
    private final Context mContext;
    private final ArrayList<StorageEventListenerDelegate> mDelegates = new ArrayList();
    private final Looper mLooper;
    private final IMountService mMountService;
    private final AtomicInteger mNextNonce = new AtomicInteger(0);
    private final ObbActionListener mObbActionListener = new ObbActionListener();
    private final ContentResolver mResolver;

    private class ObbActionListener extends Stub {
        private SparseArray<ObbListenerDelegate> mListeners;

        private ObbActionListener() {
            this.mListeners = new SparseArray();
        }

        public void onObbResult(String filename, int nonce, int status) {
            synchronized (this.mListeners) {
                ObbListenerDelegate delegate = (ObbListenerDelegate) this.mListeners.get(nonce);
                if (delegate != null) {
                    this.mListeners.remove(nonce);
                }
            }
            if (delegate != null) {
                delegate.sendObbStateChanged(filename, status);
            }
        }

        public int addListener(OnObbStateChangeListener listener) {
            ObbListenerDelegate delegate = new ObbListenerDelegate(listener);
            synchronized (this.mListeners) {
                this.mListeners.put(delegate.nonce, delegate);
            }
            return delegate.nonce;
        }
    }

    private class ObbListenerDelegate {
        private final Handler mHandler;
        private final WeakReference<OnObbStateChangeListener> mObbEventListenerRef;
        private final int nonce;

        ObbListenerDelegate(OnObbStateChangeListener listener) {
            this.nonce = StorageManager.this.getNextNonce();
            this.mObbEventListenerRef = new WeakReference(listener);
            this.mHandler = new Handler(StorageManager.this.mLooper, StorageManager.this) {
                public void handleMessage(Message msg) {
                    OnObbStateChangeListener changeListener = ObbListenerDelegate.this.getListener();
                    if (changeListener != null) {
                        changeListener.onObbStateChange((String) msg.obj, msg.arg1);
                    }
                }
            };
        }

        OnObbStateChangeListener getListener() {
            if (this.mObbEventListenerRef == null) {
                return null;
            }
            return (OnObbStateChangeListener) this.mObbEventListenerRef.get();
        }

        void sendObbStateChanged(String path, int state) {
            this.mHandler.obtainMessage(0, state, 0, path).sendToTarget();
        }
    }

    private static class StorageEventListenerDelegate extends IMountServiceListener.Stub implements Callback {
        private static final int MSG_DISK_DESTROYED = 6;
        private static final int MSG_DISK_SCANNED = 5;
        private static final int MSG_STORAGE_STATE_CHANGED = 1;
        private static final int MSG_VOLUME_FORGOTTEN = 4;
        private static final int MSG_VOLUME_RECORD_CHANGED = 3;
        private static final int MSG_VOLUME_STATE_CHANGED = 2;
        final StorageEventListener mCallback;
        final Handler mHandler;

        public StorageEventListenerDelegate(StorageEventListener callback, Looper looper) {
            this.mCallback = callback;
            this.mHandler = new Handler(looper, (Callback) this);
        }

        public boolean handleMessage(Message msg) {
            SomeArgs args = msg.obj;
            switch (msg.what) {
                case 1:
                    this.mCallback.onStorageStateChanged((String) args.arg1, (String) args.arg2, (String) args.arg3);
                    args.recycle();
                    return true;
                case 2:
                    this.mCallback.onVolumeStateChanged((VolumeInfo) args.arg1, args.argi2, args.argi3);
                    args.recycle();
                    return true;
                case 3:
                    this.mCallback.onVolumeRecordChanged((VolumeRecord) args.arg1);
                    args.recycle();
                    return true;
                case 4:
                    this.mCallback.onVolumeForgotten((String) args.arg1);
                    args.recycle();
                    return true;
                case 5:
                    this.mCallback.onDiskScanned((DiskInfo) args.arg1, args.argi2);
                    args.recycle();
                    return true;
                case 6:
                    this.mCallback.onDiskDestroyed((DiskInfo) args.arg1);
                    args.recycle();
                    return true;
                default:
                    args.recycle();
                    return false;
            }
        }

        public void onUsbMassStorageConnectionChanged(boolean connected) throws RemoteException {
        }

        public void onStorageStateChanged(String path, String oldState, String newState) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = path;
            args.arg2 = oldState;
            args.arg3 = newState;
            this.mHandler.obtainMessage(1, args).sendToTarget();
        }

        public void onVolumeStateChanged(VolumeInfo vol, int oldState, int newState) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = vol;
            args.argi2 = oldState;
            args.argi3 = newState;
            this.mHandler.obtainMessage(2, args).sendToTarget();
        }

        public void onVolumeRecordChanged(VolumeRecord rec) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = rec;
            this.mHandler.obtainMessage(3, args).sendToTarget();
        }

        public void onVolumeForgotten(String fsUuid) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = fsUuid;
            this.mHandler.obtainMessage(4, args).sendToTarget();
        }

        public void onDiskScanned(DiskInfo disk, int volumeCount) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = disk;
            args.argi2 = volumeCount;
            this.mHandler.obtainMessage(5, args).sendToTarget();
        }

        public void onDiskDestroyed(DiskInfo disk) throws RemoteException {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = disk;
            this.mHandler.obtainMessage(6, args).sendToTarget();
        }
    }

    private int getNextNonce() {
        return this.mNextNonce.getAndIncrement();
    }

    @Deprecated
    public static StorageManager from(Context context) {
        return (StorageManager) context.getSystemService(StorageManager.class);
    }

    public StorageManager(Context context, Looper looper) {
        this.mContext = context;
        this.mResolver = context.getContentResolver();
        this.mLooper = looper;
        this.mMountService = IMountService.Stub.asInterface(ServiceManager.getService("mount"));
        if (this.mMountService == null) {
            throw new IllegalStateException("Failed to find running mount service");
        }
    }

    public void registerListener(StorageEventListener listener) {
        synchronized (this.mDelegates) {
            StorageEventListenerDelegate delegate = new StorageEventListenerDelegate(listener, this.mLooper);
            try {
                this.mMountService.registerListener(delegate);
                this.mDelegates.add(delegate);
            } catch (RemoteException e) {
                throw e.rethrowAsRuntimeException();
            }
        }
    }

    public void unregisterListener(StorageEventListener listener) {
        synchronized (this.mDelegates) {
            Iterator<StorageEventListenerDelegate> i = this.mDelegates.iterator();
            while (i.hasNext()) {
                StorageEventListenerDelegate delegate = (StorageEventListenerDelegate) i.next();
                if (delegate.mCallback == listener) {
                    try {
                        this.mMountService.unregisterListener(delegate);
                        i.remove();
                    } catch (RemoteException e) {
                        throw e.rethrowAsRuntimeException();
                    }
                }
            }
        }
    }

    @Deprecated
    public void enableUsbMassStorage() {
    }

    @Deprecated
    public void disableUsbMassStorage() {
    }

    @Deprecated
    public boolean isUsbMassStorageConnected() {
        return false;
    }

    @Deprecated
    public boolean isUsbMassStorageEnabled() {
        return false;
    }

    public boolean mountObb(String rawPath, String key, OnObbStateChangeListener listener) {
        Preconditions.checkNotNull(rawPath, "rawPath cannot be null");
        Preconditions.checkNotNull(listener, "listener cannot be null");
        try {
            String str = rawPath;
            String str2 = key;
            this.mMountService.mountObb(str, new File(rawPath).getCanonicalPath(), str2, this.mObbActionListener, this.mObbActionListener.addListener(listener));
            return true;
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to resolve path: " + rawPath, e);
        } catch (RemoteException e2) {
            Log.e(TAG, "Failed to mount OBB", e2);
            return false;
        }
    }

    public boolean unmountObb(String rawPath, boolean force, OnObbStateChangeListener listener) {
        Preconditions.checkNotNull(rawPath, "rawPath cannot be null");
        Preconditions.checkNotNull(listener, "listener cannot be null");
        try {
            this.mMountService.unmountObb(rawPath, force, this.mObbActionListener, this.mObbActionListener.addListener(listener));
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to mount OBB", e);
            return false;
        }
    }

    public boolean isObbMounted(String rawPath) {
        Preconditions.checkNotNull(rawPath, "rawPath cannot be null");
        try {
            return this.mMountService.isObbMounted(rawPath);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to check if OBB is mounted", e);
            return false;
        }
    }

    public String getMountedObbPath(String rawPath) {
        Preconditions.checkNotNull(rawPath, "rawPath cannot be null");
        try {
            return this.mMountService.getMountedObbPath(rawPath);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to find mounted path for OBB", e);
            return null;
        }
    }

    public List<DiskInfo> getDisks() {
        try {
            return Arrays.asList(this.mMountService.getDisks());
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public DiskInfo findDiskById(String id) {
        Preconditions.checkNotNull(id);
        for (DiskInfo disk : getDisks()) {
            if (Objects.equals(disk.id, id)) {
                return disk;
            }
        }
        return null;
    }

    public VolumeInfo findVolumeById(String id) {
        Preconditions.checkNotNull(id);
        for (VolumeInfo vol : getVolumes()) {
            if (Objects.equals(vol.id, id)) {
                return vol;
            }
        }
        return null;
    }

    public VolumeInfo findVolumeByUuid(String fsUuid) {
        Preconditions.checkNotNull(fsUuid);
        for (VolumeInfo vol : getVolumes()) {
            if (Objects.equals(vol.fsUuid, fsUuid)) {
                return vol;
            }
        }
        return null;
    }

    public VolumeRecord findRecordByUuid(String fsUuid) {
        Preconditions.checkNotNull(fsUuid);
        for (VolumeRecord rec : getVolumeRecords()) {
            if (Objects.equals(rec.fsUuid, fsUuid)) {
                return rec;
            }
        }
        return null;
    }

    public VolumeInfo findPrivateForEmulated(VolumeInfo emulatedVol) {
        if (emulatedVol != null) {
            return findVolumeById(emulatedVol.getId().replace(VolumeInfo.ID_EMULATED_INTERNAL, VolumeInfo.ID_PRIVATE_INTERNAL));
        }
        return null;
    }

    public VolumeInfo findEmulatedForPrivate(VolumeInfo privateVol) {
        if (privateVol != null) {
            return findVolumeById(privateVol.getId().replace(VolumeInfo.ID_PRIVATE_INTERNAL, VolumeInfo.ID_EMULATED_INTERNAL));
        }
        return null;
    }

    public VolumeInfo findVolumeByQualifiedUuid(String volumeUuid) {
        if (Objects.equals(UUID_PRIVATE_INTERNAL, volumeUuid)) {
            return findVolumeById(VolumeInfo.ID_PRIVATE_INTERNAL);
        }
        if (Objects.equals(UUID_PRIMARY_PHYSICAL, volumeUuid)) {
            return getPrimaryPhysicalVolume();
        }
        return findVolumeByUuid(volumeUuid);
    }

    public List<VolumeInfo> getVolumes() {
        try {
            return Arrays.asList(this.mMountService.getVolumes(0));
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public List<VolumeInfo> getWritablePrivateVolumes() {
        try {
            ArrayList<VolumeInfo> res = new ArrayList();
            for (VolumeInfo vol : this.mMountService.getVolumes(0)) {
                if (vol.getType() == 1 && vol.isMountedWritable()) {
                    res.add(vol);
                }
            }
            return res;
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public List<VolumeRecord> getVolumeRecords() {
        try {
            return Arrays.asList(this.mMountService.getVolumeRecords(0));
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public String getBestVolumeDescription(VolumeInfo vol) {
        if (vol == null) {
            return null;
        }
        if (!TextUtils.isEmpty(vol.fsUuid)) {
            VolumeRecord rec = findRecordByUuid(vol.fsUuid);
            if (!(rec == null || TextUtils.isEmpty(rec.nickname))) {
                return rec.nickname;
            }
        }
        if (!TextUtils.isEmpty(vol.getDescription())) {
            return vol.getDescription();
        }
        if (vol.disk != null) {
            return vol.disk.getDescription();
        }
        return null;
    }

    public VolumeInfo getPrimaryPhysicalVolume() {
        for (VolumeInfo vol : getVolumes()) {
            if (vol.isPrimaryPhysical()) {
                return vol;
            }
        }
        return null;
    }

    public void mount(String volId) {
        try {
            this.mMountService.mount(volId);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public void unmount(String volId) {
        try {
            this.mMountService.unmount(volId);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public void format(String volId) {
        try {
            this.mMountService.format(volId);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public long benchmark(String volId) {
        try {
            return this.mMountService.benchmark(volId);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public void partitionPublic(String diskId) {
        try {
            this.mMountService.partitionPublic(diskId);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public void partitionPrivate(String diskId) {
        try {
            this.mMountService.partitionPrivate(diskId);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public void partitionMixed(String diskId, int ratio) {
        try {
            this.mMountService.partitionMixed(diskId, ratio);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public void wipeAdoptableDisks() {
        for (DiskInfo disk : getDisks()) {
            String diskId = disk.getId();
            if (disk.isSd()) {
                Slog.d(TAG, "Found adoptable " + diskId + "; wiping");
                try {
                    this.mMountService.partitionPublic(diskId);
                } catch (Exception e) {
                    Slog.w(TAG, "Failed to wipe " + diskId + ", but soldiering onward", e);
                }
            } else {
                Slog.d(TAG, "Ignorning non-adoptable disk " + disk.getId());
            }
        }
    }

    public void setVolumeNickname(String fsUuid, String nickname) {
        try {
            this.mMountService.setVolumeNickname(fsUuid, nickname);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public void setVolumeInited(String fsUuid, boolean inited) {
        int i = 1;
        try {
            IMountService iMountService = this.mMountService;
            if (!inited) {
                i = 0;
            }
            iMountService.setVolumeUserFlags(fsUuid, i, 1);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public void setVolumeSnoozed(String fsUuid, boolean snoozed) {
        int i = 2;
        try {
            IMountService iMountService = this.mMountService;
            if (!snoozed) {
                i = 0;
            }
            iMountService.setVolumeUserFlags(fsUuid, i, 2);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public void forgetVolume(String fsUuid) {
        try {
            this.mMountService.forgetVolume(fsUuid);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public String getPrimaryStorageUuid() {
        try {
            return this.mMountService.getPrimaryStorageUuid();
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public void setPrimaryStorageUuid(String volumeUuid, IPackageMoveObserver callback) {
        try {
            this.mMountService.setPrimaryStorageUuid(volumeUuid, callback);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public StorageVolume getStorageVolume(File file) {
        return getStorageVolume(getVolumeList(), file);
    }

    public static StorageVolume getStorageVolume(File file, int userId) {
        return getStorageVolume(getVolumeList(userId, 0), file);
    }

    private static StorageVolume getStorageVolume(StorageVolume[] volumes, File file) {
        try {
            file = file.getCanonicalFile();
            StorageVolume[] arr$ = volumes;
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                StorageVolume volume = arr$[i$];
                try {
                    if (FileUtils.contains(volume.getPathFile().getCanonicalFile(), file)) {
                        return volume;
                    }
                    i$++;
                } catch (IOException e) {
                }
            }
            return null;
        } catch (IOException e2) {
            return null;
        }
    }

    @Deprecated
    public String getVolumeState(String mountPoint) {
        if (mountPoint == null) {
            throw new IllegalArgumentException();
        }
        StorageVolume vol = getStorageVolume(new File(mountPoint));
        if (vol != null) {
            return vol.getState();
        }
        return "unknown";
    }

    public StorageVolume[] getVolumeList() {
        return getVolumeList(this.mContext.getUserId(), 0);
    }

    public static StorageVolume[] getVolumeList(int userId, int flags) {
        IMountService mountService = IMountService.Stub.asInterface(ServiceManager.getService("mount"));
        try {
            String packageName = ActivityThread.currentOpPackageName();
            if (packageName == null) {
                String[] packageNames = ActivityThread.getPackageManager().getPackagesForUid(Process.myUid());
                if (packageNames == null || packageNames.length <= 0) {
                    return new StorageVolume[0];
                }
                packageName = packageNames[0];
            }
            int uid = ActivityThread.getPackageManager().getPackageUid(packageName, userId);
            if (uid <= 0) {
                return new StorageVolume[0];
            }
            return mountService.getVolumeList(uid, packageName, flags);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    @Deprecated
    public String[] getVolumePaths() {
        StorageVolume[] volumes = getVolumeList();
        int count = volumes.length;
        String[] paths = new String[count];
        for (int i = 0; i < count; i++) {
            paths[i] = volumes[i].getPath();
        }
        return paths;
    }

    public StorageVolume getPrimaryVolume() {
        return getPrimaryVolume(getVolumeList());
    }

    public static StorageVolume getPrimaryVolume(StorageVolume[] volumes) {
        for (StorageVolume volume : volumes) {
            if (volume.isPrimary()) {
                return volume;
            }
        }
        throw new IllegalStateException("Missing primary storage");
    }

    public long getStorageBytesUntilLow(File path) {
        return path.getUsableSpace() - getStorageFullBytes(path);
    }

    public long getStorageLowBytes(File path) {
        return Math.min((path.getTotalSpace() * ((long) Global.getInt(this.mResolver, Global.SYS_STORAGE_THRESHOLD_PERCENTAGE, 10))) / 100, Global.getLong(this.mResolver, Global.SYS_STORAGE_THRESHOLD_MAX_BYTES, DEFAULT_THRESHOLD_MAX_BYTES));
    }

    public long getStorageFullBytes(File path) {
        return Global.getLong(this.mResolver, Global.SYS_STORAGE_FULL_THRESHOLD_BYTES, 1048576);
    }

    public void createNewUserDir(int userHandle, File path) {
        try {
            this.mMountService.createNewUserDir(userHandle, path.getAbsolutePath());
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public void deleteUserKey(int userHandle) {
        try {
            this.mMountService.deleteUserKey(userHandle);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public static File maybeTranslateEmulatedPathToInternal(File path) {
        try {
            for (VolumeInfo vol : IMountService.Stub.asInterface(ServiceManager.getService("mount")).getVolumes(0)) {
                if (vol.getType() == 2 && vol.isMountedReadable()) {
                    File internalPath = FileUtils.rewriteAfterRename(vol.getPath(), vol.getInternalPath(), path);
                    if (internalPath != null && internalPath.exists()) {
                        return internalPath;
                    }
                }
            }
        } catch (RemoteException e) {
        }
        return path;
    }

    public StorageVolume getVolume(String path) {
        StorageVolume[] volumes = getVolumeList();
        if (volumes == null) {
            return null;
        }
        int count = volumes.length;
        for (int i = 0; i < count; i++) {
            if (volumes[i].getPath().equals(path)) {
                return volumes[i];
            }
        }
        return null;
    }

    public void setVolumeState(String path, String state) {
        if (this.mMountService != null) {
            try {
                this.mMountService.setVolumeState(path, state);
            } catch (RemoteException e) {
                Log.e(TAG, "Failed to set volume state", e);
            }
        }
    }

    public int modifyVolumeLocked(String path, int descriptionId, boolean removable, String subsystem) {
        if (this.mMountService == null) {
            return -1;
        }
        Slog.d(TAG, "modifyVolumeLocked method is not used at Android M any more. So return 0.");
        return 0;
    }

    public int prepareDecryptOnRecovery(int type, String password) {
        int ret = 0;
        try {
            ret = this.mMountService.prepareDecryptOnRecovery(type, password);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to set volume state", e);
        }
        return ret;
    }
}
