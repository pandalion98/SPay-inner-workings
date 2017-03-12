package com.samsung.android.toolbox;

import android.content.Context;
import android.os.Binder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import com.samsung.android.toolbox.ITwToolBoxService.Stub;

public class TwToolBoxManager {
    private static final String[] EMPTY = new String[0];
    private static final String TAG = "toolbox";
    private final ITwToolBoxService mService;
    private final Binder mToken;

    public TwToolBoxManager() {
        this.mToken = new Binder();
        this.mService = Stub.asInterface(ServiceManager.getService("tw_toolbox"));
    }

    public TwToolBoxManager(Context context) {
        this();
    }

    public boolean registerCallback(ITwToolBoxServiceCallback callback) {
        boolean z = false;
        if (this.mService == null) {
            Log.w(TAG, "Failed to registerCallback(); no TwToolBoxService.");
        } else {
            try {
                z = this.mService.registerCallback(callback);
            } catch (RemoteException e) {
            } catch (Exception e2) {
            }
        }
        return z;
    }

    public boolean unregisterCallback(ITwToolBoxServiceCallback callback) {
        boolean z = false;
        if (this.mService == null) {
            Log.w(TAG, "Failed to unregisterCallback(); no TwToolBoxService.");
        } else {
            try {
                z = this.mService.unregisterCallback(callback);
            } catch (RemoteException e) {
            } catch (Exception e2) {
            }
        }
        return z;
    }

    public boolean isContain(int x, int y) {
        boolean z = false;
        if (this.mService == null) {
            Log.w(TAG, "Failed to isContain(); no TwToolBoxService.");
        } else {
            try {
                z = this.mService.isContain(x, y);
            } catch (RemoteException e) {
            } catch (Exception e2) {
            }
        }
        return z;
    }

    public void sendMessage(String pkg, int message, int option) {
        if (this.mService == null) {
            Log.w(TAG, "Failed to sendMessage(); no TwToolBoxService.");
            return;
        }
        try {
            this.mService.sendMessage(pkg, message, option);
        } catch (RemoteException e) {
        } catch (Exception e2) {
        }
    }

    public String[] getToolList() {
        if (this.mService == null) {
            Log.w(TAG, "Failed to getToolList(); no TwToolBoxService.");
            return EMPTY;
        }
        try {
            String toolList = this.mService.getToolList();
            Log.d(TAG, "TwToolBoxManager getToolList()");
            if (toolList == null) {
                return EMPTY;
            }
            if (toolList.length() > 3 && toolList.startsWith("0;")) {
                toolList = toolList.substring(2);
                Log.d(TAG, "TwToolBoxManager getToolList() substring()");
            }
            return toolList.split(";");
        } catch (RemoteException e) {
            return EMPTY;
        } catch (Exception e2) {
            return EMPTY;
        }
    }

    public void setToolList(String[] toolList) {
        if (this.mService == null) {
            Log.w(TAG, "Failed to setToolList(); no TwToolBoxService.");
            return;
        }
        try {
            String tools = "0";
            for (String tool : toolList) {
                tools = tools + ";" + tool;
            }
            this.mService.setToolList(tools);
        } catch (RemoteException e) {
        } catch (Exception e2) {
        }
    }
}
