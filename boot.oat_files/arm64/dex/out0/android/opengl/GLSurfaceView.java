package android.opengl;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.SystemProperties;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import java.io.Writer;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

public class GLSurfaceView extends SurfaceView implements Callback {
    private static final boolean DCS_IS_EXYNOS = SystemProperties.get("ro.board.platform").contains("exynos");
    public static final int DEBUG_CHECK_GL_ERROR = 1;
    private static final boolean DEBUG_DCS = "eng".equals(Build.TYPE);
    public static final int DEBUG_LOG_GL_CALLS = 2;
    private static final boolean DYNAMIC_COLOR_SCALING_IS_ENABLED = true;
    private static final boolean LOG_ATTACH_DETACH = false;
    private static final boolean LOG_EGL = false;
    private static final boolean LOG_PAUSE_RESUME = false;
    private static final boolean LOG_RENDERER = false;
    private static final boolean LOG_RENDERER_DRAW_FRAME = false;
    private static final boolean LOG_SURFACE = false;
    private static final boolean LOG_THREADS = false;
    public static final int RENDERMODE_CONTINUOUSLY = 1;
    public static final int RENDERMODE_WHEN_DIRTY = 0;
    private static final String TAG = "GLSurfaceView";
    private static final int[][] pixelConfig = new int[][]{new int[]{5, 6, 5, 0}, new int[]{5, 5, 5, 1}, new int[]{4, 4, 4, 4}, new int[]{8, 8, 8, 8}};
    private static final GLThreadManager sGLThreadManager = new GLThreadManager();
    private int mDebugFlags;
    private boolean mDetached;
    private EGLConfigChooser mEGLConfigChooser;
    private int mEGLContextClientVersion;
    private EGLContextFactory mEGLContextFactory;
    private EGLWindowSurfaceFactory mEGLWindowSurfaceFactory;
    private GLThread mGLThread;
    private GLWrapper mGLWrapper;
    private boolean mPreserveEGLContextOnPause;
    private Renderer mRenderer;
    private final WeakReference<GLSurfaceView> mThisWeakRef = new WeakReference(this);

    public interface EGLConfigChooser {
        EGLConfig chooseConfig(EGL10 egl10, EGLDisplay eGLDisplay);
    }

    private abstract class BaseConfigChooser implements EGLConfigChooser {
        protected int[] mConfigSpec;

        abstract EGLConfig chooseConfig(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig[] eGLConfigArr);

        public BaseConfigChooser(int[] configSpec) {
            this.mConfigSpec = filterConfigSpec(configSpec);
        }

        public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
            int[] num_config = new int[1];
            if (egl.eglChooseConfig(display, this.mConfigSpec, null, 0, num_config)) {
                int numConfigs = num_config[0];
                if (numConfigs <= 0) {
                    throw new IllegalArgumentException("No configs match configSpec");
                }
                EGLConfig[] configs = new EGLConfig[numConfigs];
                if (egl.eglChooseConfig(display, this.mConfigSpec, configs, numConfigs, num_config)) {
                    EGLConfig config = chooseConfig(egl, display, configs);
                    if (config != null) {
                        return config;
                    }
                    throw new IllegalArgumentException("No config chosen");
                }
                throw new IllegalArgumentException("eglChooseConfig#2 failed");
            }
            throw new IllegalArgumentException("eglChooseConfig failed");
        }

