package android.app;

import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.ResourcesKey;
import android.hardware.display.DisplayManagerGlobal;
import android.os.PersonaManager;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.util.Slog;
import android.view.Display;
import android.view.DisplayAdjustments;
import android.view.DisplayInfo;
import com.samsung.android.multidisplay.common.UnRestrictedArrayList;
import com.samsung.android.multiwindow.MultiWindowFeatures;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class ResourcesManager {
    private static final boolean DEBUG = false;
    static final String TAG = "ResourcesManager";
    private static ResourcesManager sResourcesManager;
    private final boolean DEBUG_ELASTIC = false;
    private HashMap<String, Integer> cookieMap = new HashMap();
    private final boolean isElasticEnabled = true;
    private final ArrayMap<ResourcesKey, WeakReference<Resources>> mActiveResources = new ArrayMap();
    private final ArrayMap<Pair<Integer, DisplayAdjustments>, WeakReference<Display>> mDisplays = new ArrayMap();
    private AssetManager mElasticAssets = null;
    CompatibilityInfo mResCompatibilityInfo;
    Configuration mResConfiguration;
    UnRestrictedArrayList<Configuration> mResConfigurations;

    public static ResourcesManager getInstance() {
        ResourcesManager resourcesManager;
        synchronized (ResourcesManager.class) {
            if (sResourcesManager == null) {
                sResourcesManager = new ResourcesManager();
            }
            resourcesManager = sResourcesManager;
        }
        return resourcesManager;
    }

    public Configuration getConfiguration() {
        return this.mResConfiguration;
    }

    public Configuration getConfiguration(int displayId) {
        return this.mResConfiguration;
    }

    DisplayMetrics getDisplayMetricsLocked() {
        return getDisplayMetricsLocked(0);
    }

    DisplayMetrics getDisplayMetricsLocked(int displayId) {
        DisplayMetrics dm = new DisplayMetrics();
        Display display = getAdjustedDisplay(displayId, DisplayAdjustments.DEFAULT_DISPLAY_ADJUSTMENTS);
        if (display != null) {
            display.getMetrics(dm, true);
        } else {
            dm.setToDefaults();
        }
        return dm;
    }

    final void applyNonDefaultDisplayMetricsToConfigurationLocked(DisplayMetrics dm, Configuration config) {
        config.touchscreen = 1;
        config.densityDpi = dm.densityDpi;
        config.screenWidthDp = (int) (((float) dm.widthPixels) / dm.density);
        config.screenHeightDp = (int) (((float) dm.heightPixels) / dm.density);
        int sl = Configuration.resetScreenLayout(config.screenLayout);
        if (dm.widthPixels > dm.heightPixels) {
            config.orientation = 2;
            config.screenLayout = Configuration.reduceScreenLayout(sl, config.screenWidthDp, config.screenHeightDp);
        } else {
            config.orientation = 1;
            config.screenLayout = Configuration.reduceScreenLayout(sl, config.screenHeightDp, config.screenWidthDp);
        }
        config.smallestScreenWidthDp = config.screenWidthDp;
        config.compatScreenWidthDp = config.screenWidthDp;
        config.compatScreenHeightDp = config.screenHeightDp;
        config.compatSmallestScreenWidthDp = config.smallestScreenWidthDp;
    }

    public boolean applyCompatConfiguration(int displayDensity, Configuration compatConfiguration) {
        if (this.mResCompatibilityInfo == null || this.mResCompatibilityInfo.supportsScreen()) {
            return false;
        }
        this.mResCompatibilityInfo.applyToConfiguration(displayDensity, compatConfiguration);
        return true;
    }

    public Display getAdjustedDisplay(int displayId, DisplayAdjustments displayAdjustments) {
        DisplayAdjustments displayAdjustmentsCopy;
        Display display;
        if (displayAdjustments != null) {
            displayAdjustmentsCopy = new DisplayAdjustments(displayAdjustments);
        } else {
            displayAdjustmentsCopy = new DisplayAdjustments();
        }
        Pair<Integer, DisplayAdjustments> key = Pair.create(Integer.valueOf(displayId), displayAdjustmentsCopy);
        synchronized (this) {
            WeakReference<Display> wd = (WeakReference) this.mDisplays.get(key);
            if (wd != null) {
                display = (Display) wd.get();
                if (display != null) {
                }
            }
            DisplayManagerGlobal dm = DisplayManagerGlobal.getInstance();
            if (dm == null) {
                display = null;
            } else {
                display = dm.getCompatibleDisplay(displayId, (DisplayAdjustments) key.second);
                if (display != null) {
                    this.mDisplays.put(key, new WeakReference(display));
                }
            }
        }
        return display;
    }

    public Resources getTopLevelResources(String resDir, String[] splitResDirs, String[] overlayDirs, String[] libDirs, int displayId, Configuration overrideConfiguration, CompatibilityInfo compatInfo) {
        return getTopLevelResources(resDir, splitResDirs, overlayDirs, libDirs, displayId, overrideConfiguration, compatInfo, null);
    }

    Resources getTopLevelResources(String resDir, String[] splitResDirs, String[] overlayDirs, String[] libDirs, int displayId, Configuration overrideConfiguration, CompatibilityInfo compatInfo, String packageName) {
        return getTopLevelResources(resDir, splitResDirs, overlayDirs, libDirs, displayId, overrideConfiguration, compatInfo, packageName, UserHandle.myUserId());
    }

    public Resources getTopLevelResources(String resDir, String[] splitResDirs, String[] overlayDirs, String[] libDirs, int displayId, Configuration overrideConfiguration, CompatibilityInfo compatInfo, String packageName, int userId) {
        return getTopLevelResources(resDir, splitResDirs, overlayDirs, libDirs, displayId, overrideConfiguration, compatInfo, packageName, userId, null);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.content.res.Resources getTopLevelResources(java.lang.String r38, java.lang.String[] r39, java.lang.String[] r40, java.lang.String[] r41, int r42, android.content.res.Configuration r43, android.content.res.CompatibilityInfo r44, java.lang.String r45, int r46, android.os.IBinder r47) {
        /*
        r37 = this;
        r0 = r44;
        r7 = r0.applicationScale;
        if (r43 == 0) goto L_0x007f;
    L_0x0006:
        r25 = new android.content.res.Configuration;
        r0 = r25;
        r1 = r43;
        r0.<init>(r1);
    L_0x000f:
        r3 = new android.content.res.ResourcesKey;
        r4 = r38;
        r5 = r42;
        r6 = r43;
        r8 = r46;
        r3.<init>(r4, r5, r6, r7, r8);
        monitor-enter(r37);
        r4 = "ResourcesManager";
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00c0 }
        r5.<init>();	 Catch:{ all -> 0x00c0 }
        r6 = "getTopLevelResources: ";
        r5 = r5.append(r6);	 Catch:{ all -> 0x00c0 }
        r0 = r38;
        r5 = r5.append(r0);	 Catch:{ all -> 0x00c0 }
        r6 = " / ";
        r5 = r5.append(r6);	 Catch:{ all -> 0x00c0 }
        r5 = r5.append(r7);	 Catch:{ all -> 0x00c0 }
        r6 = " running in ";
        r5 = r5.append(r6);	 Catch:{ all -> 0x00c0 }
        r6 = android.app.ActivityThread.currentPackageName();	 Catch:{ all -> 0x00c0 }
        r5 = r5.append(r6);	 Catch:{ all -> 0x00c0 }
        r6 = " rsrc of package ";
        r5 = r5.append(r6);	 Catch:{ all -> 0x00c0 }
        r0 = r45;
        r5 = r5.append(r0);	 Catch:{ all -> 0x00c0 }
        r5 = r5.toString();	 Catch:{ all -> 0x00c0 }
        android.util.Slog.w(r4, r5);	 Catch:{ all -> 0x00c0 }
        r0 = r37;
        r4 = r0.mActiveResources;	 Catch:{ all -> 0x00c0 }
        r35 = r4.get(r3);	 Catch:{ all -> 0x00c0 }
        r35 = (java.lang.ref.WeakReference) r35;	 Catch:{ all -> 0x00c0 }
        if (r35 == 0) goto L_0x0082;
    L_0x0067:
        r4 = r35.get();	 Catch:{ all -> 0x00c0 }
        r4 = (android.content.res.Resources) r4;	 Catch:{ all -> 0x00c0 }
        r32 = r4;
    L_0x006f:
        if (r32 == 0) goto L_0x0085;
    L_0x0071:
        r4 = r32.getAssets();	 Catch:{ all -> 0x00c0 }
        r4 = r4.isUpToDate();	 Catch:{ all -> 0x00c0 }
        if (r4 == 0) goto L_0x0085;
    L_0x007b:
        monitor-exit(r37);	 Catch:{ all -> 0x00c0 }
        r16 = r32;
    L_0x007e:
        return r16;
    L_0x007f:
        r25 = 0;
        goto L_0x000f;
    L_0x0082:
        r32 = 0;
        goto L_0x006f;
    L_0x0085:
        monitor-exit(r37);	 Catch:{ all -> 0x00c0 }
        r10 = 0;
        r11 = 0;
        r17 = 0;
        if (r45 == 0) goto L_0x00c3;
    L_0x008c:
        r17 = android.app.im.InjectionManager.getClassLibPath(r45);
    L_0x0090:
        if (r17 == 0) goto L_0x009f;
    L_0x0092:
        r4 = 0;
        r4 = java.lang.Integer.valueOf(r4);
        r0 = r17;
        r11 = r0.get(r4);
        r11 = (java.lang.String) r11;
    L_0x009f:
        if (r11 == 0) goto L_0x0127;
    L_0x00a1:
        r4 = "";
        r4 = r11.equals(r4);
        if (r4 != 0) goto L_0x0127;
    L_0x00a9:
        r0 = r38;
        r4 = r11.contains(r0);
        if (r4 == 0) goto L_0x0127;
    L_0x00b1:
        r0 = r37;
        r4 = r0.mElasticAssets;
        if (r4 == 0) goto L_0x00c6;
    L_0x00b7:
        r0 = r37;
        r10 = r0.mElasticAssets;
    L_0x00bb:
        if (r10 != 0) goto L_0x013a;
    L_0x00bd:
        r16 = 0;
        goto L_0x007e;
    L_0x00c0:
        r4 = move-exception;
        monitor-exit(r37);	 Catch:{ all -> 0x00c0 }
        throw r4;
    L_0x00c3:
        r17 = 0;
        goto L_0x0090;
    L_0x00c6:
        r4 = "#";
        r28 = r11.split(r4);
        r4 = 0;
        r36 = r28[r4];
        r4 = 1;
        r30 = r28[r4];
        r31 = 0;
        if (r30 == 0) goto L_0x00de;
    L_0x00d6:
        r4 = ":";
        r0 = r30;
        r31 = r0.split(r4);
    L_0x00de:
        if (r36 == 0) goto L_0x011e;
    L_0x00e0:
        if (r31 == 0) goto L_0x011e;
    L_0x00e2:
        r4 = ":";
        r0 = r36;
        r33 = r0.split(r4);
        r4 = new android.content.res.AssetManager;
        r4.<init>();
        r0 = r37;
        r0.mElasticAssets = r4;
        r18 = 0;
    L_0x00f5:
        r0 = r33;
        r4 = r0.length;
        r0 = r18;
        if (r0 >= r4) goto L_0x0119;
    L_0x00fc:
        r0 = r37;
        r4 = r0.mElasticAssets;
        r5 = r33[r18];
        r0 = r18;
        r13 = r4.addAssetPath(r5, r0);
        r0 = r37;
        r4 = r0.cookieMap;
        r5 = r31[r18];
        r6 = new java.lang.Integer;
        r6.<init>(r13);
        r4.put(r5, r6);
        r18 = r18 + 1;
        goto L_0x00f5;
    L_0x0119:
        r0 = r37;
        r10 = r0.mElasticAssets;
        goto L_0x00bb;
    L_0x011e:
        r4 = java.lang.System.out;
        r5 = "ERROR: Asset path is null";
        r4.println(r5);
        r10 = 0;
        goto L_0x00bb;
    L_0x0127:
        r10 = new android.content.res.AssetManager;
        r10.<init>();
        if (r38 == 0) goto L_0x00bb;
    L_0x012e:
        r0 = r38;
        r4 = r10.addAssetPath(r0);
        if (r4 != 0) goto L_0x00bb;
    L_0x0136:
        r16 = 0;
        goto L_0x007e;
    L_0x013a:
        if (r39 == 0) goto L_0x015a;
    L_0x013c:
        r9 = r39;
        r0 = r9.length;
        r23 = r0;
        r20 = 0;
    L_0x0143:
        r0 = r20;
        r1 = r23;
        if (r0 >= r1) goto L_0x015a;
    L_0x0149:
        r34 = r9[r20];
        r0 = r34;
        r4 = r10.addAssetPath(r0);
        if (r4 != 0) goto L_0x0157;
    L_0x0153:
        r16 = 0;
        goto L_0x007e;
    L_0x0157:
        r20 = r20 + 1;
        goto L_0x0143;
    L_0x015a:
        r27 = android.app.ActivityThread.getPackageManager();
        r29 = 0;
        r4 = 0;
        r5 = android.os.UserHandle.myUserId();	 Catch:{ RemoteException -> 0x01c0, Exception -> 0x01c5 }
        r0 = r27;
        r1 = r45;
        r29 = r0.getPackageInfo(r1, r4, r5);	 Catch:{ RemoteException -> 0x01c0, Exception -> 0x01c5 }
    L_0x016d:
        if (r29 == 0) goto L_0x01a1;
    L_0x016f:
        r0 = r29;
        r4 = r0.applicationInfo;
        r0 = r4.resourceDirs;
        r40 = r0;
        r5 = "ResourcesManager";
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r6 = "For user ";
        r4 = r4.append(r6);
        r0 = r46;
        r4 = r4.append(r0);
        r6 = " new overlays fetched ";
        r6 = r4.append(r6);
        if (r40 == 0) goto L_0x01ca;
    L_0x0192:
        r4 = java.util.Arrays.asList(r40);
    L_0x0196:
        r4 = r6.append(r4);
        r4 = r4.toString();
        android.util.Slog.d(r5, r4);
    L_0x01a1:
        if (r40 == 0) goto L_0x01cd;
    L_0x01a3:
        r4 = android.os.PersonaManager.isKnoxId(r46);
        if (r4 != 0) goto L_0x01cd;
    L_0x01a9:
        r9 = r40;
        r0 = r9.length;
        r23 = r0;
        r20 = 0;
    L_0x01b0:
        r0 = r20;
        r1 = r23;
        if (r0 >= r1) goto L_0x01cd;
    L_0x01b6:
        r21 = r9[r20];
        r0 = r21;
        r10.addOverlayPath(r0);
        r20 = r20 + 1;
        goto L_0x01b0;
    L_0x01c0:
        r15 = move-exception;
        r15.printStackTrace();
        goto L_0x016d;
    L_0x01c5:
        r15 = move-exception;
        r15.printStackTrace();
        goto L_0x016d;
    L_0x01ca:
        r4 = "Null";
        goto L_0x0196;
    L_0x01cd:
        if (r41 == 0) goto L_0x0213;
    L_0x01cf:
        r9 = r41;
        r0 = r9.length;
        r23 = r0;
        r20 = 0;
    L_0x01d6:
        r0 = r20;
        r1 = r23;
        if (r0 >= r1) goto L_0x0213;
    L_0x01dc:
        r24 = r9[r20];
        r4 = ".apk";
        r0 = r24;
        r4 = r0.endsWith(r4);
        if (r4 == 0) goto L_0x0210;
    L_0x01e8:
        r0 = r24;
        r4 = r10.addAssetPath(r0);
        if (r4 != 0) goto L_0x0210;
    L_0x01f0:
        r4 = "ResourcesManager";
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "Asset path '";
        r5 = r5.append(r6);
        r0 = r24;
        r5 = r5.append(r0);
        r6 = "' does not exist or contains no resources.";
        r5 = r5.append(r6);
        r5 = r5.toString();
        android.util.Log.w(r4, r5);
    L_0x0210:
        r20 = r20 + 1;
        goto L_0x01d6;
    L_0x0213:
        r0 = r37;
        r1 = r42;
        r14 = r0.getDisplayMetricsLocked(r1);
        if (r42 != 0) goto L_0x02dd;
    L_0x021d:
        r22 = 1;
    L_0x021f:
        r26 = 0;
        r19 = r3.hasOverrideConfiguration();
        if (r22 == 0) goto L_0x0229;
    L_0x0227:
        if (r19 == 0) goto L_0x0337;
    L_0x0229:
        r12 = new android.content.res.Configuration;
        r0 = r37;
        r1 = r42;
        r4 = r0.getConfiguration(r1);
        r12.<init>(r4);
        if (r22 != 0) goto L_0x023d;
    L_0x0238:
        r0 = r37;
        r0.applyNonDefaultDisplayMetricsToConfigurationLocked(r14, r12);
    L_0x023d:
        if (r19 == 0) goto L_0x028f;
    L_0x023f:
        r4 = com.samsung.android.multiwindow.MultiWindowFeatures.SELECTIVE1ORIENTATION_ENABLED;
        if (r4 == 0) goto L_0x0330;
    L_0x0243:
        r4 = r3.mOverrideConfiguration;
        r4 = r4.isStackOverrideConfig();
        if (r4 == 0) goto L_0x0330;
    L_0x024b:
        r4 = r3.mOverrideConfiguration;
        r12.updateFromStackOverrideConfig(r4);
        r26 = new android.util.DisplayMetrics;
        r26.<init>();
        r0 = r26;
        r0.setTo(r14);
        r4 = r12.orientation;
        r5 = 2;
        if (r4 != r5) goto L_0x02ed;
    L_0x025f:
        r4 = r14.widthPixels;
        r5 = r14.heightPixels;
        if (r4 <= r5) goto L_0x02e1;
    L_0x0265:
        r4 = r14.widthPixels;
    L_0x0267:
        r0 = r26;
        r0.widthPixels = r4;
        r4 = r14.widthPixels;
        r5 = r14.heightPixels;
        if (r4 <= r5) goto L_0x02e4;
    L_0x0271:
        r4 = r14.heightPixels;
    L_0x0273:
        r0 = r26;
        r0.heightPixels = r4;
        r4 = r14.noncompatWidthPixels;
        r5 = r14.noncompatHeightPixels;
        if (r4 <= r5) goto L_0x02e7;
    L_0x027d:
        r4 = r14.noncompatWidthPixels;
    L_0x027f:
        r0 = r26;
        r0.noncompatWidthPixels = r4;
        r4 = r14.noncompatWidthPixels;
        r5 = r14.noncompatHeightPixels;
        if (r4 <= r5) goto L_0x02ea;
    L_0x0289:
        r4 = r14.noncompatHeightPixels;
    L_0x028b:
        r0 = r26;
        r0.noncompatHeightPixels = r4;
    L_0x028f:
        r4 = com.samsung.android.multiwindow.MultiWindowFeatures.SELECTIVE1ORIENTATION_ENABLED;
        if (r4 == 0) goto L_0x0341;
    L_0x0293:
        if (r26 == 0) goto L_0x0341;
    L_0x0295:
        r32 = new android.content.res.Resources;
        r0 = r32;
        r1 = r26;
        r2 = r44;
        r0.<init>(r10, r1, r12, r2);
    L_0x02a0:
        r0 = r45;
        r1 = r32;
        r1.mResPackageName = r0;
        r0 = r46;
        r1 = r32;
        r1.mResUserId = r0;
        monitor-enter(r37);
        r0 = r37;
        r4 = r0.mActiveResources;	 Catch:{ all -> 0x02da }
        r35 = r4.get(r3);	 Catch:{ all -> 0x02da }
        r35 = (java.lang.ref.WeakReference) r35;	 Catch:{ all -> 0x02da }
        if (r35 == 0) goto L_0x034c;
    L_0x02b9:
        r4 = r35.get();	 Catch:{ all -> 0x02da }
        r4 = (android.content.res.Resources) r4;	 Catch:{ all -> 0x02da }
        r16 = r4;
    L_0x02c1:
        if (r16 == 0) goto L_0x0350;
    L_0x02c3:
        r4 = r16.getAssets();	 Catch:{ all -> 0x02da }
        r4 = r4.isUpToDate();	 Catch:{ all -> 0x02da }
        if (r4 == 0) goto L_0x0350;
    L_0x02cd:
        r4 = r32.getAssets();	 Catch:{ all -> 0x02da }
        r4.close();	 Catch:{ all -> 0x02da }
        r37.updateThemeResources();	 Catch:{ all -> 0x02da }
        monitor-exit(r37);	 Catch:{ all -> 0x02da }
        goto L_0x007e;
    L_0x02da:
        r4 = move-exception;
        monitor-exit(r37);	 Catch:{ all -> 0x02da }
        throw r4;
    L_0x02dd:
        r22 = 0;
        goto L_0x021f;
    L_0x02e1:
        r4 = r14.heightPixels;
        goto L_0x0267;
    L_0x02e4:
        r4 = r14.widthPixels;
        goto L_0x0273;
    L_0x02e7:
        r4 = r14.noncompatHeightPixels;
        goto L_0x027f;
    L_0x02ea:
        r4 = r14.noncompatWidthPixels;
        goto L_0x028b;
    L_0x02ed:
        r4 = r12.orientation;
        r5 = 1;
        if (r4 != r5) goto L_0x028f;
    L_0x02f2:
        r4 = r14.widthPixels;
        r5 = r14.heightPixels;
        if (r4 >= r5) goto L_0x0324;
    L_0x02f8:
        r4 = r14.widthPixels;
    L_0x02fa:
        r0 = r26;
        r0.widthPixels = r4;
        r4 = r14.widthPixels;
        r5 = r14.heightPixels;
        if (r4 >= r5) goto L_0x0327;
    L_0x0304:
        r4 = r14.heightPixels;
    L_0x0306:
        r0 = r26;
        r0.heightPixels = r4;
        r4 = r14.noncompatWidthPixels;
        r5 = r14.noncompatHeightPixels;
        if (r4 >= r5) goto L_0x032a;
    L_0x0310:
        r4 = r14.noncompatWidthPixels;
    L_0x0312:
        r0 = r26;
        r0.noncompatWidthPixels = r4;
        r4 = r14.noncompatWidthPixels;
        r5 = r14.noncompatHeightPixels;
        if (r4 >= r5) goto L_0x032d;
    L_0x031c:
        r4 = r14.noncompatHeightPixels;
    L_0x031e:
        r0 = r26;
        r0.noncompatHeightPixels = r4;
        goto L_0x028f;
    L_0x0324:
        r4 = r14.heightPixels;
        goto L_0x02fa;
    L_0x0327:
        r4 = r14.widthPixels;
        goto L_0x0306;
    L_0x032a:
        r4 = r14.noncompatHeightPixels;
        goto L_0x0312;
    L_0x032d:
        r4 = r14.noncompatWidthPixels;
        goto L_0x031e;
    L_0x0330:
        r4 = r3.mOverrideConfiguration;
        r12.updateFrom(r4);
        goto L_0x028f;
    L_0x0337:
        r0 = r37;
        r1 = r42;
        r12 = r0.getConfiguration(r1);
        goto L_0x028f;
    L_0x0341:
        r32 = new android.content.res.Resources;
        r0 = r32;
        r1 = r44;
        r0.<init>(r10, r14, r12, r1);
        goto L_0x02a0;
    L_0x034c:
        r16 = 0;
        goto L_0x02c1;
    L_0x0350:
        r0 = r37;
        r4 = r0.mActiveResources;	 Catch:{ all -> 0x02da }
        r5 = new java.lang.ref.WeakReference;	 Catch:{ all -> 0x02da }
        r0 = r32;
        r5.<init>(r0);	 Catch:{ all -> 0x02da }
        r4.put(r3, r5);	 Catch:{ all -> 0x02da }
        monitor-exit(r37);	 Catch:{ all -> 0x02da }
        r16 = r32;
        goto L_0x007e;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ResourcesManager.getTopLevelResources(java.lang.String, java.lang.String[], java.lang.String[], java.lang.String[], int, android.content.res.Configuration, android.content.res.CompatibilityInfo, java.lang.String, int, android.os.IBinder):android.content.res.Resources");
    }

    private void updateThemeResources() {
        for (int i = this.mActiveResources.size() - 1; i >= 0; i--) {
            ResourcesKey key = (ResourcesKey) this.mActiveResources.keyAt(i);
            Resources r = (Resources) ((WeakReference) this.mActiveResources.valueAt(i)).get();
            if (r == null) {
                this.mActiveResources.removeAt(i);
            } else if (!PersonaManager.isKnoxId(r.mResUserId)) {
                String currentPackageName = ActivityThread.currentPackageName();
                boolean isOtherPackageResource = (currentPackageName == null || r.mResPackageName == null || currentPackageName.equals(r.mResPackageName)) ? false : true;
                boolean isNotSystemResource = currentPackageName == null && !"android".equals(r.mResPackageName);
                if (isOtherPackageResource || isNotSystemResource) {
                    PackageInfo pi = null;
                    try {
                        pi = ActivityThread.getPackageManager().getPackageInfo(r.mResPackageName, 0, UserHandle.myUserId());
                    } catch (RemoteException e) {
                    }
                    if (pi != null) {
                        String[] resourceDirs = pi.applicationInfo.resourceDirs;
                        ArrayList<String> overlays = new ArrayList(r.getAssets().getOverlays());
                        if ((resourceDirs != null && resourceDirs.length != 0) || overlays.size() != 0) {
                            r.getAssets().removeOverlayPath(r.mResPackageName);
                            if (resourceDirs != null && resourceDirs.length > 0) {
                                for (String resourceDir : resourceDirs) {
                                    r.getAssets().addOverlayPath(resourceDir);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public int getResIdOffset(String packageName) {
        Integer cookie = (Integer) this.cookieMap.get(packageName);
        if (cookie == null || cookie.intValue() <= 0 || this.mElasticAssets == null) {
            return 0;
        }
        return ((Integer) this.mElasticAssets.elasticAppCookieOffset.get(cookie)).intValue();
    }

    final boolean applyConfigurationToResourcesLocked(Configuration config, CompatibilityInfo compat) {
        return applyConfigurationToResourcesLocked(config, compat, false);
    }

    public final boolean applyConfigurationToResourcesLocked(Configuration config, CompatibilityInfo compat, boolean force) {
        if (this.mResConfiguration == null) {
            this.mResConfiguration = new Configuration();
        }
        if (!this.mResConfiguration.isOtherSeqNewer(config) && compat == null && !force) {
            return false;
        }
        int changes = this.mResConfiguration.updateFrom(config);
        this.mDisplays.clear();
        DisplayMetrics defaultDisplayMetrics = getDisplayMetricsLocked(0);
        if (compat != null && (this.mResCompatibilityInfo == null || !this.mResCompatibilityInfo.equals(compat))) {
            this.mResCompatibilityInfo = compat;
            changes |= 3328;
        }
        if (config.locale != null) {
            Locale.setDefault(config.locale);
        }
        Resources.updateSystemConfiguration(config, defaultDisplayMetrics, compat);
        ApplicationPackageManager.configurationChanged();
        Configuration tmpConfig = null;
        for (int i = this.mActiveResources.size() - 1; i >= 0; i--) {
            ResourcesKey key = (ResourcesKey) this.mActiveResources.keyAt(i);
            Resources r = (Resources) ((WeakReference) this.mActiveResources.valueAt(i)).get();
            if (r != null) {
                if (!PersonaManager.isKnoxId(r.mResUserId) && Configuration.needToUpdateOverlays(changes)) {
                    String currentPackageName = ActivityThread.currentPackageName();
                    boolean isOtherPackageResource = (currentPackageName == null || r.mResPackageName == null || currentPackageName.equals(r.mResPackageName)) ? false : true;
                    boolean isNotSystemResource = currentPackageName == null && !"android".equals(r.mResPackageName);
                    if (isOtherPackageResource || isNotSystemResource) {
                        PackageInfo pi = null;
                        try {
                            pi = ActivityThread.getPackageManager().getPackageInfo(r.mResPackageName, 0, UserHandle.myUserId());
                        } catch (RemoteException e) {
                        }
                        if (pi != null) {
                            String[] resourceDirs = pi.applicationInfo.resourceDirs;
                            ArrayList<String> arrayList = new ArrayList(r.getAssets().getOverlays());
                            if (!((resourceDirs == null || resourceDirs.length == 0) && arrayList.size() == 0)) {
                                Slog.d(TAG, "removeOverlayPath " + r.mResPackageName);
                                r.getAssets().removeOverlayPath(r.mResPackageName);
                                if (resourceDirs != null && resourceDirs.length > 0) {
                                    for (String resourceDir : resourceDirs) {
                                        Slog.d(TAG, "Adding overlay path " + resourceDir + " for resources " + r.mResPackageName + "--" + currentPackageName);
                                        r.getAssets().addOverlayPath(resourceDir);
                                    }
                                }
                            }
                        }
                    }
                }
                int displayId = key.mDisplayId;
                boolean isDefaultDisplay = displayId == 0;
                DisplayMetrics overrideMetrics = null;
                DisplayMetrics dm = defaultDisplayMetrics;
                boolean hasOverrideConfiguration = key.hasOverrideConfiguration();
                if (!isDefaultDisplay || hasOverrideConfiguration) {
                    if (tmpConfig == null) {
                        tmpConfig = new Configuration();
                    }
                    tmpConfig.setTo(config);
                    if (!isDefaultDisplay) {
                        dm = getDisplayMetricsLocked(displayId);
                        applyNonDefaultDisplayMetricsToConfigurationLocked(dm, tmpConfig);
                    }
                    if (hasOverrideConfiguration) {
                        if (MultiWindowFeatures.SELECTIVE1ORIENTATION_ENABLED && key.mOverrideConfiguration.isStackOverrideConfig()) {
                            tmpConfig.updateFromStackOverrideConfig(key.mOverrideConfiguration);
                            overrideMetrics = new DisplayMetrics();
                            overrideMetrics.setTo(dm);
                            if (tmpConfig.orientation == 2) {
                                int i2;
                                overrideMetrics.widthPixels = dm.widthPixels > dm.heightPixels ? dm.widthPixels : dm.heightPixels;
                                overrideMetrics.heightPixels = dm.widthPixels > dm.heightPixels ? dm.heightPixels : dm.widthPixels;
                                overrideMetrics.noncompatWidthPixels = dm.noncompatWidthPixels > dm.noncompatHeightPixels ? dm.noncompatWidthPixels : dm.noncompatHeightPixels;
                                if (dm.noncompatWidthPixels > dm.noncompatHeightPixels) {
                                    i2 = dm.noncompatHeightPixels;
                                } else {
                                    i2 = dm.noncompatWidthPixels;
                                }
                                overrideMetrics.noncompatHeightPixels = i2;
                            } else if (tmpConfig.orientation == 1) {
                                overrideMetrics.widthPixels = dm.widthPixels < dm.heightPixels ? dm.widthPixels : dm.heightPixels;
                                overrideMetrics.heightPixels = dm.widthPixels < dm.heightPixels ? dm.heightPixels : dm.widthPixels;
                                overrideMetrics.noncompatWidthPixels = dm.noncompatWidthPixels < dm.noncompatHeightPixels ? dm.noncompatWidthPixels : dm.noncompatHeightPixels;
                                overrideMetrics.noncompatHeightPixels = dm.noncompatWidthPixels < dm.noncompatHeightPixels ? dm.noncompatHeightPixels : dm.noncompatWidthPixels;
                            }
                        } else {
                            tmpConfig.updateFrom(key.mOverrideConfiguration);
                        }
                    }
                    if (!MultiWindowFeatures.SELECTIVE1ORIENTATION_ENABLED || overrideMetrics == null) {
                        r.updateConfiguration(tmpConfig, dm, compat);
                    } else {
                        r.updateConfiguration(tmpConfig, overrideMetrics, compat);
                    }
                } else {
                    r.updateConfiguration(config, dm, compat);
                }
            } else {
                this.mActiveResources.removeAt(i);
            }
        }
        return changes != 0;
    }

    public final void applyDisplayMetricsToResourcesLocked(DisplayInfo displayInfo) {
    }
}
