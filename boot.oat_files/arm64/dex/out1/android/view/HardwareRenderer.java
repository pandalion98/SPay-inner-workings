package android.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.Surface.OutOfResourcesException;
import java.io.File;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public abstract class HardwareRenderer {
    private static final String CACHE_PATH_SHADERS = "com.android.opengl.shaders_cache";
    public static final String DEBUG_DIRTY_REGIONS_PROPERTY = "debug.hwui.show_dirty_regions";
    public static final String DEBUG_OVERDRAW_PROPERTY = "debug.hwui.overdraw";
    public static final String DEBUG_SHOW_LAYERS_UPDATES_PROPERTY = "debug.hwui.show_layers_updates";
    public static final String DEBUG_SHOW_NON_RECTANGULAR_CLIP_PROPERTY = "debug.hwui.show_non_rect_clip";
    static final String LOG_TAG = "HardwareRenderer";
    public static final String OVERDRAW_PROPERTY_SHOW = "show";
    static final String PRINT_CONFIG_PROPERTY = "debug.hwui.print_config";
    static final String PROFILE_MAXFRAMES_PROPERTY = "debug.hwui.profile.maxframes";
    public static final String PROFILE_PROPERTY = "debug.hwui.profile";
    public static final String PROFILE_PROPERTY_VISUALIZE_BARS = "visual_bars";
    static final String RENDER_DIRTY_REGIONS_PROPERTY = "debug.hwui.render_dirty_regions";
    public static boolean sRendererDisabled = false;
    public static boolean sSystemRendererDisabled = false;
    public static boolean sTrimForeground = false;
    private boolean mEnabled;
    private boolean mRequested = true;

    interface HardwareDrawCallbacks {
        void onHardwarePostDraw(DisplayListCanvas displayListCanvas);

        void onHardwarePreDraw(DisplayListCanvas displayListCanvas);
    }

    abstract void buildLayer(RenderNode renderNode);

    abstract boolean copyLayerInto(HardwareLayer hardwareLayer, Bitmap bitmap);

    abstract HardwareLayer createTextureLayer();

    abstract void destroy();

    abstract void destroyHardwareResources(View view);

    abstract void detachSurfaceTexture(long j);

    abstract void draw(View view, AttachInfo attachInfo, HardwareDrawCallbacks hardwareDrawCallbacks);

    abstract void dumpGfxInfo(PrintWriter printWriter, FileDescriptor fileDescriptor, String[] strArr);

    abstract void fence();

    abstract int getHeight();

    abstract int getWidth();

    abstract boolean initialize(Surface surface) throws OutOfResourcesException;

    abstract boolean initialize(Surface surface, boolean z, int i, double d) throws OutOfResourcesException;

    abstract void invalidate(Surface surface);

    abstract void invalidateRoot();

    abstract boolean loadSystemProperties();

    abstract void notifyFramePending();

    abstract void onLayerDestroyed(HardwareLayer hardwareLayer);

    abstract boolean pauseSurface(Surface surface);

    abstract void pushLayerUpdate(HardwareLayer hardwareLayer);

    abstract void registerAnimatingRenderNode(RenderNode renderNode);

    abstract void setLightCenter(AttachInfo attachInfo);

    abstract void setName(String str);

    abstract void setOpaque(boolean z);

    abstract void setup(int i, int i2, AttachInfo attachInfo, Rect rect);

    abstract void stopDrawing();

    abstract void updateSurface(Surface surface) throws OutOfResourcesException;

    public static void disable(boolean system) {
        sRendererDisabled = true;
        if (system) {
            sSystemRendererDisabled = true;
        }
    }

    public static void enableForegroundTrimming() {
        sTrimForeground = true;
    }

    public static boolean isAvailable() {
        return DisplayListCanvas.isAvailable();
    }

    public static void setupDiskCache(File cacheDir) {
        ThreadedRenderer.setupShadersDiskCache(new File(cacheDir, CACHE_PATH_SHADERS).getAbsolutePath());
    }

    public static void setLibDir(String libDir) {
        ThreadedRenderer.setupVulkanLayerPath(libDir);
    }

    boolean initializeIfNeeded(int width, int height, AttachInfo attachInfo, Surface surface, Rect surfaceInsets) throws OutOfResourcesException {
        if (!isRequested() || isEnabled() || !initialize(surface)) {
            return false;
        }
        setup(width, height, attachInfo, surfaceInsets);
        return true;
    }

    static HardwareRenderer create(Context context, boolean translucent) {
        return create(context, translucent, false);
    }

    static HardwareRenderer create(Context context, boolean translucent, boolean demoted) {
        if (DisplayListCanvas.isAvailable()) {
            return new ThreadedRenderer(context, translucent, demoted);
        }
        return null;
    }

    static void trimMemory(int level) {
        ThreadedRenderer.trimMemory(level);
    }

    boolean isEnabled() {
        return this.mEnabled;
    }

    void setEnabled(boolean enabled) {
        this.mEnabled = enabled;
    }

    boolean isRequested() {
        return this.mRequested;
    }

    void setRequested(boolean requested) {
        this.mRequested = requested;
    }
}
