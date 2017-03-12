package com.samsung.android.multiwindow;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Process;
import java.io.File;

public class MultiWindowFeatures {
    public static final boolean MULTIWINDOW_ENABLED = true;
    public static final boolean SELECTIVE1ORIENTATION_ENABLED = isSupportSelective1Orientation(null);
    private static final long SUPPORT_BEZEL_UI = 65536;
    private static final long SUPPORT_CENTERBAR_CLICK_SOUND = 524288;
    private static final long SUPPORT_COMMON_UI = 1024;
    private static final long SUPPORT_FIXED_SPLIT_VIEW = 8;
    private static final long SUPPORT_FREESTYLE = 2;
    private static final long SUPPORT_FREESTYLE_DOCKING = 16;
    private static final long SUPPORT_FREESTYLE_LAUNCH = 32;
    private static final long SUPPORT_MINIMIZE_ANIMATION = 8192;
    private static final long SUPPORT_MULTIWINDOW = 1;
    private static final long SUPPORT_MULTIWINDOW_LAUNCH = 128;
    private static final long SUPPORT_MULTI_INSTANCE = 512;
    private static final long SUPPORT_QUADVIEW = 4;
    private static final long SUPPORT_RECENT_UI = 4096;
    private static final long SUPPORT_SCALE_WINDOW = 64;
    private static final long SUPPORT_SELECTIVE_1_ORIENTATION = 131072;
    private static final long SUPPORT_SIMPLIFICATION_UI = 32768;
    private static final long SUPPORT_SPLIT_FULLSCREEN = 256;
    private static final long SUPPORT_STYLE_TRANSITION = 2048;
    private static final long SUPPORT_STYLE_TRANSITION_FROM_CENTERBAR = 262144;
    private static final long SUPPORT_TAB_PEN_WINDOW = 16384;
    private static long sEnabledFeaturesFlags = 0;
    private static boolean sQueriedTypeMultiWindow = false;

    private static boolean isEnabled(long feature) {
        return (sEnabledFeaturesFlags & feature) != 0;
    }

    public static long getEnabledFeaturesFlags(Context context) {
        checkMultiWindowFeature(context);
        return sEnabledFeaturesFlags;
    }

