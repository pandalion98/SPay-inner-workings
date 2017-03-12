package android.view;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.IBinder;
import android.util.Log;
import android.util.secutil.Slog;
import android.view.Surface.OutOfResourcesException;
import dalvik.system.CloseGuard;

public class SurfaceControl {
    public static final int BUILT_IN_DISPLAY_ID_EXPANDED = -1;
    public static final int BUILT_IN_DISPLAY_ID_HDMI = 1;
    public static final int BUILT_IN_DISPLAY_ID_INPUT_METHOD = -1;
    public static final int BUILT_IN_DISPLAY_ID_MAIN = 0;
    public static final int BUILT_IN_DISPLAY_ID_SUB = -1;
    public static final int CURSOR_WINDOW = 8192;
    public static final int FLAG_FIXED_ORIENATION = 1073741824;
    public static final int FLAG_INCLUDE_IN_PARTIAL_MIRROR = 268435456;
    public static final int FLAG_TRANSLUCENT_SCREENSHOT = 536870912;
    public static final int FLAG_USAGE_INTERNAL_DISP = Integer.MIN_VALUE;
    public static final int FX_SURFACE_BLUR = 524288;
    public static final int FX_SURFACE_DIM = 131072;
    public static final int FX_SURFACE_MASK = 983040;
    public static final int FX_SURFACE_NORMAL = 0;
    public static final int HIDDEN = 4;
    public static final int NON_PREMULTIPLIED = 256;
    public static final int NO_REMOTECONTROL = 134217728;
    public static final int OPAQUE = 1024;
    public static final int POWER_MODE_DOZE = 1;
    public static final int POWER_MODE_DOZE_SUSPEND = 3;
    public static final int POWER_MODE_NORMAL = 2;
    public static final int POWER_MODE_OFF = 0;
    public static final int PROTECTED_APP = 2048;
    public static final int SECURE = 128;
    private static final boolean SURFACE_DEBUG = false;
    private static final int SURFACE_HIDDEN = 1;
    private static final int SURFACE_OPAQUE = 2;
    private static final String TAG = "SurfaceControl";
    private static final boolean mIsDualDisplay = false;
    private final CloseGuard mCloseGuard = CloseGuard.get();
    private final String mName;
    long mNativeObject;

    public static final class PhysicalDisplayInfo {
        public long appVsyncOffsetNanos;
        public int colorTransform;
        public float density;
        public int height;
        public long presentationDeadlineNanos;
        public float refreshRate;
        public boolean secure;
        public int width;
        public float xDpi;
        public float yDpi;

        public PhysicalDisplayInfo(PhysicalDisplayInfo other) {
            copyFrom(other);
        }

        public boolean equals(Object o) {
            return (o instanceof PhysicalDisplayInfo) && equals((PhysicalDisplayInfo) o);
        }

        public boolean equals(PhysicalDisplayInfo other) {
            return other != null && this.width == other.width && this.height == other.height && this.refreshRate == other.refreshRate && this.density == other.density && this.xDpi == other.xDpi && this.yDpi == other.yDpi && this.secure == other.secure && this.appVsyncOffsetNanos == other.appVsyncOffsetNanos && this.presentationDeadlineNanos == other.presentationDeadlineNanos && this.colorTransform == other.colorTransform;
        }

        public int hashCode() {
            return 0;
        }

        public void copyFrom(PhysicalDisplayInfo other) {
            this.width = other.width;
            this.height = other.height;
            this.refreshRate = other.refreshRate;
            this.density = other.density;
            this.xDpi = other.xDpi;
            this.yDpi = other.yDpi;
            this.secure = other.secure;
            this.appVsyncOffsetNanos = other.appVsyncOffsetNanos;
            this.presentationDeadlineNanos = other.presentationDeadlineNanos;
            this.colorTransform = other.colorTransform;
        }

        public String toString() {
            return "PhysicalDisplayInfo{" + this.width + " x " + this.height + ", " + this.refreshRate + " fps, " + "density " + this.density + ", " + this.xDpi + " x " + this.yDpi + " dpi, secure " + this.secure + ", appVsyncOffset " + this.appVsyncOffsetNanos + ", bufferDeadline " + this.presentationDeadlineNanos + ", colorTransform " + this.colorTransform + "}";
        }
    }

    private static native boolean nativeClearAnimationFrameStats();

