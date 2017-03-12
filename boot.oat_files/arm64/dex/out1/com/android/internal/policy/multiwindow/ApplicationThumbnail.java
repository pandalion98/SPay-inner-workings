package com.android.internal.policy.multiwindow;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region.Op;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Debug;
import android.provider.Settings$System;
import android.util.Log;
import android.view.View;
import com.android.internal.R;
import com.android.internal.os.PowerProfile;
import com.samsung.android.feature.FloatingFeature;
import com.samsung.android.theme.SThemeManager;

public class ApplicationThumbnail {
    static final boolean DEBUG;
    static final String TAG = "ApplicationThumbnail";
    private Activity mActivity;
    private boolean mIsUseDefaultTheme = true;
    protected Drawable mMinimizeBg = null;
    protected Drawable mMinimizeBgFocus = null;
    protected Drawable mMinimizeIcon = null;
    protected Drawable mMinimizeLineFocus = null;
    protected Drawable mMinimizeLineNormal = null;
    private CharSequence mMinimizedAppLable = null;
    private int mMinimizedIconSize = 0;
    private Bitmap mThemeBackground = null;
    private SThemeManager mThemeMgr;
    private String mThemeType;

    static {
        boolean z = true;
        if (Debug.isProductShip() == 1) {
            z = false;
        }
        DEBUG = z;
    }

    public static ApplicationThumbnail create(Activity a) {
        return new ApplicationThumbnail(a);
    }

    public static ApplicationThumbnail create(Activity a, ComponentName c) {
        return new ApplicationThumbnail(a, c);
    }

    public boolean isUsedTheme() {
        return this.mThemeType.length() > 0 && this.mThemeType.contains("theme") && !this.mIsUseDefaultTheme;
    }

    public final Drawable getIcon() {
        return getCircleDrawable();
    }

    public final Drawable getBitmapIcon() {
        return getCircleBitmapDrawable();
    }

    public final CharSequence getLabel() {
        return this.mMinimizedAppLable;
    }

    public final void setCustomMinimizeIcon(Drawable icon) {
        if (icon == null) {
            PackageManager pm = this.mActivity.getPackageManager();
            if (pm != null) {
                try {
                    this.mMinimizeIcon = getScaledMinimizeIcon(getFullResIcon(pm.getActivityInfo(this.mActivity.getComponentName(), 0)));
                    return;
                } catch (NameNotFoundException e) {
                    e.printStackTrace();
                    return;
                }
            }
            return;
        }
        this.mMinimizeIcon = getScaledMinimizeIcon(icon);
    }