    private static void checkMultiWindowFeature(Context context) {
        if (!sQueriedTypeMultiWindow) {
            sQueriedTypeMultiWindow = true;
            PackageManager pm = null;
            if (context != null) {
                pm = context.getPackageManager();
                if (Process.myUid() != 1000) {
                    sEnabledFeaturesFlags = ((MultiWindowFacade) context.getSystemService("multiwindow_facade")).getEnabledFeaturesFlags();
                    if (sEnabledFeaturesFlags != 0) {
                        return;
                    }
                }
            }
            if (pm != null) {
                try {
                    if (pm.hasSystemFeature("com.sec.feature.multiwindow")) {
                        sEnabledFeaturesFlags |= 1;
                    }
                    if (pm.hasSystemFeature("com.sec.feature.multiwindow.freestyle")) {
                        sEnabledFeaturesFlags |= SUPPORT_FREESTYLE;
                    }
                    if (pm.hasSystemFeature("com.sec.feature.multiwindow.quadview")) {
                        sEnabledFeaturesFlags |= SUPPORT_QUADVIEW;
                    }
                    if (pm.hasSystemFeature("com.sec.feature.multiwindow.fixedsplitview")) {
                        sEnabledFeaturesFlags |= SUPPORT_FIXED_SPLIT_VIEW;
                    }
                    if (pm.hasSystemFeature("com.sec.feature.multiwindow.freestyledocking")) {
                        sEnabledFeaturesFlags |= SUPPORT_FREESTYLE_DOCKING;
                    }
                    if (pm.hasSystemFeature("com.sec.feature.multiwindow.freestylelaunch")) {
                        sEnabledFeaturesFlags |= SUPPORT_FREESTYLE_LAUNCH;
                    }
                    if (pm.hasSystemFeature("com.sec.feature.multiwindow.scalewindow")) {
                        sEnabledFeaturesFlags |= SUPPORT_SCALE_WINDOW;
                    }
                    if (pm.hasSystemFeature("com.sec.feature.multiwindow.multiwindowlaunch")) {
                        sEnabledFeaturesFlags |= SUPPORT_MULTIWINDOW_LAUNCH;
                    }
                    if (pm.hasSystemFeature("com.sec.feature.multiwindow.splitfullscreen")) {
                        sEnabledFeaturesFlags |= SUPPORT_SPLIT_FULLSCREEN;
                    }
                    if (pm.hasSystemFeature("com.sec.feature.multiwindow.multiinstance")) {
                        sEnabledFeaturesFlags |= SUPPORT_MULTI_INSTANCE;
                    }
                    if (pm.hasSystemFeature("com.sec.feature.multiwindow.commonui")) {
                        sEnabledFeaturesFlags |= SUPPORT_COMMON_UI;
                    }
                    if (pm.hasSystemFeature("com.sec.feature.multiwindow.styletransition")) {
                        sEnabledFeaturesFlags |= SUPPORT_STYLE_TRANSITION;
                    }
                    if (pm.hasSystemFeature("com.sec.feature.multiwindow.recentui")) {
                        sEnabledFeaturesFlags |= SUPPORT_RECENT_UI;
                    }
                    if (pm.hasSystemFeature("com.sec.feature.multiwindow.minimizeanimation")) {
                        sEnabledFeaturesFlags |= SUPPORT_MINIMIZE_ANIMATION;
                    }
                    if (pm.hasSystemFeature("com.sec.feature.multiwindow.tabpenwindow")) {
                        sEnabledFeaturesFlags |= SUPPORT_TAB_PEN_WINDOW;
                    }
                    if (pm.hasSystemFeature("com.sec.feature.multiwindow.simplificationui")) {
                        sEnabledFeaturesFlags |= SUPPORT_SIMPLIFICATION_UI;
                    }
                    if (pm.hasSystemFeature("com.sec.feature.multiwindow.bezelui")) {
                        sEnabledFeaturesFlags |= SUPPORT_BEZEL_UI;
                    }
                    if (pm.hasSystemFeature("com.sec.feature.multiwindow.selective1orientation")) {
                        sEnabledFeaturesFlags |= SUPPORT_SELECTIVE_1_ORIENTATION;
                    }
                    if (pm.hasSystemFeature("com.sec.feature.multiwindow.styletransitionfromcenterbar")) {
                        sEnabledFeaturesFlags |= SUPPORT_STYLE_TRANSITION_FROM_CENTERBAR;
                    }
                    if (pm.hasSystemFeature("com.sec.feature.multiwindow.centerbarclicksound")) {
                        sEnabledFeaturesFlags |= SUPPORT_CENTERBAR_CLICK_SOUND;
                        return;
                    }
                    return;
                } catch (Exception e) {
                    sQueriedTypeMultiWindow = false;
                    return;
                }
            }
            if (new File("/system/etc/permissions/com.sec.feature.multiwindow.xml").exists()) {
                sEnabledFeaturesFlags |= 1;
            }
            if (new File("/system/etc/permissions/com.sec.feature.multiwindow.freestyle.xml").exists()) {
                sEnabledFeaturesFlags |= SUPPORT_FREESTYLE;
            }
            if (new File("/system/etc/permissions/com.sec.feature.multiwindow.quadview.xml").exists()) {
                sEnabledFeaturesFlags |= SUPPORT_QUADVIEW;
            }
            if (new File("/system/etc/permissions/com.sec.feature.multiwindow.fixedsplitview").exists()) {
                sEnabledFeaturesFlags |= SUPPORT_FIXED_SPLIT_VIEW;
            }
            if (new File("/system/etc/permissions/com.sec.feature.multiwindow.freestyledocking.xml").exists()) {
                sEnabledFeaturesFlags |= SUPPORT_FREESTYLE_DOCKING;
            }
            if (new File("/system/etc/permissions/com.sec.feature.multiwindow.freestylelaunch.xml").exists()) {
                sEnabledFeaturesFlags |= SUPPORT_FREESTYLE_LAUNCH;
            }
            if (new File("/system/etc/permissions/com.sec.feature.multiwindow.scalewindow.xml").exists()) {
                sEnabledFeaturesFlags |= SUPPORT_SCALE_WINDOW;
            }
            if (new File("/system/etc/permissions/com.sec.feature.multiwindow.multiwindowlaunch.xml").exists()) {
                sEnabledFeaturesFlags |= SUPPORT_MULTIWINDOW_LAUNCH;
            }
            if (new File("/system/etc/permissions/com.sec.feature.multiwindow.splitfullscreen.xml").exists()) {
                sEnabledFeaturesFlags |= SUPPORT_SPLIT_FULLSCREEN;
            }
            if (new File("/system/etc/permissions/com.sec.feature.multiwindow.multiinstance.xml").exists()) {
                sEnabledFeaturesFlags |= SUPPORT_MULTI_INSTANCE;
            }
            if (new File("/system/etc/permissions/com.sec.feature.multiwindow.commonui.xml").exists()) {
                sEnabledFeaturesFlags |= SUPPORT_COMMON_UI;
            }
            if (new File("/system/etc/permissions/com.sec.feature.multiwindow.styletransition.xml").exists()) {
                sEnabledFeaturesFlags |= SUPPORT_STYLE_TRANSITION;
            }
            if (new File("/system/etc/permissions/com.sec.feature.multiwindow.recentui.xml").exists()) {
                sEnabledFeaturesFlags |= SUPPORT_RECENT_UI;
            }
            if (new File("/system/etc/permissions/com.sec.feature.multiwindow.minimizeanimation.xml").exists()) {
                sEnabledFeaturesFlags |= SUPPORT_MINIMIZE_ANIMATION;
            }
            if (new File("/system/etc/permissions/com.sec.feature.multiwindow.tabpenwindow.xml").exists()) {
                sEnabledFeaturesFlags |= SUPPORT_TAB_PEN_WINDOW;
            }
            if (new File("/system/etc/permissions/com.sec.feature.multiwindow.simplificationui.xml").exists()) {
                sEnabledFeaturesFlags |= SUPPORT_SIMPLIFICATION_UI;
            }
            if (new File("/system/etc/permissions/com.sec.feature.multiwindow.bezelui.xml").exists()) {
                sEnabledFeaturesFlags |= SUPPORT_BEZEL_UI;
            }
            if (new File("/system/etc/permissions/com.sec.feature.multiwindow.selective1orientation.xml").exists()) {
                sEnabledFeaturesFlags |= SUPPORT_SELECTIVE_1_ORIENTATION;
            }
            if (new File("/system/etc/permissions/com.sec.feature.multiwindow.styletransitionfromcenterbar.xml").exists()) {
                sEnabledFeaturesFlags |= SUPPORT_STYLE_TRANSITION_FROM_CENTERBAR;
            }
            if (new File("/system/etc/permissions/com.sec.feature.multiwindow.centerbarclicksound.xml").exists()) {
                sEnabledFeaturesFlags |= SUPPORT_CENTERBAR_CLICK_SOUND;
            }
        }
    }