    private static native boolean nativeClearContentFrameStats(long j);

    private static native void nativeCloseTransaction();

    private static native long nativeCreate(SurfaceSession surfaceSession, String str, int i, int i2, int i3, int i4) throws OutOfResourcesException;

    private static native IBinder nativeCreateDisplay(String str, boolean z);

    private static native IBinder nativeCreateDisplayFlags(String str, boolean z, int i);

    private static native void nativeDestroy(long j);

    private static native void nativeDestroyDisplay(IBinder iBinder);

    private static native int nativeGetActiveConfig(IBinder iBinder);

    private static native boolean nativeGetAnimationFrameStats(WindowAnimationFrameStats windowAnimationFrameStats);

    private static native IBinder nativeGetBuiltInDisplay(int i);

    private static native boolean nativeGetContentFrameStats(long j, WindowContentFrameStats windowContentFrameStats);

    private static native PhysicalDisplayInfo[] nativeGetDisplayConfigs(IBinder iBinder);

    private static native void nativeOpenTransaction();

    private static native void nativeRelease(long j);

    private static native Bitmap nativeScreenshot(IBinder iBinder, Rect rect, int i, int i2, int i3, int i4, boolean z, boolean z2, int i5);

    private static native void nativeScreenshot(IBinder iBinder, Surface surface, Rect rect, int i, int i2, int i3, int i4, boolean z, boolean z2);

    private static native boolean nativeSetActiveConfig(IBinder iBinder, int i);

    private static native void nativeSetAlpha(long j, float f);

    private static native void nativeSetAnimationTransaction();

    private static native void nativeSetDisplayLayerStack(IBinder iBinder, int i);

    private static native void nativeSetDisplayPowerMode(IBinder iBinder, int i);

    private static native void nativeSetDisplayProjection(IBinder iBinder, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9);

    private static native void nativeSetDisplaySize(IBinder iBinder, int i, int i2);

    private static native void nativeSetDisplaySurface(IBinder iBinder, long j);

    private static native void nativeSetFlags(long j, int i, int i2);

    private static native void nativeSetLayer(long j, int i);

    private static native void nativeSetLayerStack(long j, int i);

    private static native void nativeSetMatrix(long j, float f, float f2, float f3, float f4);

    private static native void nativeSetPosition(long j, float f, float f2);

    private static native void nativeSetSize(long j, int i, int i2);

    private static native void nativeSetTransparentRegionHint(long j, Region region);

    private static native void nativeSetVRCinemaMode(long j, boolean z);

    private static native void nativeSetWindowCrop(long j, int i, int i2, int i3, int i4);

    public SurfaceControl(SurfaceSession session, String name, int w, int h, int format, int flags) throws OutOfResourcesException {
        if (session == null) {
            throw new IllegalArgumentException("session must not be null");
        } else if (name == null) {
            throw new IllegalArgumentException("name must not be null");
        } else {
            if ((flags & 4) == 0) {
                Log.w(TAG, "Surfaces should always be created with the HIDDEN flag set to ensure that they are not made visible prematurely before all of the surface's properties have been configured.  Set the other properties and make the surface visible within a transaction.  New surface name: " + name, new Throwable());
            }
            this.mName = name;
            this.mNativeObject = nativeCreate(session, name, w, h, format, flags);
            if (this.mNativeObject == 0) {
                throw new OutOfResourcesException("Couldn't allocate SurfaceControl native object");
            }
            this.mCloseGuard.open("release");
        }
    }

    protected void finalize() throws Throwable {
        try {
            if (this.mCloseGuard != null) {
                this.mCloseGuard.warnIfOpen();
            }
            if (this.mNativeObject != 0) {
                nativeRelease(this.mNativeObject);
            }
            super.finalize();
        } catch (Throwable th) {
            super.finalize();
        }
    }

    public String toString() {
        return "Surface(name=" + this.mName + ")";
    }

    public void release() {
        if (this.mNativeObject != 0) {
            nativeRelease(this.mNativeObject);
            this.mNativeObject = 0;
        }
        this.mCloseGuard.close();
    }

    public void destroy() {
        if (this.mNativeObject != 0) {
            nativeDestroy(this.mNativeObject);
            this.mNativeObject = 0;
        }
        this.mCloseGuard.close();
    }

