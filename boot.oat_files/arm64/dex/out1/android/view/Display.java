package android.view;

import android.content.res.CompatibilityInfo;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.display.DisplayManagerGlobal;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import com.android.internal.telephony.IccCardConstants;
import java.util.Arrays;

public final class Display {
    private static final int CACHED_APP_SIZE_DURATION_MILLIS = 20;
    private static final int CACHED_ORIENTATION_DURATION_MILLIS = 2;
    private static final boolean DEBUG = false;
    public static final int DEFAULT_DISPLAY = 0;
    public static final int DISPLAY_NONE = -1;
    public static final int DUAL_SCREEN_BUILTIN_DISPLAY_COUNT = 2;
    public static final int DUAL_SCREEN_EXPANDED_DISPLAY = 2;
    public static final int DUAL_SCREEN_EXTERNAL_DISPLAY = 4;
    public static final int DUAL_SCREEN_INPUT_METHOD_DISPLAY = 3;
    public static final int DUAL_SCREEN_MAIN_DISPLAY = 0;
    public static final int DUAL_SCREEN_SUB_DISPLAY = 1;
    public static final int DUAL_SCREEN_TYPE_MAX = 4;
    public static final int FLAG_PRESENTATION = 8;
    public static final int FLAG_PRIVATE = 4;
    public static final int FLAG_ROUND = 16;
    public static final int FLAG_SCALING_DISABLED = 1073741824;
    public static final int FLAG_SECURE = 2;
    public static final int FLAG_SUPPORTS_PROTECTED_BUFFERS = 1;
    public static final int FLAG_VIRTUAL_SCREEN = 1048576;
    public static final int INVALID_DISPLAY = -1;
    public static final int STATE_DOZE = 3;
    public static final int STATE_DOZE_SUSPEND = 4;
    public static final int STATE_OFF = 1;
    public static final int STATE_ON = 2;
    public static final int STATE_UNKNOWN = 0;
    private static final String TAG = "Display";
    public static final int TYPE_BUILT_IN = 1;
    public static final int TYPE_HDMI = 2;
    public static final int TYPE_OVERLAY = 4;
    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_VIRTUAL = 5;
    public static final int TYPE_WIFI = 3;
    private String mAddress;
    private int mCachedAppHeightCompat;
    private int mCachedAppWidthCompat;
    private final DisplayAdjustments mDisplayAdjustments;
    private int mDisplayId;
    private DisplayInfo mDisplayInfo;
    private int mFlags;
    private final DisplayManagerGlobal mGlobal;
    private boolean mIsValid;
    private long mLastCachedAppSizeUpdate;
    private long mLastCachedOrientationUpdate;
    private int mLayerStack;
    private String mOwnerPackageName;
    private int mOwnerUid;
    private final DisplayMetrics mTempMetrics = new DisplayMetrics();
    private int mType;

    public static final class ColorTransform implements Parcelable {
        public static final Creator<ColorTransform> CREATOR = new Creator<ColorTransform>() {
            public ColorTransform createFromParcel(Parcel in) {
                return new ColorTransform(in);
            }

            public ColorTransform[] newArray(int size) {
                return new ColorTransform[size];
            }
        };
        public static final ColorTransform[] EMPTY_ARRAY = new ColorTransform[0];
        private final int mColorTransform;
        private final int mId;

        public ColorTransform(int id, int colorTransform) {
            this.mId = id;
            this.mColorTransform = colorTransform;
        }

        public int getId() {
            return this.mId;
        }

