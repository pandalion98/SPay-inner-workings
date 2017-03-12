package com.samsung.android.cover;

import android.content.ComponentName;
import android.content.Context;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import android.util.Slog;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import com.samsung.android.cover.ICoverManagerCallback.Stub;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class CoverManager {
    public static final int COVER_MODE_HIDE_SVIEW_ONCE = 2;
    public static final int COVER_MODE_NONE = 0;
    public static final int COVER_MODE_SVIEW = 1;
    private static final String TAG = "CoverManager";
    private static boolean sIsFilpCoverSystemFeatureEnabled = false;
    private static boolean sIsNfcLedCoverSystemFeatureEnabled = false;
    private static boolean sIsSViewCoverSystemFeatureEnabled = false;
    private static boolean sIsSystemFeatureQueried = false;
    private Context mContext;
    private final CopyOnWriteArrayList<CoverStateListenerDelegate> mCoverStateListenerDelegates = new CopyOnWriteArrayList();
    private final CopyOnWriteArrayList<CoverListenerDelegate> mLcdOffDisableDelegates = new CopyOnWriteArrayList();
    private final CopyOnWriteArrayList<CoverListenerDelegate> mListenerDelegates = new CopyOnWriteArrayList();
    private final CopyOnWriteArrayList<NfcLedCoverTouchListenerDelegate> mNfcLedCoverTouchListenerDelegates = new CopyOnWriteArrayList();
    private ICoverManager mService;
    private IBinder mToken = new Binder();

    public static class StateListener {
        public void onCoverStateChanged(CoverState state) {
        }
    }

    private class CoverListenerDelegate extends Stub {
        private Handler mHandler;
        private final StateListener mListener;

        CoverListenerDelegate(StateListener listener, Handler handler) {
            this.mListener = listener;
            this.mHandler = new Handler(handler == null ? CoverManager.this.mContext.getMainLooper() : handler.getLooper(), CoverManager.this) {
                public void handleMessage(Message msg) {
                    if (CoverListenerDelegate.this.mListener != null) {
                        CoverState coverState = msg.obj;
                        if (coverState != null) {
                            CoverListenerDelegate.this.mListener.onCoverStateChanged(coverState);
                        } else {
                            Log.e(CoverManager.TAG, "coverState : null");
                        }
                    }
                }
            };
        }

        public StateListener getListener() {
            return this.mListener;
        }

        public void coverCallback(CoverState state) throws RemoteException {
            Message msg = Message.obtain();
            msg.what = 0;
            msg.obj = state;
            this.mHandler.sendMessage(msg);
        }

        public String getListenerInfo() throws RemoteException {
            return this.mListener.toString();
        }
    }

    public static class CoverStateListener {
        public void onCoverSwitchStateChanged(boolean switchState) {
        }

        public void onCoverAttachStateChanged(boolean attached) {
        }
    }

    private class CoverStateListenerDelegate extends ICoverStateListenerCallback.Stub {
        private static final int MSG_LISTEN_COVER_ATTACH_STATE_CHANGE = 1;
        private static final int MSG_LISTEN_COVER_SWITCH_STATE_CHANGE = 0;
        public static final int TYPE_COVER_STATE_LISTENER = 2;
        private Handler mHandler;
        private final CoverStateListener mListener;

        CoverStateListenerDelegate(CoverStateListener listener, Handler handler) {
            this.mListener = listener;
            this.mHandler = new Handler(handler == null ? CoverManager.this.mContext.getMainLooper() : handler.getLooper(), CoverManager.this) {
                public void handleMessage(Message msg) {
                    boolean z = true;
                    if (CoverStateListenerDelegate.this.mListener != null) {
                        CoverStateListener access$200;
                        switch (msg.what) {
                            case 0:
                                access$200 = CoverStateListenerDelegate.this.mListener;
                                if (msg.arg1 != 1) {
                                    z = false;
                                }
                                access$200.onCoverSwitchStateChanged(z);
                                return;
                            case 1:
                                access$200 = CoverStateListenerDelegate.this.mListener;
                                if (msg.arg1 != 1) {
                                    z = false;
                                }
                                access$200.onCoverAttachStateChanged(z);
                                return;
                            default:
                                return;
                        }
                    }
                }
            };
        }

        public CoverStateListener getListener() {
            return this.mListener;
        }

        public void onCoverSwitchStateChanged(boolean switchState) throws RemoteException {
            int i;
            Handler handler = this.mHandler;
            if (switchState) {
                i = 1;
            } else {
                i = 0;
            }
            Message.obtain(handler, 0, i, 0).sendToTarget();
        }

        public void onCoverAttachStateChanged(boolean attached) throws RemoteException {
            int i;
            Handler handler = this.mHandler;
            if (attached) {
                i = 1;
            } else {
                i = 0;
            }
            Message.obtain(handler, 1, i, 0).sendToTarget();
        }

        public String getListenerInfo() throws RemoteException {
            return this.mListener.toString();
        }
    }

    public static class NfcLedCoverTouchListener {
        public void onCoverTouchAccept() {
        }

        public void onCoverTouchReject() {
        }
    }

    private class NfcLedCoverTouchListenerDelegate extends INfcLedCoverTouchListenerCallback.Stub {
        private static final int MSG_LISTEN_COVER_TOUCH_ACCEPT = 0;
        private static final int MSG_LISTEN_COVER_TOUCH_REJECT = 1;
        private Handler mHandler;
        private NfcLedCoverTouchListener mListener;

        NfcLedCoverTouchListenerDelegate(NfcLedCoverTouchListener listener, Handler handler) {
            this.mListener = listener;
            this.mHandler = new Handler(handler == null ? CoverManager.this.mContext.getMainLooper() : handler.getLooper(), CoverManager.this) {
                public void handleMessage(Message msg) {
                    if (NfcLedCoverTouchListenerDelegate.this.mListener != null) {
                        switch (msg.what) {
                            case 0:
                                NfcLedCoverTouchListenerDelegate.this.mListener.onCoverTouchAccept();
                                return;
                            case 1:
                                NfcLedCoverTouchListenerDelegate.this.mListener.onCoverTouchReject();
                                return;
                            default:
                                return;
                        }
                    }
                }
            };
        }

        public Object getListener() {
            return this.mListener;
        }

        public void onCoverTouchAccept() throws RemoteException {
            this.mHandler.obtainMessage(0).sendToTarget();
        }

        public void onCoverTouchReject() throws RemoteException {
            this.mHandler.obtainMessage(1).sendToTarget();
        }
    }

    public CoverManager(Context context) {
        this.mContext = context;
        initSystemFeature();
    }

    private void initSystemFeature() {
        if (!sIsSystemFeatureQueried) {
            sIsFilpCoverSystemFeatureEnabled = this.mContext.getPackageManager().hasSystemFeature("com.sec.feature.cover.flip");
            sIsSViewCoverSystemFeatureEnabled = this.mContext.getPackageManager().hasSystemFeature("com.sec.feature.cover.sview");
            sIsNfcLedCoverSystemFeatureEnabled = this.mContext.getPackageManager().hasSystemFeature("com.sec.feature.cover.nfcledcover");
            sIsSystemFeatureQueried = true;
        }
    }

    boolean isSupportCover() {
        return sIsFilpCoverSystemFeatureEnabled || sIsSViewCoverSystemFeatureEnabled || sIsNfcLedCoverSystemFeatureEnabled;
    }

    boolean isSupportNfcLedCover() {
        return sIsNfcLedCoverSystemFeatureEnabled;
    }

    boolean isSupportSViewCover() {
        return sIsSViewCoverSystemFeatureEnabled;
    }

    boolean isSupportTypeOfCover(int type) {
        switch (type) {
            case 0:
                return sIsFilpCoverSystemFeatureEnabled;
            case 1:
            case 3:
            case 6:
                return sIsSViewCoverSystemFeatureEnabled;
            default:
                return false;
        }
    }

    private synchronized ICoverManager getService() {
        if (this.mService == null) {
            this.mService = ICoverManager.Stub.asInterface(ServiceManager.getService("cover"));
            if (this.mService == null) {
                Slog.w(TAG, "warning: no COVER_MANAGER_SERVICE");
            }
        }
        return this.mService;
    }

    public void setCoverModeToWindow(Window window, int coverMode) {
        if (!isSupportSViewCover()) {
            Log.w(TAG, "setSViewCoverModeToWindow : This device is not supported s view cover");
        } else if (Process.myUid() != 1000) {
            throw new SecurityException("CoverManager only available from system UID.");
        } else {
            LayoutParams wlp = window.getAttributes();
            if (wlp != null) {
                wlp.coverMode = coverMode;
                window.setAttributes(wlp);
            }
        }
    }

    public void registerListener(StateListener listener) {
        Log.d(TAG, "registerListener");
        if (!isSupportCover()) {
            Log.w(TAG, "registerListener : This device is not supported cover");
        } else if (Process.myUid() != 1000) {
            throw new SecurityException("CoverManager only available from system UID.");
        } else if (listener == null) {
            Log.w(TAG, "registerListener : listener is null");
        } else {
            CoverListenerDelegate coverListener = null;
            Iterator<CoverListenerDelegate> i = this.mListenerDelegates.iterator();
            while (i.hasNext()) {
                CoverListenerDelegate delegate = (CoverListenerDelegate) i.next();
                if (delegate.getListener().equals(listener)) {
                    coverListener = delegate;
                    break;
                }
            }
            if (coverListener == null) {
                coverListener = new CoverListenerDelegate(listener, null);
                this.mListenerDelegates.add(coverListener);
            }
            try {
                ICoverManager svc = getService();
                if (svc != null) {
                    ComponentName cm = new ComponentName(this.mContext.getPackageName(), getClass().getCanonicalName());
                    if (coverListener != null && cm != null) {
                        svc.registerCallback(coverListener, cm);
                    }
                }
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in registerListener: ", e);
            }
        }
    }

    public void registerListener(CoverStateListener listener) {
        Log.d(TAG, "registerListener");
        if (!isSupportCover()) {
            Log.w(TAG, "registerListener : This device is not supported cover");
        } else if (Process.myUid() != 1000) {
            throw new SecurityException("CoverManager only available from system UID.");
        } else if (listener == null) {
            Log.w(TAG, "registerListener : listener is null");
        } else {
            CoverStateListenerDelegate coverListener = null;
            Iterator<CoverStateListenerDelegate> i = this.mCoverStateListenerDelegates.iterator();
            while (i.hasNext()) {
                CoverStateListenerDelegate delegate = (CoverStateListenerDelegate) i.next();
                if (delegate.getListener().equals(listener)) {
                    coverListener = delegate;
                    break;
                }
            }
            if (coverListener == null) {
                coverListener = new CoverStateListenerDelegate(listener, null);
                this.mCoverStateListenerDelegates.add(coverListener);
            }
            try {
                ICoverManager svc = getService();
                if (svc != null) {
                    ComponentName cm = new ComponentName(this.mContext.getPackageName(), getClass().getCanonicalName());
                    if (coverListener != null && cm != null) {
                        svc.registerListenerCallback(coverListener, cm, 2);
                    }
                }
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in registerListener: ", e);
            }
        }
    }

    public void unregisterListener(StateListener listener) {
        Log.d(TAG, "unregisterListener");
        if (!isSupportCover()) {
            Log.w(TAG, "unregisterListener : This device is not supported cover");
        } else if (Process.myUid() != 1000) {
            throw new SecurityException("CoverManager only available from system UID.");
        } else if (listener == null) {
            Log.w(TAG, "unregisterListener : listener is null");
        } else {
            CoverListenerDelegate coverListener = null;
            Iterator<CoverListenerDelegate> i = this.mListenerDelegates.iterator();
            while (i.hasNext()) {
                CoverListenerDelegate delegate = (CoverListenerDelegate) i.next();
                if (delegate.getListener().equals(listener)) {
                    coverListener = delegate;
                    break;
                }
            }
            if (coverListener != null) {
                try {
                    ICoverManager svc = getService();
                    if (svc != null && svc.unregisterCallback(coverListener)) {
                        this.mListenerDelegates.remove(coverListener);
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "RemoteException in unregisterListener: ", e);
                }
            }
        }
    }

    public void unregisterListener(CoverStateListener listener) {
        Log.d(TAG, "unregisterListener");
        if (!isSupportCover()) {
            Log.w(TAG, "unregisterListener : This device is not supported cover");
        } else if (Process.myUid() != 1000) {
            throw new SecurityException("CoverManager only available from system UID.");
        } else if (listener == null) {
            Log.w(TAG, "unregisterListener : listener is null");
        } else {
            CoverStateListenerDelegate coverListener = null;
            Iterator<CoverStateListenerDelegate> i = this.mCoverStateListenerDelegates.iterator();
            while (i.hasNext()) {
                CoverStateListenerDelegate delegate = (CoverStateListenerDelegate) i.next();
                if (delegate.getListener().equals(listener)) {
                    coverListener = delegate;
                    break;
                }
            }
            if (coverListener != null) {
                try {
                    ICoverManager svc = getService();
                    if (svc != null && svc.unregisterCallback(coverListener)) {
                        this.mCoverStateListenerDelegates.remove(coverListener);
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "RemoteException in unregisterListener: ", e);
                }
            }
        }
    }

    public CoverState getCoverState() {
        if (!isSupportCover()) {
            Log.w(TAG, "getCoverState : This device is not supported cover");
            return null;
        } else if (Process.myUid() != 1000) {
            throw new SecurityException("CoverManager only available from system UID.");
        } else {
            try {
                ICoverManager svc = getService();
                if (svc != null) {
                    CoverState coverState = svc.getCoverState();
                    if (coverState != null) {
                        return coverState;
                    }
                    Log.e(TAG, "getCoverState : coverState is null");
                }
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in getCoverState: ", e);
            }
            return null;
        }
    }

    public void sendDataToCover(int command, byte[] data) {
        ICoverManager svc = getService();
        if (svc != null) {
            try {
                svc.sendDataToCover(command, data);
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in sendData : ", e);
            }
        }
    }

    public void sendDataToNfcLedCover(int command, byte[] data) {
        ICoverManager svc = getService();
        if (svc != null) {
            try {
                svc.sendDataToNfcLedCover(command, data);
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in sendData to NFC : ", e);
            }
        }
    }

    public void sendPowerKeyToCover() {
        ICoverManager svc = getService();
        if (svc != null) {
            try {
                svc.sendPowerKeyToCover();
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in sendPowerKeyToCover() : ", e);
            }
        }
    }

    public boolean isCoverManagerDisabled() {
        boolean disabled = false;
        try {
            ICoverManager svc = getService();
            if (svc != null) {
                disabled = svc.isCoverManagerDisabled();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in isCoverManagerDisabled : ", e);
        }
        return disabled;
    }

    public void disableCoverManager(boolean disable) {
        try {
            ICoverManager svc = getService();
            if (svc != null) {
                svc.disableCoverManager(disable, this.mToken, this.mContext.getPackageName());
            }
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in disalbeCoverManager : ", e);
        }
    }

    public void registerNfcTouchListener(int type, NfcLedCoverTouchListener listener) {
        Log.d(TAG, "registerNfcTouchListener");
        if (!isSupportNfcLedCover()) {
            Log.w(TAG, "registerNfcTouchListener : This device does not support NFC Led cover");
        } else if (Process.myUid() != 1000) {
            throw new SecurityException("CoverManager only available from system UID.");
        } else if (listener == null) {
            Log.w(TAG, "registerNfcTouchListener : listener is null");
        } else {
            NfcLedCoverTouchListenerDelegate nfcTouchListener = null;
            Iterator<NfcLedCoverTouchListenerDelegate> i = this.mNfcLedCoverTouchListenerDelegates.iterator();
            while (i.hasNext()) {
                NfcLedCoverTouchListenerDelegate delegate = (NfcLedCoverTouchListenerDelegate) i.next();
                if (delegate.getListener().equals(listener)) {
                    nfcTouchListener = delegate;
                    break;
                }
            }
            if (nfcTouchListener == null) {
                nfcTouchListener = new NfcLedCoverTouchListenerDelegate(listener, null);
                this.mNfcLedCoverTouchListenerDelegates.add(nfcTouchListener);
            }
            try {
                ICoverManager svc = getService();
                if (svc != null) {
                    ComponentName cm = new ComponentName(this.mContext.getPackageName(), getClass().getCanonicalName());
                    if (nfcTouchListener != null && cm != null) {
                        svc.registerNfcTouchListenerCallback(type, nfcTouchListener, cm);
                    }
                }
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in registerNfcTouchListener: ", e);
            }
        }
    }

    public void unregisterNfcTouchListener(NfcLedCoverTouchListener listener) {
        Log.d(TAG, "unregisterNfcTouchListener");
        if (!isSupportNfcLedCover()) {
            Log.w(TAG, "unregisterNfcTouchListener : This device does not support NFC Led cover");
        } else if (Process.myUid() != 1000) {
            throw new SecurityException("CoverManager only available from system UID.");
        } else if (listener == null) {
            Log.w(TAG, "unregisterNfcTouchListener : listener is null");
        } else {
            NfcLedCoverTouchListenerDelegate nfcTouchListener = null;
            Iterator<NfcLedCoverTouchListenerDelegate> i = this.mNfcLedCoverTouchListenerDelegates.iterator();
            while (i.hasNext()) {
                NfcLedCoverTouchListenerDelegate delegate = (NfcLedCoverTouchListenerDelegate) i.next();
                if (delegate.getListener().equals(listener)) {
                    nfcTouchListener = delegate;
                    break;
                }
            }
            if (nfcTouchListener != null) {
                try {
                    ICoverManager svc = getService();
                    if (svc != null && svc.unregisterNfcTouchListenerCallback(nfcTouchListener)) {
                        this.mNfcLedCoverTouchListenerDelegates.remove(nfcTouchListener);
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "RemoteException in unregisterNfcTouchListener: ", e);
                }
            }
        }
    }

    public boolean disableLcdOffByCover(StateListener listener) {
        if (!isSupportCover()) {
            Log.w(TAG, "disableLcdOffByCover : This device does not support cover");
            return false;
        } else if (listener == null) {
            Log.w(TAG, "disableLcdOffByCover : listener cannot be null");
            return false;
        } else {
            Log.d(TAG, "disableLcdOffByCover");
            CoverListenerDelegate coverListener = null;
            Iterator<CoverListenerDelegate> i = this.mLcdOffDisableDelegates.iterator();
            while (i.hasNext()) {
                CoverListenerDelegate delegate = (CoverListenerDelegate) i.next();
                if (delegate.getListener().equals(listener)) {
                    coverListener = delegate;
                    break;
                }
            }
            if (coverListener == null) {
                coverListener = new CoverListenerDelegate(listener, null);
            }
            try {
                ICoverManager svc = getService();
                if (svc == null || !svc.disableLcdOffByCover(coverListener, new ComponentName(this.mContext.getPackageName(), getClass().getCanonicalName()))) {
                    return false;
                }
                this.mLcdOffDisableDelegates.add(coverListener);
                return true;
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in unregisterNfcTouchListener: ", e);
                return false;
            }
        }
    }

    public boolean enableLcdOffByCover(StateListener listener) {
        if (!isSupportCover()) {
            Log.w(TAG, "enableLcdOffByCover : This device does not support cover");
            return false;
        } else if (listener == null) {
            Log.w(TAG, "enableLcdOffByCover : listener cannot be null");
            return false;
        } else {
            Log.d(TAG, "enableLcdOffByCover");
            CoverListenerDelegate coverListener = null;
            Iterator<CoverListenerDelegate> i = this.mLcdOffDisableDelegates.iterator();
            while (i.hasNext()) {
                CoverListenerDelegate delegate = (CoverListenerDelegate) i.next();
                if (delegate.getListener().equals(listener)) {
                    coverListener = delegate;
                    break;
                }
            }
            if (coverListener == null) {
                Log.e(TAG, "enableLcdOffByCover: Matching listener not found, cannot enable");
                return false;
            }
            try {
                ICoverManager svc = getService();
                if (svc == null || !svc.enableLcdOffByCover(coverListener, new ComponentName(this.mContext.getPackageName(), getClass().getCanonicalName()))) {
                    return false;
                }
                this.mLcdOffDisableDelegates.remove(coverListener);
                return true;
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in unregisterNfcTouchListener: ", e);
                return false;
            }
        }
    }
}
