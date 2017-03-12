package com.android.internal.policy.multiwindow;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.display.DisplayManagerGlobal;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.DisplayInfo;
import android.view.WindowManagerPolicy;
import com.samsung.android.multiwindow.MultiWindowApplicationInfos;
import com.samsung.android.multiwindow.MultiWindowFacade;
import com.samsung.android.multiwindow.MultiWindowFeatures;
import com.samsung.android.multiwindow.MultiWindowStyle;
import java.util.ArrayList;
import java.util.Arrays;

public class Docking {
    private static final float DOCKING_AREA_RATIO = 0.04f;
    private static final int DOCKING_CANCEL_TIMER_TIME = 700;
    private static final float DOWN_SIDE_DOCKING_AREA_RATIO = 0.07f;
    private static final int MESSAGE_DOCKING_CANCEL = 101;
    private static final boolean bDSSEnabled = true;
    private Activity mActivity;
    private Point mCenterBarPoint;
    public int mCurScreenHeight;
    public int mCurScreenWidth;
    private Rect mDockingBounds = new Rect();
    private boolean mDockingCanceled = false;
    private OnDockingListener mDockingListener;
    private int mDockingMargin;
    private int mDockingX;
    private int mDockingY;
    private int mDockingZone = 0;
    private float mDssScale = 1.0f;
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 101:
                    if (Docking.this.mDockingListener != null) {
                        Docking.this.mDockingListener.onCancel();
                        Docking.this.mDockingCanceled = true;
                        Docking.this.mDockingX = msg.arg1;
                        Docking.this.mDockingY = msg.arg2;
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private boolean mInitCenterBarPoint = false;
    private boolean mIsPenWindowOnly = false;
    private boolean mIsSupportSplit = false;
    private boolean mIsSupportSplitDocking = false;
    private MultiWindowFacade mMultiWindowFacade;
    private boolean mOptionFixedSize = false;

    public interface OnDockingListener {
        void onCancel();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public Docking(android.app.Activity r4, com.samsung.android.multiwindow.MultiWindowFacade r5, float r6) {
        /*
        r3 = this;
        r0 = 0;
        r3.<init>();
        r3.mDockingZone = r0;
        r1 = new android.graphics.Rect;
        r1.<init>();
        r3.mDockingBounds = r1;
        r3.mDockingCanceled = r0;
        r3.mOptionFixedSize = r0;
        r3.mIsSupportSplitDocking = r0;
        r3.mIsSupportSplit = r0;
        r3.mIsPenWindowOnly = r0;
        r3.mInitCenterBarPoint = r0;
        r1 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r3.mDssScale = r1;
        r1 = new com.android.internal.policy.multiwindow.Docking$1;
        r1.<init>();
        r3.mHandler = r1;
        r3.mActivity = r4;
        r3.mMultiWindowFacade = r5;
        r3.mDssScale = r6;
        r1 = r3.mActivity;
        if (r1 == 0) goto L_0x0040;
    L_0x002e:
        r1 = r3.mActivity;
        r1 = r1.getMultiWindowStyle();
        r2 = 65536; // 0x10000 float:9.18355E-41 double:3.2379E-319;
        r1 = r1.isEnabled(r2);
        r3.mOptionFixedSize = r1;
        r1 = r3.mMultiWindowFacade;
        if (r1 == 0) goto L_0x0040;
    L_0x0040:
        r1 = r3.mActivity;
        r1 = com.samsung.android.multiwindow.MultiWindowFeatures.isSupportStyleTransition(r1);
        if (r1 == 0) goto L_0x004d;
    L_0x0048:
        r1 = r3.mOptionFixedSize;
        if (r1 != 0) goto L_0x004d;
    L_0x004c:
        r0 = 1;
    L_0x004d:
        r3.mIsSupportSplitDocking = r0;
        r0 = r4.getApplicationContext();
        r0 = r0.getResources();
        r1 = 17105612; // 0x10502cc float:2.443025E-38 double:8.451295E-317;
        r0 = r0.getDimensionPixelSize(r1);
        r3.mDockingMargin = r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.policy.multiwindow.Docking.<init>(android.app.Activity, com.samsung.android.multiwindow.MultiWindowFacade, float):void");
    }

    public static MultiWindowStyle getChanagedMultiWindowStyle(int zone, MultiWindowStyle multiWindowStyle) {
        MultiWindowStyle requestStyle = new MultiWindowStyle(multiWindowStyle);
        requestStyle.setType(1);
        if (zone == 15) {
            requestStyle.setType(0);
        } else {
            requestStyle.setZone(zone);
        }
        requestStyle.setOption(2048, false);
        return requestStyle;
    }

    public void setOnDockingListener(OnDockingListener l) {
        this.mDockingListener = l;
    }

    private boolean getDisplaySize(Point outbound, boolean isReal) {
        Display display = DisplayManagerGlobal.getInstance().getRealDisplay(0);
        if (display == null) {
            return false;
        }
        display.getSize(outbound);
        if (!MultiWindowFeatures.SELECTIVE1ORIENTATION_ENABLED || !isReal || this.mMultiWindowFacade == null) {
            return true;
        }
        DisplayInfo displayInfo = this.mMultiWindowFacade.getSystemDisplayInfo();
        if (displayInfo == null) {
            return true;
        }
        outbound.x = displayInfo.appWidth;
        outbound.y = displayInfo.appHeight;
        return true;
    }

    public void init() {
        if (this.mIsSupportSplitDocking) {
            if (checkStyleTransitionEnable()) {
                boolean z;
                if (isFullScreenOnly(this.mMultiWindowFacade.getFrontActivityMultiWindowStyle(1))) {
                    z = false;
                } else {
                    z = true;
                }
                this.mIsSupportSplit = z;
                this.mDockingZone = 0;
            }
            Point displaySize = new Point();
            if (getDisplaySize(displaySize, true)) {
                this.mCurScreenWidth = (int) (((float) displaySize.x) / this.mDssScale);
                this.mCurScreenHeight = (int) (((float) displaySize.y) / this.mDssScale);
            } else {
                this.mCurScreenWidth = this.mActivity.getResources().getDisplayMetrics().widthPixels;
                this.mCurScreenHeight = this.mActivity.getResources().getDisplayMetrics().heightPixels;
            }
            this.mInitCenterBarPoint = false;
            this.mCenterBarPoint = this.mMultiWindowFacade.getCenterBarPoint(0);
        }
    }

    public void clear() {
        this.mDockingZone = 0;
        this.mDockingBounds.setEmpty();
        this.mDockingCanceled = false;
        if (this.mHandler.hasMessages(101)) {
            this.mHandler.removeMessages(101);
        }
    }

    public void checkCenterBarPoint() {
        if (this.mInitCenterBarPoint) {
            Point point = new Point(this.mCurScreenWidth / 2, this.mCurScreenHeight / 2);
            this.mMultiWindowFacade.setCenterBarPoint(0, point);
            Intent refreshCenterbarIntent = new Intent("com.sec.android.action.ARRANGE_CONTROLL_BAR");
            refreshCenterbarIntent.putExtra("com.sec.android.extra.CONTROL_BAR_POS", point);
            this.mActivity.sendBroadcast(refreshCenterbarIntent);
        }
    }

    public boolean updateZone(int x, int y) {
        return updateZone(x, y, false);
    }

    public boolean updateZone(int x, int y, boolean ignoreRatio) {
        boolean z = true;
        try {
            if (this.mIsSupportSplitDocking) {
                int lastZone = this.mDockingZone;
                this.mDockingZone = checkDockingWindow(x, y, ignoreRatio);
                if (this.mDockingZone == 0) {
                    if (this.mDockingCanceled) {
                        this.mDockingCanceled = false;
                    }
                    if (this.mDockingZone == 0) {
                        if (!this.mHandler.hasMessages(101)) {
                            return false;
                        }
                        this.mHandler.removeMessages(101);
                        return false;
                    } else if (this.mDockingCanceled || this.mHandler.hasMessages(101)) {
                        return false;
                    } else {
                        this.mHandler.sendMessageDelayed(Message.obtain(this.mHandler, 101, x, y, null), 700);
                        return false;
                    }
                } else if (this.mDockingCanceled) {
                    if (Math.abs(x - this.mDockingX) > this.mDockingMargin || Math.abs(y - this.mDockingY) > this.mDockingMargin) {
                        this.mDockingCanceled = false;
                        return z;
                    } else if (this.mDockingZone == 0) {
                        if (!this.mHandler.hasMessages(101)) {
                            return false;
                        }
                        this.mHandler.removeMessages(101);
                        return false;
                    } else if (this.mDockingCanceled || this.mHandler.hasMessages(101)) {
                        return false;
                    } else {
                        this.mHandler.sendMessageDelayed(Message.obtain(this.mHandler, 101, x, y, null), 700);
                        return false;
                    }
                } else if (lastZone != this.mDockingZone) {
                    if (this.mDockingZone == 0) {
                        if (this.mHandler.hasMessages(101)) {
                            this.mHandler.removeMessages(101);
                        }
                    } else if (!(this.mDockingCanceled || this.mHandler.hasMessages(101))) {
                        this.mHandler.sendMessageDelayed(Message.obtain(this.mHandler, 101, x, y, null), 700);
                    }
                    return true;
                } else if (this.mDockingZone == 0) {
                    if (!this.mHandler.hasMessages(101)) {
                        return false;
                    }
                    this.mHandler.removeMessages(101);
                    return false;
                } else if (this.mDockingCanceled || this.mHandler.hasMessages(101)) {
                    return false;
                } else {
                    this.mHandler.sendMessageDelayed(Message.obtain(this.mHandler, 101, x, y, null), 700);
                    return false;
                }
            } else if (this.mDockingZone == 0) {
                if (!this.mHandler.hasMessages(101)) {
                    return false;
                }
                this.mHandler.removeMessages(101);
                return false;
            } else if (this.mDockingCanceled || this.mHandler.hasMessages(101)) {
                return false;
            } else {
                this.mHandler.sendMessageDelayed(Message.obtain(this.mHandler, 101, x, y, null), 700);
                return false;
            }
        } finally {
            if (this.mDockingZone == 0) {
                z = this.mHandler.hasMessages(101);
                if (z) {
                    z = this.mHandler;
                    z.removeMessages(101);
                }
            } else {
                z = this.mDockingCanceled;
                if (!z) {
                    z = this.mHandler.hasMessages(101);
                    if (!z) {
                        z = this.mHandler;
                        z.sendMessageDelayed(Message.obtain(this.mHandler, 101, x, y, null), 700);
                    }
                }
            }
        }
    }

    public boolean isDockingCanceled() {
        return this.mDockingCanceled;
    }

    public Rect getBounds() {
        return isDocking() ? this.mDockingBounds : null;
    }

    public boolean isDocking() {
        return this.mDockingZone != 0;
    }

    public int getZone() {
        return this.mDockingZone;
    }

    public int checkDockingWindow(int x, int y, boolean ignoreRatio) {
        float downSideDockingAreaRatio = 0.0f;
        if (!this.mIsSupportSplitDocking) {
            return 0;
        }
        int dockingZone = 0;
        if (this.mCenterBarPoint == null) {
            return 0;
        }
        float dockingAreaRatio = ignoreRatio ? 0.0f : DOCKING_AREA_RATIO;
        if (!ignoreRatio) {
            downSideDockingAreaRatio = DOWN_SIDE_DOCKING_AREA_RATIO;
        }
        if (this.mCurScreenWidth < this.mCurScreenHeight) {
            if (((float) y) < ((float) this.mCurScreenHeight) * dockingAreaRatio) {
                if (this.mCenterBarPoint.y <= 0) {
                    this.mDockingBounds.set(0, 0, this.mCurScreenWidth, this.mCurScreenHeight / 2);
                    this.mInitCenterBarPoint = true;
                } else {
                    this.mDockingBounds.set(0, 0, this.mCurScreenWidth, this.mCenterBarPoint.y);
                    this.mInitCenterBarPoint = false;
                }
                dockingZone = 3;
            } else if (((float) y) > ((float) this.mCurScreenHeight) * (1.0f - downSideDockingAreaRatio)) {
                if (this.mCenterBarPoint.y >= this.mCurScreenHeight) {
                    this.mDockingBounds.set(0, this.mCurScreenHeight / 2, this.mCurScreenWidth, this.mCurScreenHeight);
                    this.mInitCenterBarPoint = true;
                } else {
                    this.mDockingBounds.set(0, this.mCenterBarPoint.y, this.mCurScreenWidth, this.mCurScreenHeight);
                    this.mInitCenterBarPoint = false;
                }
                dockingZone = 12;
            }
            if (dockingZone != 0 && (((float) this.mDockingBounds.height()) / ((float) this.mCurScreenHeight) <= MultiWindowFacade.SPLIT_MIN_WEIGHT || ((float) this.mDockingBounds.height()) / ((float) this.mCurScreenHeight) >= MultiWindowFacade.SPLIT_MAX_WEIGHT)) {
                if (dockingZone == 3) {
                    this.mDockingBounds.top = 0;
                    this.mDockingBounds.bottom = this.mCurScreenHeight / 2;
                } else {
                    this.mDockingBounds.top = this.mCurScreenHeight / 2;
                    this.mDockingBounds.bottom = this.mCurScreenHeight;
                }
                this.mInitCenterBarPoint = true;
            }
        } else {
            if (((float) x) < ((float) this.mCurScreenWidth) * dockingAreaRatio) {
                if (this.mCenterBarPoint.x <= 0) {
                    this.mDockingBounds.set(0, 0, this.mCurScreenWidth / 2, this.mCurScreenHeight);
                    this.mInitCenterBarPoint = true;
                } else {
                    this.mDockingBounds.set(0, 0, this.mCenterBarPoint.x, this.mCurScreenHeight);
                    this.mInitCenterBarPoint = false;
                }
                dockingZone = 3;
            } else if (((float) x) > ((float) this.mCurScreenWidth) * (1.0f - dockingAreaRatio)) {
                if (this.mCenterBarPoint.x >= this.mCurScreenWidth) {
                    this.mDockingBounds.set(this.mCurScreenWidth / 2, 0, this.mCurScreenWidth, this.mCurScreenHeight);
                    this.mInitCenterBarPoint = true;
                } else {
                    this.mDockingBounds.set(this.mCenterBarPoint.x, 0, this.mCurScreenWidth, this.mCurScreenHeight);
                    this.mInitCenterBarPoint = false;
                }
                dockingZone = 12;
            }
            if (dockingZone != 0 && (((float) this.mDockingBounds.width()) / ((float) this.mCurScreenWidth) <= MultiWindowFacade.SPLIT_MIN_WEIGHT || ((float) this.mDockingBounds.width()) / ((float) this.mCurScreenWidth) >= MultiWindowFacade.SPLIT_MAX_WEIGHT)) {
                if (dockingZone == 3) {
                    this.mDockingBounds.left = 0;
                    this.mDockingBounds.right = this.mCurScreenWidth / 2;
                } else {
                    this.mDockingBounds.left = this.mCurScreenWidth / 2;
                    this.mDockingBounds.right = this.mCurScreenWidth;
                }
                this.mInitCenterBarPoint = true;
            }
        }
        switch (dockingZone) {
            case 0:
                return dockingZone;
            default:
                if (this.mIsSupportSplit && !this.mIsPenWindowOnly) {
                    return dockingZone;
                }
                this.mDockingBounds.set(0, 0, this.mCurScreenWidth, this.mCurScreenHeight);
                return 15;
        }
    }

    private boolean isFullScreenOnly(MultiWindowStyle style) {
        if (style == null || !style.isEnabled(2) || style.isEnabled(4096) || style.isEnabled(2097152)) {
            return true;
        }
        return false;
    }

    private boolean checkStyleTransitionEnable() {
        try {
            MultiWindowApplicationInfos applicationInfos = MultiWindowApplicationInfos.getInstance();
            PackageManager pm = this.mActivity.getPackageManager();
            if (pm == null) {
                return false;
            }
            ActivityInfo activityInfo = pm.getActivityInfo(this.mActivity.getComponentName(), 192);
            if (activityInfo == null) {
                return false;
            }
            Bundle applicationMetaData = activityInfo.applicationInfo != null ? activityInfo.applicationInfo.metaData : activityInfo.metaData;
            ArrayList<String> style = null;
            if (activityInfo.metaData != null) {
                style = parseActivityInfoMetaData(activityInfo.metaData, "com.sec.android.multiwindow.activity.STYLE");
            }
            if (applicationInfos.isHideAppList(activityInfo.packageName)) {
                return false;
            }
            if ((style != null && (style.contains(WindowManagerPolicy.WINDOW_STYLE_ACTIVITY_FULLSCREEN_ONLY) || style.contains(WindowManagerPolicy.WINDOW_STYLE_ISOLATED_SPLIT))) || applicationInfos.isPenWindowOnly(activityInfo)) {
                return false;
            }
            if (applicationMetaData != null && (applicationMetaData.getBoolean("com.samsung.android.sdk.multiwindow.enable") || applicationMetaData.getBoolean("com.sec.android.support.multiwindow"))) {
                return true;
            }
            if (!applicationInfos.isSupportApp(activityInfo.packageName)) {
                return false;
            }
            if (!applicationInfos.isSupportPackageList(activityInfo.packageName) || applicationInfos.isSupportComponentList(activityInfo.name)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private ArrayList<String> parseActivityInfoMetaData(Bundle activityMetaData, String metaData) {
        ArrayList<String> arrayList = new ArrayList();
        String style = activityMetaData.getString(metaData);
        if (style != null) {
            return new ArrayList(Arrays.asList(style.split("\\|")));
        }
        return arrayList;
    }
}
