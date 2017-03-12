package android.app;

import android.app.IWallpaperManagerCallback.Stub;
import android.app.wallpaperbackup.Controller;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.os.ParcelFileDescriptor.AutoCloseOutputStream;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.provider.Settings.System;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.view.WindowManagerGlobal;
import com.samsung.android.telephony.MultiSimManager;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class WallpaperManager {
    public static final String ACTION_CHANGE_LIVE_WALLPAPER = "android.service.wallpaper.CHANGE_LIVE_WALLPAPER";
    public static final String ACTION_CROP_AND_SET_WALLPAPER = "android.service.wallpaper.CROP_AND_SET_WALLPAPER";
    public static final String ACTION_LIVE_WALLPAPER_CHOOSER = "android.service.wallpaper.LIVE_WALLPAPER_CHOOSER";
    public static final String COMMAND_DROP = "android.home.drop";
    public static final String COMMAND_SECONDARY_TAP = "android.wallpaper.secondaryTap";
    public static final String COMMAND_TAP = "android.wallpaper.tap";
    private static boolean DEBUG = true;
    public static final String EXTRA_LIVE_WALLPAPER_COMPONENT = "android.service.wallpaper.extra.LIVE_WALLPAPER_COMPONENT";
    private static final int PRELOAD_WALLPAPER = 1;
    private static final String PROP_WALLPAPER = "ro.config.wallpaper";
    private static final String PROP_WALLPAPER_COMPONENT = "ro.config.wallpaper_component";
    private static final String SETTINGS_SYSTEMUI_TRANSPARENCY = "android.wallpaper.settings_systemui_transparency";
    private static String TAG = "WallpaperManager";
    private static final int THEME_WALLPAPER = 2;
    public static final String WALLPAPER_PREVIEW_META_DATA = "android.wallpaper.preview";
    private static Globals sGlobals;
    private static final Object sSync = new Object[0];
    private final Context mContext;
    private float mWallpaperXStep = -1.0f;
    private float mWallpaperYStep = -1.0f;

    static class FastBitmapDrawable extends Drawable {
        private final Bitmap mBitmap;
        private int mDrawLeft;
        private int mDrawTop;
        private final int mHeight;
        private final Paint mPaint;
        private final int mWidth;

        private FastBitmapDrawable(Bitmap bitmap) {
            this.mBitmap = bitmap;
            this.mWidth = bitmap.getWidth();
            this.mHeight = bitmap.getHeight();
            setBounds(0, 0, this.mWidth, this.mHeight);
            this.mPaint = new Paint();
            this.mPaint.setXfermode(new PorterDuffXfermode(Mode.SRC));
        }

        public void draw(Canvas canvas) {
            canvas.drawBitmap(this.mBitmap, (float) this.mDrawLeft, (float) this.mDrawTop, this.mPaint);
        }

        public int getOpacity() {
            return -1;
        }

        public void setBounds(int left, int top, int right, int bottom) {
            this.mDrawLeft = (((right - left) - this.mWidth) / 2) + left;
            this.mDrawTop = (((bottom - top) - this.mHeight) / 2) + top;
        }

        public void setAlpha(int alpha) {
            throw new UnsupportedOperationException("Not supported with this drawable");
        }

        public void setColorFilter(ColorFilter colorFilter) {
            throw new UnsupportedOperationException("Not supported with this drawable");
        }

        public void setDither(boolean dither) {
            throw new UnsupportedOperationException("Not supported with this drawable");
        }

        public void setFilterBitmap(boolean filter) {
            throw new UnsupportedOperationException("Not supported with this drawable");
        }

        public int getIntrinsicWidth() {
            return this.mWidth;
        }

        public int getIntrinsicHeight() {
            return this.mHeight;
        }

        public int getMinimumWidth() {
            return this.mWidth;
        }

        public int getMinimumHeight() {
            return this.mHeight;
        }
    }

    static class Globals extends Stub {
        private static final int MSG_CLEAR_WALLPAPER = 1;
        private Bitmap mDefaultWallpaper;
        private IWallpaperManager mService = IWallpaperManager.Stub.asInterface(ServiceManager.getService(Context.WALLPAPER_SERVICE));
        private String mSimState_1;
        private String mSimState_2;
        private Bitmap mWallpaper;

        Globals(Looper looper) {
        }

        public void onWallpaperChanged() {
            synchronized (this) {
                this.mWallpaper = null;
                this.mDefaultWallpaper = null;
            }
        }

        public Bitmap peekWallpaperBitmap(Context context, boolean returnDefault) {
            Bitmap bitmap = null;
            synchronized (this) {
                if (this.mService != null) {
                    try {
                        if (!this.mService.isWallpaperSupported(context.getOpPackageName())) {
                        }
                    } catch (RemoteException e) {
                    }
                }
                Log.w(WallpaperManager.TAG, "peekWallpaperBitmap" + context.getPackageName());
                this.mSimState_1 = SystemProperties.get("gsm.sim.state");
                this.mSimState_2 = SystemProperties.get("gsm.sim.state2");
                Log.w(WallpaperManager.TAG, "peekWallpaperBitmap:SEC_PRODUCT_FEATURE_COMMON_USE_MULTISIM");
                if (this.mSimState_1.equals("ABSENT") || this.mSimState_2.equals("ABSENT")) {
                    if (this.mWallpaper != null) {
                        bitmap = this.mWallpaper;
                    } else if (this.mDefaultWallpaper != null) {
                        bitmap = this.mDefaultWallpaper;
                    }
                }
                this.mWallpaper = null;
                try {
                    this.mWallpaper = getCurrentWallpaperLocked(context);
                } catch (OutOfMemoryError e2) {
                    Log.w(WallpaperManager.TAG, "No memory load current wallpaper", e2);
                }
                if (returnDefault) {
                    if (this.mWallpaper == null) {
                        this.mDefaultWallpaper = getDefaultWallpaperLocked(context);
                        bitmap = this.mDefaultWallpaper;
                    } else {
                        this.mDefaultWallpaper = null;
                    }
                }
                bitmap = this.mWallpaper;
            }
            return bitmap;
        }

        public void forgetLoadedWallpaper() {
            synchronized (this) {
                this.mWallpaper = null;
                this.mDefaultWallpaper = null;
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public android.graphics.Bitmap getWallpaperLockedForMultiSim(android.content.Context r11, int r12) {
            /*
            r10 = this;
            r8 = 0;
            r5 = new android.os.Bundle;	 Catch:{ RemoteException -> 0x0048 }
            r5.<init>();	 Catch:{ RemoteException -> 0x0048 }
            r7 = r10.mService;	 Catch:{ RemoteException -> 0x0048 }
            r2 = r7.getWallpaperForMultiSim(r10, r5, r12);	 Catch:{ RemoteException -> 0x0048 }
            if (r2 == 0) goto L_0x0041;
        L_0x000e:
            r7 = "width";
            r9 = 0;
            r6 = r5.getInt(r7, r9);	 Catch:{ RemoteException -> 0x0048 }
            r7 = "height";
            r9 = 0;
            r3 = r5.getInt(r7, r9);	 Catch:{ RemoteException -> 0x0048 }
            r4 = new android.graphics.BitmapFactory$Options;	 Catch:{ OutOfMemoryError -> 0x0034 }
            r4.<init>();	 Catch:{ OutOfMemoryError -> 0x0034 }
            r7 = r2.getFileDescriptor();	 Catch:{ OutOfMemoryError -> 0x0034 }
            r9 = 0;
            r0 = android.graphics.BitmapFactory.decodeFileDescriptor(r7, r9, r4);	 Catch:{ OutOfMemoryError -> 0x0034 }
            r7 = android.app.WallpaperManager.generateBitmap(r11, r0, r6, r3);	 Catch:{ OutOfMemoryError -> 0x0034 }
            r2.close();	 Catch:{ IOException -> 0x004a }
        L_0x0033:
            return r7;
        L_0x0034:
            r1 = move-exception;
            r7 = android.app.WallpaperManager.TAG;	 Catch:{ all -> 0x0043 }
            r9 = "Can't decode file";
            android.util.Log.w(r7, r9, r1);	 Catch:{ all -> 0x0043 }
            r2.close();	 Catch:{ IOException -> 0x004c }
        L_0x0041:
            r7 = r8;
            goto L_0x0033;
        L_0x0043:
            r7 = move-exception;
            r2.close();	 Catch:{ IOException -> 0x004e }
        L_0x0047:
            throw r7;	 Catch:{ RemoteException -> 0x0048 }
        L_0x0048:
            r7 = move-exception;
            goto L_0x0041;
        L_0x004a:
            r8 = move-exception;
            goto L_0x0033;
        L_0x004c:
            r7 = move-exception;
            goto L_0x0041;
        L_0x004e:
            r9 = move-exception;
            goto L_0x0047;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.app.WallpaperManager.Globals.getWallpaperLockedForMultiSim(android.content.Context, int):android.graphics.Bitmap");
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private android.graphics.Bitmap getCurrentWallpaperLocked(android.content.Context r11) {
            /*
            r10 = this;
            r7 = 0;
            r8 = r10.mService;
            if (r8 != 0) goto L_0x000f;
        L_0x0005:
            r8 = android.app.WallpaperManager.TAG;
            r9 = "WallpaperService not running";
            android.util.Log.w(r8, r9);
        L_0x000e:
            return r7;
        L_0x000f:
            r5 = new android.os.Bundle;	 Catch:{ RemoteException -> 0x0058 }
            r5.<init>();	 Catch:{ RemoteException -> 0x0058 }
            r8 = r10.mService;	 Catch:{ RemoteException -> 0x0058 }
            r2 = r8.getWallpaper(r10, r5);	 Catch:{ RemoteException -> 0x0058 }
            if (r2 == 0) goto L_0x000e;
        L_0x001c:
            r8 = "width";
            r9 = 0;
            r6 = r5.getInt(r8, r9);	 Catch:{ RemoteException -> 0x0058 }
            r8 = "height";
            r9 = 0;
            r3 = r5.getInt(r8, r9);	 Catch:{ RemoteException -> 0x0058 }
            r4 = new android.graphics.BitmapFactory$Options;	 Catch:{ OutOfMemoryError -> 0x0043 }
            r4.<init>();	 Catch:{ OutOfMemoryError -> 0x0043 }
            r8 = r2.getFileDescriptor();	 Catch:{ OutOfMemoryError -> 0x0043 }
            r9 = 0;
            r0 = android.graphics.BitmapFactory.decodeFileDescriptor(r8, r9, r4);	 Catch:{ OutOfMemoryError -> 0x0043 }
            r8 = android.app.WallpaperManager.generateBitmap(r11, r0, r6, r3);	 Catch:{ OutOfMemoryError -> 0x0043 }
            r2.close();	 Catch:{ IOException -> 0x005a }
        L_0x0041:
            r7 = r8;
            goto L_0x000e;
        L_0x0043:
            r1 = move-exception;
            r8 = android.app.WallpaperManager.TAG;	 Catch:{ all -> 0x0053 }
            r9 = "Can't decode file";
            android.util.Log.w(r8, r9, r1);	 Catch:{ all -> 0x0053 }
            r2.close();	 Catch:{ IOException -> 0x0051 }
            goto L_0x000e;
        L_0x0051:
            r8 = move-exception;
            goto L_0x000e;
        L_0x0053:
            r8 = move-exception;
            r2.close();	 Catch:{ IOException -> 0x005c }
        L_0x0057:
            throw r8;	 Catch:{ RemoteException -> 0x0058 }
        L_0x0058:
            r8 = move-exception;
            goto L_0x000e;
        L_0x005a:
            r7 = move-exception;
            goto L_0x0041;
        L_0x005c:
            r9 = move-exception;
            goto L_0x0057;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.app.WallpaperManager.Globals.getCurrentWallpaperLocked(android.content.Context):android.graphics.Bitmap");
        }

        private String getCompressedVersion(String file) {
            if (file.endsWith(".jpg") || file.endsWith(".png")) {
                return file.substring(0, file.length() - 4) + ".pkm";
            }
            return file;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private android.graphics.Bitmap getDefaultWallpaperLocked(android.content.Context r37) {
            /*
            r36 = this;
            r32 = android.app.WallpaperManager.TAG;
            r33 = "getDefaultWallpaperLocked";
            android.util.Log.d(r32, r33);
            r18 = 0;
            r0 = r36;
            r0 = r0.mService;	 Catch:{ RemoteException -> 0x009a }
            r32 = r0;
            r25 = r32.getWidthHint();	 Catch:{ RemoteException -> 0x009a }
            r0 = r36;
            r0 = r0.mService;	 Catch:{ RemoteException -> 0x009a }
            r32 = r0;
            r15 = r32.getHeightHint();	 Catch:{ RemoteException -> 0x009a }
            r9 = "/carrier/data/app/WallpaperChooser/Customization_DefaultBackground.jpg";
            r8 = new java.io.File;	 Catch:{ RemoteException -> 0x009a }
            r8.<init>(r9);	 Catch:{ RemoteException -> 0x009a }
            r32 = r8.exists();	 Catch:{ RemoteException -> 0x009a }
            if (r32 == 0) goto L_0x003f;
        L_0x002c:
            r32 = r8.length();	 Catch:{ RemoteException -> 0x009a }
            r34 = 0;
            r32 = (r32 > r34 ? 1 : (r32 == r34 ? 0 : -1));
            if (r32 <= 0) goto L_0x003f;
        L_0x0036:
            r19 = new java.io.FileInputStream;	 Catch:{ IOException -> 0x008b }
            r0 = r19;
            r0.<init>(r8);	 Catch:{ IOException -> 0x008b }
            r18 = r19;
        L_0x003f:
            r32 = 2;
            r0 = r32;
            r0 = new java.lang.String[r0];	 Catch:{ RemoteException -> 0x009a }
            r22 = r0;
            r32 = 0;
            r33 = "/data/omc/res/wallpaper/drawable/default_wallpaper.jpg";
            r22[r32] = r33;	 Catch:{ RemoteException -> 0x009a }
            r32 = 1;
            r33 = "/data/omc/res/wallpaper/drawable/default_wallpaper.png";
            r22[r32] = r33;	 Catch:{ RemoteException -> 0x009a }
            r16 = 0;
            r19 = r18;
        L_0x0057:
            r0 = r22;
            r0 = r0.length;	 Catch:{ RemoteException -> 0x031b }
            r32 = r0;
            r0 = r16;
            r1 = r32;
            if (r0 >= r1) goto L_0x00af;
        L_0x0062:
            r21 = new java.io.File;	 Catch:{ RemoteException -> 0x031b }
            r32 = r22[r16];	 Catch:{ RemoteException -> 0x031b }
            r0 = r21;
            r1 = r32;
            r0.<init>(r1);	 Catch:{ RemoteException -> 0x031b }
            r32 = r21.exists();	 Catch:{ RemoteException -> 0x031b }
            if (r32 == 0) goto L_0x00ac;
        L_0x0073:
            r32 = r21.length();	 Catch:{ RemoteException -> 0x031b }
            r34 = 0;
            r32 = (r32 > r34 ? 1 : (r32 == r34 ? 0 : -1));
            if (r32 <= 0) goto L_0x00ac;
        L_0x007d:
            r18 = new java.io.FileInputStream;	 Catch:{ IOException -> 0x009e }
            r0 = r18;
            r1 = r21;
            r0.<init>(r1);	 Catch:{ IOException -> 0x009e }
        L_0x0086:
            r16 = r16 + 1;
            r19 = r18;
            goto L_0x0057;
        L_0x008b:
            r12 = move-exception;
            r32 = android.app.WallpaperManager.TAG;	 Catch:{ RemoteException -> 0x009a }
            r33 = "Chameleon Wallpaper FileInputStream error";
            r0 = r32;
            r1 = r33;
            android.util.Log.w(r0, r1, r12);	 Catch:{ RemoteException -> 0x009a }
            goto L_0x003f;
        L_0x009a:
            r32 = move-exception;
        L_0x009b:
            r32 = 0;
        L_0x009d:
            return r32;
        L_0x009e:
            r12 = move-exception;
            r32 = android.app.WallpaperManager.TAG;	 Catch:{ RemoteException -> 0x031b }
            r33 = "OMC Wallpaper FileInputStream error";
            r0 = r32;
            r1 = r33;
            android.util.Log.w(r0, r1, r12);	 Catch:{ RemoteException -> 0x031b }
        L_0x00ac:
            r18 = r19;
            goto L_0x0086;
        L_0x00af:
            if (r19 != 0) goto L_0x02dd;
        L_0x00b1:
            r10 = "/system/wallpaper/default_wallpaper/";
            r11 = "/system/csc_contents/";
            r29 = 0;
            r31 = new java.io.File;	 Catch:{ RemoteException -> 0x031b }
            r0 = r31;
            r0.<init>(r11);	 Catch:{ RemoteException -> 0x031b }
            r28 = r31.list();	 Catch:{ RemoteException -> 0x031b }
            r20 = 0;
            if (r28 == 0) goto L_0x00ff;
        L_0x00c6:
            r16 = 0;
        L_0x00c8:
            r0 = r28;
            r0 = r0.length;	 Catch:{ RemoteException -> 0x031b }
            r32 = r0;
            r0 = r16;
            r1 = r32;
            if (r0 >= r1) goto L_0x00df;
        L_0x00d3:
            r32 = r28[r16];	 Catch:{ RemoteException -> 0x031b }
            r33 = "default_wallpaper";
            r32 = r32.startsWith(r33);	 Catch:{ RemoteException -> 0x031b }
            if (r32 == 0) goto L_0x01ab;
        L_0x00dd:
            r20 = 1;
        L_0x00df:
            if (r20 == 0) goto L_0x00ff;
        L_0x00e1:
            r29 = new java.io.File;	 Catch:{ RemoteException -> 0x031b }
            r32 = new java.lang.StringBuilder;	 Catch:{ RemoteException -> 0x031b }
            r32.<init>();	 Catch:{ RemoteException -> 0x031b }
            r0 = r32;
            r32 = r0.append(r11);	 Catch:{ RemoteException -> 0x031b }
            r33 = r28[r16];	 Catch:{ RemoteException -> 0x031b }
            r32 = r32.append(r33);	 Catch:{ RemoteException -> 0x031b }
            r32 = r32.toString();	 Catch:{ RemoteException -> 0x031b }
            r0 = r29;
            r1 = r32;
            r0.<init>(r1);	 Catch:{ RemoteException -> 0x031b }
        L_0x00ff:
            r30 = 0;
            if (r20 == 0) goto L_0x01af;
        L_0x0103:
            r32 = r29.exists();	 Catch:{ RemoteException -> 0x031b }
            if (r32 == 0) goto L_0x01af;
        L_0x0109:
            r32 = android.app.WallpaperManager.TAG;	 Catch:{ RemoteException -> 0x031b }
            r33 = "getDefaultWallpaperLocked() symbolic link is used.";
            android.util.Log.i(r32, r33);	 Catch:{ RemoteException -> 0x031b }
            r30 = new java.io.File;	 Catch:{ RemoteException -> 0x031b }
            r0 = r30;
            r0.<init>(r11);	 Catch:{ RemoteException -> 0x031b }
        L_0x0119:
            r27 = r30.list();	 Catch:{ RemoteException -> 0x031b }
            if (r27 == 0) goto L_0x02dd;
        L_0x011f:
            r26 = 0;
            if (r20 == 0) goto L_0x01b8;
        L_0x0123:
            r32 = r29.exists();	 Catch:{ RemoteException -> 0x031b }
            if (r32 == 0) goto L_0x01b8;
        L_0x0129:
            r26 = r29;
            r32 = android.app.WallpaperManager.TAG;	 Catch:{ RemoteException -> 0x031b }
            r33 = new java.lang.StringBuilder;	 Catch:{ RemoteException -> 0x031b }
            r33.<init>();	 Catch:{ RemoteException -> 0x031b }
            r34 = "getDefaultWallpaperLocked(). symbolic link path is ";
            r33 = r33.append(r34);	 Catch:{ RemoteException -> 0x031b }
            r0 = r33;
            r1 = r26;
            r33 = r0.append(r1);	 Catch:{ RemoteException -> 0x031b }
            r33 = r33.toString();	 Catch:{ RemoteException -> 0x031b }
            android.util.Log.i(r32, r33);	 Catch:{ RemoteException -> 0x031b }
        L_0x0149:
            r32 = r26.exists();	 Catch:{ RemoteException -> 0x031b }
            if (r32 == 0) goto L_0x02dd;
        L_0x014f:
            r32 = r26.length();	 Catch:{ RemoteException -> 0x031b }
            r34 = 0;
            r32 = (r32 > r34 ? 1 : (r32 == r34 ? 0 : -1));
            if (r32 <= 0) goto L_0x02dd;
        L_0x0159:
            r32 = r26.getPath();	 Catch:{ IOException -> 0x02cf }
            r0 = r36;
            r1 = r32;
            r7 = r0.getCompressedVersion(r1);	 Catch:{ IOException -> 0x02cf }
            r6 = new java.io.File;	 Catch:{ IOException -> 0x02cf }
            r6.<init>(r7);	 Catch:{ IOException -> 0x02cf }
            r32 = r6.exists();	 Catch:{ IOException -> 0x02cf }
            if (r32 == 0) goto L_0x0172;
        L_0x0170:
            r26 = r6;
        L_0x0172:
            r18 = new java.io.FileInputStream;	 Catch:{ IOException -> 0x02cf }
            r0 = r18;
            r1 = r26;
            r0.<init>(r1);	 Catch:{ IOException -> 0x02cf }
        L_0x017b:
            if (r18 != 0) goto L_0x02e1;
        L_0x017d:
            r32 = r37.getResources();	 Catch:{ RemoteException -> 0x009a }
            r33 = 17302209; // 0x10802c1 float:2.498123E-38 double:8.548427E-317;
            r18 = r32.openRawResource(r33);	 Catch:{ RemoteException -> 0x009a }
        L_0x0188:
            if (r18 == 0) goto L_0x009b;
        L_0x018a:
            r23 = new android.graphics.BitmapFactory$Options;	 Catch:{ OutOfMemoryError -> 0x02fe }
            r23.<init>();	 Catch:{ OutOfMemoryError -> 0x02fe }
            r32 = 0;
            r0 = r18;
            r1 = r32;
            r2 = r23;
            r5 = android.graphics.BitmapFactory.decodeStream(r0, r1, r2);	 Catch:{ OutOfMemoryError -> 0x02fe }
            r0 = r37;
            r1 = r25;
            r32 = android.app.WallpaperManager.generateBitmap(r0, r5, r1, r15);	 Catch:{ OutOfMemoryError -> 0x02fe }
            r18.close();	 Catch:{ IOException -> 0x01a8 }
            goto L_0x009d;
        L_0x01a8:
            r33 = move-exception;
            goto L_0x009d;
        L_0x01ab:
            r16 = r16 + 1;
            goto L_0x00c8;
        L_0x01af:
            r30 = new java.io.File;	 Catch:{ RemoteException -> 0x031b }
            r0 = r30;
            r0.<init>(r10);	 Catch:{ RemoteException -> 0x031b }
            goto L_0x0119;
        L_0x01b8:
            r4 = android.os.UserHandle.getCallingUserId();	 Catch:{ RemoteException -> 0x031b }
            r17 = 0;
            r32 = new java.lang.StringBuilder;	 Catch:{ RemoteException -> 0x031b }
            r32.<init>();	 Catch:{ RemoteException -> 0x031b }
            r33 = "default_wallpaper_";
            r32 = r32.append(r33);	 Catch:{ RemoteException -> 0x031b }
            r0 = r32;
            r32 = r0.append(r4);	 Catch:{ RemoteException -> 0x031b }
            r33 = ".png";
            r32 = r32.append(r33);	 Catch:{ RemoteException -> 0x031b }
            r24 = r32.toString();	 Catch:{ RemoteException -> 0x031b }
            r32 = android.app.WallpaperManager.TAG;	 Catch:{ RemoteException -> 0x031b }
            r33 = new java.lang.StringBuilder;	 Catch:{ RemoteException -> 0x031b }
            r33.<init>();	 Catch:{ RemoteException -> 0x031b }
            r34 = "getDefaultWallpaperLocked() userDefault=";
            r33 = r33.append(r34);	 Catch:{ RemoteException -> 0x031b }
            r0 = r33;
            r1 = r24;
            r33 = r0.append(r1);	 Catch:{ RemoteException -> 0x031b }
            r33 = r33.toString();	 Catch:{ RemoteException -> 0x031b }
            android.util.Log.i(r32, r33);	 Catch:{ RemoteException -> 0x031b }
            if (r4 == 0) goto L_0x028b;
        L_0x01f9:
            r32 = 100;
            r0 = r32;
            if (r4 >= r0) goto L_0x028b;
        L_0x01ff:
            r32 = android.app.WallpaperManager.TAG;	 Catch:{ RemoteException -> 0x031b }
            r33 = new java.lang.StringBuilder;	 Catch:{ RemoteException -> 0x031b }
            r33.<init>();	 Catch:{ RemoteException -> 0x031b }
            r34 = "getDefaultWallpaperLocked() UserID=";
            r33 = r33.append(r34);	 Catch:{ RemoteException -> 0x031b }
            r0 = r33;
            r33 = r0.append(r4);	 Catch:{ RemoteException -> 0x031b }
            r33 = r33.toString();	 Catch:{ RemoteException -> 0x031b }
            android.util.Log.i(r32, r33);	 Catch:{ RemoteException -> 0x031b }
            r16 = 0;
        L_0x021d:
            r0 = r27;
            r0 = r0.length;	 Catch:{ RemoteException -> 0x031b }
            r32 = r0;
            r0 = r16;
            r1 = r32;
            if (r0 >= r1) goto L_0x0270;
        L_0x0228:
            r32 = android.app.WallpaperManager.TAG;	 Catch:{ RemoteException -> 0x031b }
            r33 = new java.lang.StringBuilder;	 Catch:{ RemoteException -> 0x031b }
            r33.<init>();	 Catch:{ RemoteException -> 0x031b }
            r34 = "getDefaultWallpaperLocked() wpFileList[i]=";
            r33 = r33.append(r34);	 Catch:{ RemoteException -> 0x031b }
            r34 = r27[r16];	 Catch:{ RemoteException -> 0x031b }
            r33 = r33.append(r34);	 Catch:{ RemoteException -> 0x031b }
            r33 = r33.toString();	 Catch:{ RemoteException -> 0x031b }
            android.util.Log.i(r32, r33);	 Catch:{ RemoteException -> 0x031b }
            r32 = r27[r16];	 Catch:{ RemoteException -> 0x031b }
            r0 = r32;
            r1 = r24;
            r32 = r0.equals(r1);	 Catch:{ RemoteException -> 0x031b }
            if (r32 == 0) goto L_0x02cb;
        L_0x0250:
            r32 = android.app.WallpaperManager.TAG;	 Catch:{ RemoteException -> 0x031b }
            r33 = new java.lang.StringBuilder;	 Catch:{ RemoteException -> 0x031b }
            r33.<init>();	 Catch:{ RemoteException -> 0x031b }
            r34 = "getDefaultWallpaperLocked() find file. i=";
            r33 = r33.append(r34);	 Catch:{ RemoteException -> 0x031b }
            r0 = r33;
            r1 = r16;
            r33 = r0.append(r1);	 Catch:{ RemoteException -> 0x031b }
            r33 = r33.toString();	 Catch:{ RemoteException -> 0x031b }
            android.util.Log.i(r32, r33);	 Catch:{ RemoteException -> 0x031b }
            r17 = r16;
        L_0x0270:
            if (r17 != 0) goto L_0x028b;
        L_0x0272:
            r14 = "/system/wallpaper/default_wallpaper/default_wallpaper_10.png";
            r13 = new java.io.File;	 Catch:{ RemoteException -> 0x031b }
            r13.<init>(r14);	 Catch:{ RemoteException -> 0x031b }
            r32 = r13.exists();	 Catch:{ RemoteException -> 0x031b }
            if (r32 == 0) goto L_0x028b;
        L_0x027f:
            r32 = r13.length();	 Catch:{ RemoteException -> 0x031b }
            r34 = 0;
            r32 = (r32 > r34 ? 1 : (r32 == r34 ? 0 : -1));
            if (r32 <= 0) goto L_0x028b;
        L_0x0289:
            r26 = r13;
        L_0x028b:
            r32 = android.app.WallpaperManager.TAG;	 Catch:{ RemoteException -> 0x031b }
            r33 = new java.lang.StringBuilder;	 Catch:{ RemoteException -> 0x031b }
            r33.<init>();	 Catch:{ RemoteException -> 0x031b }
            r34 = "getDefaultWallpaperLocked() index=";
            r33 = r33.append(r34);	 Catch:{ RemoteException -> 0x031b }
            r0 = r33;
            r1 = r17;
            r33 = r0.append(r1);	 Catch:{ RemoteException -> 0x031b }
            r33 = r33.toString();	 Catch:{ RemoteException -> 0x031b }
            android.util.Log.i(r32, r33);	 Catch:{ RemoteException -> 0x031b }
            if (r26 != 0) goto L_0x0149;
        L_0x02ab:
            r26 = new java.io.File;	 Catch:{ RemoteException -> 0x031b }
            r32 = new java.lang.StringBuilder;	 Catch:{ RemoteException -> 0x031b }
            r32.<init>();	 Catch:{ RemoteException -> 0x031b }
            r0 = r32;
            r32 = r0.append(r10);	 Catch:{ RemoteException -> 0x031b }
            r33 = r27[r17];	 Catch:{ RemoteException -> 0x031b }
            r32 = r32.append(r33);	 Catch:{ RemoteException -> 0x031b }
            r32 = r32.toString();	 Catch:{ RemoteException -> 0x031b }
            r0 = r26;
            r1 = r32;
            r0.<init>(r1);	 Catch:{ RemoteException -> 0x031b }
            goto L_0x0149;
        L_0x02cb:
            r16 = r16 + 1;
            goto L_0x021d;
        L_0x02cf:
            r12 = move-exception;
            r32 = android.app.WallpaperManager.TAG;	 Catch:{ RemoteException -> 0x031b }
            r33 = "CSC Wallpaper FileInputStream error";
            r0 = r32;
            r1 = r33;
            android.util.Log.w(r0, r1, r12);	 Catch:{ RemoteException -> 0x031b }
        L_0x02dd:
            r18 = r19;
            goto L_0x017b;
        L_0x02e1:
            r32 = r37.getContentResolver();	 Catch:{ SecurityException -> 0x02ee }
            r33 = "android.wallpaper.settings_systemui_transparency";
            r34 = 0;
            android.provider.Settings.System.putInt(r32, r33, r34);	 Catch:{ SecurityException -> 0x02ee }
            goto L_0x0188;
        L_0x02ee:
            r12 = move-exception;
            r32 = android.app.WallpaperManager.TAG;	 Catch:{ RemoteException -> 0x009a }
            r33 = "Can't put value of SETTINGS_SYSTEMUI_TRANSPARENCY";
            r0 = r32;
            r1 = r33;
            android.util.Log.e(r0, r1, r12);	 Catch:{ RemoteException -> 0x009a }
            goto L_0x0188;
        L_0x02fe:
            r12 = move-exception;
            r32 = android.app.WallpaperManager.TAG;	 Catch:{ all -> 0x0314 }
            r33 = "Can't decode stream";
            r0 = r32;
            r1 = r33;
            android.util.Log.w(r0, r1, r12);	 Catch:{ all -> 0x0314 }
            r18.close();	 Catch:{ IOException -> 0x0311 }
            goto L_0x009b;
        L_0x0311:
            r32 = move-exception;
            goto L_0x009b;
        L_0x0314:
            r32 = move-exception;
            r18.close();	 Catch:{ IOException -> 0x0319 }
        L_0x0318:
            throw r32;	 Catch:{ RemoteException -> 0x009a }
        L_0x0319:
            r33 = move-exception;
            goto L_0x0318;
        L_0x031b:
            r32 = move-exception;
            r18 = r19;
            goto L_0x009b;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.app.WallpaperManager.Globals.getDefaultWallpaperLocked(android.content.Context):android.graphics.Bitmap");
        }
    }

    static void initGlobals(Looper looper) {
        synchronized (sSync) {
            if (sGlobals == null) {
                sGlobals = new Globals(looper);
            }
        }
    }

    WallpaperManager(Context context, Handler handler) {
        this.mContext = context;
        initGlobals(context.getMainLooper());
    }

    public static WallpaperManager getInstance(Context context) {
        return (WallpaperManager) context.getSystemService(Context.WALLPAPER_SERVICE);
    }

    public IWallpaperManager getIWallpaperManager() {
        return sGlobals.mService;
    }

    private boolean checkForSamsungLaucher(String mPackageName) {
        String[] approvedLaunchers = new String[]{"com.sec.android.app.launcher", "com.sec.android.app.easylauncher"};
        Log.w("SamsungCompression", "WallpaperManager getting PackageName " + mPackageName);
        if (mPackageName == null) {
            return false;
        }
        for (Object equals : approvedLaunchers) {
            if (mPackageName.equals(equals)) {
                return true;
            }
        }
        return false;
    }

    public Drawable getDrawable() {
        Bitmap bm = sGlobals.peekWallpaperBitmap(this.mContext, true);
        if (bm == null) {
            return null;
        }
        if (bm.isGLCompressed() && !checkForSamsungLaucher(this.mContext.getPackageName())) {
            bm = bm.copy(Config.ARGB_8888, false);
        }
        Drawable dr = new BitmapDrawable(this.mContext.getResources(), bm);
        dr.setDither(false);
        return dr;
    }

    public Drawable getBuiltInDrawable() {
        return getBuiltInDrawable(0, 0, false, 0.0f, 0.0f);
    }

    public Drawable getBuiltInDrawable(int outWidth, int outHeight, boolean scaleToFit, float horizontalAlignment, float verticalAlignment) {
        if (sGlobals.mService == null) {
            Log.w(TAG, "WallpaperService not running");
            return null;
        }
        Resources resources = this.mContext.getResources();
        horizontalAlignment = Math.max(0.0f, Math.min(1.0f, horizontalAlignment));
        verticalAlignment = Math.max(0.0f, Math.min(1.0f, verticalAlignment));
        InputStream bufferedInputStream = new BufferedInputStream(resources.openRawResource(17302209));
        if (bufferedInputStream == null) {
            Log.e(TAG, "default wallpaper input stream is null");
            return null;
        } else if (outWidth <= 0 || outHeight <= 0) {
            return new BitmapDrawable(resources, BitmapFactory.decodeStream(bufferedInputStream, null, null));
        } else {
            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(bufferedInputStream, null, options);
            if (options.outWidth == 0 || options.outHeight == 0) {
                Log.e(TAG, "default wallpaper dimensions are 0");
                return null;
            }
            RectF cropRectF;
            int inWidth = options.outWidth;
            int inHeight = options.outHeight;
            outWidth = Math.min(inWidth, outWidth);
            outHeight = Math.min(inHeight, outHeight);
            if (scaleToFit) {
                cropRectF = getMaxCropRect(inWidth, inHeight, outWidth, outHeight, horizontalAlignment, verticalAlignment);
            } else {
                float left = ((float) (inWidth - outWidth)) * horizontalAlignment;
                float top = ((float) (inHeight - outHeight)) * verticalAlignment;
                cropRectF = new RectF(left, top, left + ((float) outWidth), top + ((float) outHeight));
            }
            Rect roundedTrueCrop = new Rect();
            cropRectF.roundOut(roundedTrueCrop);
            if (roundedTrueCrop.width() <= 0 || roundedTrueCrop.height() <= 0) {
                Log.w(TAG, "crop has bad values for full size image");
                return null;
            }
            bufferedInputStream = new BufferedInputStream(resources.openRawResource(17302209));
            int scaleDownSampleSize = Math.min(roundedTrueCrop.width() / outWidth, roundedTrueCrop.height() / outHeight);
            BitmapRegionDecoder decoder = null;
            try {
                decoder = BitmapRegionDecoder.newInstance(bufferedInputStream, true);
            } catch (IOException e) {
                Log.w(TAG, "cannot open region decoder for default wallpaper");
            }
            Bitmap crop = null;
            if (decoder != null) {
                options = new Options();
                if (scaleDownSampleSize > 1) {
                    options.inSampleSize = scaleDownSampleSize;
                }
                crop = decoder.decodeRegion(roundedTrueCrop, options);
                decoder.recycle();
            }
            if (crop == null) {
                bufferedInputStream = new BufferedInputStream(resources.openRawResource(17302209));
                Bitmap fullSize = null;
                if (bufferedInputStream != null) {
                    options = new Options();
                    if (scaleDownSampleSize > 1) {
                        options.inSampleSize = scaleDownSampleSize;
                    }
                    fullSize = BitmapFactory.decodeStream(bufferedInputStream, null, options);
                }
                if (fullSize != null) {
                    crop = Bitmap.createBitmap(fullSize, roundedTrueCrop.left, roundedTrueCrop.top, roundedTrueCrop.width(), roundedTrueCrop.height());
                }
            }
            if (crop == null) {
                Log.w(TAG, "cannot decode default wallpaper");
                return null;
            }
            if (outWidth > 0 && outHeight > 0 && !(crop.getWidth() == outWidth && crop.getHeight() == outHeight)) {
                Matrix m = new Matrix();
                RectF cropRect = new RectF(0.0f, 0.0f, (float) crop.getWidth(), (float) crop.getHeight());
                RectF rectF = new RectF(0.0f, 0.0f, (float) outWidth, (float) outHeight);
                m.setRectToRect(cropRect, rectF, ScaleToFit.FILL);
                Bitmap tmp = Bitmap.createBitmap((int) rectF.width(), (int) rectF.height(), Config.ARGB_8888);
                if (tmp != null) {
                    Canvas c = new Canvas(tmp);
                    Paint p = new Paint();
                    p.setFilterBitmap(true);
                    c.drawBitmap(crop, m, p);
                    crop = tmp;
                }
            }
            return new BitmapDrawable(resources, crop);
        }
    }

    private static RectF getMaxCropRect(int inWidth, int inHeight, int outWidth, int outHeight, float horizontalAlignment, float verticalAlignment) {
        RectF cropRect = new RectF();
        if (((float) inWidth) / ((float) inHeight) > ((float) outWidth) / ((float) outHeight)) {
            cropRect.top = 0.0f;
            cropRect.bottom = (float) inHeight;
            float cropWidth = ((float) outWidth) * (((float) inHeight) / ((float) outHeight));
            cropRect.left = (((float) inWidth) - cropWidth) * horizontalAlignment;
            cropRect.right = cropRect.left + cropWidth;
        } else {
            cropRect.left = 0.0f;
            cropRect.right = (float) inWidth;
            float cropHeight = ((float) outHeight) * (((float) inWidth) / ((float) outWidth));
            cropRect.top = (((float) inHeight) - cropHeight) * verticalAlignment;
            cropRect.bottom = cropRect.top + cropHeight;
        }
        return cropRect;
    }

    public Drawable peekDrawable() {
        Bitmap bm = sGlobals.peekWallpaperBitmap(this.mContext, false);
        if (bm == null) {
            return null;
        }
        Drawable dr = new BitmapDrawable(this.mContext.getResources(), bm);
        dr.setDither(false);
        return dr;
    }

    public Drawable getFastDrawable() {
        Bitmap bm = sGlobals.peekWallpaperBitmap(this.mContext, true);
        if (bm != null) {
            return new FastBitmapDrawable(bm);
        }
        return null;
    }

    public Drawable peekFastDrawable() {
        Bitmap bm = sGlobals.peekWallpaperBitmap(this.mContext, false);
        if (bm != null) {
            return new FastBitmapDrawable(bm);
        }
        return null;
    }

    public Bitmap getBitmap() {
        return sGlobals.peekWallpaperBitmap(this.mContext, true);
    }

    public Bitmap getBitmapForMultiSim(int simSlot) {
        Bitmap wallpaper = sGlobals.getWallpaperLockedForMultiSim(this.mContext, simSlot);
        if (wallpaper == null) {
            return sGlobals.peekWallpaperBitmap(this.mContext, true);
        }
        return wallpaper;
    }

    public void forgetLoadedWallpaper() {
        if (isWallpaperSupported()) {
            sGlobals.forgetLoadedWallpaper();
        }
    }

    public WallpaperInfo getWallpaperInfo() {
        WallpaperInfo wallpaperInfo = null;
        try {
            if (sGlobals.mService == null) {
                Log.w(TAG, "WallpaperService not running");
            } else {
                wallpaperInfo = sGlobals.mService.getWallpaperInfo();
            }
        } catch (RemoteException e) {
        }
        return wallpaperInfo;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.content.Intent getCropAndSetWallpaperIntent(android.net.Uri r9) {
        /*
        r8 = this;
        r7 = 0;
        if (r9 != 0) goto L_0x000b;
    L_0x0003:
        r5 = new java.lang.IllegalArgumentException;
        r6 = "Image URI must not be null";
        r5.<init>(r6);
        throw r5;
    L_0x000b:
        r5 = "content";
        r6 = r9.getScheme();
        r5 = r5.equals(r6);
        if (r5 != 0) goto L_0x001f;
    L_0x0017:
        r5 = new java.lang.IllegalArgumentException;
        r6 = "Image URI must be of the content scheme type";
        r5.<init>(r6);
        throw r5;
    L_0x001f:
        r5 = r8.mContext;
        r3 = r5.getPackageManager();
        r0 = new android.content.Intent;
        r5 = "android.service.wallpaper.CROP_AND_SET_WALLPAPER";
        r0.<init>(r5, r9);
        r5 = 1;
        r0.addFlags(r5);
        r5 = new android.content.Intent;
        r6 = "android.intent.action.MAIN";
        r5.<init>(r6);
        r6 = "android.intent.category.HOME";
        r2 = r5.addCategory(r6);
        r5 = 65536; // 0x10000 float:9.18355E-41 double:3.2379E-319;
        r4 = r3.resolveActivity(r2, r5);
        if (r4 == 0) goto L_0x0057;
    L_0x0045:
        r5 = r4.activityInfo;
        r5 = r5.packageName;
        r0.setPackage(r5);
        r1 = r3.queryIntentActivities(r0, r7);
        r5 = r1.size();
        if (r5 <= 0) goto L_0x0057;
    L_0x0056:
        return r0;
    L_0x0057:
        r5 = "com.android.wallpapercropper";
        r0.setPackage(r5);
        r1 = r3.queryIntentActivities(r0, r7);
        r5 = r1.size();
        if (r5 > 0) goto L_0x0056;
    L_0x0066:
        r5 = new java.lang.IllegalArgumentException;
        r6 = "Cannot use passed URI to set wallpaper; check that the type returned by ContentProvider matches image/*";
        r5.<init>(r6);
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.WallpaperManager.getCropAndSetWallpaperIntent(android.net.Uri):android.content.Intent");
    }

    public void setResource(int resid) throws IOException {
        Throwable th;
        if (sGlobals.mService == null) {
            Log.w(TAG, "WallpaperService not running");
            return;
        }
        try {
            Resources resources = this.mContext.getResources();
            Log.i(TAG, "setResource() get fd");
            ParcelFileDescriptor fd = sGlobals.mService.setWallpaper("res:" + resources.getResourceName(resid), this.mContext.getOpPackageName());
            Log.i(TAG, "setResource() fd: " + fd);
            if (fd != null) {
                FileOutputStream fos = null;
                Log.i(TAG, "setResource() get stream for resid");
                InputStream stream = resources.openRawResource(resid);
                try {
                    Log.i(TAG, "setResource() close output stream");
                    FileOutputStream fos2 = new AutoCloseOutputStream(fd);
                    try {
                        Log.i(TAG, "setResource() set wallpaper");
                        setWallpaper(stream, fos2);
                        if (fos2 != null) {
                            Log.i(TAG, "setResource() sync");
                        }
                        FileUtils.sync(fos2);
                        Log.i(TAG, "setResource() complete setResource");
                        if (fos2 != null) {
                            fos2.close();
                        }
                        if (stream != null) {
                            stream.close();
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        fos = fos2;
                        if (fos != null) {
                            fos.close();
                        }
                        if (stream != null) {
                            stream.close();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    if (fos != null) {
                        fos.close();
                    }
                    if (stream != null) {
                        stream.close();
                    }
                    throw th;
                }
            }
        } catch (RemoteException e) {
            Log.e(TAG, "setResource() cannot be completed setResource / getOpPackageName(): " + this.mContext.getOpPackageName());
        }
    }

    public void setBitmap(Bitmap bitmap) throws IOException {
        Throwable th;
        Log.d(TAG, "setBitmap");
        if (sGlobals.mService == null) {
            Log.w(TAG, "WallpaperService not running");
            return;
        }
        try {
            ParcelFileDescriptor fd = sGlobals.mService.setWallpaper(null, this.mContext.getOpPackageName());
            if (fd != null) {
                FileOutputStream fos = null;
                try {
                    OutputStream fos2 = new AutoCloseOutputStream(fd);
                    try {
                        bitmap.compress(CompressFormat.PNG, 90, fos2);
                        if (fos2 != null) {
                            FileUtils.sync(fos2);
                        }
                        if (fos2 != null) {
                            fos2.close();
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        OutputStream fos3 = fos2;
                        if (fos != null) {
                            fos.close();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    if (fos != null) {
                        fos.close();
                    }
                    throw th;
                }
            }
        } catch (RemoteException e) {
        }
    }

    public void setStream(InputStream data) throws IOException {
        Throwable th;
        Log.d(TAG, "setStream");
        if (sGlobals.mService == null) {
            Log.w(TAG, "WallpaperService not running");
            return;
        }
        try {
            ParcelFileDescriptor fd = sGlobals.mService.setWallpaper(null, this.mContext.getOpPackageName());
            if (fd != null) {
                FileOutputStream fos = null;
                try {
                    FileOutputStream fos2 = new AutoCloseOutputStream(fd);
                    try {
                        setWallpaper(data, fos2);
                        if (fos2 != null) {
                            FileUtils.sync(fos2);
                        }
                        if (fos2 != null) {
                            fos2.close();
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        fos = fos2;
                        if (fos != null) {
                            fos.close();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    if (fos != null) {
                        fos.close();
                    }
                    throw th;
                }
            }
        } catch (RemoteException e) {
        }
    }

    public void setResource(int resid, int simSlot) throws IOException {
        Throwable th;
        Log.d(TAG, "setResource:" + simSlot);
        if (sGlobals.mService == null) {
            Log.w(TAG, "WallpaperService not running");
            return;
        }
        try {
            Resources resources = this.mContext.getResources();
            ParcelFileDescriptor fd = sGlobals.mService.setWallpaperForMultiSim("res:" + resources.getResourceName(resid), simSlot);
            if (fd != null) {
                FileOutputStream fos = null;
                InputStream stream = resources.openRawResource(resid);
                try {
                    FileOutputStream fos2 = new AutoCloseOutputStream(fd);
                    try {
                        setWallpaper(stream, fos2);
                        if (fos2 != null) {
                            FileUtils.sync(fos2);
                        }
                        if (fos2 != null) {
                            fos2.close();
                        }
                        if (stream != null) {
                            stream.close();
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        fos = fos2;
                        if (fos != null) {
                            fos.close();
                        }
                        if (stream != null) {
                            stream.close();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    if (fos != null) {
                        fos.close();
                    }
                    if (stream != null) {
                        stream.close();
                    }
                    throw th;
                }
            }
        } catch (RemoteException e) {
        }
    }

    public void setBitmap(Bitmap bitmap, int simSlot) throws IOException {
        Throwable th;
        Log.d(TAG, "setBitmap:" + simSlot);
        if (sGlobals.mService == null) {
            Log.w(TAG, "WallpaperService not running");
            return;
        }
        try {
            ParcelFileDescriptor fd = sGlobals.mService.setWallpaperForMultiSim(null, simSlot);
            if (fd != null) {
                FileOutputStream fos = null;
                try {
                    OutputStream fos2 = new AutoCloseOutputStream(fd);
                    try {
                        bitmap.compress(CompressFormat.PNG, 90, fos2);
                        if (fos2 != null) {
                            FileUtils.sync(fos2);
                        }
                        if (fos2 != null) {
                            fos2.close();
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        OutputStream fos3 = fos2;
                        if (fos != null) {
                            fos.close();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    if (fos != null) {
                        fos.close();
                    }
                    throw th;
                }
            }
        } catch (RemoteException e) {
        }
    }

    public void setStream(InputStream data, int simSlot) throws IOException {
        Throwable th;
        Log.d(TAG, "setStream:" + simSlot);
        if (sGlobals.mService == null) {
            Log.w(TAG, "WallpaperService not running");
            return;
        }
        try {
            ParcelFileDescriptor fd = sGlobals.mService.setWallpaperForMultiSim(null, simSlot);
            if (fd != null) {
                FileOutputStream fos = null;
                try {
                    FileOutputStream fos2 = new AutoCloseOutputStream(fd);
                    try {
                        setWallpaper(data, fos2);
                        if (fos2 != null) {
                            FileUtils.sync(fos2);
                        }
                        if (fos2 != null) {
                            fos2.close();
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        fos = fos2;
                        if (fos != null) {
                            fos.close();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    if (fos != null) {
                        fos.close();
                    }
                    throw th;
                }
            }
        } catch (RemoteException e) {
        }
    }

    private void setWallpaper(InputStream data, FileOutputStream fos) throws IOException {
        Log.d(TAG, "setWallpaper");
        byte[] buffer = new byte[32768];
        while (true) {
            int amt = data.read(buffer);
            if (amt > 0) {
                fos.write(buffer, 0, amt);
            } else {
                return;
            }
        }
    }

    public boolean hasResourceWallpaper(int resid) {
        boolean z = false;
        if (sGlobals.mService == null) {
            Log.w(TAG, "WallpaperService not running");
        } else {
            try {
                z = sGlobals.mService.hasNamedWallpaper("res:" + this.mContext.getResources().getResourceName(resid));
            } catch (RemoteException e) {
            }
        }
        return z;
    }

    public int getDesiredMinimumWidth() {
        int i = 0;
        if (sGlobals.mService == null) {
            Log.w(TAG, "WallpaperService not running");
        } else {
            try {
                i = sGlobals.mService.getWidthHint();
            } catch (RemoteException e) {
            }
        }
        return i;
    }

    public int getDesiredMinimumHeight() {
        int i = 0;
        if (sGlobals.mService == null) {
            Log.w(TAG, "WallpaperService not running");
        } else {
            try {
                i = sGlobals.mService.getHeightHint();
            } catch (RemoteException e) {
            }
        }
        return i;
    }

    public void suggestDesiredDimensions(int minimumWidth, int minimumHeight) {
        int maximumTextureSize;
        try {
            maximumTextureSize = SystemProperties.getInt("sys.max_texture_size", 0);
        } catch (Exception e) {
            maximumTextureSize = 0;
        }
        if (maximumTextureSize > 0 && (minimumWidth > maximumTextureSize || minimumHeight > maximumTextureSize)) {
            float aspect = ((float) minimumHeight) / ((float) minimumWidth);
            if (minimumWidth > minimumHeight) {
                minimumWidth = maximumTextureSize;
                minimumHeight = (int) (((double) (((float) minimumWidth) * aspect)) + 0.5d);
            } else {
                minimumHeight = maximumTextureSize;
                minimumWidth = (int) (((double) (((float) minimumHeight) / aspect)) + 0.5d);
            }
        }
        try {
            if (sGlobals.mService == null) {
                Log.w(TAG, "WallpaperService not running");
            } else {
                sGlobals.mService.setDimensionHints(minimumWidth, minimumHeight, this.mContext.getOpPackageName());
            }
        } catch (RemoteException e2) {
        }
    }

    public void setDisplayPadding(Rect padding) {
        try {
            if (sGlobals.mService == null) {
                Log.w(TAG, "WallpaperService not running");
            } else {
                sGlobals.mService.setDisplayPadding(padding, this.mContext.getOpPackageName());
            }
        } catch (RemoteException e) {
        }
    }

    public void setDisplayOffset(IBinder windowToken, int x, int y) {
        try {
            WindowManagerGlobal.getWindowSession().setWallpaperDisplayOffset(windowToken, x, y);
        } catch (RemoteException e) {
        }
    }

    public void clearWallpaper() {
        if (sGlobals.mService == null) {
            Log.w(TAG, "WallpaperService not running");
            return;
        }
        try {
            sGlobals.mService.clearWallpaper(this.mContext.getOpPackageName());
        } catch (RemoteException e) {
        }
    }

    public boolean setWallpaperComponent(ComponentName name) {
        if (sGlobals.mService == null) {
            Log.w(TAG, "WallpaperService not running");
            return false;
        }
        try {
            sGlobals.mService.setWallpaperComponentChecked(name, this.mContext.getOpPackageName());
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    public void setWallpaperOffsets(IBinder windowToken, float xOffset, float yOffset) {
        try {
            WindowManagerGlobal.getWindowSession().setWallpaperPosition(windowToken, xOffset, yOffset, this.mWallpaperXStep, this.mWallpaperYStep);
        } catch (RemoteException e) {
        }
    }

    public void setWallpaperOffsetSteps(float xStep, float yStep) {
        this.mWallpaperXStep = xStep;
        this.mWallpaperYStep = yStep;
    }

    public void sendWallpaperCommand(IBinder windowToken, String action, int x, int y, int z, Bundle extras) {
        try {
            WindowManagerGlobal.getWindowSession().sendWallpaperCommand(windowToken, action, x, y, z, extras, false);
        } catch (RemoteException e) {
        }
    }

    public boolean isWallpaperSupported() {
        if (sGlobals.mService == null) {
            Log.w(TAG, "WallpaperService not running");
        } else {
            try {
                return sGlobals.mService.isWallpaperSupported(this.mContext.getOpPackageName());
            } catch (RemoteException e) {
            }
        }
        return false;
    }

    public void clearWallpaperOffsets(IBinder windowToken) {
        try {
            WindowManagerGlobal.getWindowSession().setWallpaperPosition(windowToken, -1.0f, -1.0f, -1.0f, -1.0f);
        } catch (RemoteException e) {
        }
    }

    public void clear() throws IOException {
        Log.d(TAG, "clear wallpaper");
        String[] defaultWpFilePath = new String[]{"/system/wallpaper/default_wallpaper/default_wallpaper.png", "/system/wallpaper/default_wallpaper/default_wallpaper.jpg", "/system/csc_contents/default_wallpaper.jpg", "/system/csc_contents/default_wallpaper.png", "/data/omc/res/wallpaper/drawable/default_wallpaper.jpg", "/data/omc/res/wallpaper/drawable/default_wallpaper.png"};
        boolean setCscWallpaper = false;
        int i = 0;
        while (i < defaultWpFilePath.length) {
            File wpFile = new File(defaultWpFilePath[i]);
            if (wpFile.exists()) {
                FileInputStream is = new FileInputStream(wpFile);
                try {
                    setStream(is);
                    setCscWallpaper = true;
                } catch (IOException e) {
                    Log.e(TAG, "cannot get csc wallpaper(setStream):" + defaultWpFilePath[i]);
                    setCscWallpaper = false;
                }
                try {
                    is.close();
                    break;
                } catch (IOException e2) {
                    Log.e(TAG, "cannot get csc wallpaper(close inputstream):" + defaultWpFilePath[i]);
                }
            } else {
                i++;
            }
        }
        if (!setCscWallpaper) {
            setResource(17302209);
        }
        try {
            System.putInt(this.mContext.getContentResolver(), SETTINGS_SYSTEMUI_TRANSPARENCY, 1);
        } catch (SecurityException e3) {
            Log.e(TAG, "Can't put value of SETTINGS_SYSTEMUI_TRANSPARENCY", e3);
        }
    }

    public void clear(int simSlot) throws IOException {
        Log.d(TAG, "clear wallpaper");
        String[] defaultWpFilePath = new String[]{"/system/wallpaper/default_wallpaper/default_wallpaper.png", "/system/wallpaper/default_wallpaper/default_wallpaper.jpg", "/system/csc_contents/default_wallpaper.jpg", "/system/csc_contents/default_wallpaper.png", "/data/omc/res/wallpaper/drawable/default_wallpaper.jpg", "/data/omc/res/wallpaper/drawable/default_wallpaper.png"};
        boolean setCscWallpaper = false;
        int i = 0;
        while (i < defaultWpFilePath.length) {
            File wpFile = new File(defaultWpFilePath[i]);
            if (wpFile.exists()) {
                FileInputStream is = new FileInputStream(wpFile);
                try {
                    setStream(is, simSlot);
                    setCscWallpaper = true;
                } catch (IOException e) {
                    Log.e(TAG, "cannot get csc wallpaper(setStream):" + defaultWpFilePath[i]);
                    setCscWallpaper = false;
                }
                try {
                    is.close();
                    break;
                } catch (IOException e2) {
                    Log.e(TAG, "cannot get csc wallpaper(close inputstream):" + defaultWpFilePath[i]);
                }
            } else {
                i++;
            }
        }
        if (!setCscWallpaper) {
            setResource(17302209, simSlot);
        }
        try {
            System.putInt(this.mContext.getContentResolver(), SETTINGS_SYSTEMUI_TRANSPARENCY, 1);
        } catch (SecurityException e3) {
            Log.e(TAG, "Can't put value of SETTINGS_SYSTEMUI_TRANSPARENCY", e3);
        }
    }

    static Bitmap generateBitmap(Context context, Bitmap bm, int width, int height) {
        if (bm == null) {
            Log.d(TAG, "generateBitmap is null");
            return null;
        }
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        bm.setDensity(metrics.noncompatDensityDpi);
        if (DEBUG) {
            Log.d(TAG, "generateBitmap :: width:" + width + ", height:" + height + " :: bitmap:: width:" + bm.getWidth() + ", height:" + bm.getHeight());
        }
        if (width <= 0 || height <= 0) {
            return bm;
        }
        if (bm.getWidth() == width && bm.getHeight() == height) {
            return bm;
        }
        if (bm.getWidth() >= 4096 || bm.getHeight() >= 4096) {
            if (bm.getConfig() == null) {
                Log.d(TAG, "Assuming Texture compressed bitmap.");
                return bm;
            }
            Bitmap newbm = null;
            try {
                newbm = Bitmap.createBitmap(width, height, Config.ARGB_8888);
                newbm.setDensity(metrics.noncompatDensityDpi);
                Canvas c = new Canvas(newbm);
                Rect targetRect = new Rect();
                targetRect.right = bm.getWidth();
                targetRect.bottom = bm.getHeight();
                int deltaw = width - targetRect.right;
                int deltah = height - targetRect.bottom;
                if (deltaw > 0 || deltah > 0) {
                    float scale;
                    if (deltaw > deltah) {
                        scale = ((float) width) / ((float) targetRect.right);
                    } else {
                        scale = ((float) height) / ((float) targetRect.bottom);
                    }
                    targetRect.right = (int) (((float) targetRect.right) * scale);
                    targetRect.bottom = (int) (((float) targetRect.bottom) * scale);
                } else if (targetRect.bottom > targetRect.right) {
                    targetRect.right = width;
                    targetRect.bottom = (targetRect.bottom * width) / targetRect.right;
                } else {
                    targetRect.right = (targetRect.right * height) / targetRect.bottom;
                    targetRect.bottom = height;
                }
                targetRect.offset((width - targetRect.right) / 2, (height - targetRect.bottom) / 2);
                Paint paint = new Paint();
                paint.setFilterBitmap(true);
                paint.setXfermode(new PorterDuffXfermode(Mode.SRC));
                c.drawBitmap(bm, null, targetRect, paint);
                bm.recycle();
                c.setBitmap(null);
                return newbm;
            } catch (OutOfMemoryError e) {
                Log.w(TAG, "Can't generate default bitmap", e);
                if (newbm == null) {
                    return bm;
                }
                newbm.recycle();
                return bm;
            }
        } else if (!DEBUG) {
            return bm;
        } else {
            Log.d(TAG, "generateBitmap :: returning early ");
            return bm;
        }
    }

    public static InputStream openDefaultWallpaper(Context context) {
        String path = SystemProperties.get(PROP_WALLPAPER);
        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            if (file.exists()) {
                try {
                    return new FileInputStream(file);
                } catch (IOException e) {
                }
            }
        }
        String[] defaultWpFilePath = new String[]{"/system/wallpaper/default_wallpaper/default_wallpaper.png", "/system/wallpaper/default_wallpaper/default_wallpaper.jpg", "/system/csc_contents/default_wallpaper.jpg", "/system/csc_contents/default_wallpaper.png"};
        int i = 0;
        while (i < defaultWpFilePath.length) {
            File wpFile = new File(defaultWpFilePath[i]);
            if (wpFile.exists()) {
                try {
                    return new FileInputStream(wpFile);
                } catch (IOException e2) {
                }
            } else {
                i++;
            }
        }
        return context.getResources().openRawResource(17302209);
    }

    public static ComponentName getDefaultWallpaperComponent(Context context) {
        ComponentName cn;
        String flat = SystemProperties.get(PROP_WALLPAPER_COMPONENT);
        if (!TextUtils.isEmpty(flat)) {
            cn = ComponentName.unflattenFromString(flat);
            if (cn != null) {
                return cn;
            }
        }
        flat = context.getString(17039395);
        if (!TextUtils.isEmpty(flat)) {
            cn = ComponentName.unflattenFromString(flat);
            if (cn != null) {
                return cn;
            }
        }
        return null;
    }

    public static void startBackup(Context context, String pathValue, String source) {
        new Controller().startBackup(context, pathValue, source);
    }

    public static void startRestore(Context context, String pathValue, String source) {
        new Controller().startRestore(context, pathValue, source);
    }

    public void clearAll() throws IOException {
        Log.d(TAG, "clearAll");
        if (isMultiSIMSupported()) {
            clear(0);
            clear(1);
            return;
        }
        clear();
    }

    public void setResourceAll(int resid) throws IOException {
        Log.d(TAG, "setResourceAll");
        if (isMultiSIMSupported()) {
            setResource(resid, 0);
            setResource(resid, 1);
        } else {
            setResource(resid);
        }
        try {
            System.putInt(this.mContext.getContentResolver(), SETTINGS_SYSTEMUI_TRANSPARENCY, 2);
        } catch (SecurityException e) {
            Log.e(TAG, "Can't put value of SETTINGS_SYSTEMUI_TRANSPARENCY", e);
        }
    }

    public boolean isMultiSIMSupported() {
        String mProductName = SystemProperties.get("ro.product.name");
        boolean multiSIM_skip = false;
        if (mProductName.startsWith("trlteduos") || mProductName.startsWith("klteduos")) {
            multiSIM_skip = true;
        }
        if (MultiSimManager.getSimSlotCount() <= 1 || multiSIM_skip) {
            return false;
        }
        return true;
    }
}
