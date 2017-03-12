package android.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.Resources.Theme;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Build;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager.LayoutParams;
import android.widget.Checkable;
import android.widget.TextView;
import com.samsung.android.fingerprint.FingerprintManager;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

public class ViewDebug {
    private static final int CAPTURE_TIMEOUT = 4000;
    public static final boolean DEBUG_DRAG = false;
    private static final String REMOTE_COMMAND_CAPTURE = "CAPTURE";
    private static final String REMOTE_COMMAND_CAPTURE_LAYERS = "CAPTURE_LAYERS";
    private static final String REMOTE_COMMAND_DUMP = "DUMP";
    private static final String REMOTE_COMMAND_DUMP_THEME = "DUMP_THEME";
    private static final String REMOTE_COMMAND_DUMP_Z = "DUMPZ";
    private static final String REMOTE_COMMAND_INVALIDATE = "INVALIDATE";
    private static final String REMOTE_COMMAND_OUTPUT_DISPLAYLIST = "OUTPUT_DISPLAYLIST";
    private static final String REMOTE_COMMAND_REQUEST_LAYOUT = "REQUEST_LAYOUT";
    private static final String REMOTE_PROFILE = "PROFILE";
    @Deprecated
    public static final boolean TRACE_HIERARCHY = false;
    @Deprecated
    public static final boolean TRACE_RECYCLER = false;
    private static HashMap<Class<?>, Field[]> mCapturedViewFieldsForClasses = null;
    private static HashMap<Class<?>, Method[]> mCapturedViewMethodsForClasses = null;
    private static HashMap<AccessibleObject, ExportedProperty> sAnnotations;
    private static HashMap<Class<?>, Field[]> sFieldsForClasses;
    private static HashMap<Class<?>, Method[]> sMethodsForClasses;

    interface ViewOperation<T> {
        void post(T... tArr);

        T[] pre();

        void run(T... tArr);
    }

