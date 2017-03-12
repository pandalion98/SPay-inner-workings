package com.samsung.android.multiwindow;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.view.WindowManagerPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class MultiWindowStyle implements Parcelable {
    public static final Creator<MultiWindowStyle> CREATOR = new Creator<MultiWindowStyle>() {
        public MultiWindowStyle createFromParcel(Parcel in) {
            return new MultiWindowStyle(in);
        }

        public MultiWindowStyle[] newArray(int size) {
            return new MultiWindowStyle[size];
        }
    };
    public static final int DEFAULT_SPECIFIC_TASK_ID = -1;
    public static final int FLOATING_LAYER = 1;
    public static final int MINIMIZE_LAYER = 2;
    public static final int NORMAL_LAYER = 0;
    public static final int NOTIFY_FOCUS_REASON_ACTIVITY_CHANGED = 1;
    public static final int NOTIFY_FOCUS_REASON_FOCUSED_RELAUNCH = 4;
    public static final int NOTIFY_FOCUS_REASON_MASK = 7;
    public static final int NOTIFY_FOCUS_REASON_NO_CHANGED = 0;
    public static final int NOTIFY_FOCUS_REASON_STACK_CHANGED = 2;
    public static final int NOTIFY_REASON_ARRANGE_MINIMIZED = 4;
    public static final int NOTIFY_REASON_ARRANGE_MINIMIZED_ALL = 16;
    public static final int NOTIFY_REASON_ARRANGE_MINIMIZED_SHOW = 32;
    public static final int NOTIFY_REASON_OPTION_SPLIT_RECENT_MULTIWINDOW = 64;
    public static final int NOTIFY_REASON_ROTATION_CHANGED = 256;
    public static final int NOTIFY_REASON_SELECTIVE_ORIENTATION_CHANGED = 128;
    public static final int NOTIFY_REASON_SIZE_CHANGED = 2;
    public static final int NOTIFY_REASON_STYLE_CHANGED = 1;
    public static final int NOTIFY_REASON_TAB_MODE_CHANGED = 8;
    public static final int NOTIFY_REASON_TYPE_MASK = 510;
    public static final int NOTIFY_STATE_HIDDEN = 1;
    public static final int NOTIFY_STATE_SHOWN = 2;
    public static final int OPTION_DISABLE_FLOATING_WINDOW = 134217728;
    public static final int OPTION_EXTERNAL_DISP = 1024;
    public static final int OPTION_FIXED_ORIENTATION = 4194304;
    public static final int OPTION_FIXED_RATIO = 32768;
    public static final int OPTION_FIXED_SIZE = 65536;
    public static final int OPTION_FORCE_CONTROL_RESIZE = 16384;
    public static final int OPTION_FORCE_MAKE_PHONEWINDOW = 262144;
    public static final int OPTION_FORCE_MULTIPLE_TASK = 524288;
    public static final int OPTION_FORCE_TITLE_BAR = 32;
    public static final int OPTION_FORCE_TO_SET_MULTIWINDOW_STYLE = 268435456;
    public static final int OPTION_FULLSCREEN_MINIMIZABLE = 131072;
    public static final int OPTION_FULLSCREEN_ONLY = 2097152;
    public static final int OPTION_HIDDEN = 8;
    public static final int OPTION_HIDE_CENTER_BAR_DURING_STARTING = 33554432;
    public static final int OPTION_INHERIT_STACK = 8192;
    public static final int OPTION_ISOLATED_SPLIT = 4096;
    public static final int OPTION_LAUNCH_IN_SAME = 1048576;
    public static final int OPTION_MINIMIZED = 4;
    public static final int OPTION_NO_TITLE_BAR = 16;
    public static final int OPTION_PENWINDOWABLE = 16777216;
    public static final int OPTION_PINUP = 1;
    public static final int OPTION_RECENTS_MULTIWINDOW = 67108864;
    public static final int OPTION_RESIZE = 2;
    public static final int OPTION_SCALE = 2048;
    public static final int OPTION_TAB_MODE = 8388608;
    public static final int OPTION_TOOLKIT = 512;
    private static final String STYLE_ACTIVITY_FORCE_TITLE_BAR = "forceTitleBar";
    private static final String STYLE_ACTIVITY_FULLSCREEN_ONLY = "fullscreenOnly";
    private static final String STYLE_ACTIVITY_RESIZE_ONLY = "resizeOnly";
    private static final String STYLE_FIXED_ORIENTATION = "fixedOrientation";
    private static final String STYLE_FIXED_RATIO = "fixedRatio";
    private static final String STYLE_FIXED_SIZE = "fixedSize";
    private static final String STYLE_FREESTYLE_ONLY = "freestyleOnly";
    private static final String STYLE_ISOLATED_SPLIT = "isolatedSplit";
    private static final String STYLE_NO_TITLE_BAR = "noTitleBar";
    public static final int TYPE_CASCADE = 2;
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_SPLIT = 1;
    private static final int UNIQUE_OPTIONS = 488779826;
    private static final int UNITARY_OPTIONS = 2097152;
    public static final int ZONE_A = 3;
    public static final int ZONE_B = 12;
    public static final int ZONE_C = 1;
    public static final int ZONE_D = 2;
    public static final int ZONE_E = 4;
    public static final int ZONE_F = 8;
    public static final int ZONE_FULL = 15;
    public static final int ZONE_UNKNOWN = 0;
    public static MultiWindowStyle sConstDefaultMultiWindowStyle = new MultiWindowStyle(0) {
        public void setType(int type) {
            throw new IllegalAccessError();
        }

        public void setZone(int zone) {
            throw new IllegalAccessError();
        }

        public void setOption(int option, int mask) {
            throw new IllegalAccessError();
        }

        public void setOption(int option, boolean value) {
            throw new IllegalAccessError();
        }

        public void setTo(MultiWindowStyle other, boolean includeUniqueOptions) {
            throw new IllegalAccessError();
        }
    };
    public static int sDefaultOrientation = 0;
    private static HashMap<String, Integer> sStyleStrings = new HashMap();
    private static HashMap<String, Integer> sTypeStrings = new HashMap();
    private int mAppRequestOrientation;
    private Rect mBounds;
    private boolean mIsNull;
    private Point mIsolatedCenterPoint;
    private int mOnTransaction;
    private int mOptionFlags;
    private float mScale;
    private int mSpecificTaskId;
    private int mType;
    private int mZone;

    class CompatMultiWindowStyle {
        int mMode;
        int mOption;
        MultiWindowStyle mStyle;
        int mZone;

        CompatMultiWindowStyle(MultiWindowStyle multiWindowStyle, int mode) {
            this.mStyle = multiWindowStyle;
            this.mMode = -16777216 & mode;
            this.mZone = mode & 15;
            this.mOption = WindowManagerPolicy.WINDOW_MODE_OPTION_COMMON_MASK & mode;
        }

        int getType() {
            if (this.mMode == 16777216) {
                return 0;
            }
            if (this.mMode == 33554432 && this.mZone == 0) {
                return 2;
            }
            return 1;
        }

        int getZone() {
            if (this.mZone == 3) {
                return 3;
            }
            if (this.mZone == 12) {
                return 12;
            }
            if (this.mZone == 1) {
                return 1;
            }
            if (this.mZone == 2) {
                return 2;
            }
            if (this.mZone == 4) {
                return 4;
            }
            if (this.mZone == 8) {
                return 8;
            }
            if (this.mZone == 15) {
                return 15;
            }
            return 0;
        }

        int getOption() {
            int option = 0;
            if ((this.mOption & 8388608) != 0) {
                option = 0 | 1;
            }
            if ((this.mOption & 4194304) != 0) {
                option |= 2;
            }
            if ((this.mOption & 2097152) != 0) {
                option |= 4;
            }
            if ((this.mOption & 1048576) != 0) {
                option |= 8;
            }
            if ((this.mOption & 524288) != 0) {
                option |= 16;
            }
            if ((this.mOption & 262144) != 0) {
                option |= 32;
            }
            if ((this.mOption & 8192) != 0) {
                option |= 512;
            }
            if ((this.mOption & 4096) != 0) {
                option |= 1024;
            }
            if ((this.mOption & 2048) != 0) {
                option |= 2048;
            }
            if ((this.mOption & 65536) != 0) {
                return option | 8192;
            }
            return option;
        }

        MultiWindowStyle getMultiWindowStyle() {
            this.mStyle.setType(getType());
            this.mStyle.setZone(getZone());
            this.mStyle.setOption(getOption(), true);
            return this.mStyle;
        }
    }

    class CompatWindowMode {
        int mOption;
        int mType;
        int mZone;

        CompatWindowMode(int type, int zone, int option) {
            this.mType = type;
            this.mZone = zone;
            this.mOption = option;
        }

        int getMode() {
            if (this.mType == 0) {
                return 16777216;
            }
            return 33554432;
        }

        int getZone() {
            switch (this.mZone) {
                case 1:
                    return 1;
                case 2:
                    return 2;
                case 3:
                    return 3;
                case 4:
                    return 4;
                case 8:
                    return 8;
                case 12:
                    return 12;
                case 15:
                    return 15;
                default:
                    return 0;
            }
        }

        int getOption() {
            int option = 0;
            if ((this.mOption & 1) != 0) {
                option = 0 | 8388608;
            }
            if ((this.mOption & 2) != 0) {
                option |= 4194304;
            }
            if ((this.mOption & 4) != 0) {
                option |= 2097152;
            }
            if ((this.mOption & 8) != 0) {
                option |= 1048576;
            }
            if ((this.mOption & 16) != 0) {
                option |= 524288;
            }
            if ((this.mOption & 32) != 0) {
                option |= 262144;
            }
            if ((this.mOption & 1024) != 0) {
                option |= 4096;
            }
            if ((this.mOption & 2048) != 0) {
                return option | 2048;
            }
            return option;
        }

        int getCurrentMode() {
            return (getZone() | getMode()) | getOption();
        }
    }

    public void setScale(float scale) {
        this.mScale = scale;
    }

    public float getScale() {
        return this.mScale;
    }

    public void resetSpecificTaskId() {
        this.mSpecificTaskId = -1;
    }

    public void setSpecificTaskId(int taskId) {
        this.mSpecificTaskId = taskId;
    }

    public int getSpecificTaskId() {
        return this.mSpecificTaskId;
    }

    static {
        sStyleStrings.put("noTitleBar", Integer.valueOf(16));
        sStyleStrings.put("forceTitleBar", Integer.valueOf(32));
        sStyleStrings.put("isolatedSplit", Integer.valueOf(4096));
        sStyleStrings.put("fixedSize", Integer.valueOf(65536));
        sStyleStrings.put("fixedRatio", Integer.valueOf(32768));
        sStyleStrings.put(STYLE_FIXED_ORIENTATION, Integer.valueOf(4194304));
        sStyleStrings.put("fullscreenOnly", Integer.valueOf(2097152));
        sTypeStrings.put("freestyleOnly", Integer.valueOf(2));
    }

    public void setType(int type, boolean resetOptions) {
        if (resetOptions) {
            setType(type);
        } else {
            this.mType = type;
        }
    }

    public void setType(int type) {
        this.mType = type;
        setZone(0);
        if (this.mType == 2 && this.mBounds == null) {
            this.mBounds = new Rect();
        }
        this.mOptionFlags &= UNIQUE_OPTIONS;
    }

    public int getType() {
        return this.mType;
    }

    public int getLayer() {
        if (this.mType != 2) {
            return 0;
        }
        if (isEnabled(4)) {
            return 2;
        }
        return 1;
    }

    public int getOptionFlags() {
        return this.mOptionFlags;
    }

    public void setAppRequestOrientation(int orientation) {
        setAppRequestOrientation(orientation, false);
    }

    public void setAppRequestOrientation(int orientation, boolean force) {
        if (force || orientation != -1) {
            this.mAppRequestOrientation = orientation;
        }
    }

    public int getAppRequestOrientation() {
        return this.mAppRequestOrientation;
    }

    public boolean isCascade() {
        return this.mType == 2;
    }

    public boolean isSplit() {
        return this.mType == 1;
    }

    public boolean isNormal() {
        return this.mType == 0;
    }

    public boolean isFakeTarget(int requestedOrientation) {
        if (!MultiWindowFeatures.SELECTIVE1ORIENTATION_ENABLED) {
            return false;
        }
        if (requestedOrientation == 5 && sDefaultOrientation != 0) {
            if (sDefaultOrientation == 1) {
                requestedOrientation = 1;
            } else {
                requestedOrientation = 0;
            }
        }
        switch (requestedOrientation) {
            case 0:
            case 1:
            case 6:
            case 7:
            case 8:
            case 9:
            case 11:
            case 12:
                return true;
            default:
                return false;
        }
    }

    public boolean shouldFakeOrientation(int requestedOrientation, int currentOrientation, Rect outBound) {
        if (!MultiWindowFeatures.SELECTIVE1ORIENTATION_ENABLED) {
            return false;
        }
        if (!isCascade()) {
            return false;
        }
        boolean bFake;
        if (convertToConfigurationOrientation(requestedOrientation, currentOrientation) != currentOrientation) {
            bFake = true;
        } else {
            bFake = false;
        }
        if (outBound == null || !bFake) {
            return bFake;
        }
        int temp = outBound.left;
        outBound.left = outBound.top;
        outBound.top = temp;
        temp = outBound.right;
        outBound.right = outBound.bottom;
        outBound.bottom = temp;
        return bFake;
    }

    public static int convertToConfigurationOrientation(int requestedOrientation, int currentOrientation) {
        if (requestedOrientation == 5 && sDefaultOrientation != 0) {
            if (sDefaultOrientation == 1) {
                requestedOrientation = 1;
            } else {
                requestedOrientation = 0;
            }
        }
        if (currentOrientation == 2) {
            switch (requestedOrientation) {
                case 1:
                case 7:
                case 9:
                case 12:
                    return 1;
                default:
                    return currentOrientation;
            }
        } else if (currentOrientation != 1) {
            return currentOrientation;
        } else {
            switch (requestedOrientation) {
                case 0:
                case 6:
                case 8:
                case 11:
                    return 2;
                default:
                    return currentOrientation;
            }
        }
    }

    public void setZone(int zone) {
        this.mZone = zone;
    }

    public int getZone() {
        return this.mZone;
    }

    int findBaseZone(int requestZone) {
        switch (requestZone) {
            case 1:
            case 2:
                return 3;
            case 3:
            case 12:
                return 15;
            case 4:
            case 8:
                return 12;
            default:
                return 15;
        }
    }

    public int getOppositeZone() {
        return (this.mZone ^ -1) & findBaseZone(this.mZone);
    }

    public int getZoneByType() {
        switch (this.mType) {
            case 1:
            case 2:
                return this.mZone;
            default:
                return 15;
        }
    }

    public boolean isInValidZone() {
        switch (this.mZone) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 8:
            case 12:
                return false;
            default:
                return true;
        }
    }

    public boolean getFirstZone() {
        switch (this.mZone) {
            case 1:
            case 3:
            case 4:
                return true;
            default:
                return false;
        }
    }

    public int getZoneLevel() {
        switch (this.mZone) {
            case 1:
            case 2:
            case 4:
            case 8:
                return 2;
            case 3:
            case 12:
                return 1;
            default:
                return 0;
        }
    }

    public MultiWindowStyle() {
        this.mAppRequestOrientation = -1;
        this.mOnTransaction = 0;
        this.mSpecificTaskId = -1;
        this.mZone = 0;
        this.mIsolatedCenterPoint = new Point();
        this.mIsNull = false;
        this.mType = 0;
    }

    public MultiWindowStyle(Parcel parcelledData) {
        this.mAppRequestOrientation = -1;
        this.mOnTransaction = 0;
        this.mSpecificTaskId = -1;
        this.mZone = 0;
        this.mIsolatedCenterPoint = new Point();
        this.mIsNull = false;
        readFromParcel(parcelledData);
    }

    public MultiWindowStyle(int type) {
        this.mAppRequestOrientation = -1;
        this.mOnTransaction = 0;
        this.mSpecificTaskId = -1;
        this.mZone = 0;
        this.mIsolatedCenterPoint = new Point();
        this.mIsNull = false;
        this.mType = type;
    }

    public MultiWindowStyle(MultiWindowStyle style) {
        this.mAppRequestOrientation = -1;
        this.mOnTransaction = 0;
        this.mSpecificTaskId = -1;
        this.mZone = 0;
        this.mIsolatedCenterPoint = new Point();
        this.mIsNull = false;
        setTo(style, true);
    }

    public void setOption(int option, int mask) {
        this.mOptionFlags &= mask ^ -1;
        this.mOptionFlags |= option & mask;
    }

    public void setOption(int option, boolean value) {
        this.mOptionFlags &= option ^ -1;
        if (value) {
            this.mOptionFlags |= option;
        }
    }

    public boolean isEnabled(int option) {
        return (this.mOptionFlags & option) == option;
    }

    public boolean isSupportingMultiWindow() {
        return isEnabled(2);
    }

    public boolean isMultiPhoneWindowNeeded(ActivityInfo info, Context context) {
        MultiWindowApplicationInfos infos = MultiWindowApplicationInfos.getInstance();
        if (isEnabled(131072) || ((infos.isSupportScaleApp(info, context) && MultiWindowFeatures.isSupportFreeStyle(context)) || (isEnabled(2) && !isEnabled(262144)))) {
            return true;
        }
        return false;
    }

    public void setBounds(Rect bound) {
        if (bound == null) {
            throw new IllegalArgumentException("rect is null");
        } else if (this.mBounds != null && this.mBounds.equals(bound)) {
        } else {
            if (this.mBounds == null) {
                this.mBounds = new Rect(bound);
            } else {
                this.mBounds.set(bound);
            }
        }
    }

    public Rect getBounds() {
        return this.mBounds;
    }

    public void setIsolatedCenterPoint(Point centerPoint) {
        this.mIsolatedCenterPoint.set(centerPoint.x, centerPoint.y);
    }

    public Point getIsolatedCenterPoint() {
        return this.mIsolatedCenterPoint;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mType);
        dest.writeInt(this.mZone);
        dest.writeFloat(this.mScale);
        dest.writeInt(this.mSpecificTaskId);
        dest.writeInt(this.mOptionFlags);
        dest.writeInt(this.mAppRequestOrientation);
        dest.writeInt(sDefaultOrientation);
        if (this.mBounds != null) {
            dest.writeInt(1);
            this.mBounds.writeToParcel(dest, flags);
        } else {
            dest.writeInt(0);
        }
        this.mIsolatedCenterPoint.writeToParcel(dest, flags);
    }

    private void readFromParcel(Parcel parcelledData) {
        setType(parcelledData.readInt());
        setZone(parcelledData.readInt());
        setScale(parcelledData.readFloat());
        setSpecificTaskId(parcelledData.readInt());
        this.mOptionFlags = parcelledData.readInt();
        this.mAppRequestOrientation = parcelledData.readInt();
        sDefaultOrientation = parcelledData.readInt();
        if (parcelledData.readInt() != 0) {
            this.mBounds = (Rect) Rect.CREATOR.createFromParcel(parcelledData);
        }
        this.mIsolatedCenterPoint = (Point) Point.CREATOR.createFromParcel(parcelledData);
    }

    public boolean equals(MultiWindowStyle other) {
        if (this.mType != other.mType || this.mZone != other.mZone || this.mOptionFlags != other.mOptionFlags || !this.mIsolatedCenterPoint.equals(other.mIsolatedCenterPoint)) {
            return false;
        }
        if ((this.mBounds == null || this.mBounds.equals(other.mBounds)) && this.mScale == other.mScale) {
            return true;
        }
        return false;
    }

    public void setTo(MultiWindowStyle other) {
        setTo(other, false);
    }

    public void setTo(MultiWindowStyle other, boolean includeUniqueOptions) {
        if (other != null) {
            this.mType = other.mType;
            setZone(other.mZone);
            this.mScale = other.mScale;
            if (includeUniqueOptions || (other.mOptionFlags & 16384) != 0) {
                this.mOptionFlags = other.mOptionFlags;
                this.mOptionFlags &= -16385;
            } else {
                int unitaryOption = this.mOptionFlags & 2097152;
                this.mOptionFlags &= UNIQUE_OPTIONS;
                this.mOptionFlags |= other.mOptionFlags & -488779827;
                this.mOptionFlags &= -2097153;
                this.mOptionFlags |= unitaryOption;
            }
            if (other.mBounds != null) {
                this.mBounds = new Rect(other.mBounds);
            }
            if (other.mIsolatedCenterPoint != null) {
                this.mIsolatedCenterPoint = new Point(other.mIsolatedCenterPoint);
            }
            if (includeUniqueOptions && other.mAppRequestOrientation != -1) {
                this.mAppRequestOrientation = other.mAppRequestOrientation;
            }
            this.mIsNull = false;
        }
    }

    public static String zoneToString(int zone) {
        switch (zone) {
            case 0:
                return "ZONE_UNKNOWN";
            case 1:
                return "ZONE_C";
            case 2:
                return "ZONE_D";
            case 3:
                return "ZONE_A";
            case 4:
                return "ZONE_E";
            case 8:
                return "ZONE_F";
            case 12:
                return "ZONE_B";
            default:
                return "invaild vaue" + zone;
        }
    }

    public String toString() {
        StringBuilder out = new StringBuilder(128);
        out.append("MultiWindowStyle");
        out.append("{type=");
        out.append(this.mType);
        out.append(", zone=");
        out.append(zoneToString(this.mZone));
        out.append(", option=0x");
        out.append(String.format("%08x", new Object[]{Integer.valueOf(this.mOptionFlags)}));
        out.append(", bounds=");
        out.append(this.mBounds);
        out.append(", isNull=");
        out.append(isNull());
        out.append(", isolatedCenterPoint=");
        out.append(this.mIsolatedCenterPoint);
        out.append(", scale=");
        out.append(this.mScale);
        out.append(", specificTaskId=");
        out.append(this.mSpecificTaskId);
        if (this.mAppRequestOrientation != -1) {
            out.append(", or=");
            out.append(this.mAppRequestOrientation);
        }
        out.append('}');
        return out.toString();
    }

    public MultiWindowStyle(boolean isNull) {
        this.mAppRequestOrientation = -1;
        this.mOnTransaction = 0;
        this.mSpecificTaskId = -1;
        this.mZone = 0;
        this.mIsolatedCenterPoint = new Point();
        this.mIsNull = false;
        this.mIsNull = isNull;
    }

    public void setNull(boolean isNull) {
        this.mIsNull = isNull;
    }

    public boolean isNull() {
        return this.mIsNull;
    }

    public int convertToWindowMode() {
        return new CompatWindowMode(this.mType, this.mZone, this.mOptionFlags).getCurrentMode();
    }

    public MultiWindowStyle convertToMultiWindowStyle(int mode) {
        return new CompatMultiWindowStyle(this, mode).getMultiWindowStyle();
    }

    public void parseStyleOptions(Context mContext, ActivityInfo aInfo) {
        if (aInfo != null) {
            Bundle applicationMetaData = aInfo.applicationInfo.metaData;
            if (applicationMetaData != null) {
                parseStyleOptions(applicationMetaData.getString("com.sec.android.multiwindow.STYLE"));
            }
            Bundle activityMetaData = aInfo.metaData;
            if (activityMetaData != null) {
                parseStyleOptions(activityMetaData.getString("com.sec.android.multiwindow.activity.STYLE"));
            }
            if (isEnabled(4096)) {
                String METADATA_ISOLATED_SPLIT_CENTER_POINT_X = "com.sec.android.multiwindow.isolatedSplit.centerPoint.portrait.x";
                String METADATA_ISOLATED_SPLIT_CENTER_POINT_Y = "com.sec.android.multiwindow.isolatedSplit.centerPoint.portrait.y";
                int isolatedCenterPointXResId = applicationMetaData.getInt("com.sec.android.multiwindow.isolatedSplit.centerPoint.portrait.x");
                int isolatedCenterPointYResId = applicationMetaData.getInt("com.sec.android.multiwindow.isolatedSplit.centerPoint.portrait.y");
                try {
                    Context context = mContext.createPackageContext(aInfo.packageName, 0);
                    this.mIsolatedCenterPoint.set(context.getResources().getDimensionPixelSize(isolatedCenterPointXResId), context.getResources().getDimensionPixelSize(isolatedCenterPointYResId));
                } catch (NotFoundException e) {
                } catch (NameNotFoundException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    private void parseStyleOptions(String stylesString) {
        if (stylesString != null) {
            int options = 0;
            Iterator i$ = new ArrayList(Arrays.asList(stylesString.split("\\|"))).iterator();
            while (i$.hasNext()) {
                String style = (String) i$.next();
                if (sStyleStrings.containsKey(style)) {
                    options |= ((Integer) sStyleStrings.get(style)).intValue();
                } else if (sTypeStrings.containsKey(style) && !(getType() == 2 && isEnabled(2048))) {
                    setType(((Integer) sTypeStrings.get(style)).intValue());
                }
            }
            this.mOptionFlags |= options;
        }
    }

    public static void skipMultiWindowLaunch(Intent intent) {
        MultiWindowStyle mws = intent.getMultiWindowStyle();
        if (mws == null) {
            mws = new MultiWindowStyle();
        }
        mws.setOption(1048576, true);
        intent.setMultiWindowStyle(mws);
    }

    public static boolean isMultiWindowLaunchInSame(Intent intent) {
        return isMultiWindowLaunchInSame(intent.getMultiWindowStyle());
    }

    public static boolean isMultiWindowLaunchInSame(MultiWindowStyle mws) {
        if (mws == null) {
            return false;
        }
        return mws.isEnabled(1048576);
    }

    public static void cleanMultiWindowLaunchInSame(Intent intent) {
        isMultiWindowLaunchInSame(intent.getMultiWindowStyle());
    }

    public static void cleanMultiWindowLaunchInSame(MultiWindowStyle mws) {
        if (mws != null) {
            mws.setOption(1048576, false);
        }
    }

    public boolean hasUnitaryOption() {
        return (this.mOptionFlags & 2097152) != 0;
    }

    public void removeUnitaryOption() {
        this.mOptionFlags &= -2097153;
    }

    public void setOnTransaction() {
        this.mOnTransaction = 1;
    }

    public boolean isOnTransaction() {
        if (this.mOnTransaction != 0) {
            return true;
        }
        return false;
    }

    public void releaseOnTransaction() {
        this.mOnTransaction = 0;
    }

    public static int convertToOrientation(int requestedOrientation) {
        if (requestedOrientation == 0) {
            return 2;
        }
        if (requestedOrientation == 1) {
            return 1;
        }
        return 0;
    }
}
