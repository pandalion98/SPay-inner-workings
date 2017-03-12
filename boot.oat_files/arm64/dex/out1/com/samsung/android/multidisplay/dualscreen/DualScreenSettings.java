package com.samsung.android.multidisplay.dualscreen;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings$System;
import android.provider.Settings.Global;
import android.provider.Settings.Secure;
import android.util.Log;
import java.io.PrintWriter;

public class DualScreenSettings {
    public static final boolean DEBUG = true;
    public static final String TAG = "DualScreenSettings";
    private static int mDesktopModeEnabled;
    private static int mDualScreenDisplayChooserEnabled;
    private static int mDualScreenModeEnabled;
    private static int mDualScreenOppositeLaunchEnabled;
    private static String mDualScreenSubHomeComponentName = "";
    private static String mEnabledAccessibilityServices = "";
    private static boolean mEnabledExpandHomeMode = false;
    private Context mContext = null;
    private int mDualScreenDemoMode;
    private Handler mHandler = new Handler();
    private OnSettingChangedListener mOnSettingChangedListener;
    private SettingsObserver mSettingsObserver;

    public interface OnSettingChangedListener {
        void onChange(String str);
    }

    class SettingsObserver extends ContentObserver {
        SettingsObserver(Handler handler) {
            super(handler);
        }

        void observe() {
            Log.d(DualScreenSettings.TAG, "observe()");
            ContentResolver resolver = DualScreenSettings.this.mContext.getContentResolver();
            resolver.registerContentObserver(Settings$System.getUriFor(Settings$System.DUAL_SCREEN_MODE_ENABLED), false, this, -1);
            resolver.registerContentObserver(Settings$System.getUriFor(Settings$System.DESKTOP_MODE_ENABLED), false, this, -1);
            resolver.registerContentObserver(Settings$System.getUriFor(Settings$System.DUAL_SCREEN_DISPLAY_CHOOSER_ENABLED), false, this, -1);
            resolver.registerContentObserver(Settings$System.getUriFor(Settings$System.DUAL_SCREEN_OPPOSITE_LAUNCH_ENABLED), false, this, -1);
            resolver.registerContentObserver(Global.getUriFor("dualscreen_prototype"), false, this, -1);
            resolver.registerContentObserver(Settings$System.getUriFor("subhome_package_info"), false, this, -1);
            resolver.registerContentObserver(Secure.getUriFor("enabled_accessibility_services"), false, this, -1);
            resolver.registerContentObserver(Settings$System.getUriFor("launcher_fullview_mode"), false, this, -1);
            DualScreenSettings.this.updateSettings();
        }

        public void onChange(boolean selfChange) {
            DualScreenSettings.this.updateSettings();
        }
    }

    private DualScreenSettings() {
    }

    public DualScreenSettings(Context context) {
        if (context == null) {
            throw new NullPointerException("context is null");
        }
        this.mContext = context;
    }

    public void init() {
        this.mSettingsObserver = new SettingsObserver(this.mHandler);
        this.mSettingsObserver.observe();
    }

    public static void dump(String prefix, PrintWriter pw) {
        pw.print(prefix);
        pw.print("mDualScreenModeEnabled=");
        pw.println(mDualScreenModeEnabled);
        pw.print(prefix);
        pw.print("mDesktopModeEnabled=");
        pw.println(mDesktopModeEnabled);
        pw.print(prefix);
        pw.print("mDualScreenDisplayChooserEnabled=");
        pw.println(mDualScreenDisplayChooserEnabled);
        pw.print(prefix);
        pw.print("mDualScreenOppositeLaunchEnabled=");
        pw.println(mDualScreenOppositeLaunchEnabled);
        pw.print(prefix);
        pw.print("mDualScreenSubHomeComponentName=");
        pw.println(mDualScreenSubHomeComponentName);
        pw.print(prefix);
        pw.print("mEnabledAccessibilityServices=");
        pw.println(mEnabledAccessibilityServices);
        pw.print(prefix);
        pw.print("mEnabledExpandHomeMode=");
        pw.println(mEnabledExpandHomeMode);
    }

    public boolean isDualScreenModeEnabled() {
        return mDualScreenModeEnabled == 1;
    }

    public boolean isDesktopModeEnabled() {
        return mDesktopModeEnabled == 1;
    }

    public boolean isDualScreenDisplayChooserEnabled() {
        return mDualScreenDisplayChooserEnabled == 1;
    }

    public boolean isDualScreenOppositeLaunchEnabled() {
        return mDualScreenOppositeLaunchEnabled == 1;
    }