        private int[] filterConfigSpec(int[] configSpec) {
            if (GLSurfaceView.this.mEGLContextClientVersion != 2 && GLSurfaceView.this.mEGLContextClientVersion != 3) {
                return configSpec;
            }
            int len = configSpec.length;
            int[] newConfigSpec = new int[(len + 2)];
            System.arraycopy(configSpec, 0, newConfigSpec, 0, len - 1);
            newConfigSpec[len - 1] = EGL14.EGL_RENDERABLE_TYPE;
            if (GLSurfaceView.this.mEGLContextClientVersion == 2) {
                newConfigSpec[len] = 4;
            } else {
                newConfigSpec[len] = 64;
            }
            newConfigSpec[len + 1] = EGL14.EGL_NONE;
            return newConfigSpec;
        }
    }

    private class ComponentSizeChooser extends BaseConfigChooser {
        protected int mAlphaSize;
        protected int mBlueSize;
        protected int mDepthSize;
        protected int mGreenSize;
        protected int mRedSize;
        protected int mStencilSize;
        private int[] mValue = new int[1];

        public ComponentSizeChooser(int redSize, int greenSize, int blueSize, int alphaSize, int depthSize, int stencilSize) {
            super(new int[]{EGL14.EGL_RED_SIZE, redSize, EGL14.EGL_GREEN_SIZE, greenSize, EGL14.EGL_BLUE_SIZE, blueSize, EGL14.EGL_ALPHA_SIZE, alphaSize, EGL14.EGL_DEPTH_SIZE, depthSize, EGL14.EGL_STENCIL_SIZE, stencilSize, EGL14.EGL_NONE});
            this.mRedSize = redSize;
            this.mGreenSize = greenSize;
            this.mBlueSize = blueSize;
            this.mAlphaSize = alphaSize;
            this.mDepthSize = depthSize;
            this.mStencilSize = stencilSize;
        }

        public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display, EGLConfig[] configs) {
            for (EGLConfig config : configs) {
                int d = findConfigAttrib(egl, display, config, EGL14.EGL_DEPTH_SIZE, 0);
                int s = findConfigAttrib(egl, display, config, EGL14.EGL_STENCIL_SIZE, 0);
                if (d >= this.mDepthSize && s >= this.mStencilSize) {
                    int r = findConfigAttrib(egl, display, config, EGL14.EGL_RED_SIZE, 0);
                    int g = findConfigAttrib(egl, display, config, EGL14.EGL_GREEN_SIZE, 0);
                    int b = findConfigAttrib(egl, display, config, EGL14.EGL_BLUE_SIZE, 0);
                    int a = findConfigAttrib(egl, display, config, EGL14.EGL_ALPHA_SIZE, 0);
                    if (r == this.mRedSize && g == this.mGreenSize && b == this.mBlueSize && a == this.mAlphaSize) {
                        return config;
                    }
                }
            }
            return null;
        }

        private int findConfigAttrib(EGL10 egl, EGLDisplay display, EGLConfig config, int attribute, int defaultValue) {
            if (egl.eglGetConfigAttrib(display, config, attribute, this.mValue)) {
                return this.mValue[0];
            }
            return defaultValue;
        }
    }

    public interface EGLContextFactory {
        EGLContext createContext(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig);

        void destroyContext(EGL10 egl10, EGLDisplay eGLDisplay, EGLContext eGLContext);
    }

    private class DefaultContextFactory implements EGLContextFactory {
        private int EGL_CONTEXT_CLIENT_VERSION;

        private DefaultContextFactory() {
            this.EGL_CONTEXT_CLIENT_VERSION = 12440;
        }

        public EGLContext createContext(EGL10 egl, EGLDisplay display, EGLConfig config) {
            int[] attrib_list = new int[]{this.EGL_CONTEXT_CLIENT_VERSION, GLSurfaceView.this.mEGLContextClientVersion, EGL14.EGL_NONE};
            EGLContext eGLContext = EGL10.EGL_NO_CONTEXT;
            if (GLSurfaceView.this.mEGLContextClientVersion == 0) {
                attrib_list = null;
            }
            return egl.eglCreateContext(display, config, eGLContext, attrib_list);
        }

        public void destroyContext(EGL10 egl, EGLDisplay display, EGLContext context) {
            if (!egl.eglDestroyContext(display, context)) {
                Log.e("DefaultContextFactory", "display:" + display + " context: " + context);
                EglHelper.throwEglException("eglDestroyContex", egl.eglGetError());
            }
        }
    }

    public interface EGLWindowSurfaceFactory {
        EGLSurface createWindowSurface(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig, Object obj);

        void destroySurface(EGL10 egl10, EGLDisplay eGLDisplay, EGLSurface eGLSurface);
    }

    private static class DefaultWindowSurfaceFactory implements EGLWindowSurfaceFactory {
        private DefaultWindowSurfaceFactory() {
        }

        public EGLSurface createWindowSurface(EGL10 egl, EGLDisplay display, EGLConfig config, Object nativeWindow) {
            EGLSurface result = null;
            try {
                result = egl.eglCreateWindowSurface(display, config, nativeWindow, null);
            } catch (IllegalArgumentException e) {
                Log.e(GLSurfaceView.TAG, "eglCreateWindowSurface", e);
            }
            return result;
        }

        public void destroySurface(EGL10 egl, EGLDisplay display, EGLSurface surface) {
            egl.eglDestroySurface(display, surface);
        }
    }

    private static class EglHelper {
        EGL10 mEgl;
        EGLConfig mEglConfig;
        EGLContext mEglContext;
        EGLDisplay mEglDisplay;
        EGLSurface mEglSurface;
        private WeakReference<GLSurfaceView> mGLSurfaceViewWeakRef;

        public EglHelper(WeakReference<GLSurfaceView> glSurfaceViewWeakRef) {
            this.mGLSurfaceViewWeakRef = glSurfaceViewWeakRef;
        }

        public void start() {
            this.mEgl = (EGL10) EGLContext.getEGL();
            this.mEglDisplay = this.mEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
            if (this.mEglDisplay == EGL10.EGL_NO_DISPLAY) {
                throw new RuntimeException("eglGetDisplay failed");
            }
            if (this.mEgl.eglInitialize(this.mEglDisplay, new int[2])) {
                GLSurfaceView view = (GLSurfaceView) this.mGLSurfaceViewWeakRef.get();
                if (view == null) {
                    this.mEglConfig = null;
                    this.mEglContext = null;
                } else {
                    this.mEglConfig = view.mEGLConfigChooser.chooseConfig(this.mEgl, this.mEglDisplay);
                    this.mEglContext = view.mEGLContextFactory.createContext(this.mEgl, this.mEglDisplay, this.mEglConfig);
                }
                if (this.mEglContext == null || this.mEglContext == EGL10.EGL_NO_CONTEXT) {
                    this.mEglContext = null;
                    throwEglException("createContext");
                }
                this.mEglSurface = null;
                return;
            }
            throw new RuntimeException("eglInitialize failed");
        }

        public boolean createSurface() {
            if (this.mEgl == null) {
                throw new RuntimeException("egl not initialized");
            } else if (this.mEglDisplay == null) {
                throw new RuntimeException("eglDisplay not initialized");
            } else if (this.mEglConfig == null) {
                throw new RuntimeException("mEglConfig not initialized");
            } else {
                destroySurfaceImp();
                GLSurfaceView view = (GLSurfaceView) this.mGLSurfaceViewWeakRef.get();
                if (view != null) {
                    this.mEglSurface = view.mEGLWindowSurfaceFactory.createWindowSurface(this.mEgl, this.mEglDisplay, this.mEglConfig, view.getHolder());
                } else {
                    this.mEglSurface = null;
                }
                if (this.mEglSurface == null || this.mEglSurface == EGL10.EGL_NO_SURFACE) {
                    if (this.mEgl.eglGetError() != 12299) {
                        return false;
                    }
                    Log.e("EglHelper", "createWindowSurface returned EGL_BAD_NATIVE_WINDOW.");
                    return false;
                } else if (this.mEgl.eglMakeCurrent(this.mEglDisplay, this.mEglSurface, this.mEglSurface, this.mEglContext)) {
                    return true;
                } else {
                    logEglErrorAsWarning("EGLHelper", "eglMakeCurrent", this.mEgl.eglGetError());
                    return false;
                }
            }
        }

        GL createGL() {
            GL gl = this.mEglContext.getGL();
            GLSurfaceView view = (GLSurfaceView) this.mGLSurfaceViewWeakRef.get();
            if (view == null) {
                return gl;
            }
            if (view.mGLWrapper != null) {
                gl = view.mGLWrapper.wrap(gl);
            }
            if ((view.mDebugFlags & 3) == 0) {
                return gl;
            }
            int configFlags = 0;
            Writer log = null;
            if ((view.mDebugFlags & 1) != 0) {
                configFlags = 0 | 1;
            }
            if ((view.mDebugFlags & 2) != 0) {
                log = new LogWriter();
            }
            return GLDebugHelper.wrap(gl, configFlags, log);
        }

        public int swap() {
            if (this.mEgl.eglSwapBuffers(this.mEglDisplay, this.mEglSurface)) {
                return 12288;
            }
            return this.mEgl.eglGetError();
        }

        public void destroySurface() {
            destroySurfaceImp();
        }

        private void destroySurfaceImp() {
            if (this.mEglSurface != null && this.mEglSurface != EGL10.EGL_NO_SURFACE) {
                this.mEgl.eglMakeCurrent(this.mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
                GLSurfaceView view = (GLSurfaceView) this.mGLSurfaceViewWeakRef.get();
                if (view != null) {
                    view.mEGLWindowSurfaceFactory.destroySurface(this.mEgl, this.mEglDisplay, this.mEglSurface);
                }
                this.mEglSurface = null;
            }
        }

        public void finish() {
            if (this.mEglContext != null) {
                GLSurfaceView view = (GLSurfaceView) this.mGLSurfaceViewWeakRef.get();
                if (view != null) {
                    view.mEGLContextFactory.destroyContext(this.mEgl, this.mEglDisplay, this.mEglContext);
                }
                this.mEglContext = null;
            }
            if (this.mEglDisplay != null) {
                this.mEgl.eglTerminate(this.mEglDisplay);
                this.mEglDisplay = null;
            }
        }

        private void throwEglException(String function) {
            throwEglException(function, this.mEgl.eglGetError());
        }

        public static void throwEglException(String function, int error) {
            throw new RuntimeException(formatEglError(function, error));
        }

        public static void logEglErrorAsWarning(String tag, String function, int error) {
            Log.w(tag, formatEglError(function, error));
        }

        public static String formatEglError(String function, int error) {
            return function + " failed: " + EGLLogWrapper.getErrorString(error);
        }
    }

    static class GLThread extends Thread {
        private EglHelper mEglHelper;
        private ArrayList<Runnable> mEventQueue = new ArrayList();
        private boolean mExited;
        private boolean mFinishedCreatingEglSurface;
        private WeakReference<GLSurfaceView> mGLSurfaceViewWeakRef;
        private boolean mHasSurface;
        private boolean mHaveEglContext;
        private boolean mHaveEglSurface;
        private int mHeight = 0;
        private boolean mPaused;
        private boolean mRenderComplete;
        private int mRenderMode = 1;
        private boolean mRequestPaused;
        private boolean mRequestRender = true;
        private boolean mShouldExit;
        private boolean mShouldReleaseEglContext;
        private boolean mSizeChanged = true;
        private boolean mSurfaceIsBad;
        private boolean mWaitingForSurface;
        private int mWidth = 0;

        GLThread(WeakReference<GLSurfaceView> glSurfaceViewWeakRef) {
            this.mGLSurfaceViewWeakRef = glSurfaceViewWeakRef;
        }

        public void run() {
            setName("GLThread " + getId());
            try {
                guardedRun();
            } catch (InterruptedException e) {
            } finally {
                GLSurfaceView.sGLThreadManager.threadExiting(this);
            }
        }

        private void stopEglSurfaceLocked() {
            if (this.mHaveEglSurface) {
                this.mHaveEglSurface = false;
                this.mEglHelper.destroySurface();
            }
        }

        private void stopEglContextLocked() {
            if (this.mHaveEglContext) {
                this.mEglHelper.finish();
                this.mHaveEglContext = false;
                GLSurfaceView.sGLThreadManager.releaseEglContextLocked(this);
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void guardedRun() throws java.lang.InterruptedException {
            /*
            r24 = this;
            r21 = new android.opengl.GLSurfaceView$EglHelper;
            r0 = r24;
            r0 = r0.mGLSurfaceViewWeakRef;
            r22 = r0;
            r21.<init>(r22);
            r0 = r21;
            r1 = r24;
            r1.mEglHelper = r0;
            r21 = 0;
            r0 = r21;
            r1 = r24;
            r1.mHaveEglContext = r0;
            r21 = 0;
            r0 = r21;
            r1 = r24;
            r1.mHaveEglSurface = r0;
            r10 = 0;
            r5 = 0;
            r6 = 0;
            r7 = 0;
            r12 = 0;
            r15 = 0;
            r20 = 0;
            r8 = 0;
            r4 = 0;
            r19 = 0;
            r11 = 0;
            r9 = 0;
        L_0x002f:
            r22 = android.opengl.GLSurfaceView.sGLThreadManager;	 Catch:{ all -> 0x01d5 }
            monitor-enter(r22);	 Catch:{ all -> 0x01d5 }
        L_0x0034:
            r0 = r24;
            r0 = r0.mShouldExit;	 Catch:{ all -> 0x01d2 }
            r21 = r0;
            if (r21 == 0) goto L_0x004d;
        L_0x003c:
            monitor-exit(r22);	 Catch:{ all -> 0x01d2 }
            r22 = android.opengl.GLSurfaceView.sGLThreadManager;
            monitor-enter(r22);
            r24.stopEglSurfaceLocked();	 Catch:{ all -> 0x004a }
            r24.stopEglContextLocked();	 Catch:{ all -> 0x004a }
            monitor-exit(r22);	 Catch:{ all -> 0x004a }
            return;
        L_0x004a:
            r21 = move-exception;
            monitor-exit(r22);	 Catch:{ all -> 0x004a }
            throw r21;
        L_0x004d:
            r0 = r24;
            r0 = r0.mEventQueue;	 Catch:{ all -> 0x01d2 }
            r21 = r0;
            r21 = r21.isEmpty();	 Catch:{ all -> 0x01d2 }
            if (r21 != 0) goto L_0x0076;
        L_0x0059:
            r0 = r24;
            r0 = r0.mEventQueue;	 Catch:{ all -> 0x01d2 }
            r21 = r0;
            r23 = 0;
            r0 = r21;
            r1 = r23;
            r21 = r0.remove(r1);	 Catch:{ all -> 0x01d2 }
            r0 = r21;
            r0 = (java.lang.Runnable) r0;	 Catch:{ all -> 0x01d2 }
            r9 = r0;
        L_0x006e:
            monitor-exit(r22);	 Catch:{ all -> 0x01d2 }
            if (r9 == 0) goto L_0x0228;
        L_0x0071:
            r9.run();	 Catch:{ all -> 0x01d5 }
            r9 = 0;
            goto L_0x002f;
        L_0x0076:
            r13 = 0;
            r0 = r24;
            r0 = r0.mPaused;	 Catch:{ all -> 0x01d2 }
            r21 = r0;
            r0 = r24;
            r0 = r0.mRequestPaused;	 Catch:{ all -> 0x01d2 }
            r23 = r0;
            r0 = r21;
            r1 = r23;
            if (r0 == r1) goto L_0x00a0;
        L_0x0089:
            r0 = r24;
            r13 = r0.mRequestPaused;	 Catch:{ all -> 0x01d2 }
            r0 = r24;
            r0 = r0.mRequestPaused;	 Catch:{ all -> 0x01d2 }
            r21 = r0;
            r0 = r21;
            r1 = r24;
            r1.mPaused = r0;	 Catch:{ all -> 0x01d2 }
            r21 = android.opengl.GLSurfaceView.sGLThreadManager;	 Catch:{ all -> 0x01d2 }
            r21.notifyAll();	 Catch:{ all -> 0x01d2 }
        L_0x00a0:
            r0 = r24;
            r0 = r0.mShouldReleaseEglContext;	 Catch:{ all -> 0x01d2 }
            r21 = r0;
            if (r21 == 0) goto L_0x00b7;
        L_0x00a8:
            r24.stopEglSurfaceLocked();	 Catch:{ all -> 0x01d2 }
            r24.stopEglContextLocked();	 Catch:{ all -> 0x01d2 }
            r21 = 0;
            r0 = r21;
            r1 = r24;
            r1.mShouldReleaseEglContext = r0;	 Catch:{ all -> 0x01d2 }
            r4 = 1;
        L_0x00b7:
            if (r12 == 0) goto L_0x00c0;
        L_0x00b9:
            r24.stopEglSurfaceLocked();	 Catch:{ all -> 0x01d2 }
            r24.stopEglContextLocked();	 Catch:{ all -> 0x01d2 }
            r12 = 0;
        L_0x00c0:
            if (r13 == 0) goto L_0x00cd;
        L_0x00c2:
            r0 = r24;
            r0 = r0.mHaveEglSurface;	 Catch:{ all -> 0x01d2 }
            r21 = r0;
            if (r21 == 0) goto L_0x00cd;
        L_0x00ca:
            r24.stopEglSurfaceLocked();	 Catch:{ all -> 0x01d2 }
        L_0x00cd:
            if (r13 == 0) goto L_0x00f5;
        L_0x00cf:
            r0 = r24;
            r0 = r0.mHaveEglContext;	 Catch:{ all -> 0x01d2 }
            r21 = r0;
            if (r21 == 0) goto L_0x00f5;
        L_0x00d7:
            r0 = r24;
            r0 = r0.mGLSurfaceViewWeakRef;	 Catch:{ all -> 0x01d2 }
            r21 = r0;
            r18 = r21.get();	 Catch:{ all -> 0x01d2 }
            r18 = (android.opengl.GLSurfaceView) r18;	 Catch:{ all -> 0x01d2 }
            if (r18 != 0) goto L_0x01e3;
        L_0x00e5:
            r14 = 0;
        L_0x00e6:
            if (r14 == 0) goto L_0x00f2;
        L_0x00e8:
            r21 = android.opengl.GLSurfaceView.sGLThreadManager;	 Catch:{ all -> 0x01d2 }
            r21 = r21.shouldReleaseEGLContextWhenPausing();	 Catch:{ all -> 0x01d2 }
            if (r21 == 0) goto L_0x00f5;
        L_0x00f2:
            r24.stopEglContextLocked();	 Catch:{ all -> 0x01d2 }
        L_0x00f5:
            if (r13 == 0) goto L_0x010a;
        L_0x00f7:
            r21 = android.opengl.GLSurfaceView.sGLThreadManager;	 Catch:{ all -> 0x01d2 }
            r21 = r21.shouldTerminateEGLWhenPausing();	 Catch:{ all -> 0x01d2 }
            if (r21 == 0) goto L_0x010a;
        L_0x0101:
            r0 = r24;
            r0 = r0.mEglHelper;	 Catch:{ all -> 0x01d2 }
            r21 = r0;
            r21.finish();	 Catch:{ all -> 0x01d2 }
        L_0x010a:
            r0 = r24;
            r0 = r0.mHasSurface;	 Catch:{ all -> 0x01d2 }
            r21 = r0;
            if (r21 != 0) goto L_0x013c;
        L_0x0112:
            r0 = r24;
            r0 = r0.mWaitingForSurface;	 Catch:{ all -> 0x01d2 }
            r21 = r0;
            if (r21 != 0) goto L_0x013c;
        L_0x011a:
            r0 = r24;
            r0 = r0.mHaveEglSurface;	 Catch:{ all -> 0x01d2 }
            r21 = r0;
            if (r21 == 0) goto L_0x0125;
        L_0x0122:
            r24.stopEglSurfaceLocked();	 Catch:{ all -> 0x01d2 }
        L_0x0125:
            r21 = 1;
            r0 = r21;
            r1 = r24;
            r1.mWaitingForSurface = r0;	 Catch:{ all -> 0x01d2 }
            r21 = 0;
            r0 = r21;
            r1 = r24;
            r1.mSurfaceIsBad = r0;	 Catch:{ all -> 0x01d2 }
            r21 = android.opengl.GLSurfaceView.sGLThreadManager;	 Catch:{ all -> 0x01d2 }
            r21.notifyAll();	 Catch:{ all -> 0x01d2 }
        L_0x013c:
            r0 = r24;
            r0 = r0.mHasSurface;	 Catch:{ all -> 0x01d2 }
            r21 = r0;
            if (r21 == 0) goto L_0x015b;
        L_0x0144:
            r0 = r24;
            r0 = r0.mWaitingForSurface;	 Catch:{ all -> 0x01d2 }
            r21 = r0;
            if (r21 == 0) goto L_0x015b;
        L_0x014c:
            r21 = 0;
            r0 = r21;
            r1 = r24;
            r1.mWaitingForSurface = r0;	 Catch:{ all -> 0x01d2 }
            r21 = android.opengl.GLSurfaceView.sGLThreadManager;	 Catch:{ all -> 0x01d2 }
            r21.notifyAll();	 Catch:{ all -> 0x01d2 }
        L_0x015b:
            if (r8 == 0) goto L_0x016f;
        L_0x015d:
            r20 = 0;
            r8 = 0;
            r21 = 1;
            r0 = r21;
            r1 = r24;
            r1.mRenderComplete = r0;	 Catch:{ all -> 0x01d2 }
            r21 = android.opengl.GLSurfaceView.sGLThreadManager;	 Catch:{ all -> 0x01d2 }
            r21.notifyAll();	 Catch:{ all -> 0x01d2 }
        L_0x016f:
            r21 = r24.readyToDraw();	 Catch:{ all -> 0x01d2 }
            if (r21 == 0) goto L_0x021f;
        L_0x0175:
            r0 = r24;
            r0 = r0.mHaveEglContext;	 Catch:{ all -> 0x01d2 }
            r21 = r0;
            if (r21 != 0) goto L_0x0180;
        L_0x017d:
            if (r4 == 0) goto L_0x01e9;
        L_0x017f:
            r4 = 0;
        L_0x0180:
            r0 = r24;
            r0 = r0.mHaveEglContext;	 Catch:{ all -> 0x01d2 }
            r21 = r0;
            if (r21 == 0) goto L_0x019b;
        L_0x0188:
            r0 = r24;
            r0 = r0.mHaveEglSurface;	 Catch:{ all -> 0x01d2 }
            r21 = r0;
            if (r21 != 0) goto L_0x019b;
        L_0x0190:
            r21 = 1;
            r0 = r21;
            r1 = r24;
            r1.mHaveEglSurface = r0;	 Catch:{ all -> 0x01d2 }
            r6 = 1;
            r7 = 1;
            r15 = 1;
        L_0x019b:
            r0 = r24;
            r0 = r0.mHaveEglSurface;	 Catch:{ all -> 0x01d2 }
            r21 = r0;
            if (r21 == 0) goto L_0x021f;
        L_0x01a3:
            r0 = r24;
            r0 = r0.mSizeChanged;	 Catch:{ all -> 0x01d2 }
            r21 = r0;
            if (r21 == 0) goto L_0x01c1;
        L_0x01ab:
            r15 = 1;
            r0 = r24;
            r0 = r0.mWidth;	 Catch:{ all -> 0x01d2 }
            r19 = r0;
            r0 = r24;
            r11 = r0.mHeight;	 Catch:{ all -> 0x01d2 }
            r20 = 1;
            r6 = 1;
            r21 = 0;
            r0 = r21;
            r1 = r24;
            r1.mSizeChanged = r0;	 Catch:{ all -> 0x01d2 }
        L_0x01c1:
            r21 = 0;
            r0 = r21;
            r1 = r24;
            r1.mRequestRender = r0;	 Catch:{ all -> 0x01d2 }
            r21 = android.opengl.GLSurfaceView.sGLThreadManager;	 Catch:{ all -> 0x01d2 }
            r21.notifyAll();	 Catch:{ all -> 0x01d2 }
            goto L_0x006e;
        L_0x01d2:
            r21 = move-exception;
            monitor-exit(r22);	 Catch:{ all -> 0x01d2 }
            throw r21;	 Catch:{ all -> 0x01d5 }
        L_0x01d5:
            r21 = move-exception;
            r22 = android.opengl.GLSurfaceView.sGLThreadManager;
            monitor-enter(r22);
            r24.stopEglSurfaceLocked();	 Catch:{ all -> 0x0368 }
            r24.stopEglContextLocked();	 Catch:{ all -> 0x0368 }
            monitor-exit(r22);	 Catch:{ all -> 0x0368 }
            throw r21;
        L_0x01e3:
            r14 = r18.mPreserveEGLContextOnPause;	 Catch:{ all -> 0x01d2 }
            goto L_0x00e6;
        L_0x01e9:
            r21 = android.opengl.GLSurfaceView.sGLThreadManager;	 Catch:{ all -> 0x01d2 }
            r0 = r21;
            r1 = r24;
            r21 = r0.tryAcquireEglContextLocked(r1);	 Catch:{ all -> 0x01d2 }
            if (r21 == 0) goto L_0x0180;
        L_0x01f7:
            r0 = r24;
            r0 = r0.mEglHelper;	 Catch:{ RuntimeException -> 0x0212 }
            r21 = r0;
            r21.start();	 Catch:{ RuntimeException -> 0x0212 }
            r21 = 1;
            r0 = r21;
            r1 = r24;
            r1.mHaveEglContext = r0;	 Catch:{ all -> 0x01d2 }
            r5 = 1;
            r21 = android.opengl.GLSurfaceView.sGLThreadManager;	 Catch:{ all -> 0x01d2 }
            r21.notifyAll();	 Catch:{ all -> 0x01d2 }
            goto L_0x0180;
        L_0x0212:
            r17 = move-exception;
            r21 = android.opengl.GLSurfaceView.sGLThreadManager;	 Catch:{ all -> 0x01d2 }
            r0 = r21;
            r1 = r24;
            r0.releaseEglContextLocked(r1);	 Catch:{ all -> 0x01d2 }
            throw r17;	 Catch:{ all -> 0x01d2 }
        L_0x021f:
            r21 = android.opengl.GLSurfaceView.sGLThreadManager;	 Catch:{ all -> 0x01d2 }
            r21.wait();	 Catch:{ all -> 0x01d2 }
            goto L_0x0034;
        L_0x0228:
            if (r6 == 0) goto L_0x024c;
        L_0x022a:
            r0 = r24;
            r0 = r0.mEglHelper;	 Catch:{ all -> 0x01d5 }
            r21 = r0;
            r21 = r21.createSurface();	 Catch:{ all -> 0x01d5 }
            if (r21 == 0) goto L_0x032c;
        L_0x0236:
            r22 = android.opengl.GLSurfaceView.sGLThreadManager;	 Catch:{ all -> 0x01d5 }
            monitor-enter(r22);	 Catch:{ all -> 0x01d5 }
            r21 = 1;
            r0 = r21;
            r1 = r24;
            r1.mFinishedCreatingEglSurface = r0;	 Catch:{ all -> 0x0329 }
            r21 = android.opengl.GLSurfaceView.sGLThreadManager;	 Catch:{ all -> 0x0329 }
            r21.notifyAll();	 Catch:{ all -> 0x0329 }
            monitor-exit(r22);	 Catch:{ all -> 0x0329 }
            r6 = 0;
        L_0x024c:
            if (r7 == 0) goto L_0x0267;
        L_0x024e:
            r0 = r24;
            r0 = r0.mEglHelper;	 Catch:{ all -> 0x01d5 }
            r21 = r0;
            r21 = r21.createGL();	 Catch:{ all -> 0x01d5 }
            r0 = r21;
            r0 = (javax.microedition.khronos.opengles.GL10) r0;	 Catch:{ all -> 0x01d5 }
            r10 = r0;
            r21 = android.opengl.GLSurfaceView.sGLThreadManager;	 Catch:{ all -> 0x01d5 }
            r0 = r21;
            r0.checkGLDriver(r10);	 Catch:{ all -> 0x01d5 }
            r7 = 0;
        L_0x0267:
            if (r5 == 0) goto L_0x02a0;
        L_0x0269:
            r0 = r24;
            r0 = r0.mGLSurfaceViewWeakRef;	 Catch:{ all -> 0x01d5 }
            r21 = r0;
            r18 = r21.get();	 Catch:{ all -> 0x01d5 }
            r18 = (android.opengl.GLSurfaceView) r18;	 Catch:{ all -> 0x01d5 }
            if (r18 == 0) goto L_0x029f;
        L_0x0277:
            r22 = 8;
            r21 = "onSurfaceCreated";
            r0 = r22;
            r2 = r21;
            android.os.Trace.traceBegin(r0, r2);	 Catch:{ all -> 0x034e }
            r21 = r18.mRenderer;	 Catch:{ all -> 0x034e }
            r0 = r24;
            r0 = r0.mEglHelper;	 Catch:{ all -> 0x034e }
            r22 = r0;
            r0 = r22;
            r0 = r0.mEglConfig;	 Catch:{ all -> 0x034e }
            r22 = r0;
            r0 = r21;
            r1 = r22;
            r0.onSurfaceCreated(r10, r1);	 Catch:{ all -> 0x034e }
            r22 = 8;
            android.os.Trace.traceEnd(r22);	 Catch:{ all -> 0x01d5 }
        L_0x029f:
            r5 = 0;
        L_0x02a0:
            if (r15 == 0) goto L_0x02cd;
        L_0x02a2:
            r0 = r24;
            r0 = r0.mGLSurfaceViewWeakRef;	 Catch:{ all -> 0x01d5 }
            r21 = r0;
            r18 = r21.get();	 Catch:{ all -> 0x01d5 }
            r18 = (android.opengl.GLSurfaceView) r18;	 Catch:{ all -> 0x01d5 }
            if (r18 == 0) goto L_0x02cc;
        L_0x02b0:
            r22 = 8;
            r21 = "onSurfaceChanged";
            r0 = r22;
            r2 = r21;
            android.os.Trace.traceBegin(r0, r2);	 Catch:{ all -> 0x0355 }
            r21 = r18.mRenderer;	 Catch:{ all -> 0x0355 }
            r0 = r21;
            r1 = r19;
            r0.onSurfaceChanged(r10, r1, r11);	 Catch:{ all -> 0x0355 }
            r22 = 8;
            android.os.Trace.traceEnd(r22);	 Catch:{ all -> 0x01d5 }
        L_0x02cc:
            r15 = 0;
        L_0x02cd:
            r0 = r24;
            r0 = r0.mGLSurfaceViewWeakRef;	 Catch:{ all -> 0x01d5 }
            r21 = r0;
            r18 = r21.get();	 Catch:{ all -> 0x01d5 }
            r18 = (android.opengl.GLSurfaceView) r18;	 Catch:{ all -> 0x01d5 }
            if (r18 == 0) goto L_0x02f5;
        L_0x02db:
            r22 = 8;
            r21 = "onDrawFrame";
            r0 = r22;
            r2 = r21;
            android.os.Trace.traceBegin(r0, r2);	 Catch:{ all -> 0x035c }
            r21 = r18.mRenderer;	 Catch:{ all -> 0x035c }
            r0 = r21;
            r0.onDrawFrame(r10);	 Catch:{ all -> 0x035c }
            r22 = 8;
            android.os.Trace.traceEnd(r22);	 Catch:{ all -> 0x01d5 }
        L_0x02f5:
            r0 = r24;
            r0 = r0.mEglHelper;	 Catch:{ all -> 0x01d5 }
            r21 = r0;
            r16 = r21.swap();	 Catch:{ all -> 0x01d5 }
            switch(r16) {
                case 12288: goto L_0x0324;
                case 12302: goto L_0x0363;
                default: goto L_0x0302;
            };	 Catch:{ all -> 0x01d5 }
        L_0x0302:
            r21 = "GLThread";
            r22 = "eglSwapBuffers";
            r0 = r21;
            r1 = r22;
            r2 = r16;
            android.opengl.GLSurfaceView.EglHelper.logEglErrorAsWarning(r0, r1, r2);	 Catch:{ all -> 0x01d5 }
            r22 = android.opengl.GLSurfaceView.sGLThreadManager;	 Catch:{ all -> 0x01d5 }
            monitor-enter(r22);	 Catch:{ all -> 0x01d5 }
            r21 = 1;
            r0 = r21;
            r1 = r24;
            r1.mSurfaceIsBad = r0;	 Catch:{ all -> 0x0365 }
            r21 = android.opengl.GLSurfaceView.sGLThreadManager;	 Catch:{ all -> 0x0365 }
            r21.notifyAll();	 Catch:{ all -> 0x0365 }
            monitor-exit(r22);	 Catch:{ all -> 0x0365 }
        L_0x0324:
            if (r20 == 0) goto L_0x002f;
        L_0x0326:
            r8 = 1;
            goto L_0x002f;
        L_0x0329:
            r21 = move-exception;
            monitor-exit(r22);	 Catch:{ all -> 0x0329 }
            throw r21;	 Catch:{ all -> 0x01d5 }
        L_0x032c:
            r22 = android.opengl.GLSurfaceView.sGLThreadManager;	 Catch:{ all -> 0x01d5 }
            monitor-enter(r22);	 Catch:{ all -> 0x01d5 }
            r21 = 1;
            r0 = r21;
            r1 = r24;
            r1.mFinishedCreatingEglSurface = r0;	 Catch:{ all -> 0x034b }
            r21 = 1;
            r0 = r21;
            r1 = r24;
            r1.mSurfaceIsBad = r0;	 Catch:{ all -> 0x034b }
            r21 = android.opengl.GLSurfaceView.sGLThreadManager;	 Catch:{ all -> 0x034b }
            r21.notifyAll();	 Catch:{ all -> 0x034b }
            monitor-exit(r22);	 Catch:{ all -> 0x034b }
            goto L_0x002f;
        L_0x034b:
            r21 = move-exception;
            monitor-exit(r22);	 Catch:{ all -> 0x034b }
            throw r21;	 Catch:{ all -> 0x01d5 }
        L_0x034e:
            r21 = move-exception;
            r22 = 8;
            android.os.Trace.traceEnd(r22);	 Catch:{ all -> 0x01d5 }
            throw r21;	 Catch:{ all -> 0x01d5 }
        L_0x0355:
            r21 = move-exception;
            r22 = 8;
            android.os.Trace.traceEnd(r22);	 Catch:{ all -> 0x01d5 }
            throw r21;	 Catch:{ all -> 0x01d5 }
        L_0x035c:
            r21 = move-exception;
            r22 = 8;
            android.os.Trace.traceEnd(r22);	 Catch:{ all -> 0x01d5 }
            throw r21;	 Catch:{ all -> 0x01d5 }
        L_0x0363:
            r12 = 1;
            goto L_0x0324;
        L_0x0365:
            r21 = move-exception;
            monitor-exit(r22);	 Catch:{ all -> 0x0365 }
            throw r21;	 Catch:{ all -> 0x01d5 }
        L_0x0368:
            r21 = move-exception;
            monitor-exit(r22);	 Catch:{ all -> 0x0368 }
            throw r21;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.opengl.GLSurfaceView.GLThread.guardedRun():void");
        }

        public boolean ableToDraw() {
            return this.mHaveEglContext && this.mHaveEglSurface && readyToDraw();
        }

        private boolean readyToDraw() {
            return !this.mPaused && this.mHasSurface && !this.mSurfaceIsBad && this.mWidth > 0 && this.mHeight > 0 && (this.mRequestRender || this.mRenderMode == 1);
        }

        public void setRenderMode(int renderMode) {
            if (renderMode < 0 || renderMode > 1) {
                throw new IllegalArgumentException("renderMode");
            }
            synchronized (GLSurfaceView.sGLThreadManager) {
                this.mRenderMode = renderMode;
                GLSurfaceView.sGLThreadManager.notifyAll();
            }
        }

        public int getRenderMode() {
            int i;
            synchronized (GLSurfaceView.sGLThreadManager) {
                i = this.mRenderMode;
            }
            return i;
        }

        public void requestRender() {
            synchronized (GLSurfaceView.sGLThreadManager) {
                this.mRequestRender = true;
                GLSurfaceView.sGLThreadManager.notifyAll();
            }
        }

        public void surfaceCreated() {
            synchronized (GLSurfaceView.sGLThreadManager) {
                this.mHasSurface = true;
                this.mFinishedCreatingEglSurface = false;
                GLSurfaceView.sGLThreadManager.notifyAll();
                while (this.mWaitingForSurface && !this.mFinishedCreatingEglSurface && !this.mExited) {
                    try {
                        GLSurfaceView.sGLThreadManager.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void surfaceDestroyed() {
            synchronized (GLSurfaceView.sGLThreadManager) {
                this.mHasSurface = false;
                GLSurfaceView.sGLThreadManager.notifyAll();
                while (!this.mWaitingForSurface && !this.mExited) {
                    try {
                        GLSurfaceView.sGLThreadManager.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void onPause() {
            synchronized (GLSurfaceView.sGLThreadManager) {
                this.mRequestPaused = true;
                GLSurfaceView.sGLThreadManager.notifyAll();
                while (!this.mExited && !this.mPaused) {
                    try {
                        GLSurfaceView.sGLThreadManager.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void onResume() {
            synchronized (GLSurfaceView.sGLThreadManager) {
                this.mRequestPaused = false;
                this.mRequestRender = true;
                this.mRenderComplete = false;
                GLSurfaceView.sGLThreadManager.notifyAll();
                while (!this.mExited && this.mPaused && !this.mRenderComplete) {
                    try {
                        GLSurfaceView.sGLThreadManager.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void onWindowResize(int w, int h) {
            synchronized (GLSurfaceView.sGLThreadManager) {
                this.mWidth = w;
                this.mHeight = h;
                this.mSizeChanged = true;
                this.mRequestRender = true;
                this.mRenderComplete = false;
                GLSurfaceView.sGLThreadManager.notifyAll();
                while (!this.mExited && !this.mPaused && !this.mRenderComplete && ableToDraw()) {
                    try {
                        GLSurfaceView.sGLThreadManager.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void requestExitAndWait() {
            synchronized (GLSurfaceView.sGLThreadManager) {
                this.mShouldExit = true;
                GLSurfaceView.sGLThreadManager.notifyAll();
                while (!this.mExited) {
                    try {
                        GLSurfaceView.sGLThreadManager.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public void requestReleaseEglContextLocked() {
            this.mShouldReleaseEglContext = true;
            GLSurfaceView.sGLThreadManager.notifyAll();
        }

        public void queueEvent(Runnable r) {
            if (r == null) {
                throw new IllegalArgumentException("r must not be null");
            }
            synchronized (GLSurfaceView.sGLThreadManager) {
                this.mEventQueue.add(r);
                GLSurfaceView.sGLThreadManager.notifyAll();
            }
        }
    }

    private static class GLThreadManager {
        private static String TAG = "GLThreadManager";
        private static final int kGLES_20 = 131072;
        private static final String kMSM7K_RENDERER_PREFIX = "Q3Dimension MSM7500 ";
        private GLThread mEglOwner;
        private boolean mGLESDriverCheckComplete;
        private int mGLESVersion;
        private boolean mGLESVersionCheckComplete;
        private boolean mLimitedGLESContexts;
        private boolean mMultipleGLESContextsAllowed;

        private GLThreadManager() {
        }

        public synchronized void threadExiting(GLThread thread) {
            thread.mExited = true;
            if (this.mEglOwner == thread) {
                this.mEglOwner = null;
            }
            notifyAll();
        }

        public boolean tryAcquireEglContextLocked(GLThread thread) {
            if (this.mEglOwner == thread || this.mEglOwner == null) {
                this.mEglOwner = thread;
                notifyAll();
                return true;
            }
            checkGLESVersion();
            if (this.mMultipleGLESContextsAllowed) {
                return true;
            }
            if (this.mEglOwner != null) {
                this.mEglOwner.requestReleaseEglContextLocked();
            }
            return false;
        }

        public void releaseEglContextLocked(GLThread thread) {
            if (this.mEglOwner == thread) {
                this.mEglOwner = null;
            }
            notifyAll();
        }

        public synchronized boolean shouldReleaseEGLContextWhenPausing() {
            return this.mLimitedGLESContexts;
        }

        public synchronized boolean shouldTerminateEGLWhenPausing() {
            checkGLESVersion();
            return !this.mMultipleGLESContextsAllowed;
        }

        public synchronized void checkGLDriver(GL10 gl) {
            boolean z = true;
            synchronized (this) {
                if (!this.mGLESDriverCheckComplete) {
                    checkGLESVersion();
                    String renderer = gl.glGetString(7937);
                    if (this.mGLESVersion < 131072) {
                        boolean z2;
                        if (renderer.startsWith(kMSM7K_RENDERER_PREFIX)) {
                            z2 = false;
                        } else {
                            z2 = true;
                        }
                        this.mMultipleGLESContextsAllowed = z2;
                        notifyAll();
                    }
                    if (this.mMultipleGLESContextsAllowed) {
                        z = false;
                    }
                    this.mLimitedGLESContexts = z;
                    this.mGLESDriverCheckComplete = true;
                }
            }
        }

        private void checkGLESVersion() {
            if (!this.mGLESVersionCheckComplete) {
                this.mGLESVersion = SystemProperties.getInt("ro.opengles.version", 0);
                if (this.mGLESVersion >= 131072) {
                    this.mMultipleGLESContextsAllowed = true;
                }
                this.mGLESVersionCheckComplete = true;
            }
        }
    }

    public interface GLWrapper {
        GL wrap(GL gl);
    }

    static class LogWriter extends Writer {
        private StringBuilder mBuilder = new StringBuilder();

        LogWriter() {
        }

        public void close() {
            flushBuilder();
        }

        public void flush() {
            flushBuilder();
        }

        public void write(char[] buf, int offset, int count) {
            for (int i = 0; i < count; i++) {
                char c = buf[offset + i];
                if (c == '\n') {
                    flushBuilder();
                } else {
                    this.mBuilder.append(c);
                }
            }
        }

        private void flushBuilder() {
            if (this.mBuilder.length() > 0) {
                Log.v(GLSurfaceView.TAG, this.mBuilder.toString());
                this.mBuilder.delete(0, this.mBuilder.length());
            }
        }
    }

    public interface Renderer {
        void onDrawFrame(GL10 gl10);

        void onSurfaceChanged(GL10 gl10, int i, int i2);

        void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig);
    }

    private class SimpleEGLConfigChooser extends ComponentSizeChooser {
        public SimpleEGLConfigChooser(boolean withDepthBuffer) {
            int i;
            if (withDepthBuffer) {
                i = 16;
            } else {
                i = 0;
            }
            super(8, 8, 8, 0, i, 0);
        }

        public SimpleEGLConfigChooser(int red, int green, int blue, int alpha, boolean withDepthBuffer) {
            int i;
            if (withDepthBuffer) {
                i = 16;
            } else {
                i = 0;
            }
            super(red, green, blue, alpha, i, 0);
            if (GLSurfaceView.DEBUG_DCS) {
                Log.i("SRIB_DCS_GLSurfaceView", "SimpleEGLConfigChooser, rgba=" + red + green + blue + alpha + "depth=" + withDepthBuffer);
            }
        }
    }

    private int[] chooseParams(int format) {
        if (!(!DCS_IS_EXYNOS || format == 1 || format == 4)) {
            if (DEBUG_DCS) {
                Log.i("SRIB_DCS_GLSurfaceView", "chooseParams(" + format + ") not supported in exynos. Selecting RGBA_8888");
            }
            format = 1;
        }
        switch (format) {
            case 1:
                return pixelConfig[3];
            case 4:
                return pixelConfig[0];
            case 6:
                return pixelConfig[1];
            case 7:
                return pixelConfig[2];
            default:
                return pixelConfig[3];
        }
    }

    public GLSurfaceView(Context context) {
        super(context);
        init();
    }

    public GLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void finalize() throws Throwable {
        try {
            if (this.mGLThread != null) {
                this.mGLThread.requestExitAndWait();
            }
            super.finalize();
        } catch (Throwable th) {
            super.finalize();
        }
    }

    private void init() {
        getHolder().addCallback(this);
    }

    public void setGLWrapper(GLWrapper glWrapper) {
        this.mGLWrapper = glWrapper;
    }

    public void setDebugFlags(int debugFlags) {
        this.mDebugFlags = debugFlags;
    }

    public int getDebugFlags() {
        return this.mDebugFlags;
    }

    public void setPreserveEGLContextOnPause(boolean preserveOnPause) {
        this.mPreserveEGLContextOnPause = preserveOnPause;
    }

    public boolean getPreserveEGLContextOnPause() {
        return this.mPreserveEGLContextOnPause;
    }

    public void setRenderer(Renderer renderer) {
        checkRenderThreadState();
        if (this.mEGLConfigChooser == null) {
            try {
                PackageInfo pi = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
                boolean IsDcsEnabledApp = pi.isDcsEnabledApp;
                int format = pi.dcsFormat;
                if (!IsDcsEnabledApp || format == -1) {
                    this.mEGLConfigChooser = new SimpleEGLConfigChooser(true);
                } else {
                    int[] params = chooseParams(format);
                    this.mEGLConfigChooser = new SimpleEGLConfigChooser(params[0], params[1], params[2], params[3], true);
                }
            } catch (NameNotFoundException e) {
                Log.e("SRIB_DCS_GLSurfaceView", "init, NameNotFoundException");
            }
        }
        if (this.mEGLContextFactory == null) {
            this.mEGLContextFactory = new DefaultContextFactory();
        }
        if (this.mEGLWindowSurfaceFactory == null) {
            this.mEGLWindowSurfaceFactory = new DefaultWindowSurfaceFactory();
        }
        this.mRenderer = renderer;
        this.mGLThread = new GLThread(this.mThisWeakRef);
        this.mGLThread.start();
    }

    public Renderer getRenderer() {
        return this.mRenderer;
    }

    public void setEGLContextFactory(EGLContextFactory factory) {
        checkRenderThreadState();
        this.mEGLContextFactory = factory;
    }

    public void setEGLWindowSurfaceFactory(EGLWindowSurfaceFactory factory) {
        checkRenderThreadState();
        this.mEGLWindowSurfaceFactory = factory;
    }

    public void setEGLConfigChooser(EGLConfigChooser configChooser) {
        checkRenderThreadState();
        this.mEGLConfigChooser = configChooser;
    }

    public void setEGLConfigChooser(boolean needDepth) {
        try {
            PackageInfo pi = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
            boolean IsDcsEnabledApp = pi.isDcsEnabledApp;
            int format = pi.dcsFormat;
            if (!IsDcsEnabledApp || format == -1) {
                setEGLConfigChooser(new SimpleEGLConfigChooser(needDepth));
                return;
            }
            int[] params = chooseParams(format);
            if (DEBUG_DCS) {
                Log.i("SRIB_DCS_GLSurfaceView", "Base Package Name" + getContext().getBasePackageName() + ":" + format);
            }
            setEGLConfigChooser(new SimpleEGLConfigChooser(params[0], params[1], params[2], params[3], needDepth));
        } catch (NameNotFoundException e) {
            Log.e("SRIB_DCS_GLSurfaceView", "SV init NameNotFoundException");
        }
    }

    public void setEGLConfigChooser(int redSize, int greenSize, int blueSize, int alphaSize, int depthSize, int stencilSize) {
        try {
            PackageInfo pi = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
            boolean IsDcsEnabledApp = pi.isDcsEnabledApp;
            int format = pi.dcsFormat;
            if (!IsDcsEnabledApp || format == -1) {
                setEGLConfigChooser(new ComponentSizeChooser(redSize, greenSize, blueSize, alphaSize, depthSize, stencilSize));
                return;
            }
            int[] params = chooseParams(format);
            if (DEBUG_DCS) {
                Log.i("SRIB_DCS_GLSurfaceView", "setEGLConfigChooser, Base Package Name" + getContext().getBasePackageName() + " " + format);
            }
            setEGLConfigChooser(new ComponentSizeChooser(params[0], params[1], params[2], params[3], depthSize, stencilSize));
        } catch (NameNotFoundException e) {
            Log.e("SRIB_DCS_GLSurfaceView", "SV init NameNotFoundException");
        }
    }

    public void setEGLContextClientVersion(int version) {
        checkRenderThreadState();
        this.mEGLContextClientVersion = version;
    }

    public void setRenderMode(int renderMode) {
        this.mGLThread.setRenderMode(renderMode);
    }

    public int getRenderMode() {
        return this.mGLThread.getRenderMode();
    }

    public void requestRender() {
        this.mGLThread.requestRender();
    }

    public void surfaceCreated(SurfaceHolder holder) {
        this.mGLThread.surfaceCreated();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        this.mGLThread.surfaceDestroyed();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        this.mGLThread.onWindowResize(w, h);
    }

    public void onPause() {
        this.mGLThread.onPause();
    }

    public void onResume() {
        this.mGLThread.onResume();
    }

    public void queueEvent(Runnable r) {
        this.mGLThread.queueEvent(r);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mDetached && this.mRenderer != null) {
            int renderMode = 1;
            if (this.mGLThread != null) {
                renderMode = this.mGLThread.getRenderMode();
            }
            this.mGLThread = new GLThread(this.mThisWeakRef);
            if (renderMode != 1) {
                this.mGLThread.setRenderMode(renderMode);
            }
            this.mGLThread.start();
        }
        this.mDetached = false;
    }

    protected void onDetachedFromWindow() {
        if (this.mGLThread != null) {
            this.mGLThread.requestExitAndWait();
        }
        this.mDetached = true;
        super.onDetachedFromWindow();
    }

    private void checkRenderThreadState() {
        if (this.mGLThread != null) {
            throw new IllegalStateException("setRenderer has already been called for this instance.");
        }
    }
}
