package android.app;

import android.R;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.ContainerEncryptionParams;
import android.content.pm.FeatureInfo;
import android.content.pm.IOnPermissionsChangeListener;
import android.content.pm.IOverlayCallback;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.IPackageInstallObserver2;
import android.content.pm.IPackageManager;
import android.content.pm.IPackageMoveObserver.Stub;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.InstrumentationInfo;
import android.content.pm.IntentFilterVerificationInfo;
import android.content.pm.KeySet;
import android.content.pm.ManifestDigest;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.LegacyPackageInstallObserver;
import android.content.pm.PackageManager.MoveCallback;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageManager.OnPermissionsChangedListener;
import android.content.pm.ParceledListSlice;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.UserInfo;
import android.content.pm.VerificationParams;
import android.content.pm.VerifierDeviceIdentity;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.net.ProxyInfo;
import android.net.Uri;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.os.PersonaManager;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.storage.StorageManager;
import android.os.storage.VolumeInfo;
import android.provider.CalendarContract;
import android.provider.Settings.Global;
import android.provider.Settings.System;
import android.sec.enterprise.ApplicationPolicy;
import android.sec.enterprise.EnterpriseDeviceManager;
import android.telephony.TelephonyManager;
import android.util.ArrayMap;
import android.util.Log;
import android.util.TypedValue;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.os.SomeArgs;
import com.android.internal.util.Preconditions;
import com.android.internal.util.UserIcons;
import com.samsung.android.telephony.MultiSimManager;
import com.sec.android.app.CscFeature;
import dalvik.system.VMRuntime;
import java.io.ByteArrayInputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

