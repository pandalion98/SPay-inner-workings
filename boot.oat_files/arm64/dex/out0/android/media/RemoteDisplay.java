package android.media;

import android.os.Handler;
import android.util.Log;
import android.view.Surface;
import dalvik.system.CloseGuard;

public final class RemoteDisplay {
    public static final int DISPLAY_ERROR_CONNECTION_DROPPED = 2;
    public static final int DISPLAY_ERROR_UNKOWN = 1;
    public static final int DISPLAY_FLAG_SECURE = 1;
    private static final String TAG = "RemoteDisplay_Java";
    private static RemoteDisplayCallback mRemoteDisplayCallback = null;
    private final CloseGuard mGuard = CloseGuard.get();
    private final Handler mHandler;
    private final Listener mListener;
    private final String mOpPackageName;
    private long mPtr;

    public interface Listener {
        void onDisplayConnected(Surface surface, int i, int i2, int i3, int i4);

        void onDisplayDisconnected();

        void onDisplayError(int i);
    }

    private native void nativeDispose(long j);

    private native long nativeListen(String str, String str2);

    private native long nativeListen(String str, String str2, String str3);

    private native void nativePause(long j);

    private native void nativeResume(long j);

    private static native int nativeSetParam(String str);

    public static native void nativeStartUIBC(Object obj);

    public static native void nativeStopUIBC();

    private RemoteDisplay(Listener listener, Handler handler, String opPackageName) {
        this.mListener = listener;
        this.mHandler = handler;
        this.mOpPackageName = opPackageName;
        if (mRemoteDisplayCallback == null) {
            mRemoteDisplayCallback = new RemoteDisplayCallback();
        }
    }

    protected void finalize() throws Throwable {
        try {
            dispose(true);
        } finally {
            super.finalize();
        }
    }

    public static RemoteDisplay listen(String iface, Listener listener, Handler handler, String opPackageName) {
        if (iface == null) {
            throw new IllegalArgumentException("iface must not be null");
        } else if (listener == null) {
            throw new IllegalArgumentException("listener must not be null");
        } else if (handler == null) {
            throw new IllegalArgumentException("handler must not be null");
        } else {
            RemoteDisplay display = new RemoteDisplay(listener, handler, opPackageName);
            display.startListening(iface);
            return display;
        }
    }

    public static RemoteDisplay listen(String iface, Listener listener, Handler handler, String opPackageName, String setparamInfo) {
        if (iface == null) {
            throw new IllegalArgumentException("iface must not be null");
        } else if (listener == null) {
            throw new IllegalArgumentException("listener must not be null");
        } else if (handler == null) {
            throw new IllegalArgumentException("handler must not be null");
        } else {
            RemoteDisplay display = new RemoteDisplay(listener, handler, opPackageName);
            display.startListening(iface, setparamInfo);
            return display;
        }
    }

    public void dispose() {
        dispose(false);
    }

    public void pause() {
        nativePause(this.mPtr);
    }

    public void resume() {
        nativeResume(this.mPtr);
    }

    private void dispose(boolean finalized) {
        if (this.mPtr != 0) {
            if (this.mGuard != null) {
                if (finalized) {
                    this.mGuard.warnIfOpen();
                } else {
                    this.mGuard.close();
                }
            }
            nativeDispose(this.mPtr);
            this.mPtr = 0;
        }
    }

    private void startListening(String iface) {
        this.mPtr = nativeListen(iface, this.mOpPackageName);
        if (this.mPtr == 0) {
            throw new IllegalStateException("Could not start listening for remote display connection on \"" + iface + "\"");
        }
        this.mGuard.open("dispose");
    }

    private void startListening(String iface, String initParam) {
        this.mPtr = nativeListen(iface, this.mOpPackageName, initParam);
        if (this.mPtr == 0) {
            throw new IllegalStateException("Could not start listening for remote display connection on \"" + iface + "\"");
        }
        this.mGuard.open("dispose");
    }

    private void notifyDisplayConnected(Surface surface, int width, int height, int flags, int session) {
        final Surface surface2 = surface;
        final int i = width;
        final int i2 = height;
        final int i3 = flags;
        final int i4 = session;
        this.mHandler.post(new Runnable() {
            public void run() {
                RemoteDisplay.this.mListener.onDisplayConnected(surface2, i, i2, i3, i4);
            }
        });
    }

    private void notifyDisplayDisconnected() {
        this.mHandler.post(new Runnable() {
            public void run() {
                RemoteDisplay.this.mListener.onDisplayDisconnected();
            }
        });
    }

    private void notifyDisplayError(final int error) {
        this.mHandler.post(new Runnable() {
            public void run() {
                RemoteDisplay.this.mListener.onDisplayError(error);
            }
        });
    }

    private void cbFromNativeWFD(final int msg, final String data) {
        this.mHandler.post(new Runnable() {
            public void run() {
                RemoteDisplay.mRemoteDisplayCallback.onNoti(msg, data);
            }
        });
    }

    public static int setParam(int type, String data) {
        int iRet = nativeSetParam(data);
        Log.d(TAG, "setParam >> ret is " + iRet);
        return iRet;
    }

    public boolean isDongleRenameAvailable() {
        return mRemoteDisplayCallback.isDongleRenameAvailable();
    }

    public void setDeviceName(String name) {
        mRemoteDisplayCallback.setDeviceName(name);
    }
}