    @Target({ElementType.FIELD, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface CapturedViewProperty {
        boolean retrieveReturn() default false;
    }

    private static class DumpZ {
        private DumpZ() {
        }

        private static void dump(View root, OutputStream clientStream) throws IOException {
            Exception e;
            Throwable th;
            BufferedWriter out = null;
            try {
                BufferedWriter out2 = new BufferedWriter(new OutputStreamWriter(clientStream), 32768);
                try {
                    View view = root.getRootView();
                    if (view instanceof ViewGroup) {
                        ViewGroup group = (ViewGroup) view;
                        dumpViewHierarchyWithProperties(group.getContext(), group, out2, 0);
                    } else {
                        dumpViewWithProperties(view.getContext(), view, out2, 0);
                    }
                    out2.write("DONE.");
                    out2.newLine();
                    if (out2 != null) {
                        out2.close();
                        out = out2;
                        return;
                    }
                } catch (Exception e2) {
                    e = e2;
                    out = out2;
                    try {
                        Log.w("View", "Problem dumping the view:", e);
                        if (out != null) {
                            out.close();
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (out != null) {
                            out.close();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    out = out2;
                    if (out != null) {
                        out.close();
                    }
                    throw th;
                }
            } catch (Exception e3) {
                e = e3;
                Log.w("View", "Problem dumping the view:", e);
                if (out != null) {
                    out.close();
                }
            }
        }

        private static void dumpViewHierarchyWithProperties(Context context, ViewGroup group, BufferedWriter out, int level) {
            if (dumpViewWithProperties(context, group, out, level)) {
                int count = group.getChildCount();
                for (int i = 0; i < count; i++) {
                    View view = group.getChildAt(i);
                    if (view instanceof ViewGroup) {
                        dumpViewHierarchyWithProperties(context, (ViewGroup) view, out, level + 1);
                    } else if (view instanceof GLSurfaceView) {
                        dumpGLSurfaceView(context, (GLSurfaceView) view, out, level + 1);
                    } else {
                        dumpViewWithProperties(context, view, out, level + 1);
                    }
                }
            }
        }

        private static void dumpGLSurfaceView(Context context, GLSurfaceView view, BufferedWriter out, int level) {
            if (dumpViewWithProperties(context, view, out, level)) {
                Renderer renderer = view.getRenderer();
                if (renderer != null && (renderer instanceof IGLContext)) {
                    dumpGLHierarchyWithProperties(context, ((IGLContext) renderer).getGLRootView(), out, level + 1);
                }
            }
        }

        private static void dumpGLHierarchyWithProperties(Context context, IGLViewGroup group, BufferedWriter out, int level) {
            if (dumpViewWithProperties(context, group, out, level)) {
                for (Object obj : group.getChildren()) {
                    if (obj instanceof IGLViewGroup) {
                        dumpGLHierarchyWithProperties(context, (IGLViewGroup) obj, out, level + 1);
                    } else if (obj instanceof IGLView) {
                        dumpViewWithProperties(context, obj, out, level + 1);
                    }
                }
            }
        }

        private static boolean dumpViewWithProperties(Context context, Object view, BufferedWriter out, int level) {
            try {
                if ((view instanceof View) && ((View) view).getVisibility() != 0) {
                    return false;
                }
                for (int i = 0; i < level; i++) {
                    out.write(32);
                }
                if ("".equals(view.getClass().getSimpleName())) {
                    String[] arr_str = view.getClass().getName().split("\\.");
                    WriteProperty("class", arr_str[arr_str.length - 1], out);
                } else {
                    WriteProperty("class", view.getClass().getSimpleName(), out);
                }
                WriteProperty("hash", Integer.toHexString(view.hashCode()), out);
                dumpViewProperties(context, view, out);
                out.newLine();
                return true;
            } catch (IOException e) {
                Log.e("View", "Error while dumping hierarchy tree");
                return false;
            }
        }

        private static void dumpViewProperties(Context context, Object view, BufferedWriter out) throws IOException {
            String strValue = "";
            if (view instanceof View) {
                View tempView = (View) view;
                int[] location = new int[2];
                tempView.getLocationOnScreen(location);
                WriteProperty("id", (String) resolveId(context, tempView.getId()), out);
                WriteProperty("x", location[0], out);
                WriteProperty("y", location[1], out);
                WriteProperty("width", tempView.getWidth(), out);
                WriteProperty("height", tempView.getHeight(), out);
                WriteProperty("scrollx", tempView.getScrollX(), out);
                WriteProperty("scrolly", tempView.getScrollY(), out);
                WriteProperty("enable", tempView.isEnabled(), out);
                WriteProperty("longclickable", tempView.isLongClickable(), out);
                WriteProperty("clickable", tempView.isClickable(), out);
                if (tempView.isFocusable()) {
                    WriteProperty("hasfocus", tempView.hasFocus(), out);
                }
                if (tempView.getContentDescription() != null) {
                    WriteProperty("talkback", tempView.getContentDescription().toString(), out);
                }
                WriteProperty("isselected", tempView.isSelected(), out);
                if (tempView.getVisibility() == 0) {
                    WriteProperty("visibility", "VISIBLE", out);
                } else if (tempView.getVisibility() == 4) {
                    WriteProperty("visibility", "INVISIBLE", out);
                } else if (tempView.getVisibility() == 8) {
                    WriteProperty("visibility", "GONE", out);
                }
                try {
                    LayoutParams lp = (LayoutParams) tempView.getLayoutParams();
                    if (lp.type == 1) {
                        WriteProperty("layouttype", "TYPE_BASE_APPLICATION", out);
                    } else if (lp.type == 2) {
                        WriteProperty("layouttype", "TYPE_APPLICATION", out);
                    } else if (lp.type == 3) {
                        WriteProperty("layouttype", "TYPE_APPLICATION_STARTING", out);
                    } else if (lp.type == 1000) {
                        WriteProperty("layouttype", "TYPE_APPLICATION_PANEL", out);
                    } else if (lp.type == 1001) {
                        WriteProperty("layouttype", "TYPE_APPLICATION_MEDIA", out);
                    } else if (lp.type == 1002) {
                        WriteProperty("layouttype", "TYPE_APPLICATION_SUB_PANEL", out);
                    } else if (lp.type == 1003) {
                        WriteProperty("layouttype", "TYPE_APPLICATION_ATTACHED_DIALOG", out);
                    } else if (lp.type == 2000) {
                        WriteProperty("layouttype", "TYPE_STATUS_BAR", out);
                    } else if (lp.type == 2001) {
                        WriteProperty("layouttype", "TYPE_SEARCH_BAR", out);
                    } else if (lp.type == 2002) {
                        WriteProperty("layouttype", "TYPE_PHONE", out);
                    } else if (lp.type == 2003) {
                        WriteProperty("layouttype", "TYPE_SYSTEM_ALERT", out);
                    } else if (lp.type == 2004) {
                        WriteProperty("layouttype", "TYPE_KEYGUARD", out);
                    } else if (lp.type == 2005) {
                        WriteProperty("layouttype", "TYPE_TOAST", out);
                    } else if (lp.type == 2006) {
                        WriteProperty("layouttype", "TYPE_SYSTEM_OVERLAY", out);
                    } else if (lp.type == 2007) {
                        WriteProperty("layouttype", "TYPE_PRIORITY_PHONE", out);
                    } else if (lp.type == 2014) {
                        WriteProperty("layouttype", "TYPE_STATUS_BAR_PANEL", out);
                    } else if (lp.type == 2096) {
                        WriteProperty("layouttype", "TYPE_STATUS_BAR_PANEL_USER", out);
                    } else if (lp.type == 2017) {
                        WriteProperty("layouttype", "TYPE_STATUS_BAR_SUB_PANEL", out);
                    } else if (lp.type == 2008) {
                        WriteProperty("layouttype", "TYPE_SYSTEM_DIALOG", out);
                    } else if (lp.type == 2009) {
                        WriteProperty("layouttype", "TYPE_KEYGUARD_DIALOG", out);
                    } else if (lp.type == 2010) {
                        WriteProperty("layouttype", "TYPE_SYSTEM_ERROR", out);
                    } else if (lp.type == 2011) {
                        WriteProperty("layouttype", "TYPE_INPUT_METHOD", out);
                    } else if (lp.type == 2012) {
                        WriteProperty("layouttype", "TYPE_INPUT_METHOD_DIALOG", out);
                    } else if (lp.type == 2013) {
                        WriteProperty("layouttype", "TYPE_WALLPAPER", out);
                    } else if (lp.type == 2014) {
                        WriteProperty("layouttype", "TYPE_STATUS_BAR_PANEL", out);
                    } else if (lp.type == 2015) {
                        WriteProperty("layouttype", "TYPE_SECURE_SYSTEM_OVERLAY", out);
                    } else if (lp.type == 2016) {
                        WriteProperty("layouttype", "TYPE_DRAG", out);
                    } else if (lp.type == 2017) {
                        WriteProperty("layouttype", "TYPE_STATUS_BAR_SUB_PANEL", out);
                    } else if (lp.type == 2018) {
                        WriteProperty("layouttype", "TYPE_POINTER", out);
                    } else if (lp.type == 2019) {
                        WriteProperty("layouttype", "TYPE_NAVIGATION_BAR", out);
                    } else if (lp.type == 2020) {
                        WriteProperty("layouttype", "TYPE_VOLUME_OVERLAY", out);
                    } else if (lp.type == 2021) {
                        WriteProperty("layouttype", "TYPE_BOOT_PROGRESS", out);
                    } else if (lp.type == 2097) {
                        WriteProperty("layouttype", "TYPE_STATUS_BAR_OVERLAY", out);
                    }
                } catch (Exception e) {
                }
                try {
                    MarginLayoutParams mp = (MarginLayoutParams) tempView.getLayoutParams();
                    WriteProperty("bottommargin", mp.bottomMargin, out);
                    WriteProperty("leftmargin", mp.leftMargin, out);
                    WriteProperty("rightmargin", mp.rightMargin, out);
                    WriteProperty("topmargin", mp.topMargin, out);
                } catch (Exception e2) {
                }
                WriteProperty("willnotdraw", tempView.willNotDraw(), out);
            }
            try {
                for (Method method : view.getClass().getMethods()) {
                    if ("isFillViewport".equals(method.getName())) {
                        WriteProperty("fillviewport", ((Boolean) method.invoke(view, null)).booleanValue(), out);
                    }
                    if ("getFirstVisiblePosition".equals(method.getName())) {
                        WriteProperty("firstposition", ((Integer) method.invoke(view, null)).intValue(), out);
                    }
                    if ("getCount".equals(method.getName())) {
                        WriteProperty("itemcount", ((Integer) method.invoke(view, null)).intValue(), out);
                    }
                }
            } catch (Exception e3) {
            }
            if (view instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) view;
                WriteProperty("childcount", group.getChildCount(), out);
                if (group.getDescendantFocusability() == 131072) {
                    WriteProperty("focusability", "FOCUS_BEFORE_DESCENDANTS", out);
                } else if (group.getDescendantFocusability() == 262144) {
                    WriteProperty("focusability", "FOCUS_AFTER_DESCENDANTS", out);
                } else if (group.getDescendantFocusability() == 393216) {
                    WriteProperty("focusability", "FOCUS_BLOCK_DESCENDANTS", out);
                }
            }
            if (view instanceof TextView) {
                TextView tempView2 = (TextView) view;
                WriteProperty("text", tempView2.getText().toString(), out);
                try {
                    WriteProperty("stringname", tempView2.getStringName().toString(), out);
                } catch (Exception e4) {
                }
                try {
                    WriteProperty("hint", tempView2.getHint().toString(), out);
                } catch (Exception e5) {
                }
                WriteProperty("selectionstart", tempView2.getSelectionStart(), out);
                WriteProperty("selectionend", tempView2.getSelectionEnd(), out);
            }
            if (view instanceof Checkable) {
                WriteProperty("ischecked", ((Checkable) view).isChecked(), out);
            }
            if (view instanceof IGLView) {
                IGLView glview = (IGLView) view;
                try {
                    for (Method method2 : glview.getClass().getMethods()) {
                        if ("getObjectTag".equals(method2.getName())) {
                            WriteProperty("glTag", (String) method2.invoke(view, null), out);
                        }
                        if ("mLeft".equals(method2.getName())) {
                            WriteProperty("glleft", ((Float) method2.invoke(view, null)).floatValue(), out);
                        }
                        if ("mBottom".equals(method2.getName())) {
                            WriteProperty("glbottom", ((Float) method2.invoke(view, null)).floatValue(), out);
                        }
                        if ("mRight".equals(method2.getName())) {
                            WriteProperty("glright", ((Float) method2.invoke(view, null)).floatValue(), out);
                        }
                        if ("getOrientation".equals(method2.getName())) {
                            WriteProperty("glorientation", ((Integer) method2.invoke(view, null)).intValue(), out);
                        }
                        if ("getText".equals(method2.getName()) && "TwGLText".equals(view.getClass().getSimpleName())) {
                            WriteProperty("gltext", (String) method2.invoke(view, null), out);
                        }
                        if ("mText".equals(method2.getName()) && "TwGLButton".equals(view.getClass().getSimpleName())) {
                            WriteProperty("gltext", (String) method2.invoke(view, null), out);
                        }
                        if ("getStringName".equals(method2.getName())) {
                            WriteProperty("glstringname", ((CharSequence) method2.invoke(view, null)).toString(), out);
                        }
                        if ("mStringName".equals(method2.getName())) {
                            WriteProperty("glstringname", ((CharSequence) method2.invoke(view, null)).toString(), out);
                        }
                        if ("getAlpha".equals(method2.getName())) {
                            WriteProperty("glalpha", ((Float) method2.invoke(view, null)).floatValue(), out);
                        }
                        if ("getVisibility".equals(method2.getName())) {
                            WriteProperty("gl_getVisibility", ((Integer) method2.invoke(view, null)).intValue(), out);
                        }
                        if ("isVisible".equals(method2.getName())) {
                            WriteProperty("gl_isvisible", ((Integer) method2.invoke(view, null)).intValue(), out);
                        }
                    }
                } catch (Exception e6) {
                }
                try {
                    for (Method method22 : glview.getClass().getMethods()) {
                        if ("mTop".equals(method22.getName())) {
                            WriteProperty("gltop", ((Float) method22.invoke(view, null)).floatValue(), out);
                        }
                    }
                } catch (Exception e7) {
                }
            }
        }

        private static void WriteProperty(String name, String value, BufferedWriter out) {
            try {
                out.write(name + "=");
                value = value.replace('\n', '\u0003');
                out.write(String.valueOf(value.length()));
                out.write(FingerprintManager.FINGER_PERMISSION_DELIMITER);
                out.write(value);
                out.write(" ");
            } catch (IOException e) {
            }
        }

        private static void WriteProperty(String name, int value, BufferedWriter out) {
            try {
                out.write(name + "=");
                out.write(String.valueOf(Integer.toString(value).length()));
                out.write(FingerprintManager.FINGER_PERMISSION_DELIMITER);
                out.write(Integer.toString(value));
                out.write(" ");
            } catch (IOException e) {
            }
        }

        private static void WriteProperty(String name, float value, BufferedWriter out) {
            try {
                out.write(name + "=");
                out.write(String.valueOf(Float.toString(value).length()));
                out.write(FingerprintManager.FINGER_PERMISSION_DELIMITER);
                out.write(Float.toString(value));
                out.write(" ");
            } catch (IOException e) {
            }
        }

        private static void WriteProperty(String name, boolean value, BufferedWriter out) {
            try {
                out.write(name + "=");
                if (value) {
                    out.write("4,true ");
                } else {
                    out.write("5,false ");
                }
            } catch (IOException e) {
            }
        }

        private static Object resolveId(Context context, int id) {
            Resources resources = context.getResources();
            if (id < 0) {
                return "NO_ID";
            }
            try {
                return resources.getResourceEntryName(id);
            } catch (NotFoundException e) {
                return "0x" + Integer.toHexString(id);
            }
        }

        private static void dumpViewHierarchy(ViewGroup group, BufferedWriter out, int level) {
            if (dumpView(group, out, level)) {
                int count = group.getChildCount();
                for (int i = 0; i < count; i++) {
                    View view = group.getChildAt(i);
                    if (view instanceof ViewGroup) {
                        dumpViewHierarchy((ViewGroup) view, out, level + 1);
                    } else {
                        dumpView(view, out, level + 1);
                    }
                }
            }
        }

        private static boolean dumpView(Object view, BufferedWriter out, int level) {
            int i = 0;
            while (i < level) {
                try {
                    out.write(32);
                    i++;
                } catch (IOException e) {
                    Log.w("View", "Error while dumping hierarchy tree");
                    return false;
                }
            }
            out.write(view.getClass().getName());
            out.write(64);
            out.write(Integer.toHexString(view.hashCode()));
            out.newLine();
            return true;
        }
    }

    @Target({ElementType.FIELD, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ExportedProperty {
        String category() default "";

        boolean deepExport() default false;

        FlagToString[] flagMapping() default {};

        boolean formatToHexString() default false;

        boolean hasAdjacentMapping() default false;

        IntToString[] indexMapping() default {};

        IntToString[] mapping() default {};

        String prefix() default "";

        boolean resolveId() default false;
    }

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface FlagToString {
        int equals();

        int mask();

        String name();

        boolean outputIf() default true;
    }

    public interface HierarchyHandler {
        void dumpViewHierarchyWithProperties(BufferedWriter bufferedWriter, int i);

        View findHierarchyView(String str, int i);
    }

    @Deprecated
    public enum HierarchyTraceType {
        INVALIDATE,
        INVALIDATE_CHILD,
        INVALIDATE_CHILD_IN_PARENT,
        REQUEST_LAYOUT,
        ON_LAYOUT,
        ON_MEASURE,
        DRAW,
        BUILD_CACHE
    }

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface IntToString {
        int from();

        String to();
    }

    @Deprecated
    public enum RecyclerTraceType {
        NEW_VIEW,
        BIND_VIEW,
        RECYCLE_FROM_ACTIVE_HEAP,
        RECYCLE_FROM_SCRAP_HEAP,
        MOVE_TO_SCRAP_HEAP,
        MOVE_FROM_ACTIVE_TO_SCRAP_HEAP
    }

    public static long getViewInstanceCount() {
        return Debug.countInstancesOfClass(View.class);
    }

    public static long getViewRootImplCount() {
        return Debug.countInstancesOfClass(ViewRootImpl.class);
    }

    @Deprecated
    public static void trace(View view, RecyclerTraceType type, int... parameters) {
    }

    @Deprecated
    public static void startRecyclerTracing(String prefix, View view) {
    }

    @Deprecated
    public static void stopRecyclerTracing() {
    }

    @Deprecated
    public static void trace(View view, HierarchyTraceType type) {
    }

    @Deprecated
    public static void startHierarchyTracing(String prefix, View view) {
    }

    @Deprecated
    public static void stopHierarchyTracing() {
    }

    static void dispatchCommand(View view, String command, String parameters, OutputStream clientStream) throws IOException {
        view = view.getRootView();
        if (REMOTE_COMMAND_DUMP.equalsIgnoreCase(command)) {
            dump(view, false, true, clientStream);
        } else if ("dump_s".equalsIgnoreCase(command) && "eng".equals(Build.TYPE)) {
            dump_s(view, false, true, clientStream);
        } else if (REMOTE_COMMAND_DUMP_THEME.equalsIgnoreCase(command)) {
            dumpTheme(view, clientStream);
        } else if (REMOTE_COMMAND_DUMP_Z.equalsIgnoreCase(command)) {
            DumpZ.dump(view, clientStream);
        } else if (REMOTE_COMMAND_CAPTURE_LAYERS.equalsIgnoreCase(command)) {
            captureLayers(view, new DataOutputStream(clientStream));
        } else {
            String[] params = parameters.split(" ");
            if (REMOTE_COMMAND_CAPTURE.equalsIgnoreCase(command)) {
                capture(view, clientStream, params[0]);
            } else if (REMOTE_COMMAND_OUTPUT_DISPLAYLIST.equalsIgnoreCase(command)) {
                outputDisplayList(view, params[0]);
            } else if (REMOTE_COMMAND_INVALIDATE.equalsIgnoreCase(command)) {
                invalidate(view, params[0]);
            } else if (REMOTE_COMMAND_REQUEST_LAYOUT.equalsIgnoreCase(command)) {
                requestLayout(view, params[0]);
            } else if (REMOTE_PROFILE.equalsIgnoreCase(command)) {
                profile(view, clientStream, params[0]);
            }
        }
    }

    public static View findView(View root, String parameter) {
        if (parameter.indexOf(64) != -1) {
            String[] ids = parameter.split("@");
            String className = ids[0];
            int hashCode = (int) Long.parseLong(ids[1], 16);
            View view = root.getRootView();
            if (view instanceof ViewGroup) {
                return findView((ViewGroup) view, className, hashCode);
            }
            return null;
        }
        return root.getRootView().findViewById(root.getResources().getIdentifier(parameter, null, null));
    }

    private static void invalidate(View root, String parameter) {
        View view = findView(root, parameter);
        if (view != null) {
            view.postInvalidate();
        }
    }

    private static void requestLayout(View root, String parameter) {
        final View view = findView(root, parameter);
        if (view != null) {
            root.post(new Runnable() {
                public void run() {
                    view.requestLayout();
                }
            });
        }
    }

    private static void profile(View root, OutputStream clientStream, String parameter) throws IOException {
        Exception e;
        Throwable th;
        View view = findView(root, parameter);
        BufferedWriter out = null;
        try {
            BufferedWriter out2 = new BufferedWriter(new OutputStreamWriter(clientStream), 32768);
            if (view != null) {
                try {
                    profileViewAndChildren(view, out2);
                } catch (Exception e2) {
                    e = e2;
                    out = out2;
                    try {
                        Log.w("View", "Problem profiling the view:", e);
                        if (out != null) {
                            out.close();
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (out != null) {
                            out.close();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    out = out2;
                    if (out != null) {
                        out.close();
                    }
                    throw th;
                }
            }
            out2.write("-1 -1 -1");
            out2.newLine();
            out2.write("DONE.");
            out2.newLine();
            if (out2 != null) {
                out2.close();
                out = out2;
                return;
            }
        } catch (Exception e3) {
            e = e3;
            Log.w("View", "Problem profiling the view:", e);
            if (out != null) {
                out.close();
            }
        }
    }

    public static void profileViewAndChildren(View view, BufferedWriter out) throws IOException {
        profileViewAndChildren(view, out, true);
    }

    private static void profileViewAndChildren(final View view, BufferedWriter out, boolean root) throws IOException {
        long durationMeasure;
        long durationLayout;
        long durationDraw = 0;
        if (root || (view.mPrivateFlags & 2048) != 0) {
            durationMeasure = profileViewOperation(view, new ViewOperation<Void>() {
                public Void[] pre() {
                    forceLayout(view);
                    return null;
                }

                private void forceLayout(View view) {
                    view.forceLayout();
                    if (view instanceof ViewGroup) {
                        ViewGroup group = (ViewGroup) view;
                        int count = group.getChildCount();
                        for (int i = 0; i < count; i++) {
                            forceLayout(group.getChildAt(i));
                        }
                    }
                }

                public void run(Void... data) {
                    view.measure(view.mOldWidthMeasureSpec, view.mOldHeightMeasureSpec);
                }

                public void post(Void... data) {
                }
            });
        } else {
            durationMeasure = 0;
        }
        if (root || (view.mPrivateFlags & 8192) != 0) {
            durationLayout = profileViewOperation(view, new ViewOperation<Void>() {
                public Void[] pre() {
                    return null;
                }

                public void run(Void... data) {
                    view.layout(view.mLeft, view.mTop, view.mRight, view.mBottom);
                }

                public void post(Void... data) {
                }
            });
        } else {
            durationLayout = 0;
        }
        if (!(!root && view.willNotDraw() && (view.mPrivateFlags & 32) == 0)) {
            durationDraw = profileViewOperation(view, new ViewOperation<Object>() {
                public Object[] pre() {
                    DisplayMetrics metrics;
                    Bitmap bitmap;
                    Canvas canvas = null;
                    if (view == null || view.getResources() == null) {
                        metrics = null;
                    } else {
                        metrics = view.getResources().getDisplayMetrics();
                    }
                    if (metrics != null) {
                        bitmap = Bitmap.createBitmap(metrics, metrics.widthPixels, metrics.heightPixels, Config.RGB_565);
                    } else {
                        bitmap = null;
                    }
                    if (bitmap != null) {
                        canvas = new Canvas(bitmap);
                    }
                    return new Object[]{bitmap, canvas};
                }

                public void run(Object... data) {
                    if (data[1] != null) {
                        view.draw((Canvas) data[1]);
                    }
                }

                public void post(Object... data) {
                    if (data[1] != null) {
                        ((Canvas) data[1]).setBitmap(null);
                    }
                    if (data[0] != null) {
                        ((Bitmap) data[0]).recycle();
                    }
                }
            });
        }
        out.write(String.valueOf(durationMeasure));
        out.write(32);
        out.write(String.valueOf(durationLayout));
        out.write(32);
        out.write(String.valueOf(durationDraw));
        out.newLine();
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                profileViewAndChildren(group.getChildAt(i), out, false);
            }
        }
    }

    private static <T> long profileViewOperation(View view, final ViewOperation<T> operation) {
        final CountDownLatch latch = new CountDownLatch(1);
        final long[] duration = new long[1];
        view.post(new Runnable() {
            public void run() {
                try {
                    T[] data = operation.pre();
                    long start = Debug.threadCpuTimeNanos();
                    operation.run(data);
                    duration[0] = Debug.threadCpuTimeNanos() - start;
                    operation.post(data);
                } finally {
                    latch.countDown();
                }
            }
        });
        try {
            if (latch.await(4000, TimeUnit.MILLISECONDS)) {
                return duration[0];
            }
            Log.w("View", "Could not complete the profiling of the view " + view);
            return -1;
        } catch (InterruptedException e) {
            Log.w("View", "Could not complete the profiling of the view " + view);
            Thread.currentThread().interrupt();
            return -1;
        }
    }

    public static void captureLayers(View root, DataOutputStream clientStream) throws IOException {
        try {
            Rect outRect = new Rect();
            try {
                root.mAttachInfo.mSession.getDisplayFrame(root.mAttachInfo.mWindow, outRect);
            } catch (RemoteException e) {
            }
            clientStream.writeInt(outRect.width());
            clientStream.writeInt(outRect.height());
            captureViewLayer(root, clientStream, true);
            clientStream.write(2);
        } finally {
            clientStream.close();
        }
    }

    private static void captureViewLayer(View view, DataOutputStream clientStream, boolean visible) throws IOException {
        boolean localVisible = view.getVisibility() == 0 && visible;
        if ((view.mPrivateFlags & 128) != 128) {
            int id = view.getId();
            String name = view.getClass().getSimpleName();
            if (id != -1) {
                name = resolveId(view.getContext(), id).toString();
            }
            clientStream.write(1);
            clientStream.writeUTF(name);
            clientStream.writeByte(localVisible ? 1 : 0);
            int[] position = new int[2];
            view.getLocationInWindow(position);
            clientStream.writeInt(position[0]);
            clientStream.writeInt(position[1]);
            clientStream.flush();
            Bitmap b = performViewCapture(view, true);
            if (b != null) {
                ByteArrayOutputStream arrayOut = new ByteArrayOutputStream((b.getWidth() * b.getHeight()) * 2);
                b.compress(CompressFormat.PNG, 100, arrayOut);
                clientStream.writeInt(arrayOut.size());
                arrayOut.writeTo(clientStream);
            }
            clientStream.flush();
        }
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                captureViewLayer(group.getChildAt(i), clientStream, localVisible);
            }
        }
        if (view.mOverlay != null) {
            captureViewLayer(view.getOverlay().mOverlayViewGroup, clientStream, localVisible);
        }
    }

    private static void outputDisplayList(View root, String parameter) throws IOException {
        View view = findView(root, parameter);
        view.getViewRootImpl().outputDisplayList(view);
    }

    public static void outputDisplayList(View root, View target) {
        root.getViewRootImpl().outputDisplayList(target);
    }

    private static void capture(View root, OutputStream clientStream, String parameter) throws IOException {
        capture(root, clientStream, findView(root, parameter));
    }

    public static void capture(View root, OutputStream clientStream, View captureView) throws IOException {
        Throwable th;
        Bitmap b = performViewCapture(captureView, false);
        if (b == null) {
            Log.w("View", "Failed to create capture bitmap!");
            b = Bitmap.createBitmap(root.getResources().getDisplayMetrics(), 1, 1, Config.ARGB_8888);
        }
        BufferedOutputStream out = null;
        try {
            BufferedOutputStream out2 = new BufferedOutputStream(clientStream, 32768);
            try {
                b.compress(CompressFormat.PNG, 100, out2);
                out2.flush();
                if (out2 != null) {
                    out2.close();
                }
                b.recycle();
            } catch (Throwable th2) {
                th = th2;
                out = out2;
                if (out != null) {
                    out.close();
                }
                b.recycle();
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            if (out != null) {
                out.close();
            }
            b.recycle();
            throw th;
        }
    }

    private static Bitmap performViewCapture(final View captureView, final boolean skipChildren) {
        if (captureView != null) {
            final CountDownLatch latch = new CountDownLatch(1);
            final Bitmap[] cache = new Bitmap[1];
            captureView.post(new Runnable() {
                public void run() {
                    try {
                        cache[0] = captureView.createSnapshot(Config.ARGB_8888, 0, skipChildren);
                    } catch (OutOfMemoryError e) {
                        Log.w("View", "Out of memory for bitmap");
                    } finally {
                        latch.countDown();
                    }
                }
            });
            try {
                latch.await(4000, TimeUnit.MILLISECONDS);
                return cache[0];
            } catch (InterruptedException e) {
                Log.w("View", "Could not complete the capture of the view " + captureView);
                Thread.currentThread().interrupt();
            }
        }
        return null;
    }

    public static void dump(View root, boolean skipChildren, boolean includeProperties, OutputStream clientStream) throws IOException {
        Exception e;
        Throwable th;
        BufferedWriter bufferedWriter;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientStream, "utf-8"), 32768);
            try {
                View view = root.getRootView();
                if (view instanceof ViewGroup) {
                    ViewGroup group = (ViewGroup) view;
                    dumpViewHierarchy(group.getContext(), group, bufferedWriter, 0, skipChildren, includeProperties);
                }
                bufferedWriter.write("DONE.");
                bufferedWriter.newLine();
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            } catch (Exception e2) {
                e = e2;
                try {
                    Log.w("View", "Problem dumping the view:", e);
                    if (bufferedWriter != null) {
                        bufferedWriter.close();
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (bufferedWriter != null) {
                        bufferedWriter.close();
                    }
                    throw th;
                }
            }
        } catch (Exception e3) {
            e = e3;
            bufferedWriter = null;
            Log.w("View", "Problem dumping the view:", e);
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
        } catch (Throwable th3) {
            th = th3;
            bufferedWriter = null;
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            throw th;
        }
    }

    public static void dumpv2(final View view, ByteArrayOutputStream out) throws InterruptedException {
        final ViewHierarchyEncoder encoder = new ViewHierarchyEncoder(out);
        final CountDownLatch latch = new CountDownLatch(1);
        view.post(new Runnable() {
            public void run() {
                view.encode(encoder);
                latch.countDown();
            }
        });
        latch.await(2, TimeUnit.SECONDS);
        encoder.endStream();
    }

    public static void dumpTheme(View view, OutputStream clientStream) throws IOException {
        Exception e;
        Throwable th;
        BufferedWriter out = null;
        try {
            BufferedWriter out2 = new BufferedWriter(new OutputStreamWriter(clientStream, "utf-8"), 32768);
            try {
                String[] attributes = getStyleAttributesDump(view.getContext().getResources(), view.getContext().getTheme());
                if (attributes != null) {
                    for (int i = 0; i < attributes.length; i += 2) {
                        if (attributes[i] != null) {
                            out2.write(attributes[i] + "\n");
                            out2.write(attributes[i + 1] + "\n");
                        }
                    }
                }
                out2.write("DONE.");
                out2.newLine();
                if (out2 != null) {
                    out2.close();
                    out = out2;
                    return;
                }
            } catch (Exception e2) {
                e = e2;
                out = out2;
                try {
                    Log.w("View", "Problem dumping View Theme:", e);
                    if (out != null) {
                        out.close();
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (out != null) {
                        out.close();
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                out = out2;
                if (out != null) {
                    out.close();
                }
                throw th;
            }
        } catch (Exception e3) {
            e = e3;
            Log.w("View", "Problem dumping View Theme:", e);
            if (out != null) {
                out.close();
            }
        }
    }

    private static String[] getStyleAttributesDump(Resources resources, Theme theme) {
        TypedValue outValue = new TypedValue();
        String nullString = "null";
        int i = 0;
        int[] attributes = theme.getAllAttributes();
        String[] data = new String[(attributes.length * 2)];
        for (int attributeId : attributes) {
            try {
                String charSequence;
                data[i] = resources.getResourceName(attributeId);
                int i2 = i + 1;
                if (theme.resolveAttribute(attributeId, outValue, true)) {
                    charSequence = outValue.coerceToString().toString();
                } else {
                    charSequence = nullString;
                }
                data[i2] = charSequence;
                i += 2;
                if (outValue.type == 1) {
                    data[i - 1] = resources.getResourceName(outValue.resourceId);
                }
            } catch (NotFoundException e) {
            }
        }
        return data;
    }

    private static View findView(ViewGroup group, String className, int hashCode) {
        if (isRequestedView(group, className, hashCode)) {
            return group;
        }
        int count = group.getChildCount();
        for (int i = 0; i < count; i++) {
            View found;
            View view = group.getChildAt(i);
            if (view instanceof ViewGroup) {
                found = findView((ViewGroup) view, className, hashCode);
                if (found != null) {
                    return found;
                }
            } else if (isRequestedView(view, className, hashCode)) {
                return view;
            }
            if (view.mOverlay != null) {
                found = findView(view.mOverlay.mOverlayViewGroup, className, hashCode);
                if (found != null) {
                    return found;
                }
            }
            if (view instanceof HierarchyHandler) {
                found = ((HierarchyHandler) view).findHierarchyView(className, hashCode);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    private static boolean isRequestedView(View view, String className, int hashCode) {
        if (view.hashCode() != hashCode) {
            return false;
        }
        String viewClassName = view.getClass().getName();
        if (className.equals("ViewOverlay")) {
            return viewClassName.equals("android.view.ViewOverlay$OverlayViewGroup");
        }
        return className.equals(viewClassName);
    }

    private static void dumpViewHierarchy(Context context, ViewGroup group, BufferedWriter out, int level, boolean skipChildren, boolean includeProperties) {
        if (dumpView(context, group, out, level, includeProperties) && !skipChildren) {
            int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                View view = group.getChildAt(i);
                if (view instanceof ViewGroup) {
                    dumpViewHierarchy(context, (ViewGroup) view, out, level + 1, skipChildren, includeProperties);
                } else if (view instanceof GLSurfaceView) {
                    dumpGLSurfaceView(context, (GLSurfaceView) view, out, level + 1, includeProperties);
                } else {
                    dumpView(context, view, out, level + 1, includeProperties);
                }
                if (view.mOverlay != null) {
                    dumpViewHierarchy(context, view.getOverlay().mOverlayViewGroup, out, level + 2, skipChildren, includeProperties);
                }
            }
            if (group instanceof HierarchyHandler) {
                ((HierarchyHandler) group).dumpViewHierarchyWithProperties(out, level + 1);
            }
        }
    }

    private static void dumpGLSurfaceView(Context context, GLSurfaceView view, BufferedWriter out, int level, boolean includeProperties) {
        if (dumpView(context, view, out, level, includeProperties)) {
            Renderer renderer = view.getRenderer();
            if (renderer != null && (renderer instanceof IGLContext)) {
                dumpGLHierarchyWithProperties(context, ((IGLContext) renderer).getGLRootView(), out, level + 1, includeProperties);
            }
        }
    }

    private static void dumpGLHierarchyWithProperties(Context context, IGLViewGroup group, BufferedWriter out, int level, boolean includeProperties) {
        if (dumpView(context, group, out, level, includeProperties)) {
            for (Object obj : group.getChildren()) {
                if (obj instanceof IGLViewGroup) {
                    dumpGLHierarchyWithProperties(context, (IGLViewGroup) obj, out, level + 1, includeProperties);
                } else if (obj instanceof IGLView) {
                    dumpView(context, obj, out, level + 1, includeProperties);
                }
            }
        }
    }

    private static boolean dumpView(Context context, Object view, BufferedWriter out, int level, boolean includeProperties) {
        int i = 0;
        while (i < level) {
            try {
                out.write(32);
                i++;
            } catch (IOException e) {
                Log.w("View", "Error while dumping hierarchy tree");
                return false;
            }
        }
        String className = view.getClass().getName();
        if (className.equals("android.view.ViewOverlay$OverlayViewGroup")) {
            className = "ViewOverlay";
        }
        out.write(className);
        out.write(64);
        out.write(Integer.toHexString(view.hashCode()));
        out.write(32);
        if (includeProperties) {
            dumpViewProperties(context, view, out);
        }
        out.newLine();
        return true;
    }

    private static Field[] getExportedPropertyFields(Class<?> klass) {
        if (sFieldsForClasses == null) {
            sFieldsForClasses = new HashMap();
        }
        if (sAnnotations == null) {
            sAnnotations = new HashMap(512);
        }
        HashMap<Class<?>, Field[]> map = sFieldsForClasses;
        Field[] fields = (Field[]) map.get(klass);
        if (fields != null) {
            return fields;
        }
        try {
            Field[] declaredFields = klass.getDeclaredFieldsUnchecked(false);
            ArrayList<Field> foundFields = new ArrayList();
            for (Field field : declaredFields) {
                if (field.getType() != null && field.isAnnotationPresent(ExportedProperty.class)) {
                    field.setAccessible(true);
                    foundFields.add(field);
                    sAnnotations.put(field, field.getAnnotation(ExportedProperty.class));
                }
            }
            fields = (Field[]) foundFields.toArray(new Field[foundFields.size()]);
            map.put(klass, fields);
            return fields;
        } catch (NoClassDefFoundError e) {
            throw new AssertionError(e);
        }
    }

    private static Method[] getExportedPropertyMethods(Class<?> klass) {
        if (sMethodsForClasses == null) {
            sMethodsForClasses = new HashMap(100);
        }
        if (sAnnotations == null) {
            sAnnotations = new HashMap(512);
        }
        HashMap<Class<?>, Method[]> map = sMethodsForClasses;
        Method[] methods = (Method[]) map.get(klass);
        if (methods != null) {
            return methods;
        }
        methods = klass.getDeclaredMethodsUnchecked(false);
        ArrayList<Method> foundMethods = new ArrayList();
        for (Method method : methods) {
            try {
                method.getReturnType();
                method.getParameterTypes();
                if (method.getParameterTypes().length == 0 && method.isAnnotationPresent(ExportedProperty.class) && method.getReturnType() != Void.class) {
                    method.setAccessible(true);
                    foundMethods.add(method);
                    sAnnotations.put(method, method.getAnnotation(ExportedProperty.class));
                }
            } catch (NoClassDefFoundError e) {
            }
        }
        methods = (Method[]) foundMethods.toArray(new Method[foundMethods.size()]);
        map.put(klass, methods);
        return methods;
    }

    private static void dumpViewProperties(Context context, Object view, BufferedWriter out) throws IOException {
        dumpViewProperties(context, view, out, "");
    }

    private static void dumpViewProperties(Context context, Object view, BufferedWriter out, String prefix) throws IOException {
        if (view == null) {
            out.write(prefix + "=4,null ");
            return;
        }
        Class<?> klass = view.getClass();
        do {
            exportFields(context, view, out, klass, prefix);
            exportMethods(context, view, out, klass, prefix);
            klass = klass.getSuperclass();
        } while (klass != Object.class);
    }

    private static Object callMethodOnAppropriateTheadBlocking(final Method method, Object object) throws IllegalAccessException, InvocationTargetException, TimeoutException {
        if (!(object instanceof View)) {
            return method.invoke(object, (Object[]) null);
        }
        final View view = (View) object;
        FutureTask<Object> future = new FutureTask(new Callable<Object>() {
            public Object call() throws IllegalAccessException, InvocationTargetException {
                return method.invoke(view, (Object[]) null);
            }
        });
        Handler handler = view.getHandler();
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
        handler.post(future);
        while (true) {
            try {
                return future.get(4000, TimeUnit.MILLISECONDS);
            } catch (ExecutionException e) {
                Throwable t = e.getCause();
                if (t instanceof IllegalAccessException) {
                    throw ((IllegalAccessException) t);
                } else if (t instanceof InvocationTargetException) {
                    throw ((InvocationTargetException) t);
                } else {
                    throw new RuntimeException("Unexpected exception", t);
                }
            } catch (InterruptedException e2) {
            } catch (CancellationException e3) {
                throw new RuntimeException("Unexpected cancellation exception", e3);
            }
        }
    }

    private static String formatIntToHexString(int value) {
        return "0x" + Integer.toHexString(value).toUpperCase();
    }

    private static void exportMethods(Context context, Object view, BufferedWriter out, Class<?> klass, String prefix) throws IOException {
        for (Method method : getExportedPropertyMethods(klass)) {
            try {
                Object methodValue = callMethodOnAppropriateTheadBlocking(method, view);
                Class<?> returnType = method.getReturnType();
                ExportedProperty property = (ExportedProperty) sAnnotations.get(method);
                String categoryPrefix = property.category().length() != 0 ? property.category() + ":" : "";
                int j;
                if (returnType != Integer.TYPE) {
                    if (returnType == int[].class) {
                        String suffix = "()";
                        exportUnrolledArray(context, out, property, (int[]) methodValue, categoryPrefix + prefix + method.getName() + '_', "()");
                    } else if (returnType == String[].class) {
                        String[] array = (String[]) methodValue;
                        if (property.hasAdjacentMapping() && array != null) {
                            for (j = 0; j < array.length; j += 2) {
                                if (array[j] != null) {
                                    writeEntry(out, categoryPrefix + prefix, array[j], "()", array[j + 1] == null ? "null" : array[j + 1]);
                                }
                            }
                        }
                    } else if (!returnType.isPrimitive() && property.deepExport()) {
                        dumpViewProperties(context, methodValue, out, prefix + property.prefix());
                    }
                } else if (!property.resolveId() || context == null) {
                    FlagToString[] flagsMapping = property.flagMapping();
                    if (flagsMapping.length > 0) {
                        exportUnrolledFlags(out, flagsMapping, ((Integer) methodValue).intValue(), categoryPrefix + prefix + method.getName() + '_');
                    }
                    IntToString[] mapping = property.mapping();
                    if (mapping.length > 0) {
                        int intValue = ((Integer) methodValue).intValue();
                        boolean mapped = false;
                        for (IntToString mapper : mapping) {
                            if (mapper.from() == intValue) {
                                methodValue = mapper.to();
                                mapped = true;
                                break;
                            }
                        }
                        if (!mapped) {
                            methodValue = Integer.valueOf(intValue);
                        }
                    }
                } else {
                    methodValue = resolveId(context, ((Integer) methodValue).intValue());
                }
                writeEntry(out, categoryPrefix + prefix, method.getName(), "()", methodValue);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e2) {
            } catch (TimeoutException e3) {
            }
        }
    }

    private static void exportFields(Context context, Object view, BufferedWriter out, Class<?> klass, String prefix) throws IOException {
        for (Field field : getExportedPropertyFields(klass)) {
            Object obj = null;
            try {
                Class<?> type = field.getType();
                ExportedProperty property = (ExportedProperty) sAnnotations.get(field);
                String categoryPrefix = property.category().length() != 0 ? property.category() + ":" : "";
                int j;
                if (type != Integer.TYPE && type != Byte.TYPE) {
                    if (type == int[].class) {
                        String suffix = "";
                        exportUnrolledArray(context, out, property, (int[]) field.get(view), categoryPrefix + prefix + field.getName() + '_', "");
                    } else if (type == String[].class) {
                        String[] array = (String[]) field.get(view);
                        if (property.hasAdjacentMapping() && array != null) {
                            for (j = 0; j < array.length; j += 2) {
                                if (array[j] != null) {
                                    writeEntry(out, categoryPrefix + prefix, array[j], "", array[j + 1] == null ? "null" : array[j + 1]);
                                }
                            }
                        }
                    } else if (!type.isPrimitive() && property.deepExport()) {
                        dumpViewProperties(context, field.get(view), out, prefix + property.prefix());
                    }
                } else if (!property.resolveId() || context == null) {
                    FlagToString[] flagsMapping = property.flagMapping();
                    if (flagsMapping.length > 0) {
                        exportUnrolledFlags(out, flagsMapping, field.getInt(view), categoryPrefix + prefix + field.getName() + '_');
                    }
                    IntToString[] mapping = property.mapping();
                    if (mapping.length > 0) {
                        int intValue = field.getInt(view);
                        for (IntToString mapped : mapping) {
                            if (mapped.from() == intValue) {
                                obj = mapped.to();
                                break;
                            }
                        }
                        if (obj == null) {
                            obj = Integer.valueOf(intValue);
                        }
                    }
                    if (property.formatToHexString()) {
                        obj = field.get(view);
                        if (type == Integer.TYPE) {
                            obj = formatIntToHexString(((Integer) obj).intValue());
                        } else if (type == Byte.TYPE) {
                            obj = "0x" + Byte.toHexString(((Byte) obj).byteValue(), true);
                        }
                    }
                } else {
                    obj = resolveId(context, field.getInt(view));
                }
                if (obj == null) {
                    obj = field.get(view);
                }
                writeEntry(out, categoryPrefix + prefix, field.getName(), "", obj);
            } catch (IllegalAccessException e) {
            }
        }
    }

    private static void writeEntry(BufferedWriter out, String prefix, String name, String suffix, Object value) throws IOException {
        out.write(prefix);
        out.write(name);
        out.write(suffix);
        out.write("=");
        writeValue(out, value);
        out.write(32);
    }

    private static void exportUnrolledFlags(BufferedWriter out, FlagToString[] mapping, int intValue, String prefix) throws IOException {
        for (FlagToString flagMapping : mapping) {
            boolean ifTrue = flagMapping.outputIf();
            int maskResult = intValue & flagMapping.mask();
            boolean test = maskResult == flagMapping.equals();
            if ((test && ifTrue) || !(test || ifTrue)) {
                writeEntry(out, prefix, flagMapping.name(), "", formatIntToHexString(maskResult));
            }
        }
    }

    private static void exportUnrolledArray(Context context, BufferedWriter out, ExportedProperty property, int[] array, String prefix, String suffix) throws IOException {
        IntToString[] indexMapping = property.indexMapping();
        boolean hasIndexMapping = indexMapping.length > 0;
        IntToString[] mapping = property.mapping();
        boolean hasMapping = mapping.length > 0;
        boolean resolveId = property.resolveId() && context != null;
        int valuesCount = array.length;
        for (int j = 0; j < valuesCount; j++) {
            String value = null;
            int intValue = array[j];
            String name = String.valueOf(j);
            if (hasIndexMapping) {
                for (IntToString mapped : indexMapping) {
                    if (mapped.from() == j) {
                        name = mapped.to();
                        break;
                    }
                }
            }
            if (hasMapping) {
                for (IntToString mapped2 : mapping) {
                    if (mapped2.from() == intValue) {
                        value = mapped2.to();
                        break;
                    }
                }
            }
            if (!resolveId) {
                value = String.valueOf(intValue);
            } else if (value == null) {
                value = (String) resolveId(context, intValue);
            }
            writeEntry(out, prefix, name, suffix, value);
        }
    }

    static Object resolveId(Context context, int id) {
        Resources resources = context.getResources();
        if (id < 0) {
            return "NO_ID";
        }
        try {
            return resources.getResourceTypeName(id) + '/' + resources.getResourceEntryName(id);
        } catch (NotFoundException e) {
            return "id/" + formatIntToHexString(id);
        }
    }

    private static void writeValue(BufferedWriter out, Object value) throws IOException {
        if (value != null) {
            String output = "[EXCEPTION]";
            try {
                output = value.toString().replace("\n", "\\n");
            } finally {
                out.write(String.valueOf(output.length()));
                out.write(FingerprintManager.FINGER_PERMISSION_DELIMITER);
                out.write(output);
            }
        } else {
            out.write("4,null");
        }
    }

    private static Field[] capturedViewGetPropertyFields(Class<?> klass) {
        if (mCapturedViewFieldsForClasses == null) {
            mCapturedViewFieldsForClasses = new HashMap();
        }
        HashMap<Class<?>, Field[]> map = mCapturedViewFieldsForClasses;
        Field[] fields = (Field[]) map.get(klass);
        if (fields != null) {
            return fields;
        }
        ArrayList<Field> foundFields = new ArrayList();
        for (Field field : klass.getFields()) {
            if (field.isAnnotationPresent(CapturedViewProperty.class)) {
                field.setAccessible(true);
                foundFields.add(field);
            }
        }
        fields = (Field[]) foundFields.toArray(new Field[foundFields.size()]);
        map.put(klass, fields);
        return fields;
    }

    private static Method[] capturedViewGetPropertyMethods(Class<?> klass) {
        if (mCapturedViewMethodsForClasses == null) {
            mCapturedViewMethodsForClasses = new HashMap();
        }
        HashMap<Class<?>, Method[]> map = mCapturedViewMethodsForClasses;
        Method[] methods = (Method[]) map.get(klass);
        if (methods != null) {
            return methods;
        }
        ArrayList<Method> foundMethods = new ArrayList();
        for (Method method : klass.getMethods()) {
            if (method.getParameterTypes().length == 0 && method.isAnnotationPresent(CapturedViewProperty.class) && method.getReturnType() != Void.class) {
                method.setAccessible(true);
                foundMethods.add(method);
            }
        }
        methods = (Method[]) foundMethods.toArray(new Method[foundMethods.size()]);
        map.put(klass, methods);
        return methods;
    }

    private static String capturedViewExportMethods(Object obj, Class<?> klass, String prefix) {
        if (obj == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        for (Method method : capturedViewGetPropertyMethods(klass)) {
            try {
                Object methodValue = method.invoke(obj, (Object[]) null);
                Class<?> returnType = method.getReturnType();
                if (((CapturedViewProperty) method.getAnnotation(CapturedViewProperty.class)).retrieveReturn()) {
                    sb.append(capturedViewExportMethods(methodValue, returnType, method.getName() + "#"));
                } else {
                    sb.append(prefix);
                    sb.append(method.getName());
                    sb.append("()=");
                    if (methodValue != null) {
                        sb.append(methodValue.toString().replace("\n", "\\n"));
                    } else {
                        sb.append("null");
                    }
                    sb.append("; ");
                }
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e2) {
            }
        }
        return sb.toString();
    }

    private static String capturedViewExportFields(Object obj, Class<?> klass, String prefix) {
        if (obj == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        for (Field field : capturedViewGetPropertyFields(klass)) {
            try {
                Object fieldValue = field.get(obj);
                sb.append(prefix);
                sb.append(field.getName());
                sb.append("=");
                if (fieldValue != null) {
                    sb.append(fieldValue.toString().replace("\n", "\\n"));
                } else {
                    sb.append("null");
                }
                sb.append(' ');
            } catch (IllegalAccessException e) {
            }
        }
        return sb.toString();
    }

    public static void dumpCapturedView(String tag, Object view) {
        Class<?> klass = view.getClass();
        StringBuilder sb = new StringBuilder(klass.getName() + ": ");
        sb.append(capturedViewExportFields(view, klass, ""));
        sb.append(capturedViewExportMethods(view, klass, ""));
        Log.d(tag, sb.toString());
    }

    public static Object invokeViewMethod(View view, Method method, Object[] args) {
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<Object> result = new AtomicReference();
        final AtomicReference<Throwable> exception = new AtomicReference();
        final Method method2 = method;
        final View view2 = view;
        final Object[] objArr = args;
        view.post(new Runnable() {
            public void run() {
                try {
                    result.set(method2.invoke(view2, objArr));
                } catch (InvocationTargetException e) {
                    exception.set(e.getCause());
                } catch (Exception e2) {
                    exception.set(e2);
                }
                latch.countDown();
            }
        });
        try {
            latch.await();
            if (exception.get() == null) {
                return result.get();
            }
            throw new RuntimeException((Throwable) exception.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setLayoutParameter(final View view, String param, int value) throws NoSuchFieldException, IllegalAccessException {
        final ViewGroup.LayoutParams p = view.getLayoutParams();
        Field f = p.getClass().getField(param);
        if (f.getType() != Integer.TYPE) {
            throw new RuntimeException("Only integer layout parameters can be set. Field " + param + " is of type " + f.getType().getSimpleName());
        }
        f.set(p, Integer.valueOf(value));
        view.post(new Runnable() {
            public void run() {
                view.setLayoutParams(p);
            }
        });
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void dump_s(android.view.View r230, boolean r231, boolean r232, java.io.OutputStream r233) throws java.io.IOException {
        /*
        r81 = 0;
        r164 = 0;
        r184 = 1;
        r225 = r230.getContext();	 Catch:{ Exception -> 0x0360 }
        r226 = "power";
        r173 = r225.getSystemService(r226);	 Catch:{ Exception -> 0x0360 }
        r173 = (android.os.PowerManager) r173;	 Catch:{ Exception -> 0x0360 }
        r184 = r173.isScreenOn();	 Catch:{ Exception -> 0x0360 }
        r117 = 0;
        r166 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r166.<init>();	 Catch:{ Exception -> 0x0360 }
        r155 = 3;
        r197 = 0;
        r195 = 0;
        r51 = 0;
        r47 = 0;
        r46 = 0;
        r41 = 0;
        r45 = 0;
        r42 = 0;
        r43 = 0;
        r44 = 0;
        r32 = 0;
        r30 = 0;
        r28 = 0;
        r31 = 0;
        r29 = 0;
        r37 = 0;
        r36 = 0;
        r34 = 0;
        r33 = 0;
        r35 = 0;
        r40 = 0;
        r102 = 0;
        r6 = 0;
        r5 = 1;
        r8 = 2;
        r167 = 0;
        r27 = 0;
        r39 = 0;
        r133 = 0;
        r108 = 0;
        r107 = 0;
        r58 = 0;
        r188 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0360 }
        r188.<init>();	 Catch:{ Exception -> 0x0360 }
        r213 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0360 }
        r213.<init>();	 Catch:{ Exception -> 0x0360 }
        r92 = new java.util.Hashtable;	 Catch:{ Exception -> 0x0360 }
        r92.<init>();	 Catch:{ Exception -> 0x0360 }
        r51 = r230.getRootView();	 Catch:{ Exception -> 0x0360 }
        r0 = r51;
        r225 = r0;
        r225 = r225.getVisibility();	 Catch:{ Exception -> 0x0360 }
        if (r225 != 0) goto L_0x00af;
    L_0x007c:
        r0 = r188;
        r1 = r51;
        r0.add(r1);	 Catch:{ Exception -> 0x0360 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = r51.getClass();	 Catch:{ Exception -> 0x0360 }
        r226 = r226.getSimpleName();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r51.hashCode();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r226 = 0;
        r226 = java.lang.Integer.valueOf(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r92;
        r1 = r225;
        r2 = r226;
        r0.put(r1, r2);	 Catch:{ Exception -> 0x0360 }
    L_0x00af:
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r226.<init>();	 Catch:{ Exception -> 0x0360 }
        r227 = "Stack Size : ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r227 = r188.size();	 Catch:{ Exception -> 0x0360 }
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0360 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0360 }
    L_0x00cb:
        r225 = r188.size();	 Catch:{ Exception -> 0x0360 }
        if (r225 <= 0) goto L_0x20e8;
    L_0x00d1:
        r225 = r188.size();	 Catch:{ Exception -> 0x0360 }
        r80 = r225 + -1;
        r0 = r188;
        r1 = r80;
        r51 = r0.remove(r1);	 Catch:{ Exception -> 0x0360 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = r51.getClass();	 Catch:{ Exception -> 0x0360 }
        r226 = r226.getSimpleName();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r51.hashCode();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r92;
        r1 = r225;
        r225 = r0.get(r1);	 Catch:{ Exception -> 0x0360 }
        r225 = (java.lang.Integer) r225;	 Catch:{ Exception -> 0x0360 }
        r91 = r225.intValue();	 Catch:{ Exception -> 0x0360 }
        r0 = r213;
        r1 = r51;
        r0.add(r1);	 Catch:{ Exception -> 0x0360 }
        r0 = r51;
        r0 = r0 instanceof android.view.ViewGroup;	 Catch:{ Exception -> 0x0360 }
        r225 = r0;
        if (r225 == 0) goto L_0x0178;
    L_0x0119:
        r0 = r51;
        r0 = (android.view.ViewGroup) r0;	 Catch:{ Exception -> 0x0360 }
        r211 = r0;
        r225 = r211.getChildCount();	 Catch:{ Exception -> 0x0360 }
        r74 = r225 + -1;
    L_0x0125:
        if (r74 < 0) goto L_0x00cb;
    L_0x0127:
        r0 = r211;
        r1 = r74;
        r198 = r0.getChildAt(r1);	 Catch:{ Exception -> 0x0360 }
        r225 = r198.getVisibility();	 Catch:{ Exception -> 0x0360 }
        if (r225 != 0) goto L_0x0175;
    L_0x0135:
        r225 = "TDK";
        r226 = r198.getClass();	 Catch:{ Exception -> 0x0360 }
        r226 = r226.getName();	 Catch:{ Exception -> 0x0360 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r188;
        r1 = r198;
        r0.add(r1);	 Catch:{ Exception -> 0x0360 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = r198.getClass();	 Catch:{ Exception -> 0x0360 }
        r226 = r226.getSimpleName();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r198.hashCode();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r226 = r91 + 1;
        r226 = java.lang.Integer.valueOf(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r92;
        r1 = r225;
        r2 = r226;
        r0.put(r1, r2);	 Catch:{ Exception -> 0x0360 }
    L_0x0175:
        r74 = r74 + -1;
        goto L_0x0125;
    L_0x0178:
        r225 = r51.getClass();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.getSimpleName();	 Catch:{ Exception -> 0x0360 }
        r226 = "HomeSurfaceView";
        r225 = r225.contains(r226);	 Catch:{ Exception -> 0x0360 }
        if (r225 == 0) goto L_0x0a57;
    L_0x0188:
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r226.<init>();	 Catch:{ Exception -> 0x0360 }
        r227 = "Inside Home Surface View ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r227 = r51.getClass();	 Catch:{ Exception -> 0x0360 }
        r227 = r227.getName();	 Catch:{ Exception -> 0x0360 }
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0360 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0360 }
        r225 = android.view.SurfaceView.class;
        r226 = "mCallbacks";
        r106 = r225.getDeclaredField(r226);	 Catch:{ Exception -> 0x0341 }
        r225 = 1;
        r0 = r106;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x0341 }
        r0 = r106;
        r1 = r51;
        r194 = r0.get(r1);	 Catch:{ Exception -> 0x0341 }
        if (r194 == 0) goto L_0x00cb;
    L_0x01c3:
        r0 = r194;
        r0 = (java.util.ArrayList) r0;	 Catch:{ Exception -> 0x0341 }
        r97 = r0;
        r76 = r97.iterator();	 Catch:{ Exception -> 0x0341 }
    L_0x01cd:
        r225 = r76.hasNext();	 Catch:{ Exception -> 0x0341 }
        if (r225 == 0) goto L_0x00cb;
    L_0x01d3:
        r163 = r76.next();	 Catch:{ Exception -> 0x0341 }
        r225 = r163.getClass();	 Catch:{ Exception -> 0x0341 }
        r225 = r225.getName();	 Catch:{ Exception -> 0x0341 }
        r226 = "com.sec.android.app.launcher.activities.LauncherActivity";
        r225 = r225.equals(r226);	 Catch:{ Exception -> 0x0341 }
        if (r225 == 0) goto L_0x01cd;
    L_0x01e7:
        r225 = r163.getClass();	 Catch:{ Exception -> 0x0341 }
        r226 = "mVisibleFragmentId";
        r50 = r225.getDeclaredField(r226);	 Catch:{ Exception -> 0x0341 }
        r225 = 1;
        r0 = r50;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x0341 }
        r0 = r50;
        r1 = r163;
        r143 = r0.getInt(r1);	 Catch:{ Exception -> 0x0341 }
        if (r143 != 0) goto L_0x0616;
    L_0x0205:
        r225 = r163.getClass();	 Catch:{ Exception -> 0x0341 }
        r226 = "mHomeFragment";
        r122 = r225.getDeclaredField(r226);	 Catch:{ Exception -> 0x0341 }
        r225 = 1;
        r0 = r122;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x0341 }
        r0 = r122;
        r1 = r163;
        r71 = r0.get(r1);	 Catch:{ Exception -> 0x0341 }
        r225 = r71.getClass();	 Catch:{ Exception -> 0x0341 }
        r226 = "mPresenter";
        r123 = r225.getDeclaredField(r226);	 Catch:{ Exception -> 0x0341 }
        r225 = 1;
        r0 = r123;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x0341 }
        r0 = r123;
        r1 = r71;
        r73 = r0.get(r1);	 Catch:{ Exception -> 0x0341 }
        r225 = r73.getClass();	 Catch:{ Exception -> 0x0322 }
        r226 = "mFolderPresenter";
        r116 = r225.getDeclaredField(r226);	 Catch:{ Exception -> 0x0322 }
        r225 = 1;
        r0 = r116;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x0322 }
        r0 = r116;
        r1 = r73;
        r121 = r0.get(r1);	 Catch:{ Exception -> 0x0322 }
        r26 = 0;
        r170 = r121.getClass();	 Catch:{ Exception -> 0x0322 }
    L_0x025f:
        if (r170 == 0) goto L_0x026f;
    L_0x0261:
        r225 = r170.getSimpleName();	 Catch:{ Exception -> 0x0322 }
        r226 = "FolderPresenterBase";
        r225 = r225.equals(r226);	 Catch:{ Exception -> 0x0322 }
        if (r225 == 0) goto L_0x0374;
    L_0x026d:
        r26 = r170;
    L_0x026f:
        r225 = "getActiveOpenFolderView";
        r226 = 0;
        r0 = r26;
        r1 = r225;
        r2 = r226;
        r63 = r0.getDeclaredMethod(r1, r2);	 Catch:{ Exception -> 0x0322 }
        r225 = 1;
        r0 = r63;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x0322 }
        r225 = 0;
        r0 = r63;
        r1 = r121;
        r2 = r225;
        r10 = r0.invoke(r1, r2);	 Catch:{ Exception -> 0x0322 }
        if (r10 == 0) goto L_0x0380;
    L_0x0294:
        r225 = "getActiveFolderItem";
        r226 = 0;
        r0 = r26;
        r1 = r225;
        r2 = r226;
        r62 = r0.getDeclaredMethod(r1, r2);	 Catch:{ Exception -> 0x0322 }
        r225 = 1;
        r0 = r62;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x0322 }
        r225 = 0;
        r0 = r62;
        r1 = r121;
        r2 = r225;
        r102 = r0.invoke(r1, r2);	 Catch:{ Exception -> 0x0322 }
        r35 = r10.getClass();	 Catch:{ Exception -> 0x0322 }
        if (r37 != 0) goto L_0x02d1;
    L_0x02bd:
        r170 = r10.getClass();	 Catch:{ Exception -> 0x0322 }
    L_0x02c1:
        if (r170 == 0) goto L_0x02d1;
    L_0x02c3:
        r225 = r170.getSimpleName();	 Catch:{ Exception -> 0x0322 }
        r226 = "NativeViewBase";
        r225 = r225.equals(r226);	 Catch:{ Exception -> 0x0322 }
        if (r225 == 0) goto L_0x037a;
    L_0x02cf:
        r37 = r170;
    L_0x02d1:
        if (r37 == 0) goto L_0x01cd;
    L_0x02d3:
        r225 = "mVisible";
        r0 = r37;
        r1 = r225;
        r218 = r0.getDeclaredField(r1);	 Catch:{ Exception -> 0x0322 }
        r225 = 1;
        r0 = r218;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x0322 }
        r0 = r218;
        r225 = r0.getInt(r10);	 Catch:{ Exception -> 0x0322 }
        if (r225 != 0) goto L_0x01cd;
    L_0x02ef:
        r0 = r188;
        r0.add(r10);	 Catch:{ Exception -> 0x0322 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0322 }
        r225.<init>();	 Catch:{ Exception -> 0x0322 }
        r226 = r10.getClass();	 Catch:{ Exception -> 0x0322 }
        r226 = r226.getSimpleName();	 Catch:{ Exception -> 0x0322 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0322 }
        r226 = r10.hashCode();	 Catch:{ Exception -> 0x0322 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0322 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0322 }
        r226 = r91 + 1;
        r226 = java.lang.Integer.valueOf(r226);	 Catch:{ Exception -> 0x0322 }
        r0 = r92;
        r1 = r225;
        r2 = r226;
        r0.put(r1, r2);	 Catch:{ Exception -> 0x0322 }
        goto L_0x01cd;
    L_0x0322:
        r57 = move-exception;
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0341 }
        r226.<init>();	 Catch:{ Exception -> 0x0341 }
        r227 = "Exception while getting home fragment items : ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0341 }
        r0 = r226;
        r1 = r57;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x0341 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0341 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0341 }
        goto L_0x01cd;
    L_0x0341:
        r57 = move-exception;
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r226.<init>();	 Catch:{ Exception -> 0x0360 }
        r227 = "Exception in get mCallbacks";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r0 = r226;
        r1 = r57;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0360 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0360 }
        goto L_0x00cb;
    L_0x0360:
        r52 = move-exception;
    L_0x0361:
        r225 = "TDK";
        r226 = "Problem dumping the view:";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.w(r0, r1, r2);	 Catch:{ all -> 0x0544 }
        if (r164 == 0) goto L_0x0373;
    L_0x0370:
        r164.close();
    L_0x0373:
        return;
    L_0x0374:
        r170 = r170.getSuperclass();	 Catch:{ Exception -> 0x0322 }
        goto L_0x025f;
    L_0x037a:
        r170 = r170.getSuperclass();	 Catch:{ Exception -> 0x0322 }
        goto L_0x02c1;
    L_0x0380:
        r225 = r73.getClass();	 Catch:{ Exception -> 0x0322 }
        r226 = "mPagePresenters";
        r128 = r225.getDeclaredField(r226);	 Catch:{ Exception -> 0x0322 }
        r225 = 1;
        r0 = r128;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x0322 }
        r0 = r128;
        r1 = r73;
        r168 = r0.get(r1);	 Catch:{ Exception -> 0x0322 }
        r225 = r73.getClass();	 Catch:{ Exception -> 0x0322 }
        r226 = "mTopFivePresenter";
        r142 = r225.getDeclaredField(r226);	 Catch:{ Exception -> 0x0322 }
        r225 = 1;
        r0 = r142;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x0322 }
        r0 = r142;
        r1 = r73;
        r159 = r0.get(r1);	 Catch:{ Exception -> 0x0322 }
        r225 = r159.getClass();	 Catch:{ Exception -> 0x0322 }
        r226 = "mAdapter";
        r192 = r225.getDeclaredField(r226);	 Catch:{ Exception -> 0x0322 }
        r225 = 1;
        r0 = r192;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x0322 }
        r0 = r192;
        r1 = r159;
        r157 = r0.get(r1);	 Catch:{ Exception -> 0x0322 }
        r17 = 0;
        r206 = 0;
        r225 = r157.getClass();	 Catch:{ Exception -> 0x0525 }
        r226 = "getItems";
        r227 = 0;
        r225 = r225.getDeclaredMethod(r226, r227);	 Catch:{ Exception -> 0x0525 }
        r226 = 0;
        r0 = r225;
        r1 = r157;
        r2 = r226;
        r17 = r0.invoke(r1, r2);	 Catch:{ Exception -> 0x0525 }
        r0 = r17;
        r0 = (java.util.List) r0;	 Catch:{ Exception -> 0x0525 }
        r206 = r0;
    L_0x03f5:
        r0 = r168;
        r0 = (java.util.concurrent.CopyOnWriteArrayList) r0;	 Catch:{ Exception -> 0x0322 }
        r16 = r0;
        r74 = 0;
    L_0x03fd:
        r225 = r16.size();	 Catch:{ Exception -> 0x0322 }
        r0 = r74;
        r1 = r225;
        if (r0 >= r1) goto L_0x0588;
    L_0x0407:
        r38 = 0;
        r225 = 0;
        r0 = r16;
        r1 = r225;
        r225 = r0.get(r1);	 Catch:{ Exception -> 0x0322 }
        r225 = r225.getClass();	 Catch:{ Exception -> 0x0322 }
        r225 = r225.getSimpleName();	 Catch:{ Exception -> 0x0322 }
        r226 = "HomeZeroPagePresenter";
        r225 = r225.equals(r226);	 Catch:{ Exception -> 0x0322 }
        if (r225 == 0) goto L_0x054b;
    L_0x0423:
        r225 = 0;
        r0 = r16;
        r1 = r225;
        r225 = r0.get(r1);	 Catch:{ Exception -> 0x0322 }
        r225 = r225.getClass();	 Catch:{ Exception -> 0x0322 }
        r38 = r225.getSuperclass();	 Catch:{ Exception -> 0x0322 }
    L_0x0435:
        r225 = "getItems";
        r226 = 0;
        r0 = r38;
        r1 = r225;
        r2 = r226;
        r150 = r0.getDeclaredMethod(r1, r2);	 Catch:{ Exception -> 0x0567 }
        r225 = 1;
        r0 = r150;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x0567 }
        r0 = r16;
        r1 = r74;
        r225 = r0.get(r1);	 Catch:{ Exception -> 0x0567 }
        r226 = 0;
        r0 = r150;
        r1 = r225;
        r2 = r226;
        r190 = r0.invoke(r1, r2);	 Catch:{ Exception -> 0x0567 }
        r0 = r190;
        r0 = (java.util.List) r0;	 Catch:{ Exception -> 0x0567 }
        r7 = r0;
        if (r36 != 0) goto L_0x0483;
    L_0x0467:
        r225 = 0;
        r0 = r225;
        r225 = r7.get(r0);	 Catch:{ Exception -> 0x0567 }
        r170 = r225.getClass();	 Catch:{ Exception -> 0x0567 }
    L_0x0473:
        if (r170 == 0) goto L_0x0483;
    L_0x0475:
        r225 = r170.getSimpleName();	 Catch:{ Exception -> 0x0567 }
        r226 = "Item";
        r225 = r225.equals(r226);	 Catch:{ Exception -> 0x0567 }
        if (r225 == 0) goto L_0x055b;
    L_0x0481:
        r36 = r170;
    L_0x0483:
        r88 = 0;
    L_0x0485:
        r225 = r7.size();	 Catch:{ Exception -> 0x0567 }
        r0 = r88;
        r1 = r225;
        if (r0 >= r1) goto L_0x0584;
    L_0x048f:
        r225 = "getItemView";
        r226 = 0;
        r0 = r36;
        r1 = r225;
        r2 = r226;
        r64 = r0.getDeclaredMethod(r1, r2);	 Catch:{ Exception -> 0x0567 }
        r225 = 1;
        r0 = r64;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x0567 }
        r0 = r88;
        r225 = r7.get(r0);	 Catch:{ Exception -> 0x0567 }
        r226 = 0;
        r0 = r64;
        r1 = r225;
        r2 = r226;
        r72 = r0.invoke(r1, r2);	 Catch:{ Exception -> 0x0567 }
        if (r37 != 0) goto L_0x04ce;
    L_0x04ba:
        r170 = r72.getClass();	 Catch:{ Exception -> 0x0567 }
    L_0x04be:
        if (r170 == 0) goto L_0x04ce;
    L_0x04c0:
        r225 = r170.getSimpleName();	 Catch:{ Exception -> 0x0567 }
        r226 = "NativeViewBase";
        r225 = r225.equals(r226);	 Catch:{ Exception -> 0x0567 }
        if (r225 == 0) goto L_0x0561;
    L_0x04cc:
        r37 = r170;
    L_0x04ce:
        if (r37 == 0) goto L_0x0521;
    L_0x04d0:
        r225 = "mVisible";
        r0 = r37;
        r1 = r225;
        r218 = r0.getDeclaredField(r1);	 Catch:{ Exception -> 0x0567 }
        r225 = 1;
        r0 = r218;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x0567 }
        r0 = r218;
        r1 = r72;
        r225 = r0.getInt(r1);	 Catch:{ Exception -> 0x0567 }
        if (r225 != 0) goto L_0x0521;
    L_0x04ee:
        r0 = r188;
        r1 = r72;
        r0.add(r1);	 Catch:{ Exception -> 0x0567 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0567 }
        r225.<init>();	 Catch:{ Exception -> 0x0567 }
        r226 = r72.getClass();	 Catch:{ Exception -> 0x0567 }
        r226 = r226.getSimpleName();	 Catch:{ Exception -> 0x0567 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0567 }
        r226 = r72.hashCode();	 Catch:{ Exception -> 0x0567 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0567 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0567 }
        r226 = r91 + 1;
        r226 = java.lang.Integer.valueOf(r226);	 Catch:{ Exception -> 0x0567 }
        r0 = r92;
        r1 = r225;
        r2 = r226;
        r0.put(r1, r2);	 Catch:{ Exception -> 0x0567 }
    L_0x0521:
        r88 = r88 + 1;
        goto L_0x0485;
    L_0x0525:
        r57 = move-exception;
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0322 }
        r226.<init>();	 Catch:{ Exception -> 0x0322 }
        r227 = "Exception in adapterlist ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0322 }
        r0 = r226;
        r1 = r57;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x0322 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0322 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0322 }
        goto L_0x03f5;
    L_0x0544:
        r225 = move-exception;
    L_0x0545:
        if (r164 == 0) goto L_0x054a;
    L_0x0547:
        r164.close();
    L_0x054a:
        throw r225;
    L_0x054b:
        r225 = 0;
        r0 = r16;
        r1 = r225;
        r225 = r0.get(r1);	 Catch:{ Exception -> 0x0322 }
        r38 = r225.getClass();	 Catch:{ Exception -> 0x0322 }
        goto L_0x0435;
    L_0x055b:
        r170 = r170.getSuperclass();	 Catch:{ Exception -> 0x0567 }
        goto L_0x0473;
    L_0x0561:
        r170 = r170.getSuperclass();	 Catch:{ Exception -> 0x0567 }
        goto L_0x04be;
    L_0x0567:
        r52 = move-exception;
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0322 }
        r226.<init>();	 Catch:{ Exception -> 0x0322 }
        r227 = "Exception in get mCallbacks";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0322 }
        r0 = r226;
        r1 = r52;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x0322 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0322 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0322 }
    L_0x0584:
        r74 = r74 + 1;
        goto L_0x03fd;
    L_0x0588:
        r88 = 0;
    L_0x058a:
        r225 = r206.size();	 Catch:{ Exception -> 0x0322 }
        r0 = r88;
        r1 = r225;
        if (r0 >= r1) goto L_0x01cd;
    L_0x0594:
        r225 = "getItemView";
        r226 = 0;
        r0 = r36;
        r1 = r225;
        r2 = r226;
        r64 = r0.getDeclaredMethod(r1, r2);	 Catch:{ Exception -> 0x0322 }
        r225 = 1;
        r0 = r64;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x0322 }
        r0 = r206;
        r1 = r88;
        r225 = r0.get(r1);	 Catch:{ Exception -> 0x0322 }
        r226 = 0;
        r0 = r64;
        r1 = r225;
        r2 = r226;
        r72 = r0.invoke(r1, r2);	 Catch:{ Exception -> 0x0322 }
        if (r37 == 0) goto L_0x0612;
    L_0x05c1:
        r225 = "mVisible";
        r0 = r37;
        r1 = r225;
        r218 = r0.getDeclaredField(r1);	 Catch:{ Exception -> 0x0322 }
        r225 = 1;
        r0 = r218;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x0322 }
        r0 = r218;
        r1 = r72;
        r225 = r0.getInt(r1);	 Catch:{ Exception -> 0x0322 }
        if (r225 != 0) goto L_0x0612;
    L_0x05df:
        r0 = r188;
        r1 = r72;
        r0.add(r1);	 Catch:{ Exception -> 0x0322 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0322 }
        r225.<init>();	 Catch:{ Exception -> 0x0322 }
        r226 = r72.getClass();	 Catch:{ Exception -> 0x0322 }
        r226 = r226.getSimpleName();	 Catch:{ Exception -> 0x0322 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0322 }
        r226 = r72.hashCode();	 Catch:{ Exception -> 0x0322 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0322 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0322 }
        r226 = r91 + 1;
        r226 = java.lang.Integer.valueOf(r226);	 Catch:{ Exception -> 0x0322 }
        r0 = r92;
        r1 = r225;
        r2 = r226;
        r0.put(r1, r2);	 Catch:{ Exception -> 0x0322 }
    L_0x0612:
        r88 = r88 + 1;
        goto L_0x058a;
    L_0x0616:
        r225 = 1;
        r0 = r143;
        r1 = r225;
        if (r0 != r1) goto L_0x092c;
    L_0x061e:
        r225 = r163.getClass();	 Catch:{ Exception -> 0x0341 }
        r226 = "mAppsFragment";
        r104 = r225.getDeclaredField(r226);	 Catch:{ Exception -> 0x0341 }
        r225 = 1;
        r0 = r104;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x0341 }
        r0 = r104;
        r1 = r163;
        r14 = r0.get(r1);	 Catch:{ Exception -> 0x0341 }
        r225 = r14.getClass();	 Catch:{ Exception -> 0x0341 }
        r226 = "mAppsPresenter";
        r105 = r225.getDeclaredField(r226);	 Catch:{ Exception -> 0x0341 }
        r225 = 1;
        r0 = r105;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x0341 }
        r0 = r105;
        r12 = r0.get(r14);	 Catch:{ Exception -> 0x0341 }
        r60 = 0;
        r13 = 0;
        r225 = r12.getClass();	 Catch:{ Exception -> 0x0734 }
        r226 = "mFolderPresenter";
        r116 = r225.getDeclaredField(r226);	 Catch:{ Exception -> 0x0734 }
        r225 = 1;
        r0 = r116;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x0734 }
        r0 = r116;
        r13 = r0.get(r12);	 Catch:{ Exception -> 0x0734 }
        r26 = 0;
        r170 = r13.getClass();	 Catch:{ Exception -> 0x0734 }
    L_0x0675:
        if (r170 == 0) goto L_0x0685;
    L_0x0677:
        r225 = r170.getSimpleName();	 Catch:{ Exception -> 0x0734 }
        r226 = "FolderPresenterBase";
        r225 = r225.equals(r226);	 Catch:{ Exception -> 0x0734 }
        if (r225 == 0) goto L_0x0753;
    L_0x0683:
        r26 = r170;
    L_0x0685:
        r225 = "getActiveOpenFolderView";
        r226 = 0;
        r0 = r26;
        r1 = r225;
        r2 = r226;
        r63 = r0.getDeclaredMethod(r1, r2);	 Catch:{ Exception -> 0x0734 }
        r225 = 1;
        r0 = r63;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x0734 }
        r225 = 0;
        r0 = r63;
        r1 = r225;
        r10 = r0.invoke(r13, r1);	 Catch:{ Exception -> 0x0734 }
        if (r10 == 0) goto L_0x075f;
    L_0x06a8:
        r225 = "getActiveFolderItem";
        r226 = 0;
        r0 = r26;
        r1 = r225;
        r2 = r226;
        r62 = r0.getDeclaredMethod(r1, r2);	 Catch:{ Exception -> 0x0734 }
        r225 = 1;
        r0 = r62;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x0734 }
        r225 = 0;
        r0 = r62;
        r1 = r225;
        r102 = r0.invoke(r13, r1);	 Catch:{ Exception -> 0x0734 }
        r35 = r10.getClass();	 Catch:{ Exception -> 0x0734 }
        if (r37 != 0) goto L_0x06e3;
    L_0x06cf:
        r170 = r10.getClass();	 Catch:{ Exception -> 0x0734 }
    L_0x06d3:
        if (r170 == 0) goto L_0x06e3;
    L_0x06d5:
        r225 = r170.getSimpleName();	 Catch:{ Exception -> 0x0734 }
        r226 = "NativeViewBase";
        r225 = r225.equals(r226);	 Catch:{ Exception -> 0x0734 }
        if (r225 == 0) goto L_0x0759;
    L_0x06e1:
        r37 = r170;
    L_0x06e3:
        if (r37 == 0) goto L_0x01cd;
    L_0x06e5:
        r225 = "mVisible";
        r0 = r37;
        r1 = r225;
        r218 = r0.getDeclaredField(r1);	 Catch:{ Exception -> 0x0734 }
        r225 = 1;
        r0 = r218;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x0734 }
        r0 = r218;
        r225 = r0.getInt(r10);	 Catch:{ Exception -> 0x0734 }
        if (r225 != 0) goto L_0x01cd;
    L_0x0701:
        r0 = r188;
        r0.add(r10);	 Catch:{ Exception -> 0x0734 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0734 }
        r225.<init>();	 Catch:{ Exception -> 0x0734 }
        r226 = r10.getClass();	 Catch:{ Exception -> 0x0734 }
        r226 = r226.getSimpleName();	 Catch:{ Exception -> 0x0734 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0734 }
        r226 = r10.hashCode();	 Catch:{ Exception -> 0x0734 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0734 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0734 }
        r226 = r91 + 1;
        r226 = java.lang.Integer.valueOf(r226);	 Catch:{ Exception -> 0x0734 }
        r0 = r92;
        r1 = r225;
        r2 = r226;
        r0.put(r1, r2);	 Catch:{ Exception -> 0x0734 }
        goto L_0x01cd;
    L_0x0734:
        r57 = move-exception;
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0341 }
        r226.<init>();	 Catch:{ Exception -> 0x0341 }
        r227 = " Exception while getting folder item ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0341 }
        r0 = r226;
        r1 = r57;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x0341 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0341 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0341 }
        goto L_0x01cd;
    L_0x0753:
        r170 = r170.getSuperclass();	 Catch:{ Exception -> 0x0734 }
        goto L_0x0675;
    L_0x0759:
        r170 = r170.getSuperclass();	 Catch:{ Exception -> 0x0734 }
        goto L_0x06d3;
    L_0x075f:
        r225 = r13.getClass();	 Catch:{ Exception -> 0x0734 }
        r225 = r225.getSuperclass();	 Catch:{ Exception -> 0x0734 }
        r226 = "getFolderItems";
        r227 = 0;
        r225 = r225.getDeclaredMethod(r226, r227);	 Catch:{ Exception -> 0x0734 }
        r226 = 0;
        r0 = r225;
        r1 = r226;
        r225 = r0.invoke(r13, r1);	 Catch:{ Exception -> 0x0734 }
        r0 = r225;
        r0 = (java.util.List) r0;	 Catch:{ Exception -> 0x0734 }
        r60 = r0;
        r225 = "TDK";
        r226 = " Found folder item list ";
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0734 }
        r74 = 0;
    L_0x0788:
        r225 = r60.size();	 Catch:{ Exception -> 0x0734 }
        r0 = r74;
        r1 = r225;
        if (r0 >= r1) goto L_0x0853;
    L_0x0792:
        if (r36 != 0) goto L_0x07b0;
    L_0x0794:
        r0 = r60;
        r1 = r74;
        r225 = r0.get(r1);	 Catch:{ Exception -> 0x0734 }
        r170 = r225.getClass();	 Catch:{ Exception -> 0x0734 }
    L_0x07a0:
        if (r170 == 0) goto L_0x07b0;
    L_0x07a2:
        r225 = r170.getSimpleName();	 Catch:{ Exception -> 0x0734 }
        r226 = "Item";
        r225 = r225.equals(r226);	 Catch:{ Exception -> 0x0734 }
        if (r225 == 0) goto L_0x0848;
    L_0x07ae:
        r36 = r170;
    L_0x07b0:
        r225 = "getItemView";
        r226 = 0;
        r0 = r36;
        r1 = r225;
        r2 = r226;
        r64 = r0.getDeclaredMethod(r1, r2);	 Catch:{ Exception -> 0x0734 }
        r225 = 1;
        r0 = r64;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x0734 }
        r0 = r60;
        r1 = r74;
        r225 = r0.get(r1);	 Catch:{ Exception -> 0x0734 }
        r226 = 0;
        r0 = r64;
        r1 = r225;
        r2 = r226;
        r72 = r0.invoke(r1, r2);	 Catch:{ Exception -> 0x0734 }
        if (r37 != 0) goto L_0x07f1;
    L_0x07dd:
        r170 = r72.getClass();	 Catch:{ Exception -> 0x0734 }
    L_0x07e1:
        if (r170 == 0) goto L_0x07f1;
    L_0x07e3:
        r225 = r170.getSimpleName();	 Catch:{ Exception -> 0x0734 }
        r226 = "NativeViewBase";
        r225 = r225.equals(r226);	 Catch:{ Exception -> 0x0734 }
        if (r225 == 0) goto L_0x084e;
    L_0x07ef:
        r37 = r170;
    L_0x07f1:
        if (r37 == 0) goto L_0x0844;
    L_0x07f3:
        r225 = "mVisible";
        r0 = r37;
        r1 = r225;
        r218 = r0.getDeclaredField(r1);	 Catch:{ Exception -> 0x0734 }
        r225 = 1;
        r0 = r218;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x0734 }
        r0 = r218;
        r1 = r72;
        r225 = r0.getInt(r1);	 Catch:{ Exception -> 0x0734 }
        if (r225 != 0) goto L_0x0844;
    L_0x0811:
        r0 = r188;
        r1 = r72;
        r0.add(r1);	 Catch:{ Exception -> 0x0734 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0734 }
        r225.<init>();	 Catch:{ Exception -> 0x0734 }
        r226 = r72.getClass();	 Catch:{ Exception -> 0x0734 }
        r226 = r226.getSimpleName();	 Catch:{ Exception -> 0x0734 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0734 }
        r226 = r72.hashCode();	 Catch:{ Exception -> 0x0734 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0734 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0734 }
        r226 = r91 + 1;
        r226 = java.lang.Integer.valueOf(r226);	 Catch:{ Exception -> 0x0734 }
        r0 = r92;
        r1 = r225;
        r2 = r226;
        r0.put(r1, r2);	 Catch:{ Exception -> 0x0734 }
    L_0x0844:
        r74 = r74 + 1;
        goto L_0x0788;
    L_0x0848:
        r170 = r170.getSuperclass();	 Catch:{ Exception -> 0x0734 }
        goto L_0x07a0;
    L_0x084e:
        r170 = r170.getSuperclass();	 Catch:{ Exception -> 0x0734 }
        goto L_0x07e1;
    L_0x0853:
        r225 = r12.getClass();	 Catch:{ Exception -> 0x0734 }
        r226 = "mItemList";
        r124 = r225.getDeclaredField(r226);	 Catch:{ Exception -> 0x0734 }
        r225 = 1;
        r0 = r124;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x0734 }
        r0 = r124;
        r84 = r0.get(r12);	 Catch:{ Exception -> 0x0734 }
        r84 = (java.util.HashSet) r84;	 Catch:{ Exception -> 0x0734 }
        r75 = r84.iterator();	 Catch:{ Exception -> 0x0734 }
    L_0x0873:
        r225 = r75.hasNext();	 Catch:{ Exception -> 0x0734 }
        if (r225 == 0) goto L_0x01cd;
    L_0x0879:
        r187 = r75.next();	 Catch:{ Exception -> 0x0734 }
        if (r36 != 0) goto L_0x0893;
    L_0x087f:
        r170 = r187.getClass();	 Catch:{ Exception -> 0x0734 }
    L_0x0883:
        if (r170 == 0) goto L_0x0893;
    L_0x0885:
        r225 = r170.getSimpleName();	 Catch:{ Exception -> 0x0734 }
        r226 = "Item";
        r225 = r225.equals(r226);	 Catch:{ Exception -> 0x0734 }
        if (r225 == 0) goto L_0x0921;
    L_0x0891:
        r36 = r170;
    L_0x0893:
        r225 = "getItemView";
        r226 = 0;
        r0 = r36;
        r1 = r225;
        r2 = r226;
        r64 = r0.getDeclaredMethod(r1, r2);	 Catch:{ Exception -> 0x0734 }
        r225 = 1;
        r0 = r64;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x0734 }
        r225 = 0;
        r0 = r64;
        r1 = r187;
        r2 = r225;
        r72 = r0.invoke(r1, r2);	 Catch:{ Exception -> 0x0734 }
        if (r37 != 0) goto L_0x08cc;
    L_0x08b8:
        r170 = r72.getClass();	 Catch:{ Exception -> 0x0734 }
    L_0x08bc:
        if (r170 == 0) goto L_0x08cc;
    L_0x08be:
        r225 = r170.getSimpleName();	 Catch:{ Exception -> 0x0734 }
        r226 = "NativeViewBase";
        r225 = r225.equals(r226);	 Catch:{ Exception -> 0x0734 }
        if (r225 == 0) goto L_0x0927;
    L_0x08ca:
        r37 = r170;
    L_0x08cc:
        if (r37 == 0) goto L_0x0873;
    L_0x08ce:
        r225 = "mVisible";
        r0 = r37;
        r1 = r225;
        r218 = r0.getDeclaredField(r1);	 Catch:{ Exception -> 0x0734 }
        r225 = 1;
        r0 = r218;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x0734 }
        r0 = r218;
        r1 = r72;
        r225 = r0.getInt(r1);	 Catch:{ Exception -> 0x0734 }
        if (r225 != 0) goto L_0x0873;
    L_0x08ec:
        r0 = r188;
        r1 = r72;
        r0.add(r1);	 Catch:{ Exception -> 0x0734 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0734 }
        r225.<init>();	 Catch:{ Exception -> 0x0734 }
        r226 = r72.getClass();	 Catch:{ Exception -> 0x0734 }
        r226 = r226.getSimpleName();	 Catch:{ Exception -> 0x0734 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0734 }
        r226 = r72.hashCode();	 Catch:{ Exception -> 0x0734 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0734 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0734 }
        r226 = r91 + 1;
        r226 = java.lang.Integer.valueOf(r226);	 Catch:{ Exception -> 0x0734 }
        r0 = r92;
        r1 = r225;
        r2 = r226;
        r0.put(r1, r2);	 Catch:{ Exception -> 0x0734 }
        goto L_0x0873;
    L_0x0921:
        r170 = r170.getSuperclass();	 Catch:{ Exception -> 0x0734 }
        goto L_0x0883;
    L_0x0927:
        r170 = r170.getSuperclass();	 Catch:{ Exception -> 0x0734 }
        goto L_0x08bc;
    L_0x092c:
        r225 = 2;
        r0 = r143;
        r1 = r225;
        if (r0 != r1) goto L_0x01cd;
    L_0x0934:
        r225 = r163.getClass();	 Catch:{ Exception -> 0x0341 }
        r226 = "mWidgetsFragment";
        r144 = r225.getDeclaredField(r226);	 Catch:{ Exception -> 0x0341 }
        r225 = 1;
        r0 = r144;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x0341 }
        r0 = r144;
        r1 = r163;
        r220 = r0.get(r1);	 Catch:{ Exception -> 0x0341 }
        r225 = r220.getClass();	 Catch:{ Exception -> 0x0341 }
        r226 = "mWidgetsPresenter";
        r145 = r225.getDeclaredField(r226);	 Catch:{ Exception -> 0x0341 }
        r225 = 1;
        r0 = r145;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x0341 }
        r0 = r145;
        r1 = r220;
        r221 = r0.get(r1);	 Catch:{ Exception -> 0x0341 }
        r225 = r221.getClass();	 Catch:{ Exception -> 0x0341 }
        r226 = "mWidgetItemList";
        r124 = r225.getDeclaredField(r226);	 Catch:{ Exception -> 0x0341 }
        r225 = 1;
        r0 = r124;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x0341 }
        r0 = r124;
        r1 = r221;
        r85 = r0.get(r1);	 Catch:{ Exception -> 0x0341 }
        r85 = (java.util.List) r85;	 Catch:{ Exception -> 0x0341 }
        r74 = 0;
    L_0x098c:
        r225 = r85.size();	 Catch:{ Exception -> 0x0341 }
        r0 = r74;
        r1 = r225;
        if (r0 >= r1) goto L_0x01cd;
    L_0x0996:
        if (r36 != 0) goto L_0x09b4;
    L_0x0998:
        r0 = r85;
        r1 = r74;
        r225 = r0.get(r1);	 Catch:{ Exception -> 0x0341 }
        r170 = r225.getClass();	 Catch:{ Exception -> 0x0341 }
    L_0x09a4:
        if (r170 == 0) goto L_0x09b4;
    L_0x09a6:
        r225 = r170.getSimpleName();	 Catch:{ Exception -> 0x0341 }
        r226 = "Item";
        r225 = r225.equals(r226);	 Catch:{ Exception -> 0x0341 }
        if (r225 == 0) goto L_0x0a4c;
    L_0x09b2:
        r36 = r170;
    L_0x09b4:
        r225 = "getItemView";
        r226 = 0;
        r0 = r36;
        r1 = r225;
        r2 = r226;
        r64 = r0.getDeclaredMethod(r1, r2);	 Catch:{ Exception -> 0x0341 }
        r225 = 1;
        r0 = r64;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x0341 }
        r0 = r85;
        r1 = r74;
        r225 = r0.get(r1);	 Catch:{ Exception -> 0x0341 }
        r226 = 0;
        r0 = r64;
        r1 = r225;
        r2 = r226;
        r72 = r0.invoke(r1, r2);	 Catch:{ Exception -> 0x0341 }
        if (r37 != 0) goto L_0x09f5;
    L_0x09e1:
        r170 = r72.getClass();	 Catch:{ Exception -> 0x0341 }
    L_0x09e5:
        if (r170 == 0) goto L_0x09f5;
    L_0x09e7:
        r225 = r170.getSimpleName();	 Catch:{ Exception -> 0x0341 }
        r226 = "NativeViewBase";
        r225 = r225.equals(r226);	 Catch:{ Exception -> 0x0341 }
        if (r225 == 0) goto L_0x0a52;
    L_0x09f3:
        r37 = r170;
    L_0x09f5:
        if (r37 == 0) goto L_0x0a48;
    L_0x09f7:
        r225 = "mVisible";
        r0 = r37;
        r1 = r225;
        r218 = r0.getDeclaredField(r1);	 Catch:{ Exception -> 0x0341 }
        r225 = 1;
        r0 = r218;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x0341 }
        r0 = r218;
        r1 = r72;
        r225 = r0.getInt(r1);	 Catch:{ Exception -> 0x0341 }
        if (r225 != 0) goto L_0x0a48;
    L_0x0a15:
        r0 = r188;
        r1 = r72;
        r0.add(r1);	 Catch:{ Exception -> 0x0341 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0341 }
        r225.<init>();	 Catch:{ Exception -> 0x0341 }
        r226 = r72.getClass();	 Catch:{ Exception -> 0x0341 }
        r226 = r226.getSimpleName();	 Catch:{ Exception -> 0x0341 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0341 }
        r226 = r72.hashCode();	 Catch:{ Exception -> 0x0341 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0341 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0341 }
        r226 = r91 + 1;
        r226 = java.lang.Integer.valueOf(r226);	 Catch:{ Exception -> 0x0341 }
        r0 = r92;
        r1 = r225;
        r2 = r226;
        r0.put(r1, r2);	 Catch:{ Exception -> 0x0341 }
    L_0x0a48:
        r74 = r74 + 1;
        goto L_0x098c;
    L_0x0a4c:
        r170 = r170.getSuperclass();	 Catch:{ Exception -> 0x0341 }
        goto L_0x09a4;
    L_0x0a52:
        r170 = r170.getSuperclass();	 Catch:{ Exception -> 0x0341 }
        goto L_0x09e5;
    L_0x0a57:
        if (r35 == 0) goto L_0x0ca4;
    L_0x0a59:
        if (r102 == 0) goto L_0x0ca4;
    L_0x0a5b:
        r0 = r35;
        r1 = r51;
        r225 = r0.isInstance(r1);	 Catch:{ Exception -> 0x0360 }
        if (r225 == 0) goto L_0x0ca4;
    L_0x0a65:
        r9 = 0;
        r225 = "mTitleView";
        r0 = r35;
        r1 = r225;
        r140 = r0.getDeclaredField(r1);	 Catch:{ Exception -> 0x4d79, NoSuchFieldException -> 0x0c09 }
        r225 = 1;
        r0 = r140;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x4d79, NoSuchFieldException -> 0x0c09 }
        r0 = r140;
        r1 = r51;
        r9 = r0.get(r1);	 Catch:{ Exception -> 0x4d79, NoSuchFieldException -> 0x0c09 }
    L_0x0a82:
        if (r102 == 0) goto L_0x00cb;
    L_0x0a84:
        r25 = 0;
        r170 = r102.getClass();	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
    L_0x0a8a:
        if (r170 == 0) goto L_0x0a9a;
    L_0x0a8c:
        r225 = r170.getSimpleName();	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r226 = "FolderItem";
        r225 = r225.equals(r226);	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        if (r225 == 0) goto L_0x0bde;
    L_0x0a98:
        r25 = r170;
    L_0x0a9a:
        r225 = "getItems";
        r226 = 0;
        r0 = r25;
        r1 = r225;
        r2 = r226;
        r225 = r0.getDeclaredMethod(r1, r2);	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r226 = 0;
        r0 = r225;
        r1 = r102;
        r2 = r226;
        r87 = r0.invoke(r1, r2);	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r87 = (java.util.List) r87;	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r226.<init>();	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r227 = "Folder Item Size : ";
        r226 = r226.append(r227);	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r227 = r87.size();	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r226 = r226.append(r227);	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r226 = r226.toString();	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        android.util.Log.d(r225, r226);	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        if (r36 != 0) goto L_0x0af2;
    L_0x0ad4:
        r225 = 0;
        r0 = r87;
        r1 = r225;
        r225 = r0.get(r1);	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r170 = r225.getClass();	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
    L_0x0ae2:
        if (r170 == 0) goto L_0x0af2;
    L_0x0ae4:
        r225 = r170.getSimpleName();	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r226 = "Item";
        r225 = r225.equals(r226);	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        if (r225 == 0) goto L_0x0be4;
    L_0x0af0:
        r36 = r170;
    L_0x0af2:
        r88 = 0;
    L_0x0af4:
        r225 = r87.size();	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r0 = r88;
        r1 = r225;
        if (r0 >= r1) goto L_0x0c2e;
    L_0x0afe:
        r72 = 0;
        r225 = "getItemView";
        r226 = 0;
        r0 = r36;
        r1 = r225;
        r2 = r226;
        r64 = r0.getDeclaredMethod(r1, r2);	 Catch:{ NoSuchMethodException -> 0x0bea }
        r225 = 1;
        r0 = r64;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ NoSuchMethodException -> 0x0bea }
        r225 = r87.get(r88);	 Catch:{ NoSuchMethodException -> 0x0bea }
        r226 = 0;
        r0 = r64;
        r1 = r225;
        r2 = r226;
        r72 = r0.invoke(r1, r2);	 Catch:{ NoSuchMethodException -> 0x0bea }
    L_0x0b27:
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r226.<init>();	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r227 = "Folder Item Object";
        r226 = r226.append(r227);	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r227 = r72.getClass();	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r226 = r226.append(r227);	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r226 = r226.toString();	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        android.util.Log.d(r225, r226);	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        if (r34 != 0) goto L_0x0b59;
    L_0x0b45:
        r225 = r72.getClass();	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r225 = r225.getSimpleName();	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r226 = "HomeItemView";
        r225 = r225.equals(r226);	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        if (r225 == 0) goto L_0x0b59;
    L_0x0b55:
        r34 = r72.getClass();	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
    L_0x0b59:
        if (r33 != 0) goto L_0x0b6f;
    L_0x0b5b:
        r225 = r72.getClass();	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r225 = r225.getSimpleName();	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r226 = "HomeFolderView";
        r225 = r225.equals(r226);	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        if (r225 == 0) goto L_0x0b6f;
    L_0x0b6b:
        r33 = r72.getClass();	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
    L_0x0b6f:
        if (r37 != 0) goto L_0x0b85;
    L_0x0b71:
        r170 = r72.getClass();	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
    L_0x0b75:
        if (r170 == 0) goto L_0x0b85;
    L_0x0b77:
        r225 = r170.getSimpleName();	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r226 = "NativeViewBase";
        r225 = r225.equals(r226);	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        if (r225 == 0) goto L_0x0c28;
    L_0x0b83:
        r37 = r170;
    L_0x0b85:
        if (r37 == 0) goto L_0x0bda;
    L_0x0b87:
        if (r72 == 0) goto L_0x0bda;
    L_0x0b89:
        r225 = "mVisible";
        r0 = r37;
        r1 = r225;
        r218 = r0.getDeclaredField(r1);	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r225 = 1;
        r0 = r218;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r0 = r218;
        r1 = r72;
        r225 = r0.getInt(r1);	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        if (r225 != 0) goto L_0x0bda;
    L_0x0ba7:
        r0 = r188;
        r1 = r72;
        r0.add(r1);	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r225 = new java.lang.StringBuilder;	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r225.<init>();	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r226 = r72.getClass();	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r226 = r226.getSimpleName();	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r225 = r225.append(r226);	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r226 = r72.hashCode();	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r225 = r225.append(r226);	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r225 = r225.toString();	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r226 = r91 + 1;
        r226 = java.lang.Integer.valueOf(r226);	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r0 = r92;
        r1 = r225;
        r2 = r226;
        r0.put(r1, r2);	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
    L_0x0bda:
        r88 = r88 + 1;
        goto L_0x0af4;
    L_0x0bde:
        r170 = r170.getSuperclass();	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        goto L_0x0a8a;
    L_0x0be4:
        r170 = r170.getSuperclass();	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        goto L_0x0ae2;
    L_0x0bea:
        r57 = move-exception;
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r226.<init>();	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r227 = "Exception while getting ItemView";
        r226 = r226.append(r227);	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r0 = r226;
        r1 = r57;
        r226 = r0.append(r1);	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r226 = r226.toString();	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        android.util.Log.d(r225, r226);	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        goto L_0x0b27;
    L_0x0c09:
        r57 = move-exception;
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r226.<init>();	 Catch:{ Exception -> 0x0360 }
        r227 = "No such field in Home open folder view";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r0 = r226;
        r1 = r57;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0360 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0360 }
        goto L_0x00cb;
    L_0x0c28:
        r170 = r170.getSuperclass();	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        goto L_0x0b75;
    L_0x0c2e:
        if (r37 == 0) goto L_0x00cb;
    L_0x0c30:
        if (r9 == 0) goto L_0x00cb;
    L_0x0c32:
        r40 = r9.getClass();	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r225 = "mVisible";
        r0 = r37;
        r1 = r225;
        r218 = r0.getDeclaredField(r1);	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r225 = 1;
        r0 = r218;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r0 = r218;
        r225 = r0.getInt(r9);	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        if (r225 != 0) goto L_0x00cb;
    L_0x0c52:
        r0 = r188;
        r0.add(r9);	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r225 = new java.lang.StringBuilder;	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r225.<init>();	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r226 = r9.getClass();	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r226 = r226.getSimpleName();	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r225 = r225.append(r226);	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r226 = r9.hashCode();	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r225 = r225.append(r226);	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r225 = r225.toString();	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r226 = r91 + 1;
        r226 = java.lang.Integer.valueOf(r226);	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        r0 = r92;
        r1 = r225;
        r2 = r226;
        r0.put(r1, r2);	 Catch:{ NoSuchFieldException -> 0x0c09, Exception -> 0x0c85 }
        goto L_0x00cb;
    L_0x0c85:
        r57 = move-exception;
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r226.<init>();	 Catch:{ Exception -> 0x0360 }
        r227 = "Exception in Home open folder view";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r0 = r226;
        r1 = r57;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0360 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0360 }
        goto L_0x00cb;
    L_0x0ca4:
        if (r34 == 0) goto L_0x0cb0;
    L_0x0ca6:
        r0 = r34;
        r1 = r51;
        r225 = r0.isInstance(r1);	 Catch:{ Exception -> 0x0360 }
        if (r225 != 0) goto L_0x00cb;
    L_0x0cb0:
        if (r33 == 0) goto L_0x0cbc;
    L_0x0cb2:
        r0 = r33;
        r1 = r51;
        r225 = r0.isInstance(r1);	 Catch:{ Exception -> 0x0360 }
        if (r225 != 0) goto L_0x00cb;
    L_0x0cbc:
        if (r40 == 0) goto L_0x0cc8;
    L_0x0cbe:
        r0 = r40;
        r1 = r51;
        r225 = r0.isInstance(r1);	 Catch:{ Exception -> 0x0360 }
        if (r225 != 0) goto L_0x00cb;
    L_0x0cc8:
        r0 = r51;
        r0 = r0 instanceof android.opengl.GLSurfaceView;	 Catch:{ Exception -> 0x0360 }
        r225 = r0;
        if (r225 == 0) goto L_0x1744;
    L_0x0cd0:
        r0 = r51;
        r0 = (android.view.View) r0;	 Catch:{ Exception -> 0x10a2 }
        r225 = r0;
        r195 = r225.getResources();	 Catch:{ Exception -> 0x10a2 }
        r225 = android.opengl.GLSurfaceView.class;
        r226 = "mRenderer";
        r58 = r225.getDeclaredField(r226);	 Catch:{ Exception -> 0x10a2 }
        r225 = 1;
        r0 = r58;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x10a2 }
        r0 = r58;
        r1 = r51;
        r193 = r0.get(r1);	 Catch:{ Exception -> 0x10a2 }
        r225 = "TDK";
        r226 = r193.getClass();	 Catch:{ Exception -> 0x10a2 }
        r226 = r226.getName();	 Catch:{ Exception -> 0x10a2 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x10a2 }
        r225 = r193.getClass();	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.getName();	 Catch:{ Exception -> 0x10a2 }
        r226 = "com.sec.samsung.gallery.glview.GlRootView";
        r225 = r225.equals(r226);	 Catch:{ Exception -> 0x10a2 }
        if (r225 != 0) goto L_0x0d21;
    L_0x0d11:
        r225 = r193.getClass();	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.getName();	 Catch:{ Exception -> 0x10a2 }
        r226 = "com.sec.android.gallery3d.glcore.GlRootView";
        r225 = r225.equals(r226);	 Catch:{ Exception -> 0x10a2 }
        if (r225 == 0) goto L_0x144d;
    L_0x0d21:
        r225 = r193.getClass();	 Catch:{ Exception -> 0x0dbb }
        r226 = "mContentView";
        r58 = r225.getDeclaredField(r226);	 Catch:{ Exception -> 0x0dbb }
        r225 = 1;
        r0 = r58;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x0dbb }
        r0 = r58;
        r1 = r193;
        r113 = r0.get(r1);	 Catch:{ Exception -> 0x0dbb }
    L_0x0d3c:
        if (r113 == 0) goto L_0x0dc5;
    L_0x0d3e:
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x10a2 }
        r226.<init>();	 Catch:{ Exception -> 0x10a2 }
        r227 = "Got mContentView : ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x10a2 }
        r227 = r113.getClass();	 Catch:{ Exception -> 0x10a2 }
        r227 = r227.getName();	 Catch:{ Exception -> 0x10a2 }
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x10a2 }
        r227 = ", simple name : ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x10a2 }
        r227 = r113.getClass();	 Catch:{ Exception -> 0x10a2 }
        r227 = r227.getSimpleName();	 Catch:{ Exception -> 0x10a2 }
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x10a2 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x10a2 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x10a2 }
        r0 = r188;
        r1 = r113;
        r0.add(r1);	 Catch:{ Exception -> 0x10a2 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x10a2 }
        r225.<init>();	 Catch:{ Exception -> 0x10a2 }
        r226 = r113.getClass();	 Catch:{ Exception -> 0x10a2 }
        r226 = r226.getSimpleName();	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x10a2 }
        r226 = r113.hashCode();	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x10a2 }
        r226 = r91 + 1;
        r226 = java.lang.Integer.valueOf(r226);	 Catch:{ Exception -> 0x10a2 }
        r0 = r92;
        r1 = r225;
        r2 = r226;
        r0.put(r1, r2);	 Catch:{ Exception -> 0x10a2 }
        if (r27 != 0) goto L_0x00cb;
    L_0x0da5:
        r170 = r113.getClass();	 Catch:{ Exception -> 0x10a2 }
    L_0x0da9:
        if (r170 == 0) goto L_0x00cb;
    L_0x0dab:
        r225 = r170.getSimpleName();	 Catch:{ Exception -> 0x10a2 }
        r226 = "GLView";
        r225 = r225.equals(r226);	 Catch:{ Exception -> 0x10a2 }
        if (r225 == 0) goto L_0x0dc0;
    L_0x0db7:
        r27 = r170;
        goto L_0x00cb;
    L_0x0dbb:
        r52 = move-exception;
        r113 = 0;
        goto L_0x0d3c;
    L_0x0dc0:
        r170 = r170.getSuperclass();	 Catch:{ Exception -> 0x10a2 }
        goto L_0x0da9;
    L_0x0dc5:
        r225 = r193.getClass();	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        r226 = "mGLObjectZ";
        r58 = r225.getDeclaredField(r226);	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        r225 = 1;
        r0 = r58;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        r117 = 1;
        r0 = r58;
        r1 = r193;
        r194 = r0.get(r1);	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        if (r194 == 0) goto L_0x1079;
    L_0x0de5:
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        r226.<init>();	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        r227 = "What: ";
        r226 = r226.append(r227);	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        r227 = r194.getClass();	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        r227 = r227.getName();	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        r226 = r226.append(r227);	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        r226 = r226.toString();	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        android.util.Log.d(r225, r226);	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        r0 = r194;
        r0 = (java.util.ArrayList) r0;	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        r99 = r0;
        r75 = r99.iterator();	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
    L_0x0e0f:
        r225 = r75.hasNext();	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        if (r225 == 0) goto L_0x0ec3;
    L_0x0e15:
        r163 = r75.next();	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        r226.<init>();	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        r227 = "Array: ";
        r226 = r226.append(r227);	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        r227 = r163.getClass();	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        r227 = r227.getName();	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        r226 = r226.append(r227);	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        r226 = r226.toString();	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        android.util.Log.d(r225, r226);	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        if (r30 != 0) goto L_0x0e4f;
    L_0x0e3b:
        r170 = r163.getClass();	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
    L_0x0e3f:
        if (r170 == 0) goto L_0x0e4f;
    L_0x0e41:
        r225 = r170.getSimpleName();	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        r226 = "GlObject";
        r225 = r225.equals(r226);	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        if (r225 == 0) goto L_0x1073;
    L_0x0e4d:
        r30 = r170;
    L_0x0e4f:
        r225 = "getVisibility";
        r226 = 0;
        r0 = r30;
        r1 = r225;
        r2 = r226;
        r225 = r0.getMethod(r1, r2);	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        r226 = 0;
        r0 = r225;
        r1 = r163;
        r2 = r226;
        r225 = r0.invoke(r1, r2);	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        r225 = (java.lang.Boolean) r225;	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        r225 = r225.booleanValue();	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        if (r225 == 0) goto L_0x0e0f;
    L_0x0e71:
        r0 = r188;
        r1 = r163;
        r0.add(r1);	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        r225 = new java.lang.StringBuilder;	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        r225.<init>();	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        r226 = r163.getClass();	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        r226 = r226.getSimpleName();	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        r225 = r225.append(r226);	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        r226 = r163.hashCode();	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        r225 = r225.append(r226);	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        r225 = r225.toString();	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        r226 = r91 + 1;
        r226 = java.lang.Integer.valueOf(r226);	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        r0 = r92;
        r1 = r225;
        r2 = r226;
        r0.put(r1, r2);	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        goto L_0x0e0f;
    L_0x0ea6:
        r52 = move-exception;
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x10a2 }
        r226.<init>();	 Catch:{ Exception -> 0x10a2 }
        r227 = " No such field";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x10a2 }
        r0 = r226;
        r1 = r52;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x10a2 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x10a2 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x10a2 }
    L_0x0ec3:
        if (r117 != 0) goto L_0x00cb;
    L_0x0ec5:
        r225 = r193.getClass();	 Catch:{ Exception -> 0x10a2 }
        r226 = "mRootObject";
        r58 = r225.getField(r226);	 Catch:{ Exception -> 0x10a2 }
        r0 = r58;
        r1 = r193;
        r194 = r0.get(r1);	 Catch:{ Exception -> 0x10a2 }
        if (r194 == 0) goto L_0x00cb;
    L_0x0eda:
        r225 = r194.getClass();	 Catch:{ Exception -> 0x10a2 }
        r226 = "mChildCount";
        r58 = r225.getField(r226);	 Catch:{ Exception -> 0x10a2 }
        r0 = r58;
        r1 = r194;
        r24 = r0.getInt(r1);	 Catch:{ Exception -> 0x10a2 }
        r225 = r194.getClass();	 Catch:{ Exception -> 0x10a2 }
        r226 = "mChild";
        r58 = r225.getField(r226);	 Catch:{ Exception -> 0x10a2 }
        r0 = r58;
        r1 = r194;
        r225 = r0.get(r1);	 Catch:{ Exception -> 0x10a2 }
        r225 = (java.lang.Object[]) r225;	 Catch:{ Exception -> 0x10a2 }
        r0 = r225;
        r98 = r0;
        r74 = r24 + -1;
    L_0x0f08:
        if (r74 < 0) goto L_0x00cb;
    L_0x0f0a:
        r163 = r98[r74];	 Catch:{ Exception -> 0x10a2 }
        if (r163 == 0) goto L_0x106f;
    L_0x0f0e:
        if (r30 != 0) goto L_0x0f24;
    L_0x0f10:
        r170 = r163.getClass();	 Catch:{ Exception -> 0x10a2 }
    L_0x0f14:
        if (r170 == 0) goto L_0x0f24;
    L_0x0f16:
        r225 = r170.getSimpleName();	 Catch:{ Exception -> 0x10a2 }
        r226 = "GlObject";
        r225 = r225.equals(r226);	 Catch:{ Exception -> 0x10a2 }
        if (r225 == 0) goto L_0x10b2;
    L_0x0f22:
        r30 = r170;
    L_0x0f24:
        r225 = "mState";
        r0 = r30;
        r1 = r225;
        r58 = r0.getField(r1);	 Catch:{ Exception -> 0x10a2 }
        r0 = r58;
        r1 = r163;
        r189 = r0.getInt(r1);	 Catch:{ Exception -> 0x10a2 }
        r225 = 1;
        r0 = r189;
        r1 = r225;
        if (r0 != r1) goto L_0x106f;
    L_0x0f3f:
        r225 = "getVisibility";
        r226 = 0;
        r0 = r30;
        r1 = r225;
        r2 = r226;
        r225 = r0.getMethod(r1, r2);	 Catch:{ Exception -> 0x10a2 }
        r226 = 0;
        r0 = r225;
        r1 = r163;
        r2 = r226;
        r225 = r0.invoke(r1, r2);	 Catch:{ Exception -> 0x10a2 }
        r225 = (java.lang.Boolean) r225;	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.booleanValue();	 Catch:{ Exception -> 0x10a2 }
        if (r225 == 0) goto L_0x106f;
    L_0x0f61:
        r0 = r188;
        r1 = r163;
        r0.add(r1);	 Catch:{ Exception -> 0x10a2 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x10a2 }
        r225.<init>();	 Catch:{ Exception -> 0x10a2 }
        r226 = r163.getClass();	 Catch:{ Exception -> 0x10a2 }
        r226 = r226.getSimpleName();	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x10a2 }
        r226 = r163.hashCode();	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x10a2 }
        r226 = r91 + 1;
        r226 = java.lang.Integer.valueOf(r226);	 Catch:{ Exception -> 0x10a2 }
        r0 = r92;
        r1 = r225;
        r2 = r226;
        r0.put(r1, r2);	 Catch:{ Exception -> 0x10a2 }
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x10a2 }
        r226.<init>();	 Catch:{ Exception -> 0x10a2 }
        r227 = "GlObject class name ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x10a2 }
        r227 = r163.getClass();	 Catch:{ Exception -> 0x10a2 }
        r227 = r227.getSimpleName();	 Catch:{ Exception -> 0x10a2 }
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x10a2 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x10a2 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x10a2 }
        r225 = r163.getClass();	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.getSimpleName();	 Catch:{ Exception -> 0x10a2 }
        r226 = "GlThumbObject";
        r225 = r225.equals(r226);	 Catch:{ Exception -> 0x10a2 }
        if (r225 != 0) goto L_0x0fd4;
    L_0x0fc4:
        r225 = r163.getClass();	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.getSimpleName();	 Catch:{ Exception -> 0x10a2 }
        r226 = "GlReorderObject";
        r225 = r225.equals(r226);	 Catch:{ Exception -> 0x10a2 }
        if (r225 == 0) goto L_0x10b8;
    L_0x0fd4:
        r225 = r163.getClass();	 Catch:{ Exception -> 0x10a2 }
        r28 = r225.getSuperclass();	 Catch:{ Exception -> 0x10a2 }
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x10a2 }
        r226.<init>();	 Catch:{ Exception -> 0x10a2 }
        r227 = "GlObject super class name ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x10a2 }
        r227 = r163.getClass();	 Catch:{ Exception -> 0x10a2 }
        r227 = r227.getSuperclass();	 Catch:{ Exception -> 0x10a2 }
        r227 = r227.getSimpleName();	 Catch:{ Exception -> 0x10a2 }
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x10a2 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x10a2 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x10a2 }
        r225 = "getChildTextObject";
        r226 = 0;
        r0 = r28;
        r1 = r225;
        r2 = r226;
        r225 = r0.getMethod(r1, r2);	 Catch:{ Exception -> 0x10a2 }
        r226 = 0;
        r0 = r225;
        r1 = r163;
        r2 = r226;
        r110 = r0.invoke(r1, r2);	 Catch:{ Exception -> 0x10a2 }
        if (r110 == 0) goto L_0x106f;
    L_0x101c:
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x10a2 }
        r226.<init>();	 Catch:{ Exception -> 0x10a2 }
        r227 = "GlObject >> Text obj class name >> ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x10a2 }
        r227 = r110.getClass();	 Catch:{ Exception -> 0x10a2 }
        r227 = r227.getName();	 Catch:{ Exception -> 0x10a2 }
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x10a2 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x10a2 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x10a2 }
        r0 = r188;
        r1 = r110;
        r0.add(r1);	 Catch:{ Exception -> 0x10a2 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x10a2 }
        r225.<init>();	 Catch:{ Exception -> 0x10a2 }
        r226 = r110.getClass();	 Catch:{ Exception -> 0x10a2 }
        r226 = r226.getSimpleName();	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x10a2 }
        r226 = r110.hashCode();	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x10a2 }
        r226 = r91 + 1;
        r226 = java.lang.Integer.valueOf(r226);	 Catch:{ Exception -> 0x10a2 }
        r0 = r92;
        r1 = r225;
        r2 = r226;
        r0.put(r1, r2);	 Catch:{ Exception -> 0x10a2 }
    L_0x106f:
        r74 = r74 + -1;
        goto L_0x0f08;
    L_0x1073:
        r170 = r170.getSuperclass();	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        goto L_0x0e3f;
    L_0x1079:
        r225 = "TDK";
        r226 = "mGLObjectZ is null";
        android.util.Log.d(r225, r226);	 Catch:{ NoSuchFieldException -> 0x0ea6, SecurityException -> 0x1083 }
        goto L_0x0ec3;
    L_0x1083:
        r52 = move-exception;
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x10a2 }
        r226.<init>();	 Catch:{ Exception -> 0x10a2 }
        r227 = " No such field";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x10a2 }
        r0 = r226;
        r1 = r52;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x10a2 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x10a2 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x10a2 }
        goto L_0x0ec3;
    L_0x10a2:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "Problem: ";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x00cb;
    L_0x10b2:
        r170 = r170.getSuperclass();	 Catch:{ Exception -> 0x10a2 }
        goto L_0x0f14;
    L_0x10b8:
        r225 = r163.getClass();	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.getSimpleName();	 Catch:{ Exception -> 0x10a2 }
        r226 = "GlComposeObject";
        r225 = r225.equals(r226);	 Catch:{ Exception -> 0x10a2 }
        if (r225 == 0) goto L_0x106f;
    L_0x10c8:
        r225 = r163.getClass();	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.getSuperclass();	 Catch:{ Exception -> 0x10a2 }
        r226 = "mChild";
        r58 = r225.getField(r226);	 Catch:{ Exception -> 0x10a2 }
        r0 = r58;
        r1 = r163;
        r225 = r0.get(r1);	 Catch:{ Exception -> 0x10a2 }
        r225 = (java.lang.Object[]) r225;	 Catch:{ Exception -> 0x10a2 }
        r0 = r225;
        r95 = r0;
        r0 = r95;
        r0 = r0.length;	 Catch:{ Exception -> 0x10a2 }
        r225 = r0;
        r89 = r225 + -1;
    L_0x10ed:
        if (r89 < 0) goto L_0x106f;
    L_0x10ef:
        r23 = r95[r89];	 Catch:{ Exception -> 0x10a2 }
        if (r23 == 0) goto L_0x1449;
    L_0x10f3:
        r225 = "mState";
        r0 = r30;
        r1 = r225;
        r58 = r0.getField(r1);	 Catch:{ Exception -> 0x10a2 }
        r0 = r58;
        r1 = r23;
        r189 = r0.getInt(r1);	 Catch:{ Exception -> 0x10a2 }
        r225 = 1;
        r0 = r189;
        r1 = r225;
        if (r0 != r1) goto L_0x1449;
    L_0x110e:
        r225 = "getVisibility";
        r226 = 0;
        r0 = r30;
        r1 = r225;
        r2 = r226;
        r225 = r0.getMethod(r1, r2);	 Catch:{ Exception -> 0x10a2 }
        r226 = 0;
        r0 = r225;
        r1 = r23;
        r2 = r226;
        r225 = r0.invoke(r1, r2);	 Catch:{ Exception -> 0x10a2 }
        r225 = (java.lang.Boolean) r225;	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.booleanValue();	 Catch:{ Exception -> 0x10a2 }
        if (r225 == 0) goto L_0x1449;
    L_0x1130:
        r225 = r23.getClass();	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.getSimpleName();	 Catch:{ Exception -> 0x10a2 }
        r226 = "GroupObject";
        r225 = r225.equals(r226);	 Catch:{ Exception -> 0x10a2 }
        if (r225 == 0) goto L_0x1449;
    L_0x1140:
        r225 = "mChild";
        r0 = r30;
        r1 = r225;
        r58 = r0.getField(r1);	 Catch:{ Exception -> 0x10a2 }
        r0 = r58;
        r1 = r23;
        r225 = r0.get(r1);	 Catch:{ Exception -> 0x10a2 }
        r225 = (java.lang.Object[]) r225;	 Catch:{ Exception -> 0x10a2 }
        r0 = r225;
        r100 = r0;
        r0 = r100;
        r0 = r0.length;	 Catch:{ Exception -> 0x10a2 }
        r225 = r0;
        r88 = r225 + -1;
    L_0x1161:
        if (r88 < 0) goto L_0x1449;
    L_0x1163:
        r201 = r100[r88];	 Catch:{ Exception -> 0x10a2 }
        if (r201 == 0) goto L_0x13cc;
    L_0x1167:
        r225 = "mState";
        r0 = r30;
        r1 = r225;
        r58 = r0.getField(r1);	 Catch:{ Exception -> 0x10a2 }
        r0 = r58;
        r1 = r201;
        r189 = r0.getInt(r1);	 Catch:{ Exception -> 0x10a2 }
        r225 = 1;
        r0 = r189;
        r1 = r225;
        if (r0 != r1) goto L_0x13cc;
    L_0x1182:
        r225 = "getVisibility";
        r226 = 0;
        r0 = r30;
        r1 = r225;
        r2 = r226;
        r225 = r0.getMethod(r1, r2);	 Catch:{ Exception -> 0x10a2 }
        r226 = 0;
        r0 = r225;
        r1 = r201;
        r2 = r226;
        r225 = r0.invoke(r1, r2);	 Catch:{ Exception -> 0x10a2 }
        r225 = (java.lang.Boolean) r225;	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.booleanValue();	 Catch:{ Exception -> 0x10a2 }
        if (r225 == 0) goto L_0x11d7;
    L_0x11a4:
        r0 = r188;
        r1 = r201;
        r0.add(r1);	 Catch:{ Exception -> 0x10a2 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x10a2 }
        r225.<init>();	 Catch:{ Exception -> 0x10a2 }
        r226 = r201.getClass();	 Catch:{ Exception -> 0x10a2 }
        r226 = r226.getSimpleName();	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x10a2 }
        r226 = r201.hashCode();	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x10a2 }
        r226 = r91 + 1;
        r226 = java.lang.Integer.valueOf(r226);	 Catch:{ Exception -> 0x10a2 }
        r0 = r92;
        r1 = r225;
        r2 = r226;
        r0.put(r1, r2);	 Catch:{ Exception -> 0x10a2 }
    L_0x11d7:
        r225 = r201.getClass();	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.getSimpleName();	 Catch:{ Exception -> 0x10a2 }
        r226 = "ThumbObject";
        r225 = r225.equals(r226);	 Catch:{ Exception -> 0x10a2 }
        if (r225 == 0) goto L_0x13cc;
    L_0x11e7:
        r204 = 0;
        if (r81 == 0) goto L_0x129e;
    L_0x11eb:
        r225 = r201.getClass();	 Catch:{ Exception -> 0x13d0 }
        r59 = r225.getDeclaredFields();	 Catch:{ Exception -> 0x13d0 }
        r219 = 0;
    L_0x11f5:
        r0 = r59;
        r0 = r0.length;	 Catch:{ Exception -> 0x13d0 }
        r225 = r0;
        r0 = r219;
        r1 = r225;
        if (r0 >= r1) goto L_0x1248;
    L_0x1200:
        r225 = r59[r219];	 Catch:{ Exception -> 0x13d0 }
        r226 = 1;
        r225.setAccessible(r226);	 Catch:{ Exception -> 0x13d0 }
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x13d0 }
        r226.<init>();	 Catch:{ Exception -> 0x13d0 }
        r227 = ">> thumbObj field >> ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x13d0 }
        r227 = r59[r219];	 Catch:{ Exception -> 0x13d0 }
        r227 = r227.getName();	 Catch:{ Exception -> 0x13d0 }
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x13d0 }
        r227 = " ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x13d0 }
        r227 = r59[r219];	 Catch:{ Exception -> 0x13d0 }
        r227 = r227.getType();	 Catch:{ Exception -> 0x13d0 }
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x13d0 }
        r227 = " ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x13d0 }
        r227 = r59[r219];	 Catch:{ Exception -> 0x13d0 }
        r227 = r227.getClass();	 Catch:{ Exception -> 0x13d0 }
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x13d0 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x13d0 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x13d0 }
        r219 = r219 + 1;
        goto L_0x11f5;
    L_0x1248:
        r225 = r201.getClass();	 Catch:{ Exception -> 0x13d0 }
        r59 = r225.getFields();	 Catch:{ Exception -> 0x13d0 }
        r219 = 0;
    L_0x1252:
        r0 = r59;
        r0 = r0.length;	 Catch:{ Exception -> 0x13d0 }
        r225 = r0;
        r0 = r219;
        r1 = r225;
        if (r0 >= r1) goto L_0x129e;
    L_0x125d:
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x13d0 }
        r226.<init>();	 Catch:{ Exception -> 0x13d0 }
        r227 = ">> thumbObj field >> ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x13d0 }
        r227 = r59[r219];	 Catch:{ Exception -> 0x13d0 }
        r227 = r227.getName();	 Catch:{ Exception -> 0x13d0 }
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x13d0 }
        r227 = " ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x13d0 }
        r227 = r59[r219];	 Catch:{ Exception -> 0x13d0 }
        r227 = r227.getType();	 Catch:{ Exception -> 0x13d0 }
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x13d0 }
        r227 = " ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x13d0 }
        r227 = r59[r219];	 Catch:{ Exception -> 0x13d0 }
        r227 = r227.getClass();	 Catch:{ Exception -> 0x13d0 }
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x13d0 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x13d0 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x13d0 }
        r219 = r219 + 1;
        goto L_0x1252;
    L_0x129e:
        r225 = r201.getClass();	 Catch:{ Exception -> 0x13d0 }
        r226 = "mTitleObj";
        r58 = r225.getDeclaredField(r226);	 Catch:{ Exception -> 0x13d0 }
        r225 = 1;
        r0 = r58;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x13d0 }
        r0 = r58;
        r1 = r201;
        r204 = r0.get(r1);	 Catch:{ Exception -> 0x13d0 }
        if (r204 == 0) goto L_0x1331;
    L_0x12bc:
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x13d0 }
        r226.<init>();	 Catch:{ Exception -> 0x13d0 }
        r227 = ">> Got title object >> ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x13d0 }
        r227 = r204.getClass();	 Catch:{ Exception -> 0x13d0 }
        r227 = r227.getName();	 Catch:{ Exception -> 0x13d0 }
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x13d0 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x13d0 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x13d0 }
        r225 = "getVisibility";
        r226 = 0;
        r0 = r30;
        r1 = r225;
        r2 = r226;
        r225 = r0.getMethod(r1, r2);	 Catch:{ Exception -> 0x13d0 }
        r226 = 0;
        r0 = r225;
        r1 = r204;
        r2 = r226;
        r225 = r0.invoke(r1, r2);	 Catch:{ Exception -> 0x13d0 }
        r225 = (java.lang.Boolean) r225;	 Catch:{ Exception -> 0x13d0 }
        r225 = r225.booleanValue();	 Catch:{ Exception -> 0x13d0 }
        if (r225 == 0) goto L_0x1331;
    L_0x12fe:
        r0 = r188;
        r1 = r204;
        r0.add(r1);	 Catch:{ Exception -> 0x13d0 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x13d0 }
        r225.<init>();	 Catch:{ Exception -> 0x13d0 }
        r226 = r204.getClass();	 Catch:{ Exception -> 0x13d0 }
        r226 = r226.getSimpleName();	 Catch:{ Exception -> 0x13d0 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x13d0 }
        r226 = r204.hashCode();	 Catch:{ Exception -> 0x13d0 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x13d0 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x13d0 }
        r226 = r91 + 1;
        r226 = java.lang.Integer.valueOf(r226);	 Catch:{ Exception -> 0x13d0 }
        r0 = r92;
        r1 = r225;
        r2 = r226;
        r0.put(r1, r2);	 Catch:{ Exception -> 0x13d0 }
    L_0x1331:
        if (r204 != 0) goto L_0x13cc;
    L_0x1333:
        r225 = "TDK";
        r226 = "Checking for album title";
        android.util.Log.d(r225, r226);	 Catch:{ NoSuchFieldException -> 0x13ef, SecurityException -> 0x140d, Exception -> 0x142b }
        r225 = r201.getClass();	 Catch:{ NoSuchFieldException -> 0x13ef, SecurityException -> 0x140d, Exception -> 0x142b }
        r226 = "mAlbumTitleObj";
        r58 = r225.getDeclaredField(r226);	 Catch:{ NoSuchFieldException -> 0x13ef, SecurityException -> 0x140d, Exception -> 0x142b }
        r225 = 1;
        r0 = r58;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ NoSuchFieldException -> 0x13ef, SecurityException -> 0x140d, Exception -> 0x142b }
        r0 = r58;
        r1 = r201;
        r204 = r0.get(r1);	 Catch:{ NoSuchFieldException -> 0x13ef, SecurityException -> 0x140d, Exception -> 0x142b }
        if (r204 == 0) goto L_0x13cc;
    L_0x1357:
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ NoSuchFieldException -> 0x13ef, SecurityException -> 0x140d, Exception -> 0x142b }
        r226.<init>();	 Catch:{ NoSuchFieldException -> 0x13ef, SecurityException -> 0x140d, Exception -> 0x142b }
        r227 = ">> Got title object >> ";
        r226 = r226.append(r227);	 Catch:{ NoSuchFieldException -> 0x13ef, SecurityException -> 0x140d, Exception -> 0x142b }
        r227 = r204.getClass();	 Catch:{ NoSuchFieldException -> 0x13ef, SecurityException -> 0x140d, Exception -> 0x142b }
        r227 = r227.getName();	 Catch:{ NoSuchFieldException -> 0x13ef, SecurityException -> 0x140d, Exception -> 0x142b }
        r226 = r226.append(r227);	 Catch:{ NoSuchFieldException -> 0x13ef, SecurityException -> 0x140d, Exception -> 0x142b }
        r226 = r226.toString();	 Catch:{ NoSuchFieldException -> 0x13ef, SecurityException -> 0x140d, Exception -> 0x142b }
        android.util.Log.d(r225, r226);	 Catch:{ NoSuchFieldException -> 0x13ef, SecurityException -> 0x140d, Exception -> 0x142b }
        r225 = "getVisibility";
        r226 = 0;
        r0 = r30;
        r1 = r225;
        r2 = r226;
        r225 = r0.getMethod(r1, r2);	 Catch:{ NoSuchFieldException -> 0x13ef, SecurityException -> 0x140d, Exception -> 0x142b }
        r226 = 0;
        r0 = r225;
        r1 = r204;
        r2 = r226;
        r225 = r0.invoke(r1, r2);	 Catch:{ NoSuchFieldException -> 0x13ef, SecurityException -> 0x140d, Exception -> 0x142b }
        r225 = (java.lang.Boolean) r225;	 Catch:{ NoSuchFieldException -> 0x13ef, SecurityException -> 0x140d, Exception -> 0x142b }
        r225 = r225.booleanValue();	 Catch:{ NoSuchFieldException -> 0x13ef, SecurityException -> 0x140d, Exception -> 0x142b }
        if (r225 == 0) goto L_0x13cc;
    L_0x1399:
        r0 = r188;
        r1 = r204;
        r0.add(r1);	 Catch:{ NoSuchFieldException -> 0x13ef, SecurityException -> 0x140d, Exception -> 0x142b }
        r225 = new java.lang.StringBuilder;	 Catch:{ NoSuchFieldException -> 0x13ef, SecurityException -> 0x140d, Exception -> 0x142b }
        r225.<init>();	 Catch:{ NoSuchFieldException -> 0x13ef, SecurityException -> 0x140d, Exception -> 0x142b }
        r226 = r204.getClass();	 Catch:{ NoSuchFieldException -> 0x13ef, SecurityException -> 0x140d, Exception -> 0x142b }
        r226 = r226.getSimpleName();	 Catch:{ NoSuchFieldException -> 0x13ef, SecurityException -> 0x140d, Exception -> 0x142b }
        r225 = r225.append(r226);	 Catch:{ NoSuchFieldException -> 0x13ef, SecurityException -> 0x140d, Exception -> 0x142b }
        r226 = r204.hashCode();	 Catch:{ NoSuchFieldException -> 0x13ef, SecurityException -> 0x140d, Exception -> 0x142b }
        r225 = r225.append(r226);	 Catch:{ NoSuchFieldException -> 0x13ef, SecurityException -> 0x140d, Exception -> 0x142b }
        r225 = r225.toString();	 Catch:{ NoSuchFieldException -> 0x13ef, SecurityException -> 0x140d, Exception -> 0x142b }
        r226 = r91 + 1;
        r226 = java.lang.Integer.valueOf(r226);	 Catch:{ NoSuchFieldException -> 0x13ef, SecurityException -> 0x140d, Exception -> 0x142b }
        r0 = r92;
        r1 = r225;
        r2 = r226;
        r0.put(r1, r2);	 Catch:{ NoSuchFieldException -> 0x13ef, SecurityException -> 0x140d, Exception -> 0x142b }
    L_0x13cc:
        r88 = r88 + -1;
        goto L_0x1161;
    L_0x13d0:
        r52 = move-exception;
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x10a2 }
        r226.<init>();	 Catch:{ Exception -> 0x10a2 }
        r227 = "Exception in getting mTitleObj ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x10a2 }
        r227 = r52.getMessage();	 Catch:{ Exception -> 0x10a2 }
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x10a2 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x10a2 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x10a2 }
        goto L_0x1331;
    L_0x13ef:
        r57 = move-exception;
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x10a2 }
        r226.<init>();	 Catch:{ Exception -> 0x10a2 }
        r227 = "No mTitle field ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x10a2 }
        r0 = r226;
        r1 = r57;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x10a2 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x10a2 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x10a2 }
        goto L_0x13cc;
    L_0x140d:
        r57 = move-exception;
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x10a2 }
        r226.<init>();	 Catch:{ Exception -> 0x10a2 }
        r227 = "No mTitle field ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x10a2 }
        r0 = r226;
        r1 = r57;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x10a2 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x10a2 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x10a2 }
        goto L_0x13cc;
    L_0x142b:
        r52 = move-exception;
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x10a2 }
        r226.<init>();	 Catch:{ Exception -> 0x10a2 }
        r227 = "Exception for text title>> ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x10a2 }
        r0 = r226;
        r1 = r52;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x10a2 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x10a2 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x10a2 }
        goto L_0x13cc;
    L_0x1449:
        r89 = r89 + -1;
        goto L_0x10ed;
    L_0x144d:
        r225 = r193.getClass();	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.getName();	 Catch:{ Exception -> 0x10a2 }
        r226 = "GLContext";
        r225 = r225.contains(r226);	 Catch:{ Exception -> 0x10a2 }
        if (r225 == 0) goto L_0x169e;
    L_0x145d:
        r225 = r193.getClass();	 Catch:{ Exception -> 0x10a2 }
        r226 = "mRootView";
        r58 = r225.getDeclaredField(r226);	 Catch:{ Exception -> 0x10a2 }
        r225 = 1;
        r0 = r58;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x10a2 }
        r0 = r58;
        r1 = r193;
        r132 = r0.get(r1);	 Catch:{ Exception -> 0x10a2 }
        r47 = r132.getClass();	 Catch:{ Exception -> 0x10a2 }
        r46 = r47.getSuperclass();	 Catch:{ Exception -> 0x10a2 }
        r225 = "mClipRect";
        r0 = r46;
        r1 = r225;
        r58 = r0.getDeclaredField(r1);	 Catch:{ NoSuchFieldException -> 0x1660, SecurityException -> 0x167f }
        r225 = 1;
        r0 = r58;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ NoSuchFieldException -> 0x1660, SecurityException -> 0x167f }
        r0 = r58;
        r1 = r132;
        r111 = r0.get(r1);	 Catch:{ NoSuchFieldException -> 0x1660, SecurityException -> 0x167f }
        r111 = (android.graphics.Rect) r111;	 Catch:{ NoSuchFieldException -> 0x1660, SecurityException -> 0x167f }
        if (r111 == 0) goto L_0x14be;
    L_0x14a0:
        if (r108 != 0) goto L_0x14be;
    L_0x14a2:
        r0 = r111;
        r0 = r0.right;	 Catch:{ NoSuchFieldException -> 0x1660, SecurityException -> 0x167f }
        r225 = r0;
        r0 = r111;
        r0 = r0.left;	 Catch:{ NoSuchFieldException -> 0x1660, SecurityException -> 0x167f }
        r226 = r0;
        r108 = r225 - r226;
        r0 = r111;
        r0 = r0.bottom;	 Catch:{ NoSuchFieldException -> 0x1660, SecurityException -> 0x167f }
        r225 = r0;
        r0 = r111;
        r0 = r0.top;	 Catch:{ NoSuchFieldException -> 0x1660, SecurityException -> 0x167f }
        r226 = r0;
        r107 = r225 - r226;
    L_0x14be:
        r0 = r188;
        r1 = r132;
        r0.add(r1);	 Catch:{ Exception -> 0x10a2 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x10a2 }
        r225.<init>();	 Catch:{ Exception -> 0x10a2 }
        r226 = r132.getClass();	 Catch:{ Exception -> 0x10a2 }
        r226 = r226.getSimpleName();	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x10a2 }
        r226 = r132.hashCode();	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x10a2 }
        r226 = r91 + 1;
        r226 = java.lang.Integer.valueOf(r226);	 Catch:{ Exception -> 0x10a2 }
        r0 = r92;
        r1 = r225;
        r2 = r226;
        r0.put(r1, r2);	 Catch:{ Exception -> 0x10a2 }
        r225 = r188.size();	 Catch:{ Exception -> 0x10a2 }
        r196 = r225 + -1;
    L_0x14f7:
        r225 = r188.size();	 Catch:{ Exception -> 0x10a2 }
        r0 = r225;
        r1 = r196;
        if (r0 <= r1) goto L_0x00cb;
    L_0x1501:
        r225 = r188.size();	 Catch:{ Exception -> 0x10a2 }
        r80 = r225 + -1;
        r0 = r188;
        r1 = r80;
        r51 = r0.remove(r1);	 Catch:{ Exception -> 0x10a2 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x10a2 }
        r225.<init>();	 Catch:{ Exception -> 0x10a2 }
        r226 = r51.getClass();	 Catch:{ Exception -> 0x10a2 }
        r226 = r226.getSimpleName();	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x10a2 }
        r226 = r51.hashCode();	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x10a2 }
        r0 = r92;
        r1 = r225;
        r225 = r0.get(r1);	 Catch:{ Exception -> 0x10a2 }
        r225 = (java.lang.Integer) r225;	 Catch:{ Exception -> 0x10a2 }
        r93 = r225.intValue();	 Catch:{ Exception -> 0x10a2 }
        r0 = r213;
        r1 = r51;
        r0.add(r1);	 Catch:{ Exception -> 0x10a2 }
        if (r47 == 0) goto L_0x14f7;
    L_0x1543:
        r0 = r47;
        r1 = r51;
        r225 = r0.isInstance(r1);	 Catch:{ Exception -> 0x10a2 }
        if (r225 == 0) goto L_0x14f7;
    L_0x154d:
        r225 = "mGLViews";
        r0 = r47;
        r1 = r225;
        r58 = r0.getDeclaredField(r1);	 Catch:{ Exception -> 0x10a2 }
        r225 = 1;
        r0 = r58;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x10a2 }
        r0 = r58;
        r1 = r51;
        r96 = r0.get(r1);	 Catch:{ Exception -> 0x10a2 }
        r96 = (java.util.List) r96;	 Catch:{ Exception -> 0x10a2 }
        r225 = r96.size();	 Catch:{ Exception -> 0x10a2 }
        r74 = r225 + -1;
    L_0x1571:
        if (r74 < 0) goto L_0x14f7;
    L_0x1573:
        r0 = r96;
        r1 = r74;
        r198 = r0.get(r1);	 Catch:{ Exception -> 0x10a2 }
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x10a2 }
        r226.<init>();	 Catch:{ Exception -> 0x10a2 }
        r227 = "class Name*** ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x10a2 }
        r227 = r198.getClass();	 Catch:{ Exception -> 0x10a2 }
        r227 = r227.getName();	 Catch:{ Exception -> 0x10a2 }
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x10a2 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x10a2 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x10a2 }
        if (r41 != 0) goto L_0x15b1;
    L_0x159d:
        r225 = r198.getClass();	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.getSimpleName();	 Catch:{ Exception -> 0x10a2 }
        r226 = "GLButton";
        r225 = r225.contains(r226);	 Catch:{ Exception -> 0x10a2 }
        if (r225 == 0) goto L_0x15b1;
    L_0x15ad:
        r41 = r198.getClass();	 Catch:{ Exception -> 0x10a2 }
    L_0x15b1:
        if (r45 != 0) goto L_0x15c7;
    L_0x15b3:
        r225 = r198.getClass();	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.getSimpleName();	 Catch:{ Exception -> 0x10a2 }
        r226 = "GLText";
        r225 = r225.contains(r226);	 Catch:{ Exception -> 0x10a2 }
        if (r225 == 0) goto L_0x15c7;
    L_0x15c3:
        r45 = r198.getClass();	 Catch:{ Exception -> 0x10a2 }
    L_0x15c7:
        if (r42 != 0) goto L_0x15dd;
    L_0x15c9:
        r225 = r198.getClass();	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.getSimpleName();	 Catch:{ Exception -> 0x10a2 }
        r226 = "GLItem";
        r225 = r225.contains(r226);	 Catch:{ Exception -> 0x10a2 }
        if (r225 == 0) goto L_0x15dd;
    L_0x15d9:
        r42 = r198.getClass();	 Catch:{ Exception -> 0x10a2 }
    L_0x15dd:
        if (r43 != 0) goto L_0x15f3;
    L_0x15df:
        r225 = r198.getClass();	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.getSimpleName();	 Catch:{ Exception -> 0x10a2 }
        r226 = "GLItemDataCheckbox";
        r225 = r225.contains(r226);	 Catch:{ Exception -> 0x10a2 }
        if (r225 == 0) goto L_0x15f3;
    L_0x15ef:
        r43 = r198.getClass();	 Catch:{ Exception -> 0x10a2 }
    L_0x15f3:
        if (r44 != 0) goto L_0x1609;
    L_0x15f5:
        r225 = r198.getClass();	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.getSimpleName();	 Catch:{ Exception -> 0x10a2 }
        r226 = "GLModeSwitchButton";
        r225 = r225.contains(r226);	 Catch:{ Exception -> 0x10a2 }
        if (r225 == 0) goto L_0x1609;
    L_0x1605:
        r44 = r198.getClass();	 Catch:{ Exception -> 0x10a2 }
    L_0x1609:
        r225 = "mVisibility";
        r0 = r46;
        r1 = r225;
        r58 = r0.getDeclaredField(r1);	 Catch:{ Exception -> 0x10a2 }
        r225 = 1;
        r0 = r58;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x10a2 }
        r0 = r58;
        r1 = r198;
        r225 = r0.getInt(r1);	 Catch:{ Exception -> 0x10a2 }
        r225 = r225 & 12;
        if (r225 != 0) goto L_0x165c;
    L_0x1629:
        r0 = r188;
        r1 = r198;
        r0.add(r1);	 Catch:{ Exception -> 0x10a2 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x10a2 }
        r225.<init>();	 Catch:{ Exception -> 0x10a2 }
        r226 = r198.getClass();	 Catch:{ Exception -> 0x10a2 }
        r226 = r226.getSimpleName();	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x10a2 }
        r226 = r198.hashCode();	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x10a2 }
        r226 = r93 + 1;
        r226 = java.lang.Integer.valueOf(r226);	 Catch:{ Exception -> 0x10a2 }
        r0 = r92;
        r1 = r225;
        r2 = r226;
        r0.put(r1, r2);	 Catch:{ Exception -> 0x10a2 }
    L_0x165c:
        r74 = r74 + -1;
        goto L_0x1571;
    L_0x1660:
        r57 = move-exception;
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x10a2 }
        r226.<init>();	 Catch:{ Exception -> 0x10a2 }
        r227 = "No such field mClipRect ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x10a2 }
        r227 = r57.getMessage();	 Catch:{ Exception -> 0x10a2 }
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x10a2 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x10a2 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x10a2 }
        goto L_0x14be;
    L_0x167f:
        r57 = move-exception;
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x10a2 }
        r226.<init>();	 Catch:{ Exception -> 0x10a2 }
        r227 = "No such field mClipRect ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x10a2 }
        r227 = r57.getMessage();	 Catch:{ Exception -> 0x10a2 }
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x10a2 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x10a2 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x10a2 }
        goto L_0x14be;
    L_0x169e:
        r225 = r193.getClass();	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.getName();	 Catch:{ Exception -> 0x10a2 }
        r226 = "com.sec.android.gallery3d.ui.GLRootView";
        r225 = r225.equals(r226);	 Catch:{ Exception -> 0x10a2 }
        if (r225 == 0) goto L_0x1722;
    L_0x16ae:
        r225 = r193.getClass();	 Catch:{ Exception -> 0x10a2 }
        r226 = "mContentView";
        r58 = r225.getDeclaredField(r226);	 Catch:{ Exception -> 0x10a2 }
        r225 = 1;
        r0 = r58;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x10a2 }
        r0 = r58;
        r1 = r193;
        r113 = r0.get(r1);	 Catch:{ Exception -> 0x10a2 }
        if (r113 == 0) goto L_0x1705;
    L_0x16cb:
        r225 = "TDK";
        r226 = ">> In second test inside gallery 3D: ";
        android.util.Log.w(r225, r226);	 Catch:{ Exception -> 0x10a2 }
        r0 = r188;
        r1 = r113;
        r0.add(r1);	 Catch:{ Exception -> 0x10a2 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x10a2 }
        r225.<init>();	 Catch:{ Exception -> 0x10a2 }
        r226 = r113.getClass();	 Catch:{ Exception -> 0x10a2 }
        r226 = r226.getSimpleName();	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x10a2 }
        r226 = r113.hashCode();	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x10a2 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x10a2 }
        r226 = r91 + 1;
        r226 = java.lang.Integer.valueOf(r226);	 Catch:{ Exception -> 0x10a2 }
        r0 = r92;
        r1 = r225;
        r2 = r226;
        r0.put(r1, r2);	 Catch:{ Exception -> 0x10a2 }
    L_0x1705:
        if (r27 != 0) goto L_0x00cb;
    L_0x1707:
        r170 = r113.getClass();	 Catch:{ Exception -> 0x10a2 }
    L_0x170b:
        if (r170 == 0) goto L_0x00cb;
    L_0x170d:
        r225 = r170.getSimpleName();	 Catch:{ Exception -> 0x10a2 }
        r226 = "GLView";
        r225 = r225.equals(r226);	 Catch:{ Exception -> 0x10a2 }
        if (r225 == 0) goto L_0x171d;
    L_0x1719:
        r27 = r170;
        goto L_0x00cb;
    L_0x171d:
        r170 = r170.getSuperclass();	 Catch:{ Exception -> 0x10a2 }
        goto L_0x170b;
    L_0x1722:
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x10a2 }
        r226.<init>();	 Catch:{ Exception -> 0x10a2 }
        r227 = ">> Unknown class in GLSurfaceView: ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x10a2 }
        r227 = r193.getClass();	 Catch:{ Exception -> 0x10a2 }
        r227 = r227.getName();	 Catch:{ Exception -> 0x10a2 }
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x10a2 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x10a2 }
        android.util.Log.w(r225, r226);	 Catch:{ Exception -> 0x10a2 }
        goto L_0x00cb;
    L_0x1744:
        r0 = r51;
        r0 = r0 instanceof android.view.View;	 Catch:{ Exception -> 0x0360 }
        r225 = r0;
        if (r225 != 0) goto L_0x00cb;
    L_0x174c:
        if (r30 == 0) goto L_0x1825;
    L_0x174e:
        r0 = r30;
        r1 = r51;
        r225 = r0.isInstance(r1);	 Catch:{ Exception -> 0x0360 }
        if (r225 == 0) goto L_0x1825;
    L_0x1758:
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r226.<init>();	 Catch:{ Exception -> 0x0360 }
        r227 = "instance of GlObject : ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r0 = r226;
        r1 = r51;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0360 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0360 }
        r225 = "getView";
        r226 = 0;
        r0 = r30;
        r1 = r225;
        r2 = r226;
        r225 = r0.getMethod(r1, r2);	 Catch:{ Exception -> 0x0360 }
        r226 = 0;
        r0 = r225;
        r1 = r51;
        r2 = r226;
        r119 = r0.invoke(r1, r2);	 Catch:{ Exception -> 0x0360 }
        if (r119 == 0) goto L_0x181c;
    L_0x1790:
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r226.<init>();	 Catch:{ Exception -> 0x0360 }
        r227 = "got GlView: ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r227 = r119.getClass();	 Catch:{ Exception -> 0x0360 }
        r227 = r227.getName();	 Catch:{ Exception -> 0x0360 }
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0360 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r188;
        r1 = r119;
        r0.add(r1);	 Catch:{ Exception -> 0x0360 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = r119.getClass();	 Catch:{ Exception -> 0x0360 }
        r226 = r226.getSimpleName();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r119.hashCode();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r226 = r91 + 1;
        r226 = java.lang.Integer.valueOf(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r92;
        r1 = r225;
        r2 = r226;
        r0.put(r1, r2);	 Catch:{ Exception -> 0x0360 }
        if (r32 != 0) goto L_0x17f9;
    L_0x17e5:
        r170 = r119.getClass();	 Catch:{ Exception -> 0x0360 }
    L_0x17e9:
        if (r170 == 0) goto L_0x17f9;
    L_0x17eb:
        r225 = r170.getSimpleName();	 Catch:{ Exception -> 0x0360 }
        r226 = "GlView";
        r225 = r225.equals(r226);	 Catch:{ Exception -> 0x0360 }
        if (r225 == 0) goto L_0x1817;
    L_0x17f7:
        r32 = r170;
    L_0x17f9:
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r226.<init>();	 Catch:{ Exception -> 0x0360 }
        r227 = "got classGlView: ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r0 = r226;
        r1 = r32;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0360 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0360 }
        goto L_0x00cb;
    L_0x1817:
        r170 = r170.getSuperclass();	 Catch:{ Exception -> 0x0360 }
        goto L_0x17e9;
    L_0x181c:
        r225 = "TDK";
        r226 = "got GlView: mGlView is null";
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0360 }
        goto L_0x00cb;
    L_0x1825:
        if (r32 == 0) goto L_0x195b;
    L_0x1827:
        r0 = r32;
        r1 = r51;
        r225 = r0.isInstance(r1);	 Catch:{ Exception -> 0x0360 }
        if (r225 == 0) goto L_0x195b;
    L_0x1831:
        if (r31 != 0) goto L_0x1863;
    L_0x1833:
        r225 = r51.getClass();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.getSimpleName();	 Catch:{ Exception -> 0x0360 }
        r226 = "GlTextView";
        r225 = r225.equals(r226);	 Catch:{ Exception -> 0x0360 }
        if (r225 == 0) goto L_0x1863;
    L_0x1843:
        r31 = r51.getClass();	 Catch:{ Exception -> 0x0360 }
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r226.<init>();	 Catch:{ Exception -> 0x0360 }
        r227 = "Got ClassGlTextView : ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r0 = r226;
        r1 = r51;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0360 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0360 }
    L_0x1863:
        if (r29 != 0) goto L_0x1895;
    L_0x1865:
        r225 = r51.getClass();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.getSimpleName();	 Catch:{ Exception -> 0x0360 }
        r226 = "GlImageView";
        r225 = r225.equals(r226);	 Catch:{ Exception -> 0x0360 }
        if (r225 == 0) goto L_0x1895;
    L_0x1875:
        r29 = r51.getClass();	 Catch:{ Exception -> 0x0360 }
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r226.<init>();	 Catch:{ Exception -> 0x0360 }
        r227 = "Got classGlImageView : ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r0 = r226;
        r1 = r51;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0360 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0360 }
    L_0x1895:
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r226.<init>();	 Catch:{ Exception -> 0x0360 }
        r227 = "instance of GlView : ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r0 = r226;
        r1 = r51;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0360 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0360 }
        r225 = "getChildCount";
        r226 = 0;
        r0 = r32;
        r1 = r225;
        r2 = r226;
        r225 = r0.getMethod(r1, r2);	 Catch:{ Exception -> 0x0360 }
        r226 = 0;
        r0 = r225;
        r1 = r51;
        r2 = r226;
        r225 = r0.invoke(r1, r2);	 Catch:{ Exception -> 0x0360 }
        r225 = (java.lang.Integer) r225;	 Catch:{ Exception -> 0x0360 }
        r22 = r225.intValue();	 Catch:{ Exception -> 0x0360 }
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r226.<init>();	 Catch:{ Exception -> 0x0360 }
        r227 = "Child Count :  ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r0 = r226;
        r1 = r22;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0360 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0360 }
        r74 = r22 + -1;
    L_0x18ef:
        if (r74 < 0) goto L_0x00cb;
    L_0x18f1:
        r225 = "getChild";
        r226 = 1;
        r0 = r226;
        r0 = new java.lang.Class[r0];	 Catch:{ Exception -> 0x0360 }
        r226 = r0;
        r227 = 0;
        r228 = java.lang.Integer.TYPE;	 Catch:{ Exception -> 0x0360 }
        r226[r227] = r228;	 Catch:{ Exception -> 0x0360 }
        r0 = r32;
        r1 = r225;
        r2 = r226;
        r225 = r0.getMethod(r1, r2);	 Catch:{ Exception -> 0x0360 }
        r226 = 1;
        r0 = r226;
        r0 = new java.lang.Object[r0];	 Catch:{ Exception -> 0x0360 }
        r226 = r0;
        r227 = 0;
        r228 = java.lang.Integer.valueOf(r74);	 Catch:{ Exception -> 0x0360 }
        r226[r227] = r228;	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r51;
        r2 = r226;
        r21 = r0.invoke(r1, r2);	 Catch:{ Exception -> 0x0360 }
        r0 = r188;
        r1 = r21;
        r0.add(r1);	 Catch:{ Exception -> 0x0360 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = r21.getClass();	 Catch:{ Exception -> 0x0360 }
        r226 = r226.getSimpleName();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r21.hashCode();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r226 = r91 + 1;
        r226 = java.lang.Integer.valueOf(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r92;
        r1 = r225;
        r2 = r226;
        r0.put(r1, r2);	 Catch:{ Exception -> 0x0360 }
        r74 = r74 + -1;
        goto L_0x18ef;
    L_0x195b:
        if (r27 == 0) goto L_0x20be;
    L_0x195d:
        r0 = r27;
        r1 = r51;
        r225 = r0.isInstance(r1);	 Catch:{ Exception -> 0x0360 }
        if (r225 == 0) goto L_0x20be;
    L_0x1967:
        r225 = r51.getClass();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.getSimpleName();	 Catch:{ Exception -> 0x0360 }
        r226 = "FilmStripView";
        r225 = r225.equals(r226);	 Catch:{ Exception -> 0x0360 }
        if (r225 == 0) goto L_0x19e7;
    L_0x1977:
        r225 = r51.getClass();	 Catch:{ SecurityException -> 0x19c7, NoSuchFieldException -> 0x19d7 }
        r226 = "mAlbumView";
        r103 = r225.getDeclaredField(r226);	 Catch:{ SecurityException -> 0x19c7, NoSuchFieldException -> 0x19d7 }
        r225 = 1;
        r0 = r103;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ SecurityException -> 0x19c7, NoSuchFieldException -> 0x19d7 }
        r0 = r103;
        r1 = r51;
        r11 = r0.get(r1);	 Catch:{ SecurityException -> 0x19c7, NoSuchFieldException -> 0x19d7 }
        if (r11 == 0) goto L_0x00cb;
    L_0x1994:
        r0 = r188;
        r0.add(r11);	 Catch:{ SecurityException -> 0x19c7, NoSuchFieldException -> 0x19d7 }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x19c7, NoSuchFieldException -> 0x19d7 }
        r225.<init>();	 Catch:{ SecurityException -> 0x19c7, NoSuchFieldException -> 0x19d7 }
        r226 = r11.getClass();	 Catch:{ SecurityException -> 0x19c7, NoSuchFieldException -> 0x19d7 }
        r226 = r226.getSimpleName();	 Catch:{ SecurityException -> 0x19c7, NoSuchFieldException -> 0x19d7 }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x19c7, NoSuchFieldException -> 0x19d7 }
        r226 = r11.hashCode();	 Catch:{ SecurityException -> 0x19c7, NoSuchFieldException -> 0x19d7 }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x19c7, NoSuchFieldException -> 0x19d7 }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x19c7, NoSuchFieldException -> 0x19d7 }
        r226 = r91 + 1;
        r226 = java.lang.Integer.valueOf(r226);	 Catch:{ SecurityException -> 0x19c7, NoSuchFieldException -> 0x19d7 }
        r0 = r92;
        r1 = r225;
        r2 = r226;
        r0.put(r1, r2);	 Catch:{ SecurityException -> 0x19c7, NoSuchFieldException -> 0x19d7 }
        goto L_0x00cb;
    L_0x19c7:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "No field: mAlbumView";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x00cb;
    L_0x19d7:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "No field: mAlbumView";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x00cb;
    L_0x19e7:
        r225 = r51.getClass();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.getSimpleName();	 Catch:{ Exception -> 0x0360 }
        r226 = "AlbumView";
        r225 = r225.equals(r226);	 Catch:{ Exception -> 0x0360 }
        if (r225 != 0) goto L_0x1a07;
    L_0x19f7:
        r225 = r51.getClass();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.getSimpleName();	 Catch:{ Exception -> 0x0360 }
        r226 = "AlbumSetView";
        r225 = r225.equals(r226);	 Catch:{ Exception -> 0x0360 }
        if (r225 == 0) goto L_0x1fd3;
    L_0x1a07:
        if (r39 != 0) goto L_0x1a1d;
    L_0x1a09:
        r170 = r51.getClass();	 Catch:{ Exception -> 0x0360 }
    L_0x1a0d:
        if (r170 == 0) goto L_0x1a1d;
    L_0x1a0f:
        r225 = r170.getSimpleName();	 Catch:{ Exception -> 0x0360 }
        r226 = "SlotView";
        r225 = r225.equals(r226);	 Catch:{ Exception -> 0x0360 }
        if (r225 == 0) goto L_0x1b0b;
    L_0x1a1b:
        r39 = r170;
    L_0x1a1d:
        if (r39 == 0) goto L_0x00cb;
    L_0x1a1f:
        r203 = new java.util.HashMap;	 Catch:{ Exception -> 0x0360 }
        r203.<init>();	 Catch:{ Exception -> 0x0360 }
        r225 = "mItems";
        r0 = r39;
        r1 = r225;
        r125 = r0.getDeclaredField(r1);	 Catch:{ Exception -> 0x0360 }
        r225 = 1;
        r0 = r125;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x0360 }
        r0 = r125;
        r1 = r51;
        r86 = r0.get(r1);	 Catch:{ Exception -> 0x0360 }
        r86 = (java.util.HashMap) r86;	 Catch:{ Exception -> 0x0360 }
        r56 = r86.values();	 Catch:{ Exception -> 0x0360 }
        r82 = r56.iterator();	 Catch:{ Exception -> 0x0360 }
    L_0x1a4a:
        r225 = r82.hasNext();	 Catch:{ Exception -> 0x0360 }
        if (r225 == 0) goto L_0x1b25;
    L_0x1a50:
        r83 = r82.next();	 Catch:{ Exception -> 0x0360 }
        r191 = r83.getClass();	 Catch:{ Exception -> 0x0360 }
        r15 = r191.getDeclaredFields();	 Catch:{ Exception -> 0x0360 }
        r0 = r15.length;	 Catch:{ Exception -> 0x0360 }
        r90 = r0;
        r75 = 0;
    L_0x1a61:
        r0 = r75;
        r1 = r90;
        if (r0 >= r1) goto L_0x1a4a;
    L_0x1a67:
        r200 = r15[r75];	 Catch:{ Exception -> 0x0360 }
        r225 = 1;
        r0 = r200;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x0360 }
        r0 = r200;
        r1 = r83;
        r205 = r0.get(r1);	 Catch:{ Exception -> 0x0360 }
        r225 = r205.getClass();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.getSimpleName();	 Catch:{ Exception -> 0x0360 }
        r226 = "LabelDisplayItem";
        r225 = r225.equals(r226);	 Catch:{ Exception -> 0x0360 }
        if (r225 == 0) goto L_0x1b21;
    L_0x1a8a:
        r225 = r205.getClass();	 Catch:{ SecurityException -> 0x1afb, NoSuchFieldException -> 0x1b11 }
        r226 = "mTitle";
        r139 = r225.getDeclaredField(r226);	 Catch:{ SecurityException -> 0x1afb, NoSuchFieldException -> 0x1b11 }
        r225 = 1;
        r0 = r139;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ SecurityException -> 0x1afb, NoSuchFieldException -> 0x1b11 }
        r0 = r139;
        r1 = r205;
        r202 = r0.get(r1);	 Catch:{ SecurityException -> 0x1afb, NoSuchFieldException -> 0x1b11 }
        r202 = (java.lang.String) r202;	 Catch:{ SecurityException -> 0x1afb, NoSuchFieldException -> 0x1b11 }
        r225 = r205.getClass();	 Catch:{ SecurityException -> 0x1afb, NoSuchFieldException -> 0x1b11 }
        r226 = "mSlotIndex";
        r136 = r225.getDeclaredField(r226);	 Catch:{ SecurityException -> 0x1afb, NoSuchFieldException -> 0x1b11 }
        r225 = 1;
        r0 = r136;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ SecurityException -> 0x1afb, NoSuchFieldException -> 0x1b11 }
        r0 = r136;
        r1 = r205;
        r79 = r0.getInt(r1);	 Catch:{ SecurityException -> 0x1afb, NoSuchFieldException -> 0x1b11 }
        r225 = r205.getClass();	 Catch:{ SecurityException -> 0x1afb, NoSuchFieldException -> 0x1b11 }
        r226 = "mCount";
        r114 = r225.getDeclaredField(r226);	 Catch:{ SecurityException -> 0x1afb, NoSuchFieldException -> 0x1b11 }
        r225 = 1;
        r0 = r114;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ SecurityException -> 0x1afb, NoSuchFieldException -> 0x1b11 }
        r0 = r114;
        r1 = r205;
        r49 = r0.get(r1);	 Catch:{ SecurityException -> 0x1afb, NoSuchFieldException -> 0x1b11 }
        r49 = (java.lang.String) r49;	 Catch:{ SecurityException -> 0x1afb, NoSuchFieldException -> 0x1b11 }
        r225 = java.lang.Integer.valueOf(r79);	 Catch:{ SecurityException -> 0x1afb, NoSuchFieldException -> 0x1b11 }
        r226 = new android.util.Pair;	 Catch:{ SecurityException -> 0x1afb, NoSuchFieldException -> 0x1b11 }
        r0 = r226;
        r1 = r202;
        r2 = r49;
        r0.<init>(r1, r2);	 Catch:{ SecurityException -> 0x1afb, NoSuchFieldException -> 0x1b11 }
        r0 = r203;
        r1 = r225;
        r2 = r226;
        r0.put(r1, r2);	 Catch:{ SecurityException -> 0x1afb, NoSuchFieldException -> 0x1b11 }
        goto L_0x1a4a;
    L_0x1afb:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "No field: mTitle";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x1a4a;
    L_0x1b0b:
        r170 = r170.getSuperclass();	 Catch:{ Exception -> 0x0360 }
        goto L_0x1a0d;
    L_0x1b11:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "No field: mTitle";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x1a4a;
    L_0x1b21:
        r75 = r75 + 1;
        goto L_0x1a61;
    L_0x1b25:
        r185 = 0;
        r186 = 0;
        r225 = "mScrollX";
        r0 = r27;
        r1 = r225;
        r134 = r0.getDeclaredField(r1);	 Catch:{ SecurityException -> 0x1f93, NoSuchFieldException -> 0x1fa3 }
        r225 = 1;
        r0 = r134;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ SecurityException -> 0x1f93, NoSuchFieldException -> 0x1fa3 }
        r0 = r134;
        r1 = r51;
        r185 = r0.getInt(r1);	 Catch:{ SecurityException -> 0x1f93, NoSuchFieldException -> 0x1fa3 }
        r225 = "mScrollY";
        r0 = r27;
        r1 = r225;
        r135 = r0.getDeclaredField(r1);	 Catch:{ SecurityException -> 0x1f93, NoSuchFieldException -> 0x1fa3 }
        r225 = 1;
        r0 = r135;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ SecurityException -> 0x1f93, NoSuchFieldException -> 0x1fa3 }
        r0 = r135;
        r1 = r51;
        r186 = r0.getInt(r1);	 Catch:{ SecurityException -> 0x1f93, NoSuchFieldException -> 0x1fa3 }
    L_0x1b61:
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r226.<init>();	 Catch:{ Exception -> 0x0360 }
        r227 = "mScrollX/mScrollY: ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r0 = r226;
        r1 = r185;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r227 = ", ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r0 = r226;
        r1 = r186;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0360 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r51.getClass();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = "getVisibleStart";
        r227 = 0;
        r68 = r225.getMethod(r226, r227);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = r51.getClass();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = "getVisibleEnd";
        r227 = 0;
        r67 = r225.getMethod(r226, r227);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = r51.getClass();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = "getSlotRect";
        r227 = 1;
        r0 = r227;
        r0 = new java.lang.Class[r0];	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r227 = r0;
        r228 = 0;
        r229 = java.lang.Integer.TYPE;	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r227[r228] = r229;	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r65 = r225.getMethod(r226, r227);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = 0;
        r0 = r68;
        r1 = r51;
        r2 = r225;
        r225 = r0.invoke(r1, r2);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = (java.lang.Integer) r225;	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r153 = r225.intValue();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = 0;
        r0 = r67;
        r1 = r51;
        r2 = r225;
        r225 = r0.invoke(r1, r2);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = (java.lang.Integer) r225;	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r152 = r225.intValue();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r74 = r152 + -1;
    L_0x1be2:
        r0 = r74;
        r1 = r153;
        if (r0 < r1) goto L_0x00cb;
    L_0x1be8:
        r225 = 1;
        r0 = r225;
        r0 = new java.lang.Object[r0];	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = r0;
        r226 = 0;
        r227 = java.lang.Integer.valueOf(r74);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225[r226] = r227;	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r0 = r65;
        r1 = r51;
        r2 = r225;
        r180 = r0.invoke(r1, r2);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r180 = (android.graphics.Rect) r180;	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r0 = r180;
        r0 = r0.left;	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = r0;
        r225 = r225 - r185;
        r0 = r180;
        r0 = r0.top;	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = r0;
        r226 = r226 - r186;
        r0 = r180;
        r0 = r0.right;	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r227 = r0;
        r227 = r227 - r185;
        r0 = r180;
        r0 = r0.bottom;	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r228 = r0;
        r228 = r228 - r186;
        r0 = r180;
        r1 = r225;
        r2 = r226;
        r3 = r227;
        r4 = r228;
        r0.set(r1, r2, r3, r4);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r154 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r154.<init>();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r197 = "Slot";
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225.<init>();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = "class=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = r197.length();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r0 = r154;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = r51.hashCode();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = r225 + r74;
        r225 = r225 + 1;
        r197 = java.lang.Integer.toHexString(r225);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225.<init>();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = "hash=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = r197.length();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r0 = r154;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r197 = "true";
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225.<init>();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = "enable=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = r197.length();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r0 = r154;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r197 = "true";
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225.<init>();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = "pos_relative=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = r197.length();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r0 = r154;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r0 = r180;
        r0 = r0.left;	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r223 = r0;
        r0 = r180;
        r0 = r0.top;	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r224 = r0;
        r222 = r180.width();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r70 = r180.height();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225.<init>();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = "x=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226.<init>();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r0 = r226;
        r1 = r223;
        r226 = r0.append(r1);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r227 = "";
        r226 = r226.append(r227);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = r226.toString();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = r226.length();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r0 = r225;
        r1 = r223;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r0 = r154;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225.<init>();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = "y=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226.<init>();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r0 = r226;
        r1 = r224;
        r226 = r0.append(r1);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r227 = "";
        r226 = r226.append(r227);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = r226.toString();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = r226.length();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r0 = r225;
        r1 = r224;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r0 = r154;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225.<init>();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = "width=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226.<init>();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r0 = r226;
        r1 = r222;
        r226 = r0.append(r1);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r227 = "";
        r226 = r226.append(r227);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = r226.toString();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = r226.length();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r0 = r225;
        r1 = r222;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r0 = r154;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225.<init>();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = "height=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226.<init>();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r0 = r226;
        r1 = r70;
        r226 = r0.append(r1);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r227 = "";
        r226 = r226.append(r227);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = r226.toString();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = r226.length();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r0 = r225;
        r1 = r70;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r0 = r154;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = java.lang.Integer.valueOf(r74);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r0 = r203;
        r1 = r225;
        r225 = r0.containsKey(r1);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = 1;
        r0 = r225;
        r1 = r226;
        if (r0 != r1) goto L_0x1eed;
    L_0x1e62:
        r225 = java.lang.Integer.valueOf(r74);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r0 = r203;
        r1 = r225;
        r169 = r0.get(r1);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r169 = (android.util.Pair) r169;	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r0 = r169;
        r0 = r0.first;	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = r0;
        r0 = r225;
        r0 = (java.lang.String) r0;	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r197 = r0;
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225.<init>();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = "text=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = r197.length();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r0 = r154;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r0 = r169;
        r0 = r0.second;	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = r0;
        r0 = r225;
        r0 = (java.lang.String) r0;	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r197 = r0;
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225.<init>();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = "entry=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = r197.length();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r0 = r154;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
    L_0x1eed:
        r197 = "true";
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225.<init>();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = "clickable=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = r197.length();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r0 = r154;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r197 = "true";
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225.<init>();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = "touchable=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = r197.length();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r0 = r154;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r197 = r154.toString();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r0 = r188;
        r1 = r197;
        r0.add(r1);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225.<init>();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = r197.getClass();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = r226.getSimpleName();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = r197.hashCode();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r226 = r91 + 1;
        r226 = java.lang.Integer.valueOf(r226);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r0 = r92;
        r1 = r225;
        r2 = r226;
        r0.put(r1, r2);	 Catch:{ SecurityException -> 0x1fb3, NoSuchMethodException -> 0x1fc3 }
        r74 = r74 + -1;
        goto L_0x1be2;
    L_0x1f93:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "No field: mScrollX or mScrollY";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x1b61;
    L_0x1fa3:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "No field: mScrollX or mScrollY";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x1b61;
    L_0x1fb3:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "No field: mAlbumView";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x00cb;
    L_0x1fc3:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "No field: mAlbumView";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x00cb;
    L_0x1fd3:
        r225 = "mComponents";
        r0 = r27;
        r1 = r225;
        r112 = r0.getDeclaredField(r1);	 Catch:{ SecurityException -> 0x2038, NoSuchFieldException -> 0x20ae }
        r225 = 1;
        r0 = r112;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ SecurityException -> 0x2038, NoSuchFieldException -> 0x20ae }
        r0 = r112;
        r1 = r51;
        r94 = r0.get(r1);	 Catch:{ SecurityException -> 0x2038, NoSuchFieldException -> 0x20ae }
        r94 = (java.util.ArrayList) r94;	 Catch:{ SecurityException -> 0x2038, NoSuchFieldException -> 0x20ae }
        if (r94 == 0) goto L_0x00cb;
    L_0x1ff2:
        r225 = "getVisibility";
        r226 = 0;
        r0 = r27;
        r1 = r225;
        r2 = r226;
        r66 = r0.getMethod(r1, r2);	 Catch:{ SecurityException -> 0x2028, NoSuchMethodException -> 0x209e, NoSuchFieldException -> 0x20ae }
        r225 = 1;
        r0 = r66;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ SecurityException -> 0x2028, NoSuchMethodException -> 0x209e, NoSuchFieldException -> 0x20ae }
        r182 = new java.util.ArrayList;	 Catch:{ SecurityException -> 0x2028, NoSuchMethodException -> 0x209e, NoSuchFieldException -> 0x20ae }
        r182.<init>();	 Catch:{ SecurityException -> 0x2028, NoSuchMethodException -> 0x209e, NoSuchFieldException -> 0x20ae }
        r75 = r94.iterator();	 Catch:{ SecurityException -> 0x2028, NoSuchMethodException -> 0x209e, NoSuchFieldException -> 0x20ae }
    L_0x2012:
        r225 = r75.hasNext();	 Catch:{ SecurityException -> 0x2028, NoSuchMethodException -> 0x209e, NoSuchFieldException -> 0x20ae }
        if (r225 == 0) goto L_0x2048;
    L_0x2018:
        r156 = r75.next();	 Catch:{ SecurityException -> 0x2028, NoSuchMethodException -> 0x209e, NoSuchFieldException -> 0x20ae }
        r225 = 0;
        r0 = r182;
        r1 = r225;
        r2 = r156;
        r0.add(r1, r2);	 Catch:{ SecurityException -> 0x2028, NoSuchMethodException -> 0x209e, NoSuchFieldException -> 0x20ae }
        goto L_0x2012;
    L_0x2028:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "No method: getVisibility";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ SecurityException -> 0x2038, NoSuchFieldException -> 0x20ae }
        goto L_0x00cb;
    L_0x2038:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "No field: mComponents";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x00cb;
    L_0x2048:
        r75 = r182.iterator();	 Catch:{ SecurityException -> 0x2028, NoSuchMethodException -> 0x209e, NoSuchFieldException -> 0x20ae }
    L_0x204c:
        r225 = r75.hasNext();	 Catch:{ SecurityException -> 0x2028, NoSuchMethodException -> 0x209e, NoSuchFieldException -> 0x20ae }
        if (r225 == 0) goto L_0x00cb;
    L_0x2052:
        r156 = r75.next();	 Catch:{ SecurityException -> 0x2028, NoSuchMethodException -> 0x209e, NoSuchFieldException -> 0x20ae }
        r225 = 0;
        r0 = r66;
        r1 = r156;
        r2 = r225;
        r225 = r0.invoke(r1, r2);	 Catch:{ SecurityException -> 0x2028, NoSuchMethodException -> 0x209e, NoSuchFieldException -> 0x20ae }
        r225 = (java.lang.Integer) r225;	 Catch:{ SecurityException -> 0x2028, NoSuchMethodException -> 0x209e, NoSuchFieldException -> 0x20ae }
        r217 = r225.intValue();	 Catch:{ SecurityException -> 0x2028, NoSuchMethodException -> 0x209e, NoSuchFieldException -> 0x20ae }
        if (r217 != 0) goto L_0x204c;
    L_0x206a:
        r0 = r188;
        r1 = r156;
        r0.add(r1);	 Catch:{ SecurityException -> 0x2028, NoSuchMethodException -> 0x209e, NoSuchFieldException -> 0x20ae }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x2028, NoSuchMethodException -> 0x209e, NoSuchFieldException -> 0x20ae }
        r225.<init>();	 Catch:{ SecurityException -> 0x2028, NoSuchMethodException -> 0x209e, NoSuchFieldException -> 0x20ae }
        r226 = r156.getClass();	 Catch:{ SecurityException -> 0x2028, NoSuchMethodException -> 0x209e, NoSuchFieldException -> 0x20ae }
        r226 = r226.getSimpleName();	 Catch:{ SecurityException -> 0x2028, NoSuchMethodException -> 0x209e, NoSuchFieldException -> 0x20ae }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x2028, NoSuchMethodException -> 0x209e, NoSuchFieldException -> 0x20ae }
        r226 = r156.hashCode();	 Catch:{ SecurityException -> 0x2028, NoSuchMethodException -> 0x209e, NoSuchFieldException -> 0x20ae }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x2028, NoSuchMethodException -> 0x209e, NoSuchFieldException -> 0x20ae }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x2028, NoSuchMethodException -> 0x209e, NoSuchFieldException -> 0x20ae }
        r226 = r91 + 1;
        r226 = java.lang.Integer.valueOf(r226);	 Catch:{ SecurityException -> 0x2028, NoSuchMethodException -> 0x209e, NoSuchFieldException -> 0x20ae }
        r0 = r92;
        r1 = r225;
        r2 = r226;
        r0.put(r1, r2);	 Catch:{ SecurityException -> 0x2028, NoSuchMethodException -> 0x209e, NoSuchFieldException -> 0x20ae }
        goto L_0x204c;
    L_0x209e:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "No method: getVisibility";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ SecurityException -> 0x2038, NoSuchFieldException -> 0x20ae }
        goto L_0x00cb;
    L_0x20ae:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "No field: mComponents";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x00cb;
    L_0x20be:
        r0 = r51;
        r0 = r0 instanceof java.lang.String;	 Catch:{ Exception -> 0x0360 }
        r225 = r0;
        if (r225 != 0) goto L_0x00cb;
    L_0x20c6:
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r226.<init>();	 Catch:{ Exception -> 0x0360 }
        r227 = "!!!!!!!!!! In while Unknown classes: ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r227 = r51.getClass();	 Catch:{ Exception -> 0x0360 }
        r227 = r227.getName();	 Catch:{ Exception -> 0x0360 }
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0360 }
        android.util.Log.w(r225, r226);	 Catch:{ Exception -> 0x0360 }
        goto L_0x00cb;
    L_0x20e8:
        r225 = "TDK";
        r226 = ">> Start traversing the viewList!";
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0360 }
        r207 = 0;
        r174 = -1;
        r175 = -1;
        r75 = r213.iterator();	 Catch:{ Exception -> 0x0360 }
    L_0x20f9:
        r225 = r75.hasNext();	 Catch:{ Exception -> 0x0360 }
        if (r225 == 0) goto L_0x4d35;
    L_0x20ff:
        r163 = r75.next();	 Catch:{ Exception -> 0x0360 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = r163.getClass();	 Catch:{ Exception -> 0x0360 }
        r226 = r226.getSimpleName();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r163.hashCode();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r92;
        r1 = r225;
        r225 = r0.get(r1);	 Catch:{ Exception -> 0x0360 }
        r225 = (java.lang.Integer) r225;	 Catch:{ Exception -> 0x0360 }
        r91 = r225.intValue();	 Catch:{ Exception -> 0x0360 }
        r225 = 1;
        r0 = r207;
        r1 = r225;
        if (r0 != r1) goto L_0x2140;
    L_0x2136:
        r207 = 0;
        r0 = r91;
        r1 = r174;
        if (r0 <= r1) goto L_0x2140;
    L_0x213e:
        r91 = r174;
    L_0x2140:
        r0 = r91;
        r1 = r175;
        if (r0 <= r1) goto L_0x2152;
    L_0x2146:
        r225 = r91 - r175;
        r226 = 1;
        r0 = r225;
        r1 = r226;
        if (r0 <= r1) goto L_0x2152;
    L_0x2150:
        r91 = r175 + 1;
    L_0x2152:
        r74 = 0;
    L_0x2154:
        r0 = r74;
        r1 = r91;
        if (r0 >= r1) goto L_0x2166;
    L_0x215a:
        r225 = 32;
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r74 = r74 + 1;
        goto L_0x2154;
    L_0x2166:
        r175 = r91;
        if (r91 != 0) goto L_0x222f;
    L_0x216a:
        r210 = r230.getContext();	 Catch:{ Exception -> 0x0360 }
        r197 = r210.getPackageName();	 Catch:{ Exception -> 0x0360 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "package=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r197.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r167 = r197;
        r225 = r210.getClass();	 Catch:{ Exception -> 0x0360 }
        r197 = r225.getSimpleName();	 Catch:{ Exception -> 0x0360 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "activity=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r197.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r184;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = "";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r197 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "screenon=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r197.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r133 = r230.getWidth();	 Catch:{ Exception -> 0x0360 }
    L_0x222f:
        r0 = r163;
        r0 = r0 instanceof android.view.View;	 Catch:{ Exception -> 0x0360 }
        r225 = r0;
        if (r225 == 0) goto L_0x2e52;
    L_0x2237:
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r226.<init>();	 Catch:{ Exception -> 0x0360 }
        r227 = "ViewList: (View) ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r227 = r163.getClass();	 Catch:{ Exception -> 0x0360 }
        r227 = r227.getName();	 Catch:{ Exception -> 0x0360 }
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0360 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r163.getClass();	 Catch:{ Exception -> 0x0360 }
        r197 = r225.getSimpleName();	 Catch:{ Exception -> 0x0360 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "class=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r197.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r225 = r163.hashCode();	 Catch:{ Exception -> 0x0360 }
        r197 = java.lang.Integer.toHexString(r225);	 Catch:{ Exception -> 0x0360 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "hash=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r197.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r0 = r163;
        r0 = (android.view.View) r0;	 Catch:{ Exception -> 0x0360 }
        r209 = r0;
        r77 = r209.getId();	 Catch:{ Exception -> 0x0360 }
        if (r77 < 0) goto L_0x2364;
    L_0x22d7:
        r195 = r209.getResources();	 Catch:{ Exception -> 0x0360 }
        r0 = r195;
        r1 = r77;
        r197 = r0.getResourceEntryName(r1);	 Catch:{ NotFoundException -> 0x4d76 }
        r225 = 10;
        r0 = r197;
        r1 = r225;
        r225 = r0.indexOf(r1);	 Catch:{ NotFoundException -> 0x4d76 }
        r226 = -1;
        r0 = r225;
        r1 = r226;
        if (r0 == r1) goto L_0x2332;
    L_0x22f5:
        r225 = 10;
        r0 = r197;
        r1 = r225;
        r225 = r0.indexOf(r1);	 Catch:{ NotFoundException -> 0x4d76 }
        r226 = -1;
        r0 = r225;
        r1 = r226;
        if (r0 == r1) goto L_0x2316;
    L_0x2307:
        r225 = 10;
        r226 = 3;
        r0 = r197;
        r1 = r225;
        r2 = r226;
        r197 = r0.replace(r1, r2);	 Catch:{ NotFoundException -> 0x4d76 }
        goto L_0x22f5;
    L_0x2316:
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ NotFoundException -> 0x4d76 }
        r226.<init>();	 Catch:{ NotFoundException -> 0x4d76 }
        r227 = ">> newlineReplacement - ";
        r226 = r226.append(r227);	 Catch:{ NotFoundException -> 0x4d76 }
        r0 = r226;
        r1 = r197;
        r226 = r0.append(r1);	 Catch:{ NotFoundException -> 0x4d76 }
        r226 = r226.toString();	 Catch:{ NotFoundException -> 0x4d76 }
        android.util.Log.d(r225, r226);	 Catch:{ NotFoundException -> 0x4d76 }
    L_0x2332:
        r225 = new java.lang.StringBuilder;	 Catch:{ NotFoundException -> 0x4d76 }
        r225.<init>();	 Catch:{ NotFoundException -> 0x4d76 }
        r226 = "id=";
        r225 = r225.append(r226);	 Catch:{ NotFoundException -> 0x4d76 }
        r226 = r197.length();	 Catch:{ NotFoundException -> 0x4d76 }
        r225 = r225.append(r226);	 Catch:{ NotFoundException -> 0x4d76 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ NotFoundException -> 0x4d76 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ NotFoundException -> 0x4d76 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ NotFoundException -> 0x4d76 }
        r225 = r225.toString();	 Catch:{ NotFoundException -> 0x4d76 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ NotFoundException -> 0x4d76 }
    L_0x2364:
        r225 = 2;
        r0 = r225;
        r0 = new int[r0];	 Catch:{ Exception -> 0x0360 }
        r101 = r0;
        r0 = r209;
        r1 = r101;
        r0.getLocationOnScreen(r1);	 Catch:{ Exception -> 0x0360 }
        r225 = 0;
        r225 = r101[r225];	 Catch:{ Exception -> 0x0360 }
        r197 = java.lang.Integer.toString(r225);	 Catch:{ Exception -> 0x0360 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "x=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r197.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r225 = 1;
        r225 = r101[r225];	 Catch:{ Exception -> 0x0360 }
        r197 = java.lang.Integer.toString(r225);	 Catch:{ Exception -> 0x0360 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "y=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r197.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        if (r108 == 0) goto L_0x2ae7;
    L_0x23eb:
        r0 = r108;
        r1 = r133;
        if (r0 <= r1) goto L_0x2ae7;
    L_0x23f1:
        r225 = r163.getClass();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.getSimpleName();	 Catch:{ Exception -> 0x0360 }
        r226 = "GLSurfaceView";
        r225 = r225.contains(r226);	 Catch:{ Exception -> 0x0360 }
        if (r225 == 0) goto L_0x2ae7;
    L_0x2401:
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r107;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = "";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r197 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "width=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r197.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r107;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = "";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r108;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = "";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r197 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "height=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r197.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r108;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = "";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
    L_0x24a0:
        r225 = r209.getContentDescription();	 Catch:{ Exception -> 0x0360 }
        if (r225 == 0) goto L_0x251c;
    L_0x24a6:
        r225 = r209.getContentDescription();	 Catch:{ Exception -> 0x0360 }
        r197 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r225 = 10;
        r0 = r197;
        r1 = r225;
        r225 = r0.indexOf(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = -1;
        r0 = r225;
        r1 = r226;
        if (r0 == r1) goto L_0x24ea;
    L_0x24c0:
        r225 = "(\n|\r\n)";
        r226 = "\u0003";
        r0 = r197;
        r1 = r225;
        r2 = r226;
        r197 = r0.replaceAll(r1, r2);	 Catch:{ Exception -> 0x0360 }
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r226.<init>();	 Catch:{ Exception -> 0x0360 }
        r227 = ">> getContentDescription - ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r0 = r226;
        r1 = r197;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0360 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0360 }
    L_0x24ea:
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "contentdesc=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r197.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
    L_0x251c:
        r225 = -1;
        r0 = r209;
        r1 = r225;
        r225 = r0.canScrollVertically(r1);	 Catch:{ Exception -> 0x2b5e }
        if (r225 != 0) goto L_0x2534;
    L_0x2528:
        r225 = 1;
        r0 = r209;
        r1 = r225;
        r225 = r0.canScrollVertically(r1);	 Catch:{ Exception -> 0x2b5e }
        if (r225 == 0) goto L_0x256a;
    L_0x2534:
        r197 = "true";
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x2b5e }
        r225.<init>();	 Catch:{ Exception -> 0x2b5e }
        r226 = "vertical_draggable=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x2b5e }
        r226 = r197.length();	 Catch:{ Exception -> 0x2b5e }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x2b5e }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x2b5e }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x2b5e }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x2b5e }
        r225 = r225.toString();	 Catch:{ Exception -> 0x2b5e }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x2b5e }
    L_0x256a:
        r225 = -1;
        r0 = r209;
        r1 = r225;
        r225 = r0.canScrollHorizontally(r1);	 Catch:{ Exception -> 0x2b5e }
        if (r225 != 0) goto L_0x2582;
    L_0x2576:
        r225 = 1;
        r0 = r209;
        r1 = r225;
        r225 = r0.canScrollHorizontally(r1);	 Catch:{ Exception -> 0x2b5e }
        if (r225 == 0) goto L_0x25b7;
    L_0x2582:
        r197 = "true";
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x2b5e }
        r225.<init>();	 Catch:{ Exception -> 0x2b5e }
        r226 = "horizontal_draggable=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x2b5e }
        r226 = r197.length();	 Catch:{ Exception -> 0x2b5e }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x2b5e }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x2b5e }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x2b5e }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x2b5e }
        r225 = r225.toString();	 Catch:{ Exception -> 0x2b5e }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x2b5e }
    L_0x25b7:
        r225 = r209.isEnabled();	 Catch:{ Exception -> 0x0360 }
        r226 = 1;
        r0 = r225;
        r1 = r226;
        if (r0 != r1) goto L_0x2b7d;
    L_0x25c3:
        r197 = "true";
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "enable=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r197.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
    L_0x25f8:
        r225 = r209.isLongClickable();	 Catch:{ Exception -> 0x0360 }
        r226 = 1;
        r0 = r225;
        r1 = r226;
        if (r0 != r1) goto L_0x2639;
    L_0x2604:
        r197 = "true";
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "longclickable=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r197.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
    L_0x2639:
        r225 = r209.isClickable();	 Catch:{ Exception -> 0x0360 }
        r226 = 1;
        r0 = r225;
        r1 = r226;
        if (r0 != r1) goto L_0x267a;
    L_0x2645:
        r197 = "true";
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "clickable=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r197.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
    L_0x267a:
        r225 = r209.isEnabled();	 Catch:{ Exception -> 0x0360 }
        r226 = 1;
        r0 = r225;
        r1 = r226;
        if (r0 != r1) goto L_0x26d4;
    L_0x2686:
        r225 = r209.isLongClickable();	 Catch:{ Exception -> 0x0360 }
        r226 = 1;
        r0 = r225;
        r1 = r226;
        if (r0 == r1) goto L_0x269e;
    L_0x2692:
        r225 = r209.isClickable();	 Catch:{ Exception -> 0x0360 }
        r226 = 1;
        r0 = r225;
        r1 = r226;
        if (r0 != r1) goto L_0x26d4;
    L_0x269e:
        r197 = "true";
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "touchable=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r197.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
    L_0x26d4:
        r225 = r209.isFocusable();	 Catch:{ Exception -> 0x0360 }
        r226 = 1;
        r0 = r225;
        r1 = r226;
        if (r0 != r1) goto L_0x2715;
    L_0x26e0:
        r197 = "true";
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "hasfocus=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r197.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
    L_0x2715:
        r225 = r209.isSelected();	 Catch:{ Exception -> 0x0360 }
        r226 = 1;
        r0 = r225;
        r1 = r226;
        if (r0 != r1) goto L_0x2756;
    L_0x2721:
        r197 = "true";
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "isselected=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r197.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
    L_0x2756:
        r0 = r209;
        r0 = r0 instanceof android.widget.AdapterView;	 Catch:{ Exception -> 0x0360 }
        r225 = r0;
        if (r225 == 0) goto L_0x2793;
    L_0x275e:
        r197 = "true";
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "adapter=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r197.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
    L_0x2793:
        r0 = r209;
        r0 = r0 instanceof android.widget.TextView;	 Catch:{ Exception -> 0x0360 }
        r225 = r0;
        if (r225 == 0) goto L_0x28cf;
    L_0x279b:
        r0 = r209;
        r0 = (android.widget.TextView) r0;	 Catch:{ Exception -> 0x0360 }
        r198 = r0;
        r225 = r198.getText();	 Catch:{ Exception -> 0x0360 }
        r197 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r225 = 10;
        r0 = r197;
        r1 = r225;
        r225 = r0.indexOf(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = -1;
        r0 = r225;
        r1 = r226;
        if (r0 == r1) goto L_0x27e5;
    L_0x27bb:
        r225 = "(\n|\r\n)";
        r226 = "\u0003";
        r0 = r197;
        r1 = r225;
        r2 = r226;
        r197 = r0.replaceAll(r1, r2);	 Catch:{ Exception -> 0x0360 }
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r226.<init>();	 Catch:{ Exception -> 0x0360 }
        r227 = ">> newlineReplacement - ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r0 = r226;
        r1 = r197;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0360 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0360 }
    L_0x27e5:
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r226.<init>();	 Catch:{ Exception -> 0x0360 }
        r227 = ">> textview - ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r0 = r226;
        r1 = r197;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0360 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0360 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "text=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r197.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r225 = r198.getEditableText();	 Catch:{ Exception -> 0x0360 }
        if (r225 == 0) goto L_0x286f;
    L_0x283a:
        r197 = "true";
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "editable=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r197.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
    L_0x286f:
        r225 = r198.getBackground();	 Catch:{ Exception -> 0x0360 }
        if (r225 == 0) goto L_0x28cf;
    L_0x2875:
        r225 = android.widget.TextView.class;
        r226 = "mResource";
        r58 = r225.getDeclaredField(r226);	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        r225 = 1;
        r0 = r58;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        r0 = r58;
        r1 = r198;
        r181 = r0.getInt(r1);	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        if (r181 == 0) goto L_0x2bd3;
    L_0x2891:
        r225 = r198.getResources();	 Catch:{ NotFoundException -> 0x2bb3 }
        r0 = r225;
        r1 = r181;
        r197 = r0.getResourceEntryName(r1);	 Catch:{ NotFoundException -> 0x2bb3 }
        r225 = new java.lang.StringBuilder;	 Catch:{ NotFoundException -> 0x2bb3 }
        r225.<init>();	 Catch:{ NotFoundException -> 0x2bb3 }
        r226 = "background=";
        r225 = r225.append(r226);	 Catch:{ NotFoundException -> 0x2bb3 }
        r226 = r197.length();	 Catch:{ NotFoundException -> 0x2bb3 }
        r225 = r225.append(r226);	 Catch:{ NotFoundException -> 0x2bb3 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ NotFoundException -> 0x2bb3 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ NotFoundException -> 0x2bb3 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ NotFoundException -> 0x2bb3 }
        r225 = r225.toString();	 Catch:{ NotFoundException -> 0x2bb3 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ NotFoundException -> 0x2bb3 }
    L_0x28cf:
        r0 = r209;
        r0 = r0 instanceof android.widget.Checkable;	 Catch:{ Exception -> 0x0360 }
        r225 = r0;
        if (r225 == 0) goto L_0x291e;
    L_0x28d7:
        r0 = r209;
        r0 = (android.widget.Checkable) r0;	 Catch:{ Exception -> 0x0360 }
        r20 = r0;
        r225 = r20.isChecked();	 Catch:{ Exception -> 0x0360 }
        r226 = 1;
        r0 = r225;
        r1 = r226;
        if (r0 != r1) goto L_0x2cc5;
    L_0x28e9:
        r197 = "true";
    L_0x28ec:
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "checked=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r197.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
    L_0x291e:
        r0 = r209;
        r0 = r0 instanceof android.widget.ProgressBar;	 Catch:{ Exception -> 0x0360 }
        r225 = r0;
        if (r225 == 0) goto L_0x2a18;
    L_0x2926:
        r197 = "true";
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "progressbar=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r197.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r0 = r209;
        r0 = (android.widget.ProgressBar) r0;	 Catch:{ Exception -> 0x0360 }
        r177 = r0;
        r225 = r177.isIndeterminate();	 Catch:{ Exception -> 0x0360 }
        if (r225 != 0) goto L_0x2d09;
    L_0x2968:
        r147 = r177.getMax();	 Catch:{ Exception -> 0x0360 }
        if (r147 == 0) goto L_0x2a18;
    L_0x296e:
        r225 = android.widget.ProgressBar.class;
        r226 = "mProgress";
        r58 = r225.getDeclaredField(r226);	 Catch:{ SecurityException -> 0x2cc9, NoSuchFieldException -> 0x2cd9 }
        r225 = 1;
        r0 = r58;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ SecurityException -> 0x2cc9, NoSuchFieldException -> 0x2cd9 }
        r0 = r58;
        r1 = r177;
        r176 = r0.getInt(r1);	 Catch:{ SecurityException -> 0x2cc9, NoSuchFieldException -> 0x2cd9 }
        r225 = r176 * 100;
        r225 = r225 / r147;
        r197 = java.lang.Integer.toString(r225);	 Catch:{ SecurityException -> 0x2cc9, NoSuchFieldException -> 0x2cd9 }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x2cc9, NoSuchFieldException -> 0x2cd9 }
        r225.<init>();	 Catch:{ SecurityException -> 0x2cc9, NoSuchFieldException -> 0x2cd9 }
        r226 = "progress=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x2cc9, NoSuchFieldException -> 0x2cd9 }
        r226 = r197.length();	 Catch:{ SecurityException -> 0x2cc9, NoSuchFieldException -> 0x2cd9 }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x2cc9, NoSuchFieldException -> 0x2cd9 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x2cc9, NoSuchFieldException -> 0x2cd9 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x2cc9, NoSuchFieldException -> 0x2cd9 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x2cc9, NoSuchFieldException -> 0x2cd9 }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x2cc9, NoSuchFieldException -> 0x2cd9 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x2cc9, NoSuchFieldException -> 0x2cd9 }
    L_0x29c3:
        r225 = android.widget.ProgressBar.class;
        r226 = "mSecondaryProgress";
        r58 = r225.getDeclaredField(r226);	 Catch:{ SecurityException -> 0x2ce9, NoSuchFieldException -> 0x2cf9 }
        r225 = 1;
        r0 = r58;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ SecurityException -> 0x2ce9, NoSuchFieldException -> 0x2cf9 }
        r0 = r58;
        r1 = r177;
        r176 = r0.getInt(r1);	 Catch:{ SecurityException -> 0x2ce9, NoSuchFieldException -> 0x2cf9 }
        r225 = r176 * 100;
        r225 = r225 / r147;
        r197 = java.lang.Integer.toString(r225);	 Catch:{ SecurityException -> 0x2ce9, NoSuchFieldException -> 0x2cf9 }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x2ce9, NoSuchFieldException -> 0x2cf9 }
        r225.<init>();	 Catch:{ SecurityException -> 0x2ce9, NoSuchFieldException -> 0x2cf9 }
        r226 = "progress2=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x2ce9, NoSuchFieldException -> 0x2cf9 }
        r226 = r197.length();	 Catch:{ SecurityException -> 0x2ce9, NoSuchFieldException -> 0x2cf9 }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x2ce9, NoSuchFieldException -> 0x2cf9 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x2ce9, NoSuchFieldException -> 0x2cf9 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x2ce9, NoSuchFieldException -> 0x2cf9 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x2ce9, NoSuchFieldException -> 0x2cf9 }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x2ce9, NoSuchFieldException -> 0x2cf9 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x2ce9, NoSuchFieldException -> 0x2cf9 }
    L_0x2a18:
        r0 = r209;
        r0 = r0 instanceof android.widget.ImageView;	 Catch:{ Exception -> 0x0360 }
        r225 = r0;
        if (r225 == 0) goto L_0x2a80;
    L_0x2a20:
        r0 = r209;
        r0 = (android.widget.ImageView) r0;	 Catch:{ Exception -> 0x0360 }
        r78 = r0;
        r225 = android.widget.ImageView.class;
        r226 = "mResource";
        r58 = r225.getDeclaredField(r226);	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        r225 = 1;
        r0 = r58;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        r0 = r58;
        r1 = r78;
        r181 = r0.getInt(r1);	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        if (r181 == 0) goto L_0x2d60;
    L_0x2a42:
        r225 = r78.getResources();	 Catch:{ NotFoundException -> 0x2d40 }
        r0 = r225;
        r1 = r181;
        r197 = r0.getResourceEntryName(r1);	 Catch:{ NotFoundException -> 0x2d40 }
        r225 = new java.lang.StringBuilder;	 Catch:{ NotFoundException -> 0x2d40 }
        r225.<init>();	 Catch:{ NotFoundException -> 0x2d40 }
        r226 = "entry=";
        r225 = r225.append(r226);	 Catch:{ NotFoundException -> 0x2d40 }
        r226 = r197.length();	 Catch:{ NotFoundException -> 0x2d40 }
        r225 = r225.append(r226);	 Catch:{ NotFoundException -> 0x2d40 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ NotFoundException -> 0x2d40 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ NotFoundException -> 0x2d40 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ NotFoundException -> 0x2d40 }
        r225 = r225.toString();	 Catch:{ NotFoundException -> 0x2d40 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ NotFoundException -> 0x2d40 }
    L_0x2a80:
        r0 = r209;
        r0 = r0 instanceof android.widget.EditText;	 Catch:{ Exception -> 0x0360 }
        r225 = r0;
        if (r225 == 0) goto L_0x2adc;
    L_0x2a88:
        r0 = r209;
        r0 = (android.widget.EditText) r0;	 Catch:{ Exception -> 0x0360 }
        r53 = r0;
        r225 = r53.getHint();	 Catch:{ Exception -> 0x0360 }
        if (r225 == 0) goto L_0x2ad5;
    L_0x2a94:
        r225 = "TDK";
        r226 = "editText: editText1";
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r53.getHint();	 Catch:{ Exception -> 0x0360 }
        r197 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "hint=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r197.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
    L_0x2ad5:
        r225 = "TDK";
        r226 = "editText: editText2";
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0360 }
    L_0x2adc:
        r225 = 10;
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        goto L_0x20f9;
    L_0x2ae7:
        r225 = r209.getWidth();	 Catch:{ Exception -> 0x0360 }
        r197 = java.lang.Integer.toString(r225);	 Catch:{ Exception -> 0x0360 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "width=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r197.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r225 = r209.getHeight();	 Catch:{ Exception -> 0x0360 }
        r197 = java.lang.Integer.toString(r225);	 Catch:{ Exception -> 0x0360 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "height=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r197.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        goto L_0x24a0;
    L_0x2b5e:
        r57 = move-exception;
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r226.<init>();	 Catch:{ Exception -> 0x0360 }
        r227 = "Failed to get scrolling information: ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r0 = r226;
        r1 = r57;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0360 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0360 }
        goto L_0x25b7;
    L_0x2b7d:
        r197 = "false";
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "enable=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r197.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        goto L_0x25f8;
    L_0x2bb3:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "Failed to get Resoruce Entry Name";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        goto L_0x28cf;
    L_0x2bc3:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "No mResource for entry of ImageView";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x28cf;
    L_0x2bd3:
        r225 = android.widget.ImageView.class;
        r226 = "mUri";
        r58 = r225.getDeclaredField(r226);	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        r225 = 1;
        r0 = r58;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        r0 = r58;
        r1 = r198;
        r208 = r0.get(r1);	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        r208 = (android.net.Uri) r208;	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        if (r208 == 0) goto L_0x28cf;
    L_0x2bf1:
        r183 = r208.getScheme();	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        r225 = "android.resource";
        r0 = r225;
        r1 = r183;
        r225 = r0.equals(r1);	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        if (r225 == 0) goto L_0x2c49;
    L_0x2c01:
        r197 = r208.toString();	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        r225.<init>();	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        r226 = "background=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        r226 = r197.length();	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        goto L_0x28cf;
    L_0x2c39:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "No mResource for entry of ImageView";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x28cf;
    L_0x2c49:
        r225 = "content";
        r0 = r225;
        r1 = r183;
        r225 = r0.equals(r1);	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        if (r225 == 0) goto L_0x2c8d;
    L_0x2c55:
        r197 = r208.toString();	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        r225.<init>();	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        r226 = "background=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        r226 = r197.length();	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        goto L_0x28cf;
    L_0x2c8d:
        r197 = r208.toString();	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        r225.<init>();	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        r226 = "background=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        r226 = r197.length();	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x2bc3, NoSuchFieldException -> 0x2c39 }
        goto L_0x28cf;
    L_0x2cc5:
        r197 = "false";
        goto L_0x28ec;
    L_0x2cc9:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "Reflection Failed. ProgressBar:mProgress";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x29c3;
    L_0x2cd9:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "Reflection Failed. ProgressBar:mProgress";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x29c3;
    L_0x2ce9:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "Reflection Failed. ProgressBar:mSecondaryProgress";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x2a18;
    L_0x2cf9:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "Reflection Failed. ProgressBar:mSecondaryProgress";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x2a18;
    L_0x2d09:
        r197 = "true";
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "indeterminate=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r197.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        goto L_0x2a18;
    L_0x2d40:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "Failed to get Resoruce Entry Name";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        goto L_0x2a80;
    L_0x2d50:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "No mResource for entry of ImageView";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x2a80;
    L_0x2d60:
        r225 = android.widget.ImageView.class;
        r226 = "mUri";
        r58 = r225.getDeclaredField(r226);	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        r225 = 1;
        r0 = r58;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        r0 = r58;
        r1 = r78;
        r208 = r0.get(r1);	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        r208 = (android.net.Uri) r208;	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        if (r208 == 0) goto L_0x2a80;
    L_0x2d7e:
        r183 = r208.getScheme();	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        r225 = "android.resource";
        r0 = r225;
        r1 = r183;
        r225 = r0.equals(r1);	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        if (r225 == 0) goto L_0x2dd6;
    L_0x2d8e:
        r197 = r208.toString();	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        r225.<init>();	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        r226 = "entry=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        r226 = r197.length();	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        goto L_0x2a80;
    L_0x2dc6:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "No mResource for entry of ImageView";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x2a80;
    L_0x2dd6:
        r225 = "content";
        r0 = r225;
        r1 = r183;
        r225 = r0.equals(r1);	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        if (r225 == 0) goto L_0x2e1a;
    L_0x2de2:
        r197 = r208.toString();	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        r225.<init>();	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        r226 = "entry=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        r226 = r197.length();	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        goto L_0x2a80;
    L_0x2e1a:
        r197 = r208.toString();	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        r225.<init>();	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        r226 = "entry=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        r226 = r197.length();	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x2d50, NoSuchFieldException -> 0x2dc6 }
        goto L_0x2a80;
    L_0x2e52:
        if (r37 == 0) goto L_0x35f6;
    L_0x2e54:
        r0 = r37;
        r1 = r163;
        r225 = r0.isInstance(r1);	 Catch:{ Exception -> 0x0360 }
        if (r225 == 0) goto L_0x35f6;
    L_0x2e5e:
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r226.<init>();	 Catch:{ Exception -> 0x0360 }
        r227 = "ViewList: (NativeViewBase) ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r227 = r163.getClass();	 Catch:{ Exception -> 0x0360 }
        r227 = r227.getName();	 Catch:{ Exception -> 0x0360 }
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0360 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r163.getClass();	 Catch:{ Exception -> 0x0360 }
        r197 = r225.getSimpleName();	 Catch:{ Exception -> 0x0360 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "class=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r197.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r225 = r163.hashCode();	 Catch:{ Exception -> 0x0360 }
        r197 = java.lang.Integer.toHexString(r225);	 Catch:{ Exception -> 0x0360 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "hash=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r197.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r225 = "getScreenBoundingRect";
        r226 = 0;
        r0 = r37;
        r1 = r225;
        r2 = r226;
        r225 = r0.getDeclaredMethod(r1, r2);	 Catch:{ Exception -> 0x31dd }
        r226 = 0;
        r0 = r225;
        r1 = r163;
        r2 = r226;
        r180 = r0.invoke(r1, r2);	 Catch:{ Exception -> 0x31dd }
        r180 = (android.graphics.Rect) r180;	 Catch:{ Exception -> 0x31dd }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x31dd }
        r225.<init>();	 Catch:{ Exception -> 0x31dd }
        r0 = r180;
        r0 = r0.left;	 Catch:{ Exception -> 0x31dd }
        r226 = r0;
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x31dd }
        r226 = "";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x31dd }
        r197 = r225.toString();	 Catch:{ Exception -> 0x31dd }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x31dd }
        r225.<init>();	 Catch:{ Exception -> 0x31dd }
        r226 = "x=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x31dd }
        r226 = r197.length();	 Catch:{ Exception -> 0x31dd }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x31dd }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x31dd }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x31dd }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x31dd }
        r225 = r225.toString();	 Catch:{ Exception -> 0x31dd }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x31dd }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x31dd }
        r225.<init>();	 Catch:{ Exception -> 0x31dd }
        r0 = r180;
        r0 = r0.top;	 Catch:{ Exception -> 0x31dd }
        r226 = r0;
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x31dd }
        r226 = "";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x31dd }
        r197 = r225.toString();	 Catch:{ Exception -> 0x31dd }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x31dd }
        r225.<init>();	 Catch:{ Exception -> 0x31dd }
        r226 = "y=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x31dd }
        r226 = r197.length();	 Catch:{ Exception -> 0x31dd }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x31dd }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x31dd }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x31dd }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x31dd }
        r225 = r225.toString();	 Catch:{ Exception -> 0x31dd }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x31dd }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x31dd }
        r225.<init>();	 Catch:{ Exception -> 0x31dd }
        r226 = r180.width();	 Catch:{ Exception -> 0x31dd }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x31dd }
        r226 = "";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x31dd }
        r197 = r225.toString();	 Catch:{ Exception -> 0x31dd }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x31dd }
        r225.<init>();	 Catch:{ Exception -> 0x31dd }
        r226 = "width=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x31dd }
        r226 = r197.length();	 Catch:{ Exception -> 0x31dd }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x31dd }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x31dd }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x31dd }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x31dd }
        r225 = r225.toString();	 Catch:{ Exception -> 0x31dd }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x31dd }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x31dd }
        r225.<init>();	 Catch:{ Exception -> 0x31dd }
        r226 = r180.height();	 Catch:{ Exception -> 0x31dd }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x31dd }
        r226 = "";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x31dd }
        r197 = r225.toString();	 Catch:{ Exception -> 0x31dd }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x31dd }
        r225.<init>();	 Catch:{ Exception -> 0x31dd }
        r226 = "height=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x31dd }
        r226 = r197.length();	 Catch:{ Exception -> 0x31dd }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x31dd }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x31dd }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x31dd }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x31dd }
        r225 = r225.toString();	 Catch:{ Exception -> 0x31dd }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x31dd }
    L_0x3039:
        r48 = 0;
        r225 = "isClickable";
        r226 = 0;
        r0 = r37;
        r1 = r225;
        r2 = r226;
        r225 = r0.getDeclaredMethod(r1, r2);	 Catch:{ NoSuchMethodException -> 0x31fc, Exception -> 0x321b }
        r226 = 0;
        r0 = r225;
        r1 = r163;
        r2 = r226;
        r225 = r0.invoke(r1, r2);	 Catch:{ NoSuchMethodException -> 0x31fc, Exception -> 0x321b }
        r225 = (java.lang.Boolean) r225;	 Catch:{ NoSuchMethodException -> 0x31fc, Exception -> 0x321b }
        r48 = r225.booleanValue();	 Catch:{ NoSuchMethodException -> 0x31fc, Exception -> 0x321b }
        r197 = java.lang.String.valueOf(r48);	 Catch:{ NoSuchMethodException -> 0x31fc, Exception -> 0x321b }
        r225 = new java.lang.StringBuilder;	 Catch:{ NoSuchMethodException -> 0x31fc, Exception -> 0x321b }
        r225.<init>();	 Catch:{ NoSuchMethodException -> 0x31fc, Exception -> 0x321b }
        r226 = "clickable=";
        r225 = r225.append(r226);	 Catch:{ NoSuchMethodException -> 0x31fc, Exception -> 0x321b }
        r226 = r197.length();	 Catch:{ NoSuchMethodException -> 0x31fc, Exception -> 0x321b }
        r225 = r225.append(r226);	 Catch:{ NoSuchMethodException -> 0x31fc, Exception -> 0x321b }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ NoSuchMethodException -> 0x31fc, Exception -> 0x321b }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ NoSuchMethodException -> 0x31fc, Exception -> 0x321b }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ NoSuchMethodException -> 0x31fc, Exception -> 0x321b }
        r225 = r225.toString();	 Catch:{ NoSuchMethodException -> 0x31fc, Exception -> 0x321b }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ NoSuchMethodException -> 0x31fc, Exception -> 0x321b }
    L_0x3091:
        r55 = 0;
        r225 = "isEnabled";
        r226 = 0;
        r0 = r37;
        r1 = r225;
        r2 = r226;
        r225 = r0.getDeclaredMethod(r1, r2);	 Catch:{ NoSuchMethodException -> 0x323a, Exception -> 0x3259 }
        r226 = 0;
        r0 = r225;
        r1 = r163;
        r2 = r226;
        r225 = r0.invoke(r1, r2);	 Catch:{ NoSuchMethodException -> 0x323a, Exception -> 0x3259 }
        r225 = (java.lang.Boolean) r225;	 Catch:{ NoSuchMethodException -> 0x323a, Exception -> 0x3259 }
        r55 = r225.booleanValue();	 Catch:{ NoSuchMethodException -> 0x323a, Exception -> 0x3259 }
        r197 = java.lang.String.valueOf(r55);	 Catch:{ NoSuchMethodException -> 0x323a, Exception -> 0x3259 }
        r225 = new java.lang.StringBuilder;	 Catch:{ NoSuchMethodException -> 0x323a, Exception -> 0x3259 }
        r225.<init>();	 Catch:{ NoSuchMethodException -> 0x323a, Exception -> 0x3259 }
        r226 = "enable=";
        r225 = r225.append(r226);	 Catch:{ NoSuchMethodException -> 0x323a, Exception -> 0x3259 }
        r226 = r197.length();	 Catch:{ NoSuchMethodException -> 0x323a, Exception -> 0x3259 }
        r225 = r225.append(r226);	 Catch:{ NoSuchMethodException -> 0x323a, Exception -> 0x3259 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ NoSuchMethodException -> 0x323a, Exception -> 0x3259 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ NoSuchMethodException -> 0x323a, Exception -> 0x3259 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ NoSuchMethodException -> 0x323a, Exception -> 0x3259 }
        r225 = r225.toString();	 Catch:{ NoSuchMethodException -> 0x323a, Exception -> 0x3259 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ NoSuchMethodException -> 0x323a, Exception -> 0x3259 }
    L_0x30e9:
        r149 = 0;
        r225 = "mLongClickable";
        r0 = r37;
        r1 = r225;
        r127 = r0.getDeclaredField(r1);	 Catch:{ NoSuchFieldException -> 0x3278, Exception -> 0x3297 }
        r225 = 1;
        r0 = r127;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ NoSuchFieldException -> 0x3278, Exception -> 0x3297 }
        r0 = r127;
        r1 = r163;
        r149 = r0.getBoolean(r1);	 Catch:{ NoSuchFieldException -> 0x3278, Exception -> 0x3297 }
        r197 = java.lang.String.valueOf(r149);	 Catch:{ NoSuchFieldException -> 0x3278, Exception -> 0x3297 }
        r225 = new java.lang.StringBuilder;	 Catch:{ NoSuchFieldException -> 0x3278, Exception -> 0x3297 }
        r225.<init>();	 Catch:{ NoSuchFieldException -> 0x3278, Exception -> 0x3297 }
        r226 = "longclickable=";
        r225 = r225.append(r226);	 Catch:{ NoSuchFieldException -> 0x3278, Exception -> 0x3297 }
        r226 = r197.length();	 Catch:{ NoSuchFieldException -> 0x3278, Exception -> 0x3297 }
        r225 = r225.append(r226);	 Catch:{ NoSuchFieldException -> 0x3278, Exception -> 0x3297 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ NoSuchFieldException -> 0x3278, Exception -> 0x3297 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ NoSuchFieldException -> 0x3278, Exception -> 0x3297 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ NoSuchFieldException -> 0x3278, Exception -> 0x3297 }
        r225 = r225.toString();	 Catch:{ NoSuchFieldException -> 0x3278, Exception -> 0x3297 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ NoSuchFieldException -> 0x3278, Exception -> 0x3297 }
    L_0x313d:
        if (r55 == 0) goto L_0x3179;
    L_0x313f:
        if (r48 != 0) goto L_0x3143;
    L_0x3141:
        if (r149 == 0) goto L_0x3179;
    L_0x3143:
        r197 = "true";
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "touchable=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r197.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
    L_0x3179:
        r225 = r163.getClass();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.getSimpleName();	 Catch:{ Exception -> 0x0360 }
        r226 = "HomeItemView";
        r225 = r225.equalsIgnoreCase(r226);	 Catch:{ Exception -> 0x0360 }
        if (r225 == 0) goto L_0x3379;
    L_0x3189:
        r225 = r163.getClass();	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r226 = "mTitle";
        r151 = r225.getDeclaredField(r226);	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r225 = 1;
        r0 = r151;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r0 = r151;
        r1 = r163;
        r199 = r0.get(r1);	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        if (r81 == 0) goto L_0x32ec;
    L_0x31a7:
        r225 = r199.getClass();	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r148 = r225.getMethods();	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r74 = 0;
    L_0x31b1:
        r0 = r148;
        r0 = r0.length;	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r225 = r0;
        r0 = r74;
        r1 = r225;
        if (r0 >= r1) goto L_0x32b6;
    L_0x31bc:
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r226.<init>();	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r227 = ">> txt methods - ";
        r226 = r226.append(r227);	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r227 = r148[r74];	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r227 = r227.getName();	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r226 = r226.append(r227);	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r226 = r226.toString();	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        android.util.Log.d(r225, r226);	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r74 = r74 + 1;
        goto L_0x31b1;
    L_0x31dd:
        r52 = move-exception;
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r226.<init>();	 Catch:{ Exception -> 0x0360 }
        r227 = " Exception while getting screen bound : ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r0 = r226;
        r1 = r52;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0360 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0360 }
        goto L_0x3039;
    L_0x31fc:
        r57 = move-exception;
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r226.<init>();	 Catch:{ Exception -> 0x0360 }
        r227 = "No such method Exception while getting clickable for homeitem : ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r0 = r226;
        r1 = r57;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0360 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0360 }
        goto L_0x3091;
    L_0x321b:
        r57 = move-exception;
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r226.<init>();	 Catch:{ Exception -> 0x0360 }
        r227 = "Exception while getting clickable for homeitem : ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r0 = r226;
        r1 = r57;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0360 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0360 }
        goto L_0x3091;
    L_0x323a:
        r57 = move-exception;
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r226.<init>();	 Catch:{ Exception -> 0x0360 }
        r227 = "No such method Exception while getting isEnabled for homeitem : ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r0 = r226;
        r1 = r57;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0360 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0360 }
        goto L_0x30e9;
    L_0x3259:
        r57 = move-exception;
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r226.<init>();	 Catch:{ Exception -> 0x0360 }
        r227 = "Exception while getting isEnabled for homeitem : ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r0 = r226;
        r1 = r57;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0360 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0360 }
        goto L_0x30e9;
    L_0x3278:
        r57 = move-exception;
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r226.<init>();	 Catch:{ Exception -> 0x0360 }
        r227 = "No such field Exception while getting long clickable for homeitem : ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r0 = r226;
        r1 = r57;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0360 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0360 }
        goto L_0x313d;
    L_0x3297:
        r57 = move-exception;
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r226.<init>();	 Catch:{ Exception -> 0x0360 }
        r227 = "Exception while getting clickable for homeitem : ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r0 = r226;
        r1 = r57;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0360 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0360 }
        goto L_0x313d;
    L_0x32b6:
        r225 = r199.getClass();	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r148 = r225.getDeclaredMethods();	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r74 = 0;
    L_0x32c0:
        r0 = r148;
        r0 = r0.length;	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r225 = r0;
        r0 = r74;
        r1 = r225;
        if (r0 >= r1) goto L_0x32ec;
    L_0x32cb:
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r226.<init>();	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r227 = ">> private txt methods - ";
        r226 = r226.append(r227);	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r227 = r148[r74];	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r227 = r227.getName();	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r226 = r226.append(r227);	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r226 = r226.toString();	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        android.util.Log.d(r225, r226);	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r74 = r74 + 1;
        goto L_0x32c0;
    L_0x32ec:
        r225 = r199.getClass();	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r226 = "getText";
        r227 = 0;
        r225 = r225.getMethod(r226, r227);	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r226 = 0;
        r0 = r225;
        r1 = r199;
        r2 = r226;
        r225 = r0.invoke(r1, r2);	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r0 = r225;
        r0 = (java.lang.String) r0;	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r197 = r0;
        r225 = 10;
        r0 = r197;
        r1 = r225;
        r225 = r0.indexOf(r1);	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r226 = -1;
        r0 = r225;
        r1 = r226;
        if (r0 == r1) goto L_0x3346;
    L_0x331c:
        r225 = "(\n|\r\n)";
        r226 = "\u0003";
        r0 = r197;
        r1 = r225;
        r2 = r226;
        r197 = r0.replaceAll(r1, r2);	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r226.<init>();	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r227 = ">> newlineReplacement - ";
        r226 = r226.append(r227);	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r0 = r226;
        r1 = r197;
        r226 = r0.append(r1);	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r226 = r226.toString();	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        android.util.Log.d(r225, r226);	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
    L_0x3346:
        r225 = new java.lang.StringBuilder;	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r225.<init>();	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r226 = "text=";
        r225 = r225.append(r226);	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r226 = r197.length();	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r225 = r225.append(r226);	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r225 = r225.toString();	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ NoSuchFieldException -> 0x3496, Exception -> 0x34b5 }
    L_0x3379:
        r225 = r163.getClass();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.getSimpleName();	 Catch:{ Exception -> 0x0360 }
        r226 = "TextView";
        r225 = r225.equalsIgnoreCase(r226);	 Catch:{ Exception -> 0x0360 }
        if (r225 == 0) goto L_0x3432;
    L_0x3389:
        r225 = r163.getClass();	 Catch:{ Exception -> 0x34d4 }
        r226 = "getText";
        r227 = 0;
        r225 = r225.getDeclaredMethod(r226, r227);	 Catch:{ Exception -> 0x34d4 }
        r226 = 0;
        r0 = r225;
        r1 = r163;
        r2 = r226;
        r225 = r0.invoke(r1, r2);	 Catch:{ Exception -> 0x34d4 }
        r0 = r225;
        r0 = (java.lang.String) r0;	 Catch:{ Exception -> 0x34d4 }
        r197 = r0;
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x34d4 }
        r226.<init>();	 Catch:{ Exception -> 0x34d4 }
        r227 = "Get FolderViewtitle: ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x34d4 }
        r0 = r226;
        r1 = r197;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x34d4 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x34d4 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x34d4 }
        r225 = 10;
        r0 = r197;
        r1 = r225;
        r225 = r0.indexOf(r1);	 Catch:{ Exception -> 0x34d4 }
        r226 = -1;
        r0 = r225;
        r1 = r226;
        if (r0 == r1) goto L_0x33ff;
    L_0x33d5:
        r225 = "(\n|\r\n)";
        r226 = "\u0003";
        r0 = r197;
        r1 = r225;
        r2 = r226;
        r197 = r0.replaceAll(r1, r2);	 Catch:{ Exception -> 0x34d4 }
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x34d4 }
        r226.<init>();	 Catch:{ Exception -> 0x34d4 }
        r227 = ">> newlineReplacement - ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x34d4 }
        r0 = r226;
        r1 = r197;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x34d4 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x34d4 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x34d4 }
    L_0x33ff:
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x34d4 }
        r225.<init>();	 Catch:{ Exception -> 0x34d4 }
        r226 = "text=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x34d4 }
        r226 = r197.length();	 Catch:{ Exception -> 0x34d4 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x34d4 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x34d4 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x34d4 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x34d4 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x34d4 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x34d4 }
    L_0x3432:
        r225 = r163.getClass();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.getSimpleName();	 Catch:{ Exception -> 0x0360 }
        r226 = "HomeFolderView";
        r225 = r225.equalsIgnoreCase(r226);	 Catch:{ Exception -> 0x0360 }
        if (r225 == 0) goto L_0x2adc;
    L_0x3442:
        r225 = r163.getClass();	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r226 = "mTitleView";
        r151 = r225.getDeclaredField(r226);	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r225 = 1;
        r0 = r151;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r0 = r151;
        r1 = r163;
        r199 = r0.get(r1);	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        if (r81 == 0) goto L_0x3529;
    L_0x3460:
        r225 = r199.getClass();	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r148 = r225.getMethods();	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r74 = 0;
    L_0x346a:
        r0 = r148;
        r0 = r0.length;	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r225 = r0;
        r0 = r74;
        r1 = r225;
        if (r0 >= r1) goto L_0x34f3;
    L_0x3475:
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r226.<init>();	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r227 = ">> txt methods - ";
        r226 = r226.append(r227);	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r227 = r148[r74];	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r227 = r227.getName();	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r226 = r226.append(r227);	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r226 = r226.toString();	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        android.util.Log.d(r225, r226);	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r74 = r74 + 1;
        goto L_0x346a;
    L_0x3496:
        r57 = move-exception;
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r226.<init>();	 Catch:{ Exception -> 0x0360 }
        r227 = "No such method Exception while getting title for homeitem : ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r0 = r226;
        r1 = r57;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0360 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0360 }
        goto L_0x3379;
    L_0x34b5:
        r57 = move-exception;
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r226.<init>();	 Catch:{ Exception -> 0x0360 }
        r227 = "Exception while getting title for homeitem : ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r0 = r226;
        r1 = r57;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0360 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0360 }
        goto L_0x3379;
    L_0x34d4:
        r52 = move-exception;
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r226.<init>();	 Catch:{ Exception -> 0x0360 }
        r227 = "Exception while getting title for openFolderView : ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r0 = r226;
        r1 = r52;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0360 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0360 }
        goto L_0x3432;
    L_0x34f3:
        r225 = r199.getClass();	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r148 = r225.getDeclaredMethods();	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r74 = 0;
    L_0x34fd:
        r0 = r148;
        r0 = r0.length;	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r225 = r0;
        r0 = r74;
        r1 = r225;
        if (r0 >= r1) goto L_0x3529;
    L_0x3508:
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r226.<init>();	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r227 = ">> private txt methods - ";
        r226 = r226.append(r227);	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r227 = r148[r74];	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r227 = r227.getName();	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r226 = r226.append(r227);	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r226 = r226.toString();	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        android.util.Log.d(r225, r226);	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r74 = r74 + 1;
        goto L_0x34fd;
    L_0x3529:
        r225 = r199.getClass();	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r226 = "getText";
        r227 = 0;
        r225 = r225.getMethod(r226, r227);	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r226 = 0;
        r0 = r225;
        r1 = r199;
        r2 = r226;
        r225 = r0.invoke(r1, r2);	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r0 = r225;
        r0 = (java.lang.String) r0;	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r197 = r0;
        r225 = 10;
        r0 = r197;
        r1 = r225;
        r225 = r0.indexOf(r1);	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r226 = -1;
        r0 = r225;
        r1 = r226;
        if (r0 == r1) goto L_0x3583;
    L_0x3559:
        r225 = "(\n|\r\n)";
        r226 = "\u0003";
        r0 = r197;
        r1 = r225;
        r2 = r226;
        r197 = r0.replaceAll(r1, r2);	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r226.<init>();	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r227 = ">> newlineReplacement - ";
        r226 = r226.append(r227);	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r0 = r226;
        r1 = r197;
        r226 = r0.append(r1);	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r226 = r226.toString();	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        android.util.Log.d(r225, r226);	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
    L_0x3583:
        r225 = new java.lang.StringBuilder;	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r225.<init>();	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r226 = "text=";
        r225 = r225.append(r226);	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r226 = r197.length();	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r225 = r225.append(r226);	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r225 = r225.toString();	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ NoSuchFieldException -> 0x35b8, Exception -> 0x35d7 }
        goto L_0x2adc;
    L_0x35b8:
        r57 = move-exception;
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r226.<init>();	 Catch:{ Exception -> 0x0360 }
        r227 = "No such method Exception while getting title for homefolderitem : ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r0 = r226;
        r1 = r57;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0360 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0360 }
        goto L_0x2adc;
    L_0x35d7:
        r57 = move-exception;
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r226.<init>();	 Catch:{ Exception -> 0x0360 }
        r227 = "Exception while getting title for homefolderitem : ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r0 = r226;
        r1 = r57;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0360 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0360 }
        goto L_0x2adc;
    L_0x35f6:
        if (r46 == 0) goto L_0x40b3;
    L_0x35f8:
        r0 = r46;
        r1 = r163;
        r225 = r0.isInstance(r1);	 Catch:{ Exception -> 0x0360 }
        if (r225 == 0) goto L_0x40b3;
    L_0x3602:
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r226.<init>();	 Catch:{ Exception -> 0x0360 }
        r227 = "ViewList: (TwGLView) ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r227 = r163.getClass();	 Catch:{ Exception -> 0x0360 }
        r227 = r227.getName();	 Catch:{ Exception -> 0x0360 }
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0360 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0360 }
        r61 = r163;
        r225 = r163.getClass();	 Catch:{ Exception -> 0x0360 }
        r197 = r225.getSimpleName();	 Catch:{ Exception -> 0x0360 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "class=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r197.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r225 = r163.hashCode();	 Catch:{ Exception -> 0x0360 }
        r197 = java.lang.Integer.toHexString(r225);	 Catch:{ Exception -> 0x0360 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "hash=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r197.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r54 = 0;
        r225 = "mOnClickListener";
        r0 = r46;
        r1 = r225;
        r58 = r0.getDeclaredField(r1);	 Catch:{ NoSuchFieldException -> 0x3be2, SecurityException -> 0x3bf6 }
    L_0x36a5:
        r225 = 1;
        r0 = r58;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ SecurityException -> 0x3bf6, NoSuchFieldException -> 0x3c06 }
        r0 = r58;
        r1 = r61;
        r225 = r0.get(r1);	 Catch:{ SecurityException -> 0x3bf6, NoSuchFieldException -> 0x3c06 }
        if (r225 == 0) goto L_0x3759;
    L_0x36b8:
        r54 = 1;
        r197 = "true";
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x3bf6, NoSuchFieldException -> 0x3c06 }
        r225.<init>();	 Catch:{ SecurityException -> 0x3bf6, NoSuchFieldException -> 0x3c06 }
        r226 = "clickable=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3bf6, NoSuchFieldException -> 0x3c06 }
        r226 = r197.length();	 Catch:{ SecurityException -> 0x3bf6, NoSuchFieldException -> 0x3c06 }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3bf6, NoSuchFieldException -> 0x3c06 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3bf6, NoSuchFieldException -> 0x3c06 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x3bf6, NoSuchFieldException -> 0x3c06 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3bf6, NoSuchFieldException -> 0x3c06 }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x3bf6, NoSuchFieldException -> 0x3c06 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x3bf6, NoSuchFieldException -> 0x3c06 }
        r197 = "false";
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x3bf6, NoSuchFieldException -> 0x3c06 }
        r225.<init>();	 Catch:{ SecurityException -> 0x3bf6, NoSuchFieldException -> 0x3c06 }
        r226 = "longclickable=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3bf6, NoSuchFieldException -> 0x3c06 }
        r226 = r197.length();	 Catch:{ SecurityException -> 0x3bf6, NoSuchFieldException -> 0x3c06 }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3bf6, NoSuchFieldException -> 0x3c06 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3bf6, NoSuchFieldException -> 0x3c06 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x3bf6, NoSuchFieldException -> 0x3c06 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3bf6, NoSuchFieldException -> 0x3c06 }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x3bf6, NoSuchFieldException -> 0x3c06 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x3bf6, NoSuchFieldException -> 0x3c06 }
        r197 = "true";
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x3bf6, NoSuchFieldException -> 0x3c06 }
        r225.<init>();	 Catch:{ SecurityException -> 0x3bf6, NoSuchFieldException -> 0x3c06 }
        r226 = "touchable=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3bf6, NoSuchFieldException -> 0x3c06 }
        r226 = r197.length();	 Catch:{ SecurityException -> 0x3bf6, NoSuchFieldException -> 0x3c06 }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3bf6, NoSuchFieldException -> 0x3c06 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3bf6, NoSuchFieldException -> 0x3c06 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x3bf6, NoSuchFieldException -> 0x3c06 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3bf6, NoSuchFieldException -> 0x3c06 }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x3bf6, NoSuchFieldException -> 0x3c06 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x3bf6, NoSuchFieldException -> 0x3c06 }
    L_0x3759:
        r225 = 1;
        r0 = r54;
        r1 = r225;
        if (r0 != r1) goto L_0x3c16;
    L_0x3761:
        r197 = "true";
    L_0x3764:
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "enable=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r197.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r225 = "mTitle";
        r0 = r46;
        r1 = r225;
        r58 = r0.getDeclaredField(r1);	 Catch:{ SecurityException -> 0x3c1a, NoSuchFieldException -> 0x3c2a }
        r225 = 1;
        r0 = r58;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ SecurityException -> 0x3c1a, NoSuchFieldException -> 0x3c2a }
        r0 = r58;
        r1 = r61;
        r225 = r0.get(r1);	 Catch:{ SecurityException -> 0x3c1a, NoSuchFieldException -> 0x3c2a }
        r0 = r225;
        r0 = (java.lang.String) r0;	 Catch:{ SecurityException -> 0x3c1a, NoSuchFieldException -> 0x3c2a }
        r197 = r0;
        if (r197 == 0) goto L_0x3828;
    L_0x37ba:
        r225 = 10;
        r0 = r197;
        r1 = r225;
        r225 = r0.indexOf(r1);	 Catch:{ SecurityException -> 0x3c1a, NoSuchFieldException -> 0x3c2a }
        r226 = -1;
        r0 = r225;
        r1 = r226;
        if (r0 == r1) goto L_0x37f6;
    L_0x37cc:
        r225 = "(\n|\r\n)";
        r226 = "\u0003";
        r0 = r197;
        r1 = r225;
        r2 = r226;
        r197 = r0.replaceAll(r1, r2);	 Catch:{ SecurityException -> 0x3c1a, NoSuchFieldException -> 0x3c2a }
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x3c1a, NoSuchFieldException -> 0x3c2a }
        r226.<init>();	 Catch:{ SecurityException -> 0x3c1a, NoSuchFieldException -> 0x3c2a }
        r227 = ">> newlineReplacement - ";
        r226 = r226.append(r227);	 Catch:{ SecurityException -> 0x3c1a, NoSuchFieldException -> 0x3c2a }
        r0 = r226;
        r1 = r197;
        r226 = r0.append(r1);	 Catch:{ SecurityException -> 0x3c1a, NoSuchFieldException -> 0x3c2a }
        r226 = r226.toString();	 Catch:{ SecurityException -> 0x3c1a, NoSuchFieldException -> 0x3c2a }
        android.util.Log.d(r225, r226);	 Catch:{ SecurityException -> 0x3c1a, NoSuchFieldException -> 0x3c2a }
    L_0x37f6:
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x3c1a, NoSuchFieldException -> 0x3c2a }
        r225.<init>();	 Catch:{ SecurityException -> 0x3c1a, NoSuchFieldException -> 0x3c2a }
        r226 = "id=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3c1a, NoSuchFieldException -> 0x3c2a }
        r226 = r197.length();	 Catch:{ SecurityException -> 0x3c1a, NoSuchFieldException -> 0x3c2a }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3c1a, NoSuchFieldException -> 0x3c2a }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3c1a, NoSuchFieldException -> 0x3c2a }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x3c1a, NoSuchFieldException -> 0x3c2a }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3c1a, NoSuchFieldException -> 0x3c2a }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x3c1a, NoSuchFieldException -> 0x3c2a }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x3c1a, NoSuchFieldException -> 0x3c2a }
    L_0x3828:
        r225 = "mClipRect";
        r0 = r46;
        r1 = r225;
        r58 = r0.getDeclaredField(r1);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = 1;
        r0 = r58;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r0 = r58;
        r1 = r61;
        r111 = r0.get(r1);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r111 = (android.graphics.Rect) r111;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        if (r111 == 0) goto L_0x3d8b;
    L_0x3847:
        if (r108 == 0) goto L_0x3c3a;
    L_0x3849:
        r0 = r108;
        r1 = r133;
        if (r0 <= r1) goto L_0x3c3a;
    L_0x384f:
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225.<init>();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r0 = r111;
        r0 = r0.top;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = r0;
        r0 = r111;
        r0 = r0.bottom;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r227 = r0;
        r0 = r111;
        r0 = r0.top;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r228 = r0;
        r227 = r227 - r228;
        r226 = r226 + r227;
        r226 = r133 - r226;
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = "";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r197 = r225.toString();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225.<init>();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = "x=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = r197.length();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225.<init>();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r0 = r111;
        r0 = r0.left;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = r0;
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = "";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r197 = r225.toString();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225.<init>();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = "y=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = r197.length();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225.<init>();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r0 = r111;
        r0 = r0.bottom;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = r0;
        r0 = r111;
        r0 = r0.top;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r227 = r0;
        r226 = r226 - r227;
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = "";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r197 = r225.toString();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225.<init>();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = "width=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = r197.length();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225.<init>();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r0 = r111;
        r0 = r0.right;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = r0;
        r0 = r111;
        r0 = r0.left;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r227 = r0;
        r226 = r226 - r227;
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = "";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r197 = r225.toString();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225.<init>();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = "height=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = r197.length();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
    L_0x39a0:
        r58 = 0;
        if (r41 == 0) goto L_0x3f2e;
    L_0x39a4:
        r0 = r41;
        r1 = r61;
        r225 = r0.isInstance(r1);	 Catch:{ Exception -> 0x0360 }
        if (r225 == 0) goto L_0x3f2e;
    L_0x39ae:
        r225 = "mText";
        r0 = r41;
        r1 = r225;
        r58 = r0.getDeclaredField(r1);	 Catch:{ SecurityException -> 0x3f0c, NoSuchFieldException -> 0x3f1e }
        r225 = 1;
        r0 = r58;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ SecurityException -> 0x3f0c, NoSuchFieldException -> 0x3f1e }
    L_0x39c2:
        if (r58 == 0) goto L_0x3f92;
    L_0x39c4:
        if (r45 == 0) goto L_0x3f92;
    L_0x39c6:
        r0 = r58;
        r1 = r61;
        r138 = r0.get(r1);	 Catch:{ Exception -> 0x0360 }
        if (r138 == 0) goto L_0x3a63;
    L_0x39d0:
        r225 = "mText";
        r0 = r45;
        r1 = r225;
        r58 = r0.getDeclaredField(r1);	 Catch:{ SecurityException -> 0x3f72, NoSuchFieldException -> 0x3f82 }
        r225 = 1;
        r0 = r58;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ SecurityException -> 0x3f72, NoSuchFieldException -> 0x3f82 }
        r0 = r58;
        r1 = r138;
        r225 = r0.get(r1);	 Catch:{ SecurityException -> 0x3f72, NoSuchFieldException -> 0x3f82 }
        r0 = r225;
        r0 = (java.lang.String) r0;	 Catch:{ SecurityException -> 0x3f72, NoSuchFieldException -> 0x3f82 }
        r197 = r0;
        if (r197 == 0) goto L_0x3a63;
    L_0x39f4:
        r225 = 10;
        r0 = r197;
        r1 = r225;
        r225 = r0.indexOf(r1);	 Catch:{ SecurityException -> 0x3f72, NoSuchFieldException -> 0x3f82 }
        r226 = -1;
        r0 = r225;
        r1 = r226;
        if (r0 == r1) goto L_0x3a30;
    L_0x3a06:
        r225 = "(\n|\r\n)";
        r226 = "\u0003";
        r0 = r197;
        r1 = r225;
        r2 = r226;
        r197 = r0.replaceAll(r1, r2);	 Catch:{ SecurityException -> 0x3f72, NoSuchFieldException -> 0x3f82 }
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x3f72, NoSuchFieldException -> 0x3f82 }
        r226.<init>();	 Catch:{ SecurityException -> 0x3f72, NoSuchFieldException -> 0x3f82 }
        r227 = ">> newlineReplacement - ";
        r226 = r226.append(r227);	 Catch:{ SecurityException -> 0x3f72, NoSuchFieldException -> 0x3f82 }
        r0 = r226;
        r1 = r197;
        r226 = r0.append(r1);	 Catch:{ SecurityException -> 0x3f72, NoSuchFieldException -> 0x3f82 }
        r226 = r226.toString();	 Catch:{ SecurityException -> 0x3f72, NoSuchFieldException -> 0x3f82 }
        android.util.Log.d(r225, r226);	 Catch:{ SecurityException -> 0x3f72, NoSuchFieldException -> 0x3f82 }
    L_0x3a30:
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x3f72, NoSuchFieldException -> 0x3f82 }
        r225.<init>();	 Catch:{ SecurityException -> 0x3f72, NoSuchFieldException -> 0x3f82 }
        r226 = "text=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3f72, NoSuchFieldException -> 0x3f82 }
        r226 = r197.length();	 Catch:{ SecurityException -> 0x3f72, NoSuchFieldException -> 0x3f82 }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3f72, NoSuchFieldException -> 0x3f82 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3f72, NoSuchFieldException -> 0x3f82 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x3f72, NoSuchFieldException -> 0x3f82 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3f72, NoSuchFieldException -> 0x3f82 }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x3f72, NoSuchFieldException -> 0x3f82 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x3f72, NoSuchFieldException -> 0x3f82 }
    L_0x3a63:
        if (r45 == 0) goto L_0x3b02;
    L_0x3a65:
        r0 = r45;
        r1 = r61;
        r225 = r0.isInstance(r1);	 Catch:{ Exception -> 0x0360 }
        if (r225 == 0) goto L_0x3b02;
    L_0x3a6f:
        r225 = "mText";
        r0 = r45;
        r1 = r225;
        r58 = r0.getDeclaredField(r1);	 Catch:{ SecurityException -> 0x405f, NoSuchFieldException -> 0x406f }
        r225 = 1;
        r0 = r58;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ SecurityException -> 0x405f, NoSuchFieldException -> 0x406f }
        r0 = r58;
        r1 = r61;
        r225 = r0.get(r1);	 Catch:{ SecurityException -> 0x405f, NoSuchFieldException -> 0x406f }
        r0 = r225;
        r0 = (java.lang.String) r0;	 Catch:{ SecurityException -> 0x405f, NoSuchFieldException -> 0x406f }
        r197 = r0;
        if (r197 == 0) goto L_0x3b02;
    L_0x3a93:
        r225 = 10;
        r0 = r197;
        r1 = r225;
        r225 = r0.indexOf(r1);	 Catch:{ SecurityException -> 0x405f, NoSuchFieldException -> 0x406f }
        r226 = -1;
        r0 = r225;
        r1 = r226;
        if (r0 == r1) goto L_0x3acf;
    L_0x3aa5:
        r225 = "(\n|\r\n)";
        r226 = "\u0003";
        r0 = r197;
        r1 = r225;
        r2 = r226;
        r197 = r0.replaceAll(r1, r2);	 Catch:{ SecurityException -> 0x405f, NoSuchFieldException -> 0x406f }
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x405f, NoSuchFieldException -> 0x406f }
        r226.<init>();	 Catch:{ SecurityException -> 0x405f, NoSuchFieldException -> 0x406f }
        r227 = ">> newlineReplacement - ";
        r226 = r226.append(r227);	 Catch:{ SecurityException -> 0x405f, NoSuchFieldException -> 0x406f }
        r0 = r226;
        r1 = r197;
        r226 = r0.append(r1);	 Catch:{ SecurityException -> 0x405f, NoSuchFieldException -> 0x406f }
        r226 = r226.toString();	 Catch:{ SecurityException -> 0x405f, NoSuchFieldException -> 0x406f }
        android.util.Log.d(r225, r226);	 Catch:{ SecurityException -> 0x405f, NoSuchFieldException -> 0x406f }
    L_0x3acf:
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x405f, NoSuchFieldException -> 0x406f }
        r225.<init>();	 Catch:{ SecurityException -> 0x405f, NoSuchFieldException -> 0x406f }
        r226 = "text=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x405f, NoSuchFieldException -> 0x406f }
        r226 = r197.length();	 Catch:{ SecurityException -> 0x405f, NoSuchFieldException -> 0x406f }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x405f, NoSuchFieldException -> 0x406f }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x405f, NoSuchFieldException -> 0x406f }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x405f, NoSuchFieldException -> 0x406f }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x405f, NoSuchFieldException -> 0x406f }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x405f, NoSuchFieldException -> 0x406f }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x405f, NoSuchFieldException -> 0x406f }
    L_0x3b02:
        if (r43 == 0) goto L_0x3b72;
    L_0x3b04:
        r0 = r43;
        r1 = r61;
        r225 = r0.isInstance(r1);	 Catch:{ Exception -> 0x0360 }
        if (r225 == 0) goto L_0x3b72;
    L_0x3b0e:
        r225 = "mChecked";
        r0 = r43;
        r1 = r225;
        r58 = r0.getDeclaredField(r1);	 Catch:{ SecurityException -> 0x407f, NoSuchFieldException -> 0x408f }
        r225 = 1;
        r0 = r58;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ SecurityException -> 0x407f, NoSuchFieldException -> 0x408f }
        r0 = r58;
        r1 = r61;
        r109 = r0.getBoolean(r1);	 Catch:{ SecurityException -> 0x407f, NoSuchFieldException -> 0x408f }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x407f, NoSuchFieldException -> 0x408f }
        r225.<init>();	 Catch:{ SecurityException -> 0x407f, NoSuchFieldException -> 0x408f }
        r0 = r225;
        r1 = r109;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x407f, NoSuchFieldException -> 0x408f }
        r226 = "";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x407f, NoSuchFieldException -> 0x408f }
        r197 = r225.toString();	 Catch:{ SecurityException -> 0x407f, NoSuchFieldException -> 0x408f }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x407f, NoSuchFieldException -> 0x408f }
        r225.<init>();	 Catch:{ SecurityException -> 0x407f, NoSuchFieldException -> 0x408f }
        r226 = "checked=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x407f, NoSuchFieldException -> 0x408f }
        r226 = r197.length();	 Catch:{ SecurityException -> 0x407f, NoSuchFieldException -> 0x408f }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x407f, NoSuchFieldException -> 0x408f }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x407f, NoSuchFieldException -> 0x408f }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x407f, NoSuchFieldException -> 0x408f }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x407f, NoSuchFieldException -> 0x408f }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x407f, NoSuchFieldException -> 0x408f }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x407f, NoSuchFieldException -> 0x408f }
    L_0x3b72:
        if (r44 == 0) goto L_0x2adc;
    L_0x3b74:
        r0 = r44;
        r1 = r61;
        r225 = r0.isInstance(r1);	 Catch:{ Exception -> 0x0360 }
        if (r225 == 0) goto L_0x2adc;
    L_0x3b7e:
        r225 = "mSwitchBallPosition";
        r0 = r44;
        r1 = r225;
        r58 = r0.getDeclaredField(r1);	 Catch:{ SecurityException -> 0x3bd2, NoSuchFieldException -> 0x40a3 }
        r225 = 1;
        r0 = r58;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ SecurityException -> 0x3bd2, NoSuchFieldException -> 0x40a3 }
        r0 = r58;
        r1 = r61;
        r137 = r0.getInt(r1);	 Catch:{ SecurityException -> 0x3bd2, NoSuchFieldException -> 0x40a3 }
        if (r137 != 0) goto L_0x409f;
    L_0x3b9c:
        r197 = "Camera";
    L_0x3b9e:
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x3bd2, NoSuchFieldException -> 0x40a3 }
        r225.<init>();	 Catch:{ SecurityException -> 0x3bd2, NoSuchFieldException -> 0x40a3 }
        r226 = "entry=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3bd2, NoSuchFieldException -> 0x40a3 }
        r226 = r197.length();	 Catch:{ SecurityException -> 0x3bd2, NoSuchFieldException -> 0x40a3 }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3bd2, NoSuchFieldException -> 0x40a3 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3bd2, NoSuchFieldException -> 0x40a3 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x3bd2, NoSuchFieldException -> 0x40a3 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3bd2, NoSuchFieldException -> 0x40a3 }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x3bd2, NoSuchFieldException -> 0x40a3 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x3bd2, NoSuchFieldException -> 0x40a3 }
        goto L_0x2adc;
    L_0x3bd2:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "No mSwitchBallPosition for entry";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x2adc;
    L_0x3be2:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "No mOnClickListener for touch";
        android.util.Log.e(r225, r226);	 Catch:{ SecurityException -> 0x3bf6, NoSuchFieldException -> 0x3c06 }
        r225 = "mClickListener";
        r0 = r46;
        r1 = r225;
        r58 = r0.getDeclaredField(r1);	 Catch:{ SecurityException -> 0x3bf6, NoSuchFieldException -> 0x3c06 }
        goto L_0x36a5;
    L_0x3bf6:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "No mClickListener for touch";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x3759;
    L_0x3c06:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "No mClickListener for touch";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x3759;
    L_0x3c16:
        r197 = "false";
        goto L_0x3764;
    L_0x3c1a:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "No mTile for ID";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x3828;
    L_0x3c2a:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "No mTile for ID";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x3828;
    L_0x3c3a:
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225.<init>();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r0 = r111;
        r0 = r0.left;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = r0;
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = "";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r197 = r225.toString();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225.<init>();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = "x=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = r197.length();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225.<init>();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r0 = r111;
        r0 = r0.top;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = r0;
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = "";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r197 = r225.toString();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225.<init>();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = "y=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = r197.length();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225.<init>();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r0 = r111;
        r0 = r0.right;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = r0;
        r0 = r111;
        r0 = r0.left;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r227 = r0;
        r226 = r226 - r227;
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = "";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r197 = r225.toString();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225.<init>();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = "width=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = r197.length();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225.<init>();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r0 = r111;
        r0 = r0.bottom;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = r0;
        r0 = r111;
        r0 = r0.top;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r227 = r0;
        r226 = r226 - r227;
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = "";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r197 = r225.toString();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225.<init>();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = "height=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = r197.length();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        goto L_0x39a0;
    L_0x3d7b:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "Failed to get a rectangle";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.w(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x39a0;
    L_0x3d8b:
        r225 = "getCurrentArea";
        r226 = 0;
        r0 = r46;
        r1 = r225;
        r2 = r226;
        r225 = r0.getMethod(r1, r2);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = 0;
        r0 = r225;
        r1 = r61;
        r2 = r226;
        r115 = r0.invoke(r1, r2);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r115 = (android.graphics.RectF) r115;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225.<init>();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r0 = r115;
        r0 = r0.left;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = r0;
        r0 = r226;
        r0 = (int) r0;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = r0;
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = "";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r197 = r225.toString();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225.<init>();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = "x=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = r197.length();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225.<init>();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r0 = r115;
        r0 = r0.top;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = r0;
        r0 = r226;
        r0 = (int) r0;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = r0;
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = "";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r197 = r225.toString();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225.<init>();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = "y=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = r197.length();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225.<init>();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r0 = r115;
        r0 = r0.right;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = r0;
        r0 = r115;
        r0 = r0.left;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r227 = r0;
        r226 = r226 - r227;
        r0 = r226;
        r0 = (int) r0;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = r0;
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = "";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r197 = r225.toString();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225.<init>();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = "width=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = r197.length();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225.<init>();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r0 = r115;
        r0 = r0.bottom;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = r0;
        r0 = r115;
        r0 = r0.top;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r227 = r0;
        r226 = r226 - r227;
        r0 = r226;
        r0 = (int) r0;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = r0;
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = "";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r197 = r225.toString();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225.<init>();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = "height=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = r197.length();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x3d7b, NoSuchFieldException -> 0x3efc }
        goto L_0x39a0;
    L_0x3efc:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "Failed to get a rectangle";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.w(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x39a0;
    L_0x3f0c:
        r52 = move-exception;
        r58 = 0;
        r225 = "TDK";
        r226 = "No mText to get TwGLText";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.w(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x39c2;
    L_0x3f1e:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "No mText to get TwGLText";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.w(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x39c2;
    L_0x3f2e:
        if (r42 == 0) goto L_0x39c2;
    L_0x3f30:
        r0 = r42;
        r1 = r61;
        r225 = r0.isInstance(r1);	 Catch:{ Exception -> 0x0360 }
        if (r225 == 0) goto L_0x39c2;
    L_0x3f3a:
        r225 = "mText";
        r0 = r42;
        r1 = r225;
        r58 = r0.getDeclaredField(r1);	 Catch:{ SecurityException -> 0x3f50, NoSuchFieldException -> 0x3f62 }
        r225 = 1;
        r0 = r58;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ SecurityException -> 0x3f50, NoSuchFieldException -> 0x3f62 }
        goto L_0x39c2;
    L_0x3f50:
        r52 = move-exception;
        r58 = 0;
        r225 = "TDK";
        r226 = "No mText to get TwGLText";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.w(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x39c2;
    L_0x3f62:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "No mText to get TwGLText";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.w(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x39c2;
    L_0x3f72:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "No mText for text";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.w(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x3a63;
    L_0x3f82:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "No mText for text";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.w(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x3a63;
    L_0x3f92:
        if (r41 == 0) goto L_0x3f9e;
    L_0x3f94:
        r0 = r41;
        r1 = r61;
        r225 = r0.isInstance(r1);	 Catch:{ Exception -> 0x0360 }
        if (r225 != 0) goto L_0x3faa;
    L_0x3f9e:
        if (r42 == 0) goto L_0x3a63;
    L_0x3fa0:
        r0 = r42;
        r1 = r61;
        r225 = r0.isInstance(r1);	 Catch:{ Exception -> 0x0360 }
        if (r225 == 0) goto L_0x3a63;
    L_0x3faa:
        r225 = "mTitle";
        r0 = r46;
        r1 = r225;
        r58 = r0.getDeclaredField(r1);	 Catch:{ SecurityException -> 0x403f, NoSuchFieldException -> 0x404f }
        r225 = 1;
        r0 = r58;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ SecurityException -> 0x403f, NoSuchFieldException -> 0x404f }
        r0 = r58;
        r1 = r61;
        r225 = r0.get(r1);	 Catch:{ SecurityException -> 0x403f, NoSuchFieldException -> 0x404f }
        r0 = r225;
        r0 = (java.lang.String) r0;	 Catch:{ SecurityException -> 0x403f, NoSuchFieldException -> 0x404f }
        r197 = r0;
        if (r197 == 0) goto L_0x3a63;
    L_0x3fce:
        r225 = 10;
        r0 = r197;
        r1 = r225;
        r225 = r0.indexOf(r1);	 Catch:{ SecurityException -> 0x403f, NoSuchFieldException -> 0x404f }
        r226 = -1;
        r0 = r225;
        r1 = r226;
        if (r0 == r1) goto L_0x400a;
    L_0x3fe0:
        r225 = "(\n|\r\n)";
        r226 = "\u0003";
        r0 = r197;
        r1 = r225;
        r2 = r226;
        r197 = r0.replaceAll(r1, r2);	 Catch:{ SecurityException -> 0x403f, NoSuchFieldException -> 0x404f }
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x403f, NoSuchFieldException -> 0x404f }
        r226.<init>();	 Catch:{ SecurityException -> 0x403f, NoSuchFieldException -> 0x404f }
        r227 = ">> newlineReplacement - ";
        r226 = r226.append(r227);	 Catch:{ SecurityException -> 0x403f, NoSuchFieldException -> 0x404f }
        r0 = r226;
        r1 = r197;
        r226 = r0.append(r1);	 Catch:{ SecurityException -> 0x403f, NoSuchFieldException -> 0x404f }
        r226 = r226.toString();	 Catch:{ SecurityException -> 0x403f, NoSuchFieldException -> 0x404f }
        android.util.Log.d(r225, r226);	 Catch:{ SecurityException -> 0x403f, NoSuchFieldException -> 0x404f }
    L_0x400a:
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x403f, NoSuchFieldException -> 0x404f }
        r225.<init>();	 Catch:{ SecurityException -> 0x403f, NoSuchFieldException -> 0x404f }
        r226 = "text=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x403f, NoSuchFieldException -> 0x404f }
        r226 = r197.length();	 Catch:{ SecurityException -> 0x403f, NoSuchFieldException -> 0x404f }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x403f, NoSuchFieldException -> 0x404f }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x403f, NoSuchFieldException -> 0x404f }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x403f, NoSuchFieldException -> 0x404f }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x403f, NoSuchFieldException -> 0x404f }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x403f, NoSuchFieldException -> 0x404f }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x403f, NoSuchFieldException -> 0x404f }
        goto L_0x3a63;
    L_0x403f:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "No mTile for Text";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x3a63;
    L_0x404f:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "No mTile for Text";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x3a63;
    L_0x405f:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "No mText for text";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x3b02;
    L_0x406f:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "No mText for text";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x3b02;
    L_0x407f:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "No mChecked for check";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x3b72;
    L_0x408f:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "No mChecked for check";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x3b72;
    L_0x409f:
        r197 = "Camcorder";
        goto L_0x3b9e;
    L_0x40a3:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "No mSwitchBallPosition for entry";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x2adc;
    L_0x40b3:
        if (r32 == 0) goto L_0x475d;
    L_0x40b5:
        r0 = r32;
        r1 = r163;
        r225 = r0.isInstance(r1);	 Catch:{ Exception -> 0x0360 }
        if (r225 == 0) goto L_0x475d;
    L_0x40bf:
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r226.<init>();	 Catch:{ Exception -> 0x0360 }
        r227 = "ViewList: (GlView-Sec) ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r227 = r163.getClass();	 Catch:{ Exception -> 0x0360 }
        r227 = r227.getName();	 Catch:{ Exception -> 0x0360 }
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0360 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r163.getClass();	 Catch:{ Exception -> 0x0360 }
        r197 = r225.getSimpleName();	 Catch:{ Exception -> 0x0360 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "class=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r197.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r225 = r163.hashCode();	 Catch:{ Exception -> 0x0360 }
        r197 = java.lang.Integer.toHexString(r225);	 Catch:{ Exception -> 0x0360 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "hash=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r197.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        if (r29 == 0) goto L_0x41b9;
    L_0x4155:
        r0 = r29;
        r1 = r163;
        r225 = r0.isInstance(r1);	 Catch:{ Exception -> 0x0360 }
        if (r225 == 0) goto L_0x41b9;
    L_0x415f:
        r225 = "mResourceID";
        r0 = r29;
        r1 = r225;
        r58 = r0.getDeclaredField(r1);	 Catch:{ SecurityException -> 0x46cd, NoSuchFieldException -> 0x46dd }
        r225 = 1;
        r0 = r58;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ SecurityException -> 0x46cd, NoSuchFieldException -> 0x46dd }
        r0 = r58;
        r1 = r163;
        r131 = r0.getInt(r1);	 Catch:{ SecurityException -> 0x46cd, NoSuchFieldException -> 0x46dd }
        if (r131 == 0) goto L_0x41b9;
    L_0x417d:
        if (r195 == 0) goto L_0x41b9;
    L_0x417f:
        r0 = r195;
        r1 = r131;
        r197 = r0.getResourceEntryName(r1);	 Catch:{ NotFoundException -> 0x46bd }
        r225 = new java.lang.StringBuilder;	 Catch:{ NotFoundException -> 0x46bd }
        r225.<init>();	 Catch:{ NotFoundException -> 0x46bd }
        r226 = "entry=";
        r225 = r225.append(r226);	 Catch:{ NotFoundException -> 0x46bd }
        r226 = r197.length();	 Catch:{ NotFoundException -> 0x46bd }
        r225 = r225.append(r226);	 Catch:{ NotFoundException -> 0x46bd }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ NotFoundException -> 0x46bd }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ NotFoundException -> 0x46bd }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ NotFoundException -> 0x46bd }
        r225 = r225.toString();	 Catch:{ NotFoundException -> 0x46bd }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ NotFoundException -> 0x46bd }
    L_0x41b9:
        r118 = 0;
        r225 = "mRect";
        r0 = r32;
        r1 = r225;
        r130 = r0.getDeclaredField(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = 1;
        r0 = r130;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r0 = r130;
        r1 = r163;
        r69 = r0.get(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = "mGlObject";
        r0 = r32;
        r1 = r225;
        r58 = r0.getField(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = 1;
        r0 = r58;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r0 = r58;
        r1 = r163;
        r118 = r0.get(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = "checkPosIn";
        r226 = 2;
        r0 = r226;
        r0 = new java.lang.Class[r0];	 Catch:{ SecurityException -> 0x46ed, NoSuchMethodException -> 0x470d, NoSuchFieldException -> 0x471d }
        r226 = r0;
        r227 = 0;
        r228 = java.lang.Integer.TYPE;	 Catch:{ SecurityException -> 0x46ed, NoSuchMethodException -> 0x470d, NoSuchFieldException -> 0x471d }
        r226[r227] = r228;	 Catch:{ SecurityException -> 0x46ed, NoSuchMethodException -> 0x470d, NoSuchFieldException -> 0x471d }
        r227 = 1;
        r228 = java.lang.Integer.TYPE;	 Catch:{ SecurityException -> 0x46ed, NoSuchMethodException -> 0x470d, NoSuchFieldException -> 0x471d }
        r226[r227] = r228;	 Catch:{ SecurityException -> 0x46ed, NoSuchMethodException -> 0x470d, NoSuchFieldException -> 0x471d }
        r0 = r30;
        r1 = r225;
        r2 = r226;
        r19 = r0.getDeclaredMethod(r1, r2);	 Catch:{ SecurityException -> 0x46ed, NoSuchMethodException -> 0x470d, NoSuchFieldException -> 0x471d }
        r225 = 1;
        r0 = r19;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ SecurityException -> 0x46ed, NoSuchMethodException -> 0x470d, NoSuchFieldException -> 0x471d }
        r225 = 2;
        r0 = r225;
        r0 = new java.lang.Object[r0];	 Catch:{ SecurityException -> 0x46ed, NoSuchMethodException -> 0x470d, NoSuchFieldException -> 0x471d }
        r225 = r0;
        r226 = 0;
        r227 = 0;
        r227 = java.lang.Integer.valueOf(r227);	 Catch:{ SecurityException -> 0x46ed, NoSuchMethodException -> 0x470d, NoSuchFieldException -> 0x471d }
        r225[r226] = r227;	 Catch:{ SecurityException -> 0x46ed, NoSuchMethodException -> 0x470d, NoSuchFieldException -> 0x471d }
        r226 = 1;
        r227 = 0;
        r227 = java.lang.Integer.valueOf(r227);	 Catch:{ SecurityException -> 0x46ed, NoSuchMethodException -> 0x470d, NoSuchFieldException -> 0x471d }
        r225[r226] = r227;	 Catch:{ SecurityException -> 0x46ed, NoSuchMethodException -> 0x470d, NoSuchFieldException -> 0x471d }
        r0 = r19;
        r1 = r118;
        r2 = r225;
        r0.invoke(r1, r2);	 Catch:{ SecurityException -> 0x46ed, NoSuchMethodException -> 0x470d, NoSuchFieldException -> 0x471d }
    L_0x4241:
        r225 = "mXlt";
        r0 = r30;
        r1 = r225;
        r58 = r0.getDeclaredField(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = 1;
        r0 = r58;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r0 = r58;
        r1 = r118;
        r161 = r0.getFloat(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = "mYlt";
        r0 = r30;
        r1 = r225;
        r58 = r0.getDeclaredField(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = 1;
        r0 = r58;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r0 = r58;
        r1 = r118;
        r162 = r0.getFloat(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = "mXrb";
        r0 = r30;
        r1 = r225;
        r58 = r0.getDeclaredField(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = 1;
        r0 = r58;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r0 = r58;
        r1 = r118;
        r225 = r0.getFloat(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r160 = r225 - r161;
        r225 = "mYrb";
        r0 = r30;
        r1 = r225;
        r58 = r0.getDeclaredField(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = 1;
        r0 = r58;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r0 = r58;
        r1 = r118;
        r225 = r0.getFloat(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r158 = r225 - r162;
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225.<init>();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = "x2=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226.<init>();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r0 = r226;
        r1 = r161;
        r226 = r0.append(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r227 = "";
        r226 = r226.append(r227);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = r226.toString();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = r226.length();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r0 = r225;
        r1 = r161;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225.<init>();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = "y2=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226.<init>();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r0 = r226;
        r1 = r162;
        r226 = r0.append(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r227 = "";
        r226 = r226.append(r227);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = r226.toString();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = r226.length();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r0 = r225;
        r1 = r162;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225.<init>();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = "width2=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226.<init>();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r0 = r226;
        r1 = r160;
        r226 = r0.append(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r227 = "";
        r226 = r226.append(r227);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = r226.toString();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = r226.length();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r0 = r225;
        r1 = r160;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225.<init>();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = "height2=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226.<init>();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r0 = r226;
        r1 = r158;
        r226 = r0.append(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r227 = "";
        r226 = r226.append(r227);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = r226.toString();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = r226.length();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r0 = r225;
        r1 = r158;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = r69.getClass();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = "mLeft";
        r126 = r225.getField(r226);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = 1;
        r0 = r126;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r0 = r126;
        r1 = r69;
        r223 = r0.getInt(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = r69.getClass();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = "mTop";
        r141 = r225.getField(r226);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = 1;
        r0 = r141;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r0 = r141;
        r1 = r69;
        r224 = r0.getInt(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = r69.getClass();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = "mWidth";
        r146 = r225.getField(r226);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = 1;
        r0 = r146;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r0 = r146;
        r1 = r69;
        r222 = r0.getInt(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = r69.getClass();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = "mHeight";
        r120 = r225.getField(r226);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = 1;
        r0 = r120;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r0 = r120;
        r1 = r69;
        r70 = r0.getInt(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = "mParent";
        r0 = r32;
        r1 = r225;
        r58 = r0.getDeclaredField(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = 1;
        r0 = r58;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r0 = r58;
        r1 = r163;
        r129 = r0.get(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        if (r129 == 0) goto L_0x472d;
    L_0x446a:
        r225 = "getWidth";
        r226 = 0;
        r0 = r32;
        r1 = r225;
        r2 = r226;
        r225 = r0.getMethod(r1, r2);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = 0;
        r0 = r225;
        r1 = r129;
        r2 = r226;
        r225 = r0.invoke(r1, r2);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = (java.lang.Integer) r225;	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r172 = r225.intValue();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = "getHeight";
        r226 = 0;
        r0 = r32;
        r1 = r225;
        r2 = r226;
        r225 = r0.getMethod(r1, r2);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = 0;
        r0 = r225;
        r1 = r129;
        r2 = r226;
        r225 = r0.invoke(r1, r2);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = (java.lang.Integer) r225;	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r171 = r225.intValue();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r0 = r172;
        r0 = (float) r0;	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = r0;
        r178 = r160 / r225;
        r0 = r171;
        r0 = (float) r0;	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = r0;
        r179 = r158 / r225;
    L_0x44b8:
        r0 = r223;
        r0 = (float) r0;	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = r0;
        r225 = r225 * r178;
        r225 = r225 + r161;
        r0 = r225;
        r0 = (int) r0;	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r215 = r0;
        r0 = r224;
        r0 = (float) r0;	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = r0;
        r225 = r225 * r179;
        r225 = r225 + r162;
        r0 = r225;
        r0 = (int) r0;	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r216 = r0;
        r0 = r222;
        r0 = (float) r0;	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = r0;
        r225 = r225 * r178;
        r0 = r225;
        r0 = (int) r0;	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r214 = r0;
        r0 = r70;
        r0 = (float) r0;	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = r0;
        r225 = r225 * r179;
        r0 = r225;
        r0 = (int) r0;	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r212 = r0;
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225.<init>();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = "x=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226.<init>();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r0 = r226;
        r1 = r215;
        r226 = r0.append(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r227 = "";
        r226 = r226.append(r227);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = r226.toString();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = r226.length();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r0 = r225;
        r1 = r215;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225.<init>();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = "y=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226.<init>();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r0 = r226;
        r1 = r216;
        r226 = r0.append(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r227 = "";
        r226 = r226.append(r227);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = r226.toString();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = r226.length();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r0 = r225;
        r1 = r216;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225.<init>();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = "width=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226.<init>();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r0 = r226;
        r1 = r214;
        r226 = r0.append(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r227 = "";
        r226 = r226.append(r227);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = r226.toString();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = r226.length();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r0 = r225;
        r1 = r214;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225.<init>();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = "height=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226.<init>();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r0 = r226;
        r1 = r212;
        r226 = r0.append(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r227 = "";
        r226 = r226.append(r227);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = r226.toString();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = r226.length();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r0 = r225;
        r1 = r212;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
    L_0x4613:
        if (r31 == 0) goto L_0x4670;
    L_0x4615:
        r0 = r31;
        r1 = r163;
        r225 = r0.isInstance(r1);	 Catch:{ Exception -> 0x0360 }
        if (r225 == 0) goto L_0x4670;
    L_0x461f:
        r225 = "mText";
        r0 = r31;
        r1 = r225;
        r58 = r0.getDeclaredField(r1);	 Catch:{ SecurityException -> 0x473d, NoSuchFieldException -> 0x474d }
        r225 = 1;
        r0 = r58;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ SecurityException -> 0x473d, NoSuchFieldException -> 0x474d }
        r0 = r58;
        r1 = r163;
        r138 = r0.get(r1);	 Catch:{ SecurityException -> 0x473d, NoSuchFieldException -> 0x474d }
        r138 = (java.lang.String) r138;	 Catch:{ SecurityException -> 0x473d, NoSuchFieldException -> 0x474d }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x473d, NoSuchFieldException -> 0x474d }
        r225.<init>();	 Catch:{ SecurityException -> 0x473d, NoSuchFieldException -> 0x474d }
        r226 = "text=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x473d, NoSuchFieldException -> 0x474d }
        r226 = r138.length();	 Catch:{ SecurityException -> 0x473d, NoSuchFieldException -> 0x474d }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x473d, NoSuchFieldException -> 0x474d }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x473d, NoSuchFieldException -> 0x474d }
        r0 = r225;
        r1 = r138;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x473d, NoSuchFieldException -> 0x474d }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x473d, NoSuchFieldException -> 0x474d }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x473d, NoSuchFieldException -> 0x474d }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x473d, NoSuchFieldException -> 0x474d }
    L_0x4670:
        r54 = 1;
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r54;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = "";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r197 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "enable=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r197.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        goto L_0x2adc;
    L_0x46bd:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "Failed to get Resource entry name";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ SecurityException -> 0x46cd, NoSuchFieldException -> 0x46dd }
        goto L_0x41b9;
    L_0x46cd:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "No mResourceID for entry";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x41b9;
    L_0x46dd:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "No mResourceID for entry";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x41b9;
    L_0x46ed:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "No method: checkPosIn";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        goto L_0x4241;
    L_0x46fd:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "Failed to get a rectangle";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x4613;
    L_0x470d:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "No method: checkPosIn";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ SecurityException -> 0x46fd, NoSuchFieldException -> 0x471d }
        goto L_0x4241;
    L_0x471d:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "Failed to get a rectangle";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x4613;
    L_0x472d:
        r0 = r222;
        r0 = (float) r0;	 Catch:{ Exception -> 0x0360 }
        r225 = r0;
        r178 = r160 / r225;
        r0 = r70;
        r0 = (float) r0;	 Catch:{ Exception -> 0x0360 }
        r225 = r0;
        r179 = r158 / r225;
        goto L_0x44b8;
    L_0x473d:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "No mText for text";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x4670;
    L_0x474d:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "No mText for text";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x4670;
    L_0x475d:
        if (r30 == 0) goto L_0x4a51;
    L_0x475f:
        r0 = r30;
        r1 = r163;
        r225 = r0.isInstance(r1);	 Catch:{ Exception -> 0x0360 }
        if (r225 == 0) goto L_0x4a51;
    L_0x4769:
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r226.<init>();	 Catch:{ Exception -> 0x0360 }
        r227 = "ViewList: (GlObject-Sec) ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r227 = r163.getClass();	 Catch:{ Exception -> 0x0360 }
        r227 = r227.getName();	 Catch:{ Exception -> 0x0360 }
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0360 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r163.getClass();	 Catch:{ Exception -> 0x0360 }
        r197 = r225.getSimpleName();	 Catch:{ Exception -> 0x0360 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "class=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r197.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r225 = r163.hashCode();	 Catch:{ Exception -> 0x0360 }
        r197 = java.lang.Integer.toHexString(r225);	 Catch:{ Exception -> 0x0360 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "hash=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r197.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r197 = "true";
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "enable=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r197.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r225 = "checkPosIn";
        r226 = 2;
        r0 = r226;
        r0 = new java.lang.Class[r0];	 Catch:{ SecurityException -> 0x4a31, NoSuchMethodException -> 0x4a41 }
        r226 = r0;
        r227 = 0;
        r228 = java.lang.Integer.TYPE;	 Catch:{ SecurityException -> 0x4a31, NoSuchMethodException -> 0x4a41 }
        r226[r227] = r228;	 Catch:{ SecurityException -> 0x4a31, NoSuchMethodException -> 0x4a41 }
        r227 = 1;
        r228 = java.lang.Integer.TYPE;	 Catch:{ SecurityException -> 0x4a31, NoSuchMethodException -> 0x4a41 }
        r226[r227] = r228;	 Catch:{ SecurityException -> 0x4a31, NoSuchMethodException -> 0x4a41 }
        r0 = r30;
        r1 = r225;
        r2 = r226;
        r19 = r0.getDeclaredMethod(r1, r2);	 Catch:{ SecurityException -> 0x4a31, NoSuchMethodException -> 0x4a41 }
        r225 = 1;
        r0 = r19;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ SecurityException -> 0x4a31, NoSuchMethodException -> 0x4a41 }
        r225 = 2;
        r0 = r225;
        r0 = new java.lang.Object[r0];	 Catch:{ SecurityException -> 0x4a31, NoSuchMethodException -> 0x4a41 }
        r225 = r0;
        r226 = 0;
        r227 = 0;
        r227 = java.lang.Integer.valueOf(r227);	 Catch:{ SecurityException -> 0x4a31, NoSuchMethodException -> 0x4a41 }
        r225[r226] = r227;	 Catch:{ SecurityException -> 0x4a31, NoSuchMethodException -> 0x4a41 }
        r226 = 1;
        r227 = 0;
        r227 = java.lang.Integer.valueOf(r227);	 Catch:{ SecurityException -> 0x4a31, NoSuchMethodException -> 0x4a41 }
        r225[r226] = r227;	 Catch:{ SecurityException -> 0x4a31, NoSuchMethodException -> 0x4a41 }
        r0 = r19;
        r1 = r163;
        r2 = r225;
        r0.invoke(r1, r2);	 Catch:{ SecurityException -> 0x4a31, NoSuchMethodException -> 0x4a41 }
    L_0x4880:
        r225 = "mXlt";
        r0 = r30;
        r1 = r225;
        r126 = r0.getDeclaredField(r1);	 Catch:{ Exception -> 0x0360 }
        r225 = 1;
        r0 = r126;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x0360 }
        r0 = r126;
        r1 = r163;
        r225 = r0.getFloat(r1);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r0 = (int) r0;	 Catch:{ Exception -> 0x0360 }
        r223 = r0;
        r225 = "mYlt";
        r0 = r30;
        r1 = r225;
        r141 = r0.getDeclaredField(r1);	 Catch:{ Exception -> 0x0360 }
        r225 = 1;
        r0 = r141;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x0360 }
        r0 = r141;
        r1 = r163;
        r225 = r0.getFloat(r1);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r0 = (int) r0;	 Catch:{ Exception -> 0x0360 }
        r224 = r0;
        r225 = "mXrb";
        r0 = r30;
        r1 = r225;
        r146 = r0.getDeclaredField(r1);	 Catch:{ Exception -> 0x0360 }
        r225 = 1;
        r0 = r146;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x0360 }
        r0 = r146;
        r1 = r163;
        r225 = r0.getFloat(r1);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r0 = (int) r0;	 Catch:{ Exception -> 0x0360 }
        r225 = r0;
        r222 = r225 - r223;
        r225 = "mYrb";
        r0 = r30;
        r1 = r225;
        r120 = r0.getDeclaredField(r1);	 Catch:{ Exception -> 0x0360 }
        r225 = 1;
        r0 = r120;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ Exception -> 0x0360 }
        r0 = r120;
        r1 = r163;
        r225 = r0.getFloat(r1);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r0 = (int) r0;	 Catch:{ Exception -> 0x0360 }
        r225 = r0;
        r70 = r225 - r224;
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "x=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r226.<init>();	 Catch:{ Exception -> 0x0360 }
        r0 = r226;
        r1 = r223;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r227 = "";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0360 }
        r226 = r226.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r223;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "y=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r226.<init>();	 Catch:{ Exception -> 0x0360 }
        r0 = r226;
        r1 = r224;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r227 = "";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0360 }
        r226 = r226.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r224;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "width=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r226.<init>();	 Catch:{ Exception -> 0x0360 }
        r0 = r226;
        r1 = r222;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r227 = "";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0360 }
        r226 = r226.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r222;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "height=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r226.<init>();	 Catch:{ Exception -> 0x0360 }
        r0 = r226;
        r1 = r70;
        r226 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r227 = "";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0360 }
        r226 = r226.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r70;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        goto L_0x2adc;
    L_0x4a31:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "No method: checkPosIn";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x4880;
    L_0x4a41:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "No method: checkPosIn";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x4880;
    L_0x4a51:
        if (r27 == 0) goto L_0x4ce6;
    L_0x4a53:
        r0 = r27;
        r1 = r163;
        r225 = r0.isInstance(r1);	 Catch:{ Exception -> 0x0360 }
        if (r225 == 0) goto L_0x4ce6;
    L_0x4a5d:
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r226.<init>();	 Catch:{ Exception -> 0x0360 }
        r227 = "ViewList: (GLView-Sec) ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r227 = r163.getClass();	 Catch:{ Exception -> 0x0360 }
        r227 = r227.getName();	 Catch:{ Exception -> 0x0360 }
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0360 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r163.getClass();	 Catch:{ Exception -> 0x0360 }
        r197 = r225.getSimpleName();	 Catch:{ Exception -> 0x0360 }
        r225 = r197.isEmpty();	 Catch:{ Exception -> 0x0360 }
        if (r225 == 0) goto L_0x4a8d;
    L_0x4a8b:
        r197 = "$";
    L_0x4a8d:
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "class=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r197.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r225 = r163.hashCode();	 Catch:{ Exception -> 0x0360 }
        r197 = java.lang.Integer.toHexString(r225);	 Catch:{ Exception -> 0x0360 }
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "hash=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r197.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r197 = "true";
        r225 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r225.<init>();	 Catch:{ Exception -> 0x0360 }
        r226 = "enable=";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = r197.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ Exception -> 0x0360 }
        r225 = r225.toString();	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        r225 = "bounds";
        r226 = 0;
        r0 = r27;
        r1 = r225;
        r2 = r226;
        r18 = r0.getMethod(r1, r2);	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r225 = 1;
        r0 = r18;
        r1 = r225;
        r0.setAccessible(r1);	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r225 = 0;
        r0 = r18;
        r1 = r163;
        r2 = r225;
        r180 = r0.invoke(r1, r2);	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r180 = (android.graphics.Rect) r180;	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r0 = r180;
        r0 = r0.left;	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r223 = r0;
        r0 = r180;
        r0 = r0.top;	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r224 = r0;
        r222 = r180.width();	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r70 = r180.height();	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r225.<init>();	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r226 = "x=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r226 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r226.<init>();	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r0 = r226;
        r1 = r223;
        r226 = r0.append(r1);	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r227 = "";
        r226 = r226.append(r227);	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r226 = r226.toString();	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r226 = r226.length();	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r0 = r225;
        r1 = r223;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r225.<init>();	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r226 = "y=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r226 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r226.<init>();	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r0 = r226;
        r1 = r224;
        r226 = r0.append(r1);	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r227 = "";
        r226 = r226.append(r227);	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r226 = r226.toString();	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r226 = r226.length();	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r0 = r225;
        r1 = r224;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r225.<init>();	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r226 = "width=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r226 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r226.<init>();	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r0 = r226;
        r1 = r222;
        r226 = r0.append(r1);	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r227 = "";
        r226 = r226.append(r227);	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r226 = r226.toString();	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r226 = r226.length();	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r0 = r225;
        r1 = r222;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r225.<init>();	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r226 = "height=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r226 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r226.<init>();	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r0 = r226;
        r1 = r70;
        r226 = r0.append(r1);	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r227 = "";
        r226 = r226.append(r227);	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r226 = r226.toString();	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r226 = r226.length();	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r0 = r225;
        r1 = r70;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r197 = "true";
        r225 = new java.lang.StringBuilder;	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r225.<init>();	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r226 = "pos_relative=";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r226 = r197.length();	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r226 = ",";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r0 = r225;
        r1 = r197;
        r225 = r0.append(r1);	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r226 = " ";
        r225 = r225.append(r226);	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r225 = r225.toString();	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        r0 = r166;
        r1 = r225;
        r0.append(r1);	 Catch:{ SecurityException -> 0x4cc6, NoSuchMethodException -> 0x4cd6 }
        goto L_0x2adc;
    L_0x4cc6:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "Failed to get a boundary";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x2adc;
    L_0x4cd6:
        r52 = move-exception;
        r225 = "TDK";
        r226 = "Failed to get a boundary";
        r0 = r225;
        r1 = r226;
        r2 = r52;
        android.util.Log.e(r0, r1, r2);	 Catch:{ Exception -> 0x0360 }
        goto L_0x2adc;
    L_0x4ce6:
        r0 = r163;
        r0 = r0 instanceof java.lang.String;	 Catch:{ Exception -> 0x0360 }
        r225 = r0;
        if (r225 == 0) goto L_0x4cf9;
    L_0x4cee:
        r163 = (java.lang.String) r163;	 Catch:{ Exception -> 0x0360 }
        r0 = r166;
        r1 = r163;
        r0.append(r1);	 Catch:{ Exception -> 0x0360 }
        goto L_0x2adc;
    L_0x4cf9:
        r225 = "TDK";
        r226 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0360 }
        r226.<init>();	 Catch:{ Exception -> 0x0360 }
        r227 = "ViewList: (Unknown) ";
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r227 = r163.getClass();	 Catch:{ Exception -> 0x0360 }
        r227 = r227.getName();	 Catch:{ Exception -> 0x0360 }
        r226 = r226.append(r227);	 Catch:{ Exception -> 0x0360 }
        r226 = r226.toString();	 Catch:{ Exception -> 0x0360 }
        android.util.Log.d(r225, r226);	 Catch:{ Exception -> 0x0360 }
        r207 = 1;
        r174 = r91;
        r74 = 0;
    L_0x4d1f:
        r0 = r74;
        r1 = r91;
        if (r0 >= r1) goto L_0x20f9;
    L_0x4d25:
        r225 = r166.length();	 Catch:{ Exception -> 0x0360 }
        r225 = r225 + -1;
        r0 = r166;
        r1 = r225;
        r0.deleteCharAt(r1);	 Catch:{ Exception -> 0x0360 }
        r74 = r74 + 1;
        goto L_0x4d1f;
    L_0x4d35:
        r165 = new java.io.BufferedWriter;	 Catch:{ Exception -> 0x0360 }
        r225 = new java.io.OutputStreamWriter;	 Catch:{ Exception -> 0x0360 }
        r0 = r225;
        r1 = r233;
        r0.<init>(r1);	 Catch:{ Exception -> 0x0360 }
        r226 = 32768; // 0x8000 float:4.5918E-41 double:1.61895E-319;
        r0 = r165;
        r1 = r225;
        r2 = r226;
        r0.<init>(r1, r2);	 Catch:{ Exception -> 0x0360 }
        r225 = r166.toString();	 Catch:{ Exception -> 0x4d71, all -> 0x4d6c }
        r0 = r165;
        r1 = r225;
        r0.write(r1);	 Catch:{ Exception -> 0x4d71, all -> 0x4d6c }
        r225 = "DONE.";
        r0 = r165;
        r1 = r225;
        r0.write(r1);	 Catch:{ Exception -> 0x4d71, all -> 0x4d6c }
        r165.newLine();	 Catch:{ Exception -> 0x4d71, all -> 0x4d6c }
        if (r165 == 0) goto L_0x4d7c;
    L_0x4d65:
        r165.close();
        r164 = r165;
        goto L_0x0373;
    L_0x4d6c:
        r225 = move-exception;
        r164 = r165;
        goto L_0x0545;
    L_0x4d71:
        r52 = move-exception;
        r164 = r165;
        goto L_0x0361;
    L_0x4d76:
        r225 = move-exception;
        goto L_0x2364;
    L_0x4d79:
        r225 = move-exception;
        goto L_0x0a82;
    L_0x4d7c:
        r164 = r165;
        goto L_0x0373;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.ViewDebug.dump_s(android.view.View, boolean, boolean, java.io.OutputStream):void");
    }
}