    private void checkNotReleased() {
        if (this.mNativeObject == 0) {
            throw new NullPointerException("mNativeObject is null. Have you called release() already?");
        }
    }

    public static void openTransaction() {
        nativeOpenTransaction();
    }

    public static void closeTransaction() {
        nativeCloseTransaction();
    }

    public static void setAnimationTransaction() {
        nativeSetAnimationTransaction();
    }

    public void setLayer(int zorder) {
        checkNotReleased();
        nativeSetLayer(this.mNativeObject, zorder);
    }

    public void setPosition(float x, float y) {
        checkNotReleased();
        nativeSetPosition(this.mNativeObject, x, y);
    }

    public void setSize(int w, int h) {
        if (w < 0 || h < 0) {
            Slog.wtf(TAG, "setSize by illegal argument (w=" + w + ", h=" + h + ") : mName=" + this.mName);
        }
        checkNotReleased();
        nativeSetSize(this.mNativeObject, w, h);
    }

    public void hide() {
        checkNotReleased();
        nativeSetFlags(this.mNativeObject, 1, 1);
    }

    public void show() {
        checkNotReleased();
        nativeSetFlags(this.mNativeObject, 0, 1);
    }

    public void setTransparentRegionHint(Region region) {
        checkNotReleased();
        nativeSetTransparentRegionHint(this.mNativeObject, region);
    }

    public boolean clearContentFrameStats() {
        checkNotReleased();
        return nativeClearContentFrameStats(this.mNativeObject);
    }

    public boolean getContentFrameStats(WindowContentFrameStats outStats) {
        checkNotReleased();
        return nativeGetContentFrameStats(this.mNativeObject, outStats);
    }

    public static boolean clearAnimationFrameStats() {
        return nativeClearAnimationFrameStats();
    }

    public static boolean getAnimationFrameStats(WindowAnimationFrameStats outStats) {
        return nativeGetAnimationFrameStats(outStats);
    }

    public void setAlpha(float alpha) {
        checkNotReleased();
        nativeSetAlpha(this.mNativeObject, alpha);
    }

    public void setMatrix(float dsdx, float dtdx, float dsdy, float dtdy) {
        checkNotReleased();
        nativeSetMatrix(this.mNativeObject, dsdx, dtdx, dsdy, dtdy);
    }

    public void setWindowCrop(Rect crop) {
        checkNotReleased();
        if (crop != null) {
            nativeSetWindowCrop(this.mNativeObject, crop.left, crop.top, crop.right, crop.bottom);
        } else {
            nativeSetWindowCrop(this.mNativeObject, 0, 0, 0, 0);
        }
    }

    public void setLayerStack(int layerStack) {
        checkNotReleased();
        nativeSetLayerStack(this.mNativeObject, layerStack);
    }

    public void setOpaque(boolean isOpaque) {
        checkNotReleased();
        if (isOpaque) {
            nativeSetFlags(this.mNativeObject, 2, 2);
        } else {
            nativeSetFlags(this.mNativeObject, 0, 2);
        }
    }

    public void setSecure(boolean isSecure) {
        checkNotReleased();
        if (isSecure) {
            nativeSetFlags(this.mNativeObject, 128, 128);
        } else {
            nativeSetFlags(this.mNativeObject, 0, 128);
        }
    }

    public static void setDisplayPowerMode(IBinder displayToken, int mode) {
        if (displayToken == null) {
            throw new IllegalArgumentException("displayToken must not be null");
        }
        nativeSetDisplayPowerMode(displayToken, mode);
    }

    public static PhysicalDisplayInfo[] getDisplayConfigs(IBinder displayToken) {
        if (displayToken != null) {
            return nativeGetDisplayConfigs(displayToken);
        }
        throw new IllegalArgumentException("displayToken must not be null");
    }

    public static int getActiveConfig(IBinder displayToken) {
        if (displayToken != null) {
            return nativeGetActiveConfig(displayToken);
        }
        throw new IllegalArgumentException("displayToken must not be null");
    }

    public static boolean setActiveConfig(IBinder displayToken, int id) {
        if (displayToken != null) {
            return nativeSetActiveConfig(displayToken, id);
        }
        throw new IllegalArgumentException("displayToken must not be null");
    }