final class ApplicationPackageManager extends PackageManager {
    private static final boolean DEBUG_ICONS = false;
    private static final float DEFAULT_THEME_APPICON_SCALE = 0.72f;
    private static final float OPEN_THEME_APPICON_SCALE = 0.7f;
    private static final String SCafeVersion = SystemProperties.get("ro.build.scafe.version");
    private static final String TAG = "ApplicationPackageManager";
    private static final char[] arabicNumberArray = new char[]{'?', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩'};
    private static final String cscAppResourcePath = "/system/csc/appresource/CSCAppResource.apk";
    private static final char[] farsiNumberArray = new char[]{'?', '١', '٢', '٣', '۴', '۵', '۶', '٧', '٨', '٩'};
    private static final int[][] judgePoint = new int[][]{new int[]{31, 17}, new int[]{96, 5}, new int[]{160, 17}, new int[]{186, 96}, new int[]{160, 174}, new int[]{96, 186}, new int[]{31, 174}, new int[]{5, 96}};
    private static ArrayMap<String, Method> mLiveIconLoaders = new ArrayMap();
    private static ArrayMap<String, String> mLiveIconPackageMatchers = new ArrayMap();
    private static final int[][] movePoint = new int[][]{new int[]{1, 1}, new int[]{-1, 1}, new int[]{-1, -1}, new int[]{1, -1}};
    private static final int[][] progress = new int[][]{new int[]{1, 0}, new int[]{0, 1}, new int[]{-1, 0}, new int[]{0, -1}};
    private static final int sDefaultFlags = 1024;
    private static ArrayMap<ResourceName, WeakReference<ConstantState>> sIconCache = new ArrayMap();
    private static ArrayMap<ResourceName, WeakReference<CharSequence>> sStringCache = new ArrayMap();
    private static final Object sSync = new Object();
    private String appIconPackageName = null;
    private Bitmap mBgCalendar = null;
    private Bitmap mBgClock = null;
    int mCachedSafeMode = -1;
    private int mColor = -1;
    private final ContextImpl mContext;
    @GuardedBy("mDelegates")
    private final ArrayList<MoveCallbackDelegate> mDelegates = new ArrayList();
    private Typeface mFontDate = null;
    private Typeface mFontDay = null;
    private int mHeightDate = -1;
    private int mHeightDay = -1;
    private int mHeightMargin1 = -1;
    private int mHeightMargin2 = -1;
    private Bitmap mHour = null;
    @GuardedBy("mLock")
    private PackageInstaller mInstaller;
    private final Object mLock = new Object();
    private Bitmap mMin = null;
    private final IPackageManager mPM;
    private Paint mPaint;
    private final Map<OnPermissionsChangedListener, IOnPermissionsChangeListener> mPermissionListeners = new ArrayMap();
    @GuardedBy("mLock")
    private String mPermissionsControllerPackageName;
    private Resources mResources;
    private Bitmap mSec = null;
    private boolean mSettingStatusChecked = false;
    private boolean mSettingStatusForIconTray = false;
    private int mSizeDate = -1;
    private int mSizeDay = -1;
    @GuardedBy("mLock")
    private UserManager mUserManager;
    private int openThemeAppIconRange = 3;
    private float openThemeAppIconScale = OPEN_THEME_APPICON_SCALE;

    public class LiveIconObject implements Cloneable {
        private Object liveIcon;

        public Object getLiveIcon() {
            return this.liveIcon;
        }

        public void setLiveIcon(Object object) {
            this.liveIcon = object;
        }

        public Object clone() throws CloneNotSupportedException {
            return (LiveIconObject) super.clone();
        }
    }

    private static class MoveCallbackDelegate extends Stub implements Callback {
        private static final int MSG_CREATED = 1;
        private static final int MSG_STATUS_CHANGED = 2;
        final MoveCallback mCallback;
        final Handler mHandler;

        public MoveCallbackDelegate(MoveCallback callback, Looper looper) {
            this.mCallback = callback;
            this.mHandler = new Handler(looper, (Callback) this);
        }

        public boolean handleMessage(Message msg) {
            SomeArgs args;
            switch (msg.what) {
                case 1:
                    args = msg.obj;
                    this.mCallback.onCreated(args.argi1, (Bundle) args.arg2);
                    args.recycle();
                    return true;
                case 2:
                    args = (SomeArgs) msg.obj;
                    this.mCallback.onStatusChanged(args.argi1, args.argi2, ((Long) args.arg3).longValue());
                    args.recycle();
                    return true;
                default:
                    return false;
            }
        }

        public void onCreated(int moveId, Bundle extras) {
            SomeArgs args = SomeArgs.obtain();
            args.argi1 = moveId;
            args.arg2 = extras;
            this.mHandler.obtainMessage(1, args).sendToTarget();
        }

        public void onStatusChanged(int moveId, int status, long estMillis) {
            SomeArgs args = SomeArgs.obtain();
            args.argi1 = moveId;
            args.argi2 = status;
            args.arg3 = Long.valueOf(estMillis);
            this.mHandler.obtainMessage(2, args).sendToTarget();
        }
    }

    public class OnPermissionsChangeListenerDelegate extends IOnPermissionsChangeListener.Stub implements Callback {
        private static final int MSG_PERMISSIONS_CHANGED = 1;
        private final Handler mHandler;
        private final OnPermissionsChangedListener mListener;

        public OnPermissionsChangeListenerDelegate(OnPermissionsChangedListener listener, Looper looper) {
            this.mListener = listener;
            this.mHandler = new Handler(looper, (Callback) this);
        }

        public void onPermissionsChanged(int uid) {
            this.mHandler.obtainMessage(1, uid, 0).sendToTarget();
        }

        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    this.mListener.onPermissionsChanged(msg.arg1);
                    return true;
                default:
                    return false;
            }
        }
    }

    private static final class ResourceName {
        final int iconId;
        final String packageName;

        ResourceName(String _packageName, int _iconId) {
            this.packageName = _packageName;
            this.iconId = _iconId;
        }

        ResourceName(ApplicationInfo aInfo, int _iconId) {
            this(aInfo.packageName, _iconId);
        }

        ResourceName(ComponentInfo cInfo, int _iconId) {
            this(cInfo.applicationInfo.packageName, _iconId);
        }

        ResourceName(ResolveInfo rInfo, int _iconId) {
            this(rInfo.activityInfo.applicationInfo.packageName, _iconId);
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ResourceName that = (ResourceName) o;
            if (this.iconId != that.iconId) {
                return false;
            }
            if (this.packageName != null) {
                if (this.packageName.equals(that.packageName)) {
                    return true;
                }
            } else if (that.packageName == null) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return (this.packageName.hashCode() * 31) + this.iconId;
        }

        public String toString() {
            return "{ResourceName " + this.packageName + " / " + this.iconId + "}";
        }
    }

    private android.graphics.drawable.Drawable getLiveIcon(java.lang.String r24) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.fixSplitterBlock(BlockFinish.java:63)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:34)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r23 = this;
        r16 = 0;
        r12 = 0;
        r0 = r23;	 Catch:{ NameNotFoundException -> 0x0103 }
        r0 = r0.mContext;	 Catch:{ NameNotFoundException -> 0x0103 }
        r17 = r0;	 Catch:{ NameNotFoundException -> 0x0103 }
        r17 = r17.getPackageManager();	 Catch:{ NameNotFoundException -> 0x0103 }
        r18 = 128; // 0x80 float:1.794E-43 double:6.32E-322;	 Catch:{ NameNotFoundException -> 0x0103 }
        r0 = r17;	 Catch:{ NameNotFoundException -> 0x0103 }
        r1 = r24;	 Catch:{ NameNotFoundException -> 0x0103 }
        r2 = r18;	 Catch:{ NameNotFoundException -> 0x0103 }
        r3 = r0.getApplicationInfo(r1, r2);	 Catch:{ NameNotFoundException -> 0x0103 }
        r0 = r3.sourceDir;	 Catch:{ NameNotFoundException -> 0x0103 }
        r16 = r0;	 Catch:{ NameNotFoundException -> 0x0103 }
        r14 = r3.metaData;	 Catch:{ NameNotFoundException -> 0x0103 }
        if (r14 != 0) goto L_0x0045;	 Catch:{ NameNotFoundException -> 0x0103 }
    L_0x0021:
        r17 = "ApplicationPackageManager";	 Catch:{ NameNotFoundException -> 0x0103 }
        r18 = new java.lang.StringBuilder;	 Catch:{ NameNotFoundException -> 0x0103 }
        r18.<init>();	 Catch:{ NameNotFoundException -> 0x0103 }
        r19 = "Doesn't have metadata for LiveIcon : [";	 Catch:{ NameNotFoundException -> 0x0103 }
        r18 = r18.append(r19);	 Catch:{ NameNotFoundException -> 0x0103 }
        r0 = r18;	 Catch:{ NameNotFoundException -> 0x0103 }
        r1 = r24;	 Catch:{ NameNotFoundException -> 0x0103 }
        r18 = r0.append(r1);	 Catch:{ NameNotFoundException -> 0x0103 }
        r19 = "]  just show default Icon.";	 Catch:{ NameNotFoundException -> 0x0103 }
        r18 = r18.append(r19);	 Catch:{ NameNotFoundException -> 0x0103 }
        r18 = r18.toString();	 Catch:{ NameNotFoundException -> 0x0103 }
        android.util.Log.d(r17, r18);	 Catch:{ NameNotFoundException -> 0x0103 }
        r15 = 0;	 Catch:{ NameNotFoundException -> 0x0103 }
    L_0x0044:
        return r15;	 Catch:{ NameNotFoundException -> 0x0103 }
    L_0x0045:
        r17 = "LiveIconSupport";	 Catch:{ NameNotFoundException -> 0x0103 }
        r0 = r17;	 Catch:{ NameNotFoundException -> 0x0103 }
        r12 = r14.getBoolean(r0);	 Catch:{ NameNotFoundException -> 0x0103 }
        if (r12 == 0) goto L_0x02d9;
    L_0x004f:
        r9 = 0;
        r4 = 0;
        r13 = 0;
        r17 = new java.lang.StringBuilder;
        r17.<init>();
        r0 = r17;
        r1 = r24;
        r17 = r0.append(r1);
        r18 = ".LiveIconLoader";
        r17 = r17.append(r18);
        r8 = r17.toString();
        r18 = mLiveIconPackageMatchers;
        monitor-enter(r18);
        r19 = mLiveIconLoaders;	 Catch:{ all -> 0x028e }
        monitor-enter(r19);	 Catch:{ all -> 0x028e }
        r17 = mLiveIconPackageMatchers;	 Catch:{ all -> 0x028b }
        r0 = r17;	 Catch:{ all -> 0x028b }
        r1 = r24;	 Catch:{ all -> 0x028b }
        r17 = r0.containsKey(r1);	 Catch:{ all -> 0x028b }
        if (r17 == 0) goto L_0x01eb;	 Catch:{ all -> 0x028b }
    L_0x007b:
        if (r16 == 0) goto L_0x0123;	 Catch:{ all -> 0x028b }
    L_0x007d:
        r17 = mLiveIconPackageMatchers;	 Catch:{ all -> 0x028b }
        r0 = r17;	 Catch:{ all -> 0x028b }
        r1 = r24;	 Catch:{ all -> 0x028b }
        r17 = r0.get(r1);	 Catch:{ all -> 0x028b }
        r17 = r16.equals(r17);	 Catch:{ all -> 0x028b }
        if (r17 == 0) goto L_0x0123;	 Catch:{ all -> 0x028b }
    L_0x008d:
        r17 = "ApplicationPackageManager";	 Catch:{ all -> 0x028b }
        r20 = new java.lang.StringBuilder;	 Catch:{ all -> 0x028b }
        r20.<init>();	 Catch:{ all -> 0x028b }
        r21 = "we has ";	 Catch:{ all -> 0x028b }
        r20 = r20.append(r21);	 Catch:{ all -> 0x028b }
        r0 = r20;	 Catch:{ all -> 0x028b }
        r1 = r24;	 Catch:{ all -> 0x028b }
        r20 = r0.append(r1);	 Catch:{ all -> 0x028b }
        r21 = " class. reuse it ";	 Catch:{ all -> 0x028b }
        r20 = r20.append(r21);	 Catch:{ all -> 0x028b }
        r20 = r20.toString();	 Catch:{ all -> 0x028b }
        r0 = r17;	 Catch:{ all -> 0x028b }
        r1 = r20;	 Catch:{ all -> 0x028b }
        android.util.Log.d(r0, r1);	 Catch:{ all -> 0x028b }
        r17 = mLiveIconLoaders;	 Catch:{ all -> 0x028b }
        r0 = r17;	 Catch:{ all -> 0x028b }
        r1 = r24;	 Catch:{ all -> 0x028b }
        r17 = r0.get(r1);	 Catch:{ all -> 0x028b }
        r0 = r17;	 Catch:{ all -> 0x028b }
        r0 = (java.lang.reflect.Method) r0;	 Catch:{ all -> 0x028b }
        r13 = r0;	 Catch:{ all -> 0x028b }
    L_0x00c3:
        monitor-exit(r19);	 Catch:{ all -> 0x028b }
        monitor-exit(r18);	 Catch:{ all -> 0x028e }
        if (r13 == 0) goto L_0x02d9;
    L_0x00c7:
        r11 = new android.app.ApplicationPackageManager$LiveIconObject;	 Catch:{ InvocationTargetException -> 0x0291, IllegalAccessException -> 0x02a3, CloneNotSupportedException -> 0x02b5, Exception -> 0x02c7 }
        r0 = r23;	 Catch:{ InvocationTargetException -> 0x0291, IllegalAccessException -> 0x02a3, CloneNotSupportedException -> 0x02b5, Exception -> 0x02c7 }
        r11.<init>();	 Catch:{ InvocationTargetException -> 0x0291, IllegalAccessException -> 0x02a3, CloneNotSupportedException -> 0x02b5, Exception -> 0x02c7 }
        r17 = 0;	 Catch:{ InvocationTargetException -> 0x0291, IllegalAccessException -> 0x02a3, CloneNotSupportedException -> 0x02b5, Exception -> 0x02c7 }
        r18 = 1;	 Catch:{ InvocationTargetException -> 0x0291, IllegalAccessException -> 0x02a3, CloneNotSupportedException -> 0x02b5, Exception -> 0x02c7 }
        r0 = r18;	 Catch:{ InvocationTargetException -> 0x0291, IllegalAccessException -> 0x02a3, CloneNotSupportedException -> 0x02b5, Exception -> 0x02c7 }
        r0 = new java.lang.Object[r0];	 Catch:{ InvocationTargetException -> 0x0291, IllegalAccessException -> 0x02a3, CloneNotSupportedException -> 0x02b5, Exception -> 0x02c7 }
        r18 = r0;	 Catch:{ InvocationTargetException -> 0x0291, IllegalAccessException -> 0x02a3, CloneNotSupportedException -> 0x02b5, Exception -> 0x02c7 }
        r19 = 0;	 Catch:{ InvocationTargetException -> 0x0291, IllegalAccessException -> 0x02a3, CloneNotSupportedException -> 0x02b5, Exception -> 0x02c7 }
        r0 = r23;	 Catch:{ InvocationTargetException -> 0x0291, IllegalAccessException -> 0x02a3, CloneNotSupportedException -> 0x02b5, Exception -> 0x02c7 }
        r0 = r0.mContext;	 Catch:{ InvocationTargetException -> 0x0291, IllegalAccessException -> 0x02a3, CloneNotSupportedException -> 0x02b5, Exception -> 0x02c7 }
        r20 = r0;	 Catch:{ InvocationTargetException -> 0x0291, IllegalAccessException -> 0x02a3, CloneNotSupportedException -> 0x02b5, Exception -> 0x02c7 }
        r18[r19] = r20;	 Catch:{ InvocationTargetException -> 0x0291, IllegalAccessException -> 0x02a3, CloneNotSupportedException -> 0x02b5, Exception -> 0x02c7 }
        r0 = r17;	 Catch:{ InvocationTargetException -> 0x0291, IllegalAccessException -> 0x02a3, CloneNotSupportedException -> 0x02b5, Exception -> 0x02c7 }
        r1 = r18;	 Catch:{ InvocationTargetException -> 0x0291, IllegalAccessException -> 0x02a3, CloneNotSupportedException -> 0x02b5, Exception -> 0x02c7 }
        r17 = r13.invoke(r0, r1);	 Catch:{ InvocationTargetException -> 0x0291, IllegalAccessException -> 0x02a3, CloneNotSupportedException -> 0x02b5, Exception -> 0x02c7 }
        r0 = r17;	 Catch:{ InvocationTargetException -> 0x0291, IllegalAccessException -> 0x02a3, CloneNotSupportedException -> 0x02b5, Exception -> 0x02c7 }
        r11.setLiveIcon(r0);	 Catch:{ InvocationTargetException -> 0x0291, IllegalAccessException -> 0x02a3, CloneNotSupportedException -> 0x02b5, Exception -> 0x02c7 }
        r5 = r11.clone();	 Catch:{ InvocationTargetException -> 0x0291, IllegalAccessException -> 0x02a3, CloneNotSupportedException -> 0x02b5, Exception -> 0x02c7 }
        r5 = (android.app.ApplicationPackageManager.LiveIconObject) r5;	 Catch:{ InvocationTargetException -> 0x0291, IllegalAccessException -> 0x02a3, CloneNotSupportedException -> 0x02b5, Exception -> 0x02c7 }
        r15 = r5.getLiveIcon();	 Catch:{ InvocationTargetException -> 0x0291, IllegalAccessException -> 0x02a3, CloneNotSupportedException -> 0x02b5, Exception -> 0x02c7 }
        r0 = r15 instanceof android.graphics.drawable.Drawable;	 Catch:{ InvocationTargetException -> 0x0291, IllegalAccessException -> 0x02a3, CloneNotSupportedException -> 0x02b5, Exception -> 0x02c7 }
        r17 = r0;	 Catch:{ InvocationTargetException -> 0x0291, IllegalAccessException -> 0x02a3, CloneNotSupportedException -> 0x02b5, Exception -> 0x02c7 }
        if (r17 == 0) goto L_0x02d9;	 Catch:{ InvocationTargetException -> 0x0291, IllegalAccessException -> 0x02a3, CloneNotSupportedException -> 0x02b5, Exception -> 0x02c7 }
    L_0x00ff:
        r15 = (android.graphics.drawable.Drawable) r15;	 Catch:{ InvocationTargetException -> 0x0291, IllegalAccessException -> 0x02a3, CloneNotSupportedException -> 0x02b5, Exception -> 0x02c7 }
        goto L_0x0044;
    L_0x0103:
        r6 = move-exception;
        r17 = "ApplicationPackageManager";
        r18 = new java.lang.StringBuilder;
        r18.<init>();
        r19 = "get application info error in getLiveIcon : ";
        r18 = r18.append(r19);
        r0 = r18;
        r1 = r24;
        r18 = r0.append(r1);
        r18 = r18.toString();
        android.util.Log.i(r17, r18);
        r15 = 0;
        goto L_0x0044;
    L_0x0123:
        if (r16 == 0) goto L_0x00c3;
    L_0x0125:
        r17 = mLiveIconPackageMatchers;	 Catch:{ all -> 0x028b }
        r0 = r17;	 Catch:{ all -> 0x028b }
        r1 = r24;	 Catch:{ all -> 0x028b }
        r17 = r0.get(r1);	 Catch:{ all -> 0x028b }
        r17 = r16.equals(r17);	 Catch:{ all -> 0x028b }
        if (r17 != 0) goto L_0x00c3;	 Catch:{ all -> 0x028b }
    L_0x0135:
        r17 = "ApplicationPackageManager";	 Catch:{ all -> 0x028b }
        r20 = new java.lang.StringBuilder;	 Catch:{ all -> 0x028b }
        r20.<init>();	 Catch:{ all -> 0x028b }
        r21 = "we don't have ";	 Catch:{ all -> 0x028b }
        r20 = r20.append(r21);	 Catch:{ all -> 0x028b }
        r0 = r20;	 Catch:{ all -> 0x028b }
        r1 = r16;	 Catch:{ all -> 0x028b }
        r20 = r0.append(r1);	 Catch:{ all -> 0x028b }
        r21 = " package path. load it";	 Catch:{ all -> 0x028b }
        r20 = r20.append(r21);	 Catch:{ all -> 0x028b }
        r20 = r20.toString();	 Catch:{ all -> 0x028b }
        r0 = r17;	 Catch:{ all -> 0x028b }
        r1 = r20;	 Catch:{ all -> 0x028b }
        android.util.Log.d(r0, r1);	 Catch:{ all -> 0x028b }
        r10 = new dalvik.system.PathClassLoader;	 Catch:{ all -> 0x028b }
        r17 = java.lang.ClassLoader.getSystemClassLoader();	 Catch:{ all -> 0x028b }
        r0 = r16;	 Catch:{ all -> 0x028b }
        r1 = r17;	 Catch:{ all -> 0x028b }
        r10.<init>(r0, r1);	 Catch:{ all -> 0x028b }
        r17 = 1;
        r0 = r17;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r4 = java.lang.Class.forName(r8, r0, r10);	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r17 = "getLiveIcon";	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r20 = 1;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r0 = r20;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r0 = new java.lang.Class[r0];	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r20 = r0;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r21 = 0;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r22 = android.content.Context.class;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r20[r21] = r22;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r0 = r17;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r1 = r20;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r13 = r4.getMethod(r0, r1);	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r17 = mLiveIconPackageMatchers;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r0 = r17;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r1 = r24;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r0.remove(r1);	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r17 = mLiveIconLoaders;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r0 = r17;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r1 = r24;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r0.remove(r1);	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r17 = mLiveIconPackageMatchers;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r0 = r17;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r1 = r24;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r2 = r16;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r0.put(r1, r2);	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r17 = mLiveIconLoaders;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r0 = r17;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r1 = r24;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r0.put(r1, r13);	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r9 = r10;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        goto L_0x00c3;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
    L_0x01b2:
        r6 = move-exception;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r17 = "ApplicationPackageManager";	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r20 = new java.lang.StringBuilder;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r20.<init>();	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r21 = "!@can't found class";	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r20 = r20.append(r21);	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r0 = r20;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r20 = r0.append(r8);	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r20 = r20.toString();	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r0 = r17;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r1 = r20;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        android.util.Log.e(r0, r1);	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r15 = 0;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        monitor-exit(r19);	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        monitor-exit(r18);	 Catch:{ all -> 0x01d6 }
        goto L_0x0044;
    L_0x01d6:
        r17 = move-exception;
        r9 = r10;
    L_0x01d8:
        monitor-exit(r18);	 Catch:{ all -> 0x028e }
        throw r17;
    L_0x01da:
        r7 = move-exception;
        r17 = "ApplicationPackageManager";	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r20 = "!@call method fail getLiveIcon";	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r0 = r17;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r1 = r20;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        android.util.Log.e(r0, r1, r7);	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r15 = 0;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        monitor-exit(r19);	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        monitor-exit(r18);	 Catch:{ all -> 0x01d6 }
        goto L_0x0044;
    L_0x01eb:
        r17 = "ApplicationPackageManager";	 Catch:{ all -> 0x028b }
        r20 = new java.lang.StringBuilder;	 Catch:{ all -> 0x028b }
        r20.<init>();	 Catch:{ all -> 0x028b }
        r21 = "we don't have ";	 Catch:{ all -> 0x028b }
        r20 = r20.append(r21);	 Catch:{ all -> 0x028b }
        r0 = r20;	 Catch:{ all -> 0x028b }
        r1 = r24;	 Catch:{ all -> 0x028b }
        r20 = r0.append(r1);	 Catch:{ all -> 0x028b }
        r21 = " package name. load it";	 Catch:{ all -> 0x028b }
        r20 = r20.append(r21);	 Catch:{ all -> 0x028b }
        r20 = r20.toString();	 Catch:{ all -> 0x028b }
        r0 = r17;	 Catch:{ all -> 0x028b }
        r1 = r20;	 Catch:{ all -> 0x028b }
        android.util.Log.d(r0, r1);	 Catch:{ all -> 0x028b }
        r10 = new dalvik.system.PathClassLoader;	 Catch:{ all -> 0x028b }
        r17 = java.lang.ClassLoader.getSystemClassLoader();	 Catch:{ all -> 0x028b }
        r0 = r16;	 Catch:{ all -> 0x028b }
        r1 = r17;	 Catch:{ all -> 0x028b }
        r10.<init>(r0, r1);	 Catch:{ all -> 0x028b }
        r17 = 1;
        r0 = r17;	 Catch:{ ClassNotFoundException -> 0x0256, NoSuchMethodException -> 0x027a }
        r4 = java.lang.Class.forName(r8, r0, r10);	 Catch:{ ClassNotFoundException -> 0x0256, NoSuchMethodException -> 0x027a }
        r17 = "getLiveIcon";	 Catch:{ ClassNotFoundException -> 0x0256, NoSuchMethodException -> 0x027a }
        r20 = 1;	 Catch:{ ClassNotFoundException -> 0x0256, NoSuchMethodException -> 0x027a }
        r0 = r20;	 Catch:{ ClassNotFoundException -> 0x0256, NoSuchMethodException -> 0x027a }
        r0 = new java.lang.Class[r0];	 Catch:{ ClassNotFoundException -> 0x0256, NoSuchMethodException -> 0x027a }
        r20 = r0;	 Catch:{ ClassNotFoundException -> 0x0256, NoSuchMethodException -> 0x027a }
        r21 = 0;	 Catch:{ ClassNotFoundException -> 0x0256, NoSuchMethodException -> 0x027a }
        r22 = android.content.Context.class;	 Catch:{ ClassNotFoundException -> 0x0256, NoSuchMethodException -> 0x027a }
        r20[r21] = r22;	 Catch:{ ClassNotFoundException -> 0x0256, NoSuchMethodException -> 0x027a }
        r0 = r17;	 Catch:{ ClassNotFoundException -> 0x0256, NoSuchMethodException -> 0x027a }
        r1 = r20;	 Catch:{ ClassNotFoundException -> 0x0256, NoSuchMethodException -> 0x027a }
        r13 = r4.getMethod(r0, r1);	 Catch:{ ClassNotFoundException -> 0x0256, NoSuchMethodException -> 0x027a }
        r17 = mLiveIconPackageMatchers;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r0 = r17;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r1 = r24;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r2 = r16;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r0.put(r1, r2);	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r17 = mLiveIconLoaders;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r0 = r17;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r1 = r24;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r0.put(r1, r13);	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r9 = r10;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        goto L_0x00c3;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
    L_0x0256:
        r6 = move-exception;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r17 = "ApplicationPackageManager";	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r20 = new java.lang.StringBuilder;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r20.<init>();	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r21 = "!@can't found class";	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r20 = r20.append(r21);	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r0 = r20;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r20 = r0.append(r8);	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r20 = r20.toString();	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r0 = r17;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r1 = r20;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        android.util.Log.e(r0, r1);	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r15 = 0;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        monitor-exit(r19);	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        monitor-exit(r18);	 Catch:{ all -> 0x01d6 }
        goto L_0x0044;
    L_0x027a:
        r7 = move-exception;
        r17 = "ApplicationPackageManager";	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r20 = "!@call method fail getLiveIcon";	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r0 = r17;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r1 = r20;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        android.util.Log.e(r0, r1, r7);	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        r15 = 0;	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        monitor-exit(r19);	 Catch:{ ClassNotFoundException -> 0x01b2, NoSuchMethodException -> 0x01da, all -> 0x02dc }
        monitor-exit(r18);	 Catch:{ all -> 0x01d6 }
        goto L_0x0044;
    L_0x028b:
        r17 = move-exception;
    L_0x028c:
        monitor-exit(r19);	 Catch:{ all -> 0x028b }
        throw r17;	 Catch:{ all -> 0x028e }
    L_0x028e:
        r17 = move-exception;
        goto L_0x01d8;
    L_0x0291:
        r7 = move-exception;
        r17 = "ApplicationPackageManager";
        r18 = "!@call method fail getLiveIcon";
        r0 = r17;
        r1 = r18;
        android.util.Log.e(r0, r1, r7);
        r7.printStackTrace();
        r15 = 0;
        goto L_0x0044;
    L_0x02a3:
        r7 = move-exception;
        r17 = "ApplicationPackageManager";
        r18 = "!@call method fail getLiveIcon";
        r0 = r17;
        r1 = r18;
        android.util.Log.e(r0, r1, r7);
        r7.printStackTrace();
        r15 = 0;
        goto L_0x0044;
    L_0x02b5:
        r7 = move-exception;
        r17 = "ApplicationPackageManager";
        r18 = "!@clone fail getLiveIcon";
        r0 = r17;
        r1 = r18;
        android.util.Log.e(r0, r1, r7);
        r7.printStackTrace();
        r15 = 0;
        goto L_0x0044;
    L_0x02c7:
        r7 = move-exception;
        r17 = "ApplicationPackageManager";
        r18 = "!@clone fail getLiveIcon";
        r0 = r17;
        r1 = r18;
        android.util.Log.e(r0, r1, r7);
        r7.printStackTrace();
        r15 = 0;
        goto L_0x0044;
    L_0x02d9:
        r15 = 0;
        goto L_0x0044;
    L_0x02dc:
        r17 = move-exception;
        r9 = r10;
        goto L_0x028c;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ApplicationPackageManager.getLiveIcon(java.lang.String):android.graphics.drawable.Drawable");
    }

    UserManager getUserManager() {
        UserManager userManager;
        synchronized (this.mLock) {
            if (this.mUserManager == null) {
                this.mUserManager = UserManager.get(this.mContext);
            }
            userManager = this.mUserManager;
        }
        return userManager;
    }

    public PackageInfo getPackageInfo(String packageName, int flags) throws NameNotFoundException {
        try {
            PackageInfo pi = this.mPM.getPackageInfo(packageName, flags, this.mContext.getUserId());
            if (pi != null) {
                return pi;
            }
            throw new NameNotFoundException(packageName);
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public String[] currentToCanonicalPackageNames(String[] names) {
        try {
            return this.mPM.currentToCanonicalPackageNames(names);
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public String[] canonicalToCurrentPackageNames(String[] names) {
        try {
            return this.mPM.canonicalToCurrentPackageNames(names);
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public Intent getLaunchIntentForPackage(String packageName) {
        Intent intentToResolve = new Intent(Intent.ACTION_MAIN);
        intentToResolve.addCategory(Intent.CATEGORY_INFO);
        intentToResolve.setPackage(packageName);
        List<ResolveInfo> ris = queryIntentActivities(intentToResolve, 0);
        if (ris == null || ris.size() <= 0) {
            intentToResolve.removeCategory(Intent.CATEGORY_INFO);
            intentToResolve.addCategory(Intent.CATEGORY_LAUNCHER);
            intentToResolve.setPackage(packageName);
            ris = queryIntentActivities(intentToResolve, 0);
        }
        if (ris == null || ris.size() <= 0) {
            return null;
        }
        Intent intent = new Intent(intentToResolve);
        intent.setFlags(268435456);
        intent.setClassName(((ResolveInfo) ris.get(0)).activityInfo.packageName, ((ResolveInfo) ris.get(0)).activityInfo.name);
        return intent;
    }

    public Intent getLeanbackLaunchIntentForPackage(String packageName) {
        Intent intentToResolve = new Intent(Intent.ACTION_MAIN);
        intentToResolve.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);
        intentToResolve.setPackage(packageName);
        List<ResolveInfo> ris = queryIntentActivities(intentToResolve, 0);
        if (ris == null || ris.size() <= 0) {
            return null;
        }
        Intent intent = new Intent(intentToResolve);
        intent.setFlags(268435456);
        intent.setClassName(((ResolveInfo) ris.get(0)).activityInfo.packageName, ((ResolveInfo) ris.get(0)).activityInfo.name);
        return intent;
    }

    public int[] getPackageGids(String packageName) throws NameNotFoundException {
        try {
            int[] gids = this.mPM.getPackageGids(packageName, this.mContext.getUserId());
            if (gids != null) {
                return gids;
            }
            throw new NameNotFoundException(packageName);
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public int getPackageUid(String packageName, int userHandle) throws NameNotFoundException {
        try {
            int uid = this.mPM.getPackageUid(packageName, userHandle);
            if (uid >= 0) {
                return uid;
            }
            throw new NameNotFoundException(packageName);
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public PermissionInfo getPermissionInfo(String name, int flags) throws NameNotFoundException {
        try {
            PermissionInfo pi = this.mPM.getPermissionInfo(name, flags);
            if (pi != null) {
                return pi;
            }
            throw new NameNotFoundException(name);
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public List<PermissionInfo> queryPermissionsByGroup(String group, int flags) throws NameNotFoundException {
        try {
            List<PermissionInfo> pi = this.mPM.queryPermissionsByGroup(group, flags);
            if (pi != null) {
                return pi;
            }
            throw new NameNotFoundException(group);
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public PermissionGroupInfo getPermissionGroupInfo(String name, int flags) throws NameNotFoundException {
        try {
            PermissionGroupInfo pgi = this.mPM.getPermissionGroupInfo(name, flags);
            if (pgi != null) {
                return pgi;
            }
            throw new NameNotFoundException(name);
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public List<PermissionGroupInfo> getAllPermissionGroups(int flags) {
        try {
            return this.mPM.getAllPermissionGroups(flags);
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public ApplicationInfo getApplicationInfo(String packageName, int flags) throws NameNotFoundException {
        try {
            ApplicationInfo ai = this.mPM.getApplicationInfo(packageName, flags, this.mContext.getUserId());
            if (ai != null) {
                maybeAdjustApplicationInfo(ai);
                return ai;
            }
            throw new NameNotFoundException(packageName);
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    private static void maybeAdjustApplicationInfo(ApplicationInfo info) {
        if (info.primaryCpuAbi != null && info.secondaryCpuAbi != null) {
            String runtimeIsa = VMRuntime.getRuntime().vmInstructionSet();
            String secondaryIsa = VMRuntime.getInstructionSet(info.secondaryCpuAbi);
            String secondaryDexCodeIsa = SystemProperties.get("ro.dalvik.vm.isa." + secondaryIsa);
            if (!secondaryDexCodeIsa.isEmpty()) {
                secondaryIsa = secondaryDexCodeIsa;
            }
            if (runtimeIsa.equals(secondaryIsa)) {
                info.nativeLibraryDir = info.secondaryNativeLibraryDir;
            }
        }
    }

    public ActivityInfo getActivityInfo(ComponentName className, int flags) throws NameNotFoundException {
        try {
            ActivityInfo ai = this.mPM.getActivityInfo(className, flags, this.mContext.getUserId());
            if (ai != null) {
                return ai;
            }
            throw new NameNotFoundException(className.toString());
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public ActivityInfo getReceiverInfo(ComponentName className, int flags) throws NameNotFoundException {
        try {
            ActivityInfo ai = this.mPM.getReceiverInfo(className, flags, this.mContext.getUserId());
            if (ai != null) {
                return ai;
            }
            throw new NameNotFoundException(className.toString());
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public ServiceInfo getServiceInfo(ComponentName className, int flags) throws NameNotFoundException {
        try {
            ServiceInfo si = this.mPM.getServiceInfo(className, flags, this.mContext.getUserId());
            if (si != null) {
                return si;
            }
            throw new NameNotFoundException(className.toString());
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public ProviderInfo getProviderInfo(ComponentName className, int flags) throws NameNotFoundException {
        try {
            ProviderInfo pi = this.mPM.getProviderInfo(className, flags, this.mContext.getUserId());
            if (pi != null) {
                return pi;
            }
            throw new NameNotFoundException(className.toString());
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public String[] getSystemSharedLibraryNames() {
        try {
            return this.mPM.getSystemSharedLibraryNames();
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public FeatureInfo[] getSystemAvailableFeatures() {
        try {
            return this.mPM.getSystemAvailableFeatures();
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public boolean hasSystemFeature(String name) {
        try {
            return this.mPM.hasSystemFeature(name);
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public int getSystemFeatureLevel(String name) {
        try {
            return this.mPM.getSystemFeatureLevel(name);
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public int checkPermission(String permName, String pkgName) {
        try {
            return this.mPM.checkPermission(permName, pkgName, this.mContext.getUserId());
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public boolean isPermissionRevokedByPolicy(String permName, String pkgName) {
        try {
            return this.mPM.isPermissionRevokedByPolicy(permName, pkgName, this.mContext.getUserId());
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public boolean isPermissionRevokedByUserFixed(String permName, String pkgName) {
        try {
            return this.mPM.isPermissionRevokedByUserFixed(permName, pkgName, this.mContext.getUserId());
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public String getPermissionControllerPackageName() {
        String str;
        synchronized (this.mLock) {
            if (this.mPermissionsControllerPackageName == null) {
                try {
                    this.mPermissionsControllerPackageName = this.mPM.getPermissionControllerPackageName();
                } catch (RemoteException e) {
                    throw new RuntimeException("Package manager has died", e);
                }
            }
            str = this.mPermissionsControllerPackageName;
        }
        return str;
    }

    public boolean addPermission(PermissionInfo info) {
        try {
            return this.mPM.addPermission(info);
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public boolean addPermissionAsync(PermissionInfo info) {
        try {
            return this.mPM.addPermissionAsync(info);
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public void removePermission(String name) {
        try {
            this.mPM.removePermission(name);
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public void grantRuntimePermission(String packageName, String permissionName, UserHandle user) {
        try {
            this.mPM.grantRuntimePermission(packageName, permissionName, user.getIdentifier());
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public void revokeRuntimePermission(String packageName, String permissionName, UserHandle user) {
        try {
            this.mPM.revokeRuntimePermission(packageName, permissionName, user.getIdentifier());
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public int getPermissionFlags(String permissionName, String packageName, UserHandle user) {
        try {
            return this.mPM.getPermissionFlags(permissionName, packageName, user.getIdentifier());
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public void updatePermissionFlags(String permissionName, String packageName, int flagMask, int flagValues, UserHandle user) {
        try {
            this.mPM.updatePermissionFlags(permissionName, packageName, flagMask, flagValues, user.getIdentifier());
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public boolean shouldShowRequestPermissionRationale(String permission) {
        try {
            return this.mPM.shouldShowRequestPermissionRationale(permission, this.mContext.getPackageName(), this.mContext.getUserId());
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public int checkSignatures(String pkg1, String pkg2) {
        try {
            return this.mPM.checkSignatures(pkg1, pkg2);
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public int checkSignatures(int uid1, int uid2) {
        try {
            return this.mPM.checkUidSignatures(uid1, uid2);
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public String[] getPackagesForUid(int uid) {
        try {
            return this.mPM.getPackagesForUid(uid);
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public String getNameForUid(int uid) {
        try {
            return this.mPM.getNameForUid(uid);
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public int getUidForSharedUser(String sharedUserName) throws NameNotFoundException {
        try {
            int uid = this.mPM.getUidForSharedUser(sharedUserName);
            if (uid != -1) {
                return uid;
            }
            throw new NameNotFoundException("No shared userid for user:" + sharedUserName);
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public List<PackageInfo> getInstalledPackages(int flags) {
        return getInstalledPackages(flags, this.mContext.getUserId());
    }

    public List<PackageInfo> getInstalledPackages(int flags, int userId) {
        try {
            return this.mPM.getInstalledPackages(flags, userId).getList();
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public List<ApplicationInfo> getAutoRunPackgeList() {
        try {
            List<PackageInfo> packageInfos = this.mPM.getInstalledPackages(2, this.mContext.getUserId()).getList();
            List<ApplicationInfo> ret = new ArrayList();
            for (PackageInfo p : packageInfos) {
                if ((p.applicationInfo.flags & 129) == 0 && p.receivers != null && p.receivers.length > 0) {
                    ret.add(p.applicationInfo);
                }
            }
            return ret;
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public List<PackageInfo> getPackagesHoldingPermissions(String[] permissions, int flags) {
        try {
            return this.mPM.getPackagesHoldingPermissions(permissions, flags, this.mContext.getUserId()).getList();
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public List<ApplicationInfo> getInstalledApplications(int flags) {
        try {
            ParceledListSlice<ApplicationInfo> slice = this.mPM.getInstalledApplications(flags, this.mContext.getUserId());
            this.mPM.hasSystemFeature("dummy");
            return slice.getList();
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public ResolveInfo resolveActivity(Intent intent, int flags) {
        return resolveActivityAsUser(intent, flags, this.mContext.getUserId());
    }

    public ResolveInfo resolveActivityAsUser(Intent intent, int flags, int userId) {
        try {
            return this.mPM.resolveIntent(intent, intent.resolveTypeIfNeeded(this.mContext.getContentResolver()), flags, userId);
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public List<ResolveInfo> queryIntentActivities(Intent intent, int flags) {
        return queryIntentActivitiesAsUser(intent, flags, this.mContext.getUserId());
    }

    public List<ResolveInfo> queryIntentActivitiesAsUser(Intent intent, int flags, int userId) {
        try {
            List<ResolveInfo> items = this.mPM.queryIntentActivities(intent, intent.resolveTypeIfNeeded(this.mContext.getContentResolver()), flags, userId);
            if (Build.IS_SYSTEM_SECURE && items != null) {
                this.mContext.getResources().addAppsNames(this.mContext, this, items);
            }
            return items;
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public List<ResolveInfo> queryIntentActivityOptions(ComponentName caller, Intent[] specifics, Intent intent, int flags) {
        ContentResolver resolver = this.mContext.getContentResolver();
        String[] specificTypes = null;
        if (specifics != null) {
            int N = specifics.length;
            for (int i = 0; i < N; i++) {
                Intent sp = specifics[i];
                if (sp != null) {
                    String t = sp.resolveTypeIfNeeded(resolver);
                    if (t != null) {
                        if (specificTypes == null) {
                            specificTypes = new String[N];
                        }
                        specificTypes[i] = t;
                    }
                }
            }
        }
        try {
            return this.mPM.queryIntentActivityOptions(caller, specifics, specificTypes, intent, intent.resolveTypeIfNeeded(resolver), flags, this.mContext.getUserId());
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public List<ResolveInfo> queryBroadcastReceivers(Intent intent, int flags, int userId) {
        try {
            return this.mPM.queryIntentReceivers(intent, intent.resolveTypeIfNeeded(this.mContext.getContentResolver()), flags, userId);
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public List<ResolveInfo> queryBroadcastReceivers(Intent intent, int flags) {
        return queryBroadcastReceivers(intent, flags, this.mContext.getUserId());
    }

    public ResolveInfo resolveService(Intent intent, int flags) {
        try {
            return this.mPM.resolveService(intent, intent.resolveTypeIfNeeded(this.mContext.getContentResolver()), flags, this.mContext.getUserId());
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public List<ResolveInfo> queryIntentServicesAsUser(Intent intent, int flags, int userId) {
        try {
            return this.mPM.queryIntentServices(intent, intent.resolveTypeIfNeeded(this.mContext.getContentResolver()), flags, userId);
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public List<ResolveInfo> queryIntentServices(Intent intent, int flags) {
        return queryIntentServicesAsUser(intent, flags, this.mContext.getUserId());
    }

    public List<ResolveInfo> queryIntentContentProvidersAsUser(Intent intent, int flags, int userId) {
        try {
            return this.mPM.queryIntentContentProviders(intent, intent.resolveTypeIfNeeded(this.mContext.getContentResolver()), flags, userId);
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public List<ResolveInfo> queryIntentContentProviders(Intent intent, int flags) {
        return queryIntentContentProvidersAsUser(intent, flags, this.mContext.getUserId());
    }

    public ProviderInfo resolveContentProvider(String name, int flags) {
        return resolveContentProviderAsUser(name, flags, this.mContext.getUserId());
    }

    public ProviderInfo resolveContentProviderAsUser(String name, int flags, int userId) {
        try {
            return this.mPM.resolveContentProvider(name, flags, userId);
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public List<ProviderInfo> queryContentProviders(String processName, int uid, int flags) {
        try {
            ParceledListSlice<ProviderInfo> slice = this.mPM.queryContentProviders(processName, uid, flags);
            return slice != null ? slice.getList() : null;
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public InstrumentationInfo getInstrumentationInfo(ComponentName className, int flags) throws NameNotFoundException {
        try {
            InstrumentationInfo ii = this.mPM.getInstrumentationInfo(className, flags);
            if (ii != null) {
                return ii;
            }
            throw new NameNotFoundException(className.toString());
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public List<InstrumentationInfo> queryInstrumentation(String targetPackage, int flags) {
        try {
            return this.mPM.queryInstrumentation(targetPackage, flags);
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public Drawable getDrawable(String packageName, int resId, ApplicationInfo appInfo) {
        if (appInfo != null && resId == appInfo.icon) {
            try {
                byte[] imageData = EnterpriseDeviceManager.getInstance().getApplicationPolicy().getApplicationIconFromDb(packageName, UserHandle.getUserId(appInfo.uid));
                if (imageData != null) {
                    ByteArrayInputStream is = new ByteArrayInputStream(imageData);
                    TypedValue typedValue = new TypedValue();
                    typedValue.density = 0;
                    Options opts = new Options();
                    opts.inTargetDensity = this.mContext.getResources().getDisplayMetrics().densityDpi;
                    Drawable drw = Drawable.createFromResourceStream(this.mContext.getResources(), typedValue, is, null, opts);
                    Log.i(TAG, "EDM:ApplicationIcon got from EDM database ");
                    return drw;
                }
            } catch (Exception e) {
                Log.w(TAG, "EDM: Get Icon EX: " + e);
            }
        }
        ResourceName name = new ResourceName(packageName, resId);
        Drawable cachedIcon = getCachedIcon(name);
        if (cachedIcon != null) {
            return cachedIcon;
        }
        if (appInfo == null) {
            try {
                appInfo = getApplicationInfo(packageName, 1024);
            } catch (NameNotFoundException e2) {
                return null;
            }
        }
        if (resId != 0) {
            try {
                Resources r = getResourcesForApplication(appInfo);
                r.mPackageName = appInfo.packageName;
                r.mAppIconResId = appInfo.icon;
                r.mUserId = UserHandle.getUserId(appInfo.uid);
                Drawable dr = r.getDrawable(resId, null);
                if (dr != null) {
                    putCachedIcon(name, dr);
                }
                return dr;
            } catch (NameNotFoundException e3) {
                Log.w("PackageManager", "Failure retrieving resources for " + appInfo.packageName);
            } catch (NotFoundException e4) {
                Log.w("PackageManager", "Failure retrieving resources for " + appInfo.packageName + ": " + e4.getMessage());
            } catch (Exception e5) {
                Log.w("PackageManager", "Failure retrieving icon 0x" + Integer.toHexString(resId) + " in package " + packageName, e5);
            }
        }
        return null;
    }

    public Drawable getActivityIcon(ComponentName activityName) throws NameNotFoundException {
        return getActivityInfo(activityName, 1024).loadIcon(this);
    }

    public Drawable getActivityIcon(Intent intent) throws NameNotFoundException {
        if (intent.getComponent() != null) {
            return getActivityIcon(intent.getComponent());
        }
        ResolveInfo info = resolveActivity(intent, 65536);
        if (info != null) {
            return info.activityInfo.loadIcon(this);
        }
        throw new NameNotFoundException(intent.toUri(0));
    }

    public Drawable getActivityIconForIconTray(ComponentName activityName, int mode) throws NameNotFoundException {
        return getActivityInfo(activityName, 1024).loadIcon(this, true, mode);
    }

    public Drawable getActivityIconForIconTray(Intent intent, int mode) throws NameNotFoundException {
        if (intent.getComponent() != null) {
            return getActivityIconForIconTray(intent.getComponent(), mode);
        }
        ResolveInfo info = resolveActivity(intent, 65536);
        if (info != null) {
            return info.activityInfo.loadIcon(this, true, mode);
        }
        throw new NameNotFoundException(intent.toUri(0));
    }

    public Drawable getDefaultActivityIcon() {
        return Resources.getSystem().getDrawable(R.drawable.sym_def_app_icon);
    }

    public Drawable getApplicationIcon(ApplicationInfo info) {
        return info.loadIcon(this);
    }

    public Drawable getApplicationIcon(String packageName) throws NameNotFoundException {
        return getApplicationIcon(getApplicationInfo(packageName, 1024));
    }

    public Drawable getApplicationIconForIconTray(ApplicationInfo info, int mode) {
        return info.loadIcon(this, true, mode);
    }

    public Drawable getApplicationIconForIconTray(String packageName, int mode) throws NameNotFoundException {
        return getApplicationIconForIconTray(getApplicationInfo(packageName, 1024), mode);
    }

    public CharSequence getCSCPackageItemText(String packageItemName) {
        String appName;
        String salesCode;
        Resources stkResource;
        int appNameId;
        String fixedStkTitle;
        boolean isZVVCSC = "ZVV".equals(SystemProperties.get("ro.csc.sales_code", "null"));
        String simOperator = null;
        if ("com.android.stk".equals(packageItemName) || "com.android.stk.StkLauncherActivity".equals(packageItemName) || "com.android.stk.StkLauncherActivity1".equals(packageItemName) || "com.sec.android.app.latin.launcher.stk".equals(packageItemName) || "com.sec.android.app.latin.launcher.stk.StkShortcutWidget".equals(packageItemName)) {
            appName = null;
            salesCode = SystemProperties.get("ro.csc.sales_code");
            try {
                stkResource = this.mContext.getPackageManager().getResourcesForApplication("com.android.stk");
                appNameId = 0;
                if ("CHU".equals(salesCode)) {
                    if (MultiSimManager.getSimSlotCount() == 2) {
                        appNameId = stkResource.getIdentifier("app_name_cu", "string", "com.android.stk");
                    } else {
                        appNameId = stkResource.getIdentifier("app_name_cu_single", "string", "com.android.stk");
                    }
                }
                if (appNameId != 0) {
                    appName = stkResource.getString(appNameId);
                }
                if (appName != null) {
                    return appName;
                }
            } catch (NameNotFoundException e) {
                Log.d(TAG, "com.android.stk", e);
            }
            if (MultiSimManager.getSimSlotCount() == 1) {
                simOperator = TelephonyManager.getDefault().getSimOperator();
            } else {
                simOperator = MultiSimManager.getSimOperator(0);
            }
            if (!isZVVCSC || (isZVVCSC && ("72406".equals(simOperator) || "72410".equals(simOperator) || "72411".equals(simOperator) || "72423".equals(simOperator)))) {
                fixedStkTitle = CscFeature.getInstance().getString("CscFeature_Launcher_FixedStkTitleAs");
                if (fixedStkTitle != null && fixedStkTitle.length() > 0) {
                    if (!fixedStkTitle.startsWith("NoSIM%")) {
                        return fixedStkTitle.split(",")[0];
                    }
                    if (fixedStkTitle.startsWith("NoSIM%") && (SystemProperties.get("gsm.STK_SETUP_MENU", null) == null || SystemProperties.get("gsm.STK_SETUP_MENU", null).length() <= 0)) {
                        return fixedStkTitle.split("%")[1].split(",")[0];
                    }
                }
            }
            if (SystemProperties.get("gsm.STK_SETUP_MENU", null) != null && SystemProperties.get("gsm.STK_SETUP_MENU", null).length() > 0) {
                return SystemProperties.get("gsm.STK_SETUP_MENU", null);
            }
        }
        if ("com.android.stk2".equals(packageItemName) || "com.android.stk2.StkLauncherActivity2".equals(packageItemName) || "com.android.stk2.StkLauncherActivity".equals(packageItemName)) {
            appName = null;
            salesCode = SystemProperties.get("ro.csc.sales_code");
            try {
                stkResource = this.mContext.getPackageManager().getResourcesForApplication("com.android.stk2");
                appNameId = 0;
                if ("CHU".equals(salesCode)) {
                    if (MultiSimManager.getSimSlotCount() == 2) {
                        appNameId = stkResource.getIdentifier("app_name_cu", "string", "com.android.stk2");
                    } else {
                        appNameId = stkResource.getIdentifier("app_name_cu_single", "string", "com.android.stk2");
                    }
                }
                if (appNameId != 0) {
                    appName = stkResource.getString(appNameId);
                }
                if (appName != null) {
                    return appName;
                }
            } catch (NameNotFoundException e2) {
                Log.d(TAG, "com.android.stk2", e2);
            }
            if (MultiSimManager.getSimSlotCount() == 1) {
                simOperator = MultiSimManager.getSimOperator(0);
            } else if (MultiSimManager.getSimSlotCount() == 2) {
                simOperator = MultiSimManager.getSimOperator(1);
            }
            if (!isZVVCSC || (isZVVCSC && ("72406".equals(simOperator) || "72410".equals(simOperator) || "72411".equals(simOperator) || "72423".equals(simOperator)))) {
                fixedStkTitle = CscFeature.getInstance().getString("CscFeature_Launcher_FixedStkTitleAs");
                String[] mSplitStkTitle;
                if (SystemProperties.get("gsm.STK_SETUP_MENU2", null) != null || SystemProperties.get("gsm.STK_SETUP_MENU2", null).length() > 0) {
                    if (fixedStkTitle != null && fixedStkTitle.length() > 0) {
                        mSplitStkTitle = fixedStkTitle.split(",");
                        if (mSplitStkTitle.length > 1) {
                            return mSplitStkTitle[1];
                        }
                    }
                } else if (fixedStkTitle != null && fixedStkTitle.length() > 0) {
                    mSplitStkTitle = fixedStkTitle.split(",");
                    if (mSplitStkTitle.length > 1) {
                        return mSplitStkTitle[1].split("%")[1];
                    }
                }
            }
            if (SystemProperties.get("gsm.STK_SETUP_MENU2", null) != null && SystemProperties.get("gsm.STK_SETUP_MENU2", null).length() > 0) {
                return SystemProperties.get("gsm.STK_SETUP_MENU2", null);
            }
        }
        if ("com.sec.android.app.utk".equals(packageItemName) || "com.sec.android.app.utk.UtkLauncherActivity".equals(packageItemName)) {
            if (!("CTC".equals(SystemProperties.get("ro.csc.sales_code")) || SystemProperties.get("gsm.UTK_SETUP_MENU", null) == null || SystemProperties.get("gsm.UTK_SETUP_MENU", null).length() <= 0)) {
                return SystemProperties.get("gsm.UTK_SETUP_MENU", null);
            }
        }
        if (("com.sec.android.app.utk2".equals(packageItemName) || "com.sec.android.app.utk2.UtkLauncherActivity".equals(packageItemName)) && SystemProperties.get("gsm.UTK_SETUP_MENU2", null) != null && SystemProperties.get("gsm.UTK_SETUP_MENU2", null).length() > 0) {
            return SystemProperties.get("gsm.UTK_SETUP_MENU2", null);
        }
        ArrayMap<String, Integer> cscStringMap = this.mContext.mMainThread.getCSCAppStringMap();
        if (cscStringMap.size() > 0 && cscStringMap.containsKey(packageItemName)) {
            int resId = ((Integer) cscStringMap.get(packageItemName)).intValue();
            ResourceName resourceName = new ResourceName(packageItemName, resId);
            CharSequence text = getCachedString(resourceName);
            if (text != null) {
                return text;
            }
            try {
                Resources r = this.mContext.mMainThread.getTopLevelResources(cscAppResourcePath, null, null, null, 0, null, this.mContext.mPackageInfo);
                if (r == null) {
                    return null;
                }
                text = r.getText(resId);
                putCachedString(resourceName, text);
                return text;
            } catch (RuntimeException e3) {
                Log.w(TAG, "getCSCPackageItemText Failure retrieving text 0x" + Integer.toHexString(resId) + " in package " + packageItemName, e3);
            }
        }
        return null;
    }

    public Drawable getCSCPackageItemIcon(String packageItemName) {
        Drawable icon;
        Resources stkResource;
        int iconId;
        String simOperator = null;
        String salesCode = SystemProperties.get("ro.csc.sales_code");
        if ("ZVV".equals(salesCode)) {
            if ("com.android.stk".equals(packageItemName) || "com.android.stk.StkLauncherActivity".equals(packageItemName) || "com.android.stk.StkLauncherActivity1".equals(packageItemName)) {
                if (MultiSimManager.getSimSlotCount() == 1) {
                    simOperator = TelephonyManager.getDefault().getSimOperator();
                } else {
                    simOperator = MultiSimManager.getSimOperator(0);
                }
                if (!("72406".equals(simOperator) || "72410".equals(simOperator) || "72411".equals(simOperator) || "72423".equals(simOperator))) {
                    return null;
                }
            }
            if ("com.android.stk2".equals(packageItemName) || "com.android.stk2.StkLauncherActivity2".equals(packageItemName) || "com.android.stk2.StkLauncherActivity".equals(packageItemName)) {
                if (MultiSimManager.getSimSlotCount() == 1) {
                    simOperator = MultiSimManager.getSimOperator(0);
                } else if (MultiSimManager.getSimSlotCount() == 2) {
                    simOperator = MultiSimManager.getSimOperator(1);
                }
                if (!("72406".equals(simOperator) || "72410".equals(simOperator) || "72411".equals(simOperator) || "72423".equals(simOperator))) {
                    return null;
                }
            }
        }
        if ("com.android.stk".equals(packageItemName) || "com.android.stk.StkLauncherActivity".equals(packageItemName) || "com.android.stk.StkLauncherActivity1".equals(packageItemName)) {
            icon = null;
            try {
                stkResource = this.mContext.getPackageManager().getResourcesForApplication("com.android.stk");
                iconId = 0;
                if ("CHU".equals(salesCode)) {
                    iconId = stkResource.getIdentifier("ic_launcher_sim_toolkit_cu", "drawable", "com.android.stk");
                }
                if (iconId != 0) {
                    icon = stkResource.getDrawable(iconId);
                }
            } catch (NameNotFoundException e) {
                Log.d(TAG, "com.android.stk", e);
            }
            if (icon != null) {
                return icon;
            }
        }
        if ("com.android.stk2".equals(packageItemName) || "com.android.stk2.StkLauncherActivity2".equals(packageItemName) || "com.android.stk2.StkLauncherActivity".equals(packageItemName)) {
            icon = null;
            try {
                stkResource = this.mContext.getPackageManager().getResourcesForApplication("com.android.stk2");
                iconId = 0;
                if ("CHU".equals(salesCode)) {
                    iconId = stkResource.getIdentifier("ic_launcher_sim_toolkit_cu", "drawable", "com.android.stk2");
                }
                if (iconId != 0) {
                    icon = stkResource.getDrawable(iconId);
                }
            } catch (NameNotFoundException e2) {
                Log.d(TAG, "com.android.stk2", e2);
            }
            if (icon != null) {
                return icon;
            }
        }
        ArrayMap<String, Integer> cscIconMap = this.mContext.mMainThread.getCSCAppIconMap();
        if (cscIconMap.size() > 0 && cscIconMap.containsKey(packageItemName)) {
            int resId = ((Integer) cscIconMap.get(packageItemName)).intValue();
            ResourceName name = new ResourceName(packageItemName, resId);
            Drawable dr = getCachedIcon(name);
            if (dr != null) {
                return dr;
            }
            try {
                Resources r = this.mContext.mMainThread.getTopLevelResources(cscAppResourcePath, null, null, null, 0, null, this.mContext.mPackageInfo);
                if (r == null) {
                    return null;
                }
                dr = r.getDrawable(resId);
                putCachedIcon(name, dr);
                return dr;
            } catch (RuntimeException e3) {
                Log.w(TAG, "getCSCPackageItemIcon IconFailure retrieving icon 0x" + Integer.toHexString(resId) + " in package " + packageItemName, e3);
            }
        }
        return null;
    }

    public Drawable getThemeAppIcon(PackageItemInfo itemInfo, boolean background) {
        HashMap<String, String> mPackageIconMap = this.mContext.mMainThread.getThemeAppIconMap();
        if (!(this.appIconPackageName == null || this.appIconPackageName.isEmpty() || itemInfo == null)) {
            String overlayIcon = null;
            if (background) {
                overlayIcon = (String) mPackageIconMap.get("3rd_party_icon");
            } else if (itemInfo.name != null) {
                overlayIcon = (String) mPackageIconMap.get(itemInfo.name);
            } else if (itemInfo.packageName != null) {
                overlayIcon = (String) mPackageIconMap.get(itemInfo.packageName);
            }
            if (overlayIcon != null) {
                try {
                    Resources r = this.mContext.getPackageManager().getResourcesForApplication(this.appIconPackageName);
                    int resID = r.getIdentifier(overlayIcon, "drawable", this.appIconPackageName);
                    if (resID != 0) {
                        return r.getDrawable(resID);
                    }
                } catch (NameNotFoundException e) {
                }
            }
        }
        return null;
    }

    public Drawable getActivityBanner(ComponentName activityName) throws NameNotFoundException {
        return getActivityInfo(activityName, 1024).loadBanner(this);
    }

    public Drawable getActivityBanner(Intent intent) throws NameNotFoundException {
        if (intent.getComponent() != null) {
            return getActivityBanner(intent.getComponent());
        }
        ResolveInfo info = resolveActivity(intent, 65536);
        if (info != null) {
            return info.activityInfo.loadBanner(this);
        }
        throw new NameNotFoundException(intent.toUri(0));
    }

    public Drawable getApplicationBanner(ApplicationInfo info) {
        return info.loadBanner(this);
    }

    public Drawable getApplicationBanner(String packageName) throws NameNotFoundException {
        return getApplicationBanner(getApplicationInfo(packageName, 1024));
    }

    public Drawable getActivityLogo(ComponentName activityName) throws NameNotFoundException {
        return getActivityInfo(activityName, 1024).loadLogo(this);
    }

    public Drawable getActivityLogo(Intent intent) throws NameNotFoundException {
        if (intent.getComponent() != null) {
            return getActivityLogo(intent.getComponent());
        }
        ResolveInfo info = resolveActivity(intent, 65536);
        if (info != null) {
            return info.activityInfo.loadLogo(this);
        }
        throw new NameNotFoundException(intent.toUri(0));
    }

    public Drawable getApplicationLogo(ApplicationInfo info) {
        return info.loadLogo(this);
    }

    public Drawable getApplicationLogo(String packageName) throws NameNotFoundException {
        return getApplicationLogo(getApplicationInfo(packageName, 1024));
    }

    public Drawable getUserBadgedIcon(Drawable icon, UserHandle user) {
        int badgeResId = getBadgeResIdForUser(user.getIdentifier());
        if (badgeResId == 0) {
            return icon;
        }
        Drawable badgeIcon = getDrawable("system", badgeResId, null);
        if (user.getIdentifier() < 100) {
            int icon_width = icon.getIntrinsicWidth();
            int icon_height = icon.getIntrinsicHeight();
            int startX = icon_width / 2;
            int startY = icon_height / 2;
            if (icon_width != icon_height) {
                float shortLen = icon_width < icon_height ? (float) icon_width : (float) icon_height;
                float size = (icon_width > icon_height ? (float) icon_width : (float) icon_height) / 2.0f;
                if (shortLen > size) {
                    startX = (int) (((float) icon_width) - size);
                    startY = (int) (((float) icon_height) - size);
                } else {
                    startX = (int) (((double) icon_width) - (((double) shortLen) * 0.8d));
                    startY = (int) (((double) icon_height) - (((double) shortLen) * 0.8d));
                }
            }
            return getBadgedDrawable(icon, badgeIcon, new Rect(startX, startY, icon_width, icon_height), true);
        } else if (((PersonaManager) this.mContext.getSystemService(Context.PERSONA_SERVICE)).isKioskContainerExistOnDevice()) {
            return icon;
        } else {
            float app_icon_width;
            float app_icon_height;
            float iconwth = (float) icon.getIntrinsicWidth();
            float hiconht = (float) icon.getIntrinsicHeight();
            int badgewth = badgeIcon.getIntrinsicWidth();
            int badgeht = badgeIcon.getIntrinsicHeight();
            Resources mRes = this.mContext.getResources();
            if ("com.google.android.packageinstaller".equals(this.mContext.getPackageName())) {
                app_icon_width = iconwth;
                app_icon_height = hiconht;
            } else {
                app_icon_width = mRes.getDimension(17105546);
                app_icon_height = mRes.getDimension(17105547);
            }
            if (iconwth <= 0.0f || hiconht <= 0.0f) {
                return icon;
            }
            if (iconwth - ((float) badgewth) < 0.0f || hiconht - ((float) badgeht) < 0.0f) {
                return getBadgedDrawable(icon, badgeIcon, new Rect(((int) iconwth) / 2, ((int) hiconht) / 2, (int) iconwth, (int) hiconht), true);
            }
            if (iconwth == app_icon_width || hiconht == app_icon_height) {
                return getBadgedDrawable(icon, badgeIcon, new Rect(((int) iconwth) - badgewth, ((int) hiconht) - badgeht, (int) iconwth, (int) hiconht), true);
            }
            float large;
            if (iconwth > hiconht) {
                large = iconwth;
            } else {
                large = hiconht;
            }
            Bitmap bitmap = Bitmap.createBitmap((int) app_icon_width, (int) app_icon_height, Config.ARGB_8888);
            Canvas comboCanvas = new Canvas(bitmap);
            icon.setBounds(0, 0, (int) iconwth, (int) hiconht);
            Bitmap iTob = drawableToBitmap(icon);
            if (large > app_icon_width) {
                float reduce_ratio = app_icon_width / large;
                int newWidth = (int) (iconwth * reduce_ratio);
                int newHeight = (int) (hiconht * reduce_ratio);
                Bitmap resized_iTob = Bitmap.createScaledBitmap(iTob, newWidth, newHeight, true);
                if (((float) newWidth) >= app_icon_width && ((float) newHeight) >= app_icon_height) {
                    comboCanvas.drawBitmap(resized_iTob, 0.0f, 0.0f, null);
                } else if (((float) newWidth) < app_icon_width) {
                    comboCanvas.drawBitmap(resized_iTob, (app_icon_width / 2.0f) - ((float) (newWidth / 2)), 0.0f, null);
                } else {
                    comboCanvas.drawBitmap(resized_iTob, 0.0f, (app_icon_height / 2.0f) - ((float) (newHeight / 2)), null);
                }
            } else {
                comboCanvas.drawBitmap(Bitmap.createScaledBitmap(iTob, (int) app_icon_width, (int) app_icon_height, true), 0.0f, 0.0f, null);
            }
            comboCanvas.save();
            Drawable bitmapDrawable = new BitmapDrawable(mRes, bitmap);
            comboCanvas.restore();
            if (app_icon_width - ((float) badgewth) < 0.0f || app_icon_height - ((float) badgeht) < 0.0f) {
                return getBadgedDrawable(bitmapDrawable, badgeIcon, new Rect(((int) app_icon_width) / 2, ((int) app_icon_height) / 2, (int) app_icon_width, (int) app_icon_height), true);
            }
            int w = ((int) app_icon_width) - badgewth;
            int h = ((int) app_icon_height) - badgeht;
            return getBadgedDrawable(bitmapDrawable, badgeIcon, new Rect(((int) app_icon_width) - badgewth, ((int) app_icon_height) - badgeht, (int) app_icon_width, (int) app_icon_height), true);
        }
    }

    public Drawable getUserBadgedDrawableForDensity(Drawable drawable, UserHandle user, Rect badgeLocation, int badgeDensity) {
        Drawable badgeDrawable = getUserBadgeForDensity(user, badgeDensity);
        return badgeDrawable == null ? drawable : getBadgedDrawable(drawable, badgeDrawable, badgeLocation, true);
    }

    public Drawable getUserBadgeForDensity(UserHandle user, int density) {
        UserInfo userInfo = getUserIfProfile(user.getIdentifier());
        if (userInfo != null) {
            if (density <= 0) {
                density = this.mContext.getResources().getDisplayMetrics().densityDpi;
            }
            if (user.getIdentifier() >= 100) {
                if (userInfo.name != null && userInfo.name.equals("KNOX")) {
                    return Resources.getSystem().getDrawableForDensity(17302512, density);
                }
                if (userInfo.name != null && userInfo.name.equals("KNOX II")) {
                    return Resources.getSystem().getDrawableForDensity(17302513, density);
                }
            } else if (userInfo.isManagedProfile()) {
                return Resources.getSystem().getDrawableForDensity(17302475, density);
            }
        }
        return null;
    }

    public CharSequence getUserBadgedLabel(CharSequence label, UserHandle user) {
        UserInfo userInfo = getUserIfProfile(user.getIdentifier());
        if (userInfo == null || !userInfo.isManagedProfile()) {
            return label;
        }
        return Resources.getSystem().getString(17040758, label);
    }

    public Resources getResourcesForActivity(ComponentName activityName) throws NameNotFoundException {
        return getResourcesForApplication(getActivityInfo(activityName, 1024).applicationInfo);
    }

    public Resources getResourcesForApplication(ApplicationInfo app) throws NameNotFoundException {
        if (app.packageName.equals("system")) {
            return this.mContext.mMainThread.getSystemContext().getResources();
        }
        boolean sameUid;
        String[] strArr;
        if (app.uid == Process.myUid()) {
            sameUid = true;
        } else {
            sameUid = false;
        }
        LoadedApk pi = this.mContext.mPackageInfo;
        if (!app.packageName.equals(this.mContext.getPackageName())) {
            try {
                pi = this.mContext.mMainThread.getPackageInfo(app, this.mContext.mPackageInfo.getCompatibilityInfo(), 0);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "getResourcesForApplication using Info of " + this.mContext.getPackageName() + " instead of " + app.packageName);
            }
        }
        ActivityThread activityThread = this.mContext.mMainThread;
        String str = sameUid ? app.sourceDir : app.publicSourceDir;
        if (sameUid) {
            strArr = app.splitSourceDirs;
        } else {
            strArr = app.splitPublicSourceDirs;
        }
        Resources r = activityThread.getTopLevelResources(str, strArr, app.resourceDirs, app.sharedLibraryFiles, 0, null, pi);
        if (r != null) {
            r.mPackageName = app.packageName;
            r.mAppIconResId = app.icon;
            r.mUserId = UserHandle.getUserId(app.uid);
            return r;
        }
        throw new NameNotFoundException("Unable to open " + app.publicSourceDir);
    }

    public Resources getResourcesForApplication(String appPackageName) throws NameNotFoundException {
        return getResourcesForApplication(getApplicationInfo(appPackageName, 1024));
    }

    public Resources getResourcesForApplicationAsUser(String appPackageName, int userId) throws NameNotFoundException {
        if (userId < 0) {
            throw new IllegalArgumentException("Call does not support special user #" + userId);
        } else if ("system".equals(appPackageName)) {
            return this.mContext.mMainThread.getSystemContext().getResources();
        } else {
            try {
                ApplicationInfo ai = this.mPM.getApplicationInfo(appPackageName, 1024, userId);
                if (ai != null) {
                    return getResourcesForApplication(ai);
                }
                throw new NameNotFoundException("Package " + appPackageName + " doesn't exist");
            } catch (RemoteException e) {
                throw new RuntimeException("Package manager has died", e);
            }
        }
    }

    public boolean isSafeMode() {
        try {
            if (this.mCachedSafeMode < 0) {
                int i;
                if (this.mPM.isSafeMode()) {
                    i = 1;
                } else {
                    i = 0;
                }
                this.mCachedSafeMode = i;
            }
            if (this.mCachedSafeMode != 0) {
                return true;
            }
            return false;
        } catch (RemoteException e) {
            throw new RuntimeException("Package manager has died", e);
        }
    }

    public void addOnPermissionsChangeListener(OnPermissionsChangedListener listener) {
        synchronized (this.mPermissionListeners) {
            if (this.mPermissionListeners.get(listener) != null) {
                return;
            }
            OnPermissionsChangeListenerDelegate delegate = new OnPermissionsChangeListenerDelegate(listener, Looper.getMainLooper());
            try {
                this.mPM.addOnPermissionsChangeListener(delegate);
                this.mPermissionListeners.put(listener, delegate);
            } catch (RemoteException e) {
                throw new RuntimeException("Package manager has died", e);
            }
        }
    }

    public void removeOnPermissionsChangeListener(OnPermissionsChangedListener listener) {
        synchronized (this.mPermissionListeners) {
            IOnPermissionsChangeListener delegate = (IOnPermissionsChangeListener) this.mPermissionListeners.get(listener);
            if (delegate != null) {
                try {
                    this.mPM.removeOnPermissionsChangeListener(delegate);
                    this.mPermissionListeners.remove(listener);
                } catch (RemoteException e) {
                    throw new RuntimeException("Package manager has died", e);
                }
            }
        }
    }

    static void configurationChanged() {
        synchronized (sSync) {
            sIconCache.clear();
            sStringCache.clear();
        }
    }

    ApplicationPackageManager(ContextImpl context, IPackageManager pm) {
        this.mContext = context;
        this.mPM = pm;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private android.graphics.drawable.Drawable getCachedIcon(android.app.ApplicationPackageManager.ResourceName r5) {
        /*
        r4 = this;
        r3 = sSync;
        monitor-enter(r3);
        r2 = sIconCache;	 Catch:{ all -> 0x0023 }
        r1 = r2.get(r5);	 Catch:{ all -> 0x0023 }
        r1 = (java.lang.ref.WeakReference) r1;	 Catch:{ all -> 0x0023 }
        if (r1 == 0) goto L_0x0020;
    L_0x000d:
        r0 = r1.get();	 Catch:{ all -> 0x0023 }
        r0 = (android.graphics.drawable.Drawable.ConstantState) r0;	 Catch:{ all -> 0x0023 }
        if (r0 == 0) goto L_0x001b;
    L_0x0015:
        r2 = r0.newDrawable();	 Catch:{ all -> 0x0023 }
        monitor-exit(r3);	 Catch:{ all -> 0x0023 }
    L_0x001a:
        return r2;
    L_0x001b:
        r2 = sIconCache;	 Catch:{ all -> 0x0023 }
        r2.remove(r5);	 Catch:{ all -> 0x0023 }
    L_0x0020:
        monitor-exit(r3);	 Catch:{ all -> 0x0023 }
        r2 = 0;
        goto L_0x001a;
    L_0x0023:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0023 }
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ApplicationPackageManager.getCachedIcon(android.app.ApplicationPackageManager$ResourceName):android.graphics.drawable.Drawable");
    }

    private void putCachedIcon(ResourceName name, Drawable dr) {
        synchronized (sSync) {
            sIconCache.put(name, new WeakReference(dr.getConstantState()));
        }
    }

    static void handlePackageBroadcast(int cmd, String[] pkgList, boolean hasPkgInfo) {
        boolean immediateGc = false;
        if (cmd == 1) {
            immediateGc = true;
        }
        if (pkgList != null && pkgList.length > 0) {
            boolean needCleanup = false;
            for (String ssp : pkgList) {
                synchronized (sSync) {
                    int i;
                    for (i = sIconCache.size() - 1; i >= 0; i--) {
                        if (((ResourceName) sIconCache.keyAt(i)).packageName.equals(ssp)) {
                            sIconCache.removeAt(i);
                            needCleanup = true;
                        }
                    }
                    for (i = sStringCache.size() - 1; i >= 0; i--) {
                        if (((ResourceName) sStringCache.keyAt(i)).packageName.equals(ssp)) {
                            sStringCache.removeAt(i);
                            needCleanup = true;
                        }
                    }
                }
            }
            if (!needCleanup && !hasPkgInfo) {
                return;
            }
            if (immediateGc) {
                Runtime.getRuntime().gc();
            } else {
                ActivityThread.currentActivityThread().scheduleGcIdler();
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.CharSequence getCachedString(android.app.ApplicationPackageManager.ResourceName r5) {
        /*
        r4 = this;
        r3 = sSync;
        monitor-enter(r3);
        r2 = sStringCache;	 Catch:{ all -> 0x001f }
        r1 = r2.get(r5);	 Catch:{ all -> 0x001f }
        r1 = (java.lang.ref.WeakReference) r1;	 Catch:{ all -> 0x001f }
        if (r1 == 0) goto L_0x001c;
    L_0x000d:
        r0 = r1.get();	 Catch:{ all -> 0x001f }
        r0 = (java.lang.CharSequence) r0;	 Catch:{ all -> 0x001f }
        if (r0 == 0) goto L_0x0017;
    L_0x0015:
        monitor-exit(r3);	 Catch:{ all -> 0x001f }
    L_0x0016:
        return r0;
    L_0x0017:
        r2 = sStringCache;	 Catch:{ all -> 0x001f }
        r2.remove(r5);	 Catch:{ all -> 0x001f }
    L_0x001c:
        monitor-exit(r3);	 Catch:{ all -> 0x001f }
        r0 = 0;
        goto L_0x0016;
    L_0x001f:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x001f }
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ApplicationPackageManager.getCachedString(android.app.ApplicationPackageManager$ResourceName):java.lang.CharSequence");
    }

    private void putCachedString(ResourceName name, CharSequence cs) {
        synchronized (sSync) {
            sStringCache.put(name, new WeakReference(cs));
        }
    }

    public CharSequence getText(String packageName, int resid, ApplicationInfo appInfo) {
        ResourceName name = new ResourceName(packageName, resid);
        CharSequence text = getCachedString(name);
        if (text != null) {
            return text;
        }
        if (appInfo == null) {
            try {
                appInfo = getApplicationInfo(packageName, 1024);
            } catch (NameNotFoundException e) {
                return null;
            }
        }
        try {
            text = getResourcesForApplication(appInfo).getText(resid);
            putCachedString(name, text);
            return text;
        } catch (NameNotFoundException e2) {
            Log.w("PackageManager", "Failure retrieving resources for " + appInfo.packageName);
            return null;
        } catch (RuntimeException e3) {
            Log.w("PackageManager", "Failure retrieving text 0x" + Integer.toHexString(resid) + " in package " + packageName, e3);
            return null;
        }
    }

    public XmlResourceParser getXml(String packageName, int resid, ApplicationInfo appInfo) {
        XmlResourceParser xmlResourceParser = null;
        if (appInfo == null) {
            try {
                appInfo = getApplicationInfo(packageName, 1024);
            } catch (NameNotFoundException e) {
            }
        }
        try {
            xmlResourceParser = getResourcesForApplication(appInfo).getXml(resid);
        } catch (RuntimeException e2) {
            Log.w("PackageManager", "Failure retrieving xml 0x" + Integer.toHexString(resid) + " in package " + packageName, e2);
        } catch (NameNotFoundException e3) {
            Log.w("PackageManager", "Failure retrieving resources for " + appInfo.packageName);
        }
        return xmlResourceParser;
    }

    public CharSequence getApplicationLabel(ApplicationInfo info) {
        return info.loadLabel(this);
    }

    public void installPackage(Uri packageURI, IPackageInstallObserver observer, int flags, String installerPackageName) {
        Uri uri = packageURI;
        installCommon(uri, new LegacyPackageInstallObserver(observer), flags, installerPackageName, new VerificationParams(null, null, null, -1, null), null);
    }

    public void installPackageForMDM(String originPath, IPackageInstallObserver2 observer, int flags, int userId, String installerPkgName, VerificationParams verificationParams, String packageAbiOverride) {
        try {
            this.mPM.installPackageForMDM(originPath, observer, flags, userId, installerPkgName, verificationParams, packageAbiOverride);
        } catch (RemoteException e) {
        }
    }

    public boolean applyRuntimePermissions(String pkgName, List<String> permissions, int permState, int userId) {
        try {
            return this.mPM.applyRuntimePermissions(pkgName, permissions, permState, userId);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean applyRuntimePermissionsForAllApplications(int permState, int userId) {
        try {
            return this.mPM.applyRuntimePermissionsForAllApplications(permState, userId);
        } catch (RemoteException e) {
            return false;
        }
    }

    public String queryRuntimePermissionGroupByPermission(String permission, int flags) {
        try {
            return this.mPM.queryRuntimePermissionGroupByPermission(permission, flags);
        } catch (Exception e) {
            return null;
        }
    }

    public List<String> queryRuntimePermissionsByPermissionGroup(String permissionGroup) {
        try {
            return this.mPM.queryRuntimePermissionsByPermissionGroup(permissionGroup);
        } catch (Exception e) {
            return null;
        }
    }

    public List<String> getRequestedRuntimePermissions(String pkgName) {
        try {
            return this.mPM.getRequestedRuntimePermissions(pkgName);
        } catch (Exception e) {
            return null;
        }
    }

    public List<String> getRuntimePermissionGroups() {
        try {
            return this.mPM.getRuntimePermissionGroups();
        } catch (Exception e) {
            return null;
        }
    }

    public void installPackageWithVerification(Uri packageURI, IPackageInstallObserver observer, int flags, String installerPackageName, Uri verificationURI, ManifestDigest manifestDigest, ContainerEncryptionParams encryptionParams) {
        Uri uri = packageURI;
        installCommon(uri, new LegacyPackageInstallObserver(observer), flags, installerPackageName, new VerificationParams(verificationURI, null, null, -1, manifestDigest), encryptionParams);
    }

    public void installPackageWithVerificationAndEncryption(Uri packageURI, IPackageInstallObserver observer, int flags, String installerPackageName, VerificationParams verificationParams, ContainerEncryptionParams encryptionParams) {
        installCommon(packageURI, new LegacyPackageInstallObserver(observer), flags, installerPackageName, verificationParams, encryptionParams);
    }

    public void installPackage(Uri packageURI, PackageInstallObserver observer, int flags, String installerPackageName) {
        installCommon(packageURI, observer, flags, installerPackageName, new VerificationParams(null, null, null, -1, null), null);
    }

    public void installPackageWithVerification(Uri packageURI, PackageInstallObserver observer, int flags, String installerPackageName, Uri verificationURI, ManifestDigest manifestDigest, ContainerEncryptionParams encryptionParams) {
        installCommon(packageURI, observer, flags, installerPackageName, new VerificationParams(verificationURI, null, null, -1, manifestDigest), encryptionParams);
    }

    public void installPackageWithVerificationAndEncryption(Uri packageURI, PackageInstallObserver observer, int flags, String installerPackageName, VerificationParams verificationParams, ContainerEncryptionParams encryptionParams) {
        installCommon(packageURI, observer, flags, installerPackageName, verificationParams, encryptionParams);
    }

    private void installCommon(Uri packageURI, PackageInstallObserver observer, int flags, String installerPackageName, VerificationParams verificationParams, ContainerEncryptionParams encryptionParams) {
        if (!ContentResolver.SCHEME_FILE.equals(packageURI.getScheme())) {
            throw new UnsupportedOperationException("Only file:// URIs are supported");
        } else if (encryptionParams != null) {
            throw new UnsupportedOperationException("ContainerEncryptionParams not supported");
        } else {
            try {
                this.mPM.installPackage(packageURI.getPath(), observer.getBinder(), flags, installerPackageName, verificationParams, null);
            } catch (RemoteException e) {
            }
        }
    }

    public int installExistingPackage(String packageName) throws NameNotFoundException {
        try {
            int res = this.mPM.installExistingPackageAsUser(packageName, UserHandle.myUserId());
            if (res != -3) {
                return res;
            }
            throw new NameNotFoundException("Package " + packageName + " doesn't exist");
        } catch (RemoteException e) {
            throw new NameNotFoundException("Package " + packageName + " doesn't exist");
        }
    }

    public void verifyPendingInstall(int id, int response) {
        try {
            this.mPM.verifyPendingInstall(id, response);
        } catch (RemoteException e) {
        }
    }

    public void extendVerificationTimeout(int id, int verificationCodeAtTimeout, long millisecondsToDelay) {
        try {
            this.mPM.extendVerificationTimeout(id, verificationCodeAtTimeout, millisecondsToDelay);
        } catch (RemoteException e) {
        }
    }

    public void verifyIntentFilter(int id, int verificationCode, List<String> outFailedDomains) {
        try {
            this.mPM.verifyIntentFilter(id, verificationCode, outFailedDomains);
        } catch (RemoteException e) {
        }
    }

    public int getIntentVerificationStatus(String packageName, int userId) {
        try {
            return this.mPM.getIntentVerificationStatus(packageName, userId);
        } catch (RemoteException e) {
            return 0;
        }
    }

    public boolean updateIntentVerificationStatus(String packageName, int status, int userId) {
        try {
            return this.mPM.updateIntentVerificationStatus(packageName, status, userId);
        } catch (RemoteException e) {
            return false;
        }
    }

    public List<IntentFilterVerificationInfo> getIntentFilterVerifications(String packageName) {
        try {
            return this.mPM.getIntentFilterVerifications(packageName);
        } catch (RemoteException e) {
            return null;
        }
    }

    public List<IntentFilter> getAllIntentFilters(String packageName) {
        try {
            return this.mPM.getAllIntentFilters(packageName);
        } catch (RemoteException e) {
            return null;
        }
    }

    public String getDefaultBrowserPackageName(int userId) {
        try {
            return this.mPM.getDefaultBrowserPackageName(userId);
        } catch (RemoteException e) {
            return null;
        }
    }

    public boolean setDefaultBrowserPackageName(String packageName, int userId) {
        try {
            return this.mPM.setDefaultBrowserPackageName(packageName, userId);
        } catch (RemoteException e) {
            return false;
        }
    }

    public void setInstallerPackageName(String targetPackage, String installerPackageName) {
        try {
            this.mPM.setInstallerPackageName(targetPackage, installerPackageName);
        } catch (RemoteException e) {
        }
    }

    public String getInstallerPackageName(String packageName) {
        try {
            return this.mPM.getInstallerPackageName(packageName);
        } catch (RemoteException e) {
            return null;
        }
    }

    public int getMoveStatus(int moveId) {
        try {
            return this.mPM.getMoveStatus(moveId);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public void registerMoveCallback(MoveCallback callback, Handler handler) {
        synchronized (this.mDelegates) {
            MoveCallbackDelegate delegate = new MoveCallbackDelegate(callback, handler.getLooper());
            try {
                this.mPM.registerMoveCallback(delegate);
                this.mDelegates.add(delegate);
            } catch (RemoteException e) {
                throw e.rethrowAsRuntimeException();
            }
        }
    }

    public void unregisterMoveCallback(MoveCallback callback) {
        synchronized (this.mDelegates) {
            Iterator<MoveCallbackDelegate> i = this.mDelegates.iterator();
            while (i.hasNext()) {
                MoveCallbackDelegate delegate = (MoveCallbackDelegate) i.next();
                if (delegate.mCallback == callback) {
                    try {
                        this.mPM.unregisterMoveCallback(delegate);
                        i.remove();
                    } catch (RemoteException e) {
                        throw e.rethrowAsRuntimeException();
                    }
                }
            }
        }
    }

    public int movePackage(String packageName, VolumeInfo vol) {
        try {
            String volumeUuid;
            if (VolumeInfo.ID_PRIVATE_INTERNAL.equals(vol.id)) {
                volumeUuid = StorageManager.UUID_PRIVATE_INTERNAL;
            } else if (vol.isPrimaryPhysical()) {
                volumeUuid = StorageManager.UUID_PRIMARY_PHYSICAL;
            } else {
                volumeUuid = (String) Preconditions.checkNotNull(vol.fsUuid);
            }
            return this.mPM.movePackage(packageName, volumeUuid);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public VolumeInfo getPackageCurrentVolume(ApplicationInfo app) {
        StorageManager storage = (StorageManager) this.mContext.getSystemService(StorageManager.class);
        if (app.isInternal()) {
            return storage.findVolumeById(VolumeInfo.ID_PRIVATE_INTERNAL);
        }
        if (app.isExternalAsec()) {
            return storage.getPrimaryPhysicalVolume();
        }
        return storage.findVolumeByUuid(app.volumeUuid);
    }

    public List<VolumeInfo> getPackageCandidateVolumes(ApplicationInfo app) {
        StorageManager storage = (StorageManager) this.mContext.getSystemService(StorageManager.class);
        VolumeInfo currentVol = getPackageCurrentVolume(app);
        List<VolumeInfo> vols = storage.getVolumes();
        List<VolumeInfo> candidates = new ArrayList();
        if (currentVol != null) {
            Log.d(TAG, "getPackageCandidateVolumes, currentVol :" + currentVol.id);
        } else {
            Log.e(TAG, "getPackageCandidateVolumes, currentVol is null");
        }
        for (VolumeInfo vol : vols) {
            if (Objects.equals(vol, currentVol) || isPackageCandidateVolume(app, vol)) {
                Log.d(TAG, "add volume : " + vol.id + ", mountFlags : " + vol.mountFlags + "Type : " + vol.getType());
                if (!"privatemode".equals(vol.id)) {
                    candidates.add(vol);
                }
            }
        }
        return candidates;
    }

    private static boolean isPackageCandidateVolume(ApplicationInfo app, VolumeInfo vol) {
        if (VolumeInfo.ID_PRIVATE_INTERNAL.equals(vol.getId())) {
            return true;
        }
        if (app.isSystemApp() || app.installLocation == 1 || app.installLocation == -1) {
            if (app.installLocation != 1) {
                return false;
            }
            Log.d(TAG, "it is for only internal only.");
            return false;
        } else if (!vol.isMountedWritable()) {
            Log.d(TAG, "this volume is not mounted writable");
            return false;
        } else if (vol.isPrimaryPhysical()) {
            Log.d(TAG, "it is PrimaryPhysical, but Moving to ASEC is only for internal");
            return app.isInternal();
        } else if (vol.disk != null && !vol.disk.isSd()) {
            Log.d(TAG, "it is not sdcard, we can only move apps to sdcard");
            return false;
        } else if (vol.getType() == 1 || vol.getType() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public int movePrimaryStorage(VolumeInfo vol) {
        try {
            String volumeUuid;
            if (VolumeInfo.ID_PRIVATE_INTERNAL.equals(vol.id)) {
                volumeUuid = StorageManager.UUID_PRIVATE_INTERNAL;
            } else if (vol.isPrimaryPhysical()) {
                volumeUuid = StorageManager.UUID_PRIMARY_PHYSICAL;
            } else {
                volumeUuid = (String) Preconditions.checkNotNull(vol.fsUuid);
            }
            return this.mPM.movePrimaryStorage(volumeUuid);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public VolumeInfo getPrimaryStorageCurrentVolume() {
        StorageManager storage = (StorageManager) this.mContext.getSystemService(StorageManager.class);
        return storage.findVolumeByQualifiedUuid(storage.getPrimaryStorageUuid());
    }

    public List<VolumeInfo> getPrimaryStorageCandidateVolumes() {
        StorageManager storage = (StorageManager) this.mContext.getSystemService(StorageManager.class);
        VolumeInfo currentVol = getPrimaryStorageCurrentVolume();
        List<VolumeInfo> vols = storage.getVolumes();
        List<VolumeInfo> candidates = new ArrayList();
        if (!Objects.equals(StorageManager.UUID_PRIMARY_PHYSICAL, storage.getPrimaryStorageUuid()) || currentVol == null) {
            for (VolumeInfo vol : vols) {
                if (Objects.equals(vol, currentVol) || isPrimaryStorageCandidateVolume(vol)) {
                    candidates.add(vol);
                }
            }
        } else {
            candidates.add(currentVol);
        }
        return candidates;
    }

    private static boolean isPrimaryStorageCandidateVolume(VolumeInfo vol) {
        if (VolumeInfo.ID_PRIVATE_INTERNAL.equals(vol.getId())) {
            return true;
        }
        if (!vol.isMountedWritable()) {
            return false;
        }
        if (vol.getType() != 1) {
            return false;
        }
        return true;
    }

    public void deletePackage(String packageName, IPackageDeleteObserver observer, int flags) {
        try {
            this.mPM.deletePackageAsUser(packageName, observer, UserHandle.myUserId(), flags);
        } catch (RemoteException e) {
        }
    }

    public void clearApplicationUserData(String packageName, IPackageDataObserver observer) {
        try {
            this.mPM.clearApplicationUserData(packageName, observer, this.mContext.getUserId());
        } catch (RemoteException e) {
        }
    }

    public void deleteApplicationCacheFiles(String packageName, IPackageDataObserver observer) {
        try {
            this.mPM.deleteApplicationCacheFiles(packageName, observer);
        } catch (RemoteException e) {
        }
    }

    public void freeStorageAndNotify(String volumeUuid, long idealStorageSize, IPackageDataObserver observer) {
        try {
            this.mPM.freeStorageAndNotify(volumeUuid, idealStorageSize, observer);
        } catch (RemoteException e) {
        }
    }

    public void freeStorage(String volumeUuid, long freeStorageSize, IntentSender pi) {
        try {
            this.mPM.freeStorage(volumeUuid, freeStorageSize, pi);
        } catch (RemoteException e) {
        }
    }

    public void getPackageSizeInfo(String packageName, int userHandle, IPackageStatsObserver observer) {
        try {
            this.mPM.getPackageSizeInfo(packageName, userHandle, observer);
        } catch (RemoteException e) {
        }
    }

    public void addPackageToPreferred(String packageName) {
        try {
            this.mPM.addPackageToPreferred(packageName);
        } catch (RemoteException e) {
        }
    }

    public void removePackageFromPreferred(String packageName) {
        try {
            this.mPM.removePackageFromPreferred(packageName);
        } catch (RemoteException e) {
        }
    }

    public List<PackageInfo> getPreferredPackages(int flags) {
        try {
            return this.mPM.getPreferredPackages(flags);
        } catch (RemoteException e) {
            return new ArrayList();
        }
    }

    public void addPreferredActivity(IntentFilter filter, int match, ComponentName[] set, ComponentName activity) {
        try {
            this.mPM.addPreferredActivity(filter, match, set, activity, this.mContext.getUserId());
        } catch (RemoteException e) {
        }
    }

    public void addPreferredActivity(IntentFilter filter, int match, ComponentName[] set, ComponentName activity, int userId) {
        try {
            this.mPM.addPreferredActivity(filter, match, set, activity, userId);
        } catch (RemoteException e) {
        }
    }

    public void replacePreferredActivity(IntentFilter filter, int match, ComponentName[] set, ComponentName activity) {
        try {
            this.mPM.replacePreferredActivity(filter, match, set, activity, UserHandle.myUserId());
        } catch (RemoteException e) {
        }
    }

    public void replacePreferredActivityAsUser(IntentFilter filter, int match, ComponentName[] set, ComponentName activity, int userId) {
        try {
            this.mPM.replacePreferredActivity(filter, match, set, activity, userId);
        } catch (RemoteException e) {
        }
    }

    public void clearPackagePreferredActivities(String packageName) {
        try {
            this.mPM.clearPackagePreferredActivities(packageName);
        } catch (RemoteException e) {
        }
    }

    public int getPreferredActivities(List<IntentFilter> outFilters, List<ComponentName> outActivities, String packageName) {
        try {
            return this.mPM.getPreferredActivities(outFilters, outActivities, packageName);
        } catch (RemoteException e) {
            return 0;
        }
    }

    public ComponentName getHomeActivities(List<ResolveInfo> outActivities) {
        try {
            return this.mPM.getHomeActivities(outActivities);
        } catch (RemoteException e) {
            return null;
        }
    }

    public void setComponentEnabledSetting(ComponentName componentName, int newState, int flags) {
        try {
            this.mPM.setComponentEnabledSetting(componentName, newState, flags, this.mContext.getUserId());
        } catch (RemoteException e) {
        }
    }

    public int getComponentEnabledSetting(ComponentName componentName) {
        try {
            return this.mPM.getComponentEnabledSetting(componentName, this.mContext.getUserId());
        } catch (RemoteException e) {
            return 0;
        }
    }

    public void setApplicationEnabledSetting(String packageName, int newState, int flags) {
        try {
            this.mPM.setApplicationEnabledSetting(packageName, newState, flags, this.mContext.getUserId(), this.mContext.getOpPackageName());
        } catch (RemoteException e) {
        }
    }

    public int getApplicationEnabledSetting(String packageName) {
        try {
            return this.mPM.getApplicationEnabledSetting(packageName, this.mContext.getUserId());
        } catch (RemoteException e) {
            return 0;
        }
    }

    public boolean setApplicationHiddenSettingAsUser(String packageName, boolean hidden, UserHandle user) {
        try {
            return this.mPM.setApplicationHiddenSettingAsUser(packageName, hidden, user.getIdentifier());
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean getApplicationHiddenSettingAsUser(String packageName, UserHandle user) {
        try {
            return this.mPM.getApplicationHiddenSettingAsUser(packageName, user.getIdentifier());
        } catch (RemoteException e) {
            return false;
        }
    }

    public KeySet getKeySetByAlias(String packageName, String alias) {
        Preconditions.checkNotNull(packageName);
        Preconditions.checkNotNull(alias);
        try {
            return this.mPM.getKeySetByAlias(packageName, alias);
        } catch (RemoteException e) {
            return null;
        }
    }

    public KeySet getSigningKeySet(String packageName) {
        Preconditions.checkNotNull(packageName);
        try {
            return this.mPM.getSigningKeySet(packageName);
        } catch (RemoteException e) {
            return null;
        }
    }

    public boolean isSignedBy(String packageName, KeySet ks) {
        Preconditions.checkNotNull(packageName);
        Preconditions.checkNotNull(ks);
        try {
            return this.mPM.isPackageSignedByKeySet(packageName, ks);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean isSignedByExactly(String packageName, KeySet ks) {
        Preconditions.checkNotNull(packageName);
        Preconditions.checkNotNull(ks);
        try {
            return this.mPM.isPackageSignedByKeySetExactly(packageName, ks);
        } catch (RemoteException e) {
            return false;
        }
    }

    public VerifierDeviceIdentity getVerifierDeviceIdentity() {
        try {
            return this.mPM.getVerifierDeviceIdentity();
        } catch (RemoteException e) {
            return null;
        }
    }

    public boolean isUpgrade() {
        try {
            return this.mPM.isUpgrade();
        } catch (RemoteException e) {
            return false;
        }
    }

    public PackageInstaller getPackageInstaller() {
        PackageInstaller packageInstaller;
        synchronized (this.mLock) {
            if (this.mInstaller == null) {
                try {
                    this.mInstaller = new PackageInstaller(this.mContext, this, this.mPM.getPackageInstaller(), this.mContext.getPackageName(), this.mContext.getUserId());
                } catch (RemoteException e) {
                    throw e.rethrowAsRuntimeException();
                }
            }
            packageInstaller = this.mInstaller;
        }
        return packageInstaller;
    }

    public boolean isPackageAvailable(String packageName) {
        try {
            return this.mPM.isPackageAvailable(packageName, this.mContext.getUserId());
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    @Deprecated
    public boolean isThemeChanged(String packageName) {
        return false;
    }

    public void addCrossProfileIntentFilter(IntentFilter filter, int sourceUserId, int targetUserId, int flags) {
        try {
            this.mPM.addCrossProfileIntentFilter(filter, this.mContext.getOpPackageName(), sourceUserId, targetUserId, flags);
        } catch (RemoteException e) {
        }
    }

    public void clearCrossProfileIntentFilters(int sourceUserId) {
        try {
            this.mPM.clearCrossProfileIntentFilters(sourceUserId, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
        }
    }

    public Drawable loadItemIcon(PackageItemInfo itemInfo, ApplicationInfo appInfo) {
        return loadItemIcon(itemInfo, appInfo, false, 0);
    }

    public Drawable loadItemIcon(PackageItemInfo itemInfo, ApplicationInfo appInfo, boolean forIconContainer, int mode) {
        Drawable dr = loadUnbadgedItemIcon(itemInfo, appInfo, forIconContainer, mode);
        return itemInfo.showUserIcon != -10000 ? dr : getUserBadgedIcon(dr, new UserHandle(this.mContext.getUserId()));
    }

    public Drawable loadUnbadgedItemIcon(PackageItemInfo itemInfo, ApplicationInfo appInfo) {
        return loadUnbadgedItemIcon(itemInfo, appInfo, false, 0);
    }

    public Drawable loadUnbadgedItemIcon(PackageItemInfo itemInfo, ApplicationInfo appInfo, boolean forIconContainer, int mode) {
        int userId = 0;
        if (appInfo != null) {
            userId = UserHandle.getUserId(appInfo.uid);
        }
        boolean settingStatusForIconTray = checkSettingsForIconTray();
        Drawable dr = null;
        String themePackageName = System.getString(this.mContext.getContentResolver(), "current_sec_appicon_theme_package");
        if (ProxyInfo.LOCAL_EXCL_LIST.equals(themePackageName)) {
            themePackageName = null;
        }
        if ((this.appIconPackageName != null && !this.appIconPackageName.equals(themePackageName)) || ((themePackageName != null && !themePackageName.equals(this.appIconPackageName)) || this.openThemeAppIconRange == 3)) {
            configurationChanged();
            this.appIconPackageName = themePackageName;
            registerAppIconInfo();
        } else if (this.appIconPackageName == null && !(this.mSettingStatusChecked && this.mSettingStatusForIconTray == settingStatusForIconTray)) {
            configurationChanged();
            this.mSettingStatusChecked = true;
            this.mSettingStatusForIconTray = settingStatusForIconTray;
        }
        if (userId < 100 && this.appIconPackageName != null) {
            dr = getThemeAppIcon(itemInfo, false);
            if (dr != null && this.openThemeAppIconRange == 0) {
                dr = getThemeIconWithBG(itemInfo, dr, Boolean.valueOf(false), Boolean.valueOf(true));
            } else if (dr != null) {
                if (itemInfo.name != null) {
                    putCachedIcon(new ResourceName(itemInfo.name, dr.hashCode()), dr);
                } else {
                    putCachedIcon(new ResourceName(itemInfo.packageName, dr.hashCode()), dr);
                }
            }
            if (dr != null) {
                return dr;
            }
        }
        if (itemInfo.showUserIcon != -10000) {
            Bitmap bitmap = getUserManager().getUserIcon(itemInfo.showUserIcon);
            if (bitmap == null) {
                return UserIcons.getDefaultUserIcon(itemInfo.showUserIcon, false);
            }
            return new BitmapDrawable(this.mContext.getResources(), bitmap);
        }
        if (itemInfo.packageName != null) {
            dr = getCSCPackageItemIcon(itemInfo.icon != 0 ? itemInfo.name : itemInfo.packageName);
            if (dr == null) {
                dr = getDrawable(itemInfo.packageName, itemInfo.icon, appInfo);
            }
        }
        if (dr == null) {
            dr = itemInfo.loadDefaultIcon(this);
        }
        if (forIconContainer && this.appIconPackageName == null && dr != null && SCafeVersion != null && SCafeVersion.startsWith("2016")) {
            if (!checkComponentMetadataForIconTray(itemInfo.packageName, itemInfo.name)) {
                if (shouldPackIntoIconTray(itemInfo.packageName)) {
                    switch (mode) {
                        case 0:
                            break;
                        case 1:
                            return getThemeIconWithBG(itemInfo, dr, Boolean.valueOf(true));
                        default:
                            break;
                    }
                }
            }
            switch (mode) {
                case 1:
                    return getThemeIconWithBG(itemInfo, dr, Boolean.valueOf(true));
            }
        }
        Drawable drLiveIcon = null;
        if (dr != null && itemInfo.name == null && appInfo != null && appInfo.isSystemApp() && (CalendarContract.AUTHORITY.equals(itemInfo.packageName) || "com.sec.android.widgetapp.SPlannerAppWidget".equals(itemInfo.packageName) || "com.sec.android.app.clockpackage".equals(itemInfo.packageName) || "com.samsung.android.game.gamehome".equals(itemInfo.packageName) || "com.samsung.android.opencalendar".equals(itemInfo.packageName))) {
            drLiveIcon = getLiveIcon(itemInfo.packageName);
        }
        if (userId < 100 && drLiveIcon != null && this.openThemeAppIconRange <= 1) {
            drLiveIcon = getThemeIconWithBG(itemInfo, drLiveIcon);
        }
        if (drLiveIcon != null) {
            return drLiveIcon;
        }
        if (itemInfo.name != null && itemInfo.name.startsWith("android.permission-group")) {
            if (this.mResources == null) {
                this.mResources = this.mContext.getResources();
            }
            if (dr != null) {
                dr.setTint(this.mResources.getColor(17170644));
            }
        } else if (!(userId >= 100 || dr == null || this.appIconPackageName == null)) {
            dr = getThemeIconWithBG(itemInfo, dr);
        }
        return dr;
    }

    public boolean checkComponentMetadataForIconTray(String packageName, String componentName) {
        try {
            return this.mPM.getComponentMetadataForIconTray(packageName, componentName, "com.samsung.android.icon_container.use_icon_container", this.mContext.getUserId());
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean shouldPackIntoIconTray(String packageName) {
        try {
            return !this.mPM.getMetadataForIconTray(packageName, "com.samsung.android.icon_container.has_icon_container", this.mContext.getUserId());
        } catch (RemoteException e) {
            return true;
        }
    }

    public Drawable getDrawableForIconTray(Drawable icon, int mode) {
        switch (mode) {
            case 1:
                return getThemeIconWithBG(null, icon, Boolean.valueOf(true));
            default:
                return icon;
        }
    }

    private boolean checkSettingsForIconTray() {
        int value = Global.getInt(this.mContext.getContentResolver(), Global.TAP_TO_ICON, -1);
        if (value == 1) {
            return true;
        }
        if (value == -1 || value == 0) {
            return false;
        }
        Log.e(TAG, "checkSettingsForIconTray value : " + value);
        return true;
    }

    private void registerAppIconInfo() {
        if (this.appIconPackageName != null) {
            try {
                Resources r = this.mContext.getPackageManager().getResourcesForApplication(this.appIconPackageName);
                int resID = r.getIdentifier("icon_bg_range", "integer", this.appIconPackageName);
                if (resID != 0) {
                    this.openThemeAppIconRange = r.getInteger(resID);
                } else {
                    this.openThemeAppIconRange = 2;
                }
                resID = r.getIdentifier("icon_scale_size", "integer", this.appIconPackageName);
                if (resID != 0) {
                    this.openThemeAppIconScale = ((float) r.getInteger(resID)) * 0.01f;
                    return;
                } else {
                    this.openThemeAppIconScale = OPEN_THEME_APPICON_SCALE;
                    return;
                }
            } catch (NameNotFoundException e) {
                return;
            }
        }
        this.openThemeAppIconRange = 2;
        this.openThemeAppIconScale = OPEN_THEME_APPICON_SCALE;
    }

    private float getAppIconAlphaRelativeScale(Bitmap bm, int sizeX, int sizeY, int target, float scale) {
        int i;
        int alpha = -1;
        int smallestSide = Math.min(sizeX, sizeY) / 2;
        startPoint = new int[4][];
        startPoint[1] = new int[]{sizeX - 1, 0};
        startPoint[2] = new int[]{sizeX - 1, sizeY - 1};
        startPoint[3] = new int[]{0, sizeY - 1};
        int[] bmArray = new int[(sizeX * sizeY)];
        int[][] colorArray = (int[][]) Array.newInstance(Integer.TYPE, new int[]{sizeY, sizeX});
        int threshold = scale > 1.0f ? 26 : 0;
        bm.getPixels(bmArray, 0, sizeX, 0, 0, sizeX, sizeY);
        for (i = 0; i < sizeY; i++) {
            System.arraycopy(bmArray, sizeX * i, colorArray[i], 0, sizeX);
        }
        for (int count = 0; smallestSide > count && alpha == -1; count++) {
            for (i = 0; i < 4; i++) {
                int x = startPoint[i][0];
                int y = startPoint[i][1];
                int endPoint = 0;
                if (i != 3) {
                    endPoint = i + 1;
                }
                boolean completed = false;
                while (!completed) {
                    if (x == startPoint[endPoint][0] && y == startPoint[endPoint][1]) {
                        completed = true;
                    }
                    if ((colorArray[y][x] >>> 24) > threshold) {
                        alpha = count;
                        break;
                    }
                    x += progress[i][0];
                    y += progress[i][1];
                }
                if (alpha != -1) {
                    break;
                }
            }
            for (i = 0; i < 4; i++) {
                int[] iArr = startPoint[i];
                iArr[0] = iArr[0] + movePoint[i][0];
                iArr = startPoint[i];
                iArr[1] = iArr[1] + movePoint[i][1];
            }
        }
        if (alpha == -1) {
            alpha = 0;
        }
        if (scale > 1.0f) {
            int detectedX = (startPoint[1][0] - startPoint[0][0]) + 1;
            int detectedY = (startPoint[3][1] - startPoint[0][1]) + 1;
            int judgeCount = 0;
            for (i = 0; i < 8; i++) {
                if ((colorArray[((judgePoint[i][1] * detectedY) / 192) + startPoint[0][1]][((judgePoint[i][0] * detectedX) / 192) + startPoint[0][0]] >>> 24) != 0) {
                    judgeCount++;
                }
            }
            scale = judgeCount >= 7 ? scale == 1.1f ? 0.88f : 0.68f : scale == 1.1f ? 0.94f : DEFAULT_THEME_APPICON_SCALE;
        }
        float relativeScale = (((float) target) * scale) / ((float) (Math.max(sizeX, sizeY) - (alpha * 2)));
        Log.i(TAG, "scaled rate=" + String.valueOf(relativeScale) + ", size=" + Math.max(sizeX, sizeY) + ", alpha=" + alpha + ", hold=" + threshold + ", target=" + target);
        return relativeScale;
    }

    private Drawable getThemeIconWithBG(PackageItemInfo itemInfo, Drawable dr) {
        return getThemeIconWithBG(itemInfo, dr, Boolean.valueOf(false));
    }

    private Drawable getThemeIconWithBG(PackageItemInfo itemInfo, Drawable dr, Boolean forDefaultContainer) {
        return getThemeIconWithBG(itemInfo, dr, forDefaultContainer, Boolean.valueOf(false));
    }

    private Drawable getThemeIconWithBG(PackageItemInfo itemInfo, Drawable dr, Boolean forDefaultContainer, Boolean fromThemePackage) {
        Drawable bg;
        String pkgname = WifiEnterpriseConfig.EMPTY_VALUE;
        if (itemInfo != null) {
            pkgname = itemInfo.packageName;
        }
        if (itemInfo != null) {
            if (itemInfo.name != null) {
                bg = getCachedIcon(new ResourceName(itemInfo.name, dr.hashCode()));
                if (bg != null) {
                    return bg;
                }
            }
            bg = getCachedIcon(new ResourceName(itemInfo.packageName, dr.hashCode()));
            if (bg != null) {
                return bg;
            }
        }
        if (itemInfo == null || forDefaultContainer.booleanValue() || (this.appIconPackageName != null && this.openThemeAppIconRange > 1)) {
            bg = Resources.getSystem().getDrawable(17302423);
        } else {
            bg = getThemeAppIcon(itemInfo, true);
        }
        boolean noBG = false;
        if (bg == null && this.appIconPackageName != null && this.openThemeAppIconRange < 2) {
            noBG = true;
            bg = Resources.getSystem().getDrawable(17302423);
        }
        if (bg != null) {
            int bgWidth = bg.getIntrinsicWidth();
            int bgHeight = bg.getIntrinsicHeight();
            int drWidth = dr.getIntrinsicWidth();
            int drHeight = dr.getIntrinsicHeight();
            if (bgWidth <= 0 || bgHeight <= 0 || drWidth <= 0 || drHeight <= 0) {
                return dr;
            }
            Bitmap bgBitmap;
            Canvas canvas;
            Bitmap drBitmap;
            Log.i(TAG, "load=" + pkgname + ", bg=" + bgWidth + "-" + bgHeight + ", dr=" + drWidth + "-" + drHeight);
            int maxDr = Math.max(drWidth, drHeight);
            if (itemInfo == null && bgWidth < 144 && bgWidth < maxDr) {
                int before = bgWidth;
                bgHeight = maxDr < 192 ? maxDr : 192;
                bgWidth = bgHeight;
                bgBitmap = Bitmap.createScaledBitmap(((BitmapDrawable) Resources.getSystem().getDrawable(17302424)).getBitmap(), bgWidth, bgHeight, true);
                Log.i(TAG, "bg rescaling before=" + before + ", after=" + bgWidth + ", dr=" + maxDr);
            } else if (bg instanceof BitmapDrawable) {
                bgBitmap = ((BitmapDrawable) bg).getBitmap();
            } else {
                bgBitmap = Bitmap.createBitmap(bgWidth, bgHeight, Config.ARGB_8888);
                canvas = new Canvas(bgBitmap);
                bg.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                bg.draw(canvas);
            }
            if (((double) bgWidth) * 1.5d < ((double) maxDr)) {
                float scaleX = ((float) bgWidth) / ((float) maxDr);
                int sizeX = (int) (((float) drWidth) * scaleX);
                int sizeY = (int) (((float) drHeight) * scaleX);
                if (dr instanceof BitmapDrawable) {
                    drBitmap = Bitmap.createScaledBitmap(((BitmapDrawable) dr).getBitmap(), sizeX, sizeY, true);
                } else {
                    Bitmap tempBitmap = Bitmap.createBitmap(drWidth, drHeight, Config.ARGB_8888);
                    canvas = new Canvas(tempBitmap);
                    dr.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                    dr.draw(canvas);
                    drBitmap = Bitmap.createScaledBitmap(tempBitmap, sizeX, sizeY, true);
                }
                Log.i(TAG, "DR-icon scaling ori=" + drWidth + "," + drHeight + " - scaled=" + sizeX + "," + sizeY);
                drWidth = sizeX;
                drHeight = sizeY;
            } else if (dr instanceof BitmapDrawable) {
                drBitmap = ((BitmapDrawable) dr).getBitmap();
            } else {
                drBitmap = Bitmap.createBitmap(drWidth, drHeight, Config.ARGB_8888);
                canvas = new Canvas(drBitmap);
                dr.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                dr.draw(canvas);
            }
            if (this.mPaint == null) {
                this.mPaint = new Paint();
                this.mPaint.setAntiAlias(true);
                this.mPaint.setFilterBitmap(true);
                this.mPaint.setDither(false);
            }
            Bitmap b = Bitmap.createBitmap(bgWidth, bgHeight, Config.ARGB_8888);
            canvas = new Canvas(b);
            canvas.save();
            boolean settingStatusForIconTray = checkSettingsForIconTray();
            if (!(this.appIconPackageName == null || this.openThemeAppIconRange >= 2 || noBG) || (forDefaultContainer.booleanValue() && settingStatusForIconTray)) {
                canvas.drawBitmap(bgBitmap, 0.0f, 0.0f, this.mPaint);
            }
            if (forDefaultContainer.booleanValue()) {
                float relativeScale;
                if (settingStatusForIconTray) {
                    canvas.translate(((float) bgWidth) / 2.0f, (((float) bgHeight) / 2.0f) - (((float) bgHeight) * 0.02f));
                    relativeScale = getAppIconAlphaRelativeScale(drBitmap, drWidth, drHeight, bgWidth, 1.2f);
                } else {
                    canvas.translate(((float) bgWidth) / 2.0f, ((float) bgHeight) / 2.0f);
                    relativeScale = getAppIconAlphaRelativeScale(drBitmap, drWidth, drHeight, bgWidth, 1.1f);
                }
                canvas.scale(relativeScale, relativeScale);
            } else {
                float relativeScaleX;
                float relativeScaleY;
                canvas.translate(((float) bgWidth) / 2.0f, ((float) bgHeight) / 2.0f);
                if (fromThemePackage.booleanValue()) {
                    relativeScaleX = (((float) bgWidth) * this.openThemeAppIconScale) / ((float) drWidth);
                    relativeScaleY = (((float) bgHeight) * this.openThemeAppIconScale) / ((float) drHeight);
                } else if (this.openThemeAppIconRange == 2 || noBG) {
                    relativeScaleX = getAppIconAlphaRelativeScale(drBitmap, drWidth, drHeight, bgWidth, 1.1f);
                    relativeScaleY = relativeScaleX;
                } else {
                    relativeScaleX = getAppIconAlphaRelativeScale(drBitmap, drWidth, drHeight, bgWidth, this.openThemeAppIconScale);
                    relativeScaleY = relativeScaleX;
                }
                canvas.scale(relativeScaleX, relativeScaleY);
            }
            canvas.drawBitmap(drBitmap, ((float) (-drWidth)) / 2.0f, ((float) (-drHeight)) / 2.0f, this.mPaint);
            canvas.restore();
            Drawable bitmapDrawable = new BitmapDrawable(this.mContext.getResources(), b);
            if (itemInfo != null) {
                if (itemInfo.name != null) {
                    putCachedIcon(new ResourceName(itemInfo.name, bitmapDrawable.hashCode()), bitmapDrawable);
                } else {
                    putCachedIcon(new ResourceName(itemInfo.packageName, bitmapDrawable.hashCode()), bitmapDrawable);
                }
            }
        }
        return dr;
    }

    private Drawable getLiveIconClock() {
        Resources mRes = this.mContext.getResources();
        if (this.mBgClock == null) {
            this.mBgClock = BitmapFactory.decodeResource(mRes, 17302160);
        }
        if (this.mHour == null) {
            this.mHour = BitmapFactory.decodeResource(mRes, 17302161);
        }
        if (this.mMin == null) {
            this.mMin = BitmapFactory.decodeResource(mRes, 17302162);
        }
        if (this.mSec == null) {
            this.mSec = BitmapFactory.decodeResource(mRes, 17302163);
        }
        int iconW = this.mBgClock.getWidth();
        int iconH = this.mBgClock.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(iconW, iconH, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable liveIcon = new BitmapDrawable(mRes, bitmap);
        liveIcon.setBounds(0, 0, iconW, iconH);
        liveIcon.draw(canvas);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(11);
        int min = cal.get(12);
        int sec = cal.get(13);
        canvas.drawBitmap(this.mBgClock, 0.0f, 0.0f, paint);
        Matrix matrixHour = new Matrix();
        matrixHour.setRotate((float) ((hour * 30) + (min / 2)), (float) (this.mHour.getWidth() / 2), (float) (this.mHour.getHeight() / 2));
        canvas.drawBitmap(this.mHour, matrixHour, paint);
        Matrix matrixMin = new Matrix();
        matrixMin.setRotate((float) (min * 6), (float) (this.mMin.getWidth() / 2), (float) (this.mMin.getHeight() / 2));
        canvas.drawBitmap(this.mMin, matrixMin, paint);
        Matrix matrixSec = new Matrix();
        matrixSec.setRotate((float) (sec * 6), (float) (this.mSec.getWidth() / 2), (float) (this.mSec.getHeight() / 2));
        canvas.drawBitmap(this.mSec, matrixSec, paint);
        return liveIcon;
    }

    private String getDayOfWeekString(int i) {
        String[] dayNames;
        Resources mRes = this.mContext.getResources();
        DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
        if (mRes.getConfiguration().locale.getLanguage().equals("ko") || mRes.getConfiguration().locale.getLanguage().equals("zh") || mRes.getConfiguration().locale.getLanguage().equals("ja") || mRes.getConfiguration().locale.getLanguage().equals("ar")) {
            dayNames = symbols.getWeekdays();
        } else {
            dayNames = symbols.getShortWeekdays();
        }
        return dayNames[i].toUpperCase();
    }

    private static String toArabicDigits(String eng, String lang) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < eng.length(); i++) {
            if (!Character.isDigit(eng.charAt(i))) {
                builder.append(eng.charAt(i));
            } else if (lang.equals("ar")) {
                builder.append(arabicNumberArray[eng.charAt(i) - 48]);
            } else if (lang.equals("fa")) {
                builder.append(farsiNumberArray[eng.charAt(i) - 48]);
            }
        }
        return builder.toString();
    }

    private Drawable getLiveIconCalendar() {
        Resources mRes = this.mContext.getResources();
        if (this.mBgCalendar == null) {
            this.mBgCalendar = BitmapFactory.decodeResource(mRes, 17303260);
        }
        if (this.mColor == -1) {
            this.mColor = mRes.getColor(17170809);
        }
        if (this.mSizeDay == -1) {
            this.mSizeDay = mRes.getDimensionPixelSize(17105548);
        }
        if (this.mSizeDate == -1) {
            this.mSizeDate = mRes.getDimensionPixelSize(17105549);
        }
        if (this.mHeightMargin1 == -1) {
            this.mHeightMargin1 = mRes.getDimensionPixelSize(17105550);
        }
        if (this.mHeightDay == -1) {
            this.mHeightDay = mRes.getDimensionPixelSize(17105551);
        }
        if (this.mHeightMargin2 == -1) {
            this.mHeightMargin2 = mRes.getDimensionPixelSize(17105552);
        }
        if (this.mHeightDate == -1) {
            this.mHeightDate = mRes.getDimensionPixelSize(17105553);
        }
        if (this.mFontDay == null) {
            if (mRes.getDisplayMetrics().densityDpi == 160) {
                this.mFontDay = Typeface.create("sec-roboto-regular", 0);
            } else if (mRes.getDisplayMetrics().densityDpi == 640) {
                this.mFontDay = Typeface.create("sec-roboto-light-bold", 0);
            } else {
                this.mFontDay = Typeface.create("sec-roboto-light", 1);
            }
        }
        try {
            if (this.mFontDate == null) {
                this.mFontDate = Typeface.createFromFile("/system/fonts/SamsungNeoNum-3R.ttf");
            }
        } catch (RuntimeException e) {
            e.getMessage();
            Log.v(TAG, "System font is not enable.");
        }
        int iconW = this.mBgCalendar.getWidth();
        int iconH = this.mBgCalendar.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(iconW, iconH, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable liveIcon = new BitmapDrawable(mRes, bitmap);
        liveIcon.setBounds(0, 0, iconW, iconH);
        liveIcon.draw(canvas);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setColor(this.mColor);
        paint.setTextAlign(Align.CENTER);
        Calendar cal = Calendar.getInstance();
        canvas.drawBitmap(this.mBgCalendar, 0.0f, 0.0f, paint);
        String strDay = getDayOfWeekString(cal.get(7));
        paint.setTypeface(this.mFontDay);
        paint.setTextSize((float) this.mSizeDay);
        canvas.drawText(strDay, (float) (iconW / 2), (float) (this.mHeightMargin1 + this.mHeightDay), paint);
        String strDate = Integer.toString(cal.get(5));
        if (mRes.getConfiguration().locale.getLanguage().equals("ar") || mRes.getConfiguration().locale.getLanguage().equals("fa")) {
            strDate = toArabicDigits(strDate, mRes.getConfiguration().locale.getLanguage());
        }
        paint.setTypeface(Typeface.create(this.mFontDate, 0));
        paint.setTextSize((float) this.mSizeDate);
        canvas.drawText(strDate, (float) (iconW / 2), (float) (((this.mHeightMargin1 + this.mHeightDay) + this.mHeightMargin2) + this.mHeightDate), paint);
        return liveIcon;
    }

    private Drawable getBadgedDrawable(Drawable drawable, Drawable badgeDrawable, Rect badgeLocation, boolean tryBadgeInPlace) {
        boolean canBadgeInPlace;
        Bitmap bitmap;
        int badgedWidth = drawable.getIntrinsicWidth();
        int badgedHeight = drawable.getIntrinsicHeight();
        if (tryBadgeInPlace && (drawable instanceof BitmapDrawable) && ((BitmapDrawable) drawable).getBitmap().isMutable()) {
            canBadgeInPlace = true;
        } else {
            canBadgeInPlace = false;
        }
        if (canBadgeInPlace) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        } else {
            bitmap = Bitmap.createBitmap(badgedWidth, badgedHeight, Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(bitmap);
        if (!canBadgeInPlace) {
            drawable.setBounds(0, 0, badgedWidth, badgedHeight);
            drawable.draw(canvas);
        }
        if (badgeLocation == null) {
            badgeDrawable.setBounds(0, 0, badgedWidth, badgedHeight);
            badgeDrawable.draw(canvas);
        } else if (badgeLocation.left < 0 || badgeLocation.top < 0 || badgeLocation.width() > badgedWidth || badgeLocation.height() > badgedHeight) {
            throw new IllegalArgumentException("Badge location " + badgeLocation + " not in badged drawable bounds " + new Rect(0, 0, badgedWidth, badgedHeight));
        } else {
            badgeDrawable.setBounds(0, 0, badgeLocation.width(), badgeLocation.height());
            canvas.save();
            canvas.translate((float) badgeLocation.left, (float) badgeLocation.top);
            badgeDrawable.draw(canvas);
            canvas.restore();
        }
        if (canBadgeInPlace) {
            return drawable;
        }
        BitmapDrawable mergedDrawable = new BitmapDrawable(this.mContext.getResources(), bitmap);
        if (!(drawable instanceof BitmapDrawable)) {
            return mergedDrawable;
        }
        mergedDrawable.setTargetDensity(((BitmapDrawable) drawable).getBitmap().getDensity());
        return mergedDrawable;
    }

    private int getBadgeResIdForUser(int userHandle) {
        UserInfo userInfo = getUserIfProfile(userHandle);
        if (userInfo == null) {
            return 0;
        }
        if (userHandle >= 100) {
            ApplicationPolicy appPolicy = EnterpriseDeviceManager.getInstance().getApplicationPolicy();
            if (appPolicy != null && appPolicy.getAddHomeShorcutRequested()) {
                return 0;
            }
            if (userInfo.name != null && userInfo.name.equals("KNOX")) {
                return 17302512;
            }
            if (userInfo.name == null || !userInfo.name.equals("KNOX II")) {
                return 0;
            }
            return 17302513;
        } else if (userInfo.isManagedProfile()) {
            return 17302475;
        } else {
            return 0;
        }
    }

    private UserInfo getUserIfProfile(int userHandle) {
        for (UserInfo user : getUserManager().getProfiles(UserHandle.myUserId())) {
            if (user.id == userHandle) {
                return user;
            }
        }
        return null;
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public void setApplicationEnabledSettingWithList(List<String> listPackageName, int newState, int flags, boolean usePending, boolean startNow) {
        try {
            this.mPM.setApplicationEnabledSettingWithList(listPackageName, newState, flags, usePending, startNow, this.mContext.getUserId(), this.mContext.getBasePackageName());
        } catch (RemoteException e) {
        }
    }

    public void applyOverlays(List<String> disablePkgsList, List<String> enablePkgsList, IOverlayCallback callback, boolean resetSetting) {
        try {
            this.mPM.applyOverlays(disablePkgsList, enablePkgsList, callback, resetSetting);
        } catch (RemoteException e) {
        }
    }

    public int getProgressionOfPackageChanged() {
        try {
            return this.mPM.getProgressionOfPackageChanged();
        } catch (RemoteException e) {
            return -1;
        }
    }

    public void cancelEMPHandlerSendPendingBroadcast() {
        try {
            this.mPM.cancelEMPHandlerSendPendingBroadcast();
        } catch (RemoteException e) {
        }
    }

    public Drawable getCustomBadgedIcon(Drawable icon, UserHandle user, int badgeResourceId, int position) {
        Drawable badgeIcon = getDrawable("system", badgeResourceId, null);
        int icon_width = icon.getIntrinsicWidth();
        int icon_height = icon.getIntrinsicHeight();
        int badgeicon_width = badgeIcon.getIntrinsicWidth();
        int badgeicon_height = badgeIcon.getIntrinsicHeight();
        if (icon_width - badgeicon_width < 0 || icon_height - badgeicon_height < 0) {
            Bitmap bitmap = Bitmap.createBitmap(badgeicon_width, badgeicon_height, Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
            icon.draw(canvas);
            Drawable resized_icon = new BitmapDrawable(this.mContext.getResources(), Bitmap.createScaledBitmap(bitmap, badgeicon_width, badgeicon_height, true));
            if (position == 3) {
                return getBadgedDrawable(resized_icon, badgeIcon, new Rect(badgeicon_width / 2, badgeicon_height / 2, badgeicon_width, badgeicon_height), true);
            }
            return getBadgedDrawable(resized_icon, badgeIcon, new Rect(0, badgeicon_height / 2, badgeicon_width / 2, badgeicon_height), true);
        } else if (position == 3) {
            return getBadgedDrawable(icon, badgeIcon, new Rect(icon_width / 2, icon_height / 2, icon_width, icon_height), true);
        } else {
            return getBadgedDrawable(icon, badgeIcon, new Rect(0, icon_height / 2, icon_width / 2, icon_height), true);
        }
    }
}