        public int getColorTransform() {
            return this.mColorTransform;
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof ColorTransform)) {
                return false;
            }
            ColorTransform that = (ColorTransform) other;
            if (this.mId == that.mId && this.mColorTransform == that.mColorTransform) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return ((this.mId + 17) * 17) + this.mColorTransform;
        }

        public String toString() {
            return "{" + "id=" + this.mId + ", colorTransform=" + this.mColorTransform + "}";
        }

        public int describeContents() {
            return 0;
        }

        private ColorTransform(Parcel in) {
            this(in.readInt(), in.readInt());
        }

        public void writeToParcel(Parcel out, int parcelableFlags) {
            out.writeInt(this.mId);
            out.writeInt(this.mColorTransform);
        }
    }

    public static final class Mode implements Parcelable {
        public static final Creator<Mode> CREATOR = new Creator<Mode>() {
            public Mode createFromParcel(Parcel in) {
                return new Mode(in);
            }

            public Mode[] newArray(int size) {
                return new Mode[size];
            }
        };
        public static final Mode[] EMPTY_ARRAY = new Mode[0];
        private final int mHeight;
        private final int mModeId;
        private final float mRefreshRate;
        private final int mWidth;

        public Mode(int modeId, int width, int height, float refreshRate) {
            this.mModeId = modeId;
            this.mWidth = width;
            this.mHeight = height;
            this.mRefreshRate = refreshRate;
        }

        public int getModeId() {
            return this.mModeId;
        }

        public int getPhysicalWidth() {
            return this.mWidth;
        }

        public int getPhysicalHeight() {
            return this.mHeight;
        }

        public float getRefreshRate() {
            return this.mRefreshRate;
        }

        public boolean matches(int width, int height, float refreshRate) {
            return this.mWidth == width && this.mHeight == height && Float.floatToIntBits(this.mRefreshRate) == Float.floatToIntBits(refreshRate);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Mode)) {
                return false;
            }
            Mode that = (Mode) other;
            if (this.mModeId == that.mModeId && matches(that.mWidth, that.mHeight, that.mRefreshRate)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return ((((((this.mModeId + 17) * 17) + this.mWidth) * 17) + this.mHeight) * 17) + Float.floatToIntBits(this.mRefreshRate);
        }

        public String toString() {
            return "{" + "id=" + this.mModeId + ", width=" + this.mWidth + ", height=" + this.mHeight + ", fps=" + this.mRefreshRate + "}";
        }

        public int describeContents() {
            return 0;
        }

        private Mode(Parcel in) {
            this(in.readInt(), in.readInt(), in.readInt(), in.readFloat());
        }

        public void writeToParcel(Parcel out, int parcelableFlags) {
            out.writeInt(this.mModeId);
            out.writeInt(this.mWidth);
            out.writeInt(this.mHeight);
            out.writeFloat(this.mRefreshRate);
        }
    }

    public Display(DisplayManagerGlobal global, int displayId, DisplayInfo displayInfo, DisplayAdjustments daj) {
        this.mGlobal = global;
        this.mDisplayId = displayId;
        this.mDisplayInfo = displayInfo;
        this.mDisplayAdjustments = new DisplayAdjustments(daj);
        this.mIsValid = true;
        this.mLayerStack = displayInfo.layerStack;
        this.mFlags = displayInfo.flags;
        this.mType = displayInfo.type;
        this.mAddress = displayInfo.address;
        this.mOwnerUid = displayInfo.ownerUid;
        this.mOwnerPackageName = displayInfo.ownerPackageName;
    }

    public int getDisplayId() {
        return this.mDisplayId;
    }

    public boolean isValid() {
        boolean z;
        synchronized (this) {
            updateDisplayInfoLocked();
            z = this.mIsValid;
        }
        return z;
    }

    public boolean getDisplayInfo(DisplayInfo outDisplayInfo) {
        boolean z;
        synchronized (this) {
            updateDisplayInfoLocked();
            outDisplayInfo.copyFrom(this.mDisplayInfo);
            z = this.mIsValid;
        }
        return z;
    }

    public int getLayerStack() {
        return this.mLayerStack;
    }

    public int getFlags() {
        return this.mFlags;
    }

    public int getType() {
        return this.mType;
    }

    public String getAddress() {
        return this.mAddress;
    }

    public int getOwnerUid() {
        return this.mOwnerUid;
    }

    public String getOwnerPackageName() {
        return this.mOwnerPackageName;
    }

    public DisplayAdjustments getDisplayAdjustments() {
        return this.mDisplayAdjustments;
    }

    public String getName() {
        String str;
        synchronized (this) {
            updateDisplayInfoLocked();
            str = this.mDisplayInfo.name;
        }
        return str;
    }

    public void getSize(Point outSize) {
        synchronized (this) {
            updateDisplayInfoLocked();
            this.mDisplayInfo.getAppMetrics(this.mTempMetrics, this.mDisplayAdjustments);
            outSize.x = this.mTempMetrics.widthPixels;
            outSize.y = this.mTempMetrics.heightPixels;
        }
    }

    public void getRectSize(Rect outSize) {
        synchronized (this) {
            updateDisplayInfoLocked();
            this.mDisplayInfo.getAppMetrics(this.mTempMetrics, this.mDisplayAdjustments);
            outSize.set(0, 0, this.mTempMetrics.widthPixels, this.mTempMetrics.heightPixels);
        }
    }

    public void getCurrentSizeRange(Point outSmallestSize, Point outLargestSize) {
        synchronized (this) {
            updateDisplayInfoLocked();
            outSmallestSize.x = this.mDisplayInfo.smallestNominalAppWidth;
            outSmallestSize.y = this.mDisplayInfo.smallestNominalAppHeight;
            outLargestSize.x = this.mDisplayInfo.largestNominalAppWidth;
            outLargestSize.y = this.mDisplayInfo.largestNominalAppHeight;
        }
    }

    public int getMaximumSizeDimension() {
        int max;
        synchronized (this) {
            updateDisplayInfoLocked();
            max = Math.max(this.mDisplayInfo.logicalWidth, this.mDisplayInfo.logicalHeight);
        }
        return max;
    }

    @Deprecated
    public int getWidth() {
        int i;
        synchronized (this) {
            updateCachedAppSizeIfNeededLocked();
            i = this.mCachedAppWidthCompat;
        }
        return i;
    }

    @Deprecated
    public int getHeight() {
        int i;
        synchronized (this) {
            updateCachedAppSizeIfNeededLocked();
            i = this.mCachedAppHeightCompat;
        }
        return i;
    }

    public void getOverscanInsets(Rect outRect) {
        synchronized (this) {
            updateDisplayInfoLocked();
            outRect.set(this.mDisplayInfo.overscanLeft, this.mDisplayInfo.overscanTop, this.mDisplayInfo.overscanRight, this.mDisplayInfo.overscanBottom);
        }
    }

    public int getRotation() {
        int i;
        synchronized (this) {
            updateDisplayInfoLocked();
            i = this.mDisplayInfo.rotation;
        }
        return i;
    }

    @Deprecated
    public int getOrientation() {
        int i;
        synchronized (this) {
            updateCachedOrientationIfNeededLocked();
            i = this.mDisplayInfo.rotation;
        }
        return i;
    }

    @Deprecated
    public int getPixelFormat() {
        return 1;
    }

    public float getRefreshRate() {
        float refreshRate;
        synchronized (this) {
            updateDisplayInfoLocked();
            refreshRate = this.mDisplayInfo.getMode().getRefreshRate();
        }
        return refreshRate;
    }

    @Deprecated
    public float[] getSupportedRefreshRates() {
        float[] defaultRefreshRates;
        synchronized (this) {
            updateDisplayInfoLocked();
            defaultRefreshRates = this.mDisplayInfo.getDefaultRefreshRates();
        }
        return defaultRefreshRates;
    }

    public Mode getMode() {
        Mode mode;
        synchronized (this) {
            updateDisplayInfoLocked();
            mode = this.mDisplayInfo.getMode();
        }
        return mode;
    }

    public Mode[] getSupportedModes() {
        Mode[] modeArr;
        synchronized (this) {
            updateDisplayInfoLocked();
            Mode[] modes = this.mDisplayInfo.supportedModes;
            modeArr = (Mode[]) Arrays.copyOf(modes, modes.length);
        }
        return modeArr;
    }

    public void requestColorTransform(ColorTransform colorTransform) {
        this.mGlobal.requestColorTransform(this.mDisplayId, colorTransform.getId());
    }

    public ColorTransform getColorTransform() {
        ColorTransform colorTransform;
        synchronized (this) {
            updateDisplayInfoLocked();
            colorTransform = this.mDisplayInfo.getColorTransform();
        }
        return colorTransform;
    }

    public ColorTransform getDefaultColorTransform() {
        ColorTransform defaultColorTransform;
        synchronized (this) {
            updateDisplayInfoLocked();
            defaultColorTransform = this.mDisplayInfo.getDefaultColorTransform();
        }
        return defaultColorTransform;
    }

    public ColorTransform[] getSupportedColorTransforms() {
        ColorTransform[] colorTransformArr;
        synchronized (this) {
            updateDisplayInfoLocked();
            ColorTransform[] transforms = this.mDisplayInfo.supportedColorTransforms;
            colorTransformArr = (ColorTransform[]) Arrays.copyOf(transforms, transforms.length);
        }
        return colorTransformArr;
    }

    public long getAppVsyncOffsetNanos() {
        long j;
        synchronized (this) {
            updateDisplayInfoLocked();
            j = this.mDisplayInfo.appVsyncOffsetNanos;
        }
        return j;
    }

    public long getPresentationDeadlineNanos() {
        long j;
        synchronized (this) {
            updateDisplayInfoLocked();
            j = this.mDisplayInfo.presentationDeadlineNanos;
        }
        return j;
    }

    public void getMetrics(DisplayMetrics outMetrics) {
        getMetrics(outMetrics, false);
    }

    public void getMetrics(DisplayMetrics outMetrics, boolean isReal) {
        synchronized (this) {
            updateDisplayInfoLocked(isReal);
            this.mDisplayInfo.getAppMetrics(outMetrics, this.mDisplayAdjustments);
        }
    }

    public void getRealSize(Point outSize) {
        synchronized (this) {
            updateDisplayInfoLocked();
            outSize.x = this.mDisplayInfo.logicalWidth;
            outSize.y = this.mDisplayInfo.logicalHeight;
        }
    }

    public void getRealMetrics(DisplayMetrics outMetrics) {
        synchronized (this) {
            updateDisplayInfoLocked();
            this.mDisplayInfo.getLogicalMetrics(outMetrics, CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO, this.mDisplayAdjustments.getConfiguration());
        }
    }

    public int getState() {
        int i;
        synchronized (this) {
            updateDisplayInfoLocked();
            i = this.mIsValid ? this.mDisplayInfo.state : 0;
        }
        return i;
    }

    public boolean hasAccess(int uid) {
        return hasAccess(uid, this.mFlags, this.mOwnerUid);
    }

    public static boolean hasAccess(int uid, int flags, int ownerUid) {
        return (flags & 4) == 0 || uid == ownerUid || uid == 1000 || uid == 0;
    }

    public boolean isPublicPresentation() {
        return (this.mFlags & 12) == 8;
    }

    private void updateDisplayInfoLocked() {
        updateDisplayInfoLocked(false);
    }

    private void updateDisplayInfoLocked(boolean isReal) {
        DisplayInfo newInfo;
        if (isReal) {
            newInfo = this.mGlobal.getDisplayInfo(this.mDisplayId, null);
        } else {
            newInfo = this.mGlobal.getDisplayInfo(this.mDisplayId);
        }
        if (newInfo != null) {
            this.mDisplayInfo = newInfo;
            if (!this.mIsValid) {
                this.mIsValid = true;
            }
        } else if (this.mIsValid) {
            this.mIsValid = false;
        }
    }

    private void updateCachedAppSizeIfNeededLocked() {
        long now = SystemClock.uptimeMillis();
        if (now > this.mLastCachedAppSizeUpdate + 20) {
            updateDisplayInfoLocked();
            this.mDisplayInfo.getAppMetrics(this.mTempMetrics, this.mDisplayAdjustments);
            this.mCachedAppWidthCompat = this.mTempMetrics.widthPixels;
            this.mCachedAppHeightCompat = this.mTempMetrics.heightPixels;
            this.mLastCachedAppSizeUpdate = now;
        }
    }

    private void updateCachedOrientationIfNeededLocked() {
        long now = System.currentTimeMillis();
        if (now > this.mLastCachedOrientationUpdate + 2) {
            updateDisplayInfoLocked();
            this.mLastCachedOrientationUpdate = now;
        }
    }

    public void setTo(int displayId) {
        synchronized (this) {
            if (displayId < 0 || displayId >= 4) {
                return;
            }
            this.mDisplayId = displayId;
            updateDisplayInfoLocked();
            this.mLayerStack = this.mDisplayInfo.layerStack;
            this.mFlags = this.mDisplayInfo.flags;
            this.mType = this.mDisplayInfo.type;
            this.mAddress = this.mDisplayInfo.address;
            this.mOwnerUid = this.mDisplayInfo.ownerUid;
            this.mOwnerPackageName = this.mDisplayInfo.ownerPackageName;
        }
    }

    public String toString() {
        String str;
        synchronized (this) {
            updateDisplayInfoLocked();
            this.mDisplayInfo.getAppMetrics(this.mTempMetrics, this.mDisplayAdjustments);
            str = "Display id " + this.mDisplayId + ": " + this.mDisplayInfo + ", " + this.mTempMetrics + ", isValid=" + this.mIsValid;
        }
        return str;
    }

    public static String typeToString(int type) {
        switch (type) {
            case 0:
                return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
            case 1:
                return "BUILT_IN";
            case 2:
                return "HDMI";
            case 3:
                return "WIFI";
            case 4:
                return "OVERLAY";
            case 5:
                return "VIRTUAL";
            default:
                return Integer.toString(type);
        }
    }

    public static String stateToString(int state) {
        switch (state) {
            case 0:
                return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
            case 1:
                return "OFF";
            case 2:
                return "ON";
            case 3:
                return "DOZE";
            case 4:
                return "DOZE_SUSPEND";
            default:
                return Integer.toString(state);
        }
    }

    public static boolean isSuspendedState(int state) {
        return state == 1 || state == 4;
    }
}