    public static void setDisplayProjection(IBinder displayToken, int orientation, Rect layerStackRect, Rect displayRect) {
        if (displayToken == null) {
            throw new IllegalArgumentException("displayToken must not be null");
        } else if (layerStackRect == null) {
            throw new IllegalArgumentException("layerStackRect must not be null");
        } else if (displayRect == null) {
            throw new IllegalArgumentException("displayRect must not be null");
        } else {
            nativeSetDisplayProjection(displayToken, orientation, layerStackRect.left, layerStackRect.top, layerStackRect.right, layerStackRect.bottom, displayRect.left, displayRect.top, displayRect.right, displayRect.bottom);
        }
    }

    public static void setDisplayLayerStack(IBinder displayToken, int layerStack) {
        if (displayToken == null) {
            throw new IllegalArgumentException("displayToken must not be null");
        }
        nativeSetDisplayLayerStack(displayToken, layerStack);
    }

    public static void setDisplaySurface(IBinder displayToken, Surface surface) {
        if (displayToken == null) {
            throw new IllegalArgumentException("displayToken must not be null");
        } else if (surface != null) {
            synchronized (surface.mLock) {
                nativeSetDisplaySurface(displayToken, surface.mNativeObject);
            }
        } else {
            nativeSetDisplaySurface(displayToken, 0);
        }
    }

    public static void setDisplaySize(IBinder displayToken, int width, int height) {
        if (displayToken == null) {
            throw new IllegalArgumentException("displayToken must not be null");
        } else if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("width and height must be positive");
        } else {
            nativeSetDisplaySize(displayToken, width, height);
        }
    }

    public static IBinder createDisplay(String name, boolean secure) {
        if (name != null) {
            return nativeCreateDisplay(name, secure);
        }
        throw new IllegalArgumentException("name must not be null");
    }

    public static IBinder createDisplay(String name, boolean secure, int flags) {
        if (name != null) {
            return nativeCreateDisplayFlags(name, secure, flags);
        }
        throw new IllegalArgumentException("name must not be null");
    }

    public static void destroyDisplay(IBinder displayToken) {
        if (displayToken == null) {
            throw new IllegalArgumentException("displayToken must not be null");
        }
        nativeDestroyDisplay(displayToken);
    }

    public static IBinder getBuiltInDisplay(int builtInDisplayId) {
        return nativeGetBuiltInDisplay(builtInDisplayId);
    }

    public static void screenshot(IBinder display, Surface consumer, int width, int height, int minLayer, int maxLayer, boolean useIdentityTransform) {
        screenshot(display, consumer, new Rect(), width, height, minLayer, maxLayer, false, useIdentityTransform);
    }

    public static void screenshot(IBinder display, Surface consumer, int width, int height) {
        screenshot(display, consumer, new Rect(), width, height, 0, 0, true, false);
    }

    public static void screenshot(IBinder display, Surface consumer) {
        screenshot(display, consumer, new Rect(), 0, 0, 0, 0, true, false);
    }

    public static Bitmap screenshot(Rect sourceCrop, int width, int height, int minLayer, int maxLayer, boolean useIdentityTransform, int rotation) {
        return screenshot(sourceCrop, width, height, minLayer, maxLayer, useIdentityTransform, rotation, 0);
    }

    public static Bitmap screenshot(Rect sourceCrop, int width, int height, int minLayer, int maxLayer, boolean useIdentityTransform, int rotation, int displayId) {
        return nativeScreenshot(getBuiltInDisplay(0), sourceCrop, width, height, minLayer, maxLayer, false, useIdentityTransform, rotation);
    }

    public static Bitmap screenshot(int width, int height) {
        return nativeScreenshot(getBuiltInDisplay(0), new Rect(), width, height, 0, 0, true, false, 0);
    }

    private static void screenshot(IBinder display, Surface consumer, Rect sourceCrop, int width, int height, int minLayer, int maxLayer, boolean allLayers, boolean useIdentityTransform) {
        if (display == null) {
            throw new IllegalArgumentException("displayToken must not be null");
        } else if (consumer == null) {
            throw new IllegalArgumentException("consumer must not be null");
        } else {
            nativeScreenshot(display, consumer, sourceCrop, width, height, minLayer, maxLayer, allLayers, useIdentityTransform);
        }
    }

    public void setVRCinemaMode(boolean bVRCinemaMode) {
        nativeSetVRCinemaMode(this.mNativeObject, bVRCinemaMode);
    }
}
