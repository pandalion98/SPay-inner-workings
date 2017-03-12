package com.samsung.android.multidisplay.virtualscreen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.display.DisplayManagerGlobal;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManagerImpl;
import com.samsung.android.multidisplay.virtualscreen.IVirtualScreenManager.Stub;
import java.lang.ref.WeakReference;

public class VirtualScreenManager {
    private static final boolean DEBUG = true;
    private static final String TAG = "VirtualScreenManager";
    private final WeakReference<Context> mContext;
    private IVirtualScreenManager mService;
    private int mVirtualScreenId = -1;

    public VirtualScreenManager(Context context) {
        this.mContext = new WeakReference(context);
        this.mService = Stub.asInterface(ServiceManager.getService("virtualscreen"));
        if (this.mContext.get() instanceof Activity) {
            Log.d(TAG, "VirtualScreenManager created by Activity Context");
        } else {
            Log.d(TAG, "VirtualScreenManager created by not Activity Context");
        }
    }

    public int createVirtualScreen(Rect bound) {
        try {
            return this.mService.createVirtualScreen(bound);
        } catch (RemoteException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void removeVirtualScreen() {
        try {
            this.mService.removeVirtualScreen();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public int startActivity(Intent intent) {
        return startActivity(intent, null);
    }

    public int startActivity(Intent intent, Bundle options) {
        Log.d(TAG, "startActivity intent=" + intent + " Bundle=" + options);
        try {
            this.mVirtualScreenId = this.mService.startActivity(((Context) this.mContext.get()).getBasePackageName(), intent, options);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return this.mVirtualScreenId;
    }

    public boolean setOffset(int offsetX, int offsetY) {
        return setOffset(offsetX, offsetY, false);
    }

    public boolean setOffset(int offsetX, int offsetY, boolean force) {
        try {
            if (this.mContext.get() instanceof Activity) {
                return this.mService.setOffset(((Activity) this.mContext.get()).getActivityToken(), -1, offsetX, offsetY, force);
            } else if (this.mVirtualScreenId > 0) {
                return this.mService.setOffset(null, this.mVirtualScreenId, offsetX, offsetY, force);
            } else {
                return this.mService.setOffset(null, getDisplayId(), offsetX, offsetY, force);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Point getOffset() {
        try {
            if (this.mVirtualScreenId > -1) {
                return this.mService.getOffset(this.mVirtualScreenId);
            }
            return this.mService.getOffset(getDisplayId());
        } catch (RemoteException e) {
            e.printStackTrace();
            return new Point();
        }
    }

    public boolean bindVirtualScreen() {
        try {
            return this.mService.bindVirtualScreen(((Context) this.mContext.get()).getBasePackageName(), ((Context) this.mContext.get()).getDisplayId());
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean unBindVirtualScreen() {
        try {
            return this.mService.unBindVirtualScreen(((Context) this.mContext.get()).getBasePackageName());
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateDisplayOfWindow(Window window, int displayId) {
        if (window == null) {
            Log.d(TAG, "updateDisplayOfWindow window is null. Invalid!!");
            return false;
        }
        Display display = DisplayManagerGlobal.getInstance().getCompatibleDisplay(displayId, ((Context) this.mContext.get()).getDisplayAdjustments(displayId));
        if (display != null) {
            WindowManagerImpl wm = (WindowManagerImpl) window.getWindowManager();
            if (wm != null) {
                wm.setDisplay(display);
                return true;
            }
        }
        return false;
    }

    public boolean attachToDefaultDisplay() {
        try {
            return this.mService.moveVirtualScreenToDisplay(((Context) this.mContext.get()).getBasePackageName(), 0);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getDisplayId() {
        try {
            Context context = (Context) this.mContext.get();
            if (context instanceof Activity) {
                return this.mService.getDisplayId(((Activity) context).getActivityToken(), null);
            }
            return this.mService.getDisplayId(null, context.getBasePackageName());
        } catch (RemoteException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public Rect getVirtualScreenSize() {
        try {
            if (this.mVirtualScreenId > -1) {
                return this.mService.getVirtualScreenSize(this.mVirtualScreenId);
            }
            return this.mService.getVirtualScreenSize(getDisplayId());
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isMoving() {
        try {
            if (this.mVirtualScreenId > -1) {
                return this.mService.isMoving(this.mVirtualScreenId);
            }
            return this.mService.isMoving(getDisplayId());
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }
}
