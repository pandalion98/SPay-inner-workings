package android.os;

import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.dirEncryption.DirEncryptionManager;
import android.net.ProxyInfo;
import android.os.storage.IDirEncryptService;
import android.os.storage.IDirEncryptService.Stub;
import android.os.storage.StorageEventListener;
import android.os.storage.StorageManager;
import android.util.Log;

public class OEMEncryption {
    private static final int ACTION_CANCEL = 1;
    private static final int ACTION_NOTIFY = 0;
    private static final String EXTERNAL_SD_CARD_PATH = "/mnt/sdcard/external_sd";
    public static final String NAME = "DirEncryptService";
    private static final int SD_ENC_NOTIFICATION_ID = 1;
    private static final String TAG = "OEMEncryption";
    private static OEMEncryption mSelf;
    private String EXTERNAL_SD_STATE = ProxyInfo.LOCAL_EXCL_LIST;
    private DirEncryptionManager dem = null;
    private IntentFilter filter;
    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Log.d(OEMEncryption.TAG, "handleMessage called for: " + msg.what);
            switch (msg.what) {
                case 0:
                    Log.d(OEMEncryption.TAG, "handleMessage ACTION_NOTIFY. Pid : " + Process.myPid() + " Tid : " + Process.myTid() + " Uid : " + Process.myUid());
                    OEMEncryption.this.mNotificationManager.notify(1, OEMEncryption.this.notification);
                    return;
                case 1:
                    Log.d(OEMEncryption.TAG, "handleMessage ACTION_CANCEL. Pid : " + Process.myPid() + " Tid : " + Process.myTid() + " Uid : " + Process.myUid());
                    OEMEncryption.this.mNotificationManager.cancel(1);
                    return;
                default:
                    return;
            }
        }
    };
    private final Context mContext;
    private NotificationManager mNotificationManager;
    private boolean mSDPolicy = false;
    StorageEventListener mStorageListener = new StorageEventListener() {
        public void onStorageStateChanged(String path, String oldState, String newState) {
            Log.d(OEMEncryption.TAG, "onStorageStateChanged called for  " + path + ". New state is " + newState);
            if (path.equals(OEMEncryption.this.dem.getExternalSdPath())) {
                OEMEncryption.this.EXTERNAL_SD_STATE = newState;
                if (newState.equals(Environment.MEDIA_MOUNTED)) {
                    Log.d(OEMEncryption.TAG, "In (newState.equals(Environment.MEDIA_MOUNTED))");
                    if (OEMEncryption.this.isCurrentSDCardEncrypted()) {
                        Log.d(OEMEncryption.TAG, "Dismissing the notification beacuse SD card is encrypted now.");
                        OEMEncryption.this.handler.sendEmptyMessage(1);
                    }
                }
                if (newState.equals(Environment.MEDIA_CHECKING) && OEMEncryption.this.mSDPolicy) {
                    OEMEncryption.this.setSDEncryptionPolicy(OEMEncryption.this.mSDPolicy);
                }
                if (newState.equals(Environment.MEDIA_UNMOUNTED)) {
                    Log.d(OEMEncryption.TAG, "Dismissing the notification beacuse SD card was unmounted.");
                    OEMEncryption.this.handler.sendEmptyMessage(1);
                }
            }
        }
    };
    private StorageManager mStorageManager;
    private IDirEncryptService m_InstDirEncSvc = null;
    private IDeviceManager3LM m_dem_3lm = null;
    private Notification notification;
    BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.action.3LM_NFC_FRCRESET")) {
                Log.d(OEMEncryption.TAG, "nfc state");
                int nfc_state = intent.getIntExtra("lockstate", -1);
                Log.d(OEMEncryption.TAG, "received key" + nfc_state);
                if (nfc_state == 1 || nfc_state == 0) {
                    try {
                        OEMEncryption.this.m_dem_3lm.setNfcState(nfc_state);
                        return;
                    } catch (Exception e) {
                        Log.d(OEMEncryption.TAG, "Exception caught in 3LM service");
                        return;
                    }
                }
                return;
            }
            Log.d(OEMEncryption.TAG, "Dismissing the notification beacuse SD card is encrypted now.");
            OEMEncryption.this.handler.sendEmptyMessage(1);
        }
    };

    public static synchronized OEMEncryption getInstance(Context context) {
        OEMEncryption oEMEncryption;
        synchronized (OEMEncryption.class) {
            if (mSelf == null) {
                mSelf = new OEMEncryption(context);
            }
            oEMEncryption = mSelf;
        }
        return oEMEncryption;
    }

    public OEMEncryption(Context context) {
        this.mContext = context;
        this.mStorageManager = (StorageManager) this.mContext.getSystemService(Context.STORAGE_SERVICE);
        this.mNotificationManager = (NotificationManager) this.mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        this.dem = new DirEncryptionManager(this.mContext);
        this.m_InstDirEncSvc = Stub.asInterface(ServiceManager.getService("DirEncryptService"));
        this.m_dem_3lm = IDeviceManager3LM.Stub.asInterface(ServiceManager.getService(Context.DEVICE_MANAGER_3LM_SERVICE));
        this.filter = new IntentFilter();
        this.filter.addAction("android.action.3LM_NFC_FRCRESET");
        context.registerReceiver(this.receiver, this.filter);
        this.mStorageManager.registerListener(this.mStorageListener);
        this.EXTERNAL_SD_STATE = Environment.MEDIA_REMOVED;
    }

    public void clear() {
        Log.d(TAG, "clear called");
        this.mSDPolicy = false;
        this.handler.sendEmptyMessage(1);
    }

    public boolean getSDPolicy() {
        return this.mSDPolicy;
    }

    public boolean checkSdEncrypted() {
        if (!Environment.MEDIA_MOUNTED.equals(this.EXTERNAL_SD_STATE) || isCurrentSDCardEncrypted()) {
            return true;
        }
        return false;
    }

    public boolean isCurrentSDCardEncrypted() {
        boolean sd_state = false;
        try {
            if (this.m_InstDirEncSvc.isStorageCardEncryptionPoliciesApplied() == 1 && this.m_InstDirEncSvc.getSDCardEncryptionPrefs() != null) {
                sd_state = this.m_InstDirEncSvc.getSDCardEncryptionPrefs().getDefaultEnc() != 3;
            }
        } catch (Exception e) {
            Log.d(TAG, "Remote exception caught in check encryption status");
        }
        Log.d(TAG, "Encryption state is " + sd_state);
        return sd_state;
    }

    public void setSDEncryptionPolicy(boolean policy) {
        Log.d(TAG, "setSDEncryptionPolicy called with " + policy);
        this.mSDPolicy = policy;
        if (!policy) {
            this.handler.sendEmptyMessage(1);
        } else if (isCurrentSDCardEncrypted()) {
            Log.d(TAG, "Not showing notification because current SD card is already encrypted");
        } else {
            String state = this.EXTERNAL_SD_STATE;
            if (Environment.MEDIA_UNMOUNTED.equals(state) || Environment.MEDIA_REMOVED.equals(state) || Environment.MEDIA_BAD_REMOVAL.equals(state)) {
                Log.d(TAG, "Not showing notification because there is no SD card");
            } else if (this.m_InstDirEncSvc != null) {
                if (policy) {
                    try {
                        Log.d(TAG, "Result is " + this.m_InstDirEncSvc.setStorageCardEncryptionPolicy(2, 4, 7));
                    } catch (RemoteException e) {
                        Log.d(TAG, "Unable to communicate with DirEncryptService");
                    }
                }
                Resources resources = this.mContext.getResources();
                PendingIntent intent = PendingIntent.getActivity(this.mContext, 0, new Intent(DirEncryptionManager.SD_CARD_ENCRYPTION_ACTION), 268435456);
                this.notification = new Notification(R.drawable.stat_notify_sdcard_usb, resources.getString(17041667), System.currentTimeMillis());
                Notification notification = this.notification;
                notification.flags |= 32;
                this.notification.setLatestEventInfo(this.mContext, resources.getString(17041667), resources.getString(17041668), intent);
                this.handler.sendEmptyMessage(0);
            }
        }
    }
}