    public static boolean isSupportMultiWindow(Context context) {
        checkMultiWindowFeature(context);
        return isEnabled(1);
    }

    public static boolean isSupportFreeStyle(Context context) {
        checkMultiWindowFeature(context);
        return isEnabled(SUPPORT_FREESTYLE);
    }

    public static boolean isSupportMultiInstance(Context context) {
        checkMultiWindowFeature(context);
        return isEnabled(SUPPORT_MULTI_INSTANCE);
    }

    public static boolean isSupportQuadView(Context context) {
        checkMultiWindowFeature(context);
        return isEnabled(SUPPORT_QUADVIEW);
    }

    public static boolean isSupportScaleWindow(Context context) {
        checkMultiWindowFeature(context);
        return isEnabled(SUPPORT_SCALE_WINDOW);
    }

    public static boolean isSupportFixedSplitView(Context context) {
        checkMultiWindowFeature(context);
        return isEnabled(SUPPORT_FIXED_SPLIT_VIEW);
    }

    public static boolean isSupportFreeStyleDocking(Context context) {
        checkMultiWindowFeature(context);
        return isEnabled(SUPPORT_FREESTYLE_DOCKING);
    }

    public static boolean isSupportFreeStyleLaunch(Context context) {
        checkMultiWindowFeature(context);
        return isEnabled(SUPPORT_FREESTYLE_LAUNCH);
    }

    public static boolean isSupportMultiWindowLaunch(Context context) {
        checkMultiWindowFeature(context);
        return isEnabled(SUPPORT_MULTIWINDOW_LAUNCH);
    }

    public static boolean isSupportSplitFullScreen(Context context) {
        checkMultiWindowFeature(context);
        return isEnabled(SUPPORT_SPLIT_FULLSCREEN);
    }

    public static boolean isSupportCommonUI(Context context) {
        checkMultiWindowFeature(context);
        return isEnabled(SUPPORT_COMMON_UI);
    }

    public static boolean isSupportStyleTransition(Context context) {
        checkMultiWindowFeature(context);
        return isEnabled(SUPPORT_STYLE_TRANSITION);
    }

    public static boolean isSupportRecentUI(Context context) {
        checkMultiWindowFeature(context);
        return isEnabled(SUPPORT_RECENT_UI);
    }

    public static boolean isSupportMinimizeAnimation(Context context) {
        checkMultiWindowFeature(context);
        return isEnabled(SUPPORT_MINIMIZE_ANIMATION);
    }

    public static boolean isSupportTabPenWindow(Context context) {
        checkMultiWindowFeature(context);
        return isEnabled(SUPPORT_TAB_PEN_WINDOW);
    }

    public static boolean isSupportSimplificationUI(Context context) {
        checkMultiWindowFeature(context);
        return isEnabled(SUPPORT_SIMPLIFICATION_UI);
    }

    public static boolean isSupportBezelUI(Context context) {
        checkMultiWindowFeature(context);
        return isEnabled(SUPPORT_BEZEL_UI);
    }

    public static boolean isSupportOpenTheme(Context context) {
        return false;
    }

    public static boolean isSupportSelective1Orientation(Context context) {
        checkMultiWindowFeature(context);
        return isEnabled(SUPPORT_SELECTIVE_1_ORIENTATION);
    }

    public static boolean isSupportStyleTransitionFromCenterBar(Context context) {
        checkMultiWindowFeature(context);
        return isEnabled(SUPPORT_STYLE_TRANSITION_FROM_CENTERBAR);
    }

    public static boolean isSupportCenterbarClickSound(Context context) {
        checkMultiWindowFeature(context);
        return isEnabled(SUPPORT_CENTERBAR_CLICK_SOUND);
    }
}
