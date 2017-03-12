package com.samsung.android.smartclip;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import android.util.Slog;
import android.view.InputEvent;
import com.samsung.android.smartclip.ISpenGestureService.Stub;
import java.util.ArrayList;

public class SpenGestureManager {
    public static final int GLOBAL_AIR_BUTTON_ALL_ENABLE = -1;
    public static final int GLOBAL_AIR_BUTTON_FLASHANNOTATE = 2;
    public static final int GLOBAL_AIR_BUTTON_GALAXYFINDER = 3;
    public static final int GLOBAL_AIR_BUTTON_MULTIWINDOW = 4;
    public static final int GLOBAL_AIR_BUTTON_QUICKMEMO = 0;
    public static final int GLOBAL_AIR_BUTTON_RAKEIN = 1;
    private static String TAG = "SpenGestureManager";
    private Context mContext = null;
    private ISpenGestureService mService = null;

    public SpenGestureManager(Context context) {
        this.mContext = context;
        getService();
    }

    public synchronized boolean isServiceAvailable() {
        boolean z;
        if (Stub.asInterface(ServiceManager.getService("spengestureservice")) == null) {
            Slog.w(TAG, "isServiceAvailable : Service not available");
            z = false;
        } else {
            z = true;
        }
        return z;
    }

    private synchronized ISpenGestureService getService() {
        if (this.mService == null) {
            this.mService = Stub.asInterface(ServiceManager.getService("spengestureservice"));
            if (this.mService == null) {
                Slog.w("SpenGestureManager", "warning: no SpenGestureManager");
            }
        }
        return this.mService;
    }

    public void sendSmartClipRemoteRequestResult(SmartClipRemoteRequestResult result) {
        try {
            ISpenGestureService svc = getService();
            if (svc != null) {
                svc.sendSmartClipRemoteRequestResult(result);
            }
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void setFocusWindow(int focusSurfaceLayer) {
        try {
            if (this.mService != null) {
                Log.i(TAG, "setFocusWindow" + focusSurfaceLayer);
                this.mService.setFocusWindow(focusSurfaceLayer);
            }
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public SmartClipDataRepositoryImpl getSmartClipDataFromCurrentScreen() {
        SmartClipDataRepositoryImpl smartClipDataRepositoryImpl = null;
        try {
            ISpenGestureService svc = getService();
            if (svc != null) {
                smartClipDataRepositoryImpl = svc.getSmartClipDataByScreenRect((Rect) null, (IBinder) null, 1);
            }
            return smartClipDataRepositoryImpl;
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public SmartClipDataRepositoryImpl getSmartClipDataByScreenRect(Rect rect, IBinder skipWindowToken, int extractionMode) {
        try {
            ISpenGestureService svc = getService();
            if (svc != null) {
                return svc.getSmartClipDataByScreenRect(rect, skipWindowToken, extractionMode);
            }
            return null;
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Bundle getScrollableAreaInfo(Rect rect, IBinder skipWindowToken) {
        try {
            ISpenGestureService svc = getService();
            if (svc != null) {
                return svc.getScrollableAreaInfo(rect, skipWindowToken);
            }
            return null;
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Bundle getScrollableViewInfo(Rect rect, int viewHash, IBinder skipWindowToken) {
        try {
            ISpenGestureService svc = getService();
            if (svc != null) {
                return svc.getScrollableViewInfo(rect, viewHash, skipWindowToken);
            }
            return null;
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void injectInputEvent(int targetX, int targetY, ArrayList<InputEvent> inputEvents, boolean waitUntilConsume, IBinder skipWindowToken) {
        try {
            ISpenGestureService svc = getService();
            if (svc != null) {
                svc.injectInputEvent(targetX, targetY, (InputEvent[]) inputEvents.toArray(new InputEvent[inputEvents.size()]), waitUntilConsume, skipWindowToken);
            }
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public int getAirButtonHitTest(int id, int x, int y) {
        try {
            ISpenGestureService svc = getService();
            if (svc != null) {
                return svc.getAirButtonHitTest(id, x, y);
            }
            return -1;
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void setDisableGlobalAirBottonAppindex(int index) {
    }
}