    public int getDualScreenDemoMode() {
        return this.mDualScreenDemoMode;
    }

    public boolean isTalkBackEnabled() {
        if (mEnabledAccessibilityServices != null) {
            return mEnabledAccessibilityServices.matches("(?i).*com.samsung.android.app.talkback.TalkBackService.*") || mEnabledAccessibilityServices.matches("(?i).*com.google.android.marvin.talkback.TalkBackService.*");
        } else {
            return false;
        }
    }

    public static boolean isExpandHomeModeEnabled() {
        return mEnabledExpandHomeMode;
    }

    public static void setExpandHomeModeEnabled(boolean enabled) {
        if (mEnabledExpandHomeMode != enabled) {
            mEnabledExpandHomeMode = enabled;
        }
    }

    private void updateSettings() {
        boolean enabledExpandHomeMode = true;
        Log.d(TAG, "updateSettings() : mOnSettingChangedListener=" + this.mOnSettingChangedListener);
        ContentResolver resolver = this.mContext.getContentResolver();
        int dualScreenModeEnabled = Settings$System.getIntForUser(resolver, Settings$System.DUAL_SCREEN_MODE_ENABLED, 0, -2);
        if (mDualScreenModeEnabled != dualScreenModeEnabled) {
            mDualScreenModeEnabled = dualScreenModeEnabled;
            if (this.mOnSettingChangedListener != null) {
                this.mOnSettingChangedListener.onChange(Settings$System.DUAL_SCREEN_MODE_ENABLED);
            }
        }
        int desktopModeEnabled = Settings$System.getIntForUser(resolver, Settings$System.DESKTOP_MODE_ENABLED, 0, -2);
        if (mDesktopModeEnabled != desktopModeEnabled) {
            mDesktopModeEnabled = desktopModeEnabled;
            if (this.mOnSettingChangedListener != null) {
                this.mOnSettingChangedListener.onChange(Settings$System.DESKTOP_MODE_ENABLED);
            }
        }
        int dualScreenDisplayChooserEnabled = Settings$System.getIntForUser(resolver, Settings$System.DUAL_SCREEN_DISPLAY_CHOOSER_ENABLED, 0, -2);
        if (mDualScreenDisplayChooserEnabled != dualScreenDisplayChooserEnabled) {
            mDualScreenDisplayChooserEnabled = dualScreenDisplayChooserEnabled;
            if (this.mOnSettingChangedListener != null) {
                this.mOnSettingChangedListener.onChange(Settings$System.DUAL_SCREEN_DISPLAY_CHOOSER_ENABLED);
            }
        }
        int dualScreenOppositeLaunchEnabled = Settings$System.getIntForUser(resolver, Settings$System.DUAL_SCREEN_OPPOSITE_LAUNCH_ENABLED, 0, -2);
        if (mDualScreenOppositeLaunchEnabled != dualScreenOppositeLaunchEnabled) {
            mDualScreenOppositeLaunchEnabled = dualScreenOppositeLaunchEnabled;
            if (this.mOnSettingChangedListener != null) {
                this.mOnSettingChangedListener.onChange(Settings$System.DUAL_SCREEN_OPPOSITE_LAUNCH_ENABLED);
            }
        }
        String dualScreenSubHomeComponentName = Settings$System.getStringForUser(resolver, "subhome_package_info", -2);
        if (mDualScreenSubHomeComponentName != dualScreenSubHomeComponentName) {
            mDualScreenSubHomeComponentName = dualScreenSubHomeComponentName;
            if (this.mOnSettingChangedListener != null) {
                this.mOnSettingChangedListener.onChange("subhome_package_info");
            }
        }
        String enabledAccessibilityServices = Secure.getStringForUser(resolver, "enabled_accessibility_services", -2);
        if (mEnabledAccessibilityServices != enabledAccessibilityServices) {
            mEnabledAccessibilityServices = enabledAccessibilityServices;
            if (this.mOnSettingChangedListener != null) {
                this.mOnSettingChangedListener.onChange("enabled_accessibility_services");
            }
        }
        if (Settings$System.getIntForUser(resolver, "launcher_fullview_mode", 0, -2) != 1) {
            enabledExpandHomeMode = false;
        }
        if (mEnabledExpandHomeMode != enabledExpandHomeMode) {
            mEnabledExpandHomeMode = enabledExpandHomeMode;
            if (this.mOnSettingChangedListener != null) {
                this.mOnSettingChangedListener.onChange("launcher_fullview_mode");
            }
        }
    }

    public void setOnSettingChangedListener(OnSettingChangedListener l) {
        this.mOnSettingChangedListener = l;
    }
}
