package android.app;

import android.app.trust.ITrustManager;
import android.app.trust.ITrustManager.Stub;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.util.EventLog;
import android.view.IOnKeyguardExitResult;
import android.view.IWindowManager;
import android.view.WindowManagerGlobal;
import android.widget.RemoteViews;

public class KeyguardManager {
    public static final String ACTION_CONFIRM_DEVICE_CREDENTIAL = "android.app.action.CONFIRM_DEVICE_CREDENTIAL";
    public static final String EXTRA_DESCRIPTION = "android.app.extra.DESCRIPTION";
    public static final String EXTRA_TITLE = "android.app.extra.TITLE";
    private ITrustManager mTrustManager = Stub.asInterface(ServiceManager.getService(Context.TRUST_SERVICE));
    private IWindowManager mWM = WindowManagerGlobal.getWindowManagerService();

    public class KeyguardLock {
        private final String mTag;
        private final IBinder mToken = new Binder();

        KeyguardLock(String tag) {
            this.mTag = tag;
        }

        public void disableKeyguard() {
            try {
                EventLog.writeEvent(36099, new Object[]{Integer.valueOf(Process.myPid()), this.mTag, Integer.valueOf(0)});
                KeyguardManager.this.mWM.disableKeyguard(this.mToken, this.mTag);
            } catch (RemoteException e) {
            }
        }

        public void reenableKeyguard() {
            try {
                EventLog.writeEvent(36099, new Object[]{Integer.valueOf(Process.myPid()), this.mTag, Integer.valueOf(1)});
                KeyguardManager.this.mWM.reenableKeyguard(this.mToken);
            } catch (RemoteException e) {
            }
        }
    }

    public interface OnKeyguardExitResult {
        void onKeyguardExitResult(boolean z);
    }

    public Intent createConfirmDeviceCredentialIntent(CharSequence title, CharSequence description) {
        if (!isKeyguardSecure()) {
            return null;
        }
        Intent intent = new Intent(ACTION_CONFIRM_DEVICE_CREDENTIAL);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_DESCRIPTION, description);
        intent.setPackage("com.android.settings");
        return intent;
    }

    KeyguardManager() {
    }

    @Deprecated
    public KeyguardLock newKeyguardLock(String tag) {
        return new KeyguardLock(tag);
    }

    public boolean isKeyguardLocked() {
        try {
            return this.mWM.isKeyguardLocked();
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean isKeyguardSecure() {
        try {
            return this.mWM.isKeyguardSecure();
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean isKeyguardShowingAndNotOccluded() {
        try {
            return this.mWM.isKeyguardShowingAndNotOccluded();
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean isKeyguardShowingAndOccluded() {
        try {
            return this.mWM.isKeyguardShowingAndOccluded();
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean inKeyguardRestrictedInputMode() {
        try {
            return this.mWM.inKeyguardRestrictedInputMode();
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean isDeviceLocked() {
        return isDeviceLocked(UserHandle.getCallingUserId());
    }

    public boolean isDeviceLocked(int userId) {
        try {
            return this.mTrustManager.isDeviceLocked(userId);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean isDeviceSecure() {
        return isDeviceSecure(UserHandle.getCallingUserId());
    }

    public boolean isDeviceSecure(int userId) {
        try {
            return this.mTrustManager.isDeviceSecure(userId);
        } catch (RemoteException e) {
            return false;
        }
    }

    @Deprecated
    public void exitKeyguardSecurely(final OnKeyguardExitResult callback) {
        try {
            this.mWM.exitKeyguardSecurely(new IOnKeyguardExitResult.Stub() {
                public void onKeyguardExitResult(boolean success) throws RemoteException {
                    if (callback != null) {
                        callback.onKeyguardExitResult(success);
                    }
                }
            });
        } catch (RemoteException e) {
        }
    }

    public void setAdaptiveEvent(String requestClass, RemoteViews smallView, RemoteViews bigView) {
        try {
            this.mWM.setAdaptiveEvent(requestClass, smallView, bigView);
        } catch (RemoteException e) {
        }
    }

    public void removeAdaptiveEvent(String requestClass) {
        try {
            this.mWM.removeAdaptiveEvent(requestClass);
        } catch (RemoteException e) {
        }
    }

    public void updateAdaptiveEvent(String requestClass, RemoteViews smallView, RemoteViews bigView) {
        try {
            this.mWM.updateAdaptiveEvent(requestClass, smallView, bigView);
        } catch (RemoteException e) {
        }
    }

    public void setBendedPendingIntent(PendingIntent p) {
        setBendedPendingIntent(p, null);
    }

    public void setBendedPendingIntent(PendingIntent p, Intent fillInIntent) {
        try {
            this.mWM.setBendedPendingIntent(p, fillInIntent);
        } catch (RemoteException e) {
        }
    }

    public void setBendedPendingIntentInSecure(PendingIntent p, Intent fillInIntent) {
        try {
            this.mWM.setBendedPendingIntentInSecure(p, fillInIntent);
        } catch (RemoteException e) {
        }
    }
}
