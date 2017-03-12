package android.ddm;

import android.opengl.GLUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.view.WindowManagerGlobal;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import org.apache.harmony.dalvik.ddmc.Chunk;
import org.apache.harmony.dalvik.ddmc.ChunkHandler;
import org.apache.harmony.dalvik.ddmc.DdmServer;

public class DdmHandleViewDebug extends ChunkHandler {
    public static final int CHUNK_VUGL = type("VUGL");
    private static final int CHUNK_VULW = type("VULW");
    private static final int CHUNK_VUOP = type("VUOP");
    private static final int CHUNK_VURT = type("VURT");
    private static final int ERR_EXCEPTION = -3;
    private static final int ERR_INVALID_OP = -1;
    private static final int ERR_INVALID_PARAM = -2;
    private static final String TAG = "DdmViewDebug";
    private static final int VUOP_CAPTURE_VIEW = 1;
    private static final int VUOP_DUMP_DISPLAYLIST = 2;
    private static final int VUOP_INVOKE_VIEW_METHOD = 4;
    private static final int VUOP_PROFILE_VIEW = 3;
    private static final int VUOP_SET_LAYOUT_PARAMETER = 5;
    private static final int VURT_CAPTURE_LAYERS = 2;
    private static final int VURT_DUMP_HIERARCHY = 1;
    private static final int VURT_DUMP_THEME = 3;
    private static final DdmHandleViewDebug sInstance = new DdmHandleViewDebug();

    private DdmHandleViewDebug() {
    }

    public static void register() {
        DdmServer.registerHandler(CHUNK_VUGL, sInstance);
        DdmServer.registerHandler(CHUNK_VULW, sInstance);
        DdmServer.registerHandler(CHUNK_VURT, sInstance);
        DdmServer.registerHandler(CHUNK_VUOP, sInstance);
    }

    public void connected() {
    }

    public void disconnected() {
    }

    public Chunk handleChunk(Chunk request) {
        int type = request.type;
        if (type == CHUNK_VUGL) {
            return handleOpenGlTrace(request);
        }
        if (type == CHUNK_VULW) {
            return listWindows();
        }
        ByteBuffer in = wrapChunk(request);
        int op = in.getInt();
        View rootView = getRootView(in);
        if (rootView == null) {
            return createFailChunk(-2, "Invalid View Root");
        }
        if (type != CHUNK_VURT) {
            View targetView = getTargetView(rootView, in);
            if (targetView == null) {
                return createFailChunk(-2, "Invalid target view");
            }
            if (type == CHUNK_VUOP) {
                switch (op) {
                    case 1:
                        return captureView(rootView, targetView);
                    case 2:
                        return dumpDisplayLists(rootView, targetView);
                    case 3:
                        return profileView(rootView, targetView);
                    case 4:
                        return invokeViewMethod(rootView, targetView, in);
                    case 5:
                        return setLayoutParameter(rootView, targetView, in);
                    default:
                        return createFailChunk(-1, "Unknown view operation: " + op);
                }
            }
            throw new RuntimeException("Unknown packet " + ChunkHandler.name(type));
        } else if (op == 1) {
            return dumpHierarchy(rootView, in);
        } else {
            if (op == 2) {
                return captureLayers(rootView);
            }
            if (op == 3) {
                return dumpTheme(rootView);
            }
            return createFailChunk(-1, "Unknown view root operation: " + op);
        }
    }

    private Chunk handleOpenGlTrace(Chunk request) {
        GLUtils.setTracingLevel(wrapChunk(request).getInt());
        return null;
    }

    private Chunk listWindows() {
        String[] windowNames = WindowManagerGlobal.getInstance().getViewRootNames();
        int responseLength = 4;
        for (String name : windowNames) {
            responseLength = (responseLength + 4) + (name.length() * 2);
        }
        ByteBuffer out = ByteBuffer.allocate(responseLength);
        out.order(ChunkHandler.CHUNK_ORDER);
        out.putInt(windowNames.length);
        for (String name2 : windowNames) {
            out.putInt(name2.length());
            putString(out, name2);
        }
        return new Chunk(CHUNK_VULW, out);
    }

    private View getRootView(ByteBuffer in) {
        try {
            return WindowManagerGlobal.getInstance().getRootView(getString(in, in.getInt()));
        } catch (BufferUnderflowException e) {
            return null;
        }
    }

    private View getTargetView(View root, ByteBuffer in) {
        try {
            return ViewDebug.findView(root, getString(in, in.getInt()));
        } catch (BufferUnderflowException e) {
            return null;
        }
    }

