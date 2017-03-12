package android.app.wallpaperbackup;

import android.app.Activity;
import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Rect;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.provider.Settings.System;
import android.service.wallpaper.IWallpaperConnection.Stub;
import android.service.wallpaper.IWallpaperEngine;
import android.service.wallpaper.IWallpaperService;
import android.util.Log;
import android.view.View;

public class LivePreview {
    static final boolean DEBUG;
    static final String EXTRA_LIVE_WALLPAPER_INTENT = "android.live_wallpaper.intent";
    static final String EXTRA_LIVE_WALLPAPER_PACKAGE = "android.live_wallpaper.package";
    static final String EXTRA_LIVE_WALLPAPER_SETTINGS = "android.live_wallpaper.settings";
    private static final String LOG_TAG = "LiveWallpaperPreview";
    private static boolean bFlagLockwall = false;
    static int isSetGlasslock = -1;
    private static Context mContext;
    private static Intent mIntent;
    private static View mView;
    private static WallpaperConnection mWallpaperConnection;
    private static WallpaperManager mWallpaperManager;
    View mActivityView;

    static class WallpaperConnection extends Stub implements ServiceConnection {
        boolean mConnected;
        IWallpaperEngine mEngine;
        final Intent mIntent;
        IWallpaperService mService;

        WallpaperConnection(Intent intent) {
            this.mIntent = intent;
        }

        public boolean connect() {
            boolean z = true;
            synchronized (this) {
                if (LivePreview.mContext.bindService(this.mIntent, this, 1)) {
                    this.mConnected = true;
                } else {
                    z = false;
                }
            }
            return z;
        }

        public void disconnect() {
            synchronized (this) {
                this.mConnected = false;
                if (this.mEngine != null) {
                    try {
                        this.mEngine.destroy();
                    } catch (RemoteException e) {
                    }
                    this.mEngine = null;
                }
                try {
                    LivePreview.mContext.unbindService(this);
                } catch (IllegalArgumentException e2) {
                }
                this.mService = null;
            }
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            if (LivePreview.mWallpaperConnection == this) {
                this.mService = IWallpaperService.Stub.asInterface(service);
                try {
                    View view = LivePreview.mView;
                    View root = view.getRootView();
                    this.mService.attach(this, view.getWindowToken(), 1004, true, root.getWidth(), root.getHeight(), new Rect(root.getPaddingLeft(), root.getPaddingTop(), root.getPaddingRight(), root.getPaddingBottom()));
                } catch (RemoteException e) {
                    if (LivePreview.DEBUG) {
                        Log.e(LivePreview.LOG_TAG, "Failed attaching wallpaper; clearing", e);
                    }
                }
            }
        }

        public void onServiceDisconnected(ComponentName name) {
            this.mService = null;
            this.mEngine = null;
            if (LivePreview.mWallpaperConnection == this && LivePreview.DEBUG) {
                Log.e(LivePreview.LOG_TAG, "Wallpaper service gone: " + name);
            }
        }

        public void attachEngine(IWallpaperEngine engine) {
            synchronized (this) {
                if (this.mConnected) {
                    this.mEngine = engine;
                    try {
                        engine.setVisibility(true);
                    } catch (RemoteException e) {
                    }
                } else {
                    try {
                        engine.destroy();
                    } catch (RemoteException e2) {
                    }
                }
            }
        }

        public ParcelFileDescriptor setWallpaper(String name) {
            return null;
        }

        public void engineShown(IWallpaperEngine engine) {
        }
    }

    static {
        boolean z = true;
        if (SystemProperties.getInt("ro.debuggable", 0) != 1) {
            z = false;
        }
        DEBUG = z;
    }

    public LivePreview(Context context) {
        mContext = context;
    }

    void set(int code, Intent intent, WallpaperInfo info, boolean isLockScreen) {
        if (info == null) {
            Log.w(LOG_TAG, "Failure showing preview", new Throwable());
            return;
        }
        mIntent = intent;
        if (info != null) {
            String mSettingsActivityName = info.getSettingsActivity();
            info.getPackageName();
        }
        bFlagLockwall = isLockScreen;
        create();
    }

    public void create() {
        if (mIntent == null) {
            bFlagLockwall = false;
            ((Activity) mContext).finish();
        }
        try {
            isSetGlasslock = System.getInt(mContext.getContentResolver(), "lockscreen_wallpaper");
        } catch (Exception e) {
            if (DEBUG) {
                Log.e("zeroshuttle", "system.getInt Failed!");
            }
        }
        mWallpaperManager = WallpaperManager.getInstance(mContext);
        mWallpaperConnection = new WallpaperConnection(mIntent);
        setLiveWallpaper();
    }

    public void setLiveWallpaper() {
        if (mWallpaperConnection != null) {
            if (mWallpaperConnection.mEngine != null) {
                if (DEBUG) {
                    Log.i(LOG_TAG, "setLiveWallpaper: Destroy engine...");
                }
                try {
                    mWallpaperConnection.mEngine.destroy();
                } catch (RemoteException e) {
                    if (DEBUG) {
                        Log.e(LOG_TAG, "setLiveWallpaper: RemoteException in engine destroy");
                    }
                }
            }
            mWallpaperConnection.disconnect();
        }
        mWallpaperConnection = null;
        try {
            if (bFlagLockwall) {
                System.putInt(mContext.getContentResolver(), "lockscreen_wallpaper", 0);
            }
            mWallpaperManager.getIWallpaperManager().setWallpaperComponent(mIntent.getComponent());
            mWallpaperManager.setWallpaperOffsetSteps(0.5f, 0.0f);
        } catch (RemoteException e2) {
        } catch (RuntimeException e3) {
            if (DEBUG) {
                Log.e(LOG_TAG, "Failure setting wallpaper", e3);
            }
        }
        bFlagLockwall = false;
        isSetGlasslock = -1;
    }
}
