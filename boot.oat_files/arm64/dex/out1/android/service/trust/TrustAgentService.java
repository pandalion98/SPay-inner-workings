package android.service.trust;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.service.trust.ITrustAgentService.Stub;
import android.util.Log;
import android.util.Slog;
import java.util.List;

public class TrustAgentService extends Service {
    private static final boolean DEBUG = false;
    public static final int FLAG_GRANT_TRUST_DISMISS_KEYGUARD = 2;
    public static final int FLAG_GRANT_TRUST_INITIATED_BY_USER = 1;
    private static final int MSG_CONFIGURE = 2;
    private static final int MSG_DEVICE_LOCKED = 4;
    private static final int MSG_DEVICE_UNLOCKED = 5;
    private static final int MSG_TRUST_TIMEOUT = 3;
    private static final int MSG_UNLOCK_ATTEMPT = 1;
    public static final String SERVICE_INTERFACE = "android.service.trust.TrustAgentService";
    public static final String TRUST_AGENT_META_DATA = "android.service.trust.trustagent";
    private final String TAG = (TrustAgentService.class.getSimpleName() + "[" + getClass().getSimpleName() + "]");
    private ITrustAgentServiceCallback mCallback;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    TrustAgentService.this.onUnlockAttempt(msg.arg1 != 0);
                    return;
                case 2:
                    ConfigurationData data = msg.obj;
                    boolean result = TrustAgentService.this.onConfigure(data.options);
                    if (data.token != null) {
                        try {
                            synchronized (TrustAgentService.this.mLock) {
                                TrustAgentService.this.mCallback.onConfigureCompleted(result, data.token);
                            }
                            return;
                        } catch (RemoteException e) {
                            TrustAgentService.this.onError("calling onSetTrustAgentFeaturesEnabledCompleted()");
                            return;
                        }
                    }
                    return;
                case 3:
                    TrustAgentService.this.onTrustTimeout();
                    return;
                case 4:
                    TrustAgentService.this.onDeviceLocked();
                    return;
                case 5:
                    TrustAgentService.this.onDeviceUnlocked();
                    return;
                default:
                    return;
            }
        }
    };
    private final Object mLock = new Object();
    private boolean mManagingTrust;
    private Runnable mPendingGrantTrustTask;

    private static final class ConfigurationData {
        final List<PersistableBundle> options;
        final IBinder token;

        ConfigurationData(List<PersistableBundle> opts, IBinder t) {
            this.options = opts;
            this.token = t;
        }
    }

    private final class TrustAgentServiceWrapper extends Stub {
        private TrustAgentServiceWrapper() {
        }

        public void onUnlockAttempt(boolean successful) {
            int i;
            Handler access$400 = TrustAgentService.this.mHandler;
            if (successful) {
                i = 1;
            } else {
                i = 0;
            }
            access$400.obtainMessage(1, i, 0).sendToTarget();
        }

        public void onTrustTimeout() {
            TrustAgentService.this.mHandler.sendEmptyMessage(3);
        }

        public void onConfigure(List<PersistableBundle> args, IBinder token) {
            TrustAgentService.this.mHandler.obtainMessage(2, new ConfigurationData(args, token)).sendToTarget();
        }

        public void onDeviceLocked() throws RemoteException {
            TrustAgentService.this.mHandler.obtainMessage(4).sendToTarget();
        }

        public void onDeviceUnlocked() throws RemoteException {
            TrustAgentService.this.mHandler.obtainMessage(5).sendToTarget();
        }

        public void setCallback(ITrustAgentServiceCallback callback) {
            synchronized (TrustAgentService.this.mLock) {
                TrustAgentService.this.mCallback = callback;
                if (TrustAgentService.this.mManagingTrust) {
                    try {
                        TrustAgentService.this.mCallback.setManagingTrust(TrustAgentService.this.mManagingTrust);
                    } catch (RemoteException e) {
                        TrustAgentService.this.onError("calling setManagingTrust()");
                    }
                }
                if (TrustAgentService.this.mPendingGrantTrustTask != null) {
                    TrustAgentService.this.mPendingGrantTrustTask.run();
                    TrustAgentService.this.mPendingGrantTrustTask = null;
                }
            }
        }
    }

    public void onCreate() {
        super.onCreate();
        ComponentName component = new ComponentName(this, getClass());
        try {
            if (!"android.permission.BIND_TRUST_AGENT".equals(getPackageManager().getServiceInfo(component, 0).permission)) {
                throw new IllegalStateException(component.flattenToShortString() + " is not declared with the permission " + "\"" + "android.permission.BIND_TRUST_AGENT" + "\"");
            }
        } catch (NameNotFoundException e) {
            Log.e(this.TAG, "Can't get ServiceInfo for " + component.toShortString());
        }
    }

    public void onUnlockAttempt(boolean successful) {
    }

    public void onTrustTimeout() {
    }

    public void onDeviceLocked() {
    }

    public void onDeviceUnlocked() {
    }

    private void onError(String msg) {
        Slog.v(this.TAG, "Remote exception while " + msg);
    }

    public boolean onConfigure(List<PersistableBundle> list) {
        return false;
    }

    @Deprecated
    public final void grantTrust(CharSequence message, long durationMs, boolean initiatedByUser) {
        grantTrust(message, durationMs, initiatedByUser ? 1 : 0);
    }

    public final void grantTrust(CharSequence message, long durationMs, int flags) {
        synchronized (this.mLock) {
            if (this.mManagingTrust) {
                if (this.mCallback != null) {
                    try {
                        this.mCallback.grantTrust(message.toString(), durationMs, flags);
                    } catch (RemoteException e) {
                        onError("calling enableTrust()");
                    }
                } else {
                    final CharSequence charSequence = message;
                    final long j = durationMs;
                    final int i = flags;
                    this.mPendingGrantTrustTask = new Runnable() {
                        public void run() {
                            TrustAgentService.this.grantTrust(charSequence, j, i);
                        }
                    };
                }
            } else {
                throw new IllegalStateException("Cannot grant trust if agent is not managing trust. Call setManagingTrust(true) first.");
            }
        }
    }

    public final void revokeTrust() {
        synchronized (this.mLock) {
            if (this.mPendingGrantTrustTask != null) {
                this.mPendingGrantTrustTask = null;
            }
            if (this.mCallback != null) {
                try {
                    this.mCallback.revokeTrust();
                } catch (RemoteException e) {
                    onError("calling revokeTrust()");
                }
            }
        }
    }

    public final void setManagingTrust(boolean managingTrust) {
        synchronized (this.mLock) {
            if (this.mManagingTrust != managingTrust) {
                this.mManagingTrust = managingTrust;
                if (this.mCallback != null) {
                    try {
                        this.mCallback.setManagingTrust(managingTrust);
                    } catch (RemoteException e) {
                        onError("calling setManagingTrust()");
                    }
                }
            }
        }
    }

    public final IBinder onBind(Intent intent) {
        return new TrustAgentServiceWrapper();
    }
}