    private Chunk dumpHierarchy(View rootView, ByteBuffer in) {
        boolean skipChildren = in.getInt() > 0;
        boolean includeProperties = in.getInt() > 0;
        boolean v2 = in.hasRemaining() && in.getInt() > 0;
        long start = System.currentTimeMillis();
        ByteArrayOutputStream b = new ByteArrayOutputStream(2097152);
        if (v2) {
            try {
                ViewDebug.dumpv2(rootView, b);
            } catch (IOException e) {
                Exception e2 = e;
                return createFailChunk(1, "Unexpected error while obtaining view hierarchy: " + e2.getMessage());
            } catch (InterruptedException e3) {
                e2 = e3;
                return createFailChunk(1, "Unexpected error while obtaining view hierarchy: " + e2.getMessage());
            }
        }
        ViewDebug.dump(rootView, skipChildren, includeProperties, b);
        Log.d(TAG, "Time to obtain view hierarchy (ms): " + (System.currentTimeMillis() - start));
        byte[] data = b.toByteArray();
        return new Chunk(CHUNK_VURT, data, 0, data.length);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private org.apache.harmony.dalvik.ddmc.Chunk captureLayers(android.view.View r9) {
        /*
        r8 = this;
        r0 = new java.io.ByteArrayOutputStream;
        r4 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r0.<init>(r4);
        r2 = new java.io.DataOutputStream;
        r2.<init>(r0);
        android.view.ViewDebug.captureLayers(r9, r2);	 Catch:{ IOException -> 0x0020 }
        r2.close();	 Catch:{ IOException -> 0x0048 }
    L_0x0012:
        r1 = r0.toByteArray();
        r4 = new org.apache.harmony.dalvik.ddmc.Chunk;
        r5 = CHUNK_VURT;
        r6 = 0;
        r7 = r1.length;
        r4.<init>(r5, r1, r6, r7);
    L_0x001f:
        return r4;
    L_0x0020:
        r3 = move-exception;
        r4 = 1;
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0043 }
        r5.<init>();	 Catch:{ all -> 0x0043 }
        r6 = "Unexpected error while obtaining view hierarchy: ";
        r5 = r5.append(r6);	 Catch:{ all -> 0x0043 }
        r6 = r3.getMessage();	 Catch:{ all -> 0x0043 }
        r5 = r5.append(r6);	 Catch:{ all -> 0x0043 }
        r5 = r5.toString();	 Catch:{ all -> 0x0043 }
        r4 = createFailChunk(r4, r5);	 Catch:{ all -> 0x0043 }
        r2.close();	 Catch:{ IOException -> 0x0041 }
        goto L_0x001f;
    L_0x0041:
        r5 = move-exception;
        goto L_0x001f;
    L_0x0043:
        r4 = move-exception;
        r2.close();	 Catch:{ IOException -> 0x004a }
    L_0x0047:
        throw r4;
    L_0x0048:
        r4 = move-exception;
        goto L_0x0012;
    L_0x004a:
        r5 = move-exception;
        goto L_0x0047;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.ddm.DdmHandleViewDebug.captureLayers(android.view.View):org.apache.harmony.dalvik.ddmc.Chunk");
    }

    private Chunk dumpTheme(View rootView) {
        ByteArrayOutputStream b = new ByteArrayOutputStream(1024);
        try {
            ViewDebug.dumpTheme(rootView, b);
            byte[] data = b.toByteArray();
            return new Chunk(CHUNK_VURT, data, 0, data.length);
        } catch (IOException e) {
            return createFailChunk(1, "Unexpected error while dumping the theme: " + e.getMessage());
        }
    }

    private Chunk captureView(View rootView, View targetView) {
        ByteArrayOutputStream b = new ByteArrayOutputStream(1024);
        try {
            ViewDebug.capture(rootView, b, targetView);
            byte[] data = b.toByteArray();
            return new Chunk(CHUNK_VUOP, data, 0, data.length);
        } catch (IOException e) {
            return createFailChunk(1, "Unexpected error while capturing view: " + e.getMessage());
        }
    }

    private Chunk dumpDisplayLists(final View rootView, final View targetView) {
        rootView.post(new Runnable() {
            public void run() {
                ViewDebug.outputDisplayList(rootView, targetView);
            }
        });
        return null;
    }

    private Chunk invokeViewMethod(View rootView, View targetView, ByteBuffer in) {
        Class<?>[] argTypes;
        Object[] args;
        String methodName = getString(in, in.getInt());
        if (in.hasRemaining()) {
            int nArgs = in.getInt();
            argTypes = new Class[nArgs];
            args = new Object[nArgs];
            for (int i = 0; i < nArgs; i++) {
                char c = in.getChar();
                switch (c) {
                    case 'B':
                        argTypes[i] = Byte.TYPE;
                        args[i] = Byte.valueOf(in.get());
                        break;
                    case 'C':
                        argTypes[i] = Character.TYPE;
                        args[i] = Character.valueOf(in.getChar());
                        break;
                    case 'D':
                        argTypes[i] = Double.TYPE;
                        args[i] = Double.valueOf(in.getDouble());
                        break;
                    case 'F':
                        argTypes[i] = Float.TYPE;
                        args[i] = Float.valueOf(in.getFloat());
                        break;
                    case 'I':
                        argTypes[i] = Integer.TYPE;
                        args[i] = Integer.valueOf(in.getInt());
                        break;
                    case 'J':
                        argTypes[i] = Long.TYPE;
                        args[i] = Long.valueOf(in.getLong());
                        break;
                    case 'S':
                        argTypes[i] = Short.TYPE;
                        args[i] = Short.valueOf(in.getShort());
                        break;
                    case 'Z':
                        boolean z;
                        argTypes[i] = Boolean.TYPE;
                        if (in.get() == (byte) 0) {
                            z = false;
                        } else {
                            z = true;
                        }
                        args[i] = Boolean.valueOf(z);
                        break;
                    default:
                        Log.e(TAG, "arg " + i + ", unrecognized type: " + c);
                        return createFailChunk(-2, "Unsupported parameter type (" + c + ") to invoke view method.");
                }
            }
        } else {
            argTypes = new Class[0];
            args = new Object[0];
        }
        try {
            try {
                ViewDebug.invokeViewMethod(targetView, targetView.getClass().getMethod(methodName, argTypes), args);
                return null;
            } catch (Exception e) {
                Log.e(TAG, "Exception while invoking method: " + e.getCause().getMessage());
                String msg = e.getCause().getMessage();
                if (msg == null) {
                    msg = e.getCause().toString();
                }
                return createFailChunk(-3, msg);
            }
        } catch (NoSuchMethodException e2) {
            Log.e(TAG, "No such method: " + e2.getMessage());
            return createFailChunk(-2, "No such method: " + e2.getMessage());
        }
    }

    private Chunk setLayoutParameter(View rootView, View targetView, ByteBuffer in) {
        String param = getString(in, in.getInt());
        try {
            ViewDebug.setLayoutParameter(targetView, param, in.getInt());
            return null;
        } catch (Exception e) {
            Log.e(TAG, "Exception setting layout parameter: " + e);
            return createFailChunk(-3, "Error accessing field " + param + ":" + e.getMessage());
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private org.apache.harmony.dalvik.ddmc.Chunk profileView(android.view.View r9, android.view.View r10) {
        /*
        r8 = this;
        r5 = 32768; // 0x8000 float:4.5918E-41 double:1.61895E-319;
        r0 = new java.io.ByteArrayOutputStream;
        r0.<init>(r5);
        r1 = new java.io.BufferedWriter;
        r4 = new java.io.OutputStreamWriter;
        r4.<init>(r0);
        r1.<init>(r4, r5);
        android.view.ViewDebug.profileViewAndChildren(r10, r1);	 Catch:{ IOException -> 0x0026 }
        r1.close();	 Catch:{ IOException -> 0x004e }
    L_0x0018:
        r2 = r0.toByteArray();
        r4 = new org.apache.harmony.dalvik.ddmc.Chunk;
        r5 = CHUNK_VUOP;
        r6 = 0;
        r7 = r2.length;
        r4.<init>(r5, r2, r6, r7);
    L_0x0025:
        return r4;
    L_0x0026:
        r3 = move-exception;
        r4 = 1;
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0049 }
        r5.<init>();	 Catch:{ all -> 0x0049 }
        r6 = "Unexpected error while profiling view: ";
        r5 = r5.append(r6);	 Catch:{ all -> 0x0049 }
        r6 = r3.getMessage();	 Catch:{ all -> 0x0049 }
        r5 = r5.append(r6);	 Catch:{ all -> 0x0049 }
        r5 = r5.toString();	 Catch:{ all -> 0x0049 }
        r4 = createFailChunk(r4, r5);	 Catch:{ all -> 0x0049 }
        r1.close();	 Catch:{ IOException -> 0x0047 }
        goto L_0x0025;
    L_0x0047:
        r5 = move-exception;
        goto L_0x0025;
    L_0x0049:
        r4 = move-exception;
        r1.close();	 Catch:{ IOException -> 0x0050 }
    L_0x004d:
        throw r4;
    L_0x004e:
        r4 = move-exception;
        goto L_0x0018;
    L_0x0050:
        r5 = move-exception;
        goto L_0x004d;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.ddm.DdmHandleViewDebug.profileView(android.view.View, android.view.View):org.apache.harmony.dalvik.ddmc.Chunk");
    }
}