    protected ApplicationThumbnail(Activity a) {
        this.mActivity = a;
        ActivityManager am = (ActivityManager) this.mActivity.getSystemService("activity");
        PackageManager pm = this.mActivity.getPackageManager();
        if (!(am == null || pm == null)) {
            try {
                ActivityInfo aInfo = pm.getActivityInfo(this.mActivity.getComponentName(), 128);
                Resources res = this.mActivity.getResources();
                this.mMinimizeIcon = getScaledMinimizeIcon(getFullResIcon(aInfo));
                this.mMinimizedAppLable = aInfo.loadLabel(pm);
                if (View.TW_SCAFE_2016A) {
                    this.mMinimizedIconSize = res.getDimensionPixelSize(R.dimen.multiwindow_minimized_height);
                    this.mMinimizeBg = getScaledDrawable(res.getDrawable(R.drawable.mw_window_iconic_bg, null), this.mMinimizedIconSize, this.mMinimizedIconSize);
                    this.mMinimizeLineFocus = getScaledDrawable(res.getDrawable(R.drawable.mw_window_iconic_bg_focus, null), this.mMinimizedIconSize, this.mMinimizedIconSize);
                    this.mMinimizeLineNormal = getScaledDrawable(res.getDrawable(R.drawable.mw_window_iconic_bg_normal, null), this.mMinimizedIconSize, this.mMinimizedIconSize);
                } else {
                    this.mMinimizeBg = res.getDrawable(R.drawable.mw_window_iconic_bg, null);
                    this.mMinimizeBgFocus = res.getDrawable(R.drawable.mw_window_iconic_bg_focus, null);
                }
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        intializeTheme();
    }

    private Drawable getScaledDrawable(Drawable d, int width, int height) {
        if (d == null) {
            return d;
        }
        if (d.getIntrinsicWidth() == width && d.getIntrinsicHeight() == height) {
            return d;
        }
        if (d instanceof BitmapDrawable) {
            return new BitmapDrawable(this.mActivity.getResources(), Bitmap.createScaledBitmap(((BitmapDrawable) d).getBitmap(), width, height, true));
        }
        Bitmap b = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(b);
        d.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        d.draw(canvas);
        return new BitmapDrawable(this.mActivity.getResources(), b);
    }

    private void intializeTheme() {
        this.mThemeType = FloatingFeature.getInstance().getString("SEC_FLOATING_FEATURE_COMMON_CONFIG_CHANGEABLE_UI", PowerProfile.POWER_NONE);
        if (this.mThemeType.length() > 0 && this.mThemeType.contains("theme")) {
            this.mThemeMgr = new SThemeManager(this.mActivity.getApplicationContext());
            String themePackageName = Settings$System.getString(this.mActivity.getApplicationContext().getContentResolver(), SThemeManager.CURRENT_THEME_PACKAGE);
            if (themePackageName == null || themePackageName.isEmpty()) {
                this.mIsUseDefaultTheme = true;
            } else {
                this.mIsUseDefaultTheme = false;
            }
        }
    }

    public Drawable loadIconForResolveTheme() {
        ComponentInfo cinfo;
        PackageManager pm = this.mActivity.getPackageManager();
        ComponentInfo aInfo = null;
        ComponentInfo sInfo = null;
        if (pm != null) {
            try {
                aInfo = pm.getActivityInfo(this.mActivity.getComponentName(), 0);
                sInfo = pm.getServiceInfo(this.mActivity.getComponentName(), 0);
            } catch (Exception e) {
            }
        }
        if (aInfo != null) {
            cinfo = aInfo;
        } else {
            cinfo = sInfo;
        }
        Resources resources = null;
        if (pm != null) {
            try {
                resources = pm.getResourcesForApplication(cinfo.applicationInfo);
            } catch (NameNotFoundException e2) {
                resources = null;
            }
        }
        Drawable d = null;
        if (aInfo != null) {
            try {
                d = this.mThemeMgr.getPackageIcon(aInfo.name);
            } catch (NotFoundException e3) {
                if (DEBUG) {
                    Log.e(TAG, "loadAppIconBitmap( " + aInfo.name + " ) failed! " + e3);
                }
            }
        }
        if (d != null || pm == null) {
            return d;
        }
        Drawable icon = pm.getCSCPackageItemIcon(aInfo.icon != 0 ? aInfo.name : aInfo.packageName);
        if (icon == null && resources != null) {
            int iconId = aInfo.getIconResource();
            if (iconId != 0) {
                icon = getFullResIcon(resources, iconId);
            }
        }
        if (icon == null) {
            return d;
        }
        if (this.mThemeBackground == null) {
            try {
                Drawable bg = this.mThemeMgr.getItemDrawable("theme_app_3rd_party_icon");
                if (bg != null) {
                    this.mThemeBackground = ((BitmapDrawable) bg).getBitmap();
                }
            } catch (NotFoundException e32) {
                if (DEBUG) {
                    Log.e("", "3rd_party_icon_menu  failed! " + e32);
                }
            }
        }
        if (this.mThemeBackground == null) {
            return d;
        }
        int width = this.mThemeBackground.getWidth();
        int height = this.mThemeBackground.getHeight();
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setFilterBitmap(true);
        p.setDither(false);
        Bitmap b = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Bitmap in_bit = ((BitmapDrawable) icon).getBitmap();
        Canvas canvas = new Canvas(b);
        canvas.drawBitmap(this.mThemeBackground, 0.0f, 0.0f, p);
        canvas.save();
        canvas.translate(((float) width) / 2.0f, ((float) height) / 2.0f);
        canvas.scale(0.75f, 0.75f);
        canvas.drawBitmap(Bitmap.createScaledBitmap(in_bit, width, height, true), ((float) (-width)) / 2.0f, ((float) (-height)) / 2.0f, p);
        canvas.restore();
        return new BitmapDrawable(this.mActivity.getApplicationContext().getResources(), b);
    }

    protected ApplicationThumbnail(Activity a, ComponentName c) {
        this.mActivity = a;
        ActivityManager am = (ActivityManager) this.mActivity.getSystemService("activity");
        PackageManager pm = this.mActivity.getPackageManager();
        if (am != null && pm != null) {
            if (c == null) {
                c = this.mActivity.getComponentName();
            }
            try {
                ActivityInfo aInfo = pm.getActivityInfo(c, 0);
                Resources res = this.mActivity.getResources();
                this.mMinimizeIcon = getScaledMinimizeIcon(getFullResIcon(aInfo));
                this.mMinimizedAppLable = aInfo.loadLabel(pm);
                if (View.TW_SCAFE_2016A) {
                    this.mMinimizedIconSize = res.getDimensionPixelSize(R.dimen.multiwindow_minimized_height);
                    this.mMinimizeBg = getScaledDrawable(res.getDrawable(R.drawable.mw_window_iconic_bg, null), this.mMinimizedIconSize, this.mMinimizedIconSize);
                    this.mMinimizeLineFocus = getScaledDrawable(res.getDrawable(R.drawable.mw_window_iconic_bg_focus, null), this.mMinimizedIconSize, this.mMinimizedIconSize);
                    this.mMinimizeLineNormal = getScaledDrawable(res.getDrawable(R.drawable.mw_window_iconic_bg_normal, null), this.mMinimizedIconSize, this.mMinimizedIconSize);
                    return;
                }
                this.mMinimizeBg = res.getDrawable(R.drawable.mw_window_iconic_bg, null);
                this.mMinimizeBgFocus = res.getDrawable(R.drawable.mw_window_iconic_bg_focus, null);
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    protected Bitmap getMinimizedBitmap(boolean bFocused) {
        Drawable bgDrawable;
        Bitmap bitmap = getBitmapClippedCircle(((BitmapDrawable) this.mMinimizeIcon).getBitmap());
        Drawable outLineDrawable = null;
        if (View.TW_SCAFE_2016A) {
            bgDrawable = this.mMinimizeBg;
            outLineDrawable = bFocused ? this.mMinimizeLineFocus : this.mMinimizeLineNormal;
        } else {
            bgDrawable = bFocused ? this.mMinimizeBgFocus : this.mMinimizeBg;
        }
        int baseWidth = bgDrawable.getIntrinsicWidth();
        int baseHeight = bgDrawable.getIntrinsicWidth();
        Bitmap output = Bitmap.createBitmap(baseWidth, baseHeight, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.setBitmap(output);
        bgDrawable.setBounds(0, 0, baseWidth, baseHeight);
        bgDrawable.draw(canvas);
        canvas.setBitmap(output);
        canvas.save();
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, ((float) (baseWidth - bitmap.getWidth())) / 2.0f, ((float) (baseHeight - bitmap.getHeight())) / 2.0f, paint);
        }
        if (View.TW_SCAFE_2016A) {
            outLineDrawable.setBounds(0, 0, baseWidth, baseHeight);
            outLineDrawable.draw(canvas);
            canvas.save();
        }
        canvas.restore();
        canvas.save();
        return output;
    }

    private Drawable getCircleDrawable() {
        StateListDrawable drawable = new StateListDrawable();
        Bitmap focus = getMinimizedBitmap(true);
        Bitmap normal = getMinimizedBitmap(false);
        int[] iArr = new int[]{R.attr.state_pressed};
        drawable.addState(iArr, new BitmapDrawable(this.mActivity.getResources(), focus));
        drawable.addState(new int[0], new BitmapDrawable(this.mActivity.getResources(), normal));
        return drawable;
    }

    private Drawable getCircleBitmapDrawable() {
        return new BitmapDrawable(this.mActivity.getResources(), getMinimizedBitmap(false));
    }

    private Drawable getScaledMinimizeIcon(Drawable icon) {
        Resources res = this.mActivity.getResources();
        int originPixelSize = res.getDimensionPixelSize(R.dimen.multiwindow_app_icon);
        if (icon == null) {
            return icon;
        }
        if (icon.getMinimumHeight() == originPixelSize && (icon instanceof BitmapDrawable)) {
            return icon;
        }
        if (icon instanceof BitmapDrawable) {
            return new BitmapDrawable(res, Bitmap.createScaledBitmap(((BitmapDrawable) icon).getBitmap(), originPixelSize, originPixelSize, true));
        }
        Bitmap b = Bitmap.createBitmap(originPixelSize, originPixelSize, Config.ARGB_8888);
        Canvas canvas = new Canvas(b);
        icon.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        icon.draw(canvas);
        return new BitmapDrawable(res, b);
    }

    private Drawable getFullResIcon(ActivityInfo info) {
        Resources resources;
        PackageManager pm = this.mActivity.getPackageManager();
        try {
            resources = pm.getResourcesForApplication(info.applicationInfo);
        } catch (NameNotFoundException e) {
            resources = null;
        }
        if (resources != null) {
            int iconId = info.getIconResource();
            if (iconId != 0) {
                Drawable d = getFullResIcon(resources, iconId);
                if (d != null) {
                    Drawable liveIcon = info.loadIcon(pm, true, 1);
                    if (liveIcon == null) {
                        if (DEBUG) {
                            Log.d(TAG, "getFullResIcon() return lager density icon");
                        }
                        return d;
                    } else if (!DEBUG) {
                        return liveIcon;
                    } else {
                        Log.d(TAG, "getFullResIcon() return liveIcon");
                        return liveIcon;
                    }
                }
            }
        }
        return getFullResDefaultActivityIcon();
    }

    private Drawable getFullResIcon(Resources resources, int iconId) {
        Drawable d;
        try {
            d = resources.getDrawableForDensity(iconId, ((ActivityManager) this.mActivity.getSystemService("activity")).getLauncherLargeIconDensity());
        } catch (NotFoundException e) {
            d = null;
        }
        return d != null ? d : getFullResDefaultActivityIcon();
    }

    private Drawable getFullResDefaultActivityIcon() {
        return getFullResIcon(Resources.getSystem(), R.mipmap.sym_def_app_icon);
    }

    private Bitmap getBitmapClippedCircle(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Path path = new Path();
        path.addCircle(((float) width) / 2.0f, ((float) height) / 2.0f, (float) this.mActivity.getResources().getDimensionPixelSize(R.dimen.multiwindow_minimize_inner_icon_radius), Direction.CW);
        return clipImagePath(bitmap, path);
    }

    private Bitmap clipImagePath(Bitmap paramBitmap, Path paramPath) {
        if (paramBitmap == null) {
            return null;
        }
        if (paramPath == null || paramPath.isEmpty()) {
            return null;
        }
        RectF rectF = new RectF();
        paramPath.computeBounds(rectF, false);
        Bitmap pathBitmap = Bitmap.createBitmap(paramBitmap.getWidth(), paramBitmap.getHeight(), Config.ARGB_8888);
        Canvas pathCanvas = new Canvas(pathBitmap);
        pathCanvas.drawBitmap(paramBitmap, 0.0f, 0.0f, null);
        pathCanvas.clipPath(paramPath, Op.DIFFERENCE);
        pathCanvas.drawColor(0, Mode.CLEAR);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.argb(50, 255, 255, 255));
        paint.setStyle(Style.STROKE);
        paint.setStrokeJoin(Join.ROUND);
        paint.setStrokeCap(Cap.ROUND);
        paint.setStrokeWidth(5.0f);
        paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
        pathCanvas.drawPath(paramPath, paint);
        pathCanvas.clipPath(paramPath, Op.REVERSE_DIFFERENCE);
        pathCanvas.drawPath(paramPath, paint);
        Bitmap finalBitmap = Bitmap.createBitmap((int) rectF.width(), (int) rectF.height(), Config.ARGB_8888);
        new Canvas(finalBitmap).drawBitmap(pathBitmap, new Rect((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom), new Rect(0, 0, (int) rectF.width(), (int) rectF.height()), null);
        return finalBitmap;
    }
}
