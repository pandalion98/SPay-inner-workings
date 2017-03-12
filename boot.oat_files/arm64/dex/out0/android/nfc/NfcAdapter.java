package android.nfc;

import android.app.Activity;
import android.app.ActivityThread;
import android.app.OnActivityPausedListener;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.media.AudioParameter;
import android.net.Uri;
import android.nfc.INfcAdapter.Stub;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersonaManager;
import android.os.PersonaManager.KnoxContainerVersion;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.util.Log;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public final class NfcAdapter {
    public static final String ACTION_ADAPTER_RW_P2P_STATE_CHANGED = "com.felicanetworks.nfc.action.ADAPTER_RW_P2P_STATE_CHANGED";
    public static final String ACTION_ADAPTER_STATE_CHANGED = "android.nfc.action.ADAPTER_STATE_CHANGED";
    public static final String ACTION_ADAPTER_STATE_CHANGE_READER = "android.nfc.action.ADAPTER_STATE_CHANGE_READER";
    public static final String ACTION_HANDOVER_TRANSFER_DONE = "android.nfc.action.HANDOVER_TRANSFER_DONE";
    public static final String ACTION_HANDOVER_TRANSFER_STARTED = "android.nfc.action.HANDOVER_TRANSFER_STARTED";
    public static final String ACTION_NDEF_DISCOVERED = "android.nfc.action.NDEF_DISCOVERED";
    public static final String ACTION_TAG_DISCOVERED = "android.nfc.action.TAG_DISCOVERED";
    public static final String ACTION_TAG_LEFT_FIELD = "android.nfc.action.TAG_LOST";
    public static final String ACTION_TECH_DISCOVERED = "android.nfc.action.TECH_DISCOVERED";
    public static final String EXTRA_ADAPTER_RW_P2P_STATE = "com.felicanetworks.nfc.extra.ADAPTER_RW_P2P_STATE";
    public static final String EXTRA_ADAPTER_STATE = "android.nfc.extra.ADAPTER_STATE";
    public static final String EXTRA_HANDOVER_TRANSFER_STATUS = "android.nfc.extra.HANDOVER_TRANSFER_STATUS";
    public static final String EXTRA_HANDOVER_TRANSFER_URI = "android.nfc.extra.HANDOVER_TRANSFER_URI";
    public static final String EXTRA_ID = "android.nfc.extra.ID";
    public static final String EXTRA_NDEF_MESSAGES = "android.nfc.extra.NDEF_MESSAGES";
    public static final String EXTRA_READER_PRESENCE_CHECK_DELAY = "presence";
    public static final String EXTRA_TAG = "android.nfc.extra.TAG";
    public static final int FLAG_LISTEN_NFC_A = 1;
    public static final int FLAG_LISTEN_NFC_ACTIVE_A = 64;
    public static final int FLAG_LISTEN_NFC_ACTIVE_F = 128;
    public static final int FLAG_LISTEN_NFC_B = 2;
    public static final int FLAG_LISTEN_NFC_F = 4;
    public static final int FLAG_NDEF_PUSH_NO_CONFIRM = 1;
    public static final int FLAG_READER_NFC_A = 1;
    public static final int FLAG_READER_NFC_B = 2;
    public static final int FLAG_READER_NFC_BARCODE = 16;
    public static final int FLAG_READER_NFC_F = 4;
    public static final int FLAG_READER_NFC_V = 8;
    public static final int FLAG_READER_NO_PLATFORM_SOUNDS = 256;
    public static final int FLAG_READER_SKIP_NDEF_CHECK = 128;
    public static final int HANDOVER_TRANSFER_STATUS_FAILURE = 1;
    public static final int HANDOVER_TRANSFER_STATUS_SUCCESS = 0;
    public static final int STATE_CARD_MODE_ON = 5;
    public static final int STATE_OFF = 1;
    public static final int STATE_ON = 3;
    public static final int STATE_RW_P2P_OFF = 11;
    public static final int STATE_RW_P2P_ON = 13;
    public static final int STATE_RW_P2P_TURNING_OFF = 14;
    public static final int STATE_RW_P2P_TURNING_ON = 12;
    public static final int STATE_TURNING_OFF = 4;
    public static final int STATE_TURNING_ON = 2;
    static final String TAG = "NFC";
    public static final int TECHNOLOGY_MASK_A = 1;
    public static final int TECHNOLOGY_MASK_B = 2;
    public static final int TECHNOLOGY_MASK_F = 4;
    static INfcCardEmulation sCardEmulationService;
    static boolean sIsInitialized = false;
    static HashMap<Context, NfcAdapter> sNfcAdapters = new HashMap();
    static NfcAdapter sNullContextNfcAdapter;
    static INfcAdapter sService;
    static INfcTag sTagService;
    final Context mContext;
    private Cursor mDevSettingCr;
    OnActivityPausedListener mForegroundDispatchListener = new OnActivityPausedListener() {
        public void onPaused(Activity activity) {
            NfcAdapter.this.disableForegroundDispatchInternal(activity, true);
        }
    };
    final Object mLock;
    final NfcActivityManager mNfcActivityManager;
    final NfcHciEvtManager mNfcHciEvtManager;
    final HashMap<NfcUnlockHandler, INfcUnlockHandler> mNfcUnlockHandlers;
    private PersonaManager mPersonaManager = null;
    private Cursor mRestrictionCr;
    private Uri mUri;

    public interface CreateBeamUrisCallback {
        Uri[] createBeamUris(NfcEvent nfcEvent);
    }

    public interface CreateNdefMessageCallback {
        NdefMessage createNdefMessage(NfcEvent nfcEvent);
    }

    public interface HciEvtCallback {
        void hciEvtTransaction(Intent intent);
    }

    public interface LedCoverNotificationCallback {
        void LedCoverNotification();
    }

    public interface NfcUnlockHandler {
        boolean onUnlockAttempted(Tag tag);
    }

    public interface OnNdefPushCompleteCallback {
        void onNdefPushComplete(NfcEvent nfcEvent);
    }

    public interface ReaderCallback {
        void onTagDiscovered(Tag tag);
    }

    public void setLedCoverNotificationCallback(LedCoverNotificationCallback callback) {
        this.mNfcActivityManager.setLedCoverNtfCallback(callback);
    }

    private static boolean hasNfcFeature() {
        boolean z = false;
        IPackageManager pm = ActivityThread.getPackageManager();
        if (pm == null) {
            Log.e(TAG, "Cannot get package manager, assuming no NFC feature");
        } else {
            try {
                z = pm.hasSystemFeature(PackageManager.FEATURE_NFC);
            } catch (RemoteException e) {
                Log.e(TAG, "Package manager query failed, assuming no NFC feature", e);
            }
        }
        return z;
    }

    public static synchronized NfcAdapter getNfcAdapter(Context context) {
        NfcAdapter nfcAdapter;
        synchronized (NfcAdapter.class) {
            if (!sIsInitialized) {
                if (hasNfcFeature()) {
                    sService = getServiceInterface();
                    if (sService == null) {
                        Log.e(TAG, "could not retrieve NFC service");
                        throw new UnsupportedOperationException();
                    }
                    try {
                        sTagService = sService.getNfcTagInterface();
                        sCardEmulationService = sService.getNfcCardEmulationInterface();
                        sIsInitialized = true;
                    } catch (RemoteException e) {
                        Log.e(TAG, "could not retrieve card emulation service");
                        throw new UnsupportedOperationException();
                    } catch (RemoteException e2) {
                        Log.e(TAG, "could not retrieve NFC Tag service");
                        throw new UnsupportedOperationException();
                    }
                }
                Log.v(TAG, "this device does not have NFC support");
                throw new UnsupportedOperationException();
            }
            if (context == null) {
                if (sNullContextNfcAdapter == null) {
                    sNullContextNfcAdapter = new NfcAdapter(null);
                }
                nfcAdapter = sNullContextNfcAdapter;
            } else {
                nfcAdapter = (NfcAdapter) sNfcAdapters.get(context);
                if (nfcAdapter == null) {
                    nfcAdapter = new NfcAdapter(context);
                    sNfcAdapters.put(context, nfcAdapter);
                }
            }
        }
        return nfcAdapter;
    }

    private static INfcAdapter getServiceInterface() {
        IBinder b = ServiceManager.getService("nfc");
        if (b == null) {
            return null;
        }
        return Stub.asInterface(b);
    }

    public static NfcAdapter getDefaultAdapter(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context cannot be null");
        }
        context = context.getApplicationContext();
        if (context == null) {
            throw new IllegalArgumentException("context not associated with any application (using a mock context?)");
        }
        NfcManager manager = (NfcManager) context.getSystemService("nfc");
        if (manager == null) {
            return null;
        }
        manager.bindNfcService(context);
        return manager.getDefaultAdapter();
    }

    @Deprecated
    public static NfcAdapter getDefaultAdapter() {
        Log.w(TAG, "WARNING: NfcAdapter.getDefaultAdapter() is deprecated, use NfcAdapter.getDefaultAdapter(Context) instead", new Exception());
        return getNfcAdapter(null);
    }

    NfcAdapter(Context context) {
        this.mContext = context;
        this.mNfcActivityManager = new NfcActivityManager(this);
        this.mNfcHciEvtManager = new NfcHciEvtManager(this);
        this.mNfcUnlockHandlers = new HashMap();
        this.mLock = new Object();
        if (context != null) {
            this.mPersonaManager = (PersonaManager) context.getSystemService(Context.PERSONA_SERVICE);
        }
    }

    public Context getContext() {
        return this.mContext;
    }

    public INfcAdapter getService() {
        isEnabled();
        return sService;
    }

    public INfcTag getTagService() {
        isEnabled();
        return sTagService;
    }

    public INfcCardEmulation getCardEmulationService() {
        isEnabled();
        return sCardEmulationService;
    }

    public void attemptDeadServiceRecovery(Exception e) {
        Log.e(TAG, "NFC service dead - attempting to recover", e);
        INfcAdapter service = getServiceInterface();
        if (service == null) {
            Log.e(TAG, "could not retrieve NFC service during service recovery");
            return;
        }
        sService = service;
        try {
            sTagService = service.getNfcTagInterface();
            try {
                sCardEmulationService = service.getNfcCardEmulationInterface();
            } catch (RemoteException e2) {
                Log.e(TAG, "could not retrieve NFC card emulation service during service recovery");
            }
        } catch (RemoteException e3) {
            Log.e(TAG, "could not retrieve NFC tag service during service recovery");
        }
    }

    public boolean isEnabled() {
        try {
            return sService.getState() == 3;
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
            return false;
        }
    }

    public boolean isUnlocked() {
        try {
            return sService.getState() == 5 || sService.getState() == 3;
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
            return false;
        }
    }

    public int getAdapterState() {
        try {
            return sService.getState();
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
            return 1;
        }
    }

    public boolean enable() {
        boolean z = false;
        try {
            if (isNFCEnabled() && isNFCStateChangeAllowed()) {
                z = sService.enable();
                return z;
            }
            Log.e(TAG, "EDM : nfc policy disabled");
            return z;
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
        }
    }

    public boolean disable() {
        boolean z = false;
        try {
            if (isNFCStateChangeAllowed()) {
                z = sService.disable(true);
            } else {
                Log.e(TAG, "EDM : nfc policy disabled");
            }
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
        }
        return z;
    }

    public boolean disable(boolean persist) {
        try {
            return sService.disable(persist);
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
            return false;
        }
    }

    public void pausePolling(int timeoutInMs) {
        try {
            sService.pausePolling(timeoutInMs);
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
        }
    }

    public void resumePolling() {
        try {
            sService.resumePolling();
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
        }
    }

    public void setBeamPushUris(Uri[] uris, Activity activity) {
        if (!isAndroidBeamAllowed(false)) {
            return;
        }
        if (activity == null) {
            throw new NullPointerException("activity cannot be null");
        }
        if (uris != null) {
            for (Uri uri : uris) {
                if (uri == null) {
                    throw new NullPointerException("Uri not allowed to be null");
                }
                String scheme = uri.getScheme();
                if (scheme == null || !(scheme.equalsIgnoreCase(ContentResolver.SCHEME_FILE) || scheme.equalsIgnoreCase("content"))) {
                    throw new IllegalArgumentException("URI needs to have either scheme file or scheme content");
                }
            }
        }
        this.mNfcActivityManager.setNdefPushContentUri(activity, uris);
    }

    public void setBeamPushUrisCallback(CreateBeamUrisCallback callback, Activity activity) {
        if (!isAndroidBeamAllowed(false)) {
            return;
        }
        if (activity == null) {
            throw new NullPointerException("activity cannot be null");
        }
        this.mNfcActivityManager.setNdefPushContentUriCallback(activity, callback);
    }

    public void setNdefPushMessage(NdefMessage message, Activity activity, Activity... activities) {
        if (isAndroidBeamAllowed(false)) {
            int targetSdkVersion = getSdkVersion();
            if (activity == null) {
                try {
                    throw new NullPointerException("activity cannot be null");
                } catch (IllegalStateException e) {
                    if (targetSdkVersion < 16) {
                        Log.e(TAG, "Cannot call API with Activity that has already been destroyed", e);
                        return;
                    }
                    throw e;
                }
            }
            this.mNfcActivityManager.setNdefPushMessage(activity, message, 0);
            for (Activity a : activities) {
                if (a == null) {
                    throw new NullPointerException("activities cannot contain null");
                }
                this.mNfcActivityManager.setNdefPushMessage(a, message, 0);
            }
        }
    }

    public void setNdefPushMessage(NdefMessage message, Activity activity, int flags) {
        if (activity == null) {
            throw new NullPointerException("activity cannot be null");
        }
        this.mNfcActivityManager.setNdefPushMessage(activity, message, flags);
    }

    public void setNdefPushMessageCallback(CreateNdefMessageCallback callback, Activity activity, Activity... activities) {
        if (isAndroidBeamAllowed(false)) {
            int targetSdkVersion = getSdkVersion();
            if (activity == null) {
                try {
                    throw new NullPointerException("activity cannot be null");
                } catch (IllegalStateException e) {
                    if (targetSdkVersion < 16) {
                        Log.e(TAG, "Cannot call API with Activity that has already been destroyed", e);
                        return;
                    }
                    throw e;
                }
            }
            this.mNfcActivityManager.setNdefPushMessageCallback(activity, callback, 0);
            for (Activity a : activities) {
                if (a == null) {
                    throw new NullPointerException("activities cannot contain null");
                }
                this.mNfcActivityManager.setNdefPushMessageCallback(a, callback, 0);
            }
        }
    }

    public void setNdefPushMessageCallback(CreateNdefMessageCallback callback, Activity activity, int flags) {
        if (activity == null) {
            throw new NullPointerException("activity cannot be null");
        }
        this.mNfcActivityManager.setNdefPushMessageCallback(activity, callback, flags);
    }

    public void setOnNdefPushCompleteCallback(OnNdefPushCompleteCallback callback, Activity activity, Activity... activities) {
        int targetSdkVersion = getSdkVersion();
        if (activity == null) {
            try {
                throw new NullPointerException("activity cannot be null");
            } catch (IllegalStateException e) {
                if (targetSdkVersion < 16) {
                    Log.e(TAG, "Cannot call API with Activity that has already been destroyed", e);
                    return;
                }
                throw e;
            }
        }
        this.mNfcActivityManager.setOnNdefPushCompleteCallback(activity, callback);
        for (Activity a : activities) {
            if (a == null) {
                throw new NullPointerException("activities cannot contain null");
            }
            this.mNfcActivityManager.setOnNdefPushCompleteCallback(a, callback);
        }
    }

    public void setOnNfcHciEvtCallback(HciEvtCallback callback) {
        this.mNfcHciEvtManager.setOnHciEvtCallback(callback);
    }

    public void enableForegroundDispatch(Activity activity, PendingIntent intent, IntentFilter[] filters, String[][] techLists) {
        if (PersonaManager.isKnoxVersionSupported(KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_4_0) && this.mPersonaManager != null && !this.mPersonaManager.isNFCAllowed(UserHandle.myUserId())) {
            Log.i(TAG, "enableForegroundDispatch is disabled due to Knox restriction");
        } else if (activity == null || intent == null) {
            throw new NullPointerException();
        } else if (activity.isResumed()) {
            TechListParcel parcel = null;
            if (techLists != null) {
                try {
                    if (techLists.length > 0) {
                        parcel = new TechListParcel(techLists);
                    }
                } catch (RemoteException e) {
                    attemptDeadServiceRecovery(e);
                    return;
                }
            }
            ActivityThread.currentActivityThread().registerOnActivityPausedListener(activity, this.mForegroundDispatchListener);
            sService.setForegroundDispatch(intent, filters, parcel);
        } else {
            throw new IllegalStateException("Foreground dispatch can only be enabled when your activity is resumed");
        }
    }

    public void disableForegroundDispatch(Activity activity) {
        ActivityThread.currentActivityThread().unregisterOnActivityPausedListener(activity, this.mForegroundDispatchListener);
        disableForegroundDispatchInternal(activity, false);
    }

    void disableForegroundDispatchInternal(Activity activity, boolean force) {
        try {
            sService.setForegroundDispatch(null, null, null);
            if (!force && !activity.isResumed()) {
                throw new IllegalStateException("You must disable foreground dispatching while your activity is still resumed");
            }
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
        }
    }

    public void enableReaderMode(Activity activity, ReaderCallback callback, int flags, Bundle extras) {
        if (!PersonaManager.isKnoxVersionSupported(KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_4_0) || this.mPersonaManager == null || this.mPersonaManager.isNFCAllowed(UserHandle.myUserId())) {
            this.mNfcActivityManager.enableReaderMode(activity, callback, flags, extras);
        } else {
            Log.i(TAG, " enable reader mode is disabled due to Knox restriction");
        }
    }

    public void enableListenMode(Activity activity, int flags) {
        if (!PersonaManager.isKnoxVersionSupported(KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_4_0) || this.mPersonaManager == null || this.mPersonaManager.isNFCAllowed(UserHandle.myUserId())) {
            this.mNfcActivityManager.enableListenMode(activity, flags);
        } else {
            Log.i(TAG, " enable reader mode is disabled due to Knox restriction");
        }
    }

    public void disableListenMode(Activity activity) {
        this.mNfcActivityManager.disableListenMode(activity);
    }

    public void changeRouting(Activity activity, String proto, String tech, List<ComponentName> services) {
        Log.e(TAG, "NfcAdapter : changeRouting()");
        this.mNfcActivityManager.changeRouting(UserHandle.myUserId(), activity, proto, tech, services);
    }

    public void disableReaderMode(Activity activity) {
        this.mNfcActivityManager.disableReaderMode(activity);
    }

    public boolean invokeBeam(Activity activity) {
        if (activity == null) {
            throw new NullPointerException("activity may not be null.");
        }
        enforceResumed(activity);
        try {
            sService.invokeBeam();
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, "invokeBeam: NFC process has died.");
            attemptDeadServiceRecovery(e);
            return false;
        }
    }

    public boolean invokeBeam(BeamShareData shareData) {
        try {
            Log.e(TAG, "invokeBeamInternal()");
            sService.invokeBeamInternal(shareData);
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, "invokeBeam: NFC process has died.");
            attemptDeadServiceRecovery(e);
            return false;
        }
    }

    @Deprecated
    public void enableForegroundNdefPush(Activity activity, NdefMessage message) {
        if (PersonaManager.isKnoxVersionSupported(KnoxContainerVersion.KNOX_CONTAINER_VERSION_2_4_0) && this.mPersonaManager != null && !this.mPersonaManager.isNFCAllowed(UserHandle.myUserId())) {
            Log.i(TAG, " enableForegroundNdefPush is disabled due to Knox restriction");
        } else if (activity == null || message == null) {
            throw new NullPointerException();
        } else {
            enforceResumed(activity);
            this.mNfcActivityManager.setNdefPushMessage(activity, message, 0);
        }
    }

    @Deprecated
    public void disableForegroundNdefPush(Activity activity) {
        if (activity == null) {
            throw new NullPointerException();
        }
        enforceResumed(activity);
        this.mNfcActivityManager.setNdefPushMessage(activity, null, 0);
        this.mNfcActivityManager.setNdefPushMessageCallback(activity, null, 0);
        this.mNfcActivityManager.setOnNdefPushCompleteCallback(activity, null);
    }

    public boolean enableNdefPush() {
        boolean z = false;
        if (!isAndroidBeamAllowed(z)) {
            return disableNdefPush();
        }
        try {
            return sService.enableNdefPush();
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
            return z;
        }
    }

    public boolean disableNdefPush() {
        try {
            return sService.disableNdefPush();
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
            return false;
        }
    }

    public boolean isNdefPushEnabled() {
        boolean z = false;
        if (isAndroidBeamAllowed(z)) {
            try {
                z = sService.isNdefPushEnabled();
            } catch (RemoteException e) {
                attemptDeadServiceRecovery(e);
            }
        }
        return z;
    }

    public void dispatch(Tag tag) {
        if (tag == null) {
            throw new NullPointerException("tag cannot be null");
        }
        try {
            sService.dispatch(tag);
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
        }
    }

    public void setP2pModes(int initiatorModes, int targetModes) {
        try {
            sService.setP2pModes(initiatorModes, targetModes);
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
        }
    }

    public boolean setFilterList(byte[] filterList) {
        try {
            return sService.setFilterList(filterList);
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
            return false;
        }
    }

    public boolean enableFilterCondition(byte filterConditionTag) {
        try {
            return sService.enableFilterCondition(filterConditionTag);
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
            return false;
        }
    }

    public boolean disableFilterCondition(byte filterConditionTag) {
        try {
            return sService.disableFilterCondition(filterConditionTag);
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
            return false;
        }
    }

    private boolean isAndroidBeamAllowed(boolean showMsg) {
        try {
            Log.e(TAG, "isAndroidBeamAllowed - Begin");
            this.mUri = Uri.parse("content://com.sec.knox.provider/RestrictionPolicy1");
            String[] selectionArgs = new String[]{String.valueOf(showMsg)};
            Log.i(TAG, "showMsg is " + selectionArgs[0]);
            this.mRestrictionCr = this.mContext.getContentResolver().query(this.mUri, null, "isAndroidBeamAllowed", selectionArgs, null);
            if (this.mRestrictionCr == null) {
                return true;
            }
            this.mRestrictionCr.moveToFirst();
            try {
                if (this.mRestrictionCr.getString(this.mRestrictionCr.getColumnIndex("isAndroidBeamAllowed")).equals(AudioParameter.AUDIO_PARAMETER_VALUE_false)) {
                    Log.e(TAG, "AndroidBeam is not allowed");
                    return false;
                }
                this.mRestrictionCr.close();
                return true;
            } catch (CursorIndexOutOfBoundsException e) {
                e.printStackTrace();
                return true;
            } finally {
                this.mRestrictionCr.close();
            }
        } catch (Exception e2) {
            Log.e(TAG, "Failed to talk to Restriction Policy");
        }
    }

    private boolean isNFCEnabled() {
        try {
            Log.e(TAG, "isNFCEnabled - Begin");
            this.mUri = Uri.parse("content://com.sec.knox.provider/RestrictionPolicy2");
            this.mRestrictionCr = this.mContext.getContentResolver().query(this.mUri, null, "isNFCEnabled", null, null);
            if (this.mRestrictionCr == null) {
                return true;
            }
            this.mRestrictionCr.moveToFirst();
            try {
                if (this.mRestrictionCr.getString(this.mRestrictionCr.getColumnIndex("isNFCEnabled")).equals(AudioParameter.AUDIO_PARAMETER_VALUE_false)) {
                    return false;
                }
                this.mRestrictionCr.close();
                return true;
            } catch (CursorIndexOutOfBoundsException e) {
                e.printStackTrace();
                return true;
            } finally {
                this.mRestrictionCr.close();
            }
        } catch (Exception e2) {
            Log.e(TAG, "Failed to talk to Restriction Policy");
        }
    }

    private boolean isNFCStateChangeAllowed() {
        try {
            Log.e(TAG, "isNFCStateChangeAllowed - Begin");
            this.mUri = Uri.parse("content://com.sec.knox.provider2/MiscPolicy");
            this.mDevSettingCr = this.mContext.getContentResolver().query(this.mUri, null, "isNFCStateChangeAllowed", null, null);
            if (this.mDevSettingCr == null) {
                return true;
            }
            this.mDevSettingCr.moveToFirst();
            try {
                if (this.mDevSettingCr.getString(this.mDevSettingCr.getColumnIndex("isNFCStateChangeAllowed")).equals(AudioParameter.AUDIO_PARAMETER_VALUE_false)) {
                    return false;
                }
                this.mDevSettingCr.close();
                return true;
            } catch (CursorIndexOutOfBoundsException e) {
                e.printStackTrace();
                return true;
            } finally {
                this.mDevSettingCr.close();
            }
        } catch (Exception e2) {
            Log.e(TAG, "Failed to talk to Misc Policy");
        }
    }

    public boolean isTrustedPkg(String SEName, String packageName) {
        boolean bTrusted = false;
        try {
            bTrusted = sService.isTrustedPkg(SEName, packageName);
        } catch (RemoteException e) {
            Log.e(TAG, "could not retrieve NFC service");
            attemptDeadServiceRecovery(e);
        }
        return bTrusted;
    }

    public boolean addNfcUnlockHandler(final NfcUnlockHandler unlockHandler, String[] tagTechnologies) {
        if (tagTechnologies.length == 0) {
            return false;
        }
        try {
            synchronized (this.mLock) {
                if (this.mNfcUnlockHandlers.containsKey(unlockHandler)) {
                    sService.removeNfcUnlockHandler((INfcUnlockHandler) this.mNfcUnlockHandlers.get(unlockHandler));
                    this.mNfcUnlockHandlers.remove(unlockHandler);
                }
                INfcUnlockHandler.Stub iHandler = new INfcUnlockHandler.Stub() {
                    public boolean onUnlockAttempted(Tag tag) throws RemoteException {
                        return unlockHandler.onUnlockAttempted(tag);
                    }
                };
                sService.addNfcUnlockHandler(iHandler, Tag.getTechCodesFromStrings(tagTechnologies));
                this.mNfcUnlockHandlers.put(unlockHandler, iHandler);
            }
            return true;
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
            return false;
        } catch (IllegalArgumentException e2) {
            Log.e(TAG, "Unable to register LockscreenDispatch", e2);
            return false;
        }
    }

    public boolean removeNfcUnlockHandler(NfcUnlockHandler unlockHandler) {
        try {
            synchronized (this.mLock) {
                if (this.mNfcUnlockHandlers.containsKey(unlockHandler)) {
                    sService.removeNfcUnlockHandler((INfcUnlockHandler) this.mNfcUnlockHandlers.remove(unlockHandler));
                }
            }
            return true;
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
            return false;
        }
    }

    public int getAdapterRwP2pState() {
        try {
            return sService.getRwP2pState();
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
            return 11;
        }
    }

    public boolean setRwP2pMode(boolean isEnable) {
        try {
            return sService.setRwP2pMode(isEnable);
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
            return false;
        }
    }

    public void prepareSwitchedOffState() {
        try {
            sService.prepareSwitchedOffState();
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
        }
    }

    public int setListenMode(int technology) {
        try {
            return sService.setListenMode(technology);
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
            Log.e(TAG, "Fail to set Listen mode", e);
            return -1;
        }
    }

    public int getListenMode() {
        try {
            return sService.getListenMode();
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
            Log.e(TAG, "Fail to get Listen mode", e);
            return -1;
        }
    }

    public boolean isSimLocked() {
        try {
            return sService.isSimLocked();
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
            Log.e(TAG, "Fail to isSimLocked", e);
            return true;
        }
    }

    public void setSimLocked(boolean lock) {
        try {
            sService.setSimLocked(lock);
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
            Log.e(TAG, "Fail to setSimLocked", e);
        }
    }

    public INfcAdapterExtras getNfcAdapterExtrasInterface() {
        if (this.mContext == null) {
            throw new UnsupportedOperationException("You need a context on NfcAdapter to use the  NFC extras APIs");
        }
        try {
            return sService.getNfcAdapterExtrasInterface(this.mContext.getPackageName());
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
            return null;
        }
    }

    void enforceResumed(Activity activity) {
        if (!activity.isResumed()) {
            throw new IllegalStateException("API cannot be called while activity is paused");
        }
    }

    int getSdkVersion() {
        if (this.mContext == null) {
            return 9;
        }
        return this.mContext.getApplicationInfo().targetSdkVersion;
    }

    public void enableDisableSeTestMode(String SE, boolean enable) throws IOException {
        try {
            sService.enableDisableSeTestMode(SE, enable);
        } catch (RemoteException e) {
            Log.e(TAG, "Fail to change default routing", e);
            throw new IOException("Fail to change default routing");
        }
    }

    public void setDefaultRoutingDestination(String SE) throws IOException {
        try {
            sService.setDefaultRoutingDestination(SE);
        } catch (RemoteException e) {
            Log.e(TAG, "Fail to change default routing", e);
            throw new IOException("Fail to change default routing");
        }
    }

    public String getDefaultRoutingDestination() throws IOException {
        try {
            return sService.getDefaultRoutingDestination();
        } catch (RemoteException e) {
            Log.e(TAG, "Fail to change default routing", e);
            throw new IOException("Fail to change default routing");
        }
    }

    public boolean readerDisable() {
        try {
            return sService.readerDisable();
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
            return false;
        }
    }

    public boolean readerEnable() {
        try {
            return sService.readerEnable();
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
            return false;
        }
    }

    public boolean isAllEnabled() {
        try {
            return sService.getState() == 5 || sService.getState() == 3;
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
            return false;
        }
    }

    public byte[] startCoverAuth() throws IOException {
        try {
            return sService.startCoverAuth();
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to transmit authentication data");
            attemptDeadServiceRecovery(e);
            throw new IOException("Failed to transmit authentication data");
        }
    }

    public byte[] transceiveAuthData(byte[] data) throws IOException {
        try {
            return sService.transceiveAuthData(data);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to transmit authentication data");
            attemptDeadServiceRecovery(e);
            throw new IOException("Failed to transmit authentication data");
        }
    }

    public boolean stopCoverAuth() throws IOException {
        try {
            return sService.stopCoverAuth();
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to stop authentication");
            attemptDeadServiceRecovery(e);
            throw new IOException("Failed to stop authentication");
        }
    }

    public int getSeSupportedTech() throws IOException {
        try {
            return sService.getSeSupportedTech();
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to get Tech information");
            attemptDeadServiceRecovery(e);
            throw new IOException("Failed to get Tech information");
        }
    }

    public byte[] TransceiveLedCover(byte[] data) {
        try {
            return sService.TransceiveLedCover(data);
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
            return new byte[]{(byte) 102, (byte) 97, (byte) 108, (byte) 115, (byte) 101};
        }
    }

    public byte[] StartLedCover() {
        try {
            return sService.StartLedCover();
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
            return new byte[]{(byte) 102, (byte) 97, (byte) 108, (byte) 115, (byte) 101};
        }
    }

    public boolean StopLedCover() {
        try {
            return sService.StopLedCover();
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
            return false;
        }
    }

    public boolean SetWcControl(int isStart) {
        try {
            return sService.SetWcControl(isStart);
        } catch (RemoteException e) {
            return false;
        }
    }

    public String getIdm() {
        try {
            return sService.getIdm();
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
            return null;
        }
    }

    public void networkReset() throws IOException {
        try {
            sService.NetworkResetAtt();
        } catch (RemoteException e) {
            attemptDeadServiceRecovery(e);
            Log.e(TAG, "Fail to networkReset", e);
            throw new IOException("Fail to networkReset");
        }
    }
}
