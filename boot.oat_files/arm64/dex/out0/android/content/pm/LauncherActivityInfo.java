package android.content.pm;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.PersonaManager;
import android.os.UserHandle;
import android.provider.Settings.System;
import android.util.Log;

public class LauncherActivityInfo {
    private static final String TAG = "LauncherActivityInfo";
    private ActivityInfo mActivityInfo;
    private ComponentName mComponentName;
    private Context mContext;
    private long mFirstInstallTime;
    private final PersonaManager mPersona;
    private final PackageManager mPm;
    private ResolveInfo mResolveInfo;
    private UserHandle mUser;

    LauncherActivityInfo(Context context, ResolveInfo info, UserHandle user, long firstInstallTime) {
        this(context);
        this.mResolveInfo = info;
        this.mActivityInfo = info.activityInfo;
        this.mComponentName = LauncherApps.getComponentName(info);
        this.mUser = user;
        this.mFirstInstallTime = firstInstallTime;
    }

    LauncherActivityInfo(Context context) {
        this.mContext = context;
        this.mPm = context.getPackageManager();
        this.mPersona = (PersonaManager) context.getSystemService(Context.PERSONA_SERVICE);
    }

    public ComponentName getComponentName() {
        return this.mComponentName;
    }

    public UserHandle getUser() {
        return this.mUser;
    }

    public CharSequence getLabel() {
        return this.mResolveInfo.loadLabel(this.mPm);
    }

    public Drawable getIcon(int density) {
        Drawable icon = null;
        if (System.getString(this.mContext.getContentResolver(), "current_sec_appicon_theme_package") != null) {
            icon = this.mActivityInfo.loadIcon(this.mPm);
        }
        if (icon == null) {
            icon = getDrawableForDensity(this.mResolveInfo.getIconResource(), density);
        }
        if (icon == null) {
            return this.mResolveInfo.loadIcon(this.mPm);
        }
        return icon;
    }

    private Drawable getOriginalIcon(int density) {
        Drawable icon = null;
        if (System.getString(this.mContext.getContentResolver(), "current_sec_appicon_theme_package") != null) {
            icon = this.mActivityInfo.loadIcon(this.mPm);
        }
        if (icon == null) {
            icon = getDrawableForDensity(this.mResolveInfo.getIconResourceInternal(), density);
        }
        if (icon == null) {
            return this.mResolveInfo.loadIcon(this.mPm);
        }
        return icon;
    }

    private Drawable getDrawableForDensity(int iconRes, int density) {
        if (!(density == 0 || iconRes == 0)) {
            try {
                return this.mPm.getResourcesForApplication(this.mActivityInfo.applicationInfo).getDrawableForDensity(iconRes, density);
            } catch (NameNotFoundException e) {
            } catch (NotFoundException e2) {
            }
        }
        return null;
    }

    public int getApplicationFlags() {
        return this.mActivityInfo.applicationInfo.flags;
    }

    public ApplicationInfo getApplicationInfo() {
        return this.mActivityInfo.applicationInfo;
    }

    public long getFirstInstallTime() {
        return this.mFirstInstallTime;
    }

    public String getName() {
        return this.mActivityInfo.name;
    }

    public Drawable getBadgedIcon(int density) {
        Drawable originalIcon = getOriginalIcon(density);
        Log.d(TAG, "mActivityInfo.packageName = " + this.mActivityInfo.packageName);
        if (!("com.sec.knox.shortcutsms".equals(this.mActivityInfo.packageName) || PersonaManager.KNOX_SWITCHER_PKG.equals(this.mActivityInfo.packageName))) {
            if (originalIcon instanceof BitmapDrawable) {
                return this.mPm.getUserBadgedIcon(this.mPersona.getCustomBadgedIconifRequired(this.mContext, originalIcon, this.mActivityInfo.packageName, this.mUser), this.mUser);
            }
            Log.e(TAG, "Unable to create badged icon for " + this.mActivityInfo);
        }
        if ("com.sec.knox.shortcutsms".equals(this.mActivityInfo.packageName)) {
            originalIcon = this.mPersona.getCustomBadgedIconifRequired(this.mContext, originalIcon, this.mActivityInfo.packageName, this.mUser);
        }
        return originalIcon;
    }

    public Drawable getBadgedIconForIconTray(int density) {
        Drawable originalIcon = getOriginalIcon(density);
        if (System.getString(this.mContext.getContentResolver(), "current_sec_appicon_theme_package") == null) {
            if (this.mPm.checkComponentMetadataForIconTray(this.mActivityInfo.packageName, this.mActivityInfo.name)) {
                originalIcon = this.mPm.getDrawableForIconTray(originalIcon, 1);
            } else if (this.mPm.shouldPackIntoIconTray(this.mActivityInfo.packageName)) {
                originalIcon = this.mPm.getDrawableForIconTray(originalIcon, 1);
            }
        }
        Log.d(TAG, "mActivityInfo.packageName = " + this.mActivityInfo.packageName);
        if (!("com.sec.knox.shortcutsms".equals(this.mActivityInfo.packageName) || PersonaManager.KNOX_SWITCHER_PKG.equals(this.mActivityInfo.packageName))) {
            if (originalIcon instanceof BitmapDrawable) {
                return this.mPm.getUserBadgedIcon(this.mPersona.getCustomBadgedIconifRequired(this.mContext, originalIcon, this.mActivityInfo.packageName, this.mUser), this.mUser);
            }
            Log.e(TAG, "Unable to create badged icon for " + this.mActivityInfo);
        }
        if ("com.sec.knox.shortcutsms".equals(this.mActivityInfo.packageName)) {
            originalIcon = this.mPersona.getCustomBadgedIconifRequired(this.mContext, originalIcon, this.mActivityInfo.packageName, this.mUser);
        }
        return originalIcon;
    }
}
