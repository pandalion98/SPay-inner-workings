package android.app.trust;

import android.app.trust.ITrustManager.Stub;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.Log;

public class TrustManager {
    private static final String DATA_FLAGS = "initiatedByUser";
    private static final int MSG_TRUST_CHANGED = 1;
    private static final int MSG_TRUST_MANAGED_CHANGED = 2;
    private static final String TAG = "TrustManager";
    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            boolean z = true;
            boolean z2 = false;
            TrustListener trustListener;
            switch (msg.what) {
                case 1:
                    int flags;
                    if (msg.peekData() != null) {
                        flags = msg.peekData().getInt(TrustManager.DATA_FLAGS);
                    } else {
                        flags = 0;
                    }
                    trustListener = (TrustListener) msg.obj;
                    if (msg.arg1 != 0) {
                        z2 = true;
                    }
                    trustListener.onTrustChanged(z2, msg.arg2, flags);
                    return;
                case 2:
                    trustListener = (TrustListener) msg.obj;
                    if (msg.arg1 == 0) {
                        z = false;
                    }
                    trustListener.onTrustManagedChanged(z, msg.arg2);
                    return;
                default:
                    return;
            }
        }
    };
    private final ITrustManager mService;
    private final ArrayMap<TrustListener, ITrustListener> mTrustListeners;

    public interface TrustListener {
        void onTrustChanged(boolean z, int i, int i2);

        void onTrustManagedChanged(boolean z, int i);
    }

    public TrustManager(IBinder b) {
        this.mService = Stub.asInterface(b);
        this.mTrustListeners = new ArrayMap();
    }

    public void reportUnlockAttempt(boolean successful, int userId) {
        try {
            this.mService.reportUnlockAttempt(successful, userId);
        } catch (RemoteException e) {
            onError(e);
        }
    }

    public void reportEnabledTrustAgentsChanged(int userId) {
        try {
            this.mService.reportEnabledTrustAgentsChanged(userId);
        } catch (RemoteException e) {
            onError(e);
        }
    }

    public void reportKeyguardShowingChanged() {
        try {
            this.mService.reportKeyguardShowingChanged();
        } catch (RemoteException e) {
            onError(e);
        }
    }

    public void registerTrustListener(final TrustListener trustListener) {
        try {
            ITrustListener.Stub iTrustListener = new ITrustListener.Stub() {
                public void onTrustChanged(boolean enabled, int userId, int flags) {
                    Message m = TrustManager.this.mHandler.obtainMessage(1, enabled ? 1 : 0, userId, trustListener);
                    if (flags != 0) {
                        m.getData().putInt(TrustManager.DATA_FLAGS, flags);
                    }
                    m.sendToTarget();
                }

                public void onTrustManagedChanged(boolean managed, int userId) {
                    TrustManager.this.mHandler.obtainMessage(2, managed ? 1 : 0, userId, trustListener).sendToTarget();
                }
            };
            this.mService.registerTrustListener(iTrustListener);
            this.mTrustListeners.put(trustListener, iTrustListener);
        } catch (RemoteException e) {
            onError(e);
        }
    }

    public void unregisterTrustListener(TrustListener trustListener) {
        ITrustListener iTrustListener = (ITrustListener) this.mTrustListeners.remove(trustListener);
        if (iTrustListener != null) {
            try {
                this.mService.unregisterTrustListener(iTrustListener);
            } catch (RemoteException e) {
                onError(e);
            }
        }
    }

    private void onError(Exception e) {
        Log.e(TAG, "Error while calling TrustManagerService", e);
    }
}
